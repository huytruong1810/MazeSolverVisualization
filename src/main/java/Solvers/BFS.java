package Solvers;

import Maze.Maze;
import Maze.MazeCell;

import java.util.ArrayList;

public class BFS extends Solver {

    public BFS(Maze m) {
        super(m);
    }

    @Override
    protected ArrayList<MazeCell> solve() {

        ArrayList<MazeCell> path = new ArrayList<>();
        ArrayList<MazeCell> queue = new ArrayList<>(){{add(maze.getEnter());}};
        solution.put(maze.getEnter(), null);

        while (!queue.isEmpty()) {
            MazeCell cell = queue.remove(0);
            int x = cell.x(), y = cell.y();
            visited[x][y] = true;
            path.add(cell);
            if (cell == maze.getExit()) {
                found = true;
                break;
            }
            for (MazeCell neighbor : maze.getNeighbor(x, y))
                if (!visited[neighbor.x()][neighbor.y()]) {
                    solution.put(neighbor, cell);
                    queue.add(neighbor);
                }
        }

        return path;

    }

}
