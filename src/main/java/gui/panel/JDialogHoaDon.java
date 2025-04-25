package gui.panel;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import dto.HoaDonDTO;
import gui.components.ComponentUtils;
import model.ChiTietHoaDon;
import model.HoaDon;
import javax.naming.NamingException;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.Font;
import java.awt.event.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.util.List;


public class JDialogHoaDon extends JDialog implements ActionListener{
    private final JButton btnXuatHoaDon;
    private DecimalFormat df = new DecimalFormat("#,##0.00");
    private JTable table;
    private DefaultTableModel tableModel;
    public JDialogHoaDon(Frame parent){
        super(parent, "Phiếu Hóa Đơn", true);
        setSize(700, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // Thanh tiêu đề
        JLabel titleLabel = new JLabel("PHIẾU HÓA ĐƠN", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Panel chính chứa thông tin nhân viên và bảng
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        // Panel chứa thông tin nhân viên, căn trái
        JPanel employeePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        employeePanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        JPanel employeeDetails = new JPanel(new GridLayout(3, 1, 5, 5));
        employeeDetails.add(new JLabel(HoaDonDTO.getHoaDon().getNhanVien().getChucVuNhanVien() + ": " + HoaDonDTO.getHoaDon().getNhanVien().getTenNhanVien()));
        employeeDetails.add(new JLabel("Mã " + HoaDonDTO.getHoaDon().getNhanVien().getChucVuNhanVien() +": " + HoaDonDTO.getHoaDon().getNhanVien().getMaNhanVien()));
        employeeDetails.add(new JLabel("Số điện thoại: " + HoaDonDTO.getHoaDon().getNhanVien().getSoDienThoai()));
        employeePanel.add(employeeDetails);
        centerPanel.add(employeePanel);

        // Bảng chi tiết hóa đơn
        String[] columnNames = {"STT", "Tên hàng", "Số lượng", "Đơn giá", "Thành tiền"};


        // Tạo model cho bảng với data
        tableModel = new DefaultTableModel(columnNames, 0);

        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.setFont(new Font("Arial", Font.PLAIN, 14));

        // Căn giữa chữ trong tất cả các cột
        JScrollPane scrollPane = new JScrollPane(table);
        centerPanel.add(scrollPane);

        add(centerPanel, BorderLayout.CENTER);

        int index = 0;
        for(ChiTietHoaDon chiTietHoaDons :  HoaDonDTO.getHoaDon().getChiTietHoaDons()){
            Object[] rowData = {index + 1,
                    chiTietHoaDons.getSanPham().getTenSanPham(),
                    chiTietHoaDons.getSoLuong(),
                    df.format(chiTietHoaDons.getDonGia()) + " VND",
                    df.format(chiTietHoaDons.getThanhTien()) + " VND"
            };
            tableModel.addRow(rowData);
            index += 1;
        }

        // Panel chứa tổng tiền và thông tin khách hàng
        JPanel bottomPanel = new JPanel(new GridLayout(5, 1, 5, 5));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Tổng tiền
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel totalLabel = new JLabel("Tổng tiền: " + df.format(HoaDonDTO.getHoaDon().getTongTien()) + " VND");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalPanel.add(totalLabel);
        bottomPanel.add(totalPanel);

        // Thông tin khách hàng
        bottomPanel.add(new JLabel("Khách hàng: " + HoaDonDTO.getHoaDon().getKhachHang().getTenKhachHang()));
        bottomPanel.add(new JLabel("Số điện thoại: " + HoaDonDTO.getHoaDon().getKhachHang().getSoDienThoai()));
        bottomPanel.add(new JLabel("Điểm tích lũy hiện tại: " + HoaDonDTO.getHoaDon().getKhachHang().getDiemTichLuy() + " điểm"));

        // Nút đóng
        Box boxButton = Box.createHorizontalBox();
        JButton closeButton = new JButton("Đóng");
        closeButton.setFont(new Font("Arial", Font.PLAIN, 14));
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        boxButton.add(closeButton);
        boxButton.add(Box.createHorizontalGlue());
        boxButton.add(btnXuatHoaDon = new JButton("Xuất hóa đơn"));
        ComponentUtils.setButtonMain(btnXuatHoaDon);
        btnXuatHoaDon.setBackground( new Color(29, 216, 41));
        ComponentUtils.setTable(table);
        ComponentUtils.setButtonMain(closeButton);
        bottomPanel.add(boxButton);

        add(bottomPanel, BorderLayout.SOUTH);

        btnXuatHoaDon.addActionListener(this);
        // Đảm bảo dialog đóng khi nhấn nút close của window
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if(source.equals(btnXuatHoaDon)){
            // Tạo JFileChooser để người dùng chọn đường dẫn lưu file PDF
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Chọn vị trí lưu file PDF");

            // Đặt bộ lọc chỉ cho phép lưu file PDF
            FileNameExtensionFilter filter = new FileNameExtensionFilter("PDF Files", "pdf");
            fileChooser.setFileFilter(filter);

            // Đặt thư mục mặc định
            File defaultDirectory = new File("C:/Users/Arisu/Downloads");
            if (defaultDirectory.exists()) {
                fileChooser.setCurrentDirectory(defaultDirectory);
            }

            // Đặt tên file mặc định
            fileChooser.setSelectedFile(new File(
                    HoaDonDTO.getHoaDon().getMaHoaDon() + "_" + HoaDonDTO.getHoaDon().getKhachHang().getTenKhachHang() + ".pdf"
            ));

            // Hiển thị hộp thoại lưu file
            int userSelection = fileChooser.showSaveDialog(null);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                String filePath = fileToSave.getAbsolutePath();

                if (!filePath.toLowerCase().endsWith(".pdf")) {
                    filePath += ".pdf";
                }

                // Xuất file PDF
                exportToPDF(filePath, HoaDonDTO.getHoaDon());
                System.out.println("File PDF đã được lưu tại: " + filePath);
            }
        }
    }

    private void exportToPDF(String filePath, HoaDon hoaDon){
        // Khởi tạo tài liệu PDF
        Document document = new Document();
        try {
            // Tạo PdfWriter
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            List<ChiTietHoaDon> danhSachCTHD = hoaDon.getChiTietHoaDons();

            // Tiêu đề hóa đơn
            com.itextpdf.text.Font titleFont = FontFactory.getFont("C:/Font_Java/DejaVuSans.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 30);
            titleFont.setStyle(com.itextpdf.text.Font.BOLD);
            Paragraph title = new Paragraph("CO.OP MART NGUYỄN KIỆM", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" ")); // Dòng trống

            // Địa chỉ và số điện thoại
            com.itextpdf.text.Font addressFont = FontFactory.getFont("C:/Font_Java/DejaVuSans.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 12);
            Paragraph address = new Paragraph("573 Đ. Nguyễn Kiệm, Phường 9, Phú Nhuận, Hồ Chí Minh\nĐiện thoại: (028) 7300-3350 Ext 1907\n"
                    + "Siêu thị Nguyễn Kiệm: ST091 - Xin chào", addressFont);
            address.setAlignment(Element.ALIGN_CENTER);
            document.add(address);
            document.add(new Paragraph(" ")); // Dòng trống

            // Xin chào và thông tin nhân viên
            com.itextpdf.text.Font greetingFont = FontFactory.getFont("C:/Font_Java/DejaVuSans.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 12);
            document.add(new Paragraph("Xin chào: " + hoaDon.getKhachHang().getTenKhachHang() , greetingFont));
            document.add(new Paragraph("Mã hóa đơn: " + hoaDon.getMaHoaDon(), greetingFont)); // Dòng trống

            // Tên nhân viên và ngày giờ
            Paragraph staffAndDate = new Paragraph("Nhân viên phụ trách: " + hoaDon.getNhanVien().getTenNhanVien() , greetingFont);
            staffAndDate.setAlignment(Element.ALIGN_LEFT);
            document.add(staffAndDate);

            // Thêm ngày giờ
            String currentDate = java.time.LocalDate.now().toString();
            Paragraph date = new Paragraph("Ngày: " + currentDate, greetingFont);
            date.setAlignment(Element.ALIGN_LEFT);
            document.add(date);

            // Tên nhân viên và ngày giờ
            Paragraph totalProduct = new Paragraph("Tổng số sản phẩm: " + danhSachCTHD.size() , greetingFont);
            totalProduct.setAlignment(Element.ALIGN_LEFT);
            document.add(totalProduct);

            // Tên nhân viên và ngày giờ
            Paragraph titleVAT = new Paragraph("Giá bán đã bao gồm Thuế VAT", greetingFont);
            titleVAT.setAlignment(Element.ALIGN_LEFT);
            document.add(titleVAT);


            document.add(new Paragraph(" ")); // Dòng trống

            // Tạo bảng chứa thông tin hóa đơn
            PdfPTable table = new PdfPTable(4); // 4 cột cho các thông tin
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            // Thêm tiêu đề cột vào bảng với font in đậm và màu đỏ
            com.itextpdf.text.Font headerFont = FontFactory.getFont("C:/Font_Java/DejaVuSans.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 10);
            headerFont.setStyle(com.itextpdf.text.Font.BOLD);
            headerFont.setColor(BaseColor.RED);
            // Căn giữa các tiêu đề cột
            for (String header : new String[]{"Tên sản phẩm", "Số lượng", "Đơn giá", "Thành tiền"}) {
                PdfPCell cell = new PdfPCell(new Paragraph(header, headerFont));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(15); // Padding cho ô
//                cell.setBackgroundColor(BaseColor.YELLOW); // Đặt màu nền là vàng
                table.addCell(cell);
            }

            // Thêm thông tin hóa đơn vào bảng
            double tongTien = 0;
            com.itextpdf.text.Font cellFont = FontFactory.getFont("C:/Font_Java/DejaVuSans.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 10);

            for (ChiTietHoaDon cthoaDon : danhSachCTHD) {
                // Thêm các ô cho thông tin hóa đơn

                PdfPCell tenSPCell = new PdfPCell(new Paragraph(cthoaDon.getSanPham().getTenSanPham(), cellFont));
                tenSPCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                tenSPCell.setPadding(2);
                table.addCell(tenSPCell);

                PdfPCell soLuongCell = new PdfPCell(new Paragraph(String.valueOf(cthoaDon.getSoLuong()), cellFont));
                soLuongCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                soLuongCell.setPadding(5);
                table.addCell(soLuongCell);

                PdfPCell giaBanCell = new PdfPCell(new Paragraph(df.format(cthoaDon.getDonGia()) + " VND", cellFont));
                giaBanCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                giaBanCell.setPadding(0);
                table.addCell(giaBanCell);


                double thanhTien = cthoaDon.getThanhTien();
                PdfPCell thanhTienCell = new PdfPCell(new Paragraph(df.format(thanhTien) + "VND", cellFont));
                thanhTienCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                thanhTienCell.setPadding(0);
                table.addCell(thanhTienCell);
                tongTien += thanhTien;
            }

            // Thêm bảng vào tài liệu PDF
            document.add(table);


            // Dưới bảng table
            // Tạo bảng cho Tổng tiền và số tiền
            PdfPTable finalTable = new PdfPTable(2);
            finalTable.setWidthPercentage(100);
            finalTable.setSpacingBefore(0f);
            finalTable.setSpacingAfter(0f);
            // Nếu bạn muốn đảm bảo không có khoảng cách bên trái và bên phải
            finalTable.setHorizontalAlignment(0);

            // Ô Tổng tiền
            com.itextpdf.text.Font finalLabelFont = FontFactory.getFont("C:/Font_Java/DejaVuSans.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 12);
            finalLabelFont.setStyle(com.itextpdf.text.Font.BOLD);


            PdfPCell docInline = new PdfPCell(new Paragraph("-----------------------------------------------------------------"));
            docInline.setHorizontalAlignment(Element.ALIGN_RIGHT);
            docInline.setBorder(PdfPCell.NO_BORDER); // Không viền
            docInline.setPadding(0);
            docInline.setBorder(0);



            PdfPCell pointUseCell = new PdfPCell(new Paragraph("Điểm tích lũy sử dụng:", finalLabelFont));
            pointUseCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            pointUseCell.setBorder(PdfPCell.NO_BORDER); // Không viền
            finalTable.addCell(pointUseCell);

            // Ô số tiền
            PdfPCell pointPriceCell = new PdfPCell(new Paragraph(hoaDon.getDiemTichLuySuDung()+" điểm", finalLabelFont));
            pointPriceCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            pointPriceCell.setBorder(PdfPCell.NO_BORDER); // Không viền
            finalTable.addCell(pointPriceCell);


            finalTable.addCell(docInline);

            finalTable.addCell(docInline);

            PdfPCell totalLabelCell = new PdfPCell(new Paragraph("Tổng tiền:", finalLabelFont));
            totalLabelCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            totalLabelCell.setBorder(PdfPCell.NO_BORDER); // Không viền
            finalTable.addCell(totalLabelCell);

            // Ô số tiền

            PdfPCell totalPriceCell = new PdfPCell(new Paragraph(df.format(hoaDon.getTongTien()) + " VND", finalLabelFont));
            totalPriceCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalPriceCell.setBorder(PdfPCell.NO_BORDER); // Không viền
            finalTable.addCell(totalPriceCell);

            finalTable.addCell(docInline);

            finalTable.addCell(docInline);

            // Ô Tổng tiền
            PdfPCell moneyCell = new PdfPCell(new Paragraph("Số tiền thanh toán:", finalLabelFont));
            moneyCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            moneyCell.setBorder(PdfPCell.NO_BORDER); // Không viền
            finalTable.addCell(moneyCell);

            // Ô Tổng tiền
            PdfPCell moneyFinalCell = new PdfPCell(new Paragraph(df.format(hoaDon.getTongTien()) + " VND", finalLabelFont));
            moneyFinalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            moneyFinalCell.setBorder(PdfPCell.NO_BORDER); // Không viền
            finalTable.addCell(moneyFinalCell);

            finalTable.addCell(docInline);
            finalTable.addCell(docInline);


            // Ô Tổng tiền
            PdfPCell pointCell = new PdfPCell(new Paragraph("Tổng điểm tích lũy hiện tại:", finalLabelFont));
            pointCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            pointCell.setBorder(PdfPCell.NO_BORDER); // Không viền
            finalTable.addCell(pointCell);


            // Ô Tổng tiền
            PdfPCell pointFinalCell = new PdfPCell(new Paragraph(hoaDon.getKhachHang().getDiemTichLuy() + " điểm", finalLabelFont));
            pointFinalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            pointFinalCell.setBorder(PdfPCell.NO_BORDER); // Không viền
            finalTable.addCell(pointFinalCell);



            PdfPCell endCell = new PdfPCell(new Paragraph("Lưu ý: Đã bao gồm cả thuế VAT tính trên các sản phẩm", finalLabelFont));
            endCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            endCell.setBorder(PdfPCell.NO_BORDER); // Không viền
            finalTable.addCell(endCell);

            // Thêm bảng cuối cùng vào tài liệu PDF
            document.add(finalTable);


            // Đóng tài liệu
            document.close();
            System.out.println("Hóa đơn đã được tạo thành công tại: " + filePath);

        } catch (IOException | DocumentException e)
        {
            e.printStackTrace();
        }
    }
}
