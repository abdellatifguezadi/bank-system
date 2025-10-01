package ui;

import dao.ClientDAO;
import entity.Client;
import service.ClientService;

import java.util.Scanner;

public class ClientView {
    private final ClientService clientService;


    public ClientView(ClientService clientService) {
        this.clientService = clientService;
    }

    public void ajouteClient(){

        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("Entrer l'ID du client:");
            int id = Integer.parseInt(scanner.nextLine());
            System.out.println("Entrer le nom du client:");
            String nom = scanner.nextLine();
            System.out.println("Entrer l'email du client:");
            String email = scanner.nextLine();
            System.out.println("Entrer le téléphone du client:");
            String telephone = scanner.nextLine();
            Client client = new Client(id,nom,email,telephone);
            clientService.createClient(client);
            System.out.println("Client ajouté avec succès !");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }


    }
}
