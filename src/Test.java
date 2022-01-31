import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Test extends Thread{
  private final int WIDTH = 1080;
  private final int HEIGHT = 720;
  private final int BOARDWIDTH = WIDTH/20;
  private final int BOARDHEIGHT = HEIGHT/20;

  //graphics components
  private Painter painter;
  private JFrame frame;
  private Board board;
  
  //network components
  private ServerSocket serverSocket;
  private Socket socket;
  private DataOutputStream dos;
  private DataInputStream dis;
  private Scanner scanner = new Scanner(System.in);
  private String ip;
  private int port;

  //logic components
  private boolean circle = true;
  private boolean gameFinished = false;
  private boolean yourTurn = false;
	private boolean accepted = false;
	private boolean unableToCommunicateWithOpponent = false;
	private int lengthOfSpace = 160;
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
      accepted = true;
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("Unable to connect to the address : " + ip + ":" + port + ". Starting a server ");
      return false;
    }
    System.out.println("Sucessfully connected to server. Let's go!");
    return true;
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
      accepted = true;
      System.out.println("Opponent found. Let's begin");   
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void tick() {
    if (errors >= 10) unableToCommunicateWithOpponent = true;

    if (!yourTurn && !unableToCommunicateWithOpponent) {
      try {
        int space = dis.readInt();
        if (circle) board.set(new Point(space/BOARDHEIGHT, space%BOARDHEIGHT), false, Color.RED);
        else board.set(new Point(space/BOARDHEIGHT, space%BOARDHEIGHT), true, Color.BLUE);
        if (board.check()) {
          if (board.checkXWin()) System.out.println("X has Won");
          if (board.checkOWin()) System.out.println("O has Won");
          gameFinished = true;
        }
        yourTurn = true;
      } catch (IOException e) {
        e.printStackTrace();
        errors++;
      }
    }
  }

  public void draw(Graphics g, Point mouseLoc) {
    board.draw(g, mouseLoc);
  }

  @Override
  public void run() {
    while (!gameFinished) {
      tick();
      painter.repaint();
      if (!circle && !accepted) {
        listenForServerRequest();
      }
    }
    painter.repaint();
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {}
    System.exit(0);
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
			if (accepted) {
        if (!yourTurn && !unableToCommunicateWithOpponent && !gameFinished) {
          
        }
      }
      if (board.check()) {
        if (board.checkXWin()) System.out.println("X has Won");
        if (board.checkOWin()) System.out.println("O has Won");
        gameFinished = true;
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
