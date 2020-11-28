package config;

import java.sql.*;

public class MySQLConnector {
    private static final String MYSQL_DRIVER = "com.mysql.cj.jdbc.Driver";
    private MySQLConfig mySQLConfig;
    private Connection connection;
    public MySQLConnector() {
        try {
            this.mySQLConfig = new MySQLConfig();
            Class.forName(MYSQL_DRIVER);
            connect(this.mySQLConfig);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public MySQLConnector(MySQLConfig mySQLConfig) {
        this.mySQLConfig = mySQLConfig;
        connect(this.mySQLConfig);
    }

    public void connect(MySQLConfig mySQLConfig) {
        try {
            Class.forName(MYSQL_DRIVER);
            synchronized (DriverManager.class) {
                connection = DriverManager.getConnection(mySQLConfig.getJdbcURL(), mySQLConfig.getUsername(), mySQLConfig.getPassword());
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        try {
            if(connection == null || connection.isClosed()) {
                connect(new MySQLConfig());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
    
    public void closeConnection() {
        try {
            if(connection == null) return;
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void closeConnection(PreparedStatement pstmt) {
        try {
            if(pstmt != null) pstmt.close();
            if(connection != null) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection(PreparedStatement pstmt, ResultSet rs) {
        try {
            if(pstmt != null) pstmt.close();
            if(connection != null) connection.close();
            if(rs != null) rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
