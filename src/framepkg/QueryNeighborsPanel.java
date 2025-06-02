package framepkg;

import subway.PathFinder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Set;

class QueryNeighborsPanel extends JPanel {
    private JTextField input1, input2;
    private JTextArea output;
    private final PathFinder pathFinder;
    public QueryNeighborsPanel(PathFinder pathFinder) {
        this.pathFinder = pathFinder;
        setLayout(new BorderLayout());

        // 面板控制区域
        //label
        JPanel topPanel = new JPanel(new GridLayout(3, 1));
        JLabel label = new JLabel("查询邻近站点 界面", SwingConstants.CENTER);
        topPanel.add(label);
        //input
        JPanel inputPanel = new JPanel();
        input1 = new JTextField(12);
        input2 = new JTextField(12);
        inputPanel.add(new JLabel("站点："));
        inputPanel.add(input1);
        inputPanel.add(new JLabel("范围："));
        inputPanel.add(input2);
        topPanel.add(inputPanel);
        //button
        JButton execBtn = new JButton("查询");
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
        output.setText("");
        String in1 = input1.getText().trim();
        String in2 = input2.getText().trim();
        int n = Integer.parseInt(in2);
        Set<String> nearby = pathFinder.getNearbyStations(in1, n);
        nearby.forEach(s -> output.append(s + "\n"));
    }
}
