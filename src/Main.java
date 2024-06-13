import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Main extends Application {
    private Stage window;
    private Scene loadScene; // first scene
    private String fileName;
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
        loadButton.setPrefSize(150, 75);
        loadButton.setStyle("-fx-font-size: 24px;");
        loadButton.setOnAction(event -> loadAndProcessFile(window));
        loadLayout.getChildren().add(loadButton);
        
        return new Scene(loadLayout, 300, 250);
    }

    private void loadAndProcessFile(Stage primaryStage) {
        File file;
        Model model;
        System.out.println("test");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + File.separator + "Downloads"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("HTML Files", "*.html"));
        file = fileChooser.showOpenDialog(primaryStage);
        this.fileName = file.getName();
        if (file != null) {
            model = new Model(file);
            primaryStage.setScene(setEditScene(model));
        }
    }

    private Scene setEditScene(Model model) {
        BorderPane editLayout = new BorderPane();
        HBox bottomButtons = setEditBottom(model);

        // Set up tree to be editable
        TreeView<String> tree = setEditCenter(model);
        tree.setShowRoot(false);
        tree.setEditable(true);
        editLayout.setCenter(tree);
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
    
    private TreeView<String> setEditCenter(Model model) {
        TreeItem<String> root = new TreeItem<>();
        TreeItem<String> child;
        TreeItem<String> grandchild;
        root.setExpanded(true);

        for (String region : model.getRegionNames()) {
            child = new TreeItem<String>(region);
            root.getChildren().add(child);
            for (String study : model.getStudyNames(region)) {
                grandchild = new TreeItem<String>(study);
                child.getChildren().add(grandchild);
                for (String detail : model.getDetails(study, region)) {
                    grandchild.getChildren().add(new TreeItem<String>(detail));
                }
            }
        }

        return new TreeView<>(root);
    }

    private void outputFile(Model model) {
        File output = new File(this.fileName.replace(".html", "") + "-output.html");
        FileWriter writer;
        try {
            model.fixCollapseIDs();
            writer = new FileWriter(output);
            writer.write(model.getHtml());
            writer.close(); 
        } catch (IOException e) {
            System.out.println("ERROR: Could not write to file.");
        }
    }
}
