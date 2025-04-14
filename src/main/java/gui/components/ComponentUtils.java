package gui.components;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class ComponentUtils {
    public static TitledBorder getTitleBorder(String title){
        Border border = BorderFactory.createLineBorder(new Color(33, 150, 243), 2);
        TitledBorder titledBorder = BorderFactory.createTitledBorder(border,
                title,
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 16),
                Color.WHITE);

        titledBorder.setTitleColor(new Color(33, 150, 243));
        titledBorder.setBorder(BorderFactory.createLineBorder(new Color(33, 150, 243), 2));
        return titledBorder;
    }

    public static void setButton(JButton button, Color color){
        button.setPreferredSize(new Dimension(100, 35));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 13)); // 👈 Font to, đẹp
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false); // bỏ viền focus xấu
    }

    public static void setJcombobox(JComboBox<String> comboBox){

        comboBox.setFont(new Font("Arial", Font.PLAIN, 14)); // font chữ rõ ràng
        comboBox.setBackground(Color.WHITE); // nền trắng nhẹ nhàng
        comboBox.setForeground(Color.BLACK); // chữ đen
        comboBox.setCursor(new Cursor(Cursor.HAND_CURSOR)); // con trỏ dạng tay khi rê chuột
    }

    public static void setButtonMain(JButton button){
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBackground(new Color(53, 78, 211));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(7, 25, 7, 25)); // padding nhẹ
        button.setFont( new Font("Arial", Font.BOLD, 14));
    }

    public static void setTable(JTable table){
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);
        table.setFocusable(false);
        table.setDefaultEditor(Object.class, null);
        table.setAutoCreateRowSorter(true);
        table.getTableHeader().setFont(new Font("Arial", Font.PLAIN, 15));
        table.getTableHeader().setBackground(new Color(33, 150, 243));
        table.setRowHeight(40);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.setSelectionBackground(new Color(33, 150, 243));
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(Color.LIGHT_GRAY);

        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setForeground(Color.WHITE);
        tableHeader.setPreferredSize(new Dimension(tableHeader.getWidth(), 40));
    }
}
