import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
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
        // model.renameRegion("South Campus Women", "The Gorls (South)");
        // model.renameStudy("Park-Stradley", "Sus on 42nd Street!", "The Gorls (South)");
        // model.addStudyLeader("- Malik Sesay", "Baker East/West", "South Campus Men");
        // model.removeStudyLeader("Adam Joehelin","Baker East/West", "South Campus Men");
        // model.removeRegion("Greek Men");
        // model.addRegion("The Nether");
        // model.getRegion("The Nether").createStudy("Base");
        // model.getRegion("The Nether").getStudy("Base").addLeader("- Herobrine");
        // model.moveRegionXbeforeY(model.getRegion("The Nether"), model.getRegion("Greek Women"));
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.window = primaryStage;
        window.setTitle("Cru Website Generator");
        window.getIcons().add(new Image("file:data/cru-logo.png"));
        loadScene = setLoadScene();
        window.setScene(loadScene);
        window.show();
    }

    private Scene setLoadScene() {
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
        BorderPane editLayout = new BorderPane();
        HBox bottomButtons = setEditBottom(model);

        // Set up tree to be editable
        TreeView<String> tree = model.getTreeView();
        tree.setShowRoot(false);
        tree.setEditable(true);

        // Create a text field for user input
        TextField userInputField = new TextField();
        userInputField.setPromptText("Enter new item name");

        // Layout for user input
        VBox inputBox = new VBox(userInputField);
        inputBox.setVisible(false); // Initially hide the input box

        // Layout for the tree view and user input
        VBox treeLayout = new VBox(tree, inputBox);
        VBox.setVgrow(tree, Priority.ALWAYS);

        // Event handling for "/" key to show input box
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
            }
        });

        // Event handling for "Enter" to update tree item
        userInputField.setOnAction(event -> {
            TreeItem<String> selectedItem = tree.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                int height;
                TreeItem<String> pathFinder = selectedItem.getParent();
                for (height = 0; pathFinder != null; height++) { pathFinder = pathFinder.getParent(); }
                switch (height) {
                    case 1: // Region
                        model.renameRegion(selectedItem.getValue(),
                            userInputField.getText());
                        break;
                    case 2: // Study
                        model.renameStudy(
                            selectedItem.getValue(),
                            userInputField.getText(),
                            selectedItem.getParent().getValue());
                        break;
                    case 3: // Detail
                        model.changeDetail(
                            selectedItem.getValue(),
                            userInputField.getText(),
                            selectedItem.getParent().getValue(),
                            selectedItem.getParent().getParent().getValue());
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
        //editLayout.setRight(inputBox);
        editLayout.setBottom(bottomButtons);


        return new Scene(editLayout, 1200, 900);        
    }

    private HBox setEditBottom(Model model) {
        HBox editLayoutBottom = new HBox();
        Button cancelButton = new Button("Cancel");
        Button generateButton = new Button("Generate");
        javafx.scene.layout.Region spacer = new javafx.scene.layout.Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        cancelButton.setOnAction(event -> window.setScene(loadScene));
        generateButton.setOnAction(event -> outputFile(model));
        // Need to specify javafx Region to create a spacer
        editLayoutBottom.setPadding(new Insets(10));
        editLayoutBottom.getChildren().addAll(cancelButton, spacer, generateButton);

        return editLayoutBottom;
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
}
