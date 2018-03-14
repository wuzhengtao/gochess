package model;

/**
 * @ProjectName: gochess
 * @Author wuzht
 * @CreateDate: 2018/3/13 上午10:29
 * @UpdateUser: wuzht
 * @UpdateDate: 2018/3/13 上午10:29
 *
 * 棋盘类主要是存储棋盘，单例模式
 * 主要是三个数组
 * board数组存储棋盘状态
 * pieces数组主要是用来判断棋子的死活，这个具体怎么优化我还没想好，但是不是最完善的
 * ko数组是用来存储禁着点，也就是打劫
 */
public class Board {
    private static Board instance = new Board();
    private int[][] board = new int[9][9];
    private Piece[][] pieces = new Piece[9][9];
    private int[] ko = {-1, -1};

    private Board () {}
    public Piece newPiece(int x, int y, int chessMan) {
        if (x > 8 || x < 0 || y > 8 || y < 0) {
            return null;
        }
        if (pieces[x][y] == null) {
            pieces[x][y] = new Piece(x, y, chessMan);
        }
        pieces[x][y].setChessMan(chessMan);
        return pieces[x][y];
    }

    public static Board newInstance() {
        return instance;
    }

    public int[][] getBoard() {
        return board;
    }

    public int getcell(int x, int y) {
        return board[x][y];
    }

    public boolean setCell(Piece piece) {
        if (board[piece.getX()][piece.getY()] != 0) {
            return false;
        }
        board[piece.getX()][piece.getY()] = piece.getChessMan();
        return true;
    }

    public void setKo(int x, int y) {
        ko[0] = x;
        ko[1] = y;
    }

    public boolean isKo(int x, int y) {
        return ko[0] == x && ko[1] == y;
    }
}
