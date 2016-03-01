// specify the package
package model;

// system imports
import java.util.Properties;
import java.util.Vector;
import javafx.scene.Scene;

// project imports
import exception.InvalidPrimaryKeyException;
import event.Event;
import database.*;

import impresario.IView;

import userinterface.View;
import userinterface.ViewFactory;


/** The class containing the TransactionCollection */
//==============================================================
public class TransactionCollection  extends EntityBase implements IView
{
	private static final String myTableName = "Transaction";

	private Vector<Transaction> transactions;
	// GUI Components

	// constructor for this class
	//----------------------------------------------------------
	public TransactionCollection()
	{
		super(myTableName);

		transactions = new Vector<Transaction>();

	}
	

	//----------------------------------------------------------------------------------
	public void addTransaction(Transaction a)
	{
		//accounts.add(a);
		int index = findIndexToAdd(a);
		transactions.insertElementAt(a,index); // To build up a collection sorted on some key
	}

	//----------------------------------------------------------------------------------
	private int findIndexToAdd(Transaction a)
	{
		//users.add(u);
		int low=0;
		int high = transactions.size()-1;
		int middle;

		while (low <=high)
		{
			middle = (low+high)/2;

			Transaction midSession = transactions.elementAt(middle);

			int result = Transaction.compare(a,midSession);

			if (result ==0)
			{
				return middle;
			}
			else if (result<0)
			{
				high=middle-1;
			}
			else
			{
				low=middle+1;
			}


		}
		return low;
	}


	/**
	 *
	 */
	//----------------------------------------------------------
	public Object getState(String key)
	{
		if (key.equals("Transactions"))
			return transactions;
		else
		if (key.equals("TransactionList"))
			return this;
		return null;
	}

	//----------------------------------------------------------------
	public void stateChangeRequest(String key, Object value)
	{
		
		myRegistry.updateSubscribers(key, this);
	}

	//----------------------------------------------------------
	public Transaction retrieve(String transId)
	{
		Transaction retValue = null;
		for (int cnt = 0; cnt < transactions.size(); cnt++)
		{
			Transaction nextTrans = transactions.elementAt(cnt);
			String nextTransId = (String)nextTrans.getState("TransactionId");
			if (nextTransId.equals(transId) == true)
			{
				retValue = nextTrans;
				return retValue; // we should say 'break;' here
			}
		}

		return retValue;
	}

	/** Called via the IView relationship */
	//----------------------------------------------------------
	public void updateState(String key, Object value)
	{
		stateChangeRequest(key, value);
	}

	//------------------------------------------------------
	protected void createAndShowView()
	{

		Scene localScene = myViews.get("TransactionCollectionView");

		if (localScene == null)
		{
				// create our new view
				View newView = ViewFactory.createView("TransactionCollectionView", this);
				localScene = new Scene(newView);
				myViews.put("TransactionCollectionView", localScene);
		}
		// make the view visible by installing it into the frame
		swapToView(localScene);
		
	}

	//-----------------------------------------------------------------------------------
	protected void initializeSchema(String tableName)
	{
		if (mySchema == null)
		{
			mySchema = getSchemaInfo(tableName);
		}
	}

	//----------------------------------------------------------------------------------
	public void findMatchingTransactions(String bookId, String patronId, String dateOfTrans)
	{
		int andCount = 0;
		String query = "SELECT * FROM " + myTableName + " WHERE (";
		if(bookId != null)
		{
			andCount++;
			query = query + "bookId = " + bookId;
		}
		if(patronId != null)
		{
		
			if(andCount > 0)
			{
				query = query + " AND ";
			}
			query = query + "patronId = " + patronId;
		}
		if(dateOfTrans != null)
		{
			if(andCount > 0)
			{
				query = query + " AND ";
			}
			query = query + "dateOfTrans = '" + dateOfTrans + "'";
		}

		query = query + ")";
		System.out.println(query);
		Vector allDataRetrieved = getSelectQueryResult(query);
		if (allDataRetrieved != null)
		{
			transactions = new Vector<Transaction>();
			for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
			{
				Properties nextTransData = (Properties)allDataRetrieved.elementAt(cnt);
				System.out.println(nextTransData);
				Transaction transaction = new Transaction(nextTransData);
				if (transaction != null)
				{
					addTransaction(transaction);
				}
			}
			System.out.println();
		}
		else{}
	}

}
