package hei.school.prog3.config;

import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//@Configuration
@Component
public class DbConnection {
    private final static int defaultPort = 5432;
    private final static String host = System.getenv("dbHost");
    private final static String user = System.getenv("dbUsername");
    private final static String password = System.getenv("dbPassword");
    private final static String dbName = System.getenv("dbName");
    private final String url;

    public DbConnection() {
        url = "jdbc:postgresql://" + host + ":" + defaultPort + "/" + dbName;
    }

    public Connection getConnection() {
        try{
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

// variables d'environnement: dbHost=localhost;dbName=final_td_pairwork;dbPassword=2002;dbUrl=jdbc:postgresql://localhost:5432/final_td_pairwork;dbUsername=mihajatiana