package pharm;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("frontPage.fxml"));
        primaryStage.setTitle("Main Menu");
        primaryStage.setScene(new Scene(root,800,600));
        primaryStage.setMinHeight(600);
        primaryStage.setMinWidth(800);
        primaryStage.setMaxHeight(primaryStage.getMinHeight());
        primaryStage.setMaxWidth(primaryStage.getMinWidth());
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
