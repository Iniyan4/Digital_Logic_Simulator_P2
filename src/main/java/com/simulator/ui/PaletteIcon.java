package com.simulator.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.paint.Color;

/**
 * A simple, draggable icon for the component palette.
 */
public class PaletteIcon extends Label {

    private final String gateTypeIdentifier;

    /**
     * Constructor for standard gates.
     */
    public PaletteIcon(String labelText, String gateTypeIdentifier) {
        // Call the master constructor, setting 'isCustom' to false
        this(labelText, gateTypeIdentifier, false);
    }

    /**
     * Master constructor for all palette icons.
     */
    public PaletteIcon(String labelText, String gateTypeIdentifier, boolean isCustom) {
        super(labelText);
        this.gateTypeIdentifier = gateTypeIdentifier;

        //Basic Styling
        setMinWidth(100);
        setAlignment(Pos.CENTER);
        // Explicitly setting text fill helps avoid the lookup warning
        setStyle("-fx-font-weight: bold; -fx-text-fill: black;");
        setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID,
                null, new BorderWidths(1))));

        //Set background based on type
        if (isCustom) {
            setBackground(new Background(new BackgroundFill(Color.LIGHTSKYBLUE, null, null)));
        } else {
            setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
        }

        //Drag-and-Drop Handler
        this.setOnDragDetected(event -> {
            Dragboard db = this.startDragAndDrop(TransferMode.COPY);
            ClipboardContent content = new ClipboardContent();
            content.putString(this.gateTypeIdentifier);
            db.setContent(content);
            db.setDragView(this.snapshot(null, null));
            event.consume();
        });
    }
}