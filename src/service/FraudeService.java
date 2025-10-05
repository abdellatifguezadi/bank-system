package service;

import dao.AlerteDAO;
import entity.AlerteFraude;
import entity.OperationCarte;
import entity.NiveauAlerte;
import java.util.ArrayList;
import java.util.List;

public class FraudeService {
    private final AlerteDAO alerteDAO;

    public FraudeService(AlerteDAO alerteDAO) {
        this.alerteDAO = alerteDAO;
    }

    public void analyserUneOperation(OperationCarte operation) {
        AlerteFraude alerte = null;

        if (operation.montant() > 10000) {
            alerte = new AlerteFraude(-1,
                    "FRAUDE CRITIQUE - Montant très élevé: " + operation.montant() + " DH",
                    NiveauAlerte.CRITIQUE,
                    operation.idCarte());
        } else if (operation.montant() > 5000) {
            alerte = new AlerteFraude(-1,
                    "Montant suspect: " + operation.montant() + " DH",
                    NiveauAlerte.AVERTISSEMENT,
                    operation.idCarte());
        } else if (operation.montant() > 2000) {
            alerte = new AlerteFraude(-1,
                    "Surveillance: " + operation.montant() + " DH",
                    NiveauAlerte.INFO,
                    operation.idCarte());
        }
        if (alerte != null) {
            try {
                alerteDAO.create(alerte);
            } catch (Exception e) {
                throw new RuntimeException("Erreur lors de la création de l'alerte", e);
            }
        }
    }



    public List<AlerteFraude> consulterToutesLesAlertes() {
        try {
            return alerteDAO.getAll();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
