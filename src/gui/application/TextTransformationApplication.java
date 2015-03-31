package gui.application;

import gui.application.controller.MainViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX Application Class.
 * Loads FXML Views, Controllers, and starts the application.
 * 
 * @author Dustin Thompson
 */
public class TextTransformationApplication extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/main_view.fxml"));
		
		// Setup the primary stage
        primaryStage.setTitle("Citation Parser");
        primaryStage.setScene(
        		new Scene(
        				(Parent) loader.load(), 500, 200
        				));
        
        // Initialize controller for fxml
        MainViewController controller= loader.<MainViewController>getController();
        controller.initData(primaryStage);
        
        primaryStage.show();
	}

}
