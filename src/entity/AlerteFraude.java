package entity;

public record AlerteFraude(int id, String description, NiveauAlerte niveau, int idCarte) {}
