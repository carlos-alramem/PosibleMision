/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.Controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.LoadException;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class MainMenuController implements Initializable {
    
    @FXML private BorderPane mainPane;
    
    private MainController parent;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
   
    }
    
    public void setParent(MainController parent) {
        
        this.parent = parent;
    }
    
    public void setMainContainer(BorderPane mainPane) {
        
        this.mainPane = mainPane;
    }
    
    public void gradButtonClic() {
        
        try{                        
            
            FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("/GUI/FXMLs/Menus/GradeMenu.fxml"));
            AnchorPane menu = (AnchorPane) menuLoader.load();
            GradeMenuController menuController = menuLoader.getController();
            menuController.setParent(parent);
            menuController.setMainContainer(mainPane);
            mainPane.setLeft(menu);
            
            FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/GUI/FXMLs/GradeMain.fxml"));
            AnchorPane main = (AnchorPane) mainLoader.load();
            GradeMainController mainController = mainLoader.getController();
            mainController.setMainContainer(mainPane);
            mainPane.setCenter(main);
            
            parent.setTittle("Registro de Notas");
            parent.setEnableBackButton(true);
            parent.setEnableHomeButton(true);
        }
        catch(Exception ex){
            
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    public void adminButtonClic() {
        
        try{                        
            
            FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("/GUI/FXMLs/Menus/AdminMenu.fxml"));
            AnchorPane menu = (AnchorPane) menuLoader.load();
            AdminMenuController menuController = menuLoader.getController();
            menuController.setMainContainer(mainPane);
            mainPane.setLeft(menu);
            
            FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/GUI/FXMLs/Students.fxml"));
            AnchorPane main = (AnchorPane) mainLoader.load();
            //StudentsController mainController = mainLoader.getController();
            mainPane.setCenter(main);
            
            parent.setTittle("Administrar Estudiantes");
            parent.setEnableBackButton(true);
            parent.setEnableHomeButton(true);
        }
        catch(LoadException t){
            System.out.println("Error: " + t.getMessage());
            t.printStackTrace();
        }
        catch(Exception ex){
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    public void repButtonClic() {
        
    }
    
    public void statButtonClic() {
        
    }
}
