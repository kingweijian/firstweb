package com.urlprocess;

import com.unit.Unit;

import java.util.Objects;

public class UrlInfo {
    private int Prot = 0;
    private String Host = "";
    private String ContextPath = "";
    private String ServletPath = "";
    private StringBuffer RequestURL = null;
    private String ip = "";
    private String Reuqsetstr = "";
    private String MyReuqsetstrstr = "";
    private String urlMD5 = "";
    public UrlInfo(){}
    public UrlInfo(int prot, String host, String contextPath, String servletPath, StringBuffer requestURL, String ip) {
        this.Prot = prot;
        this.Host = host;
        this.ContextPath = contextPath;
        this.ServletPath = servletPath;
        this.RequestURL = requestURL;
        this.ip = ip;

        setRequeststr();
    }

    public String getReuqsetstr(){
        return this.Reuqsetstr;
    }


    public int getProt() {
        return Prot;
    }

    public void setProt(int prot) {
        Prot = prot;
    }

    public String getHost() {
        return Host;
    }

    public void setHost(String host) {
        Host = host;
    }

    public String getContextPath() {
        return ContextPath;
    }

    public void setContextPath(String contextPath) {
        ContextPath = contextPath;
    }

    public String getServletPath() {
        return ServletPath;
    }

    public void setServletPath(String servletPath) {
        ServletPath = servletPath;
    }

    public StringBuffer getRequestURL() {
        return RequestURL;
    }

    public void setRequestURL(StringBuffer requestURL) {
        RequestURL = requestURL;
        setRequeststr();
        this.urlMD5 = Unit.getMD5(requestURL.toString());
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }



    public String getMyReuqsetstrstr() {
        return MyReuqsetstrstr;
    }

    public String getUrlMD5() {
        return urlMD5;
    }

    public void setUrlMD5(String urlMD5) {
        this.urlMD5 = urlMD5;
    }

    @Override
    public String toString() {
        return "UrlInfo{" +
                "Prot=" + Prot +
                ", Host='" + Host + '\'' +
                ", ContextPath='" + ContextPath + '\'' +
                ", ServletPath='" + ServletPath + '\'' +
                ", RequestURL=" + RequestURL +
                ", ip='" + ip + '\'' +
                ", Reuqsetstr='" + Reuqsetstr + '\'' +
                ", MyReuqsetstrstr='" + MyReuqsetstrstr + '\'' +
                ", urlMD5='" + urlMD5 + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UrlInfo)) return false;
        UrlInfo urlInfo = (UrlInfo) o;
        return getProt() == urlInfo.getProt() &&
                Objects.equals(getHost(), urlInfo.getHost()) &&
                Objects.equals(getContextPath(), urlInfo.getContextPath()) &&
                Objects.equals(getServletPath(), urlInfo.getServletPath()) &&
                Objects.equals(getRequestURL(), urlInfo.getRequestURL()) &&
                Objects.equals(getIp(), urlInfo.getIp()) &&
                Objects.equals(getReuqsetstr(), urlInfo.getReuqsetstr()) &&
                Objects.equals(getMyReuqsetstrstr(), urlInfo.getMyReuqsetstrstr()) &&
                Objects.equals(getUrlMD5(), urlInfo.getUrlMD5());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProt(), getHost(), getContextPath(), getServletPath(), getRequestURL(), getIp(), getReuqsetstr(), getMyReuqsetstrstr(), getUrlMD5());
    }

    private String setRequeststr(){
        if(this.RequestURL == null)return  null;
        String requeststr = this.RequestURL.toString();
        requeststr = getChar_Designation_position(requeststr,"/",3);
        this.Reuqsetstr = requeststr;
        return  requeststr;
    }

    private String getChar_Designation_position(String target,String search,int i){
        if(i<=0) return null;

        if((target.equals("") || target == null) && (search.equals("") || search == null)) return null;

        for(int j = 1 ; j <= i;j++){
            target  = target.substring(target.indexOf(search)+1);
//            if(target.equals("")) return "/";
        }
//        System.out.println(target);
        //这里是去除最后一个斜杠的 不需要...
//        int last = target.lastIndexOf("/");
//        target = target.substring(0,last == -1 ? target.length() : last);
        return "/" + target;
    }

}
