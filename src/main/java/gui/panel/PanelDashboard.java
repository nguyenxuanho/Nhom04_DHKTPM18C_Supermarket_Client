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
import java.util.Random;

public class PanelDashboard extends JPanel implements MouseListener, ActionListener {
    private final JLabel labelFind;
    private final JTextField txtFind;
    private chart.Chart chart;
    private chart.PieChart pieChart;

    Dotenv dotenv = Dotenv.load();

    String drivername = dotenv.get("DRIVER_NAME");

    private final JTable jTableContent;

    private final Faker faker = new Faker();

    private final DefaultTableModel dataModel;


    private final JButton btnFind;

    private final Context context = new InitialContext();

    private final SanPhamService sanPhamService = (SanPhamService) context.lookup("rmi://" + drivername + ":9020/sanPhamService");
    private final ChiTietHoaDonService chiTietHoaDonService = (ChiTietHoaDonService) context.lookup("rmi://" + drivername + ":9020/chiTietHoaDonService");
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

        sanPhamService.getList().forEach(sanPham -> {
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
                BorderFactory.createEmptyBorder(50, 50, 50, 50),
                ComponentUtils.getTitleBorder("Danh sách sản phẩm sắp hết hạn")
        ));


        chart = new chart.Chart();
//        chart.addLegend("Income", new Color(245, 189, 135));
//        chart.addLegend("Expense", new Color(135, 189, 245));
//        chart.addLegend("Profit", new Color(189, 135, 245));
//        chart.addLegend("Cost", new Color(139, 229, 222));
//
//        chart.addData(new ModelChart("January", new double[]{500, 200, 80,89}));
//        chart.addData(new ModelChart("February", new double[]{600, 750, 90,150}));
//        chart.addData(new ModelChart("March", new double[]{200, 350, 460,900}));
//        chart.addData(new ModelChart("April", new double[]{480, 150, 750,700}));
//        chart.addData(new ModelChart("May", new double[]{350, 540, 300,150}));
//        chart.addData(new ModelChart("June", new double[]{190, 280, 81,200}));

        chart.addLegend("Lợi nhuận trong hôm nay", new Color(245, 189, 135));

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
                new Color(117, 229, 56),
                new Color(99, 194, 255),
                new Color(255, 193, 7),
                new Color(40, 167, 69),
                new Color(250, 34, 188),
                new Color(255, 193, 7),
                new Color(184, 174, 48),
                new Color(143, 23, 64)
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

        JLabel chartLabel1 = new JLabel("Biểu đồ thống kê");
        chartLabel1.setFont(new Font("SansSerif", Font.BOLD, 16));
        chartLabel1.setAlignmentX(Component.CENTER_ALIGNMENT);
        chartLabel1.setForeground(new Color(33, 37, 41)); // Màu nhẹ
        chartLabel1.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0)); // Padding top
        boxChartDetail1.add(chartLabel1);

        boxChart.add(boxChartDetail1);

        boxChart.add(Box.createHorizontalStrut(20));

        Box boxChartDetail2 = Box.createVerticalBox();
        boxChartDetail2.add(pieChart);
        JLabel chartLabel2 = new JLabel("Biểu đồ thống kê trạng thái");
        chartLabel2.setFont(new Font("SansSerif", Font.BOLD, 16));
        chartLabel2.setAlignmentX(Component.CENTER_ALIGNMENT);
        chartLabel2.setForeground(new Color(33, 37, 41)); // Màu nhẹ
        chartLabel2.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0)); // Padding top
        boxChartDetail2.add(chartLabel2);

        boxChart.add(boxChartDetail2);

        boxChart.add(boxChartDetail2);

        boxContent.add(boxChart);
        boxContent.add(Box.createVerticalStrut(20));
        boxContent.add(boxTable);







        add(boxContent, BorderLayout.CENTER);

        JPanel filterPanel = new JPanel();

        Box boxHorizonFilter = Box.createHorizontalBox();

        Box boxSearch = Box.createHorizontalBox();
        labelFind = new JLabel("Nhập mã để tìm: ");
        txtFind = new JTextField(50);
        btnFind = new JButton("Tìm");



        boxSearch.add(labelFind);
        boxSearch.add(Box.createHorizontalStrut(10));
        boxSearch.add(txtFind);
        boxSearch.add(Box.createHorizontalStrut(10));
        boxSearch.add(btnFind);



        boxHorizonFilter.add(boxSearch);


        filterPanel.add(boxHorizonFilter);



        // Panel nhập liệu và nút chức năng
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel inputPanel = new JPanel();

        inputPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(20, 20, 10, 20),
                ComponentUtils.getTitleBorder("Thông tin nhập")
        ));

        Font labelFont = new Font("Arial", Font.BOLD, 14); // Chữ lớn và đậm
        Font textFieldFont = new Font("Arial", Font.PLAIN, 14); // Chữ to cho input



        Box boxVerticalFormGroup = Box.createVerticalBox();










//        Action
        jTableContent.addMouseListener(this);

        btnFind.addActionListener(this);


//        End action





//        End custom GUI

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


