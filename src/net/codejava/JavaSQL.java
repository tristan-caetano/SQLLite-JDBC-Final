package net.codejava;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Scanner;

public class JavaSQL {

    public static void main(String[] args){
        //String jdbcURL = "jdbc:sqlite:/home/tcaetano/Projects/CIS454Final/autosDB.sqlite";
        accidents("jdbc:sqlite:/home/tcaetano/Projects/CIS454Final/autosDB.sqlite");
    }

    public static void accidents(String url){
        System.out.println("Please input an accident ID");
        Scanner in = new Scanner(System.in);
        int id = in.nextInt();

        try{
            Connection connection = DriverManager.getConnection(url);
            String sql = "SELECT accidents.aid, accident_date, city, state, involvements.vin, damages, involvements.driver_ssn " +
                         "FROM accidents " +
                         "JOIN involvements ON accidents.aid = involvements.aid " +
                         "WHERE accidents.aid = " + id;

            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);

            while(result.next()){
                String date  = result.getString("accident_date");
                String city  = result.getString("city");
                String state  = result.getString("state");
                String vin  = result.getString("vin");
                String damages  = result.getString("damages");
                String ssn  = result.getString("driver_ssn");

                System.out.println("Date: " + date +
                                   "\nLocation: " + city + ", " + state +
                                   "\nVin #: " + vin +
                                   "\nDamages: $" + damages +
                                   "\nDriver SSN: " + ssn);
                break;
            }

        }catch(SQLException e){
            System.out.println("An error has occured.");
            e.printStackTrace();
        }
    }
}
