package com.sql.dbutils;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQL_DBUtil implements DBOperating{
    private  static String NAME = "root";
    private  static String PASSWORD = "1234";
    private static String URL = "jdbc:mysql://localhost:3306/";
    private  static String DRIVER = "com.mysql.jdbc.Driver";
    private Connection conn = null;

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
            System.out.println("链接数据库成功！！！");
        } catch (ClassNotFoundException  e) {

            //System.out.println("--------驱动类没找到！！！--------");
            e.printStackTrace();
        }
        return conn;
    }
    public  Connection getConn()throws SQLException{


        return  creatConn();
    }

    public  void closeConn(){
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}

