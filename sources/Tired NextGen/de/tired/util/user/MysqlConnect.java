package de.tired.util.user;


import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MysqlConnect {


    static final String DB_URL = "jdbc:mysql://localhost:3306/tirednextgen";
    static final String USER = "root";
    static final String PASS = "admin";

    public static Connection ConnectDB() {
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            return conn;

        } catch (Exception ex) {

            System.out.println("There were errors while connecting to Database.");

            return null;
        }
    }

    public static void main(String[] args) {
    }

    public static void userRegister(String name, String email, String password, boolean premium) {
        //To change body of generated methods, choose Tools | Templates.
        Connection dbconn = ConnectDB();
        if (dbconn != null) {
            try {
                Statement stmt = dbconn.createStatement();

                String sql = "INSERT INTO tiredusers(username, email, password, premium)" + "VALUES (?, ?, ?, ?)";
                PreparedStatement preparedStatement = dbconn.prepareStatement(sql);

                preparedStatement.setString(1, name);
                preparedStatement.setString(2, email);
                preparedStatement.setString(3, password);
                preparedStatement.setBoolean(4, false);
                System.out.println(preparedStatement.executeUpdate());
                stmt.close();
                dbconn.close();

            } catch (SQLException ex) {
                Logger.getLogger(MysqlConnect.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {

            System.out.println("The Connection Not Available.");
        }

    }

    public static User userLogin(String email, String password) {
        User user = null;


        // Establishing the connection
        try {
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            // Connected to database successfully

            // SQL query for getting user
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM tiredusers WHERE email=? and password=?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            // Executing the query
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                user = new User(resultSet.getString("Username"), resultSet.getString("Email"), resultSet.getString("Password"));
            }

            // Closing the connection
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

}
