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
public class SearchView extends View
{
	private TextField searchField;
	private MessageView statusLog;

	private Button submitButton;
	private Button cancelButton;
	
	private Librarian myLibrarian;
	public SearchView(Librarian l)
	{
		super(l, "SearchView");
		myLibrarian = l;
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
	   	Label searchLabel = new Label("Search Books With Title Like:");
	   	grid.add(searchLabel, 0, 0);
	   	
	   	searchField = new TextField();
	  
	  	searchField.setOnAction(new EventHandler<ActionEvent>() {

	      		     @Override
	      		     public void handle(ActionEvent e) {
	      		     	
	      		    	 
	           	     }
	       	});
	  	grid.add(searchField, 1, 0);
	  	
	   	//---------------------------------------------
	   	
		
	  	//---------------------------------------------------
	  	
	  	submitButton = new Button("SUBMIT");
	  	submitButton.setOnAction(new EventHandler<ActionEvent>() {

	      		     @Override
	      		     public void handle(ActionEvent e) {
	      		     	processAction(e);    
	           	     }
	       	});
	  	grid.add(submitButton, 0, 1);
	  	
	  	cancelButton = new Button("CANCEL");
	  	cancelButton.setOnAction(new EventHandler<ActionEvent>() {

	      		     @Override
	      		     public void handle(ActionEvent e) {
	      		    	 myLibrarian.transactionDone();
	      		    	//return to LibrarianView
	           	     }
	       	});
	  	grid.add(cancelButton, 1, 1);
	  	


			return grid;
		}	

	// Create the status log field
	//-------------------------------------------------------------
	private MessageView createStatusLog(String initialMessage)
	{

		statusLog = new MessageView(initialMessage);

		return statusLog;
	}

//---------------------------------------------------------


	// This method processes events generated from our GUI components.
	// Make the ActionListeners delegate to this method
	//THIS METHOD IS A HELPER ACTION
	//-------------------------------------------------------------
	public void processAction(Event evt)
	{
		clearErrorMessage();
		String searchEntered = searchField.getText();
		
		
		if(searchEntered.equals(""))
		{
			displayErrorMessage("Error in input fields");
		}
		else
		{
			myLibrarian.searchBooks(searchEntered);
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
