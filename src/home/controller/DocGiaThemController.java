package home.controller;


import home.Main;
import home.dao.DocGiaDao;
import home.model.DocGia;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public class DocGiaThemController {

    DocGiaDao memberDao = new DocGiaDao();
    Main window = new Main();

    @FXML
    private TextField tfHoDocGia;

    @FXML
    private TextField tfTenDocGia;

    @FXML
    private TextField tfSDT;

    @FXML
    private TextField tfEmail;

    @FXML
    private Button btnAddMember;

    @FXML
    private Button btnCancel;

    private MainGUIController controller;

    @FXML
    public BorderPane borderPane;

    @FXML
    void addMemberAction(ActionEvent event) throws SQLException {
        String ho = tfHoDocGia.getText();
        String ten = tfTenDocGia.getText();
        String sdt = tfSDT.getText();
        String email = tfEmail.getText();

        Alert alert;
        if (ho.isEmpty() || ten.isEmpty() || sdt.isEmpty() || email.isEmpty()) {
            alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setContentText("Bạn phải nhập đầy đủ thông tin");
            alert.showAndWait();
            return;
        }

        DocGia member = new DocGia(ho, ten, sdt, email);
        boolean flag = memberDao.addMember(member);
        if (flag) {
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Thêm độc giả mới thành công !!!");
            alert.showAndWait();
            cancelAction(event);
        } else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Không thêm được độc giả");
            alert.showAndWait();
            cancelAction(event);
        }

    }


    @FXML
    void cancelAction(ActionEvent event) {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
        window.loadAnotherWindow("/home/fxml/DocGiaDanhSach.fxml");

    }



}
