/*
Tristan Caetano
CIS 452
Final Project
 */

// Imported Libraries
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Scanner;

public class  JavaSQL {

    // Contains url location of database, and asks user what function they would like to run
    public static void main(String[] args){
        String URL = "jdbc:sqlite:autosDB.sqlite";

        // Main Menu
        Scanner in = new Scanner(System.in);
        while(true) {
            System.out.println("\nPlease input value associated with function:\n" +
                               "1: Accident ID Search\n" +
                               "2: Range Search\n" +
                               "3: Add Accident\n" +
                               "4: Quit\n");

            int check = in.nextInt();

            switch(check){
                case 1: { accidents(URL); break; }
                case 2: { rangeCheck(URL); break;}
                case 3: { addAccident(URL); break; }
                case 4: { System.exit(0); }
                default: { break; }
            }
        }
    }

    // Searching by aid
    public static void accidents(String url){

        // Asks for accident ID
        System.out.println("Please input an accident ID");
        Scanner in = new Scanner(System.in);
        int id = in.nextInt();

        // Prints out information relative to the accident ID
        try{

            // Queries Database to acquire info
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

    // Menu that allows the user to search by date, average, or total damages individually, or any combination of the three
    public static void rangeCheck(String url){

        // Declaring values
        boolean isDone[] = {true, true, true};
        String query = "";
        String query2 = "";
        String addSelect = "";

        // Search Menu
        while(true) {

            // Menu
            System.out.println("Please select the range you would like to search by:\n" +
                    "1: By Date\n" +
                    "2: By Average Damages\n" +
                    "3: By Total Damages\n" +
                    "4: Submit Query\n" +
                    "5: Go Back\n");

            Scanner in = new Scanner(System.in);
            int check = in.nextInt();

            // Declaring values
            String val1;
            String val2;

            // Clearing Scanner
            in.nextLine();

            switch (check) {

                // Searching by date
                case 1: {
                    if (isDone[0]) {

                        if (isDone[1] == false || isDone[2] == false) {
                            query = query + " AND ";
                        }

                        System.out.println("Please input the first date (yyyy-mm-dd): \n");
                        val1 = in.nextLine();
                        System.out.println("Please input the second date (yyyy-mm-dd): \n");
                        val2 = in.nextLine();
                        query = query + ("accident_date BETWEEN " + "'" + val1 + "'" + " AND " + "'" + val2 + "'");
                    } else {
                        System.out.println("Dates have already been set, going back to previous menu.");
                    }
                    isDone[0] = false;
                    break;

                }

                // Searching by average damages
                case 2: {
                    if (isDone[1]) {

                        if (isDone[0] == false || isDone[2] == false) {
                            query = query + " AND ";
                        }

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

                // Searching by total damages
                case 3: {
                    if (isDone[2]) {

                        System.out.println("Please input the first value: \n");
                        val1 = in.nextLine();
                        System.out.println("Please input the second value: \n");
                        val2 = in.nextLine();
                        addSelect = " , SUM(involvements.damages) as total ";
                        query2 = "GROUP BY involvements.aid " +
                                 "HAVING total BETWEEN " + val1 + " AND " + val2;
                    } else {
                        System.out.println("Damage values have already been set, going back to previous menu.");
                        break;
                    }
                    isDone[2] = false;
                    break;
                }

                // Submitting query
                case 4: {
                    if (!(isDone[0]) || !(isDone[1])) {
                        query = "WHERE " + query;
                    }
                    System.out.println((!(isDone[0]) || !(isDone[1])));
                    rangeSearch(query, query2, addSelect, url);
                    return;
                }

                // Go back to previous menu
                case 5: {
                    return;
                }
                default: {
                    break;
                }
            }
        }
    }

    // Executing Search Query for multiple searches if applicable
    public static void rangeSearch(String query, String query2, String addSelect, String url){
        try{
            Connection connection = DriverManager.getConnection(url);
            String sql = "SELECT DISTINCT accidents.aid, accident_date, city, state " + addSelect +
                    "FROM accidents " +
                    "JOIN involvements ON accidents.aid = involvements.aid " +
                    query + " " + query2;

            // Printing out entire SQL query
            System.out.println("\n" + sql + "\n");

            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);

            // Printing Results
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

    // Searching by aid
    public static void addAccident(String url){

        while(true) {

            // Menu
            System.out.println("Please select what you would like to add:\n" +
                    "1: Add Accident\n" +
                    "2: Add Involvements\n" +
                    "3: Go Back\n");

            Scanner in = new Scanner(System.in);
            int check = in.nextInt();
            in.nextLine();

            // Initializing variables
            int aid = 0;

            String date = "";
            String city = "";
            String state = "";

            String vin = "";
            int damages = 0;
            String ssn = "";

            switch(check){

                // Adding an accident to the database
                case 1:{

                    // Asking user for accident details
                    System.out.println("Please input the date of the accident occurrence (yyyy-mm-dd):\n");
                    date = in.nextLine();

                    System.out.println("Please input the city of the accident occurrence.\nPlease use all capital letters, ie BOSTON:\n");
                    city = in.nextLine();

                    System.out.println("Please input the abbreviated state name of the accident occurrence.\nPlease use all capital letters, ie MA for Massachusetts:\n");
                    state = in.nextLine();

                    // Executing query to make changes to database
                    try{
                        Connection connection = DriverManager.getConnection(url);

                        String sql = "SELECT MAX(aid) AS max_aid FROM accidents";

                        Statement statement = connection.createStatement();
                        ResultSet result = statement.executeQuery(sql);

                        while(result.next()){
                            aid  = result.getInt("max_aid");
                            break;
                        }

                        sql =   "INSERT INTO accidents (aid, accident_date, city, state) " +
                                "VALUES ('" + (aid + 1) + "' , '" + date + "' , '" + city + "' , '" + state + "')";

                        System.out.println(sql);

                        statement = connection.createStatement();
                        statement.executeUpdate(sql);

                    }catch(SQLException e){
                        System.out.println("An error has occured.");
                        e.printStackTrace();
                    }

                }

                // Adding involvements to database
                case 2: {
                    String validate = "";

                    while(true) {

                        // Asking user for the aid associated with the involvements
                        System.out.println("If multiple vehicles are involved, just run this function for each vehicle, and make sure to input the correct accident ID each time.\n");

                        System.out.println("Please input the accident ID:\n");
                        aid = in.nextInt();

                        // Executing query to show accident info associated with aid so that the user can verify that the correct aid was input
                        try {
                            Connection connection = DriverManager.getConnection(url);

                            String sql = "SELECT aid, accident_date, city, state FROM accidents WHERE aid = " + aid;

                            Statement statement = connection.createStatement();
                            ResultSet result = statement.executeQuery(sql);

                            // Printing Results
                            while (result.next()) {
                                String aidT = result.getString("aid");
                                String dateT = result.getString("accident_date");
                                String cityT = result.getString("city");
                                String stateT = result.getString("state");

                                System.out.println("\nAccident ID: " + aidT +
                                        "\nDate: " + dateT +
                                        "\nLocation: " + cityT + ", " + stateT);
                            }
                        } catch (SQLException e) {
                            System.out.println("An error has occured.");
                            e.printStackTrace();
                        }

                        in.nextLine();

                        // Asking user to validate aid
                        System.out.println("\n\nIs this the correct accident?\n\t1: Yes\n\tAnything else: No");
                        validate = in.nextLine();
                        if(validate.contains("1")){
                            break;
                        }
                    }

                    // Asking user to input info associated with involvement
                    System.out.println("Please input the vin of the involved vehicle:\n");
                    vin = in.nextLine();

                    System.out.println("Please input the damage amount sustained of the involved vehicle:\n");
                    damages = in.nextInt();
                    in.nextLine();

                    System.out.println("Please input the social security number of the driver of the involved vehicle (123-45-6789):\n");
                    ssn = in.nextLine();

                    // Executing query to submit to database
                    try{
                        Connection connection = DriverManager.getConnection(url);

                        String sql =   "INSERT INTO involvements (aid, vin, damages, driver_ssn) " +
                                "VALUES ('" + aid + "' , '" + vin + "' , '" + damages + "' , '" + ssn + "')";

                        System.out.println(sql);

                        Statement statement = connection.createStatement();
                        statement.executeUpdate(sql);

                    }catch(SQLException e){
                        System.out.println("An error has occured.");
                        e.printStackTrace();
                    }
                }

                // Going back to previous menu
                case 3: { return; }
                default: { break; }
            }
        }
    }
}