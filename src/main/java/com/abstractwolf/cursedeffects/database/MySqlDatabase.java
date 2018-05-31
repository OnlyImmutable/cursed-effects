package com.abstractwolf.cursedeffects.database;

import com.abstractwolf.cursedeffects.utils.Callback;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySqlDatabase {

    private HikariDataSource connectionPool;
    private String host, database, user, pass;
    private int port;

    /**
     * Connect to the repository.. (MySQL)
     * @param host - the host ip address to connect with.
     * @param port - the port to connect with.
     * @param database - the database.
     * @param user - the username.
     * @param pass - the password.
     */
    public MySqlDatabase(String host, int port, String database, String user, String pass) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.user = user;
        this.pass = pass;
    }

    /**
     * Connect to the repository.. (MySQL)
     */
    public void openConnection() {

        try {

            connectionPool = new HikariDataSource();
            connectionPool.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
            connectionPool.addDataSourceProperty("serverName", host);
            connectionPool.addDataSourceProperty("port", port);
            connectionPool.addDataSourceProperty("databaseName", database);
            connectionPool.addDataSourceProperty("user", user);
            connectionPool.addDataSourceProperty("password", pass);
            connectionPool.setConnectionTimeout(3000);
            connectionPool.setValidationTimeout(1000);
//            connectionPool.setMaximumPoolSize(20);

            System.out.println("Waiting for confirmation of connection.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("There was an issue connecting to the database, please check your credentials and try again!");
            Bukkit.getServer().shutdown();
        }
    }

    /**
     * Send queries to the database (Prepared Statement).
     * @param query - the query.
     * @param callback - the callback to get the query after execution.
     */
    public void sendPreparedStatement(String query, boolean update, boolean async, Callback<PreparedStatement> callback) {

        if (connectionPool == null || connectionPool.isClosed()) {
            openConnection();
        }

        if (async) {

            new Thread(() -> {
                Connection connection;
                PreparedStatement statement;

                try {
                    connection = connectionPool.getConnection();
                    statement = connection.prepareStatement(query);

                    if (update) {
                        statement.executeUpdate();
                    } else {
                        statement.execute();
                    }

                    callback.call(statement);
                } catch (SQLException e) {
                    e.printStackTrace();
                    callback.call(null);
                }
            }).start();
        } else {

            Connection connection;
            PreparedStatement statement;

            try {
                connection = connectionPool.getConnection();
                statement = connection.prepareStatement(query);

                if (update) {
                    statement.executeUpdate();
                } else {
                    statement.execute();
                }

                callback.call(statement);
            } catch (SQLException e) {
                e.printStackTrace();
                callback.call(null);
            }
        }
    }

    /**
     * Check if the connection pool is closed.
     * @return isClosed - if the pool is closed.
     */
    public boolean isClosed() { return connectionPool.isClosed(); }

    /**
     * Close Connection Pool
     */
    public void closePool() {
        try {
            connectionPool.evictConnection(connectionPool.getConnection());
            connectionPool.close();
        } catch (Exception e) {
            /* Ignored */
        }
    }
}
