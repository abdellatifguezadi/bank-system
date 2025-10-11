package dao;

import entity.AlerteFraude;
import java.util.List;

public interface IAlerteDAO {
    void create(AlerteFraude alerte);

    List<AlerteFraude> getAll();
}
