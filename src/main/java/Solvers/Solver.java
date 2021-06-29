package Solvers;

import Maze.Maze;
import Maze.MazeCell;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Solver {

    private double solvingTime;
    protected Maze maze;
    protected int W, H;
    protected boolean found;
    protected boolean[][] visited;
    protected HashMap<MazeCell, MazeCell> solution;

    public Solver(Maze m) {

        W = m.getWidth();
        H = m.getHeight();
        maze = m;
        reset();

    }

    public boolean isSolvable() {
        return found;
    }

    public double getTime() {
        return solvingTime;
    }

    public ArrayList<MazeCell> getSolution() {
        ArrayList<MazeCell> solutionPath = new ArrayList<>();
        if (found) {
            MazeCell cur = maze.getExit();
            while (cur != null) {
                solutionPath.add(cur);
                cur = solution.get(cur);
            }
        }
        return solutionPath;
    }

    protected void reset() {
        found = false;
        solution = new HashMap<>();
        visited = new boolean[W][H];
        solvingTime = -1.0;
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
