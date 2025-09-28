package co.edu.eci.blueprints.model;

import jakarta.persistence.Embeddable;

@Embeddable
public record Point(int x, int y) { }
