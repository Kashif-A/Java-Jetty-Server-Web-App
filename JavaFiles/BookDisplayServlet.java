import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
* Servlet to display books stored in the database and process search requests by user.
*
* @author  Kashif Ahmed - 18061036
* @version 1.0
* @since   November 2018 
*/
public class BookDisplayServlet extends HttpServlet {
	/**
	 * serialVersionUID to prevent InvalidClassExceptions during deserialization.
	 */
	private static final long serialVersionUID = -4514973525022166356L;
	
	//PrintWriter to send HTTP in response.
	PrintWriter out = null;
	
	/**
	* Servlet method to respond to HTML GET requests. HttpSession objects are used for security purposes.
	* Displays initial /books page. If user logged in when accessing servlet, administrative access is 
	* given with new options given to users to edit information in database.
	* 
	* Servlet lists all books held in database in a HTML table.
	*
	* @param request to deal with HTML GET requests.
	* @param response to respond to requests made to server.
	* @exception SQLException for any SQL/database interaction related errors.
	*/
	@Override
	//Boilerplate Servlet code. Content HTML and OK response.
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		
		//Session to track administrative logins.
    	HttpSession newSesh = request.getSession();//Create session object.
    	    	
    	//Get all the books in database.
	    ArrayList<Book> bookList = new ArrayList<>();
		bookList = BookDAO.getAllBooks();
    	
    	//Session checks. If no session, show regular page without administrative privileges.
    	if(newSesh.getAttribute("user") == null){
    		try {
    			//PrintWriter to send HTTP responses.
    			out = response.getWriter();
   		    	//Send pre-table header HTML first.
   		    	out.write(new String(Files.readAllBytes(Paths.get(Controller.filepath + "Critical Files/TextFiles/HeaderContent.txt"))));
   		    	
   		    	//Loop through the database tuples and print book IDs into table.
   				for(Book b : bookList) {
   					out.write("<tr class=\"row100 body\"><td class=\"cell100 column1\"><b><em><i>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp" + b.getBookID() + "</td></tr>");}  //END of for loop.
    		    
   				//Send second header HTML.
   		    	out.write(new String(Files.readAllBytes(Paths.get(Controller.filepath + "Critical Files/TextFiles/SecondTableHeader.txt"))));
    					for(Book b : bookList) {
   						out.write(  "<tr class=\"row100 body\">\n" + 
		   							"<td class=\"column2\">" + b.getTitle() + "</td>\n" + 
		   							"<td class=\"column3\">" + b.getAuthor() + "</td>\n" + 
		   							"<td class=\"column3\">" + b.getYear() + "</td>\n" + 
		   							"<td class=\"column3\">" + b.getEdition() + "</td>\n" + 
		   							"<td class=\"column3\">" + b.getPublisher() + "</td>\n" + 
		   							"<td class=\"column3\">" + b.getIsbn() + "</td>\n" + 
		   							"<td class=\"column3\">" + b.getCover() + "</td>\n" +
		   							"<td class=\"column3\">" + b.getCondition() + "</td>\n" +
		   							"<td class=\"column3\">" + b.getPrice() + "</td>\n" +
		   							"<td class=\"column4\">" + b.getNotes() + "</td></tr>");}  //END of for loop.
   					
   					//Send closing HTML. Closing </div></body></html> tags etc.
   			    	out.write(new String(Files.readAllBytes(Paths.get(Controller.filepath + "Critical Files/TextFiles/FooterContent.txt"))));
   			    	
   				} catch (IOException e) {
   	    			e.printStackTrace();
   	    		} finally {
   					if (out != null){out.close();}
   				} 
    		}
    	//If administrative session, access administrative features.
    	else if(newSesh.getAttribute("user").equals("admin")){    	

			try {
				out = response.getWriter();
	    		//Prevent Cache so secure area is completely inaccessible after logout.
	    	    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
	    	    response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
	    	    response.setDateHeader("Expires", 0); // Proxies.

	    	  //Send pre-table header HTML first.
	    	  out.write(new String(Files.readAllBytes(Paths.get(Controller.filepath + "Critical Files/TextFiles/HeaderContentAdmin.txt"))));
			    	
	    	  //Loop through the database tuples and print into new row of the HTML table per data item.
	    	  for(Book b : bookList) {
				out.write("<tr><td><a href=\"http://localhost:3005/deletebook?id=" + b.getBookID() + "\">"
						+ "<button><img src=\"https://icon2.kisspng.com/20180626/aqu/kisspng-delete-key-logo-clip-art-5b3277a4485070.7900223415300340842962.jpg\"></button></a>"
						+ "<a href=\"http://localhost:3005/editbook?id=" + b.getBookID() + "\">"
						+ "<button>&nbsp&nbsp&nbsp<img src=\"https://www.freeiconspng.com/uploads/edit-pen-write-icon--2.png\"></button></a><b><em><i>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp" + b.getBookID() + "</td></tr>"); } // END of for loop.
		    	
		    	//Send second header information
		    	out.write(new String(Files.readAllBytes(Paths.get(Controller.filepath + "Critical Files/TextFiles/SecondTableHeader.txt"))));
	
				for(Book b : bookList) { 									
				out.write(  "<tr class=\"row100 body\">\n" + 
	   						"<td class=\"column2\">" + b.getTitle() + "</td>\n" + 
	   						"<td class=\"column3\">" + b.getAuthor() + "</td>\n" + 
	   						"<td class=\"column3\">" + b.getYear() + "</td>\n" + 
	   						"<td class=\"column3\">" + b.getEdition() + "</td>\n" + 
	   						"<td class=\"column3\">" + b.getPublisher() + "</td>\n" + 
	   						"<td class=\"column3\">" + b.getIsbn() + "</td>\n" + 
	   						"<td class=\"column3\">" + b.getCover() + "</td>\n" +
	   						"<td class=\"column3\">" + b.getCondition() + "</td>\n" +
	   						"<td class=\"column3\">" + b.getPrice() + "</td>\n" +
	   						"<td class=\"column4\">" + b.getNotes() + "</td></tr>");}  //END of for loop.
		    		
		    	//Send closing HTML. Closing </div></body></html> tags etc
		    	out.write(new String(Files.readAllBytes(Paths.get(Controller.filepath + "Critical Files/TextFiles/FooterContent.txt"))));		    	
		    } catch (IOException e){
		    	e.printStackTrace();
		    } finally { if (out != null){out.close(); } //Close BufferedWriter object out.
		}
      } 
   }
	
	/**
	* Servlet method to respond to HTML POST requests. HttpSession objects are used for security purposes.
	* Servlet lists all books matching search criteria entered by user. Criteria is Title, Author or minPrice/maxPrice.
	*
	* @param request to deal with HTML POST requests.
	* @param response to respond to requests made to server.
	* @exception IOException in-case text files missing.
	*/
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException{
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		
    	HttpSession newSesh = request.getSession();//Create session object.
    	//Get search parameters from request.
    	String title = request.getParameter("title");
    	String author = request.getParameter("author");
    	int minPrice = Integer.parseInt(request.getParameter("minPrice"));
    	int maxPrice = Integer.parseInt(request.getParameter("maxPrice"));
    	    	
		//Search books database and store matching books in ArrayList<Book> searchedBooks.
		try {
			ArrayList<Book> searchedBooks = new ArrayList<Book>();
			searchedBooks = BookDAO.searchDatabase(title, author, minPrice, maxPrice);
			//PrintWriter to send HTTP responses.
			out = response.getWriter();
			if(newSesh.getAttribute("user") == null){
		    	//Send pre-table HTML data.
		    	out.write(new String(Files.readAllBytes(Paths.get(Controller.filepath + "Critical Files/TextFiles/HeaderContent.txt"))));
		    	//Loop through the database tuples and print into new row of the HTML table per data item.
		    	
				for(Book b : searchedBooks) {
					out.write("<tr class=\"row100 body\"><td class=\"cell100 column1\"><b><em><i>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp" + b.getBookID() + "</td></tr>");}  //END of for loop.
				} else if(newSesh.getAttribute("user").equals("admin")){
				//Send pre-table HTML data.
		    	out.write(new String(Files.readAllBytes(Paths.get(Controller.filepath + "Critical Files/TextFiles/HeaderContentAdmin.txt"))));
		    	//Loop through the database tuples and print into new row of the HTML table per data item.
		    	for(Book b : searchedBooks) {
					out.write("<tr><td><a href=\"http://localhost:3005/deletebook?id=" + b.getBookID() + "\">"
							+ "<button><img src=\"https://icon2.kisspng.com/20180626/aqu/kisspng-delete-key-logo-clip-art-5b3277a4485070.7900223415300340842962.jpg\"></button></a>"
							+ "<a href=\"http://localhost:3005/editbook?id=" + b.getBookID() + "\">"
							+ "<button>&nbsp&nbsp&nbsp<img src=\"https://www.freeiconspng.com/uploads/edit-pen-write-icon--2.png\"></button></a><b><em><i>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp" + b.getBookID() + "</td></tr>");}  //END of for loop.
			}
	    
			//Send second HTML header information
	    	out.write(new String(Files.readAllBytes(Paths.get(Controller.filepath + "Critical Files/TextFiles/SecondTableHeader.txt"))));
				for(Book b : searchedBooks) {
					out.write(  "<tr class=\"row100 body\">\n" + 
	   							"<td class=\"column2\">" + b.getTitle() + "</td>\n" + 
	   							"<td class=\"column3\">" + b.getAuthor() + "</td>\n" + 
	   							"<td class=\"column4\">" + b.getYear() + "</td>\n" + 
	   							"<td class=\"column4\">" + b.getEdition() + "</td>\n" + 
	   							"<td class=\"column3\">" + b.getPublisher() + "</td>\n" + 
	   							"<td class=\"column3\">" + b.getIsbn() + "</td>\n" + 
	   							"<td class=\"column3\">" + b.getCover() + "</td>\n" +
	   							"<td class=\"column3\">" + b.getCondition() + "</td>\n" +
	   							"<td class=\"column1\">" + b.getPrice() + "</td>\n" +
	   							"<td class=\"column4\">" + b.getNotes() + "</td></tr>");}  //END of for loop.
				
				//Send closing HTML. Closing </div></body></html> tags etc
		    	out.write(new String(Files.readAllBytes(Paths.get(Controller.filepath + "Critical Files/TextFiles/FooterContent.txt"))));
		    	
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
}