import java.util.List;
import java.util.Scanner;
import java.util.Vector;

public class gameRunner {
	public static gameEngine game = new gameEngine();
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String player1 = "quangky";
		String player2 = "DoBao";
		List<Player> players = new Vector<Player>();
		
		
		players.add(new Player(player1,2500));
		players.add(new Player(player2,2500));
		
		int numOfPlayer;
				
		//Add Players
		game.addPlayer(player1, 2500);
		game.addPlayer("duckhai", 2500);
		game.addPlayer(player2, 2500);
		
		//Add Bet to 2 players
		game.addBet(player1, 50);
		game.addBet(player2, 40);
		
		Request receive = game.startDeal();
		
		System.out.println("Starting Deal");
		
		switch (receive) {
		case WAIT_FOR_PLAYER:
			//Wait for player to bet or use forceDeal() to ignore the player
			receive = game.forceDeal();
			System.out.println("ForceDealed");
			numOfPlayer = receive.getNumOfPlayer();
		case WAIT_FOR_FIRST_DEAL:
			//give the first 2 cards to players
			numOfPlayer = receive.getNumOfPlayer();
			
			break;
		default:
			numOfPlayer = receive.getNumOfPlayer();
			break;
		}
		Scanner scn;
		scn = new Scanner(System.in);
		for (int i=0 ; i< numOfPlayer;i++) {
			boolean hit;
			game.printAllPlayerStatus();
			System.out.println(players.get(i).getUsername()+"HIT? (true/false)");
			scn = new Scanner(System.in);
			hit = scn.nextBoolean();
			Request req = Request.HIT_REQUEST;
			req.setUsername(players.get(i).getUsername());
			while(hit && !players.get(i).busted) {
				req = game.dealHitCard(req);
				switch (req) {
				case PLAYER_IS_BUSTED:
					players.get(i).busted=true;
					System.out.println("Score: "+req.playerScore+" HitCard: "+req.hitCard);
					break;
				case HIT_REQUEST:
					players.get(i).addCard(req.hitCard);
					System.out.println("Score: "+req.playerScore+" HitCard: "+req.hitCard);
					System.out.println("HIT? (true/false)");
					hit = scn.nextBoolean();
					break;
				default:
					break;
				}
			}
			req = game.standRequest(req);
		}
		
		
		Request req2 = game.startBust();
		
		switch (req2) {
		case BUST_NOT_READY:
			System.out.println("Bust is not ready");
			break;
		case BUST_READY:
			System.out.println("Let's get started!");
			break;
		default:
			break;
		}
		Request req3;
		Player host = new Player(game.host);
//		Scanner scn = new Scanner(System.in);
		while (!game.allPlayerChecked()&&!host.isBusted()) {
			System.out.println("Cards: " +host.getHoldingCards());
			System.out.println("Sum: "+host.sum());
			System.out.println("Hit?");
			boolean hit; 
			hit = scn.nextBoolean();
			
			if (hit) {
				req3 = Request.HOST_HIT;
			} else {
				System.out.println("Enter username: ");
				String tmp = scn.nextLine().trim();
				tmp = scn.nextLine().trim();
				System.out.println("Bust user: "+tmp);
				req3 = Request.HOST_BUST;
				req3.setBustPlayer(tmp);
			}
			
			req3 = game.Bust(req3);
			switch(req3) {
			case HOST_HIT_CARD:
//				host.addCard(req3.hitCard);
				System.out.println("Cards: " +host.getHoldingCards());
				System.out.println("Host Score is: "+host.sum());
				break;
			case HOST_BUST_WIN:
				for(int i = 0; i<players.size();i++) {
					if (players.get(i).getUsername() == req3.getBustPlayer()) {
						System.out.println(players.get(i).getUsername()+" Lost");
					}
				}
				System.out.println(req3.bustPlayer+" lost");
				break;
			case HOST_BUST_LOSE:
				for(int i = 0; i<players.size();i++) {
					if (players.get(i).getUsername() == req3.getBustPlayer()) {
						System.out.println(players.get(i).getUsername()+" Won");
					}
				}
				System.out.println(req3.bustPlayer+" won");
				break;
			case HOST_BUST_DRAW:
				for(int i = 0; i<players.size();i++) {
					if (players.get(i).getUsername() == req3.getBustPlayer()) {
						System.out.println(players.get(i).getUsername()+" Chase");
					}
				}
				System.out.println(req3.bustPlayer+" chase");
				break;
			case HOST_BUSTED:
				break;
			default:
				break;
			}	
		}
		
		System.out.println("Host is busted");
		
		scn.close();
		if (host.isBusted()) {
			game.bustedPay();
		}
		Request req4;
		req4 = game.endGame();
		switch (req4) {
		case GAME_ENDED:
			for (Player p : players) {
				for (int i=0;i<req4.returnAccount.size();i=i+2) {
					if (p.getUsername() == req4.returnAccount.get(i)) {
						p.totalAccount = Integer.parseInt(req4.returnAccount.get(i+1));
					}
				}
			}
			break;
		default:
			System.out.println("Something went Wrong");
		}
		
		
		
	}

}
