package com.simulator.data;

public class GateSaveData {
    private String id;
    private String gateType; // e.g., "AND_GATE", "SWITCH"
    private double x;
    private double y;
    private String customLabel;

    // Default constructor (required by Jackson)
    public GateSaveData() {}

    // Main constructor
    public GateSaveData(String id, String gateType, double x, double y, String customLabel) {
        this.id = id;
        this.gateType = gateType;
        this.x = x;
        this.y = y;
        this.customLabel = customLabel;
    }

    //Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGateType() {
        return gateType;
    }

    public void setGateType(String gateType) {
        this.gateType = gateType;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getCustomLabel() {
        return customLabel;
    }

    public void setCustomLabel(String customLabel) {
        this.customLabel = customLabel;
    }
}