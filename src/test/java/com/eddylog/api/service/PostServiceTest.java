package com.eddylog.api.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.data.domain.Sort.Direction.*;

import com.eddylog.api.domain.Post;
import com.eddylog.api.repository.PostRepository;
import com.eddylog.api.request.PostCreate;
import com.eddylog.api.request.PostEdit;
import com.eddylog.api.request.PostSearch;
import com.eddylog.api.response.PostResponse;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean(){
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성")
    void test1(){
        //given
        PostCreate postCreate = PostCreate.builder()
            .title("제목입니다.")
            .content("내용입니다.")
            .build();

        //when
        postService.write(postCreate);

        //then
        assertEquals(1L,postRepository.count());
        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.", post.getContent());

    }

    @Test
    @DisplayName("글 1개 조회")
    void test2(){
        //given (게시글을 저장하고)
        Post requestPost = Post.builder()
            .title("foo")
            .content("bar")
            .build();
        postRepository.save(requestPost);

        //when (조회하는 서비스를 통해서 요청해서)
        PostResponse response = postService.get(requestPost.getId());

        //then (엔티티를 잘 가져오는지를 확인)
        assertNotNull(response);
        assertEquals("foo", response.getTitle());
        assertEquals("bar", response.getContent());
    }

    @Test
    @DisplayName("글 여러개 조회 -> 글 1페이지 조회")
    void test3(){
        //given (게시글을 저장하고)
        List<Post> requestPosts = IntStream.range(0,20)
                .mapToObj(i -> Post.builder()
                    .title("foo" + i)
                    .content("bar1 " + i)
                    .build())
                    .collect(Collectors.toList());

        postRepository.saveAll(requestPosts);

        // sql -> select, limit, offset

        PostSearch postSearch = PostSearch.builder()
            .page(1)
            .size(10)
            .build();


        //when (조회하는 서비스를 통해서 요청해서)
        List<PostResponse> posts = postService.getList(postSearch);

        //then (엔티티를 잘 가져오는지를 확인)
        assertEquals(10L, posts.size());
        assertEquals("foo19", posts.get(0).getTitle());

    }

    @Test
    @DisplayName("글 제목 수정")
    void test4(){
        //given
        Post post = Post.builder()
            .title("에디")
            .content("반포자이")
            .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
            .title("니모")
            .content("반포자이")
            .build();

        //when
        postService.edit(post.getId(), postEdit);

        //then
        Post changePost = postRepository.findById(post.getId())
            .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id=" + post.getId()));
        assertEquals("니모",changePost.getTitle());
        assertEquals("반포자이",changePost.getContent());

    }

    @Test
    @DisplayName("글 내용 수정")
    void test5(){
        //given
        Post post = Post.builder()
            .title("에디")
            .content("반포자이")
            .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
            .title("에디")
            .content("초가집")
            .build();

        //when
        postService.edit(post.getId(), postEdit);

        //then
        Post changePost = postRepository.findById(post.getId())
            .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id=" + post.getId()));
        assertEquals("초가집",changePost.getContent());

    }

}