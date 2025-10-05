package entity;

public record AlerteFraude(int id, String description, NiveauAlerte niveauAlerte, int idCarte) {}
