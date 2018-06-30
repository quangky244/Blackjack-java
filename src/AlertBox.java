

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

public class AlertBox {

	public static void display(String message) {
		// TODO Auto-generated method stub
		Stage window = new Stage();
		
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Alert");
		window.setMinWidth(400);
		window.setHeight(200);
		
		Label label = new Label();
		label.setText(message);
		label.setAlignment(Pos.CENTER);
		Button Button = new Button("Okay");
		Button.setMinWidth(100);
		Button.setOnAction(e->{
			window.close();
		});
		
		VBox layout = new VBox(20);
		layout.setAlignment(Pos.CENTER);
		layout.getChildren().addAll(label, Button);
		
		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.showAndWait();
	}

}
