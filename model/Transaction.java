// specify the package
package model;

// system imports
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;
import javax.swing.JFrame;

// project imports
import exception.InvalidPrimaryKeyException;
import database.*;

import impresario.IView;

import userinterface.View;
import userinterface.ViewFactory;

/** The class containing the Account for the ATM application */
//==============================================================
public class Transaction extends EntityBase implements IView
{
	private static final String myTableName = "Transaction";

	protected Properties dependencies;

	// GUI Components

	private String updateStatusMessage = "";

	// constructor for this class
	//----------------------------------------------------------
	public Transaction(String transId)
		throws InvalidPrimaryKeyException
	{
		super(myTableName);

		setDependencies();
		String query = "SELECT * FROM " + myTableName + " WHERE (transId = " + transId + ")";

		Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

		// You must get one account at least
		if (allDataRetrieved != null)
		{
			int size = allDataRetrieved.size();

			// There should be EXACTLY one Transaction with this Id. More than that is an error
			if (size != 1)
			{
				throw new InvalidPrimaryKeyException("Multiple transactions matching id : "
					+ transId + " found.");
			}
			else
			{
				// copy all the retrieved data into persistent state
				Properties retrievedTransactionData = allDataRetrieved.elementAt(0);
				persistentState = new Properties();

				Enumeration allKeys = retrievedTransactionData.propertyNames();
				while (allKeys.hasMoreElements() == true)
				{
					String nextKey = (String)allKeys.nextElement();
					String nextValue = retrievedTransactionData.getProperty(nextKey);

					if (nextValue != null)
					{
						persistentState.setProperty(nextKey, nextValue);
					}
				}

			}
		}
		// If no transaction found for this id, throw an exception
		else
		{
			throw new InvalidPrimaryKeyException("No transaction matching id : "
				+ transId + " found.");
		}
	}

	// Can also be used to create a NEW Transaction (if the system it is part of
	// allows for a new transaction to be set up)
	//----------------------------------------------------------
	public Transaction(Properties props)
	{
		super(myTableName);

		setDependencies();
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
	public static int compare(Transaction a, Transaction b)
	{
		String aNum = (String)a.getState("transId");
		String bNum = (String)b.getState("transId");

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
			if (persistentState.getProperty("transId") != null)
			{
				Properties whereClause = new Properties();
				whereClause.setProperty("transId",
				persistentState.getProperty("transId"));
				updatePersistentState(mySchema, persistentState, whereClause);
				updateStatusMessage = "Transaction data for transaction ID: " + persistentState.getProperty("transId") + " updated successfully in database!";
			}
			else
			{
				Integer transId =
					insertAutoIncrementalPersistentState(mySchema, persistentState);
				persistentState.setProperty("transId", "" + transId.intValue());
				updateStatusMessage = "Transaction data for new transaction : " +  persistentState.getProperty("transId")
					+ "installed successfully in database!";
			}
		}
		catch (SQLException ex)
		{
			updateStatusMessage = "Error in installing transaction data in database!";
		}
		//DEBUG System.out.println("updateStateInDatabase " + updateStatusMessage);
	}


	/**
	 * This method is needed solely to enable the Transaction information to be displayable in a table
	 *
	 */
	//--------------------------------------------------------------------------
	public Vector<String> getEntryListView()
	{
		Vector<String> v = new Vector<String>();

		v.addElement(persistentState.getProperty("transId"));
		v.addElement(persistentState.getProperty("bookId"));
		v.addElement(persistentState.getProperty("patronId"));
		v.addElement(persistentState.getProperty("patronId"));
		v.addElement(persistentState.getProperty("transType"));
		v.addElement(persistentState.getProperty("dateOfTrans"));

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
}

