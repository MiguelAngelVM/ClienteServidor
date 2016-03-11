// Fig. 26.3: PrintTask.java
// PrintTask class sleeps for a random time from 0 to 5 seconds
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class PrintTask implements Runnable 
{
   private final int sleepTime; // random sleep time for thread
   private final String taskName; // name of task
   private final static Random generator = new Random();
   private ObjectOutputStream output; // output stream to client
   private ObjectInputStream input; // input stream from client
   private Socket connectionNew; // connection to client
   // constructor
  

   public PrintTask(String name, Socket connection) {
	// TODO Auto-generated constructor stub
	taskName = name; // set task name
    connectionNew = connection;
      
    sleepTime = generator.nextInt( 1000 );
}



// method run contains the code that a thread will execute
   // method run contains the code that a thread will execute
   public void run()
   {
	   try {
		   getStreams(); // get input & output streams
	       processConnection();
	} catch (Exception e) {
		DisplayServer.displayMessage( "\nServer terminated connection" );
	}
	   finally 
       {
          closeConnection(); //  close connection
         
       } // end finally
	   
      
   } // end method run
   private void getStreams() throws IOException
   {
      // set up output stream for objects
      output = new ObjectOutputStream( connectionNew.getOutputStream() );
      output.flush(); // flush output buffer to send header information

      // set up input stream for objects
      input = new ObjectInputStream( connectionNew.getInputStream() );

      DisplayServer.displayMessage( "\nGot I/O streams\n" );
   } // end method getStreams

   // process connection with client
   private void processConnection() throws IOException
   {
      String message = "Connection successful";
      SendData.sendData( output,message ); // send connection successful message

      // enable enterField so server user can send messages
      setTextFieldEditable( true );

      do // process messages sent from client
      { 
         try // read message and display it
         {
            message = ( String ) input.readObject(); // read new message
            BaseDeDatos.BD(message, output);
         } // end try
         catch ( ClassNotFoundException classNotFoundException ) 
         {
            DisplayServer.displayMessage( "\nUnknown object type received" );
         } // end catch

      } while ( !message.equals( "CLIENT>>> TERMINATE" ) );
   } // end method processConnection

   private void setTextFieldEditable(boolean b) {
	// TODO Auto-generated method stub
	
}



private void closeConnection() 
   {
	   DisplayServer.displayMessage( "\nTerminating connection\n" );
      setTextFieldEditable( false ); // disable enterField

      try 
      {
         output.close(); // close output stream
         input.close(); // close input stream
         connectionNew.close(); // close socket
      } // end try
      catch ( IOException ioException ) 
      {
         ioException.printStackTrace();
      } // end catch
   } // end method closeConnection

   // send message to client
  

   // manipulates displayArea in the event-dispatch thread
   

   // manipulates enterField in the event-dispatch thread
   private void setTextFieldEditable( final JTextField editable )
   {
      SwingUtilities.invokeLater(
         new Runnable()
         {
            public void run() // sets enterField's editability
            {
              
            } // end method run
         }  // end inner class
      ); // end call to SwingUtilities.invokeLater
   } // end method setTextFieldEditable
  
}// end class PrintTask

/**************************************************************************
 * (C) Copyright 1992-2012 by Deitel & Associates, Inc. and               *
 * Pearson Education, Inc. All Rights Reserved.                           *
 *                                                                        *
 * DISCLAIMER: The authors and publisher of this book have used their     *
 * best efforts in preparing the book. These efforts include the          *
 * development, research, and testing of the theories and programs        *
 * to determine their effectiveness. The authors and publisher make       *
 * no warranty of any kind, expressed or implied, with regard to these    *
 * programs or to the documentation contained in these books. The authors *
 * and publisher shall not be liable in any event for incidental or       *
 * consequential damages in connection with, or arising out of, the       *
 * furnishing, performance, or use of these programs.                     *
 *************************************************************************/