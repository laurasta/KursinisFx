package com.example.kursinisfx.fxControllers;

import com.example.kursinisfx.HelloApplication;
import com.example.kursinisfx.hibernate.CommentHib;
import com.example.kursinisfx.hibernate.DestinationHib;
import com.example.kursinisfx.hibernate.ForumHib;
import com.example.kursinisfx.hibernate.UserHib;
import com.example.kursinisfx.model.User;
import com.example.kursinisfx.model.UserType;
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
import javax.persistence.Persistence;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AllUsersWindow implements Initializable {
    @FXML
    public MenuItem backButton;
    @FXML
    public ListView allUsersL;
    @FXML
    public Button deleteBtn;
    @FXML
    public Button updateBtn;
    @FXML
    public Button createBtn;
    @FXML
    public MenuItem logOutBtn;
    @FXML
    public MenuItem destinationsBtn;
    @FXML
    public MenuItem usersBtn;
    @FXML
    public MenuItem forumBtn;
    @FXML
    public MenuItem vehiclesBtn;
    private EntityManagerFactory entityManagerFactory;
    private UserHib userHib;
    private CommentHib commentHib;
    private DestinationHib destinationHib;
    private ForumHib forumHib;
    private User currentUser;
    private int currentUserId;
    private User selectedUser;

    //  SET UP ----------------------------------

    public void setData(EntityManagerFactory entityManagerFactory, int currentUserId) {
        this.entityManagerFactory = entityManagerFactory;
        this.userHib = new UserHib(entityManagerFactory);
        this.currentUserId = currentUserId;
        fillTables();
    }

    private void fillTables() {
        allUsersL.getItems().clear();
        currentUser = userHib.getUserById(currentUserId);
        List<User> usersFromDatabase = userHib.getAllUsers();
        if (currentUser.getUserType() == UserType.MANAGER){
            usersFromDatabase = usersFromDatabase.stream()
                    .filter(d -> d.getUserType() == UserType.TRUCKER)
                    .collect(Collectors.toList());
        }
        for (User u : usersFromDatabase) {
            allUsersL.getItems().add(u.getId() + ":" + u.getLogin() + " " + u.getUserType());
        }
    }

    //  CREATE -------------------------------------------

    public void createUser(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("registration-page.fxml"));
        Parent root = fxmlLoader.load();

        RegistrationPage registrationPage = fxmlLoader.getController();
        registrationPage.setData(entityManagerFactory, false);

        Scene scene = new Scene(root);
        Stage stage = (Stage) allUsersL.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    //  UPDATE -------------------------------------------

    public void updateUser(ActionEvent actionEvent) throws IOException {
        selectedUser = userHib.getUserById(Integer.parseInt(allUsersL.getSelectionModel().getSelectedItem().toString().split(":")[0]));
        currentUser = userHib.getUserById(currentUserId);
        if (selectedUser.getUserType() == UserType.TRUCKER || currentUser.getUserType() == UserType.ADMIN) {

            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("registration-page.fxml"));
            Parent root = fxmlLoader.load();

            RegistrationPage registrationPage = fxmlLoader.getController();
            registrationPage.setData(entityManagerFactory, selectedUser, currentUserId);

            Scene scene = new Scene(root);
            Stage stage = (Stage) allUsersL.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } else {
            FxUtils.generateAlert(Alert.AlertType.INFORMATION, "User update report", "You cannot modify this users information!");
        }
    }

    //  DELETE -------------------------------------------

    public void deleteUser(ActionEvent actionEvent) {
        selectedUser = userHib.getUserById(Integer.parseInt(allUsersL.getSelectionModel().getSelectedItem().toString().split(":")[0]));
        if (selectedUser.getUserType() != UserType.ADMIN)
            userHib.removeUser(selectedUser.getId());

        else FxUtils.generateAlert(Alert.AlertType.INFORMATION, "User delete report", "ADMIN user cannot be deleted!");
        fillTables();
    }

    //  MENU ITEMS ---------------------------------------

    public void allUsers() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("all-users-window.fxml"));
        Parent root = fxmlLoader.load();

        AllUsersWindow allUsersWindow = fxmlLoader.getController();
        allUsersWindow.setData(entityManagerFactory, currentUserId);

        Scene scene = new Scene(root);
        Stage stage = (Stage) allUsersL.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void allDestinations() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("all-destinations-window.fxml"));
        Parent root = fxmlLoader.load();

        AllDestinationsWindow allDestinationsWindow = fxmlLoader.getController();
        allDestinationsWindow.setData(entityManagerFactory, currentUserId);

        Scene scene = new Scene(root);
        Stage stage = (Stage) allUsersL.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void logout() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-page.fxml"));

        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) allUsersL.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void goToForum() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("forum-window.fxml"));
        Parent root = fxmlLoader.load();

        ForumWindow forumWindow = fxmlLoader.getController();
        forumWindow.setData(entityManagerFactory, currentUserId);

        Scene scene = new Scene(root);
        Stage stage = (Stage) allUsersL.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void allVehicles() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("all-vehicles.fxml"));
        Parent root = fxmlLoader.load();

        AllVehicles allVehicles = fxmlLoader.getController();
        allVehicles.setData(entityManagerFactory, currentUserId);

        Scene scene = new Scene(root);
        Stage stage = (Stage) allUsersL.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
