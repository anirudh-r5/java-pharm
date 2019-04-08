package pharm;

/* TODO - Add date to order section */


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class Billing {
    @FXML
    private Text taxField, amtField;
    @FXML
    private TableView colOrd;
    @FXML
    private TableColumn colSl, colID, colName, colPrice, colQty, colAmt;
    @FXML
    private TextField ID, QTY, name, wt, age, phone;
    @FXML
    private ToggleGroup Toggle1;
    @FXML
    private RadioButton genM, genF, genO;
    @FXML
    Button backBtn;
    private Connection conn = null;
    private ObservableList<tabData> list = FXCollections.observableArrayList();
    private static int ordTrack = 0;


    public void initialize() {
        if (conn == null)
            connect();
        try {
            String sql = "SELECT max(OID) FROM orders";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs= pstmt.executeQuery();
            ordTrack = rs.getInt(1) + 1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        colSl.setCellValueFactory(new PropertyValueFactory<tabData, Integer>("Sl"));
        colID.setCellValueFactory(new PropertyValueFactory<tabData, Integer>("ItemID"));
        colQty.setCellValueFactory(new PropertyValueFactory<tabData, Integer>("Qty"));
        colAmt.setCellValueFactory(new PropertyValueFactory<tabData, Integer>("Amt"));
        colPrice.setCellValueFactory(new PropertyValueFactory<tabData, Integer>("Price"));
        colName.setCellValueFactory(new PropertyValueFactory<tabData, String>("Name"));
        allReset();
    }

    private void connect() {
        String url = "jdbc:sqlite:res/test.db";
        try {
            conn = DriverManager.getConnection(url);
            System.out.println("Connected to DB");
        } catch (SQLException s) {
            System.out.println(s.getMessage());
        }
    }

    @FXML
    private void reader() {
        query(Integer.parseInt(ID.getText()), Integer.parseInt(QTY.getText()));
    }

    @FXML
    private void query(int i, int q) {
        String sql = "SELECT ID, name, price, qty FROM stocks where ID = ?";
        tabData td = new tabData();
        ResultSet rs;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, i);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                td.setSl(tabData.slTrack++);
                td.setItemID(rs.getInt("ID"));
                td.setName(rs.getString("name"));
                td.setPrice(rs.getInt("price"));
                td.setQty(rs.getInt("qty"));
            }
            printOut(td, q);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    private void printOut(tabData td, int q) {
        if (td.getName() == null) {
            tabData.slTrack--;
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Invalid Item ID");
            alert.setContentText("Please enter a valid item ID");
            alert.show();
        } else if (q > td.getQty()) {
            tabData.slTrack--;
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("No stock");
            alert.setContentText("The entered quantity is unavailable");
            alert.show();
        } else {
            td.setQty(q);
            td.setAmt(td.getQty() * td.getPrice());
            list.add(td);
            colOrd.setItems(list);
            colOrd.setVisible(true);
            double tmp = Double.parseDouble(taxField.getText()) + (0.05 * td.getAmt());
            taxField.setText(tmp + "");
            tmp = tmp + td.getAmt() + Double.parseDouble(amtField.getText());
            amtField.setText(tmp + "");
        }
    }
    @FXML
    private void allSubmit() {
        String sql1 = "INSERT INTO orders VALUES(?,?,?,?,?,?,?,?)";
        String sql2 = "UPDATE stocks SET qty=qty-? WHERE ID=?";
        PreparedStatement pstmt;
        try {
            for (int i = 1; i < tabData.slTrack; i++) {
                pstmt = conn.prepareStatement(sql1);
                pstmt.setInt(1, ordTrack);
                pstmt.setString(2, name.getText());
                pstmt.setInt(3, Integer.parseInt(age.getText()));
                pstmt.setString(4, phone.getText());
                pstmt.setDouble(5, Double.parseDouble(wt.getText()));
                if (Toggle1.getSelectedToggle() == genM)
                    pstmt.setString(6, "Male");
                else if (Toggle1.getSelectedToggle() == genF)
                    pstmt.setString(6, "Female");
                else
                    pstmt.setString(6, "Other");
                pstmt.setInt(7, (Integer) colID.getCellData(i - 1));
                pstmt.setInt(8, (Integer) colQty.getCellData(i - 1));
                pstmt.executeUpdate();
                pstmt = conn.prepareStatement(sql2);
                pstmt.setInt(1, (Integer) colQty.getCellData(i - 1));
                pstmt.setInt(2, (Integer) colID.getCellData(i - 1));
                pstmt.executeUpdate();
            }
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Order Complete");
            alert.setHeaderText(null);
            alert.setContentText("Order placed!");
            alert.show();
            ordTrack++;
            allReset();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void allReset() {
        colOrd.getItems().clear();
        ID.setText(null);
        QTY.setText(null);
        name.setText(null);
        wt.setText(null);
        age.setText(null);
        phone.setText(null);
        taxField.setText("0");
        amtField.setText("0");
        RadioButton tmp = (RadioButton) Toggle1.getSelectedToggle();
        if(tmp!=null)
            tmp.setSelected(false);
        tabData.slTrack = 1;
    }
    @FXML
    private void backMain() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("frontPage.fxml"));
        Stage stage=new Stage();
        Stage tmp=(Stage) backBtn.getScene().getWindow();
        stage.setTitle("Main Menu");
        stage.setScene(new Scene(root,800,800));
        stage.setMinHeight(600);
        stage.setMinWidth(800);
        stage.setMaxHeight(stage.getMinHeight());
        stage.setMaxWidth(stage.getMinWidth());
        tmp.close();
        stage.show();
    }
}
