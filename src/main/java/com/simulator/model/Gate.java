package com.simulator.model;

/**
 * The core abstraction for any component in the circuit that has an output.
 */
public interface Gate {
    /**
     * Computes and returns the output state of this gate.
     * This method will recursively pull outputs from its inputs.
     *
     * @return true for HIGH (1), false for LOW (0)
     */
    boolean getOutput();
}