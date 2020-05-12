package general;

import org.apache.log4j.Logger;
import org.ini4j.Ini;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
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
     final static String BASURL = Thread.currentThread().getContextClassLoader().getResource("").getPath();

     final static String FILEEND = "OFDCFEND";

     final static String TA_TO_SALE_DIR = BASURL + "TA_TO_SALE/###/@@/YYYYMMDD";
     final static String  SALE_TO_TA_DIR = BASURL + "SALE_TO_TA/###/@@/YYYYMMDD";

     final static String iniUrl = BASURL +"/OFD_888.ini";
     final static String confirmedUrl = BASURL+"/Required.xml";
     final static String taFileConfig = BASURL+"/TAFileConfig.ini";

     static String[] ta_to_sale_fileName=new String[9];
     static String[] ta_to_sale_fileName_21=new String[9];
     static String[] ta_to_sale_fileName_20=new String[9];

     static String[] sale_to_ta_fileName=new String[7];
     static String[] sale_to_ta_fileName_21=new String[7];
     static String[] sale_to_ta_fileName_20=new String[7];


//    final static String filename = "D:/OFD_888.ini";
     final static Logger logger = Logger.getLogger(FileTest.class);

     static Map<String,Map<String,List>> configs ;
    // 字段信息
     static Map<String,List> sequenceFiled;
     static Map<String,Map<String,List<String>>> taFileConfigMap;
     static Map<String,Map<String,String>> fileinfo =  new HashMap<String,Map<String,String>>();
     static Map<String, List<Map<String,String>>> confirmFileConf = new HashMap<String, List<Map<String,String>>> ();
     static Class<?>  pubResClass = null;
    @Before
    public void init() throws ClassNotFoundException {
        logger.info ("BASEURL -- " + BASURL);
        Ini ini = null;
        try {
            ini = new Ini(new File(iniUrl));
            sequenceFiled = initConfigFile(ini,true);
            ini = new Ini (new File (taFileConfig));
            taFileConfigMap = initConfigFile (ini,false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        pubResClass = Class.forName ("general.FileUnit");


        parsaXML();
        Map<String,String> filedetail04 = new HashMap<String,String>();
        filedetail04.put("contextLen","1235");
        filedetail04.put("filenum","04");
        filedetail04.put ("size","121");

        fileinfo.put("04",filedetail04);

        Map<String,String> filedetail01 = new HashMap<String,String>();
        filedetail01.put("contextLen","1456");
        filedetail01.put("filenum","01");
        filedetail01.put ("size","84");

        fileinfo.put("01",filedetail01);

        Map<String,String> filedetail02 = new HashMap<String,String>();
        filedetail02.put("contextLen","466");
        filedetail02.put("filenum","02");
        filedetail02.put ("size","27");

        fileinfo.put("02",filedetail02);

        Map<String,String> filedetail03 = new HashMap<String,String>();
        filedetail03.put("contextLen","666");
        filedetail03.put("filenum","03");
        filedetail03.put ("size","74");

        fileinfo.put("03",filedetail03);

        ta_to_sale_fileName[0] = "02";
        ta_to_sale_fileName[1] = "04";
        ta_to_sale_fileName[2] = "05";
        ta_to_sale_fileName[3] = "06";
        ta_to_sale_fileName[4] = "07";
        ta_to_sale_fileName[5] = "26";
        ta_to_sale_fileName[6] = "32";
        ta_to_sale_fileName[7] = "44";
        ta_to_sale_fileName[8] = "R2";

        sale_to_ta_fileName[0] = "01";
        sale_to_ta_fileName[1] = "03";
        sale_to_ta_fileName[2] = "31";
        sale_to_ta_fileName[3] = "43";
        sale_to_ta_fileName[4] = "R1";
        sale_to_ta_fileName[5] = "13";
        sale_to_ta_fileName[6] = "23";

        for (int i = 0; i < ta_to_sale_fileName.length; i++){
            ta_to_sale_fileName_21[i] = ta_to_sale_fileName[i] + "_21";
            ta_to_sale_fileName_20[i] = ta_to_sale_fileName[i] + "_20";
        }

        for (int i = 0; i < sale_to_ta_fileName.length; i++){
            sale_to_ta_fileName_21[i] = sale_to_ta_fileName[i] + "_21";
            sale_to_ta_fileName_20[i] = sale_to_ta_fileName[i] + "_20";
        }

    }
    @Test
    public void testReadFileLine() throws IOException {
        String tempFile = BASURL + taFileConfigMap.get("sale_to_ta").get ("01_21").get(0),ton;
        File file = new File (tempFile);
        BufferedReader bufferedReader = new BufferedReader (new FileReader (file));
        String[] str = retLine (bufferedReader,95).split ("\n");
        logger.info (str.length);

    }

    public static String retLine(BufferedReader bufferedReader, int len) throws IOException {
        String s = null;
        StringBuffer stringBuffer = new StringBuffer ();
            while ((s = bufferedReader.readLine ())!=null ){
                if(len>0){
                    len--;
                }else
                {
                    stringBuffer.append (s + "\n");
                }
                logger.info (len);
            }
        bufferedReader.close ();
        return stringBuffer.toString ();
    }

    public static void checkFileToNew(File file,boolean isDIR){
        if (!file.exists () && isDIR){
            file.mkdir ();
        }
    }

    public static boolean NewAirFileForConfirm(String taNo,Date date,String distributor,String interfaceType) throws IOException {
        String[] confirmFile = null;

        Map<String,List<String>> filedetail = taFileConfigMap.get("ta_to_sale");
        List<String> fileinfo = null;
        String readStr = null;
        if(interfaceType.equals ("21"))
            confirmFile = ta_to_sale_fileName_21;
        else
            confirmFile = ta_to_sale_fileName_20;

        String dir =  replaceDir(TA_TO_SALE_DIR,taNo,date,distributor),ton;

        logger.info (dir);
        File file = new File (dir);
        if(!file.exists ()){
            file.mkdirs ();
        }
        FileWriter fileWriter = null;
        String tempFile = null;
        // 生成文件
        for (int i =0 ; i < confirmFile.length; i++){
            fileinfo = filedetail.get (confirmFile[i]);
            // 获取模板路径，后面读取文件
            tempFile = BASURL + fileinfo.get(0);
            ton = dir + "/" + replaceDir(fileinfo.get(2),taNo,date,distributor);
            logger.info( "需要写入的文件 ： --- " + ton);
            file = new File (ton);
            logger.info("模板文件 ： ----- " + tempFile);
            if(!file.exists ())
                file.createNewFile ();

            readStr = replaceDir(readFile (tempFile),taNo,date,distributor) + FILEEND;
            logger.info(readStr);

            fileWriter = new FileWriter (file);
            fileWriter.write(readStr);
            fileWriter.close ();

        }

        return false;
//         throw new NullPointerException();
    }

    public static String readFile(String dir){
//        InputStreamReader inputStreamReader = new InputStreamReader (new FileInputStream (dir),"UTF-8");
        BufferedReader bufferedReader = null;
        StringBuffer stringBuffer = new StringBuffer ();
        try {
            bufferedReader = new BufferedReader (new FileReader (new File(dir)));
            String strLine = null;
            while ((strLine = bufferedReader.readLine ()) != null){
                stringBuffer.append (strLine).append("\r\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace ();
        } catch (IOException e) {
            e.printStackTrace ();
        } finally {
            try {
                bufferedReader.close ();
            } catch (IOException e) {
                e.printStackTrace ();
            }
        }

        return stringBuffer.toString ();
    }

    public static void wirtFile(String dir,String str){

    }

    public static String replaceDir(String str,String taNo,Date date,String distributor){
        String dateStr =  new SimpleDateFormat ("yyyyMMdd").format (date);
        str = str.replace ("@@",taNo);
        str = str.replace ("###",distributor);
        str = str.replace ("YYYYMMDD",dateStr);
        return str;
    }

    @Test
    public void testinit(){
        logger.info (taFileConfigMap);
        logger.info (sequenceFiled);
        try {
            NewAirFileForConfirm("F1",new Date (),"387","21");
        } catch (IOException e) {
            e.printStackTrace ();
        }
    }
    /**
    * @Description:   分解每个文件的一行数据，然后存入Map中，方便之后的操作，map 同一key为String，String ，    ---- 可以考虑返回list，因为有必填字段信息，只要遍历必填信息，然后从中取值
    * @Param: [value 需要解析的值, fileinfo 整个文件的配置]
    * @return: java.lang.Map
    * @Author: weijian
    * @Date: 2020/4/18
     *            类型		 长度	 保留位数	     中文注释	        字段名
     *            C,		 60,		0	,	 通讯地址,	     Address
    */
    public static Map<String,String> parsingLine(String value,List<List> fileinfo,Map<String,String> fileConfig){
        Map<String,String> reaulst = new HashMap<String, String> ();
        int start = 0,end; List<String> cofdetail;
        value = BackUpStr (value,Integer.valueOf(fileConfig.get("contextLen")));
        int size = Integer.valueOf (fileConfig.get ("size"));
        for (int i = 0 ; i < size ; i++){
            cofdetail = fileinfo.get (i);
//            logger.info(Arrays.toString(cofdetail.toArray())  + " --- " +cofdetail.get (1) + "  ----  " +  i + " start --- " +start);
            // 分割数据
            end = Integer.valueOf( cofdetail.get (1));
//            logger.info(value.substring(start, start+end));
            reaulst.put (cofdetail.get (4),value.substring(start, start+end));
            start += end;
        }



        return reaulst;
    }

    /**
     *
     * @param fileConent 需要解析的文件内容
     * @param fileinfo     需要解析的文件信息（里面主要包括文件字段信息）
     * @param fileConfig    需要解析的文件配置 （需要解析的长度，字段大小... 等等待补充信息，后期读取配置）
     * @return
     */
    public static String generateConfirmedFileContext(String[] fileConent,List<List> fileinfo,Map<String,String> fileConfig){


        Map<String,String> filedinfo ;
        // TA编码
        String TANo = "65";
        // 获得需要解析的文件名字
        String fileName = fileConfig.get ("filenum");
        // 根据解析文件名字获取确认文件名字
        fileName = getCofirmedFileName (fileName);
        // 获取确认文件必填字段信息
        Map<String,String> confirmInfo = confirmFileConf.get (fileName).get (0);
        // 获取确认文件必填字段信息
        String itmflag = confirmInfo.get ("itmflag");
        // 分割确认文件必填字段信息
        String[] confieFiled = itmflag.split ("\\|");
        // 获得 需要确认文件里面的字段信息
        List<List> fileconfig = sequenceFiled.get(fileName);

        List<String> fileddeatil ;
//        for (int i = 0; i < confieFiled.length; i++){
//            fileddeatil = fileconfig.get (i);
//            logger.info (confieFiled[i] + " --- " + fileddeatil.get (4));
//
//        }
        // fileConent.length - 1 排除最后一行空行
        PubRes res = new PubRes (
                fileName,TANo,
                "",null,
                true,false,
                fileName,"0");
        for (int i = 0; i < fileConent.length; i++){
            filedinfo = parsingLine (fileConent[i],fileinfo,fileConfig);

            logger.info (generateConfirmedData(filedinfo,fileconfig,confieFiled,res));
        }
        return null;
    }
    @Test
    public void TestFS() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = pubResClass.getMethod ("ShareRegisterDate", PubRes.class);
        logger.info (method.invoke (pubResClass,new PubRes ()));

    }

    /**
    * @Description: 参数：解析的数据，解析的文件信息，确认字段信息
    * @Param: [filedinfo, fileconfig, confieFiled]
    * @return: java.lang.String
    * @Author: weijian
    * @Date: 2020/5/4
    */
    public static String generateConfirmedData(
            Map<String,String> filedinfo,  List<List> fileconfig,
            String[] confieFiled, PubRes res ){
        res.setToBeresolvedInfo (filedinfo);
        StringBuffer ret = new StringBuffer ();
        List<String> fileddeatil ;
        String key = null;
        for (int i = 0; i < confieFiled.length; i++){
            fileddeatil = fileconfig.get (i);
            key = fileddeatil.get (4);

            Method method = null;
            try {
                method = pubResClass.getMethod (key, PubRes.class);
                if(method!=null){
                    ret.append (String.valueOf (method.invoke (pubResClass,res)));
                    logger.info ("确认字段 ： "+fileddeatil.get (3)+" "+confieFiled[i] + " --- " +key + " --- " +method.invoke (method,res));
                    continue;
                }
            } catch (IllegalAccessException e) {
                logger.error ("error "+key,e);
                e.printStackTrace ();
            } catch (InvocationTargetException e) {
                logger.error ("error "+key,e);

            }catch (NoSuchMethodException e) {
               logger.error ("error "+key,e);
            }
//                logger.info ("确认字段 ： "+fileddeatil.get (3)+" "+confieFiled[i] + " --- " +key + " --- " +ret.toString ());

//            logger.info ("申请字段 ： "+fileddeatil.get (3)+" "+confieFiled[i] + " --- " +key + " --- " +filedinfo.get (key));
            // 判断如果是必填的值而且在解析的字段里面有数据，那么就拿
            if (confieFiled[i].equals ("Y") && filedinfo.containsKey (key)){

                    ret.append (filedinfo.get (key));
//                logger.info ("确认字段 ： "+fileddeatil.get (3)+" "+confieFiled[i] + " --- " +key + " --- " +ret.toString ());
                continue;
            }

            if(confieFiled[i].equals ("N") ||  !filedinfo.containsKey (key)){
                ret.append (FillStr (fileddeatil.get (0),Integer.valueOf (fileddeatil.get (1))));
//                logger.info ("确认字段 ： "+fileddeatil.get (3)+" "+confieFiled[i] + " --- " +key + " --- " +ret.toString ());
                continue;
            }

        }
        return ret.toString ();
    }

    public static String getCofirmedFileName(String fileName){

        if(fileName.equals ("01")){  // 开户
            return "02";
        }else if(fileName.equals ("03")){ // 确认
            return "04";
        }else if (fileName.equals ("25")){ // 短期理财
            return "26";
        }else if(fileName.equals ("R1")){// 涉税
            return "R2";
        }else if(fileName.equals ("31")){// 电子合同 31文件
            return "32";
        }else if(fileName.equals ("43")){// 电子合同 43 文件
            return "44";
        }

        return fileName;
    }

    @Test
    public void testContext(){
        //读取文件

        // 获得文件关键字段
        String filenum = "03";
        String value = "586586                                                                                                                  568856                        6568586              20200318000000000463870113644636343871                 200317test1                                                                                                             2020031814521721000008656                          0126347              0108A00002795    387      001                                                                                                         3652259587433654                                                                                                   0000000012345678912             387                            200317test1                                                                0000000001                           招商银行13                                                  3652259587433654                     00000000   8866                                                         20991231                5                                                                                                                                                                   000000000000000000 8                                              0000000000000000                                                             2                                                                                                          \n"+
                       "586587                                                                                                                  568856                        6568586              20200318000000000463870113644636343871                 200317test1                                                                                                             2020031814521721000008656                          0126347              0108A00002795    387      001                                                                                                         3652259587433654                                                                                                   0000000012345678912             387                            200317test1                                                                0000000001                           招商银行13                                                  3652259587433654                     00000000   8866                                                         20991231                5                                                                                                                                                                   000000000000000000 8                                              0000000000000000                                                             2                                                                                                          \n"  ;
        String value01 = "湖北省武汉市                                                                                                                                                               bbbbbbbbyyy000cccccccc  1JGZyyycccccccc                个人yyydddddddd                                                                                                         bbbbbbbb1500001999999                                                   JYZHyyyffffffff  yyy      zzz                                                                                                 19990829Qyyycccccccc       yyy 01 ASP@139.C0M                                                     02                       0000000013489883848             yyy                            个人1               0                    Zyyyeeeeeeee                      0000000001                           个人yyydddddddd                                             ZJZH01                               00000000156yyy                                                                                                                                                                                                                                                      000000000000000000                                                0000000000000000                                                             2                                                                                                                 \n"
                        + "湖南省长沙市                                                                                                                                                               bbbbbbbbyyy000cccccccc  1JGZyyycccccccc                个人yyydddddddd                                                                                                         bbbbbbbb1500001999999                                                   JYZHyyyffffffff  yyy      zzz                                                                                                 19990829Qyyycccccccc       yyy 01 ASP@139.C0M                                                     02                       0000000013489883848             yyy                            个人1               0                    Zyyyeeeeeeee                      0000000001                           个人yyydddddddd                                             ZJZH01                               00000000156yyy                                                                                                                                                                                                                                                      000000000000000000                                                0000000000000000                                                             2                                                                                                                 ";
        String value04 = "2020040900000000083387202020041315600000000000000000000000000000000D4990102020040909022900000108A00002722    387      00000000000000000000003000000000149D4100008795438700000000011      110000                       20200413000000000000000000000010000387                                      00000000001        0000000000000000000000000000000000000000000                                                                                20200409                                         00000000000000000000060000000000000000000000000000000000000010000         000000000000000000000000000000000000000000000000000000000000000000000000        0000000000100                 0000000000                         8866                                                      00                                                                  00000 0000000000000000    00000      00000030000000000000000000100000000000000000000000000000000000000000000000000000000000000000000 38700000000011      00000000000000000000000000000000000000000000000000000000000000000        000000000000000000000000000000000000                                                         0000000000060000 20200413000000000000000000000000000000000000000000000000                \n";
        String value03 = "202004100000000002638720D49901 202004100945240108A00001840    387      00000000000000000000000001000000020D4100008792110000                       156387                                      10000000                                                        00000                 0000000000                              0000000000000100                                                                  00000000000000000                 1                                  8866                                                      00               2                                          00000 0000000000000000    10000                       00000000000000000000000000\n";


        List<List> fileconfig = sequenceFiled.get(filenum);

//        logger.info (parsingLine (value03,fileconfig,fileinfo.get (filenum)));

        generateConfirmedFileContext(value03.split ("\n"),fileconfig,fileinfo.get (filenum));

    }
    @Test
    public  void testFileIni(){
//        String filename = "D:/OFD_888.ini";


//            Ini ini = new Ini(new File(filename));
//            Map<String,Map<String,Object>> configs =  initConfigFile(ini);
            System.out.println(Arrays.toString(configs.get("01").get("Specification").toArray()));
            System.out.println(((String[])configs.get("01").get("Specification").toArray())[3]);

    }

    public static Map   initConfigFile(Ini ini,boolean listOrMap){
        // 最终返回的对象，里面是完整的数据（文件里面的字段顺序不是按顺序排序的）
        configs = new HashMap<String, Map<String,List>>();
//        configs = new TreeMap<String,Map<String,List>>(new Comparator<String>() {
//            @Override
//            public int compare(String o1, String o2) {
//                return o1.compareTo(o2);
//            }
//        });
        // 这个是排序的字段
        Map<String,List> ret = new HashMap<String, List>();
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
           ret.put(key,sequence);
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
        return listOrMap ? ret : configs;
    }
    public static String BackUpStr(String value,int len){
        String ret;
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
                ret += " ";
            }
            return ret;
        }
        return null;
    }

    public static String FillStr(String type,int len){
        String sby=null;
        StringBuffer sb = new StringBuffer ();
        if(type.equals ("C") || type.equals("TEXT")){
            sby = " ";
        }
//        数字和数值类型的 都转成整形，因为只有展示的时候才需要显示小数后两位，一般计算不需要小数点
        if(type.equals ("A") || type.equals ("N")){
            sby = "0";
        }

        for (int i = 0; i < len; i++)
            sb.append (sby);

        return sb.toString ();

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

    @Test
    public void parsaXML(){
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance ();

        List<Map<String,String>> attrConf;Map<String,String> attrItem;
        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder ();
            Document document = documentBuilder.parse (confirmedUrl);
            NodeList nodeList = document.getElementsByTagName ("file");
            for (int i = 0; i < nodeList.getLength (); i++){
                Node node = nodeList.item (i);
                attrConf = new ArrayList<Map<String, String>> ();
                NamedNodeMap namedNodeMap = node.getAttributes ();
//                logger.info (namedNodeMap.getNamedItem ("filetype").getNodeValue ());
//                for (int j = 0; j < namedNodeMap.getLength (); j++){
//                    Node attr = namedNodeMap.item (j);
////                    confirmFileConf.put (attr.getNodeName ())
//                    logger.info (attr.getNodeName () + " : " + attr.getNodeValue ());
//                }

                NodeList childList = node.getChildNodes ();
                for (int j = 0 ; j < childList.getLength (); j++){
                    attrItem = new HashMap<String, String> ();
                    if(childList.item(j).getNodeType() == Node.ELEMENT_NODE){
                        Node child = childList.item (j);
                        NamedNodeMap childNodeMap = child.getAttributes ();
                        for (int t = 0; t < childNodeMap.getLength (); t++){
                            Node attr = childNodeMap.item (t);
                            attrItem.put (attr.getNodeName (),attr.getNodeValue ());
//                            logger.info (attr.getNodeName () + " : " + attr.getNodeValue ());
                        }
//
                        attrConf.add (attrItem);
                    }

                }
                confirmFileConf.put (namedNodeMap.getNamedItem ("filetype").getNodeValue (),attrConf);

            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace ();
        } catch (SAXException e) {
            e.printStackTrace ();
        } catch (IOException e) {
            e.printStackTrace ();
        }
//        logger.info (confirmFileConf.get ("02"));
    }

    /**
     * 不可行
     * 解析XML只能按节点解析
     */
    @Test
    public void testParaXML(){
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance ();
        List<Map<String,String>> attrConf;Map<String,String> attrItem;

        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder ();


            Document document = documentBuilder.parse (new FileInputStream (confirmedUrl));
            logger.info (document.getDocumentElement ());
        }catch (FileNotFoundException e){
            logger.error (e);
        } catch (ParserConfigurationException e) {
            e.printStackTrace ();
        } catch (IOException e) {
            e.printStackTrace ();
        } catch (SAXException e) {
            e.printStackTrace ();
        }
    }

}
