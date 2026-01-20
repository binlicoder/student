package com.binlicoder.houtai.controller;

import com.binlicoder.houtai.common.Result;
import com.binlicoder.houtai.entity.User;
import com.binlicoder.houtai.service.UserService;
import com.binlicoder.houtai.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        User user = userService.findByUsername(username);
        if (user == null) {
            return Result.error(401, "用户名或密码错误");
        }

        if (!password.equals(user.getPassword())) {
            return Result.error(401, "用户名或密码错误");
        }

        if (user.getStatus() == 0) {
            return Result.error(403, "账号已被禁用");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());

        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("user", user);

        return Result.success("登录成功", data);
    }

    @GetMapping("/userinfo")
    public Result<User> getUserInfo(HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");
        User user = userService.findById(userId);
        return Result.success(user);
    }

    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success("退出成功");
    }
}
