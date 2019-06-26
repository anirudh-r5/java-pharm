package pharm;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class Login {

    public TextField uid,pid;
    public Button login;
    private Connection conn = null;
    public void initialize() {
        if (conn == null)
            connect();
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

    public void loGin() {
        String sql1="SELECT * from login WHERE id=? and password=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql1)) {
            ResultSet rs;
            pstmt.setString(1, uid.getText());
            pstmt.setString(2, pid.getText());
            rs = pstmt.executeQuery();
            if (rs.isClosed()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText(null);
                alert.setContentText("Incorect User ID/Password");
                alert.show();
            }
            else{
                try{
                Stage primaryStage=new Stage();
                Stage tmp=(Stage)  login.getScene().getWindow();
                Parent root = FXMLLoader.load(getClass().getResource("frontPage.fxml"));
                primaryStage.setTitle("Main Menu");
                primaryStage.setScene(new Scene(root,800,600));
                primaryStage.setMinHeight(600);
                primaryStage.setMinWidth(800);
                primaryStage.setMaxHeight(primaryStage.getMinHeight());
                primaryStage.setMaxWidth(primaryStage.getMinWidth());
                tmp.close();
                primaryStage.show();
            }catch(IOException e){
                    System.out.println(e.getMessage());
                }}
        }
             catch(SQLException e){
                System.out.println(e.getMessage());
            }
        }

            }

