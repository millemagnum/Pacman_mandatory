package org.example.canvasdemo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends Activity {

	// custom view
	MyView myView;

	// arrayliste for goldcoin
	ArrayList<GoldCoin> goldcoins = new ArrayList<GoldCoin>();

	// arrayliste for enemies
	ArrayList<Enemy> enemies = new ArrayList<Enemy>();

	// context bruges til toasts
	static Context context;

	// bruges til point for Pacman - her som 0 når spillet starter
	int points = 0;

	// timer der skal bruges til at "animere" Pacman
	private Timer timer;

	// timer til countdown, der skal holde styr på tiden og tælle ned
	private Timer countdown;

	// timer til enemy, der skal bruges til at "animere" fjenden
	private Timer enemytimer;

	// counter for countdown - tiden starter på 60 sek.
	private int counter = 60;

	// boolean for om spillet kører
	public boolean gameRunning = false;

	// direction value for at holde styr på hvor Pacman bevæger sig hen
	public int pacmanDirection = 1;

	// bestemmer at level skal være 1 til at starte med
	public int level = 1;

	// metode til at tjekke om spillet er slut/tabt
	public boolean isGameOver() {

		// skal loope alle mønter igennem, så den tjekker om alle mønterne er taget
		boolean allTaken = true;

		// kædes sammen med myView for at lave et loop, der kører igennem coins
		for (GoldCoin coin : myView.goldcoins) {

			// tilgår coinTaken fra GoldCoin klassen - getter for cointaken
			boolean cointaken = coin.isCoinTaken();

			// hvis alle coins ikke er taget, er allTaken false, da alle coins så ikke er indsamlet
			if (!cointaken) {
				allTaken = false;
			}
		}

		// boolean der bruges til at tjekke om Pacman er død eller levende :)
		boolean pacmanDied = false;

		// kædes sammen med myView for at lave et loop, der kører igennem enemies
		for (Enemy enemy : myView.enemies) {

			// tilgår enemyhit fra Enemy klassen - getter for enemyhit
			boolean enemyhit = enemy.didEnemyHit();

			// hvis enemy rammes, er pacmanDied true, da Pacman så dør
			if (enemyhit) {
				pacmanDied = true;
			}
		}

		// hvis alle mønter er taget inden tiden er gået og Pacman IKKE er død, så skal spilleren komme videre til næste level
		if(allTaken == true && pacmanDied == false) {

			// laver en toast der kommer frem, når Pacman har taget alle coins og et level er gennemført
			// toasten udskriver hvilket level, man har gennemført
			Toast toast = Toast.makeText(context,
					"Level " + level + " completed", Toast.LENGTH_SHORT);
			toast.show();

			// stopper spillet, da første level er gennemført - bare rolig det bliver sat i gang igen ved newLevel metoden
			gameRunning = false;

			// increaser level for hver gang man gennemfører det tidligere level - woohoo level up!
			level += 1;

			// kalder metoden newLevel fra myView, som sender Pacmans x, y og level med
			myView.newLevel(50, 5, level);

		}

		// hvis spilleren ikke har taget alle coins og tiden er gået eller Pacman rørte fjenden er der game over!
		if(allTaken == false && counter == 0 || pacmanDied == true) {

			// laver en toast, der skriver "Game over"
			Toast toast = Toast.makeText(context,
					"Game Over!", Toast.LENGTH_SHORT);
			toast.show();

			// stopper spillet
			gameRunning = false;

			// returnere true ift. isGameOver
			return true;
		}

		// skal returne false som standard
		return false;
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		this.context = this;

		// finder custom view
		myView = (MyView) findViewById(R.id.gameView);

		// vil bruge dette til at gemme informationer om spillet, så det kan spilles i landscape mode også
		if (savedInstanceState != null) {

			// får fat i de gemte variabler for enemies, goldcoins, level (hvor rotateScreen kaldes), points, gameRunning og counter
			// går herind hvis der er gemt noget - før orientation change
			ArrayList<Enemy> enemies = savedInstanceState.getParcelableArrayList("enemies");
			ArrayList<GoldCoin> goldcoins = savedInstanceState.getParcelableArrayList("goldcoins");
			myView.rotateScreen(savedInstanceState.getInt("level"));
			points = savedInstanceState.getInt("points");
			gameRunning = savedInstanceState.getBoolean("gameRunning");
			counter = savedInstanceState.getInt("counter");

		// hvis der ikke er gemt noget, så skal spillet bare køre
		} else {
			gameRunning = true;
		}

		// finder alle knapper
		Button rightbutton = (Button) findViewById(R.id.moveRightButton);
		Button leftbutton = (Button) findViewById(R.id.moveLeftButton);
		Button upbutton = (Button) findViewById(R.id.moveUpButton);
		Button downbutton = (Button) findViewById(R.id.moveDownButton);
		Button newgamebutton = (Button) findViewById(R.id.newGameButton);
		Button resumebutton = (Button) findViewById(R.id.resumeButton);
		Button pausebutton = (Button) findViewById(R.id.pauseButton);

		// ny timer - for pacman
		timer = new Timer();

		// ny timer - for countdown (aka nedtælling)
		countdown = new Timer();

		// ny timer - for enemy
		enemytimer = new Timer();

		// starter spillet, sætter gameRunning til true
		gameRunning = true;

		// kalder timeren for Pacman og bestemmer at det skal være en ny TimerTask samt at den vil køre timeren med det samme og med 200 milisekunder
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				PacmanRunnerMethod();
			}
		}, 0, 200);

		// timer for countdown for spil - her at milisekunder er 1000 for at tælle ned hvert sekund
		countdown.schedule(new TimerTask() {
			@Override
			public void run() {
				TimerRunnerMethod();
			}
		}, 0, 1000);

		// timer for enemies - samme milisekunder som Pacman
		enemytimer.schedule(new TimerTask() {
			@Override
			public void run() {
				EnemyRunnerMethod();
			}
		}, 0, 200);


		//listener of our pacman - for alle knapper
		rightbutton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				// kalder metoden moveRight for pacman fra MyView
				myView.moveRight(10);

				// sætter vejen Pacman går til 1
				pacmanDirection = 1;
			}
		});

		// listener for venstre knap
		leftbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// kalder metoden moveLeft for pacman fra MyView
				myView.moveLeft(10);

				// sætter vejen Pacman går til 2
				pacmanDirection = 2;
			}
		});

		// listener for op knap
		upbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// kalder metoden moveUp for pacman fra MyView
				myView.moveUp(10);

				// sætter vejen Pacman går til 3
				pacmanDirection = 3;
			}
		});

		// listener for ned knap
		downbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// kalder metoden moveDown for pacman fra MyView
				myView.moveDown(10);

				// sætter vejen Pacman går til 4
				pacmanDirection = 4;
			}
		});

		// listener for new game knap
		newgamebutton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				// sætter point til at være 0 igen
				points = 0;

				// bruger metode i custom view, der vil resette spillet
				myView.resetGame();

			}
		});

		// click event listener der sætter spillet til at køre igen
		resumebutton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				gameRunning = true;
			}
		});

		// click event listener der pauser spillet
		pausebutton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				gameRunning = false;
			}
		});

		// kalder goldcoins metoden - spawner coins ved start
		resetGoldCoins();

		// kalder enemies metoden - spawner enemies ved start
		spawnEnemies();

	}

	// runner metode for Pacman
	private void PacmanRunnerMethod() {
		// bestemmer at det skal køre på UI thread
		this.runOnUiThread(PacmanRunning);
	}

	// runner metode for tidsnedtælling
	private void TimerRunnerMethod() {
		this.runOnUiThread(TimerCountdown);
	}

	// runner metode for Ghosts/enemies
	private void EnemyRunnerMethod() {
		this.runOnUiThread(EnemyRunning);
	}

	// runnable for Pacman
	private Runnable PacmanRunning = new Runnable() {
		@Override
		public void run() {

			if(gameRunning) {

				// denne if-sætning tjekker på hvilken direction Pacman har valgt at gå og værdien sættes oppe ved click listeners på knapperne, som ændres, når
				// man klikker på de forskellige bevæge-knapper - her bevæger han sig den vej du gerne vil have med timeren
				if(pacmanDirection == 1) {

					// flytter Pacman til højre
					myView.moveRight(20);

				} else if(pacmanDirection == 2) {

					// flytter Pacman til venstre
					myView.moveLeft(20);

				} else if(pacmanDirection == 3) {

					// flytter Pacman upad
					myView.moveUp(20);

				} else if(pacmanDirection == 4) {

					// flytter Pacman nedad
					myView.moveDown(20);

				}

			}
		}
	};

	// runnable for tidsnedtælling
	private Runnable TimerCountdown = new Runnable() {
		@Override
		public void run() {

			if (gameRunning) {

				// nedtælling hvis spillet kører
				counter--;

				// finder textview for tids timer
				TextView timer = (TextView) findViewById(R.id.timeCounter);

				// viser tiden der går og counter tæller ned hvert sekund
				timer.setText("Timer: " + counter);

			}
		}
	};

	// runnable for enemies
	private Runnable EnemyRunning = new Runnable() {

		@Override
		public void run() {
			if(gameRunning) {

				// bevæger enemy nedad, hvis spillet kører
				myView.moveEnemyDown(20);
			}
		}
	};

	// denne metode bruges til at få points til at increase for hver coin der er taget
	public void pointChanger(int newPoint) {

		// increaser points med 1 for hver gang
		points++;

		// får fat i teksten fra scoreKeeper
		TextView score = (TextView) findViewById(R.id.scoreKeeper);

		// sætter teksten til at skulle være points: og de point man har
		score.setText("Points: " + points);

	}

	// metode til at clear pointene, når der trykkes på New Game knappen
	public void clearPoints() {

		// sætter points til at være 0
		points = 0;

		// får fat i teksten fra scoreKeeper
		TextView score = (TextView) findViewById(R.id.scoreKeeper);

		// sætter teksten til at skulle være points: og de point man har, som gerne skal være 0 nu
		score.setText("Points: " + points);

	}

	// metode til at lave goldcoins - og også få dem til at resette/respawne
	public void resetGoldCoins() {

		// for loop der kører igennem 0-10 forskellige goldcoins
		for (int i = 0; i < 10; i++) {

			// laver et random tal mellem 100 og 600
			Random randomnumber = new Random();
			int randomx = randomnumber.nextInt(600) + 100;
			int randomy = randomnumber.nextInt(600) + 100;

			// tilføjer det til arraylisten, så der er 10 forskellige goldcoins i den med hver deres x og y position
			goldcoins.add(new GoldCoin(randomx, randomy));
		}

		// sender metoden til myView med goldcoins
		myView.setCoins(goldcoins);
	}

	// metode der laver enemies med random placering
	public void spawnEnemies() {

			// laver et random tal som bliver x og y position for ghost
			// random tal mellem 100 og 600
			for (int i = 0; i < 1; i++) {
				Random randomnumber2 = new Random();
				int randomx2 = randomnumber2.nextInt(600) + 100;
				int randomy2 = randomnumber2.nextInt(600) + 100;

				// tilføjer enemies til listen med x og y position
				enemies.add(new Enemy(randomx2, randomy2));
			}

		// sender metoden til myView med enemies
		myView.setGhosts(enemies);

	}

	// metode der resetter timeren - for nedtællingen
	public void clearTimer() {

		// resetter timeren til 60 sekunder
		counter = 60;

		// finder textview for timeCounter
		TextView timer = (TextView) findViewById(R.id.timeCounter);

		// viser tiden der går
		timer.setText("Timer: " + counter);

		// if sætning der sætter counter ud fra hvilket level man er i - dette er min måde at gøre hvert level sværere at gennemføre
		if(level == 2) {

			// sætter tiden til at være 10 sek lavere i level 2
			counter = 50;

		} else if (level == 3) {

			// sætter tiden til at være 10 sek lavere i level 3
			counter = 40;

		} else if (level == 4) {

			// sætter tiden til at være 10 sek lavere i level 4
			counter = 30;

		} else if (level == 5) {

			// sætter tiden til at være 10 sek lavere i level 5
			counter = 30;

		} else if (level == 6) {

			// sætter tiden til at være 10 sek lavere i level 6
			counter = 20;

		} else if (level == 7) {

			// sætter tiden til at være 10 sek lavere i level 3
			counter = 10;

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


	// Gemmer værdierne til når skærmen skal vendes
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// gemmer coins, level, points, gameRunning, counter og enemies i savedInstanceState
		outState.putParcelableArrayList("goldcoins", goldcoins);
		outState.putInt("level", level);
		outState.putInt("points", points);
		outState.putBoolean("gameRunning", gameRunning);
		outState.putInt("counter", counter);
		outState.putParcelableArrayList("enemies", enemies);

	}
}
