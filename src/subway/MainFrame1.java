package subway;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Set;

public class MainFrame1 extends JFrame implements ActionListener {
    private SubwaySystem subwaySystem;        // 假设的地铁系统类
    private PathFinder pathFinder;            // 假设的路径搜索类

    private JComboBox<String> functionCombo;  // 功能选择下拉框
    private JPanel cardsPanel;               // CardLayout 容器
    private CardLayout cardLayout;

    // 输入面板组件
    private JPanel cardShortest, cardNearby, cardFareNormal, cardFareOther;
    private JTextField shortestStartField, shortestEndField;
    private JTextField nearbyStationField, nearbyStepsField;
    private JTextField fareNormalStartField, fareNormalEndField, fareNormalPathIndexField;
    private JTextField fareOtherStartField, fareOtherEndField, fareOtherPathIndexField;
    private JComboBox<String> ticketTypeCombo;
    private JButton btnFindNormalPaths, btnCalcNormalFare;
    private JButton btnFindOtherPaths, btnCalcOtherFare;
    private JButton btnSearchShortest, btnSearchNearby;

    private JTextArea outputArea;

    public MainFrame1() {
        super("武汉地铁模拟系统");
        // 初始化地铁系统和路径查找器（假设有相应构造函数）
        subwaySystem = new SubwaySystem();
        pathFinder = new PathFinder(subwaySystem);

        initUI();
    }

    private void initUI() {
        // 使用 BorderLayout 将窗口分为上中下三部分
        this.setLayout(new BorderLayout());

        // ----- 顶部：功能选择 -----
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("功能选择："));
        functionCombo = new JComboBox<>(new String[]{
                "查询最短路径", "查询邻近站", "普通票费用", "其他票种费用"
        });
        functionCombo.addActionListener(this);
        topPanel.add(functionCombo);
        this.add(topPanel, BorderLayout.NORTH);

        // 中间：卡片布局的输入面板
        cardLayout = new CardLayout();
        cardsPanel = new JPanel(cardLayout);

        // 查询最短路径面板
        cardShortest = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cardShortest.setBorder(BorderFactory.createTitledBorder("查询最短路径"));
        cardShortest.add(new JLabel("起点站名："));
        shortestStartField = new JTextField(10);
        cardShortest.add(shortestStartField);
        cardShortest.add(new JLabel("终点站名："));
        shortestEndField = new JTextField(10);
        cardShortest.add(shortestEndField);
        btnSearchShortest = new JButton("查询路径");
        btnSearchShortest.addActionListener(this);
        cardShortest.add(btnSearchShortest);
        cardsPanel.add(cardShortest, "shortest");

        // 查询邻近站面板
        cardNearby = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cardNearby.setBorder(BorderFactory.createTitledBorder("查询邻近站"));
        cardNearby.add(new JLabel("站点名称："));
        nearbyStationField = new JTextField(10);
        cardNearby.add(nearbyStationField);
        cardNearby.add(new JLabel("跳数范围："));
        nearbyStepsField = new JTextField(5);
        cardNearby.add(nearbyStepsField);
        btnSearchNearby = new JButton("查询站点");
        btnSearchNearby.addActionListener(this);
        cardNearby.add(btnSearchNearby);
        cardsPanel.add(cardNearby, "nearby");

        // 普通票费用面板
        cardFareNormal = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cardFareNormal.setBorder(BorderFactory.createTitledBorder("普通票费用"));
        cardFareNormal.add(new JLabel("起点站名："));
        fareNormalStartField = new JTextField(10);
        cardFareNormal.add(fareNormalStartField);
        cardFareNormal.add(new JLabel("终点站名："));
        fareNormalEndField = new JTextField(10);
        cardFareNormal.add(fareNormalEndField);
        btnFindNormalPaths = new JButton("查询可选路径");
        btnFindNormalPaths.addActionListener(this);
        cardFareNormal.add(btnFindNormalPaths);
        cardFareNormal.add(new JLabel("路径编号："));
        fareNormalPathIndexField = new JTextField(3);
        cardFareNormal.add(fareNormalPathIndexField);
        btnCalcNormalFare = new JButton("计算费用");
        btnCalcNormalFare.addActionListener(this);
        cardFareNormal.add(btnCalcNormalFare);
        cardsPanel.add(cardFareNormal, "normalFare");

        // 其他票种费用面板
        cardFareOther = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cardFareOther.setBorder(BorderFactory.createTitledBorder("其他票种费用"));
        cardFareOther.add(new JLabel("起点站名："));
        fareOtherStartField = new JTextField(10);
        cardFareOther.add(fareOtherStartField);
        cardFareOther.add(new JLabel("终点站名："));
        fareOtherEndField = new JTextField(10);
        cardFareOther.add(fareOtherEndField);
        cardFareOther.add(new JLabel("票种："));
        ticketTypeCombo = new JComboBox<>(new String[]{"学生票", "一日票", "三日票", "老年票"});
        cardFareOther.add(ticketTypeCombo);
        btnFindOtherPaths = new JButton("查询可选路径");
        btnFindOtherPaths.addActionListener(this);
        cardFareOther.add(btnFindOtherPaths);
        cardFareOther.add(new JLabel("路径编号："));
        fareOtherPathIndexField = new JTextField(3);
        cardFareOther.add(fareOtherPathIndexField);
        btnCalcOtherFare = new JButton("计算费用");
        btnCalcOtherFare.addActionListener(this);
        cardFareOther.add(btnCalcOtherFare);
        cardsPanel.add(cardFareOther, "otherFare");

        this.add(cardsPanel, BorderLayout.CENTER);

        // ----- 底部：输出提示/结果区域 -----
        outputArea = new JTextArea(10, 50);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        this.add(scrollPane, BorderLayout.SOUTH);

        // 窗口基本设置
        this.pack();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        // 初始化显示第一个功能的界面和提示
        functionCombo.setSelectedIndex(0);
        updateView("查询最短路径");
    }

    // 处理按钮和组合框事件
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == functionCombo) {
            // 功能选择变化：切换输入面板和输出提示
            String func = (String) functionCombo.getSelectedItem();
            if(func!=null)updateView(func);
        } else if (src == btnSearchShortest) {
            // 处理查询最短路径按钮
            String start = shortestStartField.getText().trim();
            String end = shortestEndField.getText().trim();
            if (start.isEmpty() || end.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请输入起点和终点！");
                return;
            }
            // 调用 PathFinder 获取最短路径（假设方法名为 findShortestPath）
            List<Station> path = pathFinder.findShortestPath(start, end);
            outputArea.setText("最短路径：\n");
            if (path != null) {
                outputArea.append(" -> " + String.join(" -> ", path.toString()));
            } else {
                outputArea.append("未找到路径。");
            }
        } else if (src == btnSearchNearby) {
            // 处理查询邻近站按钮
            String station = nearbyStationField.getText().trim();
            String stepsStr = nearbyStepsField.getText().trim();
            if (station.isEmpty() || stepsStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请输入站点名称和跳数范围！");
                return;
            }
            int steps = Integer.parseInt(stepsStr);
            Set<String> nearby = pathFinder.getNearbyStations(station, steps);
            outputArea.setText("距离“" + station + "”跳数 ≤ " + steps + " 的站点有：\n");
            if (nearby != null) {
                for (String s : nearby) {
                    outputArea.append(s + "\n");
                }
            } else {
                outputArea.append("未找到邻近站点。");
            }
        } else if (src == btnFindNormalPaths) {
            // 普通票查询路径列表
            String start = fareNormalStartField.getText().trim();
            String end = fareNormalEndField.getText().trim();
            if (start.isEmpty() || end.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请输入起点和终点！");
                return;
            }
            // 假设 PathFinder 的方法 findPaths 返回多个路径（每条路径为站点列表）
            List<List<Station>> paths = pathFinder.findAllPaths(start, end);
            outputArea.setText("可选路径列表（编号：路线站点）：\n");
            if (paths != null && !paths.isEmpty()) {
                int idx = 1;
                for (List<Station> path : paths) {
                    outputArea.append(idx + "：");
                    outputArea.append(String.join("->", path.toString()) + "\n");
                    idx++;
                }
                outputArea.append("请输入“路径编号”并点击“计算费用”。");
            } else {
                outputArea.append("未找到可选路径。");
            }
        } else if (src == btnCalcNormalFare) {
            // 普通票计算费用
            String indexStr = fareNormalPathIndexField.getText().trim();
            if (indexStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请输入路径编号！");
                return;
            }
            int index = Integer.parseInt(indexStr) - 1;
            // 重新获取路径列表并选择
            String start = fareNormalStartField.getText().trim();
            String end = fareNormalEndField.getText().trim();
            List<List<Station>> paths = pathFinder.findAllPaths(start, end);
            if (paths != null && index >= 0 && index < paths.size()) {
                List<Station> chosenPath = paths.get(index);
                double fare = FareCalculator.calculateFare(chosenPath,0);
                outputArea.setText("选择的路径：\n");
                outputArea.append(String.join("->", chosenPath.toString()) + "\n");
                outputArea.append("普通票费用：" + fare + " 元");
            } else {
                JOptionPane.showMessageDialog(this, "路径编号无效！");
            }
        } else if (src == btnFindOtherPaths) {
            // 其他票种查询路径（流程同上）
            String start = fareOtherStartField.getText().trim();
            String end = fareOtherEndField.getText().trim();
            if (start.isEmpty() || end.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请输入起点和终点！");
                return;
            }
            List<List<Station>> paths = pathFinder.findAllPaths(start, end);
            outputArea.setText("可选路径列表（编号：路线站点）：\n");
            if (paths != null && !paths.isEmpty()) {
                int idx = 1;
                for (List<Station> path : paths) {
                    outputArea.append(idx + "：");
                    outputArea.append(String.join("->", path.toString()) + "\n");
                    idx++;
                }
                outputArea.append("请选择路径编号并点击“计算费用”。");
            } else {
                outputArea.append("未找到可选路径。");
            }
        } else if (src == btnCalcOtherFare) {
            // 其他票种计算费用
            String indexStr = fareOtherPathIndexField.getText().trim();
            if (indexStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请输入路径编号！");
                return;
            }
            int index = Integer.parseInt(indexStr) - 1;
            String start = fareOtherStartField.getText().trim();
            String end = fareOtherEndField.getText().trim();
            List<List<Station>> paths = pathFinder.findAllPaths(start, end);
            if (paths != null && index >= 0 && index < paths.size()) {
                List<Station> chosenPath = paths.get(index);
                Integer ticketType = (Integer) ticketTypeCombo.getSelectedItem();
                ticketType = (ticketType == null ? 0 : ticketType);
                double fare = FareCalculator.calculateFare(chosenPath,ticketType);
                outputArea.setText("选择的路径：\n");
                outputArea.append(String.join("->", chosenPath.toString()) + "\n");
                outputArea.append(ticketType + "费用：" + fare + " 元");
            } else {
                JOptionPane.showMessageDialog(this, "路径编号无效！");
            }
        }
    }

    // 根据功能名称更新卡片和提示
    private void updateView(String func) {
        switch (func) {
            case "查询最短路径":
                cardLayout.show(cardsPanel, "shortest");
                outputArea.setText("输入格式：起点站名  终点站名\n例如：光谷广场 黄浦路");
                break;
            case "查询邻近站":
                cardLayout.show(cardsPanel, "nearby");
                outputArea.setText("输入格式：站点名称  跳数范围（整数）\n例如：光谷广场 3");
                break;
            case "普通票费用":
                cardLayout.show(cardsPanel, "normalFare");
                outputArea.setText("请输入起点和终点，点击“查询可选路径”列出路线，然后输入路径编号并点击“计算费用”。");
                break;
            case "其他票种费用":
                cardLayout.show(cardsPanel, "otherFare");
                outputArea.setText("请选择票种后，输入起点和终点，点击“查询可选路径”，再输入路径编号并点击“计算费用”。");
                break;
        }
    }

    // 主方法启动应用
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame1 frame = new MainFrame1();
            frame.setVisible(true);
        });
    }
}
