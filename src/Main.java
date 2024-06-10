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

        try {
            doc = Jsoup.parse(input, "UTF-8");
            Model model = new Model(doc);
            model.renameRegion("South Campus Women", "The Gorls (South)");
            model.renameStudy("Park-Stradley", "Sus on 42nd Street!", "South Campus Women");
            model.addStudyLeader("- Malik Sesay", "Baker East/West", "South Campus Men");

            writer = new FileWriter(output);
            writer.write(model.getDoc().outerHtml());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }   
    }
}
