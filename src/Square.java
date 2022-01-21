import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

public class Square extends Rectangle{
  private boolean used = false;
  private boolean circle = false;
  private Color color;
  private static int size = 20;
  public int x;
  public int y;

  public Square(int pX, int pY) {
    super(pX*20, pY*20, size, size);
    x = pX*20;
    y = pY*20;
  }

  // draw square
  public void draw(Graphics g, Point mouseLoc) {
    if (contains(mouseLoc)) {
      g.setColor(Color.GRAY);
    } else {
      g.setColor(Color.WHITE);
    }
    g.drawRect(x, y, size, size);;
    if (used)
      drawXO(g);
  }

  public boolean isO() {
    return used&circle;
  }

  public boolean isX() {
    return used&!circle;
  }
  
  // set square to X or O
  public void set(boolean o, Color color) {
    used = true;
    if (o) {
      circle = true;
    }
    else {
      circle = false;
    }
  }

  // draw X or O
  private void drawXO(Graphics g) {
    if (circle) {
      g.setColor(color);
      g.drawOval(x, y, size-2, height-2);
    }
    else {
      g.setColor(color);
      g.drawLine(x, y, x+size-2, y+size-2);
      g.drawLine(x+size-2, y, x, y+size-2);
    }
  }

}
