

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
//	static Collection<Image> imageList;
	static ObservableList<String[]> deadledCards;
	static Player host;
	static List<Player> players = new Vector<Player>();
	static TableView<Player> player1Stat;
	static TableView<Player> player2Stat;
	static TableView<Player> player3Stat;
	
	
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
			
			
		
		window.setScene(loginScene);
		window.show();
		
		System.out.println("Username from main: "+username);
	}
	
//	static Scene setHostScene() {
//		//Buttons;
//		Button startButton = new Button("Start Game");
//		startButton.setOnAction(e -> {});
//		
//		Button requestButton = new Button("Request Bet");
//		requestButton.setOnAction(e -> {});
//		
//		Button forceButton = new Button("Force Start");
//		forceButton.setOnAction(e -> {});
//		
//		VBox buttonBox = new VBox(20);
//		buttonBox.getChildren().addAll(startButton, requestButton, forceButton);
//		GridPane grid;
//		Scene scene = new Scene(grid);
//		return scene;
//	}
	
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
	
	static Scene setGameScene() {
//		host = game.host;
		
		//Receive from HOST
		
		String player1 = "quangky";
		String player2 = "DoBao";
		String player3 = "duckhai";
		
		String account1 = "2500";
		String account2 = "1400";
		String account3 = "3700";
		
		
		
		players.add(new Player(player1,Integer.parseInt(account1)));
		players.add(new Player(player2,Integer.parseInt(account2)));
		players.add(new Player(player3,Integer.parseInt(account3)));
		
		//String format: "request/numberofplayer/host/hostname
		
		String receivedFromServer = "HIT/2/host/na/quangky/3D/duckhai/na";
	
		//NameLabel
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
		hitBtn.setOnAction(e->{
			
		});
		Button standBtn = new Button("Stand");
		standBtn.setMinWidth(100);
		standBtn.setMinHeight(10);
		standBtn.setOnAction(e->{
			
		});
		sidemenu.getChildren().addAll(nameLabel, betAmnt, betBtn, hitBtn, standBtn);

		//CenterMenu
		// Player Box

		VBox playersBox = new VBox (10);
		playersBox.getChildren().add(genPlayerBox(host, true));
		
		
		for (Player p: players) {
			if (p.getUsername().equals(username)) {
				playersBox.getChildren().add(genPlayerBox(p, false));
			} else {
				playersBox.getChildren().add(genPlayerBox(p, true));
			}
		}		
		System.out.println("setBoxDone");
		
		//BorderPane
		BorderPane borderPane = new BorderPane();
		BorderPane.setMargin(sidemenu, new Insets(10,10,10,10));
//		BorderPane.setMargin(topmenu, new Insets(2,2,2,10));
		borderPane.setLeft(sidemenu);
		borderPane.setCenter(playersBox);
		
		Scene scene = new Scene(borderPane);
//		scene.
		return scene;
		
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
		
		for(int i= 0; i < players.size();i++) {
			
			playersBox.getChildren().add(genPlayerBox(players.get(i), true, i));
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
	
	static VBox genPlayerBox(Player p, boolean cardBack, int j) {
		
		TableColumn<Player, String> playerNameCol = new TableColumn<>("Player");
		playerNameCol.setMinWidth(150);
		playerNameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
		
		TableColumn<Player, String> accountCol = new TableColumn<>("Account");
		accountCol.setMinWidth(140);
		accountCol.setCellValueFactory(new PropertyValueFactory<>("totalAccount"));
		
		TableColumn<Player, String> betCol = new TableColumn<>("Bet");
		betCol.setMinWidth(120);
		betCol.setCellValueFactory(new PropertyValueFactory<>("betAmmount"));
		
		HBox playerStatBox = new HBox (25);
		
		switch (j) {
		case 1:
			player1Stat = new TableView<>();
			player1Stat.setMinWidth(420);
			player1Stat.setItems(new ObservableList<Player> p);
			player1Stat.getColumns().addAll(playerNameCol,accountCol, betCol);
			break;
		case 2:
			break;
		case 3:
			break;
		default:
			System.out.println("");
		}
		
		
		playerStatBox.getChildren().add(player1Stat);
		
		GridPane playerCards;
		if (!cardBack) {
			playerCards = getCards(p.getCardArray());
		} else {
			playerCards = getCardBacks(p.holdingCards.size());
		}
		
		
		VBox playerBox = new VBox (10);
		playerBox.getChildren().addAll(playerStat,playerCards);
		
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
	
	static GridPane getCards(String[] cardArray) {
		GridPane grid1 = new GridPane();
		grid1.setPadding(new Insets(10,10,10,10));
		grid1.setMinHeight(100);
		grid1.setVgap(8);
		grid1.setHgap(20);
		
		String imgSrc = "./png/";
		for (int i=0;i<cardArray.length;i++) {
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
