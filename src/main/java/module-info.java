module MazeSolverVisualization {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    opens Controllers to javafx.graphics, javafx.controls, javafx.fxml;
    exports Controllers;
}