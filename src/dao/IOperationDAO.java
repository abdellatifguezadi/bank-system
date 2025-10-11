package dao;

import entity.OperationCarte;
import java.util.List;

public interface IOperationDAO {
    void createOperation(OperationCarte operation);

    List<OperationCarte> getOperationByCarte(int idCarte);

    List<OperationCarte> getAllOperation();
}
