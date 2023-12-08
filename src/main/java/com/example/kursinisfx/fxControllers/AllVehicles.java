package com.example.kursinisfx.fxControllers;

import com.example.kursinisfx.HelloApplication;
import com.example.kursinisfx.hibernate.*;
import com.example.kursinisfx.model.Destination;
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
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import javax.persistence.EntityManagerFactory;
import java.io.IOException;
import java.util.List;

public class AllVehicles {
    @FXML
    public MenuItem logOutBtn;
    @FXML
    public MenuItem destinationsBtn;
    @FXML
    public MenuItem usersBtn;
    @FXML
    public MenuItem vehiclesBtn;
    @FXML
    public MenuItem forumBtn;
    @FXML
    public ListView allVehiclesL;
    @FXML
    public Button deleteBtn;
    @FXML
    public Button updateBtn;
    @FXML
    public Button createBtn;

    private EntityManagerFactory entityManagerFactory;
    private UserHib userHib;
    private CommentHib commentHib;
    private DestinationHib destinationHib;
    private VehicleHib vehicleHib;
    private ForumHib forumHib;

    private Vehicle selectedVehicle;
    private int currentUserId;

    //  SET UP -------------------------------------

    public void setData(EntityManagerFactory entityManagerFactory, int currentUserId) {
        this.entityManagerFactory = entityManagerFactory;
        this.userHib = new UserHib(entityManagerFactory);
        this.destinationHib = new DestinationHib(entityManagerFactory);
        this.vehicleHib = new VehicleHib(entityManagerFactory);
        this.currentUserId = currentUserId;
        fillTables();
    }

    private void fillTables(){
        allVehiclesL.getItems().clear();
        List<Vehicle> myVehicleList = destinationHib.getAllVehicle();
        for(Vehicle v : myVehicleList){
                allVehiclesL.getItems().add(v.getId() + ": " + v.getModel() + ", " + v.getLicenseNumber());
        }
    }

    //  CREATE ---------------------------------------

    public void createVehicle() throws IOException {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("new-vehicle-window.fxml"));
            Parent root = fxmlLoader.load();

            NewVehicleWindow newVehicleWindow = fxmlLoader.getController();
            newVehicleWindow.setData(entityManagerFactory, currentUserId);

            Scene scene = new Scene(root);
            Stage stage = (Stage) allVehiclesL.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
    }

    //  DELETE --------------------------------------

    public void deleteVehicle() {
        selectedVehicle = vehicleHib.getVehicleById(Integer.parseInt(allVehiclesL.getSelectionModel().getSelectedItem().toString().split(":")[0]));
        vehicleHib.removeVehicle(selectedVehicle.getId());
        FxUtils.generateAlert(Alert.AlertType.INFORMATION, "Vehicle delete report", "Vehicle deleted successfully");
        fillTables();

    }

    //  MENU ITEMS -----------------------------------

    public void allVehicles() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("all-vehicles.fxml"));
        Parent root = fxmlLoader.load();

        AllVehicles allVehicles = fxmlLoader.getController();
        allVehicles.setData(entityManagerFactory, currentUserId);

        Scene scene = new Scene(root);
        Stage stage = (Stage) allVehiclesL.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void allUsers() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("all-users-window.fxml"));
        Parent root = fxmlLoader.load();

        AllUsersWindow allUsersWindow = fxmlLoader.getController();
        allUsersWindow.setData(entityManagerFactory, currentUserId);

        Scene scene = new Scene(root);
        Stage stage = (Stage) allVehiclesL.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void allDestinations() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("all-destinations-window.fxml"));
        Parent root = fxmlLoader.load();

        AllDestinationsWindow allDestinationsWindow = fxmlLoader.getController();
        allDestinationsWindow.setData(entityManagerFactory, currentUserId);

        Scene scene = new Scene(root);
        Stage stage = (Stage) allVehiclesL.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void logout() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-page.fxml"));

        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) allVehiclesL.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void goToForum() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("forum-window.fxml"));
        Parent root = fxmlLoader.load();

        ForumWindow forumWindow = fxmlLoader.getController();
        forumWindow.setData(entityManagerFactory, currentUserId);

        Scene scene = new Scene(root);
        Stage stage = (Stage) allVehiclesL.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
