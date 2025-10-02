package dao;

import entity.*;
import util.MyJDBC;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class CarteDAO {

    public void create(Carte carte)  {
        if (carte == null) {
            return;
        }
        try (Connection conn = MyJDBC.getConnection();
             PreparedStatement check = conn.prepareStatement("SELECT id FROM Carte WHERE id=?");
             PreparedStatement ps = conn.prepareStatement("INSERT INTO Carte (id, numero, dateExpiration, statut, typeCarte, idClient, plafondJournalier, plafondMensuel, tauxInteret, soldeDisponible) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");) {
            check.setInt(1, carte.getId());
            ResultSet rs = check.executeQuery();
            if (rs.next()) {
                throw new SQLException("Erreur : carte déjà existante !");
            }
            ps.setInt(1, carte.getId());
            ps.setString(2, carte.getNumero());
            ps.setDate(3, Date.valueOf(carte.getDateExpiration()));
            ps.setString(4, carte.getStatut().name());
            ps.setString(5, carte.getClass().getSimpleName());
            ps.setInt(6, carte.getIdClient());
            // Set type-specific fields
            if (carte instanceof CarteDebit debit) {
               ps.setDouble(7, debit.getPlafondJournalier());
                ps.setNull(8, Types.DOUBLE);
                ps.setNull(9, Types.DOUBLE);
                ps.setNull(10, Types.DOUBLE);
            } else if (carte instanceof CarteCredit credit) {
                ps.setNull(7, Types.DOUBLE);
                ps.setDouble(8, credit.getPlafondMensuel());
                ps.setDouble(9, credit.getTeauxInteret());
                ps.setNull(10, Types.DOUBLE);
            } else if (carte instanceof CartePrepayee prepayee) {
                ps.setNull(7, Types.DOUBLE);
                ps.setNull(8, Types.DOUBLE);
                ps.setNull(9, Types.DOUBLE);
                ps.setDouble(10, prepayee.getSolde());
            } else {
                ps.setNull(7, Types.DOUBLE);
                ps.setNull(8, Types.DOUBLE);
                ps.setNull(9, Types.DOUBLE);
                ps.setNull(10, Types.DOUBLE);
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public Carte read(int id){
        try(Connection connection = MyJDBC.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM carte WHERE id=?")) {
            statement.setInt(1,id);
            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()){
                String typeCarte = resultSet.getString("typeCarte");
                Carte carte ;
                switch (typeCarte) {
                    case "CarteDebit":
                        carte = new CarteDebit();
                        break;
                    case "CarteCredit":
                        carte = new CarteCredit();
                        break;
                    case "CartePrepayee":
                        carte = new CartePrepayee();
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown carte type: " + typeCarte);
                }
                carte.setId(resultSet.getInt("id"));
                carte.setNumero(resultSet.getString("numero"));
                carte.setDateExpiration(resultSet.getDate("dateExpiration").toLocalDate());
                carte.setStatut(StatutCarte.valueOf(resultSet.getString("statut")));
                carte.setIdClient(resultSet.getInt("idClient"));
                return carte;
            }else {
                throw new SQLException("carte introuvable");
            }
        } catch (SQLException e) { System.out.println(e.getMessage()); }
       return null ;
    }


    public void update(Carte carte){
        try(Connection connection = MyJDBC.getConnection();
        PreparedStatement statement = connection.prepareStatement("UPDATE Carte SET numero=?, dateExpiration=?, statut=?, typeCarte=?, idClient=?, plafondJournalier=?, plafondMensuel=?, tauxInteret=?, soldeDisponible=? WHERE id=?")) {
            statement.setString(1, carte.getNumero());
            statement.setDate(2,Date.valueOf(carte.getDateExpiration()));
            statement.setString(3,carte.getStatut().name());
            statement.setString(4, carte.getClass().getSimpleName());
            statement.setInt(5, carte.getIdClient());
            if (carte instanceof CarteDebit debit) {
                statement.setDouble(6, debit.getPlafondJournalier());
                statement.setNull(7, Types.DOUBLE);
                statement.setNull(8, Types.DOUBLE);
                statement.setNull(9, Types.DOUBLE);
            } else if (carte instanceof CarteCredit credit) {
                statement.setNull(6, Types.DOUBLE);
                statement.setDouble(7, credit.getPlafondMensuel());
                statement.setDouble(8, credit.getTeauxInteret());
                statement.setNull(9, Types.DOUBLE);
            } else if (carte instanceof CartePrepayee prepayee) {
                statement.setNull(6, Types.DOUBLE);
                statement.setNull(7, Types.DOUBLE);
                statement.setNull(8, Types.DOUBLE);
                statement.setDouble(9, prepayee.getSolde());
            } else {
                statement.setNull(6, Types.DOUBLE);
                statement.setNull(7, Types.DOUBLE);
                statement.setNull(8, Types.DOUBLE);
                statement.setNull(9, Types.DOUBLE);
            }
            statement.setInt(10, carte.getId());
            int rows = statement.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Erreur : carte introuvable !");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Impossible de mettre à jour la carte : le client n'existe pas.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public void delete(int id)  {
        try (Connection conn = MyJDBC.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM Carte WHERE id=?");) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Erreur : carte introuvable !");
            }
        } catch (SQLException e) { System.out.println(e.getMessage()); }
    }

    public List<Carte> getByClient(int idClient) {
        List<Carte> cartes = new ArrayList<>();
        try (Connection conn = MyJDBC.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM Carte WHERE idClient=?");) {
            ps.setInt(1, idClient);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String typeCarte = rs.getString("typeCarte");
                Carte carte;
                switch (typeCarte) {
                    case "CarteDebit":
                        carte = new CarteDebit();
                        break;
                    case "CarteCredit":
                        carte = new CarteCredit();
                        break;
                    case "CartePrepayee":
                        carte = new CartePrepayee();
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown carte type: " + typeCarte);
                }
                carte.setId(rs.getInt("id"));
                carte.setNumero(rs.getString("numero"));
                carte.setDateExpiration(rs.getDate("dateExpiration").toLocalDate());
                carte.setStatut(StatutCarte.valueOf(rs.getString("statut")));
                carte.setIdClient(rs.getInt("idClient"));
                cartes.add(carte);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return cartes;
    }

    public List<Carte> getAll() {
        List<Carte> cartes = new ArrayList<>();
        try (Connection conn = MyJDBC.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM Carte");) {
            while (rs.next()) {
                String typeCarte = rs.getString("typeCarte");
                Carte carte;
                switch (typeCarte) {
                    case "CarteDebit":
                        carte = new CarteDebit();
                        break;
                    case "CarteCredit":
                        carte = new CarteCredit();
                        break;
                    case "CartePrepayee":
                        carte = new CartePrepayee();
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown carte type: " + typeCarte);
                }
                carte.setId(rs.getInt("id"));
                carte.setNumero(rs.getString("numero"));
                carte.setDateExpiration(rs.getDate("dateExpiration").toLocalDate());
                carte.setStatut(StatutCarte.valueOf(rs.getString("statut")));
                carte.setIdClient(rs.getInt("idClient"));
                cartes.add(carte);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return cartes;
    }
}
