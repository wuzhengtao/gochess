package control;

import model.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @ProjectName: gochess
 * @Author wuzht
 * @CreateDate: 2018/3/13 上午11:01
 * @UpdateUser: wuzht
 * @UpdateDate: 2018/3/13 上午11:01
 * <p>
 * 这个是逻辑类
 */
public class CheckPiece {
    private Board board = Board.newInstance();
    private Map<Piece, PieceStatus> buffer;
    private int count = 0;
    private int koX, koY;

    public CheckPiece() {
        buffer = new HashMap<>();
    }

    /**
     * 这个类就是在落子之后，判断落子是否合理，以及有没有提子的情况
     * 主要方法：
     * 判断上下左右四个方向，如果是对方的子，则判断这个子是否能继续存活，如果死了，则放入buffer，且isKill为真
     * 如果有提子的情况发生，则清理buffer，进行提子动作
     * 然后判断本身是否存活
     * 最后如果提子数为1，则被提的这个点为禁着点，也就是下一步不能走这儿
     *
     * @param x        下棋的横坐标
     * @param y        下棋的纵坐标
     * @param chessman 下棋方
     * @return boolean
     */
    public boolean check(int x, int y, int chessman) {
        Piece piece = board.newPiece(x, y, chessman);
        boolean isKill = false;
        buffer.clear();
        if (x > 8 || x < 0 || y > 8 || y < 0) {
            System.out.println("超出棋盘范围了");
            return false;
        }
        if (!board.setCell(piece)) {
            System.out.println("该处有棋子，请重新下棋");
            return false;
        }
        if (board.isKo(x, y)) {
            System.out.println("此为禁着点");
            return false;
        }
        int ochers = 3 - chessman;
        if (y > 0 && board.getcell(x, y - 1) == ochers) {
            Piece upPiece = board.newPiece(x, y - 1, ochers);
            isKill = !isAlive(upPiece, Direction.NONE);
        }
        if (y < 8 && board.getcell(x, y + 1) == ochers) {
            Piece downPiece = board.newPiece(x, y + 1, ochers);
            isKill = !isAlive(downPiece, Direction.NONE);
        }
        if (x > 0 && board.getcell(x - 1, y) == ochers) {
            Piece leftPiece = board.newPiece(x - 1, y, ochers);
            isKill = !isAlive(leftPiece, Direction.NONE);
        }
        if (x < 8 && board.getcell(x + 1, y) == ochers) {
            Piece rightPiece = board.newPiece(x + 1, y, ochers);
            isKill = !isAlive(rightPiece, Direction.NONE);
        }
        if (isKill) {
            clearDeadPiece();
        }
        if (!isAlive(piece, Direction.NONE) && (!isKill)) {
            System.out.println("别自寻死路");
            return false;
        }
        if (count == 1) {
            Board.newInstance().setKo(koX, koY);
            count = 0;
        } else {
            Board.newInstance().setKo(-1, -1);
        }
        return true;
    }

    /**
     * 这个方法用来清理buffer，并且储存禁着点。
     * 放入buffer的棋子有三种状态，true、false、unfinished，提出false
     */
    private void clearDeadPiece() {
        count = 0;
        for (Piece key : buffer.keySet()) {
            if (buffer.get(key) == PieceStatus.FALSE) {
                count++;
                board.getBoard()[key.getX()][key.getY()] = 0;
                koX = key.getX();
                koY = key.getY();
            }
        }
        buffer.clear();
    }

    /**
     * 这个方法是判断棋子是否存活
     * 主要方法
     * 只要开始判断就将棋子存入buffer
     * 分别判断上下左右，如果从某个方向过来，则这个方向不进行判断
     * 只要有一个方向判断是真，则这个棋子就存活了，将buffer里的状态修改为true
     * 四个方向都为假，则这个棋子就死了，状态修改为false
     *
     * @param piece 待判断的棋子
     * @param dir   是从哪个方向到这个棋子的，方向一共五个，见枚举
     * @return boolean
     */
    private boolean isAlive(Piece piece, Direction dir) {
        buffer.put(piece, PieceStatus.UNFINISHED);
        int x = piece.getX();
        int y = piece.getY();
        int chessman = piece.getChessMan();
        if (dir != Direction.UP) {
            Piece tempPiece = board.newPiece(x, y + 1, chessman);
            if (tempPiece != null) {
                if (isPieceAlive(tempPiece, Direction.DOWN)) {
                    buffer.put(piece, PieceStatus.TRUE);
                    return true;
                }
            }
        }
        if (dir != Direction.DOWN) {
            Piece tempPiece = board.newPiece(x, y - 1, chessman);
            if (tempPiece != null) {
                if (isPieceAlive(tempPiece, Direction.UP)) {
                    buffer.put(piece, PieceStatus.TRUE);
                    return true;
                }
            }
        }
        if (dir != Direction.LEFT) {
            Piece tempPiece = board.newPiece(x + 1, y, chessman);
            if (tempPiece != null) {
                if (isPieceAlive(tempPiece, Direction.RIGHT)) {
                    buffer.put(piece, PieceStatus.TRUE);
                    return true;
                }
            }
        }
        if (dir != Direction.RIGHT) {
            Piece tempPiece = board.newPiece(x - 1, y, chessman);
            if (tempPiece != null) {
                if (isPieceAlive(tempPiece, Direction.LEFT)) {
                    buffer.put(piece, PieceStatus.TRUE);
                    return true;
                }
            }
        }
        buffer.put(piece, PieceStatus.FALSE);
        return false;
    }

    /**
     * 判断单个点是否存活
     * 如果这个点是 气 ，则返回true
     * 如果这个点的棋子颜色相同，则返回这个棋子的状态
     *
     * @param tempPiece 判断的点
     * @param dir       方向
     * @return boolean
     */
    private boolean isPieceAlive(Piece tempPiece, Direction dir) {
        int x = tempPiece.getX();
        int y = tempPiece.getY();
        int chessman = tempPiece.getChessMan();
        int status = board.getcell(x, y);
        if (status == 0) {
            return true;
        }
        if (status == chessman) {
            if (buffer.containsKey(tempPiece)) {
                return buffer.get(tempPiece).equals(PieceStatus.TRUE);
            }
            return isAlive(tempPiece, dir);
        }
        return false;
    }


}
