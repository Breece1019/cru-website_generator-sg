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
        if (temp == null) {
            this.whenAndWhere = "- Please email for more info";
            this.doc.selectFirst(this.root.cssSelector()).appendChild(
                new Element("dd").addClass("whenandwhere")
                .text(this.whenAndWhere));
        } else {
            this.whenAndWhere = temp.text();
        }
        leaders = new ArrayList<>();
        for (Element leader : root.select("dd[class=\"leader\"]")) {
            leaders.add(leader.html());
        }
    }

    String getName() {
        return this.name;
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
        doc.selectFirst(this.root.cssSelector()).selectFirst("dd[class=\"whenandwhere\"]").text(waw);
    }

    List<String> getLeaders() {
        return this.leaders;
    }

    void addLeader(String leader) {
        this.leaders.add(leader);
        Element newLeader = new Element("dd").addClass("leader").append(leader);
        this.doc.selectFirst(this.root.cssSelector()).appendChild(newLeader);
    }

    boolean removeDetail(String detail) {
        for (Element e : doc.selectFirst(this.root.cssSelector()).select("dd")) {
            if (e.html().equals(detail)) {
                if (e.hasClass("\"whenandwhere\"")) {
                    setWhenAndWhere("");
                }
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

    void moveDetailUp(String str) {
        Element currEl = null;
        Element prevEl = null;
        for (Element el : this.root.select("dd")) {
            if (el.html().trim().equals(str)) {
                currEl = el;
                prevEl = currEl.previousElementSibling();
            }
        }
        if (prevEl != null) {
            prevEl.before(currEl);
        }
    }

    void moveDetailDown(String str) {
        Element currEl = null;
        Element nextEl = null;
        for (Element el : this.root.select("dd")) {
            if (el.html().trim().equals(str)) {
                currEl = el;
                nextEl = currEl.nextElementSibling();
            }
        }
        if (nextEl != null) {
            nextEl.after(currEl);
        }
    }
}
