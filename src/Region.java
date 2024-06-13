import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Region {
    private Element root;
    private Document doc;
    private String name;
    private Map<String, Study> studies;

    Region(Element region, Document doc) {
        this.root = region;
        this.doc = doc;
        this.name = region.selectFirst("a[class=\"accordion-toggle\"]").text();
        //System.out.println("DEBUG: collapseID = " + collapseID);
        this.studies = new HashMap<>();
    }

    String cssSelector() {
        return root.cssSelector();
    }

    Study getStudy(String studyName) {
        return this.studies.get(studyName);
    }

    Set<String> getStudyNames() {
        return this.studies.keySet();
    }

    String getName() {
        return this.name;
    }

    void setName(String newName) {
        this.name = newName;
        this.doc.selectFirst(this.root.cssSelector()).selectFirst("a[class=\"accordion-toggle\"]").text(newName);
    }

    void addStudy(Study study) {
        this.studies.put(study.getName(), study);
    }

    void addStudy(String studyName) {
        String html = "<div class=\"study\"><dt><h3>" + studyName + "</h3></dt></div>";
        Element e = Jsoup.parseBodyFragment(html, "UTF-8").selectFirst("div[class=\"study\"]");
        //System.out.println(e);
        this.doc.selectFirst(this.root.cssSelector()).selectFirst("dl").appendChild(e);
        this.studies.put(studyName, new Study(e, doc));
    }

    Study removeStudy(String studyName) {
        return this.studies.remove(studyName);
    }

    void moveStudyXbeforeY(Study X, Study Y) {
        this.doc.selectFirst("div.panel.panel-default:contains(" + Y.getName() + ")").before(this.doc.selectFirst(X.cssSelector()));
    }

    void moveStudyXafterY(Study X, Study Y) {
        this.doc.selectFirst("div.panel.panel-default:contains(" + Y.getName() + ")").after(this.doc.selectFirst(X.cssSelector()));
    }

    void renameStudy(String oldName, String newName) {
        Study study = this.studies.get(oldName);
        study.setName(newName);
        this.studies.remove(oldName);
        this.studies.put(newName, study);
    }
}
