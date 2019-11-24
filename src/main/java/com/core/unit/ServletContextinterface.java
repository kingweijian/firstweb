package com.core.unit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 此接口是传递servlet的一些操作对象的，
 * 如果在handlers中必须使用一些servlet的对象，实现该接口。
 */
public interface ServletContextinterface {
    public void setRequest(HttpServletRequest httpServletRequest);
    public void setResponse(HttpServletResponse httpServletResponse);
}
