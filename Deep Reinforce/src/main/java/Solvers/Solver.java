package Solvers;

import Maze.Maze;
import Maze.MazeCell;

import java.util.ArrayList;

public abstract class Solver {

    private double solvingTime;

    protected Maze maze;
    protected int W, H;
    protected boolean found;
    protected boolean[][] visited;

    public Solver(Maze m) {

        W = m.getWidth();
        H = m.getHeight();
        found = false;
        maze = m;
        visited = new boolean[W][H];
        reset();

    }

    public boolean isSolvable() {
        return found;
    }

    public double getTime() {
        return solvingTime;
    }

    protected void reset() {
        for (int i = 0; i < W; ++i) for (int j = 0; j < H; ++j) visited[i][j] = false;
    }

    public ArrayList<MazeCell> solveAndTime() {
        long start = System.nanoTime();
        ArrayList<MazeCell> path = solve();
        solvingTime = (System.nanoTime() - start) / 1_000_000.0;
        return path;
    }

    protected abstract ArrayList<MazeCell> solve();

}
