import gui.application.TextTransformationApplication;
import javafx.application.Application;

/**
 * The Driver to start the Text Transformation Tool.
 * 
 * @author Dustin Thompson
 */
public class TextTransformationDriver {

	public static void main(String[] args) {
		// Launch the program on the javafx application thread
		Application.launch(TextTransformationApplication.class, args);
	}

}
