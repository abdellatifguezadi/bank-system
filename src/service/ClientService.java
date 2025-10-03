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

    public void createClient(Client client) {
        clientDAO.createClient(client);
    }

    public Client findClientById(int id) {
        Client client = clientDAO.readClient(id);
        if ((client != null)) {
            return client;
        } else {
            throw new NoSuchElementException("Client introuvable");
        }
    }

    public List<Client> listerClients() {
        return clientDAO.getAllClients();
    }

    public void deleteClient(int id) {
        clientDAO.deleteClient(id);
    }


}
