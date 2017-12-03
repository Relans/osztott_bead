package hu.elte.osztott.service;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

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
            Connection connection = getConnection();
            if (connection != null) {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    result.add(new String[]{resultSet.getString(1), resultSet.getString(2), resultSet.getString(3)});
                }
                connection.close();
            } else {
                return getDataFromResoruces();
            }
        } catch (SQLException e) {
            System.err.println("Hiba a vasútvonalak betöltése közben!");
            e.printStackTrace();
        }
        return result;
    }

    private List<String[]> getDataFromResoruces() {
        List<String[]> result = new ArrayList<>();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("vonalak.txt");
             Scanner input = new Scanner(inputStream, "UTF-8")) {
            while (input.hasNext()) {
                String next = input.next();
                result.add(next.split(";"));
            }
        } catch (IOException err) {
            System.out.println("Hiba a vonalak.txt fájl olvasásakor");
            err.printStackTrace();
        }
        return result;
    }
}
