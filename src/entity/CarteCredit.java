package entity;

public final class CarteCredit extends Carte {

    private double plafondMensuel;
    private double teauxInteret;

    public double getPlafondMensuel() { return plafondMensuel; }
    public void setPlafondMensuel(double plafondMensuel) { this.plafondMensuel = plafondMensuel; }

    public double getTeauxInteret() { return teauxInteret; }
    public void setTeauxInteret(double teauxInteret) { this.teauxInteret = teauxInteret; }
}
