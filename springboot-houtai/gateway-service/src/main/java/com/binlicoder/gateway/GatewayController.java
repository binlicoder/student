package com.binlicoder.gateway;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.net.http.*;
import java.time.Duration;
import java.util.*;

@RestController
public class GatewayController {
    private final HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(2)).build();
    private final String identity, academic, reporting;
    private static final Set<String> HOP = Set.of("connection", "content-length", "host", "transfer-encoding",
            "upgrade", "keep-alive", "proxy-authenticate", "proxy-authorization", "te", "trailer", "expect");
    public GatewayController(@Value("${services.identity-url}") String identity,
            @Value("${services.academic-url}") String academic, @Value("${services.reporting-url}") String reporting) {
        this.identity = identity; this.academic = academic; this.reporting = reporting;
    }
    String target(String path) {
        if (path.matches("^/api/(auth|students|teachers)(/.*)?$")) return identity;
        if (path.matches("^/api/(classes|courses|grades|course-selections)(/.*)?$")) return academic;
        return path.equals("/api/stats") ? reporting : null;
    }
    @RequestMapping("/api/**")
    public ResponseEntity<byte[]> proxy(HttpServletRequest request, @RequestBody(required = false) byte[] body) {
        String path = request.getRequestURI(), base = target(path);
        if (base == null) return ResponseEntity.notFound().build();
        var builder = HttpRequest.newBuilder(URI.create(base + path +
                (request.getQueryString() == null ? "" : "?" + request.getQueryString())))
                .timeout(Duration.ofSeconds(5)).method(request.getMethod(), body == null ?
                        HttpRequest.BodyPublishers.noBody() : HttpRequest.BodyPublishers.ofByteArray(body));
        var excluded = excluded(Collections.list(request.getHeaders("Connection")));
        Collections.list(request.getHeaderNames()).forEach(name -> {
            if (!excluded.contains(name.toLowerCase(Locale.ROOT)))
                Collections.list(request.getHeaders(name)).forEach(value -> builder.header(name, value));
        });
        try {
            var upstream = client.send(builder.build(), HttpResponse.BodyHandlers.ofByteArray());
            var response = ResponseEntity.status(upstream.statusCode());
            var responseExcluded = excluded(upstream.headers().allValues("connection"));
            upstream.headers().map().forEach((name, values) -> {
                if (!responseExcluded.contains(name.toLowerCase(Locale.ROOT))) response.header(name, values.toArray(String[]::new));
            });
            return response.body(upstream.body());
        } catch (java.io.IOException exception) { return ResponseEntity.status(502).build();
        } catch (InterruptedException exception) { Thread.currentThread().interrupt(); return ResponseEntity.status(503).build(); }
    }
    private Set<String> excluded(List<String> values) {
        var result = new HashSet<>(HOP);
        values.forEach(value -> { for (String token : value.split(",")) result.add(token.trim().toLowerCase(Locale.ROOT)); });
        return result;
    }
    @GetMapping("/health")
    public Map<String, String> health() { return Map.of("status", "UP"); }
}
