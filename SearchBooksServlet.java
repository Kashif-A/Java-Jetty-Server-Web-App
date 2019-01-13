import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* Servlet to allow user to search for a specific book within the database.
*
* @author  Kashif Ahmed - 18061036
* @version 1.0
* @since   November 2018 
*/
public class SearchBooksServlet extends HttpServlet {
	
	/**
	 * serialVersionUID to prevent InvalidClassExceptions during deserialization.
	 */
	private static final long serialVersionUID = -4914830318232067744L;

	/**
	* Servlet method to respond to HTML GET requests. Servlet allows user to input search criteria to search for specific books matching those criteria.
	* User can search using Title, Author or minPrice/maxPrice.
	*
	* @param request to deal with HTML GET requests.
	* @param response to respond to requests made to server.
	* @exception IOException in-case text file is missing.
	*/
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		
	    try {
			//PrintWriter to send HTTP responses.
			PrintWriter out = response.getWriter();
	    	//Send pre-table data first.
	    	out.write(new String(Files.readAllBytes(Paths.get(Controller.filepath + "Critical Files/TextFiles/SearchBookQuery.txt"))));
	    	
		} catch (IOException e) {
			e.printStackTrace();
		}  
	}
}



