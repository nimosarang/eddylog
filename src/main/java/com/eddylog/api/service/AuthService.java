package com.eddylog.api.service;

import com.eddylog.api.domain.Session;
import com.eddylog.api.domain.User;
import com.eddylog.api.exception.InvalidSigninInformation;
import com.eddylog.api.repository.UserRepository;
import com.eddylog.api.request.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    @Transactional
    public String signin(Login login){
        // DB에서 조회
        User user = userRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
            .orElseThrow(InvalidSigninInformation::new);
        Session session = user.addSession();

        return session.getAccessToken();
    }
}
