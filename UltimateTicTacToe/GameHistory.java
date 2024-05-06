package com.example.cw11;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class GameHistory {
    private static GameHistory instance;
    private ObservableList<Move> moves;

    public GameHistory() {
        moves = FXCollections.observableArrayList();
    }

    public static GameHistory getInstance() {
        if (instance == null) {
            instance = new GameHistory();
        }
        return instance;
    }

    public void addMove(String player, int smallBoardId, String wspolrzedne) {
        moves.add(new Move(player, smallBoardId, wspolrzedne));
    }

    public ObservableList<Move> getMoves() {
        return moves;
    }
}
