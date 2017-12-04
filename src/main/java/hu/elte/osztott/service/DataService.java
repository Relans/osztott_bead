package hu.elte.osztott.service;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.*;

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
        //insertNewData();
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

    private void insertNewData() {
        Random random = new Random(0);
        List<String> result = new ArrayList<>();
        List<String[]> result2 = new ArrayList<>();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("v2.txt");
             Scanner input = new Scanner(inputStream, "UTF-8")) {
            while (input.hasNext()) {
                String next = input.next();
                result.add(next);
            }
        } catch (IOException err) {
            System.out.println("Hiba a v2.txt fájl olvasásakor");
            err.printStackTrace();
        }
        for (String strings : result) {
            int targets = random.nextInt(10 + 1 - 1) + 1;
            for (int i = 0; i < targets; i++) {
                result2.add(new String[]{String.valueOf(result2.size()), result.get(random.nextInt(result.size())), result.get(random.nextInt(result.size()))});
            }
        }
        try {
            Connection connection = getConnection();
            connection.prepareStatement("DELETE FROM vasut").executeUpdate();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO vasut VALUES (?,?,?)");
            for (String[] strings : result2) {
                preparedStatement.setString(1, strings[1]);
                preparedStatement.setString(2, strings[2]);
                preparedStatement.setString(3, strings[0]);
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
