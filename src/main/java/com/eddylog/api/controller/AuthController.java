package com.eddylog.api.controller;

import com.eddylog.api.config.AppConfig;
import com.eddylog.api.request.Login;
import com.eddylog.api.request.Signup;
import com.eddylog.api.response.SessionResponse;
import com.eddylog.api.service.AuthService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    // byte를 string으로 만들때는 base64 인코드 -> 인코드 된 string을 다시 byte -> base64 디코드
    private final AuthService authService;
    private final AppConfig appConfig;

    @PostMapping("/auth/login")
    public SessionResponse login(@RequestBody Login login) {
        Long userId = authService.signin(login); //로그인 성공 시

        //로그인 하는 경우, 똑같은 KEY를 가져와서 암호화 한다
        //키 같은 경우에는 보통 세션 같은 경우에는 같은 키를 쓰게 된다.
        //사용자별로 세션 암호화 할 때 마다 키가 달라지면 복호화를 할 수 없기 때문에, 보통 키를 서버에다 짱박아두고 암호화 복호화를 함 (하나의 키만 사용)

        //불러옴
        SecretKey key = Keys.hmacShaKeyFor(appConfig.getJwtKey());

        //jwt 암호화 진행
        String jws = Jwts.builder()
            .setSubject(String.valueOf(userId))
            .signWith(key)
            .setIssuedAt(new Date()) //발급일
            .compact();
        //JWT 발급

        return new SessionResponse(jws);
    }

    @PostMapping("/auth/signup")
    public void signup(@RequestBody Signup signup){
        authService.signup(signup);
    }

}

