package subway;
import java.io.*;
import java.util.*;

public class SubwaySystem {
    private Map<String, Station> stations = new HashMap<>();
    private Map<String, Line> lines = new HashMap<>();
    public SubwaySystem() {}
    public SubwaySystem(String filepath){
        try{
            parseSubwayFile(filepath);
        }catch(IOException e){
            e.printStackTrace();
            System.err.println("Error reading subway file");
        }
    }
    /*
    3.1查询地铁中转站
    在所有地铁车站中，识别出所有的地铁中转站（即至少有两条线经过该站），并返回一个包含所有地铁中转站的集合，集合中的每条记录包含站点名称、通过的所有线路（如：<中南路站，<2号线、4号线>>）；
     */
    public Set<String> getTransferStations() {
        Set<String> result = new HashSet<>();
        for (Station station : stations.values()) {
            Set<String> lines = station.getLines();
            if (lines.size() >= 2) {
                StringBuilder sb = new StringBuilder();
                sb.append("<").append(station.getName()).append("，<");
                sb.append(String.join("、", lines));
                sb.append(">>");
                result.add(sb.toString());
            }
        }
        return result;
    }

    public Station getStation(String name) {
        return stations.get(name);
    }

    public Set<String> getAllStationNames() {
        return stations.keySet();
    }

    private void parseSubwayFile(String filepath) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath)));
        String line;
        String currentLineName = null;
        List<String[]> stationPairs = new ArrayList<>();

        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.endsWith("站点间距") || line.matches(".*线站点间距")) {
                currentLineName = line.replace("站点间距", "").trim();
                lines.put(currentLineName, new Line(currentLineName));
                continue;
            }

            if (line.contains("---") || line.contains("—")) {
                line = line.replace("—", "---"); // 替换中文破折号
                String[] parts = line.split("---");
                if (parts.length != 2) continue;

                String[] nameAndDist = parts[1].trim().split("\\s+");
                if (nameAndDist.length == 0) continue;

                String from = parts[0].trim();
                String to = nameAndDist[0].trim();
                double dist = Double.parseDouble(nameAndDist[nameAndDist.length - 1]);

                addConnection(currentLineName, from, to, dist);
            }
        }

        br.close();
    }

    private void addConnection(String lineName, String from, String to, double distance) {
        Station s1 = stations.computeIfAbsent(from, Station::new);
        Station s2 = stations.computeIfAbsent(to, Station::new);
        s1.addLine(lineName);
        s2.addLine(lineName);
        s1.addNeighbor(s2, distance);
        s2.addNeighbor(s1, distance);

        lines.get(lineName).addStation(from);
        lines.get(lineName).addStation(to);
    }

    public Map<String, Station> getStations() {
        return stations;
    }
    public Map<String, Line> getLines() {
        return lines;
    }
}

