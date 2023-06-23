package com.eddylog.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.HashMap;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

/**
 * {
 *     "code": "400",
 *     "message": "잘못된 요청입니다.",
 *     "valistaion": {
 *          "title": "값을 입력해주세요"
 *     }
 * }
 */

//@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@Getter
public class ErrorResponse {

    private final String code;
    private final String message;
    private final Map<String, String> validation;

    @Builder
    public ErrorResponse(String code, String message, Map<String ,String> validation) {
        this.code = code;
        this.message = message;
        this.validation = validation;
    }

    public void addValidation(String fieldName, String errorMessage) {
        this.validation.put(fieldName, errorMessage);
    }
}
