package com.simulator.model;

/**
 * An abstraction for any gate that takes two inputs.
 */
public interface BinaryGate extends Gate {
    /**
     * Connects a source gate to this gate's first input (Input A).
     *
     * @param inputA The gate providing the signal for input A.
     */
    void setInputA(Gate inputA);

    /**
     * Connects a source gate to this gate's second input (Input B).
     *
     * @param inputB The gate providing the signal for input B.
     */
    void setInputB(Gate inputB);
}