package Controllers;

import Maze.Maze;
import Maze.MazeCell;
import Solvers.AStar;
import Solvers.BFS;
import Solvers.DFS;
import Solvers.Solver;
import javafx.animation.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static Controllers.Utils.drawMaze;

public class SolverController implements Initializable {

    private int D;
    private Stage primaryStage;
    private Maze maze;
    private double half, oneFourth;
    private SequentialTransition solveAnimation;
    private ArrayList<Shape> explorationShapes, solutionShapes;

    @FXML private AnchorPane rootPane;
    @FXML private Slider speedSlider;
    @FXML private ComboBox<String> solverMenu;
    @FXML private Button overlayButton;
    @FXML private RadioButton explorationRadio, solutionRadio;
    @FXML private Label solvableLabel, timeLabel, cellVisitedLabel;

    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        explorationRadio.setVisible(false);
        solutionRadio.setVisible(false);
        overlayButton.setVisible(false);
        solverMenu.getItems().addAll("DFS", "BFS", "A*");
        solverMenu.getSelectionModel().selectFirst();
    }

    public void closeWindow() {
        primaryStage.close();
    }

    public void applyOverlays() {
        ObservableList<Node> children = rootPane.getChildren();
        if (explorationRadio.isSelected()) {
            for (Shape shape : explorationShapes) if (!children.contains(shape)) children.add(shape);
        }
        else for (Shape shape : explorationShapes) children.remove(shape);
        if (solutionRadio.isSelected()) {
            for (Shape shape : solutionShapes) if (!children.contains(shape)) children.add(shape);
        }
        else for (Shape shape : solutionShapes) children.remove(shape);
    }

    public void activateSolver() {

        if (solveAnimation != null) solveAnimation.stop();

        explorationShapes = new ArrayList<>();
        solutionShapes = new ArrayList<>();

        drawMaze(rootPane, maze, D);

        Solver solver = switch (solverMenu.getValue()) {
            case "DFS" -> new DFS(maze);
            case "BFS" -> new BFS(maze);
            case "A*" -> new AStar(maze);
            default -> throw new IllegalStateException("Invalid solver.");
        };

        ArrayList<MazeCell> path = solver.solveAndTime();

        solvableLabel.setText(" " + solver.isSolvable());
        timeLabel.setText(" " + solver.getTime() + " milliseconds");
        cellVisitedLabel.setText(" " + path.size() + " cells");

        Circle agent = new Circle(maze.getEnter().y() * D + half, maze.getEnter().x() * D + half, oneFourth, Color.ORANGE);
        rootPane.getChildren().add(agent);

        ArrayList<Animation> animations = new ArrayList<>();
        ArrayList<Rectangle> prevRects = new ArrayList<>();
        for (int i = 1, n = path.size(); i < n; ++i) {

            MazeCell from = path.get(i - 1);
            MazeCell to = path.get(i);

            TranslateTransition agentMove = new TranslateTransition(Duration.millis(speedSlider.getValue()), agent);
            agentMove.setByX((to.y() - from.y()) * D);
            agentMove.setByY((to.x() - from.x()) * D);
            animations.add(agentMove);

            Rectangle marker = new Rectangle(to.y() * D + oneFourth, to.x() * D + oneFourth, half, half);
            if (isAJump(from, to)) {

                Rectangle prevRect = prevRects.get(prevRects.size() - 1);
                Rectangle jumpMarker = new Rectangle(prevRect.getX(), prevRect.getY(), prevRect.getWidth(), prevRect.getHeight());
                marker.setFill(Color.GREENYELLOW);
                jumpMarker.setFill(Color.DARKRED);
                prevRects.add(marker);
                explorationShapes.add(jumpMarker);
                explorationShapes.add(marker);
                rootPane.getChildren().addAll(jumpMarker, marker);

                FadeTransition jumpFill = new FadeTransition(Duration.millis(50), jumpMarker);
                FadeTransition landFill = new FadeTransition(Duration.millis(50), marker);
                jumpFill.setFromValue(0);
                jumpFill.setToValue(1);
                landFill.setFromValue(0);
                landFill.setToValue(1);
                animations.add(jumpFill);
                animations.add(landFill);
                continue;

            }
            marker.setFill(Color.ORANGE);
            prevRects.add(marker);
            explorationShapes.add(marker);
            rootPane.getChildren().add(marker);

            FadeTransition tileFill = new FadeTransition(Duration.millis(50), marker);
            tileFill.setFromValue(0);
            tileFill.setToValue(1);
            animations.add(tileFill);

        }
        solveAnimation = new SequentialTransition();
        solveAnimation.getChildren().addAll(animations);
        solveAnimation.setOnFinished(e -> {
            explorationRadio.setVisible(true);
            solutionRadio.setVisible(true);
            overlayButton.setVisible(true);
            explorationRadio.setSelected(true);
            solutionRadio.setSelected(true);
            ArrayList<MazeCell> solutionPath = solver.getSolution();
            for (int n = solutionPath.size(), i = n - 1; i > 0; --i) {
                MazeCell cur = solutionPath.get(i);
                MazeCell prev = solutionPath.get(i - 1);
                Polygon arrow = Utils.getArrow(cur, prev, D);
                arrow.setFill(Color.GREEN);
                solutionShapes.add(arrow);
                rootPane.getChildren().add(arrow);
            }
        });
        solveAnimation.play();

    }

    private boolean isAJump(MazeCell from, MazeCell to) {
        return (Math.abs(from.x() - to.x()) > 1) || (Math.abs(from.y() - to.y()) > 1) ||
                (Math.abs(from.x() - to.x()) == 1 && Math.abs(from.y() - to.y()) == 1);
    }

    public void makeSolverScene(Stage stage, Maze m, int cellSize) {

        D = cellSize;
        half = cellSize / 2.0;
        oneFourth = cellSize / 4.0;
        primaryStage = stage;
        maze = m;
        drawMaze(rootPane, maze, D);

    }

}
