module com.simulator {
    // JavaFX Modules
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    // JSON Library
    requires com.fasterxml.jackson.databind;

    // EXPORTS: Allow other modules (and tests) to see your public classes
    exports com.simulator.ui;
    exports com.simulator.model;
    exports com.simulator.data;

    // OPENS: Allow libraries to use reflection (for FXML and Jackson)
    opens com.simulator.ui to javafx.fxml;
    opens com.simulator.data to com.fasterxml.jackson.databind;
}