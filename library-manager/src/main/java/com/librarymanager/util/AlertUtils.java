package com.librarymanager.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AlertUtils {

	public static void showError(String message) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Database Error");
		alert.setHeaderText("A database connection error occurred.");
		alert.setContentText(message);
		alert.showAndWait();
	}

	public static void showInfo(String title, String message) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	public static void showWarning(String message) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Warning");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
}
