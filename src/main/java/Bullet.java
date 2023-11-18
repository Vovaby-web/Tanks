import java.util.ArrayList;

public class Bullet {
  public ArrayList<Coord> bullet = new ArrayList<>();
  public int x1, y1;
  public int x2, y2;

  public int x, y;

  public int current, length;
  public int end;
  int map[][];
  public int columns = Game.game.columns;
  public int rows = Game.game.rows;

  public int step = Game.game.step;
  public int speedstep = 10;
  public int number;
  //номер танка в который попали
  public int number_tank = 0;

  public Bullet(int map[][], int number, int x_Tank, int y_Tank, int x_Mouse, int y_Mouse) {
    x1 = x_Tank;
    y1 = y_Tank;
    x2 = x_Mouse;
    y2 = y_Mouse;
    this.map = map;
    this.number = number;
    init();
  }

  public void init() {
    end = 0;
    current = 0;
    //|AB|² = (y2 - y1)² + (x2 - x1)² - длина отрезка
    length = (int) Math.sqrt(Math.pow(Math.abs(x2 - x1), 2) + Math.pow(Math.abs(y2 - y1), 2));
    number_tank = 0;
  }

  public void result() {
    if (current < length) {
      //X = X2-X1, Y = Y2-Y1 - Координаты отрезка
      x = x1 + (x2 - x1) * current / length;
      y = y1 + (y2 - y1) * current / length;
      current += speedstep;
      box(x, y);
    } else end = 1;
  }

  //Делаем проверку, чтобы пуля летела до ближайшей стены
  //Если х попал в закрашенный квадрат, то все - выход
  public void box(int x, int y) {
    Coord c_1 = new Coord();
    Coord c_2 = new Coord();
    for (int i = 0; i < rows; i++) {
      c_1.y = i * step - speedstep - 1;
      c_2.y = i * step + step + speedstep + 1;
      for (int j = 0; j < columns; j++) {
        c_1.x = j * step - speedstep - 1;
        c_2.x = j * step + step + speedstep + 1;
        if ((y >= c_1.y && y <= c_2.y) && (x >= c_1.x && x <= c_2.x)) {
          if (map[i][j] == 1) current = length;
          if ((map[i][j] > 1 && map[i][j] != number)) {
            current = length;
            number_tank = map[i][j];
            break;
          }
        }
      }
    }
  }
}

