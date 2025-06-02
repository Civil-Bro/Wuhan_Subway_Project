package framepkg;

import javax.swing.*;
import java.awt.*;


class NavigationPanel extends JPanel {
    public NavigationPanel(CardLayout cardLayout, JPanel cardPanel, String[] items) {
        // 向卡片面板中添加功能子面板，并为每张卡片指定名称

        setLayout(new BorderLayout());
        JList<String> list = new JList<>(items);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);  // 默认选中第一个
        JScrollPane scrollPane = new JScrollPane(list);
        add(scrollPane, BorderLayout.CENTER);

        // 监听列表选择，当选择项变化时切换右侧卡片面板
        list.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selected = list.getSelectedValue();
                cardLayout.show(cardPanel, selected);
            }
        });
    }
}
