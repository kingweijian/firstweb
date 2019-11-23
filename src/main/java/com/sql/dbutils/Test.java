package com.sql.dbutils;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Test {
   static Operating operating = new Operating();
 //  static DBOperating db = new SQL_DBUtil("test");
    public static void main(String[] args) throws Exception {

//       Connection connection = db.getConn();
//       db.closeConn();
//       System.out.println(connection + "" + connection.isClosed());
//        System.out.println(db.getConn().createStatement());
//        testSel();
//        testDel();
//        testUap();
//        testInster();
        File file = new File("F:\\study\\webtest\\out\\artifacts\\webtest_war_exploded\\upfile\\b34e578ce0e24074b52315bd08ef20b6.png");
        if(file.exists()){
            System.out.println(file.delete());
        }
    }
    public static void testSel(){
        try {
        ResultSet rs = operating.db_select(operating.sql_select("account","id,name,password","","",""),new SQL_DBUtil("testone"));
        while (rs.next())
        {
            System.out.println(rs.getString(1) + "," +rs.getString(2)+ "," +rs.getString(3));
        }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void testDel(){
        try {
            System.out.println(operating.db_delete(operating.sql_delete("account","id=3"),new SQL_DBUtil("testone")));
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void testUap(){
        try {
            System.out.println(operating.db_update(operating.sql_update("account","name='王五',password=3515","id=4"),new SQL_DBUtil("testone")));
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public static void testInster(){
        try {
            System.out.println(operating.db_inster(operating.sql_inster("account","name,password","'赵六',35135135"),new SQL_DBUtil("testone")));
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
