package model;

/**
 * @ProjectName: gochess
 * @Author wuzht
 * @CreateDate: 2018/3/13 上午10:33
 * @UpdateUser: wuzht
 * @UpdateDate: 2018/3/13 上午10:33
 */
public class Piece {
    private int x, y;
    private int chessMan;

    Piece(int x, int y, int chessMan) {
        this.x = x;
        this.y = y;
        this.chessMan = chessMan;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getChessMan() {
        return chessMan;
    }

    public void setChessMan(int chessMan) {
        this.chessMan = chessMan;
    }
}
