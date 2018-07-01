

import java.io.BufferedReader;
import java.io.Console;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class GameUser {
	
	static Scanner scn;
	static String status;
	static String input;
	static String output="none";
	static String username = "quangky";
	static int account = 3000;
	static int numofCards;
	static String[]  array;
	static String[] cardsOnHand = new String[5];
	static String hostname,hostacc,p1name,p2name,p3name,p1acc,p2acc,p3acc,p1bet,p2bet,p3bet;
	static String hostNumofCards,p1NumofCards,p2NumofCards,p3NumofCards;
	
	public static void main(String[] args) {
		try {
//			ServerSocket ss= new ServerSocket(11111);
			System.out.println("Hello, I am Server, I am already for our connection \n");
			Socket socket = new Socket("LocalHost",11111);
			System.out.println("Some one connected");
			scn = new Scanner (System.in);
			PrintStream out = new PrintStream(socket.getOutputStream());
			InputStreamReader in = new InputStreamReader(socket.getInputStream());
			BufferedReader br = new BufferedReader (in);

			String Str = "HITOFFER/";
			
			boolean temp = true;
			
			Str = br.readLine();
			
			while (temp) {
				
				switch (getOrder(Str)){
				case "BETREQUEST":
					scn = new Scanner (System.in);
					System.out.println("Please Enter your Bet ammount: ");
					input = scn.nextLine();
					output = "BET/"+username+"/"+account+"/"+input+"/";
//					scn.close();
					break;
				case "FIRSTUPDATE":
					array = parseToArray(Str);
					
					hostname = array[1];
					hostacc = array[2];
					p1name= array[3];
					p1acc = array[4];
					p1bet = array[5];
					
					if(array.length<=10) {
						p2name= array[6];
						p2acc = array[7];
						p2bet = array[8];
					}  else { 
						p2name= array[6];
						p2acc = array[7];
						p2bet = array[8];
						p3name= array[6];
						p3acc = array[7];
						p3bet = array[8];
					}
					
					break;
				case "UPDATE":
					array = parseToArray(Str);

					p1NumofCards = array[1];
					
					if (array.length==2) {
						p2NumofCards = array[2];
					} else {
						p2NumofCards = array[2];
						p3NumofCards = array[3];
					}
					
					break;
				case "FIRSTCARDS":
					array = parseToArray(Str);
					cardsOnHand[0] = array[2];
					cardsOnHand[1] = array[3];
					numofCards = 2;
					output = "Received";
					
					printOutCards();
					break;
				case "HITOFFER":
					
					System.out.println("You want to HIT? (Y,N)");
					
					input = scn.nextLine();
					
					
					if (input.trim().equals("Y") || input.trim().equals("y")) {
						output = "HIT/"+ username +"/";
					} else if(input.trim().equals("n") || input.trim().equals("N")) {
						output = "STAND/"+ username +"/";
					} else {
						System.out.println("Re-enter your option");
						input = scn.nextLine();
					}
//					scn.close();
					
					break;
				case "HITCARD":
					array = parseToArray(Str);
					cardsOnHand[numofCards] = array[2];
					numofCards++;
					output = "received";
					printOutCards();
					break;
				case "BUST":
					array = parseToArray(Str);
					status = array[1];
					System.out.println("You "+ status);
					temp = false;
					output ="received";
					break;
				case "END":
					array = parseToArray(Str);
					System.out.print("Your account: "+array[1]);
					output ="received";
				default:
					break;
				}
				
				
				out.println(output);
				Str = br.readLine();
				
				
			}
	
				//SEND OUTPUT to HOST
			out.close();
				
			socket.close();
//			ss.close();

		
		}catch(Exception e){
			System.out.println("Server does not work properly \n");
		}
		
		//Receive A String from Host
		
		//Parse a String
		
		//Follows the Order

		
	}
	static void printOutCards() {
		String temp = "Cards: ";
		
		for (int i=0;i<numofCards;i++) {
			temp += cardsOnHand[i]+" ";
		}
		
		System.out.println(temp);
		
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
				return temp;
			} 
		}
		return "none";
	}

}
