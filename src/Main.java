//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import dao.AlerteDAO;
import dao.CarteDAO;
import dao.ClientDAO;
import dao.OperationDAO;
import entity.AlerteFraude;
import entity.Client;
import entity.OperationCarte;
import service.CarteService;
import service.ClientService;
import service.FraudeService;
import service.OperationService;
import ui.MenuUI;

import ui.ClientView;
import ui.CarteView;
import ui.OperationView;

import java.util.List;
import java.util.NoSuchElementException;

public class Main {
    public static void main(String[] args) {
        MenuUI menu = new MenuUI();
        ClientService clientService = new ClientService(new ClientDAO());
        CarteService carteService = new CarteService(new CarteDAO());
        OperationService operationService = new OperationService(new OperationDAO());
        FraudeService fraudeService = new FraudeService(new AlerteDAO());
        ClientView clientView = new ClientView(clientService);
        CarteView carteView = new CarteView(carteService);
        OperationView operationView = new OperationView(carteService);
        boolean running = true;
        while (running) {
            menu.afficherMenuPrincipal();
            int choix = menu.lireChoix();
            switch (choix) {
                case 1:
                    clientView.ajouteClient();
                    break;
                case 2:
                    carteView.creerCarte();
                    break;
                case 3:
                    operationView.faireOperation(operationService);
                    break;
                case 4:
                    int idCarteHistorique = menu.saisirIdCarte();
                    List<OperationCarte> historique = operationService.rechercherParCarte(idCarteHistorique);
                    System.out.println("Historique des opérations pour la carte " + idCarteHistorique + ":");
                    for (OperationCarte operation : historique) {
                        System.out.println(operation);
                    }
                    break;
                case 5:
                    int idCarteFraude = menu.saisirIdCarte();
                    List<OperationCarte> ops = operationService.rechercherParCarte(idCarteFraude);
                    List<AlerteFraude> alertes = fraudeService.analyserOperations(ops);
                    System.out.println("Alertes de fraude pour la carte " + idCarteFraude + ":");
                    for (AlerteFraude alerte : alertes) {
                        System.out.println(alerte);
                    }
                    break;
                case 6:
                    int idCarteAction = menu.saisirIdCarte();
                    System.out.print("1. Bloquer  2. Suspendre  3.Activer (ou 0 pour annulee) : ");
                    int action = menu.lireChoix();
                    try {
                        if (action == 1) {
                            if(carteService.obtenirStatutCarte(idCarteAction).equals("BLOQUEE")){
                                System.out.println("La carte est déjà bloquée.");
                                break;
                            }
                            carteService.bloquerCarte(idCarteAction);
                            System.out.println("Carte bloquée.");
                        } else if (action == 2) {
                            if(carteService.obtenirStatutCarte(idCarteAction).equals("SUSPENDUE")){
                                System.out.println("La carte est déjà suspendue.");
                                break;
                            }
                            carteService.suspendreCarte(idCarteAction);
                            System.out.println("Carte suspendue.");
                        }else if (action == 3){
                            if(carteService.obtenirStatutCarte(idCarteAction).equals("ACTIVE")){
                                System.out.println("La carte est déjà active.");
                                break;
                            }
                            carteService.activerCarte(idCarteAction);
                            System.out.println("Carte activée.");
                        }else if(action == 0) {
                            // Retour au menu principal
                            System.out.println("Retour au menu principal , action annulée.");
                            break;
                        }else{
                            System.out.println("Action invalide.");
                        }
                    } catch (NoSuchElementException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 0:
                    running = false;
                    System.out.println("Au revoir !");
                    break;
                default:
                    System.out.println("Choix invalide.");
            }
        }
    }
}