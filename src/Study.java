import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;

public class Study {
    private Element root;
    private String name;
    private String whenAndWhere;
    private List<String> leaders;

    Study (Element root) {
        this.root = root;
        this.name = root.selectFirst("dt").text();
        Element temp = root.selectFirst("dd[class=\"whenandwhere\"]");
        this.whenAndWhere = (temp != null) ? temp.text() : "ERROR: NO LOCATION!! (error for now)";
        leaders = new ArrayList<>();
        for (Element leader : root.select("dd[class=\"leader\"]")) {
            // System.out.println("DEBUG: " + leader.html());
            leaders.add(leader.html());
        }
    }

    Element getElement() {
        return this.root;
    }

    String getName() {
        return this.name;
    }

    String getWhenAndWhere() {
        return this.whenAndWhere;
    }

    List<String> getLeaders() {
        return this.leaders;
    }

    // Still not sure if this is just easier than changing the document itself. I think it is.
    String toHTML() {
        String ret = "<div class=\"study\">\n";
        ret = ret.concat("\t<dt><h3>" + this.name + "</h3></dt>\n");
        if (!this.whenAndWhere.equals("")) {
            ret = ret.concat("\t<dd class=\"whenandwhere\">" + this.whenAndWhere + "</dd>\n");
        }
        for (String leader : this.leaders) {
            ret = ret.concat("\t<dd class=\"leader\">" + leader + "</dd>\n");
        }
        ret = ret.concat("</div>");

        return ret;
    }
}
