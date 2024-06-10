import java.util.HashMap;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Model {
    private Document doc;
    private Map<String, Region> RegionList = new HashMap<>();

    Model(Document doc) {
        this.doc = doc;
        parse(this.doc);
    }

    private void parse(Document doc) {
        Elements regions = doc.select("div[class=\"panel panel-default\"]");
        for (Element r : regions) {
            Region reg = new Region(r, this.doc);
            this.RegionList.put(r.selectFirst("a[class=\"accordion-toggle\"]").text(), reg);

            Elements studyList = r.select("div[class=\"study\"]");
            for (Element study : studyList) {
                reg.addStudy(new Study(study, this.doc));
            }
        }
    }

    Document getDoc() {
        return this.doc;
    }

    void renameRegion(String oldName, String newName) {
        Region reg = RegionList.get(oldName);
        if (reg != null) {
            reg.setName(newName);
        }
    }

    void renameStudy(String oldName, String newName, String regionName) {
        Region region = RegionList.get(regionName);
        if (region != null) {
            region.renameStudy(oldName, newName);
        }
    }

    void addStudyLeader(String newLeader, String studyName, String regionName) {
        Region region = RegionList.get(regionName);
        if (region != null) {
            region.getStudy(studyName).addLeader(newLeader);
        }
    }
}
