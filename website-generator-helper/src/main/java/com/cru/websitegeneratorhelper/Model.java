package com.cru.websitegeneratorhelper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
public class Model {
    private Document doc;
    private TreeView<String> tree;
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
        TreeItem<String> root = new TreeItem<>();
        TreeItem<String> child;
        TreeItem<String> grandchild;
        root.setExpanded(true);

        Elements regions = doc.select("div[class=\"panel panel-default\"]");
        for (Element r : regions) {
            Region reg = new Region(r, this.doc);
            this.regionList.put(r.selectFirst("a[class=\"accordion-toggle\"]").text(), reg);
            child = new TreeItem<>(reg.getName());
            root.getChildren().add(child);

            Elements studyList = r.select("div[class=\"study\"]");
            for (Element s : studyList) {
                Study study = new Study(s, this.doc);
                reg.addStudy(study);
                grandchild = new TreeItem<>(study.getName());
                child.getChildren().add(grandchild);
                for (String detail : getDetails(study.getName(), reg.getName())) {
                    grandchild.getChildren().add(new TreeItem<String>(detail));
                }
            }
        }

        tree = new TreeView<>(root);
    }

    TreeView<String> getTreeView() {
        return this.tree;
    }

    String getHtml() {
        fixCollapseIDs();
        return this.doc.outerHtml();
    }

    Region getRegion(String name) {
        return regionList.get(name);
    }

    void addRegion(String regionName) {
        String html = "<div class=\"panel panel-default\"><div class=\"panel-heading\"><h4 class=\"panel-title\"><a class=\"accordion-toggle\" data-toggle=\"collapse\" data-parent=\"#accordion\" href=\"#collapse\">" + regionName + "</a></h4></div><div id=\"collapse\" class=\"panel-collapse collapse\"><div class=\"panel-body\"><dl></dl></div></div></div>";
        Element newRegion = Jsoup.parseBodyFragment(html, "UTF-8").selectFirst("div[class=\"panel panel-default\"]");
        this.doc.selectFirst("div[id=\"accordion\"]").appendChild(newRegion);
        regionList.put(regionName, new Region(newRegion, this.doc));
    }


    void moveRegionUp(Region r) {
        Element currEl = this.doc.selectFirst("div.panel.panel-default:contains(" + r.getName() + ")");
        Element lastEl = currEl.previousElementSibling();
        if (lastEl != null) {
            lastEl.before(currEl);
        }
    }

    void moveRegionDown(Region r) {
        Element currEl = this.doc.selectFirst("div.panel.panel-default:contains(" + r.getName() + ")");
        Element nextEl = currEl.nextElementSibling();
        if (nextEl != null) {
            nextEl.after(currEl);
        }
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

    private List<String> getDetails(String studyName, String regionName) {
        List<String> details = new ArrayList<>();
        details.add(this.regionList.get(regionName).getStudy(studyName).getWhenAndWhere());
        for (String leader : this.regionList.get(regionName).getStudy(studyName).getLeaders()) {
            details.add(leader);
        }

        return details;
    }

    private void fixCollapseIDs() {
        // this will be called right before a new file is generated
        int i = 1;
        for (Element e : this.doc.select("div.panel.panel-default")) {
            e.selectFirst("a[class=\"accordion-toggle\"]").attr("href", ("#collapse" + i));
            e.selectFirst("div.panel-collapse.collapse").attr("id", ("collapse" + i));
            i++;
        }
    }
}
