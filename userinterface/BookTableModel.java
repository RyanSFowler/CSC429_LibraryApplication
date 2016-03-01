
package userinterface;

import java.util.Vector;

import javafx.beans.property.SimpleStringProperty;

//==============================================================================
public class BookTableModel
{
	private final SimpleStringProperty bookId;
	private final SimpleStringProperty author;
	private final SimpleStringProperty title;
	private final SimpleStringProperty pubYear;
	private final SimpleStringProperty status;

	//----------------------------------------------------------------------------
	public BookTableModel(Vector<String> bookData)
	{
		bookId =  new SimpleStringProperty(bookData.elementAt(0));
		author =  new SimpleStringProperty(bookData.elementAt(1));
		title =  new SimpleStringProperty(bookData.elementAt(2));
		pubYear =  new SimpleStringProperty(bookData.elementAt(3));
		status =  new SimpleStringProperty(bookData.elementAt(4));
	}

	//----------------------------------------------------------------------------
	public String getBookId() {
        return bookId.get();
    }

	//----------------------------------------------------------------------------
    public void setBookID(String number) {
        bookId.set(number);
    }

    //----------------------------------------------------------------------------
    public String getAuthor() {
        return author.get();
    }

    //----------------------------------------------------------------------------
    public void setAuthor(String aType) {
        author.set(aType);
    }

    //----------------------------------------------------------------------------
    public String getTitle() {
        return title.get();
    }

    //----------------------------------------------------------------------------
    public void setTitle(String aTitle) {
        title.set(aTitle);
    }
    
    //----------------------------------------------------------------------------
    public String getPubYear() {
        return pubYear.get();
    }

    //----------------------------------------------------------------------------
    public void setPubYear(String year)
    {
    	pubYear.set(year);
    }
    //----------------------------------------------------------------------------
    public String getStatus(){
    	return status.get();
    	
    }
    //----------------------------------------------------------------------------
    public void setStatus(String aStatus){
    	status.set(aStatus);
    }
    
}
