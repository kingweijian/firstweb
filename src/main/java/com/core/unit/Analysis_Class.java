package com.core.unit;

import com.annotations.Requsetmapping;
import org.apache.log4j.Logger;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 这个Class 是 获取 注释类中的各种方法的
 */
public class Analysis_Class {
   private static Requsetmapping requsetmapping = null;
   public static final Logger logger = Logger.getLogger(Analysis_Class.class);
   /*
   * 获得磊
   * */
    public static Map get_class_annotation(Class c){
        if(c == null ) return null;
        Map<String,String[]> returns = new HashMap<String,String[]>();
        Class<?>  cati = c;
        if(c.isAnnotationPresent(Requsetmapping.class)){
//            System.out.println("have @interface");
            requsetmapping = cati.getAnnotation(Requsetmapping.class);
            returns.put(c.getName(),requsetmapping.value());
//            String[] strs = requsetmapping.value();
//            Unit.for_arr(strs,"method:");
        }else{
            logger.info("no @interface");
        }
        return returns;
    }

    public static String[] get_class_annotation_one(Class c){
        if(c == null ) return null;
       String[] ret = null;
        Class<?>  cati = c;
        if(c.isAnnotationPresent(Requsetmapping.class)){
            requsetmapping = cati.getAnnotation(Requsetmapping.class);
            ret = requsetmapping.value();
//            String[] strs = requsetmapping.value();
//            Unit.for_arr(strs,"method:");
        }else{
            System.out.println("no @interface");
        }
        return ret;
    }

    /*
    * 获得这个类中的每个 公共的方法
    * params Class 需要获取方法的class 对象
    * return 返回 这个类所有方法
    * */

    public static Map get_method_annotation(Class c){
        if(c == null ) return null;
        Map<String, String> returns = new HashMap<String, String>();
        Class<?>  cati = c;
        String[] method_arr = null;
        Method[] methods = cati.getMethods();
        for (Method method: methods) {
            if(method.isAnnotationPresent(Requsetmapping.class)){
                requsetmapping = method.getAnnotation(Requsetmapping.class);
                method_arr = requsetmapping.value();
                for(String s : method_arr){
                    returns.put(s,method.getName());
                }

//                System.out.println(requsetmapping.value());
//                Unit.for_arr(requsetmapping.value(),"method:");
            }
        }
        return returns;
    }
}
