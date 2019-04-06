package pharm;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;


import java.sql.*;

public class Orders {
    @FXML
    Text taxField,amtField,name,phone,age,wt,gen;
    @FXML
    TextField srchBox;
    @FXML
    TableColumn colSl, colID, colName, colQty;
    @FXML
    TableView colOrd;
    @FXML
    VBox box;
    private Connection conn = null;
    private ObservableList<tabData> list = FXCollections.observableArrayList();
    public void initialize() {
        if (conn == null)
            connect();
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
    private void searchOrd() {
        allReset();
        int search = Integer.parseInt(srchBox.getText());
        String sql = "SELECT * FROM orders o,stocks s WHERE OID=? AND o.item=s.ID";
        tabData td = new tabData();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs;
            pstmt.setInt(1, search);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                td.setSl(tabData.slTrack++);
                td.setItemID(rs.getInt("item"));
                td.setName(rs.getString("name"));
                td.setPrice(rs.getInt("price"));
                td.setQty(rs.getInt("oqty"));
                td.setAmt(td.getQty() * td.getPrice());
                td.setCustName(rs.getString("cname"));
                td.setAge(rs.getInt("age"));
                td.setPhone(rs.getString("phone"));
                td.setWt(rs.getDouble("weight"));
                td.setGen(rs.getString("gender"));
                printOut(td);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void printOut(tabData td) {
        if (td.getName() == null) {
            tabData.slTrack--;
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText(null);
            alert.setContentText("Order not found");
            alert.show();
        } else {
            box.setVisible(true);
            colSl.setCellValueFactory(new PropertyValueFactory<tabData, Integer>("Sl"));
            colID.setCellValueFactory(new PropertyValueFactory<tabData, Integer>("ItemID"));
            colQty.setCellValueFactory(new PropertyValueFactory<tabData, Integer>("Qty"));
            colName.setCellValueFactory(new PropertyValueFactory<tabData, String>("Name"));
            list.add(td);
            colOrd.setItems(list);
            colOrd.setVisible(true);
            double tmp = Double.parseDouble(taxField.getText()) + (0.05 * td.getAmt());
            taxField.setText(tmp + "");
            tmp = tmp + td.getAmt() + Double.parseDouble(amtField.getText());
            amtField.setText(tmp + "");
            name.setText(td.getName());
            phone.setText(td.getPhone());
            age.setText(td.getAge()+"");
            wt.setText(td.getWt()+"");
            gen.setText(td.getGen());
        }
    }
    private void allReset() {
        box.setVisible(false);
        colOrd.getItems().clear();
        taxField.setText("0");
        amtField.setText("0");
    }
}
