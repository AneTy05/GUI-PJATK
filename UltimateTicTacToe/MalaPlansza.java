package com.example.cw11;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class MalaPlansza extends GridPane {
    private EventHandler<ActionEvent> buttonClickedHandler;
    private boolean isCrossTurn;
    Image cross = new Image(getClass().getResourceAsStream("/krzyzyk.png"));
    Image circle = new Image(getClass().getResourceAsStream("/kolko.png"));
    int[][] malaButtons;
    WinChecker winChecker;
    GameHistory gameHistory;
    static int counter = 1;
    public int id;
    private int[][] buttonIds;
    public MalaPlansza() {
        this.id = counter;
        counter++;
        this.malaButtons = new int[3][3];
        this.winChecker = new WinChecker();
        this.gameHistory = GameHistory.getInstance();
        this.buttonIds = new int[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Button button = createButton(i, j);
                this.add(button, i, j);
                button.setDisable(true);
            }
        }
        setOnButtonClicked(event -> handleButtonClicked(event));
    }

    public int getPlanszaId() {
        return id;
    }
    private void handleButtonClicked(ActionEvent event) {
        int clickedBoardId = this.getPlanszaId();
    }
    public void setOnButtonClicked(EventHandler<ActionEvent> handler) {
        buttonClickedHandler = event -> {
            handleButtonClicked(event);
            handler.handle(event);
        };
    }
    public int[] getClickedButton(Button button) {
        int[] coordinates = new int[2];
        Integer rowIndex = GridPane.getRowIndex(button);
        Integer columnIndex = GridPane.getColumnIndex(button);
        coordinates[1] = (columnIndex != null) ? columnIndex : -1;
        coordinates[0] = (rowIndex != null) ? rowIndex : -1;
        return coordinates;
    }
    private Button createButton(int i, int j) {
        Button button = new Button();
        button.setMinSize(50, 50);
        button.setMaxSize(50, 50);

        ImageView imageView = new ImageView();
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);

        button.setGraphic(imageView);

        int buttonId = i * 3 + j + 1;
        button.setId(Integer.toString(buttonId));
        buttonIds[i][j] = buttonId;

        button.setOnAction(event -> {
            this.isCrossTurn = UltimateTicTacToe.isCrossTurn;
            if (buttonClickedHandler != null) {
                buttonClickedHandler.handle(event);
            }
            int[] clickedButton = getClickedButton(button);
            int clickedI = clickedButton[0];
            int clickedJ = clickedButton[1];
            button.setDisable(true);
            imageView.setImage(isCrossTurn ? cross : circle);
            if(isCrossTurn) {
                malaButtons[clickedI][clickedJ] = 1;
            } else {
                malaButtons[clickedI][clickedJ] = 2;
            }
            UltimateTicTacToe.isCrossTurn = !isCrossTurn;
            setUnavailable();
            setNextBoardActive(clickedI, clickedJ);
            int isWon = winChecker.isWon(malaButtons);
            if (isWon == 1) {
                displayCrossOnWholeBoard();
                UltimateTicTacToe.planszaSign[clickedI][clickedJ] = isWon;
            } else if (isWon == 2) {
                displayCircleOnWholeBoard();
                UltimateTicTacToe.planszaSign[clickedI][clickedJ] = isWon;
            }
            if (UltimateTicTacToe.planszaSign[clickedI][clickedJ] != 0) {
                for (int x = 0; x < 3; x++) {
                    for (int y = 0; y < 3; y++) {
                        if (UltimateTicTacToe.planszaSign[x][y] == 0) {
                            UltimateTicTacToe.allPlansze[x][y].setAvailable();
                        }
                    }
                }
            }
        });
        return button;
    }
    private void setNextBoardActive(int clickedI, int clickedJ) {
        for (Node node : this.getParent().getChildrenUnmodifiable()) {
            if (node instanceof MalaPlansza) {
                MalaPlansza board = (MalaPlansza) node;
                board.setUnavailable();
            }
        }
        int nextBoardI = clickedI;
        int nextBoardJ = clickedJ;
        MalaPlansza nextBoard = getBoardAt(nextBoardI, nextBoardJ);
        if (nextBoard != null) {
            nextBoard.setAvailable();
        }
    }
    public void setAvailable(){
        setDisableButtons(false);
    }

    public void setUnavailable(){
        setDisableButtons(true);
    }

    private void setDisableButtons(boolean disable) {
        for (Node node : this.getChildren()) {
            if (node instanceof Button) {
                Button button = (Button) node;
                button.setDisable(disable);
            }
        }
    }

    private MalaPlansza getBoardAt(int i, int j) {
        for (Node node : this.getParent().getChildrenUnmodifiable()) {
            if (node instanceof MalaPlansza) {
                MalaPlansza board = (MalaPlansza) node;
                if (GridPane.getRowIndex(board) == i && GridPane.getColumnIndex(board) == j) {
                    return board;
                }
            }
        }
        return null;
    }
    private void displayCrossOnWholeBoard() {
        this.getChildren().clear();
        ImageView imageView = new ImageView(cross);
        imageView.setFitWidth(150);
        imageView.setFitHeight(150);
        this.getChildren().add(imageView);
    }

    private void displayCircleOnWholeBoard() {
        this.getChildren().clear();
        ImageView imageView = new ImageView(circle);
        imageView.setFitWidth(150);
        imageView.setFitHeight(150);
        this.getChildren().add(imageView);
    }
}
