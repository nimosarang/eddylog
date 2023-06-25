package com.eddylog.api.controller;

import com.eddylog.api.config.data.UserSession;
import com.eddylog.api.domain.Post;
import com.eddylog.api.exception.InvalidRequest;
import com.eddylog.api.request.PostCreate;
import com.eddylog.api.request.PostEdit;
import com.eddylog.api.request.PostSearch;
import com.eddylog.api.response.PostResponse;
import com.eddylog.api.service.PostService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/foo")
    public String foo(UserSession userSession){
        log.info(">>>{}", userSession.name);
        return userSession.name;
    }

    @GetMapping("/bar")
    public String bar(){
        return "인증이 필요없는 페이지";
    }

    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostCreate request) {
        // 1. GET Parameter
        // 2. POST(body) value // 이 방법으로 인증 관련 데이터 받으면 PostCreate에 글 작성과 무관한 데이터가 들어가게 되니깐 설계가 무너진다
        // 3. Header
        request.validate();
    }

    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable Long postId) {
        return postService.get(postId);
    }

    @GetMapping("/posts")
    public List<PostResponse> getList(@ModelAttribute PostSearch postSearch) {
        return postService.getList(postSearch);
    }

    @PatchMapping("/posts/{postId}")
    public void edit(@PathVariable Long postId, @RequestBody @Valid PostEdit request) {
        postService.edit(postId, request);
    }

    @DeleteMapping("/posts/{postId}")
    public void delete(@PathVariable Long postId) {
        postService.delete(postId);

    }
}
