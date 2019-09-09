/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaDB;

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.xml.internal.ws.util.ReadAllStream;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.StatementEvent;
import org.apache.derby.client.am.stmtcache.StatementKeyFactory;

/**
 *
 * @author Java
 */
public class JavaDB 
{
    private Properties properties;//
    private Connection connection;
    private Statement statement;
    private PreparedStatement preparedStatement;
    public JavaDB(){
        try(FileReader reader = new FileReader("javaDB.properties")){
            properties = new Properties();
            properties.load(reader);
           // Scanner sc = new Scanner();
         //  properties.load(System.in);
        }catch(Exception e){
            
        }
    //    properties.load(new InputStreamReader());
    }
    public static void main(String[] arg)
    {
        JavaDB java = new JavaDB();
        if(java.connect())
        {
        java.run();
        java.close();
        }
    }
    //обеспечение JDBC драйвера- первое
    //

    private boolean connect() 
    {
       Enumeration<Driver> enumirator = DriverManager.getDrivers(); //To change body of generated methods, choose Tools | Templates.
       Driver d;
       System.out.println("Drivers list:");
       while(enumirator.hasMoreElements())
       {
           d= enumirator.nextElement();
           System.out.println("-->"+d+new Date(System.currentTimeMillis()));
       }
       String url = properties.getProperty("url");
       String user = properties.getProperty("user");
        String password = properties.getProperty("password");
        try {
            connection = DriverManager.getConnection(url,user,password);
            System.out.println("zbs");
            preparedStatement=connection.prepareStatement(properties.getProperty("update"));
            
            
        } catch (SQLException ex) {
            System.out.println("Error #");
        }
       return true;
    }

    private void run() {
        try {
            if(connection.isValid(5)){
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            }
              String delete_table = properties.getProperty("delete_table");
             statement.execute(delete_table);
        } catch (SQLException e) {
            System.out.println("Error 10"+e.getMessage());
          
        }
        try {
            if(connection.isValid(5)){
//                statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                String create_user = properties.getProperty("create_user");
                String add_user = properties.getProperty("add_user");
//                 String delete_table = properties.getProperty("delete_table");
                
//                 statement.execute(delete_table);
                System.out.println(create_user);
               statement.executeUpdate(create_user);
              
               for (int i=1;i<4;++i){
                statement.addBatch(add_user+"\'user_"+i+"\',"+"'2019-07-"+i+"'"+")");  
                 }
               statement.executeBatch();
            //   connection.commit();
                printAll();
                updateAll(10);
                printAll();
//                delete(Date date);
                 delete(new Date(System.currentTimeMillis()));
                printAll(0);
            }} catch (SQLException ex) {
            System.out.println("Error #4run:" +ex.getMessage());
        }
       
    }
    private void printAll(){
        try{
            ResultSet result = statement.executeQuery(properties.getProperty("get_all"));
            while(result.next()){
                System.out.println(result.getString(1)+" "+result.getString(2));
            }
            
        }catch(SQLException e){
            System.out.println("Error #5:"+e.getMessage());
        }
    }
    private void printAll(int limit){
        try {
            ResultSet resultSet = statement.executeQuery(properties.getProperty("get_all"));
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount();
            StringBuilder stringBuilder = new StringBuilder("Table\n------\n");
            for (int i =1;i<=columnCount;++i){
                stringBuilder.append(resultSetMetaData.getColumnName(i)).append("\t");
                
               
            }
            int[] t = new int[columnCount];
            stringBuilder.append("\n");
            for (int i =1;i<=columnCount;i++){
                stringBuilder.append(resultSetMetaData.getColumnTypeName(i)).append("\t");
                t[i-1] = resultSetMetaData.getColumnType(i);
            }
            while(resultSet.next()){
                for(int i =1;i<=columnCount;i++){
                    switch(t[i-1]){
                        case Types.DATE:
                            stringBuilder.append(resultSet.getDate(i)).append("\t");
                            break;
                        case Types.INTEGER:
                           stringBuilder.append(resultSet.getInt(i)).append("\t");
                            break;
                           case Types.VARCHAR:
                           stringBuilder.append(resultSet.getString(i)).append("\t");
                            break;
                    }
                }
                stringBuilder.append("\n");
            }
             System.out.print(stringBuilder);
        } catch (SQLException e) {
            System.out.println("Error #8"+e.getMessage());
            
            
        }
    }
    private void updateAll(){
        try{
             ResultSet result = statement.executeQuery(properties.getProperty("get_all"));
             result.afterLast();
             while(result.previous()){
                 result.updateDate("register",new Date(10000000));
                 result.updateRow();
             }
    }catch(SQLException e){
            System.out.println("Error #6:"+e.getMessage());
        }
}
    private void close() {
        try {
            if(connection.isValid(5)){
            connection.close();
            }
        } catch (SQLException ex) {
            System.out.println("Error #3:" +ex.getMessage());
                 
        }
        
    }
    private int delete(Date date){//можно еще удалить по SQL запросу.
         String delete = properties.getProperty("delete");
        try {
            statement.executeUpdate(delete+"'"+date.toString()+"'");
        } catch (SQLException ex) {
            System.out.println("Error #7");;
        }
//        try{
//        ResultSet result = statement.executeQuery(properties.getProperty("get_all"));
//            while(result.next()){
//                if (result.getString(2).toString().equals(date.toString())){
//                result.deleteRow();//удаляем столбец
//                }
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(JavaDB.class.getName()).log(Level.SEVERE, null, ex);
//        }
        return 1;
        
    }
     private void updateAll(int limit){
         long delta = 86400_000;
         try {
              ResultSet result = statement.executeQuery(properties.getProperty("get_all"));
              long t = System.currentTimeMillis();
             for(int i =0;i<limit;i++){
                 if(result.next()){
                        preparedStatement.setString(2, result.getString(1));
                        preparedStatement.setDate(1, new Date(t+delta*i));
                        preparedStatement.addBatch();
                        System.out.println("e2");
                 }else break;
         
                         
                     }
             preparedStatement.executeBatch();
         } catch (SQLException e) {
             System.out.println("Error 11"+e.getMessage());
         }
 
     }
}
