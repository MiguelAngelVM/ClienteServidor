import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class BaseDeDatos {
	static final String DATABASE_URL = "jdbc:mysql://127.0.0.1/books";
	 public static ObjectOutputStream output;
	   public static  void BD( String sentencia,ObjectOutputStream oldOutput )
	   {
		  output = oldOutput;
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
	         SendData.sendData(output, "Authors Table of Books Database:\n" );
	         
	         for ( int i = 1; i <= numberOfColumns; i++ ){
	        	total += "\t";
	        	 total += ( metaData.getColumnName( i ) );
	         }
	         	
	         SendData.sendData(output,total);
	         total = null;
	         
	         while ( resultSet.next() ) 
	         {
	            for ( int i = 1; i <= numberOfColumns; i++ )
	            {
	            	total += "\t";
	            	total += (resultSet.getObject( i ).toString());
	            }
	            SendData.sendData(output,total);
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

	   }


}
