package com.example.newsfeed.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.util.PatternMatchUtils;

import java.io.IOException;

public class UserFilter implements Filter {

    private static final String [] whiteList = {"/users/signup","/users/signin"};

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        // 요청 URI
        String requestURI = httpRequest.getRequestURI();

        // 로그인(인증)
        if(!isWhiteList(requestURI)){
            HttpSession session = httpRequest.getSession(false);

            if(session == null){
                throw new IllegalStateException("session is null");
            }
        }

        filterChain.doFilter(httpRequest,httpResponse);

    }

    private Boolean isWhiteList(String requestURI){
        return PatternMatchUtils.simpleMatch(whiteList,requestURI);
    }
}
