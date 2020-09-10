package basicClasses;

import Connector.ConnectionClass;
import identification.identificationController;


import java.sql.*;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class user {


    private static String fullName;
    private static String email;
    private static String phoneNumber, password,shopName;
    private static int userID ;

    public static int getUserID() {
        return userID;
    }

    public static void setUserID(int userID) {
        user.userID = userID;
    }

    public user() {
    }
    
    public static String getShopName() throws SQLException {
        Connection connection = ConnectionClass.getConnection();
        String sql = "SELECT shopName FROM logins where email= ? and password= ?  ";//Query
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1,getEmail());
        preparedStatement.setString(2,getPassword());
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) return resultSet.getString("shopName");
        else return "";}

    public static void setShopName(String shopName) throws SQLException {
        Connection connection = ConnectionClass.getConnection();
        String sql = "Update logins set shopName=? where email=? and password=? " ;
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1,shopName);
        preparedStatement.setString(2,getEmail());
        preparedStatement.setString(3,getPassword());
        preparedStatement.executeUpdate();

    }


    public static String getFullName() throws SQLException {
        Connection connection = ConnectionClass.getConnection();
        String sql = "SELECT * FROM logins where email=? and password=?";//Query
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, getEmail());
        preparedStatement.setString(2, getPassword());
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            return resultSet.getString("nom");
        }
        else return "";
    }



    public static void setFullName(String fullName) throws SQLException {
        Connection connection = ConnectionClass.getConnection();
        String sql = "Update logins set nom=? where email=? and password=? " ;
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1,fullName);
        preparedStatement.setString(2,getEmail());
        preparedStatement.setString(3,getPassword());
        preparedStatement.executeUpdate();
    }

    public static String getEmail() {
        return identificationController.getEmailFromLogin();
    }

    public static void setEmail(String email)  {
        Connection connection = ConnectionClass.getConnection();
        String sql = "Update logins set email=? where shopName=? and password=? " ;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,email);
            preparedStatement.setString(2,getShopName());
            preparedStatement.setString(3,getPassword());
            preparedStatement.executeUpdate();
            identificationController.setEmailFromLogin(email);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static String getPhoneNumber() throws SQLException {
        Connection connection = ConnectionClass.getConnection();
        String sql = "SELECT * FROM logins where email= ? and password= ?  ";//Query
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, getEmail());
        preparedStatement.setString(2, getPassword());
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) return resultSet.getString("phoneNumber");
        else return "";
    }

    public static void setPhoneNumber(String phoneNumber) throws SQLException {
        Connection connection = ConnectionClass.getConnection();
        String sql = "Update logins set phoneNumber=? where email=? and password=? " ;
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1,phoneNumber);
        preparedStatement.setString(2,getEmail());
        preparedStatement.setString(3,getPassword());
        preparedStatement.executeUpdate();
    }

    public static String getPassword() {
        return identificationController.getPasswordFromLogin();
    }

    public static void setPassword(String password)  {
        Connection connection = ConnectionClass.getConnection();
        String sql = "Update logins set password=? where email=? and shopName=? " ;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,password);
            preparedStatement.setString(2,getEmail());
            preparedStatement.setString(3,getShopName());
            preparedStatement.executeUpdate();
            identificationController.setPasswordFromLogin(password);
        } catch (SQLException e) {
            e.printStackTrace();
        }



    }

}

