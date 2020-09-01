package quiztastic.entries;

import quiztastic.app.ListQuestionRepository;
import quiztastic.app.QuestionReader;
import quiztastic.core.Board;
import quiztastic.domain.BoardController;
import quiztastic.domain.QuestionRepository;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;

public class DisplayBoard {
    private final BoardController boardController;

    public DisplayBoard() throws IOException, ParseException {
        InputStream s = this.getClass()
                .getClassLoader()
                .getResourceAsStream("master_season1-35clean.tsv");
        if(s == null)
            throw new FileNotFoundException("Kunne ikke finde data filen");

        QuestionReader reader = new QuestionReader(new InputStreamReader(s));
        QuestionRepository questionRepository =
                ListQuestionRepository.fromQuestionReader(reader);
        this.boardController = new BoardController(questionRepository);
    }

    public void displayBoard () throws IOException, ParseException {
        Board board = boardController.makeBoard();
        System.out.println(board);
    }

    public static void main(String[] args) throws IOException, ParseException {
        new DisplayBoard().displayBoard();
    }
}
