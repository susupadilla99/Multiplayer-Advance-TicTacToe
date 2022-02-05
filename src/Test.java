import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Test extends Thread{
  private final int WIDTH = 600;
  private final int HEIGHT = 600;
  private final int BOARDWIDTH = WIDTH/20;
  private final int BOARDHEIGHT = HEIGHT/20;

  //graphics components
  private Painter painter;
  private JFrame frame;
  private Board board;
  private Font font = new Font("Verdana", Font.BOLD, 14);
  
  //network components
  private ServerSocket serverSocket;
  private Socket socket;
  private DataOutputStream dos;
  private DataInputStream dis;
  private Scanner scanner = new Scanner(System.in);
  private String ip;
  private int port;

  //logic components
  private enum State {noConnection, connectionAccepted, connectionLost, gameEnded};
  State state = State.noConnection;
  private boolean circle = true;
  private boolean yourTurn = false;
  private boolean won = false;
	private int errors = 0;

  public Test() {
    System.out.println("Please input IP Address:");
    ip = scanner.nextLine();
    System.out.println("Please input port:");
    port = scanner.nextInt();
    while (port < 0 || port > 65535) {
      System.out.println("Invalid port. Please input a valid port:");
      port = scanner.nextInt();
    }

    board = new Board(BOARDWIDTH, BOARDHEIGHT);

    painter = new Painter();
		painter.setPreferredSize(new Dimension(WIDTH, HEIGHT));

    if (!connect()) initializeServer();

		frame = new JFrame();
		frame.setTitle("Tic-Tac-Toe");
		frame.setContentPane(painter);
		frame.setSize(WIDTH, HEIGHT);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(true);
  }

  private boolean connect() {
    try {
      socket = new Socket(ip, port);
      dos = new DataOutputStream(socket.getOutputStream());
      dis = new DataInputStream(socket.getInputStream());
      state = State.connectionAccepted;
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("Unable to connect to the address : " + ip + ":" + port + ". Starting a server ");
      return false;
    }
    System.out.println("Sucessfully connected to server. Let's go!");
    return true;
  }

  public void draw(Graphics g, Point mouseLoc) {
    board.draw(g, mouseLoc);
    drawSituationString(g);
    drawTurn(g);
  }

  @Override
  public void run() {
    while (state!=State.gameEnded) {
      tick();
      painter.repaint();
      if (!circle && state==State.noConnection) {
        listenForServerRequest();
      }
      if (state == State.connectionLost) {
        painter.repaint();
        try {
          Thread.sleep(2000);
        } catch (InterruptedException e) {}
        System.err.println("Connection Lost. Game terminated");
        System.exit(1);
      }
    }
    painter.repaint();
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {}
    System.exit(0);
  }

  private void initializeServer() {
    try {
      serverSocket = new ServerSocket(port, 8, InetAddress.getByName(ip));
    } catch (IOException e) {
      e.printStackTrace();
    }
    yourTurn = true;
    circle = false;
  }

  private void listenForServerRequest() {
    Socket socket = null;
    try {
      socket = serverSocket.accept();
      dos = new DataOutputStream(socket.getOutputStream());
      dis = new DataInputStream(socket.getInputStream());
      state = State.connectionAccepted;
      System.out.println("Opponent found. Let's begin");   
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void tick() {
    if (errors >= 10) state = State.connectionLost;

    if (!yourTurn && state==State.connectionAccepted) {
      try {
        int space = dis.readInt();
        int spaceX = space/BOARDHEIGHT*20;
        int spaceY = space%BOARDHEIGHT*20;
        if (circle) board.set(new Point(spaceX, spaceY), false, Color.RED);
        else board.set(new Point(spaceX, spaceY), true, Color.BLUE);
        if (board.check()) {
          if (board.checkXWin()) System.out.println("X has Won");
          if (board.checkOWin()) System.out.println("O has Won");
          state = State.gameEnded;
        }
        yourTurn = true;
      } catch (IOException e) {
        e.printStackTrace();
        errors++;
      }
    }
  }

  private void drawTurn(Graphics g) {
    String turnString;
    if (yourTurn) turnString = "Your Turn";
    else turnString = "Enemy Turn";
    Graphics2D g2 = (Graphics2D) g;
    int stringWidth = g2.getFontMetrics().stringWidth(turnString);
    int stringHeight = 20;
    int stringX = (WIDTH-stringWidth)/2;
    int stringY = 0;
    int offset = 10;
    g.setColor(Color.WHITE);
    g.fillRect(stringX-(offset/2), stringY, stringWidth+offset, stringHeight);
    g.setColor(Color.BLACK);
    g.drawRect(stringX-(offset/2), stringY, stringWidth+offset, stringHeight);
    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    g.setFont(font);
    g.drawString(turnString, stringX, 15);
  }

  private void drawSituationString(Graphics g) {
    Graphics2D g2 = (Graphics2D) g;
    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    g.setFont(font);
    g.setColor(Color.RED);
    int offset = 5;
    switch (state) {
      case noConnection:
        String noConnectionString = "No connections detected. Waiting for opponent to appear";
        int stringWidth1 = g2.getFontMetrics().stringWidth(noConnectionString);
        g.drawString(noConnectionString, WIDTH/2 - stringWidth1/2, HEIGHT/2 - offset);
        break;
      case connectionLost:
        String connectionLostString = "Connection to opponent lost. Game terminating";
        int stringWidth2 = g2.getFontMetrics().stringWidth(connectionLostString);
        g.drawString(connectionLostString, WIDTH/2 - stringWidth2/2, HEIGHT/2 - offset);
        break;
      case gameEnded:
        String gameEndedString = "";
        if (won) {
          g.setColor(Color.GREEN);
          gameEndedString = "You won";
        }
        else  {
          g.setColor(Color.RED);
          gameEndedString = "You Lost";
        }
        int stringWidth3 = g2.getFontMetrics().stringWidth(gameEndedString);
        g.drawString(gameEndedString, WIDTH/2 - stringWidth3/2, HEIGHT/2 - offset);
        break;
      case connectionAccepted:
        //do nothing
        break;
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
      if (yourTurn && state==State.connectionAccepted) {
        if (board.set(getMousePosition(), circle, Color.BLUE)) {
          int pos = board.getPosition(getMousePosition());
          yourTurn = !yourTurn;
          repaint();
          Toolkit.getDefaultToolkit().sync();
          try {
            dos.writeInt(pos);
            dos.flush();
          } catch (IOException exception) {
            errors++;
            exception.printStackTrace();
          }
      }
    }
    if (board.check()) {
      boolean xWin = board.checkXWin();
      boolean oWin = board.checkOWin();
      if ((xWin && !circle) || (oWin && circle)) {
        won = true;
        System.out.println("You won");
      } else {
        System.out.println("You lost");
      }
      state = State.gameEnded;
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
