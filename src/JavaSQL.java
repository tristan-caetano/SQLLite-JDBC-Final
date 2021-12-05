/*
Tristan Caetano
CIS 452
Final Project
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Scanner;

public class JavaSQL {

    public static void main(String[] args){
        String URL = "jdbc:sqlite:/home/tcaetano/Projects/CIS454Final/autosDB.sqlite";

        Scanner in = new Scanner(System.in);
        while(true) {
            System.out.println("\nPlease select value associated with function:\n" +
                               "1: Accident ID Search\n" +
                               "2: Quit\n");

            int check = in.nextInt();

            switch(check){
                case 1: { accidents(URL); break; }
                case 2: { System.exit(0); }
            }
        }
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

    public static void rangeCheck(String url){
        System.out.println("Please select the range you would like to search by:\n" +
                           "1: By Date\n" +
                           "2: By Average Damages\n" +
                           "3: By Total Damages\n");
        Scanner in = new Scanner(System.in);
        int check = in.nextInt();

        while(true){
            switch(check){
                case 1:{

                }
                case 2:{

                }
                case 3:{

                }
            }
        }


    }

    public static void rangeSearch(){
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
