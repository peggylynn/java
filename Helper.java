import java.sql.*;
import java.util.*;
import java.util.Date;

import sun.net.smtp.*;

import java.io.*;

import com.crystaldecisions.sdk.exception.SDKException;
import com.crystaldecisions.sdk.framework.ISessionMgr;
import com.crystaldecisions.sdk.framework.CrystalEnterprise;
import com.crystaldecisions.sdk.framework.IEnterpriseSession;
import com.crystaldecisions.sdk.occa.infostore.*;
import com.crystaldecisions.sdk.plugin.CeKind;
import com.crystaldecisions.sdk.plugin.desktop.common.IReportParameter;
import com.crystaldecisions.sdk.plugin.desktop.common.IReportParameterSingleValue;
import com.crystaldecisions.sdk.plugin.desktop.common.IReportParameterValue;
import com.crystaldecisions.sdk.plugin.desktop.common.IReportParameterValues;
import com.crystaldecisions.sdk.plugin.desktop.common.IReportProcessingInfo;
import com.crystaldecisions.sdk.plugin.desktop.excel.IExcel;
import com.crystaldecisions.sdk.plugin.desktop.pdf.IPDF;
import com.crystaldecisions.sdk.plugin.desktop.report.IReport;
import com.crystaldecisions.sdk.plugin.destination.smtp.IAttachments;
import com.crystaldecisions.sdk.plugin.destination.smtp.ISMTP;
import com.crystaldecisions.sdk.plugin.destination.smtp.ISMTPOptions;
import com.crystaldecisions.sdk.prompting.IPrompts;
import com.crystaldecisions.sdk.properties.IProperties;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.lang.*;



//import java.sql.Date extends java.util.Date;
//import java.sql.Time extends java.util.Date;
//import java.sql.Timestamp extends java.util.Date;

public class Helper {
	public Helper()
	{
	}

	public static String getMaxID(String mssqlSelectString) {
		//This function finds the max id in a table. 
		//String driver = "oracle.jdbc.driver.OracleDriver";
		//String url = "jdbc:oracle:thin:@dlxkrbodb1.lex.lexmark.com:1750:edr1den1";

		String driver = "mssql.jdbc.driver.SQLDriver";
		String url = "jdbc:sqlserver://DASHWBODB012;databaseName=EDR1DEN1;integratedSecurity=true;";

		ConnectMSSQLServer connServer = new ConnectMSSQLServer();
		connServer.dbConnect("jdbc:sqlserver://DASHWBODB012", "BOTRACK", "********");



		String maxID ="0";
		try
		{
			System.out.println(mssqlSelectString);
			//Class.forName(driver);		//loads the driver
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			Connection conn = DriverManager.getConnection("jdbc:sqlserver://DASHWBODB012", "BOTRACK", "********");
			System.out.println("connected");


			Statement stmt = conn.createStatement() ; 
			ResultSet rs = stmt.executeQuery(mssqlSelectString);
			while (rs.next()) 
			{
				maxID = rs.getString("maxID"); 
			}
			conn = null;
			conn.close();
		}	
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return maxID;

	}
	public static  Calendar getMaxLoadDt(String mssqlSelectString) {

		//This function finds the last time a record was loaded into a table.
		//It is used to determine if more records are to be loaded.
		System.out.println(mssqlSelectString);
		//String driver = "oracle.jdbc.driver.OracleDriver"; //supposedly deprecated
		//String driver = "oracle.jdbc.OracleDriver";
		//String url = "jdbc:oracle:thin:@dlxkrbodb1.lex.lexmark.com:1750:edr1den1";
		String username = "botrack";
		String password = "********";

		String driver = "mssql.jdbc.driver.SQLDriver";
		String url = "jdbc:sqlserver://DASHWBODB012;databaseName=EDR1DEN1;integratedSecurity=true;";

		//Calendar maxLoadDate=new GregorianCalendar(1980,Calendar.JANUARY,1);
		//java.sql.Date maxLoadDate = new java.sql.Date(1980,1,1);
		Calendar maxLoadDate = Calendar.getInstance();
		maxLoadDate.set(1980,0,1,0,0,0);
		//Date dbDate;
		//java.sql.Date dbDate = new java.sql.Date(Calendar.getInstance().getTimeInMillis());
		java.sql.Timestamp dbDate = new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis());

		
		ConnectMSSQLServer connServer = new ConnectMSSQLServer();
		connServer.dbConnect("jdbc:sqlserver://DASHWBODB012", "BOTRACK", "********");

	
		try
		{	
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			Connection conn = DriverManager.getConnection("jdbc:sqlserver://DASHWBODB012", "BOTRACK", "********");
			System.out.println("connected");
			
			Statement stmt = conn.createStatement() ; 
			System.out.println(mssqlSelectString);
			ResultSet rs = stmt.executeQuery(mssqlSelectString);

			while ( rs.next() ) 
			{
				//dbDate = rs.getDate("maxLoadDate");
				dbDate = rs.getTimestamp("maxLoadDate");
				if (dbDate != null) {
					maxLoadDate.setTime(dbDate);
					//maxLoadDate = rs.getDate("maxLoadDate");
				}
			}
			/*//System.out.println("Max date is " + maxLoadDate.getInstance());  //says I should access this in a "static way"
			System.out.println("Max date is " + maxLoadDate); 

			//Calendar cal = Calendar.getInstance();
		    SimpleDateFormat sdf = new SimpleDateFormat();
		    System.out.println(sdf.format("Max datetime is " + maxLoadDate.getTime()));
			 */

			//conn = null;
			conn.close();
		}
		catch (Exception ex)
		{
			System.out.println("Exception: " + ex.getMessage() + " " + ex.getStackTrace());
			ex.printStackTrace();

		}
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh.mm.ss");
		try{
			/*Format formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			String s = formatter.format(maxLoadDate);
			System.out.println("Formatted maxLoadDate is " + s);*/

			//SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy.hh.mm.ss"); //Formatter output: 12.05.2009.12.00.00 - this is correct except for the time  

			//System.out.println("Formatter output: " + formatter.format(maxLoadDate.getTime())); //this has the right month, but not the right time 

			//System.out.println("max date output " + maxLoadDate.DAY_OF_MONTH + "-" + maxLoadDate.MONTH + "-" + maxLoadDate.YEAR + " " + maxLoadDate.HOUR + ":" + maxLoadDate.MINUTE + ":" + maxLoadDate.SECOND);
			//System.out.println("max date issss " + oracleDateTime(maxLoadDate));

			/* //See how this works for Now()
		    java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTimeInMillis());  //should give current date-time
		    Calendar c = Calendar.getInstance();		    
		    c.setTime(date);
		    //SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy.hh.mm.ss");
		    System.out.println("Now is " + formatter.format(c.getTime()));*/

		}
		catch(Exception ex)
		{
			System.out.println("Exception: " + ex.getMessage() + " " + ex.getStackTrace());
			ex.printStackTrace();
		}



		//return formatter.format(maxLoadDate.getTime());
		return maxLoadDate;


		//		End Function
	}



	////////////////Other functions to add://////////////
	//safeSQL	 - done
	//Oracle date - done
	//Bus Obj date format -done	
	//Oracle date time -done	
	//getMaxLoadDt -done	
	//Send mail -done
	//Oracle inserter
	//Build cookie Trail - done with errors




	public static String safeSQL(String strText) {
		String strSafeText;
		//'Replace single quotes with two single quotes in strings so they can be inserted into a table
		if (strText.length() == 0) {
			strSafeText = "";
		}{	    
			strSafeText = strText.replace("'","''");
			//'strSafeText = Replace(strSafeText, "&", "\&") //Don't do this here.  Do it in the SQL
		}

		return strSafeText;

		//End Function	
	}

	public static String msSQLDate(Calendar theDate){
		//Ensures the date in an insert statement is formatted correctly
		//Date format to use:  YYYY-MM-DD HH:MM:SS //implies 24 hour clock
		String theDay;
		String theMonth;
		String theYear;

		//new Date(DateFormat.getDateInstance().parse(theDate).getTime());

		theDay = String.valueOf(theDate.get(Calendar.DAY_OF_MONTH));
		theMonth = String.valueOf(theDate.get(Calendar.MONTH));
		theYear = String.valueOf(theDate.get(Calendar.YEAR));

		if (theDay.length() == 1) {
			theDay = "0" + theDay;
		}

		if (theMonth.length() == 1) {
			theMonth = "0" + theMonth;
		}
		theMonth += 1;

		return theYear + "-" + theMonth + "-" + theDay;

	}

	public static String  oracleDate(Calendar theDate){
		String theDay;
		String theMonth;
		String theYear;

		//new Date(DateFormat.getDateInstance().parse(theDate).getTime());

		theDay = String.valueOf(theDate.get(Calendar.DAY_OF_MONTH));
		theMonth = String.valueOf(theDate.get(Calendar.MONTH));
		theYear = String.valueOf(theDate.get(Calendar.YEAR));

		if (theDay.length() == 1) {
			theDay = "0" + theDay;
		}

		if (theMonth.length() == 1) {
			theMonth = "0" + theMonth;
		}
		theMonth += 1;

		return theDay + "-" + theMonth + "-" + theYear;
		//End Function
	}

	public static String  msSQLDateTime(Calendar theDate){
		//Ensures the date in an insert statement is formatted correctly
		//Date format to use:  YYYY-MM-DD HH:MM:SS //implies 24 hour clock
		//System.out.println("The date to convert is " + theDate); ugly
		String theDay;
		String theMonth;
		String theYear;
		String theHour;
		String theMinute;
		String theSecond;

		//new Date(DateFormat.getDateInstance().parse(theDate).getTime());

		theDay = String.valueOf(theDate.get(Calendar.DAY_OF_MONTH));		
		theMonth = String.valueOf(theDate.get(Calendar.MONTH));  //months in Java are 0 - 11.  Stupid.
		//System.out.println("The month is: " + theMonth);
		theYear = String.valueOf(theDate.get(Calendar.YEAR));
		theHour = String.valueOf(theDate.get(Calendar.HOUR_OF_DAY));
		theMinute = String.valueOf(theDate.get(Calendar.MINUTE));
		theSecond = String.valueOf(theDate.get(Calendar.SECOND));

		if (theDay.length() == 1) {
			theDay = "0" + theDay;
		}	

		//System.out.println("Length of month is " + theMonth.length());  //it really is length of 1
		//theMonth +=1; this doesn't work for strings if month = 6 get 61, I want if 6 then 7.
		//example: //s = (Integer.parseInt(s) + 1) + "";
		theMonth = (Integer.parseInt(theMonth) + 1) + "";
		if (theMonth.length() == 1) {
			theMonth = "0" + theMonth;
		}		

		//System.out.println("The month is: " + theMonth);

		if (theHour.length() == 1) {
			theHour = "0" + theHour;
		}

		if (theMinute.length() == 1) {
			theMinute = "0" + theMinute;
		}

		if (theSecond.length() == 1) {
			theSecond = "0" + theSecond;
		}

		//System.out.println(theDay + "-" + theMonth + "-" + theYear + " " + theHour + ":" + theMinute + ":" + theSecond);
		System.out.println(theYear + "-" + theMonth + "-" + theDay + " " + theHour + ":" + theMinute + ":" + theSecond);
		return theYear + "-" + theMonth + "-" + theDay + " " + theHour + ":" + theMinute + ":" + theSecond;

		//End Function
	}

	public static String  oracleDateTime(Calendar theDate){
		String theDay;
		String theMonth;
		String theYear;
		String theHour;
		String theMinute;
		String theSecond;

		//new Date(DateFormat.getDateInstance().parse(theDate).getTime());

		theDay = String.valueOf(theDate.get(Calendar.DAY_OF_MONTH));		
		theMonth = String.valueOf(theDate.get(Calendar.MONTH));  //months in Java are 0 - 11.  Stupid.
		//System.out.println("The month is: " + theMonth);
		theYear = String.valueOf(theDate.get(Calendar.YEAR));
		theHour = String.valueOf(theDate.get(Calendar.HOUR_OF_DAY));
		theMinute = String.valueOf(theDate.get(Calendar.MINUTE));
		theSecond = String.valueOf(theDate.get(Calendar.SECOND));

		if (theDay.length() == 1) {
			theDay = "0" + theDay;
		}	

		//System.out.println("Length of month is " + theMonth.length());  //it really is length of 1
		//theMonth +=1; this doesn't work for strings if month = 6 get 61, I want if 6 then 7.
		//example: //s = (Integer.parseInt(s) + 1) + "";
		theMonth = (Integer.parseInt(theMonth) + 1) + "";
		if (theMonth.length() == 1) {
			theMonth = "0" + theMonth;
		}		

		//System.out.println("The month is: " + theMonth);

		if (theHour.length() == 1) {
			theHour = "0" + theHour;
		}

		if (theMinute.length() == 1) {
			theMinute = "0" + theMinute;
		}

		if (theSecond.length() == 1) {
			theSecond = "0" + theSecond;
		}

		//System.out.println(theDay + "-" + theMonth + "-" + theYear + " " + theHour + ":" + theMinute + ":" + theSecond);
		return theDay + "-" + theMonth + "-" + theYear + " " + theHour + ":" + theMinute + ":" + theSecond;

		//End Function
	}

	public static String  monthDayYear(Calendar theDate){
		String theDay;
		String theMonth;
		String theYear;
		String theHour;
		String theMinute;
		String theSecond;

		//new Date(DateFormat.getDateInstance().parse(theDate).getTime());

		theDay = String.valueOf(theDate.get(Calendar.DAY_OF_MONTH));		
		theMonth = String.valueOf(theDate.get(Calendar.MONTH));  //months in Java are 0 - 11.  Stupid.
		//System.out.println("The month is: " + theMonth);
		theYear = String.valueOf(theDate.get(Calendar.YEAR));
		theHour = String.valueOf(theDate.get(Calendar.HOUR_OF_DAY));
		theMinute = String.valueOf(theDate.get(Calendar.MINUTE));
		theSecond = String.valueOf(theDate.get(Calendar.SECOND));

		if (theDay.length() == 1) {
			theDay = "0" + theDay;
		}	

		//System.out.println("Length of month is " + theMonth.length());  //it really is length of 1
		//theMonth +=1; this doesn't work for strings if month = 6 get 61, I want if 6 then 7.
		//example: //s = (Integer.parseInt(s) + 1) + "";
		theMonth = (Integer.parseInt(theMonth) + 1) + "";
		if (theMonth.length() == 1) {
			theMonth = "0" + theMonth;
		}		

		//System.out.println("The month is: " + theMonth);

		if (theHour.length() == 1) {
			theHour = "0" + theHour;
		}

		if (theMinute.length() == 1) {
			theMinute = "0" + theMinute;
		}

		if (theSecond.length() == 1) {
			theSecond = "0" + theSecond;
		}

		System.out.println(theMonth + "-" + theDay + "-" + theYear + " " + theHour + ":" + theMinute + ":" + theSecond);
		return theMonth + "-" + theDay + "-" + theYear + " " + theHour + ":" + theMinute + ":" + theSecond;

		//End Function
	}

	public static String  BusObjDateTime(Calendar theDate){
		String theDay;
		String theMonth;
		String theYear;
		String theHour;
		String theMinute;
		String theSecond;

		//new Date(DateFormat.getDateInstance().parse(theDate).getTime());

		theDay = String.valueOf(theDate.get(Calendar.DAY_OF_MONTH));
		theMonth = String.valueOf(theDate.get(Calendar.MONTH));
		theYear = String.valueOf(theDate.get(Calendar.YEAR));
		theHour = String.valueOf(theDate.get(Calendar.HOUR_OF_DAY));
		theMinute = String.valueOf(theDate.get(Calendar.MINUTE));
		theSecond = String.valueOf(theDate.get(Calendar.SECOND));

		if (theDay.length() == 1) {
			theDay = "0" + theDay;
		}

		if (theMonth.length() == 1) {
			theMonth = "0" + theMonth;
		}

		return theDay + "." + theMonth + "." + theYear + "." + theHour + "." + theMinute + "." + theSecond;
		//End Function
	}

	public static  void sendmail(String subject, String body, String From, String To)
	{
		String strBuild  = "";
		try 
		{
			//SmtpClient client = new SmtpClient("smtp5b.notes.lexmark.com");  //'this worked in VB.Net
			//SmtpClient client = new SmtpClient("botrack-xp2.na.ds.lexmark.com");
			//SmtpClient client = new SmtpClient("smtp5b.notes.lexmark.com");  //find Sam's other mail server to try

			//This is the non-Lotus Notes smtp server: Oct 10,2014
			SmtpClient client = new SmtpClient("mail.lexmark.com"); 
			//String to = "bishopp@lexmark.com";
			//String from = "bishopp@lexmark.com";
			client.from(From);
			client.to(To);
			client.startMessage();
			PrintStream message = client.startMessage(); //throws exception
			message.println("To: " + To);
			message.println("Subject:  Sending email from Java!");
			message.println("This was sent from a new java app!");
			message.println();
			message.println("Cool beans! :-)");
			message.println();     
			message.println();
			/*client.openServer(arg0, arg1);
			client.sendServer(arg0);*/
			client.closeServer();
		}

		catch (IOException ex)
		{
			System.out.println(ex.getMessage() + " " + ex.getStackTrace());
			ex.printStackTrace();

		}
	}

	public static int getUniverseID (IEnterpriseSession objSession, String si_cuid)
	{
		//Precondition:  the si_cuid of a Universe is known, but not the si_id
		//Post-condition:  the si_id is now known for a Universe of a given si_cuid

		int si_id = 0;
		IInfoObjects colInfoObjects;
		IInfoObject objInfoObject;
		String strInfoSQL = "";

		try {
			//Don't need to relogon because the Enterprise session is being passed.
			IInfoStore iStore = (IInfoStore) objSession.getService("InfoStore");

			strInfoSQL = "Select * From CI_INFOOBJECTS Where SI_CUID =" + si_cuid;
			colInfoObjects = (IInfoObjects)iStore.query(strInfoSQL);
			int colCount = colInfoObjects.size();
			if (colCount > 0) {
				objInfoObject = (IInfoObject) colInfoObjects.get(0);
				si_id = objInfoObject.getID();
				//return si_id;

			}
		}
		catch (Exception ex) {

		}
		return si_id;

	}


	public static String BuildCookieTrail(IEnterpriseSession objSession, int si_id, String cookieTrail)
	{		
		//Recursively builds the folder path of the folder the report resides in; different from BuildUniversePath
		//only in that it queries a different CMS view.
		String strBuild = "";
		String thePath = "";
		IInfoObjects colInfoObjects;
		IInfoObject objInfoObject;
		IInfoStore objInfoStore;         
		String strInfoSQL; 

		try
		{  
			//Don't need to relogon because the Enterprise session is being passed.
			IInfoStore iStore = (IInfoStore) objSession.getService("InfoStore");

			strInfoSQL = "Select * From CI_INFOOBJECTS Where SI_ID =" + si_id;
			colInfoObjects = (IInfoObjects)iStore.query(strInfoSQL);
			int colCount = colInfoObjects.size();

			//System.out.println("count: " + colInfoObjects.getResultSize());
			System.out.println("Helper.BuildCookieTrail line 532 Count: " + colCount);

			//for objInfoObject In colInfoObjects {


			for(int i =0; i < colCount; i++) {

				objInfoObject = (IInfoObject)colInfoObjects.get(i);
				if (objInfoObject.properties().getProperty(CePropertyID.SI_PARENTID).getValue().toString() != null) {
					cookieTrail = "\\" + objInfoObject.getTitle() + cookieTrail;
					System.out.println("1-Cookie trail so far: " + cookieTrail);
					//strBuild = BuildCookieTrail(objSession, objInfoObject.properties().getProperty(CePropertyID.SI_PARENTID).getValue(), cookieTrail);
					//strBuild = BuildCookieTrail(objSession, (int)objInfoObject.properties().getProperty("SI_PARENTID").getValue(), cookieTrail);
					strBuild = BuildCookieTrail(objSession, objInfoObject.getParentID(), cookieTrail);
					return strBuild;
				}
				else {
					strBuild = "\\" + objInfoObject.properties().getProperty(CePropertyID.SI_NAME).getValue().toString() + cookieTrail;
					System.out.println("2-Cookie trail so far: " + cookieTrail);
					colInfoObjects = null;
					objInfoObject = null;
					objInfoStore = null;

					//return strBuild;

				}
				//System.out.println("From BuildCookieTrail fn: " + strBuild);
				//thePath = strBuild;
				System.out.println("From BuildCookieTrail fn: " + cookieTrail);

				//cookieTrail = "";


			}

			//return cookieTrail; //returns the wrong value, which I expected

		} 

		catch (Exception ex)
		{
			throw new RuntimeException(ex);
		} 

		//colInfoObjects = null;
		//objInfoObject = null;
		//objInfoStore = null;

		return strBuild;
		// //return thePath;
		// return cookieTrail;

	}  //End Function


	public static String BuildUniversePath(IEnterpriseSession objSession, int si_id, String cookieTrail)
	{		
		//Recursively builds the folder path of the folder the Universe resides in; different from BuildCookieTrail
		//only in that it queries a different CMS view.

		String strBuild = "";
		String thePath = "";
		IInfoObjects colInfoObjects;
		IInfoObject objInfoObject;
		IInfoStore objInfoStore;         
		String strInfoSQL; 

		try
		{  
			//Don't need to relogon because the Enterprise session is being passed.
			IInfoStore iStore = (IInfoStore) objSession.getService("InfoStore");

			strInfoSQL = "Select * From CI_APPOBJECTS Where SI_ID =" + si_id;
			colInfoObjects = (IInfoObjects)iStore.query(strInfoSQL);
			int colCount = colInfoObjects.size();

			//System.out.println("count: " + colInfoObjects.getResultSize());
			System.out.println("Helper.BuildUniversePath line 609 Count: " + colCount);

			//for objInfoObject In colInfoObjects {


			for(int i =0; i < colCount; i++) {

				objInfoObject = (IInfoObject)colInfoObjects.get(i);
				//if (objInfoObject.properties().getProperty(CePropertyID.SI_PARENTID).getValue().toString() != null) {
				if (! objInfoObject.properties().getProperty(CePropertyID.SI_PARENTID).getValue().equals(95)) {
					//95 is the root id and even the root has a parent id of 95, so the recursion went on till the string was null without restricting the
					//recursion this way.
					System.out.println("Current parent id:  " + objInfoObject.properties().getProperty(CePropertyID.SI_PARENTID).getValue().toString());
					cookieTrail = "\\" + objInfoObject.getTitle() + cookieTrail;
					System.out.println("1-Cookie trail so far: " + cookieTrail);
					//strBuild = BuildCookieTrail(objSession, objInfoObject.properties().getProperty(CePropertyID.SI_PARENTID).getValue(), cookieTrail);
					//strBuild = BuildCookieTrail(objSession, (int)objInfoObject.properties().getProperty("SI_PARENTID").getValue(), cookieTrail);
					strBuild = BuildUniversePath(objSession, objInfoObject.getParentID(), cookieTrail);
					//return strBuild;
				}
				else {
					strBuild = "\\" + objInfoObject.properties().getProperty(CePropertyID.SI_NAME).getValue().toString() + cookieTrail;
					System.out.println("2-Cookie trail so far: " + cookieTrail);
					colInfoObjects = null;
					objInfoObject = null;
					objInfoStore = null;

					//return strBuild;

				}
				//System.out.println("From BuildCookieTrail fn: " + strBuild);
				//thePath = strBuild;
				System.out.println("From BuildUniversePath fn: " + cookieTrail);

				//cookieTrail = "";
				return strBuild;

			}

			//return cookieTrail; //returns the wrong value, which I expected

		} 

		catch (Exception ex)
		{
			throw new RuntimeException(ex);
		} 

		//colInfoObjects = null;
		//objInfoObject = null;
		//objInfoStore = null;
		System.out.println("Cookie trail from sub function: " + cookieTrail);
		return strBuild;
		// //return thePath;
		// return cookieTrail;

	}  //End Function


	public static String getFolderPath(IEnterpriseSession objSession, int si_id, String cookieTrail)
	{		

		String strBuild = "";
		String thePath = "";
		IInfoObjects colInfoObjects;
		IInfoObject objInfoObject;
		IInfoStore objInfoStore;         
		String strInfoSQL; 

		try
		{  
			//Don't need to relogon because the Enterprise session is being passed.
			IInfoStore iStore = (IInfoStore) objSession.getService("InfoStore");

			strInfoSQL = "Select * From CI_INFOOBJECTS Where SI_ID =" + si_id;
			colInfoObjects = (IInfoObjects)iStore.query(strInfoSQL);
			int colCount = colInfoObjects.size();

			//System.out.println("count: " + colInfoObjects.getResultSize());
			System.out.println("Helper.getFolderPath line 692 Count: " + colCount);

			if (colCount > 0) {

				objInfoObject = (IInfoObject)colInfoObjects.get(0);
				if (objInfoObject.properties().getProperty(CePropertyID.SI_PARENTID).getValue().toString() == null) {
					//strBuild = "\\" + objInfoObject.properties().getProperty(CePropertyID.SI_NAME).getValue().toString() + cookieTrail;
					thePath = "\\" + objInfoObject.properties().getProperty(CePropertyID.SI_NAME).getValue().toString() + cookieTrail;
					System.out.println("2-Cookie trail so far: " + cookieTrail);
					colInfoObjects = null;
					objInfoObject = null;
					objInfoStore = null;
					//return cookieTrail;
					//return thePath;
				}
				else {					

					cookieTrail = "\\" + objInfoObject.getTitle() + cookieTrail;
					System.out.println("1-Cookie trail so far: " + cookieTrail);
					strBuild = getFolderPath(objSession, objInfoObject.getParentID(), cookieTrail);
					//return strBuild;

				}
			}
			System.out.println("From BuildCookieTrail fn strBuild: " + strBuild);
			//thePath = strBuild;
			System.out.println("From BuildCookieTrail fn cookieTrail: " + cookieTrail);

			//cookieTrail = "";

			//return cookieTrail; //returns the wrong value, which I expected
			//return strBuild;
			return thePath;
		} 

		catch (Exception ex)
		{
			throw new RuntimeException(ex);
		} 


	}  //End Function

	public static int getSI_ID(IEnterpriseSession objSession, String cuid)
	{		

		int si_id = 0;
		IInfoObjects colInfoObjects;
		IInfoObject objInfoObject;
		IInfoStore objInfoStore;         
		String strInfoSQL; 

		try
		{  
			//Don't need to relogon because the Enterprise session is being passed.
			IInfoStore iStore = (IInfoStore) objSession.getService("InfoStore");

			strInfoSQL = "Select * From CI_INFOOBJECTS Where SI_CUID =" + cuid;
			colInfoObjects = (IInfoObjects)iStore.query(strInfoSQL);
			int colCount = colInfoObjects.size();

			//System.out.println("count: " + colInfoObjects.getResultSize());
			System.out.println("Helper.getSI_ID line 750 Count: " + colCount);

			if (colCount > 0) {

				objInfoObject = (IInfoObject)colInfoObjects.get(0);
				si_id = objInfoObject.getID();

			}
			System.out.println("The si_id for cuid, " + cuid + " is " + si_id);
			return si_id;
		} 

		catch (Exception ex)
		{
			throw new RuntimeException(ex);
		} 


	}  //End Function

	public String getFolderPath1(IEnterpriseSession objSession, int si_id, String arg)
	{
		//arg is the cookie trail

		String tmp = null;

		String strBuild = "";
		String thePath = "";
		IInfoObjects colInfoObjects;
		IInfoObject objInfoObject;
		IInfoStore objInfoStore;         
		String strInfoSQL;


		try{
			//Don't need to relogon because the Enterprise session is being passed.
			IInfoStore iStore = (IInfoStore) objSession.getService("InfoStore");

			strInfoSQL = "Select * From CI_INFOOBJECTS Where SI_ID =" + si_id;
			colInfoObjects = (IInfoObjects)iStore.query(strInfoSQL);
			int colCount = colInfoObjects.size();
			System.out.println("Helper.BuildCookieTrail line 484 Count: " + colCount);
			objInfoObject = (IInfoObject)colInfoObjects.get(0);

			//if (objInfoObject.properties().getProperty(CePropertyID.SI_PARENTID).getValue().toString() == null)
			if (colCount == 0){

				return arg;
			}

			else
			{

				String folderPath = "\\" + objInfoObject.getTitle() + arg;	
				System.out.println("1-Cookie trail so far: " + folderPath);
				tmp = getFolderPath1(objSession, objInfoObject.getParentID(),folderPath);
				return tmp;  //you have to have this or the string returned from the function will be empty

			}
		}

		catch(Exception ex) {

		}

		return arg;  

	}

	public static String getMyString(String myString){
		//just testing a function
		return "abcdefg";
	}

	public static void runMSSQLInsertQuery(String msSQLInsertString){
		try
		{
			//check the SQL statement
			System.out.println(msSQLInsertString);
			//Use this:
			// Load the SQLServerDriver class, build the connection string, and get a connection 
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver"); 
			String connectionUrl = "jdbc:sqlserver://DASHWBODB012;" + 
					"database=EDR1DEN1;" + 
					"user=BOTRACK;" + 
					"password=********"; 
			Connection conn = DriverManager.getConnection(connectionUrl); 

			//or this:
		/*	String driver = "mssql.jdbc.driver.SQLDriver";
			String url = "jdbc:sqlserver://DASHWBODB012;databaseName=EDR1DEN1;integratedSecurity=true;";
			String username = "BOTRACK";
			String password = "********";		
			ConnectMSSQLServer connServer = new ConnectMSSQLServer();
			connServer.dbConnect("jdbc:sqlserver://DASHWBODB012", "BOTRACK", "********");
			System.out.println(msSQLInsertString);

			Class.forName(driver);		//loads the driver
			Connection conn = DriverManager.getConnection(url,username,password);	*/

			Statement stmt = conn.createStatement() ;
			stmt.execute(msSQLInsertString);
			//stmt.executeQuery(msSQLInsertString);			

			conn.close();
		}          
		catch (Exception ex)
		{
			System.out.println(ex.getMessage() + " " + ex.getStackTrace() + " runMSSQLInsertQuery line 845");
			//sendmail("Connection to EDR1DEN1 timed out ",  ex.getMessage() + " " + ex.getStackTrace(), "bishopp@lexmark.com", "bishopp@lexmark.com");
		}
	}

	public static void runOracleInsertQuery(String oraInsertString)
	{    	
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@dlxkrbodb1.lex.lexmark.com:1750:edr1den1";
		String username = "botrack";
		String password = "********";
		System.out.println(oraInsertString);
		try
		{				
			Class.forName(driver);		//loads the driver
			Connection conn = DriverManager.getConnection(url,username,password);	

			Statement stmt = conn.createStatement() ; 
			stmt.executeQuery(oraInsertString);			

			//OracleCommand oraInserter;
			//OracleConnection  devConn = new OracleConnection();
			//devConn.getConnectionAttributes() = oraConnStr;
			//devConn.ConnectionString = oraConnStr;
			//oraInserter = new OracleCommand();
			// oraInserter.CommandType = Data.CommandType.Text;
			// oraInserter.Connection = devConn;
			//conn = null;
			conn.close();
		}          
		catch (Exception ex)
		{
			System.out.println(ex.getMessage() + " " + ex.getStackTrace() + " runOracleInsertQuery line 632");
			//sendmail("Connection to EDR1DEN1 timed out ",  ex.getMessage() + " " + ex.getStackTrace(), "bishopp@lexmark.com", "bishopp@lexmark.com");
		}

	}  


	public static String formatDateBO(Calendar theDate){


		int theDay;
		int theMonth ;
		int theYear;
		int theHour;
		int theMinute;
		int theSecond;

		theDay = theDate.get(Calendar.DAY_OF_MONTH);
		theMonth = theDate.get(Calendar.MONTH);
		theMonth +=1;
		theYear = theDate.get(Calendar.YEAR);
		theHour = theDate.get(Calendar.HOUR);
		theHour = theDate.get(Calendar.HOUR_OF_DAY);
		theMinute = theDate.get(Calendar.MINUTE);
		theSecond = theDate.get(Calendar.SECOND);

		String strDay = Integer.toString(theDay);
		String strMonth = Integer.toString(theMonth);
		String strYear = Integer.toString(theYear);
		String strHour = Integer.toString(theHour);
		System.out.println("the hour:  " + strHour);
		String strMinute = Integer.toString(theMinute);
		String strSecond = Integer.toString(theSecond); 



		if (strDay.length() == 1) {
			strDay = "0" + strDay;
		}


		if (strMonth.length() == 1) {
			strMonth = "0" + strMonth;
		}

		if (strHour.length() == 1) {
			strHour = "0" + strHour;
		}

		if (strMinute.length() == 1) {
			strMinute = "0" + strMinute;
		}

		if (strSecond.length() == 1) {
			strSecond = "0" + strSecond;
		}

		return strYear + "." + strMonth + "." + strDay + "." + strHour + "." + strMinute + "." + strSecond;
	}

	public static IDestinationPlugin getDestinationPlugin(IInfoStore infoStore, String kind) throws SDKException {
		//This is only for SMTP destinations
		
		String fileExt = getFileExtension(kind);
		String mimeType = getMimeType(kind);	

		//Retrieve the SMTP Destination plugin from the InfoStore
		//this should be cast as an IDestinationPlugin *DON'T FORGET THE get(0) AT THE END**
		//Don't call this function unless you know it's destination is smtp.
		IDestinationPlugin destPlugin = (IDestinationPlugin)infoStore.query("SELECT TOP 1 * " +
				"FROM CI_SYSTEMOBJECTS " + 
				"WHERE SI_NAME='CrystalEnterprise.Smtp'").get(0);

		//Retrieve the Scheduling Options and cast it as ISMTPOptions
		//This interface is the one which allows us to set all of the required SMTP properties
		ISMTPOptions smtpOptions = (ISMTPOptions) destPlugin.getScheduleOptions();
		smtpOptions.setDomainName("domainName");
		smtpOptions.setServerName("serverName");
		smtpOptions.setPort(25);
		smtpOptions.setSMTPUserName("smtpUsername");
		smtpOptions.setSMTPPassword("smtpPassword");
		smtpOptions.setSMTPAuthenticationType(ISMTPOptions.CeSMTPAuthentication.NONE);
		smtpOptions.setSubject("subject");
		smtpOptions.setMessage("message_body");
		smtpOptions.setSenderAddress("sender@sender.com");

		//Retrieve the list of SMTP recipients. 
		List recipients = smtpOptions.getToAddresses();
		//recipients.add("recipient1@recipient.com"); 4/2/2009:  I commented out since I don't need this.

		//Retrieve the IAttachments Interface then use the add() method to add the mimetype and the 
		//embedName of the attachment.
		IAttachments attachments = smtpOptions.getAttachments();
		attachments.add(mimeType, "MyReport" + fileExt); 


		return destPlugin;     

	}

	public static String getDistributionList(String env, int si_id) {
		String qry="SELECT * FROM CI_INFOOBJECTS WHERE si_id=" + si_id + " AND si_instance=1";
		String smtpQry= "Select * From CI_SYSTEMOBJECTS Where SI_PARENTID=29 and SI_NAME='CrystalEnterprise.SMTP'";
		String addressString="";
		try
		{
			ISessionMgr  sm = CrystalEnterprise.getSessionMgr();
			System.out.println("session mgr started");

			//logon to Enterprise
			IEnterpriseSession es = sm.logon("tracker", "libambini*8", env, "secEnterprise");
			IInfoStore iStore = (IInfoStore) es.getService("InfoStore");
			System.out.println("login succeeded");

			//get report object
			IInfoObjects repInfoObjects = iStore.query(qry);
			System.out.println("query running");

			System.out.println(repInfoObjects.getResultSize());

			for(Object o: repInfoObjects) {
				IInfoObject instance = (IInfoObject) o;            


				//for(Class c: repInfoObjects.get(0).getClass().getInterfaces()) {
				//	System.out.println(c.getName());
				//}

				//cast to IReport to get at properties --not right.  
				//Can't cast a pdf to a report object
				//A report doesn't necessarily know the email list.
				//And this is an unnecessary step.

				//IReport rpt = (IReport)repInfoObjects.get(0);
				//IInfoObject instance = (IInfoObject) repInfoObjects.get(0);

				System.out.println("getting report object");
				//get plugin interface
				ISchedulingInfo schedInfo = instance.getSchedulingInfo();
				System.out.println("getting scheduling info");


				//get the destination from the scheduling info
				IDestination ceDest =(IDestination)schedInfo.getDestinations().get(0);
				System.out.println("getting destination");

				//query for destination object
				IInfoObjects smtpInfoObjects = iStore.query(smtpQry);
				System.out.println("getting smtp objects");

				//cast destination infoobject to smtp object
				ISMTP smtpObj =(ISMTP)smtpInfoObjects.get(0);

				try{
					//copy plugin properties to the smtp object to get all the properties
					ceDest.copyToPlugin(smtpObj);		


					//retrieve smtpoptions
					ISMTPOptions smtpOpts = (ISMTPOptions)smtpObj.getScheduleOptions();

					/*
				//retrieve address list
				Iterator addresses = smtpOpts.getToAddresses().iterator();

				//iterate through list and display
				String addressString="";
				for (Iterator iterator = smtpOpts.getToAddresses().iterator(); iterator.hasNext(); ) {
				//while(addresses.hasNext())
				//{
					addressString += addresses.next() + ";";
				}
				System.out.println(addressString);
					 */


					for (Object o1 : smtpOpts.getToAddresses()) { 
						addressString += o1.toString() + ";";
					}

					for (Object o2 : smtpOpts.getCCAddresses()) {
						addressString += o2.toString() + ";";
					}

					System.out.println("emails: " + addressString);
				}
				catch(Exception ex)
				{
					System.out.println("Exception on line 1078 in Helper class.");
				}

			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			System.out.println(ex.getMessage() + " " + ex.getStackTrace());
		}
		/**
		 * @param EnterpriseEnv
		 */
		System.out.println("Line 613 Helper class");
		return addressString;
	}

	public List getSmtpAddresses(IInfoStore infoStore, String kind) throws SDKException {

		//String fileExt = getFileExtension(kind); //Since this variable is never read, I'll comment it out.
		//String mimeType = getMimeType(kind);	//Since this variable is never read, I'll comment it out.

		//Retrieve the SMTP Destination plugin from the InfoStore
		//this should be cast as an IDestinationPlugin *DON'T FORGET THE get(0) AT THE END**
		IDestinationPlugin destPlugin = (IDestinationPlugin)infoStore.query("SELECT TOP 1 * " +
				"FROM CI_SYSTEMOBJECTS " + 
				"WHERE SI_NAME='CrystalEnterprise.Smtp'").get(0);

		//Retrieve the Scheduling Options and cast it as ISMTPOptions
		//This interface is the one which allows us to set all of the required SMTP properties
		ISMTPOptions smtpOptions = (ISMTPOptions) destPlugin.getScheduleOptions();
		//smtpOptions.setDomainName("domainName");
		//smtpOptions.setServerName("serverName");
		//smtpOptions.setPort(25);
		//smtpOptions.setSMTPUserName("smtpUsername");
		//smtpOptions.setSMTPPassword("smtpPassword");
		//smtpOptions.setSMTPAuthenticationType(ISMTPOptions.CeSMTPAuthentication.NONE);
		//smtpOptions.setSubject("subject");
		//smtpOptions.setMessage("message_body");
		// smtpOptions.setSenderAddress("sender@sender.com");

		//Retrieve the list of SMTP recipients. 
		List recipients = smtpOptions.getToAddresses();
		//recipients.add("recipient1@recipient.com"); 4/2/2009:  I commented out since I don't need this.

		//Retrieve the IAttachments Interface then use the add() method to add the mimetype and the 
		//embedName of the attachment.
		//IAttachments attachments = smtpOptions.getAttachments();
		//attachments.add(mimeType, "MyReport" + fileExt); 


		return recipients;     

	}

	private static String getFileExtension(String kind) {
		//'FullClient', 'Txt', 'Excel', 'Webi', 'Analysis', 'Pdf', 'Word', 'Rtf', 'CrystalReport', 'Agnostic'
		String fileExt = "";
		System.out.println("Kind is: " + kind);

		if (kind.equals("Analysis")) {
			fileExt = ".car";
		}
		else if (kind.equals("CrystalReport")) {
			fileExt = ".rpt";
		}
		else if (kind.equals("Excel")) {
			fileExt = ".xls";	
		}
		else if (kind.equals("Pdf")) {
			fileExt = ".pdf";	
		}
		else if (kind.equals("Ppt")) {
			fileExt = ".ppt";	
		}
		else if (kind.equals("Rtf")) {
			fileExt = ".rtf";	
		}
		else if (kind.equals("Txt")) {
			fileExt = ".txt";	
		}
		else if (kind.equals("Word")) {
			fileExt = ".doc";	



			/*if (kind.equals(CeKind.ANALYTIC)) {
			fileExt = ".car";
		}
		else if (kind.equals(CeKind.CRYSTAL_REPORT)) {
			fileExt = ".rpt";
		}
		else if (kind.equals(CeKind.EXCEL)) {
			fileExt = ".xls";	
		}
		else if (kind.equals(CeKind.PDF)) {
			fileExt = ".pdf";	
		}
		else if (kind.equals(CeKind.POWERPOINT)) {
			fileExt = ".ppt";	
		}
		else if (kind.equals(CeKind.RTF)) {
			fileExt = ".rtf";	
		}
		else if (kind.equals(CeKind.TEXT)) {
			fileExt = ".txt";	
		}
		else if (kind.equals(CeKind.WORD)) {
			fileExt = ".doc";	
		}*/
		}
		return fileExt;

	}


	private static String getMimeType(String kind) {
		//'FullClient', 'Txt', 'Excel', 'Webi', 'Analysis', 'Pdf', 'Word', 'Rtf', 'CrystalReport', 'Agnostic'
		String mimeType = "";

		if (kind.equals("Analysis")) {
			mimeType = "application/analytic";
		}
		else if (kind.equals("CrystalReport")) {
			mimeType = "application/report";
		}
		else if (kind.equals("Excel")) {
			mimeType = "application/vnd.ms-excel";	
		}
		else if (kind.equals("Pdf")) {
			mimeType = "application/pdf";	
		}
		else if (kind.equals("PowerPoint")) {
			mimeType = "application/mspowerpoint";	
		}
		else if (kind.equals("Rtf")) {
			mimeType = "application/msword";	
		}
		else if (kind.equals("txt")) {
			mimeType = "text/plain";	
		}
		else if (kind.equals("Word")) {
			mimeType = "application/msword";	
		}

		/*if (kind.equals(CeKind.ANALYTIC)) {
			mimeType = "application/analytic";
		}
		else if (kind.equals(CeKind.CRYSTAL_REPORT)) {
			mimeType = "application/report";
		}
		else if (kind.equals(CeKind.EXCEL)) {
			mimeType = "application/vnd.ms-excel";	
		}
		else if (kind.equals(CeKind.PDF)) {
			mimeType = "application/pdf";	
		}
		else if (kind.equals(CeKind.POWERPOINT)) {
			mimeType = "application/mspowerpoint";	
		}
		else if (kind.equals(CeKind.RTF)) {
			mimeType = "application/msword";	
		}
		else if (kind.equals(CeKind.TEXT)) {
			mimeType = "text/plain";	
		}
		else if (kind.equals(CeKind.WORD)) {
			mimeType = "application/msword";	
		}*/

		return mimeType;

	}

	public static String msSQLOnTheHour(Calendar theDate ) {
		int theDay = 0;
		int theMonth = 0;
		int theYear = 0;
		int theHour = 0;
		int theMinute = 0;
		int theSecond = 0;
		String strDay = Integer.toString(theDay);
		String strMonth = Integer.toString(theMonth);
		String strYear = Integer.toString(theYear);
		String strHour = Integer.toString(theHour);
		String strMin = Integer.toString(theMinute);
		String strSec = Integer.toString(theSecond); 

		//theDay = CStr(DatePart(DateInterval.Day, theDate))
		//theMonth = CStr(DatePart(DateInterval.Month, theDate))
		//theYear = CStr(DatePart(DateInterval.Year, theDate))
		//theHour = CStr(DatePart(DateInterval.Hour, theDate))
		//theMin = CStr(DatePart(DateInterval.Minute, theDate))
		//theSec = CStr(DatePart(DateInterval.Second, theDate))

		theDay = theDate.get(Calendar.DAY_OF_MONTH);
		theMonth = theDate.get(Calendar.MONTH);
		theMonth +=1;
		theYear = theDate.get(Calendar.YEAR);
		theHour = theDate.get(Calendar.HOUR);
		theMinute = theDate.get(Calendar.MINUTE);
		theSecond = theDate.get(Calendar.SECOND);

		strDay = Integer.toString(theDay);
		strMonth = Integer.toString(theMonth);
		strYear = Integer.toString(theYear);
		strHour = Integer.toString(theHour);
		strMin = Integer.toString(theMinute);
		strSec = Integer.toString(theSecond); 

		if (strDay.length() == 1) {
			strDay = "0" + strDay;
		}

		if (strMonth.length() == 1) {
			strMonth = "0" + strMonth;
		}

		if (strHour.length() == 1) {
			strHour = "0" + strHour;
		}


		strMin = "00";


		strSec = "00";

		System.out.println("MS SQL date: " + theYear + "-" + strMonth + "-" + strDay + " " + theHour + ":" + strMin + ":" + strSec);
		return theYear + "/" + strMonth + "/" + strDay + " " + theHour + ":" + strMin + ":" + strSec;
	}

	public static String oracleOnTheHour(Calendar theDate ) {
		int theDay = 0;
		int theMonth = 0;
		int theYear = 0;
		int theHour = 0;
		int theMinute = 0;
		int theSecond = 0;
		String strDay = Integer.toString(theDay);
		String strMonth = Integer.toString(theMonth);
		String strYear = Integer.toString(theYear);
		String strHour = Integer.toString(theHour);
		String strMin = Integer.toString(theMinute);
		String strSec = Integer.toString(theSecond); 

		//theDay = CStr(DatePart(DateInterval.Day, theDate))
		//theMonth = CStr(DatePart(DateInterval.Month, theDate))
		//theYear = CStr(DatePart(DateInterval.Year, theDate))
		//theHour = CStr(DatePart(DateInterval.Hour, theDate))
		//theMin = CStr(DatePart(DateInterval.Minute, theDate))
		//theSec = CStr(DatePart(DateInterval.Second, theDate))

		theDay = theDate.get(Calendar.DAY_OF_MONTH);
		theMonth = theDate.get(Calendar.MONTH);
		theMonth +=1;
		theYear = theDate.get(Calendar.YEAR);
		theHour = theDate.get(Calendar.HOUR);
		theMinute = theDate.get(Calendar.MINUTE);
		theSecond = theDate.get(Calendar.SECOND);

		strDay = Integer.toString(theDay);
		strMonth = Integer.toString(theMonth);
		strYear = Integer.toString(theYear);
		strHour = Integer.toString(theHour);
		strMin = Integer.toString(theMinute);
		strSec = Integer.toString(theSecond); 

		if (strDay.length() == 1) {
			strDay = "0" + strDay;
		}

		if (strMonth.length() == 1) {
			strMonth = "0" + strMonth;
		}

		if (strHour.length() == 1) {
			strHour = "0" + strHour;
		}


		strMin = "00";


		strSec = "00";

		System.out.println("Oracle date: " + strDay + "-" + strMonth + "-" + theYear + " " + theHour + ":" + strMin + ":" + strSec);
		return strDay + "-" + strMonth + "-" + theYear + " " + theHour + ":" + strMin + ":" + strSec;
	}//End Function

	public static void getParameters (IInfoObject objInfoObject){
		//This works only if the object is a report or an instance of si_kind = "CrystalReport';
		IReport oRpt;
		IReportParameter oParam;
		List lParams;
		String datatype;			
		String cefromValue;			
		String cetoValue;			
		String promptName;			
		int x;
		String promptValue;			
		String paramValueString = "";
		int paramCount;
		IReportProcessingInfo oProcessInfo;

		try {
			oRpt = (IReport)objInfoObject;
			lParams = oRpt.getReportParameters();
			paramCount = lParams.size();

			if (paramCount > 0) {
				x = 1;
				for (int i=0; i < paramCount; i++) {
					oParam = (IReportParameter)oRpt.getReportParameters().get(x);
					promptName = oParam.getParameterName();

					if (oParam.getCurrentValues().size() > 0){

						switch (oParam.getValueType()){
						case 2: datatype = "Boolean";
						case 1: datatype= "Currency";
						case 3: datatype= "Date";
						case 5: datatype = "DateTime";
						case 0: datatype = "Number";
						case 6: datatype = "String";
						case 4: datatype = "Time";
						default: datatype = "unknown";
						}
						if (oParam.isRangeValueSupported()) {
							cefromValue = (String)oParam.getCurrentValues().addRangeValue().getFromValue().getValue();
							cetoValue = (String)oParam.getCurrentValues().addRangeValue().getToValue().getValue();
							paramValueString += cefromValue + " " + cetoValue + "; ";

						}
						else {
							promptValue = (String)oParam.getCurrentValues().addSingleValue().getValue();
							paramValueString += promptValue + "; ";
						}
					}
					x +=1;
					//return paramValueString;
				}
			}
		}

		catch  (SDKException ex){
			System.out.println(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getStackTrace() + " getParamValuesString - line 1158");
		}


	}



	public static String getParamValueString(IInfoObject objInfoObject) {
		//'The idea here is to create a semi-colon separated list of parameter values to help identify duplicate recurring jobs.
		//'The semi-colon is used because in BOXI InfoView, parameter values are separated by semi-colons in the report history

		int x;
		String promptValue;	
		String promptName;
		String paramValueString = "";
		int paramCount;
		//IReportProcessingInfo oProcessInfo;//this type of variable doesn't work.
		IProperties oProcessInfo;


		try{
			System.out.println("report title is:  " + objInfoObject.getTitle().toString());
			//oProcessInfo = (IReportProcessingInfo)objInfoObject.getProcessingInfo();//throws error:
			//com.crystaldecisions.sdk.occa.infostore.internal.ProcessingInfo cannot be cast to com.crystaldecisions.sdk.plugin.desktop.common.IReportProcessingInfo com.crystaldecisions.sdk.occa.infostore.internal.ProcessingInfo cannot be cast to com.crystaldecisions.sdk.plugin.desktop.common.IReportProcessingInfo [Ljava.lang.StackTraceElement;@14ce5eb getParamValuesString - line 1158
			//The reason this doesn't work on all instances is because it must be si_kind='CrystalReport'.

			// First get the Processing Info properties for the InfoObject.
			oProcessInfo = (IProperties) objInfoObject.getProcessingInfo().properties();
			//Seems to throw an error if it is a recurring job but works if it is a success or failure.

			// Make sure that there is processing info set for the InfoObject.
			if (oProcessInfo != null) {
				//oParam = (IReportParameter)oProcessInfo.getReportParameters(); //this yields a list
				try{
					// Get the SI_PROMPTS property bag from the InfoObject's Processing Info - got this from SAP
					IProperties siPrompts = (IProperties) oProcessInfo.getProperty("SI_PROMPTS").getValue();
					//Error:  properties() and getPRoperty() undefined for IProcessingInfo

					//This doesn't work either
					//IProperties siPrompts = (IProperties) objInfoObject.properties().getProperty("SI_PROMPTS").getValue();//throws error "null null [Ljava.lang.StackTraceElement;@7bacb getParamValuesString"


					//Try this:
					//IProperties siPrompts = (IProperties) objInfoObject.properties().getProperty("SI_PROMPTS");

					// Make sure that there are parameters.
					//if (siPrompts != null){
					// Get the number of prompts (the number of parameters)
					int numberPrompts = ((Integer)siPrompts.getProperty("SI_NUM_PROMPTS").getValue()).intValue(); //get null null
					//int numberPrompts = ((Integer)siPrompts.getProperty("SI_NUM_PROMPTS").getValue());  //also fails
					//IPrompts objPrompts = (IPrompts)objInfoObject.getProcessingInfo().properties().getProperty("SI_PROMPTS"); //this threw an error
					//com.crystaldecisions.sdk.properties.internal.SDKPropertyBag$SDKProperty cannot be cast to com.crystaldecisions.sdk.prompting.IPrompts com.crystaldecisions.sdk.properties.internal.SDKPropertyBag$SDKProperty cannot be cast to com.crystaldecisions.sdk.prompting.IPrompts [Ljava.lang.StackTraceElement;@16f2067 getParamValuesString - line 1088

					//nbrPrompts = objPrompts.size();
					//int numberPrompts = objPrompts.size();
					//int numberPrompts = siPrompts.size();

					// Make sure that there is at least one parameter.
					if (numberPrompts > 0)
					{
						// Start displaying a table with the parameter names and values.
						//System.out.println("<table>");

						// Loop through the parameters to get the parameter names and parameter values.
						for(int i=1;i<=numberPrompts;i++)
						{
							// Each prompt has the name SI_PROMPT + a digit starting from 1.  For example, if
							// there are 2 prompts, the first one would be called SI_PROMPT1 and the second
							// one would be called SI_PROMPT2.
							IProperties prompt = (IProperties)siPrompts.getProperty("SI_PROMPT"+i).getValue();

							// Get the parameter name.
							promptName = prompt.getProperty("SI_NAME").getValue().toString();

							// Start displaying a row.
							//out.println("<tr>");

							// First column in the row is the parameter name.
							//out.println("<td>Parameter Name = "+promptName+"</td><td><table>");
							// Get the current values property bag.
							IProperties currentValues = (IProperties)prompt.getProperty("SI_CURRENT_VALUES").getValue();

							// Get the number of values for this particular parameter.
							int numberValues = ((Integer)currentValues.getProperty("SI_NUM_VALUES").getValue()).intValue();

							// Loop through all of the values for this particular parameter.
							for(int j=1;j<=numberValues;j++)
							{
								// Print out the values.
								// Each value has the name SI_VALUE + a digit starting from 1.  For example, if
								// there are two values, then the first value would be called SI_VALUE1 and the
								// second value would be called SI_VALUE2.
								IProperties currentValue = (IProperties)currentValues.getProperty("SI_VALUE"+j).getValue();

								// If the size of the currentValue property bag is 3, then that means that the
								// parameter is a range parameter.
								//if(currentValue.size() == 3)
								String promptType = prompt.getProperty("SI_PROMPT_TYPE").getValue().toString();
								System.out.println("prompt type is " + promptType);
								if (promptType.equals("3"))
								{
									// Range Parameter: contains 3 properties: SI_MIN, SI_MAX, SI_OPTIONS
									//6/5/2012: Have a problem where a normal prompt is evaluating as a range parameter
									System.out.println(prompt.getProperty("SI_PROMPT_TYPE").getValue().toString());

									try {
										// Get the SI_MIN property bag.
										IProperties minRange = (IProperties)currentValue.getProperty("SI_MIN").getValue();
										//6/5/2012:  This does NOT work and this parameter IS a range parameter
										//but the property bag lacks this property, hence the error. Try instance ID 47637018, AM0109DX - Total Charges For Billed Assets By Date

										// Get the SI_DATA property of the SI_MIN property bag.
										String minValue = minRange.getProperty("SI_DATA").getValue().toString();

										// Get the SI_MAX property bag.
										IProperties maxRange = (IProperties)currentValue.getProperty("SI_MAX").getValue();

										// Get the SI_MAX property of the SI_MAX property bag.
										String maxValue = maxRange.getProperty("SI_DATA").getValue().toString();
										paramValueString += minValue  + "; " + maxValue + "; ";
										// Print out the Minimum and Maximum values for the parameter.
										//System.out.println("<tr><td>Range " + j + " Min Value " + minValue + " Max Value " + maxValue + "</td></tr>");

									}
									catch (Exception ex){
										System.out.println("Might not be a true range parameter and " + ex.getMessage() + " " + ex.getStackTrace());

										System.out.println(prompt.getProperty("SI_PROMPT_TYPE").getValue().toString());
										// For this situation we are only interested in the SI_DATA property.
										String paramValue = currentValue.getProperty("SI_DATA").getValue().toString();
										//out.println("<tr><td> Value " + j + " = " + paramValue + "</td></tr>");
										paramValueString += paramValue  + "; ";
									}
								}
								// If the size of the currentValue property bag is 4, then that means that the
								// parameter is a discete parameter.
								else //if (currentValue.size() == 4)
									if (promptType.equals("4"))
									{
										// Discrete Parameter: contains 4 properties: SI_SHOWDESCONLY, SI_OPTIONS,
										// SI_DESCRIPTION, SI_DATA 
										System.out.println(prompt.getProperty("SI_PROMPT_TYPE").getValue().toString());
										// For this situation we are only interested in the SI_DATA property.
										String paramValue = currentValue.getProperty("SI_DATA").getValue().toString();
										//out.println("<tr><td> Value " + j + " = " + paramValue + "</td></tr>");
										paramValueString += paramValue  + "; ";

									}
							}
						}
					}
					else
					{
						System.out.println("The number of prompts is 0 for the InfoObject.");
					}
				}
				catch (Exception ex) {
					System.out.println(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getStackTrace() + " getParamValuesString - line 1099");
				}
				//else
				//{
				//	// There is no SI_PROMPTS for the InfoObject.
				//	System.out.println ("The siPrompts is null for the InfoObject.");
				//}
			}
			else
			{
				// There is no processing info for the InfoObject.
				System.out.println ("The processing info is null for the InfoObject.");
			}
		}
		catch  (Exception ex){
			System.out.println(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getStackTrace() + " getParamValuesString - line 1095");
		}
		System.out.println("param value string is: " + paramValueString);
		return paramValueString;
	}

	public static String getWebiParamValueString(IInfoObject objInfoObject) {
		//'The idea here is to create a semi-colon separated list of parameter values to help identify duplicate recurring jobs.
		//'The semi-colon is used because in BOXI InfoView, parameter values are separated by semi-colons in the report history
		//This is for Webi reports only, even if the output is Excel or PDF, because prompts for Webi are stored
		//in a different property than for Crystal Reports.

		int x;
		String promptValue;	
		String promptName;
		String paramValueString = "";
		int paramCount;
		//IReportProcessingInfo oProcessInfo;//this type of variable doesn't work.
		IProperties oProcessInfo;

		try{
			System.out.println("report title is:  " + objInfoObject.getTitle().toString());
			System.out.println("report instance is:  " + objInfoObject.getID());

			// First get the Processing Info properties for the InfoObject.
			oProcessInfo = (IProperties) objInfoObject.getProcessingInfo().properties();
			//Seems to throw an error if it is a recurring job but works if it is a success or failure.

			// Make sure that there is processing info set for the InfoObject.
			if (oProcessInfo != null) {
				//oParam = (IReportParameter)oProcessInfo.getReportParameters(); //this yields a list
				try{
					// Get the SI_PROMPTS property bag from the InfoObject's Processing Info 
					IProperties siPrompts = (IProperties) oProcessInfo.getProperty("SI_WEBI_PROMPTS").getValue();

					// Make sure that there are parameters.
					// Get the number of prompts (the number of parameters)
					int numberPrompts = ((Integer)siPrompts.getProperty("SI_TOTAL").getValue()).intValue(); //get null null

					// Make sure that there is at least one parameter.
					if (numberPrompts > 0)
					{
						// Loop through the parameters to get the parameter names and parameter values.
						for(int i=1;i<=numberPrompts;i++)
						{
							// Each prompt has the name SI_PROMPT + a digit starting from 1.  For example, if
							// there are 2 prompts, the first one would be called SI_PROMPT1 and the second
							// one would be called SI_PROMPT2.
							IProperties prompt = (IProperties)siPrompts.getProperty(i).getValue();

							// Get the parameter name.
							promptName = prompt.getProperty("SI_NAME").getValue().toString();
							System.out.println("Prompt name is " + promptName);

							// Get the number of values for this particular parameter.
							IProperties siValues = (IProperties)prompt.getProperty("SI_VALUES").getValue();
							int numberValues = ((Integer)siValues.getProperty("SI_TOTAL").getValue()).intValue();
							System.out.println("Number of values is " + numberValues);
							// Loop through all of the values for this particular parameter.
							for(int j=1;j<=numberValues;j++)
							{
								// Print out the values.
								//IProperties paramValue = (IProperties)siValues.getProperty(j).getValue(); //get error
								//java.lang.String cannot be cast to com.crystaldecisions.sdk.properties.IProperties java.lang.String cannot be cast to com.crystaldecisions.sdk.properties.IProperties [Ljava.lang.StackTraceElement;@1e99db4 getParamValuesString - line 1095
								String paramValue = (String)siValues.getProperty(j).getValue();
								paramValueString += paramValue  + "; ";
							}
						}
					}
					else
					{
						System.out.println("The number of prompts is 0 for the InfoObject.");
					}
				}
				catch  (Exception ex){
					System.out.println(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getStackTrace() + " getParamValuesString - line 1095");
				}
			}
		}
		catch  (Exception ex){
			System.out.println(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getStackTrace() + " getParamValuesString - line 1095");
		}
		System.out.println("param value string is: " + paramValueString);
		return paramValueString;
	}


	public static String getParamValueString1(IInfoObject objInfoObject) {
		//'The idea here is to create a semi-colon separated list of parameter values to help identify duplicate recurring jobs.
		//'The semi-colon is used because in BOXI InfoView, parameter values are separated by semi-colons in the report history

		int x;
		String promptValue;	
		String promptName;
		String paramValueString = "";
		int paramCount;
		//IReportProcessingInfo oProcessInfo;//this type of variable doesn't work.
		IProperties oProcessInfo;


		try{
			System.out.println("report title is:  " + objInfoObject.getTitle().toString());
			//oProcessInfo = (IReportProcessingInfo)objInfoObject.getProcessingInfo();//throws error:
			//com.crystaldecisions.sdk.occa.infostore.internal.ProcessingInfo cannot be cast to com.crystaldecisions.sdk.plugin.desktop.common.IReportProcessingInfo com.crystaldecisions.sdk.occa.infostore.internal.ProcessingInfo cannot be cast to com.crystaldecisions.sdk.plugin.desktop.common.IReportProcessingInfo [Ljava.lang.StackTraceElement;@14ce5eb getParamValuesString - line 1158
			//The reason this doesn't work on all instances is because it must be si_kind='CrystalReport'.


			try{
				// Get the SI_PROMPTS property bag from the InfoObject's Processing Info - got this from SAP
				IProperties siPrompts = (IProperties) objInfoObject.getProcessingInfo().properties().getProperty("SI_PROMPTS").getValue();
				//Error:  properties() and getPRoperty() undefined for IProcessingInfo

				//This doesn't work either
				//IProperties siPrompts = (IProperties) objInfoObject.properties().getProperty("SI_PROMPTS").getValue();//throws error "null null [Ljava.lang.StackTraceElement;@7bacb getParamValuesString"


				//Try this:
				//IProperties siPrompts = (IProperties) objInfoObject.properties().getProperty("SI_PROMPTS");

				// Make sure that there are parameters.
				//if (siPrompts != null){
				// Get the number of prompts (the number of parameters)
				int numberPrompts = ((Integer)siPrompts.getProperty("SI_NUM_PROMPTS").getValue()).intValue(); //get null null
				//int numberPrompts = ((Integer)siPrompts.getProperty("SI_NUM_PROMPTS").getValue());  //also fails
				//IPrompts objPrompts = (IPrompts)objInfoObject.getProcessingInfo().properties().getProperty("SI_PROMPTS"); //this threw an error
				//com.crystaldecisions.sdk.properties.internal.SDKPropertyBag$SDKProperty cannot be cast to com.crystaldecisions.sdk.prompting.IPrompts com.crystaldecisions.sdk.properties.internal.SDKPropertyBag$SDKProperty cannot be cast to com.crystaldecisions.sdk.prompting.IPrompts [Ljava.lang.StackTraceElement;@16f2067 getParamValuesString - line 1088

				//nbrPrompts = objPrompts.size();
				//int numberPrompts = objPrompts.size();
				//int numberPrompts = siPrompts.size();

				// Make sure that there is at least one parameter.
				if (numberPrompts > 0)
				{
					// Start displaying a table with the parameter names and values.
					//System.out.println("<table>");

					// Loop through the parameters to get the parameter names and parameter values.
					for(int i=1;i<=numberPrompts;i++)
					{
						// Each prompt has the name SI_PROMPT + a digit starting from 1.  For example, if
						// there are 2 prompts, the first one would be called SI_PROMPT1 and the second
						// one would be called SI_PROMPT2.
						IProperties prompt = (IProperties)siPrompts.getProperty("SI_PROMPT"+i).getValue();

						// Get the parameter name.
						promptName = prompt.getProperty("SI_NAME").getValue().toString();

						// Start displaying a row.
						//out.println("<tr>");

						// First column in the row is the parameter name.
						//out.println("<td>Parameter Name = "+promptName+"</td><td><table>");
						// Get the current values property bag.
						IProperties currentValues = (IProperties)prompt.getProperty("SI_CURRENT_VALUES").getValue();

						// Get the number of values for this particular parameter.
						int numberValues = ((Integer)currentValues.getProperty("SI_NUM_VALUES").getValue()).intValue();

						// Loop through all of the values for this particular parameter.
						for(int j=1;j<=numberValues;j++)
						{
							// Print out the values.
							// Each value has the name SI_VALUE + a digit starting from 1.  For example, if
							// there are two values, then the first value would be called SI_VALUE1 and the
							// second value would be called SI_VALUE2.
							IProperties currentValue = (IProperties)currentValues.getProperty("SI_VALUE"+j).getValue();

							// If the size of the currentValue property bag is 3, then that means that the
							// parameter is a range parameter.
							//if(currentValue.size() == 3)
							String promptType = prompt.getProperty("SI_PROMPT_TYPE").getValue().toString();
							System.out.println("prompt type is " + promptType);
							if (promptType.equals("3"))
							{
								// Range Parameter: contains 3 properties: SI_MIN, SI_MAX, SI_OPTIONS
								//6/5/2012: Have a problem where a normal prompt is evaluating as a range parameter
								System.out.println(prompt.getProperty("SI_PROMPT_TYPE").getValue().toString());

								// Get the SI_MIN property bag.
								IProperties minRange = (IProperties)currentValue.getProperty("SI_MIN").getValue();
								//6/5/2012:  This does NOT work and this parameter IS a range parameter
								//but the property bag lacks this property, hence the error. Try instance ID 47637018, AM0109DX - Total Charges For Billed Assets By Date

								// Get the SI_DATA property of the SI_MIN property bag.
								String minValue = minRange.getProperty("SI_DATA").getValue().toString();

								// Get the SI_MAX property bag.
								IProperties maxRange = (IProperties)currentValue.getProperty("SI_MAX").getValue();

								// Get the SI_MAX property of the SI_MAX property bag.
								String maxValue = maxRange.getProperty("SI_DATA").getValue().toString();
								paramValueString += minValue  + "; " + maxValue + "; ";
								// Print out the Minimum and Maximum values for the parameter.
								//out.println("<tr><td>Range " + j + " Min Value " + minValue + " Max Value " + maxValue + "</td></tr>");
							}
							// If the size of the currentValue property bag is 4, then that means that the
							// parameter is a discete parameter.
							else //if (currentValue.size() == 4)
								if (promptType.equals("4"))
								{
									// Discrete Parameter: contains 4 properties: SI_SHOWDESCONLY, SI_OPTIONS,
									// SI_DESCRIPTION, SI_DATA 
									System.out.println(prompt.getProperty("SI_PROMPT_TYPE").getValue().toString());
									// For this situation we are only interested in the SI_DATA property.
									String paramValue = currentValue.getProperty("SI_DATA").getValue().toString();
									//out.println("<tr><td> Value " + j + " = " + paramValue + "</td></tr>");
									paramValueString += paramValue  + "; ";

								}
						}
					}
				}
				else
				{
					System.out.println("The number of prompts is 0 for the InfoObject.");
				}
			}
			catch (Exception ex) {
				System.out.println(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getStackTrace() + " getParamValuesString - line 1099");
			}
			//else
			//{
			//	// There is no SI_PROMPTS for the InfoObject.
			//	System.out.println ("The siPrompts is null for the InfoObject.");
			//}


		}
		catch  (Exception ex){
			System.out.println(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getStackTrace() + " getParamValuesString - line 1095");
		}
		System.out.println("param value string is: " + paramValueString);
		return paramValueString;
	}

	public static String getParamValueString2(int recurring_id,String login, String pswd, String EnterpriseEnv, String auth) {
		//'The idea here is to create a semi-colon separated list of parameter values to help identify duplicate recurring jobs.
		//'The semi-colon is used because in BOXI InfoView, parameter values are separated by semi-colons in the report history

		int x;
		String promptValue;	
		String promptName;
		String paramValueString = "";
		int paramCount;
		//IReportProcessingInfo oProcessInfo;//this type of variable doesn't work.
		IEnterpriseSession objEnterpriseSession;
		IInfoObjects colInfoObjects;
		IInfoObject objInfoObject;
		IProperties oProcessInfo;
		String strInfoSQL = "SELECT * from CI_INFOOBJECTS WHERE SI_INSTANCE = 1 and si_recurring = 1 and si_id = " + recurring_id;


		try{
			ISessionMgr  objSessionMgr = CrystalEnterprise.getSessionMgr();
			objEnterpriseSession = objSessionMgr.logon(login, pswd, EnterpriseEnv, auth);    
			IInfoStore objInfoStore = (IInfoStore) objEnterpriseSession.getService("InfoStore");
			colInfoObjects = (IInfoObjects)objInfoStore.query(strInfoSQL);
			//objInfoObject = (IInfoObject)colInfoObjects.get(i); 

			objInfoObject = (IInfoObject)colInfoObjects.get(0);

			System.out.println("report title is:  " + objInfoObject.getTitle().toString());
			//oProcessInfo = (IReportProcessingInfo)objInfoObject.getProcessingInfo();//throws error:
			//com.crystaldecisions.sdk.occa.infostore.internal.ProcessingInfo cannot be cast to com.crystaldecisions.sdk.plugin.desktop.common.IReportProcessingInfo com.crystaldecisions.sdk.occa.infostore.internal.ProcessingInfo cannot be cast to com.crystaldecisions.sdk.plugin.desktop.common.IReportProcessingInfo [Ljava.lang.StackTraceElement;@14ce5eb getParamValuesString - line 1158
			//The reason this doesn't work on all instances is because it must be si_kind='CrystalReport'.


			try{
				// Get the SI_PROMPTS property bag from the InfoObject's Processing Info - got this from SAP
				IProperties siPrompts = (IProperties) objInfoObject.getProcessingInfo().properties().getProperty("SI_PROMPTS").getValue();
				//Error:  properties() and getPRoperty() undefined for IProcessingInfo

				//This doesn't work either
				//IProperties siPrompts = (IProperties) objInfoObject.properties().getProperty("SI_PROMPTS").getValue();//throws error "null null [Ljava.lang.StackTraceElement;@7bacb getParamValuesString"


				//Try this:
				//IProperties siPrompts = (IProperties) objInfoObject.properties().getProperty("SI_PROMPTS");

				// Make sure that there are parameters.
				//if (siPrompts != null){
				// Get the number of prompts (the number of parameters)
				int numberPrompts = ((Integer)siPrompts.getProperty("SI_NUM_PROMPTS").getValue()).intValue(); //get null null
				//int numberPrompts = ((Integer)siPrompts.getProperty("SI_NUM_PROMPTS").getValue());  //also fails
				//IPrompts objPrompts = (IPrompts)objInfoObject.getProcessingInfo().properties().getProperty("SI_PROMPTS"); //this threw an error
				//com.crystaldecisions.sdk.properties.internal.SDKPropertyBag$SDKProperty cannot be cast to com.crystaldecisions.sdk.prompting.IPrompts com.crystaldecisions.sdk.properties.internal.SDKPropertyBag$SDKProperty cannot be cast to com.crystaldecisions.sdk.prompting.IPrompts [Ljava.lang.StackTraceElement;@16f2067 getParamValuesString - line 1088

				//nbrPrompts = objPrompts.size();
				//int numberPrompts = objPrompts.size();
				//int numberPrompts = siPrompts.size();

				// Make sure that there is at least one parameter.
				if (numberPrompts > 0)
				{
					// Start displaying a table with the parameter names and values.
					//System.out.println("<table>");

					// Loop through the parameters to get the parameter names and parameter values.
					for(int i=1;i<=numberPrompts;i++)
					{
						// Each prompt has the name SI_PROMPT + a digit starting from 1.  For example, if
						// there are 2 prompts, the first one would be called SI_PROMPT1 and the second
						// one would be called SI_PROMPT2.
						IProperties prompt = (IProperties)siPrompts.getProperty("SI_PROMPT"+i).getValue();

						// Get the parameter name.
						promptName = prompt.getProperty("SI_NAME").getValue().toString();

						// Start displaying a row.
						//out.println("<tr>");

						// First column in the row is the parameter name.
						//out.println("<td>Parameter Name = "+promptName+"</td><td><table>");
						// Get the current values property bag.
						IProperties currentValues = (IProperties)prompt.getProperty("SI_CURRENT_VALUES").getValue();

						// Get the number of values for this particular parameter.
						int numberValues = ((Integer)currentValues.getProperty("SI_NUM_VALUES").getValue()).intValue();

						// Loop through all of the values for this particular parameter.
						for(int j=1;j<=numberValues;j++)
						{
							// Print out the values.
							// Each value has the name SI_VALUE + a digit starting from 1.  For example, if
							// there are two values, then the first value would be called SI_VALUE1 and the
							// second value would be called SI_VALUE2.
							IProperties currentValue = (IProperties)currentValues.getProperty("SI_VALUE"+j).getValue();

							// If the size of the currentValue property bag is 3, then that means that the
							// parameter is a range parameter.
							//if(currentValue.size() == 3)
							String promptType = prompt.getProperty("SI_PROMPT_TYPE").getValue().toString();
							System.out.println("prompt type is " + promptType);
							if (promptType.equals("3"))
							{
								// Range Parameter: contains 3 properties: SI_MIN, SI_MAX, SI_OPTIONS
								//6/5/2012: Have a problem where a normal prompt is evaluating as a range parameter
								System.out.println(prompt.getProperty("SI_PROMPT_TYPE").getValue().toString());

								// Get the SI_MIN property bag.
								IProperties minRange = (IProperties)currentValue.getProperty("SI_MIN").getValue();
								//6/5/2012:  This does NOT work and this parameter IS a range parameter
								//but the property bag lacks this property, hence the error. Try instance ID 47637018, AM0109DX - Total Charges For Billed Assets By Date

								// Get the SI_DATA property of the SI_MIN property bag.
								String minValue = minRange.getProperty("SI_DATA").getValue().toString();

								// Get the SI_MAX property bag.
								IProperties maxRange = (IProperties)currentValue.getProperty("SI_MAX").getValue();

								// Get the SI_MAX property of the SI_MAX property bag.
								String maxValue = maxRange.getProperty("SI_DATA").getValue().toString();
								paramValueString += minValue  + "; " + maxValue + "; ";
								// Print out the Minimum and Maximum values for the parameter.
								//out.println("<tr><td>Range " + j + " Min Value " + minValue + " Max Value " + maxValue + "</td></tr>");
							}
							// If the size of the currentValue property bag is 4, then that means that the
							// parameter is a discete parameter.
							else //if (currentValue.size() == 4)
								if (promptType.equals("4"))
								{
									// Discrete Parameter: contains 4 properties: SI_SHOWDESCONLY, SI_OPTIONS,
									// SI_DESCRIPTION, SI_DATA 
									System.out.println(prompt.getProperty("SI_PROMPT_TYPE").getValue().toString());
									// For this situation we are only interested in the SI_DATA property.
									String paramValue = currentValue.getProperty("SI_DATA").getValue().toString();
									//out.println("<tr><td> Value " + j + " = " + paramValue + "</td></tr>");
									paramValueString += paramValue  + "; ";

								}
						}
					}
				}
				else
				{
					System.out.println("The number of prompts is 0 for the InfoObject.");
				}
			}
			catch (Exception ex) {
				System.out.println(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getStackTrace() + " getParamValuesString - line 1099");
			}
			//else
			//{
			//	// There is no SI_PROMPTS for the InfoObject.
			//	System.out.println ("The siPrompts is null for the InfoObject.");
			//}


		}
		catch  (Exception ex){
			System.out.println(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getStackTrace() + " getParamValuesString - line 1095");
		}
		System.out.println("param value string is: " + paramValueString);
		return paramValueString;
	}

	public static String getDistributionList1(IEnterpriseSession objEnterpriseSession, String env, int si_id) {
		//Pre-condition:  we don't have the list of all the email recipients for a recurring job
		//Post-condition: using the existing session to Enterprise, collect all the email recipients
		//for a recurring job and store it in a semi-colon delimited string that is passed back to
		//the calling function
		//NOTE:  this is only for jobs that send to email


		IInfoObjects repInfoObjects;
		IInfoObject instance;

		String qry="SELECT * FROM CI_INFOOBJECTS WHERE si_id=" + si_id + " AND si_instance=1";
		String smtpQry= "Select * From CI_SYSTEMOBJECTS Where SI_PARENTID=29 and SI_NAME='CrystalEnterprise.SMTP'";
		String addressString="";
		try
		{
			//ISessionMgr  sm = CrystalEnterprise.getSessionMgr();
			//System.out.println("session mgr started");

			//Don't need to relogon because the Enterprise session is being passed.

			IInfoStore infoStore = (IInfoStore) objEnterpriseSession.getService("InfoStore");
			System.out.println("login succeeded");

			//get report object
			repInfoObjects = (IInfoObjects)infoStore.query(qry);
			System.out.println("Line 2019 query running");

			System.out.println("Result size " + repInfoObjects.getResultSize());

			for(Object o: repInfoObjects) {
				instance = (IInfoObject) o;            


				//for(Class c: repInfoObjects.get(0).getClass().getInterfaces()) {
				//	System.out.println(c.getName());
				//}

				//cast to IReport to get at properties --not right.  
				//Can't cast a pdf to a report object
				//A report doesn't necessarily know the email list.
				//And this is an unnecessary step.

				//IReport rpt = (IReport)repInfoObjects.get(0);
				//IInfoObject instance = (IInfoObject) repInfoObjects.get(0);

				try {	
					System.out.println("Helper 2040 getting report object");
					//get plugin interface
					ISchedulingInfo schedInfo = instance.getSchedulingInfo();
					System.out.println("Helper 2043 getting scheduling info");
					

					//get the destination from the scheduling info
					//This is the line throwing the error on the server (in DOS)
					//For some recurring schedules it's null
					IDestination ceDest =(IDestination)schedInfo.getDestinations().get(0);
					System.out.println("ceDest is " + ceDest.getName());
					System.out.println("Helper 2047 getting destination");

					//query for destination object
					IInfoObjects smtpInfoObjects = infoStore.query(smtpQry);
					System.out.println("Helper 2051 getting smtp objects");

					//cast destination infoobject to smtp object
					ISMTP smtpObj =(ISMTP)smtpInfoObjects.get(0);


					//copy plugin properties to the smtp object to get all the properties
					ceDest.copyToPlugin(smtpObj);		


					//retrieve smtpoptions
					ISMTPOptions smtpOpts = (ISMTPOptions)smtpObj.getScheduleOptions();
					System.out.println("Line 2063");
					/*
				//retrieve address list
				Iterator addresses = smtpOpts.getToAddresses().iterator();

				//iterate through list and display
				String addressString="";
				for (Iterator iterator = smtpOpts.getToAddresses().iterator(); iterator.hasNext(); ) {
				//while(addresses.hasNext())
				//{
					addressString += addresses.next() + ";";
				}
				System.out.println(addressString);
					 */


					for (Object o1 : smtpOpts.getToAddresses()) { 
						addressString += o1.toString() + ";";
					}

					for (Object o2 : smtpOpts.getCCAddresses()) {
						addressString += o2.toString() + ";";
					}

					System.out.println("emails: " + addressString);
				}
				catch(Exception ex)
				{
					System.out.println("Exception on line 2091 in 4.1 Helper class.  " + ex.getMessage() + " " + ex.getStackTrace());
				}

			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			System.out.println(ex.getMessage() + " " + ex.getStackTrace());
		}
		/**
		 * @param EnterpriseEnv
		 */
		System.out.println("Line 2089 Helper class");
		return addressString;
	}

	public static String getErrorCategory(String errMsg){
		//Pre-condition:  the error category is unknown
		//Post-condition:  the error category is known

		String error_category = "No error message recorded in CMS database.";

		try {
			//if (errMsg + "").Trim().Length = 0) {
			if (errMsg.length() ==0){
				error_category = "No error message recorded in CMS database.";
			} else
				if (errMsg.contains("Failed to open")) {
					error_category = "Failed to open the connection";
				} else if (errMsg.contains("Error in the report processing unit.")) {
					error_category = "Error in the report processing unit";
				} else if (errMsg.contains("The document cannot be retrieved from the File Repository Server. (WIS 30951)")) {
					error_category = "FRS document retrival error";
				} else if (errMsg.contains("The database logon information for this report is either incomplete or incorrect.")){
					error_category = "Incorrect logon parameters";
				} else if (errMsg.contains("The table")) {
					error_category = "Table could not be found";
				} else if (errMsg.contains( "CrystalEnterprise.Smtp") ) {
					if (errMsg.contains("Syntax error")) {
						error_category = "SMTP recipient address syntax error"; //the order here is important because a syntax error also has "SMTP error" in the message
					} else if (errMsg.contains( "SMTP server")) {
						error_category = "SMTP server error";
					} else {
						error_category = "SMTP Error";
					}
					//'} else if (InStr(errMsg, "address") > 0 Or InStr(errMsg, "recipient") > 0) {
					//'        error_category = "User email error"
				} else if (errMsg.contains( "Input")){
					error_category = "File Repository Server Input is down";
				} else if (errMsg.contains("Unmanaged")){
					error_category = "Unmanaged disk destination error";
				} else if (errMsg.contains("Output")){
					error_category = "File Repository Server Output is down";
				} else if (errMsg.contains("Operating system file")){
					error_category = "File Repository Server error";
				} else if (errMsg.contains("Output")){
					error_category = "File Repository Server Output is down";
				} else if (errMsg.contains("Failed to retrieve")){
					error_category = "Failed to retrieve data from the database";
				} else if (errMsg.contains("UFL")){
					error_category = "Missing Julian Date UFL";
				} else if (errMsg.contains("Information is needed")){
					error_category = "Parameter values missing";
				} else if (errMsg.contains("Not enough memory for operation")){
					error_category = "Not enough memory for operation";
				} else if (errMsg.contains("log on parameters")){
					error_category = "Incorrect logon parameters";
				} else if (errMsg.contains("database logon information")){
					error_category = "Incorrect logon parameters";
				} else if (errMsg.contains("Error in formula")){
					error_category = "Error in formula";
				} else if (errMsg.contains("Print Engine Error")){
					error_category = "Print Engine Error";
				} else if (errMsg.contains("Object could not be scheduled")){
					error_category = "Object could not be scheduled";
				} else if (errMsg.contains("Object failed to run due to an error while processing on the Job Server")){
					error_category = "Job server error";
				} else if (errMsg.contains("export DLL")){
					error_category = "Error detected by export DLL";
				} else if (errMsg.contains("background processing")){
					error_category = "Not submitted for bkgrnd processing";
				} else if (errMsg.contains("Audit notification")){
					error_category = "Audit notification failed";
				} else if (errMsg.contains("Duplicate object")){
					error_category = "Duplicate object name in same folder";
				} else if (errMsg.contains("Operation not yet implemented")){
					error_category = "Operation not yet implemented";
				} else if (errMsg.contains("rowset column not found")){
					error_category = "Rowset column not found";
				} else if (errMsg.contains("Database Connector Error")){
					//Nice way to get the exact error msg, but it takes up too much room on the graph.
					//            if (errMsg.contains("SessionMgr.1")){
					//            error_category =    mid(errMsg,(errMsg.contains("SessionMgr.1") +14)                                
					//            else
					//             error_category =   "Database connector error"
					//This shows more abbreviated errors msgs, but for the percent of errors, it makes the legend too big.
					//            if (errMsg.contains("Cannot find the user, group or object")){                               
					//             error_category =   "Cannot find the user, group or object"
					//            else if (errMsg.contains("Not a valid logon token")){                               
					//             error_category =   "Not a valid logon token"
					//            else if (errMsg.contains("Central Management Server")){                              
					//              error_category =  "Central Management Server is not running"
					//            else if (errMsg.contains("waiting for database mutex")){                               
					//               error_category = "Timeout waiting for database mutex"
					//            else if (errMsg.contains("Transport error")){                               
					//               error_category = "Transport error: No response from server, timeout exceeded"
					//            else if (errMsg.contains("Unable to connect to cluster")){                               
					//               error_category = "Unknown cluster name"
					//            else
					error_category = "Database connector error";
				} else if (errMsg.contains("rpt not found"))  {
					error_category = "Rpt not found on FRS";
				} else if (errMsg.contains("The system cannot find the path specified")) {
					error_category = "The system cannot find the path specified";
				} else if (errMsg.contains("File I/O error")){
					error_category = "File I/O error";
				} else if (errMsg.contains("Invalid summary field")){
					error_category = "Invalid summary field";
				} else if (errMsg.contains("write error.  You do not have right")){
					error_category = "'Download file right' not granted";
					//else if (errMsg.contains("Cannot access the file")){ 
					//        error_category = "Cannot access the file";
				} else if (errMsg.contains("write error. [You do not have sufficient rights")){
					error_category = "'Download file right' not granted";
				} else if (errMsg.contains("An internal error occured while calling 'processDPCommandsEx' API. (Error: ERR_WIS_30270)")){
					error_category = "Internal error";
				} else if (errMsg.contains("Maximum character output size limit reached")){
					error_category = "Maximum character output size limit reached";
				} else if (errMsg.contains(" [CrystalEnterprise.Ftp]: [Communication error")){
					error_category = "Error while reading data from FTP server";
				} else if (errMsg.contains("CrystalEnterprise.Ftp]: []")){
					error_category = "FTP disabled";
				} else if (errMsg.contains("CrystalEnterprise.Ftp]: [error")){
					error_category = "FTP error EAI_NONAME";
				} else if (errMsg.contains("CrystalEnterprise.Ftp]: [500 Illegal")){
					error_category = "FTP illegal port command";
				} else if (errMsg.contains("CrystalEnterprise.Ftp]: [504 Command")){
					error_category = "FTP command not implemented";
				} else if (errMsg.contains("CrystalEnterprise.Ftp]: [530 User")){
					error_category = "FTP error - User cannot log in";
				} else if (errMsg.contains("CrystalEnterprise.Ftp]: [WSA0")){
					error_category = "FTP WSA0 error";
				} else if (errMsg.contains("CrystalEnterprise.Ftp]: [550")){
					if ((errMsg.contains("Access is denied"))){
						error_category = "FTP 550 error - Access is denied";
					}else {
						error_category = "FTP 550 error - The process cannot access the file because it is being used by another process";
					}
				} else if (errMsg.contains("you do not have the right 'Download'")){
					error_category = "You do not have download right";
				} else if (errMsg.contains("Duplicate object name in same folder")){
					error_category = "Duplicate object name in same folder";
				} else if (errMsg.contains("rpt not found")){
					error_category = "Rpt not found on FRS";
				} else if ((errMsg.contains(".rpt") && errMsg.length() < 25)){
					error_category = "Rpt not found on FRS";
				} else if (errMsg.contains("$Proxy0")){
					error_category = "$Proxy0 error";
				} else if (errMsg.contains("Cannot initialize Report Engine server. (Error: RWI 00226)")){
					error_category = "Could not initialize Report Engine server";
				} else if (errMsg.contains("[repo_proxy 17]")){
					error_category = "Could not save the document to the repository";
				} else if (errMsg.contains("The property with ID DOCUMENT_WISaveAsXLSOptimized does not exist in the object")){
					error_category = "Missing XLS property";
				} else if (errMsg.contains("Internal error")){
					error_category = "Internal error";
				} else if (errMsg.contains("DOCUMENT_WISaveAsXLSOptimized does not exist in the object")){
					error_category = "Missing XLS property";
				} else if (errMsg.contains("Job Server")){
					error_category = "Job server processing error";
				}   else {
					error_category = "Unknown error";
				}
		}


		catch (Exception ex){
			System.out.println(ex.getMessage() + " " + ex.getStackTrace());
		}


		return error_category;

	} //End Function

	public static int getDupCount(String targetTable, String columnNames, String Env){
		String mssqlSelectString = "";
		int dupCount = 0;
		System.out.println(targetTable.indexOf("STG1"));
		System.out.println(targetTable.startsWith("STG1"));
		if (targetTable.startsWith("STG1")) {
			mssqlSelectString = "select count(instance_id) theCount from " + targetTable + " where rowid in (select max(rowid) from  " + targetTable + " group by " + columnNames + " having count(*) > 1)";
		}
		else {
			mssqlSelectString = "select count(instance_id) theCount " + targetTable + " where environment = '" + Env + "' and rowid in (select max(rowid) from  " + targetTable + " where environment = '" + Env + "' group by " + columnNames + " having count(*) > 1)";

		}

		System.out.println(mssqlSelectString);
		try {
			dupCount = Helper.runOracleSelectCountQuery(mssqlSelectString);  
		}
		catch (Exception ex){
			System.out.println("Problem with getting a dup count " + ex.getMessage() + " " + ex.getStackTrace());
		}
		return dupCount;
	}

	public static int runOracleSelectCountQuery(String mssqlSelectString) {
		// TODO Auto-generated method stub


		//This function finds the max count of dups in a table. 
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@dlxkrbodb1.lex.lexmark.com:1750:edr1den1";
		String username = "botrack";
		String password = "********";
		int theCount = 0;
		try
		{
			System.out.println(mssqlSelectString);
			Class.forName(driver);		//loads the driver
			Connection conn = DriverManager.getConnection(url,username,password);	

			Statement stmt = conn.createStatement() ; 
			ResultSet rs = stmt.executeQuery(mssqlSelectString);
			while (rs.next()) 
			{
				theCount = rs.getInt("theCount"); 
			}
			conn = null;
			conn.close();
		}	
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		return theCount;


	}

	public static int runMSSQLSelectCountQuery(String mssqlSelectString) {
		// TODO Auto-generated method stub


		//This function finds the max count of dups in a table. 
		String driver = "mssql.jdbc.driver.SQLDriver";
		String url = "jdbc:sqlserver://DASHWBODB012;databaseName=EDR1DEN1;integratedSecurity=true;";
		String username = "botrack";
		String password = "********";
		int theCount = 0;
		try
		{
			System.out.println(mssqlSelectString);
			Class.forName(driver);		//loads the driver
			Connection conn = DriverManager.getConnection(url,username,password);	

			Statement stmt = conn.createStatement() ; 
			ResultSet rs = stmt.executeQuery(mssqlSelectString);
			while (rs.next()) 
			{
				theCount = rs.getInt("theCount"); 
			}
			conn = null;
			conn.close();
		}	
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		return theCount;


	}
	
	public static String report_Name_Validator (String firstSix){
		//Checks to see that a report name doesn't start with digits.
		//The standard is 2 letters then 4 digits without spaces
		//If it starts with 6 digits or more, then it is a Webi ad-hoc instance, not a report.
		
		String result = "";
		try {
			int theDigits = Integer.parseInt(firstSix);
			result = "true";
		}
		catch (NumberFormatException ex) {
			System.out.println(ex.getMessage() + " " + ex.getStackTrace());
			result = "false";
		}

		return result;
	}
}
