import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
public class Model {
    private static final int maxRegions = 100;

    private Document doc;
    private Map<String, Region> regionList;
    private boolean[] collapseIDMemory; // stores the collapse# when modifying regions

    Model(Document doc) {
        this.doc = doc;
        this.regionList = new HashMap<>();
        this.collapseIDMemory = new boolean[maxRegions];
        parse(this.doc);
    }

    private void parse(Document doc) {
        Elements regions = doc.select("div[class=\"panel panel-default\"]");
        for (Element r : regions) {
            Region reg = new Region(r, this.doc);
            this.regionList.put(r.selectFirst("a[class=\"accordion-toggle\"]").text(), reg);
            this.collapseIDMemory[reg.getCollapseID() - 1] = true;
            //System.out.println("DEBUG: " + this.collapseIDMemory.size());

            Elements studyList = r.select("div[class=\"study\"]");
            for (Element study : studyList) {
                reg.addStudy(new Study(study, this.doc));
            }
        }
    }

    private int getFirstFalseIndex(boolean[] array) {
        int i;
        boolean found = false;
        for (i = 0; !found && i < array.length; i++) {
            if (array[i] == false) {
                found = true;
            }
        }
        return (found) ? (i - 1) : i;
    }

    Document getDoc() {
        return this.doc;
    }

    Region getRegion(String regionName) {
        return this.regionList.get(regionName);
    }

    void addRegion(String regionName) {
        int i = getFirstFalseIndex(this.collapseIDMemory);
        this.collapseIDMemory[i] = true;
        // I know this looks super gross but it works, maybe I will change it later
        String html = "<div class=\"panel panel-default\"><div class=\"panel-heading\"><h4 class=\"panel-title\"><a class=\"accordion-toggle\" data-toggle=\"collapse\" data-parent=\"#accordion\" href=\"#collapse" + (i + 1) + "\">" + regionName + "</a></h4></div><div id=\"collapse" + (i + 1) + "\" class=\"panel-collapse collapse\"><div class=\"panel-body\"><dl></dl></div></div></div>";
        Element newRegion = Jsoup.parseBodyFragment(html, "UTF-8").selectFirst("div[class=\"panel panel-default\"]");
        this.doc.selectFirst("div[id=\"accordion\"]").appendChild(newRegion);
        regionList.put(regionName, new Region(newRegion, this.doc));
    }

    void removeRegion(String regionName) {
        Region region = this.regionList.get(regionName);
        this.collapseIDMemory[region.getCollapseID() - 1] = false;
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
}
