package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.sql.*;

public class Controller {
    @FXML
    Text taxField, amtField;
    @FXML
    TableView colOrd;
    @FXML
    TableColumn colSl, colID, colName, colPrice, colQty, colAmt;
    @FXML
    TextField ID, QTY, name, wt, age, phone;
    @FXML
    ToggleGroup Toggle1;
    @FXML
    RadioButton genM, genF, genO;
    private Connection conn = null;
    private ObservableList<tabData> list = FXCollections.observableArrayList();
    private static int ordTrack=0;


    public void initialize() {
        System.out.println("UI Controller working!!!");
        if(conn==null)
            connect();
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
            double tmp = Double.parseDouble(taxField.getText()) + (0.05 * td.getAmt());
            taxField.setText(tmp + "");
            tmp = tmp + td.getAmt() + Double.parseDouble(amtField.getText());
            amtField.setText(tmp + "");
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
        String sql="INSERT INTO orders VALUES(?,?,?,?,?,?,?,?)";
        try(PreparedStatement pstmt=conn.prepareStatement(sql)){
            for(int i=1;i<=tabData.slTrack;i++){
                pstmt.setInt(1,ordTrack);
                pstmt.setString(2,name.getText());
                pstmt.setInt(3,Integer.parseInt(age.getText()));
                pstmt.setString(4,phone.getText());
                pstmt.setInt(5,Integer.parseInt(wt.getText()));
                if(Toggle1.getSelectedToggle()==genM)
                    pstmt.setString(6,"Male");
                else if(Toggle1.getSelectedToggle()==genF)
                    pstmt.setString(6,"Female");
                else
                    pstmt.setString(6,"Other");
                pstmt.setInt(7, (Integer)colID.getCellData(i-1));
                pstmt.setInt(8, (Integer)colQty.getCellData(i-1));
                pstmt.executeUpdate();
            }
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Order Complete");
            alert.setHeaderText(null);
            alert.setContentText("Order placed!");
            alert.show();
            ordTrack++;
        }
        catch(SQLException e){
            System.out.println(e);
        }
    }

    @FXML
    private void allReset(){
        colOrd.getItems().clear();
        ID.setText(null);
        QTY.setText(null);
        name.setText(null);
        wt.setText(null);
        age.setText(null);
        phone.setText(null);
        taxField.setText("0");
        amtField.setText("0");
        RadioButton tmp=(RadioButton)Toggle1.getSelectedToggle();
        tmp.setSelected(false);
    }
}
