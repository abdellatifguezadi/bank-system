package service;

import dao.CarteDAO;
import entity.*;

import java.time.LocalDate;
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
        carteDAO.createCarte(carte);
    }

    public void activerCarte(int idCarte) {
        Carte carte = carteDAO.readCarte(idCarte);
        if (carte != null) {
            carte.setStatut(StatutCarte.ACTIVE);
            carteDAO.update(carte);
        } else {
            throw new NoSuchElementException("Carte introuvable !");
        }
    }

    public void bloquerCarte(int idCarte) {
        Carte carte = carteDAO.readCarte(idCarte);
        if (carte != null) {
            carte.setStatut(StatutCarte.BLOQUEE);
            carteDAO.update(carte);
        } else {
            throw new NoSuchElementException("Carte introuvable !");
        }
    }

    public void suspendreCarte(int idCarte) {
        Carte carte = carteDAO.readCarte(idCarte);
        if (carte != null) {
            carte.setStatut(StatutCarte.SUSPENDUE);
            carteDAO.update(carte);
        } else {
            throw new NoSuchElementException("Carte introuvable !");
        }
    }

    public Carte rechercherParId(int idCarte) {
        Carte carte = carteDAO.readCarte(idCarte);
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
        Carte carte = carteDAO.readCarte(idCarte);
        if (carte != null) {
            return carte.getStatut().name();
        } else {
            throw new NoSuchElementException("Carte introuvable !");
        }
    }

    public String validerOperation(int idCarte, double montant) {
        try {
            Carte carte = carteDAO.readCarte(idCarte);
            if (carte == null) {
                return "Carte introuvable !";
            }
            if (carte.getStatut() != StatutCarte.ACTIVE) {
                return "La carte n'est pas active !";
            }
            if (carte.getDateExpiration().isBefore(LocalDate.now())) {
                return "La carte est expirée !";
            }

            if (carte instanceof CarteDebit debit) {
                if (montant > debit.getPlafondJournalier()) {
                    return "Le montant dépasse le plafond journalier de la carte !";
                }
            } else if (carte instanceof CarteCredit credit) {
                if (montant > credit.getPlafondMensuel()) {
                    return "Le montant dépasse le plafond mensuel de la carte de crédit !";
                }
            } else if (carte instanceof CartePrepayee prepayee) {
                if (montant > prepayee.getSolde()) {
                    return "Solde insuffisant sur la carte prépayée !";
                }
                prepayee.setSolde(prepayee.getSolde() - montant);
                carteDAO.update(prepayee);
            }

            return null;
        } catch (Exception e) {
            return "Erreur lors de la validation de l'opération : " + e.getMessage();
        }
    }
}
