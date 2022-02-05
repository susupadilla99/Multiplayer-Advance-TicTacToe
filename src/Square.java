import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

public class Square extends Rectangle{
  private boolean used = false;
  private boolean oValue;
  private boolean xValue;
  private Color color;
  private static int size = 20;
  public int x;
  public int y;

  public Square(int pX, int pY) {
    super(pX*20, pY*20, size, size);
    x = pX*20;
    y = pY*20;
    xValue = false;
    oValue = false;
  }

  // draw square
  public void draw(Graphics g, Point mouseLoc) {
    if (mouseLoc!=null && contains(mouseLoc)) {
      g.setColor(Color.GRAY);
    } else {
      g.setColor(Color.WHITE);
    }
    g.fillRect(x, y, size, size);
    g.setColor(Color.BLACK);
    g.drawRect(x, y, size, size);
    if (used)
      drawXO(g);
  }

  public void drawHighlight(Graphics g, Color col) {
    g.setColor(col);
    g.fillRect(x, y, size, size);
    g.setColor(Color.BLACK);
    g.drawRect(x, y, size, size);
    if (used)
      drawXO(g);
  }

  public boolean isO() {
    return used&oValue;
  }

  public boolean isX() {
    return used&xValue;
  }

  public boolean isUsed() {
    return used;
  }

  // set square to X or O
  public boolean set(boolean o, Color color) {
    if (!used) {
      used = true;
      if (o) {
        oValue = true;
      }
      else {
        xValue = true;
      }
      return true;
    }
    return false;
  }

  // draw X or O
  private void drawXO(Graphics g) {
    if (oValue) {
      g.setColor(color);
      g.drawOval(x, y, size-2, height-2);
    } else if (xValue) {
      g.setColor(color);
      g.drawLine(x, y, x+size-2, y+size-2);
      g.drawLine(x+size-2, y, x, y+size-2);
    }
  }

}
