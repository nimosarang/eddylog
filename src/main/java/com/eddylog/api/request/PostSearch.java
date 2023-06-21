package com.eddylog.api.request;

import static java.lang.Math.*;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PostSearch {

    private static final int MAX_SIZE = 2000;

    @Builder.Default
    private Integer page = 1;

    @Builder.Default
    private Integer size = 10; //한 페이지에 몇개를 출력할 것 인지

    public long getOffset(){
        return (long) (max(1, page)-1) * min(size,MAX_SIZE); //1과 page중에 가장 작은 것을 가져와
    }

}
