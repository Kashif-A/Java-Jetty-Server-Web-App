import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* Servlet to check validity of ISBNs.
*
* @author  Kashif Ahmed - 18061036
* @version 1.0
* @since   November 2018 
*/
public class ISBNValidatorServlet extends HttpServlet {
	
	/**
	 * serialVersionUID to prevent InvalidClassExceptions during deserialization.
	 */
	private static final long serialVersionUID = 8016649183450990289L;
	//PrintWriter to send HTTP responses.
	PrintWriter out = null;
			
	/**
	* Servlet method to respond to HTML GET requests. Servlet gets user to input ISBN for checking.
	*
	* @param request to deal with HTML GET requests.
	* @param response to respond to requests made to server.
	* @exception SQLException for any SQL/database interaction related errors.
	*/
	@Override
	//Boilerplate Servlet code. Content HTML and OK response.
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);

		try {
			out = response.getWriter();
	    	out.write(new String(Files.readAllBytes(Paths.get(Controller.filepath + "Critical Files/TextFiles/ISBNChecker.txt"))));
			} catch (IOException e) {
    			e.printStackTrace();
			} 
	}
	
	/**
	* Servlet method to respond to HTML POST requests. Servlet checks ISBN and tells user if it is valid or not.
	*
	* @param request to deal with HTML POST requests.
	* @param response to respond to requests made to server.
	* @exception IOException in-case text files missing.
	*/
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException{
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		
		//If ISBN 13 or longer use validateISBN13Dig() method.
		try{
			System.out.println(request.getParameter("isbn"));
			if(request.getParameter("isbn").length() >= 13){
				if(validateISBN13Dig(request.getParameter("isbn"))){
					System.out.println("13");
					PrintWriter out = response.getWriter();
					out.write(new String(Files.readAllBytes(Paths.get(Controller.filepath + "Critical Files/TextFiles/ISBNValid.txt"))));
				} else {
					PrintWriter out = response.getWriter();
					out.write(new String(Files.readAllBytes(Paths.get(Controller.filepath + "Critical Files/TextFiles/ISBNInvalid.txt"))));
				} 
			} 
			
			//If ISBN 10 or less in length use validateISBN10Dig() method.
			if (request.getParameter("isbn").length() <= 10){
				if(validateISBN10Dig(request.getParameter("isbn"))){
					System.out.println("10");
					PrintWriter out = response.getWriter();
					out.write(new String(Files.readAllBytes(Paths.get(Controller.filepath + "Critical Files/TextFiles/ISBNValid.txt"))));
					} 
				else { 
					PrintWriter out = response.getWriter();
					out.write(new String(Files.readAllBytes(Paths.get(Controller.filepath + "Critical Files/TextFiles/ISBNInvalid.txt"))));
					}
			}  
		} catch (IOException e){
			e.printStackTrace();
		}
}
	
	/**
	* ISBN validator for ISBNs that are 10 digits long.
	* 
	* @param isbn User passes 10 digit ISBN to method.
	* @return Boolean if ISBN is valid (true), if ISBN is invalid (false).
	* @exception NumberFormatException in-case user puts letters where numbers should be.
	*/
	public static boolean validateISBN10Dig(String isbn)
    {
        if (isbn == null) { return false; }

        //Remove any dashes.
        isbn = isbn.replaceAll( "-", "" );

        //must be a 10 digit ISBN
        if (isbn.length() != 10) { return false; }

        try
        {
            int total = 0;
            for (int i=0; i<9; i++) {
                int digit = Integer.parseInt( isbn.substring( i, i + 1 ) );
                total += ((10 - i) * digit); }  //END for loop.

            String checksum = Integer.toString( (11 - (total % 11)) % 11 );
            if ("10".equals(checksum)) { checksum = "X"; }

            return checksum.equals(isbn.substring(9));
        } catch (NumberFormatException e) {
            return false;
        }
    }
	
	/**
	* ISBN validator for ISBNs that are 13 digits long.
	* 
	* @param isbn User passes 13 digit ISBN to method.
	* @return Boolean if ISBN is valid (true), if ISBN is invalid (false).
	* @exception NumberFormatException in-case user puts letters where numbers should be.
	*/
	 public static boolean validateISBN13Dig(String isbn)
	    {
		 	if (isbn == null) { return false; }

	        //Remove any dashes.
	        isbn = isbn.replaceAll( "-", "" );

	        //must be a 13 digit ISBN
	        if (isbn.length() != 13) { return false; }

	        try
	        {
	            int total = 0;
	            for ( int i=0; i<12; i++)
	            {
	                int digit = Integer.parseInt( isbn.substring(i,i+1));
	                total += (i % 2 == 0) ? digit*1 : digit*3; }  //END for loop.

	            //checksum must be 0-9. If calculated as 10 then = 0
	            int checksum = 10 - (total % 10);
	            if (checksum == 10) { checksum = 0; }

	            return checksum == Integer.parseInt( isbn.substring( 12 ) );
	        }
	        catch (NumberFormatException e) {
	            return false;
	        }
	    }
}

