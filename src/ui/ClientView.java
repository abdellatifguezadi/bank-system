package ui;

import dao.ClientDAO;
import entity.Carte;
import entity.Client;
import service.CarteService;
import service.ClientService;

import java.util.List;
import java.util.Scanner;

public class ClientView {
    private final ClientService clientService;
    private CarteService carteService;
    private final MenuUI menu;

    public ClientView(ClientService clientService, CarteService carteService, MenuUI menu) {
        this.carteService = carteService;
        this.clientService = clientService;
        this.menu = menu;
    }

    public void ajouteClient() {

        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("Entrer le nom du client:");
            String nom = scanner.nextLine();
            System.out.println("Entrer l'email du client:");
            String email = scanner.nextLine();
            System.out.println("Entrer le téléphone du client:");
            String telephone = scanner.nextLine();
            Client client = new Client(0, nom, email, telephone);
            boolean success = clientService.createClient(client);
            if (success) {
                System.out.println("Client ajouté avec succès !");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

    }

    public void deleteClient() {
        try {
            int id = menu.saisirEntier("Entrer l'ID du client à supprimer: ");

            if (id == -1) {
                System.out.println("Opération annulée - ID invalide");
                return;
            }

            List<Carte> cartes = carteService.listerCartesParClient(id);

            if (!cartes.isEmpty()) {
                System.out.println("Impossible de supprimer : ce client possède " + cartes.size() + " carte(s)");
                return;
            }

            clientService.deleteClient(id);
            System.out.println("Client supprimé avec succès !");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
