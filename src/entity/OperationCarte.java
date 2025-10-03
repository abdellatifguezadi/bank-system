package entity;

import java.time.LocalDateTime;

public record OperationCarte(int id, LocalDateTime date, double montant, TypeOperation type, String lieu, int idCarte) {}
