package database;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final Dotenv DOTENV = Dotenv.load();

    private DatabaseConnection() {
    }

    public static Connection getConnection() throws SQLException {
        String host = DOTENV.get("DATABASE_HOST", "localhost");
        String port = DOTENV.get("DATABASE_PORT", "3306");
        String database = DOTENV.get("DATABASE_NAME");
        String user = DOTENV.get("DATABASE_USER");
        String password = DOTENV.get("DATABASE_PASSWORD");

        String url = String.format("jdbc:mariadb://%s:%s/%s", host, port, database);
        return DriverManager.getConnection(url, user, password);
    }
}