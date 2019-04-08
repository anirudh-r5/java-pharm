package pharm;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class FrontPage {
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
}
