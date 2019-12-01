package com.core.upfile.upfileprocess;

import com.core.upfile.FileInterface;
import com.core.unit.Unit;
import org.apache.log4j.Logger;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;


public class UpFileOperating  implements FileInterface {
    public final static Logger logger = Logger.getLogger(UpFileOperating.class);
    private int maxsize = 0;
    // 文件上传路径，通过对象初始化时赋值
    private String upfilePath = null;
    public final static String[] SUFFIXLIMIT = new String[]{"jpg","jpeg","gif","png",
            "doc","docx","docm","dotx","dotm",
            "xls","xlsx","xlsm","xltm","xlsb","xlam",
            "ppt","pptx","pptm","ppsm","potx","potm","ppam",
            "wps","wpt","doc","dot","rtf",
            "dps","dpt","ppt","pot","pps",
            "et","ett","xls","xlt",
            "zip","rar","7z","csv","txt"};
    private String[] Suffixlimit = null;


    public UpFileOperating(){}

    public UpFileOperating(int maxsize,String[] suffixlimit){
        this.maxsize = maxsize;
        this.Suffixlimit = suffixlimit;

    }
    /**
    * @Description: 这个方法是初始化 上传的大小 后缀 上传路径这三个主要参数的
    * @Param: [maxsize, suffixlimit, upfilePath]
    * @return:
    * @Author: weijian
    * @Date: 2019/11/24
    */
    public UpFileOperating(int maxsize,String[] suffixlimit,String upfilePath){
        this.maxsize = maxsize;
        this.Suffixlimit = suffixlimit;
        this.upfilePath = upfilePath;
    }
    public UpFileOperating(int maxsize){
        this.maxsize = maxsize;
    }
    
    @Override
    public boolean move_uploaded(byte[] filecontent,String filename) throws IOException {
        return move_uploaded_file(filecontent,filename,upfilePath);
    }
    /**
    * @Description: 重载上传文件方法，可以指定自定义上传路径
    * @Param: [filecontent, filename, filepath]
    * @return: boolean
    * @Author: weijian
    * @Date: 2019/11/25
    */
    public boolean move_uploaded(byte[] filecontent,String filename,String filepath) throws IOException {
        return move_uploaded_file(filecontent,filename,filepath);
    }


    private boolean move_uploaded_file(byte[] filecontent,String filename,String filepath) throws IOException {
        boolean isSuffix = limitSuffix(filename);
        logger.info(filename+" -- 判断结果不能有一个false  --- 大小判断  - "+(filecontent.length<maxsize) +", 路径判断 - "+ (filepath != null) +", 后缀判断 - "+ isSuffix );
        filepath = checkPath(filepath);
        if((filecontent.length<maxsize && filepath != null && isSuffix) == false)
        {
            logger.info("大小 - " +maxsize + "， 文件实际大小 - " + filecontent.length + " ， 路径 - " + filepath + ", 后缀 - " + isSuffix);
            return false;
        }
        logger.info(filecontent.length + " , " + maxsize);
        FileOutputStream fileOutputStream =  new FileOutputStream(filepath+filename);
        fileOutputStream.write(filecontent);
        fileOutputStream.close();
        return true;
    }

    private boolean limitSuffix(String filename){
        if(filename == null || filename.equals("")) return false;
        String[] s = Suffixlimit == null ? SUFFIXLIMIT : Suffixlimit;
        logger.info(Arrays.toString(s));
        String suffix = filename.substring(filename.lastIndexOf(".")+1,filename.length());
        logger.info(suffix);
        return  Unit.SearchArr(s,suffix);
    }


    public  String checkPath(String path){
        if(path != null && !path.equals("")){

            return path.endsWith("/") ? path : path.concat("/");
        }
        return null;
    }

    @Override
    public void setMaxSize(int size) {
        this.maxsize = size;
    }

    @Override
    public void setSuffixlimit(String[] suffixlimit) {
        this.Suffixlimit = suffixlimit;
    }

    @Override
    public void setSavePath(String path) {
        this.upfilePath = path;
    }
}