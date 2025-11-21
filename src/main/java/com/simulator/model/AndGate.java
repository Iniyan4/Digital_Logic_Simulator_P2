package com.simulator.model;

/**
 * Implements a 2-input AND gate.
 */
public class AndGate implements BinaryGate {

    private Gate inputA = null;
    private Gate inputB = null;

    @Override
    public void setInputA(Gate inputA) {
        this.inputA = inputA;
    }

    @Override
    public void setInputB(Gate inputB) {
        this.inputB = inputB;
    }

    @Override
    public boolean getOutput() {
        // Get the state of both inputs, defaulting to false if not connected
        boolean stateA = (inputA != null) && inputA.getOutput();
        boolean stateB = (inputB != null) && inputB.getOutput();

        // The core logic: A AND B
        return stateA && stateB;
    }
}