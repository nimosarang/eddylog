package com.eddylog.api.exception;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
public abstract class eddylogException extends RuntimeException{

    public final Map<String, String> validation = new HashMap<>();

    public eddylogException(String message) {
        super(message);
    }

    public eddylogException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int getStatusCode();

    public void addValidation(String fieldName, String message){
        validation.put(fieldName, message);
    }

}
