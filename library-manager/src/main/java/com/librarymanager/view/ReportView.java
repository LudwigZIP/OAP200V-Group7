
package com.librarymanager.view;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

/**
 * Displays summary reports about the private book collection.
 * as well as exporting library report.
 *
 * The view contains a text area for displaying reports, along with
 * buttons for refreshing and exporting. Report generation and
 * file export are handled by ReportsController.
 *
 * @author Dilan Shwan
 */

public class ReportView extends BorderPane {

    // Displays the generated library report
    private final TextArea reportArea = new TextArea();

    // Buttons to refresh and export the report
    private final Button refreshButton = new Button("Refresh Report");
    private final Button exportButton = new Button("Export Report");


    /**
     * Constructs the report view and initializes its components.
     *
     * Creates the report display area and the toolbar containing
     * the refresh and export buttons.
     */
    public ReportView() {
        buildReportArea();
        buildToolbar();
    }


    /**
     * Creates the area used to display the generated report.
     *
     * Configures the text area as read-only and places it
     * in the center of the BorderPane.
     */
    private void buildReportArea() {

        // Prevents the user from editing the report
        reportArea.setEditable(false);

        setCenter(reportArea);
    }

    /**
     * Creates the toolbar containing the report action buttons.
     *
     * Places the refresh and export buttons horizontally
     * at the top of the view.
     */
    private void buildToolbar() {

        HBox toolbar = new HBox(10, refreshButton, exportButton);
        toolbar.setPadding(new Insets(5));

        setTop(toolbar);
    }

    /**
     * Returns the button used to refresh the report.
     *
     * @return the refresh button
     */
    public Button getRefreshButton() {
        return refreshButton;
    }

    /**
     * Returns the button used to export the report.
     *
     * @return the export button
     */
    public Button getExportButton() {
        return exportButton;
    }

    /**
     * Retrieves the text currently displayed in the report area.
     *
     * Used by ReportController when exporting the report
     * to a text file.
     *
     * @return the currently displayed report text
     */
    public String getReportText() {
        return reportArea.getText();
    }

    /**
     * Updates the report text displayed in the interface.
     *
     * @param text the generated report text to display
     */
    public void setReportText(String text) {
        reportArea.setText(text);
    }
}