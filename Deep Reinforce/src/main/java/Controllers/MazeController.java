package Controllers;

import Maze.Maze;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import static Controllers.Utils.drawMaze;

public class MazeController implements Initializable {

    private Stage primaryStage;
    private int D; // tile size
    private Maze maze;

    @FXML private AnchorPane rootPane;
    @FXML private Button newButton, DFSButton, randomButton, pipeButton;
    @FXML private TextField cellSizeText, enterText, exitText;
    @FXML private TextField widthText, heightText;
    @FXML private TextField startXText, startYText;
    @FXML private TextField timesText, toleranceText;
    @Override public void initialize(URL url, ResourceBundle resourceBundle) { }

    public void closeWindow() {
        primaryStage.close();
    }

    private int getInt(int pos, String str) {
        int index = 0; // index at correct segment of string
        for (int i = 0; i < pos; ++i) {
            while (str.charAt(index) != ',') index++;
            index++; // starts at character after ,
        }
        StringBuilder result = new StringBuilder();
        for (int i = index; i < str.length(); ++i) {
            char ch = str.charAt(i);
            if (ch == ',' || ch == '\n') break;
            result.append(str.charAt(i));
        }
        return Integer.parseInt(result.toString());
    }

    private int[] getLocation(String str, int w, int h) {
        return switch (str) {
            case "rd" -> new int[]{(int) (Math.random() * w), (int) (Math.random() * h)};
            case "c1" -> new int[]{0, 0}; // top left
            case "c2" -> new int[]{0, h - 1}; // top right
            case "c3" -> new int[]{w - 1, 0}; // bottom left
            case "c4" -> new int[]{w - 1, h - 1}; // bottom right
            default -> new int[]{getInt(0, str), getInt(1, str)};
        };
    }

    public void newMaze() {

        D = Integer.parseInt(cellSizeText.getText());

        int width = Integer.parseInt(widthText.getText());
        int height = Integer.parseInt(heightText.getText());

        int[] mazeEnter = getLocation(enterText.getText(), width, height);
        int[] mazeExit = getLocation(exitText.getText(), width, height);

        maze = new Maze(width, height, mazeEnter, mazeExit);
        DFSButton.setVisible(true);
        randomButton.setVisible(true);
        pipeButton.setVisible(true);
        drawMaze(rootPane, maze, D);

    }

    public void DFSPunch() {
        maze.DFSPunch(Integer.parseInt(startXText.getText()), Integer.parseInt(startYText.getText()));
        DFSButton.setVisible(false);
        drawMaze(rootPane, maze, D);
    }

    public void randomPunch() {
        maze.randomPunch(Integer.parseInt(timesText.getText()), Integer.parseInt(toleranceText.getText()));
        randomButton.setVisible(false);
        drawMaze(rootPane, maze, D);
    }

    public void pipeToSolvers() throws IOException {

        Stage solverWindow = new Stage();
        solverWindow.setTitle("Solver");
        solverWindow.initOwner(primaryStage);
        solverWindow.initModality(Modality.WINDOW_MODAL);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/solver.fxml"));
        Parent sRoot = loader.load();
        SolverController c = loader.getController();
        c.makeSolverScene(solverWindow, maze, D);
        Scene s = new Scene(sRoot);
        s.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/solver.css")).toExternalForm());
        solverWindow.setResizable(true);
        solverWindow.setFullScreen(true);
        solverWindow.setScene(s);
        solverWindow.show();

    }

    public void makeMazeScene(Stage stage) {

        primaryStage = stage;
        newButton.setVisible(true);
        DFSButton.setVisible(false);
        randomButton.setVisible(false);
        pipeButton.setVisible(false);

    }

}
