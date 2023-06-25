package com.eddylog.api.controller;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.eddylog.api.domain.Session;
import com.eddylog.api.domain.User;
import com.eddylog.api.repository.SessionRepository;
import com.eddylog.api.repository.UserRepository;
import com.eddylog.api.request.Login;
import com.eddylog.api.request.PostCreate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @BeforeEach
    void clean() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("로그인 성공")
    void test() throws Exception {
        //given
        userRepository.save(User.builder()
            .name("에디")
            .email("eddy@naver.com") // Scrypt, Bcrypt 암호화 알고리즘
            .password("1234")
            .build());

        Login login = Login.builder()
            .email("eddy@naver.com")
            .password("1234")
            .build();

        String json = objectMapper.writeValueAsString(login);

        //expected
        mockMvc.perform(post("/auth/login")
                .contentType(APPLICATION_JSON)
                .content(json))
            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.code").value("400"))
//            .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
//            .andExpect(jsonPath("$.validation.title").value("타이틀을 입력해주세요."))
            .andDo(print());
    }

    @Test
    @Transactional
    @DisplayName("로그인 성공 후 세션 1개 생성")
    void test1() throws Exception {
        //given
        // 1. 회원 가입 시킴
        User user = userRepository.save(User.builder()
            .name("에디")
            .email("eddy@naver.com") // Scrypt, Bcrypt 암호화 알고리즘
            .password("1234")
            .build());

        // 2. 가입된 정보로 로그인 ( 했을 때, 내부적으로 세션이 늘어남)
        Login login = Login.builder()
            .email("eddy@naver.com")
            .password("1234")
            .build();

        String json = objectMapper.writeValueAsString(login);

        //expected
        mockMvc.perform(post("/auth/login")
                .contentType(APPLICATION_JSON)
                .content(json))
            .andExpect(status().isOk())
            .andDo(print());

        //회원 가입 된 (= 로그인 된, 연관관계때문) 사용자의 아이디를 조회해와서 그 사용자의 세션 수가 1이 되었는지 확인
//        User loggedInUser = userRepository.findById(user.getId())
//            .orElseThrow(RuntimeException::new);
//        assertEquals(1L,loggedInUser.getSessions().size());
        //위 3개는 트랜잭션으로 처리되어서 필요없어짐 (로그인유저 보장되기때문, 썩 명쾌하진않음... 오염문제로)

        assertEquals(1L, user.getSessions().size());
    }

    @Test
    @Transactional
    @DisplayName("로그인 성공 후 세션 응답")
    void test2() throws Exception {
        //given
        // 1. 회원 가입 시킴
        User user = userRepository.save(User.builder()
            .name("에디")
            .email("eddy@naver.com") // Scrypt, Bcrypt 암호화 알고리즘
            .password("1234")
            .build());

        // 2. 가입된 정보로 로그인 ( 했을 때, 내부적으로 세션이 늘어남)
        Login login = Login.builder()
            .email("eddy@naver.com")
            .password("1234")
            .build();

        String json = objectMapper.writeValueAsString(login);

        //expected
        mockMvc.perform(post("/auth/login")
                .contentType(APPLICATION_JSON)
                .content(json))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accessToken", notNullValue()))
            .andDo(print());

        assertEquals(1L, user.getSessions().size());
    }

    @Test
    @DisplayName("로그인 후 권한이 필요한 페이지 접속한다 /foo")
    void test3() throws Exception {
        //given
        User user = User.builder()
            .name("에디")
            .email("eddy@naver.com")
            .password("1234")
            .build();
        Session session = user.addSession();
        userRepository.save(user);

        //expected
        mockMvc.perform(get("/foo")
                .header("Authorization", session.getAccessToken())
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print());
    }

    @Test
    @DisplayName("로그인 후 검증되지 않은 세션 값으로 권한이 필요한 페이지에 접속할 수 없다.")
    void test4() throws Exception {
        //given
        User user = User.builder()
            .name("에디")
            .email("eddy@naver.com")
            .password("1234")
            .build();
        Session session = user.addSession();
        userRepository.save(user);

        //expected
        mockMvc.perform(get("/foo")
                .header("Authorization", session.getAccessToken() + "-o")
                .contentType(APPLICATION_JSON))
            .andExpect(status().isUnauthorized())
            .andDo(print());
    }
}