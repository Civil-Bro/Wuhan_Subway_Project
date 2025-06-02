package framepkg;

import subway.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Set;


class QueryTransferPanel extends JPanel {
    private JTextArea output;
    private final SubwaySystem subway;
    public QueryTransferPanel(SubwaySystem subway) {
        this.subway = subway;
        setLayout(new BorderLayout());

        // 面板控制区域
        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        JLabel label = new JLabel("查询中转站 界面", SwingConstants.CENTER);
        topPanel.add(label);

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
        Set<String> result = subway.getTransferStations();
        result.forEach(s -> output.append(s + "\n"));
    }
}
