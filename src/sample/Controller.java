package sample;

import java.sql.*;


import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import sample.tabData;

public class Controller {
    @FXML
    TextField ID, QTY;
    @FXML
    ObservableList obArr;
    tabData td = new tabData();
    String url = "jdbc:sqlite:C:/sqlite/test.db";
    Connection conn = null;

    public void initialize() {
        System.out.println("UI Controller working!!!");
        connect();
        System.out.println("\nID\t\tName\t\tPrice\t\tQty\t\tAmt\n");
    }

    @FXML
    private void printOut() {
        System.out.println(td.getItemID() + "\t\t" + td.getName() + "\t\t" + td.getPrice() + "\t\t" + td.getQty() + "\t\t" + td.getAmt() + "\n");
    }

    private void connect() {
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
        String sql = "SELECT ID, name, price, qty " + "FROM stocks where ID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, i);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                td.setItemID(rs.getInt("ID"));
                td.setName(rs.getString("name"));
                td.setPrice(rs.getInt("price"));
                td.setQty(q);
                td.setAmt((td.getQty() * td.getPrice()));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            printOut();
        }
    }
}
