package com.simulator.data;

import java.util.List;

public class CircuitData {
    private List<GateSaveData> gates;
    private List<WireSaveData> wires;

    //Default constructor (required by Jackson)
    public CircuitData() {}

    public CircuitData(List<GateSaveData> gates, List<WireSaveData> wires) {
        this.gates = gates;
        this.wires = wires;
    }

    //Getters and Setters
    public List<GateSaveData> getGates() {
        return gates;
    }

    public void setGates(List<GateSaveData> gates) {
        this.gates = gates;
    }

    public List<WireSaveData> getWires() {
        return wires;
    }

    public void setWires(List<WireSaveData> wires) {
        this.wires = wires;
    }
}