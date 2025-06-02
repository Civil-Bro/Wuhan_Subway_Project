package framepkg;

import subway.FareCalculator;
import subway.PathFinder;
import subway.Station;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

class QueryPathCostPanel extends JPanel {
    private JTextField input1, input2, input3;
    private JTextArea output;
    private final PathFinder pathFinder;
    private List<List<Station>> paths=new ArrayList<List<Station>>();
    public QueryPathCostPanel(PathFinder pathFinder) {
        this.pathFinder = pathFinder;
        setLayout(new BorderLayout());

        // 面板控制区域
        //label
        JPanel topPanel = new JPanel(new GridLayout(4, 1));
        JLabel label = new JLabel("路径费用计算 界面", SwingConstants.CENTER);
        topPanel.add(label);
        //input
        JPanel inputPanel = new JPanel();
        input1 = new JTextField(12);
        input2 = new JTextField(12);
        input3 = new JTextField(12);
        inputPanel.add(new JLabel("起点站："));
        inputPanel.add(input1);
        inputPanel.add(new JLabel("终点站："));
        inputPanel.add(input2);
        inputPanel.add(new JLabel("所选路径："));
        inputPanel.add(input3);
        topPanel.add(inputPanel);
        //button
        JButton execBtn1 = new JButton("查询所有路径");
        execBtn1.addActionListener(this::onExecute1);
        topPanel.add(execBtn1);
        JButton execBtn2 = new JButton("计算路径价格");
        execBtn2.addActionListener(this::onExecute2);
        topPanel.add(execBtn2);

        add(topPanel, BorderLayout.NORTH);

        // 输出区域
        output = new JTextArea();
        output.setEditable(false);
        output.setFont(new Font("Monospaced", Font.PLAIN, 13));
        add(new JScrollPane(output), BorderLayout.CENTER);
    }
    private void onExecute1(ActionEvent e) {
        output.setText("");
        String in1 = input1.getText().trim();
        String in2 = input2.getText().trim();
        paths.clear();
        paths.add(pathFinder.findShortestPath(in1, in2));
        paths.addAll(pathFinder.findAllPaths(in1, in2));
        output.append("最短");
        int i = 0;
        while (i < paths.size()) {
            output.append("路径："+i+paths.get(i) + "\n");
            i++;
        }

    }
    private void onExecute2(ActionEvent e) {
        if(paths.size()==0){
            output.append("人必须知道自己所走的路！");
            return;
        }
        output.setText("");
        String in3 = input3.getText().trim();
        int index = in3.isEmpty() ? 0 : Integer.parseInt(in3);
        double nm = FareCalculator.calculateFare(paths.get(index), 0);
        double wt = FareCalculator.calculateFare(paths.get(index), 1);
        double dp = FareCalculator.calculateFare(paths.get(index), 2);
        output.append("单程车票价：" + nm + " 元\n");
        output.append("武汉通票价：" + wt + " 元\n");
        output.append("日票价：" + dp + " 元\n");
    }
}
