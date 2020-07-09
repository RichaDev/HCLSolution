package solution.two.system.design;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseImpl {

    private String hostname = "";
    private String port = "";
    private String username = "";
    private String password = "";
    private String driverName = "com.mysql.jdbc.Driver";
    private String serviceName = "";

    public Connection getConnection() {
        String url = "jdbc:mysql://" + hostname + ":" + port + "/" + serviceName;
        Connection conn = null;

        try {
            Class.forName(driverName);
            conn = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                conn.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }

        return conn;
    }

}

