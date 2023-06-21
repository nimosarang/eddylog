package com.eddylog.api.repository;

import com.eddylog.api.domain.Post;
import com.eddylog.api.request.PostSearch;
import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);

}
