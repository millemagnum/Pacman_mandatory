package org.example.canvasdemo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by camil on 16-03-2017.
 */

public class GoldCoin implements Parcelable {

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

    // skal have dem for at kunne implementere Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(coinx);
        out.writeInt(coiny);
    }

    // Creator
    public static final Parcelable.Creator<GoldCoin> CREATOR
            = new Parcelable.Creator<GoldCoin>() {
        public GoldCoin createFromParcel(Parcel in) {
            return new GoldCoin(in);
        }

        public GoldCoin[] newArray(int size) {
            return new GoldCoin[size];
        }
    };

    // "De-parcel object
    public GoldCoin(Parcel in) {
        coinx = in.readInt();
        coiny = in.readInt();
    }

}
