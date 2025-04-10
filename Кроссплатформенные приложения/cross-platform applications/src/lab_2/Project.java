package lab_2;

import java.util.Scanner;

public class Project {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        int N = input.nextInt();
        int M = input.nextInt();

        Knight knight = new Knight(0, 0);
        Chessboard chessboard = new Chessboard(N, M);

        int knightMovesAmount = chessboard.countMovesForKnight(knight);
        System.out.println(knightMovesAmount);
    }
}

class Chessboard {
    int[][] chessboard;

    Chessboard(int N, int M) {
        this.chessboard = new int[N][M];
    }

    public int countMovesForKnight(Knight knight) {
        this.chessboard[0][0] = 1;

        int need_x = this.chessboard.length - 1;
        int need_y = this.chessboard[0].length - 1;

        for (int i = 0; i <= need_x; i++) {
            for (int j = 0; j <= need_y; j++) {
                if (chessboard[i][j] > 0) {
                    for (int[] move : knight.moves) {
                        knight.x = i + move[0];
                        knight.y = j + move[1];

                        if (this.isMoveAllowed(knight.x, knight.y)) {
                            chessboard[knight.x][knight.y] += chessboard[i][j];
                        }
                    }
                }
            }
        }

        for (int[] arr : this.chessboard) {
            for (int item : arr) {
                System.out.print(item + " ");
            }
            System.out.println();
        }

        return this.chessboard[need_x][need_y];
    }

    protected boolean isMoveAllowed(int x, int y) {
        return x >= 0 && x < chessboard.length && y >= 0 && y < chessboard[0].length;
    }
}

class Knight {
    final int[][] moves = {{2, 1}, {1, 2}};
    int x, y;

    Knight(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
