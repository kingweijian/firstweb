package com.sql.dbutils;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import com.mysql.jdbc.Driver;
import org.apache.log4j.Logger;

public class SQL_DBUtil implements DBOperating{
    private  static String NAME = "root";
    private  static String PASSWORD = "123456";
    private static String URL = "jdbc:mysql://localhost:3306/";
    private  static String DRIVER = "com.mysql.jdbc.Driver";
    private Connection conn = null;
    public static Logger logger = Logger.getLogger(SQL_DBUtil.class);

    public SQL_DBUtil (String dbname){
        URL += dbname + "?useUnicode=true&characterEncoding=UTF-8";
    }

    public SQL_DBUtil (String username,String password,String url,String driver){
        NAME = username;
        PASSWORD = password;
        URL = url;
        DRIVER = driver;
    }
    public SQL_DBUtil (String dbname ,String username,String password,String url,String driver){
        NAME = username;
        PASSWORD = password;
        URL = dbname + url;
        DRIVER = driver;
    }
    private  Connection creatConn() throws SQLException {

        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(URL, NAME, PASSWORD);
            logger.info("--- 数据库连接成功！！！！ ---- ");
        } catch (ClassNotFoundException  e) {


            logger.error(" ---- 驱动类没有找到 ----- ",e);
        }
        return conn;
    }
    public  Connection getConn()throws SQLException{


        return  creatConn();
    }

    public  void closeConn(){
        try {
            conn.close();
            logger.info(" ---- 数据库关闭成功！！！ ----");
        } catch (SQLException e) {
           logger.error(" ---- 数据库关闭异常 ---- " , e);
        }
    }


}

