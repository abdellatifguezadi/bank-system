package dao;

import entity.AlerteFraude;
import entity.NiveauAlerte;
import util.MyJDBC;

import java.sql.*;
import java.util.*;

public class AlerteDAO {
    public void create(AlerteFraude alerte) {
        try (Connection conn = MyJDBC.getConnection();
                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO AlerteFraude (description, niveauAlerte, idCarte) VALUES (?, ?, ?)");) {
            ps.setString(1, alerte.description());
            ps.setString(2, alerte.niveauAlerte().name());
            ps.setInt(3, alerte.idCarte());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la création de l'alerte: " + e.getMessage());
        }
    }



    public List<AlerteFraude> getAll() {
        List<AlerteFraude> alertes = new ArrayList<>();
        try (Connection conn = MyJDBC.getConnection();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM AlerteFraude");) {
            while (rs.next()) {
                alertes.add(new AlerteFraude(
                        rs.getInt("id"),
                        rs.getString("description"),
                        NiveauAlerte.valueOf(rs.getString("niveauAlerte")),
                        rs.getInt("idCarte")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return alertes;
    }
}
