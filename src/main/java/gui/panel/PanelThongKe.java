package gui.panel;

import chart.Chart;
import chart.ModelChart;
import chart.ModelPieChart;
import chart.PieChart;
import com.toedter.calendar.JDateChooser;
import gui.components.ComponentUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import raven.toast.Notifications;
import javax.naming.NamingException;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileOutputStream;
import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

import static gui.panel.RmiServiceLocator.chiTietHoaDonService;
import static gui.panel.RmiServiceLocator.sanPhamService;

public class PanelThongKe extends JPanel implements MouseListener, ActionListener {

    private DecimalFormat df = new DecimalFormat("#,##0.00");
    private static Chart chart;
    private static Box chartBox;
    private final JTextField txtNam;
    private final JDateChooser dateNgayBatDau, dateNgayKetThuc;
    private static JPanel cardsPanel;

    private static double doanhThu = 0, loiNhuan = 0;
    private static long soLuongHoaDon = 0, khachHangMoi = 0;

    private final JButton btnThongKe, btnThongKeNam, btnXuatThongKeNgay, btnXuatThongKeNam;
    private PieChart pieChart;
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


        // Lấy ngày 1/1 của năm hiện tại
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);  // Ngày 1
        cal.set(Calendar.MONTH, Calendar.JANUARY);  // Tháng 1
        Date ngayDauNam = cal.getTime();
        dateNgayBatDau.setDate(ngayDauNam);
        dateNgayKetThuc.setDate(new Date());

        Box boxContent = Box.createVerticalBox();
        Box boxFilter = Box.createHorizontalBox();

        boxFilter.add(new JLabel("Ngày bắt đầu: "));
        boxFilter.add(Box.createHorizontalStrut(5));
        boxFilter.add(dateNgayBatDau);
        boxFilter.add(Box.createHorizontalStrut(5));
        boxFilter.add(new JLabel("Ngày kết thúc: "));
        boxFilter.add(Box.createHorizontalStrut(5));
        boxFilter.add(dateNgayKetThuc);
        boxFilter.add(Box.createHorizontalStrut(5));
        boxFilter.add(btnThongKe = new JButton("Thống kê"));
        boxFilter.add(Box.createHorizontalStrut(5));
        boxFilter.add(btnXuatThongKeNgay = new JButton("Xuất excel"));
        boxFilter.add(Box.createHorizontalStrut(5));
        boxFilter.add(new JLabel("Nhập năm cần thống kê: "));
        boxFilter.add(Box.createHorizontalStrut(5));
        boxFilter.add(txtNam = new JTextField(10));
        txtNam.setText(LocalDate.now().getYear() + "");
        boxFilter.add(Box.createHorizontalStrut(5));
        boxFilter.add(btnThongKeNam = new JButton("Thống kê năm"));
        boxFilter.add(Box.createHorizontalStrut(5));
        boxFilter.add(btnXuatThongKeNam = new JButton("Xuất excel"));
        boxFilter.add(Box.createHorizontalStrut(5));
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
        chartBox.add(pieChart);

        add(chartBox, BorderLayout.SOUTH);

        ComponentUtils.setButtonMain(btnThongKe);
        ComponentUtils.setButtonMain(btnThongKeNam);
        ComponentUtils.setButtonMain(btnXuatThongKeNam);
        btnXuatThongKeNam.setBackground(new Color(84, 211, 92));
        ComponentUtils.setButtonMain(btnXuatThongKeNgay);
        btnXuatThongKeNgay.setBackground(new Color(84, 211, 92));


        btnThongKe.addActionListener(this);
        btnThongKeNam.addActionListener(this);
        btnXuatThongKeNam.addActionListener(this);
        btnXuatThongKeNgay.addActionListener(this);


        btnThongKeNam.setIcon(new ImageIcon(getClass().getResource("/image/analytic.png")));
        btnThongKe.setIcon(new ImageIcon(getClass().getResource("/image/analytic.png")));

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
                            LocalDate label = dataList.get(i).getKey();
                            double value = dataList.get(i).getValue();
                            Color color = colorList.get(i % colorList.size()); // Quay vòng nếu màu ít hơn dữ liệu

                            pieChart.addData(new ModelPieChart(label.toString(), value, color));
                        }

                        pieChart.revalidate();
                        pieChart.repaint();


                    }catch (Exception ex){
                        Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_RIGHT, "Ngày tháng năm không hợp lệ");
                        Notifications.getInstance().clear(Notifications.Location.TOP_RIGHT);
                    }

                }
            }
            else if (source.equals(btnThongKeNam)){
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
                            int label = dataList.get(i).getKey();
                            double value = dataList.get(i).getValue();
                            Color color = colorList.get(i % colorList.size()); // Quay vòng nếu màu ít hơn dữ liệu

                            pieChart.addData(new ModelPieChart("Tháng " + label, value, color));
                        }

                        pieChart.revalidate();
                        pieChart.repaint();

                    } else {
                        Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_RIGHT, "Năm phải lớn hơn 0");
                        Notifications.getInstance().clear(Notifications.Location.TOP_RIGHT);
                    }
                }catch (Exception ex){
                    Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_RIGHT, "Năm phải là số nguyên!");
                    Notifications.getInstance().clear(Notifications.Location.TOP_RIGHT);

                }
            }
            else if (source.equals(btnXuatThongKeNam)){
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
                        String tenFile = "Thống kê doanh thu theo năm " + nam + "_Ngày thống kê " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd_MM_yyyy"));

                        xuatBaoCao(tenFile, chiTietHoaDonService.thongKeDoanhThuTheoNam(nam), nam);

                    } else {
                        Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_RIGHT, "Năm phải lớn hơn 0");
                        Notifications.getInstance().clear(Notifications.Location.TOP_RIGHT);
                    }
                }catch (Exception ex){
                    Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_RIGHT, "Năm phải là số nguyên!");
                    Notifications.getInstance().clear(Notifications.Location.TOP_RIGHT);
                }
            }
            else if (source.equals(btnXuatThongKeNgay)) {
                java.util.Date ngayBatDauDate = dateNgayBatDau.getDate(); // Lấy ngày từ JDateChooser
                java.util.Date ngayKetThucDate = dateNgayKetThuc.getDate(); // Lấy ngày từ JDateChooser
                if (ngayBatDauDate != null && ngayKetThucDate != null) {
                    LocalDate localDateBatDau = ngayBatDauDate.toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate();

                    LocalDate localDateKetThuc = ngayKetThucDate.toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate();

                    try {
                        soLuongHoaDon = chiTietHoaDonService.getSoLuongDonTheoNgay(localDateBatDau, localDateKetThuc);
                        doanhThu = chiTietHoaDonService.thongKeDoanhThuTheoNgay(localDateBatDau, localDateKetThuc)
                                .entrySet()
                                .stream().mapToDouble(entry -> entry.getValue())
                                .sum();
                        loiNhuan = doanhThu - sanPhamService.getTongTienNhapHangTheoNgay(localDateBatDau, localDateKetThuc);
                        khachHangMoi = chiTietHoaDonService.getTongKhachHangTheoNgay(localDateBatDau, localDateKetThuc);


                        chiTietHoaDonService.thongKeDoanhThuTheoNgay(localDateBatDau, localDateKetThuc);

                        String tenFile = "Thống kê doanh thu theo ngày từ " + localDateBatDau.format(DateTimeFormatter.ofPattern("dd_MM_yyyy")) + " đến ngày " + localDateKetThuc.format(DateTimeFormatter.ofPattern("dd_MM_yyyy"));
                        xuatBaoCaoNgay(tenFile, chiTietHoaDonService.thongKeDoanhThuTheoNgay(localDateBatDau, localDateKetThuc), localDateBatDau, localDateKetThuc);


                    } catch (Exception ex) {
                        Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_RIGHT, "Ngày tháng năm không hợp lệ");
                        Notifications.getInstance().clear(Notifications.Location.TOP_RIGHT);
                    }
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





    private void xuatBaoCao(String filename, Map<Integer, Double> doanhThuTheoThang, int year) {
        DecimalFormatSymbols symbol = new DecimalFormatSymbols();
        symbol.setGroupingSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("#,##0 'VNĐ'", symbol);
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Doanh thu");

        org.apache.poi.ss.usermodel.Font boldFont = workbook.createFont();
        boldFont.setBold(true);
        boldFont.setFontHeightInPoints((short) 14);

        CellStyle style = workbook.createCellStyle();
        style.setFont(boldFont);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        CellStyle borderStyle = workbook.createCellStyle();
        borderStyle.setBorderTop(BorderStyle.THIN);
        borderStyle.setBorderBottom(BorderStyle.THIN);
        borderStyle.setBorderLeft(BorderStyle.THIN);
        borderStyle.setBorderRight(BorderStyle.THIN);

        CellStyle centerStyle = workbook.createCellStyle();
        centerStyle.setFont(boldFont);
        centerStyle.setAlignment(HorizontalAlignment.CENTER);
        centerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        // Cài đặt độ rộng cột
        sheet.setColumnWidth(0, 20 * 256); // cột Tháng
        sheet.setColumnWidth(1, 25 * 256); // cột Doanh thu
        sheet.setColumnWidth(2, 15 * 256); // cột Quý

        // Tiêu đề
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("HỆ THỐNG QUẢN LÝ SIÊU THỊ CO.OP MART NGUYỄN KIỆM");
        cell.setCellStyle(centerStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));

        row = sheet.createRow(1);
        cell = row.createCell(0);
        cell.setCellValue(filename.toUpperCase());
        cell.setCellStyle(centerStyle);
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 5));

        row = sheet.createRow(3);
        cell = row.createCell(0);
        cell.setCellValue("Bảng doanh thu theo tháng trong năm " +  year);
        cell.setCellStyle(style);
        sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, 3));

        String[] headers = {"STT", "Tháng", "Doanh thu", "Quý"};
        Row headerRow = sheet.createRow(4);
        for (int i = 0; i < headers.length; i++) {
            cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(borderStyle);
        }

        for (int i = 0; i <= 5; i++) {
            sheet.setColumnWidth(i, 25 * 170); // Giữ như cũ
        }

        int rowNum = 5;
        double tongDoanhThu = 0;
        int stt = 1; // Khởi tạo STT
        for (Map.Entry<Integer, Double> entry : doanhThuTheoThang.entrySet()) {
            int thang = entry.getKey();
            double doanhThu = entry.getValue();
            int quy = (thang - 1) / 3 + 1;

            row = sheet.createRow(rowNum++);

            Cell sttCell = row.createCell(0);
            sttCell.setCellValue(stt++);
            sttCell.setCellStyle(borderStyle);

            Cell thangCell = row.createCell(1);
            thangCell.setCellValue("Tháng " + thang);
            thangCell.setCellStyle(borderStyle);

            Cell doanhThuCell = row.createCell(2);
            doanhThuCell.setCellValue(decimalFormat.format(doanhThu));
            doanhThuCell.setCellStyle(borderStyle);

            Cell quyCell = row.createCell(3);
            quyCell.setCellValue("Quý " + quy);
            quyCell.setCellStyle(borderStyle);
            tongDoanhThu += doanhThu;
        }

        // Tổng doanh thu
        row = sheet.createRow(rowNum++);
        cell = row.createCell(0);
        cell.setCellValue("Tổng doanh thu:");
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue(decimalFormat.format(tongDoanhThu));
        cell.setCellStyle(style);

        row = sheet.createRow(rowNum++);
        cell = row.createCell(0);
        cell.setCellValue("Tổng khách hàng:");
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue(khachHangMoi + " khách");
        cell.setCellStyle(style);

        row = sheet.createRow(rowNum++);
        cell = row.createCell(0);
        cell.setCellValue("Tổng số đơn:");
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue(soLuongHoaDon + " đơn hàng");
        cell.setCellStyle(style);

        row = sheet.createRow(rowNum++);
        cell = row.createCell(0);
        cell.setCellValue("Tổng lợi nhuận:");
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue(decimalFormat.format(loiNhuan));
        cell.setCellStyle(style);

        // Ngày xuất báo cáo
        LocalDate today = LocalDate.now();
        CellStyle centerTextStyle = workbook.createCellStyle();
        centerTextStyle.setAlignment(HorizontalAlignment.CENTER);
        centerTextStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        rowNum += 2;
        row = sheet.createRow(rowNum++);
        cell = row.createCell(2);
        cell.setCellValue("TPHCM, Ngày " + today.getDayOfMonth() + " Tháng " + today.getMonthValue() + " Năm " + today.getYear());
        cell.setCellStyle(centerTextStyle);
        sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 2, 3));

        row = sheet.createRow(rowNum++);
        cell = row.createCell(2);
        cell.setCellValue("(Ký và ghi rõ họ tên)");
        cell.setCellStyle(centerTextStyle);
        sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 2, 3));

        // Xuất file
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn vị trí lưu file Excel");

        fileChooser.setFileFilter(new FileNameExtensionFilter("Excel Files", "xls", "xlsx"));

        // Đặt thư mục mặc định
        File defaultDirectory = new File("C:/Users/Arisu/Downloads");
        if (defaultDirectory.exists()) {
            fileChooser.setCurrentDirectory(defaultDirectory);
        }

        fileChooser.setSelectedFile(new File(filename + ".xlsx"));

        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();
            if (!filePath.endsWith(".xlsx")) {
                filePath += ".xlsx";
            }

            try (FileOutputStream out = new FileOutputStream(filePath)) {
                workbook.write(out);
                workbook.close();
                JOptionPane.showMessageDialog(null, "Xuất file thành công", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Lỗi khi ghi file: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    private void xuatBaoCaoNgay(String fileName, Map<LocalDate, Double> doanhThuTheoNgay, LocalDate ngayBD, LocalDate ngayKT) {
        DecimalFormatSymbols symbol = new DecimalFormatSymbols();
        symbol.setGroupingSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("#,##0 'VNĐ'", symbol);
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Doanh thu");

        // Font và Style
        org.apache.poi.ss.usermodel.Font boldFont = workbook.createFont();
        boldFont.setBold(true);
        boldFont.setFontHeightInPoints((short) 14);

        CellStyle style = workbook.createCellStyle();
        style.setFont(boldFont);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        CellStyle borderStyle = workbook.createCellStyle();
        borderStyle.setBorderTop(BorderStyle.THIN);
        borderStyle.setBorderBottom(BorderStyle.THIN);
        borderStyle.setBorderLeft(BorderStyle.THIN);
        borderStyle.setBorderRight(BorderStyle.THIN);

        CellStyle centerStyle = workbook.createCellStyle();
        centerStyle.setFont(boldFont);
        centerStyle.setAlignment(HorizontalAlignment.CENTER);
        centerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        // Cài đặt độ rộng cột
        for (int i = 0; i <= 4; i++) {
            sheet.setColumnWidth(i, 25 * 170);
        }

        // Tiêu đề chính
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("HỆ THỐNG QUẢN LÝ SIÊU THỊ CO.OP MART NGUYỄN KIỆM");
        cell.setCellStyle(centerStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));

        row = sheet.createRow(1);
        cell = row.createCell(0);
        cell.setCellValue("THỐNG KÊ DOANH THU THEO NGÀY TỪ " + ngayBD.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + " ĐẾN NGÀY " + ngayKT.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        cell.setCellStyle(centerStyle);
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 5));

        // Dòng tiêu đề bảng
        Row headerRow = sheet.createRow(3);
        String[] headers = {"STT", "Ngày", "Doanh thu"};
        for (int i = 0; i < headers.length; i++) {
            cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(borderStyle);
        }

        // Nội dung bảng
        int rowNum = 4;
        double tongDoanhThu = 0;
        int stt = 1;

        for (Map.Entry<LocalDate, Double> entry : doanhThuTheoNgay.entrySet()) {
            row = sheet.createRow(rowNum++);
            Cell sttCell = row.createCell(0);
            sttCell.setCellValue(stt++);
            sttCell.setCellStyle(borderStyle);

            Cell ngayCell = row.createCell(1);
            ngayCell.setCellValue("Ngày " + entry.getKey().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            ngayCell.setCellStyle(borderStyle);

            Cell doanhThuCell = row.createCell(2);
            doanhThuCell.setCellValue(decimalFormat.format(entry.getValue()));
            doanhThuCell.setCellStyle(borderStyle);

            tongDoanhThu += entry.getValue();
        }

        // Tổng kết
        row = sheet.createRow(rowNum++);
        cell = row.createCell(0);
        cell.setCellValue("Tổng doanh thu:");
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue(decimalFormat.format(tongDoanhThu));
        cell.setCellStyle(style);

        row = sheet.createRow(rowNum++);
        cell = row.createCell(0);
        cell.setCellValue("Tổng khách hàng:");
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue(khachHangMoi + " khách");
        cell.setCellStyle(style);

        row = sheet.createRow(rowNum++);
        cell = row.createCell(0);
        cell.setCellValue("Tổng số đơn:");
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue(soLuongHoaDon + " đơn hàng");
        cell.setCellStyle(style);

        row = sheet.createRow(rowNum++);
        cell = row.createCell(0);
        cell.setCellValue("Tổng lợi nhuận:");
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue(decimalFormat.format(loiNhuan));
        cell.setCellStyle(style);

        // Ngày xuất file
        LocalDate today = LocalDate.now();
        CellStyle centerTextStyle = workbook.createCellStyle();
        centerTextStyle.setAlignment(HorizontalAlignment.CENTER);
        centerTextStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        rowNum += 2;
        row = sheet.createRow(rowNum++);
        cell = row.createCell(2);
        cell.setCellValue("TPHCM, Ngày " + today.getDayOfMonth() + " Tháng " + today.getMonthValue() + " Năm " + today.getYear());
        cell.setCellStyle(centerTextStyle);
        sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 2, 3));

        row = sheet.createRow(rowNum++);
        cell = row.createCell(2);
        cell.setCellValue("(Ký và ghi rõ họ tên)");
        cell.setCellStyle(centerTextStyle);
        sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 2, 3));

        // Tên file gợi ý
        String suggestedFileName = fileName + ".xlsx";

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn vị trí lưu file Excel");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Excel Files", "xls", "xlsx"));

        // Đặt thư mục mặc định
        File defaultDirectory = new File("C:/Users/Arisu/Downloads");
        if (defaultDirectory.exists()) {
            fileChooser.setCurrentDirectory(defaultDirectory);
        }

        fileChooser.setSelectedFile(new File(suggestedFileName));

        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();
            if (!filePath.endsWith(".xlsx")) {
                filePath += ".xlsx";
            }

            try (FileOutputStream out = new FileOutputStream(filePath)) {
                workbook.write(out);
                workbook.close();
                JOptionPane.showMessageDialog(null, "Xuất file thành công", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Lỗi khi ghi file: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


}


