package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;

public class Controller {
    @FXML
    TableView colOrd;
    @FXML
    TableColumn colSl, colID, colName, colPrice, colQty, colAmt;
    @FXML
    TextField ID, QTY, name, wt, age, phone;
    @FXML
    RadioButton genM, genF, genO;
    Connection conn = null;
    ObservableList<tabData> list = FXCollections.observableArrayList();

    public void initialize() {
        System.out.println("UI Controller working!!!");
        connect();
//        System.out.println("\nSl\t\tID\t\tName\t\tPrice\t\tQty\t\tAmt\nNo.\n");
    }

    @FXML
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
            td.setSl(tabData.slTrack);
            colSl.setCellValueFactory(new PropertyValueFactory<tabData, Integer>("Sl"));
            colID.setCellValueFactory(new PropertyValueFactory<tabData, Integer>("ItemID"));
            colQty.setCellValueFactory(new PropertyValueFactory<tabData, Integer>("Qty"));
            colAmt.setCellValueFactory(new PropertyValueFactory<tabData, Integer>("Amt"));
            colPrice.setCellValueFactory(new PropertyValueFactory<tabData, Integer>("Price"));
            colName.setCellValueFactory(new PropertyValueFactory<tabData, String>("Name"));
            list.add(td);
            colOrd.setItems(list);
            colOrd.setVisible(true);
//            System.out.println(tabData.slTrack + "\t\t" + td.getItemID() + "\t\t" + td.getName() + "\t\t" +
//                    td.getPrice() + "\t\t" + td.getQty() + "\t\t" + td.getAmt() + "\n");
        }
    }

    private void connect() {
        String url = "jdbc:sqlite:C:/sqlite/test.db";
        try {
            conn = DriverManager.getConnection(url);
            System.out.println("Connected to DB");
        } catch (SQLException s) {
            System.out.println(s.getMessage());
        }
    }

    @FXML
    private void reader() {
//        System.out.println(this.toString());
//        if(ke.getCode() == KeyCode.ENTER )
        query(Integer.parseInt(ID.getText()), Integer.parseInt(QTY.getText()));
    }

    @FXML
    private void query(int i, int q) {
        String sql = "SELECT ID, name, price, qty FROM stocks where ID = ?";
        tabData td = new tabData();
        ResultSet rs = null;
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
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            printOut(td, q);
        }
    }

    @FXML
    private void allSubmit() {
    }
}
