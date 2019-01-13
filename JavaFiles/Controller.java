import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
* Class that contains the main(String[] args) method. Displays user menu infinitely until Jetty server is started.
* Contains basic number based console menu allowing user to run CRUD operations on books.sqlite database.
* It also allows the user to start Jetty server.
*
* @author  Kashif Ahmed - 18061036
* @version 1.0
* @since   November 2018 
*/
public class Controller {
	
	public static String filepath = "";
	//Ask user for input until correct choice from available allowed options is chosen.
	static Scanner in = new Scanner(System.in);

	/**
	* The main(String[] args) method. Start point of program. Displays the menu
	* and loops infinitely until user chooses to close the program or user starts
	* Jetty server.
	* 
	* @exception NumberFormatException In-case user enters letters where numbers are expected.
	* @exception InterruptedException In-case issue with Thread methods.
	* @exception NullPointerException If a variable in use ends up null.
	* @exception NoSuchElementException In-case InputStream is closed.
	* @exception Exception To catch unforeseen exceptions.
	*/
	public static void main(String[] args) {
		//Menu within an infinite while loop that can only be exited when user enters 8.
		loop: while(true) {
			menu();
			int choice = 0;

			try {
				choice = Integer.parseInt(in.nextLine());
				
				switch (choice) {
					   case 1: 
						   ArrayList<Book> allBooks = BookDAO.getAllBooks();
						   for (Book book : allBooks){
							   System.out.println(book.toString());
						   }
						   System.out.println("\n\n------------------------------------------------------  RELOADING MENU IN 10 SECONDS  -----------------------------------------------------\n\n\n\n\n\n\n");
						   Thread.sleep(10000);
						   break;
					   
					   case 2: 
						   System.out.println("\nEnter Book ID to get details of specific book: ");
						   int tmpInt = Integer.parseInt(in.nextLine());
						   System.out.println(BookDAO.getBook(tmpInt).toString());
						   System.out.println("\n\n------------------------------------------------------  RELOADING MENU IN 10 SECONDS  -----------------------------------------------------\n\n\n\n\n\n\n");
						   Thread.sleep(10000);
						   break;
						   
					   case 3: 
						   BookDAO.insertBook(BookDAO.createBook());
						   Thread.sleep(5000);
						   break;
						   
					   case 4: 
						   BookDAO.updateBook(BookDAO.updateBookQueryGenerator());
						   Thread.sleep(2000);
						   break;
						   
					   case 5: 
						   System.out.println("\nEnter Book ID to be deleted: ");
						   int tempInt = Integer.parseInt(in.nextLine());
						   BookDAO.deleteBook(tempInt);
						   break;
						   
					   case 6: 
						   WebServerJetty.startServer();
					   case 7: 
						   break;
						   
					   case 8: 
						   System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\nPROGRAM CLOSED... GOODBYE!");
						   break loop;
					   default: System.out.println("Invalid choice. Please choose from the allowed options (1 - 5) or 6 to exit the program...");
				  }					
			} catch (NumberFormatException e) { System.out.println("\n\nONLY NUMBERS ALLOWED AS INPUT!");
			} catch (InterruptedException e1) { e1.printStackTrace();
			} catch (NullPointerException e) { System.out.println("\n\n\n\n");
			} catch (NoSuchElementException e) { choice = Integer.parseInt(in.nextLine()); //Catch and let program carry on.
			} catch (Exception e) { e.printStackTrace(); 
			}
		} 
	}
	
	/**
	* Prints user menu for RARE BOOK COLLECTION CONSOLE + JETTY WEB APPLICATION program.
	* 
	*/
	static void menu() {
		System.out.flush(); 
		System.out.println("\n\n----------------------------------------------------------------------------------"
				+ "\n-------------RARE BOOK COLLECTION CONSOLE + JETTY WEB APPLICATION-----------------"
				+ "\n----------------------------------------------------------------------------------\n\n"
				+ "Book Inventory\n"
				+ "Choose from these options\n"
				+ "----------------------------------------------------------------------------------\n\n"
				+ "1 - Retrieve all books\n"
				+ "2 - Search for book\n"
				+ "3 - Insert new book into database\n"
				+ "4 - Update existing book details\n"
				+ "5 - Delete book from database\n"
				+ "6 - Start Jetty server\n"
				+ "7 - Reprint Menu\n"
				+ "8 - Exit\n\n"
				+ "ENTER CHOICE> ");
	}
}