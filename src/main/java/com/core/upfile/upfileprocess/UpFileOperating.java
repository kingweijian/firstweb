package com.core.upfile.upfileprocess;

import com.core.upfile.FileInterface;
import com.unit.Unit;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;


public class UpFileOperating  implements FileInterface {
    private int maxsize = 0;
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
    public UpFileOperating(int maxsize){
        this.maxsize = maxsize;

    }
    public boolean move_uploaded(byte[] filecontent,String filename,String filepath) throws IOException {
        return move_uploaded_file(filecontent,filename,filepath);
    }

    private boolean move_uploaded_file(byte[] filecontent,String filename,String filepath) throws IOException {

        System.out.println(filename+","+(filecontent.length<maxsize) +","+ (filepath != null) +","+ limitSuffix(filename));
        if((filecontent.length<maxsize && filepath != null && limitSuffix(filename)) == false){ return false;}
        System.out.println(filecontent.length + " , " + maxsize);
        FileOutputStream fileOutputStream =  new FileOutputStream(filepath+filename);
        fileOutputStream.write(filecontent);
        fileOutputStream.close();
        return true;
    }

    private boolean limitSuffix(String filename){
        if(filename == null || filename.equals("")) return false;
        String[] s = Suffixlimit == null ? SUFFIXLIMIT : Suffixlimit;
        System.out.println(Arrays.toString(s));
        String suffix = filename.substring(filename.lastIndexOf(".")+1,filename.length());
        System.out.println(suffix);
        return  Unit.SearchArr(s,suffix);
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

    }
}