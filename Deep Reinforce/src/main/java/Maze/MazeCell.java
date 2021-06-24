package Maze;

public class MazeCell {

    private final int onBoardX, onBoardY;
    private boolean N, W, E, S; // walls

    public MazeCell(int x, int y) {
        onBoardX = x;
        onBoardY = y;
        N = W = E = S = true;
    }

    public int x() { return onBoardX; }
    public int y() { return onBoardY; }

    public boolean open() {
        return !N && !W && !E && !S;
    }

    public boolean get(char dir) {
        return switch (dir) {
            case 'N' -> N;
            case 'W' -> W;
            case 'E' -> E;
            case 'S' -> S;
            default -> throw new IllegalStateException("Unknown direction.");
        };
    }

    public void build(char dir) {
        switch (dir) {
            case 'N' -> N = true;
            case 'W' -> W = true;
            case 'E' -> E = true;
            case 'S' -> S = true;
            default -> throw new IllegalStateException("Unknown direction.");
        }
    }

    public void tear(char dir) {
        switch (dir) {
            case 'N' -> N = false;
            case 'W' -> W = false;
            case 'E' -> E = false;
            case 'S' -> S = false;
            default -> throw new IllegalStateException("Unknown direction.");
        }
    }

    @Override
    public String toString() {
        return "< " + (W ? "|" : " ") + ((N) ? ((S) ? "=" : "-") : ((S) ? "_" : " ")) + (E ? "|" : " ") + "[" + onBoardX + "," + onBoardY + "] >";
    }

}