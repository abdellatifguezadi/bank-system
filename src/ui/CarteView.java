package ui;

import entity.*;
import service.CarteService;
import java.time.LocalDate;
import java.util.Scanner;

public class CarteView {
    private final CarteService carteService;
    private final Scanner scanner;
    private final MenuUI menu;

    public CarteView(CarteService carteService, MenuUI menu) {
        this.carteService = carteService;
        this.scanner = new Scanner(System.in);
        this.menu = menu;
    }

    private String genererNumeroCarte() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            sb.append((int) (Math.random() * 10));
        }
        return sb.toString();
    }

    public void creerCarte() {
        try {
            int type;
            do {
                System.out.println("Type (1=Débit, 2=Crédit, 3=Prépayée, 0=Retour menu) :");
                type = menu.saisirEntierSecurise("Votre choix : ");
                if (type == -1) {
                    System.out.println(" Opération annulée - Type invalide");
                    return;
                }
                if (type == 0)
                    return;
                if (type < 1 || type > 3) {
                    System.out.println("Type invalide ! Veuillez entrer 1, 2, 3 ou 0 pour revenir au menu.");
                }
            } while (type < 1 || type > 3);

            int idCarte = menu.saisirEntierSecurise("ID carte : ");
            if (idCarte == -1) {
                System.out.println(" Opération annulée - ID carte invalide");
                return;
            }

            String numero = genererNumeroCarte();
            LocalDate dateExpiration = LocalDate.now().plusYears(10);

            int idClient = menu.saisirEntierSecurise("ID client : ");
            if (idClient == -1) {
                System.out.println(" Opération annulée - ID client invalide");
                return;
            }

            Carte carte = null;
            if (type == 1) {
                double plafondJournalier = menu.saisirDoubleSecurise("Plafond journalier : ");
                if (plafondJournalier == -1.0) {
                    System.out.println(" Opération annulée - Plafond invalide");
                    return;
                }
                CarteDebit debit = new CarteDebit();
                debit.setId(idCarte);
                debit.setNumero(numero);
                debit.setDateExpiration(dateExpiration);
                debit.setIdClient(idClient);
                debit.setPlafondJournalier(plafondJournalier);
                carte = debit;
            } else if (type == 2) {
                double plafondMensuel = menu.saisirDoubleSecurise("Plafond mensuel : ");
                if (plafondMensuel == -1.0) {
                    System.out.println(" Opération annulée - Plafond invalide");
                    return;
                }
                double tauxInteret = menu.saisirDoubleSecurise("Taux d'intérêt : ");
                if (tauxInteret == -1.0) {
                    System.out.println(" Opération annulée - Taux invalide");
                    return;
                }
                CarteCredit credit = new CarteCredit();
                credit.setId(idCarte);
                credit.setNumero(numero);
                credit.setDateExpiration(dateExpiration);
                credit.setIdClient(idClient);
                credit.setPlafondMensuel(plafondMensuel);
                credit.setTeauxInteret(tauxInteret);
                carte = credit;
            } else if (type == 3) {
                double soldeDisponible = menu.saisirDoubleSecurise("Solde disponible : ");
                if (soldeDisponible == -1.0) {
                    System.out.println(" Opération annulée - Solde invalide");
                    return;
                }
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
