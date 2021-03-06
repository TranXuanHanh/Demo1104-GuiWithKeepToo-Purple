package home.controller;

import home.Main;
import home.dao.JDBCConnection;
import home.dao.PhieuPhatDao;
import home.model.PhieuMuonSach;
import home.model.PhieuPhat;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;

public class PhieuPhatDanhSachController implements Initializable {

    PhieuPhatDao phieuPhatDao = new PhieuPhatDao();
    ObservableList<PhieuPhat> danhSachPhieuPhat = FXCollections.observableArrayList(phieuPhatDao.lietKePhieuPhat());

    Main window = new Main();

    private static PhieuPhat selectedPhieuPhat;

    public static PhieuPhat getSelectedPhieuPhat() {
        return selectedPhieuPhat;
    }

    public static void setSelectedPhieuPhat(PhieuPhat selectedPhieuPhat) {
        PhieuPhatDanhSachController.selectedPhieuPhat = selectedPhieuPhat;
    }


    @FXML
    private GridPane panePhieuPhat;

    @FXML
    private TableView<PhieuPhat> tablePhieuPhat;

    @FXML
    private TableColumn<PhieuPhat, Integer> maPhieuPhatColumn;

    @FXML
    private TableColumn<PhieuPhat, Integer> maDocGiaColumn;

    @FXML
    private TableColumn<PhieuPhat, String> tenDocGiaColumn;

    @FXML
    private TableColumn<PhieuPhat, Integer> maPhieuTraColumn;

    @FXML
    private TableColumn<PhieuPhat, String> tenTuaSachColumn;

    @FXML
    private TableColumn<PhieuPhat, Long> tienPhatColumn;

    @FXML
    private TableColumn<PhieuPhat, Integer> maCuonSachColumn;

    @FXML
    private TextField tfSearchPhieuPhatInfo;

    @FXML
    private Button btnThemPhieuPhat;

    @FXML
    private Button btnHome;


    @FXML
    void openHomeWindow(ActionEvent event) {
        String role = MainGUIController.getUser_label();

        if (role == "Quản lí") {
            window.loadAnotherWindow("/home/fxml/MainGUI.fxml");
        } else {
            window.loadAnotherWindow("/home/fxml/MainGUI-thuthu.fxml");
        }

        cancelAction(event);
    }

    @FXML
    void cancelAction(ActionEvent event) {
        Stage stage = (Stage) btnHome.getScene().getWindow();
        stage.close();
    }

    @FXML
    void openThemPhieuPhatAction(ActionEvent event) {
        window.loadAnotherWindow("/home/fxml/PhieuPhatThem.fxml", "Thêm phiếu phạt");
        cancelAction(event);
    }

    @FXML
    void xoaPhieuPhatAction(ActionEvent event) {
        PhieuPhat selectedForDelete = tablePhieuPhat.getSelectionModel().getSelectedItem();
        if (selectedForDelete == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setContentText("Bạn chưa chọn dòng nào để xóa cả");
            alert.showAndWait();
            return;
        }

        Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
        alert1.setHeaderText(null);
        alert1.setContentText("Bạn thực sự muốn xóa phiếu phạt số \""
                + selectedForDelete.getMaPhieuPhat() + "\" chứ?");

        Optional<ButtonType> response = alert1.showAndWait();
        if (response.get() == ButtonType.CANCEL) {
            return;
        }

        boolean flag = phieuPhatDao.xoaPhieuPhat(selectedForDelete);
        if (flag) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Xóa phiếu phạt thành công !");
            alert.showAndWait();
            danhSachPhieuPhat.remove(selectedForDelete);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Không xóa được phiếu phạt, vui lòng kiểm tra lại !");
            alert.showAndWait();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initColumn();
        tablePhieuPhat.setItems(danhSachPhieuPhat);
        tablePhieuPhat.getSortOrder().add(maPhieuPhatColumn);
        searchPhieuPhatInfo();
    }

    private void searchPhieuPhatInfo() {
        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        // Đưa đối tượng ObservableList "data" vào trong một đối tượng của FilteredList (khởi đầu là show toàn bộ data)
        FilteredList<PhieuPhat> filteredData = new FilteredList<>(danhSachPhieuPhat, b -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        // tạo một bộ lọc để xác nhận bất kỳ thay đổi nào
        tfSearchPhieuPhatInfo.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(phieuPhat -> {
                // If filter text is empty, display all persons.
                // nếu ô tìm kiếm trống, thì show toàn bộ người
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

//                DateFormat dateFormat = new SimpleDateFormat(patternDay);

                // Compare first name and last name of every person with filter text.
                // ép về kiểu chữ thường rồi so sánh dữ liệu trong DB vs dữ liệu trong bộ lọc
                String lowerCaseFilter = newValue.toLowerCase();

                if (phieuPhat.getTenDocGia().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches first name. Khi trùng với first name
                } else if (phieuPhat.getTenTuaSach().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches last name.
                } else if (String.valueOf(phieuPhat.getMaPhieuPhat()).toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches last name.
                } else if (String.valueOf(phieuPhat.getMaDocGia()).toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (String.valueOf(phieuPhat.getMaPhieuTra()).toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (String.valueOf(phieuPhat.getTienPhat()).toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else
                    return false; // Does not match.
            });
        });

//        if (String.valueOf(employee.getSalary()).indexOf(lowerCaseFilter)!=-1)
//            return true;
        // ép kiểu về String để lọc (String.valueOf(employee.getSalary())

        // 3. Wrap the FilteredList in a SortedList.
        // đưa filter list vào trong sorted list
        SortedList<PhieuPhat> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        // 	  Otherwise, sorting the TableView would have no effect.
        // kiểm tra xem sortlist có khớp với tableview không
        // nếu không khớp thì tableview không bị ảnh hưởng
        sortedData.comparatorProperty().bind(tablePhieuPhat.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        // show dữ liệu đã được lọc và sắp xếp ra bảng
        tablePhieuPhat.setItems(sortedData);
        tablePhieuPhat.getSortOrder().add(maPhieuPhatColumn);
    }

    private void initColumn() {
        maPhieuPhatColumn.setCellValueFactory(new PropertyValueFactory<>("maPhieuPhat"));
        maDocGiaColumn.setCellValueFactory(new PropertyValueFactory<>("maDocGia"));
        tenDocGiaColumn.setCellValueFactory(new PropertyValueFactory<>("tenDocGia"));
        maPhieuTraColumn.setCellValueFactory(new PropertyValueFactory<>("maPhieuTra"));
        tenTuaSachColumn.setCellValueFactory(new PropertyValueFactory<>("tenTuaSach"));
        tienPhatColumn.setCellValueFactory(new PropertyValueFactory<>("tienPhat"));
        maCuonSachColumn.setCellValueFactory(new PropertyValueFactory<>("maCuonSach"));
    }

    @FXML
    void openInPhieuPhat(ActionEvent event) throws JRException {
        selectedPhieuPhat = tablePhieuPhat.getSelectionModel().getSelectedItem();
        if (selectedPhieuPhat == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setContentText("Bạn chưa chọn dòng nào để cập nhật cả");
            alert.showAndWait();
            return;
        }

        int maPP = selectedPhieuPhat.getMaPhieuPhat();

        HashMap hashmap = new HashMap();
        hashmap.put("MAPHIEUPHAT", maPP);

        /*
        Tạo kết nối với CSDL
        Chọn đường dẫn file report đã tạo, và file output dạng pdf
         */
        Connection connection = JDBCConnection.getJDBCConnection();
        String dir = ".\\src\\home\\report\\InPhieuPhat.jrxml";
//        String pdf = "D:\\IntelliJ\\iReport\\Data\\DemoTK1\\ReportCoThamSo\\ReportCoThamSo.pdf";

        /*
        Gọi các thư viện của Jasper report
         */
        JasperDesign jd = JRXmlLoader.load(dir);
        JasperReport jr = JasperCompileManager.compileReport(dir);
        JasperPrint jp = JasperFillManager.fillReport(jr, hashmap, connection);
        JasperViewer jv = new JasperViewer(jp, false); // tắt report mà không tắt chương trình
        jv.setVisible(true);
    }
}
