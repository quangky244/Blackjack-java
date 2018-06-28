
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class createRoomBox {
	static String roomName;
	public static String display() {
		Stage window = new Stage();
		
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Sign in | Blackjack");
		window.setWidth(250);
		
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(10,10,10,10));
		grid.setVgap(8);
		grid.setHgap(10);
		
		//NameLabel
		Label nameLabel = new Label("Choose a room name: ");
		GridPane.setConstraints(nameLabel, 0,0);
		
		//Username Input
		TextField roomNameInput = new TextField();
		GridPane.setConstraints(roomNameInput, 0, 1);
		
		//Button
		Button signinButton = new Button("Create Room");
		GridPane.setConstraints(signinButton,1,1);
		signinButton.setOnAction(e ->{ 
			roomName = roomNameInput.getText();
			window.close();
		});
		
		grid.getChildren().addAll(nameLabel, roomNameInput, signinButton);
		
		Scene scene = new Scene(grid);
		window.setScene(scene);
		window.showAndWait();
		return roomName;
	}
}
