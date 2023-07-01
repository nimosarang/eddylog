package com.eddylog.api.service;

import com.eddylog.api.domain.User;
import com.eddylog.api.exception.AlreadyExistEmailException;
import com.eddylog.api.repository.UserRepository;
import com.eddylog.api.request.Signup;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signup(Signup signup) { // 회원가입
        Optional<User> userOptional = userRepository.findByEmail(signup.getEmail());
        if (userOptional.isPresent()) {
            throw new AlreadyExistEmailException();
        }


        String encryptedPassword = passwordEncoder.encode(signup.getPassword());

        var user = User.builder()
            .name(signup.getName())
            .email(signup.getEmail())
            .password(encryptedPassword)
            .build();

        userRepository.save(user);
    }
}
