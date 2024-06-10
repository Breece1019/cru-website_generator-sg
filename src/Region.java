import java.util.HashMap;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Region {
    private Element root;
    private Document doc;
    private String name;
    private Map<String, Study> studies;

    Region(Element root, Document doc) {
        this.root = root;
        this.doc = doc;
        this.name = this.root.selectFirst("a[class=\"accordion-toggle\"]").text();
        studies = new HashMap<>();
    }

    void addStudy(Study study) {
        this.studies.put(study.getName(), study);
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

    Study getStudy(String studyName) {
        return this.studies.get(studyName);
    }

    String getName() {
        return this.name;
    }

    void setName(String newName) {
        this.name = newName;
        this.doc.selectFirst(root.cssSelector()).selectFirst("a[class=\"accordion-toggle\"]").text(newName);
        this.root.selectFirst("a[class=\"accordion-toggle\"]").text(newName);
    }

    Element getElement() {
        return this.root;
    }
}
