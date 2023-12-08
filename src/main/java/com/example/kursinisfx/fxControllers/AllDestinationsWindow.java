package com.example.kursinisfx.fxControllers;

import com.example.kursinisfx.HelloApplication;
import com.example.kursinisfx.hibernate.CommentHib;
import com.example.kursinisfx.hibernate.DestinationHib;
import com.example.kursinisfx.hibernate.ForumHib;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.persistence.EntityManagerFactory;
import javax.security.auth.callback.Callback;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AllDestinationsWindow implements Initializable {
    @FXML
    public MenuItem backButton;
    @FXML
    public ListView allDestinationsL;
    @FXML
    public Button deleteDBtn;
    @FXML
    public Button updateDBtn;
    @FXML
    public Button createDBtn;
    @FXML
    public MenuItem logOutBtn;
    @FXML
    public MenuItem destinationsBtn;
    @FXML
    public MenuItem usersBtn;
    @FXML
    public MenuItem forumBtn;
    @FXML
    public ListView checkpointsL;
    @FXML
    public ListView cargosL;
    @FXML
    public Button addCpBtn;
    @FXML
    public Button deleteCpBtn;
    @FXML
    public Button addCargoBtn;
    @FXML
    public Button deleteCargoBtn;
    @FXML
    public MenuItem vehiclesBtn;
    @FXML
    public Button reachedBtn;
    @FXML
    public Menu usersMenuBtn;
    @FXML
    public Menu vehicleMenuBtn;

    private EntityManagerFactory entityManagerFactory;
    private UserHib userHib;
    private CommentHib commentHib;
    private DestinationHib destinationHib;
    private ForumHib forumHib;

    private User selectedUser;
    private int currentUserId;
    private Destination selectedDestination;
    private Checkpoint selectedCheckpoint;
    private Cargo selectedCargo;

    // SET UP -----------------------------

    public void setData(EntityManagerFactory entityManagerFactory, int currentUserId) {
        this.entityManagerFactory = entityManagerFactory;
        this.userHib = new UserHib(entityManagerFactory);
        this.destinationHib = new DestinationHib(entityManagerFactory);
        this.currentUserId = currentUserId;
        fillTables();
        disableFields();
    }

    private void fillTables(){
        allDestinationsL.getItems().clear();
        checkpointsL.getItems().clear();
        cargosL.getItems().clear();
        User currentUser = userHib.getUserById(currentUserId);
        List<Destination> myDestList = destinationHib.getAllDestinations();
        if (currentUser.getUserType() == UserType.TRUCKER){
            myDestList = myDestList.stream()
                    .filter(d -> d.getDriver() == null || d.getDriver().getId() == currentUserId)
                    .collect(Collectors.toList());
        } else if (currentUser.getUserType() == UserType.MANAGER){
            myDestList = myDestList.stream()
                    .filter(d -> d.getResponsibleManager().getId() == currentUserId)
                    .collect(Collectors.toList());
        }
        for (Destination d : myDestList) {
            allDestinationsL.getItems().add(d.getId() + ":" + d.getDestStatus() + "-" + d.getTitle() + ";  responsible - " + d.getResponsibleManager().getName());
        }

    }
    private void disableFields(){
        User currentUser = userHib.getUserById(currentUserId);
        if (currentUser.getUserType() == UserType.TRUCKER){
            deleteDBtn.setVisible(false);
            createDBtn.setVisible(false);
            addCargoBtn.setVisible(false);
            deleteCargoBtn.setVisible(false);
            addCpBtn.setVisible(false);
            deleteCpBtn.setVisible(false);
            vehicleMenuBtn.setVisible(false);
            usersMenuBtn.setVisible(false);
        }
    }

    // LOAD DATA ------------------------------

    public void loadCheckpoints(MouseEvent mouseEvent) {
        checkpointsL.getItems().clear();
        cargosL.getItems().clear();
        selectedDestination = destinationHib.getDestinationById(Integer.parseInt(allDestinationsL.getSelectionModel().getSelectedItem().toString().split(":")[0]));
        List<Checkpoint> destinationCheckpoints = destinationHib.getCheckpointsByDestination(selectedDestination);
        for(Checkpoint c : destinationCheckpoints){
            if (c.getIsReached()){
                checkpointsL.getItems().add(c.getId() + ":" + c.getTitle() + " ; " + c.getCheckpointAddress() + " REACHED ✓");
            } else {
                checkpointsL.getItems().add(c.getId() + ":" + c.getTitle() + " ; " + c.getCheckpointAddress());
            }
        }
    }

    public void loadCargos(MouseEvent mouseEvent) {
        cargosL.getItems().clear();
        selectedCheckpoint = destinationHib.getCheckpointById(Integer.parseInt(checkpointsL.getSelectionModel().getSelectedItem().toString().split(":")[0]));
        List<Cargo> checkpointCargos = destinationHib.getCargosByCheckpoint(selectedCheckpoint);
        for(Cargo c : checkpointCargos){
            cargosL.getItems().add(c.getId() + ":" + c.getWeight() + " ; " + c.getContent() + "  " +c.getCargoType().toUpperCase());
        }
    }


    // CREATE ----------------------------------

    public void createDestination() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("new-destination-window.fxml"));
        Parent root = fxmlLoader.load();

        NewDestination newDestination = fxmlLoader.getController();
        newDestination.setData(entityManagerFactory, currentUserId);

        Scene scene = new Scene(root);
        Stage stage = (Stage) allDestinationsL.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }



    public void addCheckpoint() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("new-checkpoint-window.fxml"));
        Parent root = fxmlLoader.load();

        selectedDestination = destinationHib.getDestinationById(Integer.parseInt(allDestinationsL.getSelectionModel().getSelectedItem().toString().split(":")[0]));
        NewCheckpointWindow newCheckpointWindow = fxmlLoader.getController();
        newCheckpointWindow.setData(entityManagerFactory, selectedDestination, currentUserId);

        Scene scene = new Scene(root);
        Stage stage = (Stage) allDestinationsL.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }


    public void addCargo() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("new-cargo-window.fxml"));
        Parent root = fxmlLoader.load();

        selectedCheckpoint = destinationHib.getCheckpointById(Integer.parseInt(checkpointsL.getSelectionModel().getSelectedItem().toString().split(":")[0]));
        NewCargoWindow newCargoWindow = fxmlLoader.getController();
        newCargoWindow.setData(entityManagerFactory, selectedCheckpoint, selectedCheckpoint.getCheckpointDestination(), currentUserId);

        Scene scene = new Scene(root);
        Stage stage = (Stage) allDestinationsL.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }


    // UPDATE ----------------------------------

    public void updateDestination() throws IOException {
        selectedDestination = destinationHib.getDestinationById(Integer.parseInt(allDestinationsL.getSelectionModel().getSelectedItem().toString().split(":")[0]));
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("new-destination-window.fxml"));
        Parent root = fxmlLoader.load();

        NewDestination newDestination = fxmlLoader.getController();
        newDestination.setData(entityManagerFactory, currentUserId, selectedDestination);

        Scene scene = new Scene(root);
        Stage stage = (Stage) allDestinationsL.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }


    // DELETE ----------------------------------

    public void deleteCargo() {
        selectedCargo = destinationHib.getCargoById(Integer.parseInt(cargosL.getSelectionModel().getSelectedItem().toString().split(":")[0]));
        destinationHib.removeCargo(selectedCargo.getId());
        FxUtils.generateAlert(Alert.AlertType.INFORMATION, "Cargo delete report", "Cargo deleted successfully");
        fillTables();
    }

    public void deleteCheckpoint() {
        selectedCheckpoint = destinationHib.getCheckpointById(Integer.parseInt(checkpointsL.getSelectionModel().getSelectedItem().toString().split(":")[0]));
        List<Cargo> cargos = selectedCheckpoint.getCheckpointCargo();
        for (Cargo c : cargos) {
            destinationHib.removeCargo(c.getId());
        }


        destinationHib.removeCheckpoint(selectedCheckpoint.getId());
        FxUtils.generateAlert(Alert.AlertType.INFORMATION, "Checkpoint delete report", "Checkpoint with all its cargos deleted successfully");
        fillTables();
    }

    public void deleteDestination() {
        selectedDestination = destinationHib.getDestinationById(Integer.parseInt(allDestinationsL.getSelectionModel().getSelectedItem().toString().split(":")[0]));

        if (selectedDestination.getDestStatus() != DestStatus.IN_PROGRESS)
        {
            List<Checkpoint> checkpoints = selectedDestination.getCheckpoints();
            for (Checkpoint ch : checkpoints){
                List<Cargo> cargos = ch.getCheckpointCargo();
                for (Cargo c : cargos) {
                    destinationHib.removeCargo(c.getId());
                }
                destinationHib.removeCheckpoint(ch.getId());
            }
            destinationHib.removeDestination(selectedDestination.getId());

            FxUtils.generateAlert(Alert.AlertType.INFORMATION, "Destination delete report", "Destination with all its checkpoints and cargos deleted successfully");
            fillTables();
        } else {
            FxUtils.generateAlert(Alert.AlertType.WARNING, "Destination delete report", "While destination is in progress, it cannot be deleted!");
        }

    }


    // SET CHECKPOINT TO REACHED ---------------
    public void checkpointReached() {
        selectedCheckpoint = destinationHib.getCheckpointById(Integer.parseInt(checkpointsL.getSelectionModel().getSelectedItem().toString().split(":")[0]));
        selectedCheckpoint.setIsReached(true);
        destinationHib.updateCheckpoint(selectedCheckpoint);
        fillTables();
    }

    // MENU ITEMS ------------------------------

    public void allVehicles() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("all-vehicles.fxml"));
        Parent root = fxmlLoader.load();

        AllVehicles allVehicles = fxmlLoader.getController();
        allVehicles.setData(entityManagerFactory, currentUserId);

        Scene scene = new Scene(root);
        Stage stage = (Stage) allDestinationsL.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void allUsers() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("all-users-window.fxml"));
        Parent root = fxmlLoader.load();

        AllUsersWindow allUsersWindow = fxmlLoader.getController();
        allUsersWindow.setData(entityManagerFactory, currentUserId);

        Scene scene = new Scene(root);
        Stage stage = (Stage) allDestinationsL.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void allDestinations() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("all-destinations-window.fxml"));
        Parent root = fxmlLoader.load();

        AllDestinationsWindow allDestinationsWindow = fxmlLoader.getController();
        allDestinationsWindow.setData(entityManagerFactory, currentUserId);

        Scene scene = new Scene(root);
        Stage stage = (Stage) allDestinationsL.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void logout() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-page.fxml"));

        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) allDestinationsL.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void goToForum() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("forum-window.fxml"));
        Parent root = fxmlLoader.load();

        ForumWindow forumWindow = fxmlLoader.getController();
        forumWindow.setData(entityManagerFactory, currentUserId);

        Scene scene = new Scene(root);
        Stage stage = (Stage) allDestinationsL.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
