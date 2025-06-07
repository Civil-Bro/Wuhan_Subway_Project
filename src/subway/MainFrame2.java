package subway;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Set;

public class MainFrame2 extends JFrame {
    private SubwaySystem subway;
    private PathFinder pathFinder;
    private JTextArea output;
    private JComboBox<String> functionSelect;
    private JTextField input1, input2, input3;

    public MainFrame2(SubwaySystem subway) {
        this.subway = subway;
        this.pathFinder = new PathFinder(subway);
        initUI();
    }

    private void initUI() {
        setTitle("武汉地铁模拟系统");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 功能选择
        JPanel topPanel = new JPanel(new GridLayout(3, 1));

        JPanel functionPanel = new JPanel();
        functionPanel.add(new JLabel("选择功能："));
        functionSelect = new JComboBox<>(new String[]{
                "3.1 查询中转站",
                "3.2 查询跳数范围内邻近站点",
                "3.3 查询所有路径（预览）",
                "3.4 查询最短路径",
                "3.5 简洁路径（换乘信息）",
                "3.6 路径费用计算",
                "3.7 其他票种费用"
        });
        functionPanel.add(functionSelect);
        topPanel.add(functionPanel);

        // 输入区域
        JPanel inputPanel = new JPanel();
        input1 = new JTextField(12);
        input2 = new JTextField(12);
        input3 = new JTextField(6);
        inputPanel.add(new JLabel("输入1："));
        inputPanel.add(input1);
        inputPanel.add(new JLabel("输入2："));
        inputPanel.add(input2);
        inputPanel.add(new JLabel("输入3（选填）："));
        inputPanel.add(input3);
        topPanel.add(inputPanel);

        JButton execBtn = new JButton("执行");
        execBtn.addActionListener(this::onExecute);
        topPanel.add(execBtn);

        add(topPanel, BorderLayout.NORTH);

        // 输出区域
        output = new JTextArea();
        output.setEditable(false);
        output.setFont(new Font("Monospaced", Font.PLAIN, 13));
        add(new JScrollPane(output), BorderLayout.CENTER);
    }

    private void onExecute(ActionEvent e) {
        String in1 = input1.getText().trim();
        String in2 = input2.getText().trim();
        String in3 = input3.getText().trim();
        output.setText("");
        try {
            int choice = functionSelect.getSelectedIndex();
            switch (choice) {
                case 0: // 3.1 查询中转站
                    Set<String> result = subway.getTransferStations();
                    for (String s : result) {
                        output.append(s + "\n");
                    }
                    break;
                case 1: // 3.2 查询某站 n 跳内邻近站
                    int n = Integer.parseInt(in2);
                    Set<String> nearby = pathFinder.getNearbyStations(in1, n);
                    for (String s : nearby) {
                        output.append(s + "\n");
                    }
                    break;
                case 2: // 3.3 查询所有路径（最多展示前5条）
                    List<List<Station>> paths = pathFinder.findAllPaths(in1, in2);
                    for (int i = 0; i < Math.min(5, paths.size()); i++) {
                        output.append(paths.get(i) + "\n");
                    }
                    break;
                case 3: // 3.4 最短路径
                    List<Station> path = pathFinder.findShortestPath(in1, in2);
                    output.append(path + "\n");
                    break;
                case 4: // 3.5 简洁路径（换乘信息）
                    List<Station> shortPath = pathFinder.findShortestPath(in1, in2);
                    List<JourneySegment> segments = PathFinder.getSimplePath(shortPath);
                    for (JourneySegment seg : segments) {
                        output.append(seg.toString() + "\n");
                    }
                    break;
                case 5: // 3.6 普通票费用
                    List<List<Station>> farePaths = pathFinder.findAllPaths(in1, in2);
                    int index = in3.isEmpty() ? 0 : Integer.parseInt(in3);
                    double fare = FareCalculator.calculateFare(farePaths.get(index), 0);
                    output.append("普通票费用：" + fare + " 元\n");
                    break;
                case 6: // 3.7 其他票种费用
                    List<List<Station>> ticketPaths = pathFinder.findAllPaths(in1, in2);
                    int idx = in3.isEmpty() ? 0 : Integer.parseInt(in3);
                    double wt = FareCalculator.calculateFare(ticketPaths.get(idx), 1);
                    double dp = FareCalculator.calculateFare(ticketPaths.get(idx), 2);
                    output.append("武汉通票价：" + wt + " 元\n");
                    output.append("日票费用：" + dp + " 元\n");
                    break;
                default:
                    output.setText("无效的选项");
                    break;
            }
        } catch (Exception ex) {
            output.setText("发生错误：" + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                SubwaySystem subway = new SubwaySystem("C:\\Users\\13362\\eclipse-workspace\\Msys\\src\\subway.txt");
                MainFrame2 frame = new MainFrame2(subway);
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
