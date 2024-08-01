package com.cru.websitegeneratorhelper;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Study {
    private Element root;
    private Document doc;
    private String name;
    private String whenAndWhere;
    private List<String> leaders;

    Study (Element root, Document doc) {
        this.root = root;
        this.doc = doc;
        this.name = root.selectFirst("dt").text();
        Element temp = root.selectFirst("dd[class=\"whenandwhere\"]");
        this.whenAndWhere = (temp != null) ? temp.text() : "Please email for more info";
        leaders = new ArrayList<>();
        for (Element leader : root.select("dd[class=\"leader\"]")) {
            // System.out.println("DEBUG: " + leader.html());
            leaders.add(leader.html());
        }
    }

    String getName() {
        return this.name;
    }

    String cssSelector() {
        return root.cssSelector();
    }

    void setName(String newName) {
        this.name = newName;
        this.doc.selectFirst(this.root.cssSelector()).selectFirst("dt").selectFirst("h3").text(newName);
    }

    String getWhenAndWhere() {
        return this.whenAndWhere;
    }

    void setWhenAndWhere(String waw) {
        this.whenAndWhere = waw;
        doc.selectFirst(this.root.cssSelector()).selectFirst("dd[class\"whenandwhere\"]").text(waw);
    }

    List<String> getLeaders() {
        return this.leaders;
    }

    void addLeader(String leader) {
        this.leaders.add(leader);
        Element newLeader = new Element("dd").addClass("leader").text(leader);
        this.doc.selectFirst(this.root.cssSelector()).appendChild(newLeader);
    }

    //TODO
    boolean removeDetail(String detail) {
        int i = 0;

        for (Element e : doc.selectFirst(this.root.cssSelector()).select("dd")) {
            System.out.println(i++ + e.html());
            if (e.text().equals(detail)) {
                e.remove();
            }
        }
        return this.leaders.remove(detail);
    }

    void changeDetail(String oldDetail, String newDetail) {
        for (Element e : doc.select(this.root.cssSelector()).select("dd")) {
            if (e.html().contains(oldDetail)) {
                e.text("").append(newDetail);
            }
        }
    }
}
