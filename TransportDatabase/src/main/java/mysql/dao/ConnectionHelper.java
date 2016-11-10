package mysql.dao;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by yuminchen on 16/11/10.
 */
public class ConnectionHelper {
    private static ConnectionHelper connectionHelper;

    private HikariDataSource ds;

    private ConnectionHelper() {
        ds = new HikariDataSource();
        ds.setJdbcUrl("jdbc:mysql://localhost:8889/transport?useUnicode=true&characterEncoding=utf-8");
        ds.setUsername("root");
        ds.setPassword("root");
        ds.setMaximumPoolSize(50);
    }

    public static ConnectionHelper getInstance(){
        if(connectionHelper==null){
            connectionHelper = new ConnectionHelper();
        }
        return connectionHelper;
    }



    public Connection getConnection(){
        try {
            return ds.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.err.println("get connection fail");
        return null;
    }
}
