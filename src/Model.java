import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
public class Model {
    private Document doc;
    private Map<String, Region> regionList;

    Model(File file) {
        this.regionList = new HashMap<>();
        try {
            this.doc = Jsoup.parse(file, "UTF-8");
            parse(this.doc);
        } catch (IOException e) {
            System.out.println("ERROR: Could not process file.");
        }
    }

    private void parse(Document doc) {
        Elements regions = doc.select("div[class=\"panel panel-default\"]");
        for (Element r : regions) {
            Region reg = new Region(r, this.doc);
            this.regionList.put(r.selectFirst("a[class=\"accordion-toggle\"]").text(), reg);
            //System.out.println("DEBUG: " + this.collapseIDMemory.size());

            Elements studyList = r.select("div[class=\"study\"]");
            for (Element study : studyList) {
                reg.addStudy(new Study(study, this.doc));
            }
        }
    }

    String getHtml() {
        return this.doc.outerHtml();
    }

    Set<String> getRegionNames() {
        return this.regionList.keySet();
    }

    Set<String> getStudyNames(String regionName) {
        return this.regionList.get(regionName).getStudyNames();
    }

    void createRegion(String regionName) {
        // I know this looks super gross but it works, maybe I will change it later
        String html = "<div class=\"panel panel-default\"><div class=\"panel-heading\"><h4 class=\"panel-title\"><a class=\"accordion-toggle\" data-toggle=\"collapse\" data-parent=\"#accordion\" href=\"#collapse\">" + regionName + "</a></h4></div><div id=\"collapse\" class=\"panel-collapse collapse\"><div class=\"panel-body\"><dl></dl></div></div></div>";
        Element newRegion = Jsoup.parseBodyFragment(html, "UTF-8").selectFirst("div[class=\"panel panel-default\"]");
        this.doc.selectFirst("div[id=\"accordion\"]").appendChild(newRegion);
        regionList.put(regionName, new Region(newRegion, this.doc));
    }

    void moveRegionXbeforeY(Region X, Region Y) {
        this.doc.selectFirst("div.panel.panel-default:contains(" + Y.getName() + ")").before(this.doc.selectFirst(X.cssSelector()));
    }

    void moveRegionXafterY(Region X, Region Y) {
        this.doc.selectFirst("div.panel.panel-default:contains(" + Y.getName() + ")").after(this.doc.selectFirst(X.cssSelector()));
    }

    void removeRegion(String regionName) {
        Region region = this.regionList.get(regionName);
        this.doc.selectFirst(region.cssSelector()).remove();
    }

    void renameRegion(String oldName, String newName) {
        Region reg = regionList.get(oldName);
        if (reg != null) {
            reg.setName(newName);
            this.regionList.remove(oldName);
            this.regionList.put(newName, reg);
        }
    }

    void renameStudy(String oldName, String newName, String regionName) {
        Region region = regionList.get(regionName);
        if (region != null) {
            region.renameStudy(oldName, newName);
        }
    }

    void addStudyLeader(String newLeader, String studyName, String regionName) {
        Region region = regionList.get(regionName);
        if (region != null) {
            region.getStudy(studyName).addLeader(newLeader);
        }
    }

    void removeStudyLeader(String leader, String studyName, String regionName) {
        Region region = regionList.get(regionName);
        if (region != null) {
            region.getStudy(studyName).removeLeader(leader);
        }
    }

    void fixCollapseIDs() {
        // this will be called right before a new file is generated
        int i = 1;
        for (Element e : this.doc.select("div.panel.panel-default")) {
            e.selectFirst("a[class=\"accordion-toggle\"]").attr("href", ("#collapse" + i));
            e.selectFirst("div.panel-collapse.collapse").attr("id", ("collapse" + i));
            i++;
        }
    }
}
