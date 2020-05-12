package general;

import com.handlers.Test;
import org.apache.log4j.Logger;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestFile {
    final  static Logger logger = Logger.getLogger(TestFile.class);

    /**
     *  读取文件，如需要读取文件的全部内容，len请传 -1，
     *  如只需要读取指定行的内容，那么请传入行号
     * @param dir 文件路径
     * @param len 从第几行开始读文件
     * @return
     */
    public static String readFile(String dir,int len){
//        InputStreamReader inputStreamReader = new InputStreamReader (new FileInputStream (dir),"UTF-8");
        BufferedReader bufferedReader = null;
        StringBuffer stringBuffer = new StringBuffer ();
        try {
            bufferedReader = new BufferedReader (new FileReader(new File(dir)));
            String strLine = null;
            while ((strLine = bufferedReader.readLine ()) != null){
                if(len>0){
                    len--;
                }else
                {
                    stringBuffer.append (strLine).append("\r\n");
                }

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

    /**
     * 根据规则替换 TA 代码， 时间，代销人代码
     * @param str 需要替换的字符串
     * @param taNo TA 代码
     * @param date 时间
     * @param distributor 代销人代码
     * @return
     */
    public static String replaceDir(String str, String taNo, Date date, String distributor){
        String dateStr =  new SimpleDateFormat("yyyyMMdd").format (date);
        str = str.replace ("@@",taNo);
        str = str.replace ("###",distributor);
        str = str.replace ("YYYYMMDD",dateStr);
        return str;
    }
}
