import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javafx.application.Application;

public class Main {
    public static void main(String[] args) {
        // Parse the HTML file into document type
        
        File output = new File("output.html");
        FileWriter writer;

        Model model = new Model("testFull.html");
        model.renameRegion("South Campus Women", "The Gorls (South)");
        model.renameStudy("Park-Stradley", "Sus on 42nd Street!", "The Gorls (South)");
        model.addStudyLeader("- Malik Sesay", "Baker East/West", "South Campus Men");
        model.removeStudyLeader("Adam Joehelin","Baker East/West", "South Campus Men");
        model.removeRegion("Greek Men");
        model.createRegion("The Nether");
        model.getRegion("The Nether").createStudy("Base");
        model.getRegion("The Nether").getStudy("Base").addLeader("- Herobrine");
        model.moveRegionXbeforeY(model.getRegion("The Nether"), model.getRegion("Greek Women"));

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
