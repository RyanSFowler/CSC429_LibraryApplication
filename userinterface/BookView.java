
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

/** The class containing the Librarian View  for the HW2 application */
//==============================================================
public class BookView extends View
{

	// GUI stuff
	private TextField authorField;
	private TextField titleField;
	private TextField pubYearField;
	private ComboBox statusBox;

	private Button submitButton;
	private Button doneButton;


	private Book myBook;

	// For showing error message
	private MessageView statusLog;

	// constructor for this class -- takes a model object
	//----------------------------------------------------------
	public BookView( Book book)
	{

		super (book, "BookView");

		myBook = book;

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
   	//---------------------------------------------
   	Label authorLabel = new Label("Author:");
   	grid.add(authorLabel, 0, 0);

   	authorField = new TextField();

  	authorField.setOnAction(new EventHandler<ActionEvent>() {

      		     @Override
      		     public void handle(ActionEvent e) {
      		     	//myLibrarian.createNewBook();

           	     }
       	});
  	grid.add(authorField, 1, 0);

   	//---------------------------------------------
   	Label titleLabel = new Label("Title:");
   	grid.add(titleLabel, 0, 1);

   	titleField = new TextField();

  	titleField.setOnAction(new EventHandler<ActionEvent>() {

      		     @Override
      		     public void handle(ActionEvent e) {
      		     	//myLibrarian.createNewBook();

           	     }
       	});
  	grid.add(titleField, 1, 1);
   	//---------------------------------------------

   	Label pubYearLabel = new Label("Publication Year:");
   	grid.add(pubYearLabel, 0, 2);

   	pubYearField = new TextField();

  	pubYearField.setOnAction(new EventHandler<ActionEvent>() {

      		     @Override
      		     public void handle(ActionEvent e) {
      		     	//myLibrarian.createNewBook();

           	     }
       	});
  	grid.add(pubYearField, 1, 2);

   	//---------------------------------------------
  	Label statusBoxLabel = new Label("Status:");
  	grid.add(statusBoxLabel, 0, 3);
  	statusBox = new ComboBox();
  	statusBox.getItems().addAll(
  		    "IN",
  		    "OUT"
  		);
  	statusBox.setValue(statusBox.getItems().get(0));

  	grid.add(statusBox, 1, 3);

  	//---------------------------------------------------

  	submitButton = new Button("SUBMIT");
  	submitButton.setOnAction(new EventHandler<ActionEvent>() {

      		     @Override
      		     public void handle(ActionEvent e) {
      		     	processAction(e);
           	     }
       	});
  	grid.add(submitButton, 0, 4);

  	doneButton = new Button("DONE");
  	doneButton.setOnAction(new EventHandler<ActionEvent>() {

      		     @Override
      		     public void handle(ActionEvent e) {
      		    	 myBook.done();
      		     	//processAction(e);

           	     }
       	});
  	grid.add(doneButton, 1, 4);



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
		clearErrorMessage();
		String authorEntered = authorField.getText();
		String titleEntered = titleField.getText();
		String pubYearEntered = pubYearField.getText();
		String statusEntered = (String)statusBox.getValue();
		int pubYearNumeric = 0;
		if(!pubYearEntered.equals(""))
		{
			pubYearNumeric = Integer.parseInt(pubYearEntered);
		}

		if((authorEntered == null) || (titleEntered == null)
				|| (pubYearNumeric < 1800) || (pubYearNumeric > 2016))
		{
			displayErrorMessage("Error in input fields");
		}
		else
		{

			//--------THE FOLLOWING DOES NOT UPDATE DATABASE 2-22 (YES, IT DOES - 2-25-16: TOM)
			Properties bookInsert = new Properties();
			bookInsert.setProperty("author", authorEntered);
			bookInsert.setProperty("title", titleEntered);
			bookInsert.setProperty("pubYear", pubYearEntered);
			bookInsert.setProperty("status", statusEntered);

			myBook.setData(bookInsert);
			myBook.update();
			//displayMessage("Book Inserted!");

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

	//-LIBRARIAN VIEW METHODS-----------------------------------

}
