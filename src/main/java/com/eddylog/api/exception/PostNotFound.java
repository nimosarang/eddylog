package com.eddylog.api.exception;

/** 정책
 * status -> 404
 */

public class PostNotFound extends eddylogException {

    private static final String MESSAGE = "존재하지 않는 글입니다.";

    public PostNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
