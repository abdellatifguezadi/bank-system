import dao.ClientDAO;
import service.ClientService;
import ui.ClientView;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        ClientDAO clientDAO = new ClientDAO();
        ClientService clientService = new ClientService(clientDAO);
        ClientView clientView = new ClientView(clientService);


        int choice;
        do {
            System.out.println("Menu:");
            System.out.println("1. Ajouter un client");
            System.out.println("2. Supprimer un client");
            System.out.println("3. Quitter");
            System.out.print("Choisissez une option: ");
            choice = Integer.parseInt(new java.util.Scanner(System.in).nextLine());
            switch (choice) {
                case 1 -> clientView.ajouteClient();
                case 2 -> clientView.deleteClient();
                case 3 -> System.out.println("Au revoir!");
                default -> System.out.println("Option invalide, veuillez réessayer.");
            }
        } while (choice != 3);
    }
}