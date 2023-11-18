import javax.swing.*;
import java.awt.*;

public class Game extends JFrame {
  private static int step = 90;
  private static int column = 21;
  private static int row = 11;
  private static int width = column * step + 20;
  private static int height = row * step;

  public PMenu pMenu = new PMenu();
  public static PGame game = new PGame(column, row, step);
  public Game() {
    setTitle("My");
    setBounds(0, 0, width + 1, height + 68);
    setResizable(false);
    setBackground(Color.white);
    setIconImage(new ImageIcon("Image/tankIcon.png").getImage());
    setLayout(null);
    initPanel();
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setVisible(true);
  }

  private void initPanel() {
    pMenu.setBackground(Color.white);
    pMenu.setBorder(BorderFactory.createEtchedBorder());
    pMenu.setSize(width, 50);
    setJMenuBar(pMenu);
    game.setBounds(1, 1, width, height);
    game.setBackground(Color.white);
    game.setBorder(BorderFactory.createEtchedBorder());
    add(game);
  }

  public static void main(String[] args) {
    new Game();
  }
}