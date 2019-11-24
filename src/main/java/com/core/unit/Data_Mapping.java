package com.core.unit;


import org.apache.log4j.Logger;

import java.util.Map;


public  class Data_Mapping {
    public static Logger logger = Logger.getLogger(Data_Mapping.class);
    private static Map<String, Object> object = null;

    public static Map<String, Object> getObject() {
        return object;
    }

    public static void setObject(Map<String, Object> object) {
        if(InitContext.isISSTART())return;
        Data_Mapping.object = object;
    }

    public static String[] execution(String urlinfo,Map<String, Object> maps){
        logger.info("urlinfo == >> " +urlinfo + "\nmaps ===> " + maps);
//        System.out.println("执行："+maps);
        // 第一层 获取 类名
        Object[] names = getName(RemoveFirst(urlinfo).split("/"),maps,0);
//        System.out.println(Arrays.toString(names));
        // 先判断 urlinfo 是否合法，不合法执行下面的，合法执行第二层查找 1 *** 这里的合法是指第一层是否包含urlinfo

        // 判断第一层是否存在，不存在会返回null 2    // 先不合法。。。

        if(names == null )return null;
        //  去除第一层            将第一次出现的字符串替换成空 ↓
        String two = RemoveFirst(urlinfo.replaceFirst((String) names[1],""));
//        System.out.println(two);
        // 判断去除第一层截取之后是否为空，为空就代表没有后续了，直接返回
        if(two == "") return null;

         //不为空继续查找第二层对象
         // 获得第二层对象 3
        //{{OBJECT={/test=aaa}, CLASSNAME=com.handlers.Test_one}
        Map<String,Object> maps_two = (Map<String, Object>) names[0];

        // 查找第二层方法 4
//        Map<String,Object> methods = (Map<String, Object>) maps_two.get("OBJECT");
        Object[] names_two =getName(two.split("/"),maps_two,1);
//        System.out.println("names_two : "+Arrays.toString(names_two));
        if(names_two == null )return null;
        // 返回类名和方法名
        String[] ret = new String[2];
        ret[0] = (String) names_two[0];
        ret[1] = (String)names_two[1];
//        ret[1]="我的";
        return  ret;
    }
    private static Object[] getName(String[] names,Map<String,?> maps,int option){
        if(maps == null || maps.isEmpty()) return null;
        if(names == null || names.length <= 0 ) return null;
//        System.out.println(maps);
        Object[] ret = new Object[2];
        boolean istrue = false;
        String className = null,sumName = "";

        for (String s: names) {
            sumName += "/"+s;
//            System.out.println(sumName);
            // 查找类 -> obj = {com.myagent.service.instruction.Test_two={/aa=aa}, com.myagent.service.instruction.Test_one={/a=a}}
            // 查找方法 ->> com.myagent.service.instruction.Test_two={/aa=aa}

//            className = (String)obj;
            Map<String,?> twos = null;
            switch (option){
                case 0:{
                    Object obj = maps.get(sumName);
                    if(obj == null) continue;
                    twos = (Map<String, ?>) obj;
                    if (twos != null) {
                        System.out.println(className+","+ sumName);
                        ret[0] = obj;
                        ret[1] = sumName;
                        istrue = true;
                    }
                    break;
                }

                case 1:{
                    for (Map.Entry<String, ?> entry : maps.entrySet()) {
                            Map<String,String> methonds = (Map<String, String>) entry.getValue();
                        for (Map.Entry<String, ?> entrys : methonds.entrySet()) {
//                            System.out.println("Key = " + entrys.getKey() + ", Value = " + entrys.getValue() + " , istrue = " + sumName.equals(entrys.getValue()));
                            if(sumName.equals(entrys.getKey())){
                                ret[0] = entry.getKey();
                                ret[1] = entrys.getValue();
                                istrue = true;
                                break;
                            }
                        }
//                            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
                    }
                    break;
                }

            }
            if (istrue) break;

        }
        return !istrue ? null : ret;
    }

    private static String RemoveFirst(String s){
        if(s==null || s.equals("")) return s;

        return s.substring(1,s.length());
    }

}
