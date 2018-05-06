/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.Controllers;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

public class MainController implements Initializable {

    @FXML private AnchorPane menuPane;
    @FXML private BorderPane mainPane;
    @FXML private BorderPane headerPane;
    
    @FXML private Button closeButton;
    @FXML private Button maxButton;
    @FXML private Button minButton;
    @FXML private Button backButton;
    @FXML private Button forwButton;
    @FXML private Button homeButton;
    @FXML private Button settButton;
    @FXML private Button helpButton;
    
    @FXML private Label tittleLabel;
    @FXML private Label timeLabel;
    @FXML private Label estateLabel;
    
    @FXML private MainMenuController menuPaneController;
    
    private boolean maximized = false;
    private double x;
    private double y;
    private double height;
    private double width;
    private double mx;
    private double my;
    
    private Stage primaryStage;
    
    private FXMLCharger fxmlCharger;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        Timeline timeline = new Timeline(
            new KeyFrame(
                Duration.millis(500),
                event -> {
                    
                    timeLabel.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm a")));
                }
            )
        );
        timeline.setCycleCount( Animation.INDEFINITE );
        timeline.play();
        menuPaneController.setParent(this);
        menuPaneController.setMainContainer(mainPane);
    }
    
    public void setFXMLCharger(FXMLCharger charger){
        this.fxmlCharger = charger;
    }
    
    public FXMLCharger getFXMLCharger(FXMLCharger charger){
        return this.fxmlCharger;
    }
    
    public void setStage(Stage stage) {
        
        primaryStage = stage;
    }
    
    public void setTittle(String tittle) {
        
        tittleLabel.setText("Liceo Latinoamericano - " + tittle);
    }
    
    public void setEnableBackButton(boolean enable) {
        
        backButton.setDisable(!enable);
    }
    
    public void setEnableForwButton(boolean enable) {
        
        forwButton.setDisable(!enable);
    }
    
    public void setEnableHomeButton(boolean enable) {
        
        homeButton.setDisable(!enable);
    }
    
    public BorderPane getMainContainer() {
        
        return mainPane;
    }
    
    public void closeButtonClic() {
        
        Platform.exit();
    }
    
    public void maxButtonClic() {
        
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        
        if (maximized) {
            
            primaryStage.setX(x);
            primaryStage.setY(y);
            primaryStage.setWidth(width);
            primaryStage.setHeight(height);
            maxButton.setText("");
            maxButton.getTooltip().setText("Maximizar");
            maximized = false;
        }
        else {
            
            x = primaryStage.getX();
            y = primaryStage.getY();
            width = primaryStage.getWidth();
            height = primaryStage.getHeight();
            primaryStage.setX(bounds.getMinX());
            primaryStage.setY(bounds.getMinY());
            primaryStage.setWidth(bounds.getWidth());
            primaryStage.setHeight(bounds.getHeight());
            maxButton.setText("");
            maxButton.getTooltip().setText("Restaurar");
            maximized = true;
        }
    }
    
    public void minButtonClic() {
        
        primaryStage.setIconified(true);
    }
    
    public void headerPaneMousePressed(MouseEvent me) {
        
        mx = primaryStage.getX() - me.getScreenX();
        my = primaryStage.getY() - me.getScreenY();
    }
    
    public void headerPaneMouseDragged(MouseEvent me) {
        
        if (!maximized) {
            
        primaryStage.setX(me.getScreenX() + mx);
        primaryStage.setY(me.getScreenY() + my);
        }
    }
    
    public void homeButtonClic() {
        
        try {
            FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("/GUI/FXMLs/Menus/MainMenu.fxml"));
            AnchorPane menu = (AnchorPane) menuLoader.load();
            MainMenuController menuController = menuLoader.getController();
            menuController.setParent(this);
            menuController.setMainContainer(mainPane);
            mainPane.setLeft(menu);
            mainPane.setCenter(null);
            
            setTittle("Inicio");
            setEnableBackButton(false);
            setEnableForwButton(true);
            setEnableHomeButton(false);
        }
        catch(Exception ex) {
            
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
