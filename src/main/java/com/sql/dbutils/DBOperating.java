package com.sql.dbutils;

import java.sql.Connection;
import java.sql.SQLException;

public interface DBOperating {
    public Connection getConn() throws SQLException;
    public void closeConn();
}
