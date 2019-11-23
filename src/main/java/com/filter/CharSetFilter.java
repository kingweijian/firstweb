package com.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public class CharSetFilter implements Filter{
    private String charset;
    @Override
    public void destroy() {


    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest hsrq = (HttpServletRequest)request;
        HttpServletResponse hsrp = (HttpServletResponse)response;

        hsrq.setCharacterEncoding(charset);
        hsrp.setCharacterEncoding(charset);
        hsrp.setContentType("text/html;charset="+charset+"");
        System.out.println("I was intercepted: CharSetFilter");
        chain.doFilter(hsrq, hsrp);

    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {

        charset = arg0.getInitParameter("charter");

    }

}
