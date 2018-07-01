import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

public class gameRunner {
	public static gameEngine game = new gameEngine("hostname", 2500);
	static String[] array;
	static List<Player> players = new Vector<Player>();
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		
		
		Socket cl= null;
		BufferedReader inp = null;
		PrintWriter outp= null;
		BufferedReader key = null;
		String ipserver = "LocalHost";
		int portserver = 11111;
		String r;
		
		ServerSocket ss= new ServerSocket(11111);
		System.out.println("Hello, I am Server, I am already for our connection \n");
		cl = ss.accept();
		inp = new BufferedReader(new InputStreamReader(cl.getInputStream()));
		outp = new PrintWriter(cl.getOutputStream(),true);
		key = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Server connected");
		
		int numOfPlayer;
		String player1 = "quangky";
		String player2 = "DoBao";
//		List<Player> players = new Vector<Player>();
		
		players.add(new Player(player1,2500));
		players.add(new Player(player2,2500));
		//Add Players
		game.addPlayer(player1, 2500);
		game.addPlayer("duckhai", 2500);
		game.addPlayer(player2, 2500);
		
		//Add Bet to 2 players
		
		Request receive = game.startDeal();
		
		System.out.println("Starting Deal");
		
		switch (receive) {
		case WAIT_FOR_PLAYER:
			//Send this String to Player 
			String BetRequest = "BETREQUEST/";
			outp.println(BetRequest);
//			String tmp = inp.readLine();
			
			array = parseToArray(inp.readLine());
//			System.out.println(array[1]+ array[3]);

			game.addBet(array[1], Integer.parseInt(array[3]));
			String Bet2StrToHost = "BET/DoBao/2500/40/";
//			game.addBet(array[1], Integer.parseInt(array[3]));				// parse to get Info
																			// add bet

			//Wait for player to bet or use forceDeal() to ignore the player
			
			receive = game.forceDeal();
			
			System.out.println("ForceDealed");
			numOfPlayer = receive.getNumOfPlayer();
		case WAIT_FOR_FIRST_DEAL:
			//give the first 2 cards to players
			numOfPlayer = receive.getNumOfPlayer();
			String[] temp2Cards = new String[2];
			
			for (int i = 0 ; i< gameEngine.playerList.size();i++) {
				temp2Cards[0] = receive.firstDealCards.get(i)[0];
				temp2Cards[1] = receive.firstDealCards.get(i)[1];
				outp.println("FIRSTCARDS/"+gameEngine.playerList.get(i)+"/"+temp2Cards[0]+"/"+temp2Cards[1]+"/");
//				System.out.println(inp.readLine());
				inp.readLine();
			}
			
			break;
		default:
			numOfPlayer = receive.getNumOfPlayer();
			break;
		}
		
		
		Scanner scn;
		scn = new Scanner(System.in);
		for (int i=0 ; i< numOfPlayer;i++) {
			boolean hit = false;
//			game.printAllPlayerStatus();
//			System.out.println(players.get(i).getUsername()+"HIT? (true/false)");
			System.out.println("Offering Hit");
			
			String HitOfferFromHost= "HITOFFER/"; // send this to player
			String HitStrToHost = "HIT/username/"; //receive this
			String StandStrToHost = "STAND/username/";// or this
			
			String nayy=gameEngine.playerList.get(i).getUsername();
			
			outp.println("HITOFFER/");
			
			array = parseToArray(inp.readLine());
			if (array[0].equals("HIT")) {
				hit = true;
			} else if(array[0].equals("STAND")) {
				hit = false;
			}
			
			
			Request req = Request.HIT_REQUEST;
			req.setUsername(gameEngine.playerList.get(i).getUsername());
			while(hit && !gameEngine.playerList.get(i).busted) {
				req = game.dealHitCard(req);
				switch (req) {
				case PLAYER_IS_BUSTED:
					players.get(i).busted=true;
					System.out.println("Score: "+req.playerScore+" HitCard: "+req.hitCard);
					outp.println("HITCARD/"+nayy+"/"+req.hitCard+"/");
					inp.readLine();
					
					break;
				case HIT_REQUEST:
					players.get(i).addCard(req.hitCard);
//					System.out.println("Score: "+req.playerScore+" HitCard: "+req.hitCard);
					outp.println("HITCARD/"+nayy+"/"+req.hitCard+"/");
					inp.readLine();
					outp.println("HITOFFER/");
					
					array = parseToArray(inp.readLine());
					if (array[0].equals("HIT")) {
						hit = true;
					} else if(array[0].equals("STAND")) {
						hit = false;
					}
					//Send andRecv 
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
			System.out.println("Start Busting!");
			break;
		default:
			break;
		}
		
		
		Request req3;
		
		
		Player host = new Player(game.host);
//		Scanner scn = new Scanner(System.in);
		while (!game.allPlayerChecked()&&!host.isBusted()) {
			System.out.println("================================\nCards: " +host.getHoldingCards());
			System.out.println("Sum: "+host.sum());
			System.out.println("Hit? (true/false)");
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
				outp.println("BUST/LOST/");
				inp.readLine();
				
				System.out.println(req3.bustPlayer+" lost");
				break;
			case HOST_BUST_LOSE:
				for(int i = 0; i<players.size();i++) {
					if (players.get(i).getUsername() == req3.getBustPlayer()) {
						System.out.println(players.get(i).getUsername()+" Won");
					}
				}
				outp.println("BUST/WON/");
				inp.readLine();
				System.out.println(req3.bustPlayer+" won");
				break;
			case HOST_BUST_DRAW:
				for(int i = 0; i<players.size();i++) {
					if (players.get(i).getUsername() == req3.getBustPlayer()) {
						System.out.println(players.get(i).getUsername()+" Chase");
					}
				}
				outp.println("BUST/CHASED/");
				inp.readLine();
				System.out.println(req3.bustPlayer+" chase");
				break;
			case HOST_BUSTED:
				break;
			default:
				break;
			}	
		}
		
		
		
		scn.close();
		if (host.isBusted()) {
			System.out.println("Host is busted");
			game.bustedPay();
		}
		Request req4;
		req4 = game.endGame();
		switch (req4) {
		case GAME_ENDED:
			for (Player p : gameEngine.playerList) {
				for (int i=0;i<req4.returnAccount.size();i=i+2) {
					if (p.getUsername() == req4.returnAccount.get(i)) {
						p.totalAccount = Integer.parseInt(req4.returnAccount.get(i+1));
					}
				}
				outp.println("END/"+p.totalAccount+"/");
			}
//			outp.println("END/");
			break;
		default:
			System.out.println("Something went Wrong");
		}
		
		inp.close();
		key.close();
		outp.close();
		ss.close();
		cl.close();
		
	}
	static String[] parseToArray(String a) {
		String temp="";
		int k=0;
		int arrayIndex=0;
		for (int i = 0;i<a.length();i++) {
			if (a.charAt(i) == '/') {
				k++;
			} 
		}
		String[] ret = new String[k];
		for (int i = 0;i<a.length();i++) {
			temp+=a.charAt(i);
			if (a.charAt(i) == '/') {
				temp = temp.replace(temp.substring(temp.length()-1), "");
				ret[arrayIndex] = temp;
				arrayIndex++;
				temp = "";
			} 
		}
		return ret;
	}
	static String getOrder(String a) {
		String temp="";
		
		for (int i = 0;i<a.length();i++) {
			temp += a.charAt(i);
			if (a.charAt(i) == '/') {
				temp = temp.replace(temp.substring(temp.length()-1), "");
				System.out.println("temp = "+temp);
				return temp;
			} 
		}
		return "none";
	}
}
