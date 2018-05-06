/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.Controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class
 *
 * @author mananmolhurt
 */
public class GradeMainController implements Initializable {
    
    @FXML private BorderPane mainPane;
    
    @FXML private TableView mainTable;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        mainTable.getItems().addAll("","","","","","","","","","","","","","","","","","","","","","","");
    }    
    
    public void setMainContainer(BorderPane mainPane) {
        
        this.mainPane = mainPane;
    }
}
