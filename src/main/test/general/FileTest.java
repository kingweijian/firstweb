package general;

import org.apache.log4j.Logger;
import org.ini4j.Ini;
import org.junit.Before;
import org.junit.Test;


import java.io.File;

import java.io.IOException;

import java.util.*;

 enum DD {
    C("C",String.class),
    A("A",Integer.class),
    N("N",Double.class),
    TEXT("TEXT",String.class);
    public String name ;
    public Class c;
    DD(String name , Class c) {
        this.name = name;
        this.c = c;
    }
}
public class FileTest {
//   final static String filename = "/Users/weijian/Documents/unitfile/OFD_888.ini";
    final static String filename = "D:/OFD_888.ini";
    final static Logger logger = Logger.getLogger(FileTest.class);

    static Map<String,Map<String,List>> configs ;
    static Map<String,List> sequenceFiled;
    static Map<String,Map<String,String>> fileinfo =  new HashMap<String,Map<String,String>>();
    @Before
    public void init(){
        Ini ini = null;
        try {
            ini = new Ini(new File(filename));
            initConfigFile(ini);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String,String> filedetail04 = new HashMap<String,String>();
        filedetail04.put("contextLen","1235");
        filedetail04.put("filenum","04");
        fileinfo.put("04",filedetail04);

        Map<String,String> filedetail01 = new HashMap<String,String>();
        filedetail01.put("contextLen","1456");
        filedetail01.put("filenum","01");

        fileinfo.put("01",filedetail01);

        Map<String,String> filedetail03 = new HashMap<String,String>();
        filedetail03.put("contextLen","666");
        filedetail03.put("filenum","03");

        fileinfo.put("03",filedetail03);
        Map<String,String> filedetail05 = new HashMap<String,String>();
        filedetail05.put("contextLen","192");
        filedetail05.put("filenum","05");

        fileinfo.put("05",filedetail05);
    }
    @Test
    public void testinit(){}

    // 解析文件，主要用于解析申请文件，之后方便生成确认文件
    @Test
    public void testContext(){
        //读取文件

        // 获得文件关键字段
        String filenum = "05";
        String value = "586586                                                                                                                  568856                        6568586              20200318000000000463870113644636343871                 200317test1                                                                                                             2020031814521721000008656                          0126347              0108A00002795    387      001                                                                                                         3652259587433654                                                                                                   0000000012345678912             387                            200317test1                                                                0000000001                           招商银行13                                                  3652259587433654                     00000000   8866                                                         20991231                5                                                                                                                                                                   000000000000000000 8                                              0000000000000000                                                             2                                                                                                          \n" ;
        String value1 = "湖北省武汉市                                                                                                                                                               bbbbbbbbyyy000cccccccc  1JGZyyycccccccc                个人yyydddddddd                                                                                                         bbbbbbbb1500001999999                                                   JYZHyyyffffffff  yyy      zzz                                                                                                 19990829Qyyycccccccc       yyy 01 ASP@139.C0M                                                     02                       0000000013489883848             yyy                            个人1               0                    Zyyyeeeeeeee                      0000000001                           个人yyydddddddd                                             ZJZH01                               00000000156yyy                                                                                                                                                                                                                                                      000000000000000000                                                0000000000000000                                                             2                                                                                                                 \n";
        String value04 = "2020040900000000083387202020041315600000000000000000000000000000000D4990102020040909022900000108A00002722    387      00000000000000000000003000000000149D4100008795438700000000011      110000                       20200413000000000000000000000010000387                                      00000000001        0000000000000000000000000000000000000000000                                                                                20200409                                         00000000000000000000060000000000000000000000000000000000000010000         000000000000000000000000000000000000000000000000000000000000000000000000        0000000000100                 0000000000                         8866                                                      00                                                                  00000 0000000000000000    00000      00000030000000000000000000100000000000000000000000000000000000000000000000000000000000000000000 38700000000011      00000000000000000000000000000000000000000000000000000000000000000        000000000000000000000000000000000000                                                         0000000000060000 20200413000000000000000000000000000000000000000000000000                \n";
        String value03 = "202004100000000002638720D49901 202004100945240108A00001840    387      00000000000000000000000001000000020D4100008792110000                       156387                                      10000000                                                        00000                 0000000000                              0000000000000100                                                                  00000000000000000                 1                                  8866                                                      00               2                                          00000 0000000000000000    10000                       00000000000000000000000000\n";
        String value05 = "0000000040000000000000004000000020200413D499020108A00001840    387      D410000879210000000000000000387                          000000000000000000         000000000000000000000000000000000 1\n";
//        logger.info(value05.length());
//        logger.info(value05.length());
        List<List> file01 = sequenceFiled.get(filenum);
       List<String> reaulst = new ArrayList<String>();
        int start = 0,end;
        List<String> info = new ArrayList<String>();
        info.add("C");
        info.add(fileinfo.get(filenum).get("contextLen"));
        value = BackUpStr (value05,info);
        logger.info(value.length());
 //            类型		 长度	 保留位数	 中文注释	 字段名
//            C,		 60,		0	,	 通讯地址,	 Address
        logger.info(file01.size ());
            // 第一步，拿到字段信息
            for (int i = 0 ; i < file01.size () ; i++){
                logger.info(Arrays.toString(file01.get (i).toArray())  + " --- " + file01.get(i).get (1) + "  ----  " +  i + " start --- " +start);
                // 分割数据
                end = Integer.valueOf( (String)file01.get(i).get (1));
                logger.info(value.substring(start, start+end));
                start += end;
            }




    }
    // 读取 ini 文件
    @Test
    public  void testFileIni(){
        String filename = "D:/OFD_888.ini";


//            Ini ini = new Ini(new File(filename));
//            Map<String,Map<String,Object>> configs =  initConfigFile(ini);
            System.out.println(Arrays.toString(configs.get("01").get("Specification").toArray()));
            System.out.println(((String[])configs.get("01").get("Specification").toArray())[3]);

    }
    // 初始化配置文件，解析配置文件，按照我们想要的存储方式存储
    public static void   initConfigFile(Ini ini){
        // 最终返回的对象，里面是完整的数据（文件里面的字段顺序不是按顺序排序的）
        configs = new HashMap<String, Map<String,List>>();
//        configs = new TreeMap<String,Map<String,List>>(new Comparator<String>() {
//            @Override
//            public int compare(String o1, String o2) {
//                return o1.compareTo(o2);
//            }
//        });
        // 这个是排序的字段
        sequenceFiled = new HashMap<String, List>();
        Map<String,List> prams ;
        List<List> sequence;
        String[] splitValue;
        String strValue;
        // 第一层，拿到文件
        for (String key : ini.keySet()){
            prams = new HashMap<String,List>();
            sequence = new ArrayList<List>();
            // 第二层拿到文件下面的数据
           for (String key1 : ini.get(key).keySet()){
                strValue = ini.get(key).get(key1);
//                System.out.println(strValue);
               // 第三层，把数据值按，分成数组
                if(strValue.indexOf(",") != -1){
                    splitValue = strValue.split(",");
//                    System.out.println("数组输出："+splitValue[4]);
                    prams.put(key1,Arrays.asList(splitValue));
                    sequence.add(Arrays.asList(splitValue));
                }
           }
            configs.put(key,prams);
            sequenceFiled.put(key,sequence);
        }
//        for (String key :sequenceFiled.keySet()){
//            List objectMap =  (List) sequenceFiled.get(key);
//            logger.info( key + " -- " +Arrays.toString(objectMap.toArray()));
//            for (String key1 : objectMap.keySet()){
//                if(objectMap.get(key1) instanceof String)
//                    System.out.println(objectMap.get(key1));
//                else
//                    System.out.println(Arrays.toString((String[]) objectMap.get(key1)));
//
//            }
//        }
//        return files;
    }
    // 填充或者去除字符串
    public static String BackUpStr(String value,List fieldInfo){
        String ret,type = (String)fieldInfo.get(0),Fillsymbol  = (type.equals("TEXT") || type.equals("C")) ? " " : "0";
        int len = Integer.valueOf((String)fieldInfo.get(1));
        int difference = value.length () - len;
        if (difference == 0)
            return value;
        if (difference > 0){
            ret = value.substring (0,len);
            return ret;
        }
        ret = value;
        if(difference < 0){
            for (int i = difference; i < 0 ; i++ ){
                ret += Fillsymbol;
            }
            return ret;
        }
        return null;
    }

    public <T> T processField(String value,List info){
//        T ret;
        if(info.get (0).equals ("C") || info.get(0).equals("TEXT")){
            value = value.trim ();
            return (T) value;
        }
//        数字和数值类型的 都转成整形，因为只有展示的时候才需要显示小数后两位，一般计算不需要小数点
        if(info.get (0).equals ("A") || info.get (0).equals ("N")){
            return (T) Long.valueOf (value.trim ());
        }

//        if(info.get (0).equals ("N")){
//            int len = Integer.valueOf ((String) info.get (1));
//            value = value.trim ();
//
//            return (T) Integer.valueOf (value.trim ());
//        }
        return null;
    }

    @Test
    public void testprocessField(){

        List<String> c = new ArrayList<String> (),a = new ArrayList<String> (),n = new ArrayList<String> (),text = new ArrayList<String> () ;
        c.add ("C");
        a.add ("A");
        n.add ("N");
        text.add ("TEXT");


       String str = processField("   243dwad4  ",c);
       logger.info (str);
        long num = processField("000000000000025400",a);
        logger.info (num);
//        double decimal = processField("2434000",n);
//        logger.info (decimal);

        String s = "000000000000025452";
        logger.info (num + (Long) processField(s,n));
    }
    // 生成文件时，填满对应字段之后
    @Deprecated
    public static String fieldFill(String value,List fieldinfo){
        // 对字符串的填充
        if(fieldinfo.get(0).equals("C") || fieldinfo.get(0).equals("TEXT")){
            value = BackUpStr( value,fieldinfo);
            return value;
        }
        // 对数字类型的填充
        if(fieldinfo.get(0).equals("N") || fieldinfo.get(0).equals("A")){
            String ton = value.replace(".","");
            StringBuffer stringBuffer = new StringBuffer();
            int len = Integer.valueOf((String) fieldinfo.get(1)),cha = ton.length()-len;
            for (int i = cha; i < 0 ; i++ ){
                stringBuffer.append("0");
            }
            stringBuffer.append(ton);
            return stringBuffer.toString();
        }
        return null;
    }

}
