package gui.panel;

import chart.Chart;
import chart.ModelChart;
import chart.ModelPieChart;
import chart.PieChart;
import com.toedter.calendar.JDateChooser;
import gui.components.ComponentUtils;
import io.github.cdimascio.dotenv.Dotenv;
import net.datafaker.Faker;
import service.ChiTietHoaDonService;
import service.DanhMucSanPhamService;
import service.SanPhamService;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class PanelThongKe extends JPanel implements MouseListener, ActionListener {

    Dotenv dotenv = Dotenv.load();

    private DecimalFormat df = new DecimalFormat("#,##0.00");
    private static Chart chart;
    String drivername = dotenv.get("DRIVER_NAME");
    private static Box chartBox;
    private final JTextField txtNam;
    private final Faker faker = new Faker();
    private final JDateChooser dateNgayBatDau, dateNgayKetThuc;
    private static JPanel cardsPanel;

    private static double doanhThu = 0, loiNhuan = 0;
    private static long soLuongHoaDon = 0, khachHangMoi = 0;

    private final JButton btnThongKe, btnThongKeNam;
    private PieChart pieChart;
    private final Context context = new InitialContext();
    private final ChiTietHoaDonService chiTietHoaDonService = (ChiTietHoaDonService) context.lookup("rmi://" + drivername + ":9020/chiTietHoaDonService");
    private final SanPhamService sanPhamService = (SanPhamService) context.lookup("rmi://" + drivername + ":9020/sanPhamService");

    public PanelThongKe () throws NamingException, RemoteException {
        setLayout(new BorderLayout());

        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Tạo tiêu đề căn giữa
        JLabel titleLabel = new JLabel("Giao diện thống kê", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10)); // tạo khoảng cách
        add(titleLabel, BorderLayout.NORTH);


        dateNgayBatDau = new JDateChooser();
        dateNgayKetThuc = new JDateChooser();
        dateNgayBatDau.setDateFormatString("dd/MM/yyyy");
        dateNgayBatDau.setPreferredSize(new Dimension(100, 10));
        dateNgayKetThuc.setDateFormatString("dd/MM/yyyy");
        dateNgayKetThuc.setPreferredSize(new Dimension(100, 10));

        Box boxContent = Box.createVerticalBox();
        Box boxFilter = Box.createHorizontalBox();

        boxFilter.add(new JLabel("Ngày bắt đầu: "));
        boxFilter.add(Box.createHorizontalStrut(10));
        boxFilter.add(dateNgayBatDau);
        boxFilter.add(Box.createHorizontalStrut(10));
        boxFilter.add(new JLabel("Ngày kết thúc: "));
        boxFilter.add(Box.createHorizontalStrut(10));
        boxFilter.add(dateNgayKetThuc);
        boxFilter.add(Box.createHorizontalStrut(10));
        boxFilter.add(btnThongKe = new JButton("Thống kê"));
        boxFilter.add(Box.createHorizontalStrut(10));
        boxFilter.add(new JLabel("Nhập năm cần thống kê: "));
        boxFilter.add(Box.createHorizontalStrut(10));
        boxFilter.add(txtNam = new JTextField(10));
        boxFilter.add(Box.createHorizontalStrut(10));
        boxFilter.add(btnThongKeNam = new JButton("Thống kê theo năm"));
        boxFilter.add(Box.createHorizontalStrut(10));
        boxContent.add(boxFilter);


        cardsPanel = new JPanel(new GridLayout(2, 2, 10, 10)); // 2 hàng 2 cột
        cardsPanel.setOpaque(false);
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Tạo các card thống kê
        createStatCard(cardsPanel, "DOANH THU", doanhThu+"", "VND", new Color(46, 204, 113));
        createStatCard(cardsPanel, "SỐ ĐƠN", soLuongHoaDon + "", "Đơn", new Color(52, 152, 219));
        createStatCard(cardsPanel, "LỢI NHUẬN", loiNhuan + "", "VND", new Color(155, 89, 182));
        createStatCard(cardsPanel, "KHÁCH HÀNG", khachHangMoi + "", "Người", new Color(241, 196, 15));

        boxContent.add(cardsPanel);
        add(boxContent, BorderLayout.CENTER);

        // Panel chứa biểu đồ (phần này có thể phát triển sau)

        chartBox = Box.createHorizontalBox();
        chart = new Chart();
//        chart.addLegend("Expense", new Color(135, 189, 245));
//        chart.addLegend("Profit", new Color(189, 135, 245));
//        chart.addLegend("Cost", new Color(139, 229, 222));



        pieChart = new PieChart();


        chartBox.add(chart);
        chartBox.add(Box.createHorizontalStrut(50));
        chartBox.add(pieChart);

        add(chartBox, BorderLayout.SOUTH);

        ComponentUtils.setButtonMain(btnThongKe);
        ComponentUtils.setButtonMain(btnThongKeNam);
        btnThongKe.addActionListener(this);
        btnThongKeNam.addActionListener(this);

    }

    private void createStatCard(JPanel parent, String title, String value, String unit, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230)),
                BorderFactory.createEmptyBorder(20, 20, 10, 20)
        ));
        card.addMouseListener(this);

        // Tiêu đề card
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(new Color(120, 120, 120));
        card.add(titleLabel, BorderLayout.NORTH);

        // Panel chứa giá trị và đơn vị
        JPanel valuePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        valuePanel.setOpaque(false);

        // Giá trị
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        valueLabel.setForeground(color);
        valuePanel.add(valueLabel);

        // Đơn vị
        if (!unit.isEmpty()) {
            JLabel unitLabel = new JLabel(" " + unit);
            unitLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            unitLabel.setForeground(new Color(150, 150, 150));
            unitLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
            valuePanel.add(unitLabel);
        }

        card.add(valuePanel, BorderLayout.CENTER);
        parent.add(card);

    }



    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() != null) {
            Object source = e.getSource();
            if(source.equals(btnThongKe)){
                java.util.Date ngayBatDauDate = dateNgayBatDau.getDate(); // Lấy ngày từ JDateChooser
                java.util.Date ngayKetThucDate =  dateNgayKetThuc.getDate(); // Lấy ngày từ JDateChooser
                if(ngayBatDauDate != null && ngayKetThucDate != null){
                    LocalDate localDateBatDau = ngayBatDauDate.toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate();

                    LocalDate localDateKetThuc = ngayKetThucDate.toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate();

                    try{
                        soLuongHoaDon = chiTietHoaDonService.getSoLuongDonTheoNgay(localDateBatDau, localDateKetThuc);
                        doanhThu = chiTietHoaDonService.thongKeDoanhThuTheoNgay(localDateBatDau, localDateKetThuc)
                                .entrySet()
                                .stream().mapToDouble(entry -> entry.getValue())
                                .sum();
                        loiNhuan = doanhThu - sanPhamService.getTongTienNhapHangTheoNgay(localDateBatDau, localDateKetThuc);
                        khachHangMoi = chiTietHoaDonService.getTongKhachHangTheoNgay(localDateBatDau, localDateKetThuc);

                        // Xóa các card cũ (nếu cần)
                        cardsPanel.removeAll();

                        // Tạo lại các card với dữ liệu mới
                        createStatCard(cardsPanel, "DOANH THU", df.format(doanhThu) + "", "VND", new Color(46, 204, 113));
                        createStatCard(cardsPanel, "SỐ ĐƠN", soLuongHoaDon + "", "Đơn", new Color(52, 152, 219));
                        createStatCard(cardsPanel, "LỢI NHUẬN", df.format(loiNhuan) + "", "VND", new Color(155, 89, 182));
                        createStatCard(cardsPanel, "KHÁCH HÀNG", khachHangMoi + "", "Người", new Color(241, 196, 15));

                        // Cập nhật giao diện
                        cardsPanel.revalidate();
                        cardsPanel.repaint();


                        chartBox.removeAll(); // Xóa chart cũ
                        chart = new Chart(); // Tạo chart mới
                        chartBox.add(chart); // Thêm lại vào panel
                        pieChart = new PieChart();
                        chartBox.add(pieChart);
                        chartBox.revalidate();
                        chartBox.repaint();

                        chart.addLegend("Doanh thu", new Color(245, 189, 135));
                        chart.addLegend("Lợi nhuận", new Color(101, 148, 229));

                        chiTietHoaDonService.thongKeDoanhThuTheoNgay(localDateBatDau, localDateKetThuc)
                                .entrySet()
                                .stream().sorted(Map.Entry.<LocalDate, Double>comparingByKey())
                                .forEach(entry -> {
                            try {
                                chart.addData(new ModelChart(entry.getKey().toString(), new double[]{
                                        entry.getValue(),
                                        entry.getValue() - sanPhamService.getTongTienNhapHangTheoThangVaNam(entry.getKey().getMonthValue(), entry.getKey().getYear())
                                }));
                            } catch (RemoteException ex) {
                                throw new RuntimeException(ex);
                            }
                        });

                        chart.revalidate();
                        chart.repaint();


                        pieChart.setFont(new java.awt.Font("sansserif", 1, 15)); // NOI18N

                        pieChart.setChartType(PieChart.PeiChartType.DONUT_CHART);

                        java.util.List<Map.Entry<LocalDate, Double>> dataList = new ArrayList<>(chiTietHoaDonService.thongKeDoanhThuTheoNgay(localDateBatDau, localDateKetThuc).entrySet());

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
                            LocalDate label = dataList.get(i).getKey();
                            double value = dataList.get(i).getValue();
                            Color color = colorList.get(i % colorList.size()); // Quay vòng nếu màu ít hơn dữ liệu

                            pieChart.addData(new ModelPieChart(label.toString(), value, color));
                        }

                        pieChart.revalidate();
                        pieChart.repaint();


                    }catch (Exception ex){
                        JOptionPane.showMessageDialog(this, "Ngày tháng năm không hợp lệ", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }

                }
            } else if (source.equals(btnThongKeNam)){
                String inputNam = txtNam.getText();
                try{
                    int nam = Integer.parseInt(inputNam);
                    if(nam > 0){
                        soLuongHoaDon = chiTietHoaDonService.getSoLuongDonTheoNam(nam);
                        doanhThu = chiTietHoaDonService.thongKeDoanhThuTheoNam(nam)
                                .entrySet()
                                .stream().mapToDouble(entry -> entry.getValue())
                                .sum();
                        loiNhuan = doanhThu - sanPhamService.getTongTienNhapHangTheoNam(nam);
                        khachHangMoi = chiTietHoaDonService.getTongKhachHangTheoNam(nam);

                        // Xóa các card cũ (nếu cần)
                        cardsPanel.removeAll();

                        // Tạo lại các card với dữ liệu mới
                        createStatCard(cardsPanel, "DOANH THU", df.format(doanhThu) + "", "VND", new Color(46, 204, 113));
                        createStatCard(cardsPanel, "SỐ ĐƠN", soLuongHoaDon + "", "Đơn", new Color(52, 152, 219));
                        createStatCard(cardsPanel, "LỢI NHUẬN", df.format(loiNhuan) + "", "VND", new Color(155, 89, 182));
                        createStatCard(cardsPanel, "KHÁCH HÀNG", khachHangMoi + "", "Người", new Color(241, 196, 15));

                        // Cập nhật giao diện
                        cardsPanel.revalidate();
                        cardsPanel.repaint();


                        chartBox.removeAll(); // Xóa chart cũ
                        chart = new Chart(); // Tạo chart mới
                        chartBox.add(chart); // Thêm lại vào panel
                        pieChart = new PieChart();
                        chartBox.add(pieChart);
                        chartBox.revalidate();
                        chartBox.repaint();

                        chart.addLegend("Doanh thu", new Color(245, 189, 135));
                        chart.addLegend("Lợi nhuận", new Color(101, 148, 229));

                        chiTietHoaDonService.thongKeDoanhThuTheoNam(nam).entrySet()
                                .stream().sorted(Map.Entry.<Integer, Double>comparingByKey())
                                .forEach(entry -> {
                            try {
                                chart.addData(new ModelChart("Tháng " + entry.getKey(), new double[]{
                                        entry.getValue(),
                                        entry.getValue() - sanPhamService.getTongTienNhapHangTheoThangVaNam(entry.getKey(), nam)
                                }));
                            } catch (RemoteException ex) {
                                throw new RuntimeException(ex);
                            }
                        });

                        chart.revalidate();
                        chart.repaint();


                        pieChart.setFont(new java.awt.Font("sansserif", 1, 15)); // NOI18N

                        pieChart.setChartType(PieChart.PeiChartType.DONUT_CHART);

                        java.util.List<Map.Entry<Integer, Double>> dataList = new ArrayList<>(chiTietHoaDonService.thongKeDoanhThuTheoNam(nam).entrySet());

                        java.util.List<Color> colorList = List.of(
                                new Color(23, 126, 238),  // Màu xanh dương
                                new Color(221, 65, 65),   // Màu đỏ
                                new Color(255, 193, 7),   // Màu vàng
                                new Color(40, 167, 69),   // Màu xanh lá
                                new Color(108, 117, 125)  // Màu xám
                        );

                        for (int i = 0; i < dataList.size(); i++) {
                            int label = dataList.get(i).getKey();
                            double value = dataList.get(i).getValue();
                            Color color = colorList.get(i % colorList.size()); // Quay vòng nếu màu ít hơn dữ liệu

                            pieChart.addData(new ModelPieChart("Tháng " + label, value, color));
                        }

                        pieChart.revalidate();
                        pieChart.repaint();

                    } else {
                        JOptionPane.showMessageDialog(this, "Năm phải lớn hơn 0", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }catch (Exception ex){
                    JOptionPane.showMessageDialog(this, "Năm phải là số nguyên!", "Lỗi", JOptionPane.ERROR_MESSAGE);

                }
            }


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


