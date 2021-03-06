/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Models.Address;
import Models.City;
import Models.Country;
import Models.Customer;
import static Util.mainDB.dbConnect;
import static Util.mainDB.getConn;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Dane Schlea
 */
public class AddCustomerController implements Initializable {
    
    //UI items
    @FXML
    private Label labelManCust;
    @FXML
    private Label mCustName;
    @FXML
    private Label mCustAddress;
    @FXML
    private Label mCustCity;
    @FXML
    private Label mCustAddressCont;
    @FXML
    private Label mCustPostal;
    @FXML
    private Label mCustCountry;
    @FXML
    private Label mCustPhone;
    @FXML
    private TextField mCustNameField;
    @FXML
    private TextField mCustAddressField;
    @FXML
    private TextField mCustAddressContField;
    @FXML
    private ChoiceBox<String> mCustCityField;
    @FXML
    private TextField mCustPostalField;
    @FXML
    private ChoiceBox<String> mCustCountryField;
    @FXML
    private TextField mCustPhoneField;
    @FXML
    private Button mCustSave;
    @FXML
    private Button mCustCancel;
    
    @FXML
    private void handleManageCustomerCancel(ActionEvent event) throws IOException{
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirm");
        alert.setHeaderText("Confirm cancellation");
        alert.setContentText("Are you sure you want to cancel?");
        Optional<ButtonType> result = alert.showAndWait();
        
        if(result.get() == ButtonType.OK){
            Parent manageCustomerCancel = FXMLLoader.load(getClass().getResource("Customers.fxml"));
            Scene scene = new Scene(manageCustomerCancel);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        }
        else{
            System.out.println("Operation cancelled.");
        }
    }
    
    //save customer to the database
    @FXML
    private void handleManageCustomerSave(ActionEvent event) throws IOException{
        String name = mCustNameField.getText();
        String addr = mCustAddressField.getText();
        String addr2 = mCustAddressContField.getText();
        String cityName = mCustCityField.getValue();
        String postal = mCustPostalField.getText();
        String country = mCustCountryField.getValue();
        String phone = mCustPhoneField.getText();       
        if(Customer.customerValidate(name) == false || Address.addressValidate(addr, postal, phone) == false || City.cityValidate(cityName) == false || Country.countryValidate(country) == false){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.NONE);
            alert.setTitle("Error");
            alert.setHeaderText("Error submitting customer");
            alert.setContentText("One or more invalid fields.");
            alert.showAndWait();
        }
        else{
            try{
                //get cityId
                dbConnect();
                PreparedStatement prepCity = getConn().prepareStatement("SELECT cityId FROM city WHERE city = ?");
                prepCity.setString(1, cityName);
                ResultSet resCity = prepCity.executeQuery();
                while(resCity.next()){
                    int rCity = (int) resCity.getObject("cityId");
                
                //insert data into address first to get new address key
                PreparedStatement prepS = getConn().prepareStatement("INSERT INTO address (address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy)"
                                + "VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?, CURRENT_TIMESTAMP, ?)");
                prepS.setString(1, addr);
                prepS.setString(2, addr2);
                prepS.setInt(3, rCity);
                prepS.setString(4, postal);
                prepS.setString(5, phone);
                prepS.setString(6, LoginController.currentUser);
                prepS.setString(7, LoginController.currentUser);
                prepS.executeUpdate();
                
                //Get addressId
                PreparedStatement prepAdd = getConn().prepareStatement("SELECT addressId FROM address WHERE address = ?");
                prepAdd.setString(1, addr);
                ResultSet resAdd = prepAdd.executeQuery();
                while(resAdd.next()){
                    int rAdd = (int) resAdd.getObject("addressId");

                //insert data into customer
                PreparedStatement pState = getConn().prepareStatement("INSERT INTO customer (customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy) "
                        + "VALUES (?, ?, ?, CURRENT_TIMESTAMP, ?, CURRENT_TIMESTAMP, ?)");
                pState.setString(1, name);
                pState.setInt(2, rAdd);
                pState.setInt(3, 1);
                pState.setString(4, LoginController.currentUser);
                pState.setString(5, LoginController.currentUser);
                pState.executeUpdate();
                
                //Go back to customers window
                Parent customersCancel = FXMLLoader.load(getClass().getResource("Customers.fxml"));
                Scene scene = new Scene(customersCancel);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
                    }
                }
            } catch (SQLException e) {
                System.out.println("SQL error");
            }
        }
    }

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //load country and city
        loadCity();
        loadCountry();
    }    
    
    //load city choicebox
    private void loadCity(){
        //city choicebox choices
        mCustCityField.getItems().add("New York");
        mCustCityField.getItems().add("Los Angeles");
        mCustCityField.getItems().add("Houston");
        mCustCityField.getItems().add("Salt Lake City");
        mCustCityField.getItems().add("Lancaster");
        mCustCityField.getItems().add("London");
        mCustCityField.getItems().add("Glasgow");
        mCustCityField.getItems().add("Toronto");
        mCustCityField.getItems().add("Vancouver");
        mCustCityField.getItems().add("Ottawa");
        mCustCityField.getItems().add("Oslo");
        mCustCityField.getItems().add("Bergen");
        mCustCityField.getItems().add("Trondheim");
        mCustCityField.setValue("New York");
    }
    
    //load country choicebox
    private void loadCountry(){
        mCustCountryField.getItems().add("US");
        mCustCountryField.getItems().add("UK");
        mCustCountryField.getItems().add("Canada");
        mCustCountryField.getItems().add("Norway");
        mCustCountryField.setValue("US");
    }
    
}
