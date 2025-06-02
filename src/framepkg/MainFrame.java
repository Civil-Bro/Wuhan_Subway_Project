package framepkg;


import subway.PathFinder;
import subway.SubwaySystem;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private final SubwaySystem subway;
    private final PathFinder pathFinder;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private  NavigationPanel navPanel;
    public MainFrame(SubwaySystem subway) {
        //内容初始化
        this.subway = subway;
        this.pathFinder = new PathFinder(subway);

        //Frame设置
        setTitle("武汉地铁系统");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());

        // 创建右侧的卡片面板，用来切换功能面板
        cardPanel = new JPanel();
        cardLayout = new CardLayout();
        cardPanel.setLayout(cardLayout);
        String[] items= new String[]{
                "查询中转站",
                "查询邻近站点",
                "查询所有路径",
                "查询最短路径",
                "查询最短换乘路径",
                "查询路径费用",
        };
        cardPanel.add(new QueryTransferPanel(subway), items[0]);
        cardPanel.add(new QueryNeighborsPanel(pathFinder), items[1]);
        cardPanel.add(new QueryAllPathsPanel(pathFinder), items[2]);
        cardPanel.add(new QueryShortestPathPanel(pathFinder), items[3]);
        cardPanel.add(new QueryTransferPathPanel(pathFinder), items[4]);
        cardPanel.add(new QueryPathCostPanel(pathFinder), items[5]);

        // 创建左侧导航面板，CardLayout 实例和卡片容器
        navPanel = new NavigationPanel(cardLayout, cardPanel, items);

        //布局排版
        getContentPane().add(cardPanel, BorderLayout.CENTER);
        getContentPane().add(navPanel, BorderLayout.WEST);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                SubwaySystem subway = new SubwaySystem("C:\\Users\\13362\\eclipse-workspace\\Msys\\src\\subway.txt");
                MainFrame frame = new MainFrame(subway);
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}

