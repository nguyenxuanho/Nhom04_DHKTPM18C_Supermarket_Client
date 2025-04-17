package gui.components;

import com.formdev.flatlaf.FlatClientProperties;
import gui.panel.PanelDashboard;

import javax.naming.NamingException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.rmi.RemoteException;

public class MenuComponent extends JPanel{
    private static JPanel selectedPanel = null;
    private JPanel panel_header;
    private JLabel btn_Trangchu;
    private JLabel btn_Phong;
    private JLabel btn_Datphong;
    private JPanel QL;
    private JPanel jPanel_slide;
    private JLabel label_QL;
   public MenuComponent(JPanel TrangChu){
       JPanel Menu = new JPanel();
       Menu.setBorder(null);
       Menu.setBackground(new Color(188, 147, 109));
       Menu.setBounds(0, 0, 320, 1065);
       TrangChu.add(Menu);
       Menu.setLayout(null);

       JPanel panel = new JPanel();
       panel.setBorder(null);
       panel.setBackground(new Color(255, 128, 128));
       panel.setBounds(0, 0, 320, 224);
       Menu.add(panel);
       panel.setLayout(null);

//       Image avatar = new Image();
//       avatar.setBorderColor(Color.pink);
//       avatar.setBorderSize(3);
//       avatar.setBounds(103, 45, 110, 110);
//       avatar.setImage(new javax.swing.ImageIcon(getClass().getResource("/image/xuan.jpg")));
//       panel.add(avatar);

       JLabel nhanVienAccess = new JLabel("Mạch Ngọc Xuân");
       nhanVienAccess.setForeground(new Color(240, 240, 240));
       nhanVienAccess.setHorizontalAlignment(SwingConstants.CENTER);
       nhanVienAccess.setFont(new Font("Arial", Font.BOLD, 20));
       nhanVienAccess.setBounds(58, 166, 205, 27);
       panel.add(nhanVienAccess);

       JScrollPane scrollPane = new JScrollPane();
       scrollPane.setBorder(null);
       scrollPane.setBounds(0, 222, 320, 843);
       Menu.add(scrollPane);

       JPanel panel_1 = new JPanel();
       panel_1.setPreferredSize(new Dimension(230, 843));
       panel_1.setBackground(new Color(240, 240, 240));
       scrollPane.setViewportView(panel_1);
       panel_1.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

       JPanel trangchu = new JPanel();
       trangchu.setPreferredSize(new Dimension(270, 44));
       trangchu.putClientProperty(FlatClientProperties.STYLE, "arc: 15");
       trangchu.setOpaque(true);
       trangchu.setLayout(new BorderLayout(0, 0));
       panel_1.add(trangchu);

       if (selectedPanel != null) {
           selectedPanel.setBackground(new Color(240, 240, 240));
       }
       trangchu.setBackground(new Color(187, 222, 251));
       selectedPanel = trangchu;

       trangchu.addMouseListener(new MouseAdapter() {
           @Override
           public void mouseClicked(MouseEvent e) {
               QL.removeAll();
               QL.add(jPanel_slide);
               QL.revalidate();
               QL.repaint();
               label_QL.setText("Trang chủ");
               if (selectedPanel != null) {
                   selectedPanel.setBackground(new Color(240, 240, 240));
               }
               trangchu.setBackground(new Color(187, 222, 251));
               selectedPanel = trangchu;
           }
           @Override
           public void mouseEntered(MouseEvent e) {
               if (selectedPanel != trangchu) {
                   trangchu.setBackground(Color.PINK);
               }
           }
           @Override
           public void mouseExited(MouseEvent e) {
               if (selectedPanel != trangchu) {
                   trangchu.setBackground(new Color(240, 240, 240));
               }
           }
       });



       btn_Trangchu = new JLabel("   Trang chủ");
       btn_Trangchu.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 0));
       btn_Trangchu.setIcon(new ImageIcon(getClass().getResource("/image/home.png")));
       btn_Trangchu.setFont(new Font("Arial", Font.PLAIN, 20));
       btn_Trangchu.setHorizontalAlignment(SwingConstants.LEFT);
       trangchu.add(btn_Trangchu);


       panel_header = new JPanel();
       panel_header.setBackground(new Color(192, 192, 192));
       panel_header.setBounds(320, 0, 1600, 111);
       TrangChu.add(panel_header);
       panel_header.setLayout(null);


       QL = new JPanel();
       QL.setBounds(320, 111, 1600, 954);
       TrangChu.add(QL);

       QL.setLayout(null);

       jPanel_slide = new JPanel();
       jPanel_slide.setBounds(0, 0, 1600, 954);
       jPanel_slide.setLayout(null);
       QL.add(jPanel_slide);

       label_QL = new JLabel("Trang chủ");
       label_QL.setFont(new Font("Arial", Font.BOLD, 25));
       label_QL.setHorizontalAlignment(SwingConstants.LEFT);
       label_QL.setBounds(45, 31, 285, 49);
       panel_header.add(label_QL);



   }
}
