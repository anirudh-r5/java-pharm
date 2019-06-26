package pharm;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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


public class Stocks {

    @FXML
    private TextField srchID, srchQty, srchPrice, srchName, modID, modName, modQty, modPrice;
    @FXML
    private TableColumn colID, colName, colPrice, colQty, colSl;
    @FXML
    private TableView colOrd;
    @FXML
    RadioButton radioNew, radioExis, radioDelet;
    @FXML
    ToggleGroup Toggle1;
    @FXML
    Text itemLabel;
    @FXML
    private Button modAdd,backBtn;
    private Connection conn = null;
    private ObservableList<tabData> list = FXCollections.observableArrayList();

    public void initialize() {
        if (conn == null)
            connect();
        srchID.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> changed, String oldS, String newS) {
                if (!srchID.getText().isEmpty()) {
                    srchQty.setDisable(true);
                    srchPrice.setDisable(true);
                    srchName.setDisable(true);
                } else {
                    srchQty.setDisable(false);
                    srchPrice.setDisable(false);
                    srchName.setDisable(false);
                }
            }
        });
        srchQty.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> changed, String oldS, String newS) {
                if (!srchQty.getText().isEmpty()) {
                    srchID.setDisable(true);
                    srchPrice.setDisable(true);
                    srchName.setDisable(true);
                } else {
                    srchID.setDisable(false);
                    srchPrice.setDisable(false);
                    srchName.setDisable(false);
                }
            }
        });
        srchName.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> changed, String oldS, String newS) {
                if (!srchName.getText().isEmpty()) {
                    srchQty.setDisable(true);
                    srchPrice.setDisable(true);
                    srchID.setDisable(true);
                } else {
                    srchQty.setDisable(false);
                    srchPrice.setDisable(false);
                    srchID.setDisable(false);
                }
            }
        });
        srchPrice.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> changed, String oldS, String newS) {
                if (!srchPrice.getText().isEmpty()) {
                    srchQty.setDisable(true);
                    srchID.setDisable(true);
                    srchName.setDisable(true);
                } else {
                    srchQty.setDisable(false);
                    srchID.setDisable(false);
                    srchName.setDisable(false);
                }
            }
        });
        colSl.setCellValueFactory(new PropertyValueFactory<tabData, Integer>("Sl"));
        colID.setCellValueFactory(new PropertyValueFactory<tabData, Integer>("ItemID"));
        colQty.setCellValueFactory(new PropertyValueFactory<tabData, Integer>("Qty"));
        colPrice.setCellValueFactory(new PropertyValueFactory<tabData, Integer>("Price"));
        colName.setCellValueFactory(new PropertyValueFactory<tabData, String>("Name"));
        colOrd.setItems(list);
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
    private void searchStk() {
        resetAll();
        TextField[] tmp = new TextField[]{srchID, srchName, srchQty, srchPrice};
        String url1 = "SELECT * FROM stocks WHERE ID=?";
        String url2 = "SELECT * FROM stocks WHERE name=?";
        String url3 = "SELECT * FROM stocks WHERE qty=?";
        String url4 = "SELECT * FROM stocks WHERE price=?";
        PreparedStatement pstmt;
        int flag = -1;
        for (int i = 0; i < 4; i++) {
            if (!tmp[i].isDisabled() && !tmp[i].getText().isEmpty()) {
                flag = i;
                break;
            }
        }
        try {
            switch (flag) {
                case 0:
                    pstmt = conn.prepareStatement(url1);
                    pstmt.setInt(1, Integer.parseInt(srchID.getText()));
                    break;
                case 1:
                    pstmt = conn.prepareStatement(url2);
                    pstmt.setString(1, srchName.getText());
                    break;
                case 2:
                    pstmt = conn.prepareStatement(url3);
                    pstmt.setInt(1, Integer.parseInt(srchQty.getText()));
                    break;
                case 3:
                    pstmt = conn.prepareStatement(url4);
                    pstmt.setInt(1, Integer.parseInt(srchPrice.getText()));
                    break;
                default:
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Please enter values to filter");
                    alert.show();
                    return;
            }
            ResultSet rs = pstmt.executeQuery();
            tabData td;
            if (rs.isClosed()) {
                td = null;
                printOut(td);
            } else {
                while (rs.next()) {
                    td=new tabData();
                    td.setSl(tabData.slTrack++);
                    td.setItemID(rs.getInt("ID"));
                    td.setName(rs.getString("name"));
                    td.setPrice(rs.getInt("price"));
                    td.setQty(rs.getInt("qty"));
                    printOut(td);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void printOut(tabData td) {
        if (td == null) {
            tabData.slTrack--;
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText(null);
            alert.setContentText("No items found");
            alert.show();
        } else {
            list.add(td);

        }
    }

    private void resetAll() {
        tabData.slTrack = 1;
        colOrd.getItems().clear();
        modID.setVisible(false);
        modName.setVisible(false);
        modPrice.setVisible(false);
        modQty.setVisible(false);
        modAdd.setVisible(false);
        itemLabel.setVisible(false);
        modID.setText(null);
        modName.setText(null);
        modPrice.setText(null);
        modQty.setText(null);
        RadioButton tmp = (RadioButton) Toggle1.getSelectedToggle();
        if(tmp!=null)
            tmp.setSelected(false);
    }

    @FXML
    private void radioSelect() {
        itemLabel.setVisible(true);
        if (Toggle1.getSelectedToggle() == radioNew) {
            modID.setVisible(true);
            modName.setVisible(true);
            modPrice.setVisible(true);
            modQty.setVisible(true);
            modAdd.setVisible(true);
            modAdd.setText("Add Item");
        } else if (Toggle1.getSelectedToggle() == radioExis) {
            modID.setVisible(true);
            modQty.setVisible(true);
            modName.setVisible(false);
            modPrice.setVisible(false);
            modAdd.setVisible(true);
            modAdd.setText("Add Item");
        } else {
            modID.setVisible(true);
            modName.setVisible(false);
            modPrice.setVisible(false);
            modQty.setVisible(false);
            modAdd.setVisible(true);
            modAdd.setText("Delete Item");
        }

    }

    @FXML
    private void modItem() {
        String url;
        if (Toggle1.getSelectedToggle() == radioNew) {
            if (modID.getText().isEmpty() || modName.getText().isEmpty() || modQty.getText().isEmpty() ||
                    modPrice.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Details");
                alert.setContentText("Please enter all details of new item");
                alert.show();
            } else {
                url = "INSERT INTO stocks VALUES(?,?,?,?)";
                try (PreparedStatement pstmt = conn.prepareStatement(url)) {
                    pstmt.setInt(1, Integer.parseInt(modID.getText()));
                    pstmt.setString(2, modName.getText());
                    pstmt.setInt(3, Integer.parseInt(modPrice.getText()));
                    pstmt.setInt(4, Integer.parseInt(modQty.getText()));
                    pstmt.executeUpdate();
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Item Added!");
                    alert.show();
                    resetAll();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        else if(Toggle1.getSelectedToggle() == radioExis){
            if (modID.getText().isEmpty() || modQty.getText().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Details");
                alert.setContentText("Please enter all details of item");
                alert.show();
            }
            else{
                url="UPDATE stocks SET qty=? WHERE ID=?";
                try (PreparedStatement pstmt = conn.prepareStatement(url)) {
                    pstmt.setInt(1,Integer.parseInt(modQty.getText()));
                    pstmt.setInt(2, Integer.parseInt(modID.getText()));
                    pstmt.executeUpdate();
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Item Added!");
                    alert.show();
                    resetAll();
                }catch(SQLException e){System.out.println(e.getMessage());}
            }
        }
        else{
            if (modID.getText().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Details");
                alert.setContentText("Please enter all details of item");
                alert.show();
            }
            else{
                url="DELETE FROM stocks WHERE ID=?";
                try (PreparedStatement pstmt = conn.prepareStatement(url)) {
                    pstmt.setInt(1, Integer.parseInt(modID.getText()));
                    pstmt.executeUpdate();
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Item Deleted!");
                    alert.show();
                    resetAll();
                }catch(SQLException e){System.out.println(e.getMessage());}
            }
        }
    }
    @FXML
    private void showAll() {
        String url="SELECT * FROM stocks";
        resetAll();
        try(PreparedStatement pstmt=conn.prepareStatement(url)){
            ResultSet rs=pstmt.executeQuery();
            tabData td;
            while (rs.next()) {
                td=new tabData();
                td.setSl(tabData.slTrack++);
                td.setItemID(rs.getInt("ID"));
                td.setName(rs.getString("name"));
                td.setPrice(rs.getInt("price"));
                td.setQty(rs.getInt("qty"));
                printOut(td);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
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
