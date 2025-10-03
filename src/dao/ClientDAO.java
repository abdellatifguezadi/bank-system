package dao;

import entity.Client;
import util.MyJDBC;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {

    public void create(Client client) {
        try(Connection connection = MyJDBC.getConnection();
            PreparedStatement check = connection.prepareStatement("SELECT id FROM Client WHERE id = ?");
            PreparedStatement ps = connection.prepareStatement("INSERT INTO Client (id, nom, email, telephone) VALUES (?, ?, ?, ?)")) {
            check.setInt(1, client.id());
            ResultSet rs = check.executeQuery();
            if(rs.next()){
                throw new Exception("Client with id " + client.id() + " already exists.");
            }
            ps.setInt(1, client.id());
            ps.setString(2, client.nom());
            ps.setString(3, client.email());
            ps.setString(4, client.telephone());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public Client read(int id){

        try(Connection conn = MyJDBC.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Client WHERE id = ?")) {
            ps.setInt(1,id);
            ResultSet resultSet = ps.executeQuery();
            if(resultSet.next()){
                return new Client(resultSet.getInt("id"), resultSet.getString("nom") , resultSet.getString("email"), resultSet.getString("telephone"));
            }else{
                throw new SQLException("Erreur : client introuvable !");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    public List<Client> getAll(){
            List<Client> clients = new ArrayList<>();
            try(Connection connection = MyJDBC.getConnection();
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("SELECT * FROM Client")
            ) {
                while (rs.next()){
                    clients.add(new Client(rs.getInt("id"),rs.getString("nom"),rs.getString("email"), rs.getString("telephone")));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return clients;
    }

    public void delete(int id){
        try(Connection connection = MyJDBC.getConnection();
            PreparedStatement ps = connection.prepareStatement("DELETE FROM Client WHERE id = ?")) {
            ps.setInt(1,id);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Erreur : client introuvable !");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
