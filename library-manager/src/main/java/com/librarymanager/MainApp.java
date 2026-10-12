
package com.librarymanager;

import com.librarymanager.controller.WriterController;
import com.librarymanager.dao.WriterDAO;
import com.librarymanager.model.Writer;
import com.librarymanager.view.WriterView;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {

        // Temporary DAO for testing without a database
        WriterDAO testDAO = new WriterDAO() {

            @Override
            public Writer create(Writer writer) {
                return writer;
            }

            @Override
            public void updateWriter(Writer writer) {
            }

            @Override
            public void deleteWriter(Writer writer) {
            }

            @Override
            public Optional<Writer> findById(int writerId) {
                return Optional.empty();
            }

            @Override
            public List<Writer> findAll() {
                return List.of();
            }
        };

        // Connect the DAO, controller and view
        WriterController controller = new WriterController(testDAO);
        WriterView root = new WriterView(controller);

        // Create the window
        Scene scene = new Scene(root, 800, 500);

        stage.setTitle("Library Manager - Writers");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
