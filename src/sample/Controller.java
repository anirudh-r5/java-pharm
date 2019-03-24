package sample;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class Controller {
    @FXML
    TextField IDfield,QTYfield;
    TableColumn IDcol,QTYcol;
    public void initialize() {
        System.out.println("UI Controller working!!!");
    }

    @FXML
    private void printOut(){
        System.out.println(IDfield.getText());
        System.out.println(QTYfield.getText());

    }
}
