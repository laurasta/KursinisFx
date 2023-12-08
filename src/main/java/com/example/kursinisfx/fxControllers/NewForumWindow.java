package com.example.kursinisfx.fxControllers;

import com.example.kursinisfx.HelloApplication;
import com.example.kursinisfx.hibernate.DestinationHib;
import com.example.kursinisfx.hibernate.ForumHib;
import com.example.kursinisfx.hibernate.UserHib;
import com.example.kursinisfx.model.Forum;
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
import java.time.LocalDateTime;

public class NewForumWindow {
    @FXML
    public MenuItem cancelForumCreate;
    @FXML
    public TextField titleField;
    @FXML
    public TextField descriptionField;
    @FXML
    public Button createForumF;
    private EntityManagerFactory entityManagerFactory;
    private UserHib userHib;
    private int currentUserId;
    private ForumHib forumHib;
    private User currentUser;


    public void setData(EntityManagerFactory entityManagerFactory, int currentUserId) {
        this.entityManagerFactory = entityManagerFactory;
        this.userHib = new UserHib(entityManagerFactory);
        this.forumHib = new ForumHib(entityManagerFactory);
        this.currentUserId = currentUserId;

    }
    public void returnToPrevious() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("forum-window.fxml"));
        Parent root = fxmlLoader.load();

        ForumWindow forumWindow = fxmlLoader.getController();
        forumWindow.setData(entityManagerFactory, currentUserId);

        Scene scene = new Scene(root);
        Stage stage = (Stage) titleField.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    public void CreateForum() throws IOException {
        currentUser = userHib.getUserById(currentUserId);
        if (titleField.getText().isEmpty() || descriptionField.getText().isEmpty()){
            FxUtils.generateAlert(Alert.AlertType.WARNING, "Forum report", "Please fill all fields");
        } else {
            Forum forum = new Forum(titleField.getText(),descriptionField.getText(), LocalDateTime.now(), currentUser.getName());
            forumHib.createForum(forum);

            FxUtils.generateAlert(Alert.AlertType.INFORMATION, "Forum report", "Forum created successfully!");
            returnToPrevious();
        }
    }
}
