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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javax.persistence.EntityManagerFactory;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class ForumWindow {
    @FXML
    public MenuItem logOutBtn;
    @FXML
    public MenuItem destinationsBtn;
    @FXML
    public MenuItem usersBtn;
    @FXML
    public ListView forumList;
    @FXML
    public TreeView commentTree;
    @FXML
    public MenuItem ReplyItem;
    @FXML
    public MenuItem deleteItem;
    @FXML
    public MenuItem forumBtn;
    @FXML
    public Button createForumBtn;
    @FXML
    public Button deleteForumBtn;
    @FXML
    public Button createCommentBtn;
    @FXML
    public MenuItem vehiclesBtn;
    @FXML
    public TextArea descriptionField;
    @FXML
    public Label usernameLabel;
    @FXML
    public Label dateLabel;
    @FXML
    public Menu usersMenuBtn;
    @FXML
    public Menu vehicleMenuBtn;

    private EntityManagerFactory entityManagerFactory;
    private UserHib userHib;
    private CommentHib commentHib;
    private DestinationHib destinationHib;
    private int currentUserId;
    private ForumHib forumHib;
    private Comment selectedComment;
    private Forum selectedForum;

    //  SET UP ---------------------------------------

    public void setData(EntityManagerFactory entityManagerFactory, int currentUserId) {
        this.entityManagerFactory = entityManagerFactory;
        this.userHib = new UserHib(entityManagerFactory);
        this.forumHib = new ForumHib(entityManagerFactory);
        this.commentHib = new CommentHib(entityManagerFactory);
        this.currentUserId = currentUserId;
        fillTables();
        disableFields();
    }
    private void fillTables(){
        forumList.getItems().clear();
        commentTree.setRoot(new TreeItem<>());
        commentTree.getRoot().getChildren().clear();
        List<Forum> forumsL = forumHib.getAllForums();
        for (Forum c : forumsL){
            forumList.getItems().add(c.getId() + ": " + c.getTitle());
        }

    }

    private void disableFields(){
        User currentUser = userHib.getUserById(currentUserId);
        if (currentUser.getUserType() == UserType.TRUCKER){
            deleteForumBtn.setVisible(false);
            vehicleMenuBtn.setVisible(false);
            usersMenuBtn.setVisible(false);
            deleteItem.setVisible(false);
        }
    }

    // LOAD DATA -------------------------------------

    public void loadComments() {
        Forum selectedForum = forumHib.getForumById(Integer.parseInt(forumList.getSelectionModel().getSelectedItem().toString().split(":")[0]));
        descriptionField.setText(selectedForum.getDescription());
        usernameLabel.setText(selectedForum.getAuthor());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        try {
            String postDate = selectedForum.getPostDateTime().format(formatter);
            dateLabel.setText(postDate);
        } catch (Exception e) {

        }

        commentTree.setRoot(new TreeItem<>(new Comment()));
        commentTree.setShowRoot(false);
        commentTree.getRoot().setExpanded(true);
        List<Comment> topLevelComments = selectedForum.getComments().stream().filter(c -> c.getParentComment() == null).collect(Collectors.toList());
        topLevelComments.forEach(comment -> addTreeItem(comment, commentTree.getRoot()));
    }

    private void addTreeItem(Comment comment, TreeItem parent) {
        TreeItem<Comment> treeItem = new TreeItem<>(comment);
        parent.getChildren().add(treeItem);
        comment.getReplies().forEach(r -> addTreeItem(r, treeItem));
    }

    //  CREATE -----------------------------------------

    public void createComment() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("new-comment-window.fxml"));
        Parent root = fxmlLoader.load();
        selectedForum = forumHib.getForumById(Integer.parseInt(forumList.getSelectionModel().getSelectedItem().toString().split(":")[0]));

        NewCommentWindow newCommentWindow = fxmlLoader.getController();
        newCommentWindow.setData(entityManagerFactory, currentUserId, selectedForum, null);

        Scene scene = new Scene(root);
        Stage stage = (Stage) forumList.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void replyComment() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("new-comment-window.fxml"));
        Parent root = fxmlLoader.load();
        selectedForum = forumHib.getForumById(Integer.parseInt(forumList.getSelectionModel().getSelectedItem().toString().split(":")[0]));

        String treeItemString = commentTree.getSelectionModel().getSelectedItem().toString();
        String modifiedString = treeItemString.substring(18);
        selectedComment = commentHib.getCommentById(Integer.parseInt(modifiedString.split(":")[0]));

        NewCommentWindow newCommentWindow = fxmlLoader.getController();
        newCommentWindow.setData(entityManagerFactory, currentUserId, selectedForum, selectedComment);

        Scene scene = new Scene(root);
        Stage stage = (Stage) forumList.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void createForum() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("new-forum-window.fxml"));
        Parent root = fxmlLoader.load();

        NewForumWindow newForumWindow = fxmlLoader.getController();
        newForumWindow.setData(entityManagerFactory, currentUserId);

        Scene scene = new Scene(root);
        Stage stage = (Stage) forumList.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    //  DELETE -----------------------------------------

    public void deleteForum(ActionEvent actionEvent) {
        selectedForum = forumHib.getForumById(Integer.parseInt(forumList.getSelectionModel().getSelectedItem().toString().split(":")[0]));
        List<Comment> forumComments = selectedForum.getComments();
        for (Comment c : forumComments) {
            commentHib.removeComment(c.getId());
        }
        forumHib.removeForum(selectedForum.getId());
        FxUtils.generateAlert(Alert.AlertType.INFORMATION, "Forum delete report", "Forum with all its comments deleted successfully");
        fillTables();
    }

    public void deleteComment(ActionEvent actionEvent) {
        String treeItemString = commentTree.getSelectionModel().getSelectedItem().toString();
        String modifiedString = treeItemString.substring(18);
        selectedComment = commentHib.getCommentById(Integer.parseInt(modifiedString.split(":")[0]));
        List<Comment> comments = selectedComment.getReplies();
        for (Comment c : comments) {
            commentHib.removeComment(c.getId());
        }
        commentHib.removeComment(selectedComment.getId());

        FxUtils.generateAlert(Alert.AlertType.INFORMATION, "Comment delete report", "Comment with all its replays deleted successfully");
        fillTables();
    }

    //  MENU ITEMS -----------------------------------

    public void allVehicles() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("all-vehicles.fxml"));
        Parent root = fxmlLoader.load();

        AllVehicles allVehicles = fxmlLoader.getController();
        allVehicles.setData(entityManagerFactory, currentUserId);

        Scene scene = new Scene(root);
        Stage stage = (Stage) forumList.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void allUsers() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("all-users-window.fxml"));
        Parent root = fxmlLoader.load();

        AllUsersWindow allUsersWindow = fxmlLoader.getController();
        allUsersWindow.setData(entityManagerFactory, currentUserId);

        Scene scene = new Scene(root);
        Stage stage = (Stage) forumList.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void allDestinations() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("all-destinations-window.fxml"));
        Parent root = fxmlLoader.load();

        AllDestinationsWindow allDestinationsWindow = fxmlLoader.getController();
        allDestinationsWindow.setData(entityManagerFactory, currentUserId);

        Scene scene = new Scene(root);
        Stage stage = (Stage) forumList.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void logout() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-page.fxml"));

        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) forumList.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void goToForum() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("forum-window.fxml"));
        Parent root = fxmlLoader.load();

        ForumWindow forumWindow = fxmlLoader.getController();
        forumWindow.setData(entityManagerFactory, currentUserId);

        Scene scene = new Scene(root);
        Stage stage = (Stage) forumList.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
