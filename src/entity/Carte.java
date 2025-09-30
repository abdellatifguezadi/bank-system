package entity;

import java.time.LocalDate;

public sealed class Carte permits CarteCredit, CarteDebit , CartePrepayee {
    protected int id;
    protected String numero;
    protected LocalDate dateExpiration;
    protected StatutCarte statut;
    protected int idClient;

    public int getId() { return id; }
    public int getIdClient() { return idClient; }
    public StatutCarte getStatut() { return statut; }
    public void setStatut(StatutCarte statut) { this.statut = statut; }
    public String getNumero() { return numero; }
    public LocalDate getDateExpiration() { return dateExpiration; }

    public void setId(int id) { this.id = id; }
    public void setNumero(String numero) { this.numero = numero; }
    public void setDateExpiration(LocalDate dateExpiration) { this.dateExpiration = dateExpiration; }
    public void setIdClient(int idClient) { this.idClient = idClient; }
}
