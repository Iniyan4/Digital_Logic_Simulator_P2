Digital Logic Simulator
A feature-rich digital logic simulator built from scratch using Java and JavaFX. This application provides a full drag-and-drop workspace for building, simulating, and debugging both combinational and sequential logic circuits.

It was designed from the ground up to be a showcase of Object-Oriented Principles, featuring a clean separation between the logical model and the GUI.

✨ Features
Complete Component Library: Drag and drop all 7 basic logic gates (AND, OR, NOT, NAND, NOR, XOR, XNOR).

Sequential Logic: Simulate sequential circuits using an interactive Clock component that provides a steady pulse.

Interactive I/O: Use toggleable Switches for inputs and "LED" Probes for outputs (RED-0, GREEN-1).

Custom Labeling: Double-click any component (gates, switches, probes) to give it a custom name (e.g., "A", "Sum").

Create Custom Gates: Select a group of components (like a half-adder), "Bundle" them, and save them as a new, reusable gate in your palette.

Save & Load: Save your entire circuit design to a JSON file and load it back in later.

Truth Table Generation: Automatically analyze your circuit's inputs and outputs and generate a complete truth table.

Full GUI Tooling:

Multi-selection with Shift-click.

Delete components with the Delete key or BackSpace.

Dynamic palette that reloads custom gates without a restart.

🏛️ OOP Design
This project was built to demonstrate strong Object-Oriented design:

Abstraction: The Gate, UnaryGate, and BinaryGate interfaces define a strict "contract" for all logical components, allowing any gate to connect to any other.

Encapsulation: Complex gates (like XorGate) are built by composing base gates (ANDs, ORs, NOTs), completely hiding their internal complexity from the rest of the application.

Polymorphism: The GateView.update() method polymorphically renders different components (InputSwitch, OutputProbe, ClockGate) based on their underlying model, allowing the UI to update with a single triggerCircuitUpdate() call.

Inheritance: Core GUI components like GateView (extends Pane) and ConnectionPin (extends Circle) inherit JavaFX functionality.

🚀 How to Run
Clone the repository.

Open the project in an IDE (like IntelliJ or Eclipse).

Ensure you have a JDK with JavaFX (e.g., Liberica Full JDK, ZuluFX).

Add the Jackson libraries (Core, Databind, Annotations) to your project's dependencies for the save/load feature.

Locate and run the SimulatorApp.java file.
