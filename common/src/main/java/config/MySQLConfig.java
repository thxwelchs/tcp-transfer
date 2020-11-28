package config;

public class MySQLConfig {
    private final String HOST = "localhost";
    private final int PORT= 3306;
    private final String USERNAME = "root";
    private final String PASSWORD = "";
    private final String DATABASE = "osci";
    private final String TIME_ZONE = "Asia/Seoul";

    public String getUsername() {
        return USERNAME;
    }

    public String getPassword() {
        return PASSWORD;
    }

    public String getJdbcURL() {
        return "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE + "?serverTimezone=" + TIME_ZONE;
    }
}
