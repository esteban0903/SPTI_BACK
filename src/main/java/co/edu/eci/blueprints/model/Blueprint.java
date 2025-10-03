package co.edu.eci.blueprints.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Blueprint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String author;
    private String name;
    @ElementCollection
    private final List<Point> points = new ArrayList<>();
    public Blueprint() { };
    public Blueprint(String author, String name, List<Point> pts) {
        this.author = author;
        this.name = name;
        if (pts != null) points.addAll(pts);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getAuthor() { return author; }
    public String getName() { return name; }
    public List<Point> getPoints() { return Collections.unmodifiableList(points); }

    public void addPoint(Point p) { points.add(p); }
    
    // Method to clear and replace all points (needed for updates)
    public void replacePoints(List<Point> newPoints) {
        points.clear();
        if (newPoints != null) {
            points.addAll(newPoints);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Blueprint bp)) return false;
        return Objects.equals(author, bp.author) && Objects.equals(name, bp.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(author, name);
    }
}
