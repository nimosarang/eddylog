package com.eddylog.api.service;

import com.eddylog.api.domain.Post;
import com.eddylog.api.repository.PostRepository;
import com.eddylog.api.request.PostCreate;
import com.eddylog.api.response.PostResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public void write(PostCreate postCreate){
        Post post = Post.builder()
            .title(postCreate.getTitle())
            .content(postCreate.getContent())
            .build();

        postRepository.save(post);
    }

    public PostResponse get(Long id) {
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));

        return PostResponse.builder()
            .id(post.getId())
            .title(post.getTitle())
            .content(post.getContent())
            .build();
    }

    // 글 전체 조회하는 경우는 별로 없다
    // 글이 너무 많은 경우 -> 비용이 너무 많이 든다.
    // 글이 -> 100,000,000 -> DB 글 모두 조회하는 경우 -> DB가 뻗을 수 있다
    // DB -> 애플리케이션 서버로 전달하는 시간, 트래픽비용 등이 많이 발생할 수 있다.
    // 페이징 처리로 극복~~

    public List<PostResponse> getList(int page) {
        // web -> page 1 -> 0
        Pageable pageable = PageRequest.of(page, 5, Sort.by(Direction.DESC, "id"));

       return postRepository.findAll(pageable).stream()
           .map(PostResponse::new)
           .collect(Collectors.toList());
    }
}
