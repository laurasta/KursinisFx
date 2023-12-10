package com.example.kursinisfx.fxControllers;

import com.example.kursinisfx.HelloApplication;
import com.example.kursinisfx.hibernate.DestinationHib;
import com.example.kursinisfx.hibernate.UserHib;
import com.example.kursinisfx.model.*;
import com.example.kursinisfx.utils.FxUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javax.persistence.EntityManagerFactory;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class NewDestination implements Initializable {
    @FXML
    private MenuItem cancelDestCreate;

    @FXML
    private TextField titleField;

    @FXML
    private TextField startAddressField;

    @FXML
    private TextField endAddressField;

    @FXML
    private Button createDestinationF;

    @FXML
    private ComboBox<DestStatus> DestStatusComboBox = new ComboBox<>();

    @FXML
    private Label destStatusLabel;

    @FXML
    private Label departureDateLabel;

    @FXML
    private Label arrivalDateLabel;

    @FXML
    private ComboBox<String> driverComboBox;

    @FXML
    private ComboBox<String> managerComboBox;

    @FXML
    private Label driverLabel;

    @FXML
    private Label respManagerLabel;

    @FXML
    private TextField departureDateF;

    @FXML
    private TextField arrivalDateF;

    @FXML
    private Label windowName;

    @FXML
    private Button assignBtn;

    private EntityManagerFactory entityManagerFactory;
    private UserHib userHib;
    private int currentUserId;
    private DestinationHib destinationHib;
    private Destination selectedDestination;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DestStatusComboBox.getItems().addAll(DestStatus.values());
        disableFields();
    }

    public void setData(EntityManagerFactory entityManagerFactory, int currentUserId) {
        this.entityManagerFactory = entityManagerFactory;
        this.userHib = new UserHib(entityManagerFactory);
        this.destinationHib = new DestinationHib(entityManagerFactory);
        this.currentUserId = currentUserId;
        windowName.setText("New destination");
        disableFields();
    }

    public void setData(EntityManagerFactory entityManagerFactory, int currentUserId, Destination selectedDestination) {
        this.entityManagerFactory = entityManagerFactory;
        this.userHib = new UserHib(entityManagerFactory);
        this.destinationHib = new DestinationHib(entityManagerFactory);
        this.currentUserId = currentUserId;
        this.selectedDestination = selectedDestination;
        DestStatusComboBox.setValue(selectedDestination.getDestStatus());

        List<Trucker> truckers = userHib.getAllTruckers();
        truckers.forEach(t -> driverComboBox.getItems().add(t.getId() + ": " + t.getName() + " " + t.getSurname()));

        List<Manager> managers = userHib.getAllManagers();
        managers.forEach(m -> managerComboBox.getItems().add(m.getId() + ": " + m.getName() + " " + m.getSurname()));

        createDestinationF.setOnAction(this::updateDestination);
        createDestinationF.setText("Update");

        fillFields();
        windowName.setText("Update destination");
        disableUpdateFields();
    }

    private void disableUpdateFields() {
        User currentUser = userHib.getUserById(currentUserId);
        if (currentUser.getUserType() == UserType.TRUCKER) {
            DestStatusComboBox.setDisable(false);
            departureDateF.setDisable(true);
            arrivalDateF.setDisable(true);
            driverComboBox.setDisable(true);
            managerComboBox.setDisable(true);
            titleField.setDisable(true);
            startAddressField.setDisable(true);
            endAddressField.setDisable(true);
            assignBtn.setVisible(true);
        } else if (currentUser.getUserType() == UserType.MANAGER) {
            departureDateF.setDisable(true);
            arrivalDateF.setDisable(true);
            managerComboBox.setDisable(true);
        }
    }

    private void disableFields() {
        DestStatusComboBox.setDisable(true);
        departureDateF.setDisable(true);
        arrivalDateF.setDisable(true);
        driverComboBox.setDisable(true);
        managerComboBox.setDisable(true);
    }

    private void fillFields() {
        titleField.setText(selectedDestination.getTitle());
        startAddressField.setText(selectedDestination.getRouteStartAddress());
        endAddressField.setText(selectedDestination.getDestinationAddress());
        DestStatusComboBox.setValue(selectedDestination.getDestStatus());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        try {
            String departureDateString = selectedDestination.getDepartureDate().format(formatter);
            departureDateF.setText(departureDateString);
        } catch (Exception e) {
            departureDateF.setText("none");
        }
        try {
            String arrivalDateString = selectedDestination.getArrivalDate().format(formatter);
            arrivalDateF.setText(arrivalDateString);
        } catch (Exception e) {
            arrivalDateF.setText("none");
        }
        try {
            driverComboBox.getSelectionModel().select(selectedDestination.getDriver().getId() + ": " + selectedDestination.getDriver().getName() + " " + selectedDestination.getDriver().getSurname());
        } catch (Exception e) {
            //code
        }
        managerComboBox.getSelectionModel().select(selectedDestination.getResponsibleManager().getId() + ": " + selectedDestination.getResponsibleManager().getName() + " " + selectedDestination.getResponsibleManager().getSurname());
    }

    public void createDestination() throws IOException {
        if (titleField.getText().isEmpty() || startAddressField.getText().isEmpty() || endAddressField.getText().isEmpty()) {
            FxUtils.generateAlert(Alert.AlertType.WARNING, "Destination report", "Please fill all fields");
        } else {
            Manager manager = userHib.getManagerById(currentUserId);
            Destination destination = new Destination(titleField.getText(), startAddressField.getText(), endAddressField.getText(), manager);
            destinationHib.createDestination(destination);

            FxUtils.generateAlert(Alert.AlertType.INFORMATION, "Destination report", "Destination created successfully!");
            returnToPrevious();
        }
    }

    private void updateDestination(ActionEvent actionEvent) {
        try {
            selectedDestination.setTitle(titleField.getText());
            selectedDestination.setRouteStartAddress(startAddressField.getText());
            selectedDestination.setDestinationAddress(endAddressField.getText());

            if (selectedDestination.getDestStatus() == DestStatus.NEW && DestStatusComboBox.getValue() != DestStatus.NEW) {
                selectedDestination.setDepartureDate(LocalDateTime.now());
            }
            if ((selectedDestination.getDestStatus() == DestStatus.NEW || selectedDestination.getDestStatus() == DestStatus.IN_PROGRESS)
                    && (DestStatusComboBox.getValue() == DestStatus.CANCELED || DestStatusComboBox.getValue() == DestStatus.COMPLETED)) {
                selectedDestination.setArrivalDate(LocalDateTime.now());
            }
            selectedDestination.setDestStatus(DestStatusComboBox.getValue());
            String driverComboBoxValue = driverComboBox.getValue();
            if (driverComboBoxValue != null) {
                selectedDestination.setDriver(userHib.getTruckerById(Integer.parseInt(driverComboBoxValue.split(":")[0])));
            }
            selectedDestination.setResponsibleManager(userHib.getManagerById(Integer.parseInt(managerComboBox.getValue().split(":")[0])));

            destinationHib.updateDestination(selectedDestination);
            FxUtils.generateAlert(Alert.AlertType.INFORMATION, "Destination update report", "Destination updated successfully!");
            returnToPrevious();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void returnToPrevious() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("all-destinations-window.fxml"));
        Parent root = fxmlLoader.load();

        AllDestinationsWindow allDestinationsWindow = fxmlLoader.getController();
        allDestinationsWindow.setData(entityManagerFactory, currentUserId);

        Scene scene = new Scene(root);
        Stage stage = (Stage) titleField.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void AssignTruckerToDestination() {
        Trucker currentTrucker = userHib.getTruckerById(currentUserId);
        selectedDestination.setDriver(currentTrucker);
        driverComboBox.getSelectionModel().select(selectedDestination.getDriver().getId() + ": " + selectedDestination.getDriver().getName() + " " + selectedDestination.getDriver().getSurname());
    }
}
