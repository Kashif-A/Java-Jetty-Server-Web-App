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
* Servlet to update book sales records based on user input.
*
* @author  Kashif Ahmed - 18061036
* @version 1.0
* @since   November 2018 
*/
public class BookSalesUpdate extends HttpServlet {
	/**
	 * serialVersionUID to prevent InvalidClassExceptions during deserialization.
	 */
	private static final long serialVersionUID = -6194447782563679737L;
	//PrintWriter to write HTTP responses after requests received.
	PrintWriter out = null;
	
	/**
	* Servlet method to respond to HTML GET requests. HttpSession objects are used for security purposes.
	* Servlet allows user to change book sales record through HTML form.
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
			//Creating session object for security.
			HttpSession newSesh = request.getSession(true);
	    	//Session checks.
	    	if(newSesh.getAttribute("user") == null)
				response.sendRedirect("login");

	    	else if(newSesh.getAttribute("user").equals("admin")){  
	    		//Prevent Cache so secure area is completely inaccessible after logout.
	    	    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
	    	    response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
	    	    response.setDateHeader("Expires", 0); // Proxies.
				ArrayList<String> salesRecord = BookDAO.getSpecificSalesRecord(Integer.parseInt(request.getParameter("id")));
				out = response.getWriter();
				//Send HTML in response to request
		    	out.write(new String(Files.readAllBytes(Paths.get(Controller.filepath + "Critical Files/TextFiles/EditSalesRecords.txt"))));
		    	out.write("<p id=\"two\" style=\"width: 100%\">Book Title: &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp" + salesRecord.get(1) + "</p>"
						+ "<p id=\"two\">Copies Sold: &nbsp</p><input type=\"text\" name=\"sales\" value=\"" + salesRecord.get(2) + "\"/><br>"
						+ "<input type=\"text\" name=\"id\" value=\"" + salesRecord.get(0) + "\" style=\"display: none;\"/><br>"
						+ "<br><button>Submit</button></form><br><a href=\"/booksales\"><button>go back</button></a>"
						+ "<br><br><br><p id=\"error\">Error! Only a number can be entered in Year, Edition, Price and Sales fields.</p></div></div></div></div></body>"
						+ "<script type=\"text/javascript\">"
							+ "function formChecker() {"
							+ "	var sales; var allow = true;"
							+ "	sales = document.forms[\"loginForm\"][\"sales\"].value;"
							+ "	if(isNaN(sales)){"
									+ "document.getElementById(\"error\").style.visibility = \"visible\";"
									+ "allow = false;"
									+ "setTimeout(function(){"
										+ "document.getElementById(\"error\").style.visibility = \"hidden\";"
										+ "}, 6000); return false;}"
								+ "return allow;"
								+ "}"
						+ "</script></html>");
	    	}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
    }
	
	/**
	* Servlet method to respond to HTML POST requests. HttpSession objects are used for security purposes.
	* Servlet attempts to update sales record entered by user and sends success or failure message to user accordingly.
	*
	* @param request to deal with HTML POST requests.
	* @param response to respond to requests made to server.
	* @exception IOException in-case text files missing.
	*/
	@SuppressWarnings("unused")
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException{
		boolean bookInsertionSuccess = false;
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);    	
		try {
			//Creating session object for security.
			HttpSession newSesh = request.getSession();
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
	    		
				//Add user input received through key value pairs into ArrayList.
				userInputs.add(request.getParameter("id"));
				userInputs.add(request.getParameter("sales"));
				System.out.println(request.getParameter("id"));
				System.out.println(request.getParameter("sales"));
		    	
		    	//Insert book using bookDAO method.
	    		out = response.getWriter();
				if(BookDAO.updateSalesRecord(userInputs)){
					out.write(new String(Files.readAllBytes(Paths.get(Controller.filepath + "Critical Files/TextFiles/EditSalesFinal.txt"))));
				} else{
					out.write(new String(Files.readAllBytes(Paths.get(Controller.filepath + "Critical Files/TextFiles/EditSalesFail.txt"))));
			    }
	    	}
		} catch (IOException e) {
				e.printStackTrace();
		} 					
	}
}
