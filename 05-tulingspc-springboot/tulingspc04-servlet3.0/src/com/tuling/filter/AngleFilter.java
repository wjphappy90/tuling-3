package com.tuling.filter;


import javax.servlet.*;
import java.io.IOException;

/**
 * Created by smlz on 2019/7/31.
 */
public class AngleFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("AngleFilter start");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("AngleFilter doFilter");
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {
        System.out.println("AngleFilter  destroy");
    }

}
