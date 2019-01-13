import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

/**
* Class to provide connectivity and CRUD methods to allow access
* and manipulation of books.sqlite database. There are other methods specifically
* needed for the Jetty WebServer application hosted at localhost:3005/ after
* Jetty is activated by the User.
*
* @author  Kashif Ahmed - 18061036
* @version 1.0
* @since   November 2018 
*/
public class BookDAO {
		
		//Global variables relevant for database connectivity and interaction.
		static PreparedStatement statement = null;
		static ResultSet queryResult = null;
		
		/**
		* Method allows connection to be established
		* to the books.sqlite database.
		* 
		* @return Connection object providing connectivity to books.sqlite.
		* @exception ClassNotFoundException incase JDBC driver is not found.
		* @exception SQLException for any SQL/database interaction related errors.
		*/
		public static Connection getDBConnection() {	
			//Load JDBC driver and attempt database connection.
			Connection conn = null;
			try {
				Class.forName("org.sqlite.JDBC");
				String url = "jdbc:sqlite:books.sqlite";
				conn = DriverManager.getConnection(url);	
				System.out.println("\n\n\n\n\n\nConnected to Database...");
				} catch(ClassNotFoundException e){ 
					e.printStackTrace();
				} catch (SQLException e){ 
					e.printStackTrace();
				}
			return conn;
		}
	
		/**
		* Method allows capturing of all books from database
		* using 'SELECT * FROM book;' SQL query. Query result is stored as
		* an array of Book objects.
		* 
		* @return ArrayList<Book> object containing all books held in database books table.
		* @exception ClassNotFoundException incase JDBC driver is not found.
		* @exception SQLException for any SQL/database interaction related errors.
		*/
	    public static  ArrayList<Book> getAllBooks() {
			String query = "SELECT * FROM books;"; //SQL query to obtain all books.
			ArrayList<Book> bookList = new ArrayList<>();
			Connection conn = null;
			try {
				//Connect to database and get statement object.
				conn = getDBConnection();
				statement = conn.prepareStatement(query);
				
				//User update.
				System.out.println("Retrieving all Books...\nDB QUERY = \"" + query + "\"");
				
				queryResult = statement.executeQuery(); //Execute SQL query.
				
				//Iterate through query response and add all tuples into bookList ArrayList<Book>.
				while (queryResult.next()) {
					//Creating Book objects and adding to bookList.
					bookList.add(new Book(queryResult.getInt("ID"), queryResult.getString("Title"), queryResult.getString("Author"), 
							queryResult.getInt("Year"),	queryResult.getInt("Edition"), queryResult.getString("Publisher"), 
							queryResult.getString("ISBN"), queryResult.getString("Cover"), queryResult.getString("Condition"), queryResult.getInt("Price"),
							queryResult.getString("Notes")));
				}	
			} catch (SQLException e){
				e.printStackTrace();
			} finally {
				if (queryResult != null) { try {
					queryResult.close();
				} catch (SQLException e) {
					e.printStackTrace();
				} }
				if (statement != null) { try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				} }
				if (conn != null) { try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				} }
				 }
			return bookList;
	    }
	    
	    /**
		* Method allows getting a single book from database dependent un BookID
		* provided as a parameter ID.
		* 
		* @param ID to provide ID of book about which information is required.
		* @return Book object with BookID same as ID chosen by user.
		* @exception SQLException for any SQL/database interaction related errors.
		*/
	    public static Book getBook(int ID) {
			String query = "SELECT * FROM books WHERE ID = ? ;"; //SQL query to obtain books with ID passed in as parameter to this method.
			Book book = null;
			Connection conn = null;
			try {
				//Connect to database and get statement object.
				conn = getDBConnection();
				statement = conn.prepareStatement(query);
				statement.setString(1, Integer.toString(ID));
								
				//User update.
				System.out.println("Retrieving Book Details where BookID is " + ID);
				
				queryResult = statement.executeQuery(); //Execute SQL query.
				
				book = new Book(queryResult.getInt("ID"), queryResult.getString("Title"), queryResult.getString("Author"), 
						queryResult.getInt("Year"),	queryResult.getInt("Edition"), queryResult.getString("Publisher"), 
						queryResult.getString("ISBN"), queryResult.getString("Cover"), queryResult.getString("Condition"), queryResult.getInt("Price"),
						queryResult.getString("Notes"));
			} catch (SQLException e){
				System.out.println("\n\n!!!!!!!!WARNING!!!!!!!!ERROR!!!!!!!!WARNING!!!!!!!!ERROR"
	    				+ "\nBook with ID " + ID + " does not exist. Please try again!\n"); 
			} finally {
				try {
				if (queryResult != null)
					queryResult.close();
				if (statement != null) 
					statement.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			}
			return book;
	    }

	    /**
		* Method deletes book in database based on ID passed by user.
		* 
		* @param ID to provide ID of book to be deleted.
		* @return Boolean to check if deletion was successful (true) or unsuccessful (false).
		* @exception SQLException for any SQL/database interaction related errors.
		*/
	    public static Boolean deleteBook(int ID) {
			String query = "DELETE FROM books WHERE ID = ? ;"; //SQL query to delete book that has ID passed into the method as parameter.
			Connection conn = null;
			try {
				//Connect to database and get statement object.
				conn = getDBConnection();
				statement = conn.prepareStatement(query);
				statement.setString(1, Integer.toString(ID));
				
				//User update.
				System.out.println("Deleting Book with ID " + ID);
				statement.executeUpdate(); //Execute SQL query.
				
				System.out.println("Book Deleted Successfully...");
				return true;
			} catch (SQLException e){
				e.printStackTrace();
				return false;
			} finally {
				try {
				if (statement != null) 
					statement.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			}
	    }

	    /**
		* Method allows user to insert a new book into the database. User
		* must first createBook().
		* 
		* @param userInputs an ArrayList<String> containing all details (title, author etc..) about the new book being created.
		* @return Boolean to check if insertion was successful (true) or unsuccessful (false).
		* @exception SQLException for any SQL/database interaction related errors.
		*/
	    public static boolean insertBook(ArrayList<String> userInputs) {
	    	//SQL query that inserts book with parameters passed in through method parameter userInputs.
	    	String query = "INSERT INTO books (Title, Author, Year, Edition, Publisher, ISBN, Cover, Condition, Price, Notes, Sales) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
	    	Connection conn = null;
	    	//Prepare statement query and execute. 
			try {
				conn = getDBConnection();
				statement = conn.prepareStatement(query);
				statement.setString(1, userInputs.get(0));
				statement.setString(2, userInputs.get(1));
				statement.setInt(3, Integer.parseInt(userInputs.get(2)));
				statement.setInt(4, Integer.parseInt(userInputs.get(3)));
				statement.setString(5, userInputs.get(4));
				statement.setString(6, userInputs.get(5));
				statement.setString(7, userInputs.get(6));
				statement.setString(8, userInputs.get(7));
				statement.setInt(9, Integer.parseInt(userInputs.get(8)));
				statement.setString(10, userInputs.get(9));
				statement.setInt(11, Integer.parseInt(userInputs.get(10)));
				System.out.println("Inserting book...");
				statement.executeUpdate(); //Execute SQL Query.
				System.out.println("Book inserted...");
				return true;
			} catch (SQLException e){
				e.printStackTrace();
				return false;
			} finally {
				try {
				if (statement != null) 
					statement.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			}
		}
	    
	    /**
		* Method creates a book that can be inserted using the insertBook() method. 
		* User is asked to input all the details of the new book. Inputs are stored in ArrayList<String>.
		* 
		* @return ArrayList<String> containing all information (Title, Author etc..) about the new book created.
		* @exception NumberFormatException for cases when letters are put instead of numbers for Year, Edition, Price or Sales.
		* @exception Exception for any errors.
		*/
	    @SuppressWarnings("resource")
		public static ArrayList<String> createBook() {
	    	//Keep looping until user inputs a valid book to insert.
	    	while(true){
		    	try {
		    		System.out.println("\n\n\n|||||||||||||||||||||||||||||||||||||||  ENTER BOOK DETAILS TO INSERT BOOK  |||||||||||||||||||||||||||||||||||||||");
		    		//Get user input and store in ArrayList<String> userInputs.
		    		//Scanner used to get user input, stored in userInputs ArrayList<String>.
			    	Scanner in = new Scanner(System.in);
			    	ArrayList<String> userInputs = new ArrayList<String>();
	    			System.out.println("\nPlease enter book title: ");
			    	userInputs.add(in.nextLine());
			    	System.out.println("Please enter book author: ");
			    	userInputs.add(in.nextLine());
			    	System.out.println("Please enter book year (Must be a number ONLY!): ");
			    	userInputs.add(in.nextLine());
			    	System.out.println("Please enter book edition (Must be a number ONLY!): ");
			    	userInputs.add(in.nextLine());
			    	System.out.println("Please enter book publisher: ");
			    	userInputs.add(in.nextLine());
			    	System.out.println("Please enter book isbn (MUST BE VALID!): ");
			    	
			    	ISBNloop: while (true) {
			    		System.out.println("You must enter a valid ISBN or you will not be able to continue.");
			    		String tmpStr = in.nextLine();
			    		if (ISBNValidatorServlet.validateISBN10Dig(tmpStr)) {
			    			userInputs.add(tmpStr);
			    			break ISBNloop; }
			    		if (ISBNValidatorServlet.validateISBN13Dig(tmpStr)) {
			    			userInputs.add(tmpStr);
			    			break ISBNloop; }
			    	}
			    	
			    	System.out.println("Please enter book cover: ");
			    	userInputs.add(in.nextLine());
			    	System.out.println("Please enter book condition: ");
			    	userInputs.add(in.nextLine());
			    	System.out.println("Please enter book price (Must be a number ONLY!)");
			    	userInputs.add(in.nextLine());
			    	System.out.println("Please enter any notes: ");
			    	userInputs.add(in.nextLine());
			    	System.out.println("Please enter Sales figure (Must be a number ONLY!): ");
			    	userInputs.add(in.nextLine());
			    	//Using integer conversion to check if Year, Edition, Price and Sales are numeric values. Else catch exception NumberFormatException.
			    	Integer.parseInt(userInputs.get(2));
			    	Integer.parseInt(userInputs.get(3));
			    	Integer.parseInt(userInputs.get(8));
			    	Integer.parseInt(userInputs.get(10));
			    	return userInputs;		    		
		    	} catch (NumberFormatException e) {
		    		System.out.println("\n\n!!!!!!!!WARNING!!!!!!!!ERROR!!!!!!!!WARNING!!!!!!!!ERROR!!!!!!!!WARNING!!!!!!!!ERROR!!!!!!!!WARNING!!!!!!!!ERROR"
		    				+ "\nSomething went wrong. Please try again. Check format of your input please i.e do not put letters where numbers are expected..."); 
		    	} catch (Exception e) {
		    		System.out.println("\n\nSomething went wrong. Please try again. Check format of your input please i.e do not put letters where numbers are expected..."); 
		    	}
	    	}
	    }
	    
	    /**
		* Method updates a book in database based on userInputs. Update depends on whether
		* user is using console or web. userInputs.get(0) contains "console" or "web" to indicate what
		* the user is using.
		* 
		* @param userInputs contains user inputs that are used to update a book held in the database.
		* @return Boolean to check if book update successful (true), or update was unsuccessful (false).
		* @exception SQLException for any SQL/database interaction related errors.
		* @exception IndexOutOfBoundsException incase userInputs element is accessed that does not exist.
		*/
	    public static boolean updateBook(ArrayList<String> userInputs) {
	    	System.out.println("Updating book...");
	    	Connection conn = null;
			try {
				//CONSOLE MODE: Creating SQL query to insert new book into database and running the query.
				if(userInputs.get(0).equals("console")){
					String query = "UPDATE books SET " + userInputs.get(2) + " = ? WHERE ID = ? ;";
					//Connect to database, prepare statement and execute.
					conn = getDBConnection();
					statement = conn.prepareStatement(query);
					statement.setString(1, userInputs.get(3));
					statement.setString(2, userInputs.get(1));
					statement.executeUpdate(); //Execute SQL query.
					System.out.println("Book updated...");
					return true;
				
				//WEB MODE: Creating SQL query to insert new book into database and running the query.
				} else if(userInputs.get(0).equals("web")){
					String query = "UPDATE books SET Title = ?, Author = ?, Year = ?, Edition = ?, Publisher = ?, ISBN = ?, Cover = ?, Condition = ?, Price = ?, Notes = ? "
									+ "WHERE ID = ? ;";
					//Connect to database, prepare statement and execute SQL query.
					conn = getDBConnection();
					statement = conn.prepareStatement(query);
					statement.setString(1, userInputs.get(1));
					statement.setString(2, userInputs.get(2));
					statement.setInt(3, Integer.parseInt(userInputs.get(3)));
					statement.setInt(4, Integer.parseInt(userInputs.get(4)));
					statement.setString(5, userInputs.get(5));
					statement.setString(6, userInputs.get(6));
					statement.setString(7, userInputs.get(7));
					statement.setString(8, userInputs.get(8));
					statement.setInt(9, Integer.parseInt(userInputs.get(9)));
					statement.setString(10, userInputs.get(10));
					statement.setString(11, userInputs.get(11));
					statement.executeUpdate(); //Execute SQL query.
					return true;
					} 				
			} catch (SQLException e){
				e.printStackTrace();
			} catch (IndexOutOfBoundsException e) {
	    		System.out.println("\n\nSomething went wrong. Please try again. Check format of your input please i.e do not put letters where numbers are expected");
	    	} finally {
				try {
				if (statement != null) 
					statement.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			}
			return false;
	    }
	    
	    /**
		* Method creates SQL query used to update a particular book when console
		* is used to update a book in the books.sqlite database.
		* 
		* @return ArrayList<String> object containing BookID, Column name and Update entered by user.
		* @exception NumberFormatException incase letter(s) is entered by user where number is expected.
		*/
	    @SuppressWarnings("unused")
		public static ArrayList<String> updateBookQueryGenerator() {
	    	//Scanner to get user input and store in ArrayList<String> userInputs.
	    	@SuppressWarnings("resource") // Closing Scanner closes inputstream that can not be opened again.
			Scanner input = new Scanner(System.in);
	    	ArrayList<String> userInputs = new ArrayList<String>();
	    	userInputs.add("console");
	    	int columnChoice = 0;
	    	
	    	System.out.println("\n\nWARNING! no semi-colons or single quotation marks are allowed as acceptable inputs to prevent SQL Injection attacks. NOT ALLOWED:  ; or '");
	    	
	    	//Get user to input ID, Column and Update so book can be updated with new information entered.
	    	try {
	    		while (true){
			    	System.out.println("\nEnter ID of book to be updated (MUST BE A NUMBER): ");
			    	String tmpStringA = input.nextLine();
						int tmpInt = Integer.parseInt(tmpStringA);
			    		userInputs.add(tmpStringA);
				    	break;	
	    		}
	    		//Get from user column that needs changing.
	    		controlledLoop: while (true){
			    	System.out.println("\nWhich column would you like to change? Choose associated digit. !!WARNING, INVALID ENTRIES WILL BE IGNORED DUE TO SECURITY REASONS: \n");
			    	System.out.println("Title - Enter 1 \n"
			    					 + "Author - Enter 2 \n"
			    		 			 + "Year - Enter 3 \n"
			    					 + "Edition - Enter 4 \n"
			    					 + "Publisher - Enter 5 \n"
			    					 + "ISBN - Enter 6 \n"
			    					 + "Cover - Enter 7 \n"
			    					 + "Condition - Enter 8 \n"
			    					 + "Price - Enter 9 \n"
			    					 + "Notes - Enter 10 \n");
			    	String tmpStringB = input.nextLine();
			    	if(tmpStringB != null){
		    			columnChoice = Integer.parseInt(tmpStringB);
			    		if(columnChoice < 1 || columnChoice > 10){
			    			System.out.println("\nYou entered " + columnChoice + ". This is outside the acceptable range of 1 - 10. Please try again!");
			    		} else {	
			    			switch(columnChoice) {
			    				case 1: userInputs.add("Title");		break;
			    				case 2: userInputs.add("Author"); 		break;
			    				case 3: userInputs.add("Year"); 		break;
			    				case 4: userInputs.add("Edition"); 		break;
			    				case 5: userInputs.add("Publisher"); 	break;
			    				case 6: userInputs.add("ISBN"); 		break;
			    				case 7: userInputs.add("Cover"); 		break;
			    				case 8: userInputs.add("Condition"); 	break;
			    				case 9: userInputs.add("Price"); 		break;
			    				case 10: userInputs.add("Notes"); 		break;
			    			}
			    		}
			    		break controlledLoop;
			    	}
	    		}
	    		//Get from user input the updated information.
	    		while (true){
			    	System.out.println("\nEnter Update (If you chose 3, 4 or 9, your update MUST be a number as they are numeric fields): ");
			    	if(columnChoice == 3 || columnChoice == 4 || columnChoice == 9){
		    			int tmpInt = Integer.parseInt(input.nextLine());// POTENTIALLY CAN BE REMOVED AND ADDED DIRECTLY TO FOLLOWING INPUT. NEEDS CHECKING THOUGH TO ENSURE FUNCTIONALITY.
			    		userInputs.add(Integer.toString(tmpInt));
			    		break;
			    	} else if(columnChoice == 6) {
			    		//ISBN VALIDITY CHECKS.
				    	ISBNloop: while (true) {
				    		System.out.println("You must enter a valid ISBN or you will not be able to continue.");
				    		String tmpStr = input.nextLine();
				    		if (ISBNValidatorServlet.validateISBN10Dig(tmpStr)) {
				    			userInputs.add(tmpStr);
				    			break ISBNloop; }
				    		if (ISBNValidatorServlet.validateISBN13Dig(tmpStr)) {
				    			userInputs.add(tmpStr);
				    			break ISBNloop; }
				    	}
			    	break;
			    	} else {
			    		String tmpStr = input.nextLine();
			    		if(tmpStr.contains("'") || tmpStr.contains(";")){
			    			System.out.println("No semi-colons ; or single quotation marks ' allowed to prevent SQL Injection attacks. Retry please...");
			    		} else{		
			    			userInputs.add(tmpStr);
			    			break;
			    		}
			    	}
	    		}
	    	} catch (NumberFormatException e) {
	    		System.out.println("\n\nSomething went wrong. Please try again. Check format of your input please i.e do not put letters where numbers are expected...");
	    	} 
	    	return userInputs;
	    }    
	    
	    /**
		* Method searches for a book based on either Title, Author or Price range.
	    //Get specific books from database based on search criteria.
		* 
		* @param title to search database based on book title.
		* @param author to search database based on book author.
		* @param minPrice to search based on minimum price range.
		* @param maxPrice to search based on maximum price range.
		* @return ArrayList<Book> object containing books matching search criteria.
		* @exception SQLException for any SQL/database interaction related errors.
		*/
		public static ArrayList<Book> searchDatabase(String title, String author, int minPrice, int maxPrice) {	    	
	    	//All searched books stored in ArrayList<Book> searchedBooks.
	    	ArrayList<Book> searchedBooks = new ArrayList<>();
	    	Connection conn = null;
			try {
				//SQL queries for PreparedStatements.
				String queryTitle = "SELECT * FROM books WHERE Title LIKE ? ;";
				String queryAuthor = "SELECT * FROM books WHERE Author LIKE ? ;";
				String queryPrice = "SELECT * FROM books WHERE Price BETWEEN ? AND ? ;";
				
				//Connect to database.
				conn = getDBConnection();
				
				//Finalise query depending on search criteria.
				if(title != "") {
					PreparedStatement titleSearch = conn.prepareStatement(queryTitle);
					titleSearch.setString(1, "%" + title + "%");
					queryResult = titleSearch.executeQuery();
				};
				if(author != "") {
					PreparedStatement authorSearch = conn.prepareStatement(queryAuthor);
					authorSearch.setString(1, "%" + author + "%");
					queryResult = authorSearch.executeQuery();
				};
				if(title == "" && author == "") {
					PreparedStatement querySearch = conn.prepareStatement(queryPrice);
					querySearch.setInt(1, minPrice);
					querySearch.setInt(2, maxPrice);
					queryResult = querySearch.executeQuery();
				}
				
				//Iterate through query response and add all books into searchedBooks ArrayList<Book>.
				while (queryResult.next()) {
					searchedBooks.add(new Book(queryResult.getInt("ID"), queryResult.getString("Title"), queryResult.getString("Author"), 
							queryResult.getInt("Year"),	queryResult.getInt("Edition"), queryResult.getString("Publisher"), 
							queryResult.getString("ISBN"), queryResult.getString("Cover"), queryResult.getString("Condition"), queryResult.getInt("Price"),
							queryResult.getString("Notes")));
				}	
				
			} catch (SQLException e){
				e.printStackTrace();
			} finally {
				try {
				if (queryResult != null)
					queryResult.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			}
			return searchedBooks;
	    }
	    
		/**
		* Method reads and returns all books and associated sales records
		* from database.
		*
		* @return ArrayList<String> object containing books and sales records.
		* @exception SQLException for any SQL/database interaction related errors.
		*/
		public static  ArrayList<String> getAllSalesRecord() {
			String query = "SELECT * FROM books;"; //SQL query to obtain all books.
			ArrayList<String> salesRecord = new ArrayList<>();
			Connection conn = null;
			try {
				//Connect to database and get statement object.
				conn = getDBConnection();
				statement = conn.prepareStatement(query);
				
				//Execute SQL query stored in query String.
				queryResult = statement.executeQuery(); //Execute SQL query and record response.
				
				//Iterate through query response and add all table rows into bookList.
				while (queryResult.next()) {
						salesRecord.add(Integer.toString(queryResult.getInt("ID")));
						salesRecord.add(queryResult.getString("Title"));
						salesRecord.add(queryResult.getString("Author"));
						salesRecord.add(Integer.toString(queryResult.getInt("Sales")));
 				}					
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
				if (queryResult != null)
					queryResult.close();
				if (statement != null) 
					statement.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			}
			return salesRecord;
		}
		
		/**
		* Method gets specific book sales record to allow user to change/update it.
		*
		* @param ID to obtain sales record for book with ID chosen by user.
		* @return ArrayList<String> object containing specific book and sales record.
		* @exception SQLException for any SQL/database interaction related errors.
		*/
		public static  ArrayList<String> getSpecificSalesRecord(int ID) {
			String query = "SELECT * FROM books WHERE ID = ? ;"; //SQL query to obtain book information dependent on ID passed in as parameter to method.
			ArrayList<String> salesRecord = new ArrayList<>();
			Connection conn = null;
			try {
				//Connect to database and get statement object.
				conn = getDBConnection();
				statement = conn.prepareStatement(query);
				statement.setInt(1, ID);
				
				//Execute SQL query stored in query String.
				queryResult = statement.executeQuery(); //Execute SQL query and record response.
				
				//Iterate through query response and add all table rows into bookList ArrayList<Book>.
				while (queryResult.next()) { 
					salesRecord.add(Integer.toString(queryResult.getInt("ID")));
					salesRecord.add(queryResult.getString("Title"));
					salesRecord.add(Integer.toString(queryResult.getInt("Sales")));
 				}					
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
				if (queryResult != null)
					queryResult.close();
				if (statement != null) 
					statement.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			}
			return salesRecord;
		}
				
		/**
		* Method updates specific book sales record to allow user to change/update it.
		*
		* @param userInputs contains BookID and updated sales figure to update specific book.
		* @return Boolean true if book update successful.
		* @exception SQLException for any SQL/database interaction related errors.
		*/
	    public static boolean updateSalesRecord(ArrayList<String> userInputs) {	
	    	Connection conn = null;
			try {
			//Creating SQL query to update sales record based on userInputs passed in as parameter.
			String query = "UPDATE books SET Sales = ? WHERE ID = ? ;";
			//Connect to database, prepare statement and execute query.
			conn = getDBConnection();
			statement = conn.prepareStatement(query);
			statement.setString(1, userInputs.get(1));
			statement.setString(2, userInputs.get(0));
			statement.executeUpdate(); //Execute SQL query.
			
			return true;
			} catch (SQLException e){
				e.printStackTrace();
				return false;
			} 
	    }
	    
	    /**
		* Method to check username and password against users table of books.sqlite database.
		* Password hashing used for security. MD5 algorithm implemented.
		* 
		* @param uname Username entered by user.
		* @param pwd Password entered by user.
		* @return Boolean If username and password correct (true), otherwise return (false).
		* @exception NumberFormatException in-case user puts letters where numbers should be.
		*/
		public static boolean checkLoginCredentials(String uname, String pwd) {
	    	String query = "SELECT * FROM users;"; //SQL query to obtain all users.	
	    	Connection conn = null;
			try {
				//Connect to database and get statement object.
				conn = BookDAO.getDBConnection();
				statement = conn.prepareStatement(query);
				//Execute SQL query stored in query String.
				queryResult = statement.executeQuery(); //Execute SQL query and record response.
				while(queryResult.next()) {
					if(uname.equals(queryResult.getString("username")) && PasswordHashing.getSecurePassword(pwd).equals(queryResult.getString("password"))){
						if(checkUserPrivilages(uname)){
							conn.close();
							return true;
						}
					};
				}
				
				
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				} finally {
					try {
					if (queryResult != null)
						queryResult.close();
					if (statement != null) 
						statement.close();
					if (conn != null)
						conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
			return false;
		}
		
		/**
		* Method to check username and password against users table of books.sqlite database.
		* Password hashing used for security. MD5 algorithm implemented.
		* 
		* @param uname Username entered by user.
		* @param pwd Password entered by user.
		* @return Boolean If username and password correct (true), otherwise return (false).
		* @exception NumberFormatException in-case user puts letters where numbers should be.
		*/
		public static boolean checkUserPrivilages(String uname) {
	    	String query = "SELECT * FROM users;"; //SQL query to obtain all users.		
	    	Connection conn = null;
			try {
				//Connect to database and get statement object.
				conn = BookDAO.getDBConnection();
				statement = conn.prepareStatement(query);
				//Execute SQL query stored in query String.
				queryResult = statement.executeQuery(); //Execute SQL query and record response.
				while(queryResult.next()) {
					if(uname.equals(queryResult.getString("username")) && queryResult.getInt("user_type") == 1){  //This code checks if user_type is admin i.e user_type == 1.
						conn.close();
						return true;
					};
				}
				
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				} finally {
					try {
					if (queryResult != null)
						queryResult.close();
					if (statement != null) 
						statement.close();
					if (conn != null)
						conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			return false;
		}
}

