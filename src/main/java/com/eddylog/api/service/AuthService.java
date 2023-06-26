package com.eddylog.api.service;

import com.eddylog.api.crypto.PasswordEncoder;
import com.eddylog.api.domain.User;
import com.eddylog.api.exception.AlreadyExistEmailException;
import com.eddylog.api.exception.InvalidSigninInformation;
import com.eddylog.api.repository.UserRepository;
import com.eddylog.api.request.Login;
import com.eddylog.api.request.Signup;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long signin(Login login){ //로그인

        //사용자를 찾아
        User user = userRepository.findByEmail(login.getEmail())
            .orElseThrow(InvalidSigninInformation::new);
        //비밀번호 검증

        var matches = passwordEncoder.matches(login.getPassword(), user.getPassword());//평문 비번 vs 버무려진놈 비교

        if (!matches){
            throw new InvalidSigninInformation();
        }

        return user.getId();
    }

    public void signup(Signup signup) { // 회원가입
        Optional<User> userOptional = userRepository.findByEmail(signup.getEmail());
        if (userOptional.isPresent()) {
            throw new AlreadyExistEmailException();
        }


        String encryptedPassword = passwordEncoder.encrypt(signup.getPassword());

        var user = User.builder()
            .name(signup.getName())
            .email(signup.getEmail())
            .password(encryptedPassword)
            .build();

        userRepository.save(user);
    }
}
