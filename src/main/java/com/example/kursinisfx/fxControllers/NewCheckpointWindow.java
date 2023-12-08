package com.example.kursinisfx.fxControllers;

import com.example.kursinisfx.HelloApplication;
import com.example.kursinisfx.hibernate.DestinationHib;
import com.example.kursinisfx.hibernate.UserHib;
import com.example.kursinisfx.model.Checkpoint;
import com.example.kursinisfx.model.Destination;
import com.example.kursinisfx.model.Manager;
import com.example.kursinisfx.model.User;
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

public class NewCheckpointWindow {
    @FXML
    public MenuItem cancelCheckpointCreate;
    @FXML
    public TextField titleField;
    @FXML
    public TextField addressField;
    @FXML
    public Button createCheckpointF;

    private EntityManagerFactory entityManagerFactory;
    private UserHib userHib;
    private DestinationHib destinationHib;
    private Destination selectedDestination;
    private int currentUserId;

    public void setData(EntityManagerFactory entityManagerFactory, Destination selectedDestination, int currentUserId) {
        this.entityManagerFactory = entityManagerFactory;
        this.userHib = new UserHib(entityManagerFactory);
        this.destinationHib = new DestinationHib(entityManagerFactory);
        this.selectedDestination = selectedDestination;
        this.currentUserId = currentUserId;
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
    public void CreateCheckpoint() throws IOException {
        if (titleField.getText().isEmpty() || addressField.getText().isEmpty()) {
            FxUtils.generateAlert(Alert.AlertType.WARNING, "Checkpoint report", "Please fill all fields");
        } else {
            Checkpoint checkpoint = new Checkpoint(titleField.getText(), addressField.getText(), selectedDestination);
            destinationHib.createCheckpoint(checkpoint);

            FxUtils.generateAlert(Alert.AlertType.INFORMATION, "Checkpoint report", "Checkpoint created successfully!");
            returnToPrevious();
        }
    }
}
