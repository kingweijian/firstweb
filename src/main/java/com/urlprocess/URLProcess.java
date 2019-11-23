package com.urlprocess;

import com.unit.Unit;

import javax.servlet.http.HttpServletRequest;
import javax.swing.plaf.nimbus.State;

public class URLProcess {
    private HttpServletRequest httpServletRequest = null;
    private UrlInfo urlInfo = null;
    private String[] address = new String[]{"127.0.0.1:8080","127.0.0.1",".*localhost:[0-9]*",".*[a-zA-Z].(com|cn)"};
    public URLProcess(HttpServletRequest httpServletRequest){
        this.httpServletRequest = httpServletRequest;
        processURL();
    }

    private void processURL(){
        urlInfo = new UrlInfo();
        urlInfo.setContextPath(this.httpServletRequest.getContextPath());
        urlInfo.setHost(this.httpServletRequest.getRemoteHost());
        urlInfo.setProt(this.httpServletRequest.getServerPort());
        urlInfo.setRequestURL(this.httpServletRequest.getRequestURL());
        urlInfo.setServletPath(this.httpServletRequest.getServletPath());
        urlInfo.setIp(this.httpServletRequest.getRemoteAddr());
//        urlInfo.setMyReuqsetstrstr(getMyReuqsetstrstr(urlInfo.getRequestURL().toString(),0));

    }

    public UrlInfo getUrlInfo_inttance(){
        return this.urlInfo;
    }

    public String getHost(){
        return urlInfo.getHost();
    }

    public String getContextPath(){
        return urlInfo.getContextPath();
    }

    public int getProt(){
        return urlInfo.getProt();
    }

    public String getRequestURL(){
        return urlInfo.getRequestURL().toString();
    }

    public String getReuqsetstr(){
        return urlInfo.getReuqsetstr();
    }

    public String getServletPath(){
        return urlInfo.getServletPath();
    }
    public String getIp(){
        return urlInfo.getIp();
    }

    public String getMyReuqsetstrstr(String target,int index){
        if(index >= address.length) return null;
        String i = address[index];
        int len = target.indexOf(i) == -1 ? 0 : target.indexOf(i) + i.length();
        int start = len == 0 ? Unit.getRegexPosition(i,target) : len;
//        System.out.println(index + " " + start + " " +len);
        String ret = target.substring(start,target.length());
        if(start ==0 && index < address.length) ret = getMyReuqsetstrstr(target,index+1);

        return ret;
    }



}
