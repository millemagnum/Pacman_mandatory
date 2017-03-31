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

	// arrayliste for ghosts - igen
	ArrayList<Enemy> enemies = new ArrayList<Enemy>();

	// får fat i MainActivity og ligger det i myActivity variabel
	MainActivity myActivity = (MainActivity) getContext();

	// pacman
	Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pacman);

	// goldcoin
	Bitmap coinbitmap = BitmapFactory.decodeResource(getResources(), R.drawable.goldcoin);

	// ghost/enemy
	Bitmap ghostbitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ghost);

    //The coordinates for our dear pacman: (0,0) is the top-left corner
	int pacx = 50;
    int pacy = 400;
    int h,w; //used for storing our height and width

	// sætter coins ud fra resetGoldCoins metoden fra activity
	public void setCoins(ArrayList<GoldCoin> coins) {

		// gemme det i en variabel - som skal loopes igennem nede ved onDraw
		goldcoins = coins;

	}

	// sætter spøgelser ud fra spawnEnemies metoden fra activity
	public void setGhosts(ArrayList<Enemy> ghosts) {

		// gemme det i en variabel - som skal loopes igennem nede ved onDraw
		enemies = ghosts;

	}

	// denne metode kaldes, når spilleren trykker på "right" knappen og bevæger Pacman til højre (aka fremad) - også denne metode der bruges ved spillets start
    public void moveRight(int x)
    {
    	//still within our boundaries?
		// bevæger sig plus på x, så til højre
    	if (pacx+x+bitmap.getWidth()<w)
    		pacx=pacx+x;
		else {
			// gør at Pacman kommer ind fra venstre side, når han har bevæget sig helt til højre
			pacx=0;
		}

		// opdatere view
    	invalidate();
    }

    // denne metode kaldes, når spilleren trykker på "left" knappen og bevæger Pacman til venstre (aka baglæns)
	public void moveLeft(int x)
	{
		// bevæger sig minus på x, så til venstre
		// skal gerne være minus her og skal være ved 0, da det er startpunktet for koordinatsystemet og det er venstre vi arbejder med
		if (pacx-x>0)
			pacx=pacx-x;
		else {
			// gør at Pacman kommer ind fra højre side, når han har bevæget sig helt til venstre
			pacx=650;
		}

		// opdatere view
		invalidate();
	}

	// denne metode kaldes, når spilleren trykker på "up" knappen og bevæger Pacman opad
	public void moveUp(int y)
	{
		// bruger y da det er opad og minus
		// skal gerne være minus her og skal være ved 0, da det er startpunktet for koordinatsystemet og det er opad vi arbejder med
		if (pacy-y>0)
			pacy=pacy-y;
		else {
			// gør at Pacman kommer op fra bunden, når han har bevæget sig helt op i toppen
			pacy=650;
		}

		// opdatere view
		invalidate();
	}

	// denne metode kaldes, når spilleren trykker på "down" knappen og bevæger Pacman nedad
	public void moveDown(int y)
	{
		// bruger y da det er nedad og plus
		if (pacy+y+bitmap.getWidth()<h)
			pacy=pacy+y;
		else {
			// gør at Pacman kommer ned fra toppen, når han har bevæget sig helt ned i bunden
			pacy=0;
		}

		// opdatere view
		invalidate();
	}


	// denne metode kaldes, når spillet køres, hvor spøgelset bevæger sig nedad
	public void moveEnemyDown(int y) {

		// tjekker om fjendens position stemmer overens med canvassets højde
		if (enemies.get(0).getEnemyy()+y+ghostbitmap.getWidth()<h) {
			// får fjenden til at bevæge sig nedad
			enemies.get(0).setEnemyy(enemies.get(0).getEnemyy() + 10);
		} else {
			// bevæger sig ned i bunden og kommer ind igen fra toppen
			enemies.get(0).setEnemyy(enemies.get(0).getEnemyy() - 650);
		}

		// opdatere view
		invalidate();
	}


	// denne metode kaldes, når man starter et nyt spil
	public void resetGame()
	{
		// resetter Pacman's position
		pacx = 50;

		// level sættes til 1, da man starter forfra i et nyt spil
		myActivity.level = 1;

		// tømmer arraylisten af coins og resetter coins position
		goldcoins.clear();
		myActivity.resetGoldCoins();

		// sætter points til nul, da man starter forfra :)
		myActivity.clearPoints();

		// når spillet startes på ny, sætter jeg den i gang med at køre spillet, så Pacman bevæger sig og bevæger sig fremad
		myActivity.gameRunning = true;
		myActivity.pacmanDirection = 1;

		// timeren bliver resettet
		myActivity.clearTimer();

		// tømmer arrayliste af enemies og re-spawner fjender
		enemies.clear();
		myActivity.spawnEnemies();

		// opdatere view
		invalidate();
	}

	// denne metode kaldes, når et level er gennemført og det nye level opsættes
	public void newLevel(int x, int y, int level)
	{

		// resetter Pacman's position
		pacx = x;
		pacy = y;

		// tømmer arraylisten af coins og kalder metoden der resetter coins
		goldcoins.clear();
		myActivity.resetGoldCoins();

		// når et nyt level starter, sætter jeg den i gang med at køre spillet, så Pacman bevæger sig og bevæger sig fremad (mod højre)
		myActivity.gameRunning = true;
		myActivity.pacmanDirection = 1;
		// resetter timeren, så den starter forfra
		myActivity.clearTimer();

		// tømmer arraylisten af enemies og re-spawner enemeis
		enemies.clear();
		myActivity.spawnEnemies();

		// holder styr på level
		myActivity.level = level;

		// opdatere view
		invalidate();
	}


	// denne metode kaldes ved orientation change og har til formål at ændre Pacmans position og spøgelsets position
	public void rotateScreen(int level) {

		// sætter Pacmans position til noget nyt ved orientation change
		pacx = 50;
		pacy = 50;

		// sætter spillet til at køre og hiver fat i level fra activity
		myActivity.gameRunning = true;
		myActivity.level = level;

		// kunne ikke få fat i de rigtige points ved orientation change, selvom den gemte og fandt de rigtige points i savedInstanceState.. mystisk
		//myActivity.points = points;
        //myActivity.pointChanger(points);

		// opretter en ny fjende og tilføjer til arraylisten "enemies"
		Enemy enemy = new Enemy();
		enemies.add(enemy);

		// her prøver jeg at sætte fjendens nye position ved orientation change
		enemies.get(0).setEnemyx(200);
		enemies.get(0).setEnemyy(150);

		// opdatere view
		invalidate();
	}


	/* The next 3 constructors are needed for the Android view system,
	when we have a custom view. */
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

	// dette bruges til at afregne distancen mellem Pacman og mønter og Pacman og spøgelset, som bruges i onDraw metoden
	public double distance(int x1, int y1, int x2, int y2) {

		// her tages den ene x værdi minus den anden x værdi og så ganges det med sig selv og det samme gøres ved y-værdierne
		// pythagoras' sætning
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
		
		//setting the color using the format: Transparency, Red, Green, Blue
		paint.setColor(0xff000099);


		// kalder metoden for game over
		myActivity.isGameOver();

		// looper igennem de coins der er i goldcoins arraylisten
		for (GoldCoin coin : goldcoins) {

			// tilgår coinTaken fra GoldCoin klassen
			boolean cointaken = coin.isCoinTaken();

			// tjekker om coin er taget
			if(cointaken) {

			// hvis coin er taget, skal den ikke tegnes

			} else {

				// tegner hver coin på canvasset, hvis disse ikke er taget af Pacman
				canvas.drawBitmap(coinbitmap, coin.getCoinx(), coin.getCoiny(), paint);
			}

			// tjekker om Pacman har taget en mønt og om mønten ikke er taget
			if ((distance(pacx, pacy, coin.getCoinx(), coin.getCoiny()) <= 50) && !coin.isCoinTaken()) {

				// her kalder jeg metoden pointChanger, som skal opdatere textview'et, med 1 point hver gang den kaldes
				myActivity.pointChanger(1);

				// sætter coinTaken til at være true, fordi den bliver taget af Pacman
				coin.setCoinTaken(true);
			}

		}

		// looper igennem de enemies der er i enemies arraylisten
		for (Enemy enemy : enemies) {

			// tegner hvert spøgelse (som i dette tilfælde er ét spøgelse) på canvasset
			canvas.drawBitmap(ghostbitmap, enemy.getEnemyx(), enemy.getEnemyy(), paint);

			// her tjekkes om Pacman rammer spøgelset og om fjenden ikke har ramt Pacman endnu
			// hvis de rammer hinanden, så sættes fjenden til at have ramt Pacman ved at bruge setEnemyHit fra Enemy klassen
			if ((distance(pacx, pacy, enemy.getEnemyx(), enemy.getEnemyy()) <= 50) && !enemy.didEnemyHit()) {

				enemy.setEnemyHit(true);
			}
		}


		// Pacman tegnes på canvasset med hans x og y position
		canvas.drawBitmap(bitmap, pacx, pacy, paint);
		super.onDraw(canvas);
	}

}
