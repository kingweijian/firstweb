package com.core.upfile;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

/**
 * UPfile，解析上传请求内容
 */
public class Upfile implements Servlet {
    private Map<String, String> values = new HashMap<String, String>();        //存放name=value，其中value存放在另一个类中
    private Map<String, Object> files= new HashMap();           //存放文件内容的。

    public void test_my(HttpServletRequest httpServletRequest) throws IOException {
        String contentType = httpServletRequest.getContentType();
        String boundary = "--"+contentType.substring(contentType.indexOf("boundary=")+10);
        int blen = get_Bytelen(boundary);

        int contentlen = httpServletRequest.getContentLength();
        byte[] buffer = new byte[contentlen];

        int total = 0,i = 0;
        DataInputStream dataInputStream = new DataInputStream(httpServletRequest.getInputStream());
        while ((total<contentlen) && (i>=0)) {
            i  = dataInputStream.read(buffer,total,contentlen);  // 不能一次读完，根据缓存的大小来取得，最后一个值定义的过大没意义
            total += i;
//  //          System.out.println(total);
        }
        dataInputStream.close();
//        OutputStream outputStream = new FileOutputStream("F:\\study\\Test\\src\\file_test.jpg");
//
//
//        outputStream .write(buffer);
//        outputStream.close();
        int start = byteIndexof(buffer,boundary.getBytes(),0);
        int end = 0;

        do{
            start += blen;
            end = byteIndexof(buffer,boundary.getBytes(),start);
//            System.out.println(start+","+end);
            if(end == -1)
                break;
            //  boundary \r\n DATA \r\n boundary \r\n DATA \r\n boundart ……
            Parse(subbytes(buffer,start,end),values,files);

            start = end;
        }while (true);

    }
        //   解析
    private void Parse(byte[] sourse,Map<String,String> values, Map<String,Object> files) throws IOException {
        String ton[] = {"name=\"","\"; filename=\"","\"\r\n","Content-Type: ","\r\n\r\n"};
        int position[] = new int[ton.length];
        for(int i = 0; i < ton.length;i++){

            position[i] = byteIndexof(sourse,ton[i].getBytes(),0);
        }
//        System.out.println(Arrays.toString(position));
         // 第一个值要 ton[i]
//        System.out.println(new String(subbytes(sourse,position[0]+bytesLen(ton[0]),position[2])));
        if(position[1] > 0 && position[1] < position[2]){
            String name="",filename="";
            byte[] filecontent = null;
            name = new String(subbytes(sourse,position[0]+bytesLen(ton[0]),position[1]));
            filename = new String(subbytes(sourse,position[1]+bytesLen(ton[1]),position[2]),"UTF-8");
            if(filename=="") return;
            filecontent = subbytes(sourse,position[4]+bytesLen(ton[4]),sourse.length);

//            System.out.println(name + "," + filename );

            files.put("name",name);
            files.put("filename",filename);
            files.put("filecontent",filecontent);
            FileOutputStream fileOutputStream =  new FileOutputStream("F://" + filename);
            fileOutputStream.write(filecontent);
            fileOutputStream.close();
        }else{
//            System.out.println(position[0]+","+position[1]);
            String key = new String(subbytes(sourse,position[0]+bytesLen(ton[0]),position[2]));
            String val = new String(subbytes(sourse,position[4]+bytesLen(ton[4]),sourse.length),"UTF-8");
            values.put(key,val);
        }

//        for (Map.Entry<String, String> entry : values.entrySet()) {
////            System.out.println("Key = " + entry.getKey() +"Value = " +  entry.getValue());
//        }
//        for (Map.Entry<String, Object> entry : files.entrySet()) {
////            System.out.println("Key = " + entry.getKey() );
//        }

    }
    private byte[] subbytes(byte[] source,int start,int end){
        int size = end-start;
        byte[] ret = new byte[size];
//        System.arraycopy(source,start,ret,0,size);
        return ret;
    }
    // 检索字节数组中是否包含某个数组
    private int byteIndexof(byte[] source, byte[] search,int start){
        if(source.length == 0){
            return 0;
        }
        //  源长度 - 搜索长度 = 需要查找的长度
        int max = source.length - search.length;

        if(max < 0){
            return -1;
        }
        if(search.length == 0){
            return 0;
        }
        if(start < 0 ){
            start = 0;
        }
        searchForFirst: for (int i = start; i < max; i++){
            if(source[i] == search[0]){
                int j = 1;
                while ( j<search.length ){
                    if(source[j+i] != search[j]){
                        continue searchForFirst;
                    }
                    j++;
                }
                return i;
            }
        }

        return -1;
    }
    @Override
    public void init(ServletConfig servletConfig) throws ServletException {

    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        Map<String,Object> fileinfo =(Map<String, Object>) httpServletRequest.getAttribute("fileinfo");

        byte[] filecontent = (byte[]) fileinfo.get("filecontent");
        String filename =  (String) fileinfo.get("filename");
        FileOutputStream fileOutputStream =  new FileOutputStream("F://" + filename);
        fileOutputStream.write(filecontent);
        fileOutputStream.close();

//        test_my(httpServletRequest);
    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {

    }


    public void test(HttpServletRequest httpServletRequest) throws IOException {
        String contentType=httpServletRequest.getContentType();
        int start=contentType.indexOf("boundary=");
        int boundaryLen=new String("boundary=").length(),blength = 0;
        int contentLength = httpServletRequest.getContentLength();

        String boundary=contentType.substring(start+boundaryLen);
        boundary="--"+boundary;
        blength = get_Bytelen(boundary);
//        System.out.println(boundary);
        byte buffer[] = new byte[contentLength];
        int once = 0;
        int total = 0;
        DataInputStream dataInputStream = new DataInputStream(httpServletRequest.getInputStream());
        while ((total<contentLength) && (once>=0)) {
            once = dataInputStream.read(buffer,total,contentLength);
            total += once;
        }

        //对buffer中的数据进行拆分
        int pos1=0;                  //pos1 记录 在buffer 中下一个 boundary 的位置
        //pos0,pos1 用于 subBytes 的两个参数
        int   pos0=byteIndexOf(buffer,boundary,0);   //pos0 记录 boundary 的第一个字节在buffer 中的位置
        do{
            pos0+=boundaryLen;                                 //记录boundary后面第一个字节的下标
            pos1=byteIndexOf(buffer,boundary,pos0);
            if (pos1==-1) {
                break;
            }
            pos0+=2;          //考虑到boundary后面的 \r\n

           // parse(subBytes(buffer,pos0,pos1-2),values,files);      //考虑到boundary后面的 \r\n
            pos0=pos1;
        }while(true);
//        System.out.println(total+","+once);
//        Enumeration s = values.keys(),ss = files.keys();
////        System.out.println(s.nextElement().toString());
//        s.hasMoreElements();
////        System.out.println(s.nextElement().toString());


    }
    private static int get_Bytelen(String s){
        return s.getBytes().length;
    }
    /**字节数组中的 indexof 函数，与 String 类中的 indexOf类似
     **@para source 源字节数组
     **@para search 目标字符串
     **@para start 搜索的起始点
     **@return 如果找到，返回search的第一个字节在buffer中的下标，没有则返回-1
     **/
    private static int byteIndexOf (byte[] source,String search,int start)
    {
        return byteIndexOf(source,search.getBytes(),start);
    }
    /**字节数组中的 indexof 函数，与 String 类中的 indexOf类似
     **@para source 源字节数组
     **@para search 目标字节数组
     **@para start 搜索的起始点
     **@return 如果找到，返回search的第一个字节在buffer中的下标，没有则返回-1
     **/
    private static int byteIndexOf (byte[] source,byte[] search,int start)
    {
        int i;
        if (search.length==0) return 0;

        int max=source.length-search.length;
        if (max<0)
            return -1;
        if (start>max)
            return -1;
        if (start<0)
            start=0;
        // 在source中找到search的第一个元素
        searchForFirst:
        for (i=start;i<=max ; i++)
        {
            if (source[i]==search[0])
            {
                //找到了search中的第一个元素后，比较剩余的部分是否相等
                int k=1;
                while(k<search.length)
                {
                    if (source[k+i]!=search[k])
                    {
                        continue searchForFirst;
                    }
                    k++;
                }
                return i;
            }
        }
        return -1;
    }
    /**
     **用于从一个字节数组中提取一个字节数组
     **类似于 String 类的substring()
     **/
    private static byte[] subBytes(byte[] source,int from,int end)
    {
        byte[] result=new byte[end-from];
//        System.arraycopy(source,from,result,0,end-from);
        return result;
    }
    /**
     **用于从一个字节数组中提取一个字符串
     **类似于 String 类的substring()
     **/
    private static String subBytesString(byte[] source,int from,int end)
    {
        return new String(subBytes(source,from,end));
    }
    /**
     **返回字符串S转换为字节数组后的长度
     **/
    private static int bytesLen(String s)
    {
        return s.getBytes().length;
    }
    private static void parse(byte[] buffer,Hashtable values,Hashtable files)
    {
            /* this is a smiple to parse
            [boundary]
            Content-Disposition: form-data; name="file3"; filename="C:\Autoexec.bat"
            Content-Type: application/octet-stream

            @echo off
            prompt $d $t [ $p ]$_$$

            [boundary]
            Content-Disposition: form-data; name="Submit"

            Submit
            [boundary]
            */
        String[] tokens={"name=\"","\"; filename=\"", "\"\r\n","Content-Type: ","\r\n\r\n"};
        //                          0           1                               2          3                         4
        int[] position=new int[tokens.length];

        for (int i=0;i<tokens.length ;i++ )
        {
            position[i]=byteIndexOf(buffer,tokens[i],0);
        }
        if (position[1]>0 && position[1]<position[2])
        {
            //包含tokens 中的第二个元素，说明是个文件数据段
            //1.得到字段名
            String name =subBytesString(buffer,position[0]+bytesLen(tokens[0]),position[1]);
            //2.得到文件名
            String file= subBytesString(buffer,position[1]+bytesLen(tokens[1]),position[2]);
            if (file.equals("")) return;
            file=new File(file).getName();     //this is the way to get the name from a path string
            //3.得到 Content-Type
            String contentType=subBytesString(buffer,position[3]+bytesLen(tokens[3]),position[4]);
            //4.得到文件内容
            byte[] b=subBytes(buffer,position[4]+bytesLen(tokens[4]),buffer.length);
            FileHolder f=new FileHolder(b,contentType,file,name);
            Vector v=(Vector)files.get(name);
            if (v==null)
            {
                v=new Vector();
            }
            if (!v.contains(f))
            {
                v.add(f);
            }
            files.put(name,v);
            //同时将 name 属性和 file 属性作为普通字段，存入values;
            v=(Vector)values.get(name);
            if (v==null)
            {
                v=new Vector();
            }
            if (!v.contains(file))
            {
                v.add(file);
            }
            values.put(name,v);
        }else
        {
//            String[] tokens={"name=\"","\"; filename=\"", "\"\r\n","Content-Type: ","\r\n\r\n"}
//             index                      0           1                               2          3                         4
            //不包含tokens 中的第二个元素，说明是个 name/value 型的数据段
            //所以没有tokens[1]和 tokens[3]
            //name 在 tokens[0] 和 tokens[2] 之间
            //value 在 tokens[4]之后
            //1.得到name
            String name =subBytesString(buffer,position[0]+bytesLen(tokens[0]),position[2]);
            String value= subBytesString(buffer,position[4]+bytesLen(tokens[4]),buffer.length);
            Vector v=(Vector)values.get(name);
            if (v==null)
            {
                v=new Vector();
            }
            if (!v.contains(value))
            {
                v.add(value);
            }
            values.put(name,v);
        }
    }
}
