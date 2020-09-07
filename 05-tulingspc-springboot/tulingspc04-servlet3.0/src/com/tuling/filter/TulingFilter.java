package com.tuling.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
* @vlog: 高于生活，源于生活
* @desc: 类的描述 servlet3.0 新特性 注册Filter
* @author: smlz
* @createDate: 2019/7/31 16:45
* @version: 1.0
*/
@WebFilter(value = "/tulingHello")
public class TulingFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("TulingFilter start");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("TulingFilter doFilter");
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {
        System.out.println("TulingFilter  destroy");
    }
}
