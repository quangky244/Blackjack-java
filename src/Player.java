import java.util.List;
import java.util.Vector;

public class Player  {
	public String username;
	public boolean doneHit = false;
	public int totalAccount=0;
	public int betAmmount=0;
	public boolean win=false;
	public boolean chase = false;
	public boolean busted=false;
	public boolean blackjack=false;
	private boolean checked = false;
	
	public List<String> holdingCards = new Vector<String>();
	
	public Player (Player  p){
		p.username = this.username;
		this.holdingCards = p.holdingCards;
		this.totalAccount = p.totalAccount;
	}
	
	public Player(String name,int account, boolean randomCards) {
		username = name;
		totalAccount = account;
		holdingCards.add("2C");
		holdingCards.add("JH");
		holdingCards.add("10D");
		holdingCards.add("AS");
	}
	
	public Player(String name,int account) {
		username = name;
		totalAccount = account;
	}
	public Player (String name,int account, int betAmmount) {
		username = name;
		totalAccount = account;
		this.betAmmount = betAmmount;
	}
	

	
	
	
	public String[] getCardArray() {
		if (holdingCards.size()!=0) {
			String[] card = new String[holdingCards.size()];
			
			for (int i = 0; i< holdingCards.size(); i++) {
				card[i] = holdingCards.get(i);
			}
			return card;
		}
		
		else {
//			String[] card ={"2S","AC","JH","10D","KD"};
			String[] card = {};
			return card;
		}
		
	}
	
	public int getTotalAccount () {
		return totalAccount;
	}
	public boolean isBlackjack() {
		boolean halfjack=false;
		boolean halfblack=false;
		for (int i = 0; i< 2;i++) {
			if ((extractPoint(holdingCards.get(i)) == 1)) {
				halfblack = true;
			} else if ((extractPoint(holdingCards.get(i)) == 10)) {
				halfjack = true;
			}
		}
		if (halfblack && halfjack) return true;
		else return false;
	}
	public String[] getFirstDealedCard() {
		String[] karte = new String[2];
		karte[0] = holdingCards.get(0);
		karte[1] = holdingCards.get(1);
		return karte;
	}
	public void reward() {
		totalAccount += betAmmount;
		betAmmount = 0;
	}
	public void charge() {
		totalAccount -= betAmmount;
		betAmmount = 0;
	}
	public void chase() {
		betAmmount =0;
	}
	private String getStatus() {
		String stat="";
		if(win) {
			stat = " Won";
		} else if (chase) {
			stat = " Chase";
		} else stat = " Lost";
		
		return stat;
	} 
	public void printPlayerStat() {
		System.out.println(username +" "+ totalAccount + "\nCards: "+getHoldingCards()+" "+sum()+getStatus()+" Bet:"+betAmmount);
	}
	
	public static void main(String[] args) {
		System.out.println(extractPoint("10C"));
		System.out.println(extractPoint("JC"));
		System.out.println(extractPoint("KC"));
		System.out.println(extractPoint("9C"));
		System.out.println(extractPoint("AC"));
	}
	public void setWin() {
		win = true;
	}
	public void setChase() {
		chase = true;
	}
	public boolean isDoneHit() {
		return doneHit;
	}
	public void doneHit() {
		doneHit = true;
	}
	public boolean getCheckStatus() {
		return checked;
	}
	public void check() {
		checked = true;
	}
	
	private static int extractPoint(String card) {
		int point=0;
		card = card.replace(card.substring(card.length()-1), "");
		switch (card) {
		case "J": case "Q": case"K":
			point = 10;
			break;
			
		case "A":
			point = 1;
			break;
			
		default:
			point = Integer.parseInt(card);
			break;
		}
		return point;
		
	}
	public int sum() {
		int sum=0;
		int numAces=0;
		for (int i = 0;i<holdingCards.size();i++) {
			String card = holdingCards.get(i);
			int cardPoint = extractPoint(card);
			if (cardPoint == 1) {
				numAces++;
				sum+= 10;
			} else {
				sum+=cardPoint;
			}
			
			while (sum >21 && numAces>0) {
				sum-=9;
				numAces--;
			}
		}
		return sum;
	}
	
	
	public String getHoldingCards() {
		String cards="";
		for (String a : holdingCards) {
			cards+=" "+a;
		}
		
//		cards += " Sum: "+sum();
		
		return cards;
	}

	public String getPlayerInfo() {
		String playerinfo = username + " Account: " +totalAccount;
		if (betAmmount > 0) {
			playerinfo +=" Bet: "+betAmmount;
		}
		return playerinfo;
	}

	public void addBet(int betAmmount) {
		this.betAmmount = betAmmount;
	}
	public void addCard(String newCard) {
		holdingCards.add(newCard);
		if (isBusted()) {
			System.out.println("PlayerBusted");
		}
	}
	public boolean isBet() {
		if (betAmmount!=0) {
			return true;
		} else {return false;}
	}
	public boolean isBusted() {
		if (sum()<=21) {
			busted = false;
		} else {
			busted = true;
			doneHit();
		}
		return busted;
	}
	public String getUsername() {
		return (username);
	}
	public void printScore() {
		System.out.println(sum());
	}
}
