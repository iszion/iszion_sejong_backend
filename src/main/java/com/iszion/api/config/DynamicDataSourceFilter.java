package com.iszion.api.config;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@WebFilter("/*")
public class DynamicDataSourceFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 필터 초기화 코드 (필요한 경우)
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            chain.doFilter(request, response);  // 요청을 처리
        } finally {
            DynamicDataSource.clear();  // 요청이 끝난 후 DB 설정 초기화
        }
    }

    @Override
    public void destroy() {
        // 필터 종료 시 처리할 코드 (필요한 경우)
    }
}
