// specify the package
package model;

// system imports
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.JFrame;



// project imports
import exception.InvalidPrimaryKeyException;
import database.*;
import impresario.IView;
import userinterface.*;
import userinterface.BookView;


/** The class containing the Account for the ATM application */
//==============================================================
public class Book extends EntityBase implements IView
{
	private static final String myTableName = "Book";

	protected Properties dependencies;
	protected Stage myStage;
	protected Librarian myLibrarian;

	// GUI Components

	private String updateStatusMessage = "";

	// constructor for this class
	//----------------------------------------------------------
	public Book(int bookId)
		throws InvalidPrimaryKeyException
	{
		super(myTableName);

		setDependencies();
		String query = "SELECT * FROM " + myTableName + " WHERE (bookId = " + bookId + ")";

		Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

		// You must get one account at least
		if (allDataRetrieved != null)
		{
			int size = allDataRetrieved.size();

			// There should be EXACTLY one bookIds. More than that is an error
			if (size != 1)
			{
				throw new InvalidPrimaryKeyException("Multiple accounts matching id : "
					+ bookId + " found.");
			}
			else
			{
				// copy all the retrieved data into persistent state
				Properties retrievedBookData = allDataRetrieved.elementAt(0);
				persistentState = new Properties();

				Enumeration allKeys = retrievedBookData.propertyNames();
				while (allKeys.hasMoreElements() == true)
				{
					String nextKey = (String)allKeys.nextElement();
					String nextValue = retrievedBookData.getProperty(nextKey);

					if (nextValue != null)
					{
						persistentState.setProperty(nextKey, nextValue);
					}
				}

			}
		}
		// If no Book found for this id, throw an exception
		else
		{
			throw new InvalidPrimaryKeyException("No book matching id : "
				+ bookId  + " found.");
		}
	}

	// Can also be used to create a NEW Book (if the system it is part of
	// allows for a new book to be set up)
	//----------------------------------------------------------
	public Book(Properties props)
	{
		super(myTableName);

		setDependencies();
		setData(props);
	}
	
	public Book(Librarian l)
		{
		super(myTableName);
		myStage = MainStageContainer.getInstance();
		myLibrarian = l;
			
		}
	

	//-----------------------------------------------------------------------------------
	public void setData(Properties props)
	{
		persistentState = new Properties();
		Enumeration allKeys = props.propertyNames();
		while (allKeys.hasMoreElements() == true)
		{
			String nextKey = (String)allKeys.nextElement();
			String nextValue = props.getProperty(nextKey);

			if (nextValue != null)
			{
				persistentState.setProperty(nextKey, nextValue);
			}
		}
	}
	//-----------------------------------------------------------------------------------
	
	private void setDependencies()
	{
		dependencies = new Properties();
	
		myRegistry.setDependencies(dependencies);
	}

	//----------------------------------------------------------
	public Object getState(String key)
	{
		if (key.equals("UpdateStatusMessage") == true)
			return updateStatusMessage;

		return persistentState.getProperty(key);
	}

	//----------------------------------------------------------------
	public void stateChangeRequest(String key, Object value)
	{

		myRegistry.updateSubscribers(key, this);
	}

	/** Called via the IView relationship */
	//----------------------------------------------------------
	public void updateState(String key, Object value)
	{
		stateChangeRequest(key, value);
	}



	
	//-----------------------------------------------------------------------------------
	public static int compare(Book a, Book b)
	{
		String aNum = (String)a.getState("bookId");//this or title
		String bNum = (String)b.getState("bookId");

		return aNum.compareTo(bNum);
	}

	//-----------------------------------------------------------------------------------
	//****Needs to be modified.
	public void update()
	{
		updateStateInDatabase();
	}
	
	//-----------------------------------------------------------------------------------
	private void updateStateInDatabase() 
	{
		try
		{
			if (persistentState.getProperty("bookId") != null)
			{
				Properties whereClause = new Properties();
				whereClause.setProperty("bookId",
				persistentState.getProperty("bookId"));
				updatePersistentState(mySchema, persistentState, whereClause);
				updateStatusMessage = "Book data for book number : " + persistentState.getProperty("bookId") + " updated successfully in database!";
				System.out.println("Trying to update Book");
			}
			else
			{
				
				Integer bookId =
					insertAutoIncrementalPersistentState(mySchema, persistentState);
				persistentState.setProperty("bookId", "" + bookId.intValue());
				System.out.println("Book data for new book installed successfully in database");
				updateStatusMessage = "Book data for new book : " +  persistentState.getProperty("bookId")
					+ "installed successfully in database!";
			}
			Scene currentScene = (Scene)myViews.get("BookView");

			if (currentScene != null)
			{
					((BookView)currentScene.getRoot()).displayMessage("Book saved successfully!");
			}
			
		}
		catch (SQLException ex)
		{
			System.out.println("Error in installing book data in database!");
			updateStatusMessage = "Error in installing book data in database!";
			
			Scene currentScene = (Scene)myViews.get("BookView");

			if (currentScene != null)
			{
					((BookView)currentScene.getRoot()).displayErrorMessage("ERROR in book save!");
			}
		}
		//DEBUG System.out.println("updateStateInDatabase " + updateStatusMessage);
	}


	/**
	 * This method is needed solely to enable the Book information to be displayable in a table
	 *
	 */
	//--------------------------------------------------------------------------
	public Vector<String> getEntryListView()
	{
		Vector<String> v = new Vector<String>();

		v.addElement(persistentState.getProperty("bookId"));
		v.addElement(persistentState.getProperty("author"));
		v.addElement(persistentState.getProperty("title"));
		v.addElement(persistentState.getProperty("pubYear"));
		v.addElement(persistentState.getProperty("status"));

		return v;
	}

	//-----------------------------------------------------------------------------------
	protected void initializeSchema(String tableName)
	{
		if (mySchema == null)
		{
			mySchema = getSchemaInfo(tableName);
		}
	}
	
	//-------------------------------------------------------------------------
	public String toString()
	{
		return persistentState.toString();
	}
	//-------------------------------------------------------------------------
	//NEW METHODS 2-21-16------------------------------------------------------
	public void createAndShowBookView()
	{
		Scene currentScene = (Scene)myViews.get("BookView");

		if (currentScene == null)
		{
			// create our initial view
			View newView = new BookView(this); // USE VIEW FACTORY
			currentScene = new Scene(newView);
			myViews.put("BookView", currentScene);
		}
				
		swapToView(currentScene);
		
	}
	
	public void done()
	{
		myLibrarian.transactionDone();
		
	}
}

