import java.util.ArrayList;

public class Tank {
  //определяем положение танка по градусам - текущее, предыдущее, следующее
  int degrees_current = 0, degrees_previous = 0, degrees_next = 0;
  public Coord tm = new Coord();
  //Текущая позиция танка
  public int column, row; //Максимальное количество столбцов и рядов
  public int columns, rows;
  //Шаг клетки
  public int step;
  //Направление танка
  Direction pos_tm;
  //Копия нашего лабиринта для одного танка
  Maze maze;
  int stak = 0;
  //проверка изменения направления - он обнуляется
  boolean stak_pos = true;
  //счетчик выполнения выбора направления
  int s_step = 0;
  //Выбор направления если ячейка занята
  private ArrayList<Direction> direct = new ArrayList<>();
  public int speed_StepTank = 5;//шаг скорости игры
  public int speed_stepRotate = 2;//шаг скорости поворота танка
  public int number;
  public int number_Tank;

  //История движения танка
  public ArrayList<LabPos> coordPos = new ArrayList<>();

  public Tank(Maze maze, int column, int row, int step, int columns, int rows, int number, int number_Tank) {
    this.maze = maze;
    //передаем значения лабиринта
    this.columns = columns;
    this.rows = rows;
    this.column = column;
    this.row = row;
    this.step = step;
    pos_tm = Direction.TOP;
    this.number = number;
    this.number_Tank=number_Tank;
    tm.x = column * step;
    tm.y = row * step;
//записываем координаты нашего танка - 3 точки и 0-вая позиция постоянно будет уходить
    for (int i = 0; i < 3; i++) coordPos.add(new LabPos(column, row));
  }

  public void direction(Direction x, Direction y, Direction z) {
    if (ex_pos()) {
      direct.clear();
      direct.add(x);
      direct.add(y);
      direct.add(z);
      Direction coord = direct.get((int) (Math.random() * 3));
      sel_Direct(coord);
    } else closeMove();
  }

  public void direction(Direction x, Direction y) {
    //проверяем есть ли вообще путь
    if (ex_pos()) {
      direct.clear();
      direct.add(x);
      direct.add(y);
      Direction coord = direct.get((int) (Math.random() * 2));
      sel_Direct(coord);
    } else closeMove();
  }

  public boolean ex_pos() {
    boolean ex = false;
    if (row < rows - 1)
      if (maze.getMap()[row + 1][column] == 0 || maze.getMap()[row + 1][column] == number)
        ex = true;
    if (column < columns - 1)
      if (maze.getMap()[row][column + 1] == 0 || maze.getMap()[row][column + 1] == number)
        ex = true;
    if (column > 0)
      if (maze.getMap()[row][column - 1] == 0 || maze.getMap()[row][column - 1] == number)
        ex = true;
    if (row > 0)
      if (maze.getMap()[row - 1][column] == 0 || maze.getMap()[row - 1][column] == number)
        ex = true;
    return ex;
  }

  //рекурсивный метод определяющий направление движения
  public void sel_Direct(Direction x) {
    if (stak_pos) {
      switch (x) {
        case DOWN:
          if (row < rows - 1) {
            if (maze.getMap()[row + 1][column] == 0 || maze.getMap()[row + 1][column] == number) {
              degrees_previous = degrees_current;
              degrees_next = 180;
              pos_tm = Direction.DOWN;
              addCoordPos(column, row);
              row++;
              addCoordPos(column, row);
              stak = 1;
              stak_pos = false;
              s_step = 0;
            } else direction(Direction.LEFT, Direction.RIGHT);
          } else direction(Direction.LEFT, Direction.TOP, Direction.RIGHT);
          break;
        case RIGHT:
          if (column < columns - 1) {
            if (maze.getMap()[row][column + 1] == 0 || maze.getMap()[row][column + 1] == number) {
              degrees_previous = degrees_current;
              degrees_next = 90;
              pos_tm = Direction.RIGHT;
              addCoordPos(column, row);
              column++;
              addCoordPos(column, row);
              stak = 1;
              stak_pos = false;
              s_step = 0;
            } else direction(Direction.DOWN, Direction.TOP);
          } else direction(Direction.DOWN, Direction.TOP, Direction.LEFT);
          break;
        case LEFT:
          if (column > 0) {
            if (maze.getMap()[row][column - 1] == 0 || maze.getMap()[row][column - 1] == number) {
              degrees_previous = degrees_current;
              degrees_next = 270;
              pos_tm = Direction.LEFT;
              addCoordPos(column, row);
              column--;
              addCoordPos(column, row);
              stak = 1;
              stak_pos = false;
              s_step = 0;
            } else direction(Direction.DOWN, Direction.TOP);
          } else direction(Direction.DOWN, Direction.TOP, Direction.RIGHT);
          break;
        case TOP:
          if (row > 0) {
            if (maze.getMap()[row - 1][column] == 0 || maze.getMap()[row - 1][column] == number) {
              degrees_previous = degrees_current;
              degrees_next = 360;
              pos_tm = Direction.TOP;
              addCoordPos(column, row);
              row--;
              addCoordPos(column, row);
              stak = 1;
              stak_pos = false;
              s_step = 0;
            } else direction(Direction.LEFT, Direction.RIGHT);
          } else direction(Direction.DOWN, Direction.LEFT, Direction.RIGHT);
          break;
      }
    }
  }

  public void closeMove() {
    //если некуда ехать
    if (!ex_pos() && degrees_current == degrees_next) {
      switch (pos_tm) {
        case TOP:
          degrees_previous = degrees_current;
          degrees_next = 90;
          pos_tm = Direction.RIGHT;
          stak = 1;
          break;
        case DOWN:
          degrees_previous = degrees_current;
          degrees_next = 270;
          pos_tm = Direction.LEFT;
          stak = 1;
          break;
        case LEFT:
          degrees_previous = degrees_current;
          degrees_next = 360;
          pos_tm = Direction.TOP;
          stak = 1;
          break;
        case RIGHT:
          degrees_previous = degrees_current;
          degrees_next = 180;
          pos_tm = Direction.DOWN;
          stak = 1;
          break;
      }
    }
  }

  //поварачиваем если есть необходимость
  public void tm_rotate() {
    if (degrees_current != degrees_next) {
      if (degrees_previous == 0) degrees_previous = 360;
      if (degrees_current == 0) degrees_current = 360;
      switch (pos_tm) {
        case TOP:
          if (degrees_next == 360 && degrees_current != 360 && degrees_current != 0) {
            if (degrees_previous == 270) degrees_current += speed_stepRotate;
            if (degrees_previous == 180 || degrees_previous == 90) degrees_current -= speed_stepRotate;
          }
          break;
        case DOWN:
          if (degrees_next == 180 && degrees_current != 180) {
            if (degrees_previous == 270 || degrees_previous == 360) degrees_current -= speed_stepRotate;
            if (degrees_previous == 90) degrees_current += speed_stepRotate;
          }
          break;
        case LEFT:
          if (degrees_next == 270 && degrees_current != 270) {
            if (degrees_previous == 180 || degrees_previous == 90) degrees_current += speed_stepRotate;
            if (degrees_previous == 360) degrees_current -= speed_stepRotate;
          }
          break;
        case RIGHT:
          if (degrees_next == 90 && degrees_current != 90) {
            if (degrees_current == 360) degrees_current = 0;
            if (degrees_previous == 360) degrees_current += speed_stepRotate;
            if (degrees_previous == 180 || degrees_previous == 270) degrees_current -= speed_stepRotate;
          }
          break;
      }
      if (degrees_current == 0) degrees_current = 360;
      if (degrees_current == degrees_next) {
        stak = 0;
        stak_pos = true;
      } else stak = 1;
    }
  }

  public boolean coincidence() {
    boolean b = false;
    for (int i = 0; i < coordPos.size() - 1; i++) {
      if (coordPos.get(i).column != coordPos.get(i + 1).column) b = true;
      if (coordPos.get(i).row != coordPos.get(i + 1).row) b = true;
    }
    return b;
  }

  //Едем прямо на велечину шага
  public void movement() {
    if (degrees_current == degrees_next && coincidence()) {
      switch (pos_tm) {
        case TOP:
          tm.y -= speed_StepTank;
          s_step += speed_StepTank;
          break;
        case DOWN:
          tm.y += speed_StepTank;
          s_step += speed_StepTank;
          break;
        case LEFT:
          tm.x -= speed_StepTank;
          s_step += speed_StepTank;
          break;
        case RIGHT:
          tm.x += speed_StepTank;
          s_step += speed_StepTank;
          break;
      }
      if (s_step >= step) {
        stak = 0;
        stak_pos = true;
      } else stak = 1;
    }
  }

  public void addCoordPos(int column, int row) {
    maze.map[coordPos.get(0).row][coordPos.get(0).column] = 0;
    maze.map_Tank[coordPos.get(0).row][coordPos.get(0).column] = 0;
    coordPos.remove(0);
    coordPos.add(new LabPos(column, row));
    for (int j = 0; j < coordPos.size(); j++) {
      maze.map[coordPos.get(j).row][coordPos.get(j).column] = number;
      maze.map_Tank[coordPos.get(j).row][coordPos.get(j).column] = number_Tank;
    }
  }

  //Смотрим если перекресток, то рандомно выбираем направление
  public Direction examination(Direction pos) {
    direct.clear();
    int x = 0;
    if (row < rows - 1)
      if (maze.getMap()[row + 1][column] == 0 || maze.getMap()[row + 1][column] == number) {
        direct.add(Direction.DOWN);
        x++;
      }
    if (column < columns - 1)
      if (maze.getMap()[row][column + 1] == 0 || maze.getMap()[row][column + 1] == number) {
        direct.add(Direction.RIGHT);
        x++;
      }
    if (column > 0)
      if (maze.getMap()[row][column - 1] == 0 || maze.getMap()[row][column - 1] == number) {
        direct.add(Direction.LEFT);
        x++;
      }
    if (row > 0)
      if (maze.getMap()[row - 1][column] == 0 || maze.getMap()[row - 1][column] == number) {
        direct.add(Direction.TOP);
        x++;
      }
    if (x >= 3) return direct.get((int) (Math.random() * direct.size()));
    else return pos;
  }

  public void position() {
    if (stak == 0) {
      pos_tm = examination(pos_tm);
      switch (pos_tm) {
        case TOP:
          sel_Direct(Direction.TOP);
          break;
        case DOWN:
          sel_Direct(Direction.DOWN);
          break;
        case LEFT:
          sel_Direct(Direction.LEFT);
          break;
        case RIGHT:
          sel_Direct(Direction.RIGHT);
          break;
      }
    }
    tm_rotate();
    movement();
  }
}
