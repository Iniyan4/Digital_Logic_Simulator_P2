package com.simulator.ui;

import javafx.scene.Cursor;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;

/**
 * A visual representation of an input or output connection point on a gate.
 * It's a Circle that handles mouse events to initiate wiring.
 */
public class ConnectionPin extends Circle {

    // Enum to define pin type
    public enum PinType {
        INPUT,
        OUTPUT
    }

    private final PinType type;
    private final GateView parentGateView; // The GateView this pin belongs to
    private final SimulatorApp app; // Reference to the main app to call onPinClicked
    private boolean isConnected = false;

    /**
     * Creates a new ConnectionPin.
     *
     * @param type           Whether this is an INPUT or OUTPUT pin.
     * @param parentGateView The GateView this pin is part of.
     * @param app            The main SimulatorApp instance for event handling.
     */
    public ConnectionPin(PinType type, GateView parentGateView, SimulatorApp app) {
        super(8); // Create a circle with a radius of 8
        this.type = type;
        this.parentGateView = parentGateView;
        this.app = app;

        // Set initial visual style
        setFill(Color.WHITE);
        setStroke(Color.BLACK);
        setStrokeWidth(2);
        setStrokeType(StrokeType.INSIDE);

        // Add Event Handlers
        this.setOnMouseEntered(event -> {
            if (!event.isPrimaryButtonDown()) {
                // Show CROSSHAIR cursor to indicate a connection can be made
                getScene().setCursor(Cursor.CROSSHAIR);
            }
        });

        this.setOnMouseExited(event -> {
            if (!event.isPrimaryButtonDown()) {
                // Revert to default cursor
                getScene().setCursor(Cursor.DEFAULT);
            }
        });

        this.setOnMouseClicked(event -> {
            // Tell the main app that this pin was clicked
            app.onPinClicked(this);
            event.consume(); // Stop event from bubbling up to the GateView
        });
    }

    public PinType getPinType() {
        return type;
    }

    /**
     * Gets the GateView that this pin is attached to.
     *
     * @return The parent GateView.
     */
    public GateView getParentGateView() {
        return parentGateView;
    }

    // We can use these methods later to show connection status
    public void setConnected(boolean connected) {
        this.isConnected = connected;
        if (connected) {
            setFill(Color.LIGHTGREEN);
        } else {
            setFill(Color.WHITE);
        }
    }

    public boolean isConnected() {
        return isConnected;
    }
}