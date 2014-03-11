package com.db.LibraryApp.UtilityClasses;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: Rahul Nair
 */
public class MySQLConnection {
    public static Connection connection = null;

    public static Connection getConnection() throws LibraryAppException {
        try {
            if (null == connection || connection.isClosed()) {
                createConnection();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new LibraryAppException("Connection to DB failed : " + e.getMessage());
        }
        return connection;
    }

    public static void createConnection() throws LibraryAppException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "extc300132");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new LibraryAppException("Connection to DB failed : " + e.getMessage());
        }
    }

    public static void closeConnection()
    {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
