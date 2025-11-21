package com.simulator.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simulator.ui.ConnectionPin;
import com.simulator.ui.GateView;
import com.simulator.ui.Wire;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class TemplateManager {
    private final Map<String, CustomGateTemplate> customGateTemplates = new HashMap<>();
    private final ObjectMapper objectMapper;

    public TemplateManager(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void loadCustomGateTemplates() {
        customGateTemplates.clear();
        File directory = new File("custom_gates");
        if (!directory.exists() || !directory.isDirectory()) return;

        File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".json"));
        if (files == null) return;

        for (File file : files) {
            try {
                CustomGateTemplate template = objectMapper.readValue(file, CustomGateTemplate.class);
                if (template.getGateName() != null) {
                    customGateTemplates.put(template.getGateName(), template);
                }
            } catch (IOException e) {
                System.err.println("Error loading custom gate: " + e.getMessage());
            }
        }
    }

    public CustomGateTemplate getTemplate(String name) {
        return customGateTemplates.get(name);
    }

    public Map<String, CustomGateTemplate> getAllTemplates() {
        return customGateTemplates;
    }

    public void bundleSelectedCircuit(List<Node> selectedItems, Pane rootPane) {
        List<GateView> selectedGates = new ArrayList<>();
        for (Node node : selectedItems) {
            if (node instanceof GateView) selectedGates.add((GateView) node);
        }

        if (selectedGates.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please select one or more gates to bundle.").showAndWait();
            return;
        }

        List<PinMapping> mappings = new ArrayList<>();
        Map<String, String> inputPinSources = new HashMap<>();
        int outCounter = 1;

        for (GateView gate : selectedGates) {
            checkAndMapInput(gate, gate.getPinInA(), selectedGates, mappings, inputPinSources, "A", rootPane);
            checkAndMapInput(gate, gate.getPinInB(), selectedGates, mappings, inputPinSources, "B", rootPane);

            ConnectionPin pinOut = gate.getPinOut();
            if (pinOut != null) {
                List<Wire> outWires = getWiresConnectedFrom(pinOut, rootPane);
                boolean isExternal = outWires.isEmpty();
                if (!isExternal) {
                    for (Wire wire : outWires) {
                        if (!selectedGates.contains(wire.getEndPin().getParentGateView())) {
                            isExternal = true;
                            break;
                        }
                    }
                }
                if (isExternal) {
                    String exposedName = "Out-" + outCounter++;
                    mappings.add(new PinMapping(exposedName, "OUT", gate.getId(), "OUT"));
                }
            }
        }

        List<Object> internalData = getInternalCircuitData(selectedGates, rootPane);

        TextInputDialog dialog = new TextInputDialog("MyCustomGate");
        dialog.setTitle("Create Custom Gate");
        dialog.setHeaderText("Found " + inputPinSources.size() + " inputs and " + (outCounter - 1) + " outputs.");
        dialog.setContentText("Name:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent() && !result.get().isEmpty()) {
            CustomGateTemplate template = new CustomGateTemplate();
            template.setGateName(result.get());
            template.setInternalGates((List<GateSaveData>) internalData.get(0));
            template.setInternalWires((List<WireSaveData>) internalData.get(1));
            template.setMappings(mappings);
            saveCustomGateTemplate(template);
        }
    }

    private void saveCustomGateTemplate(CustomGateTemplate template) {
        File directory = new File("custom_gates");
        if (!directory.exists()) directory.mkdir();

        String fileName = template.getGateName().replaceAll("[^a-zA-Z0-9_\\-]", "") + ".json";
        File file = new File(directory, fileName);

        try {
            objectMapper.writeValue(file, template);
            loadCustomGateTemplates(); // Reload to memory
            new Alert(Alert.AlertType.INFORMATION, "Custom gate saved! Reloading Palette...").showAndWait();
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Error saving custom gate: " + e.getMessage()).showAndWait();
        }
    }

    //Helpers

    private void checkAndMapInput(GateView gate, ConnectionPin pin, List<GateView> selectedGates, List<PinMapping> mappings, Map<String, String> sources, String pinType, Pane rootPane) {
        if (pin != null) {
            Wire wire = getWireConnectedTo(pin, rootPane);
            if (wire == null || !selectedGates.contains(wire.getStartPin().getParentGateView())) {
                String sourceId = (wire == null) ? "unconnected_" + pin.getId() : wire.getStartPin().getParentGateView().getId();
                String exposedName = sources.computeIfAbsent(sourceId, k -> "In-" + (sources.size() + 1));
                mappings.add(new PinMapping(exposedName, "IN", gate.getId(), pinType));
            }
        }
    }

    private Wire getWireConnectedTo(ConnectionPin inputPin, Pane rootPane) {
        if (inputPin == null) return null;
        for (Node node : rootPane.getChildren()) {
            if (node instanceof Wire) {
                Wire wire = (Wire) node;
                if (wire.getEndPin() == inputPin) return wire;
            }
        }
        return null;
    }

    private List<Wire> getWiresConnectedFrom(ConnectionPin outputPin, Pane rootPane) {
        List<Wire> connections = new ArrayList<>();
        if (outputPin == null) return connections;
        for (Node node : rootPane.getChildren()) {
            if (node instanceof Wire) {
                Wire wire = (Wire) node;
                if (wire.getStartPin() == outputPin) connections.add(wire);
            }
        }
        return connections;
    }

    private List<Object> getInternalCircuitData(List<GateView> selectedGates, Pane rootPane) {
        List<GateSaveData> internalGatesData = new ArrayList<>();
        List<WireSaveData> internalWiresData = new ArrayList<>();

        for (GateView view : selectedGates) {
            internalGatesData.add(new GateSaveData(
                    view.getId(), view.getGateType(), view.getLayoutX(), view.getLayoutY(), view.getCustomLabel()
            ));
        }

        for (Node node : rootPane.getChildren()) {
            if (node instanceof Wire) {
                Wire wire = (Wire) node;
                GateView startView = wire.getStartPin().getParentGateView();
                GateView endView = wire.getEndPin().getParentGateView();
                if (selectedGates.contains(startView) && selectedGates.contains(endView)) {
                    String endPinType = (wire.getEndPin() == endView.getPinInA()) ? "A" : "B";
                    internalWiresData.add(new WireSaveData(startView.getId(), endView.getId(), endPinType));
                }
            }
        }
        return Arrays.asList(internalGatesData, internalWiresData);
    }
}