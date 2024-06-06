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
            model.parse();
            model.renameRegion("South Campus Men", "New South Campus Men");

            writer = new FileWriter(output);
            writer.write(doc.outerHtml());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }   
    }
}
