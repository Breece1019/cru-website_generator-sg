import java.util.HashMap;
import java.util.Map;

import org.jsoup.nodes.Element;

public class Region {
    private Element root;
    private Map<String, Study> studies;

    Region(Element root) {
        this.root = root;
        studies = new HashMap<>();
    }

    void addStudy(Study study) {
        this.studies.put(study.getName(), study);
        System.out.println(study.toHTML());
    }
    
    Study removeStudy(String studyName) {
        return this.studies.remove(studyName);
    }

    Study getStudy(String studyName) {
        return this.studies.get(studyName);
    }
}
