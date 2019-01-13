import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
* Servlet to handle user logout. Invalidates old session.
*
* @author  Kashif Ahmed - 18061036
* @version 1.0
* @since   November 2018 
*/
public class LogoutServlet extends HttpServlet {
	
	/**
	 * serialVersionUID to prevent InvalidClassExceptions during deserialization.
	 */
	private static final long serialVersionUID = -8962866084952713644L;

	/**
	* Servlet method to respond to HTML GET requests. Invalidate old session.
	*
	* @param request to deal with HTML GET requests.
	* @param response to respond to requests made to server.
	* @exception IOException to ensure proper functionality.
	*/
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException{
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		
		//Get session and invalidate.
        HttpSession session = request.getSession();
        session.removeAttribute("user");
        session.invalidate();
        try {
			response.sendRedirect("books");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
