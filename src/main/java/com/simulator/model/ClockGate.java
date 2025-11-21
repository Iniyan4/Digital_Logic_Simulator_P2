package com.simulator.model;

/**
 * A logical clock gate. It's a source (like InputSwitch)
 * but its state will be toggled by a central timer in the app.
 * It implements Gate so it can be connected to other gates.
 */
public class ClockGate implements Gate {

    private boolean state = false;

    /**
     * Toggles the internal state of the clock.
     * This will be called by the SimulatorApp's timer.
     */
    public void toggle() {
        this.state = !this.state;
    }

    /**
     * Required by the Gate interface.
     * @return the current state (0 or 1) of the clock.
     */
    @Override
    public boolean getOutput() {
        return this.state;
    }
}