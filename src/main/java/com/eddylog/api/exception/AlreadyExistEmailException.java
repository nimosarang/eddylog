package com.eddylog.api.exception;

public class AlreadyExistEmailException extends EddylogException {

    private static final String MESSAGE = "이미 가입된 이메일입니다.";

    public AlreadyExistEmailException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
