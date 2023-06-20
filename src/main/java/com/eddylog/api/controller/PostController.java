package com.eddylog.api.controller;

import com.eddylog.api.domain.Post;
import com.eddylog.api.request.PostCreate;
import com.eddylog.api.response.PostResponse;
import com.eddylog.api.service.PostService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostCreate request) {
        postService.write(request);
    }

    // 조회 API
    // 지난 시간 = 단건 조회 API (1개의 글 Post를 가져오는 기능)
    // 이번 시간 = 여러개의 글을 조회 API (06.13) // /posts

    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable Long postId){
        return postService.get(postId);
    }

    @GetMapping("/posts")
    public List<PostResponse> getList(@RequestParam int page){
        return postService.getList(page);
    }



}
