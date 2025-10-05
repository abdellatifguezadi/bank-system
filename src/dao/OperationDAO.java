package dao;

import entity.OperationCarte;
import entity.TypeOperation;
import util.MyJDBC;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OperationDAO {

    public void createOperation(OperationCarte operation) {
        try (Connection connection = MyJDBC.getConnection();
             PreparedStatement check = connection.prepareStatement("SELECT id FROM OperationCarte WHERE id = ?");
             PreparedStatement statement = connection.prepareStatement("INSERT INTO OperationCarte (idCarte, montant, dateOperation, typeOperation, description) VALUES (?, ?, ?, ?, ?)");) {
            check.setInt(1, operation.id());
            var resultSet = check.executeQuery();
            if (resultSet.next()) {
                throw new Exception("Operation with id " + operation.id() + " already exists.");
            }
            statement.setInt(1, operation.idCarte());
            statement.setDouble(2, operation.montant());
            statement.setTimestamp(3, Timestamp.valueOf(operation.date()));
            statement.setString(4, operation.type().name());
            statement.setString(5, operation.lieu());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public List<OperationCarte> getOperationByCarte(int idCarte) {
        List<OperationCarte> ops = new ArrayList<>();
        try (Connection connection = MyJDBC.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM OperationCarte WHERE idCarte=?");) {
            statement.setInt(1, idCarte);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                ops.add(new OperationCarte(
                        resultSet.getInt("id"),
                        resultSet.getTimestamp("dateOperation").toLocalDateTime(),
                        resultSet.getDouble("montant"),
                        TypeOperation.valueOf(resultSet.getString("typeOperation")),
                        resultSet.getString("description"),
                        resultSet.getInt("idCarte")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return ops;
    }

    public List<OperationCarte> getAllOperation() {
        List<OperationCarte> ops = new ArrayList<>();
        try (Connection connection = MyJDBC.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM OperationCarte");) {
            while (resultSet.next()) {
                ops.add(new OperationCarte(
                        resultSet.getInt("id"),
                        resultSet.getTimestamp("dateOperation").toLocalDateTime(),
                        resultSet.getDouble("montant"),
                        TypeOperation.valueOf(resultSet.getString("typeOperation")),
                        resultSet.getString("description"),
                        resultSet.getInt("idCarte")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return ops;
    }
}
