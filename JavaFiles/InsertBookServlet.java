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
* Servlet to insert a book chosen by user using Jetty server web app.
*
* @author  Kashif Ahmed - 18061036
* @version 1.0
* @since   November 2018 
*/
public class InsertBookServlet extends HttpServlet {
	/**
	 * serialVersionUID to prevent InvalidClassExceptions during deserialization.
	 */
	private static final long serialVersionUID = -999669599162525515L;
	//PrintWriter to send HTML responses when requests are received.
	PrintWriter out = null;
	
	/**
	* Servlet method to respond to HTML GET requests. HttpSession objects are used for security purposes.
	* Servlet allows user to insert book into books.sqlite database.
	*
	* @param request to deal with HTML GET requests.
	* @param response to respond to requests made to server.
	* @exception SQLException for any SQL/database interaction related errors.
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
				out = response.getWriter();
				//Send HTML in response to request
		    	out.write(new String(Files.readAllBytes(Paths.get(Controller.filepath + "Critical Files/TextFiles/InsertBook.txt"))));
			} 
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	/**
	* Servlet method to respond to HTML POST requests. HttpSession objects are used for security purposes.
	* Servlet processes user book insertion POST request.
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
			HttpSession newSesh = request.getSession(true);
	    	
			//Prevent Cache so secure area is completely inaccessible after logout.
		    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
		    response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
		    response.setDateHeader("Expires", 0); // Proxies.
		    
	    	//Session checks.
	    	if(newSesh.getAttribute("user") == null)
	    		response.sendRedirect("login");
	    	
	    	//Session checks.
	    	else if(newSesh.getAttribute("user").equals("admin")){ 
	    		ArrayList<String> userInputs = new ArrayList<String>();
	    		
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
				userInputs.add(request.getParameter("sales"));	
				out = response.getWriter();
				if(BookDAO.insertBook(userInputs))
					out.write(new String(Files.readAllBytes(Paths.get(Controller.filepath + "Critical Files/TextFiles/InsertBookSuccess.txt"))));
				else
					out.write(new String(Files.readAllBytes(Paths.get(Controller.filepath + "Critical Files/TextFiles/InsertBookFail.txt"))));
			} 
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
}
