package dao;

import entity.AlerteFraude;
import entity.NiveauAlerte;
import util.MyJDBC;

import java.sql.*;
import java.util.*;

public class AlerteDAO {
    public void create(AlerteFraude alerte) {
        try (Connection conn = MyJDBC.getConnection();
             PreparedStatement check = conn.prepareStatement("SELECT id FROM AlerteFraude WHERE id=?");
             PreparedStatement ps = conn.prepareStatement("INSERT INTO AlerteFraude VALUES (?, ?, ?, ?, ?)");) {
            check.setInt(1, alerte.id());
            ResultSet rs = check.executeQuery();
            if (rs.next()) {
                throw new IllegalArgumentException("Erreur : cette alerte existe déjà !");
            }
            ps.setInt(1, alerte.id());
            ps.setString(2, alerte.description());
            ps.setString(3, alerte.niveau().name());
            ps.setInt(4, alerte.idCarte());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public AlerteFraude read(int id) {
        try (Connection conn = MyJDBC.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM AlerteFraude WHERE id=?");) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new AlerteFraude(
                        rs.getInt("id"),
                        rs.getString("description"),
                        NiveauAlerte.valueOf(rs.getString("niveau")),
                        rs.getInt("idCarte")
                );
            } else {
                throw new java.util.NoSuchElementException("Erreur : alerte introuvable !");
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public void update(AlerteFraude alerte) {
        try (Connection conn = MyJDBC.getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE AlerteFraude SET description=?, niveau=?, idCarte=? WHERE id=?");) {
            ps.setString(1, alerte.description());
            ps.setString(2, alerte.niveau().name());
            ps.setInt(3, alerte.idCarte());
            ps.setInt(4, alerte.id());
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new java.util.NoSuchElementException("Erreur : alerte introuvable !");
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void delete(int id) {
        try (Connection conn = MyJDBC.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM AlerteFraude WHERE id=?");) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new java.util.NoSuchElementException("Erreur : alerte introuvable !");
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public List<AlerteFraude> getByCarte(int idCarte) {
        List<AlerteFraude> alertes = new ArrayList<>();
        try (Connection conn = MyJDBC.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM AlerteFraude WHERE idCarte=?");) {
            ps.setInt(1, idCarte);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                alertes.add(new AlerteFraude(
                        rs.getInt("id"),
                        rs.getString("description"),
                        NiveauAlerte.valueOf(rs.getString("niveau")),
                        rs.getInt("idCarte")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return alertes;
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
                        NiveauAlerte.valueOf(rs.getString("niveau")),
                        rs.getInt("idCarte")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return alertes;
    }
}
