import javax.swing.SwingUtilities;

public class DisplayServer {
	public static  void displayMessage( final String messageToDisplay )
	   {
	      SwingUtilities.invokeLater(
	         new Runnable() 
	         {
	            public void run() // updates displayArea
	            {
	               Server.displayArea.append( messageToDisplay ); // append message
	            } // end method run
	         } // end anonymous inner class
	      ); // end call to SwingUtilities.invokeLater
	   } // end method displayMessage
}
