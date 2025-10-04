package util;

import dao.CarteDAO;
import dao.ClientDAO;
import dao.OperationDAO;
import dao.AlerteDAO;
import entity.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ExportExcel {

    private final CarteDAO carteDAO;
    private final OperationDAO operationDAO;

    public ExportExcel() {
        this.carteDAO = new CarteDAO();
        this.operationDAO = new OperationDAO();
    }

    public void exporterVersExcel() {
        try {
            FileOutputStream fos = new FileOutputStream("export_banque.csv");
            StringBuilder csvContent = new StringBuilder();

            List<Carte> cartes = carteDAO.getAll();
            List<OperationCarte> operations = operationDAO.getAllOperation();

            csvContent.append("=== CARTES ===\n");
            csvContent.append("ID,Numero,Type,Date_Expiration,ID_Client,Statut,Details\n");
            for (Carte carte : cartes) {
                String details = "";
                if (carte instanceof CarteDebit) {
                    CarteDebit debit = (CarteDebit) carte;
                    details = "Plafond_Journalier:" + debit.getPlafondJournalier();
                } else if (carte instanceof CarteCredit) {
                    CarteCredit credit = (CarteCredit) carte;
                    details = "Plafond_Mensuel:" + credit.getPlafondMensuel() +
                            "|Taux:" + credit.getTeauxInteret();
                } else if (carte instanceof CartePrepayee) {
                    CartePrepayee prepayee = (CartePrepayee) carte;
                    details = "Solde:" + prepayee.getSolde();
                }

                csvContent.append(carte.getId()).append(",")
                        .append(carte.getNumero()).append(",")
                        .append(carte.getClass().getSimpleName().toUpperCase()).append(",")
                        .append(carte.getDateExpiration()).append(",")
                        .append(carte.getIdClient()).append(",")
                        .append(carte.getStatut()).append(",")
                        .append(details).append("\n");
            }
            csvContent.append("\n");


            csvContent.append("=== OPERATIONS ===\n");
            csvContent.append("ID,Date_Operation,Montant,Type_Operation,Lieu,ID_Carte\n");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            for (OperationCarte operation : operations) {
                csvContent.append(operation.id()).append(",")
                        .append(operation.date().format(formatter)).append(",")
                        .append(operation.montant()).append(",")
                        .append(operation.type()).append(",")
                        .append(operation.lieu()).append(",")
                        .append(operation.idCarte()).append("\n");
            }
            csvContent.append("\n");






            double montantTotal = operations.stream().mapToDouble(OperationCarte::montant).sum();
            csvContent.append("Montant_Total_Operations,").append(montantTotal).append("\n");

            if (!operations.isEmpty()) {
                double montantMax = operations.stream().mapToDouble(OperationCarte::montant).max().orElse(0);
                double montantMin = operations.stream().mapToDouble(OperationCarte::montant).min().orElse(0);
                csvContent.append("Operation_Max,").append(montantMax).append("\n");
                csvContent.append("Operation_Min,").append(montantMin).append("\n");
            }



            fos.write(csvContent.toString().getBytes("UTF-8"));
            fos.close();

            System.out.println("Export Excel terminé avec succès !");
            System.out.println("Données exportées :");
            System.out.println("   - " + cartes.size() + " cartes");
            System.out.println("   - " + operations.size() + " opérations");
            System.out.println("   - Montant total des opérations : " + montantTotal + " DH");
            System.out.println("Fichier : export_banque.csv");

        } catch (IOException e) {
            System.err.println("Erreur lors de l'export : " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erreur de base de données : " + e.getMessage());
        }
    }
}