package com.example.kursinisfx.fxControllers;

import com.example.kursinisfx.HelloApplication;
import com.example.kursinisfx.hibernate.CommentHib;
import com.example.kursinisfx.hibernate.ForumHib;
import com.example.kursinisfx.hibernate.UserHib;
import com.example.kursinisfx.model.Comment;
import com.example.kursinisfx.model.Forum;
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

public class NewCommentWindow {
    @FXML
    public MenuItem cancelCommentCreate;
    @FXML
    public TextField contentField;
    @FXML
    public Button createcommentF;

    private EntityManagerFactory entityManagerFactory;
    private UserHib userHib;
    private int currentUserId;
    private ForumHib forumHib;
    private CommentHib commentHib;
    private User selectedUser;
    private Forum selectedForum;
    private Comment parentComment;

    public void setData(EntityManagerFactory entityManagerFactory, int currentUserId, Forum selectedForum, Comment parentComment) {
        this.entityManagerFactory = entityManagerFactory;
        this.userHib = new UserHib(entityManagerFactory);
        this.forumHib = new ForumHib(entityManagerFactory);
        this.commentHib = new CommentHib(entityManagerFactory);
        this.currentUserId = currentUserId;
        this.selectedForum = selectedForum;
        this.parentComment = parentComment;
    }

    public void returnToPrevious() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("forum-window.fxml"));
        Parent root = fxmlLoader.load();

        ForumWindow forumWindow = fxmlLoader.getController();
        forumWindow.setData(entityManagerFactory, currentUserId);

        Scene scene = new Scene(root);
        Stage stage = (Stage) contentField.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void createComment() throws IOException {
        selectedUser = userHib.getUserById(currentUserId);
        if (contentField.getText().isEmpty()) {
            FxUtils.generateAlert(Alert.AlertType.WARNING, "Comment report", "Fill comment text field");
        } else {
            Comment comment = new Comment(selectedUser.getName(), contentField.getText(), parentComment, selectedForum);
            commentHib.createComment(comment);

            FxUtils.generateAlert(Alert.AlertType.INFORMATION, "Comment report", "Comment added successfully!");
            returnToPrevious();
        }
    }

}
