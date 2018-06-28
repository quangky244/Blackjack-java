import java.util.List;
import java.util.Vector;

public enum Request {
	WAIT_FOR_PLAYER,PLAYER_ADDED, CONFIRM_PLAYER_AMMOUNT, WAIT_FOR_FIRST_DEAL,PLAYER_ADDED_WITHBET,
	PLAYER_BET_ADDED,HIT_REQUEST,PLAYER_IS_BUSTED,HIT_ERROR,PLAYER_STAND_SUCCESS, PLAYER_STAND_ERROR,
	BUST_READY, BUST_NOT_READY,
	
	HOST_DRAW, HOST_PICK, HOST_HIT, HOST_BUSTED, HOST_HIT_CARD, HOST_BUST, HOST_BUST_LOSE, HOST_BUST_WIN, HOST_BUST_DRAW, CASE_ERROR, 
	ENDGAME_ERROR, GAME_ENDED
	;
	
	public String hitCard="";
	public String username ="";
	public List<String> leftOutPlayer = new Vector<String>();
	public List<String> notStandPlayer = new Vector<String>();
	public String bustPlayer;
	public int playerScore;
	public List<String[]> firstDealCards = new Vector<String[]>();
	public int numOfPlayer;
	
	public List<String> returnAccount = new Vector<String>();
	
	public void addLeftOut(String user) {
		leftOutPlayer.add(user);
	}
	
	public void addReturnAccount(String user, int account) {
		returnAccount.add(user);
		String a="";
		a+=account;
		returnAccount.add(a);
	}
	public void setScore(int s) {
		playerScore = s;
	}
	public int getScore() {
		return playerScore;
	}
	
	public int getNumOfPlayer() {
		return numOfPlayer;
	}
	public void setNumOfPlayer(int n) {
		numOfPlayer = n;
	}
	public void setFirstDeal(String[] a) {
		firstDealCards.add(a);
	}
	public void addNotStandPlayer(String notStandUsername) {
		notStandPlayer.add(notStandUsername);
	}
	public int getNumberOfNotStandUser() {
		return notStandPlayer.size();
	}
	public void setUsername(String username) {
		this.username=username;
	}
	public String getUsername() {
		return username;
	}
	public void setHitCard(String card) {
		hitCard = card;
	}
	public void printInfo() {
		System.out.println("username: "+username);
		System.out.println("hitCard: "+hitCard);
	}
	public String getBustPlayer() {
		// TODO Auto-generated method stub
		return bustPlayer;
	}
	public void setBustPlayer(String bustPlayer) {
		// TODO Auto-generated method stub
		this.bustPlayer = bustPlayer;
		
	}
	
}