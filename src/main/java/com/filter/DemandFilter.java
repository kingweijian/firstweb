package com.filter;


import com.core.upfile.upfileprocess.Up_file;
import com.urlprocess.UrlInfo;
import org.apache.log4j.Logger;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
/**
* @Description: 这个类主要是对请求处理
* @Param:
* @return:
* @Author: weijian
* @Date: 2019/12/1
*/
public class DemandFilter implements Filter {
    public static Logger logger = Logger.getLogger(DemandFilter.class);
    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        httpServletRequest.setCharacterEncoding("UTF-8");
        logger.info(" / ====================================  /");
        String contenttype =  httpServletRequest.getContentType();
        logger.info(contenttype);
        int type = ( contenttype == null) ? -1 : contenttype.indexOf("boundary=");

//        logger.info(type);
        if(type>-1){
            Up_file up_file = new Up_file(httpServletRequest);
            up_file.test_my();

            httpServletRequest.setAttribute("fileinfo", up_file.files);
        }

     //   logger.info("QueryString:" + httpServletRequest.getQueryString());
        httpServletRequest.setAttribute("urlinfo",getUrlInfo(httpServletRequest));
        httpServletRequest.setAttribute("params",getParameters(httpServletRequest)); // 获取get 中的参数

        logger.info(" / ====================================  /");
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
    private UrlInfo getUrlInfo(HttpServletRequest httpServletRequest){
        if(httpServletRequest == null) return null;
        return new UrlInfo(httpServletRequest.getServerPort() , httpServletRequest.getHeader("host") , httpServletRequest.getContextPath() , httpServletRequest.getServletPath() , httpServletRequest.getRequestURL(),httpServletRequest.getRemoteAddr());
    }
    private Map getParameters(HttpServletRequest httpServletRequest){
        if(httpServletRequest == null) return null;
        Map<String,String> params = new HashMap<String,String>();
        Enumeration<String> keys = httpServletRequest.getParameterNames();
        String name = "";
        while (keys.hasMoreElements()){
            name = keys.nextElement();
            params.put(name,httpServletRequest.getParameter(name));
        }

        return params;
    }
    @Override
    public void destroy() {
        logger.info(" --------------- DemandFilter destroy -----------------------");
    }
    public String get_form_data(InputStream inputStream) throws IOException {
        if(inputStream == null ) return null;
        StringBuffer sb  = new StringBuffer();
        byte[] data  = new byte[1024];
        int hasRead = 0;
        while (0 < (hasRead = inputStream.read(data))){
            sb.append(new String(data,0,hasRead,"UTF-8"));
        }
        inputStream.close();

        return sb.toString();
    }

    private Map<String,Object> get_form_data_map(String sb){
        String[] ton; String key= null,value = null;
        String data = sb.toString(),d = data.substring(0,data.indexOf("Content-Disposition"));
        String[] datas = data.split(d);
        for(int i = 0;i < datas.length;i++) {
           if(datas[i].indexOf("Content-Type") != -1){
                ton = datas[i].split(";");
                Map<String , Object> maps = new HashMap<String , Object>();
                for(int j = 1;j<ton.length;j++){
                    String[] kv = ton[j].split("=");
                    key = kv[0];
                    value = kv[1];
                    if(key.equals("name")) {
                        key = kv[1];
                        value = datas[i].split("\\n")[1];
                    }
                }
                maps.put(key,value);

           }
        }
        return null;
    }

}
