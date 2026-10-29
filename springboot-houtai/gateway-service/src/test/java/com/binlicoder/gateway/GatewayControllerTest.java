package com.binlicoder.gateway;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class GatewayControllerTest {
    @Test void forwardsQueryTokenBodyAndUpstreamStatus() throws Exception {
        var server = com.sun.net.httpserver.HttpServer.create(new java.net.InetSocketAddress("localhost", 0), 0);
        server.createContext("/api/students", exchange -> {
            String result = exchange.getRequestMethod() + "|" + exchange.getRequestURI().getRawQuery()
                    + "|" + exchange.getRequestHeaders().getFirst("Authorization") + "|"
                    + new String(exchange.getRequestBody().readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
            byte[] payload = result.getBytes(java.nio.charset.StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "text/plain");
            exchange.sendResponseHeaders(409, payload.length);
            exchange.getResponseBody().write(payload);
            exchange.close();
        });
        server.start();
        try {
            var gateway = new GatewayController("http://localhost:" + server.getAddress().getPort(), "unused", "unused");
            var request = new org.springframework.mock.web.MockHttpServletRequest("POST", "/api/students");
            request.setQueryString("name=a%20b&page=2");
            request.addHeader("Authorization", "Bearer example");
            var response = gateway.proxy(request, "hello".getBytes(java.nio.charset.StandardCharsets.UTF_8));
            assertEquals(409, response.getStatusCode().value());
            assertEquals("POST|name=a%20b&page=2|Bearer example|hello", new String(response.getBody(), java.nio.charset.StandardCharsets.UTF_8));
        } finally { server.stop(0); }
    }
    @Test void routesRespectDomainBoundaries() {
        var gateway = new GatewayController("http://identity", "http://academic", "http://reporting");
        assertEquals("http://identity", gateway.target("/api/auth/login"));
        assertEquals("http://identity", gateway.target("/api/students/1"));
        assertEquals("http://academic", gateway.target("/api/course-selections/student/1"));
        assertEquals("http://academic", gateway.target("/api/classes"));
        assertEquals("http://reporting", gateway.target("/api/stats"));
        assertNull(gateway.target("/api/students-invalid"));
        assertNull(gateway.target("/api/stats/internal"));
    }
}
