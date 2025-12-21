package vn.bluemoon.util;

import vn.bluemoon.config.DbConfig;
import vn.bluemoon.exception.DbException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class for JDBC operations
 */
public class JdbcUtils {
    private static final DbConfig dbConfig = DbConfig.getInstance();

    /**
     * Get database connection
     * @return Connection object
     * @throws DbException if connection fails
     */
    public static Connection getConnection() throws DbException {
        try {
            Class.forName(dbConfig.getDriver());
            return DriverManager.getConnection(
                dbConfig.getUrl(),
                dbConfig.getUsername(),
                dbConfig.getPassword()
            );
        } catch (ClassNotFoundException e) {
            throw new DbException("Database driver not found: " + dbConfig.getDriver(), e);
        } catch (SQLException e) {
            throw new DbException("Failed to connect to database: " + e.getMessage(), e);
        }
    }

    /**
     * Close connection safely
     * @param connection Connection to close
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                AppLogger.error("Error closing connection", e);
            }
        }
    }
}


