package home.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class CuonSach {
    private SimpleIntegerProperty maCuonSach;
    private SimpleIntegerProperty maTuaSach;
    private SimpleStringProperty tenTuaSach;
    private SimpleStringProperty tenTheLoai;
    private SimpleStringProperty tacGia;
    private SimpleStringProperty trangThai; // 1 true, 0 false

    public CuonSach() {

    }

    public CuonSach(String tenTuaSach, String trangThai) {
        this.tenTuaSach = new SimpleStringProperty(tenTuaSach);
        this.trangThai = new SimpleStringProperty(trangThai);
    }

    public CuonSach(int maTuaSach, String tenTuaSach,
                    String tenTheLoai, String tacGia, String trangThai) {
        this.maTuaSach = new SimpleIntegerProperty(maTuaSach);
        this.tenTuaSach = new SimpleStringProperty(tenTuaSach);
        this.tenTheLoai = new SimpleStringProperty(tenTheLoai);
        this.tacGia = new SimpleStringProperty(tacGia);
        this.trangThai = new SimpleStringProperty(trangThai);
    }

    public CuonSach(int maCuonSach, int maTuaSach, String tenTuaSach,
                    String tenTheLoai, String tacGia, String trangThai) {
        this.maCuonSach = new SimpleIntegerProperty(maCuonSach);
        this.maTuaSach = new SimpleIntegerProperty(maTuaSach);
        this.tenTuaSach = new SimpleStringProperty(tenTuaSach);
        this.tenTheLoai = new SimpleStringProperty(tenTheLoai);
        this.tacGia = new SimpleStringProperty(tacGia);
        this.trangThai = new SimpleStringProperty(trangThai);
    }

    public int getMaCuonSach() {
        return maCuonSach.get();
    }

    public SimpleIntegerProperty maCuonSachProperty() {
        return maCuonSach;
    }

    public void setMaCuonSach(int maCuonSach) {
        this.maCuonSach.set(maCuonSach);
    }

    public int getMaTuaSach() {
        return maTuaSach.get();
    }

    public SimpleIntegerProperty maTuaSachProperty() {
        return maTuaSach;
    }

    public void setMaTuaSach(int maTuaSach) {
        this.maTuaSach.set(maTuaSach);
    }

    public String getTenTuaSach() {
        return tenTuaSach.get();
    }

    public SimpleStringProperty tenTuaSachProperty() {
        return tenTuaSach;
    }

    public void setTenTuaSach(String tenTuaSach) {
        this.tenTuaSach.set(tenTuaSach);
    }

    public String getTenTheLoai() {
        return tenTheLoai.get();
    }

    public SimpleStringProperty tenTheLoaiProperty() {
        return tenTheLoai;
    }

    public void setTenTheLoai(String tenTheLoai) {
        this.tenTheLoai.set(tenTheLoai);
    }

    public String getTacGia() {
        return tacGia.get();
    }

    public SimpleStringProperty tacGiaProperty() {
        return tacGia;
    }

    public void setTacGia(String tacGia) {
        this.tacGia.set(tacGia);
    }

    public String getTrangThai() {
        return trangThai.get();
    }

    public SimpleStringProperty trangThaiProperty() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai.set(trangThai);
    }
}
