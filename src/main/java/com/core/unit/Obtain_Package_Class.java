package com.core.unit;

import com.annotations.Requsetmapping;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

/**
 *
 */
public class Obtain_Package_Class {
    private static Requsetmapping requsetmapping = null;
    public static final String CLASS_SUFFIX = ".class";

    private static final Pattern INNER_PATTERN = Pattern.compile("\\$(\\d+).", Pattern.CASE_INSENSITIVE);

    public Obtain_Package_Class() {
        super();
    }
    // 获取 包中所有class
    public Set<String> findCandidateClasses(String packageName) throws IOException {
        // 查询 是否以 点结束 如果是点结束的话就去掉点
        if (packageName.endsWith(".")) {
            packageName = packageName.substring(0, packageName.length()-1);
        }

        Map<String, String> classMap = new HashMap<String, String>(32);
        String path = packageName.replace(".", "/");

        Enumeration<URL> urls = findAllClassPathResources(path);
        while (urls!=null && urls.hasMoreElements()) {
            URL url = urls.nextElement();

            String protocol = url.getProtocol();
            if ("file".equals(protocol)) {
                String file = URLDecoder.decode(url.getFile(), "UTF-8");
//                System.out.println(packageName);
                parseClassFile(new File(file), packageName, classMap);
            } else if ("jar".equals(protocol)) {
                parseJarFile(url, classMap);
            }
        }
//        System.out.println(classMap.values());
        return new HashSet(classMap.values());
    }
    // 遍历 packageName 中的所有文件 packageName 是一个路径
    protected void parseClassFile(File classfile, String packageName, Map<String, String> classMap){
        if (!classfile.exists()) {
            return;
        }
        // 第一次进来是对的目录，但是回调的时候 在加

        if(classfile.isDirectory()){
            File[] files = classfile.listFiles();
            for (File file : files) {
                parseClassFile(file, packageName+"."+file.getName(), classMap);
            }
        } else if(classfile.getName().endsWith(CLASS_SUFFIX)) {
            addToClassMap(packageName, classMap);
        }
    }

    protected void parseJarFile(URL url, Map<String, String> classMap) throws IOException {
        JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
        Enumeration<JarEntry> entries = jar.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            if (entry.isDirectory()) {
                continue;
            }
            String name = entry.getName();
            if(name.endsWith(CLASS_SUFFIX)){
                addToClassMap(name.replace("/", "."), classMap);
            }
        }
    }
    //  过滤文件方法
    private boolean addToClassMap(String name, Map<String, String> classMap){

        if(INNER_PATTERN.matcher(name).find()){ //过滤掉匿名内部类
//            System.out.println("anonymous inner class:"+name);
            return false;
        }
//        System.out.println("class:"+name);
        if(name.indexOf("$")>0){ //内部类
//            System.out.println("inner class:"+name);
        }
        if(!classMap.containsKey(name)){
            classMap.put(name, name.substring(0, name.length()-6)); //去掉.class
        }
        return true;
    }
    // 获取 完整路径
    protected Enumeration<URL> findAllClassPathResources(String path) throws IOException {
        if(path.startsWith("/")){
            path = path.substring(1);
        }
        Enumeration<URL> urls = getClassLoader().getResources(path);

        return urls;
    }

    public static ClassLoader getClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ex) {
            // Cannot access thread context ClassLoader - falling back to system class loader...
        }
        Class<?> cls = Obtain_Package_Class.class;
        if (cl == null) {
            // No thread context class loader -> use class loader of this class.
            cl = cls.getClassLoader();
        }
        return cl;
    }


    public static String[] isStringArr(Map maps){
        for (Object obj:maps.values()){
            if(obj instanceof String[]){
                return (String[]) obj;
            }else{
                isStringArr((Map)obj);
            }
        }
        return null;
    }
}
