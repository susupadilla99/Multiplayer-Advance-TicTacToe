import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Board {
  private Square[][] squares;
  private boolean xWin;
  private boolean oWin;

  public Board(int pX, int pY) {
    squares = new Square[pX][pY];
    xWin = false;
    oWin = false;
    for (int i=0; i<squares.length; i++) {
      for (int j=0; j<squares[0].length; j++) {
        squares[i][j] = new Square(i, j);
      }
    }
  }

  public boolean check() {
    for (int i=0; i<squares.length; i++) {
      for (int j=0; j<squares[0].length; j++) {
        if (squares[i][j].isUsed() && hasFullLine(i, j, "none", 1)) {
          if (squares[i][j].isX()) xWin = true;
          else oWin = true;
          return true;
        }
      }
    }
    return false;
  }

  public boolean checkXWin() {
    return xWin;
  }

  public boolean checkOWin() {
    return oWin;
  }

  public void draw(Graphics g, Point mouseLoc) {
    for (int i=0; i<squares.length; i++) {
      for (int j=0; j<squares[0].length; j++) {
        squares[i][j].draw(g, mouseLoc);
      }
    }
  }

  public boolean set(Point mouseLoc, boolean o, Color col) {
    if (mouseLoc == null) return false;
    int x = (int)mouseLoc.getX()/20;
    int y = (int)mouseLoc.getY()/20;
    return squares[x][y].set(o, col);
  }  

  private boolean hasFullLine(int i, int j, String direction, int val) {
    //check all adjacent squares
    if (direction.equals("none")) {
      if (i<squares.length-1 && isSameXO(squares[i][j], squares[i+1][j])) {
        if (hasFullLine(i+1, j, "right", val+1)) return true;
      } 
      if (i>0 && isSameXO(squares[i][j], squares[i-1][j])) {
        if (hasFullLine(i-1, j, "left", val+1)) return true;
      }
      if (j>0 && isSameXO(squares[i][j], squares[i][j-1])) {
        if (hasFullLine(i, j-1, "up", val+1)) return true;
      } 
      if (j<squares.length-1 &&  isSameXO(squares[i][j], squares[i][j+1])) {
        if (hasFullLine(i, j+1, "down", val+1)) return true;
      }
      if (i<squares.length-1 && j>0 && isSameXO(squares[i][j], squares[i+1][j-1])) {
        if (hasFullLine(i+1, j-1, "up right", val+1)) return true;
      } 
      if (i>0 && j>0 && isSameXO(squares[i][j], squares[i-1][j-1])) {
        if (hasFullLine(i-1, j-1, "up left", val+1)) return true;
      }
      if (i<squares.length && j<squares[0].length && isSameXO(squares[i][j], squares[i+1][j+1])) {
        if (hasFullLine(i+1, j+1, "down right", val+1)) return true;
      } 
      if (i>0 && j<squares[0].length-1 && isSameXO(squares[i][j], squares[i-1][j+1])) {
        if (hasFullLine(i-1, j+1, "down left", val+1)) return true;
      } 
    }
    //check directional squares
    if (direction.equals("right")) {
      boolean nextSquare = i<squares.length-1 && isSameXO(squares[i][j], squares[i+1][j]);
      if (val == 4) return nextSquare;
      if (nextSquare) return hasFullLine(i+1, j, "right", val+1);
      return false;
    } else if (direction.equals("left")) {
      boolean nextSquare = i>0 && isSameXO(squares[i][j], squares[i-1][j]);
      if (val == 4) return nextSquare;
      if (nextSquare) return hasFullLine(i-1, j, "left", val+1);
      return false;
    } else if (direction.equals("up")) {
      boolean nextSquare = j>0 && isSameXO(squares[i][j], squares[i][j-1]);
      if (val == 4) return nextSquare;
      if (nextSquare) return hasFullLine(i, j-1, "up", val+1);
      return false;
    } else if (direction.equals("down")) {
      boolean nextSquare = j<squares.length-1 && isSameXO(squares[i][j], squares[i][j+1]);
      if (val == 4) return nextSquare;
      if (nextSquare) return hasFullLine(i, j+1, "down", val+1);
      return false;
    } else if (direction.equals("up right")) {
      boolean nextSquare = i<squares.length-1 && j>0 && isSameXO(squares[i][j], squares[i+1][j-1]);
      if (val == 4) return nextSquare;
      if (nextSquare) return hasFullLine(i+1, j-1, "up right", val+1);
      return false;
    } else if (direction.equals("up left")) {
      boolean nextSquare = i>0 && j>0 && isSameXO(squares[i][j], squares[i-1][j-1]);
      if (val == 4) return nextSquare;
      if (nextSquare) return hasFullLine(i-1, j-1, "up left", val+1);
      return false;
    } else if (direction.equals("down right")) {
      boolean nextSquare = i<squares.length && j<squares[0].length && isSameXO(squares[i][j], squares[i+1][j+1]);
      if (val == 4) return nextSquare;
      if (nextSquare) return hasFullLine(i+1, j+1, "down right", val+1);
      return false;
    } else if (direction.equals("down left")) {
      boolean nextSquare = i>0 && j<squares[0].length-1 && isSameXO(squares[i][j], squares[i-1][j+1]);
      if (val == 4) return nextSquare;
      if (nextSquare) return hasFullLine(i-1, j+1, "down left", val+1);
      return false;
    }
    return false;
  }

  private boolean isSameXO(Square one, Square two) {
    if (!one.isUsed() || !two.isUsed()) return false;
    if (one.isX() == two.isX() && one.isO() == two.isO()) return true;
    return false;
  }

}
