package com.eddylog.api.config;

import java.util.List;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class UserPrincipal extends User {

    private final Long userId;

    // role: 역할 -> 관리자, 사용자, 매니저 "ROLE_ADMIN"
    // authority: 권한 -> 글쓰기, 글 읽기, 사용자 정지 시키기 "ADMIN"

    public UserPrincipal(com.eddylog.api.domain.User user){
        super(user.getEmail(),user.getPassword(),
            List.of(new SimpleGrantedAuthority("ROLE_ADMIN")
            ));
        this.userId = user.getId();
    }

    public Long getUserId() {
        return userId;
    }
}
