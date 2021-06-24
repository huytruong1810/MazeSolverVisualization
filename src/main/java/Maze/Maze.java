package Maze;

import java.util.ArrayList;

public class Maze {

    private final int W, H;
    private final MazeCell enter, exit;
    private final MazeCell[][] cells;

    public Maze(int w, int h, int[] en, int[] ex) {

        W = w;
        H = h;
        cells = new MazeCell[w][h];
        for (int i = 0; i < w; ++i) for (int j = 0; j < h; ++j) cells[i][j] = new MazeCell(i, j);

        int enterx = en[0], entery = en[1];
        enter = cells[enterx][entery];
        if (enterx == 0) enter.tear('N');
        else if (enterx == w - 1) enter.tear('S');
        if (entery == 0) enter.tear('W');
        else if (entery == h - 1) enter.tear('E');

        int exitx = ex[0], exity = ex[1];
        exit = cells[exitx][exity];
        if (exitx == 0) exit.tear('N');
        else if (exitx == w - 1) exit.tear('S');
        if (exity == 0) exit.tear('W');
        else if (exity == h - 1) exit.tear('E');

    }

    public void DFSPunch(int startx, int starty) {
        boolean[][] visited = new boolean[W][H];
        for (int i = 0; i < W; ++i) for (int j = 0; j < H; ++j) visited[i][j] = false;
        _punch(startx, starty, visited);
    }

    private void _punch(int x, int y, boolean[][] visited) {
        visited[x][y] = true;
        boolean[] deadEnd = new boolean[]{false, false, false, false};
        while (!deadEnd[0] || !deadEnd[1] || !deadEnd[2] || !deadEnd[3]) {
            switch ((int) (Math.random() * 4)) {
                case 0 -> { // punch north
                    if (x == 0 || visited[x - 1][y]) {
                        deadEnd[0] = true;
                        continue;
                    }
                    cells[x][y].tear('N');
                    cells[x - 1][y].tear('S');
                    _punch(x - 1, y, visited);
                }
                case 1 -> { // punch south
                    if (x == W - 1 || visited[x + 1][y]) {
                        deadEnd[1] = true;
                        continue;
                    }
                    cells[x][y].tear('S');
                    cells[x + 1][y].tear('N');
                    _punch(x + 1, y, visited);
                }
                case 2 -> { // punch west
                    if (y == 0 || visited[x][y - 1]) {
                        deadEnd[2] = true;
                        continue;
                    }
                    cells[x][y].tear('W');
                    cells[x][y - 1].tear('E');
                    _punch(x, y - 1, visited);
                }
                case 3 -> { // punch east
                    if (y == H - 1 || visited[x][y + 1]) {
                        deadEnd[3] = true;
                        continue;
                    }
                    cells[x][y].tear('E');
                    cells[x][y + 1].tear('W');
                    _punch(x, y + 1, visited);
                }
                default -> throw new IllegalStateException("Random fails.");
            }
        }
    }

    public void randomPunch(int times, int tolerance) {

        if (tolerance < 1 || tolerance > 4) throw new IllegalStateException("Invalid tolerance value.");
        int[][] visited = new int[W][H];
        for (int i = 0; i < W; ++i) for (int j = 0; j < H; ++j) visited[i][j] = tolerance;
        for (int t = 0; t < times; ++t) {
            if (openMaze()) break;
            int randx = (int)(Math.random() * W);
            int randy = (int)(Math.random() * H);
            while (visited[randx][randy] <= 0 || cells[randx][randy].open()) {
                randx = (int)(Math.random() * W);
                randy = (int)(Math.random() * H);
            }
            visited[randx][randy]--;
            boolean searching = true;
            while (searching) {
                switch ((int) (Math.random() * 4)) {
                    case 0:
                        if (cells[randx][randy].get('N')) {
                            cells[randx][randy].tear('N');
                            searching = false;
                        } else continue;
                        break;
                    case 1:
                        if (cells[randx][randy].get('E')) {
                            cells[randx][randy].tear('E');
                            searching = false;
                        } else continue;
                        break;
                    case 2:
                        if (cells[randx][randy].get('S')) {
                            cells[randx][randy].tear('S');
                            searching = false;
                        } else continue;
                        break;
                    case 3:
                        if (cells[randx][randy].get('W')) {
                            cells[randx][randy].tear('W');
                            searching = false;
                        } else continue;
                        break;
                    default: throw new IllegalStateException("Random fails.");
                }
            }
        }

    }

    private boolean passable(int x, int y, char dir) {
        MazeCell cell = cells[x][y];
        return switch (dir) { // wall checks
            case 'N' -> !cell.get('N') && !cells[x - 1][y].get('S');
            case 'S' -> !cell.get('S') && !cells[x + 1][y].get('N');
            case 'W' -> !cell.get('W') && !cells[x][y - 1].get('E');
            case 'E' -> !cell.get('E') && !cells[x][y + 1].get('W');
            default -> throw new IllegalStateException("Invalid direction.");
        };
    }

    public ArrayList<MazeCell> getNeighbor(int x, int y) {

        ArrayList<MazeCell> neighbors = new ArrayList<>();
        if (x == 0) { // border checks
            if (y == 0) {
                if (passable(x, y, 'E')) neighbors.add(cells[x][y + 1]); // 1 right
            }
            else if (y == H - 1) {
                if (passable(x, y, 'W')) neighbors.add(cells[x][y - 1]); // 1 left
            }
            else {
                if (passable(x, y, 'E')) neighbors.add(cells[x][y + 1]); // 1 right
                if (passable(x, y, 'W')) neighbors.add(cells[x][y - 1]); // 1 left
            }
            if (passable(x, y, 'S')) neighbors.add(cells[x + 1][y]); // 1 down
        }
        else if (x == W - 1) {
            if (y == 0) {
                if (passable(x, y, 'E')) neighbors.add(cells[x][y + 1]); // 1 right
            }
            else if (y == H - 1) {
                if (passable(x, y, 'W')) neighbors.add(cells[x][y - 1]); // 1 left
            }
            else {
                if (passable(x, y, 'W')) neighbors.add(cells[x][y - 1]); // 1 left
                if (passable(x, y, 'E')) neighbors.add(cells[x][y + 1]); // 1 right
            }
            if (passable(x, y, 'N')) neighbors.add(cells[x - 1][y]); // 1 up
        }
        else {
            if (y == 0) {
                if (passable(x, y, 'E')) neighbors.add(cells[x][y + 1]); // 1 right
            }
            else if (y == H - 1) {
                if (passable(x, y, 'W')) neighbors.add(cells[x][y - 1]); // 1 left
            }
            else {
                if (passable(x, y, 'E')) neighbors.add(cells[x][y + 1]); // 1 right
                if (passable(x, y, 'W')) neighbors.add(cells[x][y - 1]); // 1 left
            }
            if (passable(x, y, 'N')) neighbors.add(cells[x - 1][y]); // 1 up
            if (passable(x, y, 'S')) neighbors.add(cells[x + 1][y]); // 1 down
        }
        return neighbors;

    }

    private boolean openMaze() {
        for (int i = 0; i < W; ++i) {
            for (int j = 0; j < H; ++j) {
                if (!cells[i][j].open()) return false;
            }
        }
        return true;
    }

    public MazeCell[][] getCells() {
        return cells;
    }

    public MazeCell getEnter() {
        return enter;
    }

    public MazeCell getExit() {
        return exit;
    }

    public int getWidth() {
        return W;
    }

    public int getHeight() {
        return H;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (MazeCell[] row : cells) {
            for (MazeCell cell : row) {
                str.append(cell);
            }
            str.append("\n");
        }
        return str.toString();
    }

}
