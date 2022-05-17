package edu.nus.iss.vttpfinalapplication.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;

@Component
public class AutheticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpServletResponse httpResp = (HttpServletResponse) response;

        HttpSession session = httpReq.getSession();
        String username = (String) session.getAttribute("username");
        // System.out.println(username);
        
        if(username == null) {
            httpResp.sendRedirect("/");
            return;
        }

        System.out.printf(">>>> url: %s\n", httpReq.getRequestURI().toString());
        System.out.printf(">>>>>> name: %s\n", session.getAttribute("username"));
        
        chain.doFilter(request, response);
        
    }
    
}
