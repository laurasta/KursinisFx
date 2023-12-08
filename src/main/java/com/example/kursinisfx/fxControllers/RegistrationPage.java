package com.example.kursinisfx.fxControllers;

import com.example.kursinisfx.HelloApplication;
import com.example.kursinisfx.hibernate.UserHib;
import com.example.kursinisfx.hibernate.VehicleHib;
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
import java.util.List;
import java.util.ResourceBundle;

public class RegistrationPage implements Initializable {
    @FXML
    public TextField loginField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public TextField nameField;
    @FXML
    public TextField surnameField;
    @FXML
    public TextField emailField;
    @FXML
    public TextField phoneNumberField;
    @FXML
    public CheckBox HealthCertifField;
    @FXML
    public Button createBtn;
    @FXML
    public TextField driverLicenseField;
    @FXML
    public RadioButton driverRadio;
    @FXML
    public RadioButton managerRadio;
    @FXML
    public TextField workEmailField;
    @FXML
    public CheckBox isAdminField;
    @FXML
    public MenuItem cancelRegistration;
    @FXML
    public Label driverLicenseT;
    @FXML
    public Label workEmailT;
    @FXML
    public Button addVehicleBtn;
    @FXML
    public ComboBox vehicleCombobox;
    private EntityManagerFactory entityManagerFactory;
    private UserHib userHib;
    private VehicleHib vehicleHib;
    private User selectedUser;
    private int currentUserId;
    private Boolean registration = false;
    final ToggleGroup group = new ToggleGroup();

    //  SET UP ------------------------------------

    public void setData(EntityManagerFactory entityManagerFactory, User selectedUser, int currentUserId) {
        this.entityManagerFactory = entityManagerFactory;
        this.selectedUser = selectedUser;
        this.userHib = new UserHib(entityManagerFactory);
        this.vehicleHib = new VehicleHib(entityManagerFactory);
        this.currentUserId = currentUserId;

        List<Vehicle> vehicles = vehicleHib.getAllVehicles();
        vehicles.forEach(v -> vehicleCombobox.getItems().add(v.getId() +": " + v.getModel() + " " + v.getLicenseNumber()));
        fillFields();
    }

    public void setData(EntityManagerFactory entityManagerFactory, Boolean registration) {
        this.entityManagerFactory = entityManagerFactory;
        this.userHib = new UserHib(entityManagerFactory);
        this.registration = registration;
        vehicleCombobox.setVisible(false);
        addVehicleBtn.setVisible(false);
    }

    private void fillFields() {
        loginField.setText(selectedUser.getLogin());
        passwordField.setText(selectedUser.getPassword());
        nameField.setText(selectedUser.getName());
        surnameField.setText(selectedUser.getSurname());
        emailField.setText(selectedUser.getEmail());
        phoneNumberField.setText(selectedUser.getPhoneNumber());
        Trucker selectedTrucker = userHib.getTruckerById(selectedUser.getId());
        Manager selectedManager = userHib.getManagerById(selectedUser.getId());
        vehicleCombobox.setVisible(true);
        addVehicleBtn.setVisible(true);
        if(selectedUser.getUserType() == UserType.TRUCKER){
            driverLicenseField.setText(selectedTrucker.getDriverLicense());
            HealthCertifField.setSelected(selectedTrucker.getHealthCertificate());
            workEmailField.setVisible(false);
            isAdminField.setVisible(false);
            workEmailT.setVisible(false);
            managerRadio.setVisible(false);
            driverRadio.setSelected(true);
            disableFields();
        } else if(selectedUser.getUserType() != UserType.TRUCKER){
            workEmailField.setText(selectedManager.getWorkEmail());
            isAdminField.setSelected(selectedManager.getIsAdmin());
            driverLicenseField.setVisible(false);
            driverLicenseT.setVisible(false);
            HealthCertifField.setVisible(false);
            driverRadio.setVisible(false);
            managerRadio.setSelected(true);
            disableFields();
        }
        try {
            vehicleCombobox.setValue(selectedTrucker.getVehicle());
            vehicleCombobox.getSelectionModel().select(selectedTrucker.getVehicle().getId() + ": " + selectedTrucker.getVehicle().getModel() + " " + selectedTrucker.getVehicle().getLicenseNumber());

        } catch (Exception e) {

        }
        createBtn.setOnAction(actionEvent ->  {
            try {
                updateUser();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        createBtn.setText("Update");
    }

    public void disableFields() {
        driverRadio.setToggleGroup(group);
        managerRadio.setToggleGroup(group);

        if (driverRadio.isSelected()) {
            workEmailField.setDisable(true);
            isAdminField.setDisable(true);
            HealthCertifField.setDisable(false);
            driverLicenseField.setDisable(false);
        } else if (managerRadio.isSelected()) {
            HealthCertifField.setDisable(true);
            driverLicenseField.setDisable(true);
            workEmailField.setDisable(false);
            isAdminField.setDisable(false);
        } else {
            workEmailField.setDisable(true);
            isAdminField.setDisable(true);
            HealthCertifField.setDisable(true);
            driverLicenseField.setDisable(true);
        }
    }

    //  CREATE -------------------------------------

    public void createNewUser() throws IOException {
        if (driverRadio.isSelected()) {
            Trucker trucker = new Trucker(loginField.getText(), passwordField.getText(), nameField.getText(), surnameField.getText(), emailField.getText(), phoneNumberField.getText(), HealthCertifField.isSelected(), driverLicenseField.getText(), null);
            userHib.createUser(trucker);
        } else {
            Manager manager = new Manager(loginField.getText(), passwordField.getText(), nameField.getText(), surnameField.getText(), emailField.getText(), phoneNumberField.getText(),  workEmailField.getText(), isAdminField.isSelected());
            if (manager.getIsAdmin()){manager.setUserType(UserType.ADMIN);}
            userHib.createUser(manager);
        }
        FxUtils.generateAlert(Alert.AlertType.INFORMATION, "User registration report", "User created successfully!");
        returnToPrevious();
    }

    public void AddVehicle() {
        Trucker selectedTrucker = userHib.getTruckerById(selectedUser.getId());
        selectedTrucker.setVehicle(vehicleHib.getVehicleById(Integer.parseInt(vehicleCombobox.getValue().toString().split(":")[0])));
        userHib.editUser(selectedTrucker);

        FxUtils.generateAlert(Alert.AlertType.INFORMATION, "User update report", "Vehicle added successfully!");

    }

    //  UPDATE ---------------------------------------------

    public void updateUser() throws IOException {
        Trucker selectedTrucker = userHib.getTruckerById(selectedUser.getId());
        Manager selectedManager = userHib.getManagerById(selectedUser.getId());
        if(selectedUser.getUserType() == UserType.TRUCKER){
            selectedTrucker.setLogin(loginField.getText());
            selectedTrucker.setPassword(passwordField.getText());
            selectedTrucker.setName(nameField.getText());
            selectedTrucker.setSurname(surnameField.getText());
            selectedTrucker.setEmail(emailField.getText());
            selectedTrucker.setPhoneNumber(phoneNumberField.getText());
            selectedTrucker.setDriverLicense(driverLicenseField.getText());
            selectedTrucker.setHealthCertificate(HealthCertifField.isSelected());
            userHib.editUser(selectedTrucker);
        } else if(selectedUser.getUserType() != UserType.TRUCKER){
            selectedManager.setLogin(loginField.getText());
            selectedManager.setPassword(passwordField.getText());
            selectedManager.setName(nameField.getText());
            selectedManager.setSurname(surnameField.getText());
            selectedManager.setEmail(emailField.getText());
            selectedManager.setPhoneNumber(phoneNumberField.getText());
            selectedManager.setWorkEmail(workEmailField.getText());
            selectedManager.setIsAdmin(isAdminField.isSelected());
            if (isAdminField.isSelected()){selectedManager.setUserType(UserType.ADMIN);
            } else {selectedManager.setUserType(UserType.MANAGER);}
            userHib.editUser(selectedManager);
        }
        FxUtils.generateAlert(Alert.AlertType.INFORMATION, "User update report", "User updated successfully!");
        allUsers();
    }

    //  MENU ITEMS -------------------------

    public void returnToPrevious() throws IOException
    {
        if(registration) {
            goToLogin();
        } else {
            allUsers();
        }
    }
    private void goToLogin() throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-page.fxml"));

        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) loginField.getScene().getWindow();
        stage.setTitle("Trucker system");
        stage.setScene(scene);
        stage.show();
    }

    public void allUsers() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("all-users-window.fxml"));
        Parent root = fxmlLoader.load();

        AllUsersWindow allUsersWindow = fxmlLoader.getController();
        allUsersWindow.setData(entityManagerFactory, currentUserId);

        Scene scene = new Scene(root);
        Stage stage = (Stage) loginField.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        disableFields();

    }

}
