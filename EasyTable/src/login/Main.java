package login;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

public class Main extends Application {
	Stage stage;

	@Override
	public void start(Stage primaryStage) {
		try {
			stage = primaryStage;
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/Login.fxml"));
			AnchorPane pane = loader.load();
			stage.initStyle(StageStyle.UNDECORATED);
			stage.setScene(new Scene(pane));
			stage.centerOnScreen();
			stage.show();

		} catch (IOException ioe) {
			ioe.printStackTrace();
			
		} catch (Exception ie) {
			// TODO Auto-generated catch block
			ie.printStackTrace();
		}
	}

}
