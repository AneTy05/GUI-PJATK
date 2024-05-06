package com.example.cw11;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class UltimateTicTacToe extends Application {
    static boolean isCrossTurn = true;
    static MalaPlansza allPlansze[][] = new MalaPlansza[3][3];
    static int planszaSign[][] = new int[3][3];
    WinChecker winChecker;
    GameHistory gameHistory;
    TableView<Move> moveTable;
    TableColumn<Move, String> playerColumn;
    TableColumn<Move, Integer> smallBoardRowColumn;
    TableColumn<Move, String> wspolrzedneColumn;
    private int lastClickedRow;
    private int lastClickedColumn;
    public void start(Stage stage) {
        GridPane duzaPlansza = new GridPane();
        duzaPlansza.setHgap(7);
        duzaPlansza.setVgap(7);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                MalaPlansza malaPlansza = new MalaPlansza();
                malaPlansza.setOnButtonClicked(event -> handleButtonClicked(event));
                allPlansze[i][j] = malaPlansza;
                duzaPlansza.add(malaPlansza, j, i);
            }
        }
        winChecker = new WinChecker();
        gameHistory = new GameHistory();

        allPlansze[1][1].setAvailable();

        moveTable = new TableView<>();
        playerColumn = new TableColumn<>("Gracz");
        smallBoardRowColumn = new TableColumn<>("Plansza");
        wspolrzedneColumn = new TableColumn<>("Pole");

        playerColumn.setCellValueFactory(new PropertyValueFactory<>("player"));
        smallBoardRowColumn.setCellValueFactory(new PropertyValueFactory<>("smallBoardRow"));
        wspolrzedneColumn.setCellValueFactory(new PropertyValueFactory<>("wspolrzedne"));
        moveTable.getColumns().addAll(playerColumn, smallBoardRowColumn, wspolrzedneColumn);
        moveTable.setItems(gameHistory.getMoves());

        HBox hbox = new HBox(duzaPlansza, moveTable);
        Scene scene = new Scene(hbox);
        stage.setScene(scene);
        stage.show();

    }
    private void handleButtonClicked(ActionEvent event) {
        Button button = (Button) event.getSource();
        MalaPlansza smallBoard = (MalaPlansza) button.getParent();
        int clickedI = GridPane.getRowIndex(smallBoard);
        int clickedJ = GridPane.getColumnIndex(smallBoard);
        int[] clickedButton = smallBoard.getClickedButton(button);
        int row = clickedButton[0];
        int column = clickedButton[1];
        String wspolrzedne = row + "-" + column;
        String player = isCrossTurn ? "X" : "O";
        UltimateTicTacToe.isCrossTurn = !isCrossTurn;
        int isBigWon = winChecker.isWon(planszaSign);
        gameHistory.addMove(player, smallBoard.getPlanszaId(), wspolrzedne);
        if (isBigWon != 0) {
            gameHistory.addMove("none", -1, "none");
        }
        lastClickedRow = clickedI;
        lastClickedColumn = clickedJ;
    }
    public boolean isCrossTurn() {
        return UltimateTicTacToe.isCrossTurn;
    }

    public static void main(String[] args) {
        launch();
    }
}
