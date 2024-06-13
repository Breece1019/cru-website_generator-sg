import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {
    Button button;
    public static void main(String[] args) {
        launch(args);

        // // Parse the HTML file into document type
        
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
        primaryStage.setTitle("Cru Website Generator");
        button = new Button("Testing 1 2 3");

        StackPane layout = new StackPane();
        layout.getChildren().add(button);
        Scene scene = new Scene(layout, 300, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
