import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
* Servlet to edit a book chosen by user using Jetty server web app.
*
* @author  Kashif Ahmed - 18061036
* @version 1.0
* @since   November 2018 
*/
public class EditBookServlet extends HttpServlet {
	/**
	 * serialVersionUID to prevent InvalidClassExceptions during deserialization.
	 */
	private static final long serialVersionUID = -7956506893071164751L;
	//PrintWriter to send HTML responses when requests received.
	PrintWriter out = null;
	
	/**
	* Servlet method to respond to HTML GET requests. HttpSession objects are used for security purposes.
	* Servlet method to allow user to edit a specific book.
	*
	* @param request to deal with HTML GET requests.
	* @param response to respond to requests made to server.
	* @exception IOException in-case text file is missing.
	* @exception NumberFormatException in-case user enters letter(s) where number(s) is expected.
	*/
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException{
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		try {
			//Session object for security.
			HttpSession newSesh = request.getSession(true);
	    	//Session checks.
	    	if(newSesh.getAttribute("user") == null)
					response.sendRedirect("login");
	    	
	    	//Session checks.
	    	else if(newSesh.getAttribute("user").equals("admin")){  
	    		//Prevent Cache so secure area is completely inaccessible after logout.
	    	    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
	    	    response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
	    	    response.setDateHeader("Expires", 0); // Proxies.
	    	    //Get all information of book requested for edit and make available for editing. Using HTML <input> tags to get edits from user for SQL query construction.
			
				Book book = BookDAO.getBook(Integer.parseInt(request.getParameter("id")));
				out = response.getWriter();
				//Send HTML in response to request
		    	out.write(new String(Files.readAllBytes(Paths.get(Controller.filepath + "Critical Files/TextFiles/EditBook.txt"))));
		    	
		    	out.write("<input type=\"text\" placeholder=\"Title\" name=\"title\" value=\"" + book.getTitle() + "\"required/>"
						+ "<input type=\"text\" placeholder=\"Author\" name=\"author\" value=\"" + book.getAuthor() + "\"required/>"
						+ "<input type=\"text\" placeholder=\"Year\" name=\"year\" value=\"" + book.getYear() + "\"required/>"
						+ "<input type=\"text\" placeholder=\"Edition\" name=\"edition\" value=\"" + book.getEdition() + "\"required/>"
						+ "<input type=\"text\" placeholder=\"Publisher\" name=\"publisher\" value=\"" + book.getPublisher() + "\"/>"
						+ "<input type=\"text\" placeholder=\"ISBN\" name=\"ISBN\" value=\"" + book.getIsbn() + "\"/>"
						+ "<p id=\"error3\"><span>************************************** ISBN IS INVALID! **************************************</span></p>"
						+ "<input type=\"text\" placeholder=\"Cover\" name=\"cover\" value=\"" + book.getCover() + "\"/>"
						+ "<input type=\"text\" placeholder=\"Condition\" name=\"condition\" value=\"" + book.getCondition() + "\"/>"
						+ "<input type=\"text\" placeholder=\"Price\" name=\"price\" value=\"" + book.getPrice() + "\"required/>"
						+ "<input type=\"text\" placeholder=\"Notes\" name=\"notes\" value=\"" + book.getNotes() + "\"/>"
						+ "<input type=\"text\" name=\"id\" value=\"" + book.getBookID() 
						+ "\" style=\"visibility: hidden; padding: 0px; margin: 0px;\"/>"
						+ "<div></div>"
						+ "<button>Submit</button></form>"
						+ "<br><a href=\"/books\"><button>go back</button></a>"
						+ "<br><br><p id=\"error\">Error! Only a number can be entered in Year, Edition, Price and Sales fields.</p>"
						+ "<br><p id=\"error2\">Error! No ' or ; allowed to prevent SQL Injection attacks. Please check ALL field.</p>"
						+ "</div></div></div></div></body></html>");
				} 
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}
	
	/**
	* Servlet method to respond to HTML POST requests. HttpSession objects are used for security purposes.
	* Processes user book edit request using BookDAO CRUD method updateBook();
	*
	* @param request to deal with HTML POST requests.
	* @param response to respond to requests made to server.
	* @exception IOException in-case text files missing.
	*/
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException{
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		
		try {
			//Session object for security.
			HttpSession newSesh = request.getSession(true);
	    	//Session checks.
	    	if(newSesh.getAttribute("user") == null)
					response.sendRedirect("login");
	    	
	    	//Session checks.
	    	else if(newSesh.getAttribute("user").equals("admin")){ 
	    		//Prevent Cache so secure area is completely inaccessible after logout.
	    	    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
	    	    response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
	    	    response.setDateHeader("Expires", 0); // Proxies.
	    		ArrayList<String> userInputs = new ArrayList<String>();
	    		userInputs.add("web");
	    		
				//Add user input received through key value pairs into ArrayList.
				userInputs.add(request.getParameter("title"));
				userInputs.add(request.getParameter("author"));
				userInputs.add(request.getParameter("year"));
				userInputs.add(request.getParameter("edition"));
				userInputs.add(request.getParameter("publisher"));
				userInputs.add(request.getParameter("ISBN"));
				userInputs.add(request.getParameter("cover"));
				userInputs.add(request.getParameter("condition"));
				userInputs.add(request.getParameter("price"));
				userInputs.add(request.getParameter("notes"));
				userInputs.add(request.getParameter("id"));					
		    	
		    	//Insert book using bookDAO method.
	    	
	    		out = response.getWriter();
	    		//If book updated successfully, send Success HTML file. Else send Fail file.
				if(BookDAO.updateBook(userInputs))
					out.write(new String(Files.readAllBytes(Paths.get(Controller.filepath + "Critical Files/TextFiles/EditBookSuccess.txt"))));
				else
					out.write(new String(Files.readAllBytes(Paths.get(Controller.filepath + "Critical Files/TextFiles/EditBookFail.txt"))));
			} 
		} catch (IOException e) {
			e.printStackTrace();
		}  								
	}
}
