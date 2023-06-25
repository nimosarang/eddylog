package com.eddylog.api.controller;

import com.eddylog.api.config.data.UserSession;
import com.eddylog.api.request.PostCreate;
import com.eddylog.api.request.PostEdit;
import com.eddylog.api.request.PostSearch;
import com.eddylog.api.response.PostResponse;
import com.eddylog.api.service.PostService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/foo")
    public Long foo(UserSession userSession){
        log.info(">>>{}", userSession.id);
        return userSession.id;
    }

    @GetMapping("/bar")
    public String bar(){
        return "인증이 필요없는 페이지";
    }

//    @PostMapping("/posts")
//    public void post(@RequestBody @Valid PostCreate request,UserSession userSession) {
//        //UserSession userSession 받아서 로그인(인가)된 사용자인지 검등도 하고
//        request.validate();
//        postService.write(request, userSession.id); //실제 글 작성한 사용자의 아이디를 넘겨준다
//    }

    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostCreate request) {
        request.validate();
        postService.write(request);
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
