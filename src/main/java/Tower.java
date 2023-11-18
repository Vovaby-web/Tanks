import java.util.ArrayList;

public class Tower {
  //класс определяет положение башни - координаты будут танка
  //определяем положение танка по градусам - текущее, предыдущее, следующее
  public int degrees_current = 0;
  public int degrees_next = 0;

  public int speed_stepRotate;
  Tank tank;


  int change = 0, change_1 = 0, change_bullet = 0;

  Coord mouse = new Coord();

  //пуля
  public ArrayList<Bullet> bullet = new ArrayList<>();

  public Tower(Tank tank) {
    this.tank = tank;
    speed_stepRotate = tank.speed_stepRotate;

    position();
  }

  public boolean if_Definition(int column, int row) {
    if (tank.maze.getMap()[row][column] > 1 &&
        tank.maze.getMap()[row][column] != tank.number &&
        tank.maze.map_Tank[row][column]!=tank.number_Tank)return true;
    else return false;
  }
  //видимость другого танка
  public void visible() {
    int columns = tank.columns;
    int rows = tank.rows;
    int column = tank.column;
    int row = tank.row;
    int s = tank.step;
    boolean n = false;
    //Проверяем каждую линию что там - стена или танк
    for (int i = row; i >= 0; i--) {
      if (tank.maze.getMap()[i][column] == 1) break;
      if (if_Definition(column,i)) {
        pos(column * s + s / 2, i * s + s / 2);
        break;
      }
    }
    for (int i = row; i < rows; i++) {
      if (tank.maze.getMap()[i][column] == 1) break;
      if (if_Definition(column,i)) {
        pos(column * s + s / 2, i * s + s / 2);
        break;
      }
    }
    for (int j = column; j >= 0; j--) {
      if (tank.maze.getMap()[row][j] == 1) break;
      if (if_Definition(j, row)) {
        pos(j * s + s / 2, row * s + s / 2);
        break;
      }
    }
    for (int j = column; j < columns; j++) {
      if (tank.maze.getMap()[row][j] == 1) break;
      if (if_Definition(j, row)) {
        pos(j * s + s / 2, row * s + s / 2);
        break;
      }
    }

  }

  public void pos(int x, int y) {
    mouse.x = x;
    mouse.y = y;
    change = 1;
    change_1 = 1;
  }

  public void position() {
    visible();
    tm_rotate();
  }

  public void tm_rotate() {
    if (change == 1) {
      if (change_1 == 1) {
        //Нам каждую миллисекунду стрелять не надо, поэтому будем стрелять каждые 50мл.сек
        if (Game.game.timer_s % (Game.game.timer.getDelay() * 2) == 0) change_bullet = 1;
        calculation();
        change_1 = 0;
      }
    } else {
      if (change_1 == 1) {
        //баню будем поворачивать куда и сам танк
        degrees_next = tank.degrees_next;
        change_1 = 0;
      }
    }
    rotate();

    if (bullet.size() > 0)
      for (int i = 0; i < bullet.size(); i++) {
        bullet.get(i).result();
        if (bullet.get(i).end == 1) bullet.remove(i);
      }
  }

  private void calculation() {
    // Вычисление угла между двумя точками
    double angle = Math.atan2((mouse.y - tank.tm.y), (mouse.x - tank.tm.x));
    // Конвертация радиан в градусы
    double degreeAngle = Math.toDegrees(angle) + 90;
    if (degreeAngle < 0) degreeAngle = 360 + degreeAngle;
    degrees_next = (int) degreeAngle;
  }

  //Полет пули начинается с места выстрела дула
  //У нас есть градусы где находится дуло
  public Coord bulletRotate() {
    Coord s = new Coord();
    int step = tank.step;
    double degreeB = 360 - (degrees_current - 90);
    if (degreeB >= 360) degreeB -= 360;
    s.x = (int) (step / 2 + Math.round((step / 2) * (Math.cos(Math.toRadians(degreeB)))));
    s.y = (int) (step / 2 - Math.round((step / 2) * (Math.sin(Math.toRadians(degreeB)))));
    return s;
  }

  public void rotate() {
    if (degrees_next == degrees_current) {
      if (change_bullet == 1) {
        bullet.add(new Bullet(tank.maze.map, tank.number, tank.tm.x + bulletRotate().x,
            tank.tm.y + bulletRotate().y, mouse.x, mouse.y));
        change_bullet = 0;
      }
      change = 0;
      change_1 = 1;
    } else {
      //если башню надо поворачивать, найдем крадчайший путь поворота и повернем башню
      if (degrees_next > degrees_current) {
        if ((360 - degrees_next + degrees_current) == (degrees_next - degrees_current)) {
          degrees_current = tank.degrees_current;
        } else if ((360 - degrees_next + degrees_current) < (degrees_next - degrees_current)) {
          degrees_current -= speed_stepRotate;
          if (degrees_current < 0) degrees_current = 360;
        } else degrees_current += speed_stepRotate;
      } else {
        if ((360 - degrees_current + degrees_next) == (degrees_current - degrees_next)) {
          degrees_current = tank.degrees_current;
        } else if ((360 - degrees_current + degrees_next) < (degrees_current - degrees_next)) {
          degrees_current += speed_stepRotate;
          if (degrees_current > 360) degrees_current = 0;
        } else degrees_current -= speed_stepRotate;
      }
    }
    if (Math.abs(degrees_next - degrees_current) < speed_stepRotate) degrees_current = degrees_next;
  }
}
