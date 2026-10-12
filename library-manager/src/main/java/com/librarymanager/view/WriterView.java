package com.librarymanager.view;

import com.librarymanager.controller.WriterController;
import com.librarymanager.model.Writer;
import com.mysql.cj.jdbc.result.UpdatableResultSet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;

import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

public class WriterView extends BorderPane {

    // Give the view a reference to the controller
    private final WriterController controller;
    
    // Table to show writer objects
    private final TableView<Writer> table = new TableView<>();

    // A list that updates automatically when changed
    private final ObservableList<Writer> data = FXCollections.observableArrayList();

    // Input fields for receiving name and nationality for the Writer
    private final TextField nameField = new TextField();
    private final TextField nationalityField = new TextField();


    /**
     * Creates the WriterView
     * 
     * @param controller the writer controller
     */
    public WriterView(WriterController controller) {
        this.controller = controller;
        buildTable();
        buildForm();
        setCenter(table);

        // Temp data for testing table
        data.add(new Writer("Jo Nesbø", "Norwegian"));
        data.add(new Writer("J.K Rowling", "British"));
    }

    private void buildTable() {
        TableColumn<Writer, String> nameColumn = new TableColumn<>("Name");

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("writerName"));

        TableColumn<Writer, String> nationalityColumn = new TableColumn<>("Nationality");

        nationalityColumn.setCellValueFactory(new PropertyValueFactory<>("writerNationality"));

        table.getColumns().addAll(nameColumn, nationalityColumn);

        table.setItems(data);
    }
    /**
     * Creates the input fields and buttons for writers
     */
    private void buildForm() {
        // Labels for the input fields
        Label nameLabel = new Label("Name");
        Label nationalityLabel = new Label("Nationality");

        // Buttons for writer operations
        Button addButton = new Button("Add");
        Button updateButton = new Button("Update");
        Button deleteButton = new Button("Delete");

        addButton.setOnAction(e -> addWriter());

        // Places for the buttons next to each other
        HBox buttons = new HBox(10, addButton, updateButton, deleteButton);

        // Places for fields and buttons vertically
        VBox form = new VBox(10,
            nameLabel,
            nameField,
            nationalityLabel,
            nationalityField,
            buttons
        );
        form.setPadding(new Insets(15));

        // Places the form at the bottom of the window
        setBottom(form);
    }

    /**
     * Create a new Writer
     */
    private void addWriter() {
        String name = nameField.getText().trim();
        String nationality = nameField.getText().trim();

        // Check if the fields are empty
        if (name.isEmpty() || nationality.isEmpty()) {
            return;
        }

        Writer writer = new Writer(name, nationality);

        Writer createdWriter = controller.addWriter(writer);

        // Show the new Writer in the Table
        data.add(createdWriter);

        // Empty the TextFields after data is added
        nameField.clear();
        nationalityField.clear();
    }

    private void updateWriter() {

    }

    private void deleteWriter() {

    }
}