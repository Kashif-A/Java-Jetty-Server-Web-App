/**
* Class  with variables matching the books.sqlite database books table.
* This class is extensively used in this program to represent 
* rows in the books table of books.sqlite database.
*
* @author  Kashif Ahmed - 18061036
* @version 1.0
* @since   November 2018 
*/
public class Book {
	
	//Private variables. Matches the column names of books table in books.sqlite database.
	private int bookID;
	private String title;
	private String author;
	private int year;
	private int edition;
	private String publisher;
	private String isbn;
	private String cover;
	private String condition;
	private int price;
	private String notes;
	
	//Public Getters & Setters methods for all the private variables.
	/**
	* Getter method for BookID private variable.
	* 
	*/
	public int getBookID() {
		return bookID;
	}
	
	/**
	* Setter method for BookID private variable.
	* 
	*/
	public void setBookID(int bookID) {
		this.bookID = bookID;
	}
	
	/**
	* Getter method for Title private variable.
	* 
	*/
	public String getTitle() {
		return title;
	}
	
	/**
	* Setter method for Title private variable.
	* 
	*/
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	* Getter method for Author private variable.
	* 
	*/
	public String getAuthor() {
		return author;
	}
	
	/**
	* Setter method for Author private variable.
	* 
	*/
	public void setAuthor(String author) {
		this.author = author;
	}
	
	/**
	* Getter method for Year private variable.
	* 
	*/
	public int getYear() {
		return year;
	}
	
	/**
	* Setter method for Year private variable.
	* 
	*/
	public void setYear(int year) {
		this.year = year;
	}
	
	/**
	* Getter method for Edition private variable.
	* 
	*/
	public int getEdition() {
		return edition;
	}
	
	/**
	* Setter method for Edition private variable.
	* 
	*/
	public void setEdition(int edition) {
		this.edition = edition;
	}
	
	/**
	* Getter method for Publisher private variable.
	* 
	*/
	public String getPublisher() {
		return publisher;
	}
	
	/**
	* Setter method for Publisher private variable.
	* 
	*/
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	
	/**
	* Getter method for Isbn private variable.
	* 
	*/
	public String getIsbn() {
		return isbn;
	}
	
	/**
	* Setter method for Isbn private variable.
	* 
	*/
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	
	/**
	* Getter method for Cover private variable.
	* 
	*/
	public String getCover() {
		return cover;
	}
	
	/**
	* Setter method for Cover private variable.
	* 
	*/
	public void setCover(String cover) {
		this.cover = cover;
	}
	
	/**
	* Getter method for Condition private variable.
	* 
	*/
	public String getCondition() {
		return condition;
	}
	
	/**
	* Setter method for Condition private variable.
	* 
	*/
	public void setCondition(String condition) {
		this.condition = condition;
	}
	
	/**
	* Getter method for Price private variable.
	* 
	*/
	public int getPrice() {
		return price;
	}
	
	/**
	* Setter method for Price private variable.
	* 
	*/
	public void setPrice(int price) {
		this.price = price;
	}
	
	/**
	* Getter method for Notes private variable.
	* 
	*/
	public String getNotes() {
		return notes;
	}
	
	/**
	* Setter method for Notes private variable.
	* 
	*/
	public void setNotes(String notes) {
		this.notes = notes;
	}	
	
	//Constructor.
	public Book(int bookID, String title, String author, int year, int edition, String publisher, 
							String isbn, String cover, String condition, int price, String notes) {
		
		this.bookID = bookID;
		this.title = title;
		this.author = author;
		this.year = year;
		this.edition = edition;
		this.publisher = publisher;
		this.isbn = isbn;
		this.cover = cover;
		this.condition = condition;
		this.price = price;
		this.notes = notes;
	}
	
	/**
	* toString() method allows information about a book
	* to be displayed to the console window.
	*/
	public String toString() {
		return "----------------------------------------------------------------\n   BOOK ID:  " + this.getBookID()
		+ "\n     TITLE:  " + this.getTitle()
		+ "\n    AUTHOR:  " + this.getAuthor()
		+ "\n      YEAR:  " + this.getYear()
		+ "\n   EDITION:  " + this.getEdition()
		+ "\n PUBLISHER:  " + this.getPublisher()
		+ "\n      ISBN:  " + this.getIsbn()
		+ "\n     COVER:  " + this.getCover()
		+ "\n CONDITION:  " + this.getCondition()
		+ "\n     PRICE:  " + this.getPrice()
		+ "\n     NOTES:  " + this.getNotes()
		+ "\n----------------------------------------------------------------";
	}
}