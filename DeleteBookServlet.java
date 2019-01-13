import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
* Servlet to delete a book chosen by user using Jetty server web app.
*
* @author  Kashif Ahmed - 18061036
* @version 1.0
* @since   November 2018 
*/
public class DeleteBookServlet extends HttpServlet {
		
	/**
	 * serialVersionUID to prevent InvalidClassExceptions during deserialization.
	 */
	private static final long serialVersionUID = -2637120354138697111L;

	/**
	* Servlet method to respond to HTML GET requests. HttpSession objects are used for security purposes.
	* Servlet allows user to change book sales record through HTML form.
	*
	* @param request to deal with HTML GET requests.
	* @param response to respond to requests made to server.
	* @exception IOException in-case text file is missing.
	*/
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException{
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);    	
		try {
			//Creating session object for security.
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
				//PrintWriter to send HTML responses.
				PrintWriter out = response.getWriter();
				//If book deleted successfully, send Success HTML file. Else send Fail file.
				if(BookDAO.deleteBook(Integer.parseInt(request.getParameter("id").toString())))
					out.write(new String(Files.readAllBytes(Paths.get(Controller.filepath + "Critical Files/TextFiles/DeleteBookSuccess.txt"))));
				else  
					out.write(new String(Files.readAllBytes(Paths.get(Controller.filepath + "Critical Files/TextFiles/DeleteBookFail.txt"))));
			}						 
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
	}
}
