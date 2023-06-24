package com.eddylog.api.controller;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.eddylog.api.domain.Post;
import com.eddylog.api.repository.PostRepository;
import com.eddylog.api.request.PostCreate;
import com.eddylog.api.request.PostEdit;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성 요청 시 title값은 필수이다. (title null 테스트)")
    void test2() throws Exception {
        //given
        PostCreate request = PostCreate.builder()
            .title("제목입니다.")
            .content("내용입니다.")
            .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/posts")
                .contentType(APPLICATION_JSON)
                .content(json))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("400"))
            .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
            .andExpect(jsonPath("$.validation.title").value("타이틀을 입력해주세요."))
            .andDo(print());
    }

    @Test
    @DisplayName("글 작성 요청 시 DB에 값이 저장된다.")
    void test3() throws Exception {
        //given
        PostCreate request = PostCreate.builder()
            .title("제목입니다.")
            .content("내용입니다.")
            .build();

        String json = objectMapper.writeValueAsString(request);

        //when
        mockMvc.perform(post("/posts")
                .header("authorization", "eddy")
                .contentType(APPLICATION_JSON)
                .content(json))
            .andExpect(status().isOk())
            .andDo(print());

        //then
        assertEquals(1L, postRepository.count());

        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.", post.getContent());
    }

    @Test
    @DisplayName("글 1개 조회")
    void test4() throws Exception {
        //given (글을 저장하고)
        Post post = Post.builder()
            .title("123456789012345")
            .content("bar")
            .build();
        postRepository.save(post);

        //expected (when & then) - 조회 API를 통해서 JSON 형태로 응답이 잘 내려오는지 확인
        mockMvc.perform(get("/posts/{postId}", post.getId())
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(post.getId()))
            .andExpect(jsonPath("$.title").value("1234567890"))
            .andExpect(jsonPath("$.content").value("bar"))
            .andDo(print());

    }

    @Test
    @DisplayName("페이지를 0으로 요청하면 첫 페이지를 가져온다.")
    void test5() throws Exception {
        //given (글을 저장하고)
        List<Post> requestPosts = IntStream.range(0, 20)
            .mapToObj(i -> Post.builder()
                .title("foo" + i)
                .content("bar" + i)
                .build())
            .collect(Collectors.toList());

        postRepository.saveAll(requestPosts);

        //expected (when & ttfrhen) - 조회 API를 통해서 JSON 형태로 응답이 잘 내려오는지 확인
        mockMvc.perform(get("/posts?page=0&size=10")
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()", is(10)))
            .andExpect(jsonPath("$[0].title").value("foo19"))
            .andExpect(jsonPath("$[0].content").value("bar19"))
            .andDo(print());
    }

    @Test
    @DisplayName("글 제목 수정")
    void test7() throws Exception {
        //given (글을 저장하고)
        Post post = Post.builder()
            .title("에디")
            .content("반포자이")
            .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
            .title("니모")
            .content("반포자이")
            .build();

        //expected (when & ttfrhen) - 조회 API를 통해서 JSON 형태로 응답이 잘 내려오는지 확인
        mockMvc.perform(patch("/posts/{postId}", post.getId()) // PATCH /posts/{postId}
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postEdit))
            )
            .andExpect(status().isOk())
            .andDo(print());
    }

    @Test
    @DisplayName("글 삭제")
    void test8() throws Exception {
        //given
        Post post = Post.builder()
            .title("에디")
            .content("니모사랑")
            .build();

        postRepository.save(post);

        // expected mockMvc로 수행을 하고 어떠한 결과 값 까지 기대를 한다!
        mockMvc.perform(delete("/posts/{postId}", post.getId())
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 게시글 조회")
    void test09() throws Exception {
        //expected
        mockMvc.perform(delete("/posts/{postId}",1L)
            .contentType(APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 게시글 수정")
    void test10() throws Exception {
        //given
        PostEdit postEdit = PostEdit.builder()
            .title("니모")
            .content("반포자이")
            .build();

        //expected (when & ttfrhen) - 조회 API를 통해서 JSON 형태로 응답이 잘 내려오는지 확인
        mockMvc.perform(patch("/posts/{postId}", 1L) // PATCH /posts/{postId}
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postEdit))
            )
            .andExpect(status().isNotFound())
            .andDo(print());

    }

    @Test
    @DisplayName("게시글 작성시 제목에 '바보'는 포함될 수 없다.")
    void test11() throws Exception {
        //given
        PostCreate request = PostCreate.builder()
            .title("나는 바보입니다.")
            .content("반포자이")
            .build();

        String json = objectMapper.writeValueAsString(request);

        //when
        mockMvc.perform(post("/posts")
                .contentType(APPLICATION_JSON)
                .content(json)
            )
            .andExpect(status().isBadRequest())
            .andDo(print());
    }

}