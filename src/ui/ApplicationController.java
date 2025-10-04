package ui;

import dao.AlerteDAO;
import dao.CarteDAO;
import dao.ClientDAO;
import dao.OperationDAO;
import entity.AlerteFraude;
import entity.OperationCarte;
import service.CarteService;
import service.ClientService;
import service.FraudeService;
import service.OperationService;
import util.ExportExcel;

import java.util.List;
import java.util.NoSuchElementException;

public class ApplicationController {
    private final MenuUI menu;
    private final ClientService clientService;
    private final CarteService carteService;
    private final OperationService operationService;
    private final FraudeService fraudeService;
    private final ClientView clientView;
    private final CarteView carteView;
    private final OperationView operationView;

    public ApplicationController() {
        this.menu = new MenuUI();
        this.clientService = new ClientService(new ClientDAO());
        this.carteService = new CarteService(new CarteDAO());
        this.operationService = new OperationService(new OperationDAO());
        this.fraudeService = new FraudeService(new AlerteDAO());
        this.clientView = new ClientView(clientService, carteService, menu);
        this.carteView = new CarteView(carteService, menu);
        this.operationView = new OperationView(carteService, menu);
    }

    public void demarrerApplication() {
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
                    operationView.faireOperation(operationService, fraudeService);
                    break;
                case 4:
                    afficherHistoriqueOperations();
                    break;
                case 5:
                    afficherAlertesFraude();
                    break;
                case 6:
                    gererStatutCarte();
                    break;
                case 7:
                    clientView.deleteClient();
                    break;
                case 8:
                    exporterVersExcel();
                    break;
                case 0:
                    running = false;
                    System.out.println("Au revoir !");
                    break;
                default:
                    System.out.println("❌ Choix invalide ! Veuillez choisir un nombre entre 0 et 8.");
            }
        }
    }

    private void afficherHistoriqueOperations() {
        int idCarteHistorique = menu.saisirIdCarte();
        List<OperationCarte> historique = operationService.rechercherParCarte(idCarteHistorique);
        System.out.println("Historique des opérations pour la carte " + idCarteHistorique + ":");
        for (OperationCarte operation : historique) {
            System.out.println(operation);
        }
    }

    private void afficherAlertesFraude() {
        System.out.println("=== TOUTES LES ALERTES DE FRAUDE ===");

        List<AlerteFraude> alertes = fraudeService.consulterToutesLesAlertes();

        if (alertes.isEmpty()) {
            System.out.println("Aucune alerte trouvée.");
        } else {
            System.out.println("Total: " + alertes.size() + " alerte(s) détectée(s)");
            System.out.println("----------------------------------------");
            for (AlerteFraude alerte : alertes) {
                System.out.println("Carte " + alerte.idCarte() + " - " +
                        alerte.niveau() + ": " + alerte.description());
            }
        }
    }

    private void gererStatutCarte() {
        int idCarteAction = menu.saisirIdCarte();
        System.out.print("1. Bloquer  2. Suspendre  3.Activer (ou 0 pour annulee) : ");
        int action = menu.lireChoix();

        try {
            if (action == 1) {
                if (carteService.obtenirStatutCarte(idCarteAction).equals("BLOQUEE")) {
                    System.out.println("La carte est déjà bloquée.");
                    return;
                }
                carteService.bloquerCarte(idCarteAction);
                System.out.println("Carte bloquée.");
            } else if (action == 2) {
                if (carteService.obtenirStatutCarte(idCarteAction).equals("SUSPENDUE")) {
                    System.out.println("La carte est déjà suspendue.");
                    return;
                }
                carteService.suspendreCarte(idCarteAction);
                System.out.println("Carte suspendue.");
            } else if (action == 3) {
                if (carteService.obtenirStatutCarte(idCarteAction).equals("ACTIVE")) {
                    System.out.println("La carte est déjà active.");
                    return;
                }
                carteService.activerCarte(idCarteAction);
                System.out.println("Carte activée.");
            } else if (action == 0) {
                System.out.println("Retour au menu principal , action annulée.");
            } else {
                System.out.println("Action invalide.");
            }
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
        }
    }

    private void exporterVersExcel() {
        System.out.println("🔄 Export des données vers Excel en cours...");
        ExportExcel export = new ExportExcel();
        export.exporterVersExcel();
    }
}