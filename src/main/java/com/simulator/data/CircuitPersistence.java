package com.simulator.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.simulator.model.BinaryGate;
import com.simulator.model.Gate;
import com.simulator.model.UnaryGate;
import com.simulator.ui.ConnectionPin;
import com.simulator.ui.GateView;
import com.simulator.ui.SimulatorApp;
import com.simulator.ui.Wire;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CircuitPersistence {

    private final ObjectMapper objectMapper;
    private final FileChooser fileChooser;
    private final SimulatorApp app;
    private final Pane rootPane;
    private final List<GateView> allGateViews;

    public CircuitPersistence(SimulatorApp app, Pane rootPane, List<GateView> allGateViews) {
        this.app = app;
        this.rootPane = rootPane;
        this.allGateViews = allGateViews;

        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        this.fileChooser = new FileChooser();
        this.fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        this.fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("JSON Files", "*.json")
        );
    }

    public void saveCircuit(Stage mainStage) {
        fileChooser.setTitle("Save Circuit");
        File file = fileChooser.showSaveDialog(mainStage);
        if (file == null) return;

        List<GateSaveData> gateDataList = new ArrayList<>();
        List<WireSaveData> wireDataList = new ArrayList<>();

        for (GateView view : allGateViews) {
            gateDataList.add(new GateSaveData(
                    view.getId(), view.getGateType(), view.getLayoutX(), view.getLayoutY(), view.getCustomLabel()
            ));
        }

        for (Node node : rootPane.getChildren()) {
            if (node instanceof Wire) {
                Wire wire = (Wire) node;
                GateView startView = wire.getStartPin().getParentGateView();
                GateView endView = wire.getEndPin().getParentGateView();
                String endPinType = (wire.getEndPin() == endView.getPinInB()) ? "B" : "A";
                wireDataList.add(new WireSaveData(startView.getId(), endView.getId(), endPinType));
            }
        }

        CircuitData circuitData = new CircuitData(gateDataList, wireDataList);
        try {
            objectMapper.writeValue(file, circuitData);
            fileChooser.setInitialFileName(file.getName());
        } catch (IOException e) {
            System.err.println("Error saving file: " + e.getMessage());
        }
    }

    /**
     * Loads a circuit. Returns true if successful, false if cancelled or failed.
     */
    public boolean loadCircuit(Stage mainStage) {
        fileChooser.setTitle("Load Circuit");
        File file = fileChooser.showOpenDialog(mainStage);

        if (file == null) {
            return false; // User cancelled
        }

        try {
            CircuitData circuitData = objectMapper.readValue(file, CircuitData.class);

            app.clearWorkspace();

            Map<String, GateView> gateMap = new HashMap<>();

            // 1. Re-create gates
            for (GateSaveData gateData : circuitData.getGates()) {
                GateView view = app.createAndAddGate(
                        gateData.getGateType(), gateData.getX(), gateData.getY(), gateData.getId()
                );
                if (gateData.getCustomLabel() != null) {
                    view.setCustomLabel(gateData.getCustomLabel());
                }
                gateMap.put(view.getId(), view);
            }

            // 2. Re-create wires
            for (WireSaveData wireData : circuitData.getWires()) {
                GateView startView = gateMap.get(wireData.getStartGateId());
                GateView endView = gateMap.get(wireData.getEndGateId());
                if (startView == null || endView == null) continue;

                ConnectionPin startPin = startView.getPinOut();
                ConnectionPin endPin = (wireData.getEndPinType().equals("B")) ? endView.getPinInB() : endView.getPinInA();
                if (startPin == null || endPin == null) continue;

                Wire wire = new Wire(startPin, endPin);
                wire.setOnMouseClicked(event -> {
                    app.toggleSelectItem(wire, event.isShiftDown());
                    event.consume();
                });
                rootPane.getChildren().add(wire);

                Gate outputGate = startView.getGateModel();
                Gate inputGate = endView.getGateModel();
                if (inputGate instanceof UnaryGate) ((UnaryGate) inputGate).setInput(outputGate);
                else if (inputGate instanceof BinaryGate) {
                    if (wireData.getEndPinType().equals("B")) ((BinaryGate) inputGate).setInputB(outputGate);
                    else ((BinaryGate) inputGate).setInputA(outputGate);
                }
            }

            fileChooser.setInitialFileName(file.getName());
            app.triggerCircuitUpdate();
            return true; // Success

        } catch (IOException e) {
            System.err.println("Error loading file: " + e.getMessage());
            return false; // Failure
        }
    }

    public void resetFileChooser() {
        fileChooser.setInitialFileName(null);
    }
}