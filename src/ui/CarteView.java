package ui;

import entity.*;
import service.CarteService;
import java.time.LocalDate;
import java.util.Scanner;

public class CarteView {
    private final CarteService carteService;
    private final Scanner scanner;

    public CarteView(CarteService carteService) {
        this.carteService = carteService;
        this.scanner = new Scanner(System.in);
    }

    private String genererNumeroCarte() {
        // Génère un numéro de carte unique de 16 chiffres
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            sb.append((int)(Math.random() * 10));
        }
        return sb.toString();
    }

    public void creerCarte() {
        try {
            int type;
            do {
                System.out.println("Type (1=Débit, 2=Crédit, 3=Prépayée, 0=Retour menu) :");
                type = Integer.parseInt(scanner.nextLine());
                if (type == 0) return;
                if (type < 1 || type > 3) {
                    System.out.println("Type invalide ! Veuillez entrer 1, 2, 3 ou 0 pour revenir au menu.");
                }
            } while (type < 1 || type > 3);

            System.out.println("ID carte :");
            int idCarte = Integer.parseInt(scanner.nextLine());
            String numero = genererNumeroCarte();
            System.out.println("Date d'expiration (yyyy-mm-dd) :");
            LocalDate dateExpiration = LocalDate.parse(scanner.nextLine());
            System.out.println("ID client :");
            int idClient = Integer.parseInt(scanner.nextLine());

            Carte carte = null;
            if (type == 1) {
                System.out.println("Plafond journalier :");
                double plafondJournalier = Double.parseDouble(scanner.nextLine());
                CarteDebit debit = new CarteDebit();
                debit.setId(idCarte);
                debit.setNumero(numero);
                debit.setDateExpiration(dateExpiration);
                debit.setIdClient(idClient);
                debit.setPlafondJournalier(plafondJournalier);
                carte = debit;
            } else if (type == 2) {
                System.out.println("Plafond mensuel :");
                double plafondMensuel = Double.parseDouble(scanner.nextLine());
                System.out.println("Taux d'intérêt :");
                double tauxInteret = Double.parseDouble(scanner.nextLine());
                CarteCredit credit = new CarteCredit();
                credit.setId(idCarte);
                credit.setNumero(numero);
                credit.setDateExpiration(dateExpiration);
                credit.setIdClient(idClient);
                credit.setPlafondMensuel(plafondMensuel);
                credit.setTeauxInteret(tauxInteret);
                carte = credit;
            } else if (type == 3) {
                System.out.println("Solde disponible :");
                double soldeDisponible = Double.parseDouble(scanner.nextLine());
                CartePrepayee prepayee = new CartePrepayee();
                prepayee.setId(idCarte);
                prepayee.setNumero(numero);
                prepayee.setDateExpiration(dateExpiration);
                prepayee.setIdClient(idClient);
                prepayee.setSolde(soldeDisponible);
                carte = prepayee;
            }
            if (carte != null) {
                carte.setStatut(StatutCarte.ACTIVE);
            }
            carteService.creerCarte(carte);
            System.out.println("Carte émise avec succès ! Numéro généré : " + numero);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
