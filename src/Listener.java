// Import required java libraries
import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import appLogic.DiningHallScraper;

// Extend HttpServlet class
public class Listener extends HttpServlet {
 
  private String message;

  public void init() throws ServletException
  {
      // Do required initialization
      message = "Hello World";
  }

  
  @Override
  public void doGet(HttpServletRequest request,HttpServletResponse response) 
		  throws ServletException, IOException{

  }
}