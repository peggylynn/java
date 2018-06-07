
//import com.crystaldecisions.reports.sdk.*;
import java.lang.*;
import java.sql.*;
import java.util.Date;
import java.util.*;
import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Iterator;
import java.text.SimpleDateFormat;
import java.text.DateFormat;





import sun.net.smtp.*;

//import com.businessobjects.prompting.objectmodel.common.IPromptValue$IString; //cannot be resolved
import  com.crystaldecisions.sdk.occa.enadmin.*;
import com.crystaldecisions.sdk.occa.report.application.ReportClientDocument;
import com.crystaldecisions.sdk.occa.report.lib.IStrings;
import  com.crystaldecisions.sdk.occa.security.*;
import  com.crystaldecisions.sdk.plugin.admin.cmsadmin.*;
import com.crystaldecisions.sdk.plugin.admin.jobserveradmin.IJobServerAdmin;
import  com.crystaldecisions.sdk.plugin.desktop.common.*;
import com.crystaldecisions.reports.*;
import com.crystaldecisions.sdk.exception.SDKException;
import com.crystaldecisions.sdk.framework.*;
import com.crystaldecisions.sdk.framework.internal.CEFactory;
import com.crystaldecisions.sdk.occa.infostore.*;
import com.crystaldecisions.sdk.occa.managedreports.IReportAppFactory;
import com.crystaldecisions.sdk.plugin.desktop.*;
import com.crystaldecisions.sdk.plugin.desktop.common.IReportLogon;
import com.crystaldecisions.sdk.plugin.desktop.common.IReportParameter;
import com.crystaldecisions.sdk.plugin.desktop.report.*;
import com.crystaldecisions.sdk.plugin.desktop.server.*;
import com.crystaldecisions.sdk.plugin.destination.smtp.*;
import com.crystaldecisions.sdk.prompting.IPrompts;
import com.crystaldecisions.sdk.properties.IProperties;
import com.crystaldecisions.sdk.properties.IProperty;
import com.crystaldecisions.sdk.properties.ISDKList;


public class BOBJ4Alerter {
	//Dec 31, 2014:
	//The original design of this alerter was to detect pending report schedules (a big problem in BOXI R2) and 
	// to detect long-running schedules - anything longer than 3 hours, more than generous by most standards.  In BOXI R2
	// pending jobs were common, several times a year, sometimes more than once in a month, mostly due to 
	//long-running reports that filled up the available queues for hours at a time and more importantly: network latency.
	//No report schedule should pend for longer than 10 minutes past its scheduled start time, so I ran the code every 12 minutes.
	//BOXI R2 was upgraded to BOXI 3.1 in 2010 and we saw a lot fewer problems with pending jobs due to a more robust BO CMS, 
	//but they did still happen, especially with the existence of long-running reports.  
	//With the upgrade to BI 4.0 in 2012, the number of pending jobs was nearly undetectable in part because of a more robust set of servers
	//and because in 2012 we on the BI COC instituted a report design review process to help find and eliminate design flaws that
	//could lead to long runtime durations.  The report design reviews are no longer done from what I can tell.
	//We upgraded to BI 4.1 in the second half of 2014, and did have a couple of severe incidents that caused
	//pending jobs.  The servers are as robust as before if not more so, but now there is latency between them and
	// some of the data sources.  The cloud is actually a co-location in Virginia run by TCS, but that has it's costs.
	// When modifying this code to save BO meta data to MS SQL Server instead of Oracle, I considered dropping
	// the pending jobs detection.  However, because of the two incidents where the major symptom was pending jobs,
	// I will keep the pending job detection, but run it less frequently.  It would be better if the support team were
	// made aware of pending jobs just as soon as there is a problem, but the cost to do this is high, meaning that
	// the Tracker id must log into BOE about 6 times per hour at a minimum.  If anything should prevent the connection
	// from disconnecting properly, then the count of sessions for Tracker could grow quite high.  Therefore, I will run this
	// once per hour instead of every 12 minutes.  I will keep the criteria to flag a job as pending if it has pended
	// for 10 minutes or longer.


	public static void main(String[] args){
		String entLoginID = "tracker";
		String entPassword = "libambini*8";
		String auth = "secLDAP";

		try {
			//test sendmail
			//Helper.sendmail("Hi", "cool java app!", "bishopp@lexmark.com", "bishopp@lexmark.com");

			//This works:
			//Emailer.sendEmail("bishopp@lexmark.com", "bishopp@lexmark.com","Testito", "Testing Emailer class", null);

			//This works now
			//Emailer.sendEmail("bishopp@lexmark.com", "bishopp@lexmark.com, pbishop@hotmail.com","Testito", "Testing Emailer class", null);
			//getCMCStatistics("USLEXBRP02", entLoginID, entPassword, auth);
			//getCMCRunningCRJobs(String boCmsName, String boUser, String boPassword,  String boAuthType, String jobServerName) 
			//getCMCRunningCRJobs("USLEXBCS01", entLoginID, entPassword, auth, "USLEXBRP01.CrystalReportsJobServer");
			//getCMCRunningCRJobs("USLEXBCS01", entLoginID, entPassword, auth, "USLEXBRP02.CrystalReportsJobServer");
			//getCMCRunningCRJobs("USLEXBCS01", entLoginID, entPassword, auth, "USLEXBRP03SIA.CrystalReportsJobServer");
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage() + " " + ex.getStackTrace());
		}

		//System.out.println("Email sent");


		Calendar now = Calendar.getInstance();

		///////////////TESTING////////////////

		///////////////END TESTING/////////////


		load_stg1_boxi_LongRunningJobs("PASHWBOBJ013", entLoginID, entPassword, auth, "STG1_bo4p_currjobs");
		load_stg1_boxi_LongRunningJobs("DASHWBOBJ012", entLoginID, entPassword, auth, "stg1_bo4v_currjobs");
		load_stg1_boxi_LongRunningJobs("dashwbobj011", entLoginID, entPassword, auth, "STG1_bo4d_currjobs");
		load_stg1_boxi_LongRunningJobs("QASHWBOBJ013", entLoginID, entPassword, auth, "STG1_bo4q_currjobs");




		//if (now.get(Calendar.MINUTE) > 0) {
		/*		load_stg1_boxi_CurrJobs("USLEXBCS01", entLoginID, entPassword, auth, "stg1_boxi3p_currjobs",1);
		load_stg1_boxi_CurrJobs("USLEXBCS01", entLoginID, entPassword, auth, "stg1_boxi3p_currjobs",2);
		load_stg1_boxi_CurrJobs("DASHWBOBJ012", entLoginID, entPassword, auth, "stg1_boxi3d_currjobs",0);
		load_stg1_boxi_CurrJobs("duslexboj02", entLoginID, entPassword, auth, "stg1_boxi3t_currjobs",0);
		load_stg1_boxi_CurrJobs("TUSLEXBCS01", entLoginID, entPassword, auth, "stg1_boxi3q_currjobs",0);*/

		//Now that I've worked around the nextruntime bug of Jan 30, 1979, this can run on the hour, too.
		//This are to check for pending jobs
		load_stg1_boxi_CurrJobs("PASHWBOBJ013", entLoginID, entPassword, auth, "stg1_bo4p_currjobs");

		load_stg1_boxi_CurrJobs("DASHWBOBJ012", entLoginID, entPassword, auth, "stg1_bo4v_currjobs");

		load_stg1_boxi_CurrJobs("dashwbobj011", entLoginID, entPassword, auth, "stg1_bo4d_currjobs");

		load_stg1_boxi_CurrJobs("QASHWBOBJ013", entLoginID, entPassword, auth, "stg1_bo4q_currjobs");


		/*	I will run these once per hour via the task manager in Windows.  That makes this conditional obsolete.
		 * if (now.get(Calendar.MINUTE) == 0){
			load_stg1_boxi_LongRunningJobs("PASHWBOBJ013", entLoginID, entPassword, auth, "STG1_bo4p_currjobs");
			load_stg1_boxi_LongRunningJobs("DASHWBOBJ012", entLoginID, entPassword, auth, "stg1_bo4v_currjobs");
			load_stg1_boxi_LongRunningJobs("dashwbobj011", entLoginID, entPassword, auth, "STG1_bo4d_currjobs");
			load_stg1_boxi_LongRunningJobs("QASHWBOBJ013", entLoginID, entPassword, auth, "STG1_bo4q_currjobs");
		}*/


	}

	public static void load_stg1_boxi_LongRunningJobs(String EnterpriseEnv, String login, String pswd,String auth, String destTblNm) {
		//Records the reports that are currently running where the current duration is 3 hours or longer.

		IEnterpriseSession objEnterpriseSession;
		//IInfoStore objInfoStore;
		IInfoObjects colInfoObjects;
		IInfoObject objInfoObject;
		IInfoObject objInfoObject2;
		String errorMsg = "";
		String cookieCrumbs = "";
		int count;
		String si_owner;
		//Calendar runDate;
		//String runDate;
		Date runDate;

		//Date startTime;
		//String startTime;
		Calendar endTime;
		long theInterval = 0;

		String dtToday;
		String msSQLInsertString;
		String maxID ="0";
		String logon1 = "unknown";
		String logon2 = "unknown";
		String pswd1 = "unknown";
		String pswd2 = "unknown";
		String datasrc1 = "not found in CMS";
		String datasrc2 = "not found in CMS";
		int numPrompts;
		Calendar maxEndDate;
		String destination = "";
		String outputFile = "";
		String distributionList = "";
		String si_file1 = "not set";
		String strInfoSQL;
		Calendar maxCreationDate;
		//java.sql.Date maxCreationDate;
		int i = 0;
		IFiles ifiles;
		String scheduleStatus;
		IProcessingInfo processingInfo;
		IReport iReport;
		String server = "";
		String server1 = "";
		String server2 = "";
		int recordCount;
		String runningCountMsg;
		int RunningCount;
		int duration = 0;
		String machineUsed;		
		int nbrPrompts = 0;
		IPrompts objPrompts;
		IPrompts siPrompts;
		String paramValues = "";
		String strDate;
		DateFormat formatter;
		Date date;
		Date theRunDate;
		Calendar startDate= Calendar.getInstance();
		Calendar endDate= Calendar.getInstance();
		Calendar cal=Calendar.getInstance();
		Calendar currentTime= Calendar.getInstance();
		Calendar nextRunTime = Calendar.getInstance();
		Calendar creationTime = Calendar.getInstance();
		Calendar updateTS = Calendar.getInstance();
		Calendar startTime = Calendar.getInstance();
		String schedDeleted = "N";
		IReport oReport;
		String emails;
		//IStrings serverNames;

		try 
		{


			ISessionMgr  objSessionMgr = CrystalEnterprise.getSessionMgr();
			objEnterpriseSession = objSessionMgr.logon(login, pswd, EnterpriseEnv, auth);    
			IInfoStore objInfoStore = (IInfoStore) objEnterpriseSession.getService("InfoStore");
			//IReportAppFactory rptAppFactory = (IReportAppFactory)objEnterpriseSession.getService( "", "RASReportService" ) ;

			// Get the running jobs
			//THIS IS TEST CODE:
			//strInfoSQL = "SELECT * from CI_INFOOBJECTS WHERE si_schedule_status=0 and si_new_job_id != '' and si_name='83D F0006 Business Units for Cat Code & Description'";
			//THIS IS PRODUCTION CODE:
			strInfoSQL = "SELECT * from CI_INFOOBJECTS WHERE si_schedule_status=0 and si_new_job_id != '' and si_kind !='PlatformSearchScheduling'";
			//Nov 12, 2013:  I added a filter to remove from the resultset Platform Search Scheduling Object so the support team could test the search function.
			System.out.println(strInfoSQL);
			colInfoObjects = (IInfoObjects)objInfoStore.query(strInfoSQL);
			recordCount = colInfoObjects.size();

			//System.out.println(recordCount);
			System.out.println("Line 237, There are " + recordCount + " running jobs.");
			//System.out.println("Did this step print?");
			runningCountMsg = "There are " + recordCount + " running jobs.";
			RunningCount = recordCount;

			if (recordCount > 0){
				//Connection conn=null;
				//Class.forName("oracle.jdbc.driver.OracleDriver");
				//conn=DriverManager.getConnection("jdbc:oracle:thin:@dlxkrbodb1.lex.lexmark.com:1750:edr1den1","bishopp","********");
				//Statement s=conn.createStatement();
				//'This records all the instances found on the BOXI system

				for (int x=0; x < recordCount;x++){
					//System.out.println("looping..." + x);

					objInfoObject = (IInfoObject)colInfoObjects.get(x);	
					//oReport = (IReport) colInfoObjects.get(x);

					//System.out.println(objInfoObject.getTitle());
					currentTime = Calendar.getInstance();
					//System.out.println("The currentime is " + currentTime.getTime());


					//runDate = (Date)objInfoObject.properties().getProperty("SI_STARTTIME").getValue(); //java.util.Date cannot be cast to java.util.Calendar [Ljava.lang.StackTraceElement;@1d95492
					//runDate = (Calendar)objInfoObject.properties().getProperty("SI_STARTTIME").getValue(); //java.util.Date cannot be cast to java.util.Calendar [Ljava.lang.StackTraceElement;@1d95492
					theRunDate = (Date)objInfoObject.properties().getProperty("SI_STARTTIME").getValue(); //java.util.Date cannot be cast to java.util.Calendar [Ljava.lang.StackTraceElement;@1d95492
					//System.out.println("set runDate var " + theRunDate);

					startTime.setTime((Date) objInfoObject.properties().getProperty("SI_STARTTIME").getValue());
					System.out.println("The start time is " + startTime.getTime());

					//strDate = objInfoObject.properties().getProperty("SI_STARTTIME").getValue().toString();
					//System.out.println("the strDate is " + strDate);
					//formatter = new SimpleDateFormat(strDate);  //
					//date = (Date)formatter.parse("MM-DD-YYYY hh:mi:ss");		//not sure about this format mask			
					//date = (Date) formatter.parse(strDate);
					//Calendar dt = Calendar.getInstance();
					//dt.setTime(date);
					//System.out.println("Today is " + dt);
					//System.out.println(dt.getTime());
					//cal.setTime(date);

					//startTime = (Calendar)runDate;
					//startTime = (Date)theRunDate;
					//endTime = (Calendar)objInfoObject.properties().getProperty("SI_ENDTIME").getValue();
					//System.out.println("code ran till here");
					//theInterval = 60/(endTime.getTimeInMillis() - startTime.getTimeInMillis())*1000;
					//theInterval = 60/(currentTime.getTimeInMillis() - startTime.getTimeInMillis())*1000;
					//theInterval = 60;
					//System.out.println("the interval is " + theInterval);

					System.out.println("current time is " + currentTime.toString() + " start time was " + startTime.toString());
					long difference = currentTime.getTimeInMillis()-startTime.getTimeInMillis();
					//System.out.println("The difference is " + difference);

					int days = (int) ((double) difference)/1000/60/60/24;
					long days_millis = days*1000*60*60*24;
					int hours = (int) ((double) (difference-days_millis))/1000/60/60;
					long hours_millis = hours*1000*60*60;
					int mins = (int) ((double) (difference-days_millis-hours_millis))/1000/60;
					long mins_millis = mins*1000*60;
					int secs = (int) ((double) (difference-days_millis-hours_millis-mins_millis))/1000;
					long secs_millis = secs*1000;
					int millis = (int) (difference-days_millis-hours_millis-mins_millis-secs_millis);

					//System.out.print("Difference: " + (difference) + " milliseconds ==> " + days + " days " + hours + " hours " + mins + " mins " + secs + " secs " + millis + " millis");
					System.out.println("Time since report started running is " + hours + " hours " + days + " days.");
					hours += days*24;
					if ((hours >= 3) || (days >= 1)) {
						//System.out.println("The number hours this report has been running is " + hours);
						//System.out.println("Long-running report, " + objInfoObject.getTitle() + " Report " + objInfoObject.getTitle() + " has been running for " + hours + " hours.  Please go to the report job history to see if this is unusual.  In all likelihood, this is unusual since 99% of reports run in 2 hours or less.  There could be a BOXI system problem.");

						//BufferedReader input = new BufferedReader(new FileReader("c:\\emails.txt"));
						BufferedReader input = new BufferedReader(new FileReader("c:\\users\\bishopp\\emails.txt"));
						emails = input.readLine();

						Emailer.sendEmail("bishopp@lexmark.com",emails,"Long-running report, " + objInfoObject.getTitle() + " " + EnterpriseEnv + " ", "Report " + objInfoObject.getTitle() + " has been running for " + hours + " hours and " + days + " days..  Please go to the report job history to see if this is unusual.  In all likelihood, this is unusual since 99% of reports run in 2 hours or less.  There could be a BOXI system problem.",  null);
						//Emailer.sendEmail("bishopp@lexmark.com","bishopp@lexmark.com","Long-running report, " + objInfoObject.getTitle() + " - " + EnterpriseEnv + " ", "Report " + objInfoObject.getTitle() + " has been running for " + hours + " hours and " + days + " days.  Please go to the report job history to see if this is unusual.  In all likelihood, this is unusual since 99% of reports run in 2 hours or less.  There could be a BOXI system problem.",  null);

						RunningCount += 1;

						try{
							//6/5/2012:  this used to work.  Now it doesn't
							//machineUsed = objInfoObject.properties().getProperty("SI_MACHINE_USED").getValue().toString();
							//get SI_MACHINE_USED, the job server the job ran on
							IProperties properties = objInfoObject.getSchedulingInfo().properties();
							IProperty machineUsedProperty = properties.getProperty(CePropertyID.SI_MACHINE_USED);
							//String value = (String) creationTimeProperty.getValue();
							machineUsed =  machineUsedProperty.toString();
							System.out.println("job server " + machineUsed);
						}
						catch (Exception ex)
						{
							machineUsed = "unk";
						}


						try{
							destination = objInfoObject.getSchedulingInfo().getDestination().getName();
							//destination = objInfoObject.getSchedulingInfo().getDestinations().get(0).toString(); //can I use this?
							if (destination =="CrystalEntprise.Smtp") {	                		
								distributionList = Helper.getDistributionList("PASHWBOBJ013", objInfoObject.getID());		                		   
							}

						}
						catch (Exception ex)
						{}


						try{
							//Get the logon info
							//processingInfo = objInfoObject.getProcessingInfo();
							//6/5/2012:  this used to work.  Now it doesn't.
							/*		iReport = (IReport) objInfoObject;

							//Get the custom username 
							ISDKList dbLogons = iReport.getReportLogons();

							//Get the db logon credentials 
							for (int h = 0; h < dbLogons.size(); h++) {	
								IReportLogon dbLogon = (IReportLogon)dbLogons.get(h);
								if (h == 0){
									logon1 = dbLogon.getUserName();
									server1 = dbLogon.getDatabaseName();
								}
								if (h ==1) {
									logon2 = dbLogon.getUserName();
									server2 = dbLogon.getDatabaseName();
								}							
							}*/

							// First get the Processing Info properties for the InfoObject.
							IProperties boProcessingInfo = (IProperties) objInfoObject.getProcessingInfo().properties();

							// Make sure that there is processing info set for the InfoObject.
							if (boProcessingInfo != null)
							{
								// Get the SI_PROMPTS property bag from the InfoObject's Processing Info
								IProperties Prompts = (IProperties) boProcessingInfo.getProperty("SI_PROMPTS").getValue();

								// Make sure that there are parameters.
								if (Prompts != null)
								{
									// Get the number of prompts (the number of parameters)
									numPrompts = ((Integer)Prompts.getProperty("SI_NUM_PROMPTS").getValue()).intValue();
								}

								// Get the SI_LOGON_INFO property bag
								IProperties logonProperties = (IProperties) boProcessingInfo.getProperty("SI_LOGON_INFO").getValue();
								if (logonProperties != null) {
									IProperties silogonProperties = (IProperties) logonProperties.getProperty("SI_LOGON1").getValue();

									System.out.println("SI_LOGON1 size is " + silogonProperties.size());
									//16, it's true
									String numLogins = logonProperties.getProperty("SI_NUM_LOGONS").toString();

									System.out.println("Num logons is " + numLogins);

									//Get the db logon credentials 
									int instanceID = objInfoObject.getID();
									System.out.println("instance id is " + instanceID);
									for (int h = 0; h < Integer.parseInt(numLogins); h++) {	
										//IReportLogon dbLogon = (IReportLogon)silogonProperties.get(h);
										if (h == 0){

											server1 = silogonProperties.getProperty("SI_SERVER").toString();
											logon1 = silogonProperties.getProperty("SI_USER").toString();
										}
										if (h ==1) {
											server2 = silogonProperties.getProperty("SI_SERVER").toString();
											logon2 = silogonProperties.getProperty("SI_USER").toString();
										}							
									}

								}
							}
							server = server1;
						}
						catch (Exception ex)
						{
							System.out.println(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getCause() + " " + ex.getStackTrace());
						}


						try{
							si_owner = (String)objInfoObject.properties().getProperty("SI_OWNER").getValue();
						}
						catch (Exception ex)
						{}


						try{
							//nextRunTime = (Calendar)objInfoObject.properties().getProperty("SI_NEXTRUNTIME").getValue();
							nextRunTime.setTime((Date) objInfoObject.properties().getProperty("SI_NEXTRUNTIME").getValue());
						}
						catch (Exception ex)
						{}


						try{
							//nextRunTime = (Calendar)objInfoObject.properties().getProperty("SI_NEXTRUNTIME").getValue();
							creationTime.setTime((Date) objInfoObject.properties().getProperty("SI_CREATION_TIME").getValue());
						}
						catch (Exception ex)
						{}


						try{
							//nextRunTime = (Calendar)objInfoObject.properties().getProperty("SI_NEXTRUNTIME").getValue();
							updateTS.setTime((Date) objInfoObject.properties().getProperty("SI_UPDATE_TS").getValue());
						}
						catch (Exception ex)
						{}


						//Run this only if the report has parameters
						try {
							//siPrompts = (IPrompts)objInfoObject.getProcessingInfo().properties().getProperty("SI_PROMPTS").getValue();
							//nbrPrompts = siPrompts.size();
							//if (nbrPrompts >0) {
							//	paramValues = Helper.getParamValueString(objInfoObject); //this throws an error:
							//	//com.crystaldecisions.sdk.plugin.desktop.excel.internal.a cannot be cast to com.crystaldecisions.sdk.plugin.desktop.report.IReport 
							//	System.out.println(paramValues);
							//} 
							//IReport report = (IReport) oInfoObjects.get(i);
							paramValues = Helper.getParamValueString(objInfoObject); //this throws an error:
							//paramValues = Helper.getParamValueString(oReport); //this throws an error:
							//com.crystaldecisions.sdk.plugin.desktop.excel.internal.a cannot be cast to com.crystaldecisions.sdk.plugin.desktop.report.IReport
							//$Proxy1 cannot be cast to com.crystaldecisions.sdk.plugin.desktop.report.IReport $Proxy1 cannot be cast to com.crystaldecisions.sdk.plugin.desktop.report.IReport null [Ljava.lang.StackTraceElement;@b56559

							System.out.println("param values are:  " + paramValues);

						} 
						catch (Exception ex) {
							System.out.println(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getCause() + " " + ex.getStackTrace());
						}


						try{
							//6/5/2012:  this used to work.  Now it doesn't
							//ifiles = (IFiles)objInfoObject.properties().getProperty("SI_FILES").getValue();
							//outputFile = (String)ifiles.properties().getProperty("SI_PATH").getValue();	

							IProperties properties = objInfoObject.getProcessingInfo().properties();
							IProperty siFilesProperty = properties.getProperty(CePropertyID.SI_FILES);
							outputFile = siFilesProperty.toString();
							System.out.println("output file is " + outputFile);
							IProperties allProperty = (IProperties)objInfoObject.properties();
							IProperties pathProperty = allProperty.getProperties("SI_FILES");
							IProperty path = (IProperty)pathProperty.getProperty("SI_PATH");
							Object getPath = path.getValue();
							String filepath = getPath.toString();
							System.out.println("file path is " + filepath);

							IProperty outputFileProperty  = (IProperty)pathProperty.getProperty("SI_FILE1");
							outputFile = outputFileProperty.getValue().toString();
							System.out.println("output file is " + outputFile);

						}
						catch (Exception ex)
						{
							System.out.println(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getCause() + " " + ex.getStackTrace());
						}

						//See if you can read the limit from a text file
						//BufferedReader in = new BufferedReader(new FileReader("c:\\limit.txt"));
						BufferedReader in = new BufferedReader(new FileReader("c:\\Users\\bishopp\\limit.txt"));
						String limit = "0";
						limit = in.readLine();
						int theLimit;
						theLimit = Integer.parseInt(limit.trim());

						//////JUST FOR TESTING!!!!!!!!!!!!***********
						//if (hours < theLimit) {
						if (hours > theLimit) {
							schedDeleted = "Y";
						}

						//msSQLInsertString = "insert into " + destTblNm + " (REPORT_ID, INSTANCE_ID, SI_STARTTIME,   SI_SUBMITTER, SI_SERVER, SI_OWNER, SI_SCHEDULE_STATUS,REPORT_PATH, SI_NAME, SI_CREATION_TIME, SI_NUM_PROMPTS, DATASRC_LOGIN_NM, SI_PASSWORD, DATASRC_LOGIN_NM2, SI_PASSWORD2,  SI_MACHINE_USED, SI_UPDATE_TS, DESTINATION, OUTPUT_FILE, REPORT_CUID, SI_PARENT_FOLDER_CUID, ENVIRONMENT, SCHEDULE_STATUS, SI_NEXTRUNTIME) values (" + objInfoObject.getParentID() + ", " + objInfoObject.getID() + ", to_date('" + startTime.getTime() + "','MM/DD/YYYY HH12:MI:SS AM')" + ", '" + Helper.safeSQL((String)objInfoObject.getSchedulingInfo().properties().getProperty("SI_SUBMITTER").getValue()) + "', '" + Helper.safeSQL(server) + "', '" + Helper.safeSQL((String)objInfoObject.getSchedulingInfo().properties().getProperty("SI_SUBMITTER").getValue()) + "', " + objInfoObject.properties().getProperty("SI_SCHEDULE_STATUS").getValue().toString() + ", '" + Helper.safeSQL(cookieCrumbs) + "', '" + Helper.safeSQL(objInfoObject.getTitle()) + "', to_date('" + objInfoObject.properties().getProperty("SI_CREATION_TIME").getValue() + "','MM/DD/YYYY HH12:MI:SS AM'), " + nbrPrompts + ",'" + logon1 + "','" + pswd1 + "', '" + logon2 + "', '" + pswd2 + "','" + machineUsed + "', to_date('" + objInfoObject.properties().getProperty("SI_UPDATE_TS").getValue() + "','MM/DD/YYYY HH12:MI:SS AM'), '" + destination + "','" + outputFile + "', '" + objInfoObject.getParentCUID() + "', '" + objInfoObject.properties().getProperty("SI_PARENT_FOLDER_CUID").getValue() + "', '" + EnterpriseEnv + "','Pending', " + " to_date('" + objInfoObject.properties().getProperty("SI_NEXTRUNTIME").getValue() + "','MM/DD/YYYY HH12:MI:SS AM'))";
						msSQLInsertString = "insert into " + destTblNm + " (REPORT_ID, INSTANCE_ID, SI_STARTTIME,   SI_SUBMITTER, SI_SERVER, SI_OWNER, SI_SCHEDULE_STATUS,REPORT_PATH, SI_NAME, SI_CREATION_TIME, SI_NUM_PROMPTS, DATASRC_LOGIN_NM, SI_PASSWORD, DATASRC_LOGIN_NM2, SI_PASSWORD2,  SI_MACHINE_USED, SI_UPDATE_TS, DESTINATION, OUTPUT_FILE, REPORT_CUID, SI_PARENT_FOLDER_CUID, ENVIRONMENT, SCHEDULE_STATUS, SI_NEXTRUNTIME, DURATION, SCHED_DELETED, PARAM_VALUES) values (" + objInfoObject.getParentID() + ", " + objInfoObject.getID() + ", ('" + Helper.msSQLDateTime(startTime) + "'), '" + Helper.safeSQL((String)objInfoObject.getSchedulingInfo().properties().getProperty("SI_SUBMITTER").getValue()) + "', '" + Helper.safeSQL(server) + "', '" + Helper.safeSQL((String)objInfoObject.getSchedulingInfo().properties().getProperty("SI_SUBMITTER").getValue()) + "', " + objInfoObject.properties().getProperty("SI_SCHEDULE_STATUS").getValue().toString() + ", '" + Helper.safeSQL(cookieCrumbs) + "', '" + Helper.safeSQL(objInfoObject.getTitle()) + "', ('" + Helper.msSQLDateTime(creationTime) + "'), " + nbrPrompts + ",'" + logon1 + "','" + pswd1 + "', '" + logon2 + "', '" + pswd2 + "','" + machineUsed + "', ('" + Helper.msSQLDateTime(updateTS) + "'), '" + destination + "','" + outputFile + "', '" + objInfoObject.getParentCUID() + "', '" + objInfoObject.properties().getProperty("SI_PARENT_FOLDER_CUID").getValue() + "', '" + EnterpriseEnv + "','Running', " + " ('" + Helper.msSQLDateTime(nextRunTime) + "')," + hours + ",'" + schedDeleted + "', '" + paramValues + "')";
						System.out.println(msSQLInsertString);

						try
						{
							//System.out.println(msSQLInsertString);
							Helper.runMSSQLInsertQuery(msSQLInsertString);

						}


						catch (Exception ex)
						{
							String errMsg = ex.getMessage();
							String errStackTrace = ex.getStackTrace().toString();

							errMsg = errMsg.replace("\r", " ");
							errStackTrace = errStackTrace.replace("\n", " ");

							System.out.println("Could not insert long-running report info into database.");
							System.out.println(ex.getStackTrace() + " " + ex.getMessage());
							Emailer.sendEmail("bishopp@lexmark.com", "bishopp@lexmark.com", "Error with BOXI Alerter - long-running rpts - " + EnterpriseEnv, ex.getMessage() + " " + ex.getStackTrace(), null);
							Helper.runMSSQLInsertQuery("insert into STG1_BOXI_TRACKER_ERRORS " + errMsg + " " + errStackTrace + " " + EnterpriseEnv + "', GETDATE()");

						}

						try
						{
							//If the report has been running longer than XX hours, delete the schedule and inform the user.
							//Get the current acceptable runtime duration:
							//File testFile = new File("D:\\Documents and Settings\\bishopp\\workspace\\Tracker Application\\duration.txt");
							//System.out.println("Original file contents: " + ReadWriteTextFile.getContents(testFile));
							//if (hours.toString >= ReadWriteTextFile.getContents(testFile)) {
							//							this isn't working yet

							//}

							//BufferedReader in = new BufferedReader(new FileReader("c:\\limit.txt"));
							//String limit = "0";
							//limit = in.readLine();
							//int theLimit;
							//theLimit = Integer.parseInt(limit.trim());

							System.out.println("Hours this report has been running:  " + hours);

							//See if you can read the limit from a text file
							//BufferedReader in = new BufferedReader(new FileReader("c:\\limit.txt"));
							//String limit = "0";
							//limit = in.readLine();
							//int theLimit;
							//theLimit = Integer.parseInt(limit.trim());
							System.out.println("The limit for long running reports is " + theLimit + " hours");


							if(hours > theLimit){
								System.out.println("Current long-running schedule " + objInfoObject.getTitle() + " has been running for " + hours + " hours.");
								//deleteLongRunningReports(objInfoObject,hours);
								deleteLongRunningReports(objInfoObject, hours, EnterpriseEnv, theLimit +1);
							}

						}
						catch (Exception ex)
						{System.out.println(ex.getMessage() + " " + ex.getStackTrace());}
					}//end if

				}
				// Clean up the Enterprise Session.
				if(objEnterpriseSession != null) {
					try {
						objEnterpriseSession.logoff();

					} catch(Exception e_ignore_in_cleanup) {}
				}

			}

		}
		catch (Exception ex)
		{
			String errMsg = ex.getMessage();
			String errStackTrace = ex.getStackTrace().toString();

			errMsg = errMsg.replace("\r", " ");
			errStackTrace = errStackTrace.replace("\n", " ");

			System.out.println(ex.getMessage() + " " + ex.getStackTrace());
			System.out.println("insert into STG1_BOXI_TRACKER_ERRORS " + errMsg + " " + errStackTrace + " " + EnterpriseEnv + "', GETDATE()");
			//for this to work you must replace any line feed carriage returns with dashes
			//carriage return is '\r' and line feed is '\n'
			Helper.runMSSQLInsertQuery("insert into STG1_BOXI_TRACKER_ERRORS " + errMsg + " " + errStackTrace + " " + EnterpriseEnv + "', GETDATE()");
			//'sendmail("Logon error", ex.getMessage() + " " + ex.getStackTrace)
			//Helper.sendmail("bishopp@lexmark.com", "bishopp@lexmark.com, amarabes@lexmark.com,msankar@lexmark.com,ajain@lexmark.com", "Logon error - load_stg1_migr_XI_reports(String, String, String, String, String) - " + EnterpriseEnv, ex.getMessage() + " " + ex.getStackTrace());
			//Helper.sendmail("bishopp@lexmark.com", "bishopp@lexmark.com", "Logon error - load_stg1_boxi_runhist(String, String, String, String, String) - " + EnterpriseEnv, ex.getMessage() + " " + ex.getStackTrace());

			//Emailer.sendEmail("bishopp@lexmark.com", "bishopp@lexmark.com", "Error with BOXI Alerter - long-running rpts - " + EnterpriseEnv, ex.getMessage() + " " + ex.getStackTrace(), null);

		}
	}


	public static void load_stg1_boxi_CurrJobs(String EnterpriseEnv, String login, String pswd,String auth, String destTblNm, int long_running_flg){
		//This function sends to the boxi admins an email alert whenever there are report schedules stuck in pending for 10 min or longer
		//The long-running_flg indicates whether to not monitor reports that are assigned to a long-running queue.
		//1 means get just long-running; 2 means get no long-running reports; 0 means get any

		IEnterpriseSession objEnterpriseSession;
		//IInfoStore objInfoStore;
		IInfoObjects colInfoObjects;
		IInfoObject objInfoObject;
		IInfoObjects colInfoObjects2;
		IInfoObject objInfoObject2;
		String errorMsg = "";
		String cookieCrumbs = "";
		int count;
		String si_owner;		
		long theInterval = 0;
		Calendar startDate;
		Calendar endDate;
		String dtToday;
		String msSQLInsertString;
		String maxID ="0";
		String logon1 = "unknown";
		String logon2 = "unknown";
		String pswd1 = "unknown";
		String pswd2 = "unknown";
		String datasrc1 = "not found in CMS";
		String datasrc2 = "not found in CMS";
		int numPrompts;
		Calendar maxEndDate;
		String destination = "";
		String outputFile = "";
		String distributionList = "";
		String si_file1 = "not set";
		String strInfoSQL;
		Calendar maxCreationDate;
		//java.sql.Date maxCreationDate;
		int i = 0;
		IFiles ifiles;
		String scheduleStatus;
		IProcessingInfo processingInfo;
		IReport iReport;
		String server = "";
		String server1 = "";
		String server2 = "";
		int recordCount;
		int runningCount;
		String runningCountMsg;
		int PendingCount = 0;
		int StuckPendingCount = 0;
		int duration;
		String machineUsed;
		Calendar runDate = Calendar.getInstance();
		Calendar startTime = Calendar.getInstance();
		Calendar endTime = Calendar.getInstance();
		Calendar nextRunTime = Calendar.getInstance();
		Calendar currentTime = Calendar.getInstance();
		Calendar creationTime = Calendar.getInstance();
		Calendar updateTS = Calendar.getInstance();
		int nbrPrompts = 0;
		IPrompts objPrompts;
		String targetTable = "";
		String oraSelectString = "";
		String filter = "";
		String emailSubject ="";
		IPrompts siPrompts;
		String paramValues = "";
		IReport oReport;
		String emails;

		try 
		{
			if (EnterpriseEnv =="PASHWBOBJ013") {
				targetTable = "STG1_bo4p_creports";
			}
			else if (EnterpriseEnv == "DASHWBOBJ011") {
				targetTable = "STG1_bo4d_creports";
			}
			else if (EnterpriseEnv == "QASHWBOBJ013") {
				targetTable = "STG1_bo4q_creports";
			}
			else if (EnterpriseEnv == "dlexwbobj003"){
				targetTable = "STG1_bo4v_creports";	
			}


			switch (long_running_flg) {
			case 0: {
				//get any pending report
				oraSelectString = "";
				break;
			}

			case 1: {
				//get only long-running pending reports
				oraSelectString = "select cuid from " + targetTable + " where long_running_flg='Y'";
				break;
			}

			case 2: {
				//get only non-long-running pending reports
				//oraSelectString = "select cuid from " + targetTable + " where long_running_flg is null";
				oraSelectString = "select cuid from " + targetTable + " where long_running_flg='Y'";
				//Use this query so that you can use "where not in".
				break;
			} 

			}
			//Determine the list of reports to detect as pending
			String driver = "oracle.jdbc.driver.OracleDriver";
			String url = "jdbc:oracle:thin:@dlxkrbodb1.lex.lexmark.com:1750:edr1den1";
			String username = "botrack";
			String password = "Lex405!1";
			System.out.println(oraSelectString);
			try
			{				
				Class.forName(driver);		//loads the driver - ClassNotFoundException
				System.out.println("(Driver loaded)");
				Connection conn = DriverManager.getConnection(url,username,password);	

				Statement stmt = conn.createStatement() ; 
				System.out.println(oraSelectString);
				ResultSet rs = stmt.executeQuery(oraSelectString);

				while (rs.next()){
					try {
						//filter += rs.getNString("'" + "cuid" + "', ");
						filter += "'";
						filter += rs.getString("cuid");
						//filter += rs.getNString("cuid");
						filter += "', ";
					}
					catch (Exception ex){
						System.out.println(ex.getMessage() + " " + ex.getStackTrace());
					}

				}
				//				Remove final comma and space
				filter = filter.substring(0, filter.length() - 2);

				//Set the filter according to the long_running_flg value
				if (long_running_flg == 0) {
					filter = "";
				}
				else if (long_running_flg == 1) {
					filter = " and si_parent_cuid in (" + filter + ")";
				}
				else
					filter = " and si_parent_cuid not in (" + filter + ")";

				System.out.println("the filter is " + filter);

				//conn = null;
				//conn.close(); Since I call the Helper.runMSSQLInsertQuery, this isn't needed.
				//Besides, it closes the connection too soon.
			}          
			catch (Exception ex)
			{
				System.out.println(ex.getMessage() + " " + ex.getStackTrace() + " runMSSQLInsertQuery line 582");
				//sendmail("Connection to EDR1DEN1 timed out ",  ex.getMessage() + " " + ex.getStackTrace(), "bishopp@lexmark.com", "bishopp@lexmark.com");
			}

			//use the above cuid string in the where clause in the follwing SQL statements.
			//need to cut off the final comma, first

			ISessionMgr  objSessionMgr = CrystalEnterprise.getSessionMgr();
			objEnterpriseSession = objSessionMgr.logon(login, pswd, EnterpriseEnv, auth);    
			IInfoStore objInfoStore = (IInfoStore) objEnterpriseSession.getService("InfoStore");

			// Get the pending jobs
			strInfoSQL = "select * from ci_infoobjects where si_schedule_status=9  and si_recurring=0 " + filter;
			System.out.println(strInfoSQL);

			colInfoObjects = (IInfoObjects)objInfoStore.query(strInfoSQL);
			recordCount = colInfoObjects.size();

			//System.out.println(recordCount);
			System.out.println("Line 462, There are " + recordCount + " total pending jobs.");
			runningCountMsg = "There are " + recordCount + " pending jobs.";
			PendingCount = recordCount;

			if (recordCount > 0){ //we have pending jobs.  Check to see if these have been pending for 10 minutes or longer.

				//Connection conn=null;
				//Class.forName("oracle.jdbc.driver.OracleDriver");
				//conn=DriverManager.getConnection("jdbc:oracle:thin:@dlxkrbodb1.lex.lexmark.com:1750:edr1den1","bishopp","plb350");
				//Statement s=conn.createStatement();
				//'This records all the instances found on the BOXI system


				for (int x=0; x < recordCount;x++){
					objInfoObject = (IInfoObject)colInfoObjects.get(x);
					oReport = (IReport)colInfoObjects.get(x);

					//Check the nextruntime to see if it really is pending; if it has been pending for 10 minutes or longer, we'll count it.
					nextRunTime.setTime((Date) objInfoObject.properties().getProperty("SI_NEXTRUNTIME").getValue());
					Calendar now = Calendar.getInstance();

					//long difference =  currentTime.getTimeInMillis() - nextRunTime.getTimeInMillis();
					long difference =  now.getTimeInMillis() - nextRunTime.getTimeInMillis();
					//System.out.println("The difference is " + difference);
					System.out.println("The next run time year is " + nextRunTime.YEAR);

					int days = (int) ((double) difference)/1000/60/60/24;
					long days_millis = days*1000*60*60*24;
					int hours = (int) ((double) (difference-days_millis))/1000/60/60;
					long hours_millis = hours*1000*60*60;
					int mins = (int) ((double) (difference-days_millis-hours_millis))/1000/60;
					long mins_millis = mins*1000*60;
					int secs = (int) ((double) (difference-days_millis-hours_millis-mins_millis))/1000;
					long secs_millis = secs*1000;
					int millis = (int) (difference-days_millis-hours_millis-mins_millis-secs_millis);

					//System.out.print("Difference: " + (difference) + " milliseconds ==> " + days + " days " + hours + " hours " + mins + " mins " + secs + " secs " + millis + " millis");


					theInterval = 60/(now.getTimeInMillis() - nextRunTime.getTimeInMillis())*1000;

					if (mins >= 10 && mins < 1440){						

						System.out.println("pending report for 10 min or longer");

						try {
							runDate.setTime((Date) objInfoObject.properties().getProperty("SI_STARTTIME").getValue()); //threw an error 
							startTime = runDate;	
						}

						catch (Exception ex){
							//set a default starttime
							//System.out.println("Default startTime is" + startTime.getTime()); //however, this worked without error, but used current time

						}

						try {
							endTime.setTime((Date) objInfoObject.properties().getProperty("SI_ENDTIME").getValue());	//this also threw an error, but I don't know why I'm looking for an endtime for a pending job
						}
						catch (Exception ex){
							//set a default endtime
							//System.out.println("Default end time is " + endTime.getTime());
						}


						StuckPendingCount += 1;

						try{	                    
							machineUsed = objInfoObject.properties().getProperty("SI_MACHINE_USED").getValue().toString();  //this also threw an error
						}
						catch (Exception ex)
						{
							machineUsed = "unk";
						}


						try{
							destination = objInfoObject.getSchedulingInfo().getDestination().getName();
							destination = objInfoObject.getSchedulingInfo().getDestinations().get(0).toString();
							if (destination =="CrystalEntprise.Smtp") {	                		
								distributionList = Helper.getDistributionList(EnterpriseEnv, objInfoObject.getID());		                		   
							}

						}
						catch (Exception ex)
						{}


						/*try{
							//Get the logon info
							//processingInfo = objInfoObject.getProcessingInfo();
							iReport = (IReport) objInfoObject;

							//Get the custom username 
							ISDKList dbLogons = iReport.getReportLogons();

							//Get the db logon credentials 
							for (int h = 0; h < dbLogons.size(); h++) {	
								IReportLogon dbLogon = (IReportLogon)dbLogons.get(h);
								if (h == 0){
									logon1 = dbLogon.getUserName();
									server1 = dbLogon.getDatabaseName();
								}
								if (h ==1) {
									logon2 = dbLogon.getUserName();
									server2 = dbLogon.getDatabaseName();
								}							
							}
							server = server1;
						}
						catch (Exception ex)
						{}*/

						// Get the SI_LOGON_INFO property bag
						try {
							IProperties boProcessingInfo = (IProperties) objInfoObject.getProcessingInfo().properties();

							IProperties logonProperties = (IProperties) boProcessingInfo.getProperty("SI_LOGON_INFO").getValue();
							if (logonProperties != null) {
								IProperties silogonProperties = (IProperties) logonProperties.getProperty("SI_LOGON1").getValue();

								System.out.println("SI_LOGON1 size is " + silogonProperties.size());
								//16, it's true
								String numLogins = logonProperties.getProperty("SI_NUM_LOGONS").toString();

								System.out.println("Num logons is " + numLogins);

								//Get the db logon credentials 
								int instanceID = objInfoObject.getID();
								System.out.println("instance id is " + instanceID);
								for (int h = 0; h < Integer.parseInt(numLogins); h++) {	
									//IReportLogon dbLogon = (IReportLogon)silogonProperties.get(h);
									if (h == 0){

										server1 = silogonProperties.getProperty("SI_SERVER").toString();
										logon1 = silogonProperties.getProperty("SI_USER").toString();
									}
									if (h ==1) {
										server2 = silogonProperties.getProperty("SI_SERVER").toString();
										logon2 = silogonProperties.getProperty("SI_USER").toString();
									}							
								}

							}
						}
						catch (Exception ex){
							System.out.println("Error getting logon properties " + ex.getMessage() + " " + ex.getStackTrace());
						}


						try{
							si_owner = (String)objInfoObject.properties().getProperty("SI_OWNER").getValue();
						}
						catch (Exception ex)
						{}


						try{
							//nextRunTime = (Calendar)objInfoObject.properties().getProperty("SI_NEXTRUNTIME").getValue();
							nextRunTime.setTime((Date) objInfoObject.properties().getProperty("SI_NEXTRUNTIME").getValue());
						}
						catch (Exception ex)
						{}


						try{
							//nextRunTime = (Calendar)objInfoObject.properties().getProperty("SI_NEXTRUNTIME").getValue();
							creationTime.setTime((Date) objInfoObject.properties().getProperty("SI_CREATION_TIME").getValue());
						}
						catch (Exception ex)
						{}


						try{
							//nextRunTime = (Calendar)objInfoObject.properties().getProperty("SI_NEXTRUNTIME").getValue();
							updateTS.setTime((Date) objInfoObject.properties().getProperty("SI_UPDATE_TS").getValue());
						}
						catch (Exception ex)
						{}


						try{
							objPrompts = (IPrompts)objInfoObject.getProcessingInfo().properties().getProperty("SI_PROMPTS"); //this threw an error
							nbrPrompts = objPrompts.size();
							//.properties().getProperty("SI_NUM_PROMPTS").getValue();


							//Get the number of prompts - alternate way in case the other way fails

							/*iReport = (IReport) objInfoObject;
	    					List paramList = iReport.getReportParameters();
	    					numPrompts = paramList.size();	*/
						}
						catch (Exception ex)
						{}

						try{
							ifiles = (IFiles)objInfoObject.properties().getProperty("SI_FILES").getValue(); //threw an error
							outputFile = (String)ifiles.properties().getProperty("SI_PATH").getValue();	
						}
						catch (Exception ex)
						{}

						//Run this only if the report has parameters
						try {
							//siPrompts = (IPrompts)objInfoObject.getProcessingInfo().properties().getProperty("SI_PROMPTS").getValue();
							//nbrPrompts = siPrompts.size();
							//if (nbrPrompts >0) {
							//	paramValues = Helper.getParamValueString(objInfoObject); //this throws an error:
							//	//com.crystaldecisions.sdk.plugin.desktop.excel.internal.a cannot be cast to com.crystaldecisions.sdk.plugin.desktop.report.IReport 
							//	System.out.println(paramValues);
							//} 
							paramValues = Helper.getParamValueString(oReport); //this throws an error:
							//com.crystaldecisions.sdk.plugin.desktop.excel.internal.a cannot be cast to com.crystaldecisions.sdk.plugin.desktop.report.IReport 
							System.out.println(paramValues);

						} 
						catch (Exception ex) {
							System.out.println(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getCause() + " " + ex.getStackTrace());
						}

						//Save the pending jobs to the tracker database
						//msSQLInsertString = "insert into " + destTblNm + " (REPORT_ID, INSTANCE_ID, SI_STARTTIME,   SI_SUBMITTER, SI_SERVER, SI_OWNER, SI_SCHEDULE_STATUS,REPORT_PATH, SI_NAME, SI_CREATION_TIME, SI_NUM_PROMPTS, DATASRC_LOGIN_NM, SI_PASSWORD, DATASRC_LOGIN_NM2, SI_PASSWORD2,  SI_MACHINE_USED, SI_UPDATE_TS, DESTINATION, OUTPUT_FILE, REPORT_CUID, SI_PARENT_FOLDER_CUID, ENVIRONMENT, SCHEDULE_STATUS, SI_NEXTRUNTIME) values (" + objInfoObject.getParentID() + ", " + objInfoObject.getID() + ", to_date('" + startTime + "','MM/DD/YYYY HH12:MI:SS AM')" + ", '" + Helper.safeSQL((String)objInfoObject.getSchedulingInfo().properties().getProperty("SI_SUBMITTER").getValue()) + "', '" + Helper.safeSQL(server) + "', '" + Helper.safeSQL((String)objInfoObject.getSchedulingInfo().properties().getProperty("SI_SUBMITTER").getValue()) + "', " + objInfoObject.properties().getProperty("SI_SCHEDULE_STATUS").getValue().toString() + ", '" + Helper.safeSQL(cookieCrumbs) + "', '" + Helper.safeSQL(objInfoObject.getTitle()) + "', to_date('" + objInfoObject.properties().getProperty("SI_CREATION_TIME").getValue() + "','MM/DD/YYYY HH12:MI:SS AM'), " + nbrPrompts + ",'" + logon1 + "','" + pswd1 + "', '" + logon2 + "', '" + pswd2 + "','" + machineUsed + "', to_date('" + objInfoObject.properties().getProperty("SI_UPDATE_TS").getValue() + "','MM/DD/YYYY HH12:MI:SS AM'), '" + destination + "','" + outputFile + "', '" + objInfoObject.getParentCUID() + "', '" + objInfoObject.properties().getProperty("SI_PARENT_FOLDER_CUID").getValue() + "', '" + EnterpriseEnv + "','Pending', " + " to_date('" + objInfoObject.properties().getProperty("SI_NEXTRUNTIME").getValue() + "','MM/DD/YYYY HH12:MI:SS AM'))";
						//msSQLInsertString = "insert into " + destTblNm + " (REPORT_ID, INSTANCE_ID, SI_STARTTIME,   SI_SUBMITTER, SI_SERVER, SI_OWNER, SI_SCHEDULE_STATUS,REPORT_PATH, SI_NAME, SI_CREATION_TIME, SI_NUM_PROMPTS, DATASRC_LOGIN_NM, SI_PASSWORD, DATASRC_LOGIN_NM2, SI_PASSWORD2,  SI_MACHINE_USED, SI_UPDATE_TS, DESTINATION, OUTPUT_FILE, REPORT_CUID, SI_PARENT_FOLDER_CUID, ENVIRONMENT, SCHEDULE_STATUS, SI_NEXTRUNTIME) values (" + objInfoObject.getParentID() + ", " + objInfoObject.getID() + ", to_date('" + startTime + "','MM/DD/YYYY HH12:MI:SS AM')" + ", '" + Helper.safeSQL((String)objInfoObject.getSchedulingInfo().properties().getProperty("SI_SUBMITTER").getValue()) + "', '" + Helper.safeSQL(server) + "', '" + Helper.safeSQL((String)objInfoObject.getSchedulingInfo().properties().getProperty("SI_SUBMITTER").getValue()) + "', " + objInfoObject.properties().getProperty("SI_SCHEDULE_STATUS").getValue() + ", '" + Helper.safeSQL(cookieCrumbs) + "', '" + Helper.safeSQL(objInfoObject.getTitle()) + "', to_date('" + objInfoObject.properties().getProperty("SI_CREATION_TIME").getValue() + "','MM/DD/YYYY HH12:MI:SS AM'), " + nbrPrompts + ",'" + logon1 + "','" + pswd1 + "', '" + logon2 + "', '" + pswd2 + "','" + machineUsed + "', to_date('" + objInfoObject.properties().getProperty("SI_UPDATE_TS").getValue() + "','MM/DD/YYYY HH12:MI:SS AM'), '" + destination + "','" + outputFile + "', '" + objInfoObject.getParentCUID() + "', '" + objInfoObject.properties().getProperty("SI_PARENT_FOLDER_CUID").getValue() + "', '" + EnterpriseEnv + "','Pending', " + " to_date('" + objInfoObject.properties().getProperty("SI_NEXTRUNTIME").getValue() + "','MM/DD/YYYY HH12:MI:SS AM'))";
						msSQLInsertString = "insert into " + destTblNm + " (REPORT_ID, INSTANCE_ID, SI_STARTTIME,   SI_SUBMITTER, SI_SERVER, SI_OWNER, SI_SCHEDULE_STATUS,REPORT_PATH, SI_NAME, SI_CREATION_TIME, SI_NUM_PROMPTS, DATASRC_LOGIN_NM, SI_PASSWORD, DATASRC_LOGIN_NM2, SI_PASSWORD2,  SI_MACHINE_USED, SI_UPDATE_TS, DESTINATION, OUTPUT_FILE, REPORT_CUID, SI_PARENT_FOLDER_CUID, ENVIRONMENT, SCHEDULE_STATUS, SI_NEXTRUNTIME, PARAM_VALUES) values (" + objInfoObject.getParentID() + ", " + objInfoObject.getID() + ", ('" + Helper.msSQLDateTime(startTime) + "')" + ", '" + Helper.safeSQL((String)objInfoObject.getSchedulingInfo().properties().getProperty("SI_SUBMITTER").getValue()) + "', '" + Helper.safeSQL(server) + "', '" + Helper.safeSQL((String)objInfoObject.getSchedulingInfo().properties().getProperty("SI_SUBMITTER").getValue()) + "', " + objInfoObject.properties().getProperty("SI_SCHEDULE_STATUS").getValue().toString() + ", '" + Helper.safeSQL(cookieCrumbs) + "', '" + Helper.safeSQL(objInfoObject.getTitle()) + "', ('" + Helper.msSQLDateTime(creationTime) + "'), " + nbrPrompts + ",'" + logon1 + "','" + pswd1 + "', '" + logon2 + "', '" + pswd2 + "','" + machineUsed + "', ('" + Helper.msSQLDateTime(updateTS) + "'), '" + destination + "','" + outputFile + "', '" + objInfoObject.getParentCUID() + "', '" + objInfoObject.properties().getProperty("SI_PARENT_FOLDER_CUID").getValue() + "', '" + EnterpriseEnv + "','Pending', " + " ('" + Helper.msSQLDateTime(nextRunTime) + "')', '" + paramValues + "')";
						System.out.println(msSQLInsertString);


						try
						{
							//System.out.println(msSQLInsertString);
							Helper.runMSSQLInsertQuery(msSQLInsertString);
						}
						catch (Exception ex)
						{

							String errMsg = ex.getMessage();
							String errStackTrace = ex.getStackTrace().toString();

							errMsg = errMsg.replace("\r", " "); //get NullPointException here.
							errStackTrace = errStackTrace.replace("\n", " ");

							System.out.println(ex.getMessage() + " " + ex.getStackTrace());
							System.out.println("insert into STG1_BOXI_TRACKER_ERRORS " + errMsg + " " + errStackTrace + " " + EnterpriseEnv + "', GETDATE()");
							//for this to work you must replace any line feed carriage returns with dashes
							//carriage return is '\r' and line feed is '\n'
							Helper.runMSSQLInsertQuery("insert into STG1_BOXI_TRACKER_ERRORS " + errMsg + " " + errStackTrace + " " + EnterpriseEnv + "', GETDATE()");
							//'sendmail("Logon error", ex.getMessage() + " " + ex.getStackTrace)
							//Helper.sendmail("bishopp@lexmark.com", "bishopp@lexmark.com, amarabes@lexmark.com,msankar@lexmark.com,ajain@lexmark.com", "Logon error - load_stg1_migr_XI_reports(String, String, String, String, String) - " + EnterpriseEnv, ex.getMessage() + " " + ex.getStackTrace());
							Emailer.sendEmail("bishopp@lexmark.com", "bishopp@lexmark.com", "Error with BOXI Alerter - currjobs - " + EnterpriseEnv, errMsg + " " + errStackTrace, null);
							//Emailer.sendEmail("bishopp@lexmark.com", "bishopp@lexmark.com, amarabes@lexmark.com,msankar@lexmark.com,ajain@lexmark.com", "Error with BOXI Alerter - currjobs - " + EnterpriseEnv, errMsg + " " + errStackTrace, null);

						}

					}
					else
					{
						System.out.println("No jobs are stuck in Pending for  " + EnterpriseEnv);
					}

				}
				if (StuckPendingCount > 0 && (long_running_flg == 0 || long_running_flg== 2)){  //reset this back to GREATER THAN after the test
					//Get the number of running jobs
					strInfoSQL = "select * from ci_infoobjects where si_schedule_status=0 and si_new_job_id != ''";
					colInfoObjects2 = (IInfoObjects)objInfoStore.query(strInfoSQL);
					runningCount = colInfoObjects2.size();

					for (int x=0; x < runningCount;x++){  //this was x < recordCount, which is the same as pendingCount - NOT RIGHT!
						objInfoObject2 = (IInfoObject)colInfoObjects2.get(x);


						try {
							runDate.setTime((Date) objInfoObject2.properties().getProperty("SI_STARTTIME").getValue());
							startTime = runDate;	
						}

						catch (Exception ex){
							//set a default starttime
							//System.out.println("Default startTime is" + startTime.getTime());

						}

						try {
							endTime.setTime((Date) objInfoObject2.properties().getProperty("SI_ENDTIME").getValue());	
						}
						catch (Exception ex){
							//set a default endtime
							//System.out.println("Default end time is " + endTime.getTime());
						}


						try{	                    
							machineUsed = objInfoObject2.properties().getProperty("SI_MACHINE_USED").getValue().toString();
						}
						catch (Exception ex)
						{
							machineUsed = "unk";
						}


						try{
							destination = objInfoObject2.getSchedulingInfo().getDestination().getName();
							destination = objInfoObject2.getSchedulingInfo().getDestinations().get(0).toString();
							if (destination =="CrystalEntprise.Smtp") {	                		
								distributionList = Helper.getDistributionList(EnterpriseEnv, objInfoObject2.getID());		                		   
							}

						}
						catch (Exception ex)
						{}


						/*try{
							//Get the logon info
							//processingInfo = objInfoObject2.getProcessingInfo();
							iReport = (IReport) objInfoObject2;

							//Get the custom username 
							ISDKList dbLogons = iReport.getReportLogons();

							//Get the db logon credentials 								//threw an exception but was caught
							for (int h = 0; h < dbLogons.size(); h++) {	
								IReportLogon dbLogon = (IReportLogon)dbLogons.get(h);
								if (h == 0){
									logon1 = dbLogon.getUserName();
									server1 = dbLogon.getDatabaseName();
								}
								if (h ==1) {
									logon2 = dbLogon.getUserName();
									server2 = dbLogon.getDatabaseName();
								}							
							}
							server = server1;
						}
						catch (Exception ex)
						{}*/

						// Get the SI_LOGON_INFO property bag
						try {
							IProperties boProcessingInfo = (IProperties) objInfoObject2.getProcessingInfo().properties();

							IProperties logonProperties = (IProperties) boProcessingInfo.getProperty("SI_LOGON_INFO").getValue();
							if (logonProperties != null) {
								IProperties silogonProperties = (IProperties) logonProperties.getProperty("SI_LOGON1").getValue();

								System.out.println("SI_LOGON1 size is " + silogonProperties.size());
								//16, it's true
								String numLogins = logonProperties.getProperty("SI_NUM_LOGONS").toString();

								System.out.println("Num logons is " + numLogins);

								//Get the db logon credentials 
								int instanceID = objInfoObject2.getID();
								System.out.println("instance id is " + instanceID);
								for (int h = 0; h < Integer.parseInt(numLogins); h++) {	
									//IReportLogon dbLogon = (IReportLogon)silogonProperties.get(h);
									if (h == 0){

										server1 = silogonProperties.getProperty("SI_SERVER").toString();
										logon1 = silogonProperties.getProperty("SI_USER").toString();
									}
									if (h ==1) {
										server2 = silogonProperties.getProperty("SI_SERVER").toString();
										logon2 = silogonProperties.getProperty("SI_USER").toString();
									}							
								}

							}
						}
						catch (Exception ex){
							System.out.println("Error getting logon properties " + ex.getMessage() + " " + ex.getStackTrace());
						}



						try{
							si_owner = (String)objInfoObject2.properties().getProperty("SI_OWNER").getValue();
						}
						catch (Exception ex)
						{}


						try{
							//nextRunTime = (Calendar)objInfoObject2.properties().getProperty("SI_NEXTRUNTIME").getValue();
							nextRunTime.setTime((Date) objInfoObject2.properties().getProperty("SI_NEXTRUNTIME").getValue());
						}
						catch (Exception ex)
						{}


						try{
							//nextRunTime = (Calendar)objInfoObject2.properties().getProperty("SI_NEXTRUNTIME").getValue();
							creationTime.setTime((Date) objInfoObject2.properties().getProperty("SI_CREATION_TIME").getValue());
						}
						catch (Exception ex)
						{}


						try{
							//nextRunTime = (Calendar)objInfoObject2.properties().getProperty("SI_NEXTRUNTIME").getValue();
							updateTS.setTime((Date) objInfoObject2.properties().getProperty("SI_UPDATE_TS").getValue());
						}
						catch (Exception ex)
						{}


						try{
							objPrompts = (IPrompts)objInfoObject2.getProcessingInfo().properties().getProperty("SI_PROMPTS");  //threw exception
							nbrPrompts = objPrompts.size();
							//.properties().getProperty("SI_NUM_PROMPTS").getValue();


							//Get the number of prompts - alternate way in case the other way fails

							/*iReport = (IReport) objInfoObject2;
					List paramList = iReport.getReportParameters();
					numPrompts = paramList.size();	*/
						}
						catch (Exception ex)
						{}

						try{
							ifiles = (IFiles)objInfoObject2.properties().getProperty("SI_FILES").getValue();
							outputFile = (String)ifiles.properties().getProperty("SI_PATH").getValue();	
						}
						catch (Exception ex)
						{}

						//Save the running jobs to the tracker database
						//msSQLInsertString = "insert into " + destTblNm + " (REPORT_ID, INSTANCE_ID, SI_STARTTIME,   SI_SUBMITTER, SI_SERVER, SI_OWNER, SI_SCHEDULE_STATUS,REPORT_PATH, SI_NAME, SI_CREATION_TIME, SI_NUM_PROMPTS, DATASRC_LOGIN_NM, SI_PASSWORD, DATASRC_LOGIN_NM2, SI_PASSWORD2,  SI_MACHINE_USED, SI_UPDATE_TS, DESTINATION, OUTPUT_FILE, REPORT_CUID, SI_PARENT_FOLDER_CUID, ENVIRONMENT, SCHEDULE_STATUS, SI_NEXTRUNTIME) values (" + objInfoObject2.getParentID() + ", " + objInfoObject2.getID() + ", to_date('" + startTime + "','MM/DD/YYYY HH12:MI:SS AM')" + ", '" + Helper.safeSQL((String)objInfoObject2.getSchedulingInfo().properties().getProperty("SI_SUBMITTER").getValue()) + "', '" + Helper.safeSQL(server) + "', '" + Helper.safeSQL((String)objInfoObject2.getSchedulingInfo().properties().getProperty("SI_SUBMITTER").getValue()) + "', " + objInfoObject2.properties().getProperty("SI_SCHEDULE_STATUS").getValue().toString() + ", '" + Helper.safeSQL(cookieCrumbs) + "', '" + Helper.safeSQL(objInfoObject2.getTitle()) + "', to_date('" + objInfoObject2.properties().getProperty("SI_CREATION_TIME").getValue() + "','MM/DD/YYYY HH12:MI:SS AM'), " + nbrPrompts + ",'" + logon1 + "','" + pswd1 + "', '" + logon2 + "', '" + pswd2 + "','" + machineUsed + "', to_date('" + objInfoObject2.properties().getProperty("SI_UPDATE_TS").getValue() + "','MM/DD/YYYY HH12:MI:SS AM'), '" + destination + "','" + outputFile + "', '" + objInfoObject2.getParentCUID() + "', '" + objInfoObject2.properties().getProperty("SI_PARENT_FOLDER_CUID").getValue() + "', '" + EnterpriseEnv + "','Pending', " + " to_date('" + objInfoObject2.properties().getProperty("SI_NEXTRUNTIME").getValue() + "','MM/DD/YYYY HH12:MI:SS AM'))";
						//msSQLInsertString = "insert into " + destTblNm + " (REPORT_ID, INSTANCE_ID, SI_STARTTIME,   SI_SUBMITTER, SI_SERVER, SI_OWNER, SI_SCHEDULE_STATUS,REPORT_PATH, SI_NAME, SI_CREATION_TIME, SI_NUM_PROMPTS, DATASRC_LOGIN_NM, SI_PASSWORD, DATASRC_LOGIN_NM2, SI_PASSWORD2,  SI_MACHINE_USED, SI_UPDATE_TS, DESTINATION, OUTPUT_FILE, REPORT_CUID, SI_PARENT_FOLDER_CUID, ENVIRONMENT, SCHEDULE_STATUS, SI_NEXTRUNTIME) values (" + objInfoObject2.getParentID() + ", " + objInfoObject2.getID() + ", to_date('" + startTime + "','MM/DD/YYYY HH12:MI:SS AM')" + ", '" + Helper.safeSQL((String)objInfoObject2.getSchedulingInfo().properties().getProperty("SI_SUBMITTER").getValue()) + "', '" + Helper.safeSQL(server) + "', '" + Helper.safeSQL((String)objInfoObject2.getSchedulingInfo().properties().getProperty("SI_SUBMITTER").getValue()) + "', " + objInfoObject2.properties().getProperty("SI_SCHEDULE_STATUS").getValue() + ", '" + Helper.safeSQL(cookieCrumbs) + "', '" + Helper.safeSQL(objInfoObject2.getTitle()) + "', to_date('" + objInfoObject2.properties().getProperty("SI_CREATION_TIME").getValue() + "','MM/DD/YYYY HH12:MI:SS AM'), " + nbrPrompts + ",'" + logon1 + "','" + pswd1 + "', '" + logon2 + "', '" + pswd2 + "','" + machineUsed + "', to_date('" + objInfoObject2.properties().getProperty("SI_UPDATE_TS").getValue() + "','MM/DD/YYYY HH12:MI:SS AM'), '" + destination + "','" + outputFile + "', '" + objInfoObject2.getParentCUID() + "', '" + objInfoObject2.properties().getProperty("SI_PARENT_FOLDER_CUID").getValue() + "', '" + EnterpriseEnv + "','Pending', " + " to_date('" + objInfoObject2.properties().getProperty("SI_NEXTRUNTIME").getValue() + "','MM/DD/YYYY HH12:MI:SS AM'))";


						msSQLInsertString = "insert into " + destTblNm + " (REPORT_ID, INSTANCE_ID, SI_STARTTIME,   SI_SUBMITTER, SI_SERVER, SI_OWNER, SI_SCHEDULE_STATUS,REPORT_PATH, SI_NAME, SI_CREATION_TIME, SI_NUM_PROMPTS, DATASRC_LOGIN_NM, SI_PASSWORD, DATASRC_LOGIN_NM2, SI_PASSWORD2,  SI_MACHINE_USED, SI_UPDATE_TS, DESTINATION, OUTPUT_FILE, REPORT_CUID, SI_PARENT_FOLDER_CUID, ENVIRONMENT, SCHEDULE_STATUS, SI_NEXTRUNTIME) values (" + objInfoObject2.getParentID() + ", " + objInfoObject2.getID() + ", ('" + Helper.msSQLDateTime(startTime) + "')" + ", '" + Helper.safeSQL((String)objInfoObject2.getSchedulingInfo().properties().getProperty("SI_SUBMITTER").getValue()) + "', '" + Helper.safeSQL(server) + "', '" + Helper.safeSQL((String)objInfoObject2.getSchedulingInfo().properties().getProperty("SI_SUBMITTER").getValue()) + "', " + objInfoObject2.properties().getProperty("SI_SCHEDULE_STATUS").getValue().toString() + ", '" + Helper.safeSQL(cookieCrumbs) + "', '" + Helper.safeSQL(objInfoObject2.getTitle()) + "', ('" + Helper.msSQLDateTime(creationTime) + "'), " + nbrPrompts + ",'" + logon1 + "','" + pswd1 + "', '" + logon2 + "', '" + pswd2 + "','" + machineUsed + "', ('" + Helper.msSQLDateTime(updateTS) + "'), '" + destination + "','" + outputFile + "', '" + objInfoObject2.getParentCUID() + "', '" + objInfoObject2.properties().getProperty("SI_PARENT_FOLDER_CUID").getValue() + "', '" + EnterpriseEnv + "','Running', " + " ('" + Helper.msSQLDateTime(nextRunTime) + "'))";
						//System.out.println(msSQLInsertString);


						try
						{
							System.out.println(msSQLInsertString);
							//just comment out while testing
							Helper.runMSSQLInsertQuery(msSQLInsertString);
						}
						catch (Exception ex)
						{

							String errMsg = ex.getMessage();
							String errStackTrace = ex.getStackTrace().toString();

							errMsg = errMsg.replace("\r", " "); //get NullPointException here.
							errStackTrace = errStackTrace.replace("\n", " ");

							System.out.println(ex.getMessage() + " " + ex.getStackTrace());
							System.out.println("insert into STG1_BOXI_TRACKER_ERRORS " + errMsg + " " + errStackTrace + " " + EnterpriseEnv + "', GETDATE()");
							//for this to work you must replace any line feed carriage returns with dashes
							//carriage return is '\r' and line feed is '\n'
							Helper.runMSSQLInsertQuery("insert into STG1_BOXI_TRACKER_ERRORS " + errMsg + " " + errStackTrace + " " + EnterpriseEnv + "', GETDATE()");
							//'sendmail("Logon error", ex.getMessage() + " " + ex.getStackTrace)
							//Helper.sendmail("bishopp@lexmark.com", "bishopp@lexmark.com, amarabes@lexmark.com,msankar@lexmark.com,ajain@lexmark.com", "Logon error - load_stg1_migr_XI_reports(String, String, String, String, String) - " + EnterpriseEnv, ex.getMessage() + " " + ex.getStackTrace());
							//Emailer.sendEmail("bishopp@lexmark.com", "bishopp@lexmark.com", "dadadadaaaaaaaa Error with BOXI Alerter - currjobs - " + EnterpriseEnv, errMsg + " " + errStackTrace, null);
							//Emailer.sendEmail("bishopp@lexmark.com", "bishopp@lexmark.com,maaftab@lexmark.com, amarabes@lexmark.com,msankar@lexmark.com,arghosh@lexmark.com", "Error with BOXI Alerter - currjobs - " + EnterpriseEnv, errMsg + " " + errStackTrace, null);
							Emailer.sendEmail("bishopp@lexmark.com", "bishopp@lexmark.com", "Error with BOXI Alerter - currjobs - " + EnterpriseEnv, errMsg + " " + errStackTrace, null);


						}
					}

					System.out.println("Line 1161, Running job count is " + runningCount);

					if (long_running_flg == 0){
						emailSubject = "Pending Jobs on " + EnterpriseEnv + "  (any report)";
					}
					else if (long_running_flg == 1) {
						emailSubject = "Pending Jobs on " + EnterpriseEnv + "  (designated long-running reports only)";
					}
					else
						emailSubject = "Pending Jobs on " + EnterpriseEnv + "  (any not designated as long-running reports only)";
					//***********PRODUCTION FILE***************
					BufferedReader input = new BufferedReader(new FileReader("c:\\emails.txt"));

					//***********THIS IS A TEST FILE****************
					//BufferedReader input = new BufferedReader(new FileReader("c:\\users\\bishopp\\emails.txt"));
					emails = input.readLine();

					Emailer.sendEmail("bishopp@lexmark.com",emails,emailSubject, "There are " + StuckPendingCount + " pending jobs and " + runningCount + " running jobs.",  null);
					//Emailer.sendEmail("bishopp@lexmark.com","bishopp@lexmark.com","stuPending Jobs on " + EnterpriseEnv + " ", "There are " + StuckPendingCount + " pending jobs and " + runningCount + " running jobs.",  null);

					// Clean up the Enterprise Session.
					if(objEnterpriseSession != null) {
						try {
							objEnterpriseSession.logoff();

						} catch(Exception e_ignore_in_cleanup) {}
					}
				}

			}

		}
		catch (Exception ex)
		{
			//String errMsg = ex.getMessage();
			String errMsg = "";
			String errStackTrace = ex.getStackTrace().toString();

			if (ex.getMessage() == null) {
				errMsg = "Though there was an exception caught, ex.getMessage() was null and there is no error message";
				System.out.println(errMsg);
			}
			else
			{
				errMsg = errMsg.replace("\r", " "); //get NullPointException here. Is errMsg not getting set??
				errStackTrace = errStackTrace.replace("\n", " ");

				System.out.println(ex.getMessage() + " " + ex.getStackTrace().toString());
				System.out.println("insert into STG1_BOXI_TRACKER_ERRORS '" + errMsg.toString() + " " + errStackTrace.toString() + " " + EnterpriseEnv + "', GETDATE()");
				//for this to work you must replace any line feed carriage returns with dashes
				//carriage return is '\r' and line feed is '\n'
				Helper.runMSSQLInsertQuery("insert into STG1_BOXI_TRACKER_ERRORS '" + errMsg.toString() + " " + errStackTrace.toString() + " " + EnterpriseEnv + "', GETDATE()");

			}
			//'sendmail("Logon error", ex.getMessage() + " " + ex.getStackTrace)
			//Helper.sendmail("bishopp@lexmark.com", "bishopp@lexmark.com, amarabes@lexmark.com,msankar@lexmark.com,ajain@lexmark.com", "Logon error - load_stg1_migr_XI_reports(String, String, String, String, String) - " + EnterpriseEnv, ex.getMessage() + " " + ex.getStackTrace());
			Emailer.sendEmail("bishopp@lexmark.com", "bishopp@lexmark.com", "Error with BOXI Alerter - currjobs - " + EnterpriseEnv, errMsg + " " + errStackTrace, null);

		}


	}
	public static void load_stg1_boxi_CurrJobs(String EnterpriseEnv, String login, String pswd,String auth, String destTblNm){
		//Record the jobs currently running and any that are pending

		IEnterpriseSession objEnterpriseSession;
		//IInfoStore objInfoStore;
		IInfoObjects colInfoObjects;
		IInfoObject objInfoObject;
		IInfoObjects colInfoObjects2;
		IInfoObject objInfoObject2;
		String errorMsg = "";
		String cookieCrumbs = "";
		int count;
		String si_owner;		
		long theInterval = 0;
		Calendar startDate;
		Calendar endDate;
		String dtToday;
		String msSQLInsertString;
		String maxID ="0";
		String logon1 = "unknown";
		String logon2 = "unknown";
		String pswd1 = "unknown";
		String pswd2 = "unknown";
		String datasrc1 = "not found in CMS";
		String datasrc2 = "not found in CMS";
		int numPrompts;
		Calendar maxEndDate;
		String destination = "";
		String outputFile = "";
		String distributionList = "";
		String si_file1 = "not set";
		String strInfoSQL;
		Calendar maxCreationDate;
		//java.sql.Date maxCreationDate;
		int i = 0;
		IFiles ifiles;
		String scheduleStatus;
		IProcessingInfo processingInfo;
		IReport iReport;
		String server = "";
		String server1 = "";
		String server2 = "";
		int recordCount;
		int runningCount;
		String runningCountMsg;
		int PendingCount = 0;
		int StuckPendingCount = 0;
		int duration;
		String machineUsed;
		Calendar runDate = Calendar.getInstance();
		Calendar startTime = Calendar.getInstance();
		Calendar endTime = Calendar.getInstance();
		Calendar nextRunTime = Calendar.getInstance();
		Calendar currentTime = Calendar.getInstance();
		Calendar creationTime = Calendar.getInstance();
		Calendar updateTS = Calendar.getInstance();
		Calendar oldDate = new java.util.GregorianCalendar(1979, 01, 30);

		Calendar now;
		int nbrPrompts = 0;
		IPrompts objPrompts;
		int longRunningCount = 0;
		String oraSelectString = "";
		String targetTable = "";
		int longRunningQ = 0;
		String serverUsed = "";
		int delayInMin = 0;
		int cmcJobServer1Count = 0;
		int cmcJobServer2Count = 0;
		int cmcJobServer3Count = 0;
		int cmcJobServer4Count = 0;
		//int totalRunningCount = 0;
		int totalRunningCount;
		String emailSubject = "";
		int cmsJobServer1Count = 0;
		int cmsJobServer2Count = 0;
		int cmsJobServer3Count = 0;
		int cmsJobServer4Count = 0;
		IPrompts siPrompts;
		String paramValues = "";
		IReport oReport;
		String emails;
		//IStrings serverNames;
		long difference;

		try 
		{

			if (EnterpriseEnv =="PASHWBOBJ013") {
				targetTable = "STG1_bo4p_creports";
			}
			else if (EnterpriseEnv == "DASHWBOBJ011") {
				targetTable = "STG1_bo4d_creports";
			}
			else if (EnterpriseEnv == "QASHWBOBJ013") {
				targetTable = "STG1_bo4q_creports";
			}
			else if (EnterpriseEnv == "DASHWBOBJ012"){
				targetTable = "STG1_bo4v_creports";	
			}

			ISessionMgr  objSessionMgr = CrystalEnterprise.getSessionMgr();
			objEnterpriseSession = objSessionMgr.logon(login, pswd, EnterpriseEnv, auth);    
			IInfoStore objInfoStore = (IInfoStore) objEnterpriseSession.getService("InfoStore");
			//IReportAppFactory rptAppFactory = (IReportAppFactory)objEnterpriseSession.getService( "", "RASReportService" ) ;


			//Get the number of running jobs
			strInfoSQL = "select * from ci_infoobjects where si_schedule_status=0 and si_new_job_id != ''";
			colInfoObjects2 = (IInfoObjects)objInfoStore.query(strInfoSQL);
			runningCount = colInfoObjects2.size();
			System.out.println("Running count is " + runningCount);

			for (int x=0; x < runningCount;x++){  //this was x < recordCount, which is the same as pendingCount - NOT RIGHT!
				objInfoObject2 = (IInfoObject)colInfoObjects2.get(x);

				//Get the running count for each job server (according to the CMS query, not the CMC -> that's done below)
				serverUsed = (String)objInfoObject2.getSchedulingInfo().properties().getProperty("SI_MACHINE_USED").getValue();

				System.out.println("Job server used:  " + serverUsed);
				/*if (serverUsed.equals("PROD015.AdaptiveJobServer1") || serverUsed.equals("PROD015.AdaptiveJobServer2") || serverUsed.equals("PROD015.CrystalReports2013ProcessingServer") || serverUsed.equals("PROD015.CrystalReports2013ReportApplicationServer")  || serverUsed.equals("PROD015.CrystalReportsCacheServer")  || serverUsed.equals("PROD015.CrystalReportsProcessingServer") || serverUsed.equals("QA015.AdaptiveJobServer1") || serverUsed.equals("QA015.AdaptiveJobServerCR") || serverUsed.equals("DEV.AdaptiveJobServer") || serverUsed.equals("DEV.AdaptiveJobServerCR") || serverUsed.equals("DEV.AdaptiveJobServerLCM") || serverUsed.equals("DEV.AdaptiveJobServerNew") || serverUsed.equals("VALIDATION.AdaptiveJobServer") ){
					cmsJobServer1Count +=1;
				}
				else if (serverUsed.equals("PROD016.AdaptiveJobServer") || serverUsed.equals("PROD016.AdaptiveJobServerCR") || serverUsed.equals("QA016.AdaptiveJobServer") || serverUsed.equals("QA016.AdaptiveJobServerCR")){
					cmsJobServer2Count +=1;
				}
				else if (serverUsed.equals("PROD017.AdaptiveJobServer") || serverUsed.equals("PROD017.AdaptiveJobServerCR") || serverUsed.equals("QA017.AdaptiveJobServer") || serverUsed.equals("QA017.AdaptiveJobServerCR")){
					cmsJobServer3Count +=1;
				}
				else if (serverUsed.equals("PROD018.AdaptiveJobServer") || serverUsed.equals("PROD018.AdaptiveJobServerCR") || serverUsed.equals("QA018.AdaptiveJobServer") || serverUsed.equals("QA018.AdaptiveJobServerCR")){
					cmsJobServer4Count +=1;
				} */

				if (serverUsed.contains("PROD015") || serverUsed.contains("QA0015") || serverUsed.contains("DEV") || serverUsed.contains("VALIDATION")) {
					cmsJobServer1Count +=1;
				}
				else if (serverUsed.contains("PROD016") || serverUsed.contains("QA0016"))  {
					cmsJobServer2Count +=1;
				}
				else if (serverUsed.contains("PROD017") || serverUsed.contains("QA0017"))  {
					cmsJobServer3Count +=1;
				}
				else if  (serverUsed.contains("PROD018") || serverUsed.contains("QA0018"))  {
					cmsJobServer4Count +=1;
				}

				//System.out.println("job server used:  " + serverUsed);
				//As of Oct 17, 2012, we don't have a long-running queue set up in BI4
				if (serverUsed.indexOf("BRP02",0) > 0){
					longRunningQ +=1;
				}

				try {
					runDate.setTime((Date) objInfoObject2.properties().getProperty("SI_STARTTIME").getValue());
					startTime = runDate;	
				}

				catch (Exception ex){
					//set a default starttime
					//System.out.println("Default startTime is" + startTime.getTime());

				}

				try {
					endTime.setTime((Date) objInfoObject2.properties().getProperty("SI_ENDTIME").getValue());	
				}
				catch (Exception ex){
					//set a default endtime
					//System.out.println("Default end time is " + endTime.getTime());
				}


				try{	                    
					machineUsed = objInfoObject2.properties().getProperty("SI_MACHINE_USED").getValue().toString();
				}
				catch (Exception ex)
				{
					machineUsed = "unk";
				}


				try{
					destination = objInfoObject2.getSchedulingInfo().getDestination().getName();
					destination = objInfoObject2.getSchedulingInfo().getDestinations().get(0).toString();
					if (destination =="CrystalEntprise.Smtp") {	                		
						distributionList = Helper.getDistributionList(EnterpriseEnv, objInfoObject2.getID());		                		   
					}

				}
				catch (Exception ex)
				{}

				//Get the logon info
				/*ReportClientDocument rcd = null ;
				try {
					// open the report
					rcd = rptAppFactory.openDocument( (IInfoObject)colInfoObjects2.get(x), 0, Locale.ENGLISH ) ;
					// Get the server names and show them
					serverNames = rcd.getDatabaseController().getServerNames() ;
					if ( serverNames.size() == 0 ){
						System.out.println( "No datasources found." ) ;
						server1 = "none found";
						server2 = "none found";
					}
					else {
						if ( serverNames.size() > 1 ){
							// multiple datasources					            	
							for ( int d=0; d < serverNames.size(); d++ ) {
								System.out.println( serverNames.getString(d)  ) ;
								if (d < 0) {
									server1 = serverNames.getString(d);
								}
								else
									server2 = serverNames.getString(d);
							}
						}
						else {
							// just one datasource
							server1 =  serverNames.getString(0);
							System.out.println( serverNames.getString(0) ) ;
						}
					}
					rcd.close();
					rcd = null;
				}
				catch (Exception ex){
					System.out.println("Could be a CR4E document " + objInfoObject2.getKind() + " " + ex.getMessage() + " " + ex.getStackTrace());
					server1 = "none found";
					server2 = "none found";
				}*/

				/*// Get the SI_LOGON_INFO property bag
				try {
					IProperties boProcessingInfo = (IProperties) objInfoObject2.getProcessingInfo().properties();

					IProperties logonProperties = (IProperties) boProcessingInfo.getProperty("SI_LOGON_INFO").getValue();
					if (logonProperties != null) {
						IProperties silogonProperties = (IProperties) logonProperties.getProperty("SI_LOGON1").getValue();

						System.out.println("SI_LOGON1 size is " + silogonProperties.size());
						//16, it's true
						String numLogins = logonProperties.getProperty("SI_NUM_LOGONS").toString();

						System.out.println("Num logons is " + numLogins);

						//Get the db logon credentials 
						int instanceID = objInfoObject2.getID();
						System.out.println("instance id is " + instanceID);
						for (int h = 0; h < Integer.parseInt(numLogins); h++) {	
							//IReportLogon dbLogon = (IReportLogon)silogonProperties.get(h);
							if (h == 0){

								server1 = silogonProperties.getProperty("SI_SERVER").toString();
								logon1 = silogonProperties.getProperty("SI_USER").toString();
							}
							if (h ==1) {
								server2 = silogonProperties.getProperty("SI_SERVER").toString();
								logon2 = silogonProperties.getProperty("SI_USER").toString();
							}							
						}

					}
				}
				catch (Exception ex){
					System.out.println("Error getting logon properties " + ex.getMessage() + " " + ex.getStackTrace());
				}*/



				try{
					si_owner = (String)objInfoObject2.properties().getProperty("SI_OWNER").getValue();
				}
				catch (Exception ex)
				{}


				try{
					//nextRunTime = (Calendar)objInfoObject2.properties().getProperty("SI_NEXTRUNTIME").getValue();
					nextRunTime.setTime((Date) objInfoObject2.properties().getProperty("SI_NEXTRUNTIME").getValue());
					System.out.println("Next run time is " + Helper.msSQLDateTime(nextRunTime));
				}
				catch (Exception ex)
				{}


				try{
					creationTime.setTime((Date) objInfoObject2.properties().getProperty("SI_CREATION_TIME").getValue());
				}
				catch (Exception ex)
				{}


				try{
					//nextRunTime = (Calendar)objInfoObject2.properties().getProperty("SI_NEXTRUNTIME").getValue();
					updateTS.setTime((Date) objInfoObject2.properties().getProperty("SI_UPDATE_TS").getValue());
				}
				catch (Exception ex)
				{}

				try {
					
				// First get the Processing Info properties for the InfoObject.
				IProperties boProcessingInfo = (IProperties) objInfoObject2.getProcessingInfo().properties();

				// Make sure that there is processing info set for the InfoObject.
				if (boProcessingInfo != null)
				{
					// Get the SI_PROMPTS property bag from the InfoObject's Processing Info
					IProperties Prompts = (IProperties) boProcessingInfo.getProperty("SI_PROMPTS").getValue();

					// Make sure that there are parameters.
					if (Prompts != null)
					{
						// Get the number of prompts (the number of parameters)
						numPrompts = ((Integer)Prompts.getProperty("SI_NUM_PROMPTS").getValue()).intValue();
					}

					// Get the SI_LOGON_INFO property bag
					IProperties logonProperties = (IProperties) boProcessingInfo.getProperty("SI_LOGON_INFO").getValue();
					if (logonProperties != null) {
						IProperties silogonProperties = (IProperties) logonProperties.getProperty("SI_LOGON1").getValue();

						System.out.println("SI_LOGON1 size is " + silogonProperties.size());
						//16, it's true
						String numLogins = logonProperties.getProperty("SI_NUM_LOGONS").toString();

						System.out.println("Num logons is " + numLogins);

						//Get the db logon credentials 
						int instanceID = objInfoObject2.getID();
						System.out.println("instance id is " + instanceID);
						for (int h = 0; h < Integer.parseInt(numLogins); h++) {	
							//IReportLogon dbLogon = (IReportLogon)silogonProperties.get(h);
							if (h == 0){

								server1 = silogonProperties.getProperty("SI_SERVER").toString();
								logon1 = silogonProperties.getProperty("SI_USER").toString();
							}
							if (h ==1) {
								server2 = silogonProperties.getProperty("SI_SERVER").toString();
								logon2 = silogonProperties.getProperty("SI_USER").toString();
							}							
						}

					}
				}
				server = server1;
			}
			catch (Exception ex)
			{
				System.out.println("Error getting prompts " + ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getCause() + " " + ex.getStackTrace());
			}

				/*try{
					objPrompts = (IPrompts)objInfoObject2.getProcessingInfo().properties().getProperty("SI_PROMPTS");  //threw exception
					nbrPrompts = objPrompts.size();
					//.properties().getProperty("SI_NUM_PROMPTS").getValue();


					//Get the number of prompts - alternate way in case the other way fails

					iReport = (IReport) objInfoObject2;
					List paramList = iReport.getReportParameters();
					numPrompts = paramList.size();	
				}
				catch (Exception ex)
				{
					System.out.println("Error getting prompts " + ex.getMessage() + " " + ex.getStackTrace());
				}*/

				try{
					ifiles = (IFiles)objInfoObject2.properties().getProperty("SI_FILES").getValue();
					outputFile = (String)ifiles.properties().getProperty("SI_PATH").getValue();	
				}
				catch (Exception ex)
				{}

				//Get the current duration
				try {
					//Determine the current duration:  current time minus start time
					//startTime.setTime((Date) objInfoObject.properties().getProperty("SI_STARTTIME").getValue());
					now = Calendar.getInstance();

					//long difference =  currentTime.getTimeInMillis() - nextRunTime.getTimeInMillis();
					difference =  now.getTimeInMillis() - startTime.getTimeInMillis();
					//System.out.println("The difference is " + difference);
					System.out.println("The start time year is " + nextRunTime.YEAR);

					int days = (int) ((double) difference)/1000/60/60/24;
					long days_millis = days*1000*60*60*24;
					int hours = (int) ((double) (difference-days_millis))/1000/60/60;
					long hours_millis = hours*1000*60*60;
					int mins = (int) ((double) (difference-days_millis-hours_millis))/1000/60;
					long mins_millis = mins*1000*60;
					int secs = (int) ((double) (difference-days_millis-hours_millis-mins_millis))/1000;
					long secs_millis = secs*1000;
					int millis = (int) (difference-days_millis-hours_millis-mins_millis-secs_millis);

					System.out.print("Difference: " + (difference) + " milliseconds ==> " + days + " days " + hours + " hours " + mins + " mins " + secs + " secs " + millis + " millis");

					//Save the running jobs to the tracker database
					//msSQLInsertString = "insert into " + destTblNm + " (REPORT_ID, INSTANCE_ID, SI_STARTTIME,   SI_SUBMITTER, SI_SERVER, SI_OWNER, SI_SCHEDULE_STATUS,REPORT_PATH, SI_NAME, SI_CREATION_TIME, SI_NUM_PROMPTS, DATASRC_LOGIN_NM, SI_PASSWORD, DATASRC_LOGIN_NM2, SI_PASSWORD2,  SI_MACHINE_USED, SI_UPDATE_TS, DESTINATION, OUTPUT_FILE, REPORT_CUID, SI_PARENT_FOLDER_CUID, ENVIRONMENT, SCHEDULE_STATUS, SI_NEXTRUNTIME) values (" + objInfoObject2.getParentID() + ", " + objInfoObject2.getID() + ", to_date('" + startTime + "','MM/DD/YYYY HH12:MI:SS AM')" + ", '" + Helper.safeSQL((String)objInfoObject2.getSchedulingInfo().properties().getProperty("SI_SUBMITTER").getValue()) + "', '" + Helper.safeSQL(server) + "', '" + Helper.safeSQL((String)objInfoObject2.getSchedulingInfo().properties().getProperty("SI_SUBMITTER").getValue()) + "', " + objInfoObject2.properties().getProperty("SI_SCHEDULE_STATUS").getValue().toString() + ", '" + Helper.safeSQL(cookieCrumbs) + "', '" + Helper.safeSQL(objInfoObject2.getTitle()) + "', to_date('" + objInfoObject2.properties().getProperty("SI_CREATION_TIME").getValue() + "','MM/DD/YYYY HH12:MI:SS AM'), " + nbrPrompts + ",'" + logon1 + "','" + pswd1 + "', '" + logon2 + "', '" + pswd2 + "','" + machineUsed + "', to_date('" + objInfoObject2.properties().getProperty("SI_UPDATE_TS").getValue() + "','MM/DD/YYYY HH12:MI:SS AM'), '" + destination + "','" + outputFile + "', '" + objInfoObject2.getParentCUID() + "', '" + objInfoObject2.properties().getProperty("SI_PARENT_FOLDER_CUID").getValue() + "', '" + EnterpriseEnv + "','Pending', " + " to_date('" + objInfoObject2.properties().getProperty("SI_NEXTRUNTIME").getValue() + "','MM/DD/YYYY HH12:MI:SS AM'))";
					//msSQLInsertString = "insert into " + destTblNm + " (REPORT_ID, INSTANCE_ID, SI_STARTTIME,   SI_SUBMITTER, SI_SERVER, SI_OWNER, SI_SCHEDULE_STATUS,REPORT_PATH, SI_NAME, SI_CREATION_TIME, SI_NUM_PROMPTS, DATASRC_LOGIN_NM, SI_PASSWORD, DATASRC_LOGIN_NM2, SI_PASSWORD2,  SI_MACHINE_USED, SI_UPDATE_TS, DESTINATION, OUTPUT_FILE, REPORT_CUID, SI_PARENT_FOLDER_CUID, ENVIRONMENT, SCHEDULE_STATUS, SI_NEXTRUNTIME) values (" + objInfoObject2.getParentID() + ", " + objInfoObject2.getID() + ", to_date('" + startTime + "','MM/DD/YYYY HH12:MI:SS AM')" + ", '" + Helper.safeSQL((String)objInfoObject2.getSchedulingInfo().properties().getProperty("SI_SUBMITTER").getValue()) + "', '" + Helper.safeSQL(server) + "', '" + Helper.safeSQL((String)objInfoObject2.getSchedulingInfo().properties().getProperty("SI_SUBMITTER").getValue()) + "', " + objInfoObject2.properties().getProperty("SI_SCHEDULE_STATUS").getValue() + ", '" + Helper.safeSQL(cookieCrumbs) + "', '" + Helper.safeSQL(objInfoObject2.getTitle()) + "', to_date('" + objInfoObject2.properties().getProperty("SI_CREATION_TIME").getValue() + "','MM/DD/YYYY HH12:MI:SS AM'), " + nbrPrompts + ",'" + logon1 + "','" + pswd1 + "', '" + logon2 + "', '" + pswd2 + "','" + machineUsed + "', to_date('" + objInfoObject2.properties().getProperty("SI_UPDATE_TS").getValue() + "','MM/DD/YYYY HH12:MI:SS AM'), '" + destination + "','" + outputFile + "', '" + objInfoObject2.getParentCUID() + "', '" + objInfoObject2.properties().getProperty("SI_PARENT_FOLDER_CUID").getValue() + "', '" + EnterpriseEnv + "','Pending', " + " to_date('" + objInfoObject2.properties().getProperty("SI_NEXTRUNTIME").getValue() + "','MM/DD/YYYY HH12:MI:SS AM'))";

					//msSQLInsertString = "insert into " + destTblNm + " (REPORT_ID, INSTANCE_ID, SI_STARTTIME,   SI_SUBMITTER, SI_SERVER, SI_OWNER, SI_SCHEDULE_STATUS,REPORT_PATH, SI_NAME, SI_CREATION_TIME, SI_NUM_PROMPTS, DATASRC_LOGIN_NM, SI_PASSWORD, DATASRC_LOGIN_NM2, SI_PASSWORD2,  SI_MACHINE_USED, SI_UPDATE_TS, DESTINATION, OUTPUT_FILE, REPORT_CUID, SI_PARENT_FOLDER_CUID, ENVIRONMENT, SCHEDULE_STATUS, SI_NEXTRUNTIME, PARAM_VALUES) values (" + objInfoObject2.getParentID() + ", " + objInfoObject2.getID() + ", to_date('" + Helper.oracleDateTime(startTime) + "','DD-MM-YYYY HH24:MI:SS')" + ", '" + Helper.safeSQL((String)objInfoObject2.getSchedulingInfo().properties().getProperty("SI_SUBMITTER").getValue()) + "', '" + Helper.safeSQL(server) + "', '" + Helper.safeSQL((String)objInfoObject2.getSchedulingInfo().properties().getProperty("SI_SUBMITTER").getValue()) + "', " + objInfoObject2.properties().getProperty("SI_SCHEDULE_STATUS").getValue().toString() + ", '" + Helper.safeSQL(cookieCrumbs) + "', '" + Helper.safeSQL(objInfoObject2.getTitle()) + "', to_date('" + Helper.oracleDateTime(creationTime) + "','DD-MM-YYYY HH24:MI:SS'), " + nbrPrompts + ",'" + logon1 + "','" + pswd1 + "', '" + logon2 + "', '" + pswd2 + "','" + machineUsed + "', to_date('" + Helper.oracleDateTime(updateTS) + "','DD-MM-YYYY HH24:MI:SS'), '" + destination + "','" + outputFile + "', '" + objInfoObject2.getParentCUID() + "', '" + objInfoObject2.properties().getProperty("SI_PARENT_FOLDER_CUID").getValue() + "', '" + EnterpriseEnv + "','Running', " + " to_date('" + Helper.oracleDateTime(nextRunTime) + "','DD-MM-YYYY HH24:MI:SS'),' " + paramValues +"' )";
					msSQLInsertString = "insert into " + destTblNm + " (REPORT_ID, INSTANCE_ID, SI_STARTTIME, DURATION,  SI_SUBMITTER, SI_SERVER, SI_OWNER, SI_SCHEDULE_STATUS,REPORT_PATH, SI_NAME, SI_CREATION_TIME, SI_NUM_PROMPTS, DATASRC_LOGIN_NM, SI_PASSWORD, DATASRC_LOGIN_NM2, SI_PASSWORD2,  SI_MACHINE_USED, SI_UPDATE_TS, DESTINATION, OUTPUT_FILE, REPORT_CUID, SI_PARENT_FOLDER_CUID, ENVIRONMENT, SCHEDULE_STATUS, SI_NEXTRUNTIME, PARAM_VALUES) values (" + objInfoObject2.getParentID() + ", " + objInfoObject2.getID() + ", ('" + Helper.msSQLDateTime(startTime) + "')," + hours + ", '" + Helper.safeSQL((String)objInfoObject2.getSchedulingInfo().properties().getProperty("SI_SUBMITTER").getValue()) + "', '" + Helper.safeSQL(server) + "', '" + Helper.safeSQL((String)objInfoObject2.getSchedulingInfo().properties().getProperty("SI_SUBMITTER").getValue()) + "', " + objInfoObject2.properties().getProperty("SI_SCHEDULE_STATUS").getValue().toString() + ", '" + Helper.safeSQL(cookieCrumbs) + "', '" + Helper.safeSQL(objInfoObject2.getTitle()) + "', ('" + Helper.msSQLDateTime(creationTime) + "'), " + nbrPrompts + ",'" + logon1 + "','" + pswd1 + "', '" + logon2 + "', '" + pswd2 + "','" + machineUsed + "', ('" + Helper.msSQLDateTime(updateTS) + "'), '" + destination + "','" + outputFile + "', '" + objInfoObject2.getParentCUID() + "', '" + objInfoObject2.properties().getProperty("SI_PARENT_FOLDER_CUID").getValue() + "', '" + EnterpriseEnv + "','Running', " + " '" + Helper.msSQLDateTime(nextRunTime) + "',' " + paramValues +"' )";

					System.out.println(msSQLInsertString);

					//System.out.println(msSQLInsertString);
					//just comment out while testing
					Helper.runMSSQLInsertQuery(msSQLInsertString);
				}
				catch (Exception ex)
				{

					String errMsg = ex.getMessage();
					String errStackTrace = ex.getStackTrace().toString();

					errMsg = errMsg.replace("\r", " "); //get NullPointException here.
					errStackTrace = errStackTrace.replace("\n", " ");

					System.out.println(ex.getMessage() + " " + ex.getStackTrace());
					System.out.println("insert into STG1_BOXI_TRACKER_ERRORS '" + errMsg + " " + errStackTrace + " " + EnterpriseEnv + "', GETDATE()");
					//for this to work you must replace any line feed carriage returns with dashes
					//carriage return is '\r' and line feed is '\n'
					Helper.runMSSQLInsertQuery("insert into STG1_BOXI_TRACKER_ERRORS '" + errMsg + " " + errStackTrace + " " + EnterpriseEnv + "', GETDATE()");
					//'sendmail("Logon error", ex.getMessage() + " " + ex.getStackTrace)
					//Helper.sendmail("bishopp@lexmark.com", "bishopp@lexmark.com, amarabes@lexmark.com,msankar@lexmark.com,ajain@lexmark.com", "Logon error - load_stg1_migr_XI_reports(String, String, String, String, String) - " + EnterpriseEnv, ex.getMessage() + " " + ex.getStackTrace());
					//Emailer.sendEmail("bishopp@lexmark.com", "bishopp@lexmark.com", "dadadadaaaaaaaa Error with BOXI Alerter - currjobs  - " + EnterpriseEnv, errMsg + " " + errStackTrace, null);
					Emailer.sendEmail("bishopp@lexmark.com", "bishopp@lexmark.com", "Error with BOXI Alerter - currjobs - " + EnterpriseEnv, errMsg + " " + errStackTrace, null);

				}
			}

			//System.out.println("Line 651, Running job count is " + runningCount);

			//Since there currently is a discrepancy between the count of running jobs on CMS versus CMC, this is a work-around
			//to get the count from the CMC till there is a fix.
			if (EnterpriseEnv =="PASHWBOBJ013") {
				cmcJobServer1Count = getCMCRunningCRJobs("PASHWBOBJ013", login, pswd, auth, "PROD015.AdaptiveJobServer1");
				cmcJobServer1Count +=getCMCRunningCRJobs("PASHWBOBJ013", login, pswd, auth, "PROD015.AdaptiveJobServer2");
				//cmcJobServer1Count +=getCMCRunningCRJobs("PASHWBOBJ013", login, pswd, auth, "PROD015.CrystalReports2013ProcessingServer");
				//cmcJobServer1Count +=getCMCRunningCRJobs("PASHWBOBJ013", login, pswd, auth, "PROD015.CrystalReports2013ReportApplicationServer");
				//cmcJobServer1Count +=getCMCRunningCRJobs("PASHWBOBJ013", login, pswd, auth, "PROD015.CrystalReportsCacheServer");
				cmcJobServer2Count = getCMCRunningCRJobs("PASHWBOBJ013", login, pswd, auth, "PROD016.AdaptiveJobServer1");
				cmcJobServer2Count +=getCMCRunningCRJobs("PASHWBOBJ013", login, pswd, auth, "PROD016.AdaptiveJobServer2");
				//cmcJobServer2Count +=getCMCRunningCRJobs("PASHWBOBJ013", login, pswd, auth, "PROD016.CrystalReports2013ProcessingServer");
				//cmcJobServer2Count +=getCMCRunningCRJobs("PASHWBOBJ013", login, pswd, auth, "PROD016.CrystalReports2013ReportApplicationServer");
				cmcJobServer3Count = getCMCRunningCRJobs("PASHWBOBJ013", login, pswd, auth, "PROD017.AdaptiveJobServer1");
				cmcJobServer3Count +=getCMCRunningCRJobs("PASHWBOBJ013", login, pswd, auth, "PROD017.AdaptiveJobServer2");
				cmcJobServer4Count = getCMCRunningCRJobs("PASHWBOBJ013", login, pswd, auth, "PROD018.AdaptiveJobServer1");
				cmcJobServer4Count +=getCMCRunningCRJobs("PASHWBOBJ013", login, pswd, auth, "PROD018.AdaptiveJobServer2");
				totalRunningCount = cmcJobServer1Count + cmcJobServer2Count + cmcJobServer3Count + cmcJobServer4Count;
				//System.out.println("Total running count according to CMC:  " + totalRunningCount);
				//emailSubject = "There are " + StuckPendingCount + " pending jobs, " + longRunningCount + " of which are designated long-running reports, and " + totalRunningCount + " running jobs, "  + cmcJobServer2Count + " jobs are running on USLEXBRP02, " + cmcJobServer1Count + " running on USLEXBRP01 and " + cmcJobServer3Count + " running on USLEXBRP03.";
				emailSubject = "There are " + StuckPendingCount + " pending jobs, " + longRunningCount + " of which are designated long-running reports, and " + totalRunningCount + " running jobs (according to CMC). Job server1: CMC:  " + cmcJobServer1Count + " jobs, CMS:  " + cmsJobServer1Count + " jobs;  Job server2: CMC:  " + cmcJobServer2Count + " jobs, CMS:  " + cmsJobServer2Count + " jobs; Job server3: CMC:  " + cmcJobServer3Count + " jobs, CMS:  " + cmcJobServer3Count + " jobs, CMS:  " + cmsJobServer4Count + " jobs;";
				System.out.println(emailSubject);
				//The following insert captures running jobs according to the CMC and to the CMS.  The CMC will give a larger count than the CMS 
				//when there are orphaned child job servers, so the data could be incorrect.
				Helper.runMSSQLInsertQuery("insert into STG1_BO4P_CURRJOBS_AGGR  (TOT_RUNNING_CNT,TOT_PENDING_CNT,LONG_RUNNING_PENDING_CNT, JOBSERVER1_CNT,	JOBSERVER2_CNT,	JOBSERVER3_CNT,	JOBSERVER4_CNT, CMS_NM, CMS_RUNNING_CNT,	CMS_JOBSRVR1_CNT,	CMS_JOBSRVR2_CNT,	CMS_JOBSRVR3_CNT, CMS_JOBSRVR4_CNT) VALUES(" + totalRunningCount  + ", " + StuckPendingCount  + "," + longRunningCount+ ", " + cmcJobServer1Count + "," + cmcJobServer2Count + "," + cmcJobServer3Count + "," + cmcJobServer4Count + ",'" + EnterpriseEnv + "', " + runningCount + "," + cmsJobServer1Count + "," + cmsJobServer2Count + "," + cmsJobServer3Count + "," + cmsJobServer4Count + ")");
			}
			else if (EnterpriseEnv =="QASHWBOBJ013") {
				cmcJobServer1Count = getCMCRunningCRJobs("QASHWBOBJ013", login, pswd, auth, "QA015.AdaptiveJobServer");
				//cmcJobServer1Count +=getCMCRunningCRJobs("QASHWBOBJ013", login, pswd, auth, "QA005.AdaptiveJobServerCR");
				cmcJobServer2Count = getCMCRunningCRJobs("QASHWBOBJ013", login, pswd, auth, "QA016.AdaptiveJobServer");
				//cmcJobServer2Count +=getCMCRunningCRJobs("QASHWBOBJ013", login, pswd, auth, "QA006.AdaptiveJobServerCR");
				//cmcJobServer3Count = getCMCRunningCRJobs("QASHWBOBJ013", login, pswd, auth, "QA007.AdaptiveJobServer");
				//cmcJobServer3Count +=getCMCRunningCRJobs("QASHWBOBJ013", login, pswd, auth, "QA007.AdaptiveJobServerCR");
				//cmcJobServer4Count = getCMCRunningCRJobs("QASHWBOBJ013", login, pswd, auth, "QA008.AdaptiveJobServer");
				//cmcJobServer4Count +=getCMCRunningCRJobs("QASHWBOBJ013", login, pswd, auth, "QA008.AdaptiveJobServerCR");
				totalRunningCount = cmcJobServer1Count + cmcJobServer2Count + cmcJobServer3Count + cmcJobServer4Count;
				//System.out.println("Total running count according to CMC:  " + totalRunningCount);
				//emailSubject = "There are " + StuckPendingCount + " pending jobs, " + longRunningCount + " of which are designated long-running reports, and " + totalRunningCount + " running jobs, "  + cmcJobServer2Count + " jobs are running on TUSLEXBRP02, " + cmcJobServer1Count + " running on TUSLEXBRP01 and " + cmcJobServer3Count + " running on TUSLEXBRP03.";
				emailSubject = "There are " + StuckPendingCount + " pending jobs, " + longRunningCount + " of which are designated long-running reports, and " + totalRunningCount + " running jobs (according to CMC). Job server1: CMC:  " + cmcJobServer1Count + " jobs, CMS:  " + cmsJobServer1Count + " jobs;  Job server2: CMC:  " + cmcJobServer2Count + " jobs, CMS:  " + cmsJobServer2Count + " jobs; Job server3: CMC:  " + cmcJobServer3Count + " jobs, CMS:  " + cmcJobServer3Count + " jobs, CMS:  " + cmsJobServer4Count + " jobs;";
				//The following insert captures running jobs according to the CMC and to the CMS.  The CMC will give a larger count than the CMS 
				//when there are orphaned child job servers, so the data could be incorrect.
				Helper.runMSSQLInsertQuery("insert into STG1_BO4Q_CURRJOBS_AGGR  (TOT_RUNNING_CNT,TOT_PENDING_CNT,LONG_RUNNING_PENDING_CNT, JOBSERVER1_CNT,	JOBSERVER2_CNT,	JOBSERVER3_CNT,	JOBSERVER4_CNT, CMS_NM, CMS_RUNNING_CNT,	CMS_JOBSRVR1_CNT,	CMS_JOBSRVR2_CNT,	CMS_JOBSRVR3_CNT, CMS_JOBSRVR4_CNT) VALUES(" +  totalRunningCount + ", " + StuckPendingCount + "," + longRunningCount + ", " + cmcJobServer1Count + "," + cmcJobServer2Count + "," + cmcJobServer3Count + "," + cmcJobServer4Count + ",'" + EnterpriseEnv + "', " + runningCount + "," + cmsJobServer1Count + "," + cmsJobServer2Count + "," + cmsJobServer3Count + "," + cmsJobServer4Count + ")");
			}
			else if (EnterpriseEnv =="DASHWBOBJ011") {
				cmcJobServer1Count = getCMCRunningCRJobs("DASHWBOBJ011", login, pswd, auth, "DEV.AdaptiveJobServer");
				//Throws error:  The CrystalEnterprise.null plug-in does not exist in the CMS (FWM 02017) [Ljava.lang.StackTraceElement;@1da691a
				cmcJobServer1Count += getCMCRunningCRJobs("DASHWBOBJ011", login, pswd, auth, "DEV.AdaptiveJobServerCR");
				cmcJobServer1Count += getCMCRunningCRJobs("DASHWBOBJ011", login, pswd, auth, "DEV.AdaptiveJobServerLCM");
				cmcJobServer1Count += getCMCRunningCRJobs("DASHWBOBJ011", login, pswd, auth, "DEV.AdaptiveJobServerWEBI");

				totalRunningCount = cmcJobServer1Count;
				System.out.println("Total running count according to CMC:  " + totalRunningCount);
				//emailSubject = "There are " + StuckPendingCount + " pending jobs, " + longRunningCount + " of which are designated long-running reports, and " + totalRunningCount + " running jobs.";
				emailSubject = "There are " + StuckPendingCount + " pending jobs, " + longRunningCount + " of which are designated long-running reports, and " + totalRunningCount + " running jobs (according to CMC). Job server1: CMC:  " + cmcJobServer1Count + ", CMS:  " + cmsJobServer1Count + ";";
				//The following insert captures running jobs according to the CMC and to the CMS.  The CMC will give a larger count than the CMS 
				//when there are orphaned child job servers, so the data could be incorrect.
				Helper.runMSSQLInsertQuery("insert into STG1_BO4D_CURRJOBS_AGGR  (TOT_RUNNING_CNT,TOT_PENDING_CNT,LONG_RUNNING_PENDING_CNT, JOBSERVER1_CNT,	JOBSERVER2_CNT,	JOBSERVER3_CNT,	JOBSERVER4_CNT,CMS_NM, CMS_RUNNING_CNT,	CMS_JOBSRVR1_CNT,	CMS_JOBSRVR2_CNT,	CMS_JOBSRVR3_CNT, CMS_JOBSRVR4_CNT) VALUES(" + totalRunningCount + ", " + StuckPendingCount + "," + longRunningCount + ", " + cmcJobServer1Count + "," + cmcJobServer2Count + "," + cmcJobServer3Count + "," + cmcJobServer4Count + ",'" + EnterpriseEnv + "', " + runningCount + "," + cmsJobServer1Count + "," + cmsJobServer2Count + "," + cmsJobServer3Count + "," + cmsJobServer4Count + ")");
			}
			else if (EnterpriseEnv =="DASHWBOBJ012") {
				cmcJobServer1Count = getCMCRunningCRJobs("DASHWBOBJ012", login, pswd, auth, "VALIDATION.AdaptiveJobServer");
				totalRunningCount = cmcJobServer1Count + cmcJobServer2Count + cmcJobServer3Count;
				//System.out.println("Total running count according to CMC:  " + totalRunningCount);
				//emailSubject = "There are " + StuckPendingCount + " pending jobs, " + longRunningCount + " of which are designated long-running reports, and " + totalRunningCount + " running jobs.";
				emailSubject = "There are " + StuckPendingCount + " pending jobs, " + longRunningCount + " of which are designated long-running reports, and " + totalRunningCount + " running jobs (according to CMC). Job server1: CMC:  " + cmcJobServer1Count + ", CMS:  " + cmsJobServer1Count + ";";
				//The following insert captures running jobs according to the CMC and to the CMS.  The CMC will give a larger count than the CMS 
				//when there are orphaned child job servers, so the data could be incorrect.
				Helper.runMSSQLInsertQuery("insert into STG1_BO4V_CURRJOBS_AGGR  (TOT_RUNNING_CNT,TOT_PENDING_CNT,LONG_RUNNING_PENDING_CNT, JOBSERVER1_CNT,	JOBSERVER2_CNT,	JOBSERVER3_CNT,	CMS_NM,	CMS_RUNNING_CNT, CMS_JOBSRVR1_CNT,	CMS_JOBSRVR2_CNT,	CMS_JOBSRVR3_CNT) VALUES (" +  totalRunningCount + ", " + StuckPendingCount + "," + longRunningCount + ", " + cmcJobServer1Count + "," + cmcJobServer2Count + "," + cmcJobServer3Count + ",'" + EnterpriseEnv + "', " + runningCount + "," + cmsJobServer1Count + "," + cmsJobServer2Count + "," + cmsJobServer3Count + ")");
			}

			emailSubject += " There are " + runningCount  + " jobs running according to the instance manager (CMS)";
			System.out.println(emailSubject);

			//System.out.println("total running count from cmc is:  " + totalRunningCount);
			//Use the next line when the CMS can give the right running count.
			//Emailer.sendEmail("bishopp@lexmark.com","bishopp@lexmark.com, arghosh@lexmark.com, maaftab@lexmark.com, amarabes@lexmark.com,snabi@lexmark.com, sdwivedi@lexmark.com","Pending Jobs on " + EnterpriseEnv + " ", "There are " + StuckPendingCount + " pending jobs, " + longRunningCount + " of which are designated long-running reports, and " + runningCount + " running jobs, "  + longRunningQ + " jobs are running on USLEXBRP02.",  null);

			//This shows count by job server from CMC: (it's a work-around)
			//Emailer.sendEmail("bishopp@lexmark.com","bishopp@lexmark.com, arghosh@lexmark.com, maaftab@lexmark.com, amarabes@lexmark.com,snabi@lexmark.com, sdwivedi@lexmark.com","Pending Jobs on " + EnterpriseEnv + "  ", "There are " + StuckPendingCount + " pending jobs, " + longRunningCount + " of which are designated long-running reports, and " + totalRunningCount + " running jobs, "  + cmcJobServer2Count + " jobs are running on USLEXBRP02, " + cmcJobServer1Count + " running on USLEXBRP01 and " + cmcJobServer3Count + " running on USLEXBRP03.",  null);
			//BufferedReader input = new BufferedReader(new FileReader("c:\\emails.txt"));

			//If have stuck pending jobs, send an email alert and write a database record.
			BufferedReader input = new BufferedReader(new FileReader("c:\\users\\bishopp\\emails.txt"));
			emails = input.readLine();
			//I don't know why there is a command to send email before we have the stuck pending count.
			if (StuckPendingCount > 0) {
				Emailer.sendEmail("bishopp@lexmark.com",emails,"Pending Jobs on " + EnterpriseEnv + "  ", emailSubject,  null);
			}


			// Get the pending jobs
			strInfoSQL = "select * from ci_infoobjects where si_schedule_status=9  and si_recurring=0 ";

			colInfoObjects = (IInfoObjects)objInfoStore.query(strInfoSQL);
			recordCount = colInfoObjects.size();

			//System.out.println(recordCount);
			System.out.println("Line 1728, There are " + recordCount + " total pending jobs.");
			runningCountMsg = "There are " + recordCount + " pending jobs.";
			PendingCount = recordCount;

			if (recordCount > 0){ //we have pending jobs.  Check to see if these have been pending for 10 minutes or longer.

				String driver = "mssql.jdbc.driver.SQLDriver";
				String url = "jdbc:sqlserver://DASHWBODB012;databaseName=EDR1DEN1;integratedSecurity=true;";

				ConnectMSSQLServer connServer = new ConnectMSSQLServer();
				connServer.dbConnect("jdbc:sqlserver://DASHWBODB012", "BOTRACK", "Lex405!1");

				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				Connection conn = DriverManager.getConnection("jdbc:sqlserver://DASHWBODB012", "BOTRACK", "Lex405!1");
				System.out.println("connected");

				//Connection conn=null;
				//Class.forName("oracle.jdbc.driver.OracleDriver");
				//conn=DriverManager.getConnection("jdbc:oracle:thin:@dlxkrbodb1.lex.lexmark.com:1750:edr1den1","botrack","Lex405!1");
				//Statement s=conn.createStatement();
				//'This records all the instances found on the BOXI system


				for (int y=0; y < recordCount;y++){
					objInfoObject = (IInfoObject)colInfoObjects.get(y);
					System.out.println(objInfoObject.getTitle());
					//oReport = (IReport)colInfoObjects.get(y);

					try {
						//Check the nextruntime to see if it really is pending; if it has been pending for 10 minutes or longer, we'll count it.
						nextRunTime.setTime((Date) objInfoObject.properties().getProperty("SI_NEXTRUNTIME").getValue());
						now = Calendar.getInstance();
						System.out.println("next run time is:  " + Helper.oracleDateTime(nextRunTime));
						//if (Helper.oracleDateTime(nextRunTime) == "30-01-1979 00:00:00") {//output was 30-01-1979 00:00:00, but this is not evaluating at this value when it should
						//I set oldDate to Jan 30, 1979 in the declaration
						//if (nextRunTime ==  oldDate) {
						//System.out.println("(line 1141) Next runtime year is:  " + nextRunTime.get(nextRunTime.YEAR));
						//System.out.println("(line 1142) This year is:  " + now.get(now.YEAR));
						if (nextRunTime.get(nextRunTime.YEAR) ==1979){

							//System.out.println("Nextruntime is 1/30/1979!");
							nextRunTime.setTime((Date) objInfoObject.properties().getProperty("SI_CREATION_TIME").getValue());
							now = Calendar.getInstance();
						}
					}
					catch (Exception ex) {
						nextRunTime.setTime((Date) objInfoObject.properties().getProperty("SI_CREATION_TIME").getValue());
						now = Calendar.getInstance();
					}

					//System.out.println("now:  " + now);
					//System.out.println("nextruntime: " + Helper.oracleDateTime( nextRunTime));

					//long difference =  currentTime.getTimeInMillis() - nextRunTime.getTimeInMillis();
					difference =  now.getTimeInMillis() - nextRunTime.getTimeInMillis();
					//System.out.println("The difference is " + difference);
					//System.out.println("The next run time year is " + nextRunTime.get(nextRunTime.YEAR));

					int days = (int) ((double) difference)/1000/60/60/24;
					long days_millis = days*1000*60*60*24;
					int hours = (int) ((double) (difference-days_millis))/1000/60/60;
					long hours_millis = hours*1000*60*60;
					int mins = (int) ((double) (difference-days_millis-hours_millis))/1000/60;
					long mins_millis = mins*1000*60;
					int secs = (int) ((double) (difference-days_millis-hours_millis-mins_millis))/1000;
					long secs_millis = secs*1000;
					int millis = (int) (difference-days_millis-hours_millis-mins_millis-secs_millis);

					System.out.print("Difference: " + (difference) + " milliseconds ==> " + days + " days " + hours + " hours " + mins + " mins " + secs + " secs " + millis + " millis");


					theInterval = 60/(now.getTimeInMillis() - nextRunTime.getTimeInMillis())*1000;
					//System.out.println("Minutes pending is:  " + mins + " mins and the hours are " + hours + " hours since " + nextRunTime.get(nextRunTime.DATE));
					//BUG:  if a report started pending at 10:40 AM and it is now 11:45 AM, it will not be considered
					//pending by this code because the minutes are only 5, when they should be 65.

					//total delay in min
					delayInMin = mins + hours*60;
					System.out.println("Pending delay in mins is " + delayInMin);
					//if (mins >= 10 && mins < 1440){	
					//if (mins >= 10){
					//***********CHANGE BACK TO GREATER THAN AFTER THE TEST*************
					if (delayInMin > 10){

						//reset the long running count: Nope, this reset it too much
						//longRunningCount = 0;

						/*	As of BOBJ 4.0, a "long-running queue" was no longer needed due to increase capacity.
						 * Removed from code Nov 24, 2014.
						 * //Count the number of jobs that are long-running
							System.out.println("Report title that is pending:  " + objInfoObject.getTitle() + ", " + objInfoObject.getParentID());
							oraSelectString = "select r.long_running_flg from " + targetTable + " r where r.si_id =" + objInfoObject.getParentID();
							Statement stmt = conn.createStatement() ; 
							System.out.println(oraSelectString);
							ResultSet rs = stmt.executeQuery(oraSelectString);

							while (rs.next()){
								try {

									System.out.println("long running flag is: " + rs.getString("LONG_RUNNING_FLG"));
									if (rs.getString("LONG_RUNNING_FLG").isEmpty()) {
										longRunningCount = longRunningCount;
									}
									else
										longRunningCount +=1;
								}

								catch (Exception ex){
									System.out.println(ex.getMessage() + " " + ex.getStackTrace());
								}

							}
							System.out.println("long-running count is:  " + longRunningCount);
						 */
						//System.out.println("pending report for 10 min or longer");

						try {
							runDate.setTime((Date) objInfoObject.properties().getProperty("SI_STARTTIME").getValue()); //threw an error 
							startTime = runDate;	
						}

						catch (Exception ex){
							//set a default starttime
							//System.out.println("Default startTime is" + startTime.getTime()); //however, this worked without error, but used current time

						}

						try {
							endTime.setTime((Date) objInfoObject.properties().getProperty("SI_ENDTIME").getValue());	//this also threw an error, but I don't know why I'm looking for an endtime for a pending job
						}
						catch (Exception ex){
							//set a default endtime
							//System.out.println("Default end time is " + endTime.getTime());
						}


						StuckPendingCount += 1;

						try{	                    
							machineUsed = objInfoObject.properties().getProperty("SI_MACHINE_USED").getValue().toString();  //this also threw an error
						}
						catch (Exception ex)
						{
							machineUsed = "unk";
						}


						try{
							destination = objInfoObject.getSchedulingInfo().getDestination().getName();
							destination = objInfoObject.getSchedulingInfo().getDestinations().get(0).toString();
							if (destination =="CrystalEntprise.Smtp") {	                		
								distributionList = Helper.getDistributionList(EnterpriseEnv, objInfoObject.getID());		                		   
							}

						}
						catch (Exception ex)
						{}


						/*try{
								//Get the logon info
								ReportClientDocument rcd1 = null ;

								// open the report
								rcd1 = rptAppFactory.openDocument( (IInfoObject)colInfoObjects2.get(x), 0, Locale.ENGLISH ) ;


								// Get the server names and show them
								serverNames = rcd1.getDatabaseController().getServerNames() ;
								if ( serverNames.size() == 0 ){
									System.out.println( "No datasources found." ) ;
									server1 = "none found";
									server2 = "none found";
								}
								else {
									if ( serverNames.size() > 1 ){
										// multiple datasources					            	
										for ( int d=0; d < serverNames.size(); d++ ) {
											System.out.println( serverNames.getString(d)  ) ;
											if (d < 0) {
												server1 = serverNames.getString(d);
											}
											else
												server2 = serverNames.getString(d);
										}
									}
									else {
										// just one datasource
										server1 =  serverNames.getString(0);
										System.out.println( serverNames.getString(0) ) ;
									}
								}
								rcd.close();
								rcd = null;

							}
							catch (Exception ex)
							{}*/

						// Get the SI_LOGON_INFO property bag
						try {
							IProperties boProcessingInfo = (IProperties) objInfoObject.getProcessingInfo().properties();

							IProperties logonProperties = (IProperties) boProcessingInfo.getProperty("SI_LOGON_INFO").getValue();
							if (logonProperties != null) {
								IProperties silogonProperties = (IProperties) logonProperties.getProperty("SI_LOGON1").getValue();

								System.out.println("SI_LOGON1 size is " + silogonProperties.size());
								//16, it's true
								String numLogins = logonProperties.getProperty("SI_NUM_LOGONS").toString();

								System.out.println("Num logons is " + numLogins);

								//Get the db logon credentials 
								int instanceID = objInfoObject.getID();
								System.out.println("instance id is " + instanceID);
								for (int h = 0; h < Integer.parseInt(numLogins); h++) {	
									//IReportLogon dbLogon = (IReportLogon)silogonProperties.get(h);
									if (h == 0){

										server1 = silogonProperties.getProperty("SI_SERVER").toString();
										logon1 = silogonProperties.getProperty("SI_USER").toString();
									}
									if (h ==1) {
										server2 = silogonProperties.getProperty("SI_SERVER").toString();
										logon2 = silogonProperties.getProperty("SI_USER").toString();
									}							
								}

							}
						}
						catch (Exception ex){
							System.out.println("Error getting logon properties " + ex.getMessage() + " " + ex.getStackTrace());
						}



						try{
							si_owner = (String)objInfoObject.properties().getProperty("SI_OWNER").getValue();
						}
						catch (Exception ex)
						{}


						try{
							//nextRunTime = (Calendar)objInfoObject.properties().getProperty("SI_NEXTRUNTIME").getValue();
							nextRunTime.setTime((Date) objInfoObject.properties().getProperty("SI_NEXTRUNTIME").getValue());
						}
						catch (Exception ex)
						{}


						try{
							//nextRunTime = (Calendar)objInfoObject.properties().getProperty("SI_NEXTRUNTIME").getValue();
							creationTime.setTime((Date) objInfoObject.properties().getProperty("SI_CREATION_TIME").getValue());
						}
						catch (Exception ex)
						{}


						try{
							//nextRunTime = (Calendar)objInfoObject.properties().getProperty("SI_NEXTRUNTIME").getValue();
							updateTS.setTime((Date) objInfoObject.properties().getProperty("SI_UPDATE_TS").getValue());
						}
						catch (Exception ex)
						{}



						try{
							ifiles = (IFiles)objInfoObject.properties().getProperty("SI_FILES").getValue(); //threw an error
							outputFile = (String)ifiles.properties().getProperty("SI_PATH").getValue();	
						}
						catch (Exception ex)
						{}


						try {
							//siPrompts = (IPrompts)objInfoObject.getProcessingInfo().properties().getProperty("SI_PROMPTS").getValue();
							//nbrPrompts = siPrompts.size();
							//if (nbrPrompts >0) {
							//	paramValues = Helper.getParamValueString(objInfoObject); //this throws an error:
							//	//com.crystaldecisions.sdk.plugin.desktop.excel.internal.a cannot be cast to com.crystaldecisions.sdk.plugin.desktop.report.IReport 
							//	System.out.println(paramValues);
							//} 
							//The function called checks to see if there are parameters or not.
							paramValues = Helper.getParamValueString(objInfoObject);
							//paramValues = Helper.getParamValueString(oReport); //this throws an error:
							//com.crystaldecisions.sdk.plugin.desktop.excel.internal.a cannot be cast to com.crystaldecisions.sdk.plugin.desktop.report.IReport 
							System.out.println(paramValues);

						} 
						catch (Exception ex) {
							System.out.println(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getCause() + " " + ex.getStackTrace());
						}


						//Save the pending jobs to the tracker database
						//msSQLInsertString = "insert into " + destTblNm + " (REPORT_ID, INSTANCE_ID, SI_STARTTIME,   SI_SUBMITTER, SI_SERVER, SI_OWNER, SI_SCHEDULE_STATUS,REPORT_PATH, SI_NAME, SI_CREATION_TIME, SI_NUM_PROMPTS, DATASRC_LOGIN_NM, SI_PASSWORD, DATASRC_LOGIN_NM2, SI_PASSWORD2,  SI_MACHINE_USED, SI_UPDATE_TS, DESTINATION, OUTPUT_FILE, REPORT_CUID, SI_PARENT_FOLDER_CUID, ENVIRONMENT, SCHEDULE_STATUS, SI_NEXTRUNTIME) values (" + objInfoObject.getParentID() + ", " + objInfoObject.getID() + ", to_date('" + startTime + "','MM/DD/YYYY HH12:MI:SS AM')" + ", '" + Helper.safeSQL((String)objInfoObject.getSchedulingInfo().properties().getProperty("SI_SUBMITTER").getValue()) + "', '" + Helper.safeSQL(server) + "', '" + Helper.safeSQL((String)objInfoObject.getSchedulingInfo().properties().getProperty("SI_SUBMITTER").getValue()) + "', " + objInfoObject.properties().getProperty("SI_SCHEDULE_STATUS").getValue().toString() + ", '" + Helper.safeSQL(cookieCrumbs) + "', '" + Helper.safeSQL(objInfoObject.getTitle()) + "', to_date('" + objInfoObject.properties().getProperty("SI_CREATION_TIME").getValue() + "','MM/DD/YYYY HH12:MI:SS AM'), " + nbrPrompts + ",'" + logon1 + "','" + pswd1 + "', '" + logon2 + "', '" + pswd2 + "','" + machineUsed + "', to_date('" + objInfoObject.properties().getProperty("SI_UPDATE_TS").getValue() + "','MM/DD/YYYY HH12:MI:SS AM'), '" + destination + "','" + outputFile + "', '" + objInfoObject.getParentCUID() + "', '" + objInfoObject.properties().getProperty("SI_PARENT_FOLDER_CUID").getValue() + "', '" + EnterpriseEnv + "','Pending', " + " to_date('" + objInfoObject.properties().getProperty("SI_NEXTRUNTIME").getValue() + "','MM/DD/YYYY HH12:MI:SS AM'))";
						//msSQLInsertString = "insert into " + destTblNm + " (REPORT_ID, INSTANCE_ID, SI_STARTTIME,   SI_SUBMITTER, SI_SERVER, SI_OWNER, SI_SCHEDULE_STATUS,REPORT_PATH, SI_NAME, SI_CREATION_TIME, SI_NUM_PROMPTS, DATASRC_LOGIN_NM, SI_PASSWORD, DATASRC_LOGIN_NM2, SI_PASSWORD2,  SI_MACHINE_USED, SI_UPDATE_TS, DESTINATION, OUTPUT_FILE, REPORT_CUID, SI_PARENT_FOLDER_CUID, ENVIRONMENT, SCHEDULE_STATUS, SI_NEXTRUNTIME) values (" + objInfoObject.getParentID() + ", " + objInfoObject.getID() + ", to_date('" + startTime + "','MM/DD/YYYY HH12:MI:SS AM')" + ", '" + Helper.safeSQL((String)objInfoObject.getSchedulingInfo().properties().getProperty("SI_SUBMITTER").getValue()) + "', '" + Helper.safeSQL(server) + "', '" + Helper.safeSQL((String)objInfoObject.getSchedulingInfo().properties().getProperty("SI_SUBMITTER").getValue()) + "', " + objInfoObject.properties().getProperty("SI_SCHEDULE_STATUS").getValue() + ", '" + Helper.safeSQL(cookieCrumbs) + "', '" + Helper.safeSQL(objInfoObject.getTitle()) + "', to_date('" + objInfoObject.properties().getProperty("SI_CREATION_TIME").getValue() + "','MM/DD/YYYY HH12:MI:SS AM'), " + nbrPrompts + ",'" + logon1 + "','" + pswd1 + "', '" + logon2 + "', '" + pswd2 + "','" + machineUsed + "', to_date('" + objInfoObject.properties().getProperty("SI_UPDATE_TS").getValue() + "','MM/DD/YYYY HH12:MI:SS AM'), '" + destination + "','" + outputFile + "', '" + objInfoObject.getParentCUID() + "', '" + objInfoObject.properties().getProperty("SI_PARENT_FOLDER_CUID").getValue() + "', '" + EnterpriseEnv + "','Pending', " + " to_date('" + objInfoObject.properties().getProperty("SI_NEXTRUNTIME").getValue() + "','MM/DD/YYYY HH12:MI:SS AM'))";
						msSQLInsertString = "insert into " + destTblNm + " (REPORT_ID, INSTANCE_ID, SI_STARTTIME,   SI_SUBMITTER, SI_SERVER, SI_OWNER, SI_SCHEDULE_STATUS,REPORT_PATH, SI_NAME, SI_CREATION_TIME, SI_NUM_PROMPTS, DATASRC_LOGIN_NM, SI_PASSWORD, DATASRC_LOGIN_NM2, SI_PASSWORD2,  SI_MACHINE_USED, SI_UPDATE_TS, DESTINATION, OUTPUT_FILE, REPORT_CUID, SI_PARENT_FOLDER_CUID, ENVIRONMENT, SCHEDULE_STATUS, SI_NEXTRUNTIME, PARAM_VALUES) values (" + objInfoObject.getParentID() + ", " + objInfoObject.getID() + ", ('" + Helper.msSQLDateTime(startTime) + "'), '" + Helper.safeSQL((String)objInfoObject.getSchedulingInfo().properties().getProperty("SI_SUBMITTER").getValue()) + "', '" + Helper.safeSQL(server) + "', '" + Helper.safeSQL((String)objInfoObject.getSchedulingInfo().properties().getProperty("SI_SUBMITTER").getValue()) + "', " + objInfoObject.properties().getProperty("SI_SCHEDULE_STATUS").getValue().toString() + ", '" + Helper.safeSQL(cookieCrumbs) + "', '" + Helper.safeSQL(objInfoObject.getTitle()) + "', ('" + Helper.msSQLDateTime(creationTime) + "'), " + nbrPrompts + ",'" + logon1 + "','" + pswd1 + "', '" + logon2 + "', '" + pswd2 + "','" + machineUsed + "', '" + Helper.oracleDateTime(updateTS) + "', '" + destination + "','" + outputFile + "', '" + objInfoObject.getParentCUID() + "', '" + objInfoObject.properties().getProperty("SI_PARENT_FOLDER_CUID").getValue() + "', '" + EnterpriseEnv + "','Pending', ('" + Helper.msSQLDateTime(nextRunTime) + "'),' " + paramValues +"' )";
						//System.out.println(msSQLInsertString);


						try
						{
							//System.out.println(msSQLInsertString);
							Helper.runMSSQLInsertQuery(msSQLInsertString);
						}
						catch (Exception ex)
						{

							String errMsg = ex.getMessage();
							String errStackTrace = ex.getStackTrace().toString();

							errMsg = errMsg.replace("\r", " "); //get NullPointException here.
							errStackTrace = errStackTrace.replace("\n", " ");

							System.out.println(ex.getMessage() + " " + ex.getStackTrace());
							System.out.println("insert into STG1_BOXI_TRACKER_ERRORS '" + errMsg + " " + errStackTrace + " " + EnterpriseEnv + "', GETDATE()");
							//for this to work you must replace any line feed carriage returns with dashes
							//carriage return is '\r' and line feed is '\n'
							Helper.runMSSQLInsertQuery("insert into STG1_BOXI_TRACKER_ERRORS '" + errMsg + " " + errStackTrace + " " + EnterpriseEnv + "', GETDATE()");
							//'sendmail("Logon error", ex.getMessage() + " " + ex.getStackTrace)
							//Helper.sendmail("bishopp@lexmark.com", "bishopp@lexmark.com, amarabes@lexmark.com,msankar@lexmark.com,ajain@lexmark.com", "Logon error - load_stg1_migr_XI_reports(String, String, String, String, String) - " + EnterpriseEnv, ex.getMessage() + " " + ex.getStackTrace());
							Emailer.sendEmail("bishopp@lexmark.com", "bishopp@lexmark.com", "Error with BOXI Alerter - currjobs -  " + EnterpriseEnv, errMsg + " " + errStackTrace, null);
							//Emailer.sendEmail("bishopp@lexmark.com", "bishopp@lexmark.com, amarabes@lexmark.com,msankar@lexmark.com,ajain@lexmark.com", "Error with BOXI Alerter - currjobs - " + EnterpriseEnv, errMsg + " " + errStackTrace, null);

						}

					}
					else
					{
						//System.out.println("No jobs are stuck in Pending for " + EnterpriseEnv);
						System.out.println("This job is in Pending for but not stuck in Pending");
					}

				}
				if (StuckPendingCount > 0){
					//Emailer.sendEmail("bishopp@lexmark.com","bishopp@lexmark.com","stuPending Jobs on " + EnterpriseEnv + " ", "There are " + StuckPendingCount + " pending jobs and " + runningCount + " running jobs.",  null);
				}
				conn = null;
				//conn.close();

				// Clean up the Enterprise Session.
				if(objEnterpriseSession != null) {
					try {
						objEnterpriseSession.logoff();

					} catch(Exception e_ignore_in_cleanup) {}
				}
			}


		}
		catch (Exception ex)
		{
			//String errMsg = ex.getMessage();
			String errMsg = "";
			String errStackTrace = ex.getStackTrace().toString();

			if (ex.getMessage() == null) {
				errMsg = "Though there was an exception caught, ex.getMessage() was null and there is no error message";
				System.out.println(errMsg);
			}
			else
			{
				errMsg = errMsg.replace("\r", " "); //get NullPointException here. Is errMsg not getting set??
				errStackTrace = errStackTrace.replace("\n", " ");

				System.out.println(ex.getMessage() + " " + ex.getStackTrace().toString());
				System.out.println("insert into STG1_BOXI_TRACKER_ERRORS '" + errMsg.toString() + " " + errStackTrace.toString() + " " + EnterpriseEnv + "', GETDATE()");
				//for this to work you must replace any line feed carriage returns with dashes
				//carriage return is '\r' and line feed is '\n'
				Helper.runMSSQLInsertQuery("insert into STG1_BOXI_TRACKER_ERRORS '" + errMsg.toString() + " " + errStackTrace.toString() + " " + EnterpriseEnv + "', GETDATE()");

			}
			//'sendmail("Logon error", ex.getMessage() + " " + ex.getStackTrace)
			//Helper.sendmail("bishopp@lexmark.com", "bishopp@lexmark.com, amarabes@lexmark.com,msankar@lexmark.com,ajain@lexmark.com", "Logon error - load_stg1_migr_XI_reports(String, String, String, String, String) - " + EnterpriseEnv, ex.getMessage() + " " + ex.getStackTrace());
			Emailer.sendEmail("bishopp@lexmark.com", "bishopp@lexmark.com", "Error with BOXI Alerter - currjobs -  " + EnterpriseEnv, errMsg + " " + errStackTrace, null);

		}

	}


	public static void deleteLongRunningReports(IInfoObject rptSchedule, int duration, String env, int theLimit)  {
		//Precondition:  there are scheduled reports running on BOXI that exceed the maximum-allowed runtime.
		//Post-condition:  the long-running schedules have been deleted.
		String submitter;
		String reportTitle;
		String emailBody;
		String emails;

		//Get the submitter's name to email him that the schedule was deleted due to excessive runtime duration
		//figure out how to delete the schedule.

		try {
			reportTitle = rptSchedule.getTitle();
			//submitter = rptSchedule.properties().getProperty("SI_SUBMITTER").getValue().toString();	//crashes here for no known reason
			submitter = (String)rptSchedule.getSchedulingInfo().properties().getProperty("SI_SUBMITTER").getValue();
			if (submitter == "Administrator"){
				submitter = "vanbalangan";			
			}
			emailBody = "The report schedule " + reportTitle + " running on " + env + " has exceeded the maximum time allowed to run on the system.  The schedule has been deleted.  Please either run the report for a smaller amount of data or have your BI Key User tune the report and promote the tuned report to BOXI Prod.";

			Emailer.sendEmail("reporting@lexmark.com", submitter + "@lexmark.com", reportTitle + " on " + env + " exceeded max runtime of " + theLimit + " hours", emailBody, "");

			try {
				//BufferedReader input = new BufferedReader(new FileReader("c:\\emails.txt"));
				BufferedReader input = new BufferedReader(new FileReader("c:\\users\\bishopp\\emails.txt"));
				emails = input.readLine();
				emails = emails + ", " + submitter + "@lexmark.com";
				Emailer.sendEmail("reporting@lexmark.com", emails, reportTitle + " on " + env + " exceeded max runtime of " + theLimit + " hours", emailBody, "");

				//Write a record to the Tracker DB so we know how often this happens.

			}
			catch (Exception ex){
				System.out.println(ex.getMessage() + " " + ex.getStackTrace());
			}

			//System.out.println("reporting@lexmark.com " + submitter + "@lexmark.com " + reportTitle + " exceeded max runtime of " + duration + " hours " + emailBody + "");

			//test code only for reports submitted by me
			//if (submitter == "Administrator" || submitter =="BISHOPP"){

			System.out.println("Report schedule deleted:  " + reportTitle);
			rptSchedule.deleteNow();
			//rptSchedule.getCUID();//just a test line of code to made the catch clause work.



		}
		catch (SDKException ex){
			System.out.println(ex.getMessage() + " " + ex.getStackTrace());
		}

	}
	public static int getCMCRunningCRJobs(String boCmsName, String boUser, String boPassword,  String boAuthType, String jobServerName) {
		//Pre-condition:  a current count of running Crystal Reports has not been captured.
		//Post-condition:  a current count of running Crystal Reports has been captured.

		//NOTE:  this is a work-around for getting a running count from the CMS.  As of September 2010, the query
		// to the CMS database often yields an inaccurately-low running count.
		// The CMC count is only a count.  I might be able to get the PID's but only the ones available
		// in the CMS will yield the report details.


		// Declare Variables
		//System.out.println("CMS: " + boCmsName );
		//System.out.println("User: " + boUser);
		//System.out.println("pswd: " + boPassword);
		//System.out.println("auth: " + boAuthType);
		//System.out.println("jobserver: " + jobServerName);

		IInfoStore boInfoStore=null;
		IInfoObjects boInfoObjects=null;
		SDKException failure = null;
		IEnterpriseSession boEnterpriseSession = null;

		IServer currentServer = null; 
		int runningJobCount = 0;

		try{
			// Logon and obtain an Enterprise Session
			boEnterpriseSession = CrystalEnterprise.getSessionMgr().logon( boUser, boPassword, boCmsName, boAuthType);
			boInfoStore = (IInfoStore) boEnterpriseSession.getService("", "InfoStore");

			boInfoObjects = boInfoStore.query("Select * From CI_SYSTEMOBJECTS Where SI_NAME='" + jobServerName + "'");
			//boInfoObjects = boInfoStore.query("Select * From CI_SYSTEMOBJECTS Where SI_PROGID='CrystalEnterprise.Server' and SI_DESCRIPTION='" + jobServerName + "'");
			currentServer = (IServer) boInfoObjects.get(0);



			IJobServerAdmin boJobAdmin = (IJobServerAdmin)currentServer.getServerAdmin();

			System.out.println("Running jobs for " + jobServerName + ": " + boJobAdmin.getCurrentJobs());
			runningJobCount = boJobAdmin.getCurrentJobs();

		}
		catch (Exception ex){
			System.out.println(ex.getMessage() + " " + ex.getStackTrace());
		}
		return runningJobCount;
	}

	public static void getCMCStatistics(String boCmsName,String boUser, String boPassword,  String boAuthType) {
		//This is the example code from SAP-BO

		// Declare Variables
		IInfoStore boInfoStore=null;
		IInfoObjects boInfoObjects=null;
		SDKException failure = null;
		IEnterpriseSession boEnterpriseSession = null;

		IServer currentServer = null; 
		ICMSAdmin cmsAdmin = null;
		IServerGeneralMetrics serverMetrics = null; 

		try{
			// Logon and obtain an Enterprise Session
			boEnterpriseSession = CrystalEnterprise.getSessionMgr().logon( boUser, boPassword, boCmsName, boAuthType);
			boInfoStore = (IInfoStore) boEnterpriseSession.getService("", "InfoStore");

			boInfoObjects = boInfoStore.query("Select * From CI_SYSTEMOBJECTS Where SI_PROGID='CrystalEnterprise.Server' and SI_DESCRIPTION='Central Management Server'");
			currentServer = (IServer) boInfoObjects.get(0);

			System.out.println("Before Metrics");
			//Display the server information
			System.out.println("Server: " + currentServer.getFriendlyName());
			System.out.println("Enabled: " + !currentServer.isDisabled());
			System.out.println("Running: " + currentServer.isAlive());

			//I'm guessing here:
			System.out.println("getSIAHostname: " + currentServer.getSIAHostname());
			System.out.println("getServerKind: " + currentServer.getServerKind());
			System.out.println("kind: " + currentServer.getKind());
			System.out.println("name: " + currentServer.getName());
			System.out.println("description " + currentServer.getDescription());
			System.out.println("title   " + currentServer.getTitle());
			System.out.println(" blah  " + currentServer.getMetrics());
			//			System.out.println("service admin:  " + currentServer.getServiceAdminObject("CrystalReportsJobServer")); throws error "The plugin CrystalEnterprise.CrystalReportsJobServer does not exist in the CMS (FWM 02017)"
			System.out.println("Hosted services:  " + currentServer.getHostedServices());
			System.out.println("Configured services:  " + currentServer.getHostedServices().getConfiguredServiceIDs());

			System.out.println("General metrics");
			//retrieve and display Server metrics
			serverMetrics = currentServer.getServerGeneralAdmin();
			System.out.println("CPU: " + serverMetrics.getCPU());
			System.out.println("CPU count: " + serverMetrics.getCPUCount());
			System.out.println("Current time: " + serverMetrics.getCurrentTime());
			System.out.println("Server start time: " + serverMetrics.getStartTime());
			System.out.println("Available disk space: " + serverMetrics.getDiskSpaceAvailable()+"bytes");
			System.out.println("Total disk space: " + serverMetrics.getDiskSpaceTotal()+"bytes");
			System.out.println("Memory available: " + serverMetrics.getMemory()+"bytes");
			System.out.println("Operating system: " + serverMetrics.getOperatingSystem());
			System.out.println("Server Version: " + serverMetrics.getVersion()+"");


			//Display cms specific Metrics
			System.out.println("cms Metrics");
			//cast the server as IcmsAdmin
			cmsAdmin = (ICMSAdmin)currentServer.getServerAdmin();
			System.out.println("Build date: " + cmsAdmin.getCMSBuildDate());
			System.out.println("Database name: " + cmsAdmin.getCMSDatabaseName());
			System.out.println("Database server name: " + cmsAdmin.getCMSDatabaseServerName());
			System.out.println("Database user name: " + cmsAdmin.getCMSDatabaseUserName());
			System.out.println("Database source name: " + cmsAdmin.getCMSDataSourceName());
			System.out.println("Private build number: " + cmsAdmin.getCMSPrivateBuildNumber());
			System.out.println("Product version: " + cmsAdmin.getCMSProductVersion());
			System.out.println("Pending jobs: " + cmsAdmin.getPendingJobs());
			System.out.println("Running jobs: " + cmsAdmin.getRunningJobs());
			System.out.println("Successful jobs: " + cmsAdmin.getSuccessJobs());
			System.out.println("Waiting jobs: " + cmsAdmin.getWaitingJobs());
			System.out.println("Failed jobs: " + cmsAdmin.getFailedJobs());
			System.out.println("Cluster members:");

			String[] clusterMembers = cmsAdmin.getClusterMembers();
			for (int k=0;k<clusterMembers.length;k++) {
				System.out.println("* "+clusterMembers[k]);
			}



			System.out.println("");
			System.out.println("Concurrent licenses: " + cmsAdmin.getLicensesConcurrent());
			System.out.println("Named user licenses: " + cmsAdmin.getLicensesNamedUsers());
			System.out.println("License processors: " + cmsAdmin.getLicensesProcessors());
			System.out.println("Concurrent users connected: " + cmsAdmin.getUserConnectedConcurrent());
			System.out.println("Named users connected: " + cmsAdmin.getUserConnectedNamedUsers());
			System.out.println("Existing concurrent users: " + cmsAdmin.getUserExistingConcurrent());
			System.out.println("Existing named users: " + cmsAdmin.getUserExistingNamedUsers());
			System.out.println("Token connections: " + cmsAdmin.getUserTokenConnections());



		}
		catch(SDKException e)
		{
			System.out.println(e.getMessage());
			System.err.println("Unable to retrieve server metrics. Exception caught: " + e.getMessage());
		}
	}

}

