/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataAccess;
import java.sql.*;
/**
 *
 * @author carlo
 */
public class Data {
    
    
    private static Connection connection;
    
    private static void buildConnection() throws Exception{
       
        
        try{
            Class.forName("org.postgresql.Driver");
            
            String url = "jdbc:postgresql://localhost:5432/notas";
            String user = "postgres";
            String pass = "rm11048";

            connection = DriverManager.getConnection(url,user,pass);
        }catch(ClassNotFoundException e){
            throw new Exception(e.getMessage());
        }
        catch (SQLException e) {
            throw new Exception(e.getMessage());
        }
    }
    
    public static int persist(String sqlSentence) throws Exception{
        int rows = -1;
        Statement stt = null;
        
        try{
            
            buildConnection();
            stt = connection.createStatement();
            rows = stt.executeUpdate(sqlSentence);
            
        }catch(Exception ex){
            
            throw new Exception(ex.getMessage());
        
        }
        finally{
            if(!connection.isClosed()){
                connection.close();
                //stt.close();               
            }
        }
        
        return rows;
    }
        
    public static ResultSet get(String sqlSentence) throws Exception{
        
        ResultSet result = null;
        Statement S = null;
        
        try {
            
            buildConnection();
            S = connection.createStatement();
            
            result = S.executeQuery(sqlSentence);
            
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
        finally{
            if(!connection.isClosed()){
                connection.close();
                //S.close();
                //result.close();
            }
        }
        
        return result;
    }
       
    
}
