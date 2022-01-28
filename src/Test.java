import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Test extends Thread{
  private final int WIDTH = 1080;
  private final int HEIGHT = 720;
  private final int BOARDWIDTH = WIDTH/20;
  private final int BOARDHEIGHT = HEIGHT/20;

  private Painter painter;
  private JFrame frame;
  private Board board;
  private boolean circle;

  public Test() {
    board = new Board(BOARDWIDTH, BOARDHEIGHT);
    circle = false;

    painter = new Painter();
		painter.setPreferredSize(new Dimension(WIDTH, HEIGHT));

		frame = new JFrame();
		frame.setTitle("Tic-Tac-Toe");
		frame.setContentPane(painter);
		frame.setSize(WIDTH, HEIGHT);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(true);
  }

  public void draw(Graphics g, Point mouseLoc) {
    board.draw(g, mouseLoc);
  }

  @Override
  public void run() {
    while (true) {
      painter.repaint();
    }
  }

  public static void main(String[] args) {
    Test test = new Test();
    test.start();
  }

	private class Painter extends JPanel implements MouseListener {
		private static final long serialVersionUID = 1L;

		public Painter() {
			setFocusable(true);
			requestFocus();
			setBackground(Color.WHITE);
			addMouseListener(this);
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
      draw(g, getMousePosition());
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			if (circle) {
        board.set(getMousePosition(), circle, Color.RED);
      } else {
        board.set(getMousePosition(), circle, Color.BLUE);
      }
      circle = !circle;
      if (board.check()) {
        if (board.checkXWin()) System.out.println("Player 1 has Won");
        if (board.checkOWin()) System.out.println("Player 2 has Won");
      }
		}

		@Override
		public void mousePressed(MouseEvent e) {

		}

		@Override
		public void mouseReleased(MouseEvent e) {

		}

		@Override
		public void mouseEntered(MouseEvent e) {

		}

		@Override
		public void mouseExited(MouseEvent e) {

		}

	}
  
}
