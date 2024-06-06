import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Model {
    Document doc;

    Model(Document doc) {
        this.doc = doc;
    }

    void parse() {
        Elements regions = doc.select("div[class=\"panel panel-default\"]");
        for (Element r : regions) {
            System.out.println(r.selectFirst("a[class=\"accordion-toggle\"]").text());   // Print Region Name

            Elements studyList = r.select("div[class=\"study\"]");
            for (Element study : studyList) {
                System.out.println("\t" + study.selectFirst("dt").text()); // Print Study Name

                Elements ddTags = study.select("dd");
                for (Element dd : ddTags) {
                    System.out.println("\t\t" + dd.text()); // Print Details
                }
            }
        }
    }

    Element findRegion(String regionName) {
        Elements regions = doc.select("div[class=\"panel panel-default\"]");
        for (Element r : regions) {
            if (r.selectFirst("a[class=\"accordion-toggle\"]").text().equals(regionName)) {
                System.out.println("DEBUG Found Element: " + regionName);
            }
            return r.selectFirst("a[class=\"accordion-toggle\"]");
        }
        return null;
    }

    void renameRegion(String region, String newName) {
        Element reg = findRegion(region);
        if (reg != null) {
            System.out.println("DEBUG Region name WAS: " + reg.text());
            reg.text(newName);
            System.out.println("DEBUG Region name is NOW: " + reg.text());
        } else {
            System.out.println("ERROR: Region not found");
        }
    }
}
