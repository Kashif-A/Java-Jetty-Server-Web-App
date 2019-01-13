import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
* Class to start and set up a Jetty server.
*
* @author  Kashif Ahmed - 18061036
* @version 1.0
* @since   November 2018 
*/
public class WebServerJetty {
	/**
	* Jetty Server set-up and start. Listens on port 3005.
	* 
	*/
	public static void startServer() throws Exception {
		//New Server object that will listen on port 3005 when started.
		Server server = new Server(3005); 
		//Session support and handler set-up.
		ServletContextHandler servletHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
		server.setHandler(servletHandler);
		//Handlers to connect servlets to paths.
		servletHandler.setContextPath("/");
		servletHandler.addServlet(new ServletHolder(new BookDisplayServlet()), "/books");
		servletHandler.addServlet(new ServletHolder(new ISBNValidatorServlet()), "/isbnvalidator");
		servletHandler.addServlet(new ServletHolder(new SearchBooksServlet()), "/searchbooks");
		servletHandler.addServlet(new ServletHolder(new BookSalesServlet()), "/booksales");
		servletHandler.addServlet(new ServletHolder(new BookSalesUpdate()), "/booksalesupdate");
		servletHandler.addServlet(new ServletHolder(new LoginServlet()), "/login");
		servletHandler.addServlet(new ServletHolder(new BookDisplayServlet()), "/*");
		servletHandler.addServlet(new ServletHolder(new InsertBookServlet()), "/insertbook");
		servletHandler.addServlet(new ServletHolder(new DeleteBookServlet()), "/deletebook");
		servletHandler.addServlet(new ServletHolder(new EditBookServlet()), "/editbook");
		servletHandler.addServlet(new ServletHolder(new LogoutServlet()), "/logout");
		
		server.start();   	//Start Jetty Server.
		
		System.out.println("\n\n\n\nJetty Server started. Listening on port 3005");
		
		server.join();
	}
}

