module rushhour {
    requires transitive javafx.graphics;
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.media;
    requires java.desktop;

    opens rushhour.view to javafx.fxml;
    exports rushhour.view;
    exports rushhour.model;
    exports backtracker;
}
