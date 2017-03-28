package org.example.canvasdemo;

/**
 * Created by camil on 23-03-2017.
 */

public class Enemy {


    private boolean enemyHit;

    // getter for enemyHit
    public boolean didEnemyHit() {
        return enemyHit;
    }

    // setter for enemyHit
    public void setEnemyHit(boolean enemyHit) {
        this.enemyHit = enemyHit;
    }

    private int enemyx = 100;

    // getter for enemyx
    public int getEnemyx() {
        return enemyx;
    }

    // setter for enemyx
    public void setEnemyx(int enemyx) {
        this.enemyx = enemyx;
    }

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

    public Enemy(int enemyx, int enemyy) {
        this.enemyx = enemyx;
        this.enemyy = enemyy;
    }

}
