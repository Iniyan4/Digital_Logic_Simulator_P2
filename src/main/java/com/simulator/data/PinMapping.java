package com.simulator.data;

public class PinMapping {
    private String exposedName; // e.g., "In-1", "Out-1"
    private String direction;   // "IN" or "OUT"
    private String internalGateId; // ID of the gate inside the bundle
    private String internalPinType; // "A", "B", or "OUT"

    //No-arg constructor for Jackson
    public PinMapping() {}

    public PinMapping(String exposedName, String direction, String internalGateId, String internalPinType) {
        this.exposedName = exposedName;
        this.direction = direction;
        this.internalGateId = internalGateId;
        this.internalPinType = internalPinType;
    }

    //Getters and Setters
    public String getExposedName() {
        return exposedName;
    }

    public void setExposedName(String exposedName) {
        this.exposedName = exposedName;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getInternalGateId() {
        return internalGateId;
    }

    public void setInternalGateId(String internalGateId) {
        this.internalGateId = internalGateId;
    }

    public String getInternalPinType() {
        return internalPinType;
    }

    public void setInternalPinType(String internalPinType) {
        this.internalPinType = internalPinType;
    }
}