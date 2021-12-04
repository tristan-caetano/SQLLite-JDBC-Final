package net.codejava;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class JavaSQL {

    public static void main(String[] args){
        String jdbcURL = "jdbc:sqlite:/home/tcaetano/Projects/CIS454Final/autosDB.sqlite";
        try{
            Connection connection = DriverManager.getConnection(jdbcURL);
            //String sql = "SELECT * FROM people";
            String sql = "SELECT * FROM people";


            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);

            while(result.next()){
                String fname  = result.getString("fname");
                String lname  = result.getString("lname");

                System.out.println(fname + "|" + lname);
            }

        }catch(SQLException e){
            System.out.println("An error has occured.");
            e.printStackTrace();
        }
    }
}
