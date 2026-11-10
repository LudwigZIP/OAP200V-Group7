
package com.librarymanager.view;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

/**
 * Displays summary reports about the private book collection.
 *
 * The view contains a text area for displaying reports and a button
 * for refreshing the information. Report generation and database
 * access are handled by ReportsController.
 *
 * @author Dilan Shwan
 */
public class ReportView extends BorderPane {

    // Displays the generated report and allows the user to refresh it
    private final TextArea reportArea = new TextArea();
    private final Button refreshButton = new Button("Refresh Report");


    // Constructs the report view and initializes its components
    public ReportView() {
        buildReportArea();
        buildToolbar();
    }


    // Creates the area where the generated report is displayed

    private void buildReportArea() {

        // Reports are read-only and cannot be edited by the user
        reportArea.setEditable(false);

        setCenter(reportArea);
    }

    // Creates the toolbar containing the refresh button
    private void buildToolbar() {

        HBox toolbar = new HBox(refreshButton);
        toolbar.setPadding(new Insets(5));

        setTop(toolbar);
    }

    // Returns the refresh button so the controller can assign its action
    public Button getRefreshButton() {
        return refreshButton;
    }

    // Displays the generated report in the text area
    public void setReportText(String text) {
        reportArea.setText(text);
    }
}