package com.eddylog.api.config;

import com.eddylog.api.config.data.UserSession;
import com.eddylog.api.exception.Unauthorized;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class AuthResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) { //정말 원하는 DTO인가에 대해
        return parameter.getParameterType().equals(UserSession.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory)
        throws Exception { // 실제 DTO 세팅
        String accessToken = webRequest.getParameter("accessToken");
        if (accessToken == null || accessToken.equals("")){
            throw new Unauthorized();
        }
        UserSession userSession = new UserSession();
        userSession.name = accessToken;
        return userSession;
    }
}
