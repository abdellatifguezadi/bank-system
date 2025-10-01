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


        clientView.ajouteClient();
    }
}