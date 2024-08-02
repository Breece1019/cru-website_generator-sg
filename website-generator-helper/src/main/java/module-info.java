module com.cru.websitegeneratorhelper {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires org.jsoup;

    opens com.cru.websitegeneratorhelper to javafx.fxml;
    exports com.cru.websitegeneratorhelper;
}
