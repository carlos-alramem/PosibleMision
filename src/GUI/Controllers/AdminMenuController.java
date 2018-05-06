/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.Controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class AdminMenuController implements Initializable {

    @FXML private BorderPane mainPane;
    
    @FXML private Button profButton;
    
    @FXML private Button estButton;
    
    @FXML private Button gradButton;
    
    private BorderPane mainContainer;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    public void setMainContainer(BorderPane mainPane) {
        
        this.mainPane = mainPane;
    }

    public void profButtonClic() {
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/FXMLs/Teacher.fxml"));
            AnchorPane container = (AnchorPane) loader.load();
            TeachersController controller = loader.getController();
            mainPane.setCenter(container);
        } 
        catch (Exception ex) {
             ex.printStackTrace();
            System.out.println("ProfButtonClic"+ex.getMessage());
        }
    }
    
    
    public void studentButtonClic(){
        try {
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/FXMLs/Students.fxml"));
            AnchorPane container = (AnchorPane) loader.load();
            mainPane.setCenter(container);
        } 
        catch (Exception ex) {
            
            ex.printStackTrace();
        }
    }

    /**
     * @return the mainContainer
     */
    public BorderPane getMainContainer() {
        return mainContainer;
    }
    
    public void gradButtonClic(){
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/FXMLs/Grade.fxml"));
            AnchorPane container = (AnchorPane) loader.load();
            mainPane.setCenter(container);
            
        } catch (Exception e) {
            
            System.out.println(e.getMessage());
        }
    
    
    }
    
    
}
