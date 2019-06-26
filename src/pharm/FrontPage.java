package pharm;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class FrontPage {
    public Button logOut;
    @FXML
    Button stck,ord,bill;
    @FXML
    private void onStck() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("stocks.fxml"));
        Stage stage=new Stage();
        Stage tmp=(Stage) stck.getScene().getWindow();
        stage.setTitle("Stocks");
        stage.setScene(new Scene(root));
        stage.setMinHeight(800);
        stage.setMinWidth(1024);
        tmp.close();
        stage.show();
    }
    @FXML
    private void onBill() throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("billing.fxml"));
        Stage stage=new Stage();
        Stage tmp=(Stage) stck.getScene().getWindow();
        stage.setTitle("Billing");
        stage.setScene(new Scene(root));
        stage.setMinHeight(800);
        stage.setMinWidth(1024);
        tmp.close();
        stage.show();
    }
    @FXML
    private void onOrd() throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("orders.fxml"));
        Stage stage=new Stage();
        Stage tmp=(Stage) stck.getScene().getWindow();
        stage.setTitle("Orders");
        stage.setScene(new Scene(root));
        stage.setMinHeight(800);
        stage.setMinWidth(1024);
        tmp.close();
        stage.show();
    }

    public void logoutbtn ()throws IOException {
        Stage primaryStage=new Stage();
       Stage tmp=(Stage)  logOut.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        primaryStage.setTitle("Main Menu");
        primaryStage.setScene(new Scene(root,800,600));
        primaryStage.setMinHeight(600);
        primaryStage.setMinWidth(800);
        primaryStage.setMaxHeight(primaryStage.getMinHeight());
        primaryStage.setMaxWidth(primaryStage.getMinWidth());
        tmp.close();
        primaryStage.show();
    }
}
