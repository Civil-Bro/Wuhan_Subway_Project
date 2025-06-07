package subway;

import java.util.*;

public class FareCalculator {

    public static double calculateFare(List<Station> path,int model) {
        double distance = PathFinder.calculatePathLength(path);
        return calculateFare(distance,model);
    }
    public static double calculateFare(double distance,int model) {
        switch (model) {
            case 0:return calculateNormalFare(distance);
            case 1:return calculateWuhanTongFare(distance);
            case 2:return 0;
            default:throw new IllegalArgumentException("支付方式不合法");
        }
    }
    public static double calculateNormalFare(double distance) {
        //官网打不开，但是查到的票价规则如下
        if (distance <= 9) return 2;
        else if (distance <= 14) return 3;
        else return 3 + Math.ceil((distance - 14) / 2.0);
    }

    public static double calculateWuhanTongFare(double distance) {
        return Math.round(calculateNormalFare(distance) * 0.9 * 10.0) / 10.0; // 保留1位小数
    }

    public static double calculateDayPassFare(String type) {
        switch (type) {
            case "1日票": return 18;
            case "3日票": return 45;
            case "7日票": return 90;
            default : throw new IllegalArgumentException("票种类型不合法");
        }
    }
}
