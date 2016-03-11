import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Comunicacion {
	private JTextField enterField; // inputs message from user
	   private JTextArea displayArea; // display information to user
	   private ObjectOutputStream output; // output stream to client
	   private ObjectInputStream input; // input stream from client
	  	private ServerSocket server; // server socket
	   private Socket connection; // connection to client
	   private int counter = 1; 
	   static final String DATABASE_URL = "jdbc:mysql://127.0.0.1/books";
	   
	public void start(Socket connection,ServerSocket server,  int counter, ObjectOutputStream output,
			ObjectInputStream input,JTextArea displayArea,JTextField enterField ) throws IOException {
		this.connection = connection;
		this.server = server;
		this.counter = counter;
		this.output = output;
		this.input = input;
		this.displayArea = displayArea;
		this.enterField = enterField;

		try {
			waitForConnection(); // wait for a connection
			 ExecutorService threadExecutor = Executors.newCachedThreadPool();
             // create and name each runnable
			 // process connection
		} // end try
		catch (EOFException eofException) {
			displayMessage("\nServer terminated connection");
		} // end catch
		finally {
			closeConnection(); // close connection
		
		} // end finally
	}
	private void waitForConnection() throws IOException
	   {
	      displayMessage( "Waiting for connection\n" );
	      connection = server.accept(); // allow server to accept connection            
	      displayMessage( "Connection " + counter + " received from: " +
	         connection.getInetAddress().getHostName() );
	      
	   } // end method waitForConnection

	   // get streams to send and receive data
	   private void getStreams() throws IOException
	   {
	      // set up output stream for objects
	      output = new ObjectOutputStream( connection.getOutputStream() );
	      output.flush(); // flush output buffer to send header information

	      // set up input stream for objects
	      input = new ObjectInputStream( connection.getInputStream() );

	      displayMessage( "\nGot I/O streams\n" );
	   } // end method getStreams

	   // process connection with client
	   private void processConnection() throws IOException
	   {
	      String message = "Connection successful";
	      sendData( message ); // send connection successful message

	      // enable enterField so server user can send messages
	      setTextFieldEditable( true );

	      do // process messages sent from client
	      { 
	         try // read message and display it
	         {
	            message = ( String ) input.readObject(); // read new message
	            BD(message);
	         } // end try
	         catch ( ClassNotFoundException classNotFoundException ) 
	         {
	            displayMessage( "\nUnknown object type received" );
	         } // end catch

	      } while ( !message.equals( "CLIENT>>> TERMINATE" ) );
	   } // end method processConnection

	   public  void displayMessage( final String messageToDisplay )
	   {
	      SwingUtilities.invokeLater(
	         new Runnable() 
	         {
	            public void run() // updates displayArea
	            {
	               displayArea.append( messageToDisplay ); // append message
	            } // end method run
	         } // end anonymous inner class
	      ); // end call to SwingUtilities.invokeLater
	   } // end method displayMessage
	   private void closeConnection() 
	   {
	      displayMessage( "\nTerminating connection\n" );
	      setTextFieldEditable( false ); // disable enterField

	      try 
	      {
	         output.close(); // close output stream
	         input.close(); // close input stream
	         connection.close(); // close socket
	      } // end try
	      catch ( IOException ioException ) 
	      {
	         ioException.printStackTrace();
	      } // end catch
	   } // end method closeConnection
	   private void sendData( String message )
	   {
	      try // send object to client
	      {
	         output.writeObject( "SERVER>>> " + message );
	         output.flush(); // flush output to client
	         
	      } // end try
	      catch ( IOException ioException ) 
	      {
	         displayArea.append( "\nError writing object" );
	      } // end catch
	   } // end method sendData

	   // manipulates displayArea in the event-dispatch thread
	   private void setTextFieldEditable( final boolean editable )
	   {
	      SwingUtilities.invokeLater(
	         new Runnable()
	         {
	            public void run() // sets enterField's editability
	            {
	               enterField.setEditable( editable );
	            } // end method run
	         }  // end inner class
	      ); // end call to SwingUtilities.invokeLater
	   } // end method setTextFieldEditable
	   public   void BD( String sentencia )
	   {
		  String total = null;
	      Connection connection = null; // manages connection
	      Statement statement = null; // query statement
	      ResultSet resultSet = null; // manages results
	    
	      // connect to database books and query database
	      try 
	      {
	         // establish connection to database                              
	         connection = DriverManager.getConnection( 
	            DATABASE_URL, "root", "" );

	         // create Statement for querying database
	         statement = connection.createStatement();
	         
	         // query database                                        
	         resultSet = statement.executeQuery(            
	            sentencia );
	         
	         // process query results
	         ResultSetMetaData metaData = resultSet.getMetaData();
	         int numberOfColumns = metaData.getColumnCount();     
	         sendData( "Authors Table of Books Database:\n" );
	         
	         for ( int i = 1; i <= numberOfColumns; i++ ){
	        	total += "\t";
	        	 total += ( metaData.getColumnName( i ) );
	         }
	         	
	         sendData(total);
	         total = null;
	         
	         while ( resultSet.next() ) 
	         {
	            for ( int i = 1; i <= numberOfColumns; i++ )
	            {
	            	total += "\t";
	            	total += (resultSet.getObject( i ).toString());
	            }
	            sendData(total);
	            total = null;
	         } // end while
	         
	      }  // end try
	      catch ( SQLException sqlException )                                
	      {                                                                  
	         sqlException.printStackTrace();
	      } // end catch                                                     
	      finally // ensure resultSet, statement and connection are closed
	      {                                                             
	         try                                                        
	         {                                                          
	            resultSet.close();                                      
	            statement.close();                                      
	            connection.close();                                     
	         } // end try                                               
	         catch ( Exception exception )                              
	         {                                                          
	            exception.printStackTrace();                            
	         } // end catch                                             
	      } // end finally                                              
	   } // end main

	   
	   
	 
}
