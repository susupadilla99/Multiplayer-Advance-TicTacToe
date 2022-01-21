public class Board {
  public Square[][] squares;

  public Board(int pX, int pY) {
    squares = new Square[pX][pY];
    for (int i=0; i<pX; i++) {
      for (int j=0; j<pY; j++) {
        squares[pX][pY] = new Square(pX, pY);
      }
    }
  }

  public void draw(Graphics g) {
    for (int i=0; i<squares.size; i++) {
      for (int j=0; j<squares[0].size; j++) {
        squares[i][j].draw();
      }
    }
  }

  

}
