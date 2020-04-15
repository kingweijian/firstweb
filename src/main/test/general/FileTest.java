package general;

import org.apache.log4j.Logger;
import org.ini4j.Ini;
import org.junit.Before;
import org.junit.Test;


import java.io.File;

import java.io.IOException;

import java.util.*;


public class FileTest {
   final static String filename = "D:/OFD_888.ini";
   final static Logger logger = Logger.getLogger(FileTest.class);

    static Map<String,Map<String,List>> configs ;
    @Before
    public void init(){
        Ini ini = null;
        try {
            ini = new Ini(new File(filename));
            configs =  initConfigFile(ini);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Test
    public void testinit(){}
    @Test
    public void testContext(){
        String value = "586586                                                                                                                  568856                        6568586              20200318000000000463870113644636343871                 200317test1                                                                                                             2020031814521721000008656                          0126347              0108A00002795    387      001                                                                                                         3652259587433654                                                                                                   0000000012345678912             387                            200317test1                                                                0000000001                           招商银行13                                                  3652259587433654                     00000000   8866                                                         20991231                5                                                                                                                                                                   000000000000000000 8                                              0000000000000000                                                             2                                                                                                                \n" ;
        String value1 = "湖北省武汉市                                                                                                                                                               bbbbbbbbyyy000cccccccc  1JGZyyycccccccc                个人yyydddddddd                                                                                                         bbbbbbbb1500001999999                                                   JYZHyyyffffffff  yyy      zzz                                                                                                 19990829Qyyycccccccc       yyy 01 ASP@139.C0M                                                     02                       0000000013489883848             yyy                            个人1               0                    Zyyyeeeeeeee                      0000000001                           个人yyydddddddd                                             ZJZH01                               00000000156yyy                                                                                                                                                                                                                                                      000000000000000000                                                0000000000000000                                                             2                                                                                                                 \n";
        logger.info(value.length());
        logger.info(value1.length());
        Map<String,List> file01 = configs.get("01");
       List<String> iteminfo = new ArrayList<String>(), reaulst = new ArrayList<String>();
        int start = 0,end;
        for(String key : file01.keySet()){
 //            类型		 长度	 保留位数	 中文注释	 字段名
//            C,		 60,		0	,	 通讯地址,	 Address
            // 第一步，拿到字段信息
            iteminfo = file01.get(key);
            logger.info(Arrays.toString(iteminfo.toArray())  + " --- " + iteminfo.get(1));
            // 分割数据
            end = Integer.valueOf(iteminfo.get(1));
            logger.info(value.substring(start, start+end));
            start += end;

        }
    }
    @Test
    public  void testFileIni(){
        String filename = "D:/OFD_888.ini";


//            Ini ini = new Ini(new File(filename));
//            Map<String,Map<String,Object>> configs =  initConfigFile(ini);
            System.out.println(Arrays.toString(configs.get("01").get("Specification").toArray()));
            System.out.println(((String[])configs.get("01").get("Specification").toArray())[3]);

    }

    public static Map<String,Map<String, List>>   initConfigFile(Ini ini){
        // 最终返回的对象，里面是完整的数据（文件里面的字段顺序不是按顺序排序的）
        Map<String,Map<String,List>> files = new HashMap<String, Map<String,List>>();
        // 这个是排序的字段
        Map<String,List> sequenceFiled = new HashMap<String, List>();
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
           files.put(key,prams);
           sequenceFiled.put(key,sequence);
        }
        for (String key :sequenceFiled.keySet()){
            List objectMap =  (List) sequenceFiled.get(key);
            logger.info(Arrays.toString(objectMap.toArray()));
//            for (String key1 : objectMap.keySet()){
//                if(objectMap.get(key1) instanceof String)
//                    System.out.println(objectMap.get(key1));
//                else
//                    System.out.println(Arrays.toString((String[]) objectMap.get(key1)));
//
//            }
        }
        return files;
    }

}
