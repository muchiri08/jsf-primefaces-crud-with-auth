/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.muchiri;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author developer
 */
public class AuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        
        
        if (((HttpServletRequest) request).getSession().getAttribute(
                "role") == null) {
            ((HttpServletResponse) response).sendRedirect(((HttpServletRequest)request).getContextPath() + "/Web Pages/index.xhtml");
        } else {
            chain.doFilter(request, response);
        }

    }

}
