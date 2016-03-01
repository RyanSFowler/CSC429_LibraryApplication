
//specify the package
package model;

//system imports
import java.util.Hashtable;


import java.util.Properties;

import javafx.stage.Stage;
import javafx.scene.Scene;

//project imports
import impresario.IModel;
import impresario.ISlideShow;
import impresario.IView;
import impresario.ModelRegistry;
import exception.InvalidPrimaryKeyException;
import exception.PasswordMismatchException;
import event.Event;
import userinterface.MainStageContainer;
import userinterface.View;
import userinterface.ViewFactory;
import userinterface.WindowPosition;
import userinterface.LibrarianView;
import userinterface.SearchView;

/** The class containing the Librarian  for the Library application */
//==============================================================
public class Librarian implements IView,IModel
//This class implements all these interfaces (and does NOT extend 'EntityBase')
//because it does NOT play the role of accessing the back-end database tables.
//It only plays a front-end role. 'EntityBase' objects play both roles.
{
	// For Impresario
	private Properties dependencies;
	private ModelRegistry myRegistry;

	//private AccountHolder myAccountHolder;

	// GUI Components
	private Hashtable<String, Scene> myViews;
	private Stage	  	myStage;

	private String loginErrorMessage = "";
	private String transactionErrorMessage = "";

	// constructor for this class
	//----------------------------------------------------------
	public Librarian()
	{
		myStage = MainStageContainer.getInstance();
		myViews = new Hashtable<String, Scene>();

		// STEP 3.1: Create the Registry object - if you inherit from
		// EntityBase, this is done for you. Otherwise, you do it yourself
		myRegistry = new ModelRegistry("Librarian");
		if(myRegistry == null)
		{
			new Event(Event.getLeafLevelClassName(this), "Librarian",
				"Could not instantiate Registry", Event.ERROR);
		}

		// STEP 3.2: Be sure to set the dependencies correctly
		setDependencies();

		// Set up the initial view
		createAndShowLibrarianView();
	}

	//-----------------------------------------------------------------------------------
	private void setDependencies()
	{

	}

	public void stateChangeRequest(String key, Object value)
	{


		myRegistry.updateSubscribers(key, this);
	}


	//----------------------------------------------------------
		public Object getState(String key)
		{
			return null;
		}

	/** Called via the IView relationship */
	//----------------------------------------------------------
	public void updateState(String key, Object value)
	{
		// DEBUG System.out.println("Teller.updateState: key: " + key);

		stateChangeRequest(key, value);
	}
	public void done()
	{
		System.exit(0);
	}


	/**
	 * Create a Transaction depending on the Transaction type (deposit,
	 * withdraw, transfer, etc.). Use the AccountHolder holder data to do the
	 * create.
	 */
	//----------------------------------------------------------



	//LIBRARIAN METHODS
	//------------------------------------------------------------
	public void createNewBook()
	{
		Book b = new Book(this);
		b.createAndShowBookView();
	}

	public void createNewPatron()
	{
		Patron p = new Patron(this);
		p.createAndShowPatronView();
	}

	public void searchBooks(String searchEntered)
	{
		BookCollection bc = new BookCollection(this);
		bc.findBooksWithTitleLike(searchEntered);
		bc.createAndShowBookCollectionView();
	}

	//CREATE AND SHOW LIBRARIAN VIEW----------------------------
	private void createAndShowLibrarianView()
	{
		Scene currentScene = (Scene)myViews.get("LibrarianView");

		if (currentScene == null)
		{
			// create our initial view
			View newView = new LibrarianView(this); // USE VIEW FACTORY
			currentScene = new Scene(newView);
			myViews.put("LibrarianView", currentScene);
		}

		swapToView(currentScene);

	}

	public void createAndShowSearchView()
	{
		Scene currentScene = (Scene)myViews.get("SearchView");

		if (currentScene == null)
		{
			// create our initial view
			View newView = new SearchView(this); // USE VIEW FACTORY
			currentScene = new Scene(newView);
			myViews.put("BookCollectionView", currentScene);
		}

		swapToView(currentScene);

	}
	//---------------------------------------------------------
	public void transactionDone()
	{
		createAndShowLibrarianView();
	}

	/** Register objects to receive state updates. */
	//----------------------------------------------------------
	public void subscribe(String key, IView subscriber)
	{
		// DEBUG: System.out.println("Cager[" + myTableName + "].subscribe");
		// forward to our registry
		myRegistry.subscribe(key, subscriber);
	}

	/** Unregister previously registered objects. */
	//----------------------------------------------------------
	public void unSubscribe(String key, IView subscriber)
	{
		// DEBUG: System.out.println("Cager.unSubscribe");
		// forward to our registry
		myRegistry.unSubscribe(key, subscriber);
	}



	//-----------------------------------------------------------------------------
	public void swapToView(Scene newScene)
	{


		if (newScene == null)
		{
			System.out.println("Librarian.swapToView(): Missing view for display");
			new Event(Event.getLeafLevelClassName(this), "swapToView",
				"Missing view for display ", Event.ERROR);
			return;
		}

		myStage.setScene(newScene);
		myStage.sizeToScene();


		//Place in center
		WindowPosition.placeCenter(myStage);

	}

}
