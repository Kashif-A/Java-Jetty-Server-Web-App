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
* Servlet to allow user to login and validate the login details.
*
* @author  Kashif Ahmed - 18061036
* @version 1.0
* @since   November 2018 
*/
public class LoginServlet extends HttpServlet {
	
	/**
	 * serialVersionUID to prevent InvalidClassExceptions during deserialization.
	 */
	private static final long serialVersionUID = -3054516715115982849L;

	/**
	* Servlet method to respond to HTML GET requests. Allows user to login.
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
			PrintWriter out = response.getWriter();
			out.write(new String(Files.readAllBytes(Paths.get(Controller.filepath + "Critical Files/TextFiles/Login.txt"))));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	* Servlet method to respond to HTML POST requests. HttpSession objects are used for security purposes.
	* Servlet checks user login details and redirects to either login again or /books page.
	*
	* @param request to deal with HTML POST requests.
	* @param response to respond to requests made to server.
	* @exception IOException in-case text files missing.
	* @exception IllegalStateException to ensure proper session functionality.
	* */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException{
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		
		try {
			if(BookDAO.checkLoginCredentials(request.getParameter("uname"), request.getParameter("pwd"))){
				
					//Get any old session and invalidate.
		            HttpSession oldSesh = request.getSession(false);
		            if (oldSesh != null) {
		                oldSesh.invalidate();
		            }
		            //Form new session.
		            HttpSession newSesh = request.getSession(true);
		            newSesh.setAttribute("user", request.getParameter("uname"));
	
		            //Setting session to expire in 30 mins.
		            newSesh.setMaxInactiveInterval(30*60);
		            //Redirect user to /books. With session, /books will display administrative functions.
		            response.sendRedirect("books");
				} else
					response.sendRedirect("login"); //If username or password wrong, redirect to login for user to try again.

		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}
	}
}
