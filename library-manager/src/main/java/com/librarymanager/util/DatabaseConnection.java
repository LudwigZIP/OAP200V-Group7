package com.librarymanager.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import javafx.application.Platform;

public class DatabaseConnection {

	private static final String PROPERTIES_FILE = "/db.properties";

	public static Connection getConnection() {
		try (InputStream input = DatabaseConnection.class.getResourceAsStream(PROPERTIES_FILE)) {
			if (input == null) {
				showError("Database configuration file not found.");
				return null;
			}
			Properties props = new Properties();
			props.load(input);

			String url = props.getProperty("db.url");
			String user = props.getProperty("db.user");
			String password = props.getProperty("db.password");

			return DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			showError("Failed to connect to the database.\n" + e.getMessage());
			return null;
		}
	}

	private static void showError(String message) {
		Platform.runLater(() -> AlertUtils.showError(message));
	}
}
