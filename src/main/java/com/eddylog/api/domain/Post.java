package com.eddylog.api.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String content;

    @Builder
    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

//    public void change(String title, String content){
//        this.title = title;
//        this.content = content;
//    }
    public  PostEditor.PostEditorBuilder toEditor() { //build 하지 않은 빌더 클래스 자체를 넘겨줌
        return PostEditor.builder()
            .title(title)
            .content(content); //build로 데이터 픽스 노노
    }

    public void edit(PostEditor postEditor) {
        //픽스된 postEditor가 넘어온다
        title = postEditor.getTitle();
        content = postEditor.getContent();
    }
}
