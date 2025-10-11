package dao;

import entity.Client;

public interface IClientDAO {
    boolean createClient(Client client);

    void deleteClient(int id);
}
