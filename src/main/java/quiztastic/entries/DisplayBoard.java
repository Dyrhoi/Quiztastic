package quiztastic.entries;

import quiztastic.core.Board;
import quiztastic.domain.BoardController;

public class DisplayBoard {
    private final BoardController  boardController;

    public DisplayBoard() {
        this.boardController = new BoardController();
    }

    public void displayBoard() {
        Board board = boardController.makeBoard();
        System.out.println(board);
    }

    public static void main(String[] args) {
        new DisplayBoard().displayBoard();
    }
}
