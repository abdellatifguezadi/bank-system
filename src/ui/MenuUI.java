package ui;

import entity.*;

import java.time.LocalDateTime;
import java.util.Scanner;

public class MenuUI {
    private final Scanner scanner = new Scanner(System.in);

    public void afficherMenuPrincipal() {
        System.out.println("=== Gestion des Cartes Bancaires ===");
        System.out.println("1. Créer un client");
        System.out.println("2. Émettre une carte");
        System.out.println("3. Effectuer une opération");
        System.out.println("4. Consulter l’historique d’une carte");
        System.out.println("5. Lancer une analyse des fraudes");
        System.out.println("6. Bloquer/Suspendre une carte");
        System.out.println("7. Supprimer client");
        System.out.println("8. Exporter vers Excel");
        System.out.println("0. Quitter");
        System.out.print("Choix : ");
    }

    public int lireChoix() {
        try {
            return scanner.nextInt();
        } catch (Exception e) {
            scanner.nextLine();
            System.out.println("Erreur : Veuillez entrer un nombre valide (0-8)");
            return -1;
        }
    }

    public int saisirIdCarte() {
        return saisirEntier("ID carte : ");
    }

    public int saisirEntier(String message) {
        System.out.print(message);
        try {
            int valeur = scanner.nextInt();
           scanner.nextLine();
            return valeur;
        } catch (Exception e) {
            scanner.nextLine();
            System.out.println("Erreur : Veuillez entrer un nombre entier valide");
            return -1;
        }
    }

    public double saisirDouble(String message) {
        System.out.print(message);
        try {
            double valeur = Double.parseDouble(scanner.nextLine());
            return valeur;
        } catch (Exception e) {
            System.out.println("Erreur : Veuillez entrer un nombre décimal valide");
            return -1.0;
        }
    }
}
