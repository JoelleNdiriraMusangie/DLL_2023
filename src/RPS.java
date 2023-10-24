import java.util.Locale;
import java.util.ResourceBundle;

import java.util.Scanner;

public class RPS {
	private Player player;
	private Bot bot;
	private Scoreboard scoreboard;
	private ResourceBundle messages;
	//enregistrement du temps de début
	private long gameStartTime; 
	//Ajout var pour le temps total
	private double gameDuration;



	static Scanner sc = new Scanner(System.in);

	public RPS() {
		try {
			messages = ResourceBundle.getBundle("text");
		} catch (Exception e) {
			// if the default system local doesn't exist we will default to french
			messages = ResourceBundle.getBundle("text", Locale.FRENCH);
		}
		// explicitly set the local to english for testing
		// messages = ResourceBundle.getBundle("text", Locale.ENGLISH);
		player = new Player(messages);
		bot = new Bot();
		scoreboard = new Scoreboard(player, bot, messages);
	}

	public static void main(String[] args) {
		RPS rps = new RPS();
		rps.getPlayerName();
		rps.getRules();
		rps.getNumberOfRound();
		char gameModeRPS = rps.getGameMode();
		rps.startGame(gameModeRPS);
	}

	// Fonction appelee recursivement tant que le joueur souhaite jouer
	public void startGame(char gameModeRPS) {
		int playerSign = player.selectSign(gameModeRPS);
		Controller.display(player.getName(), playerSign, messages);
		int botSign = bot.selectSign(gameModeRPS);
		Controller.display("bot", botSign, messages);
		
		//enregister le temps de début de la manche
		long roundStartTime = System.currentTimeMillis();

		int result = Controller.compareSigns(playerSign, botSign);
		switch (result) {
		case 0: 
			System.out.println(messages.getString("egalite"));
			break;
		case 1: 
			System.out.println(player.getName()+ " " + messages.getString("player_win"));
			player.incrementScore();
			break;
		case -1: 
			bot.incrementScore();
			System.out.println(messages.getString("bot_win"));
			break;
		}

		scoreboard.displayScore();

		//calcul de la duréee de la manche
		double roundDuration= (System.currentTimeMillis()- roundStartTime) /1000.0;
		System.out.println("le Temps de la manche est :" +roundDuration +"Secondes");


		// Demande a l'utilisateur s'il veut jouer a nouveau
		int nbRound = player.getNbOfRound();
		int playerCount = player.getScore();
		int botCount = bot.getScore();
		if (playerCount == nbRound || botCount == nbRound){
			System.out.println(messages.getString("game_over"));
			if (gameDuration == 0 ){
				//on calcul le temps réel de jeu si ce n'est pas fait

				gameDuration = (System.currentTimeMillis() - gameStartTime) / 1000.0;
			}

			System.out.println ("Le Temps total de jeu est : " + gameDuration +"secondes");

			player.resetScore();
			bot.resetScore();
			if (player.playAgain()) {
				System.out.println();
				getNumberOfRound();
				startGame(gameModeRPS);
			} else {
				System.out.println(messages.getString("game_over"));
			}
		 }
		 else { 
			startGame(gameModeRPS);
		}
	}

	// Fonction utilisee pour demander le nom du joueur
	public void getPlayerName() {
		player.askName();
	}

	// Fonction utilisee pour demander si le joueur veut voir les rapports de puissances des signes
	public void getRules() {
		player.askRules();
	}
	
	
	public void getNumberOfRound(){
		player.askNumberOfRound();
		
		//enrgistrer le temps de début du jeu au début du jeu
		gameStartTime = System.currentTimeMillis();
	}

	public char getGameMode(){
		System.out.println(messages.getString("select_game_mode"));
		String gameMode = sc.next();
		char c = gameMode.charAt(0);
		if(c != 'C' && c != 'B'){
			getGameMode();
		}
		return c;
	}
}
