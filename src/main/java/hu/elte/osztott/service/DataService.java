package hu.elte.osztott.service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DataService {

    private final String userName = "ntgg4v";
    private final String password = "ntgg4v";
    private final String dbName = "aramis.inf.elte.hu";
    private final String serviceName = "eszakigrid97";

    private Connection getConnection() {
        Connection conn = null;
        Properties connectionProps = new Properties();
        connectionProps.put("user", this.userName);
        connectionProps.put("password", this.password);

        try {
            conn = DriverManager.getConnection(
                    "jdbc:oracle:thin:@//" +
                            this.dbName +
                            ":1521/" +
                            this.serviceName,
                    connectionProps);
        } catch (SQLException e) {
            System.err.println("Hiba a kapcsolat létrehozása közben!");
            e.printStackTrace();
        }
        return conn;
    }

    public List<String[]> getData() {
        List<String[]> result = new ArrayList<>();
        try {
            final String sql = "SELECT id,honnan,hova FROM vasut";
            PreparedStatement preparedStatement = getConnection().prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                result.add(new String[]{resultSet.getString(1), resultSet.getString(2), resultSet.getString(3)});
            }
        } catch (SQLException e) {
            System.err.println("Hiba a vasútvonalak betöltése közben!");
            e.printStackTrace();
        }
        return result;
    }
}
