
import java.io.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

public class gameEngine {

	static int curCard=0;
	static int numberOfPlayers;
	static List<Player> playerList = new Vector<Player>();
	static List<String> deckList = new Vector<String>();
	Player host; 
	
	static String[] deck = {
			"2S","3S","4S","5S","6S","7S","8S","9S","10S","JS","QS","KS","AS",
			"2C","3C","4C","5C","6C","7C","8C","9C","10C","JC","QC","KC","AC",
			"2D","3D","4D","5D","6D","7D","8D","9D","10D","JD","QD","KD","AD",
			"2H","3H","4H","5H","6H","7H","8H","9H","10H","JH","QH","KH","AH"
	};
	
	
	public gameEngine(String usrname, int account) {
		// TODO Auto-generated constructor stub
		host = new Player(usrname, account);
	}

	public void main(String[] args) {

		
		addPlayer("quangkytran", 2500, 50);
		addPlayer("Baovtt", 2500, 250);
		addPlayer("KhaiKD", 2500);
//		addDeck();
//		deal();
		
		showPlayersCards();
		showHostCards();
		
		Request rqst = Request.HIT_REQUEST;
		rqst.username="quangkytran";
		dealHitCard(rqst);
		showPlayersCards();
		dealHitCard(rqst);
		System.out.println("\n=========\n");
		dealHitCard(rqst);
		showPlayersCards();
		System.out.println("\n=========\n");
		dealHitCard(rqst);
		showPlayersCards();
		System.out.println("\n=========\n");
//		Request rqst = Request.HIT_REQUEST;
//		rqst.username="quangkytran";
//		
//		dealHitCard(rqst);
//		rqst.printInfo();
//		showPlayersCards();
//		System.out.println("\n=========\n");
//		dealHitCard(rqst);
//		rqst.printInfo();
//		showPlayersCards();
//		System.out.println("\n=========\n");
//		dealHitCard(rqst);
//		showPlayersCards();
//		rqst.printInfo();
//		System.out.println("\n=========\n");
		
	}
//	public static Request startOffer() {
//		//check requirements to Start offering
//		
//		for (int i =0;i<playerList.size();i++) {
//			Player curr = playerList.get(i);
//			while (!curr.doneHit) {
//				offer()
//			}
//		}
//		
//		return Request;
//	}
	
	
	
	public Request Bust(Request rqst) {
		Request req;
		
		switch (rqst){
		case HOST_HIT:
			String card;
			if(!host.isBusted()) {
				req = Request.HOST_HIT_CARD;
				card = takeOneCard();
				host.addCard(card);
				req.setHitCard(card);
				return req;
			}
			req = Request.HOST_BUSTED;
			return req;
			
		case HOST_BUST:
			if (!host.isBusted()) {
//				System.out.println("GE: bust: "+rqst.getBustPlayer());
				switch (isWin(rqst.getBustPlayer())) {
				case 0:
					req = Request.HOST_BUST_LOSE;
					req.setBustPlayer(rqst.getBustPlayer());
					return req;
					
				case 1:
					req = Request.HOST_BUST_WIN;
					req.setBustPlayer(rqst.getBustPlayer());
					return req;
					
				case 2:
					req = Request.HOST_BUST_DRAW;
					req.setBustPlayer(rqst.getBustPlayer());
					return req;
				
				default: 
					break;
				}
			}
			req = Request.HOST_BUSTED;
			return req;
			
			default:
				break;
		}
		req = Request.CASE_ERROR;
		return req;
	}
	public void bustedPay() {
		for (int i = 0; i<playerList.size();i++) {
			if (!playerList.get(i).getCheckStatus() && playerList.get(i).sum()<22){
				playerList.get(i).setWin();
			} else if (!playerList.get(i).getCheckStatus() && playerList.get(i).sum()>=22){
				playerList.get(i).setChase();
			} 
		}
	}
	
	private static int getInGamePlayer() {
		int n=0;
		for(int i=0;i<playerList.size();i++) {
			if (playerList.get(i).isBet()) {
				n++;
			}
		}
		return n;
	}
	
	public boolean allPlayerChecked() {
		for(int i=0;i<playerList.size();i++) {
			if (!playerList.get(i).getCheckStatus()) {
				return false;
			}
		}
		return true;
	}
	
	public Request endGame() {
		Request req;
		rewardChargeReturn();
		printAllPlayerStatus();
		req = Request.GAME_ENDED;
		for (Player p : playerList) {
			req.addReturnAccount(p.getUsername(), p.totalAccount);
		}
			
		return req;
	}
	public void printAllPlayerStatus() {
		for(int i=0;i<playerList.size();i++) {
			playerList.get(i).printPlayerStat();
		}
	}
	
	private static void rewardChargeReturn() {
		for(int i=0;i<playerList.size();i++) {
			if (playerList.get(i).win) {
				playerList.get(i).reward();
			} else if (playerList.get(i).chase) {
				playerList.get(i).chase();
			} else {
				playerList.get(i).charge();
			}
		}
	}
	
	public Request startBust() {
		Request req = Request.BUST_NOT_READY;
		int playerChecked=0;
		for (int i=0; i<playerList.size();i++) {
			if (playerList.get(i).isDoneHit()==playerList.get(i).isBet()) {
				playerChecked++;
			}else {
				req = Request.BUST_NOT_READY;
				req.addNotStandPlayer(playerList.get(i).getUsername());
			}
		}
		if (playerChecked == playerList.size()) return Request.BUST_READY; 
		else return req;
	}
	
	
	public Request dealHitCard(Request rqst) {
		for	(int i = 0;i<playerList.size();i++) {
			if (playerList.get(i).username==rqst.getUsername()&&playerList.get(i).isBet() && !playerList.get(i).isBusted() &&!playerList.get(i).isDoneHit() ) {
				System.out.println("Dealing for: "+rqst.getUsername());
				String tempcard = deckList.get(0);
				deckList.remove(0);
				
//				rqst.setScore(playerList.get(i).sum());
				playerList.get(i).addCard(tempcard);
				if (playerList.get(i).isBusted()) {
					rqst= Request.PLAYER_IS_BUSTED;
				}
				rqst.setScore(playerList.get(i).sum());
				rqst.setHitCard(tempcard);
				return rqst;
			} else if (playerList.get(i).username==rqst.getUsername()&&playerList.get(i).isBet() && playerList.get(i).isBusted()) {
				rqst = Request.PLAYER_IS_BUSTED;
			
				return rqst;
				
			}
		}
		Request err = Request.HIT_ERROR;
		return err;
	}
	
	public Request standRequest(Request rqst) {
		for	(int i = 0;i<playerList.size();i++) {
			if(playerList.get(i).getUsername() == rqst.getUsername()) {
				playerList.get(i).doneHit();
				Request req = Request.PLAYER_STAND_SUCCESS;
				req.setUsername(rqst.getUsername());
				return req;
			}
		}
		Request req = Request.PLAYER_STAND_ERROR;
		req.setUsername(rqst.getUsername());
		return req;
	}	
	
	public Request startDeal() {
		if(areAllPlayersReady()){
			System.out.println("AllplayersReady");
			addDeck();
			shuffle();
			deal();
		} else {
			return Request.WAIT_FOR_PLAYER;
		}
		Request firstDeal = Request.WAIT_FOR_FIRST_DEAL;
		for (int i=0; i<playerList.size();i++) {
			firstDeal.setFirstDeal(playerList.get(i).getFirstDealedCard());
		}
		firstDeal.setNumOfPlayer(playerList.size());
		return firstDeal;
	}
	
	public Request forceDeal() {
		int i=0;
		Request req = Request.WAIT_FOR_FIRST_DEAL;
		while(playerList.size()!=i) {
			if (!playerList.get(i).isBet()) {
				System.out.println(playerList.get(i).getUsername() +" is removed.");
				req.addLeftOut(playerList.get(i).getUsername());
				playerList.remove(i);
			}
			else i++;
			
		}
//		System.out.println("Size = "+ playerList.size());
		addDeck();
		shuffle();
		deal();
//		System.out.println("Size = "+ playerList.size());
		
		req.setNumOfPlayer(playerList.size());
		for (int j=0; j<playerList.size();j++) {
			req.setFirstDeal(playerList.get(j).getFirstDealedCard());
		}
		return req;
	}
	
	public static void showDeck(int n,boolean shuffled) {
		addDeck();
		System.out.println("deckList.size()="+deckList.size());
		if (shuffled) {
			shuffle();
		}
		for (int i=0;i<n;i++) {
			System.out.println(deckList.get(i));
		}
	}
	
	
	
	public static void setNumberOfPlayers(int n) {
		numberOfPlayers = n;
	}
	
	public Request addPlayer(String username, int account) {
		playerList.add(new Player(username, account));
		return Request.PLAYER_ADDED;
	}
	
	public static Request addPlayer(String username, int account, int betAmmount) {
		playerList.add(new Player(username, account,betAmmount));
		return Request.PLAYER_ADDED_WITHBET;
	}
	
	public Request addBet(String username, int betAmmount) {
	
		for (int i = 0;i<playerList.size();i++) {
	
			if (username.equals(playerList.get(i).username)) {
				playerList.get(i).addBet(betAmmount);
			}
		}
		return Request.PLAYER_BET_ADDED;
	}
	private void showHostCards() {
		System.out.println("Host's Cards: " + host.getHoldingCards());
	}
	private static void showPlayersCards() {
		for (int i=0;i<playerList.size();i++) {
			System.out.println(playerList.get(i).getPlayerInfo());
			System.out.println("Cards: "+ playerList.get(i).getHoldingCards() +"\n");
		}
	}
	private static void showPlayers() {
		for (int i=0;i<playerList.size();i++) {
			System.out.println(playerList.get(i).getPlayerInfo());
		}
	}
	
	private static int getNumberOfPlayer() {
		return playerList.size();
	}
	private static void addDeck() {
		for(int i = 0; i< deck.length;i++) {
			deckList.add(deck[i]);
		}
	}
	private static void shuffle() {
		for(int i = 0; i<deckList.size();i++) {
			int index = (int)(Math.random()*deck.length);
			Collections.swap(deckList,i,index);
		}
	}
	private static boolean areAllPlayersReady() {
		int n=0;
		System.out.println("GE: playerList size: "+ playerList.size());
		for (int i=0;i<playerList.size();i++) {
			if (playerList.get(i).isBet()) {
			
				n++;
				System.out.println(playerList.get(i).getUsername()+" Bet "+ playerList.get(i).betAmmount);
			}
		}
		if (n==playerList.size()) {
			return true;
		} else {
			return false;
		}
	}
	private void deal() {
		for (int j = 1; j<=2; j++) {
			for (int i=0; i< playerList.size();i++) {
				String newCard = deckList.get(0);
				deckList.remove(0);
				playerList.get(i).addCard(newCard);
				
			}
			String newCard = deckList.get(0);
			deckList.remove(0);
			host.addCard(newCard);
		}
	}
	
	private static void dealOneCard(int Playernumber) {
		String newCard = deckList.get(0);
		deckList.remove(0);
		playerList.get(Playernumber).holdingCards.add(newCard);
	}
	
	private int isWin(String user) {
		int i=0;
		for (i=0;i<playerList.size();i++) {
			System.out.println(playerList.get(i).getUsername());
			if(playerList.get(i).getUsername().equals(user)) {
				playerList.get(i).check();
				break;
			}
			if (i== playerList.size()-1) {
				System.out.println("Cant Find User");
			}
			System.out.println(i);
		}
		
//		if()
		
		if ((playerList.get(i).sum() < host.sum()  && !host.isBusted())|| playerList.get(i).isBusted()||(host.isBlackjack() && !playerList.get(i).isBlackjack()) ) {
			return 1; //host win
		}
		else if ((playerList.get(i).sum() == host.sum() && !playerList.get(i).isBusted()) ||(host.isBlackjack() && playerList.get(i).isBlackjack()) ) {
			playerList.get(i).setChase(); 
			return 2; //draw
		}
		else if((!host.isBlackjack() && playerList.get(i).isBlackjack())) {
			playerList.get(i).setWin() ;
			return 0;
		}
		else 
//		if (playerList.get(i).sum() > host.sum() && !playerList.get(i).isBusted()) 
		{
			playerList.get(i).setWin() ;
			return 0;//host lose
		}
	}
	
	private static String takeOneCard() {
		String card = deckList.get(0);
		deckList.remove(0);
		return card;
	}
	
//	public static void createPlayerList() {
//		
//	}
	
	
	
//	public static void setPlayerInfo(String username) {
//		
//	}
	
}
