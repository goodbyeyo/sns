package com.mysql.study.controller;

import com.mysql.study.application.facade.CreatePostFacade;
import com.mysql.study.application.facade.GetTimelinePostFacade;
import com.mysql.study.domain.post.dto.DailyPostCount;
import com.mysql.study.domain.post.dto.DailyPostCountRequest;
import com.mysql.study.domain.post.dto.PostCommand;
import com.mysql.study.domain.post.dto.PostDto;
import com.mysql.study.domain.post.entity.Post;
import com.mysql.study.domain.post.service.PostReadService;
import com.mysql.study.domain.post.service.PostWriteService;
import com.mysql.study.util.CursorRequest;
import com.mysql.study.util.PageCursor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final GetTimelinePostFacade getTimelinePostFacade;
    private final CreatePostFacade createPostFacade;

    @PostMapping
    public Long create(PostCommand command) {
        return createPostFacade.execute(command);
        // return postWriteService.create(command);
    }

    @GetMapping("/daily-post-counts")
    public List<DailyPostCount> getDailyPostCount(
            @RequestParam Long memberId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate firstDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate lastDate) {
        var request = new DailyPostCountRequest(memberId, firstDate, lastDate);
        return postReadService.getDairyPostCount(request);
    }

    @PostMapping("/members/{memberId}")
    public Page<PostDto> getPosts(
            @PathVariable Long memberId,
            Pageable pageable
            // @RequestParam Integer page,
            // @RequestParam Integer size
    ) {
        return postReadService.getPosts(memberId, pageable);
        // return postReadService.getPosts(memberId, PageRequest.of(page, size));
    }

    @PostMapping("/members/{memberId}/by-cursor")
    public PageCursor<Post> getPostsByCursor(
            @PathVariable Long memberId,
            CursorRequest cursorRequest
    ) {
        return postReadService.getPosts(memberId, cursorRequest);
    }

    @GetMapping("/members/{memberId}/timeline")
    public PageCursor<Post> getTimeline(
            @PathVariable Long memberId,
            CursorRequest cursorRequest
    ) {
        return getTimelinePostFacade.executeByTimeline(memberId, cursorRequest);
    }

    @PostMapping("/{postId}/like/v1")
    public void likePostV1(@PathVariable Long postId) {
        postWriteService.likePostWithOptimisticLock(postId);
    }

    @PostMapping("/{postId}/like/v2")
    public void likePostV2(@PathVariable Long postId) {
        postWriteService.likePostWithOptimisticLock(postId);
    }
}
