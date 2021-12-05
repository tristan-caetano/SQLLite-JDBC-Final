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
                               "2: Range Search\n" +
                               "3: Quit\n");

            int check = in.nextInt();

            switch(check){
                case 1: { accidents(URL); break; }
                case 2: { rangeCheck(URL); break;}
                case 3: { System.exit(0); }
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
        boolean isDone[] = {true, true, true};
        String query = "WHERE ";

        while(true) {
            System.out.println("Please select the range you would like to search by:\n" +
                    "1: By Date\n" +
                    "2: By Average Damages\n" +
                    "3: By Total Damages\n" +
                    "4: Submit Query\n" +
                    "5: Go Back\n");

            Scanner in = new Scanner(System.in);
            int check = in.nextInt();

            String val1;
            String val2;
            in.nextLine();

            switch (check) {
                case 1: {
                    if (isDone[1] == false || isDone[2] == false) {
                        query = query + " AND ";
                    }

                    if (isDone[0]) {
                        System.out.println("Please input the first date: \n");
                        val1 = in.nextLine();
                        System.out.println("Please input the second date: \n");
                        val2 = in.nextLine();
                        query = query + ("accident_date BETWEEN " + "'" + val1 + "'" + " AND " + "'" + val2 + "'");
                    } else {
                        System.out.println("Dates have already been set, going back to previous menu.");
                    }
                    isDone[0] = false;
                    break;

                }
                case 2: {
                    if (isDone[0] == false || isDone[2] == false) {
                        query = query + " AND ";
                    }

                    if (isDone[1]) {
                        System.out.println("Please input the first value: \n");
                        val1 = in.nextLine();
                        System.out.println("Please input the second value: \n");
                        val2 = in.nextLine();
                        query = query + ("damages BETWEEN " + "'" + val1 + "'" + " AND " + "'" + val2 + "'");
                    } else {
                        System.out.println("Damage values have already been set, going back to previous menu.");
                        break;
                    }
                    isDone[1] = false;
                    break;
                }
                case 3: {
                    if (isDone[0] == false || isDone[1] == false) {
                        query = query + " AND ";
                    }
                    break;
                }
                case 4: {
                    rangeSearch(query, url);
                    return;
                }
                case 5: {
                    return;
                }
                default: {
                    break;
                }
            }
        }
    }

    public static void rangeSearch(String query, String url){
        try{
            Connection connection = DriverManager.getConnection(url);
            String sql = "SELECT DISTINCT accidents.aid, accident_date, city, state " +
                    "FROM accidents " +
                    "JOIN involvements ON accidents.aid = involvements.aid " + query;

            System.out.println("\n" + sql + "\n");

            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);

            while(result.next()){
                String aid  = result.getString("aid");
                String date  = result.getString("accident_date");
                String city  = result.getString("city");
                String state  = result.getString("state");

                System.out.println("\nAccident ID: " + aid +
                        "\nDate: " + date +
                        "\nLocation: " + city + ", " + state);
            }

        }catch(SQLException e){
            System.out.println("An error has occured.");
            e.printStackTrace();
        }
    }
}
