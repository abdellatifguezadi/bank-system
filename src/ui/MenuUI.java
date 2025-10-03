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
        System.out.println("0. Quitter");
        System.out.print("Choix : ");
    }

    public int lireChoix() {
        return scanner.nextInt();
    }


    public int saisirIdCarte() {
        System.out.print("ID carte : ");
        int idCarte = scanner.nextInt();
        scanner.nextLine();
        return idCarte;
    }
}
