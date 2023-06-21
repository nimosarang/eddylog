package com.eddylog.api.repository;

import static com.eddylog.api.domain.QPost.*;

import com.eddylog.api.domain.Post;
import com.eddylog.api.domain.QPost;
import com.eddylog.api.request.PostSearch;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> getList(PostSearch postSearch) {
        return jpaQueryFactory.selectFrom(post)
            .limit(postSearch.getSize()) // limit : 페이지 사이즈
            .offset(postSearch.getOffset())
            .orderBy(post.id.desc())
            .fetch();
    }
}
