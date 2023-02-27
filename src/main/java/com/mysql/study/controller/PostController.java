package com.mysql.study.controller;

import com.mysql.study.domain.post.dto.DailyPostCount;
import com.mysql.study.domain.post.dto.DailyPostCountRequest;
import com.mysql.study.domain.post.dto.PostCommand;
import com.mysql.study.domain.post.service.PostReadService;
import com.mysql.study.domain.post.service.PostWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostWriteService postWriteService;
    private final PostReadService postReadService;

    @PostMapping
    public Long create(PostCommand command) {
        return postWriteService.create(command);
    }

    @GetMapping("/daily-post-counts")
    public List<DailyPostCount> getDailyPostCount(
            @RequestParam Long memberId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate firstDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate lastDate) {
        var request = new DailyPostCountRequest(memberId, firstDate, lastDate);
        return postReadService.getDairyPostCount(request);
    }
}
