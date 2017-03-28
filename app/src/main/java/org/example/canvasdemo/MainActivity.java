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

	static Context context;

	// bruges til point for Pacman
	int points = 0;

	// timer der skal bruges til at "animere" Pacman
	private Timer timer;

	// timer til countdown
	private Timer countdown;

	// timer til enemy
	private Timer enemytimer;

	// counter for countdown
	private int counter = 10; // skal ændres til 10 sek til debug

	// game running
	public boolean gameRunning = false;

	// direction value for at holde styr på hvor Pacman bevæger sig hen
	public int pacmanDirection = 1;

	public int ghostDirection = 1;

	// bestemmer at level skal være 1
	public int level = 1;


	// metode til at tjekke om spillet er slut/tabt
	public boolean isGameOver() {


		// skal loope alle mønter igennem, så den tjekker om alle mønterne er taget
		boolean allTaken = true; // true

		// skal kædes sammen med myView - skal være samme for loop som myView??
		for (GoldCoin coin : myView.goldcoins) {

			// tilgår coinTaken fra GoldCoin klassen
			boolean cointaken = coin.isCoinTaken();

			// hvis coin ikke er taget, er allTaken false
			if (!cointaken) {
				allTaken = false;
			}
		}

		boolean pacmanDied = false;

		// skal kædes sammen med myView - skal være samme for loop som myView??
		for (Enemy enemy : myView.enemies) {

			// tilgår enemyhit fra Enemy klassen
			boolean enemyhit = enemy.didEnemyHit();

			// hvis enemy rammes, er pacmanDied true, da pacman så dør
			if (enemyhit) {
				pacmanDied = true;
			}
		}

		// hvis alle mønter er taget inden tiden er gået, så skal spilleren komme videre
		if(allTaken == true && pacmanDied == false) {

			// laver en toast der kommer frem, når Pacman har taget alle coins og et level er gennemført
			// toasten udskriver hvilket level, man har gennemført
			Toast toast = Toast.makeText(context,
					"Level " + level + " completed", Toast.LENGTH_SHORT);
			toast.show();

			// stopper spillet!
			gameRunning = false;

			// increaser level for hver gang man gennemfører det tidligere level
			level += 1;

			// kalder metode, der resetter Pacman, counter og enemies position for hvert level
			myView.newLevel(50, 2);

		}


		// hvis spilleren ikke har fået alle coins og tiden er gået eller Pacman rørte fjenden er der game over!
		if(allTaken == false && counter == 0 || pacmanDied == true) {

			// laver en toast, der skriver "Game over"
			Toast toast = Toast.makeText(context,
					"Game Over!", Toast.LENGTH_SHORT);
			toast.show();

			// stopper spillet
			gameRunning = false;

			return true;
		}

		// skal returne false
		return false;
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		this.context = this;



		// finder alle knapper
		Button rightbutton = (Button) findViewById(R.id.moveRightButton);
		Button leftbutton = (Button) findViewById(R.id.moveLeftButton);
		Button upbutton = (Button) findViewById(R.id.moveUpButton);
		Button downbutton = (Button) findViewById(R.id.moveDownButton);
		Button newgamebutton = (Button) findViewById(R.id.newGameButton);
		Button resumebutton = (Button) findViewById(R.id.resumeButton);
		Button pausebutton = (Button) findViewById(R.id.pauseButton);
		// finder custom view
		myView = (MyView) findViewById(R.id.gameView);

		// ny timer - for pacman
		timer = new Timer();

		// ny timer for countdown
		countdown = new Timer();

		// enemy timer
		enemytimer = new Timer();

		// starter spillet (hvis gameRunning er true)
		gameRunning = true;

		// kalder timeren og bestemmer at det skal være en ny TimerTask
		// samt at den vil køre timeren med det samme og med 200 milisekunder
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				PacmanRunnerMethod();
			}
		}, 0, 200);

		// timer for countdown for spil
		countdown.schedule(new TimerTask() {
			@Override
			public void run() {
				TimerRunnerMethod();
			}
		}, 0, 1000);

		// timer for enemies
		enemytimer.schedule(new TimerTask() {
			@Override
			public void run() {
				EnemyRunnerMethod();
			}
		}, 0, 200);


		//listener of our pacman - for all the buttons
		rightbutton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				// kalder metoden moveRight for pacman fra MyView
				myView.moveRight(10);

				// sætter vejen Pacman går til 1
				pacmanDirection = 1;
			}
		});
		leftbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// kalder metoden moveLeft for pacman fra MyView
				myView.moveLeft(10);

				// sætter vejen Pacman går til 2
				pacmanDirection = 2;
			}
		});
		upbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// kalder metoden moveUp for pacman fra MyView
				myView.moveUp(10);

				// sætter vejen Pacman går til 3
				pacmanDirection = 3;
			}
		});
		downbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// kalder metoden moveDown for pacman fra MyView
				myView.moveDown(10);

				// sætter vejen Pacman går til 4
				pacmanDirection = 4;
			}
		});
		newgamebutton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				// sætter point til at være 0 igen
				points = 0;

				// bruger metode i custom view, der vil resette Pacman
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

		// kalder goldcoins metoden
		resetGoldCoins();

		// kalder enemies metoden
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
				//counter++;

				// finder textview
				//TextView timer = (TextView) findViewById(R.id.timeCounter);

				// viser tiden der går
				//timer.setText("Timer: " + counter);

				// denne if-sætning tjekker på hvilken direction Pacman har valgt at gå
				// værdien sættes oppe ved click listeners på knapperne, som ændres, når
				// man klikker på de forskellige bevæge-knapper
				// her bevæger han sig den vej du gerne vil have med timeren
				if(pacmanDirection == 1) {
					// skal flytte Pacman til højre
					myView.moveRight(20);

				} else if(pacmanDirection == 2) {

					// skal flytte Pacman til venstre
					myView.moveLeft(20);

				} else if(pacmanDirection == 3) {

					// skal flytte Pacman upad
					myView.moveUp(20);

				} else if(pacmanDirection == 4) {

					// skal flytte Pacman nedad
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
				counter--;

				// finder textview
				TextView timer = (TextView) findViewById(R.id.timeCounter);

				// viser tiden der går
				timer.setText("Timer: " + counter);


			}
		}
	};

	// runnable for enemies
	private Runnable EnemyRunning = new Runnable() {

		@Override
		public void run() {
			if(gameRunning) {
				// tjekke om fjenden bevæger sig opad eller nedad
//				if(pacmanDirection == 1) {
//					ghostDirection = 1;
//					myView.moveEnemyDown(20);
//				} else if(pacmanDirection == 2) {
//					ghostDirection = 2;
//					myView.moveEnemyUp(20);
//				} else if(pacmanDirection == 3) {
//					ghostDirection = 3;
//					myView.moveEnemyLeft(20);
//				} else if(pacmanDirection == 4) {
//					ghostDirection = 4;
//					myView.moveEnemyRight(20);
//				}

				myView.moveEnemyDown(20);
			}
		}
	};

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

	// laver goldcoins
	public void resetGoldCoins() {
		// for loop der kører igennem 0-10 forskellige goldcoins
		for (int i = 0; i < 10; i++) {

			// laver et random tal mellem 0 og 100
			Random randomnumber = new Random();
			int randomx = randomnumber.nextInt(600) + 100;
			int randomy = randomnumber.nextInt(600) + 100;

			// tilføjer det til listen, så der er 10 forskellige goldcoins
			goldcoins.add(new GoldCoin(randomx, randomy));
		}

		// sender metoden til myView med goldcoins
		myView.setCoins(goldcoins);
	}

	// lavar enemies med random placering
	public void spawnEnemies() {

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

	public void clearTimer() {

		// resetter timeren
		counter = 60;

		// finder textview
		TextView timer = (TextView) findViewById(R.id.timeCounter);

		// viser tiden der går
		timer.setText("Timer: " + counter);

		if(level == 2) {

			// sætter tiden til at være lavere i level 2
			counter = 50;

		} else if (level == 3) {

			// sætter tiden til at være endnu mindre i level 3
			counter = 40;

		} else if (level == 4) {

			// sætter tiden til at være endnu mindre i level 4
			counter = 30;

		} else if (level == 5) {

			// sætter tiden til at være endnu mindre i level 5
			counter = 30;

		} else if (level == 6) {

			// sætter tiden til at være endnu mindre i level 6
			counter = 20;

		} else if (level == 7) {

			// sætter tiden til at være endnu mindre i level 3
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
}
