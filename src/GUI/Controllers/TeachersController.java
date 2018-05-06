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
import Entities.Teacher;
import java.awt.Robot;
import java.util.Optional;
import java.util.function.Predicate;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableRow;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jdk.nashorn.internal.ir.Assignment;

/**
 * FXML Controller class
 *
 * @author carlo
 */
public class TeachersController implements Initializable {

    
    ObservableList<Teacher> datos;
    SortedList<Teacher> listaOrdenada;
    
    @FXML
    private TableView<Teacher> tvTeachers;
    
    @FXML
    private  TableColumn<Teacher, String> tcIdentificacion;
    
    @FXML
    private  TableColumn<Teacher, String> tcNombres;
    
    @FXML
    private  TableColumn<Teacher, String> tcApellidos;
    
    @FXML
    private  TableColumn<Teacher, String> tcTelefono;
    
    @FXML
    private TableColumn<Teacher, Teacher> tcOpciones;
    
    @FXML
    private TableColumn<Teacher, Boolean> tcActivo;
    
    @FXML
    private Pane paneFocus;
    
    @FXML
    private TextField tfBuscar;
        
    @FXML
    private Button btnNuevo;
    
    @FXML
    private Button btnAsignarMateria;
    
    @FXML
    private Button btnAsignarGrado;
    
    @FXML
    private Button btnCancelarBusqueda;
    
    private Boolean editarDui = false;
    
    private Boolean buscando = false;
    
    private Boolean creandoNuevo = false;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        try{
                llenarCamposColumnas();
                agregarBotonesATabla();
                filasEditables();
                asignarEventoBusqueda();
                detectarPerdidaFocoTabla();
        }catch(Exception ex){
            System.out.println("**********************Start Teacher");
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }     
    
       
    
    public TableView<Teacher> getTeacherTable(){
        return this.tvTeachers;
    }
    
    public ObservableList<Teacher> getTeacherList(){
        return this.datos;
    }
    
    private void detectarPerdidaFocoTabla(){
        tvTeachers.setOnMouseClicked(value -> handleOnMouseClick(value));  
        
    }
    
    
     private void asignarEventoBusqueda(){
         
         btnCancelarBusqueda.setOnAction(value -> {
             if(buscando){
                 cancelarBusqueda();
             }
         });
         
         
         tfBuscar.setOnKeyReleased(value -> {
             try {
                 
                 if(value.getCode().equals(KeyCode.ESCAPE)){
                    cancelarBusqueda();
                }
                 
                 if(tfBuscar.getText().length() > 0){
                    btnCancelarBusqueda.setText("");
                    buscando = true;
                }else if(tfBuscar.getText().length() == 0){
                    btnCancelarBusqueda.setText("");
                    buscando = false;
                }
             } catch (Exception e) {
                 System.out.println("???????"+e.getMessage());
             }
         });
         
        try{
        FilteredList<Teacher> listaFiltro = new FilteredList(datos,  e -> true);
            tfBuscar.setOnKeyPressed(e -> {
                
                if(e.getCode().equals(KeyCode.ESCAPE)){
                    cancelarBusqueda();
                }
                
                tfBuscar.textProperty().addListener((observableValue, oldValue, newValue) ->{
                    
                    listaFiltro.setPredicate((Predicate<? super Teacher>) teacher->{
                        
                        if(newValue == null || newValue.isEmpty()){
                            return true;
                        }
                        
                        String newValueLower = newValue.toLowerCase();
                        
                        if(teacher.getDui().contains(newValue)){
                            return true;
                        }else if(teacher.getNombre().toLowerCase().contains(newValueLower)){
                            return true;
                        }else if(teacher.getApellido().toLowerCase().contains(newValueLower)){
                            return true;
                        }
                        
                        return false;
                    
                    });
                
                });
                
                listaOrdenada = new SortedList<>(listaFiltro);
                listaOrdenada.comparatorProperty().bind(tvTeachers.comparatorProperty());
                tvTeachers.setItems(listaOrdenada);                
            });            
            //Fin del evento de búsqueda.
        }catch(Exception ex){
            System.out.println("Estoy en el evento búsqueda: "+ ex.getMessage() + tvTeachers.getItems().size());
        }
    }
 
    private void filasEditables(){ 
        
        tcIdentificacion.setOnEditStart(data -> {
            if(!data.getOldValue().equals("")){
                            Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
                            alerta.setTitle("Cuidado");
                            alerta.setHeaderText(null);
                            alerta.setContentText("Generalmente este es un campo que no debe editarse, ¿desea continuar?");                
                            alerta.setResizable(false);
                    
                            Optional<ButtonType> borrar= alerta.showAndWait();
                             if(borrar.get() == ButtonType.CANCEL){
                                 
                                 try {
                                       Robot r = new Robot();
                                     r.keyPress(java.awt.event.KeyEvent.VK_ESCAPE);
                                     r.keyRelease(java.awt.event.KeyEvent.VK_ESCAPE);
                                 } catch (Exception e) {
                                 }
                                  
                             }else{
                                 
                                 
                            }
                    }
        });
        
        tcIdentificacion.setOnEditCommit(new EventHandler<CellEditEvent<Teacher, String>>(){
            @Override
            public void handle(CellEditEvent<Teacher, String> event) {
                event.getRowValue().setDui(event.getNewValue()); 
                creandoNuevo = false;
                 desbloquearControles();

               
                    TablePosition focusedCellPosition = tvTeachers.getFocusModel().getFocusedCell();
                    if(focusedCellPosition.getColumn() == 0 && focusedCellPosition.getRow() == tvTeachers.getItems().size() -1){
                        
                        tvTeachers.getSelectionModel().selectLeftCell();
                        tvTeachers.getFocusModel().focus(tvTeachers.getItems().size() -1,tcNombres);
                        tvTeachers.edit(tvTeachers.getItems().size() -1,tcNombres);
                    }
                
           }
        });
        
        tcNombres.setOnEditCommit(data ->{
            data.getRowValue().setNombre(data.getNewValue());
            
            TablePosition focusedCellPosition = tvTeachers.getFocusModel().getFocusedCell();
            if(focusedCellPosition.getColumn() == 1 && focusedCellPosition.getRow() == tvTeachers.getItems().size() -1){
                tvTeachers.getSelectionModel().selectLeftCell();
                tvTeachers.getFocusModel().focus(tvTeachers.getItems().size() -1,tcApellidos);
                tvTeachers.edit(tvTeachers.getItems().size() -1,tcApellidos);
            }
        });
        
        tcApellidos.setOnEditCommit(data ->{
            data.getRowValue().setApellido(data.getNewValue());
            
            TablePosition focusedCellPosition = tvTeachers.getFocusModel().getFocusedCell();
            if(focusedCellPosition.getColumn() == 2 && focusedCellPosition.getRow() == tvTeachers.getItems().size() -1){
                tvTeachers.getSelectionModel().selectLeftCell();
                tvTeachers.getFocusModel().focus(tvTeachers.getItems().size() -1, tcTelefono);
                tvTeachers.edit(tvTeachers.getItems().size() -1, tcTelefono);
            }
        });
        
        tcTelefono.setOnEditCommit(data -> {
            data.getRowValue().setTelefono(data.getNewValue());
        });
        
        tcActivo.setOnEditCommit(value ->{
            value.getRowValue().setActivo(value.getNewValue());
        });
        
        
        tvTeachers.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {

                // switch to edit mode on keypress, but only if we aren't already in edit mode
                if(!tfBuscar.isFocused()){
                    if( tvTeachers.getEditingCell() == null) {
                        if( event.getCode().isLetterKey() || event.getCode().isDigitKey()) {  
                            TablePosition focusedCellPosition = tvTeachers.getFocusModel().getFocusedCell();
                            tvTeachers.edit(focusedCellPosition.getRow(), focusedCellPosition.getTableColumn());
                        }
                    }
                }
            }
        });
        // single cell selection mode
        //tvTeachers.getSelectionModel().setCellSelectionEnabled(true);
    }
    
    private void llenarCamposColumnas(){
        
            datos = FXCollections.observableArrayList(new Teacher("342219", "Daniel", "Reyes","22736000", true, Teacher.Genero.Masculino),
                        new Teacher("532319", "Joel", "Rodríguez","22653633", true, Teacher.Genero.Masculino),
                        new Teacher("5543", "Hugo", "Hurtado","22212452", false, Teacher.Genero.Masculino),
                        new Teacher("643323", "Quevin", "Mayorga","22245421", true, Teacher.Genero.Masculino),
                        new Teacher("014345", "Héber", "Gonzáles","22455424", false, Teacher.Genero.Masculino),
                        new Teacher("312453", "Mario", "Marroquín","14452455", false, Teacher.Genero.Masculino),
                        new Teacher("084545", "James", "Marroquín","22524455", true, Teacher.Genero.Masculino)                
                    );

            tvTeachers.setItems(datos);
            
            tcIdentificacion.setCellValueFactory(new PropertyValueFactory<>("dui"));
            tcIdentificacion.setCellFactory(TextFieldTableCell.forTableColumn());

            tcNombres.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            tcNombres.setCellFactory(TextFieldTableCell.forTableColumn());

            tcApellidos.setCellValueFactory(new PropertyValueFactory<>("apellido"));
            tcApellidos.setCellFactory(TextFieldTableCell.forTableColumn());

            tcTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
            tcTelefono.setCellFactory(TextFieldTableCell.forTableColumn());
            
            tcActivo.setCellValueFactory(cell -> cell.getValue().getActivoProperty());
            tcActivo.setCellFactory(CheckBoxTableCell.forTableColumn(tcActivo));
        }
    
    private void agregarBotonesATabla(){        
            tcOpciones.setCellValueFactory( param -> new ReadOnlyObjectWrapper<>(param.getValue()) );
            tcOpciones.setCellFactory(param -> new TableCell<Teacher, Teacher>() {
                private final Button deleteButton = new Button();
                
                @Override
                protected void updateItem(Teacher t, boolean empty) {
                    deleteButton.setFont(Font.font("FontAwesome", 16));
                    deleteButton.setText("");
                    deleteButton.getStyleClass().add("table-button");
                    deleteButton.setTooltip(new Tooltip("Eliminar"));
                    HBox panel = new HBox(deleteButton);
                    panel.setAlignment(Pos.CENTER);
                    super.updateItem(t, empty);
                    Teacher profeSeleccionado = getItem();
                    if (t == null) {
                        setGraphic(null);
                        return;
                    }

                    setGraphic(panel);
                    deleteButton.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                                 Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
                                alerta.setTitle("Eliminación de profesores");
                                alerta.setHeaderText(null);
                                Text nombre = new Text( profeSeleccionado.getNombre() + " "+ profeSeleccionado.getApellido());
                                nombre.setStyle("-fx-font-weight: bold");
                                Text pregunta = new Text("¿Desea eliminar el profersor: ");
                                Text preguntaF = new Text("?");
                                HBox hb = new HBox();
                                hb.getChildren().addAll(pregunta, nombre, preguntaF);
                                
                                alerta.getDialogPane().setContent(hb);
                                alerta.setResizable(false);
                                
                                Optional<ButtonType> borrar= alerta.showAndWait();

                                if(borrar.isPresent() && borrar.get() == ButtonType.OK ){
                                    try{
                                        
                                        if(creandoNuevo && ((Teacher) getTableRow().getItem()).getDui().equals("")){
                                            desbloquearControles();
                                            creandoNuevo = false;
                                        }
                                        datos.remove((Teacher) getTableRow().getItem());
                                        getTableView().getItems().remove((Teacher) getTableRow().getItem());
                                        paneFocus.requestFocus();
                                        
                                    }catch(Exception ex){
                                        System.out.println("Exception a borrar:  " + ex.getMessage());
                                    }
                                }
                        }
                    });
                }
          });
    }
     
    public void btnNuevoAction() {
        try {
            
            if(buscando){
                cancelarBusqueda();
            }
            Teacher t = new Teacher("","","","", true, Teacher.Genero.Masculino);
            datos.add(t);
            tvTeachers.scrollTo(t);
            tvTeachers.requestFocus();
            tvTeachers.getSelectionModel().select(datos.size()-1);
            tvTeachers.getFocusModel().focus(datos.size()-1, tcIdentificacion);
            TablePosition focuscell = tvTeachers.getFocusModel().getFocusedCell();
            tvTeachers.getSelectionModel().select(datos.size()-1, tcIdentificacion);
            creandoNuevo = true;
            bloquearControles();
        } catch (Exception e) {
            System.out.println("nuevo: "+e.getMessage()+ tvTeachers.getItems().size());
        }
        
    }
        
    private void cancelarBusqueda(){
        try {
            tfBuscar.setText("");
            btnCancelarBusqueda.setText("");
            buscando= false;
            tfBuscar.clear();
            paneFocus.requestFocus();
            tvTeachers.setItems(datos);
            
        } catch (Exception e) {
               System.out.println(e.getMessage());
        }
    }
    
    private void asignarEventoKeysTabla(){
        tvTeachers.setOnKeyPressed(event ->{
            if(event.getCode().equals(KeyCode.ENTER)){
                
            }
        });
    }
    
    private void handleOnMouseClick(MouseEvent event) {
        TableView tv = (TableView) event.getSource();

        // TODO : get the mouse clicked cell index
        int index = 0;
        
        if(creandoNuevo){
                if (event.getButton().equals(MouseButton.PRIMARY) || event.getButton().equals(MouseButton.SECONDARY))
                {
                if (event.getClickCount() ==1 || event.getClickCount() == 2)
                {
                    Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
                    alerta.setTitle("Cuidado");
                    alerta.setHeaderText(null);
                    alerta.setContentText("No puedes crear un profesor sin haber asignado una identificación, ¿Deseas escribir una identificación?");                
                    alerta.setResizable(false);

                    
                    Optional<ButtonType> borrar= alerta.showAndWait();
                        if(borrar.get() == ButtonType.OK){
                            tvTeachers.requestFocus();
                            tvTeachers.getSelectionModel().select(datos.size()-1);
                            tvTeachers.getFocusModel().focus(datos.size()-1, tcIdentificacion);
                            TablePosition focuscell = tvTeachers.getFocusModel().getFocusedCell();
                            tvTeachers.getSelectionModel().select(datos.size()-1, tcIdentificacion);
                        }else if(borrar.get() == ButtonType.CANCEL){{
                             tvTeachers.getItems().remove(datos.size()-1);
                             desbloquearControles();
                             creandoNuevo = false;
                             paneFocus.requestFocus();
                        }
                        System.out.println("Mal hecho maistro");

                        final int focusedIndex = tv.getSelectionModel().getFocusedIndex();
                        if (index == focusedIndex)
                        {
                         // TODO : Double click
                        }
                    }
                }
                
        
            }
        }
    }
    private void bloquearControles(){
        btnNuevo.setDisable(true);
        btnAsignarGrado.setDisable(true);
        btnAsignarMateria.setDisable(true);
        tfBuscar.setDisable(true);
    }
    
    private void desbloquearControles(){
             btnNuevo.setDisable(false);
            btnAsignarGrado.setDisable(false);
            btnAsignarMateria.setDisable(false);
            tfBuscar.setDisable(false);
    }
}
