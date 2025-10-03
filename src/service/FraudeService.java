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

    public List<AlerteFraude> analyserOperations(List<OperationCarte> operations) {
        List<AlerteFraude> alertes = new ArrayList<>();
        for (OperationCarte op : operations) {
            if (op.montant() > 10000) {
                AlerteFraude alerte = new AlerteFraude(op.id(), "Montant élevé", NiveauAlerte.CRITIQUE, op.idCarte());
                alertes.add(alerte);
                genererAlerte(alerte);
            }
        }
        return alertes;
    }

    public void genererAlerte(AlerteFraude alerte) {
        alerteDAO.create(alerte);
    }
}
