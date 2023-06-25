package com.eddylog.api.repository;

import com.eddylog.api.domain.Session;
import com.eddylog.api.domain.User;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface SessionRepository extends CrudRepository<Session, Long> {

    Optional<Session> findByAccessToken(String accessToken);
}
