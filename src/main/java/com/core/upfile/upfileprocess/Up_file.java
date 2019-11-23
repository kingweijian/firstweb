package com.core.upfile.upfileprocess;

import javax.servlet.http.HttpServletRequest;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Up_file {
    public  Map<String, String> values = new HashMap<String, String>();        //存放name=value，其中value存放在另一个类中
    public  Map<String, Object> files= new HashMap();           //存放文件内容的。
    private HttpServletRequest httpServletRequest = null;
    public Up_file(HttpServletRequest httpServletRequest){
        this.httpServletRequest = httpServletRequest;
    }
    public  void test_my() throws IOException {
        String contentType = this.httpServletRequest.getContentType();
        String boundary = "--"+contentType.substring(contentType.indexOf("boundary=")+10);
        int blen = bytesLen(boundary);

        int contentlen = this.httpServletRequest.getContentLength();
        byte[] buffer = new byte[contentlen];

        int total = 0,i = 0;
        DataInputStream dataInputStream = new DataInputStream(this.httpServletRequest.getInputStream());
        while ((total<contentlen) && (i>=0)) {
            i  = dataInputStream.read(buffer,total,contentlen);  // 不能一次读完，根据缓存的大小来取得，最后一个值定义的过大没意义
            total += i;
//                      System.out.println(total);
        }
        dataInputStream.close();
//        System.out.println(new String(buffer,"UTF-8"));
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
    private void Parse(byte[] sourse, Map<String, String> values, Map<String, Object> files) throws IOException {
        String ton[] = {"name=\"","\"; filename=\"","\"\r\n","Content-Type: ","\r\n\r\n"};
//        System.out.println(new String(sourse,"UTF-8"));
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
//            System.out.println(name + "," + filename );
            if(filename=="") return;
            filecontent = subbytes(sourse,position[4]+bytesLen(ton[4]),sourse.length);


            files.put("name",name);
            files.put("filename",filename);
            files.put("filecontent",filecontent);
//            FileOutputStream fileOutputStream =  new FileOutputStream("F://" + filename);
//            fileOutputStream.write(filecontent);
//            fileOutputStream.close();
        }else{
//            System.out.println(position[0]+","+position[1]);
            String key = new String(subbytes(sourse,position[0]+bytesLen(ton[0]),position[2]));
            String val = new String(subbytes(sourse,position[4]+bytesLen(ton[4]),sourse.length),"UTF-8");
            values.put(key,val);
        }

//        for (Map.Entry<String, String> entry : values.entrySet()) {
//            System.out.println("Key = " + entry.getKey() +"Value = " +  entry.getValue());
//        }
//        for (Map.Entry<String, Object> entry : files.entrySet()) {
//            System.out.println("Key = " + entry.getKey() );
//        }

    }
    private  byte[] subbytes(byte[] source, int start, int end){
        int size = end-start;
        byte[] ret = new byte[size];
        System.arraycopy(source,start,ret,0,size);
        return ret;
    }
    private static int byteIndexof(byte[] source, byte[] search, int start){
        if(source.length == 0){
            return 0;
        }
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

    private  int bytesLen(String s)
    {
        return s.getBytes().length;
    }
}
