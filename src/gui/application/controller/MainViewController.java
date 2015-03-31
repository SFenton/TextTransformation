package gui.application.controller;

import java.io.File;
import java.io.IOException;

import parse.binary.tree.TreeParser;
import parse.text.citation.TextFileParser;
import tree.regex.RegExTree;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainViewController {
	/**
	 * Text Field used for input citation file location.
	 */
	@FXML
	TextField inputFileTextField;
	/**
	 * Text Field used for output CSV file location.
	 */
	@FXML
	TextField outputFileTextField;
	/**
	 * Button used to 'start' parsing process.
	 */
	@FXML
	Button startButton;
	/**
	 * Stage on which to set scenes for tool.
	 */
	private Stage primaryStage;
	
	/**
	 * Regex Tree to use as the basis for parsing citation
	 * text files.
	 */
	RegExTree regexTree;
	/**
	 * Parser used to import regex tree binary files.
	 */
	TreeParser binaryParser;
	
	public void initialize() {
		
	}
	
	public void initData(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.binaryParser = new TreeParser();
	}
	
	/**
	 * Handler for the 'Browse' button for the Citation File input.
	 * Opens File Chooser to select a Citation Text file that will be
	 * parsed into a CSV file.
	 * 
	 * @param event The event that triggered the handler
	 */
	@FXML
	protected void handleBrowseInputButtonAction(ActionEvent event) {
		// Configure the file chooser before opening
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select Input File");
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		fileChooser.getExtensionFilters().addAll(
				 new FileChooser.ExtensionFilter(".TXT", "*.txt"));
		
		// Get selected binary file
		File inputFile = fileChooser.showOpenDialog(primaryStage);
		if (inputFile != null) {
			inputFileTextField.setText(inputFile.getAbsolutePath());
		}
	}
	
	/**
	 * Handler for the 'Browse' button for the CSV File output.
	 * Opens File Chooser to select an output location for a Parsed
	 * Citation CSV File.
	 * 
	 * @param event The event that triggered the handler
	 */
	@FXML
	protected void handleBrowseOutputButtonAction(ActionEvent event) {
		// Configure the file chooser before opening
		DirectoryChooser dirChooser = new DirectoryChooser();
		dirChooser.setTitle("Select Output Directory");
		dirChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		
		// Get selected binary file
		File outputDir = dirChooser.showDialog(primaryStage);
		if (outputDir != null) {
			outputFileTextField.setText(outputDir.getAbsolutePath());
		}	
	}
	
	/**
	 * Handler for the 'start' button.
	 * Begins the parsing of the input text file into a CSV file.
	 * 
	 * @param event The event that triggered the handler
	 */
	@FXML
	protected void handleStartButtonAction(ActionEvent event) {
		try {
			TextFileParser parser = new TextFileParser(inputFileTextField.getText(),
					outputFileTextField.getText(), regexTree);
			parser.addToOutput();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Handler for the 'Import Binary' menu item.
	 * Opens File Chooser to select a .rgxt binary file to
	 * use as the regex parsing tree for parsing text citations.
	 * 
	 * @param event The event that triggered the handler
	 */
	@FXML
	protected void handleImportAction(ActionEvent event) {
		// Configure the file chooser before opening
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Import Binary File");
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		fileChooser.getExtensionFilters().addAll(
				 new FileChooser.ExtensionFilter(".RGXT", "*.rgxt"));
		
		// Get selected binary file
		File binaryFile = fileChooser.showOpenDialog(primaryStage);
		if (binaryFile != null) {
			try {
				// TODO: This may need to be threaded to avoid GUI lag
				binaryParser.parseRegexTreeFromFile(binaryFile.getAbsolutePath());
				regexTree = binaryParser.getTree();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
