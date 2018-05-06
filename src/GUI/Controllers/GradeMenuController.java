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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class GradeMenuController implements Initializable {

    @FXML private BorderPane mainPane;
    
    private MainController parent;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    public void setParent(MainController parent) {
        
        this.parent = parent;
    }
    
    public void setMainContainer(BorderPane mainPane) {
        
        this.mainPane = mainPane;
    }    
    
    public void inputButtonClic() {
        
        try{                        
            
            FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/GUI/FXMLs/GradeMain.fxml"));
            AnchorPane main = (AnchorPane) mainLoader.load();
            GradeMainController mainController = mainLoader.getController();
            mainController.setMainContainer(mainPane);
            mainPane.setCenter(main);
            
            parent.setTittle("Ingresar Notas");
            parent.setEnableBackButton(true);
            parent.setEnableHomeButton(true);
        }
        catch(Exception ex){
            
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    public void consolidatedButtonClic() {
        
        try{                        
            
            FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/GUI/FXMLs/GradeConsolidated.fxml"));
            AnchorPane main = (AnchorPane) mainLoader.load();
            GradeConsolidatedController mainController = mainLoader.getController();
            mainController.setMainContainer(mainPane);
            mainPane.setCenter(main);
            
            parent.setTittle("Consolidado de Notas");
            parent.setEnableBackButton(true);
            parent.setEnableHomeButton(true);
        }
        catch(Exception ex){
            
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    public void honorButtonClic() {
        
        try{                        
            
            FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/GUI/FXMLs/GradeHonor.fxml"));
            AnchorPane main = (AnchorPane) mainLoader.load();
            GradeHonorController mainController = mainLoader.getController();
            mainController.setMainContainer(mainPane);
            mainPane.setCenter(main);
            
            parent.setTittle("Cuadro de Honor");
            parent.setEnableBackButton(true);
            parent.setEnableHomeButton(true);
        }
        catch(Exception ex){
            
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    public void sheetButtonClic() {
        
        try{                        
            
            FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/GUI/FXMLs/GradeSheet.fxml"));
            AnchorPane main = (AnchorPane) mainLoader.load();
            GradeSheetController mainController = mainLoader.getController();
            mainController.setMainContainer(mainPane);
            mainPane.setCenter(main);
            
            parent.setTittle("Sábana");
            parent.setEnableBackButton(true);
            parent.setEnableHomeButton(true);
        }
        catch(Exception ex){
            
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    public void scoreButtonClic() {
        
        try{                        
            
            FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/GUI/FXMLs/GradeScore.fxml"));
            AnchorPane main = (AnchorPane) mainLoader.load();
            GradeScoreController mainController = mainLoader.getController();
            mainController.setMainContainer(mainPane);
            mainPane.setCenter(main);
            
            parent.setTittle("Puntaje Total");
            parent.setEnableBackButton(true);
            parent.setEnableHomeButton(true);
        }
        catch(Exception ex){
            
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
