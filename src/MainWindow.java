

import java.util.Collection;

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
	static int k =3;
	static Stage window;
	static Scene loginScene,roomScene, gameScene;
	static String username = "hellokitty";
	static String password = "hithere";
	static String room[] = {"room1/BlueCap/2","RoOm2/CrystalTiger/1", "ROOM3/RedVelvet/0"};
	static ListView<String> roomList;
	static TableView<roomInfo> roomTable;
	static String players[]= {"BlackAvocado","GreenLantern","AngryBird"};
	static String cards[]= {"2S","AC","JH","10D","KD"};
//	static Collection<Image> imageList;
	static ObservableList<String[]> deadledCards;
	
	public static void main(String[] args) {
		launch(args);
	}
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		
		window = primaryStage;
		
		window.setTitle("Sign in | Blackjack");
		window.setWidth(700);
		window.setHeight(350);
		
		loginScene = setLoginScene(false);
		window.setScene(loginScene);
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
		
		//NameLabel
		Label nameLabel = new Label("You're logged in as: "+ username);
		GridPane.setConstraints(nameLabel, 0,0);
		
		
		//Topmenu
		HBox topmenu = new HBox();
		topmenu.setPadding(new Insets(5));
		topmenu.setSpacing(15);
		topmenu.setMinHeight(30);
		Label hostLab = new Label("Host: "+ username);
		Label player1lab = new Label("Player1: "+players[0]);
		Label player2lab = new Label("Player2: "+players[1]);
		Label player3lab = new Label("Player3: "+players[2]);
		topmenu.getChildren().addAll(hostLab,player1lab,player2lab,player3lab);
		
		
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
		Button hitBtn = new Button("Hit");
		hitBtn.setMinWidth(100);
		hitBtn.setMinHeight(10);
		Button standBtn = new Button("Stand");
		standBtn.setMinWidth(100);
		standBtn.setMinHeight(10);
		sidemenu.getChildren().addAll(betAmnt, betBtn, hitBtn, standBtn);
		
		//Grid
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(10,10,10,10));
		grid.setVgap(8);
		grid.setHgap(20);
		
		grid = getCards(grid);
		
		
		//BorderPane
		BorderPane borderPane = new BorderPane();
		BorderPane.setMargin(sidemenu, new Insets(10,10,10,10));
		BorderPane.setMargin(topmenu, new Insets(2,2,2,10));
		borderPane.setTop(topmenu);
		borderPane.setLeft(sidemenu);
		borderPane.setCenter(grid);
		
		Scene scene = new Scene(borderPane);
		return scene;
		
	}
	
	static GridPane getCards(GridPane grid) {
		String imgSrc = "./png/";
		for (int i=0;i<cards.length;i++) {
			imgSrc = imgSrc + cards[i] + ".png";
//			imageList.add(new Image(imgSrc));
			
			System.out.println(imgSrc);
			ImageView iv2 = new ImageView();
	        iv2.setImage(new Image(imgSrc));
	        iv2.setFitWidth(85);
	        iv2.setPreserveRatio(true);
	        iv2.setSmooth(true);
	        iv2.setCache(true);
	        GridPane.setConstraints(iv2,i,0);
			grid.getChildren().add(iv2);
			imgSrc = "./png/";
		}
		return grid;
	}
	
}
