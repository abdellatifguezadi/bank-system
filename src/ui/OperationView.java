package ui;

import entity.OperationCarte;
import entity.TypeOperation;
import service.CarteService;
import service.FraudeService;
import service.OperationService;

import java.time.LocalDateTime;
import java.util.Scanner;

public class OperationView {
    private final CarteService carteService;
    private final Scanner scanner;
    private final MenuUI menu;

    public OperationView(CarteService carteService, MenuUI menu) {
        this.carteService = carteService;
        this.scanner = new Scanner(System.in);
        this.menu = menu;
    }

    public void faireOperation(OperationService operationService, FraudeService fraudeService) {
        int idCarte = menu.saisirEntier("ID carte : ");
        if (idCarte == -1) {
            System.out.println("Opération annulée - ID invalide");
            return;
        }

        System.out.println("Type d'opération : 1=Achat, 2=Retrait, 3=Paiement en ligne");
        int typeOp = menu.saisirEntier("Votre choix : ");
        if (typeOp == -1) {
            System.out.println("Opération annulée - Type invalide");
            return;
        }
        TypeOperation typeOperation;
        switch (typeOp) {
            case 1:
                typeOperation = TypeOperation.ACHAT;
                break;
            case 2:
                typeOperation = TypeOperation.RETRAIT;
                break;
            case 3:
                typeOperation = TypeOperation.PAIEMENTENLIGNE;
                break;
            default:
                System.out.println("Type d'opération invalide.");
                return;
        }
        double montant = menu.saisirDouble("Montant : ");
        if (montant == -1.0) {
            System.out.println(" Opération annulée - Montant invalide");
            return;
        }

        String msgValidation = carteService.validerOperation(idCarte, montant);
        if (msgValidation != null) {
            System.out.println(msgValidation);
            return;
        }
        LocalDateTime date = LocalDateTime.now();
        System.out.print("Lieu de l'opération : ");
        String lieu = scanner.nextLine();
        OperationCarte operation = new OperationCarte(0, date, montant, typeOperation, lieu, idCarte);
        operationService.enregistrerOperation(operation);
        System.out.println(" Opération enregistrée avec succès.");

        fraudeService.analyserUneOperation(operation);
    }

}
