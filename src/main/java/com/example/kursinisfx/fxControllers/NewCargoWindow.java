package com.example.kursinisfx.fxControllers;

import com.example.kursinisfx.HelloApplication;
import com.example.kursinisfx.hibernate.DestinationHib;
import com.example.kursinisfx.hibernate.UserHib;
import com.example.kursinisfx.model.*;
import com.example.kursinisfx.utils.FxUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javax.persistence.EntityManagerFactory;
import java.io.IOException;

public class NewCargoWindow {
    @FXML
    public MenuItem cancelCargoCreate;
    @FXML
    public TextField typeField;
    @FXML
    public TextField contentField;
    @FXML
    public Button createVehicleF;
    @FXML
    public TextField weightField;
    @FXML
    public ComboBox<CargoType> cargoTypeComboBox = new ComboBox<>();
    private EntityManagerFactory entityManagerFactory;
    private UserHib userHib;
    private DestinationHib destinationHib;
    private Destination selectedDestination;
    private Checkpoint selectedCheckpoint;
    private int currentUserId;

    public void setData(EntityManagerFactory entityManagerFactory, Checkpoint selectedCheckpoint, Destination selectedDestination, int currentUserId) {
        this.entityManagerFactory = entityManagerFactory;
        this.userHib = new UserHib(entityManagerFactory);
        this.destinationHib = new DestinationHib(entityManagerFactory);
        this.selectedDestination = selectedDestination;
        this.selectedCheckpoint = selectedCheckpoint;
        this.currentUserId = currentUserId;
        cargoTypeComboBox.getItems().addAll(CargoType.values());
    }
    public void returnToPrevious() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("all-destinations-window.fxml"));
        Parent root = fxmlLoader.load();

        AllDestinationsWindow allDestinationsWindow = fxmlLoader.getController();
        allDestinationsWindow.setData(entityManagerFactory, currentUserId);

        Scene scene = new Scene(root);
        Stage stage = (Stage) contentField.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    public void CreateCargo() throws IOException {
        if (contentField.getText().isEmpty() || weightField.getText().isEmpty() || cargoTypeComboBox.getValue() == null){
            FxUtils.generateAlert(Alert.AlertType.WARNING, "Cargo report", "Please fill all fields");
        } else {
            Cargo cargo = new Cargo(contentField.getText(), weightField.getText(), selectedCheckpoint, selectedDestination, cargoTypeComboBox.getValue().toString());
            destinationHib.createCargo(cargo);


            FxUtils.generateAlert(Alert.AlertType.INFORMATION, "Cargo report", "Cargo created successfully!");
            returnToPrevious();
        }
    }
}
