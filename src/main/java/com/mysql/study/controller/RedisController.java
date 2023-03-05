package com.mysql.study.controller;

import com.mysql.study.domain.redis.dto.UserProfile;
import com.mysql.study.domain.redis.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

@RequiredArgsConstructor
@RestController
public class RedisController {

    private final StringRedisTemplate redisTemplate;
    private final UserProfileService userProfileService;

    @GetMapping("/setFruit")
    public String setFruit(@RequestParam String name) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.set("fruit", name);
        return "saved";
    }

    @GetMapping("/getFruit")
    public String getFruit() {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        return ops.get("fruit");
    }

    HashMap<String, String> sessionMap = new HashMap<>();

    @GetMapping("/login")
    public String login(HttpSession session, @RequestParam String name) {
        // sessionMap.put(session.getId(), name);
        session.setAttribute("name", name); // 한 인스턴스만 관리된다
        return "login";
    }

    @GetMapping("/myName")
    public String myName(HttpSession session) {
        // sessionMap.get(session.getId());
        return (String)session.getAttribute("name");
    }

    @GetMapping("/users/{userId}/profile")
    public UserProfile getProfile(@PathVariable String userId) {
        return userProfileService.getProfile(userId);
    }


}
