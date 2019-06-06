/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Models.Appointment;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Dane Schlea
 */
public class AppointmentsController implements Initializable {
    
    //UI items
    @FXML
    private TableView<Appointment> appView;
    @FXML
    private TableColumn<Appointment, String> appStartView;
    @FXML
    private TableColumn<Appointment, String> appEndView;
    @FXML
    private TableColumn<Appointment, String> appTitleView;
    @FXML
    private TableColumn<Appointment, String> appDescView;
    @FXML
    private TableColumn<Appointment, String> appLocView;
    @FXML
    private TableColumn<Appointment, String> appCustView;
    @FXML
    private TableColumn<Appointment, String> appConsView;
    @FXML
    private RadioButton appMonthly;
    @FXML
    private RadioButton appWeekly;
    @FXML
    private Button appClose;
    @FXML
    private Button appAdd;
    @FXML
    private Button appUpdate;
    @FXML
    private Button appDelete;
    @FXML
    private Label labelAppointments;
    
    @FXML
    private void handleAppointmentsCancel(ActionEvent event) throws IOException{
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirm");
        alert.setHeaderText("Confirm cancellation");
        alert.setContentText("Are you sure you want to cancel?");
        Optional<ButtonType> result = alert.showAndWait();
        
        if(result.get() == ButtonType.OK){
            Parent appointmentsCancel = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
            Scene scene = new Scene(appointmentsCancel);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        }
        else{
            System.out.println("Operation cancelled.");
        }
    }
    
    @FXML
    private void addAppointmentHandler(ActionEvent event) throws IOException {
        Parent addAppointmentParent = FXMLLoader.load(getClass().getResource("AddAppointment.fxml"));
        Scene addAppointmentScene = new Scene(addAppointmentParent);
        Stage addAppointmentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        addAppointmentStage.setScene(addAppointmentScene);
        addAppointmentStage.show();
    }
    
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
