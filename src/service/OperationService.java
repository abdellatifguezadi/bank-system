package service;

import dao.OperationDAO;
import entity.OperationCarte;

import java.util.ArrayList;
import java.util.List;

public class OperationService {

    private final OperationDAO operationDAO;

    public OperationService(OperationDAO operationDAO) {
        this.operationDAO = operationDAO;
    }

    public void enregistrerOperation(OperationCarte operation)  {
        operationDAO.createOperation(operation);
    }

    public List<OperationCarte> rechercherParCarte(int idCarte) {
        return operationDAO.getOperationByCarte(idCarte);
    }

}
