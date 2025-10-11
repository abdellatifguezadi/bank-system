package dao;

import entity.*;
import util.MyJDBC;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarteDAO implements ICarteDAO {

    public void createCarte(Carte carte) {
        if (carte == null) {
            return;
        }
        try (Connection conn = MyJDBC.getConnection();
                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO Carte (numero, dateExpiration, statut, typeCarte, idClient, plafondJournalier, plafondMensuel, tauxInteret, soldeDisponible) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");) {

            ps.setString(1, carte.getNumero());
            ps.setDate(2, Date.valueOf(carte.getDateExpiration()));
            ps.setString(3, carte.getStatut().name());
            ps.setString(4, carte.getClass().getSimpleName());
            ps.setInt(5, carte.getIdClient());
            if (carte instanceof CarteDebit debit) {
                ps.setDouble(6, debit.getPlafondJournalier());
                ps.setNull(7, Types.DOUBLE);
                ps.setNull(8, Types.DOUBLE);
                ps.setNull(9, Types.DOUBLE);
            } else if (carte instanceof CarteCredit credit) {
                ps.setNull(6, Types.DOUBLE);
                ps.setDouble(7, credit.getPlafondMensuel());
                ps.setDouble(8, credit.getTeauxInteret());
                ps.setNull(9, Types.DOUBLE);
            } else if (carte instanceof CartePrepayee prepayee) {
                ps.setNull(6, Types.DOUBLE);
                ps.setNull(7, Types.DOUBLE);
                ps.setNull(8, Types.DOUBLE);
                ps.setDouble(9, prepayee.getSolde());
            } else {
                ps.setNull(6, Types.DOUBLE);
                ps.setNull(7, Types.DOUBLE);
                ps.setNull(8, Types.DOUBLE);
                ps.setNull(9, Types.DOUBLE);
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Carte readCarte(int id) {
        try (Connection connection = MyJDBC.getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM carte WHERE id=?")) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String typeCarte = resultSet.getString("typeCarte");
                Carte carte;
                switch (typeCarte) {
                    case "CarteDebit":
                        CarteDebit debit = new CarteDebit();
                        debit.setPlafondJournalier(resultSet.getDouble("plafondJournalier"));
                        carte = debit;
                        break;
                    case "CarteCredit":
                        CarteCredit credit = new CarteCredit();
                        credit.setPlafondMensuel(resultSet.getDouble("plafondMensuel"));
                        credit.setTeauxInteret(resultSet.getDouble("tauxInteret"));
                        carte = credit;
                        break;
                    case "CartePrepayee":
                        CartePrepayee prepayee = new CartePrepayee();
                        prepayee.setSolde(resultSet.getDouble("soldeDisponible"));
                        carte = prepayee;
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
            } else {
                throw new SQLException("carte introuvable");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void update(Carte carte) {
        try (Connection connection = MyJDBC.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "UPDATE Carte SET numero=?, dateExpiration=?, statut=?, typeCarte=?, idClient=?, plafondJournalier=?, plafondMensuel=?, tauxInteret=?, soldeDisponible=? WHERE id=?")) {
            statement.setString(1, carte.getNumero());
            statement.setDate(2, Date.valueOf(carte.getDateExpiration()));
            statement.setString(3, carte.getStatut().name());
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
                        CarteDebit debit = new CarteDebit();
                        debit.setPlafondJournalier(rs.getDouble("plafondJournalier"));
                        carte = debit;
                        break;
                    case "CarteCredit":
                        CarteCredit credit = new CarteCredit();
                        credit.setPlafondMensuel(rs.getDouble("plafondMensuel"));
                        credit.setTeauxInteret(rs.getDouble("tauxInteret"));
                        carte = credit;
                        break;
                    case "CartePrepayee":
                        CartePrepayee prepayee = new CartePrepayee();
                        prepayee.setSolde(rs.getDouble("soldeDisponible"));
                        carte = prepayee;
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
                        CarteDebit debit = new CarteDebit();
                        debit.setPlafondJournalier(rs.getDouble("plafondJournalier"));
                        carte = debit;
                        break;
                    case "CarteCredit":
                        CarteCredit credit = new CarteCredit();
                        credit.setPlafondMensuel(rs.getDouble("plafondMensuel"));
                        credit.setTeauxInteret(rs.getDouble("tauxInteret"));
                        carte = credit;
                        break;
                    case "CartePrepayee":
                        CartePrepayee prepayee = new CartePrepayee();
                        prepayee.setSolde(rs.getDouble("soldeDisponible"));
                        carte = prepayee;
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cartes;
    }

}
