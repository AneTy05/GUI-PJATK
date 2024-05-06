package com.example.cw11;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Move {
    private final SimpleStringProperty player;
    private final SimpleIntegerProperty smallBoardRow;
    private final SimpleStringProperty wspolrzedne;

    public Move(String player, int smallBoardRow, String wspolrzedne) {
        this.player = new SimpleStringProperty(player);
        this.smallBoardRow = new SimpleIntegerProperty(smallBoardRow);
        this.wspolrzedne = new SimpleStringProperty(wspolrzedne);
    }

    public String getPlayer() {
        return player.get();
    }
    public int getSmallBoardRow() {
        return smallBoardRow.get();
    }
    public String getWspolrzedne() {
        return wspolrzedne.get();
    }
}
