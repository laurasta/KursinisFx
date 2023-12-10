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
    public CheckBox healthCertifField;
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
    public ComboBox<String> vehicleCombobox;
    private EntityManagerFactory entityManagerFactory;
    private UserHib userHib;
    private VehicleHib vehicleHib;
    private User selectedUser;
    private int currentUserId;
    private Boolean registration = false;
    final ToggleGroup group = new ToggleGroup();

    private static final int VEHICLE_ID_INDEX = 0;

    public void setData(EntityManagerFactory entityManagerFactory, User selectedUser, int currentUserId) {
        initializeFields(entityManagerFactory, selectedUser, currentUserId);
        fillVehicleCombobox();
        fillFields();
    }

    public void setData(EntityManagerFactory entityManagerFactory, Boolean registration) {
        initializeFields(entityManagerFactory, null, 0);
        this.registration = registration;
        vehicleCombobox.setVisible(false);
        addVehicleBtn.setVisible(false);
    }

    private void initializeFields(EntityManagerFactory entityManagerFactory, User selectedUser, int currentUserId) {
        this.entityManagerFactory = entityManagerFactory;
        this.selectedUser = selectedUser;
        this.userHib = new UserHib(entityManagerFactory);
        this.vehicleHib = new VehicleHib(entityManagerFactory);
        this.currentUserId = currentUserId;
    }

    private void fillVehicleCombobox() {
        List<Vehicle> vehicles = vehicleHib.getAllVehicles();
        vehicles.forEach(v -> vehicleCombobox.getItems().add(formatVehicleInfo(v)));
    }

    private String formatVehicleInfo(Vehicle vehicle) {
        return vehicle.getId() + ": " + vehicle.getModel() + " " + vehicle.getLicenseNumber();
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

        if (selectedUser.getUserType() == UserType.TRUCKER) {
            fillTruckerFields(selectedTrucker);
        } else if (selectedUser.getUserType() != UserType.TRUCKER) {
            fillManagerFields(selectedManager);
        }

        try {
            String vehicleInfo = formatVehicleInfo(selectedTrucker.getVehicle());
            vehicleCombobox.setValue(vehicleInfo);
            vehicleCombobox.getSelectionModel().select(vehicleInfo);
        } catch (Exception e) {
            // Handle the exception (log or provide feedback)
        }

        createBtn.setOnAction(this::updateUser);
        createBtn.setText("Update");
    }

    private void fillTruckerFields(Trucker selectedTrucker) {
        driverLicenseField.setText(selectedTrucker.getDriverLicense());
        healthCertifField.setSelected(selectedTrucker.getHealthCertificate());
        workEmailField.setVisible(false);
        isAdminField.setVisible(false);
        workEmailT.setVisible(false);
        managerRadio.setVisible(false);
        driverRadio.setSelected(true);
        disableFields();
    }

    private void fillManagerFields(Manager selectedManager) {
        workEmailField.setText(selectedManager.getWorkEmail());
        isAdminField.setSelected(selectedManager.getIsAdmin());
        driverLicenseField.setVisible(false);
        driverLicenseT.setVisible(false);
        healthCertifField.setVisible(false);
        driverRadio.setVisible(false);
        managerRadio.setSelected(true);
        disableFields();
    }

    public void disableFields() {
        if (selectedUser == null) {
            return;
        }

        driverRadio.setToggleGroup(group);
        managerRadio.setToggleGroup(group);

        switch (selectedUser.getUserType()) {
            case TRUCKER:
                workEmailField.setDisable(true);
                isAdminField.setDisable(true);
                healthCertifField.setDisable(false);
                driverLicenseField.setDisable(false);
                break;
            case MANAGER:
                healthCertifField.setDisable(true);
                driverLicenseField.setDisable(true);
                workEmailField.setDisable(false);
                isAdminField.setDisable(false);
                break;
            default:
                workEmailField.setDisable(true);
                isAdminField.setDisable(true);
                healthCertifField.setDisable(true);
                driverLicenseField.setDisable(true);
        }
    }


    public void createNewUser() throws IOException {
        if (driverRadio.isSelected()) {
            createTruckerUser();
        } else {
            createManagerUser();
        }
        FxUtils.generateAlert(Alert.AlertType.INFORMATION, "User registration report", "User created successfully!");
        returnToPrevious();
    }

    private void createTruckerUser() {
        Trucker trucker = new Trucker(loginField.getText(), passwordField.getText(), nameField.getText(),
                surnameField.getText(), emailField.getText(), phoneNumberField.getText(),
                healthCertifField.isSelected(), driverLicenseField.getText(), null);
        userHib.createUser(trucker);
    }

    private void createManagerUser() {
        Manager manager = new Manager(loginField.getText(), passwordField.getText(), nameField.getText(),
                surnameField.getText(), emailField.getText(), phoneNumberField.getText(), workEmailField.getText(),
                isAdminField.isSelected());
        manager.setUserType(isAdminField.isSelected() ? UserType.ADMIN : UserType.MANAGER);
        userHib.createUser(manager);
    }

    public void AddVehicle() {
        Trucker selectedTrucker = userHib.getTruckerById(selectedUser.getId());
        int vehicleId = Integer.parseInt(getVehicleIdFromCombobox());
        selectedTrucker.setVehicle(vehicleHib.getVehicleById(vehicleId));
        userHib.editUser(selectedTrucker);
        FxUtils.generateAlert(Alert.AlertType.INFORMATION, "User update report", "Vehicle added successfully!");
    }

    private String getVehicleIdFromCombobox() {
        return vehicleCombobox.getValue().split(":")[VEHICLE_ID_INDEX];
    }

    public void updateUser(ActionEvent actionEvent) {
        try {
            if (selectedUser.getUserType() == UserType.TRUCKER) {
                updateTruckerUser();
            } else {
                updateManagerUser();
            }
            FxUtils.generateAlert(Alert.AlertType.INFORMATION, "User update report", "User updated successfully!");
            allUsers();
        } catch (IOException e) {
            handleIOException(e);
        }
    }

    private void updateTruckerUser() {
        Trucker selectedTrucker = userHib.getTruckerById(selectedUser.getId());
        updateCommonFields(selectedTrucker);
        selectedTrucker.setDriverLicense(driverLicenseField.getText());
        selectedTrucker.setHealthCertificate(healthCertifField.isSelected());
        userHib.editUser(selectedTrucker);
    }

    private void updateManagerUser() {
        Manager selectedManager = userHib.getManagerById(selectedUser.getId());
        updateCommonFields(selectedManager);
        selectedManager.setWorkEmail(workEmailField.getText());
        selectedManager.setIsAdmin(isAdminField.isSelected());
        selectedManager.setUserType(isAdminField.isSelected() ? UserType.ADMIN : UserType.MANAGER);
        userHib.editUser(selectedManager);
    }

    private void updateCommonFields(User user) {
        user.setLogin(loginField.getText());
        user.setPassword(passwordField.getText());
        user.setName(nameField.getText());
        user.setSurname(surnameField.getText());
        user.setEmail(emailField.getText());
        user.setPhoneNumber(phoneNumberField.getText());
    }

    private void handleIOException(IOException e) {
        e.printStackTrace(); // Handle or log the exception as needed
    }

    public void returnToPrevious() throws IOException {
        if (registration) {
            goToLogin();
        } else {
            allUsers();
        }
    }

    private void goToLogin() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-page.fxml"));
        setupScene(fxmlLoader, "Trucker system");
    }

    public void allUsers() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("all-users-window.fxml"));
        AllUsersWindow allUsersWindow = fxmlLoader.getController();
        allUsersWindow.setData(entityManagerFactory, currentUserId);
        setupScene(fxmlLoader, "All Users");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        disableFields();
    }

    private void setupScene(FXMLLoader fxmlLoader, String title) throws IOException {
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) loginField.getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }
}