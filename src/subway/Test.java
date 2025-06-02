package subway;
import java.util.*;

public class Test {
    public static void main(String[] args) {
        SubwaySystem subway = new SubwaySystem("C:\\Users\\13362\\eclipse-workspace\\Msys\\src\\subway.txt");
        PathFinder pathFinder = new PathFinder(subway);

        //3.1查询地铁中转站
        System.out.println(subway.getTransferStations());
        //3.2查询某站点距离小于 n 的所有站点
        System.out.println(pathFinder.getNearbyStations("华中科技大学",4));
        //3.3查询所有的路径
        List<List<Station>> paths = pathFinder.findAllPaths("华中科技大学", "青鱼嘴");
        for(int i=0; i<4; i++){
            System.out.println(paths.get(i));
        }
        //3.4查询最短路径
        try{
            List<Station> shortestPath = pathFinder.findShortestPath("华中科技大学","青鱼嘴");
            System.out.println(shortestPath);
            //3.5构造简洁路径
            System.out.println(PathFinder.getSimplePath(shortestPath));
        }catch(IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
        //3.6选择路径的费用
        System.out.println(FareCalculator.calculateFare(paths.get(7),0));

        //3.7其他模式的费用
        System.out.println(FareCalculator.calculateFare(paths.get(7),1));
        System.out.println(FareCalculator.calculateFare(paths.get(7),2));
    }
}
