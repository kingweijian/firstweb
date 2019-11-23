package com.core;

import com.unit.Data_Mapping;
import com.unit.InitContext;
import com.unit.ServletContextinterface;
import com.unit.Unit;
import com.urlprocess.URLProcess;
import com.urlprocess.UrlInfo;
import org.apache.log4j.Logger;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

public class test implements Servlet {
//    private String pacckage = "";
//    private static boolean isstatr = false;
static Logger logger = Logger.getLogger(test.class);
    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
//        pacckage =  servletConfig.getInitParameter("pacakge");
        InitContext.PAGEKETNAME  = servletConfig.getInitParameter("pacakge");
        System.out.println(InitContext.PAGEKETNAME);
        InitContext.init();

    }

    @Override
    public ServletConfig getServletConfig() {
        return getServletConfig();
    }

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws IOException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        servletResponse.setCharacterEncoding("UTF-8");
        URLProcess urlProcess = new URLProcess(httpServletRequest);
        UrlInfo urlInfo = urlProcess.getUrlInfo_inttance();

        logger.info(" 需要解析URL ：  " + urlInfo.getReuqsetstr() + "  状态  == >>> " +  InitContext.isISSTART() );

//        System.out.println(httpServletRequest);
        if(InitContext.isISSTART()){
            logger.info("我正在解析");
            Unit.invokewWeb(urlInfo.getReuqsetstr(),httpServletRequest,(HttpServletResponse) servletResponse);
        }else{
            logger.info("初始化容器失败 ............ 尝试初始化容器 .... 包名" + InitContext.PAGEKETNAME +" ... 初始化结果 : " +InitContext.init());


        }

    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {

    }


}
