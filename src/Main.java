import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Graphics;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Main extends Thread{
  private final int WIDTH = 509;
  private final int HEIGHT = 339;
  
  private JFrame frame;
  private Painter painter = new Painter();
  private boolean exit = false;
  private Multiplayer test;
 
  public Main() {
    frame = new JFrame();
		frame.setTitle("Main Menu");
		frame.setContentPane(painter);
		frame.setSize(WIDTH, HEIGHT);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(true);
  }

  @Override
  public void run() {
    while (!exit) {
      painter.repaint();
    }
  }

  private void launchMultiplayer() {
    exit = true;
    this.interrupt();
    frame.dispose();
    test = new Multiplayer(painter.getIP(), painter.getPort());
    test.start();
  }

  private class Painter extends JPanel implements ActionListener{
		private static final long serialVersionUID = 1L;

    private final int WIDTH = 509;
    private final int HEIGHT = 339;

    private String ipInfo;
    private int portInfo;

    private BufferedImage background;
    private JButton spButton = new JButton("Single Player");
    private JButton mpButton = new JButton("Multiplayer"); 
    private JTextArea instruction = new JTextArea();
    private JTextField userInputField = new JTextField(30); 

		public Painter() {
			setFocusable(true);
			requestFocus();
      setSize(WIDTH, HEIGHT);
			setBackground(Color.WHITE);
      add(spButton);
      add(mpButton);
      spButton.addActionListener(this);
      mpButton.addActionListener(this);
      try {
        background = ImageIO.read(new File("res/sky_background.png"));
      } catch (IOException e) {
        e.printStackTrace();
        System.err.println("Unable to load background image. Check res/sky_background.png");
        System.exit(1);
      }
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
      g.drawImage(background, 0, 0, this);
		}

    @Override
    public void actionPerformed(ActionEvent e) {
      // Single Player Case
      if (e.getActionCommand().equals("Single Player")) {
        System.out.println("Single Player not available. You got no other choice =)");
      }
      // Multiplayer Case
      else if (e.getActionCommand().equals("Multiplayer")) {
        getIPInfo();
        this.validate();
      } 
      // Get IP info
      else if (userInputField.getName().equals("User IP Input") ) {
        ipInfo = e.getActionCommand();
        getPortInfo();
        this.validate();
      } 
      // Get Port Info
      else if (userInputField.getName().equals("User Port Input")) {
        try {
          portInfo = Integer.parseInt(userInputField.getText());
          if (portInfo>=0 && portInfo <=65535)
            launchMultiplayer();
          else {
            System.err.println("Error: Invalid port input");
            instruction.setText("Invalid port, please try again:");
            userInputField.setText("");
          }
        } catch (NumberFormatException exception) {
          System.err.println("Error: Invalid port input");
          instruction.setText("Invalid port, please try again:");
          userInputField.setText("");
        }
      }
    }

    public String getIP() {
      return ipInfo;
    }

    public int getPort() {
      return portInfo;
    }

    private void getIPInfo() {
      instruction.setText("Please enter your IP address:");
      add(instruction);
      add(userInputField);
      userInputField.setName("User IP Input");
      userInputField.addActionListener(this);
    }

    private void getPortInfo() {
      instruction.setText("Please enter your port:");
      userInputField.setName("User Port Input");
      userInputField.setText("");
    }

	}

  public static void main(String[] args) {
    Main main = new Main();
    main.start();
  }
  
}