package Connection;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionClientClass {
    static public Connection connection;
    private static String userName="root";
    private static String password="";
    static public Connection getConnection(){
        String dbName="user";


        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connection= DriverManager.getConnection("jdbc:mysql://localhost/"+dbName,userName,password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return connection;
    }
    static public Connection getClientConnection(){
        String dbName="product";

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connection= DriverManager.getConnection("jdbc:mysql://localhost/"+dbName,userName,password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return connection;
}
}