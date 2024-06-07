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
        Element region = null;
        Elements regions = doc.select("div[class=\"panel panel-default\"]");
        for (Element r : regions) {
            if (r.selectFirst("a[class=\"accordion-toggle\"]").text().equals(regionName)) {
                System.out.println("DEBUG Found Element: " + regionName);
                region = r;
            }
        }

        if (region == null) {
            System.out.println("ERROR: Region '" + regionName + "' not found");
        }

        return region;
    }

    Element findStudy(String studyName) {
        Element study = null;
        Elements studies = doc.select("div[class=\"study\"]");
        for (Element s: studies) {
            if (s.selectFirst("dt").text().equals(studyName)) {
                System.out.println("DEBUG Found Element: " + studyName);
                study = s;
            }
        }

        if (study == null) {
            System.out.println("ERROR: Study '" + studyName + "' not found");
        }

        return study;
    }

    Element findStudy(String studyName, String regionName) {
        Element study = null;
        Element region = findRegion(regionName);
        if (region == null) {
            return null;
        }
        Elements studies = region.select("div[class=\"study\"]");
        for (Element s: studies) {
            if (s.selectFirst("dt").text().equals(studyName)) {
                System.out.println("DEBUG Found Element: " + studyName + " of " + regionName);
            }
            study = s.selectFirst("dt");
        }

        if (study == null) {
            System.out.println("ERROR: Study '" + studyName + " of " + regionName + "' not found");
        }

        return study;
    }

    void renameRegion(String oldName, String newName) {
        Element reg = findRegion(oldName);
        if (reg != null) {
            reg = reg.selectFirst("a[class=\"accordion-toggle\"]");
            System.out.println("DEBUG Region name WAS: " + reg.text());
            reg.text(newName);
            System.out.println("DEBUG Region name is NOW: " + reg.text());
        }
    }

    void renameStudy(String oldName, String newName) {
        Element study = findStudy(oldName);
        if (study != null) {
            study = study.selectFirst("dt");
            System.out.println("DEBUG Study name WAS: " + study.text());
            study.text(newName);
            System.out.println("DEBUG Study name is NOW: " + study.text());
        }
    }

    void renameStudy(String oldName, String newName, String regionName) {
        Element study = findStudy(oldName, regionName);
        if (study != null) {
            study = study.selectFirst("dt");
            System.out.println("DEBUG Study name WAS: " + study.text());
            study.text(newName);
            System.out.println("DEBUG Study name is NOW: " + study.text());
        }
    }
}
