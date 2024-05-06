package com.example.cw11;

public class WinChecker {
    public int isWon (int[][] signs) {
        //sprawdzanie poziomu
        for (int i = 0; i < 3; i++) {
            if (signs[i][0] != 0 || signs[i][1] != 0 || signs[i][2] != 0) {
                if (signs[i][0] == signs[i][1] && signs[i][1] == signs[i][2]) {
                    return signs[i][0];
                }
            }
        }
        //sprawdzanie pionu
        for (int i = 0; i < 3; i++) {
            if (signs[0][i] != 0 || signs[1][i] != 0 || signs[2][i] != 0) {
                if (signs[0][i] == signs[1][i] && signs[1][i] == signs[2][i]) {
                    return signs[0][i];
                }
            }
        }
        //sprawdzanie przekątnaj od góry-lewej
        if (signs[0][0] != 0 || signs[1][1] != 0 || signs[2][2] != 0) {
            if (signs[0][0] == signs[1][1] && signs[1][1] == signs[2][2]) {
                return signs[0][0];
            }
        }
        //sprawdzanie przekątnej od góry-prawej
        if (signs[2][0] != 0 && signs[0][2] == signs[1][1] && signs[1][1] == signs[2][0]) {
            return signs[0][2];
        }
        else return 0;
    }
}