package com.example.kursinisfx.fxControllers;

import com.example.kursinisfx.HelloApplication;
import com.example.kursinisfx.hibernate.DestinationHib;
import com.example.kursinisfx.hibernate.UserHib;
import com.example.kursinisfx.hibernate.VehicleHib;
import com.example.kursinisfx.model.Destination;
import com.example.kursinisfx.model.Manager;
import com.example.kursinisfx.model.User;
import com.example.kursinisfx.model.Vehicle;
import com.example.kursinisfx.utils.FxUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.persistence.EntityManagerFactory;
import java.io.IOException;

public class NewVehicleWindow {
    @FXML
    public MenuItem cancelVehicleCreate;
    @FXML
    public TextField modelField;
    @FXML
    public TextField licenseNumberField;
    @FXML
    public Button createVehicleF;
    private EntityManagerFactory entityManagerFactory;
    private UserHib userHib;
    private int currentUserId;
    private DestinationHib destinationHib;
    private VehicleHib vehicleHib;
    private User selectedUser;

    public void setData(EntityManagerFactory entityManagerFactory, int currentUserId) {
        this.entityManagerFactory = entityManagerFactory;
        this.userHib = new UserHib(entityManagerFactory);
        this.destinationHib = new DestinationHib(entityManagerFactory);
        this.vehicleHib = new VehicleHib(entityManagerFactory);
        this.currentUserId = currentUserId;
    }
    public void returnToPrevious() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("all-vehicles.fxml"));
        Parent root = fxmlLoader.load();

        AllVehicles allVehicles = fxmlLoader.getController();
        allVehicles.setData(entityManagerFactory, currentUserId);

        Scene scene = new Scene(root);
        Stage stage = (Stage) modelField.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void CreateVehicle() throws IOException {
        if (modelField.getText().isEmpty() || licenseNumberField.getText().isEmpty()){
            FxUtils.generateAlert(Alert.AlertType.WARNING, "Vehicle report", "Please fill all fields");
        } else {
            Vehicle vehicle = new Vehicle(modelField.getText(), licenseNumberField.getText());
            vehicleHib.createVehicle(vehicle);

            FxUtils.generateAlert(Alert.AlertType.INFORMATION, "Vehicle report", "Vehicle added successfully!");
            returnToPrevious();
        }
    }
}
