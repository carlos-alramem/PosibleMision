/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.Controllers;

import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author carlo
 */
public class FXMLCharger {

    private static List<FXMLComponents> fxmls = new ArrayList();
    //private static FXMLLoader file;
    
    private static boolean charger = false;
    
    public enum fxml{
        

    } /// Aquí lo haré con enumenraciones mejor.
    
    
    private static String [] gui = {"Grade","GradeConsolidate","GradeHonor","GradeMain",
        "GradeScore","GradeSheet","Init","Main","Students","Teacher"};
    private static String [] menus = {"AdminMenu","GradeMenu","MainMenu"};
    
    
    public FXMLCharger(){
        chargerFXML();
        
       
    }
    
    private static void chargerFXML(){
        
        
        //fxmls = new ArrayList();
        
        try {
            
            for(String fxml : gui){
                FXMLLoader file = new FXMLLoader(FXMLCharger.class.getResource("/GUI/FXMLs/"+fxml+".fxml"));
                fxmls.add(new FXMLComponents(file));
            }
            
            System.out.println(fxmls.size());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
        
    }
    
    public static FXMLComponents getGUI(int index){
        try {
            System.out.println("Tamaño lista " +((fxmls == null) ? fxmls.size(): "Es nula" ));
            //if(!fxmls.equals(null)){
                if(!charger || fxmls.equals(null)){
                    System.out.println("Se descargó");
                    chargerFXML();
                    System.out.println("Cargo");
                    charger = true;
                }
            //}
        } catch (Exception e) {
            System.out.println("*******kkkkk"+e.getMessage());
        }
        
        return fxmls.get(index);
    }
    
    
    public static class FXMLComponents{
        
        private FXMLLoader fxml;
        private AnchorPane fxmlPane;
        private Class<?> fxmlController;
        
        public FXMLComponents(FXMLLoader input){
            
            try {
                fxml = input;
                fxmlPane = (AnchorPane) input.load();
                fxmlController = input.getController();
            } catch (Exception e) {
            }
            
            
        }
        
        public FXMLLoader getFXMLLoader(){
            return fxml;
        }
        
        public AnchorPane getFXMLPanel(){
            return fxmlPane;
        }
        
        public Class<?> getFXMLController(){
            return fxmlController;
        }

    }
    
}
