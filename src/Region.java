import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Region {
    private String root;
    private Document doc;
    private String name;
    private int collapseID;
    private Map<String, Study> studies;

    Region(Element region, Document doc) {
        this.root = region.cssSelector();
        this.doc = doc;
        this.name = region.selectFirst("a[class=\"accordion-toggle\"]").text();
        this.collapseID = Integer.parseInt(region.selectFirst("a[class=\"accordion-toggle\"]").attr("href").substring(9));
        System.out.println("DEBUG: collapseID = " + collapseID);
        this.studies = new HashMap<>();
    }

    String cssSelector() {
        return root;
    }

    Study getStudy(String studyName) {
        return this.studies.get(studyName);
    }

    String getName() {
        return this.name;
    }

    void setName(String newName) {
        this.name = newName;
        this.doc.selectFirst(root).selectFirst("a[class=\"accordion-toggle\"]").text(newName);
    }

    int getCollapseID() {
        return this.collapseID;
    }

    void addStudy(Study study) {
        this.studies.put(study.getName(), study);
    }

    void createStudy(String studyName) {
        String html = "<div class=\"study\"><dt><h3>" + studyName + "</h3></dt></div>";
        Element e = Jsoup.parseBodyFragment(html, "UTF-8").selectFirst("div[class=\"study\"]");
        //System.out.println(e);
        this.doc.selectFirst(root).selectFirst("dl").appendChild(e);
        this.studies.put(studyName, new Study(e, doc));
    }

    Study removeStudy(String studyName) {
        return this.studies.remove(studyName);
    }

    void renameStudy(String oldName, String newName) {
        Study study = this.studies.get(oldName);
        study.setName(newName);
        this.studies.remove(oldName);
        this.studies.put(newName, study);
    }
}
