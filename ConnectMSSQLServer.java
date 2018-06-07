import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class ConnectMSSQLServer {
	
	public static void main(String[] args)
	   {
	      ConnectMSSQLServer connServer = new ConnectMSSQLServer();
	      //This was a test to a non-BO SQL Server just to test that I can connect.
	      //connServer.dbConnect("jdbc:sqlserver://TLEXWTMDB001", "filenet_arch", "*******");
	      
	      //This is a test to a BO SQL Server database server
	      connServer.dbConnect("jdbc:sqlserver://DASHWBODB012", "BOTRACK", "********");
	   }
	
	  public void dbConnect(String db_connect_string, String db_userid, String db_password)
	   {
	      try {
	         Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	         Connection conn = DriverManager.getConnection(db_connect_string, db_userid, db_password);
	         System.out.println("connected");
	         Statement statement = conn.createStatement();
	         //String queryString = "select * from sysobjects where type='u'";
	         //String queryString = "select * from USAPDOC01.dbo.ELEMENT ELEMENT where ELEMENT.E_INA22='Lexmark Singapore - 00042' and ELEMENT.E_INA05=41015616";
	         String queryString = "select * from STG1_BO4D_RUNHIST";
	         ResultSet rs = statement.executeQuery(queryString);
	         while (rs.next()) {
	            System.out.println(rs.getString(1));
	         }
	      } catch (Exception e) {
	         System.out.println (e.getMessage());
	         e.printStackTrace();
	      }
	   }

	   

}
