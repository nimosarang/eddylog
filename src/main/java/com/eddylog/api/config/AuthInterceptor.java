package com.eddylog.api.config;

import com.eddylog.api.exception.Unauthorized;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) throws Exception {
        log.info(">> preHandle"); //controller이전에 수행된것

        String accessToken = request.getParameter("accessToken");
        if (accessToken != null && accessToken.equals("eddy")){
            return true;
        }

        throw  new Unauthorized();
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
        ModelAndView modelAndView) throws Exception {
        log.info(">> postHandle"); //컨트롤러가 텍스트까지 반환해준 상태
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
        Object handler, Exception ex) throws Exception {
        log.info(">> afterCompletion"); //뷰 반환까지 끝내고나서 컴플리션 시행
    }
}
