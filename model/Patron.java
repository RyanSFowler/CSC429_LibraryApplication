// specify the package
package model;

// system imports
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

import javax.swing.JFrame;

import javafx.scene.Scene;
import javafx.stage.Stage;

// project imports
import exception.InvalidPrimaryKeyException;
import database.*;
import impresario.IView;
import userinterface.*;

/** The class containing the Account for the ATM application */
//==============================================================
public class Patron extends EntityBase implements IView
{
	private static final String myTableName = "Patron";

	protected Properties dependencies;
	protected Stage myStage;
	protected Librarian myLibrarian;

	// GUI Components

	private String updateStatusMessage = "";

	// constructor for this class
	//----------------------------------------------------------
	public Patron(String patronId)
		throws InvalidPrimaryKeyException
	{
		super(myTableName);

		setDependencies();
		String query = "SELECT * FROM " + myTableName + " WHERE (patronId = " + patronId + ")";

		Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

		// You must get one account at least
		if (allDataRetrieved != null)
		{
			int size = allDataRetrieved.size();

			// There should be EXACTLY one account. More than that is an error
			if (size != 1)
			{
				throw new InvalidPrimaryKeyException("Multiple patronIds matching : "
					+ patronId + " found.");
			}
			else
			{
				// copy all the retrieved data into persistent state
				Properties retrievedPatronData = allDataRetrieved.elementAt(0);
				persistentState = new Properties();

				Enumeration allKeys = retrievedPatronData.propertyNames();
				while (allKeys.hasMoreElements() == true)
				{
					String nextKey = (String)allKeys.nextElement();
					String nextValue = retrievedPatronData.getProperty(nextKey);

					if (nextValue != null)
					{
						persistentState.setProperty(nextKey, nextValue);
					}
				}

			}
		}
		// If no Patron was found for this user name, throw an exception
		else
		{
			throw new InvalidPrimaryKeyException("No patron matching id : "
				+ patronId + " found.");
		}
	}

	// Can also be used to create a NEW patron(if the system it is part of
	// allows for a new account to be set up)
	//----------------------------------------------------------
	public Patron(Properties props)
	{
		super(myTableName);

		setDependencies();
		setData(props);
		
	}

	public Patron(Librarian l)
	{
		super(myTableName);
		myStage = MainStageContainer.getInstance();
		myLibrarian = l;
	}
	
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
	public static int compare(Patron a, Patron b)
	{
		String aNum = (String)a.getState("patronId");
		String bNum = (String)b.getState("patronId");

		return aNum.compareTo(bNum);
	}

	//-----------------------------------------------------------------------------------
	public void update()
	{
		updateStateInDatabase();
	}
	
	//-----------------------------------------------------------------------------------
	private void updateStateInDatabase() 
	{
		try
		{
			if (persistentState.getProperty("patronId") != null)
			{
				Properties whereClause = new Properties();
				whereClause.setProperty("patronId",
				persistentState.getProperty("patronId"));
				updatePersistentState(mySchema, persistentState, whereClause);
				updateStatusMessage = "Patron data for patronId : " + persistentState.getProperty("patronId") + " updated successfully in database!";
			}
			else
			{
				Integer patronId =
					insertAutoIncrementalPersistentState(mySchema, persistentState);
				persistentState.setProperty("patronId", "" + patronId.intValue());
				updateStatusMessage = "patronId for new account : " +  persistentState.getProperty("patronId")
					+ "installed successfully in database!";
			}
			Scene currentScene = (Scene)myViews.get("PatronView");
			if (currentScene != null)
			{
					((PatronView)currentScene.getRoot()).displayMessage("Patron saved successfully!");
			}

		}
		catch (SQLException ex)
		{
			System.out.println("Error in installing patron data in database!");
			updateStatusMessage = "Error in installing patron data in database!";
			
			Scene currentScene = (Scene)myViews.get("PatronView");

			if (currentScene != null)
			{
					((PatronView)currentScene.getRoot()).displayErrorMessage("ERROR in patron save!");
			}
		}
		

		//DEBUG System.out.println("updateStateInDatabase " + updateStatusMessage);
	}


	/**
	 * This method is needed solely to enable the Account information to be displayable in a table
	 *
	 */
	//--------------------------------------------------------------------------
	public Vector<String> getEntryListView()
	{
		Vector<String> v = new Vector<String>();

		v.addElement(persistentState.getProperty("patronId"));
		v.addElement(persistentState.getProperty("name"));
		v.addElement(persistentState.getProperty("address"));
		v.addElement(persistentState.getProperty("city"));
		v.addElement(persistentState.getProperty("stateCode"));
		v.addElement(persistentState.getProperty("zip"));
		v.addElement(persistentState.getProperty("email"));
		v.addElement(persistentState.getProperty("dateOfBirth"));
		
		

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
	
	//---------------------------------------------------------------------------------------
	public String toString()
	{
		return persistentState.toString();
	}
	
	
	public void createAndShowPatronView()
	{
		Scene currentScene = (Scene)myViews.get("PatronView");

		if (currentScene == null)
		{
			// create our initial view
			View newView = new PatronView(this); // USE VIEW FACTORY
			currentScene = new Scene(newView);
			myViews.put("PatronView", currentScene);
		}
				
		swapToView(currentScene);
		
	}
	
	public void done()
	{
		myLibrarian.transactionDone();
		
	}
}

