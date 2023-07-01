package com.eddylog.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.eddylog.api.domain.User;
import com.eddylog.api.exception.AlreadyExistEmailException;
import com.eddylog.api.repository.UserRepository;
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
        PasswordEncoder2 encoder = new PasswordEncoder2();
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
}