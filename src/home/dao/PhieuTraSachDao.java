package home.dao;

import home.model.PhieuTraSach;
import jdk.nashorn.internal.scripts.JD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PhieuTraSachDao {
    public List<PhieuTraSach> lietKePhieuTraSach() {
        List<PhieuTraSach> danhSachPhieuTra = new ArrayList<>();
        Connection connection = JDBCConnection.getJDBCConnection();
        String sql = "SELECT PTS.MAPHIEUTRA, PMS.MAPHIEUMUON, TS.TENTUASACH, DG.TENDOCGIA, PMS.NGAYMUONSACH, " +
                "PTS.NGAYTRASACH, SONGAYMUON,SONGAYTRATRE,TIENPHAT\n" +
                "FROM (((PHIEUTRASACH PTS JOIN PHIEUMUONSACH PMS ON PTS.MAPHIEUMUON = PMS.MAPHIEUMUON)\n" +
                "    JOIN DOCGIA DG ON PMS.MADOCGIA = DG.MADOCGIA) JOIN CUONSACH CS ON CS.MACUONSACH = PMS" +
                ".MACUONSACH)\n" +
                "JOIN TUASACH TS ON CS.MATUASACH = TS.MATUASACH\n" +
                "ORDER BY PTS.MAPHIEUTRA";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int maPhieuTra = rs.getInt(1);
                int maPhieuMuon = rs.getInt(2);
                String tenSach = rs.getString(3);
                String tenDocGia = rs.getString(4);
                Date ngayMuonSach = rs.getTimestamp(5);
                Date ngayTraSach = rs.getTimestamp(6);
                int soNgayMuon = rs.getInt(7);
                int soNgayTraTre = rs.getInt(8);
                long tienPhat = rs.getLong(9);

                danhSachPhieuTra.add(new PhieuTraSach(maPhieuTra, maPhieuMuon, tenSach, tenDocGia, ngayMuonSach,
                        ngayTraSach, soNgayMuon, soNgayTraTre, tienPhat));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return danhSachPhieuTra;
    }

    public boolean themPhieuTraSach(PhieuTraSach phieuTraSach) {
        Connection connection = JDBCConnection.getJDBCConnection();
        String sql = "INSERT INTO PHIEUTRASACH(MAPHIEUMUON,MADOCGIA,MACUONSACH,NGAYTRASACH,SONGAYMUON,SONGAYTRATRE," +
                "TIENPHAT)\n" +
                "VALUES (?,?,?,?,?,?,?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, phieuTraSach.getMaPhieuMuon());
            preparedStatement.setInt(2, phieuTraSach.getMaDocGia());
            preparedStatement.setInt(3, phieuTraSach.getMaCuonSach());
            preparedStatement.setDate(4, (java.sql.Date) phieuTraSach.getNgayTraSach());
            preparedStatement.setInt(5, phieuTraSach.getSoNgayMuon());
            preparedStatement.setInt(6, phieuTraSach.getSoNgayTraTre());
            preparedStatement.setLong(7, phieuTraSach.getTienPhat());

            int rs = preparedStatement.executeUpdate();
            if (rs > 0) {
                return true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

}