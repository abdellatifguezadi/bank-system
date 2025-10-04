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
        int idCarteOp = menu.saisirEntierSecurise("ID carte : ");
        if (idCarteOp == -1) {
            System.out.println("Opération annulée - ID invalide");
            return;
        }

        System.out.println("Type d'opération : 1=Paiement en ligne, 2=Achat, 3=Retrait");
        int typeOp = menu.saisirEntierSecurise("Votre choix : ");
        if (typeOp == -1) {
            System.out.println("Opération annulée - Type invalide");
            return;
        }
        TypeOperation typeOperation;
        switch (typeOp) {
            case 1:
                typeOperation = TypeOperation.PAIEMENTENLIGNE;
                break;
            case 2:
                typeOperation = TypeOperation.ACHAT;
                break;
            case 3:
                typeOperation = TypeOperation.RETRAIT;
                break;
            default:
                System.out.println("Type d'opération invalide.");
                return;
        }
        double montant = menu.saisirDoubleSecurise("Montant : ");
        if (montant == -1.0) {
            System.out.println(" Opération annulée - Montant invalide");
            return;
        }

        String msgValidation = carteService.validerOperation(idCarteOp, montant);
        if (msgValidation != null) {
            System.out.println(msgValidation);
            return;
        }
        LocalDateTime dateOp = LocalDateTime.now();
        System.out.print("Lieu de l'opération : ");
        String lieu = scanner.nextLine();
        OperationCarte operation = new OperationCarte(0, dateOp, montant, typeOperation, lieu, idCarteOp);
        operationService.enregistrerOperation(operation);
        System.out.println(" Opération enregistrée avec succès.");

        fraudeService.analyserUneOperation(operation);
    }

}
