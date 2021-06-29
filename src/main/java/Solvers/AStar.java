package Solvers;

import Maze.Maze;
import Maze.MazeCell;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class AStar extends Solver {

    public AStar(Maze maze) {
        super(maze);
    }

    class ScoredCell {
        MazeCell cell;
        int depth;
        int score;
        ScoredCell(MazeCell c, int gScore) {
            cell = c;
            MazeCell e = maze.getExit();
            int hScore = Math.abs(c.x() - e.x()) + Math.abs(c.y() - e.y());
            depth = gScore;
            score = gScore + hScore;
        }
    }

    @Override
    protected ArrayList<MazeCell> solve() {

        ArrayList<MazeCell> path = new ArrayList<>();
        PriorityQueue<ScoredCell> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(o -> o.score));
        priorityQueue.add(new ScoredCell(maze.getEnter(), 0));
        solution.put(maze.getEnter(), null);

        while (!priorityQueue.isEmpty()) {
            ScoredCell scoredCell = priorityQueue.poll();
            int x = scoredCell.cell.x(), y = scoredCell.cell.y();
            visited[x][y] = true;
            path.add(scoredCell.cell);
            if (scoredCell.cell == maze.getExit()) {
                found = true;
                break;
            }
            for (MazeCell neighbor : maze.getNeighbor(x, y))
                if (!visited[neighbor.x()][neighbor.y()]) {
                    solution.put(neighbor, scoredCell.cell);
                    priorityQueue.add(new ScoredCell(neighbor, scoredCell.depth + 1));
                }
        }

        return path;

    }

}
