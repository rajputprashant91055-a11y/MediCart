package database;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

    private static String url = "jdbc:mysql://localhost:3306/medicart_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static String user = "medicart_app";
    private static String password = "MediCartApp2026!";

    static {
        // Explicitly load MySQL JDBC driver (works even when SPI/ServiceLoader fails in VS Code)
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("WARNING: MySQL JDBC driver not found on classpath!");
            System.err.println("Make sure lib/mysql-connector-j-26.7.0.jar is included.");
        }
        loadConfig();
    }

    private static void loadConfig() {
        Properties props = new Properties();
        InputStream in = null;
        try {
            // Check classpath first
            in = DBConnection.class.getResourceAsStream("/database/db.properties");
            if (in == null) {
                in = DBConnection.class.getResourceAsStream("/db.properties");
            }
            // Check filesystem relative paths
            if (in == null) {
                File file = new File("database/db.properties");
                if (file.exists()) {
                    in = new FileInputStream(file);
                } else {
                    file = new File("db.properties");
                    if (file.exists()) {
                        in = new FileInputStream(file);
                    }
                }
            }

            if (in != null) {
                try (InputStream is = in) {
                    props.load(is);
                    if (props.getProperty("db.url") != null) {
                        url = props.getProperty("db.url").trim();
                    }
                    if (props.getProperty("db.user") != null) {
                        user = props.getProperty("db.user").trim();
                    }
                    if (props.getProperty("db.password") != null) {
                        password = props.getProperty("db.password").trim();
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Notice: Could not load db.properties, using defaults: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public static void main(String[] args) {
        try (Connection connection = getConnection()) {
            System.out.println("MediCart database connected successfully!");
            System.out.println("Database Product: " + connection.getMetaData().getDatabaseProductName());
            System.out.println("Database Version: " + connection.getMetaData().getDatabaseProductVersion());
        } catch (SQLException e) {
            System.err.println("Database connection failed:");
            e.printStackTrace();
        }
    }
}
