package service;

import dao.ClientDAO;
import entity.Client;

import java.util.List;
import java.util.NoSuchElementException;

public class ClientService {

    private final ClientDAO clientDAO;


    public ClientService(ClientDAO clientDAO) {
        this.clientDAO = clientDAO;
    }

    public boolean createClient(Client client) {
        return clientDAO.createClient(client);
    }



    public void deleteClient(int id) {
        clientDAO.deleteClient(id);
    }


}
