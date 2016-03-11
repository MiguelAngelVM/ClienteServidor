// Fig. 27.5: Server.java
// Server portion of a client/server stream-socket connection. 
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
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Server extends JFrame 
{


	 private ObjectOutputStream output; // output stream to client
	   private ObjectInputStream input; // input st
   private JTextField enterField; // inputs message from user
   public static JTextArea displayArea; // display information to user
   private ServerSocket server; // server socket
   private Socket connection; // connection to client
   private int counter = 1; // counter of number of connections
   
   public JTextArea getDisplayArea() {
		return displayArea;
	}

	public void setDisplayArea(JTextArea displayArea) {
		this.displayArea = displayArea;
	}

   // set up GUI
   public Server()
   {
      super( "Server" );

      enterField = new JTextField(); // create enterField
      enterField.setEditable( false );
      enterField.addActionListener(
         new ActionListener() 
         {
            // send message to client
            public void actionPerformed( ActionEvent event )
            {
               SendData.sendData(output, event.getActionCommand() );
               enterField.setText( "" );
            } // end method actionPerformed
         } // end anonymous inner class
      ); // end call to addActionListener

      add( enterField, BorderLayout.NORTH );

      displayArea = new JTextArea(); // create displayArea
      add( new JScrollPane( displayArea ), BorderLayout.CENTER );

      setSize( 300, 150 ); // set size of window
      setVisible( true ); // show window
   } // end Server constructor

   // set up and run server 
   public void runServer()
   {
      try // set up server to receive connections; process connections
      {
         server = new ServerSocket( 12345, 100 ); // create ServerSocket

         while ( true ) 
         {
            try 
            {
               waitForConnection(); // wait for a connection
               ExecutorService threadExecutor = Executors.newCachedThreadPool();
               PrintTask task = new PrintTask( "task"+counter, connection);
    		   threadExecutor.execute( task );
              
            } // end try
            catch ( EOFException eofException ) 
            {
               DisplayServer.displayMessage( "\nServer terminated connection" );
            } // end catch
            finally 
            {
              
               ++counter;
            } // end finally
         } // end while
      } // end try
      catch ( IOException ioException ) 
      {
         ioException.printStackTrace();
      } // end catch
   } // end method runServer

   // wait for connection to arrive, then display connection info
   private void waitForConnection() throws IOException
   {
      DisplayServer.displayMessage( "Waiting for connection\n" );
      connection = server.accept(); // allow server to accept connection            
      DisplayServer.displayMessage( "Connection " + counter + " received from: " +
         connection.getInetAddress().getHostName() );
   } // end method waitForConnection

   // get streams to send and receive data
   
   // close streams and socket
   
   
   

} // end class Server




// launch the application




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