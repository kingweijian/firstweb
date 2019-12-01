package com.sql.dbutils;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Operating {
    public static Logger logger = Logger.getLogger(Operating.class);
    Statement statement = null;
    ResultSet rs = null;
    Connection connection = null;

    public ResultSet db_select(String sql,DBOperating dbOperating)  {
        if(!isDBOperating(dbOperating)) return null;
        try {
            connection = dbOperating.getConn();
            statement = connection.createStatement();
            rs =  statement.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            // 不能再这里关闭连接，因为关闭会导致数据随着连接或者留的关闭而被销毁，所有应当在使用出关闭
//            dbOperating.closeConn();
//            try {
//                statement.close();
//                rs.close();
//            } catch (SQLException e) {
//               logger.error("satement | resulset 关闭异常！",e);
//            }
//
        }


        if(rs != null ) return rs;
        return null;
    }

    public int db_update(String sql,DBOperating dbOperating) throws SQLException {
        if(!isDBOperating(dbOperating)) return 0;
        connection = dbOperating.getConn();
        statement = connection.createStatement();
        return statement.executeUpdate(sql);
    }

    public int db_delete(String sql,DBOperating dbOperating) throws SQLException {
        if(!isDBOperating(dbOperating)) return 0;
        connection = dbOperating.getConn();
        statement = connection.createStatement();
        return statement.executeUpdate(sql);
    }

    public int db_inster(String sql,DBOperating dbOperating) throws SQLException {
        if(!isDBOperating(dbOperating)) return 0;
        connection = dbOperating.getConn();
        statement = connection.createStatement();
        return statement.executeUpdate(sql);
    }
    public String sql_inster(String table,String fileds,String values){
        String sqlstr = "INSERT INTO " + table + " ( " + fileds + ") VALUE ( " + values + ")";
        logger.debug("sql ---- > " + sqlstr);
        return sqlstr;

    }
    public String sql_delete(String table,String where)
    {
        String sqlstr = "DELETE FROM " + table;
        if (where!="") sqlstr.concat(" WHERE " +where);
        logger.debug("sql ---- > " + sqlstr);
        return sqlstr;
    }
    public String sql_select(String table,String fileds,String where,String order,String limit,String group){
        String sqlstr = "SELECT "+ fileds+ " FROM " + table;
        if ( where !=null && where!="") sqlstr.concat(" WHERE "+where);
        if ( order !=null && order!="") sqlstr.concat(" ORDER BY "+order);
        if ( limit !=null && limit!="") sqlstr.concat(" LIMIT "+limit);
        if ( group !=null && group!="") sqlstr.concat(" GROUP BY "+group);
        logger.debug("sql ---- > " + sqlstr);
        return sqlstr;

    }

    public String sql_select(String table,String fileds){
        return sql_select(table,fileds,null,null,null,null);
    }

    public String sql_select(String table,String fileds,String where){
        return sql_select(table,fileds,where,null,null,null);
    }

    public String sql_select(String table,String fileds,String where,String order){
        return sql_select(table,fileds,where,order,null,null);
    }

    public String sql_select(String table,String fileds,String where,String order,String limit){
        return sql_select(table,fileds,where,order,limit,null);
    }

    public String sql_update(String table,String set,String where)
    {
        String sqlstr = "UPDATE "+table+" SET "+set;
        if (where!="") sqlstr.concat(" WHERE "+where);
        logger.debug("sql ---- > " + sqlstr);
        return sqlstr;
    }


    private boolean isDBOperating(DBOperating dbOperating){
        if(dbOperating != null) return true;
        return false;
    }

    public void closeSQL(){
            try {
                if(statement != null) statement.close();
            } catch (SQLException e) {
               logger.error("关闭数据库【statement】出现异常 --- " ,e );
            }
            try {
                if(rs != null) rs.close();
            } catch (SQLException e) {
                logger.error("关闭数据库 【resultset】异常  -- " ,e);
            }try {
                if(connection != null) connection.close();
            } catch (SQLException e) {
               logger.error("关闭数据库 【connection】 异常 ---- " ,e);
            }
    }



}
