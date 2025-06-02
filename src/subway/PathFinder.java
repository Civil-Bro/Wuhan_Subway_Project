package subway;

import java.util.*;

public class PathFinder {
    private SubwaySystem subway;

    public PathFinder(SubwaySystem subway) {
        this.subway = subway;
    }

    /*
    * 3.2查询某站点距离小于 n 的所有站点
    * 输入某一站点，输出线路距离小于n的所有站点集合，包含站点名称、所在线路、距离给定站点的距离（输入不合规时进行异常处理）（例如：华中科技大学站，距离为1的站点为<<珞雄路站，2号线，1>, <光谷大道站，2号线，1>>）；
    */
    public Set<String> getNearbyStations(String stationName, int maxHops) {
        Map<String, Station> allStations = subway.getStations();

        if (!allStations.containsKey(stationName)) {
            throw new IllegalArgumentException("站点不存在：" + stationName);
        }

        Station source = allStations.get(stationName);
        Set<String> result = new HashSet<>();

        // BFS 初始化
        Queue<Station> queue = new LinkedList<>();
        Map<Station, Integer> hopCount = new HashMap<>();
        Set<Station> visited = new HashSet<>();

        queue.add(source);
        hopCount.put(source, 0);
        visited.add(source);

        while (!queue.isEmpty()) {
            Station current = queue.poll();
            int hops = hopCount.get(current);

            if (hops >= maxHops) continue;

            for (Station neighbor : current.getNeighbors().keySet()) {
                if (!visited.contains(neighbor)) {
                    int newHops = hops + 1;
                    hopCount.put(neighbor, newHops);
                    visited.add(neighbor);
                    queue.add(neighbor);

                    String line = findCommonLine(current, neighbor);
                    result.add("<" + neighbor.getName() + "，" + line + "，" + newHops + ">");
                }
            }
        }

        return result;
    }

    /*
     * 3.3查询所有的路径
     * 输入起点站和终点站的名称，返回一个包含连接起点和终点的所有路径的集合，每条路径经过的站点不重复（即不包含环路）；
     */
    public List<List<Station>> findAllPaths(String startName, String endName) {
        Map<String, Station> allStations = subway.getStations();
        if (!allStations.containsKey(startName) || !allStations.containsKey(endName)) {
            throw new IllegalArgumentException("站点不存在！");
        }

        List<List<Station>> allPaths = new ArrayList<>();
        Set<Station> visited = new HashSet<>();
        List<Station> currentPath = new ArrayList<>();

        dfs(allStations.get(startName), allStations.get(endName), visited, currentPath, allPaths);
        return allPaths;
    }

    /*
    * 3.4查询最短路径
    * 给定起点站和终点站的名称，利用Dijkstra算法，返回一条最短路径，该路径是一个包含从起点开始直到终点，所需要经过的所有站点的数组（输入不合规时进行异常处理）
    */
    public List<Station> findShortestPath(String startName, String endName) {
        Map<String, Station> allStations = subway.getStations();
        if (!allStations.containsKey(startName) || !allStations.containsKey(endName)) {
            throw new IllegalArgumentException("站点名称无效！");
        }

        Station start = allStations.get(startName);
        Station end = allStations.get(endName);

        Map<Station, Double> dist = new HashMap<>();
        Map<Station, Station> prev = new HashMap<>();
        Set<Station> visited = new HashSet<>();
        PriorityQueue<Station> pq = new PriorityQueue<>(Comparator.comparingDouble(dist::get));

        for (Station s : allStations.values()) {
            dist.put(s, Double.POSITIVE_INFINITY);
        }

        dist.put(start, 0.0);
        pq.add(start);

        while (!pq.isEmpty()) {
            Station current = pq.poll();
            if (visited.contains(current)) continue;
            visited.add(current);

            for (Map.Entry<Station, Double> entry : current.getNeighbors().entrySet()) {
                Station neighbor = entry.getKey();
                double distance = entry.getValue();

                if (dist.get(current) + distance < dist.get(neighbor)) {
                    dist.put(neighbor, dist.get(current) + distance);
                    prev.put(neighbor, current);
                    pq.add(neighbor);
                }
            }
        }

        List<Station> path = new LinkedList<>();
        for (Station at = end; at != null; at = prev.get(at)) {
            path.add(0, at);
        }

        if (path.get(0) != start) {
            return Collections.emptyList(); // unreachable
        }

        return path;
    }

    /*
     * 3.5构造简洁路径
     * 当找到最短乘车路径后，我们需要把它以更方便的形式呈现给用户。请实现一个方法，将路径以简洁的形式打印至标准输出，仅包含每一段乘车路径的起始（比如先坐1号线从A站到B站，在B站换 乘2号线到C站，等等）。
     */
    public static List<JourneySegment> getSimplePath(List<Station> path) {
        List<JourneySegment> segments = new ArrayList<>();
        if (path.size() < 2) return segments;

        String currentLine = findCommonLine(path.get(0), path.get(1));
        Station segmentStart = path.get(0);

        for (int i = 1; i < path.size(); i++) {
            Station prev = path.get(i - 1);
            Station curr = path.get(i);
            String line = findCommonLine(prev, curr);

            if (!line.equals(currentLine)) {
                segments.add(new JourneySegment(currentLine, segmentStart, prev));
                currentLine = line;
                segmentStart = prev;
            }
        }

        segments.add(new JourneySegment(currentLine, segmentStart, path.get(path.size() - 1)));
        return segments;
    }

    public static double calculatePathLength(List<Station> path) {
        double total = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            Station from = path.get(i);
            Station to = path.get(i + 1);
            total += from.getNeighbors().get(to);
        }
        return total;
    }
    private void dfs(Station current, Station target,
                     Set<Station> visited,
                     List<Station> currentPath,
                     List<List<Station>> allPaths) {

        visited.add(current);
        currentPath.add(current);

        if (current.equals(target)) {
            allPaths.add(new ArrayList<>(currentPath));
        } else {
            for (Station neighbor : current.getNeighbors().keySet()) {
                if (!visited.contains(neighbor)) {
                    dfs(neighbor, target, visited, currentPath, allPaths);
                }
            }
        }

        visited.remove(current);
        currentPath.remove(currentPath.size() - 1);
    }

    private static String findCommonLine(Station a, Station b) {
        for (String line : a.getLines()) {
            if (b.getLines().contains(line)) return line;
        }
        return "未知线路";
    }
}
