import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class PMenu extends JMenuBar {
  private JMenu menu_M = new JMenu("Menu");
  private JMenu menu_G = new JMenu("Game");
  private JMenuItem gener = new JMenuItem("Generation");
  private JMenuItem gam_start = new JMenuItem("Start");
  private JMenuItem gam_add = new JMenuItem("addTank");
  private JMenuItem gam_stop = new JMenuItem("Stop");
  private JMenuItem exit = new JMenuItem("Exit");

  public static JLabel label = new JLabel();

  public PMenu() {
    setLayout(null);
    init();
    generation();
    exit();
    game();
  }

  private void game() {
    gam_start.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        gener.setEnabled(false);
        gam_start.setEnabled(false);
        gam_stop.setEnabled(true);
        Game.game.game_start();
      }
    });

    gam_stop.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Game.game.game_stop();
        gener.setEnabled(true);
        gam_start.setEnabled(true);
        gam_stop.setEnabled(false);
        gam_add.setEnabled(true);
      }
    });
    gam_add.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Game.game.initTank();
      }
    });
  }

  private void generation() {
    gener.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Game.game.init();
      }
    });
  }

  private void exit() {
    exit.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        System.exit(0);
      }
    });
  }
  private void init() {
    Color color = new Color(5, 24, 37);

    menu_M.setFont(new Font("Consolas", Font.PLAIN, 16));
    menu_M.setBorder(BorderFactory.createEtchedBorder());
    menu_M.setForeground(color);
    gener.setFont(new Font("Consolas", Font.PLAIN, 14));
    exit.setFont(new Font("Consolas", Font.PLAIN, 14));
    menu_M.add(gener);
    menu_M.add(exit);
    add(menu_M);

    menu_G.setFont(new Font("Consolas", Font.PLAIN, 16));
    menu_G.setBorder(BorderFactory.createEtchedBorder());
    menu_G.setForeground(color);
    gam_start.setFont(new Font("Consolas", Font.PLAIN, 14));
    gam_add.setFont(new Font("Consolas", Font.PLAIN, 14));
    gam_add.setAccelerator(KeyStroke.getKeyStroke('C', KeyEvent.CTRL_MASK));
    gam_stop.setFont(new Font("Consolas", Font.PLAIN, 14));
    gam_stop.setEnabled(false);
    menu_G.add(gam_start);
    menu_G.add(gam_add);
    menu_G.add(gam_stop);
    add(menu_G);

    add(Box.createHorizontalGlue());
    label.setBorder(BorderFactory.createEtchedBorder());
    add(label);
  }
}
