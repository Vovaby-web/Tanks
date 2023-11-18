import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class PGame extends JPanel {
  public int columns;
  public int rows;
  public int step;
  private Maze maze;
  //Наши танки
  private ArrayList<Tank> tank = new ArrayList<>();
  //Положение башни
  public ArrayList<Tower> tower = new ArrayList<>();
  public Timer timer;
  public ArrayList<Integer> number = new ArrayList<>();

  public int timer_s = 0;

  public int number_Tank;

  public PGame(int columns, int rows, int step) {
    this.columns = columns;
    this.rows = rows;
    this.step = step;
    maze = new Maze(columns, rows);
    init();
    time();
  }

  public void init() {
    number.clear();
    tank.clear();
    tower.clear();
    generation_Maze();
    timer_s = 0;
    for (int i = 0; i < 4; i++) initTank();
  }

  private void generation_Maze() {
    maze.init();
    maze.bild();
    repaint();
  }

  public void time() {
    timer = new Timer(25, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (timer_s <= 32000) timer_s++;
        for (int i = 0; i < tank.size(); i++) {
          tank.get(i).position();
          tower.get(i).position();
        }
        int num = 0;
        if (number.size() > 0)
          for (int k = 0; k < number.size(); k++) {
            if (tower.get(k).bullet.size() > 0)
              for (Bullet bul : tower.get(k).bullet)
                if (bul.number_tank > 1) {
                  num = bul.number_tank;
                  break;
                }
          }
        for (int k = 0; k < number.size(); k++) {
          if (number.get(k) == num) {
            tank.remove(k);
            tower.remove(k);
            number.remove(k);
            for (int i = 0; i < rows; i++)
              for (int j = 0; j < columns; j++)
                if (maze.map[i][j] == num)
                  maze.map[i][j] = 0;
          }
        }
        repaint();
        if (number.size()>0)PMenu.label.setText("Количество танков = " + number.size() + " ");
      }
    });
  }

  public void initTank() {
    for (int k = 0; k < 4; k++) {
      LabPos coord=new LabPos();
      for (int i = 0; i < rows; i++) {
        if (maze.map[i][0] == 0) {
          coord.column = 0;
          coord.row = i;
          break;
        }
        if (maze.map[i][columns - 1] == 0) {
          coord.column = columns - 1;
          coord.row = i;
          break;
        }
      }
      for (int i = 0; i < columns; i++) {
        if (maze.map[0][i] == 0) {
          coord.column = i;
          coord.row = 0;
          break;
        }
        if (maze.map[rows - 1][i] == 0) {
          coord.column = i;
          coord.row = rows - 1;
          break;
        }
      }
      // У каждого танка свой номер - в матрице своя цифра.
      if (ex_pos(coord.column, coord.row)) {
        if (number.size() > 0) number.add(number.get(number.size() - 1) + 1);
        else number.add(2);
        int n_Tank = number.get(number.size() - 1);
        number_Tank = (int) (Math.random() * 3+1);
        tank.add(new Tank(maze, coord.column, coord.row, step, columns, rows, n_Tank, number_Tank));
        tower.add(new Tower(tank.get(tank.size() - 1)));
        maze.map[coord.row][coord.column] = n_Tank;
      }
    }
  }


  public boolean ex_pos(int x_column, int y_row) {
    boolean ex = false;
    if (y_row < rows - 1) if (maze.map[y_row + 1][x_column] == 0) ex = true;
    if (x_column < columns - 1) if (maze.map[y_row][x_column + 1] == 0) ex = true;
    if (x_column > 0) if (maze.map[y_row][x_column - 1] == 0) ex = true;
    if (y_row > 0) if (maze.map[y_row - 1][x_column] == 0) ex = true;
    return ex;
  }

  public void game_start() {
    timer.start();
  }

  public void game_stop() {
    timer.stop();
  }

  //След танка

  public void drawing_Labirint(Graphics g) {
    for (int y = 0; y < rows; y++)
      for (int x = 0; x < columns; x++)
        if (maze.getMap()[y][x] == 1) {
          g.setColor(Color.darkGray);
          g.fillRect(x * step, y * step, step, step);
          g.setColor(Color.gray);
          g.drawRect(x * step + 1, y * step + 1, step - 4, step - 4);
          g.setColor(Color.white);
          g.drawRect(x * step + 2, y * step + 2, step - 5, step - 5);
        } else {
          g.setColor(Color.white);
          g.fillRect(x * step, y * step, step, step);
        }
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    drawing_Labirint(g);
    for (int i = 0; i < tank.size(); i++) {
      paint_tank(g, tank.get(i).tm.x, tank.get(i).tm.y, step,
          tank.get(i).degrees_current, "frame");
      paint_tank(g, tank.get(i).tm.x, tank.get(i).tm.y, step,
          tower.get(i).degrees_current, "tower");
      if (tank.get(i).number_Tank==1)
        paint_tank(g, tank.get(i).tm.x, tank.get(i).tm.y, step,0, "1");
      else if (tank.get(i).number_Tank==2)paint_tank(g, tank.get(i).tm.x, tank.get(i).tm.y, step,0, "2");
      else paint_tank(g, tank.get(i).tm.x, tank.get(i).tm.y, step,0, "3");
      if (tower.get(i).bullet.size() > 0) {
        for (Bullet bul : tower.get(i).bullet) {
          g.setColor(Color.black);
          g.fillOval(bul.x, bul.y, 5, 5);
        }
      }
    }

  }

  public void paint_tank(Graphics g, int x, int y, int step, int degrees_current, String name) {
    Graphics2D g2d = (Graphics2D) g;
    AffineTransform old = g2d.getTransform();
    g2d.translate(x + step / 2, y + step / 2);
    g2d.rotate(Math.toRadians(degrees_current));
    g2d.translate(-step * 1 / 2, -step * 1 / 2);
    g2d.drawImage(Game.game.initlmage(name, (int) (step * 1), (int) (step * 1)), 0, 0, null);
    g2d.setTransform(old);
  }

  protected Image initlmage(String name, int x, int y) {
    ImageIcon icon = new ImageIcon("Image/" + name + ".png");
    Image img = icon.getImage().getScaledInstance(x, y, java.awt.Image.SCALE_DEFAULT);
    ImageIcon Image = new ImageIcon(img);
    return Image.getImage();
  }
}

