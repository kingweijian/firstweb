package com.unit;

import com.annotations.Requsetmapping;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public  class InitContext {
    public static String PAGEKETNAME = null;
    private static  boolean ISSTART;
    public static Logger logger = Logger.getLogger(InitContext.class);



    public static boolean isISSTART() {
        return ISSTART;
    }

    //    static {
//        ISSTART = init();
//        ISSTART = false;
//    }
    // 处理初始化
    public static boolean init(){
        PropertyConfigurator.configure ("C:\\log4j.properties");
        if(!ISSTART) {
            System.out.println("InitContext ========= ????????>>>>>>>>>>: 我被执行了！！！！！！！");
            logger.info("我被执行了");
            try {
                Map<String, Object> maps = getContent();
                if (maps == null || maps.isEmpty()) return false;
                Data_Mapping.setObject(maps);
                ISSTART = true;
                return ISSTART;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ISSTART = false;
        return ISSTART;
    }

    // {"test":object} 第一层
    // {"test":调用方法，"classonject":类名} 第二层

    /// 下面是新的

    /**
     * 第一层 注释
     * 第二层 类名
     * 第三层 方法名
     * 新思路 ：（类的注释读两次，现这样，后面再优化）
     *      第一步：先读出所有的类注释，存在一个map中
     *      第二步：读出类名，存在相应的注释map中
     *      第三步：读出所有方法注释，存在类的map中
     * @return
     * @throws IOException
     */
    private static Map<String, Object> getContent() throws IOException, ClassNotFoundException {
        if(PAGEKETNAME==null) return null;
        Obtain_Package_Class scanner = new Obtain_Package_Class();

        Set<String> set = scanner.findCandidateClasses(PAGEKETNAME);
        if(set.isEmpty())return null;

        Iterator<String> iterator = set.iterator();


        return getClass_obj(iterator);
    }

//    private static void zhix(Map map,String s){
//
//    }

   private static Map<String,Object> getClass_obj(Iterator iterator) throws ClassNotFoundException {
       Map<String,Object> commenmap = new HashMap<String, Object>();
       String className = null;
       Class<?> c= null;
       Requsetmapping requsetmapping = null;
       Map<String,Object> chile = null;
       while (iterator.hasNext())
       {
           // 以下是获取每一个类的注释
           className = (String) iterator.next();
           logger.info( "wadw == >> " + className);
           c = Class.forName(className);
           requsetmapping = isRequestMapping(c);
           String[] commen = requsetmapping == null ? null : getComment(requsetmapping);
           if(commen == null)continue;
           // 遍历注释，把注释存入map中
           for (String s:commen) {
               if(commenmap.get(s) != null){
                   chile = (Map<String, Object>) commenmap.get(s);
                   chile.put(className,Analysis_Class.get_method_annotation(c));
               }else{
                   chile = new HashMap<String,Object>();
                   chile.put(className,Analysis_Class.get_method_annotation(c));
                   commenmap.put(s,chile);
               }
               // 这里是判断注释是否存在，不存在的话就存入当前注释

               //

           }

       }

        return commenmap;
   }

   private static Requsetmapping isRequestMapping(Class<?>  cati) throws ClassNotFoundException {

       Requsetmapping requsetmapping = null;
       if(cati.isAnnotationPresent(Requsetmapping.class)){
           requsetmapping = cati.getAnnotation(Requsetmapping.class);
           return requsetmapping;
       }
        return null;
   }

   private static String[] getComment(Requsetmapping requsetmapping){

        return requsetmapping.value();
   }

}
