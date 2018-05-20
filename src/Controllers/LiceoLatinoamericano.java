/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Entities.Alumno;
import GUI.Controllers.MainController;
import java.util.List;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class LiceoLatinoamericano extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/FXMLs/Main.fxml"));
            Parent root = loader.load();
            Scene sceneHome = new Scene(root);
            MainController controller = loader.getController();
            controller.setStage(primaryStage);
            controller.setTittle("Inicio");
            primaryStage.setScene(sceneHome);
            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.show();
            
                        
        } 
        catch (Exception e) {
            
            System.out.println("Estoy en el START "+ e.getMessage());
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
 }
