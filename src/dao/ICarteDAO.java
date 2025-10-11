package dao;

import entity.Carte;
import java.util.List;

public interface ICarteDAO {
    void createCarte(Carte carte);

    Carte readCarte(int id);

    void update(Carte carte);

    List<Carte> getByClient(int idClient);

    List<Carte> getAll();
}
