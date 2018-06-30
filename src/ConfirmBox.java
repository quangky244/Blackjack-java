

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmBox {
	static boolean answer = false;
	public static boolean display(String title, String message,String yesStr, String noStr) {
		// TODO Auto-generated method stub
		Stage window = new Stage();
		
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		window.setMinWidth(400);
		window.setHeight(200);
		
		Label label = new Label();
		label.setText(message);
		label.setAlignment(Pos.CENTER);
		Button yesButton = new Button(yesStr);
		yesButton.setMinWidth(100);
		Button noButton = new Button(noStr);
		noButton.setMinWidth(100);
		
		HBox answerBox = new HBox(20);
		answerBox.getChildren().addAll(yesButton, noButton);
		answerBox.setAlignment(Pos.CENTER);
		
		yesButton.setOnAction(e-> {
			answer = true;
			window.close();
		});
		noButton.setOnAction(e->{
			answer = false;
			window.close();
		});
		
		VBox layout = new VBox(20);
		layout.setAlignment(Pos.CENTER);
		layout.getChildren().addAll(label, answerBox);
		
		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.showAndWait();
		
		return answer;
	}

}
