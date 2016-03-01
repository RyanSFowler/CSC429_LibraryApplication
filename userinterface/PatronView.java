package userinterface;

//system imports
import java.text.NumberFormat;
import java.util.Properties;

import model.Book;
import model.Patron;
import javafx.event.Event;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

//project imports
import impresario.IModel;
import model.Librarian;

public class PatronView extends View
{
	private TextField nameField;
	private TextField addressField;
	private TextField cityField;
	private TextField stateCodeField;
	private TextField zipField;
	private TextField emailField;
	private TextField dateOfBirthField;
	private TextField statusField;
	private Button submitButton;
	private Button doneButton;
	private ComboBox statusBox;


	private Patron myPatron;

	private MessageView statusLog;

	public PatronView(Patron patron)
	{
		super(patron, "PatronView");
		myPatron = patron;

		// create a container for showing the contents
				VBox container = new VBox(10);

				container.setPadding(new Insets(15, 5, 5, 5));

				// create a Node (Text) for showing the title
				container.getChildren().add(createTitle());

				// create a Node (GridPane) for showing data entry fields
				container.getChildren().add(createFormContents());

				// Error message area
				container.getChildren().add(createStatusLog("                          "));

				getChildren().add(container);


				// STEP 0: Be sure you tell your model what keys you are interested in
				myModel.subscribe("LoginError", this);
	}

				// Create the label (Text) for the title of the screen
				//-------------------------------------------------------------
				private Node createTitle()
				{

					Text titleText = new Text("       LIBRARY SYSTEM          ");
					titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
					titleText.setTextAlignment(TextAlignment.CENTER);
					titleText.setFill(Color.DARKGREEN);


					return titleText;
				}

				// Create the main form contents
				//-------------------------------------------------------------
				private GridPane createFormContents()
				{
					GridPane grid = new GridPane();
			   	grid.setAlignment(Pos.CENTER);
			  		grid.setHgap(10);
			   	grid.setVgap(10);
			   	grid.setPadding(new Insets(25, 25, 25, 25));


				Label nameLabel = new Label("Name:");
			   	grid.add(nameLabel, 0, 0);

			  	nameField = new TextField();

			  	nameField.setOnAction(new EventHandler<ActionEvent>() {

			      		     @Override
			      		     public void handle(ActionEvent e) {


			           	     }
			       	});
			  	grid.add(nameField, 1, 0);


				Label addressLabel = new Label("Address:");
			   	grid.add(addressLabel, 0, 1);

			  	addressField = new TextField();

			  	addressField.setOnAction(new EventHandler<ActionEvent>() {

			      		     @Override
			      		     public void handle(ActionEvent e) {


			           	     }
			       	});
			  	grid.add(addressField, 1, 1);


				Label cityLabel = new Label("City:");
			   	grid.add(cityLabel, 0, 2);

			  	cityField = new TextField();

			  	cityField.setOnAction(new EventHandler<ActionEvent>() {

			      		     @Override
			      		     public void handle(ActionEvent e) {


			           	     }
			       	});
			  	grid.add(cityField, 1, 2);


				Label stateCodeLabel = new Label("State Code:");
			   	grid.add(stateCodeLabel, 0, 3);

			  	stateCodeField = new TextField();

			  	stateCodeField.setOnAction(new EventHandler<ActionEvent>() {

			      		     @Override
			      		     public void handle(ActionEvent e) {


			           	     }
			       	});
			  	grid.add(stateCodeField, 1, 3);


				Label zipLabel = new Label("Zip:");
			   	grid.add(zipLabel, 0, 4);

			  	zipField = new TextField();

			  	zipField.setOnAction(new EventHandler<ActionEvent>() {

			      		     @Override
			      		     public void handle(ActionEvent e) {


			           	     }
			       	});
			  	grid.add(zipField, 1, 4);


				Label emailLabel = new Label("Email:");
			   	grid.add(emailLabel, 0, 5);

			  	emailField = new TextField();

			  	emailField.setOnAction(new EventHandler<ActionEvent>() {

			      		     @Override
			      		     public void handle(ActionEvent e) {


			           	     }
			       	});
			  	grid.add(emailField, 1, 5);


				Label dateOfBirthLabel = new Label("DOB yyyy-mm-dd:");
			   	grid.add(dateOfBirthLabel, 0, 6);

			  	dateOfBirthField = new TextField();

			  	dateOfBirthField.setOnAction(new EventHandler<ActionEvent>() {

			      		     @Override
			      		     public void handle(ActionEvent e) {


			           	     }
			       	});
			  	grid.add(dateOfBirthField, 1, 6);




			  	Label statusBoxLabel = new Label("Status:");
			  	grid.add(statusBoxLabel, 0, 7);
			  	statusBox = new ComboBox();
			  	statusBox.getItems().addAll(
			  		    "YES",
			  		    "NO"
			  		);
			  	statusBox.setValue(statusBox.getItems().get(0));

			  	grid.add(statusBox, 1, 7);


			  	submitButton = new Button("SUBMIT");
			  	submitButton.setOnAction(new EventHandler<ActionEvent>() {

			      		     @Override
			      		     public void handle(ActionEvent e) {
			      		     	processAction(e);
			           	     }
			       	});
			  	grid.add(submitButton, 0, 8);

			  	doneButton = new Button("DONE");
			  	doneButton.setOnAction(new EventHandler<ActionEvent>() {

			      		     @Override
			      		     public void handle(ActionEvent e) {
			      		     myPatron.done();
			           	     }
			       	});
			  	grid.add(doneButton, 1, 8);



					return grid;
				}
//---------------------------------------------------------------------

				// Create the status log field
				//-------------------------------------------------------------
				private MessageView createStatusLog(String initialMessage)
				{

					statusLog = new MessageView(initialMessage);

					return statusLog;
				}

				//-------------------------------------------------------------

			//-EDITED UP TO EVENT HANDLER's FOR MITRA ASGN2-----------------
			//--------------------------------------------------------------
			//--------------------------------------------------------------


				// This method processes events generated from our GUI components.
				// Make the ActionListeners delegate to this method
				//THIS METHOD IS A HELPER ACTION
				//-------------------------------------------------------------
				public void processAction(Event evt)
				{
					clearErrorMessage();
					String nameEntered = nameField.getText();
					String addressEntered = addressField.getText();
					String cityEntered = cityField.getText();
					String stateCodeEntered = stateCodeField.getText();
					String zipEntered = zipField.getText();
					String emailEntered = emailField.getText();
					String dateOfBirthEntered = dateOfBirthField.getText();
					String statusEntered = (String)statusBox.getItems().get(0);

					//int pubYearNumeric = Integer.parseInt(pubYearEntered);

					if((nameEntered == null) || (addressEntered == null)
							|| (cityEntered == null) || (stateCodeEntered == null)
							|| (zipEntered == null) || (emailEntered == null)
							|| (dateOfBirthEntered == null))
					{
						displayErrorMessage("Error in input fields");
					}
					else
					{


						Properties patronInsert = new Properties();
						patronInsert.setProperty("name", nameEntered);
						patronInsert.setProperty("address", addressEntered);
						patronInsert.setProperty("city", cityEntered);
						patronInsert.setProperty("stateCode", stateCodeEntered);
						patronInsert.setProperty("zip", zipEntered);
						patronInsert.setProperty("email", emailEntered);
						patronInsert.setProperty("dateOfBirth", dateOfBirthEntered);
						patronInsert.setProperty("status", statusEntered);
						myPatron.setData(patronInsert);
						myPatron.update();
						System.out.println("Patron Data for new Patron installed successfully into the database");

					}
				}

				/**
				 * Process userid and pwd supplied when Submit button is hit.
				 * Action is to pass this info on to the teller object
				 */
				//----------------------------------------------------------

				//---------------------------------------------------------
				public void updateState(String key, Object value)
				{
					// STEP 6: Be sure to finish the end of the 'perturbation'
					// by indicating how the view state gets updated.
					if (key.equals("LoginError") == true)
					{
						// display the passed text
						displayErrorMessage((String)value);
					}

				}

				/**
				 * Display error message
				 */
				//----------------------------------------------------------
				public void displayErrorMessage(String message)
				{
					statusLog.displayErrorMessage(message);
				}

				/**
				 * Display message
				 */
				//----------------------------------------------------------
				public void displayMessage(String message)
				{
					statusLog.displayMessage(message);
				}

				/**
				 * Clear error message
				 */
				//----------------------------------------------------------
				public void clearErrorMessage()
				{
					statusLog.clearErrorMessage();
				}




}
