Digital Logic Simulator

A robust, feature-rich digital logic simulator built from scratch using Java and JavaFX. This application provides a drag-and-drop environment for designing, simulating, and analyzing complex digital circuits, ranging from simple combinational logic to advanced sequential circuits.

🚀 Features

🛠 Component Library

Basic Gates: AND, OR, NOT, NAND, NOR, XOR, XNOR.

Input Sources:

Switch: Toggle input states (0/1) manually.

Clock: A time-based oscillator for sequential logic circuits.

Output Indicators:

Probe: LED-style indicator to visualize logic states (High/Low).

⚡ Simulation Capabilities

Real-time Simulation: Circuit logic updates instantly as inputs change.

Sequential Logic: Support for clocks enables the creation of Flip-Flops, Latches, Counters, and Registers.

Truth Table Generation: Automatically analyzes the current circuit to generate a complete truth table for all $2^n$ input combinations.

📦 Advanced Tooling

Custom Gate Bundling: Select a group of gates (e.g., a Half Adder), bundle them, and save them as a reusable custom component in the palette.

Save & Load: Persist your circuits to JSON files and reload them later using a robust persistence layer.

Multi-Selection: Shift-click to select multiple components for moving or deleting.

Custom Labeling: Double-click any component to assign custom names (e.g., "Carry Out", "Sum").

📋 Prerequisites

To run this project, you need the following installed on your machine:

Java Development Kit (JDK): Version 17 or higher (JDK 21 recommended).

JavaFX SDK: If not bundled with your JDK (e.g., if using OpenJDK), you need the JavaFX libraries.

Jackson Library: For JSON processing (Save/Load functionality).

jackson-core

jackson-databind

jackson-annotations

⚙️ Installation & Setup

Option 1: Using Maven (Recommended)

Add the following dependencies to your pom.xml:

<dependencies>
    <!-- JavaFX Controls -->
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-controls</artifactId>
        <version>21.0.1</version>
    </dependency>
    <!-- JavaFX FXML -->
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-fxml</artifactId>
        <version>21.0.1</version>
    </dependency>
    <!-- Jackson Databind (for JSON) -->
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>
        <version>2.15.2</version>
    </dependency>
</dependencies>


Option 2: Manual Setup (IntelliJ IDEA)

Clone the repository.

Open the project in IntelliJ IDEA.

Go to File > Project Structure > Libraries.

Add the JavaFX .jar files and the Jackson .jar files to the project classpath.

Ensure your VM Options include JavaFX modules if running non-modularly:
--module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml

🎮 Usage Guide

1. Building a Circuit

Drag and Drop components from the left Palette onto the central workspace.

Wire components by clicking an output pin (right side of a gate) and then clicking an input pin (left side of another gate).

Move components by dragging them; wires will update automatically.

2. Running the Simulation

The simulation runs continuously.

Click Switches to toggle them between 0 (White) and 1 (Yellow).

Probes light up Green for High (1) and Dark Red for Low (0).

Clocks toggle automatically every second.

3. Creating a Custom Gate

Build a circuit (e.g., an XOR gate using NANDs).

Hold Shift and click to select the specific gates you want to bundle.

Click the "Bundle Selected" button in the palette.

Give your new gate a name (e.g., "MyNANDXOR").

The new gate appears in the "Custom Gates" section of the palette and is saved to disk.

4. Analysis

Click "Generate Table" to see the truth table for your current circuit on the right panel.

📂 Project Structure

The source code is organized into a clean Model-View-Controller (MVC) architecture:

com.simulator.model: Contains the core logic (e.g., AndGate, LogicGate interface). Pure Java, no UI code.

com.simulator.ui: Contains all JavaFX visual components (GateView, Wire, SimulatorApp).

com.simulator.data: Handles persistence (CircuitPersistence, TemplateManager) and POJO data classes for JSON serialization.

👥 Team

CS24B1109

CS24B1004

CS24B1006

CS24B1042

CS24I1003

📜 License

This project is open-source and available under the MIT License.
