package org.example.canvasdemo;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by camil on 16-03-2017.
 */

public class GoldCoin {

    // skal lave gettere og settere for coinTaken, der skal returnere om coin
    // er taken eller ej - true eller false

    private boolean coinTaken;

    // getter for cointaken
    public boolean isCoinTaken() {
        return coinTaken;
    }

    // setter for cointaken
    public void setCoinTaken(boolean coinTaken) {
        this.coinTaken = coinTaken;
    }

    private int coinx = 100;

    // getter for coinx
    public int getCoinx() {
        return coinx;
    }

    // setter for coinx
    public void setCoinx(int coinx) {
        this.coinx = coinx;
    }

    private int coiny = 150;

    // getter for coiny
    public int getCoiny() {
        return coiny;
    }

    // setter for coiny
    public void setCoiny(int coiny) {
        this.coiny = coiny;
    }

    public GoldCoin() {} // tom constructor som vi vil bruge senere

    public GoldCoin(int coinx, int coiny) {
        this.coinx = coinx;
        this.coiny = coiny;
    }

}
