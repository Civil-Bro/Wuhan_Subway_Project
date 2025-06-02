package subway;
import java.util.*;

public class Line {
    private String name;
    private List<String> stationNames = new ArrayList<>();

    public Line(String name) {
        this.name = name;
    }

    public void addStation(String stationName) {
        stationNames.add(stationName);
    }

    public List<String> getStationNames() {
        return stationNames;
    }

    public String getName() {
        return name;
    }
}
