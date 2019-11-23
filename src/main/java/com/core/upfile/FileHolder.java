package com.core.upfile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**一个存放文件信息的类，包括文件的名称（ String ），
 **字段名（ String ）， Content-Type（String） 和内容（byte[]）
 **还提供了一个直接将文件内容保存到一个文件的函数 void saveTo(File f)
 **可以调用 类{@link ContentFactory}中的适当方法，生成该类的实例。
 ** @see        ContentFactory
 ** @see ContentFactory#getFileParameter
 ** @see ContentFactory#getFileParameterValues
 **/
public class FileHolder
{
    String contentType;
    byte[] buffer;
    String fileName;
    String parameterName;
    FileHolder(byte[] buffer,String contentType,String fileName,String parameterName)
    {
        this.buffer=buffer;
        this.contentType=contentType;
        this.fileName=fileName;
        this.parameterName=parameterName;
    }
    /**把文件的内容存到指定的文件中，
     **<b>这个方法不会检查这个文件是否可写、是否已经存在。</b>
     **@param file  目的文件
     **@throws 在 I/O 操作中被抛出的 IOException
     **/
    public void saveTo(File file) throws IOException
    {
        BufferedOutputStream out=new BufferedOutputStream(new FileOutputStream(file));
        out.write(buffer);
        out.close();
    }
    /**把文件的内容存到指定的文件中，
     **<b>这个方法不会检查这个文件是否可写、是否已经存在。</b>
     **@param name 目的文件名
     **@throws 在 I/O 操作中被抛出的 IOException
     **/
    public void saveTo(String name) throws IOException
    {
        saveTo(new File(name));
    }
    /**
     **返回一个文件内容的字节数组
     **@return 一个代表文件内容的字节数组
     **/
    public byte[] getBytes()
    {
        return buffer;
    }
    /**
     **返回该文件在文件上载前在客户端的名称
     **@return 该文件在文件上载前在客户端的名称
     **/
    public String getFileName()
    {
        return fileName;
    }

    /**
     **返回该文件的 Content-Type
     **@return 该文件的 Content-Type
     **/
    public String getContentType()
    {
        return contentType;
    }
    /**
     **返回上载该文件时，Html 页面窗体中 file 控件的 name 属性
     **@return 返回上载该文件时，Html 页面窗体中 file 控件的 name 属性
     **/
    public String getParameterName()
    {
        return parameterName;
    }
}