package Controllers;

import Maze.Maze;
import Maze.MazeCell;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

import java.util.ArrayList;

public class Utils {

    public static void drawMaze(AnchorPane rootPane, Maze maze, int D) {
        rootPane.getChildren().clear();
        ArrayList<Node> nodes = new ArrayList<>();
        for (int i = 0; i < maze.getHeight(); ++i) {
            for (int j = 0; j < maze.getWidth(); ++j) {
                MazeCell cell = maze.getCells()[j][i];
                if (cell.get('N')) nodes.add(new Line(i * D, j * D, i * D + D, j * D));
                if (cell.get('S')) nodes.add(new Line(i * D, j * D + D, i * D + D, j * D + D));
                if (cell.get('W')) nodes.add(new Line(i * D, j * D, i * D, j * D + D));
                if (cell.get('E')) nodes.add(new Line(i * D + D, j * D, i * D + D, j * D + D));
            }
        }
        MazeCell mazeEnter = maze.getEnter(), mazeExit = maze.getExit();
        nodes.add(getTermShape(maze.getWidth(), maze.getHeight(), mazeEnter.x(), mazeEnter.y(), D, true, Color.GREEN));
        nodes.add(getTermShape(maze.getWidth(), maze.getHeight(), mazeExit.x(), mazeExit.y(), D, false, Color.RED));
        rootPane.getChildren().addAll(nodes);
    }

    private static Shape getTermShape(int W, int H, int x, int y, int D, boolean entry, Paint color) {
        Shape shape;
        double y1 = y * D + D / 8.0, y2 = y * D + 7 * D / 8.0, y3 = y * D + D / 2.0;
        double x1 = x * D + D / 8.0, x2 = x * D + 7 * D / 8.0, x3 = x * D + D / 2.0;
        if (x == 0) {
            if (entry) shape = new Polygon(y1, 0, y2, 0, y3, D);
            else shape = new Polygon(y1, D, y2, D, y3, 0);
        }
        else if (x == W - 1) {
            if (entry) shape = new Polygon(y1, x * D + D, y2, x * D + D, y3, x * D);
            else shape = new Polygon(y1, x * D, y2, x * D, y3, x * D + D);
        }
        else if (y == 0) {
            if (entry) shape = new Polygon(0, x1, 0, x2, D, x3);
            else shape = new Polygon(D, x1, D, x2, 0, x3);
        }
        else if (y == H - 1) {
            if (entry) shape = new Polygon(y * D + D, x1, y * D + D, x2, y * D, x3);
            else shape = new Polygon(y * D, x1, y * D, x2, y * D + D, x3);
        }
        else shape = new Circle(y3, x3, D / 4.0);
        shape.setFill(color);
        return shape;
    }

}
