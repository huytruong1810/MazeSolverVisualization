package Controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Driver extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/maze.fxml"));
        Parent sRoot = loader.load();
        MazeController c = loader.getController();
        c.makeMazeScene(stage);
        Scene s = new Scene(sRoot);
        s.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/maze.css")).toExternalForm());
        stage.setResizable(true);
        stage.setFullScreen(true);
        stage.setScene(s);
        stage.show();

    }

}
