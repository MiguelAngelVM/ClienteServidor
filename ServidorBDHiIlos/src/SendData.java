import java.io.IOException;
import java.io.ObjectOutputStream;

public class SendData {
	 public static void sendData(ObjectOutputStream output, String message )
	   {
	      try // send object to client
	      {
	         output.writeObject( "SERVER>>> " + message );
	         output.flush(); // flush output to client
	         
	      } // end try
	      catch ( IOException ioException ) 
	      {
	         DisplayServer.displayMessage( "\nError writing object" );
	      } // end catch
	   } // end method sendData

}
