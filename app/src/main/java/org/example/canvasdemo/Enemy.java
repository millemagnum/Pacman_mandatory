package org.example.canvasdemo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by camil on 23-03-2017.
 */

public class Enemy implements Parcelable {

    // implementerer Parcelable, for at kunne bruge arraylisten af enemies i savedInstanceState og onSaveInstanceState!

    // boolean der bruges til at tjekke om enemy er ramt
    private boolean enemyHit;

    // getter for enemyHit - returnere om enemy er ramt eller ej
    public boolean didEnemyHit() {
        return enemyHit;
    }

    // setter for enemyHit - bruges til at sætte enemy til at være ramt
    public void setEnemyHit(boolean enemyHit) {
        this.enemyHit = enemyHit;
    }

    // x værdi for enemy
    private int enemyx = 100;

    // getter for enemyx
    public int getEnemyx() {
        return enemyx;
    }

    // setter for enemyx
    public void setEnemyx(int enemyx) {
        this.enemyx = enemyx;
    }

    // y værdi for enemy
    private int enemyy = 150;

    // getter for enemyy
    public int getEnemyy() {
        return enemyy;
    }

    // setter for enemyy
    public void setEnemyy(int enemyy) {
        this.enemyy = enemyy;
    }

    public Enemy() {} // tom constructor som vi vil bruge senere

    // constructor for Enemy, der kræver x og y position
    public Enemy(int enemyx, int enemyy) {
        this.enemyx = enemyx;
        this.enemyy = enemyy;
    }

    // skal have dem for at kunne implementere Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(enemyx);
        out.writeInt(enemyy);
    }

    // Creator
    public static final Parcelable.Creator<Enemy> CREATOR
            = new Parcelable.Creator<Enemy>() {
        public Enemy createFromParcel(Parcel in) {
            return new Enemy(in);
        }

        public Enemy[] newArray(int size) {
            return new Enemy[size];
        }
    };

    // "De-parcel object
    public Enemy(Parcel in) {
        enemyx = in.readInt();
        enemyy = in.readInt();
    }

}
