package com.simulator.ui;

import com.simulator.model.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimulationManager {

    private final List<GateView> allGateViews;
    private Timeline clockTimer;
    private final TableView<boolean[]> truthTable;

    public SimulationManager(List<GateView> allGateViews, TableView<boolean[]> truthTable) {
        this.allGateViews = allGateViews;
        this.truthTable = truthTable;
    }

    public void startClock() {
        clockTimer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            for (GateView view : allGateViews) {
                if (view.getGateModel() instanceof ClockGate) {
                    ((ClockGate) view.getGateModel()).toggle();
                }
            }
            triggerCircuitUpdate();
        }));
        clockTimer.setCycleCount(Animation.INDEFINITE);
        clockTimer.play();
    }

    public void triggerCircuitUpdate() {
        for (GateView view : allGateViews) {
            view.update();
        }
    }

    /**
     * NEW: Clears the truth table UI.
     */
    public void clearTruthTable() {
        truthTable.getColumns().clear();
        truthTable.getItems().clear();
    }

    public void generateTruthTable() {
        clearTruthTable();

        List<GateView> inputs = new ArrayList<>();
        List<GateView> outputs = new ArrayList<>();

        for (GateView view : allGateViews) {
            if (view.getGateModel() instanceof InputSwitch || view.getGateModel() instanceof ClockGate) {
                inputs.add(view);
            } else if (view.getGateModel() instanceof OutputProbe) {
                outputs.add(view);
            }
        }

        if (inputs.isEmpty() || outputs.isEmpty()) return;

        int numInputs = inputs.size();
        int numOutputs = outputs.size();
        Map<Gate, Boolean> originalStates = new HashMap<>();

        // Save state
        for (GateView inputView : inputs) {
            if (inputView.getGateModel() instanceof InputSwitch) {
                InputSwitch sw = (InputSwitch) inputView.getGateModel();
                originalStates.put(sw, sw.getOutput());
            }
        }

        // Create columns
        for (int i = 0; i < numInputs; i++) {
            final int colIndex = i;
            String colName = inputs.get(i).getLabelText().replace(": 0", "").replace(": 1", "");
            TableColumn<boolean[], Boolean> col = new TableColumn<>(colName + " (In)");
            col.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue()[colIndex]));
            col.setCellFactory(c -> new TruthTableCell());
            truthTable.getColumns().add(col);
        }
        for (int i = 0; i < numOutputs; i++) {
            final int colIndex = i + numInputs;
            String colName = outputs.get(i).getLabelText();
            TableColumn<boolean[], Boolean> col = new TableColumn<>(colName + " (Out)");
            col.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue()[colIndex]));
            col.setCellFactory(c -> new TruthTableCell());
            truthTable.getColumns().add(col);
        }

        // Generate rows
        int numRows = (int) Math.pow(2, numInputs);
        List<boolean[]> allRowsData = new ArrayList<>();

        for (int i = 0; i < numRows; i++) {
            boolean[] rowData = new boolean[numInputs + numOutputs];
            for (int j = 0; j < numInputs; j++) {
                // MODIFIED: Reverse bit order so first column is MSB (slowest change)
                // Old: boolean state = ((i >> j) & 1) == 1;
                boolean state = ((i >> (numInputs - 1 - j)) & 1) == 1;

                Gate model = inputs.get(j).getGateModel();
                if (model instanceof InputSwitch) ((InputSwitch) model).setState(state);
                else if (model instanceof ClockGate) state = model.getOutput();
                rowData[j] = state;
            }
            for (int k = 0; k < numOutputs; k++) {
                boolean state = ((OutputProbe) outputs.get(k).getGateModel()).getResult();
                rowData[k + numInputs] = state;
            }
            allRowsData.add(rowData);
        }

        truthTable.setItems(FXCollections.observableArrayList(allRowsData));

        // Restore state
        for (Map.Entry<Gate, Boolean> entry : originalStates.entrySet()) {
            ((InputSwitch) entry.getKey()).setState(entry.getValue());
        }
        triggerCircuitUpdate();
    }

    private static class TruthTableCell extends TableCell<boolean[], Boolean> {
        @Override
        protected void updateItem(Boolean item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) setText(null);
            else {
                setText(item ? "1" : "0");
                setAlignment(Pos.CENTER);
            }
        }
    }
}