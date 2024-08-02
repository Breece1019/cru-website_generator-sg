package com.cru.websitegeneratorhelper;

import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Region {
    private Element root;
    private Document doc;
    private String name;
    private Map<String, Study> studies;

    Region(Element region, Document doc) {
        this.root = region;
        this.doc = doc;
        this.name = region.selectFirst("a[class=\"accordion-toggle\"]").text();
        //System.out.println("DEBUG: collapseID = " + collapseID);
        this.studies = new HashMap<>();
    }

    String cssSelector() {
        return root.cssSelector();
    }

    Study getStudy(String studyName) {
        return this.studies.get(studyName);
    }

    String getName() {
        return this.name;
    }

    void setName(String newName) {
        this.name = newName;
        this.doc.selectFirst(this.root.cssSelector()).selectFirst("a[class=\"accordion-toggle\"]").text(newName);
    }

    void addStudy(Study study) {
        this.studies.put(study.getName(), study);
    }

    Study addStudy(String studyName) {
        String html = "<div class=\"study\"><dt><h3>" + studyName + "</h3></dt></div>";
        Element e = Jsoup.parseBodyFragment(html, "UTF-8").selectFirst("div[class=\"study\"]");
        this.doc.selectFirst(this.root.cssSelector()).selectFirst("dl").appendChild(e);
        this.studies.put(studyName, new Study(e, doc));

        return this.studies.get(studyName);
    }

    Study removeStudy(String studyName) {
        for (Element e : doc.selectFirst(this.root.cssSelector()).select("div.study")) {
            if (e.text().contains(studyName)) {
                e.remove();
            }
        }
        return this.studies.remove(studyName);
    }

    void renameStudy(String oldName, String newName) {
        Study study = this.studies.get(oldName);
        study.setName(newName);
        this.studies.remove(oldName);
        this.studies.put(newName, study);
    }

    void moveStudyUp(Study s) {
        Element currEl = this.root.selectFirst("div.study:contains(" + s.getName() + ")");
        Element lastEl = currEl.previousElementSibling();
        if (lastEl != null) {
            lastEl.before(currEl);
        }
    }

    void moveStudyDown(Study s) {
        Element currEl = this.root.selectFirst("div.study:contains(" + s.getName() + ")");
        Element nextEl = currEl.nextElementSibling();
        if (nextEl != null) {
            nextEl.after(currEl);
        }
    }
}
