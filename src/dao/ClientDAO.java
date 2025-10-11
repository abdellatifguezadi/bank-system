package dao;

import entity.Client;
import util.MyJDBC;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO implements IClientDAO {

    public boolean createClient(Client client) {
        try (Connection connection = MyJDBC.getConnection();
                PreparedStatement ps = connection
                        .prepareStatement("INSERT INTO Client (nom, email, telephone) VALUES (?, ?, ?)");
                PreparedStatement check = connection.prepareStatement("SELECT * FROM Client WHERE email=?")) {
            check.setString(1, client.email());
            ResultSet resultSet = check.executeQuery();
            if (resultSet.next()) {
                System.out.println("Erreur : email déjà utilisé !");
                return false;
            }

            ps.setString(1, client.nom());
            ps.setString(2, client.email());
            ps.setString(3, client.telephone());

            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void deleteClient(int id) {
        try (Connection connection = MyJDBC.getConnection();
                PreparedStatement ps = connection.prepareStatement("DELETE FROM Client WHERE id = ?")) {
            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Erreur : client introuvable !");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
