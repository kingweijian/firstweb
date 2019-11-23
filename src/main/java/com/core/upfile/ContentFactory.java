package com.core.upfile;

import javax.servlet.http.HttpServletRequest;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**存放报文内容的类，提供类似于 ServletRequest 中的部分 get 方法
 *你必须在 html 页面的窗体(form)中指定 enctype="multipart/form-data"。
 *才可以正确的使用这个类。
 **@author kammi(coid@sina.com)
 **<a href=mailto:coid@sina.com>Mail to me </a><br>
 **<a href=http://javabubble.topcool.net>my home</a><br><hr>
 **/
public class ContentFactory
{
    //成员变量
    private Hashtable values;        //存放name=value，其中value存放在另一个类中
    private Hashtable files;           //存放文件内容的。

    private ContentFactory(Hashtable values,Hashtable files)
    {
        this.values=values;
        this.files=files;
    }
    /**
     * Returns the value of a request parameter as a <code>String</code>,
     * or <code>null</code> if the parameter does not exist.
     * parameters are contained in the posted form data.
     *
     *<p>
     * <b>This method can only be used with a from encoding by multipart/form-data
     *To deal with an normal encoding form, you may use the same method declared
     *in interface ServletRequest. </b>
     *
     * <p>You should only use this method when you are sure the
     * parameter has only one value. If the parameter might have
     * more than one value, use {@link #getParameterValues}.
     *
     * <p>If you use this method with a multivalued
     * parameter, the value returned is equal to the first value
     * in the array returned by <code>getParameterValues</code>.
     *
     * @param name 	a <code>String</code> specifying the
     *			name of the parameter
     *
     * @return		a <code>String</code> representing the
     *			single value of the parameter,
     *if the input type is file, then the filename will returned.
     * @see 		#getParameterValues
     *
     */
    public String getParameter(String name)
    {
        Vector v=(Vector)values.get(name);
        if(v!=null)
            return (String)v.elementAt(0);
        return null;
    }
    /**
     *
     * Returns an <code>Enumeration</code> of <code>String</code>
     * objects containing the names of the parameters contained
     * in this request. If the request has
     * no parameters, the method returns an
     * empty <code>Enumeration</code>.
     *
     *<p>
     * <b>This method can only be used with a from encoding by multipart/form-data
     * To deal with an normal encoding form, you may use the same method declared
     *in interface ServletRequest. </b>
     *
     * @return		an <code>Enumeration</code> of <code>String</code>
     *			objects, each <code>String</code> containing
     * 			the name of a request parameter; or an
     *			empty <code>Enumeration</code> if the
     *			request has no parameters
     *
     */
    public Enumeration getParameterNames()
    {
        return values.keys();
    }
    /**
     * Returns an array of <code>String</code> objects containing
     * all of the values the given request parameter has, or
     * <code>null</code> if the parameter does not exist.
     *
     *<p>
     * <b>This method can only be used with a from encoding by multipart/form-data</b>
     * <b>To deal with an normal encoding form, you may use the same method declared
     *in interface ServletRequest. </b>
     *
     * <p>If the parameter has a single value, the array has a length
     * of 1.
     *
     * @param name	a <code>String</code> containing the name of
     *			the parameter whose value is requested
     *
     * @return		an array of <code>String</code> objects
     *			containing the parameter's values
     *
     * @see		#getParameter
     *
     */
    public String[] getParameterValues(String name)
    {
        Vector v=(Vector)values.get(name);
        if(v!=null)
        {
            String[] result=new String[v.size()];
            v.toArray(result);
            return result;
        }

        return new String[0];
    }

    /**
     *返回一个 FileHolder 实例，该实例包含了通过字段名为 name 的 file 控件上载的文件信息，
     *如果不存在这个字段或者提交页面时，没有选择上载的文件，则返回 null。
     * <p>如果 Html 页面中存在不止一个字段名为 name 的 file 控件，
     * 返回值等于  {@link #getFileParameterValues} 中的第一个元素。
     *
     * @param name 	一个 <code>String</code> ，对应于 Html 页面窗体中 file 控件
     *的name 属性。
     *
     * @return		返回一个 FileHolder 实例，该实例包含了通过字段名为 name 的 file 控件上载的文件信息，
     *如果不存在这个字段或者提交页面是，没有选择上载的文件，则返回 null。
     *
     * @see 		#getFileParameterValues
     *
     */
    public FileHolder getFileParameter(String name)
    {
        Vector v=(Vector)files.get(name);
        if(v!=null)
            return (FileHolder)v.elementAt(0);
        return null;
    }
    /**
     * 返回一个 由 String 对象构成的 Enumeration ，包含了 Html 页面
     *窗体中所有 file 控件的 name 属性。
     *如果窗体中没有 file 控件，则返回一个空的 Enumeration
     * @return		     返回一个 由 String 对象构成的 Enumeration ，包含了 Html 页面
     *窗体中所有 file 控件的 name 属性。
     *如果窗体中没有 file 控件，则返回一个空的 Enumeration
     */
    public Enumeration getFileParameterNames()
    {
        return files.keys();
    }
    /**
     *返回一个 FileHolder 数组，该数组包含了所有通过字段名为 name 的 file 控件上载的文件信息，
     *如果不存在这个字段或者提交页面时，没有选择任何上载的文件，则返回一个 零元素的数组（不是 null )。
     * @param name 	一个 <code>String</code> ，对应于 Html 页面窗体中 file 控件
     *的name 属性。
     *
     * @return		返回一个 FileHolder 数组，该数组包含了所有通过字段名为 name 的 file 控件上载的文件信息，
     *如果不存在这个字段或者提交页面时，没有选择任何上载的文件，则返回一个 零元素的数组（不是 null )。
     *
     * @see 		#getFileParameter
     */
    public FileHolder[] getFileParameterValues(String name)
    {
        Vector v=(Vector)files.get(name);
        if(v!=null)
        {
            FileHolder[] result=new FileHolder[v.size()];
            v.toArray(result);
            return result;
        }
        return new FileHolder[0];
    }
    //------------->Factory 部分
    /**
     **返回根据当前请求生成的一个 ContentFactory 实例
     **@param request 提交的请求
     **@return 返回根据当前请求生成的一个 ContentFactory 实例，如果 request
    数据包的内容不是以 mutilpart/form-data 型编码，则返回 null。
     ** ContentFactoryException 当提交的数据和文件太大时抛出，
     **根据 Content-Length 判断，默认的许可值为 1024* 1024。
     **/
    public static ContentFactory getContentFactory(HttpServletRequest request) throws IOException
    {
        // default maxLength is 1MB.
        return getContentFactory(request,1024*1024);
    }
    /**
     **返回根据当前请求生成的一个 ContentFactory 实例
     **@param request 提交的请求
     **@param maxLength 数据包的最大长度，默认为1024*1024
     **@return 返回根据当前请求生成的一个 ContentFactory 实例，如果 request
    数据包的内容不是以 mutilpart/form-data 型编码，则返回 null。
     ** ContentFactoryException 当提交的数据和文件太大时抛出，
     **根据 Content-Length 判断，默认的许可值为 1024* 1024。
     **/
    public static ContentFactory getContentFactory(HttpServletRequest request, int maxLength) throws IOException {
        Hashtable values=new Hashtable();
        Hashtable files=new Hashtable();
        String contentType=request.getContentType();
        int contentLength = request.getContentLength();

        if (contentLength>maxLength)
        {

        }
        if(contentType==null || !contentType.startsWith("multipart/form-data")) {
            return null;
        }
        //get out the boudary from content-type
        int start=contentType.indexOf("boundary=");
        //这里应该
        int boundaryLen=new String("boundary=").length();
        String boundary=contentType.substring(start+boundaryLen);
        boundary="--"+boundary;
        //用字节表示，以免 String  和 byte 数组的长度不一致
        boundaryLen=bytesLen(boundary);

        //把request 中的数据读入一个byte数组
        byte buffer[] = new byte[contentLength];
        int once = 0;
        int total = 0;
        DataInputStream in = new DataInputStream(request.getInputStream());
        while ((total<contentLength) && (once>=0)) {
            once = in.read(buffer,total,contentLength);
            total += once;
        }
        //对buffer中的数据进行拆分
        int pos1=0;                  //pos1 记录 在buffer 中下一个 boundary 的位置
        //pos0,pos1 用于 subBytes 的两个参数
        int   pos0=byteIndexOf(buffer,boundary,0);   //pos0 记录 boundary 的第一个字节在buffer 中的位置
        do
        {
            pos0+=boundaryLen;        // 去除boundary                          //记录boundary后面第一个字节的下标
            pos1=byteIndexOf(buffer,boundary,pos0); // 查找下个boundary 的位置
            if (pos1==-1)
                break;
            //
            pos0+=2;          //考虑到boundary后面的 \r\n

            parse(subBytes(buffer,pos0,pos1-2),values,files);      //考虑到boundary后面的 \r\n
            pos0=pos1;
        }while(true);
        return new ContentFactory(values,files);
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
        if (search.length==0)
        {
            return 0;
        }
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
        System.arraycopy(source,from,result,0,end-from);
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
}