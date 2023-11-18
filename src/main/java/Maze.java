import java.util.ArrayList;
import java.util.Random;

public class Maze {
  public int map[][];
  public int map_Tank[][];


  private int max_x, max_y;
  private ArrayList<Coord> path = new ArrayList<>();
  private ArrayList<Direction> minerDirection = new ArrayList<>();
  private Coord last;
  private int totalStep;
  private int steps = 1;

  public Maze(int max_x, int max_y) {
    map = new int[max_y][];
    map_Tank = new int[max_y][];

    this.max_x = max_x;
    this.max_y = max_y;
    totalStep = max_x * max_y / 4;
    for (int y = 0; y < max_y; y++) {
      map[y] = new int[max_x];
      map_Tank[y] = new int[max_x];
    }
  }

  public void init() {
    path.clear();
    totalStep = max_x * max_y / 4;
    steps = 1;
    for (int y = 0; y < max_y; y++)
      for (int x = 0; x < max_x; x++) map[y][x] = 1;
    map[1][1] = 0;
    path.add(new Coord(1, 1));
  }

  public void bild() {
    while (steps < totalStep) step();
    ex();
  }

  private void ex() {
    Random j = new Random();
    int rnd = 0;
    do {
      rnd = j.nextInt(map.length - 1);
      if (map[rnd][1] == 0) map[rnd][0] = 0;
    } while (map[rnd][1] != 0);
    do {
      rnd = j.nextInt(map.length - 1);
      if (map[rnd][map[0].length - 2] == 0) map[rnd][map[0].length - 1] = 0;
    } while (map[rnd][map[0].length - 2] != 0);
    do {
      rnd = j.nextInt(map[0].length - 1);
      if (map[1][rnd] == 0) map[0][rnd] = 0;
    } while (map[1][rnd] != 0);
    do {
      rnd = j.nextInt(map[0].length - 1);
      if (map[map.length - 2][rnd] == 0) map[map.length - 1][rnd] = 0;
    } while (map[map.length - 2][rnd] != 0);
  }

  private void step() {
    minerDirection.clear();
    last = path.get(path.size() - 1);

    if ((last.x + 2) < map[0].length && map[last.y][last.x + 2] == 1)
      minerDirection.add(Direction.RIGHT);
    if ((last.x - 2) > 0 && map[last.y][last.x - 2] == 1)
      minerDirection.add(Direction.LEFT);
    if ((last.y + 2) < map.length && map[last.y + 2][last.x] == 1)
      minerDirection.add(Direction.DOWN);
    if ((last.y - 2) > 0 && map[last.y - 2][last.x] == 1)
      minerDirection.add(Direction.TOP);

    if (minerDirection.size() > 0) {
      steps++;
      int rnd = (int) (Math.random() * minerDirection.size());
      switch (minerDirection.get(rnd)) {
        case RIGHT:
          map[last.y][last.x + 1] = 0;
          map[last.y][last.x + 2] = 0;
          path.add(new Coord(last.y, last.x + 2));
          break;
        case LEFT:
          map[last.y][last.x - 1] = 0;
          map[last.y][last.x - 2] = 0;
          path.add(new Coord(last.y, last.x - 2));

          break;
        case TOP:
          map[last.y - 1][last.x] = 0;
          map[last.y - 2][last.x] = 0;
          path.add(new Coord(last.y - 2, last.x));
          break;
        case DOWN:
          map[last.y + 1][last.x] = 0;
          map[last.y + 2][last.x] = 0;
          path.add(new Coord(last.y + 2, last.x));
          break;
      }
    } else {
      if (path.size() > 1) path.remove(path.size() - 1);
      else steps = totalStep;
    }
  }

  public ArrayList<Coord> getPath() {
    return path;
  }

  public int[][] getMap() {
    return map;
  }


}
