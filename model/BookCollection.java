// specify the package
package model;

// system imports
import java.util.Properties;
import java.util.Vector;

import javafx.scene.Scene;
import javafx.stage.Stage;

// project imports
import exception.InvalidPrimaryKeyException;
import event.Event;
import database.*;
import impresario.IView;
import userinterface.BookView;
import userinterface.MainStageContainer;
import userinterface.View;
import userinterface.ViewFactory;
import userinterface.BookCollectionView;
//import userinterface.BookCollectionView;


/** The class containing the BookCollection */
//==============================================================
public class BookCollection  extends EntityBase implements IView
{
	private static final String myTableName = "Book";

	private Vector<Book> books;
	protected Stage myStage;
	protected Librarian myLibrarian;
	
	// GUI Components

	// constructor for this class
	//----------------------------------------------------------
	public BookCollection()   //(BookHolder cust) for param is removed
	{
		super(myTableName);
		books = new Vector<Book>();
	}
	
	public BookCollection(Librarian l)
	{
		super(myTableName);
		myStage = MainStageContainer.getInstance();
		myLibrarian = l;
	}
	//----------------------------------------------------------------------------------
	public void addBook(Book a)
	{
		//book.add(a);
		int index = findIndexToAdd(a);
		books.insertElementAt(a,index); // To build up a collection sorted on some key
	}
	//----------------------------------------------------------------------------------
	private int findIndexToAdd(Book b)
	{
		//users.add(u);
		int low=0;
		int high = books.size()-1;
		int middle;

		while (low <=high)
		{
			middle = (low+high)/2;

			Book midSession = books.elementAt(middle);

			int result = Book.compare(b,midSession);

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
		if (key.equals("Books"))
			return books;
		else
		if (key.equals("BookList"))
			return this;
		return null;
	}

	//----------------------------------------------------------------
	public void stateChangeRequest(String key, Object value)
	{
		
		myRegistry.updateSubscribers(key, this);
	}

	//----------------------------------------------------------
	public Book retrieve(String bookId)
	{
		Book retValue = null;
		for (int cnt = 0; cnt < books.size(); cnt++)
		{
			Book nextBk = books.elementAt(cnt);
			String nextBkNum = (String)nextBk.getState("BookId");
			if (nextBkNum.equals(bookId) == true)
			{
				retValue = nextBk;
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

		Scene localScene = myViews.get("BookCollectionView");

		if (localScene == null)
		{
				// create our new view
				View newView = ViewFactory.createView("BookCollectionView", this);
				localScene = new Scene(newView);
				myViews.put("BookCollectionView", localScene);
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

	//method to find books older than _____
	public void findBooksOlderThanDate(String year)
	{

		String query = "SELECT * FROM " + myTableName + " WHERE (pubYear < " + year + ")";

		Vector allDataRetrieved = getSelectQueryResult(query);

		if (allDataRetrieved != null)
		{
			books = new Vector<Book>();

			for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
			{
				Properties nextBookData = (Properties)allDataRetrieved.elementAt(cnt);
				
				Book book = new Book(nextBookData);

				if (book != null)
				{
					addBook(book);
				}
			}

		}
		else{}

	}

	public void findBooksNewerThanDate(String year)
	{

		//SQL data?
		//Select Books From BookCollection Where (pubYear > year);
		String query = "SELECT * FROM " + myTableName + " WHERE (pubYear > " + year + ")";

		Vector allDataRetrieved = getSelectQueryResult(query);

		if (allDataRetrieved != null)
		{
			books = new Vector<Book>();

			for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
			{
				Properties nextBookData = (Properties)allDataRetrieved.elementAt(cnt);
				
				Book book = new Book(nextBookData);

				if (book != null)
				{
					addBook(book);
				}
			}

		}
		else{}

	}

	public void findBooksWithTitleLike(String title)
	{

		String query = "SELECT * FROM " + myTableName + " WHERE (title LIKE '%" + title + "%')";
		
		Vector allDataRetrieved = getSelectQueryResult(query);

		if (allDataRetrieved != null)
		{
			books = new Vector<Book>();

			for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
			{
				Properties nextBookData = (Properties)allDataRetrieved.elementAt(cnt);
			
				Book book = new Book(nextBookData);

				if (book != null)
				{
					addBook(book);
				}
			}

		}
		else{}


	}

	public void findBooksWithAuthorLike(String author)
	{
		String query = "SELECT * FROM " + myTableName + " WHERE (author LIKE '%" + author + "%')";
		Vector allDataRetrieved = getSelectQueryResult(query);
		if (allDataRetrieved != null)
		{
			books = new Vector<Book>();
			for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
			{
				Properties nextBookData = (Properties)allDataRetrieved.elementAt(cnt);
				
				Book book = new Book(nextBookData);
				if (book != null)
				{
					addBook(book);
				}
			}
		}
		else{}
	}
	
	//---------------------------------------------------------------
	public void display()
	{
		for (int i = 0; i < books.size(); i++)
		{
			System.out.println(books.get(i));
		}
	}
	//---------------------------------------------------------------
	public void createAndShowBookCollectionView()
	{
		Scene currentScene = (Scene)myViews.get("BookCollectionView");
		
		if (currentScene == null)
		{
			// create our initial view
			View newView = new BookCollectionView(this); // USE VIEW FACTORY
			currentScene = new Scene(newView);
			myViews.put("BookCollectionView", currentScene);
		}
				
		swapToView(currentScene);
		
		
	}
	
	public void done()
	{
		myLibrarian.transactionDone();
		
	}
}





