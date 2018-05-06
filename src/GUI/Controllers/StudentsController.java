/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.Controllers;

import Entities.Student;
import Entities.Teacher;
import java.awt.Robot;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author carlos
 */
public class StudentsController implements Initializable {

    ObservableList<Student> datos;
    
    SortedList<Student> listaOrdenada;
    
    @FXML
    private TableView<Student> tvStudents;
    
    @FXML
    private  TableColumn<Student, String> tcCodigo;
    
    @FXML
    private  TableColumn<Student, String> tcNie;
    
    @FXML
    private  TableColumn<Student, String> tcNombres;
    
    @FXML
    private  TableColumn<Student, String> tcApellido1;
    
    @FXML
    private TableColumn<Student, String> tcApellido2;
    
    @FXML
    private TableColumn<Student, Student.Genero> tcGenero; 
    
    @FXML
    private TableColumn<Student, Student> tcOpciones;
    
    @FXML
    private TableColumn tcMain;
    
    @FXML
    private TextField tfBuscar;
    
    @FXML
    private Button btNuevo;
    
    @FXML
    private Button btnCancelarBusqueda;
    
   @FXML
   private Pane paneFocus;
    
    private Boolean editarCodigo = false;
    
    private Boolean editarNie = false;
    
    private Boolean buscando = false;
    
    private Boolean creandoNuevo = false;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        llenarCamposColumnas();
        agregarBotonesATabla();
        filasEditables();
        asignarEventoBusqueda();
        detectarPerdidaFocoTabla();
        //tvStudents.columnResizePolicyProperty().
        
        
    }    
    
      private void detectarPerdidaFocoTabla(){
        tvStudents.setOnMouseClicked(value -> handleOnMouseClick(value));  
        
    }
    
    private void llenarCamposColumnas(){
        
        try{
        
        datos = FXCollections.observableArrayList(
                new Student("01", "50", "Nelson", "Palacios", "Villalta", Student.Genero.Masculino, true),
                new Student("02", "78", "Juan", "Andrino", "Valencia", Student.Genero.Masculino, false),
                new Student("03", "83", "Jaime", "Talx", "Torres", Student.Genero.Masculino, true),
                new Student("04", "65", "Camila", "Uribe", "Ramírez", Student.Genero.Femenino, true),
                new Student("05", "43", "Ana", "Ramírez", "Díaz", Student.Genero.Femenino, true)
        );
        
        tvStudents.setItems(datos);
        
        tcCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        tcCodigo.setCellFactory(TextFieldTableCell.forTableColumn());
        
        tcNie.setCellValueFactory(new PropertyValueFactory<>("nie"));
        tcNie.setCellFactory(TextFieldTableCell.forTableColumn());
        
        tcNombres.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        tcNombres.setCellFactory(TextFieldTableCell.forTableColumn());
        
        tcApellido1.setCellValueFactory(new PropertyValueFactory<>("apellido1"));
        tcApellido1.setCellFactory(TextFieldTableCell.forTableColumn());
        
        tcApellido2.setCellValueFactory(new PropertyValueFactory<>("apellido2"));
        tcApellido2.setCellFactory(TextFieldTableCell.forTableColumn());
        
        tcGenero.setCellValueFactory(new PropertyValueFactory<>("genero"));
        tcGenero.setCellFactory(ChoiceBoxTableCell.forTableColumn(Student.Genero.Femenino, Student.Genero.Masculino));
        
         tvStudents.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {

                // switch to edit mode on keypress, but only if we aren't already in edit mode
                try {
                    
                    if(!tfBuscar.isFocused()){
                        if( tvStudents.getEditingCell() == null) {
                            if( event.getCode().isLetterKey() || event.getCode().isDigitKey()) {  

                                TablePosition focusedCellPosition = tvStudents.getFocusModel().getFocusedCell();
                                System.out.println(tcNie);
                                System.out.println(focusedCellPosition.getRow() +" *********  "+focusedCellPosition.getTableColumn());
                                tvStudents.edit(focusedCellPosition.getRow(), focusedCellPosition.getTableColumn());
                            }
                         }
                    }
                    
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        });
        
        //tvStudents.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        }catch(Exception e){
            System.out.println("Llenar columnas "+e.getMessage());
        }
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
         
         try {
            FilteredList<Student> listaFiltro = new FilteredList(datos,  e -> true);
            tfBuscar.setOnKeyPressed(e -> {
                
                    if(e.getCode().equals(KeyCode.ESCAPE)){
                        cancelarBusqueda();
                    }
                
                    tfBuscar.textProperty().addListener((observableValue, oldValue, newValue) ->{
                        
                        listaFiltro.setPredicate((Predicate<? super Student>) student->{
                        
                        if(newValue == null || newValue.isEmpty()){
                            return true;
                        }
                        String newValueLower = newValue.toLowerCase();
                        
                        if(student.getCodigo().contains(newValue)){
                            return true;
                        }else if(student.getNie().toLowerCase().contains(newValueLower)){
                            return true;
                        }else if(student.getNombre().toLowerCase().contains(newValueLower)){
                            return true;
                        }else if(student.getApellido1().toLowerCase().contains(newValueLower)){
                            return true;
                        }else if(student.getApellido2().toLowerCase().contains(newValueLower)){
                            return true;
                        }
                        return false;
                    });
                });
                listaOrdenada = new SortedList<>(listaFiltro);
                listaOrdenada.comparatorProperty().bind(tvStudents.comparatorProperty());
                tvStudents.setItems(listaOrdenada);
                });
            
            }catch(Exception ex){
                    System.out.println("Estoy en el evento búsqueda: "+ ex.getMessage());
             }
    }

    
    
    private void agregarBotonesATabla(){
            
        tcOpciones.setCellValueFactory( param -> new ReadOnlyObjectWrapper<>(param.getValue()) );
         tcOpciones.setCellFactory((TableColumn<Student, Student> param) -> new TableCell<Student, Student>() {
                private final Button deleteButton = new Button();                
                
                @Override
                protected void updateItem(Student st, boolean empty) {
                    deleteButton.setFont(Font.font("FontAwesome", 16));
                    deleteButton.setText("");
                    deleteButton.getStyleClass().add("table-button");
                    deleteButton.setTooltip(new Tooltip("Eliminar"));
                    HBox panel = new HBox(deleteButton);
                    panel.setAlignment(Pos.CENTER);
                    super.updateItem(st, empty);
                    Student studentSeleccionado = getItem();
                    if (st == null) {
                        setGraphic(null);
                        return;
                    }

                    setGraphic(panel);
                    deleteButton.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                                 Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
                                alerta.setTitle("Eliminación de estudiantes");
                                alerta.setHeaderText(null);
                                Text nombre = new Text( studentSeleccionado.getNombre() + " "+ studentSeleccionado.getApellido1());
                                nombre.setStyle("-fx-font-weight: bold");
                                Text pregunta = new Text("¿Desea eliminar el estudiante: ");
                                Text preguntaF = new Text("?");
                                HBox hb = new HBox();
                                hb.getChildren().addAll(pregunta, nombre, preguntaF);
                                
                                alerta.getDialogPane().setContent(hb);
                                alerta.setResizable(false);
                                
                                Optional<ButtonType> borrar= alerta.showAndWait();

                                if(borrar.isPresent() && borrar.get() == ButtonType.OK ){
                                    try{
                                        
                                        if(creandoNuevo && ((Student) getTableRow().getItem()).getCodigo().equals("")){
                                            desbloquearControles();
                                            creandoNuevo = false;
                                        }
                                        datos.remove((Student) getTableRow().getItem());
                                        getTableView().getItems().remove((Student) getTableRow().getItem());
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
    
    private void filasEditables(){
        
        /*tcNie.setOnEditStart(data -> {
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
        });*/
        
         tcNie.setOnEditCommit(data ->{
             if(!data.getRowValue().toString().equals("")){
                 data.getRowValue().setNie(data.getNewValue());
                 
                 creandoNuevo = false;
               
                    TablePosition focusedCellPosition = tvStudents.getFocusModel().getFocusedCell();
                    System.out.println("Antes de ver donde estaba");
                    if(focusedCellPosition.getColumn() == 1 && focusedCellPosition.getRow() == tvStudents.getItems().size() -1){
                        System.out.println("Tengo seleccionado el correcto");
                        tvStudents.getSelectionModel().selectLeftCell();
                        tvStudents.getFocusModel().focus(tvStudents.getItems().size() -1,tcNombres);
                        tvStudents.edit(tvStudents.getItems().size() -1,tcNombres);
                    }
                    desbloquearControles();
             }else{
                 data.getRowValue().setNie(data.getOldValue());
             }
        });
        
        
        tcNombres.setOnEditCommit(data ->{
            if(!data.getRowValue().toString().equals("")){
                 data.getRowValue().setNombre(data.getNewValue());
                 
                    TablePosition focusedCellPosition = tvStudents.getFocusModel().getFocusedCell();
                    if(focusedCellPosition.getColumn() == 2 && focusedCellPosition.getRow() == tvStudents.getItems().size() -1){
                        
                        tvStudents.getSelectionModel().selectLeftCell();
                        tvStudents.getFocusModel().focus(tvStudents.getItems().size() -1,tcApellido1);
                        tvStudents.edit(tvStudents.getItems().size() -1,tcApellido1);
                    }
             }else{
                 data.getRowValue().setNombre(data.getOldValue());
             }
        });
        
        tcApellido1.setOnEditCommit(data ->{
            if(!data.getRowValue().toString().equals("")){
                 data.getRowValue().setApellido1(data.getNewValue());
                 
                    TablePosition focusedCellPosition = tvStudents.getFocusModel().getFocusedCell();
                    if(focusedCellPosition.getColumn() == 3 && focusedCellPosition.getRow() == tvStudents.getItems().size() -1){
                        
                        tvStudents.getSelectionModel().selectLeftCell();
                        tvStudents.getFocusModel().focus(tvStudents.getItems().size() -1,tcApellido2);
                        tvStudents.edit(tvStudents.getItems().size() -1,tcApellido2);
                    }
             }else{
                 data.getRowValue().setApellido1(data.getOldValue());
             }
        });
        
        tcApellido2.setOnEditCommit(data ->{
            if(!data.getRowValue().toString().equals("")){
                 data.getRowValue().setApellido2(data.getNewValue());
                                 
             }else{
                 data.getRowValue().setApellido2(data.getOldValue());
             }
        });
        
        tcGenero.setOnEditCommit(data -> {
            data.getRowValue().setGenero(data.getNewValue());
        });
    }
    
    public void btnNuevoAction(){
            
        try {
             if(buscando){
                cancelarBusqueda();
            }
             buscando = false;
            Student nuevo = new Student("", "", "", "", "", Student.Genero.Masculino, true);
            System.out.println(datos.size());
            datos.add(nuevo);
            tvStudents.scrollTo(nuevo);
            tvStudents.requestFocus();
            System.out.println(datos.size());
            tvStudents.getSelectionModel().select(datos.size()-1);
            tvStudents.getFocusModel().focus(datos.size()-1, tcNie);
            TablePosition focuscell = tvStudents.getFocusModel().getFocusedCell();
            tvStudents.getSelectionModel().select(datos.size()-1, tcNie);
            System.out.println("_/////// "+focuscell);
            creandoNuevo = true;
            bloquearControles();
            
        }catch (Exception e) {
            System.out.println("btnNuevoClic: "+e.getMessage());
        }catch(Throwable ee){
            System.out.println("btnNuevoClic: "+ee.getMessage());
        }
    }
    
     private void cancelarBusqueda(){
        try {
            tfBuscar.setText("");
            btnCancelarBusqueda.setText("");
            buscando= false;
            tfBuscar.clear();
            paneFocus.requestFocus();
            tvStudents.setItems(datos);
            
        } catch (Exception e) {
               System.out.println(e.getMessage());
        }
     }
     
      private void bloquearControles(){
        btNuevo.setDisable(true);
        //btnAsignarGrado.setDisable(true);
        //btnAsignarMateria.setDisable(true);
        tfBuscar.setDisable(true);
    }
    
    private void desbloquearControles(){
             btNuevo.setDisable(false);
            //btnAsignarGrado.setDisable(false);
            //btnAsignarMateria.setDisable(false);
            tfBuscar.setDisable(false);
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
                    alerta.setContentText("No puedes crear un alumno sin haber un nombre o apellido, ¿Deseas escribir uno?");                
                    alerta.setResizable(false);

                    
                    Optional<ButtonType> borrar= alerta.showAndWait();
                        if(borrar.get() == ButtonType.OK){
                            tvStudents.requestFocus();
                            tvStudents.getSelectionModel().select(datos.size()-1);
                            tvStudents.getFocusModel().focus(datos.size()-1, tcNie);
                            TablePosition focuscell = tvStudents.getFocusModel().getFocusedCell();
                            tvStudents.getSelectionModel().select(datos.size()-1, tcNie);
                        }else if(borrar.get() == ButtonType.CANCEL){{
                             tvStudents.getItems().remove(datos.size()-1);
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
    
}

