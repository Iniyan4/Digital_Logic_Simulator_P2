package com.simulator.model;

/**
 * An abstraction for any gate that takes a single input.
 */
public interface UnaryGate extends Gate {
    /**
     * Connects a source gate to this gate's single input.
     *
     * @param input The gate providing the input signal.
     */
    void setInput(Gate input);
}