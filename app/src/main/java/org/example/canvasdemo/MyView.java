package org.example.canvasdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.sqrt;


public class MyView extends View{

	// arrayliste for goldcoin - igen
	ArrayList<GoldCoin> goldcoins = new ArrayList<GoldCoin>();

	// arrayliste for ghots
	ArrayList<Enemy> enemies = new ArrayList<Enemy>();


	// får fat i MainActivity og ligger det i myActivity variabel
	MainActivity myActivity = (MainActivity) getContext();


	// pacman
	Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pacman);

	// goldcoin
	Bitmap coinbitmap = BitmapFactory.decodeResource(getResources(), R.drawable.goldcoin);

	// ghost
	Bitmap ghostbitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ghost);

	int enemypos1;
	int enemypos2;

    //The coordinates for our dear pacman: (0,0) is the top-left corner
	int pacx = 50;
    int pacy = 400;
    int h,w; //used for storing our height and width



	// tager metoden fra
	public void setCoins(ArrayList<GoldCoin> coins) {

		// gemme det i en variabel - som skal loopes igennem nede ved onDraw
		goldcoins = coins;

	}

	// tager metoden fra
	public void setGhosts(ArrayList<Enemy> ghosts) {

		// gemme det i en variabel - som skal loopes igennem nede ved onDraw
		enemies = ghosts;

	}


    public void moveRight(int x)
    {
    	//still within our boundaries?
		// bevæger sig plus på x, så til højre
		// pacx her!
    	if (pacx+x+bitmap.getWidth()<w)
    		pacx=pacx+x;
		else {
			pacx=0;
		}


    	invalidate(); //redraw everything - this ensures onDraw() is called.
    }

	public void moveLeft(int x)
	{
		// bevæger sig minus på x, så til venstre
		// skal gerne være minus her og skal være ved 0, da det er startpunktet
		// for koordinatsystemet og det er jo venstre vi arbejder med
		if (pacx-x>0)
			pacx=pacx-x;
		else {
			pacx=650;
		}

		invalidate();
	}

	public void moveUp(int y)
	{
		// bruger y da det er opad og minus
		// skal gerne være minus her og skal være ved 0, da det er startpunktet
		// for koordinatsystemet og det er jo venstre vi arbejder med
		// pacy her!
		if (pacy-y>0)
			pacy=pacy-y;
		else {
			pacy=650;
		}
		invalidate();
	}

	public void moveDown(int y)
	{
		// bruger y da det er nedad og plus
		if (pacy+y+bitmap.getWidth()<h)
			pacy=pacy+y;
		else {
			pacy=0;
		}

		invalidate();
	}

	//int enemyy = enemies.get(0).getEnemyy();
	//int enemyx = enemies.get(0).getEnemyx();

	public void moveEnemyDown(int y) {
		// får fjenden til at bevæge sig nedad

		if (enemies.get(0).getEnemyy()+y+ghostbitmap.getWidth()<h) {
			// får fjenden til at bevæge sig nedad
			enemies.get(0).setEnemyy(enemies.get(0).getEnemyy() + 10); // var + 10
		} else {
			// kommer igennem skærmen som Pacman også gør
			enemies.get(0).setEnemyy(enemies.get(0).getEnemyy() - 650);
		}

		invalidate();
	}

	/*

	public void moveEnemyUp(int y) {

		// får fjenden til at bevæge sig opad
		if (enemies.get(0).getEnemyy()-y<0) {
			// får fjenden til at bevæge sig nedad
			enemies.get(0).setEnemyy(enemies.get(0).getEnemyy() - 10);
		} else {
			enemies.get(0).setEnemyy(enemies.get(0).getEnemyy() + 50);
		}


		//enemies.get(0).setEnemyy(enemies.get(0).getEnemyy() - 5);

		invalidate();
	}

	public void moveEnemyLeft(int x) {

		// får fjenden til at bevæge sig til venstre
		enemies.get(0).setEnemyx(enemies.get(0).getEnemyx() - 5);

		invalidate();
	}

	public void moveEnemyRight(int x) {

		// får fjenden til at bevæge sig til højre
		enemies.get(0).setEnemyx(enemies.get(0).getEnemyx() + 5);

		invalidate();
	}
	*/

	public void resetGame()
	{
		// resetter Pacman's position
		pacx = 50;

		myActivity.level = 1;

		// skal resette goldcoins - fjerner alle goldcoins
		goldcoins.clear();

		// skal tegne goldcoins igen - metoder her hvor der skal clear points og resette gold coins positioner igen
		myActivity.clearPoints();
		myActivity.resetGoldCoins();

		// når spillet startes på ny, sætter jeg den i gang med at køre spillet
		// så Pacman bevæger sig og bevæger sig fremad
		myActivity.gameRunning = true;
		myActivity.pacmanDirection = 1;
		myActivity.clearTimer();

		// resetter fjender
		enemies.clear();
		myActivity.spawnEnemies();


		// opdatere view
		invalidate();
	}

	public void newLevel(int x, int y, int level) //,ArrayList<GoldCoin> goldcoins, int level, int points, ArrayList<Enemy> enemies)
	{

		// resetter Pacman's position
		pacx = x;
		pacy = y;

		// skal resette goldcoins - fjerner alle goldcoins
		goldcoins.clear();

		// skal tegne goldcoins igen - metoder her hvor der skal clear points og resette gold coins positioner igen
		myActivity.resetGoldCoins();

		// når spillet startes på ny, sætter jeg den i gang med at køre spillet
		// så Pacman bevæger sig og bevæger sig fremad
		myActivity.gameRunning = true;
		myActivity.pacmanDirection = 1;
		myActivity.clearTimer();

		// resetter fjender
		enemies.clear();
		myActivity.spawnEnemies();

		myActivity.level = level;

		// opdatere view
		invalidate();
	}



	public void rotateScreen(int level) { //int points) { // int x, int y, int ghostx, int ghosty, int level

		// sætter værdierne omvendt, da skærmen roteres
		//pacx = y;
		//pacy = x;

		// sætter Pacmans position til noget nyt ved orientation change
		pacx = 50;
		pacy = 50;

		// sætter fjendens position til ny ved orientation change
		//enemypos2 = (enemies.get(0).setEnemyy(enemies.get(0).getEnemyy() + 10));
		//moveEnemyUp(20);

		//
		myActivity.gameRunning = true;
		myActivity.level = level;
		//myActivity.points = points;
        //myActivity.pointChanger(points);

		// prøver at sætte fjendens position
		//enemies.get(0).setEnemyy(enemies.get(0).getEnemyy() + 200);
		Enemy enemy = new Enemy();
		enemies.add(enemy);

		enemies.get(0).setEnemyx(200);
		enemies.get(0).setEnemyy(50);


		// bytter også rundt på værdierne for ghost - dropper det igen
		//enemypos1 = ghostx;
		//enemypos2 = ghosty;
	}


	/* The next 3 constructors are needed for the Android view system,
	when we have a custom view.
	 */
	public MyView(Context context) {
		super(context);
		
	}
	
	public MyView(Context context, AttributeSet attrs) {
		super(context,attrs);
	}
	
	
	public MyView(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context,attrs,defStyleAttr);
	}

	public double distance(int x1, int y1, int x2, int y2) {

		// skal bruge pythagoras til at udregne distancen mellem pacman og coins
		// skal huske at det skal være i anden, så det skal ganges med sig selv
		double distancemath = sqrt(((x2-x1)*(x2-x1)) + ((y2-y1)*(y2-y1)));

		return distancemath;
	}

	//In the onDraw we put all our code that should be
	//drawn whenever we update the screen.
	@Override
	protected void onDraw(Canvas canvas) {  
		//Here we get the height and weight
		h = canvas.getHeight();
		w = canvas.getWidth();
		System.out.println("h = "+h+", w = "+w);
		//Making a new paint object
		Paint paint = new Paint();
		//setting the color
		paint.setColor(Color.RED);
		canvas.drawColor(Color.WHITE); //clear entire canvas to white color
		//drawing a line from (0,0) -> (300,200)
		//canvas.drawLine(0,0,300,200,paint);
		//paint.setColor(Color.GREEN);
		//canvas.drawLine(0,200,300,0,paint);
		
		//setting the color using the format: Transparency, Red, Green, Blue
		paint.setColor(0xff000099);
		
		//drawing a circle with radius 20, and center in (100,100) 
		//canvas.drawCircle(100,100,20,paint);

		// kalder metoden for gameover?
		myActivity.isGameOver();

		// looper igennem de coins der er i goldcoins arraylisten
		for (GoldCoin coin : goldcoins) {

			// tilgår coinTaken fra GoldCoin klassen
			boolean cointaken = coin.isCoinTaken();

			// hvis coin er taget, skal den ikke tegnes
			if(cointaken) {
				// så skal scoren opdateres og der skal lægges point
				// til hver gang der tages en coin


			} else {
				// tegner hver coin på canvasset
				canvas.drawBitmap(coinbitmap, coin.getCoinx(), coin.getCoiny(), paint);
			}

			// havde <= 4 før
			if ((distance(pacx, pacy, coin.getCoinx(), coin.getCoiny()) <= 50) && !coin.isCoinTaken()) {

				// her kalder jeg metoden pointChanger, som skal opdatere textview'et, med 1 hver gang den kaldes
				myActivity.pointChanger(1);

				// sætter coinTaken til at være true, fordi den bliver taget af pacman
				coin.setCoinTaken(true);
			}

		}

		// looper igennem de enemies der er i enemies arraylisten
		for (Enemy enemy : enemies) {

			// tilgår coinTaken fra GoldCoin klassen
			//boolean enemyhit = enemy.didEnemyHit();

			// hvis coin er taget, skal den ikke tegnes
			//if(enemyhit) {
				// så skal scoren opdateres og der skal lægges point
				// til hver gang der tages en coin


			//} else {
				// tegner hver coin på canvasset
				canvas.drawBitmap(ghostbitmap, enemy.getEnemyx(), enemy.getEnemyy(), paint);
			//}

			// havde <= 4 før
			if ((distance(pacx, pacy, enemy.getEnemyx(), enemy.getEnemyy()) <= 50) && !enemy.didEnemyHit()) {

				enemy.setEnemyHit(true);
			}

			// prøver at finde fjendens position
			//enemypos1 = enemies.get(0).getEnemyy();
			//enemypos2 = enemies.get(0).getEnemyx();

			// enemypos1 = enemies.get(0).setEnemyy(20);



		}



		canvas.drawBitmap(bitmap, pacx, pacy, paint);
		super.onDraw(canvas);
	}

}
