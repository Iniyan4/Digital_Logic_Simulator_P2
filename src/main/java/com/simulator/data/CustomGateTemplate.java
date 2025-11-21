package com.simulator.data;

import java.util.List;

public class CustomGateTemplate {
    private String gateName; // e.g., Half-Adder

    // Lists of the internal components
    private List<GateSaveData> internalGates;
    private List<WireSaveData> internalWires;

    // List of all pin mappings
    private List<PinMapping> mappings;

    // No-arg constructor for Jackson
    public CustomGateTemplate() {

    }

    //Getters and Setters
    public String getGateName() {
        return gateName;
    }

    public void setGateName(String gateName) {
        this.gateName = gateName;
    }

    public List<GateSaveData> getInternalGates() {
        return internalGates;
    }

    public void setInternalGates(List<GateSaveData> internalGates) {
        this.internalGates = internalGates;
    }

    public List<WireSaveData> getInternalWires() {
        return internalWires;
    }

    public void setInternalWires(List<WireSaveData> internalWires) {
        this.internalWires = internalWires;
    }

    public List<PinMapping> getMappings() {
        return mappings;
    }

    public void setMappings(List<PinMapping> mappings) {
        this.mappings = mappings;
    }
}