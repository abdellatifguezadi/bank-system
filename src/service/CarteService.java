package service;

import dao.CarteDAO;
import entity.Carte;
import entity.StatutCarte;

import java.util.List;
import java.util.NoSuchElementException;

public class CarteService {
    private final CarteDAO carteDAO;

    public CarteService(CarteDAO carteDAO) {
        this.carteDAO = carteDAO;
    }

    public void creerCarte(Carte carte) {
        if (carte == null) {
            return;
        }
        carteDAO.create(carte);
    }

    public void activerCarte(int idCarte) {
        Carte carte = carteDAO.read(idCarte);
        if (carte != null) {
            carte.setStatut(StatutCarte.ACTIVE);
            carteDAO.update(carte);
        } else {
            throw new NoSuchElementException("Carte introuvable !");
        }
    }

    public void bloquerCarte(int idCarte) {
        Carte carte = carteDAO.read(idCarte);
        if (carte != null) {
            carte.setStatut(StatutCarte.BLOQUEE);
            carteDAO.update(carte);
        } else {
            throw new NoSuchElementException("Carte introuvable !");
        }
    }

    public void suspendreCarte(int idCarte) {
        Carte carte = carteDAO.read(idCarte);
        if (carte != null) {
            carte.setStatut(StatutCarte.SUSPENDUE);
            carteDAO.update(carte);
        } else {
            throw new NoSuchElementException("Carte introuvable !");
        }
    }

    public Carte rechercherParId(int idCarte) {
        Carte carte = carteDAO.read(idCarte);
        if (carte != null) {
            return carte;
        } else {
            throw new NoSuchElementException("Carte introuvable !");
        }
    }

    public List<Carte> listerCartesParClient(int idClient) {
        return carteDAO.getByClient(idClient);
    }



    public List<Carte> listerToutesLesCartes() {
        return carteDAO.getAll();
    }

    public String obtenirStatutCarte(int idCarte) {
        Carte carte = carteDAO.read(idCarte);
        if (carte != null) {
            return carte.getStatut().name();
        } else {
            throw new NoSuchElementException("Carte introuvable !");
        }
    }
}
