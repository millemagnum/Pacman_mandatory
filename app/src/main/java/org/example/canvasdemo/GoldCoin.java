package org.example.canvasdemo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by camil on 16-03-2017.
 */

public class GoldCoin implements Parcelable {

    // implementerer Parcelable, for at kunne bruge arraylisten af goldcoins i savedInstanceState og onSaveInstanceState!

    // boolean der bruges til at tjekke om coin er taken
    private boolean coinTaken;

    // getter for cointaken - returnere om coin er taken eller ej
    public boolean isCoinTaken() {
        return coinTaken;
    }

    // setter for cointaken - bruges til at sætte coin til at være taken
    public void setCoinTaken(boolean coinTaken) {
        this.coinTaken = coinTaken;
    }

    // x værdi for coin
    private int coinx = 100;

    // getter for coinx
    public int getCoinx() {
        return coinx;
    }

    // setter for coinx
    public void setCoinx(int coinx) {
        this.coinx = coinx;
    }

    // y værdi for coin
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

    // constructor for GoldCoin, der kræver x og y position
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
