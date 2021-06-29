package Solvers;

import Maze.Maze;
import Maze.MazeCell;

import java.util.ArrayList;

public class DFS extends Solver {

    public DFS(Maze maze) {
        super(maze);
    }

    @Override
    protected ArrayList<MazeCell> solve() {

        ArrayList<MazeCell> path = new ArrayList<>();
        solution.put(maze.getEnter(), null);
        step(maze.getEnter(), path);

        return path;
    }

    private void step(MazeCell cell, ArrayList<MazeCell> path) {

        int x = cell.x(), y = cell.y();
        visited[x][y] = true;
        path.add(cell);
        if (cell == maze.getExit()) {
            found = true;
            return;
        }
        for (MazeCell neighbor : maze.getNeighbor(x, y)) {
            if (!visited[neighbor.x()][neighbor.y()]) {
                if (!found) {
                    solution.put(neighbor, cell);
                    step(neighbor, path);
                }
                else break;
            }
        }

    }

}
