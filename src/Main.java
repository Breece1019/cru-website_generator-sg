import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Main extends Application {
    private Stage window;
    private Scene loadScene; // first scene
    public static void main(String[] args) {
        launch(args);

        // File output = new File("output.html");
        // FileWriter writer;

        // Model model = new Model("testFull.html");
        // model.renameRegion("South Campus Women", "The Gorls (South)");
        // model.renameStudy("Park-Stradley", "Sus on 42nd Street!", "The Gorls (South)");
        // model.addStudyLeader("- Malik Sesay", "Baker East/West", "South Campus Men");
        // model.removeStudyLeader("Adam Joehelin","Baker East/West", "South Campus Men");
        // model.removeRegion("Greek Men");
        // model.createRegion("The Nether");
        // model.getRegion("The Nether").createStudy("Base");
        // model.getRegion("The Nether").getStudy("Base").addLeader("- Herobrine");
        // model.moveRegionXbeforeY(model.getRegion("The Nether"), model.getRegion("Greek Women"));

        // try {
        //     model.fixCollapseIDs();
        //     writer = new FileWriter(output);
        //     writer.write(model.getHtml());
        //     writer.close(); 
        // } catch (IOException e) {
        //     System.out.println("ERROR: Could not write to file.");
        // }
         
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
        if (file != null) {
            model = new Model(file);
            primaryStage.setScene(setEditScene(model));
        }
    }

    private Scene setEditScene(Model model) {
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(event -> window.setScene(loadScene));
        StackPane layout2 = new StackPane();
        layout2.getChildren().add(cancelButton);
        
        return new Scene(layout2, 800, 600);        
    }
}
