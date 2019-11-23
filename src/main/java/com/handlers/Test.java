package com.handlers;

import com.annotations.Admin_entity;
import com.annotations.Requsetmapping;
import com.core.upfile.upfileprocess.UpFileOperating;
import com.google.gson.Gson;
import com.sql.dbutils.Operating;
import com.sql.dbutils.SQL_DBUtil;
import com.unit.ServletContextinterface;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Requsetmapping({"/api/test","/api/aaa","/api/ttt"})
public class Test implements ServletContextinterface {
    HttpServletResponse httpServletResponse = null;
    HttpServletRequest httpServletRequest = null;
    Operating operating = new Operating();
    @Requsetmapping("/ccc")
    public String aaa(){
//        ActionContext ac = ActionContext.getContext();
//        HttpServletRequest request =(HttpServletRequest)ac.get(ServletActionContext.HTTP_REQUEST);
        System.out.println("success");
        return "success";
    }
    @Requsetmapping("/upfile")
    public String upfile(){
        Map<String,Object> fileinfo =(Map<String, Object>) httpServletRequest.getAttribute("fileinfo");
        httpServletResponse.setContentType("application/json");
        byte[] filecontent = (byte[]) fileinfo.get("filecontent");
        String filename =  (String) fileinfo.get("filename");
        boolean a = false ;
        try {
             a = new UpFileOperating(100000).move_uploaded(filecontent,filename,"D://");
            System.out.println(a);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "{\"rst\":"+a+"}";
    }
    @Requsetmapping("/test")
    public String test(){
        testSel();
        return "success";
    }
    @Requsetmapping("/getall")
    public String getAllarticle() throws SQLException {
        Gson gson = new Gson();
        Admin_entity admin_entity = null;
        List<Admin_entity> ret = new ArrayList<>();
        ResultSet rs = operating.db_select(operating.sql_select("admin", "id,username,password", "", "", ""), new SQL_DBUtil("myforum"));
        while(rs.next()) {
            admin_entity = new Admin_entity();
            admin_entity.setId(rs.getInt("id"));
            admin_entity.setUsername(rs.getString("username"));
            admin_entity.setPassword(rs.getString("password"));
            ret.add(admin_entity);
        }
        this.httpServletResponse.setContentType("application/json");
        return gson.toJson(ret);
    }
    @Requsetmapping("/getarticlehotpage")
    public String getArticleHotPage(){

        return null;
    }
    @Requsetmapping("/dada")
    public String dawd() {
        System.out.println(httpServletRequest);
        System.out.println(httpServletRequest.getAttribute("params"));
        testSel();
        return "success";
    }
        public void testSel(){
            ResultSet rs = null;
        try {
            rs = operating.db_select(operating.sql_select("account","id,name,password","","",""),new SQL_DBUtil("testone"));
            while (rs.next())
            {
                System.out.println(rs.getString(1) + "," +rs.getString(2)+ "," +rs.getString(3));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if(rs!=null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void setRequest(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    @Override
    public void setResponse(HttpServletResponse httpServletResponse) {
        this.httpServletResponse = httpServletResponse;
    }
}
