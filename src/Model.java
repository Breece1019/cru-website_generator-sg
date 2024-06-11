import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Model {
    private Document doc;
    private Map<String, Region> RegionList = new HashMap<>();
    private int[] indexMemory; // some way to store the collapse# when a region is removed

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

    Region getRegion(String regionName) {
        return this.RegionList.get(regionName);
    }

    void addRegion(String regionName) {
        // I know this looks super gross but it works, I will change it later
        // TODO fix href="#collapse1" to be the correct number
        String html = "<div class=\"panel panel-default\"><div class=\"panel-heading\"><h4 class=\"panel-title\"><a class=\"accordion-toggle\" data-toggle=\"collapse\" data-parent=\"#accordion\" href=\"#collapse1\">" + regionName + "</a></h4></div><div id=\"collapse1\" class=\"panel-collapse collapse\"><div class=\"panel-body\"><dl></dl></div></div></div>";
        Element newRegion = Jsoup.parseBodyFragment(html, "UTF-8").selectFirst("div[class=\"panel panel-default\"]");
        this.doc.selectFirst("div[id=\"accordion\"]").appendChild(newRegion);
        RegionList.put(regionName, new Region(newRegion, this.doc));
    }

    void removeRegion(String regionName) {
        this.doc.selectFirst(this.RegionList.get(regionName).cssSelector()).remove();
    }

    void renameRegion(String oldName, String newName) {
        Region reg = RegionList.get(oldName);
        if (reg != null) {
            reg.setName(newName);
            this.RegionList.remove(oldName);
            this.RegionList.put(newName, reg);
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

    void removeStudyLeader(String leader, String studyName, String regionName) {
        Region region = RegionList.get(regionName);
        if (region != null) {
            region.getStudy(studyName).removeLeader(leader);
        }
    }
}
