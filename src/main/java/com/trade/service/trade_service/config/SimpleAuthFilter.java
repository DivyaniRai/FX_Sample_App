package com.trade.service.trade_service.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SimpleAuthFilter extends HttpFilter {

    private static final String HEADER = "X-API-KEY";
    private static final String VALID = "test-key";

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String path = req.getRequestURI();
        // allow swagger/openapi, h2-console, actuator and api-docs endpoints
        String[] allowPrefixes = new String[]{"/swagger", "/swagger-ui", "/swagger-ui.html", "/v3", "/v3/api-docs", "/api-docs", "/h2-console", "/h2", "/actuator"};
        for (String p : allowPrefixes) {
            if (path.startsWith(p)) {
                chain.doFilter(req, res);
                return;
            }
        }

        // basic logging to help debug swagger loading issues
        System.out.println("[SimpleAuthFilter] Incoming " + req.getMethod() + " " + path);
        String key = req.getHeader(HEADER);
        if (VALID.equals(key)) {
            chain.doFilter(req, res);
        } else {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.setContentType("application/json");
            res.getWriter().write("{\"message\": \"Missing or invalid API key\"}");
        }
    }
}
