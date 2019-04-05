package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import java.sql.*;

public class Controller {
    @FXML
    TextField ID, QTY;
    Connection conn = null;

    public void initialize() {
        System.out.println("UI Controller working!!!");
        connect();
        System.out.println("\nID\t\tName\t\tPrice\t\tQty\t\tAmt\n");
    }

    @FXML
    private void printOut(tabData td, int q) {
        if (td.getName() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText(null);
            alert.setContentText("Invalid Item ID. Please enter a valid item ID");
            alert.show();
        } else if (q > td.getQty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("No stock");
            alert.setContentText("The entered quantity is unavailable");
            alert.show();
        } else
            System.out.println(td.getItemID() + "\t\t" + td.getName() + "\t\t" + td.getPrice() + "\t\t" + td.getQty() + "\t\t" + td.getAmt() + "\n");
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
        String sql = "SELECT ID, name, price, qty " + "FROM stocks where ID = ?";
        tabData td = new tabData();
        ResultSet rs = null;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, i);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                td.setItemID(rs.getInt("ID"));
                td.setName(rs.getString("name"));
                td.setPrice(rs.getInt("price"));
                td.setQty(rs.getInt("qty"));
                td.setAmt((td.getQty() * td.getPrice()));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            printOut(td, q);
        }
    }

    @FXML
    private void allSubmit(KeyEvent ke) {

    }
}
