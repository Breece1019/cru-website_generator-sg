import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        // Parse the HTML file into document type
        File input = new File("testFull.html");
        File output = new File("output.html");
        Document doc;
        FileWriter writer;

        // TODO change model constructor to take filename, not document
        // (move try-catch to model.java)
        try {
            doc = Jsoup.parse(input, "UTF-8");
            Model model = new Model(doc);
            model.renameRegion("South Campus Women", "The Gorls (South)");
            model.renameStudy("Park-Stradley", "Sus on 42nd Street!", "The Gorls (South)");
            model.addStudyLeader("- Malik Sesay", "Baker East/West", "South Campus Men");
            model.removeStudyLeader("Adam Joehelin","Baker East/West", "South Campus Men");
            model.removeRegion("Greek Men");
            model.createRegion("The Nether");
            model.getRegion("The Nether").createStudy("Base");
            model.getRegion("The Nether").getStudy("Base").addLeader("- Herobrine");
            model.moveRegionXbeforeY(model.getRegion("The Nether"), model.getRegion("Greek Women"));


            writer = new FileWriter(output);
            writer.write(model.getDoc().outerHtml());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }   
    }
}
