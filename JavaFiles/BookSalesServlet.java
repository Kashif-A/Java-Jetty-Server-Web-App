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
* Servlet displays books and associated sales record retrieved from database.
*
* @author  Kashif Ahmed - 18061036
* @version 1.0
* @since   December 2018 
*/
public class BookSalesServlet extends HttpServlet {	
	/**
	 * serialVersionUID to prevent InvalidClassExceptions during deserialization.
	 */
	private static final long serialVersionUID = -6729153345761427073L;

	/**
	* Servlet method to respond to HTML GET requests. HttpSession objects are used for security purposes.
	* Servlet displays sales record for books.
	*
	* @param request to deal with HTML GET requests.
	* @param response to respond to requests made to server.
	* @exception IOException in-case any text files are missing.
	*/
	@Override
	//Boilerplate Servlet code. Content HTML and OK response.
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
    	try {
    		//Session creation for security.
        	HttpSession newSesh = request.getSession(true);//Create session object.
        	//Session checks.
    		if(newSesh.getAttribute("user") == null)
    			response.sendRedirect("login");
    		
    		//Session checks.
    		else if(newSesh.getAttribute("user").equals("admin")){  
				//PrintWriter to write HTTP responses after requests received.
				PrintWriter out = response.getWriter();
				//Get all the books in database.
				ArrayList<String> salesList = new ArrayList<>();
				salesList = BookDAO.getAllSalesRecord();
  	
				//Prevent Cache so secure area is completely inaccessible after logout.
				response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
				response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
				response.setDateHeader("Expires", 0); // Proxies.

				//Send pre-database HTML data first.
				out.write(new String(Files.readAllBytes(Paths.get(Controller.filepath + "Critical Files/TextFiles/HeaderContentAdmin.txt"))));
					
					//Loop through the database tuples and print into new row of the HTML table per data item.
					for(int i=0; i<salesList.size(); i+=4) {
						out.write("<tr><td><a href=\"http://localhost:3005/booksalesupdate?id=" + salesList.get(i).toString() + "\">"
								+ "<button>&nbsp&nbsp&nbsp<img src=\"https://www.freeiconspng.com/uploads/edit-pen-write-icon--2.png\"></button></a><b><em><i>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp" + salesList.get(i).toString() + "</td></tr>"); } // END of for loop.
					
					//Send second header information
					out.write(new String(Files.readAllBytes(Paths.get(Controller.filepath + "Critical Files/TextFiles/SalesRecord.txt"))));

					for(int i=0; i<salesList.size(); i+=4) { 									
						out.write(  "<tr class=\"row100 body\">\n" + 
									"<td class=\"column2\">" + salesList.get(i+1) + "</td>\n" + 
									"<td class=\"column2\">" + salesList.get(i+2) + "</td>\n" + 
									"<td class=\"column3\">" + salesList.get(i+3) + "</td></tr>");}  //END of for loop.
					
					//Send closing HTML. Closing </div></body></html> tags etc
					out.write(new String(Files.readAllBytes(Paths.get(Controller.filepath + "Critical Files/TextFiles/FooterContent.txt"))));
			    }
    		} catch (IOException e) {
	    			e.printStackTrace(); 
    		}
	}
}



