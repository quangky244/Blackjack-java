

import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainWindow extends Application{
	
	Button button;
	static Stage window2;
	static String status;
	static Request req;
	static boolean dealed = false;
	static boolean roomClosed =false;
	static boolean bustReady = false;
	static int k =3;
	static Stage window;
	static Scene loginScene,roomScene, gameScene;
	static String username = "quangky";
	static String password = "hithere";
	static String room[] = {"room1/BlueCap/2","RoOm2/CrystalTiger/1", "ROOM3/RedVelvet/0"};
	static ListView<String> roomList;
	static TableView<roomInfo> roomTable;
	static String players1[]= {"BlackAvocado","GreenLantern","AngryBird"};
	static String cards[]= {"2S","AC","JH","10D","KD"};
	static String[] cardsOnHand = {"2S","AC","JH","10D","KD"}; 
//	static String[] cardsOnHand = new String[5]; 
//	static Collection<Image> imageList;
	static ObservableList<String[]> deadledCards;
	static Player host;
	static List<Player> players = new Vector<Player>();
	static String[] array;
//	static TableView<> player1Stat;
	
	
	public static gameEngine game = new gameEngine();
	
	public static void main(String[] args) {
		
		//Recieved from server
		
		String player1 = "quangky";
		String player2 = "DoBao";
		String player3 = "duckhai";
		
		String account1 = "2500";
		String account2 = "1400";
		String account3 = "3700";
		
		
		players.add(new Player(player1,Integer.parseInt(account1)));
		players.add(new Player(player2,Integer.parseInt(account2)));
		players.add(new Player(player3,Integer.parseInt(account3)));
		
		for(Player p : players) {
			p.printPlayerStat();
		}
		
		launch(args);
		
	}
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		
		window = primaryStage;
		
		window.setTitle("Sign in | Blackjack");
		window.setWidth(700);
		window.setHeight(720);
		
		gameScene = setGameScene();
		
		loginScene = setLoginScene(false);

		window.setScene(gameScene);
		window.show();
		
		System.out.println("Username from main: "+username);
	}
	
	
	static Scene setLoginScene(boolean wrongSomething) {
		
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(10,10,10,10));
		grid.setVgap(8);
		grid.setHgap(10);
		
		//NameLabel
		Label Label = new Label("Sign in");
//		GridPane.setConstraints(nameLabel, 0,0);
		
		Label nameLabel = new Label("Username: ");
		GridPane.setConstraints(nameLabel, 0,0);
		
		//PasswordLabel
		Label passLabel = new Label("Password: ");
		GridPane.setConstraints(passLabel, 0,1);
		
		Label wrongAlert = new Label("Wrong username or password!");
		wrongAlert.setTextFill(Color.web("#ff0000"));
		wrongAlert.setVisible(wrongSomething);
				
		//Username Input
		TextField nameInput = new TextField();
		nameInput.setMinWidth(150);
		nameInput.setMaxWidth(150);
		nameInput.setPromptText("username");
		nameInput.setText(username);
		GridPane.setConstraints(nameInput, 1, 0);
		
		PasswordField passField = new PasswordField();
		passField.setMinWidth(150);
		passField.setMaxWidth(150);
		passField.setText(password);
		passField.setPromptText("password");
		GridPane.setConstraints(passField, 1, 1);
		
		//Button
		Button signinButton = new Button("Sign in!");
//		GridPane.setConstraints(signinButton,1,1);
		signinButton.setOnAction(e -> {
			String input= nameInput.getText();
			String pass = passField.getText();
			
			if (input.equals(username)&&pass.equals(password)) {
				changeToRoomScene();
				System.out.println("Correct username & password");
			} else {
				loginScene = setLoginScene(true);
				window.setScene(loginScene);
				System.out.println("Inorrect username or password");
			}
		});
		grid.setAlignment(Pos.CENTER);
		grid.getChildren().addAll(nameLabel, nameInput, passLabel, passField);
		VBox loginBox = new VBox(20);
		loginBox.setAlignment(Pos.CENTER);
		loginBox.getChildren().addAll(Label, grid, signinButton,wrongAlert);
		Scene scene = new Scene(loginBox);
		return scene;
	}
	
	static void changeToRoomScene() {
			roomScene = setRoomScene();
			window.setScene(roomScene);
	}
	
	
//	@SuppressWarnings("unchecked")
	static Scene setRoomScene() {
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(10,10,10,10));
		grid.setVgap(8);
		grid.setHgap(10);
		
		//NameLabel
		Label nameLabel = new Label("You're logged in as: "+ username);
		GridPane.setConstraints(nameLabel, 0,0);
		
		//RoomList
		
		roomList = new ListView<String>();
		roomList.getItems().addAll(room); 
		GridPane.setConstraints(roomList,0,1);
		
		
		
		TableColumn<roomInfo, String> roomNameCol = new TableColumn<>("Room Name");
		roomNameCol.setMinWidth(150);
		roomNameCol.setCellValueFactory(new PropertyValueFactory<>("roomName2"));
		
		TableColumn<roomInfo, String> hostNameCol = new TableColumn<>("Host User");
		hostNameCol.setMinWidth(140);
		hostNameCol.setCellValueFactory(new PropertyValueFactory<>("HostName"));
		
		TableColumn<roomInfo, String> numPlayerCol = new TableColumn<>("Number of Players");
		numPlayerCol.setMinWidth(120);
		numPlayerCol.setCellValueFactory(new PropertyValueFactory<>("numPlayer"));
		
		roomTable = new TableView<>();
		roomTable.setMinWidth(420);
		loadRoom(room);
		GridPane.setConstraints(roomTable,0,1);
        roomTable.getColumns().addAll(roomNameCol,hostNameCol, numPlayerCol);
        
		//Button
		Button joinRoomButton = new Button("Join room!");
		joinRoomButton.setMinWidth(200);
//		GridPane.setConstraints(joinRoomButton,2,0);
		joinRoomButton.setOnAction(e->{
			String selected;
			selected = roomList.getSelectionModel().getSelectedItem();
			System.out.println("Chosen: "+selected);
			gameScene = setGameScene();
			window.setScene(gameScene);
		});
		Button createRoomButton = new Button("Create room!");
		createRoomButton.setMinWidth(200);
//		GridPane.setConstraints(createRoomButton,3,0);
		createRoomButton.setOnAction(e->{
			System.out.println("buttonclick: Create!");
			String roomName = createRoomBox.display();
			System.out.println(roomName);
			gameScene = setGameSceneHost();
			window.setScene(gameScene);
		});
		Button logoutButton = new Button("Logout");
		logoutButton.setMinWidth(200);
//		GridPane.setConstraints(logoutButton,4,0);
		logoutButton.setOnAction(e->{
			loginScene = setLoginScene(false);
			window.setScene(loginScene);
			System.out.println("Logged Out");
		});
		Button refreshButton = new Button("Refresh");
		refreshButton.setMinWidth(200);
//		GridPane.setConstraints(logoutButton,4,0);
		refreshButton.setOnAction(e->{
			k++;
			room[2] = "ROOM3/RedVelvet/" + k;
			loadRoom(room);
		});
		
		
		VBox buttonBox = new VBox(20);
		buttonBox.setAlignment(Pos.TOP_CENTER);
		
		GridPane.setConstraints(buttonBox,1,1);
		buttonBox.getChildren().addAll(joinRoomButton, createRoomButton,refreshButton,logoutButton);
		
		grid.getChildren().addAll(nameLabel, roomTable, buttonBox);
		Scene scene = new Scene(grid);
		return scene;
	}
	
	private static void loadRoom(String[] roomString) {
		ObservableList<roomInfo> rooms = FXCollections.observableArrayList();
		rooms.removeAll();
		for (int i =0;i<roomString.length;i++) {
			rooms.add(new roomInfo(roomString[i]));
		}
		roomTable.setItems(rooms);
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
	
	static String hostname= "";
	static String hbet="";
	static String hostacc = "";
	static String p1name= "";
	static String p1bet="";
	static String p1acc = "";
	static String p2name= "";
	static String p2bet="";
	static String p2acc = "";
	static String p3name ="" ;
	static String p3bet ="";
	static String p3acc ="";
	
	static Label hplayerName,hplayerBet,hplayerAccount;
	static Label playerName1,playerBet1,playerAccount1;
	static Label playerName2,playerBet2,playerAccount2;
	static Label playerName3,playerBet3,playerAccount3;
	
	static 	int numofCards=0;
	static int myNumber = 0;
	static String output;
	static String input="";
	static String Str = "";
	static String account = "5000";
	static String hostNumofCards ="2";
	static String p1NumofCards="3";
	static String p2NumofCards="2";
	static String p3NumofCards="1";
	static GridPane playerCards1;
	static GridPane playerCards2;
	static GridPane playerCards3;
	
	static Scene setGameScene() {
		
		Label nameLabel = new Label("You're logged in as: "+ username);
		GridPane.setConstraints(nameLabel, 0,0);
		
		//Sidemenu
		VBox sidemenu = new VBox();
		
		sidemenu.setAlignment(Pos.TOP_CENTER);
		sidemenu.setPadding(new Insets(5));
		sidemenu.setSpacing(30);
		TextField betAmnt = new TextField();
		betAmnt.setMaxWidth(100);
		Button betBtn = new Button("Bet");
		betBtn.setMinWidth(100);
		betBtn.setMinHeight(10);
		betBtn.setOnAction(e->{
			System.out.println("Hithere");
			AlertBox.display("Hry");
			for (Player p : players) {
				System.out.println(p.getUsername());
				if (p.getUsername().equals(username)) {
					p.addBet(Integer.parseInt(betAmnt.getText()));
					AlertBox.display(p.getUsername()+"bet "+ betAmnt.getText());
				}
			}
		});
		
		Button hitBtn = new Button("Hit");
		hitBtn.setMinWidth(100);
		hitBtn.setMinHeight(10);
		
		Button standBtn = new Button("Stand");
		standBtn.setMinWidth(100);
		standBtn.setMinHeight(10);
		standBtn.setOnAction(e->{
			
		});
		sidemenu.getChildren().addAll(nameLabel, betAmnt, betBtn, hitBtn, standBtn);

		//CenterMenu
		// Player Box
		
		VBox playersBox = new VBox (10);
		
		//FROM HOST

		String strFromHost = "FIRSTUPDATE/hostname/hostacc/quangky/p1bet/p1acc/p2name/p2bet/p2acc/p3name/p3bet/p3acc/";
		String UpdateFromHost = "UPDATE/p1NumOfCards/p2NumOfCards/p3NumOfCards/";
		String HitStrFromHost = "HITCARD/username/ 4C/";
		String FirstCardsFromHost = "FIRSTCARDS/username/3D/AH/";
		String HitOfferFromHost= "HITOFFER/";
		String BetRequest = "BETREQUEST/";
		String BustAlert = "BUST/WIN(LOSE, CHASE)/";		

		//TO HOST

		String BetStrToHost = "BET\n username\n Account\n BetAmmount\n";
		String HitStrToHost = "HIT\n username\n";
		String StandStrToHost = "STAND\n username\n";
		
		Str = "FIRSTUPDATE/hostname/hostacc/quangky/p1bet/p1acc/p2name/p2bet/p2acc/p3name/p3bet/p3acc/";
		
		switch (getOrder(Str)){
		case "BETREQUEST":

			System.out.println("Please Enter your Bet ammount: ");
			input = InputBox.display("Enter your Bet Ammount");
			output = "BET/"+username+"/"+account+"/"+input+"/";
			
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
			if(username!= "") {
				if(p1name!= "" && username.equals(p1name)) {
					myNumber = 1;
					System.out.println("haha");
				} else if (p2name!= "" && username.equals(p2name)) {
					myNumber =2;
				} else if (p3name!= "" && username.equals(p3name)) {
					myNumber =3;
				}
			}
			break;
		case "UPDATE":
			array = parseToArray(Str);
			hostNumofCards = array[1];
			p1NumofCards = array[2];
			
			if (array.length==2) {
				p2NumofCards = array[3];
			} else {
				p2NumofCards = array[3];
				p3NumofCards = array[4];
			}
			
			break;
		case "FIRSTCARDS":
			array = parseToArray(Str);
			cardsOnHand[1] = array[2];
			cardsOnHand[2] = array[3];
			numofCards = 2;
			
			break;
		case "HITOFFER":
//				scn = new Scanner (System.in);
			System.out.println("You want to HIT? (Y,N)");
			
			if (ConfirmBox.display("HIT OFFER", "Do you want to HIT or STAND?","Hit", "Stand")) {
				output = "HIT\n"+ username +"\n";
			} else {
				output = "STAND\n"+ username +"\n";
			}
						
			break;
		case "HITCARDS":
			array = parseToArray(Str);
			cardsOnHand[numofCards] = array[2];
			numofCards++;
			
			break;
		case "BUST":
			array = parseToArray(Str);
			status = array[1];
			System.out.println("You "+ status);
			break;
		default:
			break;
		}
		
		// PLayer Host
		hplayerName = new Label("Player: " + hostname);
		hplayerBet = new Label("Bet: "+ hbet);
		hplayerAccount = new Label("Account: " + hostacc);
		HBox playerStatBoxh = new HBox (25);
		playerStatBoxh.getChildren().addAll(hplayerName, hplayerAccount,hplayerBet);
		
		GridPane hostCardBox;
		hostCardBox = getCardBacks(Integer.parseInt(hostNumofCards));
		
		// PLayer1
		
		playerName1 = new Label("Player: " + p1name);
		playerBet1 = new Label("Bet: "+ p1bet);
		playerAccount1 = new Label("Account: " + p1acc);
		HBox playerStatBox1 = new HBox (25);
		playerStatBox1.getChildren().addAll(playerName1, playerAccount1,playerBet1);
		
		if (myNumber==1) {
			playerCards1 = getCards(cardsOnHand,numofCards);
		} else {
			playerCards1 = getCardBacks(Integer.parseInt(p1NumofCards));
		}
		
		// PLayer2
		
		playerName2 = new Label("Player: " + p2name);
		playerBet2= new Label("Bet: "+ p2bet);
		playerAccount2 = new Label("Account: " + p2acc);
		HBox playerStatBox2 = new HBox (25);
		playerStatBox2.getChildren().addAll(playerName2, playerAccount2,playerBet2);
		
		if (myNumber==2) {
			playerCards2 = getCards(cardsOnHand,numofCards);
		} else {
			playerCards2 = getCardBacks(Integer.parseInt(p2NumofCards));
		}
	
		// PLayer3
		
		playerName3 = new Label("Player: " + p3name);
		playerBet3 = new Label("Bet: "+ p3bet);
		playerAccount3 = new Label("Account: " + p3acc);
		HBox playerStatBox3 = new HBox (25);
		playerStatBox3.getChildren().addAll(playerName3, playerAccount3,playerBet3);
		
		

		if (myNumber==3) {
			playerCards3 = getCards(cardsOnHand,numofCards);
		} else {
			playerCards3= getCardBacks(Integer.parseInt(p3NumofCards));
		}
	 
		playersBox.getChildren().addAll(playerStatBoxh,hostCardBox,playerStatBox1,playerCards1,playerStatBox2,playerCards2,playerStatBox3,playerCards3);
		
		System.out.println("setBoxDone");
		
		
		// ChangeValuefunction
		
		hitBtn.setOnAction(e->{
			playerName3.setText("QuangKy");
			playerBet3.setText("110204");
		});
		
		//BorderPane
		BorderPane borderPane = new BorderPane();
		BorderPane.setMargin(sidemenu, new Insets(10,10,10,10));

		borderPane.setLeft(sidemenu);
		borderPane.setCenter(playersBox);

		Scene scene = new Scene(borderPane);

		return scene;
	}

	static void updatePlayerLabels() {
		hplayerName.setText("Player: " + hostname);
		hplayerBet.setText("Bet: "+ hbet);
		hplayerAccount.setText("Account: " + hostacc);
		playerName1.setText("Player: " + p1name);
		playerBet1.setText("Bet: "+ p1bet);
		playerAccount1.setText("Account: " + p1acc);
		playerName2.setText("Player: " + p2name);
		playerBet2.setText("Bet: "+ p2bet);
		playerAccount2.setText("Account: " + p2acc);
		playerName3.setText("Player: " + p3name);
		playerBet3.setText("Bet: "+ p3bet);
		playerAccount3.setText("Account: " + p3acc);
		
	}
	
	
	static Scene setGameSceneHost() {
		
		//Sidemenu
		VBox sidemenu = new VBox();
		
		sidemenu.setAlignment(Pos.TOP_CENTER);
		sidemenu.setPadding(new Insets(5));
		sidemenu.setSpacing(30);
		
		Button generateGameBtn = new Button("Close Room");
		generateGameBtn.setMinWidth(100);
		generateGameBtn.setMinHeight(40);
		generateGameBtn.setOnAction(e->{
			for(Player p : players) {
				game.addPlayer(p.getUsername(), p.getTotalAccount());
				p.printPlayerStat();
			}
			
			game.printAllPlayerStatus();
			
			System.out.println();
			AlertBox.display("Game Generated, Room Closed");
			
			roomClosed = true;
			gameScene = setGameSceneHost();
			window.setScene(gameScene);
//			boolean done = playerSimulator(username);
		});
		
		Button requestBetBtn = new Button("Request Bet");
		requestBetBtn.setMinWidth(100);
		requestBetBtn.setMinHeight(40);
		requestBetBtn.setOnAction(e->{
			//Request bet money from users
//			players.get(0).addBet(50);
			players.get(1).addBet(40);
			
			for(Player p : players) {
				if(p.isBet()) {
					game.addBet(p.getUsername(), p.betAmmount);
				}
			} 
			
		});
		
		Button refreshBtn = new Button("Refresh");
		refreshBtn.setMinWidth(100);
		refreshBtn.setMinHeight(40);
		refreshBtn.setOnAction(e->{
		});
		
		Button dealBtn = new Button("Start Deal");
		dealBtn.setMinWidth(100);
		dealBtn.setMinHeight(40);
		dealBtn.setOnAction(e->startDealHandler());
		
		Button hitBtn = new Button("Hit");
		hitBtn.setMinWidth(100);
		hitBtn.setMinHeight(40);
		hitBtn.setOnAction(e->{
			req = Request.HOST_HIT;
			bustingHandler();
		});
		
		Button endGameBtn = new Button("endGame");
		endGameBtn.setMinWidth(100);
		endGameBtn.setMinHeight(40);
		endGameBtn.setOnAction(e->{
			req = game.endGame();
			endGameHandler();
		});
		
		TextField bustPlayerInput = new TextField();
		bustPlayerInput.setMaxWidth(100);
		bustPlayerInput.setPromptText("bust who?");
		Button bustBtn = new Button("Bust");
		bustBtn.setMinWidth(100);
		bustBtn.setMinHeight(40);
		bustBtn.setOnAction(e->{
			boolean found = false;
			req = Request.HOST_BUST;
			for (Player p : players) {
				if (bustPlayerInput.getText().equals(p.getUsername())) {
					req.setBustPlayer(p.getUsername());
					found=true;
				}
			}
			if (!found) {
				AlertBox.display("Please re-enter Player's name");
			} else {
				bustingHandler();
			}
		});
		
		
//		sidemenu.getChildren().addAll(generateGameBtn,refreshBtn,requestBetBtn ,dealBtn,hitBtn, bustPlayerInput, bustBtn);
		sidemenu.setAlignment(Pos.CENTER);
		if (!roomClosed) {
			sidemenu.getChildren().addAll(generateGameBtn,refreshBtn);
//			sidemenu.getChildren().addAll(requestBetBtn ,dealBtn);
		} else if(roomClosed&&!dealed) {
			sidemenu.getChildren().addAll(requestBetBtn ,dealBtn);
		} else {
			sidemenu.getChildren().addAll(hitBtn, bustPlayerInput, bustBtn,endGameBtn);
			
		}
		
		//CenterMenu
		// Player Box
		VBox playersBox = new VBox (10);
		host = game.host;
		playersBox.getChildren().add(genPlayerBox(host, false));
		
		for(Player p : players) {
			playersBox.getChildren().add(genPlayerBox(p, true));
		}
		
		
//		playersBox.getChildren().addAll(player1Box, player2Box, player3Box, player4Box);
		
		System.out.println("setBoxDone");
		
		//BorderPane
		BorderPane borderPane = new BorderPane();
		BorderPane.setMargin(sidemenu, new Insets(10,10,10,10));
		BorderPane.setMargin(playersBox, new Insets(2,2,2,10));
		borderPane.setLeft(sidemenu);
		borderPane.setCenter(playersBox);
		
		Scene scene = new Scene(borderPane);

		return scene;
		
	}

	static void endGameHandler() {
		switch (req) {
		case GAME_ENDED:
			for (Player p : players) {
				for (int i=0;i<req.returnAccount.size();i=i+2) {
					if (p.getUsername() == req.returnAccount.get(i)) {
						p.totalAccount = Integer.parseInt(req.returnAccount.get(i+1));
					}
				}
			}
			
			gameScene = setGameSceneHost();
			window.setScene(gameScene);	

			break;
		default:
			System.out.println("Something went Wrong");
		}
	}

	static void bustingHandler() {
		req = game.Bust(req);
		switch (req) {
		case HOST_HIT_CARD:
//			host.addCard(req.hitCard);
			if (host.isBusted()) {
				AlertBox.display("You are BUSTED");
			}
			gameScene = setGameSceneHost();
			window.setScene(gameScene);
			break;
		case HOST_BUST_WIN:
			for (Player p : players) {
				if (req.getBustPlayer().equals(p.username)){
					AlertBox.display("You WON player "+p.getUsername());
				}
			}
			break;
		case HOST_BUST_LOSE:
			for (Player p : players) {
				if (req.getBustPlayer().equals(p.username)){
					AlertBox.display("Player "+p.getUsername()+" WON");
					p.setWin();
				}
			}
			break;
		case HOST_BUST_DRAW:
			for (Player p : players) {
				if (req.getBustPlayer().equals(p.username)){
					AlertBox.display("Player "+p.getUsername()+" CHASE");
					p.setChase();
				}
			}
			break;
		case HOST_BUSTED:
			AlertBox.display("You are Busted");
			break;
		default:
			break;
		}
	}
	
	static VBox genPlayerBox(Player p, boolean cardBack) {
		
		Label playerName = new Label("Player: " + p.getUsername());
		Label playerBet = new Label("Bet: "+ p.betAmmount);
		Label playerAccount = new Label("Account: " + p.getTotalAccount());
//		Label playerScore = new Label ("Score: "+p.sum() );
		
		HBox playerStatBox = new HBox (25);
		playerStatBox.getChildren().addAll(playerName, playerAccount,playerBet);
		
		GridPane playerCards;
		if (!cardBack) {
			playerCards = getCards(p.getCardArray(),p.getCardArray().length);
		} else {
			playerCards = getCardBacks(p.holdingCards.size());
		}
		
		
		VBox playerBox = new VBox (10);
		playerBox.getChildren().addAll(playerStatBox,playerCards);
		
		return playerBox;
	}
	
	static GridPane getCardBacks(int numCards) {
		GridPane grid1 = new GridPane();
		grid1.setPadding(new Insets(10,10,10,10));
		grid1.setMinHeight(100);
		grid1.setVgap(8);
		grid1.setHgap(20);
		String imgSrc = "./png/blue_back.png";
		
		for (int i=0;i<numCards;i++) {
			ImageView iv2 = new ImageView();
	        iv2.setImage(new Image(imgSrc));
	        iv2.setFitWidth(70);
	        iv2.setPreserveRatio(true);
	        iv2.setSmooth(true);
	        iv2.setCache(true);
	        GridPane.setConstraints(iv2,i,0);
			grid1.getChildren().add(iv2);
		}
		return grid1;
		
	}
	
	static GridPane getCards(String[] cardArray,int numofCards) {
		GridPane grid1 = new GridPane();
		grid1.setPadding(new Insets(10,10,10,10));
		grid1.setMinHeight(100);
		grid1.setVgap(8);
		grid1.setHgap(20);
		
		String imgSrc = "./png/";
		for (int i=0;i<numofCards;i++) {
			imgSrc = imgSrc + cardArray[i] + ".png";
//			imageList.add(new Image(imgSrc));
			System.out.println(imgSrc);
			ImageView iv2 = new ImageView();
	        iv2.setImage(new Image(imgSrc));
	        iv2.setFitWidth(70);
	        iv2.setPreserveRatio(true);
	        iv2.setSmooth(true);
	        iv2.setCache(true);
	        GridPane.setConstraints(iv2,i,0);
			grid1.getChildren().add(iv2);
			imgSrc = "./png/";
		}
		return grid1;
	}
	
	private static void startDealHandler() {
		Request receive = game.startDeal();
		switch (receive) {
		case WAIT_FOR_PLAYER:
			//Wait for player to bet or use forceDeal() to ignore the player
			System.out.println("Players are not Ready. Force start?");
			if (ConfirmBox.display("Force Start","Players are not Ready. Force start?","Hit it!","Wait a bit")) {
				startForceDealHandler();
			}
			break;
		case WAIT_FOR_FIRST_DEAL:
			//give the first 2 cards to players
			int k = 0;
			for (Player p : players) {
				if (p.isBet()) {
					p.addCard(receive.firstDealCards.get(k)[0]);
					p.addCard(receive.firstDealCards.get(k)[1]);
					System.out.println("k= "+k);
					k++;
				}
			}
			dealed = true;
			
			gameScene = setGameSceneHost();
			window.setScene(gameScene);
			
			boolean done = playerSimulator(players.get(0).username);
			
//			for (Player p : players) {
//				boolean done = playerSimulator(p.username);
//			}
			
//			username = host.getUsername();
			
			if(ConfirmBox.display("Start Busting", "Hey, all players have done picking. Start Busting?", "Okay","Nope")) {
				startBust();
			}
			break;
		default:
			break;
		}
		
	}
	
	private static void startForceDealHandler() {
		Request receive = game.forceDeal();
		switch (receive) {
		case WAIT_FOR_PLAYER:
			//Wait for player to bet or use forceDeal() to ignore the player
			System.out.println("Players are not Ready. Force start?");
			break;
		case WAIT_FOR_FIRST_DEAL:
			//give the first 2 cards to players
			int k=0;
			for (Player p : players) {
				if (p.isBet()) {
					p.addCard(receive.firstDealCards.get(k)[0]);
					p.addCard(receive.firstDealCards.get(k)[1]);
					System.out.println("k= "+k);
					k++;
				} else {
					AlertBox.display(p.getUsername() +" is left out");
					System.out.println(p.getUsername() +" is left out");
				}	
			}
			dealed = true;
			for (int i = 0; i < players.size();i++) {
				if (!players.get(i).isBet()) {
					players.remove(i);
				}
			}
			gameScene = setGameSceneHost();
			window.setScene(gameScene);
			
			
//			for (Player p : players) {
//				boolean done = playerSimulator(p.username);
//			}
			
			playerSimulator2();
			
			
			if(ConfirmBox.display("Start Busting", "Hey, all players have done picking. Start Busting?", "Okay","Nope")) {
				startBust();
			}
			
			break;
		default:
			break;
		}
	}
	static void startBust() {
		Request req2 = game.startBust();
		
		switch (req2) {
		case BUST_NOT_READY:
			System.out.println("Bust is not ready");
			AlertBox.display("Bust is not ready");
			break;
		case BUST_READY:
			System.out.println("Let's get started!");
			bustReady = true;
			gameScene = setGameSceneHost();
			window.setScene(gameScene);
			break;
		default:
			break;
		}

	}
	
	static boolean playerSimulator(String user) {
		username = user;
		window2 = new Stage();
//		window2.initModality(Modality.APPLICATION_MODAL);
		window2.setTitle("Sign in | Blackjack");
		window2.setWidth(700);
		window2.setHeight(720);
		Scene scene = setGameScene();
		window2.setScene(scene);
//		window2.showAndWait();
		window2.show();
		return true;
	}
		
	
	static void playerSimulator2() {
		
		for (Player p: players) {
			boolean hit;
			
			hit = ConfirmBox.display("Hit or Stand?","    	 Hey " + p.getUsername() + "\nDo you wanna Hit or Stand?", "HIT", "STAND");
			
			Request req = Request.HIT_REQUEST;
			req.setUsername(p.getUsername());
			
			while(hit && !p.busted) {
				req = game.dealHitCard(req);
				switch (req) {
				case PLAYER_IS_BUSTED:
					p.addCard(req.hitCard);
					AlertBox.display(p.getUsername() +"You are BUSTED");
					gameScene = setGameSceneHost();
					window.setScene(gameScene);
					break;
				case HIT_REQUEST:
					p.addCard(req.hitCard);
					gameScene = setGameSceneHost();
					window.setScene(gameScene);
					hit = ConfirmBox.display("Hit or Stand?","    	 Hey " + p.getUsername() + "\nDo you wanna Hit or Stand?", "HIT", "STAND");
					break;
				default:
					break;
				}
			}
			req = game.standRequest(req);	
		}
		
	}
	
	
	
}
