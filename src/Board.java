import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Board {
  private Square[][] squares;

  public Board(int pX, int pY) {
    squares = new Square[pX][pY];
    for (int i=0; i<pX; i++) {
      for (int j=0; j<pY; j++) {
        squares[pX][pY] = new Square(pX, pY);
      }
    }
  }

  public void draw(Graphics g, Point mouseLoc) {
    for (int i=0; i<squares.length; i++) {
      for (int j=0; j<squares[0].length; j++) {
        squares[i][j].draw(g, mouseLoc);
      }
    }
  }

  public void set(int pX, int pY, boolean o, Color col) {
    squares[pX][pY].set(o, col);
  }  

}
