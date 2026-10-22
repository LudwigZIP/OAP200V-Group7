package com.librarymanager;

import com.librarymanager.view.MainView;
import com.librarymanager.controller.AppController;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The main entry point for the Private Library Manager application.
 *
 * This class initializes the JavaFX application, creates the main
 * application window, and displays the MainView.
 *
 * Contribution: Responsible for application startup and
 * initialization of the primary JavaFX stage.
 *
 * @author Dilan Shwan
 * @version 1.0
 */


public class MainApp extends Application {

    private AppController appController;
    
    /**
 * Initializes and displays the main application window.
 *
 * @param primaryStage the primary stage provided by JavaFX
 */

    @Override 
    public void start(Stage primaryStage) {

        appController = new AppController();
        // Establish DB connection at startup
        appController.start();

        //Pass controller to MainView
        MainView mainView = new MainView(appController);

        Scene scene = new Scene(mainView, 1300, 500);

        primaryStage.setTitle("Private Library Manager");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override 
    public void stop() {
        if (appController != null) {
            appController.stop();
        }
    }

    /**
    * Launches the JavaFX application.
    *
    * @param args command-line arguments
    */
   
    public static void main(String[] args) {
        launch();
    }
}