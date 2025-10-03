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

    public OperationView(CarteService carteService) {
        this.carteService = carteService;
        this.scanner = new Scanner(System.in);
    }

    public void faireOperation(OperationService operationService, FraudeService fraudeService) {
        System.out.print("ID carte : ");
        int idCarteOp = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Type d'opération : 1=Paiement en ligne, 2=Achat, 3=Retrait");
        int typeOp = scanner.nextInt();
        scanner.nextLine();
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
        System.out.print("Montant : ");
        double montant = scanner.nextDouble();
        scanner.nextLine();

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
