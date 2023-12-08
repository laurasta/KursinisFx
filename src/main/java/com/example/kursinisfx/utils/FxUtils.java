package com.example.kursinisfx.utils;

import javafx.scene.control.Alert;

public class FxUtils {
    public static void generateAlert(Alert.AlertType alertType, String header, String contentText){
        Alert alert = new Alert(alertType);
        alert.setTitle("Trucker system");
        alert.setHeaderText(header);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
}
