package general;

import org.apache.log4j.Logger;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * ClassName TestFileNew
 * Author weijian
 * DateTime 2020/5/17 10:47 PM
 * Version 1.0
 */
public class TestFileNew {
    final static Logger logger = Logger.getLogger (TestFileNew.class);
    @Test
    public void testContext(){
        //读取文件
        try {
            FileTest.init ();
        } catch (ClassNotFoundException e) {
            e.printStackTrace ();
        }
        // 获得文件关键字段
//        String filenum = "03";
//        String value = "586586                                                                                                                  568856                        6568586              20200318000000000463870113644636343871                 200317test1                                                                                                             2020031814521721000008656                          0126347              0108A00002795    387      001                                                                                                         3652259587433654                                                                                                   0000000012345678912             387                            200317test1                                                                0000000001                           招商银行13                                                  3652259587433654                     00000000   8866                                                         20991231                5                                                                                                                                                                   000000000000000000 8                                              0000000000000000                                                             2                                                                                                          \n"+
//                       "586587                                                                                                                  568856                        6568586              20200318000000000463870113644636343871                 200317test1                                                                                                             2020031814521721000008656                          0126347              0108A00002795    387      001                                                                                                         3652259587433654                                                                                                   0000000012345678912             387                            200317test1                                                                0000000001                           招商银行13                                                  3652259587433654                     00000000   8866                                                         20991231                5                                                                                                                                                                   000000000000000000 8                                              0000000000000000                                                             2                                                                                                          \n"  ;
//        String value01 = "湖北省武汉市                                                                                                                                                               bbbbbbbbyyy000cccccccc  1JGZyyycccccccc                个人yyydddddddd                                                                                                         bbbbbbbb1500001999999                                                   JYZHyyyffffffff  yyy      zzz                                                                                                 19990829Qyyycccccccc       yyy 01 ASP@139.C0M                                                     02                       0000000013489883848             yyy                            个人1               0                    Zyyyeeeeeeee                      0000000001                           个人yyydddddddd                                             ZJZH01                               00000000156yyy                                                                                                                                                                                                                                                      000000000000000000                                                0000000000000000                                                             2                                                                                                                 \n"
//                        + "湖南省长沙市                                                                                                                                                               bbbbbbbbyyy000cccccccc  1JGZyyycccccccc                个人yyydddddddd                                                                                                         bbbbbbbb1500001999999                                                   JYZHyyyffffffff  yyy      zzz                                                                                                 19990829Qyyycccccccc       yyy 01 ASP@139.C0M                                                     02                       0000000013489883848             yyy                            个人1               0                    Zyyyeeeeeeee                      0000000001                           个人yyydddddddd                                             ZJZH01                               00000000156yyy                                                                                                                                                                                                                                                      000000000000000000                                                0000000000000000                                                             2                                                                                                                 ";
//        String value04 = "2020040900000000083387202020041315600000000000000000000000000000000D4990102020040909022900000108A00002722    387      00000000000000000000003000000000149D4100008795438700000000011      110000                       20200413000000000000000000000010000387                                      00000000001        0000000000000000000000000000000000000000000                                                                                20200409                                         00000000000000000000060000000000000000000000000000000000000010000         000000000000000000000000000000000000000000000000000000000000000000000000        0000000000100                 0000000000                         8866                                                      00                                                                  00000 0000000000000000    00000      00000030000000000000000000100000000000000000000000000000000000000000000000000000000000000000000 38700000000011      00000000000000000000000000000000000000000000000000000000000000000        000000000000000000000000000000000000                                                         0000000000060000 20200413000000000000000000000000000000000000000000000000                \n";
//        String value03 = "202004100000000002638720D49901 202004100945240108A00001840    387      00000000000000000000000001000000020D4100008792110000                       156387                                      10000000                                                        00000                 0000000000                              0000000000000100                                                                  00000000000000000                 1                                  8866                                                      00               2                                          00000 0000000000000000    10000                       00000000000000000000000000\n";


//        List<List> fileconfig = sequenceFiled.get(filenum);

//        logger.info (parsingLine (value03,fileconfig,fileinfo.get (filenum)));
        String TANO = "E6", date = "20200507",  distributor = "387", interfaceType = "21",
                baseDir = FileTest.replaceDir (FileTest.SALE_TO_TA_DIR,TANO,date,distributor);
        // 获取文件全称
        String[] filename = FileTest.getSale_to_taFileList (
                TANO,date,distributor,interfaceType,2
        );
        // 获取文件开始位置
        String[] fileStart = FileTest.getSale_to_taFileList (
                TANO,date,distributor,interfaceType,1
        );

        // 获取文件有多少个字段
        String[] fileFiledNum = FileTest.getSale_to_taFileList (
                TANO,date,distributor,interfaceType,4
        );
        // 文件一行有多长
        String[] fileLinelen = FileTest.getSale_to_taFileList (
                TANO,date,distributor,interfaceType,3
        );

        // 获取文件简称
        String[] fileAbbreviation = FileTest.getTaFileName (filename);
//        logger.info (Arrays.toString (fileAbbreviation));
        String filenum = "",ret;
        for (int i = 0; i < fileAbbreviation.length; i++){

            File file = new File(baseDir + "/" + filename[i]);
            logger.info(file.getPath());
            if(file.exists ()){
                logger.info (file.getPath ());
                filenum = fileAbbreviation[i];
                logger.info("文件别名: " + filenum + " ,文件有多少个字段 : "+fileFiledNum[i] + " , 文件一行有多长 ： " + fileLinelen[i]);
                // 解析文件，根据申请文件中的数据生成确认数据  ↑ 有问题
                ret = FileTest.generateConfirmedFileContext(
                        FileTest.readFile (file,Integer.valueOf (fileStart[i])).split ("\r\n"),
                        FileTest.sequenceFiled.get(filenum),
                        fileFiledNum[i],
                        fileLinelen[i],
                        "03",
                        TANO);
                logger.info(filenum + " ---- >>>>" + ret);

                try {
                    // 根据生成的确认数据，生成确认文件
                    FileTest.NewFile (TANO,date,distributor,interfaceType,ret,FileTest.getCofirmedFileName (filenum)+"_" + interfaceType);
                } catch (IOException e) {
                    e.printStackTrace ();
                }

            }

        }



    }
}
