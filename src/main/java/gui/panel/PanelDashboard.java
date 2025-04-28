package gui.panel;
import chart.ModelChart;
import chart.ModelPieChart;
import chart.PieChart;
import gui.components.ComponentUtils;
import io.github.cdimascio.dotenv.Dotenv;
import net.datafaker.Faker;
import service.ChiTietHoaDonService;
import service.SanPhamService;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static gui.panel.RmiServiceLocator.chiTietHoaDonService;
import static gui.panel.RmiServiceLocator.sanPhamService;

public class PanelDashboard extends JPanel implements MouseListener, ActionListener {
    private chart.Chart chart;
    private chart.PieChart pieChart;
    private final JTable jTableContent;
    private final DefaultTableModel dataModel;

    public PanelDashboard () throws NamingException, RemoteException {
        setLayout(new BorderLayout());

        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Tạo tiêu đề căn giữa
        JLabel titleLabel = new JLabel("Giao diện trang chủ", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10)); // tạo khoảng cách

        add(titleLabel, BorderLayout.NORTH);


        // Tên cột cho bảng
        String[] columnNames = {"Mã sản phẩm", "Tên sản phẩm", "Số lượng", "Hạn sử dụng"};


        // Tạo model cho bảng với data
        dataModel = new DefaultTableModel(columnNames, 0);


        jTableContent = new JTable(dataModel);



        ComponentUtils.setTable(jTableContent);

        sanPhamService.getDanhSachSanPhamSapHetHan().forEach(sanPham -> {
            dataModel.addRow ( new Object[]{
                    sanPham.getMaSanPham(),
                    sanPham.getTenSanPham(),
                    sanPham.getSoLuongTon(),
                    sanPham.getHanSuDung()
            });
        });

        // Tùy chỉnh độ cao hàng và font chữ nếu cần
        jTableContent.setRowHeight(30);
        jTableContent.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jTableContent.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));

        // Thêm bảng vào JScrollPane để có thể cuộn
        JScrollPane scrollPane = new JScrollPane(jTableContent);

        Box boxTable = Box.createHorizontalBox();

        boxTable.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder( 10, 10, 10, 10),
                ComponentUtils.getTitleBorder("Danh sách sản phẩm hết hạn")
        ));

        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));


        chart = new chart.Chart();

        chart.addLegend("Doanh thu trong ngày", new Color(245, 189, 135));

        chiTietHoaDonService.doanhThuTheoNgayGanNhat(23).entrySet()
                .stream().sorted(Map.Entry.<LocalDate, Double>comparingByKey())
                .forEach(entry -> {
            chart.addData(new ModelChart(entry.getKey().toString(), new double[]{entry.getValue()}));
        });



        pieChart = new PieChart();

        pieChart.setPreferredSize(new Dimension(600, 600));

        pieChart.setFont(new java.awt.Font("sansserif", 1, 15)); // NOI18N

        pieChart.setChartType(PieChart.PeiChartType.DONUT_CHART);

        List<Map.Entry<String, Integer>> dataList = new ArrayList<>(sanPhamService.thongKeSoLuongTheoTrangThai().entrySet());

        List<Color> colorList = List.of(
                new Color(117, 229, 56),   // xanh lá sáng
                new Color(99, 194, 255),   // xanh dương nhạt
                new Color(255, 193, 7),    // vàng tươi
                new Color(40, 167, 69),    // xanh lá đậm
                new Color(250, 34, 188),   // hồng đậm
                new Color(255, 87, 34),    // cam đỏ
                new Color(255, 152, 0),    // cam vàng
                new Color(156, 39, 176),   // tím
                new Color(33, 150, 243),   // xanh da trời
                new Color(0, 188, 212),    // xanh ngọc
                new Color(76, 175, 80),    // xanh lá cây
                new Color(205, 220, 57),   // vàng chanh
                new Color(255, 138, 101),  // cam nhạt
                new Color(103, 58, 183),   // tím đậm
                new Color(121, 85, 72),    // nâu
                new Color(96, 125, 139),   // xanh xám
                new Color(255, 235, 59),   // vàng chói
                new Color(244, 67, 54),    // đỏ tươi
                new Color(0, 150, 136),    // xanh teal
                new Color(3, 169, 244)     // xanh nước biển
        );




        for (int i = 0; i < dataList.size(); i++) {
            String label = dataList.get(i).getKey();
            int value = dataList.get(i).getValue();

            Color color = colorList.get(i % colorList.size()); // Quay vòng nếu màu ít hơn dữ liệu

            pieChart.addData(new ModelPieChart(label, value, color));
        }



        boxTable.add(scrollPane);
        Box boxContent = Box.createVerticalBox();
        Box boxChart = Box.createHorizontalBox();

        Box boxChartDetail1 = Box.createVerticalBox();
        boxChartDetail1.add(chart);

        JLabel chartLabel1 = new JLabel("Doanh thu 7 ngày gần đây");
        chartLabel1.setFont(new Font("SansSerif", Font.BOLD, 16));
        chartLabel1.setAlignmentX(Component.CENTER_ALIGNMENT);
        chartLabel1.setForeground(new Color(33, 37, 41)); // Màu nhẹ
        chartLabel1.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0)); // Padding top
        boxChartDetail1.add(chartLabel1);

        boxChart.add(boxChartDetail1);

        boxChart.add(Box.createHorizontalStrut(20));

        Box boxChartDetail2 = Box.createVerticalBox();
        boxChartDetail2.add(pieChart);
        JLabel chartLabel2 = new JLabel("Biểu đồ thống kê trạng thái của sản phẩm");
        chartLabel2.setFont(new Font("SansSerif", Font.BOLD, 16));
        chartLabel2.setAlignmentX(Component.CENTER_ALIGNMENT);
        chartLabel2.setForeground(new Color(33, 37, 41)); // Màu nhẹ
        chartLabel2.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0)); // Padding top
        boxChartDetail2.add(chartLabel2);

        boxChart.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10),
                ComponentUtils.getTitleBorder("Tổng quan")
        ));

        boxChart.add(boxChartDetail2);

        boxChart.add(boxChartDetail2);

        boxContent.add(boxChart);
        boxContent.add(Box.createVerticalStrut(10));
        boxContent.add(boxTable);







        add(boxContent, BorderLayout.CENTER);



//        Action
        jTableContent.addMouseListener(this);



    }



    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() != null) {
            Object source = e.getSource();

        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getSource() != null) {
            Object source = e.getSource();

        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}


