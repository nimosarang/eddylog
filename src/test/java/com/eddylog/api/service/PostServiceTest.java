package com.eddylog.api.service;

import static org.junit.jupiter.api.Assertions.*;

import com.eddylog.api.domain.Post;
import com.eddylog.api.repository.PostRepository;
import com.eddylog.api.request.PostCreate;
import com.eddylog.api.response.PostResponse;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
        List<Post> requestPosts = IntStream.range(1,31)
                .mapToObj(i -> Post.builder()
                    .title("에디 제목 " + i)
                    .content("반포자이 " + i)
                    .build())
                    .collect(Collectors.toList());

        postRepository.saveAll(requestPosts);

        // sql -> select, limit, offset

        //when (조회하는 서비스를 통해서 요청해서)
        List<PostResponse> posts = postService.getList(0);

        //then (엔티티를 잘 가져오는지를 확인)
        assertEquals(5L, posts.size());
        assertEquals("에디 제목 30", posts.get(0).getTitle());
        assertEquals("에디 제목 26", posts.get(4).getTitle());

    }

}