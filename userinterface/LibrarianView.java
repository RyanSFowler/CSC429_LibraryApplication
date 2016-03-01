
//specify the package
package userinterface;

//system imports
import java.text.NumberFormat;
import java.util.Properties;

import model.Book;
import javafx.event.Event;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

/** The class containing the Librarian View  for the HW2 application */
//==============================================================
public class LibrarianView extends View
{

	// GUI stuff
	//private TextField userid;
	//private PasswordField password;
	//private Button submitButton;

	private Button insertBookButton;
	private Button insertPatronButton;
	private Button searchBooksButton;
	private Button doneButton;

	private Librarian myLibrarian;

	// For showing error message
	private MessageView statusLog;

	// constructor for this class -- takes a model object
	//----------------------------------------------------------
	public LibrarianView( Librarian librarian)
	{

		super (librarian, "LibrarianView");

		myLibrarian = librarian;

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

		// data entry fields
    	insertBookButton = new Button("INSERT NEW BOOK");
    	insertBookButton.setOnAction(new EventHandler<ActionEvent>() {

        		     @Override
        		     public void handle(ActionEvent e) {
        		     	myLibrarian.createNewBook();

             	     }
         	});
    	grid.add(insertBookButton, 0, 0);

    	insertPatronButton = new Button("INSERT NEW PATRON");
    	insertPatronButton.setOnAction(new EventHandler<ActionEvent>() {

        		     @Override
        		     public void handle(ActionEvent e) {
        		    	 myLibrarian.createNewPatron();

             	     }
         	});
    	grid.add(insertPatronButton, 0, 1);


    	searchBooksButton = new Button("SEARCH BOOKS");
    	searchBooksButton.setOnAction(new EventHandler<ActionEvent>() {

        		     @Override
        		     public void handle(ActionEvent e) {
        		     	myLibrarian.createAndShowSearchView();
             	     }
         	});
    	grid.add(searchBooksButton, 0, 2);

    	doneButton = new Button("DONE");
    	doneButton.setOnAction(new EventHandler<ActionEvent>() {

        		     @Override
        		     public void handle(ActionEvent e) {
        		     	myLibrarian.done();   
             	     }
         	});
    	grid.add(doneButton, 0, 3);



		return grid;
	}









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
		// DEBUG: System.out.println("TellerView.actionPerformed()");



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
	 * Clear error message
	 */
	//----------------------------------------------------------
	public void clearErrorMessage()
	{
		statusLog.clearErrorMessage();
	}

	//-LIBRARIAN VIEW METHODS-----------------------------------

}
