package com.example.kursinisfx.fxControllers;

import com.example.kursinisfx.HelloApplication;
import com.example.kursinisfx.hibernate.CommentHib;
import com.example.kursinisfx.hibernate.DestinationHib;
import com.example.kursinisfx.hibernate.ForumHib;
import com.example.kursinisfx.hibernate.UserHib;
import com.example.kursinisfx.model.Destination;
import com.example.kursinisfx.model.User;
import com.example.kursinisfx.model.UserType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.util.List;

public class MainWindow {
    @FXML
    public MenuItem logOutBtn;
    @FXML
    public MenuItem destinationsBtn;
    @FXML
    public MenuItem usersBtn;
    @FXML
    public ListView destinationsList;
    @FXML
    public Label nameSurname;
    @FXML
    public MenuItem forumBtn;
    @FXML
    public MenuItem vehiclesBtn;
    @FXML
    public Menu usersMenuBtn;
    @FXML
    public Menu vehicleMenuBtn;
    private EntityManagerFactory entityManagerFactory;
    private UserHib userHib;
    private CommentHib commentHib;
    private DestinationHib destinationHib;
    private ForumHib forumHib;
    private int currentUserId;
    private User currentUser;

    public void setData(EntityManagerFactory entityManagerFactory, int currentUserId) {
        this.entityManagerFactory = entityManagerFactory;
        this.userHib = new UserHib(entityManagerFactory);
        this.destinationHib = new DestinationHib(entityManagerFactory);
        this.forumHib = new ForumHib(entityManagerFactory);
        this.commentHib = new CommentHib(entityManagerFactory);
        this.currentUser = userHib.getUserById(currentUserId);
        this.currentUserId = currentUserId;
        fillTables();
        disableFields();
    }

    private void fillTables() {
        nameSurname.setText(currentUser.getName() + " " + currentUser.getSurname());
    }

    private void disableFields(){
        User currentUser = userHib.getUserById(currentUserId);
        if (currentUser.getUserType() == UserType.TRUCKER){
            vehicleMenuBtn.setVisible(false);
            usersMenuBtn.setVisible(false);
        }
    }

    public void logout() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-page.fxml"));

        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) nameSurname.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void allDestinations() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("all-destinations-window.fxml"));
        Parent root = fxmlLoader.load();

        AllDestinationsWindow allDestinationsWindow = fxmlLoader.getController();
        allDestinationsWindow.setData(entityManagerFactory, currentUserId);

        Scene scene = new Scene(root);
        Stage stage = (Stage) nameSurname.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void allUsers() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("all-users-window.fxml"));
        Parent root = fxmlLoader.load();

        AllUsersWindow allUsersWindow = fxmlLoader.getController();
        allUsersWindow.setData(entityManagerFactory, currentUserId);

        Scene scene = new Scene(root);
        Stage stage = (Stage) nameSurname.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void destinationDetails(ActionEvent actionEvent) {
    }

    public void destinationUpdate(ActionEvent actionEvent) {
    }

    public void goToForum() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("forum-window.fxml"));
        Parent root = fxmlLoader.load();

        ForumWindow forumWindow = fxmlLoader.getController();
        forumWindow.setData(entityManagerFactory, currentUserId);

        Scene scene = new Scene(root);
        Stage stage = (Stage) nameSurname.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void allVehicles() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("all-vehicles.fxml"));
        Parent root = fxmlLoader.load();

        AllVehicles allVehicles = fxmlLoader.getController();
        allVehicles.setData(entityManagerFactory, currentUserId);

        Scene scene = new Scene(root);
        Stage stage = (Stage) nameSurname.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
