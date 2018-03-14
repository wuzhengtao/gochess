package view;

import control.CheckPiece;
import model.Board;

import java.util.Scanner;

/**
 * @ProjectName: gochess
 * @Author wuzht
 * @CreateDate: 2018/3/13 上午10:24
 * @UpdateUser: wuzht
 * @UpdateDate: 2018/3/13 上午10:24
 */
public class BoardView {
    //主函数，模拟下棋界面
    public static void main(String[] args) {
        CheckPiece cp = new CheckPiece();
        int count = 1;
        int chessman = 1;
        int[][] board = Board.newInstance().getBoard();
        printBoard(board);

        while (true) {
            String name = chessman == 1 ? "black" : "white";
            Scanner scanner = new Scanner(System.in);
            System.out.println("第" + count++ + "次 " + name + " 下棋：");

            System.out.println("请输入 x 和 y 值，0～8，空格分割");
            String input = scanner.nextLine();
            String[] inputs = input.split(" ");
            int x = Integer.parseInt(inputs[0]);
            int y = Integer.parseInt(inputs[1]);
            /*
             * 每次下棋会做检查，如果不符合规则，就重新下棋。
             * 主要有三种不符合规则的情况
             * 1。下到棋盘外面了
             * 2。下的地方本来有棋子
             * 3。此处是死路或者是禁着点
             */
            while (!cp.check(x, y, chessman)) {
                System.out.println("请输入 x 和 y 值，0～8，空格分割");
                input = scanner.nextLine();
                inputs = input.split(" ");
                x = Integer.parseInt(inputs[0]);
                y = Integer.parseInt(inputs[1]);
            }
            printBoard(board);
            //这个是更改下棋的人，1为黑，2为白
            chessman = 3 - chessman;
        }

    }

    private static void printBoard(int[][] board) {
        for (int[] cells : board) {
            for (int cell : cells) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }
}
