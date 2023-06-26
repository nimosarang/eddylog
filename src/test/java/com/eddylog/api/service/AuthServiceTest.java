package com.eddylog.api.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.eddylog.api.crypto.PasswordEncoder;
import com.eddylog.api.domain.User;
import com.eddylog.api.exception.AlreadyExistEmailException;
import com.eddylog.api.exception.InvalidSigninInformation;
import com.eddylog.api.repository.UserRepository;
import com.eddylog.api.request.Login;
import com.eddylog.api.request.Signup;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class AuthServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    @AfterEach
    void clean() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 성공")
    void test1() {
        //given
        PasswordEncoder encoder = new PasswordEncoder();
        Signup signup = Signup.builder()
            .email("eddy@naver.com")
            .password("1234")
            .name("eddy")
            .build();

        //when
        authService.signup(signup);

        //then
        assertEquals(1, userRepository.count());

        User user = userRepository.findAll().iterator().next();

        assertEquals("eddy@naver.com", user.getEmail());
//        assertNotNull(user.getPassword());
//        assertNotNull("1234", user.getPassword());
        assertTrue(encoder.matches("1234",user.getPassword()));
        assertEquals("eddy", user.getName());
    }

    @Test
    @DisplayName("회원가입 시 중복된 이메일")
    void test2() {

        //given
        User existingUser = User.builder()
            .email("eddy@naver.com")
            .password("1234")
            .name("짱똘맨")
            .build();
        userRepository.save(existingUser);

        Signup signup = Signup.builder()
            .email("eddy@naver.com")
            .password("1234")
            .name("eddy")
            .build();

        //expected
//        authService.signup(signup);
        assertThrows(AlreadyExistEmailException.class, () -> authService.signup(signup));
    }

    @Test
    @DisplayName("로그인 성공")
    void test3() {
        //given
        PasswordEncoder encoder = new PasswordEncoder();
        String encryptedPassword =  encoder.encrypt("1234"); //암호화 해주기

        User existingUser = User.builder()
            .email("eddy@naver.com")
            .password(encryptedPassword) //암호화 해주기
            .name("짱똘맨")
            .build();
        userRepository.save(existingUser);

        Login login = Login.builder()
            .email("eddy@naver.com")
            .password("1234")
            .build();

        //when
        Long userId = authService.signin(login);

        //then
        assertNotNull(userId);
    }

    @Test
    @DisplayName("로그인시 비밀번호 틀림")
    void test4() {
        //given
        Signup signup = Signup.builder()
            .email("eddy@naver.com")
            .password("1234")
            .name("eddy")
            .build();
        authService.signup(signup);

        Login login = Login.builder()
            .email("eddy@naver.com")
            .password("5678")
            .build();

        //expected
        assertThrows(InvalidSigninInformation.class, () -> authService.signin(login));

    }
}