package gui.components;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
        button.setBackground(new Color(30, 144, 255));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(7, 25, 7, 25)); // padding nhẹ
        button.setFont( new Font("Arial", Font.BOLD, 14));
    }

    public static void setTable(JTable table){
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);
        table.setFocusable(false);
        table.setShowGrid(true);
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


        // Căn giữa chữ trong tất cả các cột
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }



        // Tạo renderer tùy chỉnh với hiệu ứng hover
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            private int hoverRow = -1;

            {
                // Thêm mouse listener để theo dõi hàng đang hover
                table.addMouseMotionListener(new MouseAdapter() {
                    @Override
                    public void mouseMoved(MouseEvent e) {
                        int row = table.rowAtPoint(e.getPoint());
                        if (row != hoverRow) {
                            hoverRow = row;
                            table.repaint();
                        }
                        // Thiết lập cursor HAND khi di chuyển chuột vào bảng
                        table.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        if (hoverRow != -1) {
                            hoverRow = -1;
                            table.repaint();
                        }
                        // Trả về cursor mặc định khi chuột rời khỏi bảng
                        table.setCursor(Cursor.getDefaultCursor());
                    }
                });
            }

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                // Đặt màu nền tùy theo trạng thái
                if (isSelected) {
                    c.setBackground(new Color(63, 167, 236)); // Màu khi chọn
                } else if (row == hoverRow) {
                    c.setBackground(new Color(63, 167, 236)); // Màu khi hover
                } else {
                    c.setBackground(Color.WHITE); // Màu mặc định
                }

                return c;
            }
        });


    }
}
