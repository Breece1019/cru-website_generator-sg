import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Study {
    private String root;
    private Document doc;
    private String name;
    private String whenAndWhere;
    private List<String> leaders;

    Study (Element root, Document doc) {
        this.root = root.cssSelector();
        this.doc = doc;
        this.name = root.selectFirst("dt").text();
        Element temp = root.selectFirst("dd[class=\"whenandwhere\"]");
        this.whenAndWhere = (temp != null) ? temp.text() : "ERROR: NO LOCATION!! (error for now)";
        leaders = new ArrayList<>();
        for (Element leader : root.select("dd[class=\"leader\"]")) {
            // System.out.println("DEBUG: " + leader.html());
            leaders.add(leader.html());
        }
    }

    String getName() {
        return this.name;
    }

    void setName(String newName) {
        this.name = newName;
        this.doc.selectFirst(this.root).selectFirst("dt").text(newName);
    }

    String getWhenAndWhere() {
        return this.whenAndWhere;
    }

    void setWhenAndWhere(String waw) {
        this.whenAndWhere = waw;
        doc.selectFirst(root).selectFirst("dd[class\"whenandwhere\"]").text(waw);
    }

    List<String> getLeaders() {
        return this.leaders;
    }

    void addLeader(String leader) {
        this.leaders.add(leader);
        Element newLeader = new Element("dd").addClass("leader").text(leader);
        this.doc.selectFirst(root).appendChild(newLeader);
    }

    boolean removeLeader(String leader) {
        for (Element e : doc.selectFirst(root).select("dd")) {
            if (e.text().contains(leader)) {
                e.remove();
            }
        }
        return this.leaders.remove(leader);
    }
}
