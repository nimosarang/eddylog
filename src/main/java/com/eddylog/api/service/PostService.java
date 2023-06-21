package com.eddylog.api.service;

import com.eddylog.api.domain.Post;
import com.eddylog.api.domain.PostEditor;
import com.eddylog.api.repository.PostRepository;
import com.eddylog.api.request.PostCreate;
import com.eddylog.api.request.PostEdit;
import com.eddylog.api.request.PostSearch;
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
import org.springframework.transaction.annotation.Transactional;

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

    public List<PostResponse> getList(PostSearch postSearch) {
       return postRepository.getList(postSearch).stream()
           .map(PostResponse::new)
           .collect(Collectors.toList());
    }

    @Transactional
    public void edit(Long id, PostEdit postEdit){
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));

        PostEditor.PostEditorBuilder editorBuilder = post.toEditor();

        PostEditor postEditor = editorBuilder.title(postEdit.getTitle())
            .content(postEdit.getContent())
            .build();

        post.edit(postEditor);

    }
}
