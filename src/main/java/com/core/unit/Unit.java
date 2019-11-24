package com.core.unit;


import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Unit {
    static Logger logger = Logger.getLogger(Unit.class);
    public static void for_arr(String[] strs,String s){
        for (String str: strs ) {
            System.out.println(s + " " + str);
        }
    }
    public static boolean SearchArr(Object[] arr,Object key){
//        System.out.println( Arrays.binarySearch(arr,key));
//        return Arrays.binarySearch(arr,key) >= 0 ? true : false;
        return Arrays.asList(arr).contains(key);
    }
    public static int getRegexPosition(String regex,String targetstr){
        Matcher m = Pattern.compile(regex).matcher(targetstr);
        int ret = 0;
        while (m.find()) ret = m.end();
        return ret;
    }
    /**
     * 对字符串md5加密
     *
     * @param str
     * @return
     */
    public static String getMD5(String str) {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static <T> T isAir(T t){
        return t == null ? null : t;
    }

    public static void invokewWeb(String url,HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) throws IOException {
        PrintWriter printWriter = httpServletResponse.getWriter();
        Map<String,Object> content = Data_Mapping.getObject();
//            System.out.println("InitContext  = "+content);
//            System.out.println("return ==> :"+ Arrays.toString(Data_Mapping.execution(urlInfo.getServletPath(),content)));
//            System.out.println(urlInfo.getReuqsetstr() + " \n" + content.toString());
        String[] s = Data_Mapping.execution(url,content);

        if(s!=null){
            logger.info("不为空");
            try {
                //getMethod,getDeclaredMethod。看了下说明大概的意思就是getMethod只能调用public声明的方法，而getDeclaredMethod基本可以调用任何类型声明的方法
                Class<?> c = Class.forName(s[0]);
                Object obj = c.newInstance();

                Method m =  c.getMethod(s[1]);
                if(obj instanceof ServletContextinterface){
                    ((ServletContextinterface) obj).setRequest(httpServletRequest);
                    ((ServletContextinterface) obj).setResponse(httpServletResponse);
//                    System.out.println("我需要reuqest和response对象");
                }
                // 返回类型 str , json ,
                Object ret =  m.invoke(obj);
//                  System.out.println( "反射调用："+obj);
                printWriter.print(ret);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        logger.info("空的"  + s);
    }
}
