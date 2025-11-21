package com.simulator.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.simulator.data.CircuitPersistence;
import com.simulator.data.TemplateManager;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class SimulatorApp extends Application {

    private Stage mainStage;
    private Scene mainScene; // Keep the workspace scene in memory

    // Core Components
    private Pane rootPane;
    private final List<GateView> allGateViews = new ArrayList<>();

    // Managers
    private WorkspaceManager workspaceManager;
    private SimulationManager simulationManager;
    private PaletteManager paletteManager;
    private CircuitPersistence circuitPersistence;
    private TemplateManager templateManager;

    @Override
    public void start(Stage primaryStage) {
        this.mainStage = primaryStage;
        mainStage.setTitle("Digital Logic Simulator");

        // Show the Welcome Screen first
        showWelcomeScreen();
        mainStage.show();
    }

    /**
     * Builds and displays the Front Page / Welcome Screen.
     */
    public void showWelcomeScreen() {
        VBox welcomeLayout = new VBox(20);
        welcomeLayout.setAlignment(Pos.CENTER);
        welcomeLayout.setStyle("-fx-background-color: linear-gradient(to bottom right, #2b2b2b, #3d3d3d);");

        // Title
        Label title = new Label("Digital Logic Simulator");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        title.setTextFill(Color.WHITE);
        title.setEffect(new DropShadow(10, Color.BLACK));

        Label subtitle = new Label("Design, Simulate, Bundle");
        subtitle.setFont(Font.font("Arial", 18));
        subtitle.setTextFill(Color.LIGHTGRAY);

        // Buttons
        Button newProjectBtn = createStyledButton("New Project");
        newProjectBtn.setOnAction(e -> {
            initializeWorkspace();
            mainStage.setScene(mainScene);
            mainStage.centerOnScreen();
        });

        Button loadProjectBtn = createStyledButton("Load Project");
        loadProjectBtn.setOnAction(e -> {
            // Initialize workspace (invisible) first so we have managers ready
            if (mainScene == null) {
                initializeWorkspace();
            }
            // Try to load
            boolean success = circuitPersistence.loadCircuit(mainStage);
            if (success) {
                // Only switch view if user actually picked a file
                mainStage.setScene(mainScene);
                mainStage.centerOnScreen();
            }
        });

        welcomeLayout.getChildren().addAll(title, subtitle, newProjectBtn, loadProjectBtn);
        Scene welcomeScene = new Scene(welcomeLayout, 800, 600);
        mainStage.setScene(welcomeScene);
    }

    /**
     * Helper to style the Welcome Screen buttons.
     */
    private Button createStyledButton(String text) {
        Button btn = new Button(text);
        btn.setFont(Font.font("Arial", 16));
        btn.setPrefWidth(200);
        btn.setPrefHeight(40);
        btn.setStyle(
                "-fx-background-color: #4a90e2; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 5; " +
                        "-fx-cursor: hand;"
        );
        // Simple hover effect
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #357abd; -fx-text-fill: white; -fx-background-radius: 5;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: #4a90e2; -fx-text-fill: white; -fx-background-radius: 5;"));
        return btn;
    }

    /**
     * Initializes the main simulation workspace.
     * This is called only once, when the user clicks New or Load.
     */
    private void initializeWorkspace() {
        if (mainScene != null) return; // Already initialized

        // 1. Init Data & Persistence
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        templateManager = new TemplateManager(objectMapper);
        templateManager.loadCustomGateTemplates();

        rootPane = new Pane();
        circuitPersistence = new CircuitPersistence(this, rootPane, allGateViews);

        // 2. Init Managers
        workspaceManager = new WorkspaceManager(rootPane, this, allGateViews, templateManager);

        TableView<boolean[]> truthTable = new TableView<>();
        simulationManager = new SimulationManager(allGateViews, truthTable);

        paletteManager = new PaletteManager(this, templateManager, circuitPersistence, mainStage);
        paletteManager.reloadPalette();

        // 3. Layout
        BorderPane mainLayout = new BorderPane();
        mainLayout.setLeft(paletteManager.getPalettePane());
        mainLayout.setCenter(rootPane);

        ScrollPane tablePane = new ScrollPane(truthTable);
        tablePane.setFitToWidth(true);
        tablePane.setMinWidth(250);
        mainLayout.setRight(tablePane);

        // 4. Scene & Input
        mainScene = new Scene(mainLayout, 1280, 768);
        mainScene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.DELETE || event.getCode() == KeyCode.BACK_SPACE) {
                workspaceManager.removeSelectedItems();
            }
        });

        simulationManager.startClock();
    }

    //Delegators (Exposed for Interaction)

    public GateView createAndAddGate(String gateType, double x, double y, String id) {
        return workspaceManager.createGate(gateType, x, y, id);
    }

    public void toggleSelectItem(Node item, boolean isShiftDown) {
        workspaceManager.toggleSelectItem(item, isShiftDown);
    }

    public void selectItem(Node item) {
        workspaceManager.toggleSelectItem(item, false);
    }

    public void onPinClicked(ConnectionPin pin) {
        workspaceManager.onPinClicked(pin);
    }

    public void triggerCircuitUpdate() {
        simulationManager.triggerCircuitUpdate();
    }

    public void generateTruthTable() {
        simulationManager.generateTruthTable();
    }

    public void clearWorkspace() {
        workspaceManager.clear();
        simulationManager.clearTruthTable();

        circuitPersistence.resetFileChooser();
        simulationManager.triggerCircuitUpdate();
        templateManager.loadCustomGateTemplates();
        paletteManager.reloadPalette();
    }

    public List<Node> getSelectedItems() {
        return workspaceManager.getSelectedItems();
    }

    public Pane getRootPane() {
        return rootPane;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
