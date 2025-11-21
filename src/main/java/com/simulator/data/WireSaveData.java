package com.simulator.data;

public class WireSaveData {
    private String startGateId;
    private String endGateId;
    private String endPinType; // "A" for pinInA, "B" for pinInB

    //Default constructor (required by Jackson)
    public WireSaveData() {}

    public WireSaveData(String startGateId, String endGateId, String endPinType) {
        this.startGateId = startGateId;
        this.endGateId = endGateId;
        this.endPinType = endPinType;
    }

    //Getters and Setters
    public String getStartGateId() {
        return startGateId;
    }

    public void setStartGateId(String startGateId) {
        this.startGateId = startGateId;
    }

    public String getEndGateId() {
        return endGateId;
    }

    public void setEndGateId(String endGateId) {
        this.endGateId = endGateId;
    }

    public String getEndPinType() {
        return endPinType;
    }

    public void setEndPinType(String endPinType) {
        this.endPinType = endPinType;
    }
}