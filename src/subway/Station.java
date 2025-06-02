package subway;
import java.util.*;

public class Station {
    private String name;
    private Set<String> lines = new HashSet<>();
    private Map<Station, Double> neighbors = new HashMap<>();

    public Station(String name) {
        this.name = name;
    }

    public String getName() { return name; }

    public void addLine(String line) {
        lines.add(line);
    }

    public Set<String> getLines() {
        return lines;
    }

    public void addNeighbor(Station neighbor, double distance) {
        neighbors.put(neighbor, distance);
    }

    public Map<Station, Double> getNeighbors() {
        return neighbors;
    }

    @Override
    public String toString() {
        return name;
    }
}