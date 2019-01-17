package ru.project.server.storage;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBHelper {

    public static final String DB_HOST = "jdbc:mysql://localhost:3306/dbname?characterEncoding=unf8&autoreconnect=true";
    public static final String DB_USER = "team5_rest";
    public static final String DB_PASSWORD = "team5_work";
    private static final int MAX_OPEN_PREPARED_STATEMENTS = 100;
    private static DBHelper s_dbHelper;
    public static final String DB_DRIVER = "com.mysql.jdbc.Driver";


    private final BasicDataSource mBasicDataSource;

    private DBHelper() {
        mBasicDataSource = new BasicDataSource();
        mBasicDataSource.setDriverClassName(DB_DRIVER);
        mBasicDataSource.setUsername(DB_USER);
        mBasicDataSource.setPassword(DB_PASSWORD);
        mBasicDataSource.setUrl(DB_HOST);

        mBasicDataSource.setMinIdle(4);
        mBasicDataSource.setMaxIdle(10);
        mBasicDataSource.setMaxOpenPreparedStatements(MAX_OPEN_PREPARED_STATEMENTS);

        try (Connection connection = getConnection()) {
            runUpdate(connection, "SET sql_mode='NO_UNSIGNED_SUBTRACTION'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static DBHelper getInstance() {
        DBHelper localInstance = s_dbHelper;
        if (localInstance == null) {
            synchronized (DBHelper.class) {
                localInstance = s_dbHelper;
                if (localInstance == null)
                    s_dbHelper = localInstance = new DBHelper();
            }
        }
        return localInstance;
    }

    public Connection getConnection() throws SQLException {
        return mBasicDataSource.getConnection();
    }

    public int runUpdate(Connection connection, String sql) throws SQLException {
        int updated = 0;
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
            updated = statement.getUpdateCount();
        } finally {
            connection.close();
        }
        return updated;
    }

    public void runQuery(Connection connection, String sql, ResultHandlet resultHandlet) throws SQLException {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            resultHandlet.handle(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }
}
