package com.cru.websitegeneratorhelper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Main extends Application {
    private Stage window;
    private Scene loadScene; // first scene
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // initialize load window
        this.window = primaryStage;
        window.setTitle("Cru Website Generator");
        window.getIcons().add(new Image("file:data/cru-logo.png"));
        loadScene = setLoadScene();
        window.setScene(loadScene);
        window.show();
    }

    private Scene setLoadScene() {
        // fill Load window (1st window) with its function
        StackPane loadLayout = new StackPane();
        Button loadButton = new Button("Load File");
        loadButton.setPrefSize(150, 70);
        loadButton.setStyle("-fx-font-size: 24px;");
        loadButton.setOnAction(event -> loadAndProcessFile(window));
        loadLayout.getChildren().add(loadButton);
        
        return new Scene(loadLayout, 300, 250);
    }

    private void loadAndProcessFile(Stage primaryStage) {
        File file;
        Model model;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + File.separator + "Downloads"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("HTML Files", "*.html"));
        file = fileChooser.showOpenDialog(primaryStage);
        if (file != null) {
            model = new Model(file);
            primaryStage.setScene(setEditScene(model));
        }
    }

    private Scene setEditScene(Model model) {
        // fill Edit window (2nd window) with its functions

        BorderPane editLayout = new BorderPane();
        HBox bottomButtons = setEditBottom(model);

        // Set up tree to be editable
        TreeView<String> tree = model.getTreeView();
        tree.setShowRoot(false);
        tree.setEditable(true);

        // Create a text field for user input
        TextField userInputField = new TextField();
        userInputField.setPromptText("Enter new item name");

        VBox inputBox = new VBox(userInputField);
        inputBox.setVisible(false); // Initially hide the input box

        // Layout for the tree view and user input
        VBox treeLayout = new VBox(tree, inputBox);
        VBox.setVgrow(tree, Priority.ALWAYS);

        // Event handling for "/" to show input box and "ESC" to deselect item
        tree.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.SLASH && tree.getSelectionModel().getSelectedItem() != null) {
                TreeItem<String> selectedItem = tree.getSelectionModel().getSelectedItem();
                userInputField.setText(selectedItem.getValue());
                inputBox.setVisible(true);
                
                // Delay the focus request to avoid the "/" character being added
                Platform.runLater(() -> {
                    userInputField.requestFocus();
                    userInputField.selectAll(); // Select all text so user can start typing immediately
                });
            } if (event.getCode() == KeyCode.ESCAPE) {
                tree.getSelectionModel().clearSelection();  // removes what is selected
            }
        });

        // Event handling for "Enter" to update tree item
        userInputField.setOnAction(event -> {
            TreeItem<String> selectedItem = tree.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                switch (getHeight(selectedItem)) {
                    case 1: // Region
                        model.renameRegion(selectedItem.getValue(),
                            userInputField.getText());
                        break;
                    case 2: // Study
                        model.getRegion(selectedItem.getParent().getValue())
                            .renameStudy(selectedItem.getValue(),
                            userInputField.getText());
                        break;
                    case 3: // Detail
                        model.getRegion(selectedItem.getParent().getParent().getValue())
                            .getStudy(selectedItem.getParent().getValue())
                            .changeDetail(selectedItem.getValue(), userInputField.getText());
                        break;
                    default:

                }
                selectedItem.setValue(userInputField.getText());
                inputBox.setVisible(false);
                userInputField.clear();
                tree.requestFocus(); // Return focus to the tree view
            }
        });

        // Event handling for "Esc" to cancel the update
        userInputField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                inputBox.setVisible(false);
                userInputField.clear();
                tree.requestFocus(); // Return focus to the tree view
            }
        });

        treeLayout.setStyle("-fx-font-size: 24px;");
        editLayout.setCenter(treeLayout);
        editLayout.setLeft(setEditLeft(model));
        editLayout.setBottom(bottomButtons);


        return new Scene(editLayout, 1200, 900);        
    }

    private HBox setEditBottom(Model model) {
        HBox editLayoutBottom = new HBox(10);
        Button cancelButton = new Button("Cancel");
        Button generateButton = new Button("Generate");
        Button addButton = new Button("Add");
        Button removeButton = new Button("Remove");
        editLayoutBottom.setStyle(("-fx-font-size: 20px;"));
        // Need to specify javafx Region to create a spacer
        javafx.scene.layout.Region spacer1 = new javafx.scene.layout.Region();
        javafx.scene.layout.Region spacer2 = new javafx.scene.layout.Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS);
        HBox.setHgrow(spacer2, Priority.ALWAYS);
        cancelButton.setOnAction(event -> window.setScene(loadScene));
        generateButton.setOnAction(event -> outputFile(model));

        addButton.setOnAction(event -> {
            TreeItem<String> selectedItem = model.getTreeView().getSelectionModel().getSelectedItem();
            String newString = "";

            if (selectedItem != null) { // Study or Detail
                switch (getHeight(selectedItem)) {
                    case 1: // Study
                        newString = "New Study";
                        model.getRegion(selectedItem.getValue()).addStudy(newString);
                        break;
                    case 2: // Detail
                    case 3: // Detail TODO add whenandwhere
                        newString = "New Leader";
                        Study study = model.getRegion(
                            selectedItem.getParent().getParent().getValue())
                            .getStudy(selectedItem.getParent().getValue());

                        study.addLeader(newString);

                        break;
                    default:
                }
                
            } else {    // new region
                newString = "New Region";
                model.addRegion(newString);
            }
            selectedItem.getChildren().add(new TreeItem<>(newString));
        });

        removeButton.setOnAction(event -> {
            TreeItem<String> selectedItem = model.getTreeView().getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                switch (getHeight(selectedItem)) {
                    case 1: // Region
                        model.removeRegion(selectedItem.getValue());
                        break;
                    case 2: // Study
                        model.getRegion(selectedItem.getParent().getValue())
                            .removeStudy(selectedItem.getValue());
                        break;
                    case 3: // Detail
                        model.getRegion(selectedItem.getParent().getParent().getValue())
                            .getStudy(selectedItem.getParent().getValue())
                            .removeDetail(selectedItem.getValue());
                        break;
                    default:
                }
                selectedItem.getParent().getChildren().remove(selectedItem);
            }
        });

        editLayoutBottom.setPadding(new Insets(10));
        editLayoutBottom.getChildren().addAll(cancelButton, spacer1, addButton, removeButton, spacer2, generateButton);
        

        return editLayoutBottom;
    }

    private VBox setEditLeft(Model model) {
        VBox editLayoutLeft = new VBox(100);
        Button upButton = new Button("\u2191");
        Button downButton = new Button("\u2193");
        editLayoutLeft.setStyle("-fx-font-size: 26px;");
        upButton.setPrefSize(50, 150);
        downButton.setPrefSize(50, 150);


        editLayoutLeft.setPadding(new Insets(10));
        editLayoutLeft.getChildren().addAll(upButton, downButton);
        editLayoutLeft.setAlignment(Pos.CENTER);

        return editLayoutLeft;
    }
    
    private void outputFile(Model model) {
        //File output = new File(this.fileName.replace(".html", "") + "-output.html");
        FileWriter writer;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + File.separator + "Downloads"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("HTML Files", "*.html"));
        model.fixCollapseIDs();
        File output = fileChooser.showSaveDialog(window);
        if (output != null) {
            try {
                writer = new FileWriter(output);
                writer.write(model.getHtml());
                writer.close(); 
            } catch (IOException e) {
                System.out.println("ERROR: Could not write to file.");
            }
        }
    }

    private int getHeight(TreeItem<String> tItem) {
        int height;
        TreeItem<String> pathFinder = tItem.getParent();
        for (height = 0; pathFinder != null; height++) { pathFinder = pathFinder.getParent(); }

        return height;
    }
}
