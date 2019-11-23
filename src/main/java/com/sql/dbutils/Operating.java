package com.sql.dbutils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Operating {
    public ResultSet db_select(String sql,DBOperating dbOperating)  {
        if(!isDBOperating(dbOperating)) return null;
        ResultSet rs = null;
        Connection connection = null;
        try {
            connection = dbOperating.getConn();
            Statement statement = connection.createStatement();
            rs =  statement.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {

        }


        if(rs != null ) return rs;
        return null;
    }

    public int db_update(String sql,DBOperating dbOperating) throws SQLException {
        if(!isDBOperating(dbOperating)) return 0;
        Connection connection = dbOperating.getConn();
        Statement statement = connection.createStatement();
        return statement.executeUpdate(sql);
    }

    public int db_delete(String sql,DBOperating dbOperating) throws SQLException {
        if(!isDBOperating(dbOperating)) return 0;
        Connection connection = dbOperating.getConn();
        Statement statement = connection.createStatement();
        return statement.executeUpdate(sql);
    }

    public int db_inster(String sql,DBOperating dbOperating) throws SQLException {
        if(!isDBOperating(dbOperating)) return 0;
        Connection connection = dbOperating.getConn();
        Statement statement = connection.createStatement();
        return statement.executeUpdate(sql);
    }
    public String sql_inster(String table,String fileds,String values){
        String sqlstr = "INSERT INTO " + table + " ( " + fileds + ") VALUE ( " + values + ")";
        return sqlstr;

    }
    public String sql_delete(String table,String where)
    {
        String sqlstr = "DELETE FROM " + table;
        if (where!="") sqlstr  += " WHERE " +where;
        return sqlstr;
    }
    public String sql_select(String table,String fileds,String where,String order,String limit,String group){
        String sqlstr = "SELECT "+ fileds+ " FROM " + table;
        if ( where !=null && where!="") sqlstr += " WHERE "+where;
        if ( order !=null && order!="") sqlstr += " ORDER BY "+order;
        if ( limit !=null && limit!="") sqlstr += " LIMIT "+limit;
        if ( group !=null && group!="") sqlstr += " GROUP BY "+group;
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
        if (where!="") sqlstr += " WHERE "+where;
        return sqlstr;
    }


    private boolean isDBOperating(DBOperating dbOperating){
        if(dbOperating != null) return true;
        return false;
    }




}
