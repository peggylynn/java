import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.commons.io.FileUtils;

import com.crystaldecisions.sdk.exception.SDKException;
import com.crystaldecisions.sdk.framework.CrystalEnterprise;
import com.crystaldecisions.sdk.framework.IEnterpriseSession;
import com.crystaldecisions.sdk.framework.ISessionMgr;
import com.crystaldecisions.sdk.occa.infostore.CePropertyID;
import com.crystaldecisions.sdk.occa.infostore.IDestination;
import com.crystaldecisions.sdk.occa.infostore.IDestinationPlugin;
import com.crystaldecisions.sdk.occa.infostore.IEffectivePrincipal;
import com.crystaldecisions.sdk.occa.infostore.IEffectiveRights;
import com.crystaldecisions.sdk.occa.infostore.IExplicitPrincipal;
import com.crystaldecisions.sdk.occa.infostore.IExplicitPrincipals;
import com.crystaldecisions.sdk.occa.infostore.IFile;
import com.crystaldecisions.sdk.occa.infostore.IFiles;
import com.crystaldecisions.sdk.occa.infostore.IInfoObject;
import com.crystaldecisions.sdk.occa.infostore.IInfoObjects;
import com.crystaldecisions.sdk.occa.infostore.IInfoStore;
import com.crystaldecisions.sdk.occa.infostore.IProcessingInfo;
import com.crystaldecisions.sdk.occa.infostore.ISchedulingInfo;
import com.crystaldecisions.sdk.occa.infostore.ISecurityInfo2;
import com.crystaldecisions.sdk.occa.infostore.ISendable;
import com.crystaldecisions.sdk.occa.managedreports.IReportAppFactory;
import com.crystaldecisions.sdk.occa.report.application.ReportClientDocument;
import com.crystaldecisions.sdk.occa.report.lib.IStrings;
import com.crystaldecisions.sdk.plugin.desktop.common.IExcelFormat;
import com.crystaldecisions.sdk.plugin.desktop.common.IPDFFormat;
import com.crystaldecisions.sdk.plugin.desktop.common.IReportFormatOptions;
import com.crystaldecisions.sdk.plugin.desktop.common.IReportLogon;
import com.crystaldecisions.sdk.plugin.desktop.common.IReportParameter;
import com.crystaldecisions.sdk.plugin.desktop.common.IReportProcessingInfo;
import com.crystaldecisions.sdk.plugin.desktop.common.IRichTextFormat;
import com.crystaldecisions.sdk.plugin.desktop.common.IRichTextFormatEditable;
import com.crystaldecisions.sdk.plugin.desktop.common.IWordFormat;
import com.crystaldecisions.sdk.plugin.desktop.excel.IExcel;
import com.crystaldecisions.sdk.plugin.desktop.pdf.IPDF;
import com.crystaldecisions.sdk.plugin.desktop.report.IReport;
import com.crystaldecisions.sdk.plugin.desktop.txt.ITxt;
import com.crystaldecisions.sdk.plugin.desktop.user.IUser;
import com.crystaldecisions.sdk.plugin.desktop.usergroup.IUserGroup;
import com.crystaldecisions.sdk.prompting.IPrompts;
import com.crystaldecisions.sdk.properties.IProperties;
import com.crystaldecisions.sdk.properties.IProperty;
import com.crystaldecisions.sdk.properties.ISDKList;
//import com.businessobjects.sdk.plugin.desktop.webi; 
//import com.sap.sl.sdk.authoring.connection;
//import com.crystaldecisions.sdk.authoring.connection;
//import com.businessobjects.sdk.plugin.desktop.webi;


public class LoadFunctions {



	public static void load_stg1_boxi_recurringjobs(String EnterpriseEnv, String login, String pswd,String auth, String destTblNm, String environ, String loadtype){
		//Precondition:  today's recurring jobs snapshot since the last load are not in the database table.
		//Postcondition:  today's recurring jobs snapshot since the last load are in the database table.

		IEnterpriseSession objEnterpriseSession;
		//IInfoStore objInfoStore;
		IInfoObjects colInfoObjects;
		IInfoObjects colRecurObjects;
		IInfoObject objInfoObject;
		IInfoObject objInstObject;
		IInfoObject objRecurringObject;
		String errorMsg = "";
		String cookieCrumbs;
		int count;
		String si_owner;
		Calendar runDate = Calendar.getInstance();
		Calendar startTime = Calendar.getInstance();
		startTime.set(1980,1,1,0,0,0);
		Calendar endTime = Calendar.getInstance();
		endTime.set(1980,1,1,0,0,0);
		long theInterval = 0;
		Calendar startDate = Calendar.getInstance();;
		startDate.set(1980,1,1,0,0,0);
		Calendar endDate = Calendar.getInstance();;
		endDate.set(1980,1,1,0,0,0);
		String dtToday;
		String msSQLInsertString;
		String maxID ="0";
		String logon1 = "unknown";
		String logon2 = "unknown";
		String pswd1 = "unknown";
		String pswd2 = "unknown";
		String datasrc1 = "not found in CMS";
		String datasrc2 = "not found in CMS";
		int numPrompts = 0;
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
		String scheduleStatus = "9";
		IProcessingInfo processingInfo;
		IReport iReport;
		String server;
		String server1 = "";
		String server2 = "";
		IProperty aggCount;
		String strCount="0";
		int colCount = 0;
		Boolean si_format_export_allpages;
		String siFormatExportAllpages  = "non-existent";
		String paramValues;
		String recurringIDList ="0";
		String machine_used;
		//IPrompts siPrompts;
		int nbrPrompts =0;
		String promptName;
		String paramValueString = "";

		Calendar nextRunTime = Calendar.getInstance();
		nextRunTime.set(1980,1,1,0,0,0);
		String machineUsed = "";
		String submitter = "";
		String owner = "";
		Calendar creationTime = Calendar.getInstance();
		creationTime.set(1980,1,1,0,0,0);

		Calendar updateTS = Calendar.getInstance();
		updateTS.set(1980,1,1,0,0,0);

		String parentCuid = "unknown";
		String parentFolderCuid = "";
		String cuid = "";
		int scheduleType = 0;
		int scheduleIntervalMinutes = 0;
		int scheduleIntervalDays = 0;
		int scheduleIntervalHours = 0;
		int scheduleIntervalMonths = 0;



		try {
			ISessionMgr  objSessionMgr = CrystalEnterprise.getSessionMgr();
			objEnterpriseSession = objSessionMgr.logon(login, pswd, EnterpriseEnv, auth);    
			IInfoStore objInfoStore = (IInfoStore) objEnterpriseSession.getService("InfoStore");

			if (loadtype == "Full") {
				strInfoSQL = "SELECT count(SI_ID) from CI_INFOOBJECTS WHERE SI_INSTANCE = 1 and si_recurring = 1";
				//TEMPORARY
				//strInfoSQL = "SELECT count(SI_ID) from CI_INFOOBJECTS WHERE SI_INSTANCE = 1 and si_recurring = 1 and CUID in ('AcPQF39rfLVKhKf82YJ9n4I','AdunDuiQ4f5KrO0cy3PMDEU')";
			} else {
				String username = "botrack";
				String password = "********";
				//Get the list of si_id's that were missed

				String driver = "mssql.jdbc.driver.SQLDriver";
				String url = "jdbc:sqlserver://DASHWBODB012;databaseName=EDR1DEN1;integratedSecurity=true;";

				ConnectMSSQLServer connServer = new ConnectMSSQLServer();
				connServer.dbConnect("jdbc:sqlserver://DASHWBODB012", "BOTRACK", "********");


				String oraSelectString ="select distinct i.RECURRING_ID from temp_recurring_ids i, (select instance_id from stg1_bo4p_recurringjobs where load_dt >= GETDATE()) rc where i.RECURRING_ID =  rc.INSTANCE_ID (+) and rc.instance_id is null";
				try
				{
					System.out.println(oraSelectString);
					Class.forName(driver);		//loads the driver
					Connection conn = DriverManager.getConnection(url,username,password);	

					Statement stmt = conn.createStatement() ; 
					ResultSet rs = stmt.executeQuery(oraSelectString);
					while (rs.next()) 
					{
						recurringIDList += ", " + rs.getString("RECURRING_ID"); 
						//maxID = rs.getString("maxID");
					}
					recurringIDList = recurringIDList.substring(2, recurringIDList.length());
					System.out.println(recurringIDList);  //make sure the string looks right
					//conn = null;
					conn.close();
				}	
				catch (Exception ex)
				{
					ex.printStackTrace();
					System.out.println(ex.getMessage() + " " + ex.getStackTrace() + " line 1999");
				}
				strInfoSQL = "SELECT count(SI_ID) from CI_INFOOBJECTS WHERE SI_INSTANCE = 1 and SI_RECURRING=1 and si_id in (" + recurringIDList + ")";
			}
			System.out.println(strInfoSQL);

			colInfoObjects = (IInfoObjects)objInfoStore.query(strInfoSQL);
			//objInfoObject = (IInfoObject)colInfoObjects.get(i); 

			objInfoObject = (IInfoObject)colInfoObjects.get(0);

			//The VB code used the collection here, but in Java, properties() isn't an available function, so I am trying to get this via InfoObject

			//System.out.println(objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").properties().getProperty("SI_ID"));
			System.out.println(objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").getID());//shows the ID, not the count!
			//count = objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").properties().getProperty("SI_ID");
			//count = objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").getID();
			aggCount = objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT");

			count = objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").getID();
			strCount = ((IProperties)objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").getValue()).getProperty("SI_ID").getValue().toString();

			System.out.println("Record count is " + strCount);


			if (loadtype == "Full") {
				strInfoSQL = "SELECT TOP " + strCount + " * FROM CI_INFOOBJECTS WHERE  SI_INSTANCE = 1 and si_recurring=1  order by SI_ID";
				//strInfoSQL = "SELECT TOP " + strCount + " REPORT_ID, INSTANCE_ID, SI_STARTTIME, SI_ENDTIME,  SI_SUBMITTER, SI_SERVER, SI_OWNER, SI_SCHEDULE_STATUS,REPORT_PATH, SI_NAME, SI_CREATION_TIME, SI_NUM_PROMPTS, DATASRC_LOGIN_NM, SI_PASSWORD, DATASRC_LOGIN_NM2, SI_PASSWORD2,  SI_MACHINE_USED,  SI_DESTINATION,  REPORT_CUID,  DISTRIBUTION_LIST,  SI_KIND, SI_FORMAT_EXPORT_ALLPAGES, CUID, PARAM_VALUES,SI_SCHEDULE_TYPE, SI_SCHEDULE_INTERVAL_MINUTES, SI_SCHEDULE_INTERVAL_HOURS, SI_SCHEDULE_INTERVAL_MONTHS, SI_SCHEDULE_INTERVAL_DAYS, SI_NEXTRUNTIME FROM CI_INFOOBJECTS WHERE  SI_INSTANCE = 1 and si_recurring=1  order by SI_ID";
				//Temporary
				//strInfoSQL = "SELECT TOP " + strCount + " * FROM CI_INFOOBJECTS WHERE  SI_INSTANCE = 1 and si_recurring=1 and CUID in ('AcPQF39rfLVKhKf82YJ9n4I','AdunDuiQ4f5KrO0cy3PMDEU') order by SI_ID";
			}
			else {
				//strInfoSQL = "SELECT TOP " + strCount + " SI_ID FROM CI_INFOOBJECTS WHERE SI_KIND in ('CrystalReport') and SI_INSTANCE = 0 and SI_CREATION_TIME >= '" + Helper.formatDateBO(maxCreationDate) + "'";
				//PRODUCTION SQL:
				strInfoSQL = "SELECT TOP " + strCount + " * FROM CI_INFOOBJECTS WHERE  SI_INSTANCE = 1 and si_recurring=1 and si_id in (" + recurringIDList + ") order by SI_ID";
				//JUST A TEST TO SEE HOW FAILED INSTANCES WORK IN THE CODE
				//strInfoSQL = "SELECT TOP " + strCount + " SI_ID FROM CI_INFOOBJECTS WHERE  SI_INSTANCE = 1 and si_recurring=0 and si_schedule_status=1 and SI_CREATION_TIME >= '" + Helper.formatDateBO(maxCreationDate) + "'";

			}

			System.out.println(strInfoSQL);
			colRecurObjects = (IInfoObjects)objInfoStore.query(strInfoSQL);
			System.out.println("Line 210 Count of objects in collection: " + colRecurObjects.size());




			if (loadtype =="Full"){
				//'*************INSERT THE SI_ID'S INTO A TEMP TABLE TO COMPARE WITH WHAT YOU GOT LOADED IN THE RECURRINGJOBS TABLE********
				//Helper.runMSSQLInsertQuery("truncate table temp_recurring_ids");
				for (int y = 0; y < colRecurObjects.size(); y++){
					objRecurringObject = (IInfoObject)colRecurObjects.get(y);
					Helper.runMSSQLInsertQuery("insert into temp_recurring_ids (recurring_id) values (" + objRecurringObject.getID() + ")");
				}
			}

			//'****************************************************************************************************

			if (colRecurObjects.size() > 0){
				//Connection conn=null;
				//Class.forName("oracle.jdbc.driver.OracleDriver");
				//conn=DriverManager.getConnection("jdbc:oracle:thin:@dlxkrbodb1.lex.lexmark.com:1750:edr1den1","botrack","********");
				//Statement s=conn.createStatement();
				//'This records all the instances found on the BOXI system
				colCount = colRecurObjects.size();
				for (int x=0; x <= colCount; x++){
					//msSQLInsertString = "insert into temp_p_allinstances(report_id, instance_id) values (" + objInfoObject.getParentID() + "," + objInfoObject.getID() + ")";
					//System.out.println(msSQLInsertString);
					//s.execute(msSQLInsertString);
					objRecurringObject = (IInfoObject)colRecurObjects.get(x);
					System.out.println ("si_id is " + objRecurringObject.getID());
					System.out.println("report title is " + objRecurringObject.getTitle());
					System.out.println("schedule status is " + objRecurringObject.properties().getProperty("SI_SCHEDULE_STATUS").getValue());

					// 'Reset these for each recurring job
					si_owner = "";
					logon1 = "";
					pswd1 = "";
					logon2 = "";
					pswd2 = "";
					server1 = "";
					machine_used = "unknown";
					nextRunTime.set(1980,1,1,0,0,0);
					paramValues = "";
					paramValueString = "";

					//Run this only if the report has parameters
					try {

						IProperties siProperties;
						// Get the SI_PROMPTS property bag from the InfoObject's Processing Info
						System.out.println("Report kind: " + objRecurringObject.getKind());
						if (objRecurringObject.getKind().toString() == "CrystalReport") {
							siProperties = (IProperties) objRecurringObject.getProcessingInfo().properties().getProperty("SI_PROMPTS").getValue();
							//Aug 13, 2013:  this isn't working for a CDM Webi report, RS0801, it's null even though the report does have properties.
						}
						else {
							siProperties = (IProperties) objRecurringObject.getProcessingInfo().properties().getProperty("SI_WEBI_PROMPTS").getValue();
						}

						//IProperties oProcessInfo = (IProperties) objInfoObject.getProcessingInfo().properties();
						//IProperties siPrompts = (IProperties) oProcessInfo.getProperty("SI_PROMPTS").getValue();
						// Make sure that there are parameters.
						if (siProperties != null)
						{
							// Get the number of prompts (the number of parameters)
							if (objRecurringObject.getKind().toString() == "CrystalReport") {
								numPrompts = ((Integer)siProperties.getProperty("SI_NUM_PROMPTS").getValue()).intValue();
							}  else {
								numPrompts = ((Integer)siProperties.getProperty("SI_TOTAL").getValue()).intValue();   
							}
							System.out.println("number of prompts: "  + numPrompts);


							//siPrompts = (IPrompts)objRecurringObject.getProcessingInfo().properties().getProperty("SI_PROMPTS").getValue();
							//now this error:
							//com.crystaldecisions.sdk.properties.internal.SDKPropertyBag cannot be cast to com.crystaldecisions.sdk.prompting.IPrompts com.crystaldecisions.sdk.properties.internal.SDKPropertyBag cannot be cast to com.crystaldecisions.sdk.prompting.IPrompts null [Ljava.lang.StackTraceElement;@7c218e

							//nbrPrompts = siPrompts.size();
							//if (nbrPrompts >0) {
							if (numPrompts > 0) {
								//paramValues = Helper.getParamValueString2(objRecurringObject.getID(), login, pswd, EnterpriseEnv, auth);
								//This one is throwing that awful ContentProcesingHandler that happens when I make a really stupid mistake.

								//paramValues = Helper.getParamValueString(objRecurringObject); //this throws an error:						
								//com.crystaldecisions.sdk.plugin.desktop.excel.internal.a cannot be cast to com.crystaldecisions.sdk.plugin.desktop.report.IReport
								//or the function returns Null on everything even though the recurring job has parameters


								try{
									// Get the SI_PROMPTS property bag from the InfoObject's Processing Info - got this from SAP
									//IProperties siProperties = (IProperties) objInfoObject.getProcessingInfo().properties().getProperty("SI_PROMPTS").getValue();
									//Error:  properties() and getPRoperty() undefined for IProcessingInfo

									// Loop through the parameters to get the parameter names and parameter values.
									for(i=1;i<=numPrompts;i++)
									{
										IProperties prompt;
										// Each prompt has the name SI_PROMPT + a digit starting from 1.  For example, if
										// there are 2 prompts, the first one would be called SI_PROMPT1 and the second
										// one would be called SI_PROMPT2.
										//IProperties prompt = (IProperties)siPrompts.getProperty("SI_PROMPT"+i).getValue();
										if (objRecurringObject.getKind().toString() == "CrystalReport") {
											prompt = (IProperties)siProperties.getProperty("SI_PROMPT"+i).getValue();
										}
										else {
											prompt = (IProperties)siProperties.getProperty(i).getValue();
										}

										// Get the parameter name.
										promptName = prompt.getProperty("SI_NAME").getValue().toString();
										System.out.println("Prompt name is "  + promptName);

										// First column in the row is the parameter name.
										//out.println("<td>Parameter Name = "+promptName+"</td><td><table>");
										// Get the current values property bag.
										IProperties currentValues;
										String theValue;
										if (objRecurringObject.getKind().toString() == "CrystalReport") {
											currentValues = (IProperties)prompt.getProperty("SI_CURRENT_VALUES").getValue();
										}
										else {
											currentValues = (IProperties)prompt.getProperty("SI_VALUES").getValue();
											theValue = (String)currentValues.getProperty(1).getValue();
											System.out.println("prompt value is :" + theValue);
										}

										System.out.println("prompt value is :" + currentValues);
										int numberValues;
										// Get the number of values for this particular parameter.
										if (objRecurringObject.getKind().toString() == "CrystalReport") {
											numberValues = ((Integer)currentValues.getProperty("SI_NUM_VALUES").getValue()).intValue();
										} else {
											numberValues = (Integer)currentValues.getProperty("SI_TOTAL").getValue();
										}

										System.out.println("Number of prompt values is " + numberValues);

										// Loop through all of the values for this particular parameter.
										for(int j=1;j<=numberValues;j++)
										{
											IProperties currentValue;
											IProperties si_values;
											String aValue;
											// Print out the values.
											// For Crystal Reports:
											// Each value has the name SI_VALUE + a digit starting from 1.  For example, if
											// there are two values, then the first value would be called SI_VALUE1 and the
											// second value would be called SI_VALUE2.

											//For Webi, the name is SI_VALUES and there is a child property that is an integer.
											//Also for Webi, there is no property called SI_PROMPT_TYPE.
											System.out.println("si_kind is:  " + objRecurringObject.getKind().toString());
											//if (objRecurringObject.getKind().toString() == "Webi") {
											if	(objRecurringObject.getKind().toString().equals("Webi")) {
												//si_values = (IProperties)currentValues.getProperty("SI_VALUES").getValue();
												//aValue = (IProperty)si_values.getProperty(i).getValue();
												aValue = (String)currentValues.getProperty(j).getValue();
												System.out.println("Current param value for " + promptName + " is: " + aValue);
												paramValueString += aValue  + "; ";
												//System.out.println("Param value string so far is " + paramValueString);

											} else {



												currentValue = (IProperties)currentValues.getProperty("SI_VALUE"+j).getValue();

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
													//System.out.println("Param value string so far is " + paramValueString);
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
														//System.out.println("Param value string so far is " + paramValueString);

													}
													else //if (currentValue.size() == 4)
														if (promptType.equals("0"))
														{
															// Discrete Parameter: contains 4 properties: SI_SHOWDESCONLY, SI_OPTIONS,
															// SI_DESCRIPTION, SI_DATA 
															System.out.println(prompt.getProperty("SI_PROMPT_TYPE").getValue().toString());
															// For this situation we are only interested in the SI_DATA property.
															String paramValue = currentValue.getProperty("SI_DATA").getValue().toString();
															//out.println("<tr><td> Value " + j + " = " + paramValue + "</td></tr>");
															paramValueString += paramValue  + "; ";
															//System.out.println("Param value string so far is " + paramValueString);

														}

														else
														{
															System.out.println("The number of prompts is 0 for the InfoObject.");
														}
											}
										}
									}
								}
								catch (Exception ex) {
									System.out.println(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getStackTrace() + " getParamValuesString - line 427");
								}
								//param values can't exceed 3000 characters
								System.out.println("Param value string length is " + paramValueString.length());
								if (paramValueString.length() > 4000) {
									System.out.println("The param value string for " + objInfoObject.getID() + " exceeds 4000 char");
									//paramValueString = paramValueString.substring(0, 2995); //Either Oracle is miscounting, or there's a bug in Eclipse that comes back from Oracle saying the length exceeds 3000 even though it doesn't.
									//Therefore, trim it more
									//paramValueString = paramValueString.substring(0, 2900);
									//Due to some param strings exceeding 6600 characters, I increased the column to the Oracle limit:  4000.
									paramValueString = paramValueString.substring(0, 3900);
								}

								System.out.println("Param value string is " + paramValueString);
								System.out.println("Param value string length is " + paramValueString.length());
							} 

						}
					}
					catch (Exception ex) {
						System.out.println(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getCause() + " " + ex.getStackTrace());
					}


					try {
						scheduleStatus = objRecurringObject.properties().getProperty("SI_SCHEDULE_STATUS").getValue().toString();
					} 
					catch (Exception ex){
						System.out.println(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getCause() + " " + ex.getStackTrace());
					}					


					try {
						//nextRunTime = (Calendar)objRecurringObject.properties().getProperty("SI_NEXTRUNTIME").getValue();
						nextRunTime.setTime((Date) objRecurringObject.properties().getProperty("SI_NEXTRUNTIME").getValue());
						//nextRunTime.setTime((Date) objRecurringObject.getSchedulingInfo().properties().getProperty("SI_NEXTRUNTIME").getValue());
					} 
					catch (Exception ex) {
						System.out.println(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getCause() + " " + ex.getStackTrace());
					}

					try {
						si_owner = (String)objRecurringObject.properties().getProperty("SI_OWNER").getValue();

					} catch (Exception ex) {

					}

					//					I used to set cookiecrumbs, but it is so time-consuming
					cookieCrumbs = "See report table for path";

					try {
						//destination = objRecurringObject.getSchedulingInfo().getDestination().getName();  //--> destination was null, but it can't be!
						//destination = (String)objRecurringObject.getSchedulingInfo().properties().getProperty("SI_DESTINATION").getValue();

						//VB:  destination = objRecurringObject.SendToDestination.Name and this works
						ISendable obj = (ISendable)objRecurringObject;						
						IDestinationPlugin destinationPlugin = Helper.getDestinationPlugin(objInfoStore, objRecurringObject.getKind());
						IDestination odestination = obj.getSendToDestination();	
						odestination.setFromPlugin(destinationPlugin);
						System.out.println("Line 535: odestination is " + odestination.getName());
						destination = odestination.getName();
						System.out.println("destination is " + destination + " for si_id " + objRecurringObject.getID());
						//distributionList = Helper.getDistributionList("uslexbcs02", objRecurringObject.getID());	
						if (destination.equals("CrystalEnterprise.Smtp")) {
							System.out.println("Line 540");							
							//distributionList = Helper.getDistributionList(EnterpriseEnv, objRecurringObject.getID());
							distributionList = Helper.getDistributionList1(objEnterpriseSession, EnterpriseEnv, objRecurringObject.getID());
							System.out.println("Line 543");
						}
						else
							System.out.println("Destination actually is:  " + destination);
					}
					catch(SDKException sdkEx)
					{
						System.out.println("Line 681 " + sdkEx.getDetailMessage() + " " + sdkEx.getStackTrace());
						Helper.runMSSQLInsertQuery("insert into STG1_BO4_TRACKER_ERRORS (ERROR_MESSAGE, LOAD_DT) VALUES ('" + sdkEx.getMessage() + " " + sdkEx.getStackTrace() + " " + EnterpriseEnv + "', GETDATE())");
					}

					try {

						ifiles = (IFiles)objRecurringObject.properties().getProperty("SI_FILES").getValue();
						outputFile = (String)ifiles.properties().getProperty("SI_PATH").getValue();
					} 
					catch (Exception ex) {
					}


					//Get the logon info
					try {
						iReport = (IReport) objRecurringObject;
						ISDKList dbLogons = iReport.getReportLogons();

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
					} 
					catch (Exception ex) {	}

					server = server1;
					//Get the number of prompts
					try {
						iReport = (IReport) objRecurringObject;
						List paramList = iReport.getReportParameters();
						numPrompts = paramList.size();	
					} 
					catch (Exception ex) {	}


					try {
						si_format_export_allpages = getFormatAllPagesSetting(objRecurringObject);
						if (si_format_export_allpages) {
							siFormatExportAllpages = "true";}
						else
							siFormatExportAllpages = "false";
					}
					catch (Exception ex) {
						//System.out.println(ex.getMessage() + " " + ex.getStackTrace() + " line 2187");
						//siFormatExportAllpages = "non-existent";
					}

					//Start time
					try {
						System.out.println("Start date is " + objRecurringObject.getSchedulingInfo().getBeginDate());
						//runDate = (Calendar)objRecurringObject.properties().getProperty("SI_STARTTIME").getValue();
						//nextRunTime.setTime((Date) objInfoObject.properties().getProperty("SI_NEXTRUNTIME").getValue());
						//ERROR runDate.setTime((Date) objRecurringObject.properties().getProperty("SI_STARTTIME").getValue());
						//runDate = (Calendar) objRecurringObject.getSchedulingInfo().getBeginDate();
						runDate.setTime((Date) objRecurringObject.getSchedulingInfo().getBeginDate());
						System.out.println("SI_STARTTIME is " + runDate.getTime());
						startTime = runDate;
						System.out.println("start time is " + startTime.getTime());

						//End time

						//endTime = (Calendar)objRecurringObject.properties().getProperty("SI_ENDTIME").getValue();
						//endTime.setTime((Date) objRecurringObject.properties().getProperty("SI_ENDTIME").getValue());
						endTime.setTime((Date) objRecurringObject.getSchedulingInfo().getEndDate());
						System.out.println("end time is " + endTime.getTime());

						theInterval = 60/(endTime.getTimeInMillis() - startTime.getTimeInMillis())*1000;
					} catch (Exception ex) {
						System.out.println("There was a problem getting Start time and/or End time " + ex.getMessage());
					}

					try {
						machineUsed = (String)objRecurringObject.getSchedulingInfo().properties().getProperty("SI_MACHINE_USED").getValue();
					} catch (Exception ex) {

					}

					try {
						submitter = Helper.safeSQL((String)objRecurringObject.getSchedulingInfo().properties().getProperty("SI_SUBMITTER").getValue());
						owner = submitter;
						System.out.println("submitter is " + owner);
					} catch (Exception ex) {
						System.out.println("There was a problem getting the submitter " + ex.getMessage());
					}


					try {
						//creationTime = (Calendar)objRecurringObject.properties().getProperty("SI_CREATION_TIME").getValue();
						creationTime.setTime((Date) objRecurringObject.properties().getProperty("SI_CREATION_TIME").getValue());
						System.out.println(creationTime.getTime());
					} catch (Exception ex) {

					}


					try {
						parentCuid = objRecurringObject.getParentCUID();
					} catch (Exception ex) {

					}

					try {
						parentFolderCuid = (String) objRecurringObject.properties().getProperty("SI_PARENT_FOLDER_CUID").getValue();
					} catch (Exception ex) {

					}

					try {
						cuid = objRecurringObject.getCUID();
					} catch (Exception ex) {

					}

					try {
						//scheduleType = (int)objRecurringObject.getSchedulingInfo().properties().getProperty("SI_SCHEDULE_TYPE").getValue();
						scheduleType = objRecurringObject.getSchedulingInfo().getType();
					} catch (Exception ex) {

					}


					try {

					} catch (Exception ex) {
						//scheduleIntervalMinutes = (int)objRecurringObject.getSchedulingInfo().properties().getProperty("SI_SCHEDULE_INTERVAL_MINUTES").getValue();
						scheduleIntervalMinutes = objRecurringObject.getSchedulingInfo().getIntervalMinutes();
					}


					try {
						scheduleIntervalHours = objRecurringObject.getSchedulingInfo().getIntervalHours();
					} catch (Exception ex) {

					}

					try {
						scheduleIntervalDays = objRecurringObject.getSchedulingInfo().getIntervalDays();
					} catch (Exception ex) {

					}

					try {
						scheduleIntervalMonths = objRecurringObject.getSchedulingInfo().getIntervalMonths();
					} catch (Exception ex) {

					}


					//msSQLInsertString = "insert into " + destTblNm + " (REPORT_ID, INSTANCE_ID, SI_STARTTIME, SI_ENDTIME, DURATION, SI_SUBMITTER, SI_SERVER, SI_OWNER, SI_SCHEDULE_STATUS,REPORT_PATH, SI_NAME, SI_CREATION_TIME, SI_NUM_PROMPTS, DATASRC_LOGIN_NM, SI_PASSWORD, DATASRC_LOGIN_NM2, SI_PASSWORD2, SI_ERROR_MESSAGE, SI_MACHINE_USED, SI_UPDATE_TS, DESTINATION, OUTPUT_FILE, REPORT_CUID, SI_PARENT_FOLDER_CUID, DISTRIBUTION_LIST, si_file1) values (" + objRecurringObject.getParentID() + ", " + objRecurringObject.getID() + ", to_date('" + objRecurringObject.properties().getProperty("SI_STARTTIME").getValue() + "','MM/DD/YYYY HH12:MI:SS AM')" + ", to_date('" + objRecurringObject.properties().getProperty("SI_ENDTIME").getValue() + "','MM/DD/YYYY HH12:MI:SS AM')" + ", " + theInterval + ", '" + Helper.safeSQL((String)objRecurringObject.getSchedulingInfo().properties().getProperty("SI_SUBMITTER").getValue()) + "', '" + Helper.safeSQL(server) + "', '" + Helper.safeSQL((String)objRecurringObject.getSchedulingInfo().properties().getProperty("SI_SUBMITTER").getValue()) + "', " + objRecurringObject.properties().getProperty("SI_SCHEDULE_STATUS").getValue() + ", '" + Helper.safeSQL(cookieCrumbs) + "', '" + Helper.safeSQL(objRecurringObject.getTitle()) + "', to_date('" + objRecurringObject.properties().getProperty("SI_CREATION_TIME").getValue() + "','MM/DD/YYYY HH12:MI:SS AM'), " + numPrompts + ",'" + logon1 + "','" + pswd1 + "', '" + logon2 + "', '" + pswd2 + "','" + errorMsg + "','" + objRecurringObject.getSchedulingInfo().properties().getProperty("SI_MACHINE_USED").getValue() + "', to_date('" + objRecurringObject.properties().getProperty("SI_UPDATE_TS").getValue() + "','MM/DD/YYYY HH12:MI:SS AM'), '" + destination + "','" + outputFile + "', '" + objRecurringObject.getParentCUID() + "', '" + objRecurringObject.properties().getProperty("SI_PARENT_FOLDER_CUID").getValue() + "','" + Helper.getDistributionList(EnterpriseEnv, objRecurringObject.getID()) + "', '" + si_file1 + "')";

					//THIS IS THE NEW QUERY. Convert to java syntax.
					//msSQLInsertString = "insert into " + destTblNm + " (REPORT_ID, INSTANCE_ID, SI_STARTTIME, SI_ENDTIME, DURATION, SI_SUBMITTER, SI_SERVER, SI_OWNER, SI_SCHEDULE_STATUS,REPORT_PATH, SI_NAME, SI_CREATION_TIME, SI_NUM_PROMPTS, DATASRC_LOGIN_NM, SI_PASSWORD, DATASRC_LOGIN_NM2, SI_PASSWORD2, SI_ERROR_MESSAGE, SI_MACHINE_USED, SI_UPDATE_TS, DESTINATION, OUTPUT_FILE, REPORT_CUID, SI_PARENT_FOLDER_CUID, DISTRIBUTION_LIST, si_file1, ENVIRONMENT, SI_KIND, SI_FORMAT_EXPORT_ALLPAGES, CUID, PARAM_VALUES) values (" + objRecurringObject2.ParentID + ", " + objRecurringObject2.ID + ", to_date('" + objRecurringObject2.Properties.Item("SI_STARTTIME").Value + "','MM/DD/YYYY HH12:MI:SS AM')" + ", to_date('" + objRecurringObject2.Properties.Item("SI_ENDTIME").Value + "','MM/DD/YYYY HH12:MI:SS AM')" + ", " + theInterval + ", '" + safeSQL(objRecurringObject2.SchedulingInfo.Properties.Item("SI_SUBMITTER").Value) + "', '" + safeSQL(server) + "', '" + safeSQL(objRecurringObject2.SchedulingInfo.Properties.Item("SI_SUBMITTER").Value) + "', " + objRecurringObject2.Properties.Item("SI_SCHEDULE_STATUS").Value + ", '" + safeSQL(cookieCrumbs) + "', '" + safeSQL(objRecurringObject2.Title) + "', to_date('" + objRecurringObject2.Properties.Item("SI_CREATION_TIME").Value + "','MM/DD/YYYY HH12:MI:SS AM'), " + numPrompts + ",'" + logon1 + "','" + pswd1 + "', '" + logon2 + "', '" + pswd2 + "','" + errorMsg + "','" + objRecurringObject2.SchedulingInfo.Properties.Item("SI_MACHINE_USED").Value + "', to_date('" + objRecurringObject2.Properties.Item("SI_UPDATE_TS").Value + "','MM/DD/YYYY HH12:MI:SS AM'), '" + destination + "','" + outputFile + "', '" + objRecurringObject2.ParentCUID + "', '" + objRecurringObject2.Properties.Item("SI_PARENT_FOLDER_CUID").Value + "','" + distributionList + "', '" + si_file1 + "','" + environ + "','" + objRecurringObject2.Kind + "','" + siFormatExportAllpages + "','" + objRecurringObject2.CUID + "','" + safeSQL(paramValues) + "')"

					//msSQLInsertString = "insert into " + destTblNm + " (REPORT_ID, INSTANCE_ID, SI_STARTTIME, SI_ENDTIME, SI_SUBMITTER, SI_SERVER, SI_OWNER, SI_SCHEDULE_STATUS, SI_SCHEDULE_TYPE, SI_SCHEDULE_INTERVAL_MINUTES, SI_SCHEDULE_INTERVAL_HOURS, SI_SCHEDULE_INTERVAL_MONTHS, SI_NAME, SI_CREATION_TIME, SI_DESTINATION, SI_PASSWORD, DATASRC_LOGIN_NM,  DATASRC_LOGIN_NM2, SI_PASSWORD2, SI_NUM_PROMPTS, REPORT_PATH, REPORT_CUID, SI_MACHINE_USED, SI_NEXTRUNTIME, PARAM_VALUES, DISTRIBUTION_LIST, CUID, SI_FORMAT_EXPORT_ALLPAGES, SI_KIND) values (" + objRecurringObject2.ParentID + ", " + objRecurringObject2.ID + ", to_date('" + objRecurringObject2.SchedulingInfo.Properties.Item("SI_STARTTIME").Value + "','MM/DD/YYYY HH12:MI:SS AM')" + ", to_date('" + objRecurringObject2.SchedulingInfo.Properties.Item("SI_ENDTIME").Value + "','MM/DD/YYYY HH12:MI:SS AM')" + ", '" + safeSQL(objRecurringObject2.SchedulingInfo.Properties.Item("SI_SUBMITTER").Value) + "', '" + server1 + "', '" + safeSQL(objRecurringObject2.Properties.Item("SI_OWNER").Value) + "', " + objRecurringObject2.Properties.Item("SI_SCHEDULE_STATUS").Value + ", " + objRecurringObject2.SchedulingInfo.Properties.Item("SI_SCHEDULE_TYPE").Value + "," + objRecurringObject2.SchedulingInfo.Properties.Item("SI_SCHEDULE_INTERVAL_MINUTES").Value + "," + objRecurringObject2.SchedulingInfo.Properties.Item("SI_SCHEDULE_INTERVAL_HOURS").Value + "," + objRecurringObject2.SchedulingInfo.Properties.Item("SI_SCHEDULE_INTERVAL_MONTHS").Value + ", '" + safeSQL(objRecurringObject2.Title) + "', to_date('" + objRecurringObject2.Properties.Item("SI_CREATION_TIME").Value + "','MM/DD/YYYY HH12:MI:SS AM'), '" + destination + "','" + pswd1 + "', '" + logon1 + "','" + logon2 + "', '" + pswd2 + "', " + nbrPrompts + ",'" + safeSQL(cookieCrumbs) + "','" + objRecurringObject2.ParentCUID + "'," + "'" + machine_used + "'," + " to_date('" + nextRunTime + "','MM/DD/YYYY HH12:MI:SS AM'), '" + paramValues + "', '" + distributionList + "','" + objRecurringObject2.CUID + "','" + siFormatExportAllpages + "','" + objRecurringObject2.Kind + "')"
					//					still need to get SI_SCHEDULE_TYPE, SI_SCHEDULE_INTERVAL_MINUTES, SI_SCHEDULE_INTERVAL_HOURS, SI_SCHEDULE_INTERVAL_MONTHS
					System.out.println("Param value string length is " + paramValueString.length());
					/*	msSQLInsertString = "insert into " + destTblNm + " (REPORT_ID, INSTANCE_ID, SI_STARTTIME, SI_ENDTIME,  SI_SUBMITTER, SI_SERVER, SI_OWNER, SI_SCHEDULE_STATUS,REPORT_PATH, SI_NAME, SI_CREATION_TIME, SI_NUM_PROMPTS, DATASRC_LOGIN_NM, SI_PASSWORD, DATASRC_LOGIN_NM2, SI_PASSWORD2,  SI_MACHINE_USED,  SI_DESTINATION,  REPORT_CUID,  DISTRIBUTION_LIST,  SI_KIND, SI_FORMAT_EXPORT_ALLPAGES, CUID, PARAM_VALUES,SI_SCHEDULE_TYPE, SI_SCHEDULE_INTERVAL_MINUTES, SI_SCHEDULE_INTERVAL_HOURS, SI_SCHEDULE_INTERVAL_MONTHS, SI_SCHEDULE_INTERVAL_DAYS, SI_NEXTRUNTIME) values (" + objRecurringObject.getParentID() + ", ";
					msSQLInsertString += objRecurringObject.getID();
					msSQLInsertString += ", to_date('" + Helper.msSQLDateTime(startTime) + "','DD-MM-YYYY HH24:MI:SS')";
					msSQLInsertString += ", to_date('" + Helper.msSQLDateTime(endTime) + "','DD-MM-YYYY HH24:MI:SS')"  + ", '";
					msSQLInsertString += submitter;					
					msSQLInsertString += "', '" + Helper.safeSQL(server);
					msSQLInsertString += "', '" + owner;					
					msSQLInsertString += "', " + scheduleStatus;
					msSQLInsertString += ", '" + Helper.safeSQL(cookieCrumbs) + "', '" + Helper.safeSQL(objRecurringObject.getTitle());//threw an error
					msSQLInsertString += "', to_date('" + Helper.msSQLDateTime(creationTime) + "','DD-MM-YYYY HH24:MI:SS'), ";					
					msSQLInsertString += numPrompts + ",'" + logon1 + "','" + pswd1 + "', '" + logon2 + "', '" + pswd2  + "','" + machineUsed + "','";
					//msSQLInsertString += "', to_date('" + Helper.msSQLDateTime(updateTS) + "','MM/DD/YYYY HH12:MI:SS AM'), '";
					msSQLInsertString += destination +  "', '" ;
					msSQLInsertString += parentCuid;
					//the error is in this line:
					msSQLInsertString += "','" + distributionList + "','" + objRecurringObject.getKind() + "', '" + siFormatExportAllpages + "','";
					msSQLInsertString += cuid;
					msSQLInsertString += "','" + Helper.safeSQL(paramValueString) + "',";	
					System.out.println("String length of param Value String is " + paramValueString.length()); //still says 2995, but Eclipse says Oracle says 3072 or 3076; even if I check this string in Oracle directly, it says 2977!
					msSQLInsertString +=  scheduleType + ", " + scheduleIntervalMinutes + ", " + scheduleIntervalHours + ", " + scheduleIntervalMonths + ", " + scheduleIntervalDays + ", to_date('" + Helper.msSQLDateTime(nextRunTime) + "','DD-MM-YYYY HH24:MI:SS')) ";
					 */
					msSQLInsertString = "insert into " + destTblNm + " (REPORT_ID, INSTANCE_ID, SI_STARTTIME, SI_ENDTIME,  SI_SUBMITTER, SI_SERVER, SI_OWNER, SI_SCHEDULE_STATUS,REPORT_PATH, SI_NAME, SI_CREATION_TIME, SI_NUM_PROMPTS, DATASRC_LOGIN_NM, SI_PASSWORD, DATASRC_LOGIN_NM2, SI_PASSWORD2,  SI_MACHINE_USED,  SI_DESTINATION,  REPORT_CUID,  DISTRIBUTION_LIST,  SI_KIND, SI_FORMAT_EXPORT_ALLPAGES, CUID, PARAM_VALUES,SI_SCHEDULE_TYPE, SI_SCHEDULE_INTERVAL_MINUTES, SI_SCHEDULE_INTERVAL_HOURS, SI_SCHEDULE_INTERVAL_MONTHS, SI_SCHEDULE_INTERVAL_DAYS, SI_NEXTRUNTIME) values (" + objRecurringObject.getParentID() + ", ";
					msSQLInsertString += objRecurringObject.getID();
					msSQLInsertString += ", ('" + Helper.msSQLDateTime(startTime) + "')";
					msSQLInsertString += ", ('" + Helper.msSQLDateTime(endTime) + "')"  + ", '";
					msSQLInsertString += submitter;					
					msSQLInsertString += "', '" + Helper.safeSQL(server);
					msSQLInsertString += "', '" + owner;					
					msSQLInsertString += "', " + scheduleStatus;
					msSQLInsertString += ", '" + Helper.safeSQL(cookieCrumbs) + "', '" + Helper.safeSQL(objRecurringObject.getTitle());//threw an error
					msSQLInsertString += "', ('" + Helper.msSQLDateTime(creationTime) + "'), ";					
					msSQLInsertString += numPrompts + ",'" + logon1 + "','" + pswd1 + "', '" + logon2 + "', '" + pswd2  + "','" + machineUsed + "','";
					msSQLInsertString += destination +  "', '" ;
					msSQLInsertString += parentCuid;
					//the error is in this line:
					msSQLInsertString += "','" + distributionList + "','" + objRecurringObject.getKind() + "', '" + siFormatExportAllpages + "','";
					msSQLInsertString += cuid;
					msSQLInsertString += "','" + Helper.safeSQL(paramValueString) + "',";	
					System.out.println("String length of param Value String is " + paramValueString.length()); //still says 2995, but Eclipse says Oracle says 3072 or 3076; even if I check this string in Oracle directly, it says 2977!
					msSQLInsertString +=  scheduleType + ", " + scheduleIntervalMinutes + ", " + scheduleIntervalHours + ", " + scheduleIntervalMonths + ", " + scheduleIntervalDays + ", ('" + Helper.msSQLDateTime(nextRunTime) + "')) "; 

					System.out.println(msSQLInsertString);
					//si_submitter shouldn't be null and start time and end time should have real values and not default to 1980
					try
					{
						Helper.runMSSQLInsertQuery(msSQLInsertString);
						destination = "";
					}
					catch (Exception ex)
					{
						System.out.println("Problem with insert query");
						Helper.runMSSQLInsertQuery("insert into STG1_BO4_TRACKER_ERRORS (ERROR_MESSAGE, LOAD_DT) VALUES ('" + ex.getMessage() + " " + ex.getStackTrace() + " " + EnterpriseEnv + "', GETDATE())");
						System.out.println(ex.getMessage() + " " + ex.getStackTrace() + " line 2208");
					}
				}
			}



			// Clean up the Enterprise Session.
			if(objEnterpriseSession != null) {
				try {
					objEnterpriseSession.logoff();

				} catch(Exception e_ignore_in_cleanup) {}
			}
		}
		catch (Exception ex)
		{
			System.out.println(ex.getMessage() + " " + ex.getStackTrace() + " line 2215");
			Helper.runMSSQLInsertQuery("insert into STG1_BO4_TRACKER_ERRORS (ERROR_MESSAGE, LOAD_DT) VALUES ('" + ex.getMessage() + " " + ex.getStackTrace() + " " + EnterpriseEnv + "', GETDATE())");
			//'sendmail("Logon error", ex.getMessage() + " " + ex.getStackTrace)
			//Helper.sendmail("bishopp@lexmark.com", "bishopp@lexmark.com, amarabes@lexmark.com,ajpatel@lexmark.com,msankar@lexmark.com,ajain@lexmark.com", "Logon error - load_stg1_migr_XI_reports(String, String, String, String, String) - " + EnterpriseEnv, ex.getMessage() + " " + ex.getStackTrace());
			//Helper.sendmail("bishopp@lexmark.com", "bishopp@lexmark.com", "Logon error - load_stg1_boxi_runhist(String, String, String, String, String) - " + EnterpriseEnv, ex.getMessage() + " " + ex.getStackTrace());

		}


	}

	public static void load_stg1_boxi_recurringjobsIncremental(String EnterpriseEnv, String login, String pswd,String auth, String destTblNm, String environ){
		//Precondition:  today's recurring jobs since the last load are not in the database table.
		//Postcondition:  today's recurring jobs since the last load are in the database table.

		//Dec 9, 2014, I'm not sure this is running:
		//Helper.runMSSQLInsertQuery("insert into STG1_BO4_TRACKER_ERRORS (ERROR_MESSAGE, LOAD_DT) VALUES ('recurring jobs started " + EnterpriseEnv + "', GETDATE())");

		IEnterpriseSession objEnterpriseSession;
		//IInfoStore objInfoStore;
		IInfoObjects colInfoObjects;
		IInfoObjects colRecurObjects;
		IInfoObject objInfoObject;
		IInfoObject objInstObject;
		IInfoObject objRecurringObject;
		int instanceID;
		String errorMsg = "";
		String cookieCrumbs;
		int count;
		String si_owner;
		Calendar runDate = Calendar.getInstance();
		Calendar startTime = Calendar.getInstance();
		startTime.set(1980,1,1,0,0,0);
		Calendar endTime = Calendar.getInstance();
		endTime.set(1980,1,1,0,0,0);
		long theInterval = 0;
		Calendar startDate = Calendar.getInstance();;
		startDate.set(1980,1,1,0,0,0);
		Calendar endDate = Calendar.getInstance();;
		endDate.set(1980,1,1,0,0,0);
		String dtToday;
		String msSQLInsertString;
		String maxID ="0";
		String logon1 = "unknown";
		String logon2 = "unknown";
		String pswd1 = "unknown";
		String pswd2 = "unknown";
		String datasrc1 = "not found in CMS";
		String datasrc2 = "not found in CMS";
		int numPrompts = 0;
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
		String scheduleStatus = "9";
		IProcessingInfo processingInfo;
		IReport iReport;
		String server;
		String server1 = "";
		String server2 = "";
		IProperty aggCount;
		String strCount="0";
		int colCount = 0;
		Boolean si_format_export_allpages;
		String siFormatExportAllpages  = "non-existent";
		String paramValues;
		IPrompts webiPrompts;
		String recurringIDList ="0";
		String machine_used;
		//IPrompts siPrompts;
		String numLogins;
		int nbrPrompts =0;
		String promptName;
		String paramValueString = "";

		Calendar nextRunTime = Calendar.getInstance();
		nextRunTime.set(1980,1,1,0,0,0);
		String machineUsed = "";
		String submitter = "";
		String owner = "";
		Calendar creationTime = Calendar.getInstance();
		creationTime.set(1980,1,1,0,0,0);

		Calendar updateTS = Calendar.getInstance();
		updateTS.set(1980,1,1,0,0,0);

		String parentCuid = "unknown";
		String parentFolderCuid = "";
		String cuid = "";
		int scheduleType = 0;
		int scheduleIntervalMinutes = 0;
		int scheduleIntervalDays = 0;
		int scheduleIntervalHours = 0;
		int scheduleIntervalMonths = 0;
		String maxid;
		int intDestinationCount;
		String theProgID;



		try {
			ISessionMgr  objSessionMgr = CrystalEnterprise.getSessionMgr();
			objEnterpriseSession = objSessionMgr.logon(login, pswd, EnterpriseEnv, auth);    
			IInfoStore objInfoStore = (IInfoStore) objEnterpriseSession.getService("InfoStore");

			maxid = Helper.getMaxID("select max(instance_id) maxID from " + destTblNm);
			///////////////////////////
			//maxid = "0";
			System.out.println("maxid is " + maxid);

			if (maxid == "0")  {
				//'strInfoSQL = "SELECT count(SI_ID) from CI_INFOOBJECTS WHERE SI_PROGID= 'CrystalEnterprise.Report' and SI_INSTANCE = 0"
				strInfoSQL = "SELECT count(SI_ID) from CI_INFOOBJECTS WHERE SI_RECURRING = 1 and SI_SCHEDULE_STATUS = 9 order by si_id";

			}
			else  ////////UNCOMMENT AFTER WE GO LIVE
				//strInfoSQL = "SELECT count(SI_ID) from CI_INFOOBJECTS WHERE SI_KIND in ('CrystalReport') and SI_INSTANCE = 0 and si_creation_time >= '" + Helper.formatDateBO(maxCreationDate) + "'" ;
				strInfoSQL = "SELECT count(SI_ID) from CI_INFOOBJECTS WHERE SI_RECURRING = 1 and SI_SCHEDULE_STATUS = 9 and SI_ID > " + maxid + "  order by si_id";



			System.out.println(strInfoSQL);

			colInfoObjects = (IInfoObjects)objInfoStore.query(strInfoSQL);
			//objInfoObject = (IInfoObject)colInfoObjects.get(i); 

			objInfoObject = (IInfoObject)colInfoObjects.get(0);

			//The VB code used the collection here, but in Java, properties() isn't an available function, so I am trying to get this via InfoObject

			//System.out.println(objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").properties().getProperty("SI_ID"));
			System.out.println(objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").getID());//shows the ID, not the count!
			//count = objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").properties().getProperty("SI_ID");
			//count = objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").getID();
			aggCount = objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT");

			count = objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").getID();
			strCount = ((IProperties)objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").getValue()).getProperty("SI_ID").getValue().toString();

			System.out.println("Record count is " + strCount);

			//''***************PRODUCTION CODE***********UNCOMMENT AFTER TEST
			if (maxid == "0") {
				strInfoSQL = "SELECT TOP " + strCount + " * FROM CI_INFOOBJECTS WHERE SI_RECURRING = 1 and SI_SCHEDULE_STATUS = 9";

				/////////////THIS IS TO DEBUG WHY 78 REPORTS AREN'T GETTING LOADED////////////////
				//strInfoSQL = "SELECT TOP " + strCount + " SI_ID FROM CI_INFOOBJECTS WHERE CUID in ('Aft3iSZVMf9DsqZckj.rGTs', 'ASoItkg1BERAvRhh3DNr2MY', 'Ab4OtSUbf4hOozQNSSwTKks', 'AR5D.blIvD1Likk7WIimYn0', 'AYlJYAHUiMRLp3yNENfWCk8', 'ATasuIyz2bhMg2vRm3UxFYc', 'ARkg2pB.wepAp4gsaWxO0og', 'AQmOscIdT45DoVztZGVr5Xk', 'AWphIRJpIGpCilCEq2mQEwI', 'AeYn7TL7JoRGk9Urd_Ascq8', 'AchsiVtJPXtMlk6TNuWNsxU', 'AUSn9WjaASJPkL_C6cgwpX0', 'AR.Z6i8PcpxJn1dO6uluoqQ', 'AZH07dLG1ipAtF3nHCgCg78', 'AUM4.kCHV1JNvAlUA4H2804', 'AUa3zEwrpphPs8jN9C._B3M', 'AQl37D2ZcVpPju3NOVZbZUA', 'Ab_nasfKlY5NjCU6ERGegO8', 'Ab_R5islS1RCvf13ftHkakw', 'ASRfqAbMowRJl6BboScc0UI', 'AWdHKCmapIVHmLqLPdWe0Lk', 'AfwjsFgym8dDtvfEwJDfdLA', 'ASxd.Ww0WeNMvfYQA9kS8yc', 'Aa7b87HBpLVOr_sxEYS2Hus', 'AQD9B1oZSrJAlGDfdPRFbHQ', 'AcS6w6nRJxhPrhJ38PQANY0', 'Af3aRD1xqM9JrJFNXemjYPQ', 'AYGRmSSXW6NIhyPI8wFpfoY', 'AUJKAt_NBwJCgtWJftU63AA', 'Ae_vkGYDlW5Ag9F93BRZGW8', 'ATKkK8N2gvBKgoLcSyCQhvE', 'AZv8arSzluJGhe2bhxpUd5A', 'ATSbLtcljRVNgEgaZLdZx50', 'AS7Obx4zEd5LnlJOsH089eA', 'AYaezAvr1wVGrQ0sWfMdmeM', 'AWxl8wgXCSNDnl3eSk5VDfw', 'AVFyvrebBQVIncmr7hgAr5U', 'ASCarWwCIBFMgirePQq_75U', 'AYKbcOMlUspHvAG9cbCOiPc', 'Aa1OAVNXOc5FtQUyBHhGm4M', 'AZbfyx3Q6H5JtgxmVwtK2YA', 'AavVPBoddS5FjfEgjMaplbI', 'ARXLPLX8NFJLu5Zp_0Lj2vA', 'AVXgu6OEwWRAsx_BJo9atLg', 'AaXVLtwyGDlIj8VfmnSdbFQ', 'AXTRTlzgPBpNkpnXjVFqmgQ', 'AWVBcyH.s4VBiSIXd.WAmSk', 'AWZsdtlrzN5CgcV6FkhzlNc', 'AbiKZWM2euVFiVtCyS62e9k', 'AUn0Rxm93YNHvDSu_KtmgDU', 'AZdEukjY4XJMqotv9BeM66c', 'AbJAuTmDx9hNuukJ2g_5AEw', 'AQAeGwGCGkNLhviiI_r1o1s', 'AdAzD9FQflxPjKO0Tk3U2a8', 'ASSGKgUE7EBMtvNXXIBqykU', 'AXMhekO6bTxJg3_sYNcqF6o', 'AYRimkQynQxDtod0TJ1FBVk', 'AW5vxQzoUiVPn5H3X5DkCJ8', 'ATwpV1Mm.2ROs53KXUjQOU0', 'AdEvFioCCQ1Ij.qo3bu8JGk', 'Abe1SvevEnhBkZgWmPlojOs', 'AQoYvBa9H5xKsag.prhxw.U', 'AeBoUjX2p4JKnAA.1w34XDY', 'AexxgbS95BdGjzW2UvsLvaI', 'ATflUMs7_xRDpcqW1noe6oU', 'Abu1qtrUEk5HoQtiA2O7cPo', 'AQTIAwgIxKxCmDaHf5NYF6I', 'AYBfimABkBBFl.T7SCGn23w', 'AXcygrcBjatPse1D.1BPjeI', 'ASeJLZVfT3VDjbWyD5ylW2s', 'AZVVIu_DQV5IpytYDLcMBNs', 'AQ.yoiSAjgVDg.CgPfGJhnI')";
			}
			else  //////////UNCOMMENT AFTER GO LIVE
				//strInfoSQL = "SELECT TOP " + strCount + " SI_ID FROM CI_INFOOBJECTS WHERE SI_KIND in ('CrystalReport') and SI_INSTANCE = 0 and SI_CREATION_TIME >= '" + Helper.formatDateBO(maxCreationDate) + "'";
				strInfoSQL = "SELECT TOP " + strCount + " * FROM CI_INFOOBJECTS WHERE SI_RECURRING = 1 and SI_SCHEDULE_STATUS = 9 and SI_ID > " + maxid;
			//Limit to 1000 to avoid a java heap error
			//strInfoSQL = "SELECT TOP 1000 * FROM CI_INFOOBJECTS WHERE SI_RECURRING = 1 and SI_SCHEDULE_STATUS = 9 and SI_ID > " + maxid;

			//strInfoSQL = "SELECT TOP " + strCount + " SI_ID FROM CI_INFOOBJECTS WHERE SI_KIND in ('CrystalReport') and SI_INSTANCE = 0 and SI_ID =52944";

			/////////JUST TEMPORARY TO DEBUG THE PARAMETER VALUES FOR CRYSTAL REPORTS
			//strInfoSQL = "SELECT TOP " + strCount + " * FROM CI_INFOOBJECTS WHERE SI_RECURRING = 1 and SI_ID in (34997141, 35001128, 35001264, 35004797)";
			//strInfoSQL = "SELECT TOP " + strCount + " * FROM CI_INFOOBJECTS WHERE SI_RECURRING = 1 and SI_ID in (154298)";
			//strInfoSQL = "SELECT TOP 1000 * FROM CI_INFOOBJECTS WHERE SI_RECURRING = 1 and SI_ID > 154298";
			//strInfoSQL = "SELECT TOP " + strCount + " * FROM CI_INFOOBJECTS WHERE SI_RECURRING = 1 and SI_ID > 154298";
			///////JUST TEMPORARY TO DEBUG THE SERVER INFO
			//strInfoSQL = "SELECT TOP " + strCount + " SI_ID FROM CI_INFOOBJECTS WHERE SI_KIND in ('CrystalReport') and SI_INSTANCE = 0 and SI_ID in (590858, 716770)";

			System.out.println(strInfoSQL);
			colRecurObjects = (IInfoObjects)objInfoStore.query(strInfoSQL);
			System.out.println("Line 210 Count of objects in collection: " + colRecurObjects.size());



			//'*************INSERT THE SI_ID'S INTO A TEMP TABLE TO COMPARE WITH WHAT YOU GOT LOADED IN THE RECURRINGJOBS TABLE********
			Helper.runMSSQLInsertQuery("truncate table temp_recurring_ids");
			for (int y = 0; y < colRecurObjects.size(); y++){
				objRecurringObject = (IInfoObject)colRecurObjects.get(y);
				Helper.runMSSQLInsertQuery("insert into temp_recurring_ids (recurring_id) values (" + objRecurringObject.getID() + ")");
			}


			//'****************************************************************************************************

			if (colRecurObjects.size() > 0){
				Helper.runMSSQLInsertQuery("insert into STG1_BO4_TRACKER_ERRORS (ERROR_MESSAGE, LOAD_DT) VALUES ('LoadFunctions() line 948 " + EnterpriseEnv + "', GETDATE())");
				//Connection conn=null;
				//Class.forName("oracle.jdbc.driver.OracleDriver");
				//conn=DriverManager.getConnection("jdbc:oracle:thin:@dlxkrbodb1.lex.lexmark.com:1750:edr1den1","botrack","********");
				//Statement s=conn.createStatement();
				//'This records all the instances found on the BOXI system
				colCount = colRecurObjects.size();
				for (int x=0; x <= colCount; x++){
					//msSQLInsertString = "insert into temp_p_allinstances(report_id, instance_id) values (" + objInfoObject.getParentID() + "," + objInfoObject.getID() + ")";
					//System.out.println(msSQLInsertString);
					//s.execute(msSQLInsertString);
					objRecurringObject = (IInfoObject)colRecurObjects.get(x);
					instanceID = objRecurringObject.getID();
					System.out.println ("si_id is " + objRecurringObject.getID());
					System.out.println("report title is " + objRecurringObject.getTitle());

					//Error for the following line:  The property with ID SI_SCHEDULE_STATUS does not exist in the object. [Ljava.lang.StackTraceElement;@1274069 line 2215
					//But I can see it in Query Builder!
					try {
						//System.out.println("schedule status is " + objRecurringObject.getSchedulingInfo().getStatus());
						//Oct 14, 2013: the following line gave a Null error:
						System.out.println("schedule status is " + objRecurringObject.properties().getProperty("SI_SCHEDULE_STATUS").getValue().toString());

					}
					catch (Exception ex){
						System.out.println(ex.getMessage());
						//The property with ID SI_SCHEDULE_STATUS does not exist in the object - it's true
					}
					// 'Reset these for each recurring job
					si_owner = "";
					logon1 = "";
					pswd1 = "";
					logon2 = "";
					pswd2 = "";
					server1 = "";
					machine_used = "unknown";
					nextRunTime.set(1980,1,1,0,0,0);
					paramValues = "";
					paramValueString = "";
					numPrompts = 0;

					//Run this only if the report has parameters
					try {

						IProperties siProperties;
						Helper.runMSSQLInsertQuery("insert into STG1_BO4_TRACKER_ERRORS (ERROR_MESSAGE, LOAD_DT) VALUES ('LoadFunctions() line 993 " + EnterpriseEnv + "', GETDATE())");
						// Get the SI_PROMPTS property bag from the InfoObject's Processing Info
						System.out.println("Report kind: " + objRecurringObject.getKind());
						//if (objRecurringObject.getKind().toString() == "CrystalReport") {
						if (objRecurringObject.properties().getProperty("SI_PROGID_MACHINE").getValue().toString().equals("CrystalEnterprise.Report")) {
							siProperties = (IProperties) objRecurringObject.getProcessingInfo().properties().getProperty("SI_PROMPTS").getValue();
							//Aug 13, 2013:  this isn't working for a CDM Webi report, RS0801, it's null even though the report does have properties.
						}
						else {
							siProperties = (IProperties) objRecurringObject.getProcessingInfo().properties().getProperty("SI_WEBI_PROMPTS").getValue();
						}

						//IProperties oProcessInfo = (IProperties) objInfoObject.getProcessingInfo().properties();
						//IProperties siPrompts = (IProperties) oProcessInfo.getProperty("SI_PROMPTS").getValue();
						// Make sure that there are parameters.
						if (siProperties != null)
						{
							// Get the number of prompts (the number of parameters)
							//if (objRecurringObject.getKind().toString() == "CrystalReport") {
							if (objRecurringObject.properties().getProperty("SI_PROGID_MACHINE").getValue().toString().equals("CrystalEnterprise.Report")) {
								numPrompts = ((Integer)siProperties.getProperty("SI_NUM_PROMPTS").getValue()).intValue();
							}  else {
								numPrompts = ((Integer)siProperties.getProperty("SI_TOTAL").getValue()).intValue();   
							}
							System.out.println("number of prompts: "  + numPrompts);


							//siPrompts = (IPrompts)objRecurringObject.getProcessingInfo().properties().getProperty("SI_PROMPTS").getValue();
							//now this error:
							//com.crystaldecisions.sdk.properties.internal.SDKPropertyBag cannot be cast to com.crystaldecisions.sdk.prompting.IPrompts com.crystaldecisions.sdk.properties.internal.SDKPropertyBag cannot be cast to com.crystaldecisions.sdk.prompting.IPrompts null [Ljava.lang.StackTraceElement;@7c218e

							//nbrPrompts = siPrompts.size();
							//if (nbrPrompts >0) {
							if (numPrompts > 0) {
								//paramValues = Helper.getParamValueString2(objRecurringObject.getID(), login, pswd, EnterpriseEnv, auth);
								//This one is throwing that awful ContentProcesingHandler that happens when I make a really stupid mistake.

								//paramValues = Helper.getParamValueString(objRecurringObject); //this throws an error:						
								//com.crystaldecisions.sdk.plugin.desktop.excel.internal.a cannot be cast to com.crystaldecisions.sdk.plugin.desktop.report.IReport
								//or the function returns Null on everything even though the recurring job has parameters


								try{
									// Get the SI_PROMPTS property bag from the InfoObject's Processing Info - got this from SAP
									//IProperties siProperties = (IProperties) objInfoObject.getProcessingInfo().properties().getProperty("SI_PROMPTS").getValue();
									//Error:  properties() and getPRoperty() undefined for IProcessingInfo

									// Loop through the parameters to get the parameter names and parameter values.
									for(i=1;i<=numPrompts;i++)
									{
										IProperties prompt;
										// Each prompt has the name SI_PROMPT + a digit starting from 1.  For example, if
										// there are 2 prompts, the first one would be called SI_PROMPT1 and the second
										// one would be called SI_PROMPT2.
										//IProperties prompt = (IProperties)siPrompts.getProperty("SI_PROMPT"+i).getValue();
										//if (objRecurringObject.getKind().toString() == "CrystalReport") {
										if (objRecurringObject.properties().getProperty("SI_PROGID_MACHINE").getValue().toString().equals("CrystalEnterprise.Report")) {
											prompt = (IProperties)siProperties.getProperty("SI_PROMPT"+i).getValue();
										}
										else {
											prompt = (IProperties)siProperties.getProperty(i).getValue();
										}

										// Get the parameter name.
										promptName = prompt.getProperty("SI_NAME").getValue().toString();
										System.out.println("Prompt name is "  + promptName);

										// First column in the row is the parameter name.
										//out.println("<td>Parameter Name = "+promptName+"</td><td><table>");
										// Get the current values property bag.
										IProperties currentValues;
										String theValue;
										//if (objRecurringObject.getKind().toString() == "CrystalReport") {
										if (objRecurringObject.properties().getProperty("SI_PROGID_MACHINE").getValue().toString().equals("CrystalEnterprise.Report")) {
											currentValues = (IProperties)prompt.getProperty("SI_CURRENT_VALUES").getValue();
										}
										else {
											currentValues = (IProperties)prompt.getProperty("SI_VALUES").getValue();
											theValue = (String)currentValues.getProperty(1).getValue();
											System.out.println("prompt value is :" + theValue);
										}

										System.out.println("prompt value is :" + currentValues);
										int numberValues;
										// Get the number of values for this particular parameter.
										//if (objRecurringObject.getKind().toString() == "CrystalReport") {
										if (objRecurringObject.properties().getProperty("SI_PROGID_MACHINE").getValue().toString().equals("CrystalEnterprise.Report")) {
											numberValues = ((Integer)currentValues.getProperty("SI_NUM_VALUES").getValue()).intValue();
										} else {
											numberValues = (Integer)currentValues.getProperty("SI_TOTAL").getValue();
										}

										System.out.println("Number of prompt values is " + numberValues);

										// Loop through all of the values for this particular parameter.
										for(int j=1;j<=numberValues;j++)
										{
											IProperties currentValue;
											IProperties si_values;
											String aValue;
											// Print out the values.
											// For Crystal Reports:
											// Each value has the name SI_VALUE + a digit starting from 1.  For example, if
											// there are two values, then the first value would be called SI_VALUE1 and the
											// second value would be called SI_VALUE2.

											//For Webi, the name is SI_VALUES and there is a child property that is an integer.
											//Also for Webi, there is no property called SI_PROMPT_TYPE.
											System.out.println("si_kind is:  " + objRecurringObject.getKind().toString());
											//A lot of Webi recurring schedules are run as Excel, so the SI_KIND is "Excel", but
											//when I try to gather the prompt values, it fails because the property bag
											//is actually for Webi, but it doesn't evaluate as Webi since the schedule kind
											// is Excel.  Arggggghhhhhh!!!

											if (objRecurringObject.properties().getProperty("SI_PROGID_MACHINE").getValue().toString().equals("CrystalEnterprise.Webi")) {
												//if (objRecurringObject.getKind().toString() == "Webi") { double equals sign didn't work
												//if	(objRecurringObject.getKind().toString().equals("Webi")) {
												//si_values = (IProperties)currentValues.getProperty("SI_VALUES").getValue();
												//aValue = (IProperty)si_values.getProperty(i).getValue();
												aValue = (String)currentValues.getProperty(j).getValue();
												System.out.println("Current param value for " + promptName + " is: " + aValue);
												paramValueString += aValue  + "; ";
												//System.out.println("Param value string so far is " + paramValueString);

											} else {

												currentValue = (IProperties)currentValues.getProperty("SI_VALUE"+j).getValue();

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
													//System.out.println("Param value string so far is " + paramValueString);
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
														//System.out.println("Param value string so far is " + paramValueString);

													}
													else //if (currentValue.size() == 4)
														if (promptType.equals("0"))
														{
															// Discrete Parameter: contains 4 properties: SI_SHOWDESCONLY, SI_OPTIONS,
															// SI_DESCRIPTION, SI_DATA 
															System.out.println(prompt.getProperty("SI_PROMPT_TYPE").getValue().toString());
															// For this situation we are only interested in the SI_DATA property.
															String paramValue = currentValue.getProperty("SI_DATA").getValue().toString();
															//out.println("<tr><td> Value " + j + " = " + paramValue + "</td></tr>");
															paramValueString += paramValue  + "; ";
															//System.out.println("Param value string so far is " + paramValueString);

														}

														else
														{
															System.out.println("The number of prompts is 0 for the InfoObject.");
														}
											}
										}
									}
								}
								catch (Exception ex) {
									System.out.println(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getStackTrace() + " getParamValuesString - line 427");
								}
								//param values can't exceed 3000 characters
								System.out.println("Param value string length is " + paramValueString.length());
								if (paramValueString.length() > 4000) {
									System.out.println("The param value string for " + objInfoObject.getID() + " exceeds 4000 char");
									//paramValueString = paramValueString.substring(0, 2995); //Either Oracle is miscounting, or there's a bug in Eclipse that comes back from Oracle saying the length exceeds 3000 even though it doesn't.
									//Therefore, trim it more
									//paramValueString = paramValueString.substring(0, 2900);
									//Due to some param strings exceeding 6600 characters, I increased the column to the Oracle limit:  4000.
									paramValueString = paramValueString.substring(0, 3900);
								}

								System.out.println("Param value string is " + paramValueString);
								System.out.println("Param value string length is " + paramValueString.length());
								//Helper.runMSSQLInsertQuery("insert into STG1_BO4_TRACKER_ERRORS (ERROR_MESSAGE, LOAD_DT) VALUES ('LoadFunctions() line 1203 " + EnterpriseEnv + "', GETDATE())");
							} 

						}
					}
					catch (Exception ex) {
						System.out.println(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getCause() + " " + ex.getStackTrace());
					}


					try {
						scheduleStatus = objRecurringObject.properties().getProperty("SI_SCHEDULE_STATUS").getValue().toString();
					} 
					catch (Exception ex){
						System.out.println(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getCause() + " " + ex.getStackTrace());
					}					


					try {
						//nextRunTime = (Calendar)objRecurringObject.properties().getProperty("SI_NEXTRUNTIME").getValue();
						nextRunTime.setTime((Date) objRecurringObject.properties().getProperty("SI_NEXTRUNTIME").getValue());
						//nextRunTime.setTime((Date) objRecurringObject.getSchedulingInfo().properties().getProperty("SI_NEXTRUNTIME").getValue());
					} 
					catch (Exception ex) {
						System.out.println(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getCause() + " " + ex.getStackTrace());
					}

					try {
						si_owner = (String)objRecurringObject.properties().getProperty("SI_OWNER").getValue();

					} catch (Exception ex) {

					}

					//					I used to set cookiecrumbs, but it is so time-consuming
					cookieCrumbs = "See report table for path";

					//VB:  destination = objRecurringObject.SendToDestination.Name and this works
					//String destinationFlag;
					IProperties pDestinations;

					try{
						//This should error out if "SI_DESTINATIONS" property bag doesn't exist
						pDestinations = (IProperties) objRecurringObject.getSchedulingInfo().properties().getProperty("SI_DESTINATIONS").getValue();
						Helper.runMSSQLInsertQuery("insert into STG1_BO4_TRACKER_ERRORS (ERROR_MESSAGE, LOAD_DT) VALUES ('LoadFunctions() line 1247 " + EnterpriseEnv + "', GETDATE())");
						String destinationCount = pDestinations.getProperty("SI_TOTAL").getValue().toString();
						intDestinationCount = Integer.parseInt(destinationCount);
						if (intDestinationCount > 0) {
							//Get the name of the destination
							IProperties PDestinations = (IProperties)objRecurringObject.getSchedulingInfo().properties().getProperty("SI_DESTINATIONS").getValue();
							Helper.runMSSQLInsertQuery("insert into STG1_BO4_TRACKER_ERRORS (ERROR_MESSAGE, LOAD_DT) VALUES ('LoadFunctions() line 1253 " + EnterpriseEnv + "', GETDATE())");
							//IProperty theDestination = (IProperty)PDestinations.getProperty("SI_PROGID").getValue();
							//This throws an error

							//IProperties theDestinations = (IProperties)PDestinations.getProperties("1").getProperty("SI_PROGID").getValue();
							theProgID = (String)PDestinations.getProperties("1").getProperty("SI_PROGID").getValue();
							//theProdID is really the destination.  I just named it that because it matches what's in the CMS.

							//Helper.runMSSQLInsertQuery("insert into STG1_BO4_TRACKER_ERRORS (ERROR_MESSAGE, LOAD_DT) VALUES ('LoadFunctions() line 1261 " + EnterpriseEnv + "', GETDATE())");

							//IProperty destProgId = theDestination.
							System.out.println("the destination property value so far is " + theProgID);

							// Dec 5, 2014: In BOBJ 4.1, if the destination is "Default" SI_DESTINATIONS property bag does not exist.
							//Dec 7, 2014, now that I can get the destination fairly directly, I think these next lines are unneccessary.
							/*ISendable obj = (ISendable)objRecurringObject;						
							IDestinationPlugin destinationPlugin = Helper.getDestinationPlugin(objInfoStore, objRecurringObject.getKind());
							IDestination odestination = obj.getSendToDestination();	
							odestination.setFromPlugin(destinationPlugin);
							System.out.println("Line 1231: odestination is " + odestination.getName());
							destination = odestination.getName();
							System.out.println("destination is " + destination + " for si_id " + objRecurringObject.getID());
							 */
							//distributionList = Helper.getDistributionList("uslexbcs02", objRecurringObject.getID());
							destination = theProgID;
							//Helper.runMSSQLInsertQuery("insert into STG1_BO4_TRACKER_ERRORS (ERROR_MESSAGE, LOAD_DT) VALUES ('LoadFunctions() line 1278 " + EnterpriseEnv + "', GETDATE())");
							if (theProgID.equals("CrystalEnterprise.Smtp")) {
								System.out.println("Line 1267");							
								//distributionList = Helper.getDistributionList(EnterpriseEnv, objRecurringObject.getID());
								distributionList = Helper.getDistributionList1(objEnterpriseSession, EnterpriseEnv, objRecurringObject.getID());
								System.out.println("Line 1270");
							}
							else
								System.out.println("Destination actually is:  " + destination);
						}
					}
					catch(Exception Ex){
						System.out.println("Line 1246 (SI_DESTINATIONS was probably null) " + Ex.getMessage() + " " + Ex.getStackTrace());
						//Helper.runMSSQLInsertQuery("insert into STG1_BO4_TRACKER_ERRORS (ERROR_MESSAGE, LOAD_DT) VALUES ('" + sdkEx.getMessage() + " " + sdkEx.getStackTrace() + " " + EnterpriseEnv + "', GETDATE())");
						Helper.runMSSQLInsertQuery("insert into STG1_BO4_TRACKER_ERRORS (ERROR_MESSAGE, LOAD_DT) VALUES ('Line 1246 (SI_DESTINATIONS was probably null) " + Ex.getMessage() + " " + Ex.getStackTrace() + " " + EnterpriseEnv + "', GETDATE())");

					}

					try {

						ifiles = (IFiles)objRecurringObject.properties().getProperty("SI_FILES").getValue();
						outputFile = (String)ifiles.properties().getProperty("SI_PATH").getValue();
					} 
					catch (Exception ex) {
					}

					////////////////////////////////////////////////////////////////////////////////////////////////
					/* These methods always throw errors in BO 4.1.  
					//Get the logon info
					try {
						System.out.println("si_kind is " +objRecurringObject.getKind());
						iReport = (IReport) objRecurringObject;
						ISDKList dbLogons = iReport.getReportLogons();

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
					} 
					catch (Exception ex) {	}

					server = server1;
					//Get the number of prompts
					try {
						iReport = (IReport) objRecurringObject;
						List paramList = iReport.getReportParameters();
						numPrompts = paramList.size();	
					} 
					catch (Exception ex) {	}
					 */
					////////////////////////////////////////////////////////////////////////////////////////////////////

					try {
						//Dec 7, 2014:  Since recurring jobs are instances and could be in different formats, this is the method used
						//to get logon info used in the runhist function.

						//System.out.println("si_kind is " + objInfoObject.getKind());
						//Error: Logons The -32769 plug-in does not exist in the CMS (FWM 02017) [Ljava.lang.StackTraceElement;@347448

						System.out.println("si_kind is " + objRecurringObject.getKind());
						if (objRecurringObject.getKind() == "Webi") {
							webiPrompts = (IPrompts)objRecurringObject.properties().getProperty("SI_WEBI_PROMPTS").getValue();
							numPrompts = webiPrompts.size();
							logon1 = "n/a";
							logon2 = "n/a";
							server1 = "n/a";
							server2 = "n/a";
							if (numPrompts > 0 ) {
								//Getting the param list might not be possible
								//List paramList = webiPrompts.getReportParameters();
								Object paramList =  webiPrompts.toArray();
							}

							try {
								// Get the SI_LOGON_INFO property bag
								//IProperties boProcessingInfo = (IProperties) objInfoObject.getProcessingInfo().properties();
								//This is null for recurring jobs I know this is populated
								Helper.runMSSQLInsertQuery("insert into STG1_BO4_TRACKER_ERRORS (ERROR_MESSAGE, LOAD_DT) VALUES ('LoadFunctions() line 1362 " + EnterpriseEnv + "', GETDATE())");
								IProperties boProcessingInfo = (IProperties) objRecurringObject.getProcessingInfo().properties();
								Helper.runMSSQLInsertQuery("insert into STG1_BO4_TRACKER_ERRORS (ERROR_MESSAGE, LOAD_DT) VALUES ('LoadFunctions() line 1264 " + EnterpriseEnv + "', GETDATE())");
								IProperties logonProperties = (IProperties) boProcessingInfo.getProperty("SI_LOGON_INFO").getValue();
								if (logonProperties != null) {
									IProperties silogonProperties = (IProperties) logonProperties.getProperty("SI_LOGON1").getValue();


									System.out.println("SI_LOGON1 size is " + silogonProperties.size());
									//16, it's true
									numLogins = logonProperties.getProperty("SI_NUM_LOGONS").toString();
									System.out.println("Num logons is " + numLogins);
									//System.out.println("logon1 is " + logon1);
									//Get the db logon credentials 
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
								System.out.println("Logon properties problem line 1339 " + ex.getMessage() + " " + ex.getStackTrace());
							}


						} else {
							//see if this will work for all non-Webi file types, .rpt, .xls and .pdf

							// First get the Processing Info properties for the InfoObject.
							IProperties boProcessingInfo = (IProperties) objRecurringObject.getProcessingInfo().properties();

							// Make sure that there is processing info set for the InfoObject.
							if (boProcessingInfo != null)
							{
								// Get the SI_PROMPTS property bag from the InfoObject's Processing Info
								IProperties siPrompts = (IProperties) boProcessingInfo.getProperty("SI_PROMPTS").getValue();

								// Make sure that there are parameters.
								if (siPrompts != null)
								{
									// Get the number of prompts (the number of parameters)
									numPrompts = ((Integer)siPrompts.getProperty("SI_NUM_PROMPTS").getValue()).intValue();
								}

								// Get the SI_LOGON_INFO property bag
								IProperties logonProperties = (IProperties) boProcessingInfo.getProperty("SI_LOGON_INFO").getValue();
								if (logonProperties != null) {
									IProperties silogonProperties = (IProperties) logonProperties.getProperty("SI_LOGON1").getValue();

									System.out.println("SI_LOGON1 size is " + silogonProperties.size());
									//16, it's true
									numLogins = logonProperties.getProperty("SI_NUM_LOGONS").toString();
									System.out.println("Num logons is " + numLogins);
									//System.out.println("logon1 is " + logon1);
									//Get the db logon credentials 
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
									} //end for loop

								}//end if
							}//end if

							server = server1;

							//////////////ONLY WORKS FOR CRYSTAL REPORTS FORMAT
							/*iReport = (IReport) objInfoObject;  //can't do this if the report is not in CR format

						//Get the username 
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

						List paramList = iReport.getReportParameters(); //getting an error here
						//Line 1607 The operation is not supported [Ljava.lang.StackTraceElement;@12b9f14
						//ORA-00923: FROM keyword not found where expected
						// [Ljava.lang.StackTraceElement;@1c67b54 runMSSQLInsertQuery line 632
						numPrompts = paramList.size();*/
							/////////////////////////////////////////////
						} //end else clause - handles non-Webi reports


						//The number of prompts is stored differently between Crystal Reports and Webi docs
						//Crystal: SI_PROMPTS
						//Webi:  SI_WEBI_PROMPTS
						//Xcelsius:  


					} catch (Exception ex) {
						System.out.println("Processing info and Logons " + ex.getMessage() + " " + ex.getStackTrace());
						//IF IS EXCEL OUTPUT
						try {
							IExcel eReport = (IExcel)objInfoObject;
							//Get the username 
							//									ISDKList dbLogons = eReport.getReportLogons();
							IProperties allProperty = (IProperties)eReport.properties();
							//IProperties pathProperty = allProperty.getProperties("SI_FILES");
							//IProperty logonProperty = eReport.getProcessingInfo().properties().getProperty("SI_LOGONS")
							IProperties logonProperties = allProperty.getProperties("SI_LOGON_INFO");
							IProperties logon1Properties = logonProperties.getProperties("SI_LOGON1");
							//IProperty userProperty = logon1Properties.getProperty("SI_USER");


							//Get the db logon credentials 
							for (int h = 0; h < logon1Properties.size(); h++) {	
								IReportLogon dbLogon = (IReportLogon)logon1Properties.get(h);
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
							//List paramList = eReport.getReportParameters(); //getting an error here
							//Line 1607 The operation is not supported [Ljava.lang.StackTraceElement;@12b9f14
							//ORA-00923: FROM keyword not found where expected
							// [Ljava.lang.StackTraceElement;@1c67b54 runMSSQLInsertQuery line 632

							IProperties paramProperties = allProperty.getProperties("SI_PROMPTS");
							numPrompts = ((Integer)paramProperties.getProperty("SI_NUM_PROMPTS").getValue()).intValue();
							System.out.println("nbr prompts is" + numPrompts);
							//numPrompts = paramList.size();
						}
						catch (Exception ex1){
							System.out.println("excel prompts " + ex.getMessage() + " " + ex.getStackTrace());
						}

						//IF IS PDF OUTPUT
						try {
							IPDF pReport = (IPDF)objInfoObject;
							//Get the username 
							//ISDKList dbLogons = eReport.getReportLogons();
							IProperties allProperty = (IProperties)pReport.properties();
							//IProperties pathProperty = allProperty.getProperties("SI_FILES");
							//IProperty logonProperty = eReport.getProcessingInfo().properties().getProperty("SI_LOGONS")
							IProperties logonProperties = allProperty.getProperties("SI_LOGON_INFO");
							IProperties logon1Properties = logonProperties.getProperties("SI_LOGON1"); //get error here for a PDF file
							//$Proxy0 cannot be cast to com.crystaldecisions.sdk.plugin.desktop.report.IReport [Ljava.lang.StackTraceElement;@92af24
							IProperty userProperty = logon1Properties.getProperty("SI_USER");


							//Get the db logon credentials 
							for (int h = 0; h < logon1Properties.size(); h++) {	
								IReportLogon dbLogon = (IReportLogon)logon1Properties.get(h);
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
							//List paramList = eReport.getReportParameters(); //getting an error here
							//Line 1607 The operation is not supported [Ljava.lang.StackTraceElement;@12b9f14
							//ORA-00923: FROM keyword not found where expected
							// [Ljava.lang.StackTraceElement;@1c67b54 runMSSQLInsertQuery line 632

							IProperties paramProperties = allProperty.getProperties("SI_PROMPTS");
							numPrompts = ((Integer)paramProperties.getProperty("SI_NUM_PROMPTS").getValue()).intValue();
							System.out.println("nbr prompts is" + numPrompts);
							//numPrompts = paramList.size();
						}
						catch (Exception ex1){
							System.out.println("PDF prompts " + ex.getMessage() + " " + ex.getStackTrace());
						}

						//IF IS TEXT OUTPUT
						try {
							ITxt tReport = (ITxt)objInfoObject;
							//Get the username 
							//ISDKList dbLogons = eReport.getReportLogons();
							IProperties allProperty = (IProperties)tReport.properties();
							//IProperties pathProperty = allProperty.getProperties("SI_FILES");
							//IProperty logonProperty = eReport.getProcessingInfo().properties().getProperty("SI_LOGONS")
							IProperties logonProperties = allProperty.getProperties("SI_LOGON_INFO");
							IProperties logon1Properties = logonProperties.getProperties("SI_LOGON1");
							IProperty userProperty = logon1Properties.getProperty("SI_USER");


							//Get the db logon credentials 
							for (int h = 0; h < logon1Properties.size(); h++) {	
								IReportLogon dbLogon = (IReportLogon)logon1Properties.get(h);
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
							//List paramList = eReport.getReportParameters(); //getting an error here
							//Line 1607 The operation is not supported [Ljava.lang.StackTraceElement;@12b9f14
							//ORA-00923: FROM keyword not found where expected
							// [Ljava.lang.StackTraceElement;@1c67b54 runMSSQLInsertQuery line 632

							IProperties paramProperties = allProperty.getProperties("SI_PROMPTS");
							numPrompts = ((Integer)paramProperties.getProperty("SI_NUM_PROMPTS").getValue()).intValue();
							System.out.println("nbr prompts is" + numPrompts);
							//numPrompts = paramList.size();
						}
						catch (Exception ex1){
							System.out.println("txt prompts " + ex.getMessage() + " " + ex.getStackTrace());
						}


						System.out.println("prompts " + ex.getMessage() + " " + ex.getStackTrace());
					}

					//////////// Some reports cause a NoClassDefFoundError during the
					//////////// next function, but I have all the required libraries.
					//////////// I don't know if this field has ever been needed in a report
					//////////// so I'm going to comment it out.
					siFormatExportAllpages = "unk";
					/*try {
						si_format_export_allpages = getFormatAllPagesSetting(objRecurringObject);
						if (si_format_export_allpages) {
							siFormatExportAllpages = "true";}
						else
							siFormatExportAllpages = "false";
					}
					catch (Exception ex) {
						System.out.println(ex.getMessage() + " " + ex.getStackTrace() + " line 1596");
						//siFormatExportAllpages = "non-existent";
						//Dec 9, 2014, I'm not sure this is running:
						Helper.runMSSQLInsertQuery("insert into STG1_BO4_TRACKER_ERRORS (ERROR_MESSAGE, LOAD_DT) VALUES ('si_format_export_allpages line 1611 " + ex.getMessage() + " " + ex.getStackTrace() + " " + EnterpriseEnv + "', GETDATE())");
					}*/

					//Start time
					try {
						System.out.println("Start date is " + objRecurringObject.getSchedulingInfo().getBeginDate());
						//runDate = (Calendar)objRecurringObject.properties().getProperty("SI_STARTTIME").getValue();
						//nextRunTime.setTime((Date) objInfoObject.properties().getProperty("SI_NEXTRUNTIME").getValue());
						//ERROR runDate.setTime((Date) objRecurringObject.properties().getProperty("SI_STARTTIME").getValue());
						//runDate = (Calendar) objRecurringObject.getSchedulingInfo().getBeginDate();
						runDate.setTime((Date) objRecurringObject.getSchedulingInfo().getBeginDate());
						System.out.println("SI_STARTTIME is " + runDate.getTime());
						startTime = runDate;
						System.out.println("start time is " + startTime.getTime());

						//End time

						//endTime = (Calendar)objRecurringObject.properties().getProperty("SI_ENDTIME").getValue();
						//endTime.setTime((Date) objRecurringObject.properties().getProperty("SI_ENDTIME").getValue());
						endTime.setTime((Date) objRecurringObject.getSchedulingInfo().getEndDate());
						System.out.println("end time is " + endTime.getTime());

						theInterval = 60/(endTime.getTimeInMillis() - startTime.getTimeInMillis())*1000;
					} catch (Exception ex) {
						System.out.println("There was a problem getting Start time and/or End time " + ex.getMessage());
					}

					try {
						machineUsed = (String)objRecurringObject.getSchedulingInfo().properties().getProperty("SI_MACHINE_USED").getValue();
					} catch (Exception ex) {

					}

					try {
						submitter = Helper.safeSQL((String)objRecurringObject.getSchedulingInfo().properties().getProperty("SI_SUBMITTER").getValue().toString());
						//submitter = objRecurringObject.getOwner().toString();
						owner = submitter;
						System.out.println("submitter is " + owner);
					} catch (Exception ex) {
						System.out.println("There was a problem getting the submitter " + ex.getMessage());
					}


					try {
						//creationTime = (Calendar)objRecurringObject.properties().getProperty("SI_CREATION_TIME").getValue();
						creationTime.setTime((Date) objRecurringObject.properties().getProperty("SI_CREATION_TIME").getValue());
						System.out.println(creationTime.getTime());
					} catch (Exception ex) {

					}


					try {
						parentCuid = objRecurringObject.getParentCUID();
					} catch (Exception ex) {

					}

					try {
						parentFolderCuid = (String) objRecurringObject.properties().getProperty("SI_PARENT_FOLDER_CUID").getValue();
					} catch (Exception ex) {

					}

					try {
						cuid = objRecurringObject.getCUID();
					} catch (Exception ex) {

					}

					try {
						//scheduleType = (int)objRecurringObject.getSchedulingInfo().properties().getProperty("SI_SCHEDULE_TYPE").getValue();
						scheduleType = objRecurringObject.getSchedulingInfo().getType();
					} catch (Exception ex) {

					}


					try {

					} catch (Exception ex) {
						//scheduleIntervalMinutes = (int)objRecurringObject.getSchedulingInfo().properties().getProperty("SI_SCHEDULE_INTERVAL_MINUTES").getValue();
						scheduleIntervalMinutes = objRecurringObject.getSchedulingInfo().getIntervalMinutes();
					}


					try {
						scheduleIntervalHours = objRecurringObject.getSchedulingInfo().getIntervalHours();
					} catch (Exception ex) {

					}

					try {
						scheduleIntervalDays = objRecurringObject.getSchedulingInfo().getIntervalDays();
					} catch (Exception ex) {

					}

					try {
						scheduleIntervalMonths = objRecurringObject.getSchedulingInfo().getIntervalMonths();
					} catch (Exception ex) {

					}

					server = server1;
					//msSQLInsertString = "insert into " + destTblNm + " (REPORT_ID, INSTANCE_ID, SI_STARTTIME, SI_ENDTIME, DURATION, SI_SUBMITTER, SI_SERVER, SI_OWNER, SI_SCHEDULE_STATUS,REPORT_PATH, SI_NAME, SI_CREATION_TIME, SI_NUM_PROMPTS, DATASRC_LOGIN_NM, SI_PASSWORD, DATASRC_LOGIN_NM2, SI_PASSWORD2, SI_ERROR_MESSAGE, SI_MACHINE_USED, SI_UPDATE_TS, DESTINATION, OUTPUT_FILE, REPORT_CUID, SI_PARENT_FOLDER_CUID, DISTRIBUTION_LIST, si_file1) values (" + objRecurringObject.getParentID() + ", " + objRecurringObject.getID() + ", to_date('" + objRecurringObject.properties().getProperty("SI_STARTTIME").getValue() + "','MM/DD/YYYY HH12:MI:SS AM')" + ", to_date('" + objRecurringObject.properties().getProperty("SI_ENDTIME").getValue() + "','MM/DD/YYYY HH12:MI:SS AM')" + ", " + theInterval + ", '" + Helper.safeSQL((String)objRecurringObject.getSchedulingInfo().properties().getProperty("SI_SUBMITTER").getValue()) + "', '" + Helper.safeSQL(server) + "', '" + Helper.safeSQL((String)objRecurringObject.getSchedulingInfo().properties().getProperty("SI_SUBMITTER").getValue()) + "', " + objRecurringObject.properties().getProperty("SI_SCHEDULE_STATUS").getValue() + ", '" + Helper.safeSQL(cookieCrumbs) + "', '" + Helper.safeSQL(objRecurringObject.getTitle()) + "', to_date('" + objRecurringObject.properties().getProperty("SI_CREATION_TIME").getValue() + "','MM/DD/YYYY HH12:MI:SS AM'), " + numPrompts + ",'" + logon1 + "','" + pswd1 + "', '" + logon2 + "', '" + pswd2 + "','" + errorMsg + "','" + objRecurringObject.getSchedulingInfo().properties().getProperty("SI_MACHINE_USED").getValue() + "', to_date('" + objRecurringObject.properties().getProperty("SI_UPDATE_TS").getValue() + "','MM/DD/YYYY HH12:MI:SS AM'), '" + destination + "','" + outputFile + "', '" + objRecurringObject.getParentCUID() + "', '" + objRecurringObject.properties().getProperty("SI_PARENT_FOLDER_CUID").getValue() + "','" + Helper.getDistributionList(EnterpriseEnv, objRecurringObject.getID()) + "', '" + si_file1 + "')";

					//THIS IS THE NEW QUERY. Convert to java syntax.
					//msSQLInsertString = "insert into " + destTblNm + " (REPORT_ID, INSTANCE_ID, SI_STARTTIME, SI_ENDTIME, DURATION, SI_SUBMITTER, SI_SERVER, SI_OWNER, SI_SCHEDULE_STATUS,REPORT_PATH, SI_NAME, SI_CREATION_TIME, SI_NUM_PROMPTS, DATASRC_LOGIN_NM, SI_PASSWORD, DATASRC_LOGIN_NM2, SI_PASSWORD2, SI_ERROR_MESSAGE, SI_MACHINE_USED, SI_UPDATE_TS, DESTINATION, OUTPUT_FILE, REPORT_CUID, SI_PARENT_FOLDER_CUID, DISTRIBUTION_LIST, si_file1, ENVIRONMENT, SI_KIND, SI_FORMAT_EXPORT_ALLPAGES, CUID, PARAM_VALUES) values (" + objRecurringObject2.ParentID + ", " + objRecurringObject2.ID + ", to_date('" + objRecurringObject2.Properties.Item("SI_STARTTIME").Value + "','MM/DD/YYYY HH12:MI:SS AM')" + ", to_date('" + objRecurringObject2.Properties.Item("SI_ENDTIME").Value + "','MM/DD/YYYY HH12:MI:SS AM')" + ", " + theInterval + ", '" + safeSQL(objRecurringObject2.SchedulingInfo.Properties.Item("SI_SUBMITTER").Value) + "', '" + safeSQL(server) + "', '" + safeSQL(objRecurringObject2.SchedulingInfo.Properties.Item("SI_SUBMITTER").Value) + "', " + objRecurringObject2.Properties.Item("SI_SCHEDULE_STATUS").Value + ", '" + safeSQL(cookieCrumbs) + "', '" + safeSQL(objRecurringObject2.Title) + "', to_date('" + objRecurringObject2.Properties.Item("SI_CREATION_TIME").Value + "','MM/DD/YYYY HH12:MI:SS AM'), " + numPrompts + ",'" + logon1 + "','" + pswd1 + "', '" + logon2 + "', '" + pswd2 + "','" + errorMsg + "','" + objRecurringObject2.SchedulingInfo.Properties.Item("SI_MACHINE_USED").Value + "', to_date('" + objRecurringObject2.Properties.Item("SI_UPDATE_TS").Value + "','MM/DD/YYYY HH12:MI:SS AM'), '" + destination + "','" + outputFile + "', '" + objRecurringObject2.ParentCUID + "', '" + objRecurringObject2.Properties.Item("SI_PARENT_FOLDER_CUID").Value + "','" + distributionList + "', '" + si_file1 + "','" + environ + "','" + objRecurringObject2.Kind + "','" + siFormatExportAllpages + "','" + objRecurringObject2.CUID + "','" + safeSQL(paramValues) + "')"

					//msSQLInsertString = "insert into " + destTblNm + " (REPORT_ID, INSTANCE_ID, SI_STARTTIME, SI_ENDTIME, SI_SUBMITTER, SI_SERVER, SI_OWNER, SI_SCHEDULE_STATUS, SI_SCHEDULE_TYPE, SI_SCHEDULE_INTERVAL_MINUTES, SI_SCHEDULE_INTERVAL_HOURS, SI_SCHEDULE_INTERVAL_MONTHS, SI_NAME, SI_CREATION_TIME, SI_DESTINATION, SI_PASSWORD, DATASRC_LOGIN_NM,  DATASRC_LOGIN_NM2, SI_PASSWORD2, SI_NUM_PROMPTS, REPORT_PATH, REPORT_CUID, SI_MACHINE_USED, SI_NEXTRUNTIME, PARAM_VALUES, DISTRIBUTION_LIST, CUID, SI_FORMAT_EXPORT_ALLPAGES, SI_KIND) values (" + objRecurringObject2.ParentID + ", " + objRecurringObject2.ID + ", to_date('" + objRecurringObject2.SchedulingInfo.Properties.Item("SI_STARTTIME").Value + "','MM/DD/YYYY HH12:MI:SS AM')" + ", to_date('" + objRecurringObject2.SchedulingInfo.Properties.Item("SI_ENDTIME").Value + "','MM/DD/YYYY HH12:MI:SS AM')" + ", '" + safeSQL(objRecurringObject2.SchedulingInfo.Properties.Item("SI_SUBMITTER").Value) + "', '" + server1 + "', '" + safeSQL(objRecurringObject2.Properties.Item("SI_OWNER").Value) + "', " + objRecurringObject2.Properties.Item("SI_SCHEDULE_STATUS").Value + ", " + objRecurringObject2.SchedulingInfo.Properties.Item("SI_SCHEDULE_TYPE").Value + "," + objRecurringObject2.SchedulingInfo.Properties.Item("SI_SCHEDULE_INTERVAL_MINUTES").Value + "," + objRecurringObject2.SchedulingInfo.Properties.Item("SI_SCHEDULE_INTERVAL_HOURS").Value + "," + objRecurringObject2.SchedulingInfo.Properties.Item("SI_SCHEDULE_INTERVAL_MONTHS").Value + ", '" + safeSQL(objRecurringObject2.Title) + "', to_date('" + objRecurringObject2.Properties.Item("SI_CREATION_TIME").Value + "','MM/DD/YYYY HH12:MI:SS AM'), '" + destination + "','" + pswd1 + "', '" + logon1 + "','" + logon2 + "', '" + pswd2 + "', " + nbrPrompts + ",'" + safeSQL(cookieCrumbs) + "','" + objRecurringObject2.ParentCUID + "'," + "'" + machine_used + "'," + " to_date('" + nextRunTime + "','MM/DD/YYYY HH12:MI:SS AM'), '" + paramValues + "', '" + distributionList + "','" + objRecurringObject2.CUID + "','" + siFormatExportAllpages + "','" + objRecurringObject2.Kind + "')"
					//					still need to get SI_SCHEDULE_TYPE, SI_SCHEDULE_INTERVAL_MINUTES, SI_SCHEDULE_INTERVAL_HOURS, SI_SCHEDULE_INTERVAL_MONTHS
					System.out.println("Param value string length is " + paramValueString.length());
					msSQLInsertString = "insert into " + destTblNm + " (REPORT_ID, INSTANCE_ID, SI_STARTTIME, SI_ENDTIME,  SI_SUBMITTER, SI_SERVER, SI_OWNER, SI_SCHEDULE_STATUS,REPORT_PATH, SI_NAME, SI_CREATION_TIME, SI_NUM_PROMPTS, DATASRC_LOGIN_NM, SI_PASSWORD, DATASRC_LOGIN_NM2, SI_PASSWORD2,  SI_MACHINE_USED,  SI_DESTINATION,  REPORT_CUID,  DISTRIBUTION_LIST,  SI_KIND, SI_FORMAT_EXPORT_ALLPAGES, CUID, PARAM_VALUES,SI_SCHEDULE_TYPE, SI_SCHEDULE_INTERVAL_MINUTES, SI_SCHEDULE_INTERVAL_HOURS, SI_SCHEDULE_INTERVAL_MONTHS, SI_SCHEDULE_INTERVAL_DAYS, SI_NEXTRUNTIME) values (" + objRecurringObject.getParentID() + ", ";
					msSQLInsertString += objRecurringObject.getID();
					msSQLInsertString += ", ('" + Helper.msSQLDateTime(startTime) + "')";
					msSQLInsertString += ", ('" + Helper.msSQLDateTime(endTime) + "')"  + ", '";
					msSQLInsertString += submitter;					
					msSQLInsertString += "', '" + Helper.safeSQL(server);
					msSQLInsertString += "', '" + owner;					
					msSQLInsertString += "', " + scheduleStatus;
					msSQLInsertString += ", '" + Helper.safeSQL(cookieCrumbs) + "', '" + Helper.safeSQL(objRecurringObject.getTitle());//threw an error
					msSQLInsertString += "', ('" + Helper.msSQLDateTime(creationTime) + "'), ";					
					msSQLInsertString += numPrompts + ",'" + logon1 + "','" + pswd1 + "', '" + logon2 + "', '" + pswd2  + "','" + machineUsed + "','";
					//msSQLInsertString += "', to_date('" + Helper.msSQLDateTime(updateTS) + "','MM/DD/YYYY HH12:MI:SS AM'), '";
					msSQLInsertString += destination +  "', '" ;
					msSQLInsertString += parentCuid;
					//the error is in this line:
					msSQLInsertString += "','" + distributionList + "','" + objRecurringObject.getKind() + "', '" + siFormatExportAllpages + "','";
					msSQLInsertString += cuid;
					msSQLInsertString += "','" + Helper.safeSQL(paramValueString) + "',";	
					System.out.println("String length of param Value String is " + paramValueString.length()); //still says 2995, but Eclipse says Oracle says 3072 or 3076; even if I check this string in Oracle directly, it says 2977!
					msSQLInsertString +=  scheduleType + ", " + scheduleIntervalMinutes + ", " + scheduleIntervalHours + ", " + scheduleIntervalMonths + ", " + scheduleIntervalDays + ", ('" + Helper.msSQLDateTime(nextRunTime) + "')) "; 
					System.out.println(msSQLInsertString);
					//si_submitter shouldn't be null and start time should have real values and not default to 1980
					try
					{
						Helper.runMSSQLInsertQuery(msSQLInsertString);
						destination = "";
					}
					catch (Exception ex)
					{
						System.out.println("Problem with insert query");
						Helper.runMSSQLInsertQuery("insert into STG1_BO4_TRACKER_ERRORS (ERROR_MESSAGE, LOAD_DT) VALUES ('" + ex.getMessage() + " " + ex.getStackTrace() + " " + EnterpriseEnv + "', GETDATE())");
						System.out.println(ex.getMessage() + " " + ex.getStackTrace() + " line 1688");
					}
				}
			}
			// Clean up the Enterprise Session.
			if(objEnterpriseSession != null) {
				try {
					objEnterpriseSession.logoff();

				} catch(Exception e_ignore_in_cleanup) {}
			}
		}
		catch (Exception ex)
		{
			System.out.println(ex.getMessage() + " " + ex.getStackTrace() + " line 1770");
			Helper.runMSSQLInsertQuery("insert into STG1_BO4_TRACKER_ERRORS ' line 1770" + ex.getMessage() + " " + ex.getStackTrace() + " " + EnterpriseEnv + "', GETDATE())");
			//'sendmail("Logon error", ex.getMessage() + " " + ex.getStackTrace)
			//Helper.sendmail("bishopp@lexmark.com", "bishopp@lexmark.com, amarabes@lexmark.com,ajpatel@lexmark.com,msankar@lexmark.com,ajain@lexmark.com", "Logon error - load_stg1_migr_XI_reports(String, String, String, String, String) - " + EnterpriseEnv, ex.getMessage() + " " + ex.getStackTrace());
			//Helper.sendmail("bishopp@lexmark.com", "bishopp@lexmark.com", "Logon error - load_stg1_boxi_runhist(String, String, String, String, String) - " + EnterpriseEnv, ex.getMessage() + " " + ex.getStackTrace());

		}


	}

	public static void load_stg1_boxi_recurringjobsByID(String EnterpriseEnv, String login, String pswd,String auth, String destTblNm, String environ, int recurringID){
		//Precondition:  today's recurring jobs snapshot since the last load are not in the database table.
		//Postcondition:  by reading from a database table with the recurring id's, loading one-by-one, connecting and disconnecting per recurring id, today's recurring jobs snapshot since the last load are in the database table.

		IEnterpriseSession objEnterpriseSession;
		//IInfoStore objInfoStore;
		IInfoObjects colInfoObjects;
		IInfoObjects colRecurObjects;
		IInfoObject objInfoObject;
		IInfoObject objInstObject;
		IInfoObject objRecurringObject;
		String errorMsg = "";
		String cookieCrumbs;
		int count;
		String si_owner;
		Calendar runDate = Calendar.getInstance();
		Calendar startTime = Calendar.getInstance();
		startTime.set(1980,1,1,0,0,0);
		Calendar endTime = Calendar.getInstance();
		endTime.set(1980,1,1,0,0,0);
		long theInterval = 0;
		Calendar startDate = Calendar.getInstance();;
		startDate.set(1980,1,1,0,0,0);
		Calendar endDate = Calendar.getInstance();;
		endDate.set(1980,1,1,0,0,0);
		String dtToday;
		String msSQLInsertString;
		String maxID ="0";
		String logon1 = "unknown";
		String logon2 = "unknown";
		String pswd1 = "unknown";
		String pswd2 = "unknown";
		String datasrc1 = "not found in CMS";
		String datasrc2 = "not found in CMS";
		int numPrompts = 0;
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
		String scheduleStatus = "9";
		IProcessingInfo processingInfo;
		IReport iReport;
		String server;
		String server1 = "";
		String server2 = "";
		IProperty aggCount;
		String strCount="0";
		int colCount = 0;
		Boolean si_format_export_allpages;
		String siFormatExportAllpages  = "non-existent";
		String paramValues;
		String recurringIDList ="0";
		String machine_used;
		//IPrompts siPrompts;
		int nbrPrompts =0;
		String promptName;
		String paramValueString = "";

		Calendar nextRunTime = Calendar.getInstance();
		nextRunTime.set(1980,1,1,0,0,0);
		String machineUsed = "";
		String submitter = "";
		String owner = "";
		Calendar creationTime = Calendar.getInstance();
		creationTime.set(1980,1,1,0,0,0);

		Calendar updateTS = Calendar.getInstance();
		updateTS.set(1980,1,1,0,0,0);

		String parentCuid = "unknown";
		String parentFolderCuid = "";
		String cuid = "";
		int scheduleType = 0;
		int scheduleIntervalMinutes = 0;
		int scheduleIntervalDays = 0;
		int scheduleIntervalHours = 0;
		int scheduleIntervalMonths = 0;



		try {
			ISessionMgr  objSessionMgr = CrystalEnterprise.getSessionMgr();
			objEnterpriseSession = objSessionMgr.logon(login, pswd, EnterpriseEnv, auth);    
			IInfoStore objInfoStore = (IInfoStore) objEnterpriseSession.getService("InfoStore");


			/*//Get the list of si_id's that were missed

			String driver = "oracle.jdbc.driver.OracleDriver";
			String url = "jdbc:oracle:thin:@dlxkrbodb1.lex.lexmark.com:1750:edr1den1";
			String username = "botrack";
			String password = "********";

			//String oraSelectString ="select i.RECURRING_ID from temp_recurring_ids i, (select instance_id from stg1_bo4p_recurringjobs where trunc(load_dt) >= trunc(sysdate)) rc where i.RECURRING_ID =  rc.INSTANCE_ID (+) and rc.instance_id is null";
			String oraSelectString ="select RECURRING_ID from temp_recurring_ids where RECURRING_ID = " + recurringID;
			try
			{
				System.out.println(oraSelectString);
				Class.forName(driver);		//loads the driver
				Connection conn = DriverManager.getConnection(url,username,password);	

				Statement stmt = conn.createStatement() ; 
				ResultSet rs = stmt.executeQuery(oraSelectString);
				while (rs.next()) 
				{
					recurringIDList += ", " + rs.getString("RECURRING_ID"); 
					//maxID = rs.getString("maxID");
				}
				recurringIDList = recurringIDList.substring(1, recurringIDList.length());
				System.out.println(recurringIDList);  //make sure the string looks right
				conn = null;
				conn.close();
			}	
			catch (Exception ex)
			{
				ex.printStackTrace();
				System.out.println(ex.getMessage() + " " + ex.getStackTrace() + " line 813");
			}
			 */
			System.out.println("recurring_id: " + recurringID);
			//strInfoSQL = "SELECT count(SI_ID) from CI_INFOOBJECTS WHERE SI_INSTANCE = 1 and SI_RECURRING=1 and si_id in (" + recurringID + ")";

			//System.out.println(strInfoSQL);

			//colInfoObjects = (IInfoObjects)objInfoStore.query(strInfoSQL);
			//objInfoObject = (IInfoObject)colInfoObjects.get(i); 

			//objInfoObject = (IInfoObject)colInfoObjects.get(0);

			//The VB code used the collection here, but in Java, properties() isn't an available function, so I am trying to get this via InfoObject

			//System.out.println(objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").properties().getProperty("SI_ID"));
			//System.out.println(objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").getID());//shows the ID, not the count!
			//count = objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").properties().getProperty("SI_ID");
			//count = objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").getID();
			//aggCount = objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT");

			//count = objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").getID();
			//strCount = ((IProperties)objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").getValue()).getProperty("SI_ID").getValue().toString();

			//System.out.println("Record count is " + strCount);



			//strInfoSQL = "SELECT TOP " + strCount + " SI_ID FROM CI_INFOOBJECTS WHERE SI_KIND in ('CrystalReport') and SI_INSTANCE = 0 and SI_CREATION_TIME >= '" + Helper.formatDateBO(maxCreationDate) + "'";
			//PRODUCTION SQL:
			strInfoSQL = "SELECT * FROM CI_INFOOBJECTS WHERE  SI_INSTANCE = 1 and si_recurring=1 and si_id in (" + recurringID + ") order by SI_ID";
			//JUST A TEST TO SEE HOW FAILED INSTANCES WORK IN THE CODE
			//strInfoSQL = "SELECT TOP " + strCount + " SI_ID FROM CI_INFOOBJECTS WHERE  SI_INSTANCE = 1 and si_recurring=0 and si_schedule_status=1 and SI_CREATION_TIME >= '" + Helper.formatDateBO(maxCreationDate) + "'";

			System.out.println(strInfoSQL);
			colRecurObjects = (IInfoObjects)objInfoStore.query(strInfoSQL);
			System.out.println("Line 1950 Count of objects in collection: " + colRecurObjects.size());

			//'*************INSERT THE SI_ID'S INTO A TEMP TABLE TO COMPARE WITH WHAT YOU GOT LOADED IN THE RECURRINGJOBS TABLE********
			//Helper.runMSSQLInsertQuery("truncate table temp_recurring_ids");

			//DON'T DO THIS ANYMORE/////////////
			//for (int y = 0; y < colRecurObjects.size(); y++){
			//	objRecurringObject = (IInfoObject)colRecurObjects.get(y);
			//	Helper.runMSSQLInsertQuery("insert into temp_recurring_ids (recurring_id) values (" + objRecurringObject.getID() + ")");
			//}
			//'****************************************************************************************************

			if (colRecurObjects.size() > 0){
				//Connection conn=null;
				//Class.forName("oracle.jdbc.driver.OracleDriver");
				//conn=DriverManager.getConnection("jdbc:oracle:thin:@dlxkrbodb1.lex.lexmark.com:1750:edr1den1","botrack","********");
				//Statement s=conn.createStatement();
				//'This records all the instances found on the BOXI system
				colCount = colRecurObjects.size();
				for (int x=0; x <= colCount; x++){
					//msSQLInsertString = "insert into temp_p_allinstances(report_id, instance_id) values (" + objInfoObject.getParentID() + "," + objInfoObject.getID() + ")";
					//System.out.println(msSQLInsertString);
					//s.execute(msSQLInsertString);
					objRecurringObject = (IInfoObject)colRecurObjects.get(x);
					System.out.println ("si_id is " + objRecurringObject.getID());
					System.out.println("report title is " + objRecurringObject.getTitle());
					System.out.println("schedule status is " + objRecurringObject.properties().getProperty("SI_SCHEDULE_STATUS").getValue());

					// 'Reset these for each recurring job
					si_owner = "";
					logon1 = "";
					pswd1 = "";
					logon2 = "";
					pswd2 = "";
					server1 = "";
					machine_used = "unknown";
					nextRunTime.set(1980,1,1,0,0,0);
					paramValues = "";
					paramValueString = "";

					//Run this only if the report has parameters
					try {


						// Get the SI_PROMPTS property bag from the InfoObject's Processing Info
						IProperties siProperties = (IProperties) objRecurringObject.getProcessingInfo().properties().getProperty("SI_PROMPTS").getValue();

						//IProperties oProcessInfo = (IProperties) objInfoObject.getProcessingInfo().properties();
						//IProperties siPrompts = (IProperties) oProcessInfo.getProperty("SI_PROMPTS").getValue();
						// Make sure that there are parameters.
						if (siProperties != null)
						{
							// Get the number of prompts (the number of parameters)
							numPrompts = ((Integer)siProperties.getProperty("SI_NUM_PROMPTS").getValue()).intValue();
						}



						//siPrompts = (IPrompts)objRecurringObject.getProcessingInfo().properties().getProperty("SI_PROMPTS").getValue();
						//now this error:
						//com.crystaldecisions.sdk.properties.internal.SDKPropertyBag cannot be cast to com.crystaldecisions.sdk.prompting.IPrompts com.crystaldecisions.sdk.properties.internal.SDKPropertyBag cannot be cast to com.crystaldecisions.sdk.prompting.IPrompts null [Ljava.lang.StackTraceElement;@7c218e

						//nbrPrompts = siPrompts.size();
						//if (nbrPrompts >0) {
						//if (numPrompts > 0) {
						if (numPrompts > 0) {
							//paramValues = Helper.getParamValueString2(objRecurringObject.getID(), login, pswd, EnterpriseEnv, auth);
							//This one is throwing that awful ContentProcesingHandler that happens when I make a really stupid mistake.

							//paramValues = Helper.getParamValueString(objRecurringObject); //this throws an error:						
							//com.crystaldecisions.sdk.plugin.desktop.excel.internal.a cannot be cast to com.crystaldecisions.sdk.plugin.desktop.report.IReport
							//or the function returns Null on everything even though the recurring job has parameters


							try{
								// Get the SI_PROMPTS property bag from the InfoObject's Processing Info - got this from SAP
								//IProperties siProperties = (IProperties) objInfoObject.getProcessingInfo().properties().getProperty("SI_PROMPTS").getValue();
								//Error:  properties() and getPRoperty() undefined for IProcessingInfo

								// Loop through the parameters to get the parameter names and parameter values.
								for(i=1;i<=numPrompts;i++)
								{
									IProperties prompt;
									// Each prompt has the name SI_PROMPT + a digit starting from 1.  For example, if
									// there are 2 prompts, the first one would be called SI_PROMPT1 and the second
									// one would be called SI_PROMPT2.
									//IProperties prompt = (IProperties)siPrompts.getProperty("SI_PROMPT"+i).getValue();
									if (objRecurringObject.getKind().toString() == "CrystalReport") {
										prompt = (IProperties)siProperties.getProperty("SI_PROMPT"+i).getValue();
									}
									else {
										prompt = (IProperties)siProperties.getProperty(i).getValue();
									}

									// Get the parameter name.
									promptName = prompt.getProperty("SI_NAME").getValue().toString();
									System.out.println("Prompt name is "  + promptName);

									// First column in the row is the parameter name.
									//out.println("<td>Parameter Name = "+promptName+"</td><td><table>");
									// Get the current values property bag.
									IProperties currentValues;
									String theValue;
									if (objRecurringObject.getKind().toString() == "CrystalReport") {
										currentValues = (IProperties)prompt.getProperty("SI_CURRENT_VALUES").getValue();
									}
									else {
										currentValues = (IProperties)prompt.getProperty("SI_VALUES").getValue();
										theValue = (String)currentValues.getProperty(1).getValue();
										System.out.println("prompt value is :" + theValue);
									}

									System.out.println("prompt value is :" + currentValues);
									int numberValues;
									// Get the number of values for this particular parameter.
									if (objRecurringObject.getKind().toString() == "CrystalReport") {
										numberValues = ((Integer)currentValues.getProperty("SI_NUM_VALUES").getValue()).intValue();
									} else {
										numberValues = (Integer)currentValues.getProperty("SI_TOTAL").getValue();
									}

									System.out.println("Number of prompt values is " + numberValues);

									// Loop through all of the values for this particular parameter.
									for(int j=1;j<=numberValues;j++)
									{
										IProperties currentValue;
										IProperties si_values;
										String aValue;
										// Print out the values.
										// For Crystal Reports:
										// Each value has the name SI_VALUE + a digit starting from 1.  For example, if
										// there are two values, then the first value would be called SI_VALUE1 and the
										// second value would be called SI_VALUE2.

										//For Webi, the name is SI_VALUES and there is a child property that is an integer.
										//Also for Webi, there is no property called SI_PROMPT_TYPE.
										System.out.println("si_kind is:  " + objRecurringObject.getKind().toString());
										//if (objRecurringObject.getKind().toString() == "Webi") {
										if	(objRecurringObject.getKind().toString().equals("Webi")) {
											//si_values = (IProperties)currentValues.getProperty("SI_VALUES").getValue();
											//aValue = (IProperty)si_values.getProperty(i).getValue();
											aValue = (String)currentValues.getProperty(j).getValue();
											System.out.println("Current param value for " + promptName + " is: " + aValue);
											paramValueString += aValue  + "; ";
											//System.out.println("Param value string so far is " + paramValueString);

										} else {



											currentValue = (IProperties)currentValues.getProperty("SI_VALUE"+j).getValue();

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
												//System.out.println("Param value string so far is " + paramValueString);
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
													//System.out.println("Param value string so far is " + paramValueString);

												}
												else //if (currentValue.size() == 4)
													if (promptType.equals("0"))
													{
														// Discrete Parameter: contains 4 properties: SI_SHOWDESCONLY, SI_OPTIONS,
														// SI_DESCRIPTION, SI_DATA 
														System.out.println(prompt.getProperty("SI_PROMPT_TYPE").getValue().toString());
														// For this situation we are only interested in the SI_DATA property.
														String paramValue = currentValue.getProperty("SI_DATA").getValue().toString();
														//out.println("<tr><td> Value " + j + " = " + paramValue + "</td></tr>");
														paramValueString += paramValue  + "; ";
														//System.out.println("Param value string so far is " + paramValueString);

													}

													else
													{
														System.out.println("The number of prompts is 0 for the InfoObject.");
													}
										}
									}
								}
							}
							catch (Exception ex) {
								System.out.println(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getStackTrace() + " getParamValuesString - line 427");
							}
							//param values can't exceed 3000 characters
							System.out.println("Param value string length is " + paramValueString.length());
							if (paramValueString.length() > 4000) {
								System.out.println("The param value string for " + recurringID + " exceeds 4000 char");
								//paramValueString = paramValueString.substring(0, 2995); //Either Oracle is miscounting, or there's a bug in Eclipse that comes back from Oracle saying the length exceeds 3000 even though it doesn't.
								//Therefore, trim it more
								//paramValueString = paramValueString.substring(0, 2900);
								//Due to some param strings exceeding 6600 characters, I increased the column to the Oracle limit:  4000.
								paramValueString = paramValueString.substring(0, 3900);
							}

							System.out.println("Param value string is " + paramValueString);
							System.out.println("Param value string length is " + paramValueString.length());
						} 
						//} 

					} 
					catch (Exception ex) {
						System.out.println(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getCause() + " " + ex.getStackTrace());
					}


					try {
						scheduleStatus = objRecurringObject.properties().getProperty("SI_SCHEDULE_STATUS").getValue().toString();
					} 
					catch (Exception ex){
						System.out.println(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getCause() + " " + ex.getStackTrace());
					}					


					try {
						//nextRunTime = (Calendar)objRecurringObject.properties().getProperty("SI_NEXTRUNTIME").getValue();
						//nextRunTime.setTime((Date) objRecurringObject.properties().getProperty("SI_NEXTRUNTIME").getValue());
						nextRunTime.setTime((Date) objRecurringObject.getSchedulingInfo().properties().getProperty("SI_NEXTRUNTIME").getValue());
					} 
					catch (Exception ex) {
						System.out.println(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getCause() + " " + ex.getStackTrace());
					}

					try {
						si_owner = (String)objRecurringObject.properties().getProperty("SI_OWNER").getValue();

					} catch (Exception ex) {

					}

					//					I used to set cookiecrumbs, but it is so time-consuming
					cookieCrumbs = "See report table for path";

					try {
						//destination = objRecurringObject.getSchedulingInfo().getDestination().getName();  //--> destination was null, but it can't be!
						//destination = (String)objRecurringObject.getSchedulingInfo().properties().getProperty("SI_DESTINATION").getValue();

						//VB:  destination = objRecurringObject.SendToDestination.Name and this works
						ISendable obj = (ISendable)objRecurringObject;						
						IDestinationPlugin destinationPlugin = Helper.getDestinationPlugin(objInfoStore, objRecurringObject.getKind());
						IDestination odestination = obj.getSendToDestination();	
						odestination.setFromPlugin(destinationPlugin);
						System.out.println("Line 2085: odestination is " + odestination.getName());
						destination = odestination.getName();
						System.out.println("destination is " + destination + " for si_id " + objRecurringObject.getID());
						//distributionList = Helper.getDistributionList("uslexbcs02", objRecurringObject.getID());	
						if (destination.equals("CrystalEnterprise.Smtp")) {
							System.out.println("Line 436");
							System.out.println("getting email distribution list");
							distributionList = Helper.getDistributionList(EnterpriseEnv, objRecurringObject.getID());	//Throws error:
							//com.crystaldecisions.sdk.occa.infostore.internal.SchedulableCategoryContentInfoObject cannot be cast to com.crystaldecisions.sdk.plugin.desktop.common.IReportProcessingInfo [Ljava.lang.StackTraceElement;@11775bc

							System.out.println("Line 2258");
						}
						else
							System.out.println("Destination actually is:  " + destination);
					}
					catch(SDKException sdkEx)
					{
						System.out.println("Line 2268 " + sdkEx.getDetail() + " " + sdkEx.getStackTrace());
						Helper.runMSSQLInsertQuery("insert into STG1_BO4_TRACKER_ERRORS (ERROR_MESSAGE, LOAD_DT) VALUES('" + sdkEx.getMessage() + " " + sdkEx.getStackTrace() + " " + EnterpriseEnv + "', GETDATE())");
					}

					try {

						ifiles = (IFiles)objRecurringObject.properties().getProperty("SI_FILES").getValue();
						outputFile = (String)ifiles.properties().getProperty("SI_PATH").getValue();
					} 
					catch (Exception ex) {
					}


					//Get the logon info
					try {
						iReport = (IReport) objRecurringObject;
						ISDKList dbLogons = iReport.getReportLogons();

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
					} 
					catch (Exception ex) {	}

					server = server1;
					//Get the number of prompts
					try {
						iReport = (IReport) objRecurringObject;
						List paramList = iReport.getReportParameters();
						numPrompts = paramList.size();	
					} 
					catch (Exception ex) {	}


					try {
						si_format_export_allpages = getFormatAllPagesSetting(objRecurringObject);
						if (si_format_export_allpages) {
							siFormatExportAllpages = "true";}
						else
							siFormatExportAllpages = "false";
					}
					catch (Exception ex) {
						//System.out.println(ex.getMessage() + " " + ex.getStackTrace() + " line 2187");
						//siFormatExportAllpages = "non-existent";
					}

					//Start time
					try {
						System.out.println("Start date is " + objRecurringObject.getSchedulingInfo().getBeginDate());
						//runDate = (Calendar)objRecurringObject.properties().getProperty("SI_STARTTIME").getValue();
						//nextRunTime.setTime((Date) objInfoObject.properties().getProperty("SI_NEXTRUNTIME").getValue());
						//ERROR runDate.setTime((Date) objRecurringObject.properties().getProperty("SI_STARTTIME").getValue());
						//runDate = (Calendar) objRecurringObject.getSchedulingInfo().getBeginDate();
						runDate.setTime((Date) objRecurringObject.getSchedulingInfo().getBeginDate());
						System.out.println("SI_STARTTIME is " + runDate);
						startTime = runDate;
						System.out.println("start time is " + startTime);

						//End time

						//endTime = (Calendar)objRecurringObject.properties().getProperty("SI_ENDTIME").getValue();
						//endTime.setTime((Date) objRecurringObject.properties().getProperty("SI_ENDTIME").getValue());
						endTime.setTime((Date) objRecurringObject.getSchedulingInfo().getEndDate());
						System.out.println("end time is " + endTime);

						theInterval = 60/(endTime.getTimeInMillis() - startTime.getTimeInMillis())*1000;
					} catch (Exception ex) {
						System.out.println("There was a problem getting Start time and/or End time " + ex.getMessage());
					}

					try {
						machineUsed = (String)objRecurringObject.getSchedulingInfo().properties().getProperty("SI_MACHINE_USED").getValue();
					} catch (Exception ex) {

					}

					try {
						submitter = Helper.safeSQL((String)objRecurringObject.getSchedulingInfo().properties().getProperty("SI_SUBMITTER").getValue());
						owner = submitter;
						System.out.println("submitter is " + owner);
					} catch (Exception ex) {
						System.out.println("There was a problem getting the submitter " + ex.getMessage());
					}


					try {
						//creationTime = (Calendar)objRecurringObject.properties().getProperty("SI_CREATION_TIME").getValue();
						creationTime.setTime((Date) objRecurringObject.properties().getProperty("SI_CREATION_TIME").getValue());
						System.out.println(creationTime.getTime());
					} catch (Exception ex) {

					}

					try {
						parentCuid = objRecurringObject.getParentCUID();
					} catch (Exception ex) {

					}

					try {
						parentFolderCuid = (String) objRecurringObject.properties().getProperty("SI_PARENT_FOLDER_CUID").getValue();
					} catch (Exception ex) {

					}

					try {
						cuid = objRecurringObject.getCUID();
					} catch (Exception ex) {

					}

					try {
						//scheduleType = (int)objRecurringObject.getSchedulingInfo().properties().getProperty("SI_SCHEDULE_TYPE").getValue();
						scheduleType = objRecurringObject.getSchedulingInfo().getType();
					} catch (Exception ex) {

					}


					try {

					} catch (Exception ex) {
						//scheduleIntervalMinutes = (int)objRecurringObject.getSchedulingInfo().properties().getProperty("SI_SCHEDULE_INTERVAL_MINUTES").getValue();
						scheduleIntervalMinutes = objRecurringObject.getSchedulingInfo().getIntervalMinutes();
					}


					try {
						scheduleIntervalHours = objRecurringObject.getSchedulingInfo().getIntervalHours();
					} catch (Exception ex) {

					}

					try {
						scheduleIntervalDays = objRecurringObject.getSchedulingInfo().getIntervalDays();
					} catch (Exception ex) {

					}

					try {
						scheduleIntervalMonths = objRecurringObject.getSchedulingInfo().getIntervalMonths();
					} catch (Exception ex) {

					}


					//msSQLInsertString = "insert into " + destTblNm + " (REPORT_ID, INSTANCE_ID, SI_STARTTIME, SI_ENDTIME, DURATION, SI_SUBMITTER, SI_SERVER, SI_OWNER, SI_SCHEDULE_STATUS,REPORT_PATH, SI_NAME, SI_CREATION_TIME, SI_NUM_PROMPTS, DATASRC_LOGIN_NM, SI_PASSWORD, DATASRC_LOGIN_NM2, SI_PASSWORD2, SI_ERROR_MESSAGE, SI_MACHINE_USED, SI_UPDATE_TS, DESTINATION, OUTPUT_FILE, REPORT_CUID, SI_PARENT_FOLDER_CUID, DISTRIBUTION_LIST, si_file1) values (" + objRecurringObject.getParentID() + ", " + objRecurringObject.getID() + ", to_date('" + objRecurringObject.properties().getProperty("SI_STARTTIME").getValue() + "','MM/DD/YYYY HH12:MI:SS AM')" + ", to_date('" + objRecurringObject.properties().getProperty("SI_ENDTIME").getValue() + "','MM/DD/YYYY HH12:MI:SS AM')" + ", " + theInterval + ", '" + Helper.safeSQL((String)objRecurringObject.getSchedulingInfo().properties().getProperty("SI_SUBMITTER").getValue()) + "', '" + Helper.safeSQL(server) + "', '" + Helper.safeSQL((String)objRecurringObject.getSchedulingInfo().properties().getProperty("SI_SUBMITTER").getValue()) + "', " + objRecurringObject.properties().getProperty("SI_SCHEDULE_STATUS").getValue() + ", '" + Helper.safeSQL(cookieCrumbs) + "', '" + Helper.safeSQL(objRecurringObject.getTitle()) + "', to_date('" + objRecurringObject.properties().getProperty("SI_CREATION_TIME").getValue() + "','MM/DD/YYYY HH12:MI:SS AM'), " + numPrompts + ",'" + logon1 + "','" + pswd1 + "', '" + logon2 + "', '" + pswd2 + "','" + errorMsg + "','" + objRecurringObject.getSchedulingInfo().properties().getProperty("SI_MACHINE_USED").getValue() + "', to_date('" + objRecurringObject.properties().getProperty("SI_UPDATE_TS").getValue() + "','MM/DD/YYYY HH12:MI:SS AM'), '" + destination + "','" + outputFile + "', '" + objRecurringObject.getParentCUID() + "', '" + objRecurringObject.properties().getProperty("SI_PARENT_FOLDER_CUID").getValue() + "','" + Helper.getDistributionList(EnterpriseEnv, objRecurringObject.getID()) + "', '" + si_file1 + "')";

					//THIS IS THE NEW QUERY. Convert to java syntax.
					//msSQLInsertString = "insert into " + destTblNm + " (REPORT_ID, INSTANCE_ID, SI_STARTTIME, SI_ENDTIME, DURATION, SI_SUBMITTER, SI_SERVER, SI_OWNER, SI_SCHEDULE_STATUS,REPORT_PATH, SI_NAME, SI_CREATION_TIME, SI_NUM_PROMPTS, DATASRC_LOGIN_NM, SI_PASSWORD, DATASRC_LOGIN_NM2, SI_PASSWORD2, SI_ERROR_MESSAGE, SI_MACHINE_USED, SI_UPDATE_TS, DESTINATION, OUTPUT_FILE, REPORT_CUID, SI_PARENT_FOLDER_CUID, DISTRIBUTION_LIST, si_file1, ENVIRONMENT, SI_KIND, SI_FORMAT_EXPORT_ALLPAGES, CUID, PARAM_VALUES) values (" + objRecurringObject2.ParentID + ", " + objRecurringObject2.ID + ", to_date('" + objRecurringObject2.Properties.Item("SI_STARTTIME").Value + "','MM/DD/YYYY HH12:MI:SS AM')" + ", to_date('" + objRecurringObject2.Properties.Item("SI_ENDTIME").Value + "','MM/DD/YYYY HH12:MI:SS AM')" + ", " + theInterval + ", '" + safeSQL(objRecurringObject2.SchedulingInfo.Properties.Item("SI_SUBMITTER").Value) + "', '" + safeSQL(server) + "', '" + safeSQL(objRecurringObject2.SchedulingInfo.Properties.Item("SI_SUBMITTER").Value) + "', " + objRecurringObject2.Properties.Item("SI_SCHEDULE_STATUS").Value + ", '" + safeSQL(cookieCrumbs) + "', '" + safeSQL(objRecurringObject2.Title) + "', to_date('" + objRecurringObject2.Properties.Item("SI_CREATION_TIME").Value + "','MM/DD/YYYY HH12:MI:SS AM'), " + numPrompts + ",'" + logon1 + "','" + pswd1 + "', '" + logon2 + "', '" + pswd2 + "','" + errorMsg + "','" + objRecurringObject2.SchedulingInfo.Properties.Item("SI_MACHINE_USED").Value + "', to_date('" + objRecurringObject2.Properties.Item("SI_UPDATE_TS").Value + "','MM/DD/YYYY HH12:MI:SS AM'), '" + destination + "','" + outputFile + "', '" + objRecurringObject2.ParentCUID + "', '" + objRecurringObject2.Properties.Item("SI_PARENT_FOLDER_CUID").Value + "','" + distributionList + "', '" + si_file1 + "','" + environ + "','" + objRecurringObject2.Kind + "','" + siFormatExportAllpages + "','" + objRecurringObject2.CUID + "','" + safeSQL(paramValues) + "')"

					//msSQLInsertString = "insert into " + destTblNm + " (REPORT_ID, INSTANCE_ID, SI_STARTTIME, SI_ENDTIME, SI_SUBMITTER, SI_SERVER, SI_OWNER, SI_SCHEDULE_STATUS, SI_SCHEDULE_TYPE, SI_SCHEDULE_INTERVAL_MINUTES, SI_SCHEDULE_INTERVAL_HOURS, SI_SCHEDULE_INTERVAL_MONTHS, SI_NAME, SI_CREATION_TIME, SI_DESTINATION, SI_PASSWORD, DATASRC_LOGIN_NM,  DATASRC_LOGIN_NM2, SI_PASSWORD2, SI_NUM_PROMPTS, REPORT_PATH, REPORT_CUID, SI_MACHINE_USED, SI_NEXTRUNTIME, PARAM_VALUES, DISTRIBUTION_LIST, CUID, SI_FORMAT_EXPORT_ALLPAGES, SI_KIND) values (" + objRecurringObject2.ParentID + ", " + objRecurringObject2.ID + ", to_date('" + objRecurringObject2.SchedulingInfo.Properties.Item("SI_STARTTIME").Value + "','MM/DD/YYYY HH12:MI:SS AM')" + ", to_date('" + objRecurringObject2.SchedulingInfo.Properties.Item("SI_ENDTIME").Value + "','MM/DD/YYYY HH12:MI:SS AM')" + ", '" + safeSQL(objRecurringObject2.SchedulingInfo.Properties.Item("SI_SUBMITTER").Value) + "', '" + server1 + "', '" + safeSQL(objRecurringObject2.Properties.Item("SI_OWNER").Value) + "', " + objRecurringObject2.Properties.Item("SI_SCHEDULE_STATUS").Value + ", " + objRecurringObject2.SchedulingInfo.Properties.Item("SI_SCHEDULE_TYPE").Value + "," + objRecurringObject2.SchedulingInfo.Properties.Item("SI_SCHEDULE_INTERVAL_MINUTES").Value + "," + objRecurringObject2.SchedulingInfo.Properties.Item("SI_SCHEDULE_INTERVAL_HOURS").Value + "," + objRecurringObject2.SchedulingInfo.Properties.Item("SI_SCHEDULE_INTERVAL_MONTHS").Value + ", '" + safeSQL(objRecurringObject2.Title) + "', to_date('" + objRecurringObject2.Properties.Item("SI_CREATION_TIME").Value + "','MM/DD/YYYY HH12:MI:SS AM'), '" + destination + "','" + pswd1 + "', '" + logon1 + "','" + logon2 + "', '" + pswd2 + "', " + nbrPrompts + ",'" + safeSQL(cookieCrumbs) + "','" + objRecurringObject2.ParentCUID + "'," + "'" + machine_used + "'," + " to_date('" + nextRunTime + "','MM/DD/YYYY HH12:MI:SS AM'), '" + paramValues + "', '" + distributionList + "','" + objRecurringObject2.CUID + "','" + siFormatExportAllpages + "','" + objRecurringObject2.Kind + "')"
					//					still need to get SI_SCHEDULE_TYPE, SI_SCHEDULE_INTERVAL_MINUTES, SI_SCHEDULE_INTERVAL_HOURS, SI_SCHEDULE_INTERVAL_MONTHS
					msSQLInsertString = "insert into " + destTblNm + " (REPORT_ID, INSTANCE_ID, SI_STARTTIME, SI_ENDTIME,  SI_SUBMITTER, SI_SERVER, SI_OWNER, SI_SCHEDULE_STATUS,REPORT_PATH, SI_NAME, SI_CREATION_TIME, SI_NUM_PROMPTS, DATASRC_LOGIN_NM, SI_PASSWORD, DATASRC_LOGIN_NM2, SI_PASSWORD2,  SI_MACHINE_USED,  SI_DESTINATION,  REPORT_CUID,  DISTRIBUTION_LIST,  SI_KIND, SI_FORMAT_EXPORT_ALLPAGES, CUID, PARAM_VALUES,SI_SCHEDULE_TYPE, SI_SCHEDULE_INTERVAL_MINUTES, SI_SCHEDULE_INTERVAL_HOURS, SI_SCHEDULE_INTERVAL_MONTHS, SI_SCHEDULE_INTERVAL_DAYS) values (" + objRecurringObject.getParentID() + ", ";
					msSQLInsertString += objRecurringObject.getID();
					msSQLInsertString += ", ('" + Helper.msSQLDateTime(startTime) + "')";
					msSQLInsertString += ", ('" + Helper.msSQLDateTime(endTime) + "')"  + ", '";
					msSQLInsertString += submitter;					
					msSQLInsertString += "', '" + Helper.safeSQL(server);
					msSQLInsertString += "', '" + owner;					
					msSQLInsertString += "', " + scheduleStatus;
					msSQLInsertString += ", '" + Helper.safeSQL(cookieCrumbs) + "', '" + Helper.safeSQL(objRecurringObject.getTitle());//threw an error
					msSQLInsertString += "', ('" + Helper.msSQLDateTime(creationTime) + "'), ";						
					msSQLInsertString += numPrompts + ",'" + logon1 + "','" + pswd1 + "', '" + logon2 + "', '" + pswd2  + "','" + machineUsed + "','";
					//msSQLInsertString += "', to_date('" + Helper.msSQLDateTime(updateTS) + "','MM/DD/YYYY HH12:MI:SS AM'), '";
					msSQLInsertString += destination +  "', '" ;
					msSQLInsertString += parentCuid;
					//the error is in this line:
					msSQLInsertString += "','" + distributionList + "','" + objRecurringObject.getKind() + "', '" + siFormatExportAllpages + "','";
					msSQLInsertString += cuid;
					msSQLInsertString += "','" + Helper.safeSQL(paramValueString) + "',";
					msSQLInsertString +=  scheduleType + ", " + scheduleIntervalMinutes + ", " + scheduleIntervalHours + ", " + scheduleIntervalMonths + ", " + scheduleIntervalDays + ")"; 

					System.out.println(msSQLInsertString);
					//si_submitter shouldn't be null and start time and end time should have real values and not default to 1980
					try
					{
						Helper.runMSSQLInsertQuery(msSQLInsertString);
						destination = "";
					}
					catch (Exception ex)
					{
						System.out.println("Problem with insert query");
						Helper.runMSSQLInsertQuery("insert into STG1_BO4_TRACKER_ERRORS (ERROR_MESSAGE, LOAD_DT)  VALUES('" + ex.getMessage() + " " + ex.getStackTrace() + " " + EnterpriseEnv + "', GETDATE())");
						System.out.println(ex.getMessage() + " " + ex.getStackTrace() + " line 2208");
					}
				}
			}
			// Clean up the Enterprise Session.
			if(objEnterpriseSession != null) {
				try {
					objEnterpriseSession.logoff();

				} catch(Exception e_ignore_in_cleanup) {}
			}
		}
		catch (Exception ex)
		{
			System.out.println(ex.getMessage() + " " + ex.getStackTrace() + " line 2215");
			Helper.runMSSQLInsertQuery("insert into STG1_BO4_TRACKER_ERRORS (ERROR_MESSAGE, LOAD_DT)  VALUES ('" + ex.getMessage() + " " + ex.getStackTrace() + " " + EnterpriseEnv + "', GETDATE())");
			//'sendmail("Logon error", ex.getMessage() + " " + ex.getStackTrace)
			//Helper.sendmail("bishopp@lexmark.com", "bishopp@lexmark.com, amarabes@lexmark.com,ajpatel@lexmark.com,msankar@lexmark.com,ajain@lexmark.com", "Logon error - load_stg1_migr_XI_reports(String, String, String, String, String) - " + EnterpriseEnv, ex.getMessage() + " " + ex.getStackTrace());
			//Helper.sendmail("bishopp@lexmark.com", "bishopp@lexmark.com", "Logon error - load_stg1_boxi_runhist(String, String, String, String, String) - " + EnterpriseEnv, ex.getMessage() + " " + ex.getStackTrace());

		}


	}

	public static void load_stg1_bo_users (String EnterpriseEnv,String login, String pswd, 
			String auth, String destTblNm){
		String strInfoSQL ;
		//Logon;
		//ISessionMgr objSessionMgr = new CrystalEnterprise.getSessionMgr(); //should be New()


		IEnterpriseSession objEnterpriseSession;
		//IInfoStore objInfoStore;
		IInfoObjects colInfoObjects;
		IInfoObject objInfoObject;
		IInfoObjects colInfoObjects2;   
		IInfoObject objInfoObject2;
		//int count = 1000000;
		String strCount = "0";
		Integer maxID;
		Integer count;
		String msSQLInsertString;        
		Calendar defaultDate = Calendar.getInstance();
		defaultDate.set(1980,1,1,0,0,0);
		String lastlogontime  = Helper.msSQLDate(defaultDate);
		Calendar calLastLogonTime;
		Date dtLastLogonTime;
		Calendar maxCreationDate;
		String maxid;
		String userfullname = "";
		IProperty aggCount;
		int colCount;
		Calendar creationTime = Calendar.getInstance();
		Calendar lastLogonTime = Calendar.getInstance();
		Calendar updateTime = Calendar.getInstance();

		try
		{
			ISessionMgr  objSessionMgr = CrystalEnterprise.getSessionMgr();
			objEnterpriseSession = objSessionMgr.logon(login, pswd, EnterpriseEnv, auth);    
			IInfoStore objInfoStore = (IInfoStore) objEnterpriseSession.getService("InfoStore");

			//Since I learned that si_id can be reused by CMS,I started using dates to do incrementals (03/30/2007)

			System.out.println("select max(si_creation_time) from " + destTblNm);
			maxCreationDate = Helper.getMaxLoadDt("select max(si_creation_time) maxLoadDate from " + destTblNm);
			System.out.println("Max creation date is " + maxCreationDate.getTime());
			maxid = Helper.getMaxID("select max(si_id) maxID from " + destTblNm);

			///COMMENT OUT AFTER GOING TO PROD///
			//maxid = "0"; //Oct 2014:  no more full loads daily.  Need to mark deleted users with another function
			if (maxid == "0")  {
				//'strInfoSQL = "SELECT count(SI_ID) from CI_INFOOBJECTS WHERE SI_PROGID= 'CrystalEnterprise.Report' and SI_INSTANCE = 0"
				//strInfoSQL = "SELECT count(SI_ID) from CI_SYSTEMOBJECTS WHERE SI_PROGID= 'CrystalEnterprise.User'";
				strInfoSQL = "SELECT count(SI_ID) from CI_SYSTEMOBJECTS WHERE SI_KIND= 'User'";
			}
			else
				//'strInfoSQL = "SELECT count(SI_ID) from CI_INFOOBJECTS WHERE SI_PROGID= 'CrystalEnterprise.Report' and SI_INSTANCE = 0 and si_creation_time > '" + formatDateBO(maxCreationDate) + "'"
				//'strInfoSQL = "SELECT count(SI_ID) from CI_INFOOBJECTS WHERE SI_PROGID= 'CrystalEnterprise.Report' and SI_INSTANCE = 0 and si_creation_time >= '" + formatDateBO(maxCreationDate) + "'"  '4/18/2007
				//strInfoSQL = "SELECT count(SI_ID) from CI_SYSTEMOBJECTS WHERE SI_PROGID= 'CrystalEnterprise.User' and si_creation_time > '" + Helper.formatDateBO(maxCreationDate) + "'" ;
				//Nov 5, 2012:  it seems like BI 4 interprets a ">" as ">=" and so I'm getting duplicates.
				//BUT, I'm trying to load all users everyday, so this query should never even run
				strInfoSQL = "SELECT count(SI_ID) from CI_SYSTEMOBJECTS WHERE SI_KIND= 'User' and si_creation_time > '" + Helper.formatDateBO(maxCreationDate) + "'" ;

			//''***************PRODUCTION CODE***********UNCOMMENT AFTER TEST

			System.out.println(strInfoSQL);

			//EXAMPLE OF RETRIEVING PROPERTY BAG VALUES
			//objInfoObject.properties().getProperty("SI_UPDATE_TS");

			colInfoObjects = (IInfoObjects)objInfoStore.query(strInfoSQL);
			//objInfoObject = (IInfoObject)colInfoObjects.get(i);    
			objInfoObject = (IInfoObject)colInfoObjects.get(0);

			//The VB code used the collection here, but in Java, properties() isn't an available function, so I am trying to get this via InfoObject

			//System.out.println(objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").properties().getProperty("SI_ID"));
			System.out.println(objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").getID());//shows the ID, not the count!

			aggCount = objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT");

			//count = aggCount.getID().byteValue(); //that's not it
			strCount = ((IProperties)objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").getValue()).getProperty("SI_ID").getValue().toString();

			System.out.println("Record count is " + strCount);
			//count = (int)strCount;

			//System.out.println("Record count is " + aggCount.getValue().toString());

			//''***************PRODUCTION CODE***********UNCOMMENT AFTER TEST
			if (maxid == "0") {
				strInfoSQL = "SELECT TOP " + strCount + " SI_ID FROM CI_SYSTEMOBJECTS WHERE  SI_KIND= 'User'";
			}
			else
				//'strInfoSQL = "SELECT TOP " + count + " SI_ID FROM CI_INFOOBJECTS WHERE SI_PROGID= 'CrystalEnterprise.Report' and SI_INSTANCE = 0 and SI_ID>" + CInt(maxid)
				//'strInfoSQL = "SELECT TOP " + count + " SI_ID FROM CI_INFOOBJECTS WHERE SI_PROGID= 'CrystalEnterprise.Report' and SI_INSTANCE = 0 and SI_CREATION_TIME > '" + formatDateBO(maxCreationDate) + "'"
				strInfoSQL = "SELECT TOP " + strCount + " SI_ID FROM CI_SYSTEMOBJECTS WHERE  SI_KIND= 'User'  and SI_CREATION_TIME > '" + Helper.formatDateBO(maxCreationDate) + "'";

			//''***************PRODUCTION CODE***********UNCOMMENT AFTER TEST
			System.out.println("line 2218 " + strInfoSQL);

			colInfoObjects = (IInfoObjects)objInfoStore.query(strInfoSQL);

			if (colInfoObjects.size() > 0)  {
				System.out.println("We do have objects in collection");		

				/*Connection con=null;
				Class.forName("oracle.jdbc.driver.OracleDriver");
				System.out.println("oracle driver loaded in load_stg1_Crystal_Reports()");
				con=DriverManager.getConnection("jdbc:oracle:thin:@dlxkrbodb1.lex.lexmark.com:1750:edr1den1","botrack","********");
				Statement s=con.createStatement();*/


				//'System.out.println("creation time  " + objInfoObject2.Properties.Item("SI_CREATION_TIME").getValue)

				//oReportAppFactory = (IReportAppFactory)objEnterpriseSession.getService("", "RASReportService");
				System.out.println("collection size is " + colInfoObjects.size());
				colCount = colInfoObjects.size();
				for (int k =0; k < colCount; k++)
				{
					userfullname = "";
					//lastlogontime.set(1980,1,1,0,0,0);
					defaultDate.set(1980,1,1,0,0,0);
					lastlogontime  = Helper.msSQLDate(defaultDate);

					System.out.println("iterator, k, is now " + k);
					objInfoObject = (IInfoObject) colInfoObjects.get(k);

					//'throws an error when I let it just rip through the query result.
					//strInfoSQL = "Select * From CI_APPOBJECTS Where SI_INSTANCE=0 and SI_ID =" + objInfoObject.getID();
					//strInfoSQL = "Select SI_ID, SI_OWNER, SI_UPDATE_TS, SI_CREATION_TIME, SI_PROGID, SI_LASTLOGONTIME, SI_USERFULLNAME, SI_ALIASES, CUID, SI_KIND From CI_SYSTEMOBJECTS Where SI_ID =" + objInfoObject.properties().getProperty(1).getValue();
					strInfoSQL = "Select * From CI_SYSTEMOBJECTS Where SI_ID =" + objInfoObject.getID();
					System.out.println(strInfoSQL);
					colInfoObjects2 = (IInfoObjects)objInfoStore.query(strInfoSQL);
					int colCount2 = colInfoObjects2.size();

					for (int j =0; j < colCount2; j++)
					{

						objInfoObject2 = (IInfoObject)colInfoObjects2.get(j);	
						try
						{
							try {
								userfullname = (String)objInfoObject2.properties().getProperty("SI_USERFULLNAME").getValue();
								//lastlogontime = (String)objInfoObject2.properties().getProperty("SI_LASTLOGONTIME").getValue();
								//calLastLogonTime.set(objInfoObject2.properties().getProperty("SI_LASTLOGONTIME").getValue());
							}
							catch (Exception ex)
							{}


							try{
								IProperties properties = objInfoObject2.properties();
								IProperty lastLogonTimeProperty = properties.getProperty(CePropertyID.SI_LASTLOGONTIME);
								//String value = (String) creationTimeProperty.getValue();
								lastLogonTime.setTime ((Date) lastLogonTimeProperty.getValue());  //This works
								System.out.println("Last logon time is " + lastLogonTime.getTime());



								//lastLogonTime.setTime((Date) objInfoObject.properties().getProperty("SI_LASTLOGONTIME").getValue());  //throws error
							}
							catch (Exception ex)
							{
								System.out.println("Error with getting last logon time for " + objInfoObject2.getTitle() + ex.getMessage() + " " + ex.getStackTrace());
							}


							try{
								//nextRunTime = (Calendar)objInfoObject.properties().getProperty("SI_NEXTRUNTIME").getValue();
								updateTime.setTime((Date) objInfoObject.properties().getProperty("SI_UPDATE_TS").getValue());
							}
							catch (Exception ex)
							{
								System.out.println("Error with getting update TS time" + ex.getMessage() + " " + ex.getStackTrace());
							}


							try 
							{
								//Got this from D:\Documents and Settings\bishopp\My Documents\BOXI\Code Examples\xi4_boejava_dg_en\html\topic15.html
								IProperties properties = objInfoObject2.properties();
								IProperty creationTimeProperty = properties.getProperty(CePropertyID.SI_CREATION_TIME);
								//String value = (String) creationTimeProperty.getValue();
								creationTime.setTime ((Date) creationTimeProperty.getValue());  //This works
								System.out.println("creation time is " + creationTime.getTime());
							}
							catch (Exception ex){

							}

							msSQLInsertString = "INSERT INTO " + destTblNm + " (SI_ID, SI_OWNER, SI_NAME, SI_UPDATE_TS, SI_CREATION_TIME, SI_PROGID, SI_LASTLOGONTIME, SI_USERFULLNAME, CUID, SI_KIND) VALUES (" + objInfoObject2.getID() + ", '" + Helper.safeSQL(objInfoObject2.properties().getProperty("SI_OWNER").getValue().toString()) + "','" + Helper.safeSQL(objInfoObject2.getTitle()) + "', ('" + Helper.msSQLDateTime(updateTime) + "'), ('" + Helper.msSQLDateTime(creationTime) + "'), '" + objInfoObject2.properties().getProperty("SI_PROGID").getValue() + "', ('" + Helper.msSQLDateTime(lastLogonTime) + "'), '" + Helper.safeSQL(userfullname) + "','" + objInfoObject2.getCUID() + "','" + objInfoObject2.getKind()+ "')";
							System.out.println(msSQLInsertString);
							Helper.runMSSQLInsertQuery(msSQLInsertString);

							try {
								//Now insert all the groups for each user into STG1_BO4x_USER_GROUP_REL.
								String destTblNm2 ="";

								if (EnterpriseEnv.equals("PASHWBOBJ013")) {
									destTblNm2 = "STG1_BO4P_USER_GROUP_REL";
								}
								else if (EnterpriseEnv.equals("QASHWBOBJ013")) {
									destTblNm2 = "STG1_BO4Q_USER_GROUP_REL";
								}
								else if (EnterpriseEnv.equals("DASHWBOBJ011")) {
									destTblNm2 = "STG1_BO4D_USER_GROUP_REL";
								}
								else if (EnterpriseEnv.equals("DASHWBOBJ012")) {
									destTblNm2 = "STG1_BO4V_USER_GROUP_REL";
								}


								IProperties theGroups = objInfoObject2.properties();
								//IProperty theGroupCount = (IProperty) theGroups.getProperty("SI_TOTAL").getValue();
								//IProperty theGroupCount = theGroups.getProperty(CePropertyID.SI_GROUP_MEMBERS);

								//int groupCount = (int) Integer.parseInt(theGroupCount.getValue().toString());
								////always null and throws an error

								IProperties oGroupInfo = (IProperties)objInfoObject2.properties().getProperty("SI_USERGROUPS").getValue();
								//Retrieve the number of groups for the user
								int numGroups =((Integer)oGroupInfo.getProperty("SI_TOTAL").getValue()).intValue();

								for (int i=1; i <= numGroups; i++){
									System.out.println("Group id is " + oGroupInfo.getProperty(i).getID());
									String msSQLInsertString2 = "INSERT INTO " + destTblNm2 + " (GROUP_SI_ID, USER_SI_ID, LOAD_DT) VALUES ( " + oGroupInfo.getProperty(i).getID() + ", " + objInfoObject2.getID() + ", GETDATE())";
									System.out.println(msSQLInsertString2);
									Helper.runMSSQLInsertQuery(msSQLInsertString2);
								}
							}
							catch (Exception ex){
								System.out.println("Problem somewhere in loop to insert groups." + ex.getMessage() + " " + ex.getStackTrace());
							}
						}
						catch (Exception ex){
							System.out.println("Problem somewhere in loop to build insert statement." + ex.getMessage() + " " + ex.getStackTrace());
						}
					}
				}
				objSessionMgr = null;
				//objEnterpriseSession = Nothing;
				objInfoStore = null;
				colInfoObjects = null;
				objInfoObject = null;
				colInfoObjects2 = null;
				objInfoObject2 = null;

			}
			// Clean up the Enterprise Session.
			if(objEnterpriseSession != null) {
				try {
					objEnterpriseSession.logoff();

				} catch(Exception e_ignore_in_cleanup) {}
			}
		}
		catch (Exception ex)
		{
			System.out.println(ex.getMessage());
			ex.printStackTrace(System.out);
			Helper.runMSSQLInsertQuery("insert into STG1_BO4_TRACKER_ERRORS (ERROR_MESSAGE, LOAD_DT) VALUES ('" + ex.getMessage() + " " + ex.getStackTrace() + " " + EnterpriseEnv + "', GETDATE())");
			//'sendmail("Logon error", ex.getMessage() + " " + ex.getStackTrace)
			//Helper.sendmail("bishopp@lexmark.com", "bishopp@lexmark.com, amarabes@lexmark.com,ajpatel@lexmark.com,msankar@lexmark.com,ajain@lexmark.com", "Logon error - load_stg1_migr_XI_reports(String, String, String, String, String) - " + EnterpriseEnv, ex.getMessage() + " " + ex.getStackTrace());
			Emailer.sendEmail("bishopp@lexmark.com", "bishopp@lexmark.com", "Logon error - load_stg1_bo_users(String, String, String, String, String) - " + EnterpriseEnv, ex.getMessage() + " " + ex.getStackTrace(), null);
		}			
	}

	public static void load_stg1_boxi_groups (String EnterpriseEnv,String login, String pswd, 
			String auth, String destTblNm){
		String strInfoSQL ;
		//Logon;
		//ISessionMgr objSessionMgr = new CrystalEnterprise.getSessionMgr(); //should be New()


		IEnterpriseSession objEnterpriseSession;
		//IInfoStore objInfoStore;
		IInfoObjects colInfoObjects;
		IInfoObject objInfoObject;
		IInfoObjects colInfoObjects2;   
		IInfoObject objInfoObject2;
		//int count = 1000000;
		String strCount = "0";
		Integer maxID;
		Integer count;
		String msSQLInsertString;        
		Calendar defaultDate = Calendar.getInstance();
		defaultDate.set(1980,1,1,0,0,0);
		String lastlogontime  = Helper.oracleDate(defaultDate);
		Calendar calLastLogonTime;
		Date dtLastLogonTime;
		Calendar maxCreationDate= Calendar.getInstance();
		String maxid;
		String groupname = "";
		IProperty aggCount;
		int colCount;
		Calendar creationTime = Calendar.getInstance();
		Calendar updateTime = Calendar.getInstance();
		String SI_OWNER = "";
		String SI_NAME = "";
		String updateTS = "";
		//String createDate = "";
		Calendar createDate = Calendar.getInstance();
		String parentCuid = "";


		try
		{
			ISessionMgr  objSessionMgr = CrystalEnterprise.getSessionMgr();
			objEnterpriseSession = objSessionMgr.logon(login, pswd, EnterpriseEnv, auth);    
			IInfoStore objInfoStore = (IInfoStore) objEnterpriseSession.getService("InfoStore");

			//Since I learned that si_id can be reused by CMS,I started using dates to do incrementals (03/30/2007)

			//System.out.println("select max(si_creation_time) from " + destTblNm);
			//maxCreationDate = Helper.getMaxLoadDt("select max(si_creation_time) maxLoadDate from " + destTblNm);
			//System.out.println("Max creation date is " + maxCreationDate);
			//maxid = Helper.getMaxID("select max(si_id) maxID from " + destTblNm);
			//Hard-code this so you get a full load everyday
			maxid = "0";
			if (maxid == "0")  {
				//'strInfoSQL = "SELECT count(SI_ID) from CI_INFOOBJECTS WHERE SI_PROGID= 'CrystalEnterprise.Report' and SI_INSTANCE = 0"
				strInfoSQL = "SELECT count(SI_ID) from CI_SYSTEMOBJECTS WHERE SI_KIND= 'UserGroup'";
			}
			else
				//'strInfoSQL = "SELECT count(SI_ID) from CI_INFOOBJECTS WHERE SI_PROGID= 'CrystalEnterprise.Report' and SI_INSTANCE = 0 and si_creation_time > '" + formatDateBO(maxCreationDate) + "'"
				//'strInfoSQL = "SELECT count(SI_ID) from CI_INFOOBJECTS WHERE SI_PROGID= 'CrystalEnterprise.Report' and SI_INSTANCE = 0 and si_creation_time >= '" + formatDateBO(maxCreationDate) + "'"  '4/18/2007
				strInfoSQL = "SELECT count(SI_ID) from CI_SYSTEMOBJECTS WHERE SI_KIND= 'UserGroup' and si_creation_time > '" + Helper.formatDateBO(maxCreationDate) + "'" ;

			//''***************PRODUCTION CODE***********UNCOMMENT AFTER TEST

			System.out.println(strInfoSQL);

			//EXAMPLE OF RETRIEVING PROPERTY BAG VALUES
			//objInfoObject.properties().getProperty("SI_UPDATE_TS");

			colInfoObjects = (IInfoObjects)objInfoStore.query(strInfoSQL);
			//objInfoObject = (IInfoObject)colInfoObjects.get(i);    
			objInfoObject = (IInfoObject)colInfoObjects.get(0);

			//The VB code used the collection here, but in Java, properties() isn't an available function, so I am trying to get this via InfoObject

			//System.out.println(objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").properties().getProperty("SI_ID"));
			System.out.println(objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").getID());//shows the ID, not the count!

			aggCount = objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT");

			//count = aggCount.getID().byteValue(); //that's not it
			strCount = ((IProperties)objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").getValue()).getProperty("SI_ID").getValue().toString();

			System.out.println("Record count is " + strCount);
			//count = (int)strCount;

			//System.out.println("Record count is " + aggCount.getValue().toString());

			//''***************PRODUCTION CODE***********UNCOMMENT AFTER TEST
			if (maxid == "0") {
				strInfoSQL = "SELECT TOP " + strCount + " SI_ID FROM CI_SYSTEMOBJECTS WHERE SI_KIND= 'UserGroup'";
			}
			else
				//'strInfoSQL = "SELECT TOP " + count + " SI_ID FROM CI_INFOOBJECTS WHERE SI_PROGID= 'CrystalEnterprise.Report' and SI_INSTANCE = 0 and SI_ID>" + CInt(maxid)
				//'strInfoSQL = "SELECT TOP " + count + " SI_ID FROM CI_INFOOBJECTS WHERE SI_PROGID= 'CrystalEnterprise.Report' and SI_INSTANCE = 0 and SI_CREATION_TIME > '" + formatDateBO(maxCreationDate) + "'"
				strInfoSQL = "SELECT TOP " + strCount + " SI_ID FROM CI_SYSTEMOBJECTS WHERE  SI_KIND= 'UserGroup' and SI_CREATION_TIME > '" + Helper.formatDateBO(maxCreationDate) + "'";

			//''***************PRODUCTION CODE***********UNCOMMENT AFTER TEST
			System.out.println("line 1595 " + strInfoSQL);

			colInfoObjects = (IInfoObjects)objInfoStore.query(strInfoSQL);

			if (colInfoObjects.size() > 0)  {
				System.out.println("We do have objects in collection");		

				/*Connection con=null;
				Class.forName("oracle.jdbc.driver.OracleDriver");
				System.out.println("oracle driver loaded in this function");
				con=DriverManager.getConnection("jdbc:oracle:thin:@dlxkrbodb1.lex.lexmark.com:1750:edr1den1","botrack","********");
				Statement s=con.createStatement();*/


				//'System.out.println("creation time  " + objInfoObject2.Properties.Item("SI_CREATION_TIME").getValue)

				//oReportAppFactory = (IReportAppFactory)objEnterpriseSession.getService("", "RASReportService");
				System.out.println("collection size is " + colInfoObjects.size());
				colCount = colInfoObjects.size();
				for (int k =0; k < colCount; k++)
				{
					groupname = "";
					//lastlogontime.set(1980,1,1,0,0,0);
					defaultDate.set(1980,1,1,0,0,0);
					lastlogontime  = Helper.oracleDate(defaultDate);

					System.out.println("iterator, k, is now " + k);
					objInfoObject = (IInfoObject) colInfoObjects.get(k);

					//'throws an error when I let it just rip through the query result.
					//strInfoSQL = "Select * From CI_APPOBJECTS Where SI_INSTANCE=0 and SI_ID =" + objInfoObject.getID();
					try {

						//strInfoSQL = "Select SI_ID, SI_OWNER, SI_UPDATE_TS, SI_CREATION_TIME,  SI_LASTLOGONTIME, SI_USERFULLNAME, SI_ALIASES, CUID From CI_SYSTEMOBJECTS Where SI_ID =" + objInfoObject.properties().getProperty(1).getValue();
						strInfoSQL = "Select SI_ID, SI_OWNER, SI_NAME, SI_UPDATE_TS, SI_CREATION_TIME,  SI_PROGID, SI_KIND, CUID, SI_PARENT_CUID, SI_LASTLOGONTIME, SI_USERFULLNAME, SI_ALIASES From CI_SYSTEMOBJECTS Where SI_ID =" + objInfoObject.getID();
						System.out.println(strInfoSQL);
						colInfoObjects2 = (IInfoObjects)objInfoStore.query(strInfoSQL);
						int colCount2 = colInfoObjects2.size();
						System.out.println("collection size is " + colCount2);
						//for (int j =0; j < colCount2; j++)
						//{

						objInfoObject2 = (IInfoObject)colInfoObjects2.get(0);	
						try 
						{
							//Got this from D:\Documents and Settings\bishopp\My Documents\BOXI\Code Examples\xi4_boejava_dg_en\html\topic15.html
							IProperties properties = objInfoObject2.properties();
							IProperty creationTimeProperty = properties.getProperty(CePropertyID.SI_CREATION_TIME);
							//String value = (String) creationTimeProperty.getValue();
							creationTime.setTime ((Date) creationTimeProperty.getValue());  //This works
							System.out.println("creation time is " + creationTime.getTime());
						}
						catch (Exception ex){
							System.out.println(ex.getMessage() + " " + ex.getStackTrace());
						}

						try
						{
							groupname = (String)objInfoObject2.properties().getProperty("SI_NAME").getValue();

						}
						catch (Exception ex){
							System.out.println(ex.getMessage() + " " + ex.getStackTrace());
						}

						try {
							SI_OWNER = Helper.safeSQL(objInfoObject2.properties().getProperty("SI_OWNER").getValue().toString());
						}
						catch (Exception ex){
							System.out.println(ex.getMessage() + " " + ex.getStackTrace());
						}

						try {
							SI_NAME = Helper.safeSQL(objInfoObject2.getTitle());
						}
						catch (Exception ex){
							System.out.println(ex.getMessage() + " " + ex.getStackTrace());
						} 

						try {
							//updateTS = Helper.oracleDate((Calendar)objInfoObject2.properties().getProperty("SI_UPDATE_TS").getValue().toString());
							//updateTS = Helper.oracleDate((Calendar)objInfoObject2.properties().getProperty("SI_UPDATE_TS").getValue());
							updateTime.setTime((Date) objInfoObject2.properties().getProperty("SI_UPDATE_TS").getValue());
						}
						catch (Exception ex){
							System.out.println(ex.getMessage() + " " + ex.getStackTrace());
						} 

						try {
							//createDate = Helper.oracleDate((Calendar)objInfoObject2.properties().getProperty("SI_CREATION_TIME").getValue());
							creationTime.setTime((Date) objInfoObject2.properties().getProperty("SI_CREATION_TIME").getValue());
						}
						catch (Exception ex){
							System.out.println(ex.getMessage() + " " + ex.getStackTrace());
						} 

						try {
							parentCuid = objInfoObject2.properties().getProperty("SI_PARENT_CUID").getValue().toString();
						}
						catch (Exception ex){
							System.out.println(ex.getMessage() + " " + ex.getStackTrace());
						} 

						try
						{
							//this insert query is wrong
							//msSQLInsertString = "INSERT INTO " + destTblNm + " (SI_ID, SI_OWNER, SI_NAME, SI_UPDATE_TS, SI_CREATION_TIME,  SI_PROGID, SI_KIND, CUID, SI_PARENT_CUID) VALUES (" + objInfoObject2.getID() + ", '" + Helper.safeSQL(objInfoObject2.properties().getProperty("SI_OWNER").getValue().toString()) + "','" + Helper.safeSQL(objInfoObject2.getTitle()) + "', to_date('" + Helper.oracleDate((Calendar)objInfoObject2.properties().getProperty("SI_UPDATE_TS").getValue()) + "', 'DD-MM-YYYY'), to_date('" + objInfoObject2.properties().getProperty("SI_CREATION_TIME").getValue() + "','MM/DD/YYYY HH12:MI:SS AM'), "  + Helper.safeSQL(groupname) + "','" + objInfoObject2.getCUID() + "')";
							msSQLInsertString = "INSERT INTO " + destTblNm + " (SI_ID, SI_OWNER, SI_NAME, SI_UPDATE_TS, SI_CREATION_TIME,  SI_KIND, CUID, SI_PARENT_CUID) VALUES (" + objInfoObject2.getID() + ", '" + SI_OWNER + "','" + SI_NAME + "', ('" + Helper.msSQLDateTime(updateTime) + "'), ('" + Helper.msSQLDateTime(creationTime) + "'), '" + objInfoObject2.getKind() + "', '" + objInfoObject2.getCUID() + "','" + parentCuid + "')";
							System.out.println(msSQLInsertString);
							Helper.runMSSQLInsertQuery(msSQLInsertString);
						}
						catch (Exception ex){
							System.out.println(ex.getMessage() + " " + ex.getStackTrace());
						}

						//}

					}
					catch (Exception ex){
						System.out.println(ex.getMessage() + " " + ex.getStackTrace());
						ex.printStackTrace(System.out);
						Helper.runMSSQLInsertQuery("insert into STG1_BO4_TRACKER_ERRORS (ERROR_MESSAGE, LOAD_DT) VALUES ( '" + ex.getMessage() + " " + ex.getStackTrace() + " " + EnterpriseEnv + "', GETDATE())");
						//'sendmail("Logon error", ex.getMessage() + " " + ex.getStackTrace)
						//Helper.sendmail("bishopp@lexmark.com", "bishopp@lexmark.com, amarabes@lexmark.com,ajpatel@lexmark.com,msankar@lexmark.com,ajain@lexmark.com", "Logon error - load_stg1_migr_XI_reports(String, String, String, String, String) - " + EnterpriseEnv, ex.getMessage() + " " + ex.getStackTrace());
						Emailer.sendEmail("bishopp@lexmark.com", "bishopp@lexmark.com", "Logon error - load_stg1_boxi_groups(String, String, String, String, String) - " + EnterpriseEnv, ex.getMessage() + " " + ex.getStackTrace(), null);

					}


				}
				objSessionMgr = null;
				//objEnterpriseSession = Nothing;
				objInfoStore = null;
				colInfoObjects = null;
				objInfoObject = null;
				colInfoObjects2 = null;
				objInfoObject2 = null;

			}
			// Clean up the Enterprise Session.
			if(objEnterpriseSession != null) {
				try {
					objEnterpriseSession.logoff();

				} catch(Exception e_ignore_in_cleanup) {}
			}
		}
		catch (Exception ex)
		{
			System.out.println(ex.getMessage() + " " + ex.getStackTrace());
			ex.printStackTrace(System.out);
			Helper.runMSSQLInsertQuery("insert into STG1_BO4_TRACKER_ERRORS (ERROR_MESSAGE, LOAD_DT) VALUES ( '" + ex.getMessage() + " " + ex.getStackTrace() + " " + EnterpriseEnv + "', GETDATE())");
			//'sendmail("Logon error", ex.getMessage() + " " + ex.getStackTrace)
			//Helper.sendmail("bishopp@lexmark.com", "bishopp@lexmark.com, amarabes@lexmark.com,ajpatel@lexmark.com,msankar@lexmark.com,ajain@lexmark.com", "Logon error - load_stg1_migr_XI_reports(String, String, String, String, String) - " + EnterpriseEnv, ex.getMessage() + " " + ex.getStackTrace());
			Emailer.sendEmail("bishopp@lexmark.com", "bishopp@lexmark.com", "Logon error - load_stg1_boxi_groups(String, String, String, String, String) - " + EnterpriseEnv, ex.getMessage() + " " + ex.getStackTrace(), null);
		}			
	}

	public static Boolean getFormatAllPagesSetting(IInfoObject oReport) {
		Boolean formatAllPagesSetting = false;
		String formattype = "";

		System.out.println("SI_ID is " + oReport.getID());
		try {
			System.out.println("Kind is " + oReport.getKind());
		} catch (SDKException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {


			IReportProcessingInfo processInfo2 = (IReportProcessingInfo) oReport;
			//This simple cast from IInfoObject to IReportProcessingInfo can throw an error.

			//IReportProcessingInfo processInfo2 = (IReportProcessingInfo) oReport.getPluginProcessingInterface(oReport.getKind());

			//IReportProcessingInfo processInfo2 = (IReportProcessingInfo) oReport.getProcessingInfo();
			//com.crystaldecisions.sdk.occa.infostore.internal.ProcessingInfo cannot be cast to 
			//com.crystaldecisions.sdk.plugin.desktop.common.IReportProcessingInfo [Ljava.lang.StackTraceElement;@3aacb4

			IReportFormatOptions repformoptions2 = (IReportFormatOptions) processInfo2.getReportFormatOptions();


			try {
				formattype = oReport.getKind();

				if (formattype.equals("Pdf")) {
					System.out.println("Format is Pdf");
					IPDFFormat formatPDF = (IPDFFormat) repformoptions2.getFormatInterface();
					//formatPDF.setAllPageExported(true);
					formatAllPagesSetting = formatPDF.isAllPagesExported();
				} else if (formattype.equals("Excel")){
					System.out.println("Format is Excel");
					IExcelFormat formatExcel = (IExcelFormat) repformoptions2.getFormatInterface();
					//formatExcel.setAllPageExported(true);
					formatAllPagesSetting = formatExcel.isAllPagesExported();
				} else if (formattype.equals("RichText")){
					System.out.println("Format is Rich Text");
					IRichTextFormat formatRichText = (IRichTextFormat) repformoptions2.getFormatInterface();
					formatAllPagesSetting = formatRichText.isAllPagesExported();
				} else if (formattype.equals("RichTextFormatEditable")){
					System.out.println("Format is Rich Text Editable");
					IRichTextFormatEditable formatRichTextEditable = (IRichTextFormatEditable) repformoptions2.getFormatInterface();
					formatAllPagesSetting = formatRichTextEditable.isAllPagesExported();
				} else if (formattype.equals("Word")){
					System.out.println("Format is Word");
					IWordFormat formatWord = (IWordFormat) repformoptions2.getFormatInterface();
					formatAllPagesSetting = formatWord.isAllPagesExported();
				} else
					formatAllPagesSetting = false;


				//return formatAllPagesSetting;
			} catch (SDKException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		catch (Exception ex){
			System.out.println(ex.getMessage() + " " + ex.getStackTrace());
			Helper.runMSSQLInsertQuery("insert into STG1_BO4_TRACKER_ERRORS (ERROR_MESSAGE, LOAD_DT) VALUES ('getFormatAllPagesSetting() line 2995 " + ex.getMessage() + " " + ex.getStackTrace()  + "', GETDATE())");
		}
		return formatAllPagesSetting;



	}

	public static void load_stg1_Crystal_Reports(String EnterpriseEnv , String login, String pswd, String auth, String destTblNm ){
		String strInfoSQL = "";
		//Logon;
		//ISessionMgr objSessionMgr = new CrystalEnterprise.getSessionMgr(); //should be New()


		IEnterpriseSession objEnterpriseSession;
		//IInfoStore objInfoStore;
		IInfoObjects colInfoObjects = null;
		IInfoObject objInfoObject;
		IInfoObjects colInfoObjects2;   
		IInfoObject objInfoObject2;
		//int count = 1000000;
		String strCount = "0";
		//Date rundate;
		//String si_user;
		String cookiecrumbs;
		String maxid = "0";
		String si_owner = "";
		//CrystalReportPluginLib.TablePrefix tablePrefix;
		//ReportClientDocument oReportClientDoc;
		//IReportAppFactory oReportAppFactory;
		//IReportLogon oReportLogon;

		String server1   = "none needed";
		String server2  = "none needed";
		String msSQLInsertString;

		String sourceSystem  = "TR";
		String businessViewNm = "";
		String originalFileNm = "";
		String localFilePath  = "";
		//Calendar maxCreationDate;
		//java.sql.Date maxCreationDate;
		Calendar creationTime = Calendar.getInstance();
		Calendar updateTime = Calendar.getInstance();

		//Helper helperFn = new Helper();  //for some reason this isn't needed
		int i = 0;
		//String maxid;

		IProperties si_files;
		String  si_path;
		String si_file1;
		//String server[] = new String[ 2 ];
		//IEnterpriseSession eSession;
		IProperty aggCount;
		int colCount = 0;

		//IStrings serverNames;
		String logon1 = "unknown";
		String logon2 = "unknown";
		int si_doc_common_connection = 0;
		String useOriginalDS = "";
		String password = "";
		String customPassword = "";
		String siDB = "";
		String siServerType = "";
		String siDSLUniverseStr = "";  //This normally is an SI_ID, which is a number; for CR4E it is the CUID of the universe; a conversion to SI_ID IS NECESSARY TO STORE IN THE TRACKER DB.
		IProperties siDSLUniverseProps;
		String datasourceType = "";
		int siDSLUniverseInt = 0;

		try
		{
			ISessionMgr  objSessionMgr = CrystalEnterprise.getSessionMgr();
			objEnterpriseSession = objSessionMgr.logon(login, pswd, EnterpriseEnv, auth);    
			IInfoStore objInfoStore = (IInfoStore) objEnterpriseSession.getService("InfoStore");
			//IReportAppFactory rptAppFactory = (IReportAppFactory)objEnterpriseSession.getService( "", "RASReportService" ) ;

			//Since I learned that si_id can be reused by CMS,I started using dates to do incrementals (03/30/2007)

			///////////////////////////Don't use these till we're in production
			//System.out.println("select max(si_creation_time) from " + destTblNm);
			//maxCreationDate = Helper.getMaxLoadDt("select max(si_creation_time) maxLoadDate from " + destTblNm);
			//System.out.println("Max creation date is " + maxCreationDate);
			maxid = Helper.getMaxID("select max(si_id) maxID from " + destTblNm);
			///////////////////////////
			//maxid = "0";
			System.out.println("maxid is " + maxid);

			//if (maxid.equals(null)) {
			//	maxid = "0";
			//}

			if (maxid == "0")  {
				//'strInfoSQL = "SELECT count(SI_ID) from CI_INFOOBJECTS WHERE SI_PROGID= 'CrystalEnterprise.Report' and SI_INSTANCE = 0"
				strInfoSQL = "SELECT count(SI_ID) from CI_INFOOBJECTS WHERE SI_KIND in ('CrystalReport') and SI_INSTANCE = 0";
				//strInfoSQL = "SELECT count(SI_ID) from CI_INFOOBJECTS WHERE SI_KIND in ('CrystalReport') and CUID IN ('AeTFFkuEtPBBnhCwVr22HpM', 'Adxj_0Jo1rNNowGx.j8bBiI')";
			}
			else  ////////UNCOMMENT AFTER WE GO LIVE
				//strInfoSQL = "SELECT count(SI_ID) from CI_INFOOBJECTS WHERE SI_KIND in ('CrystalReport') and SI_INSTANCE = 0 and si_creation_time >= '" + Helper.formatDateBO(maxCreationDate) + "'" ;
				strInfoSQL = "SELECT count(SI_ID) from CI_INFOOBJECTS WHERE SI_KIND in ('CrystalReport') and SI_INSTANCE = 0 and SI_ID > " + maxid;
			///JUST TEMPORARY TO FIX THE SERVER NAME
			//strInfoSQL = "SELECT count(SI_ID) from CI_INFOOBJECTS WHERE SI_KIND in ('CrystalReport') and SI_INSTANCE = 0 and SI_ID in (590858, 716770)";

			//strInfoSQL = "SELECT count(SI_ID) from CI_INFOOBJECTS WHERE SI_KIND in ('CrystalReport') and SI_INSTANCE = 0 and CUID in ('AcDDPxnvzNdFojPPaCIbS1g','AWmJ2OLgU.BNoN_Fs8Y90AE','AcetHSzh099Ni9S0jz8Zmlc','AQ7Zlou0yRlIuX1UC.RUqC4','AVw0yTztr39PooaEj.yYDck','AXx1oJSMg99JhnEjA2RcX1E','AR2ZVHHIuVVJgRPRiQo0_uk')";

			//''***************PRODUCTION CODE***********UNCOMMENT AFTER TEST

			System.out.println(strInfoSQL);

			//EXAMPLE OF RETRIEVING PROPERTY BAG VALUES
			//objInfoObject.properties().getProperty("SI_UPDATE_TS");

			colInfoObjects = (IInfoObjects)objInfoStore.query(strInfoSQL);
			//objInfoObject = (IInfoObject)colInfoObjects.get(i);  
			System.out.println("Number of rows:  " + colInfoObjects.getResultSize());
			objInfoObject = (IInfoObject)colInfoObjects.get(0);

			//The VB code used the collection here, but in Java, properties() isn't an available function, so I am trying to get this via InfoObject

			//System.out.println(objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").properties().getProperty("SI_ID"));
			System.out.println(objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").getID());//shows the ID, not the count!
			//count = objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").properties().getProperty("SI_ID");
			//count = objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").getID();
			aggCount = objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT");
			//count = aggCount.getValue().toString();
			//count = aggCount.getID().SIZE; //32
			//count = aggCount.getID().intValue(); //same as si_id value
			//count = aggCount.BAG;
			//count = aggCount.ALL; 65536, wrong 
			//count = aggCount.PROP_ID_LIST; //too big
			//count = aggCount.getID().hashCode(); //same as si_id
			//count = aggCount.getFlags(); //that's not it
			//count = aggCount.BINARY; //that's not it
			//count = aggCount.getID().byteValue(); //that's not it
			strCount = ((IProperties)objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").getValue()).getProperty("SI_ID").getValue().toString();

			System.out.println("Record count is " + strCount);
			//count = (int)strCount;

			//System.out.println("Record count is " + aggCount.getValue().toString());

			//''***************PRODUCTION CODE***********UNCOMMENT AFTER TEST
			if (maxid == "0") {
				strInfoSQL = "SELECT TOP " + strCount + " SI_ID FROM CI_INFOOBJECTS WHERE SI_KIND in ('CrystalReport') and SI_INSTANCE = 0";

				/////////////THIS IS TO DEBUG WHY 78 REPORTS AREN'T GETTING LOADED////////////////
				//strInfoSQL = "SELECT TOP " + strCount + " SI_ID FROM CI_INFOOBJECTS WHERE CUID in ('Aft3iSZVMf9DsqZckj.rGTs', 'ASoItkg1BERAvRhh3DNr2MY', 'Ab4OtSUbf4hOozQNSSwTKks', 'AR5D.blIvD1Likk7WIimYn0', 'AYlJYAHUiMRLp3yNENfWCk8', 'ATasuIyz2bhMg2vRm3UxFYc', 'ARkg2pB.wepAp4gsaWxO0og', 'AQmOscIdT45DoVztZGVr5Xk', 'AWphIRJpIGpCilCEq2mQEwI', 'AeYn7TL7JoRGk9Urd_Ascq8', 'AchsiVtJPXtMlk6TNuWNsxU', 'AUSn9WjaASJPkL_C6cgwpX0', 'AR.Z6i8PcpxJn1dO6uluoqQ', 'AZH07dLG1ipAtF3nHCgCg78', 'AUM4.kCHV1JNvAlUA4H2804', 'AUa3zEwrpphPs8jN9C._B3M', 'AQl37D2ZcVpPju3NOVZbZUA', 'Ab_nasfKlY5NjCU6ERGegO8', 'Ab_R5islS1RCvf13ftHkakw', 'ASRfqAbMowRJl6BboScc0UI', 'AWdHKCmapIVHmLqLPdWe0Lk', 'AfwjsFgym8dDtvfEwJDfdLA', 'ASxd.Ww0WeNMvfYQA9kS8yc', 'Aa7b87HBpLVOr_sxEYS2Hus', 'AQD9B1oZSrJAlGDfdPRFbHQ', 'AcS6w6nRJxhPrhJ38PQANY0', 'Af3aRD1xqM9JrJFNXemjYPQ', 'AYGRmSSXW6NIhyPI8wFpfoY', 'AUJKAt_NBwJCgtWJftU63AA', 'Ae_vkGYDlW5Ag9F93BRZGW8', 'ATKkK8N2gvBKgoLcSyCQhvE', 'AZv8arSzluJGhe2bhxpUd5A', 'ATSbLtcljRVNgEgaZLdZx50', 'AS7Obx4zEd5LnlJOsH089eA', 'AYaezAvr1wVGrQ0sWfMdmeM', 'AWxl8wgXCSNDnl3eSk5VDfw', 'AVFyvrebBQVIncmr7hgAr5U', 'ASCarWwCIBFMgirePQq_75U', 'AYKbcOMlUspHvAG9cbCOiPc', 'Aa1OAVNXOc5FtQUyBHhGm4M', 'AZbfyx3Q6H5JtgxmVwtK2YA', 'AavVPBoddS5FjfEgjMaplbI', 'ARXLPLX8NFJLu5Zp_0Lj2vA', 'AVXgu6OEwWRAsx_BJo9atLg', 'AaXVLtwyGDlIj8VfmnSdbFQ', 'AXTRTlzgPBpNkpnXjVFqmgQ', 'AWVBcyH.s4VBiSIXd.WAmSk', 'AWZsdtlrzN5CgcV6FkhzlNc', 'AbiKZWM2euVFiVtCyS62e9k', 'AUn0Rxm93YNHvDSu_KtmgDU', 'AZdEukjY4XJMqotv9BeM66c', 'AbJAuTmDx9hNuukJ2g_5AEw', 'AQAeGwGCGkNLhviiI_r1o1s', 'AdAzD9FQflxPjKO0Tk3U2a8', 'ASSGKgUE7EBMtvNXXIBqykU', 'AXMhekO6bTxJg3_sYNcqF6o', 'AYRimkQynQxDtod0TJ1FBVk', 'AW5vxQzoUiVPn5H3X5DkCJ8', 'ATwpV1Mm.2ROs53KXUjQOU0', 'AdEvFioCCQ1Ij.qo3bu8JGk', 'Abe1SvevEnhBkZgWmPlojOs', 'AQoYvBa9H5xKsag.prhxw.U', 'AeBoUjX2p4JKnAA.1w34XDY', 'AexxgbS95BdGjzW2UvsLvaI', 'ATflUMs7_xRDpcqW1noe6oU', 'Abu1qtrUEk5HoQtiA2O7cPo', 'AQTIAwgIxKxCmDaHf5NYF6I', 'AYBfimABkBBFl.T7SCGn23w', 'AXcygrcBjatPse1D.1BPjeI', 'ASeJLZVfT3VDjbWyD5ylW2s', 'AZVVIu_DQV5IpytYDLcMBNs', 'AQ.yoiSAjgVDg.CgPfGJhnI')";
				//strInfoSQL = "SELECT count(SI_ID) from CI_INFOOBJECTS WHERE SI_KIND in ('CrystalReport') and CUID IN ('AeTFFkuEtPBBnhCwVr22HpM', 'Adxj_0Jo1rNNowGx.j8bBiI')";
			}
			else  //////////UNCOMMENT AFTER GO LIVE
				//strInfoSQL = "SELECT TOP " + strCount + " SI_ID FROM CI_INFOOBJECTS WHERE SI_KIND in ('CrystalReport') and SI_INSTANCE = 0 and SI_CREATION_TIME >= '" + Helper.formatDateBO(maxCreationDate) + "'";
				strInfoSQL = "SELECT TOP " + strCount + " SI_ID FROM CI_INFOOBJECTS WHERE SI_KIND in ('CrystalReport') and SI_INSTANCE = 0 and SI_ID > " + maxid;
			//strInfoSQL = "SELECT TOP " + strCount + " SI_ID FROM CI_INFOOBJECTS WHERE SI_KIND in ('CrystalReport') and SI_INSTANCE = 0 and SI_ID =52944";

			///////JUST TEMPORARY TO DEBUG THE SERVER INFO
			//strInfoSQL = "SELECT TOP " + strCount + " SI_ID FROM CI_INFOOBJECTS WHERE SI_KIND in ('CrystalReport') and SI_INSTANCE = 0 and CUID in ('AcDDPxnvzNdFojPPaCIbS1g','AWmJ2OLgU.BNoN_Fs8Y90AE','AcetHSzh099Ni9S0jz8Zmlc','AQ7Zlou0yRlIuX1UC.RUqC4','AVw0yTztr39PooaEj.yYDck','AXx1oJSMg99JhnEjA2RcX1E','AR2ZVHHIuVVJgRPRiQo0_uk')";

			//''***************PRODUCTION CODE***********UNCOMMENT AFTER TEST
			System.out.println("line 241 " + strInfoSQL);

			colInfoObjects = (IInfoObjects)objInfoStore.query(strInfoSQL);

			if (colInfoObjects.size() > 0)  {
				System.out.println("We do have objects in collection");		

				//Connection con=null;
				//Class.forName("oracle.jdbc.driver.OracleDriver");
				//System.out.println("oracle driver loaded in load_stg1_Crystal_Reports()");
				//con=DriverManager.getConnection("jdbc:oracle:thin:@dlxkrbodb1.lex.lexmark.com:1750:edr1den1","botrack","********");
				//Statement s=con.createStatement();

				/*String driver = "mssql.jdbc.driver.SQLDriver";
			String url = "jdbc:sqlserver://DASHWBODB012;databaseName=EDR1DEN1;integratedSecurity=true;";

			ConnectMSSQLServer connServer = new ConnectMSSQLServer();
			connServer.dbConnect("jdbc:sqlserver://DASHWBODB012", "BOTRACK", "********");
			Connection conn = DriverManager.getConnection("jdbc:sqlserver://DASHWBODB012", "BOTRACK", "********");

			Statement stmt = conn.createStatement() ;*/

				//'System.out.println("creation time  " + objInfoObject2.Properties.Item("SI_CREATION_TIME").getValue)

				//oReportAppFactory = (IReportAppFactory)objEnterpriseSession.getService("", "RASReportService");
				System.out.println("collection size is " + colInfoObjects.size());
				colCount = colInfoObjects.size();
				for (int k =0; k < colCount; k++)
				{
					System.out.println("iterator, k, is now " + k);
					objInfoObject = (IInfoObject) colInfoObjects.get(k);
					//'Record all the si_ids discovered by the query to BOXI
					msSQLInsertString = "insert into TEMP40_P_ALLRPTIDS (report_id) values (" + objInfoObject.getID() + ")";
					System.out.println(msSQLInsertString);
					//s.execute(msSQLInsertString); 

					try {
						//Temporary comment till done testing rest of function - 4/16/2010
						Helper.runMSSQLInsertQuery(msSQLInsertString);
						System.out.println("Insert to TEMP40_P_ALLRPTIDS done");
					}
					catch (Exception ex){
						System.out.println(ex.getMessage() + " " + ex.getStackTrace());
						Helper.sendmail("Error with insert for report id " +  objInfoObject.getID(), "", "reporting@lexmark.com", "bishopp@lexmark.com");
						Helper.runMSSQLInsertQuery("insert into STG1_BO4_TRACKER_ERRORS (ERROR_MESSAGE, LOAD_DT) VALUES ('" + objInfoObject.getID() + " " + ex.getMessage() + " " + ex.getStackTrace() + " " + EnterpriseEnv + "', GETDATE())");

					}

					//'throws an error when I let it just rip through the query result.
					strInfoSQL = "Select * From CI_INFOOBJECTS Where SI_INSTANCE=0 and SI_ID =" + objInfoObject.getID();
					System.out.println(strInfoSQL);
					colInfoObjects2 = (IInfoObjects)objInfoStore.query(strInfoSQL);
					int colCount2 = colInfoObjects2.size();

					for (int j =0; j < colCount2; j++)
					{

						objInfoObject2 = (IInfoObject)colInfoObjects2.get(j);
						//IReportAppFactory sometimes runs slow or gets stuck
						//ReportClientDocument rcd = null ;
						/*try {
							// open the report
							//rcd = rptAppFactory.openDocument( (IInfoObject)colInfoObjects2.get(j), 0, Locale.ENGLISH ) ;
							rcd = rptAppFactory.openDocument( (IInfoObject)colInfoObjects2.get(j), 0, Locale.US ) ;
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

								//Get the password info.  Note: the password will be equal to ******** if it is present
								//useOriginalDS = silogonProperties.getProperty("SI_USE_ORIGINALDS").toString();
								useOriginalDS = silogonProperties.getProperty("SI_USE_ORIGINALDS").getValue().toString();
								password = silogonProperties.getProperty("SI_PASSWORD").toString();
								customPassword = silogonProperties.getProperty("SI_CUSTOM_PASSWORD").toString();
								System.out.println("Use Original DS is " + useOriginalDS);
								System.out.println("Password is " + password);
								System.out.println("Custom Password is " + customPassword);

								//Get the SI_DB value - has universe name for CR4E
								siDB = silogonProperties.getProperty("SI_DB").toString();
								System.out.println("siDB is " + siDB);

								//Get the server type value
								siServerType = silogonProperties.getProperty("SI_SERVER_TYPE").toString();
								if (siServerType.equals("crdb_odbc")) {
									datasourceType = "Database";
								}
								else if (siServerType.equals("crdb_jdbc")) {
									datasourceType = "Database";										
								}
								else if (siServerType.equals("crdb_dsl")) {
									datasourceType = "unx";
								}
								else if (siServerType.equals("crdb_dao")) {
									datasourceType = "Access";										
								}
								else if (siServerType.equals("crdb_ado")) {
									datasourceType = "MS SQL Server";										
								}
								else if (siServerType.equals("crdb_oracle")) {
									datasourceType = "Oracle";										
								}
								else if (siServerType.equals("crdb_opensql")) {
									datasourceType = "ECC";										
								}
								else if (siServerType.equals("crdb_bics")) {
									datasourceType = "BICS";	
									//I could use SI_DB to get the Bex query name, but then I'd have to do it for all the Bex queries
								}



							}
						}
						catch (Exception ex){
							System.out.println("Error getting logon properties " + ex.getMessage() + " " + ex.getStackTrace());
						}

						//Get the SI_DSL_UNIVERSE - good for CR4E that use universes
						try 
						{
							//I'll store the DSL universe id (.unx) in the same field as the .unv to make joins easier
							siDSLUniverseProps = (IProperties) objInfoObject2.properties().getProperty("SI_DSL_UNIVERSE").getValue();
							siDSLUniverseStr = (String)siDSLUniverseProps.getProperty("1").getValue();
							datasourceType="unx";
							//Look up the SI_ID for the SI_DSL_UNIVERSE property, which here is a cuid.
							siDSLUniverseInt = Helper.getSI_ID(objEnterpriseSession, siDSLUniverseStr);
						}
						catch (Exception ex) {

						}

						//This doesn't work in BI 4:
						/*IReport oReport = (IReport)colInfoObjects2.get(j);						
							ISDKList dbLogons = oReport.getReportLogons();

							if (oReport.isLogonNeeded()) {
								System.out.println("dblogons is " + dbLogons.size());  
								System.out.println("getting logon info...");
								for (int m = 0; m < dbLogons.size() && m<2; m++) {
									System.out.println("iterator, m, is " + m);

									IReportLogon dbLogon = (IReportLogon)dbLogons.get(m);
									server[m].equals(dbLogon.getCustomServerName());
									System.out.println("server name is " + dbLogon.getCustomServerName());
									//server[m] = dbLogon.getCustomDatabaseName();
									//System.out.println("server name is " + dbLogon.getCustomDatabaseName());
									System.out.println("server name is " + server[m]);

									//Set the db logon credentials to be the same for each db connection.

								}

								try {
									System.out.println("Did this line run?"); //yes
									if (server[0].length() > 0) {
										System.out.println("Did this line run?");  //no, when the dbLogons size is 0, this doesn't run
										server1 = server[0];
										server2 = server[1];
									}
									else {
										server1 = "none found";
										server2 = "none found";
									}

								}
								catch (Exception ex)
								{
									System.out.println("exception detected " + ex.getMessage() + " " + ex.getStackTrace());
									//throw new RuntimeException(ex);
								}
							}*/


						//cookiecrumbs = Helper.BuildCookieTrail(objEnterpriseSession, objInfoObject2.getParentID(), "");  //even with extra return variables, this just doesn't work
						//cookiecrumbs = Helper.getFolderPath1(objEnterpriseSession, objInfoObject2.getParentID(), "");

						Helper crumbs = new Helper();
						cookiecrumbs = crumbs.getFolderPath1(objEnterpriseSession, objInfoObject2.getParentID(), "");

						//cookiecrumbs = Helper.getMyString("12345678");

						System.out.println("Cookiecrumb trail is " + cookiecrumbs);
						System.out.println("Cookiecrumb length is " + cookiecrumbs.length());

						if (cookiecrumbs.length() > 0) {
							//'If the cookiecrumbs ends up null, then it was an Inbox document,
							//'which I don't want.
							System.out.println("cookiecrumbs length > 0");

							if (cookiecrumbs.contains("\\SI\\")){
								sourceSystem = "SI";
							}
							else
								sourceSystem = "UK";  //"UK" = "unknown"


							System.out.println("objInfoObject2 id:  " + objInfoObject2.getID());
							/*System.out.println("server1 length is " + server1.length());
							if (server1.length() > 0) {
							}*/

							try
							{
								//Retrieve the Business View properties for the current InfoObject
								//int numBusViews = ((IInfoObject)objInfoObject2.getProcessingInfo().properties().getProperty("SI_BUSINESS_VIEW_INFO").getValue()).getProperty("SI_TOTAL").getValue();


								//BVcount = (((IProperties)objInfoObject2.getProcessingInfo().properties().getProperty("SI_BUSINESS_VIEW_INFO").getValue()).getProperty("SI_TOTAL").getValue());
								IProperties oBusViewInfo = (IProperties)objInfoObject2.getProcessingInfo().properties().getProperty("SI_BUSINESS_VIEW_INFO").getValue();
								//Retrieve the number of Business views to the report
								int numBusViews =((Integer)oBusViewInfo.getProperty("SI_TOTAL").getValue()).intValue();

								System.out.println("NumBusViews:  " + numBusViews);
								if (numBusViews > 0) {
									//businessViewNm = objInfoObject2.getProcessingInfo().properties().getProperty("SI_BUSINESS_VIEW_INFO").Properties.Item("1").getValue;
									//businessViewNm = (String)oBusViewInfo.getProperty("1").getValue();
									//businessViewNm = (String)oBusViewInfo.getProperty(0).getValue();
									//businessViewNm = (((IInfoObject)objInfoObject2.getProcessingInfo().properties().getProperty("SI_BUSINESS_VIEW_INFO").getValue()).getProperty("SI_TOTAL").getValue()).getProperty("1").getValue;
									//businessViewNm = oBusViewInfo.getProperty(0).getValue().toString();
									//businessViewNm = oBusViewInfo.getProperty("SI_NAME").getValue().toString();
									businessViewNm = oBusViewInfo.getProperty(1).getValue().toString();
									System.out.println("BV cuid is " + businessViewNm);
									datasourceType = "BV";
								}	
							}
							catch (Exception ex)
							{
								System.out.println("exception detected " + ex.getMessage() + " " + ex.getStackTrace());
								//throw new RuntimeException(ex);
							}




							try
							{
								//'Get the local file path if it exists
								localFilePath = (String)objInfoObject2.properties().getProperty("SI_LOCAL_FILEPATH").getValue();
								//'Get the original file name
								localFilePath = (String)objInfoObject2.properties().getProperty("SI_LOCAL_FILEPATH").getValue();
								localFilePath.indexOf("~");

								originalFileNm = (localFilePath.substring(localFilePath.indexOf("~")));
								System.out.println("original file name is " + originalFileNm);
							}
							catch (Exception ex)
							{
								//I don't care if I get this value or not.
								//throw new RuntimeException(ex);
							}


							try
							{
								si_owner = objInfoObject2.properties().getProperty("SI_OWNER").getValue().toString();
							}
							catch (Exception ex)
							{
								//throw new RuntimeException(ex);
							}

							try
							{
								//creationTime.setTime((Date) objInfoObject2.properties().getProperty("SI_CREATION_TIME").getValue());
								//creationTime.setTime(arg0)((Calendar) (IInfoObject) objInfoObject2.properties().getProperty("SI_CREATION_TIME").getValue());
								//IProperty parentIDs=((IInfoObject)infoObjs.get( index )).properties().getProperty("SI_CREATION_TIME");
								//creationTime.setTime((Date)((IInfoObject)objInfoObject2.get( 0 )).properties().getProperty("SI_CREATION_TIME");
								//creationTime.setTimeInMillis(((java.util.Date) objInfoObject2.properties().getProperty(objInfoObject2.getID()).getValue()).getTime()); 
								//creationTime = (Calendar) objInfoObject2.properties().getProperty("SI_CREATION_TIME").getValue();
								//java.util.Date cannot be cast to java.util.Calendar

								//////////This works, but it would be a pain to convert the resulting string to a date//////////
								//String createTime = objInfoObject2.properties().getProperty("SI_CREATION_TIME").getValue().toString();
								//System.out.println("creation time is " + creationTime);
								//System.out.println("creation time is " + createTime);
								//////////////////if this works, convert the string to an oracle date

								//Got this from D:\Documents and Settings\bishopp\My Documents\BOXI\Code Examples\xi4_boejava_dg_en\html\topic15.html
								IProperties properties = objInfoObject2.properties();
								IProperty creationTimeProperty = properties.getProperty(CePropertyID.SI_CREATION_TIME);
								//String value = (String) creationTimeProperty.getValue();
								creationTime.setTime ((Date) creationTimeProperty.getValue());  //This works
								System.out.println("creation time is " + creationTime.getTime());
							}
							catch (Exception ex)
							{
								ex.printStackTrace();
								System.out.println(" for report id: " +objInfoObject2.getID() + " " + ex.getMessage() + " " + ex.getStackTrace());
							}

							try
							{
								updateTime.setTime((Date) objInfoObject2.properties().getProperty("SI_UPDATE_TS").getValue());
								System.out.println(updateTime);

							}

							catch (Exception ex)
							{
								ex.printStackTrace();
								System.out.println(" for report id: " +objInfoObject2.getID() + " " + ex.getMessage() + " " + ex.getStackTrace());
							}


							try
							{
								//si_doc_common_connection = (int)objInfoObject2.properties().getProperty("SI_DOC_COMMON_CONNECTION").getID();

								IProperties cxnProperties = (IProperties)objInfoObject2.properties().getProperty("SI_DOC_COMMON_CONNECTION").getValue();
								//IProperty creationTimeProperty = properties.getProperty(CePropertyID.SI_CREATION_TIME);

								IProperty  docCommonConnProp = (IProperty)cxnProperties.get("1");
								si_doc_common_connection = (Integer) docCommonConnProp.getValue();

								System.out.println("si_doc_common_connection is " + si_doc_common_connection);
							}
							catch (Exception ex) {

							}

							si_files = (IProperties) objInfoObject2.properties().getProperty("SI_FILES").getValue();
							si_path = (String) si_files.getProperty("SI_PATH").getValue();
							si_file1 = (String) si_files.getProperty("SI_FILE1").getValue();


							//msSQLInsertString = "INSERT INTO " + destTblNm + " (SI_ID, SI_PATH, SI_FILE1, REPORT_NM, REPORT_FILE_PATH, DATA_SOURCE_NM, DATA_SOURCE2_NM, DYNAMIC_PICK_LIST_NM, LOAD_DT, SI_CREATION_TIME, SI_UPDATE_TS, SOURCE_SYSTEM, CUID, SI_BUSINESS_VIEW_INFO, SI_LOCAL_FILEPATH, ORIGINAL_FILE_NM, SI_PARENT_FOLDER_CUID, SI_OWNER, SI_KIND, SI_USE_ORIGINALDS, SI_PASSWORD, SI_CUSTOM_PASSWORD, SI_DOC_COMMON_CONNECTION, SI_DB, DATASOURCE_TYPE, SI_DSL_UNIVERSE, SI_SERVER_TYPE) VALUES (" + objInfoObject2.getID() + ", '" + Helper.safeSQL(si_path) + "', '" + Helper.safeSQL(si_file1) + "', '" + Helper.safeSQL(objInfoObject2.getTitle()) + "', '" + Helper.safeSQL(cookiecrumbs) + "','" + server1 + "', '" + server2 + "','See STG1_MIGR_RPT_PICK_LIST_REL',sysdate, to_date('" + Helper.msSQLDateTime(creationTime) + "','DD-MM-YYYY HH24:MI:SS'), to_date('" + Helper.msSQLDateTime(updateTime) + "','DD-MM-YYYY HH24:MI:SS'), '" + sourceSystem + "','" + objInfoObject2.getCUID() + "','" + Helper.safeSQL(businessViewNm) + "',' " + Helper.safeSQL(localFilePath) + "','" + originalFileNm + "', '" + objInfoObject2.properties().getProperty("SI_PARENT_FOLDER_CUID").getValue() + "','" + si_owner + "', '" + objInfoObject2.getKind() + "', '" + useOriginalDS + "','" + password + "','" + customPassword + "', " + si_doc_common_connection + ", ' " + siDB + "', '" + datasourceType + "', " + siDSLUniverseInt + ",'" + siServerType + "')";
							msSQLInsertString = "INSERT INTO " + destTblNm + " (SI_ID, SI_PATH, SI_FILE1, REPORT_NM, REPORT_FILE_PATH, DATA_SOURCE_NM, DATA_SOURCE2_NM, DYNAMIC_PICK_LIST_NM, LOAD_DT, SI_CREATION_TIME, SI_UPDATE_TS, SOURCE_SYSTEM, CUID, SI_BUSINESS_VIEW_INFO, SI_LOCAL_FILEPATH, ORIGINAL_FILE_NM, SI_PARENT_FOLDER_CUID, SI_OWNER, SI_KIND, SI_USE_ORIGNALIDS, SI_PASSWORD, SI_CUSTOM_PASSWORD, SI_DOC_COMMON_CONNECTION, SI_DB, DATASOURCE_TYPE, SI_DSL_UNIVERSE, SI_SERVER_TYPE) VALUES (" + objInfoObject2.getID() + ", '" + Helper.safeSQL(si_path) + "', '" + Helper.safeSQL(si_file1) + "', '" + Helper.safeSQL(objInfoObject2.getTitle()) + "', '" + Helper.safeSQL(cookiecrumbs) + "','" + server1 + "', '" + server2 + "','See STG1_MIGR_RPT_PICK_LIST_REL',GETDATE(), ('" + Helper.msSQLDateTime(creationTime) + "'), ('" + Helper.msSQLDateTime(updateTime) + "'), '" + sourceSystem + "','" + objInfoObject2.getCUID() + "','" + Helper.safeSQL(businessViewNm) + "',' " + Helper.safeSQL(localFilePath) + "','" + originalFileNm + "', '" + objInfoObject2.properties().getProperty("SI_PARENT_FOLDER_CUID").getValue() + "','" + si_owner + "', '" + objInfoObject2.getKind() + "', '" + useOriginalDS + "','" + password + "','" + customPassword + "', " + si_doc_common_connection + ", ' " + siDB + "', '" + datasourceType + "', " + siDSLUniverseInt + ",'" + siServerType + "')";


							//************************************** TEST INSERT STATEMENT - TESTS DATES COMMENT WHEN DONE ********************
							//msSQLInsertString = "INSERT INTO testLoadingReports (SI_ID, SI_CREATION_TIME, ENVIRONMENT) VALUES (" + objInfoObject2.getID() + ", to_date('" + objInfoObject2.properties().getProperty("SI_CREATION_TIME").getValue() + "','MM/DD/YYYY HH12:MI:SS AM'), 'ITG')";

							//***********************************************************************************************

							//oraInserter.CommandText = msSQLInsertString;
							System.out.println(msSQLInsertString);
							//'Comment out just to see if the getCEReportParams method is working
							try
							{
								//stmt.execute(msSQLInsertString);
								Helper.runMSSQLInsertQuery(msSQLInsertString);
							}
							catch (Exception ex)
							{
								ex.printStackTrace();
								System.out.println(" for report id: " + objInfoObject2.getID() + " " + ex.getMessage() + " " + ex.getStackTrace());
								//'Records the skipped si_id
								msSQLInsertString = "insert into temp_p_skippedrptids (report_id) values (" + objInfoObject2.getID() + ")";
								//s.execute(msSQLInsertString);
								Helper.runMSSQLInsertQuery(msSQLInsertString);
							}
							originalFileNm = "";
							localFilePath = "";
							businessViewNm = "";
							si_doc_common_connection = 0;
							datasourceType = "";

							//''Then find all the dynamic pick list prompts and store in the Rel table
							//'9/13/2006:  Not sure this is necessary because we already know which ones have dynamic pick list from CE
							//'oraInsertString2 = getCEReportParams(objEnterpriseSession, objCERpt, objInfoObject2.getID())
							//'If oraInsertString2 <> Nothing Then
							//'    System.out.println(oraInsertString2)
							//'    oraInserter.CommandText = oraInsertString2
							//'    oraInserter.ExecuteNonQuery()
							//'End If
							server1 = "not found in CMS";
							server2 = "not found in CMS";

							//s.close();
							//con.close();

						}
					}
				}

				objSessionMgr = null;
				//objEnterpriseSession = Nothing;
				objInfoStore = null;
				colInfoObjects = null;
				objInfoObject = null;
				colInfoObjects2 = null;
				objInfoObject2 = null;
				//s.close();
				//con.close();
			}
			// Clean up the Enterprise Session.
			if(objEnterpriseSession != null) {
				try {
					objEnterpriseSession.logoff();

				} catch(Exception e_ignore_in_cleanup) {}
			}
		}
		catch (Exception ex)
		{
			System.out.println(ex.getMessage());
			ex.printStackTrace(System.out);
			Helper.runMSSQLInsertQuery("insert into STG1_BO4_TRACKER_ERRORS (ERROR_MESSAGE, LOAD_DT) VALUES ('" + ex.getMessage() + " " + ex.getStackTrace() + " " + EnterpriseEnv + "', GETDATE())");
			//'sendmail("Logon error", ex.getMessage() + " " + ex.getStackTrace)
			//Helper.sendmail("bishopp@lexmark.com", "bishopp@lexmark.com, amarabes@lexmark.com,ajpatel@lexmark.com,msankar@lexmark.com,ajain@lexmark.com", "Logon error - load_stg1_migr_XI_reports(String, String, String, String, String) - " + EnterpriseEnv, ex.getMessage() + " " + ex.getStackTrace());
			Emailer.sendEmail("bishopp@lexmark.com", "bishopp@lexmark.com", "Logon error - load_stg1_Crystal_Reports(String, String, String, String, String) - " + EnterpriseEnv, ex.getMessage() + " " + ex.getStackTrace(), null);

		}
	}

	public static void load_stg1_BO_runhist (String EnterpriseEnv, String login, String pswd,String auth, String destTblNm, String env){
		IEnterpriseSession objEnterpriseSession;
		//IInfoStore objInfoStore;
		IInfoObjects colInfoObjects;
		IInfoObject objInfoObject;
		IInfoObject objInfoObject2;
		String errorMsg = "";
		String cookieCrumbs;
		int count;
		String si_owner = "";
		Calendar runDate;
		Calendar startTime= Calendar.getInstance();
		Calendar endTime= Calendar.getInstance();
		Calendar creationTime = Calendar.getInstance();
		Calendar updateTS = Calendar.getInstance();
		Calendar expiryTime = Calendar.getInstance();
		double theInterval = 0;
		Calendar startDate;
		Calendar endDate;
		String dtToday;
		String msSQLInsertString;
		String maxID = "0";
		String logon1 = "unknown";
		String logon2 = "unknown";
		String pswd1 = "unknown";
		String pswd2 = "unknown";
		String datasrc1 = "not found in CMS";
		String datasrc2 = "not found in CMS";
		int numPrompts = 0;
		Calendar maxEndDate;
		String destination = "";
		String outputFile = "";
		String distributionList = "";
		String filepath = "not set";
		String strInfoSQL;
		Calendar maxCreationDate;
		//java.sql.Date maxCreationDate;
		int i = 0;
		IFiles ifiles;
		String scheduleStatus = "999"; //means is not yet known.  I made this up because this is not a valid value.  Tells me it's not getting set from the recordset.
		IProcessingInfo processingInfo;
		IReport iReport;
		String server ="";
		String server1 = "";
		String server2 = "";
		IProperty aggCount;
		String strCount="0";
		int colCount = 0;
		Boolean si_format_export_allpages;
		String siFormatExportAllpages;
		String paramValues = "";
		//		IWebiPrompts iWebiPrompts;
		IPrompts webiPrompts;
		IReport oReport;
		String submitter = "";
		String machineUsed = "";
		String parentFolderCuid = "";
		String siOwner = "";
		String testString = "";
		int schedStatus;
		String siKind = "";
		String cuid = "";
		String reportCuid = "";
		int instanceID = 0;
		String errorMessage = "no error";
		//int numLogins = 0;
		String numLogins = "0";
		String errorCategory;

		try 
		{
			ISessionMgr  objSessionMgr = CrystalEnterprise.getSessionMgr();
			objEnterpriseSession = objSessionMgr.logon(login, pswd, EnterpriseEnv, auth);    
			IInfoStore objInfoStore = (IInfoStore) objEnterpriseSession.getService("InfoStore");

			maxID = Helper.getMaxID("select max(instance_id) maxID from " + destTblNm);
			System.out.println("maxID is " + maxID);
			//maxCreationDate = Helper.getMaxLoadDt("select max(si_creation_time) maxLoadDate from " + destTblNm);
			//System.out.println("Max creation date is " + maxCreationDate);
			maxEndDate = Helper.getMaxLoadDt("select max(si_endtime) maxLoadDate from " + destTblNm);
			System.out.println("Max end date is " + Helper.msSQLDateTime(maxEndDate));

			//4/26/2013:  In Enterprise, Analysis reports, SI_KIND='MDAnalysis', has objects where SI_INSTANCE=1.  These error out.
			//Added a filter to prevent them from showing up in the recordset.
			if (maxID == "0" || maxID == null) {
				//Is a first-time load
				strInfoSQL = "SELECT count(SI_ID) from CI_INFOOBJECTS WHERE SI_INSTANCE = 1 and si_recurring = 0 and SI_KIND not in ('MDAnalysis','XL.XcelsiusEnterprise')";
			}
			else
				//is an incremental load
				//***************** These are to make up for lost loads due to the very large number of failures, about 20K per day.  By May 5, 2014, the query couldn't complete on the BOE side.  BO team deleted the extra failures, and now we get a Java heap error due to trying to load 72K rows at once.

				//strInfoSQL = "SELECT count(SI_ID) from CI_INFOOBJECTS WHERE SI_INSTANCE = 1 and SI_KIND != 'MDAnalysis' and si_recurring = 0 and si_endtime >= '2014.05.05.18.56.23' and si_endtime < '2014.05.06.00.00.00'";
				//strInfoSQL = "SELECT count(SI_ID) from CI_INFOOBJECTS WHERE SI_INSTANCE = 1 and SI_KIND != 'MDAnalysis' and si_recurring = 0 and si_endtime >= '2014.05.06.00.00.00' and si_endtime < '2014.05.06.12.00.00'";
				//strInfoSQL = "SELECT count(SI_ID) from CI_INFOOBJECTS WHERE SI_INSTANCE = 1 and SI_KIND != 'MDAnalysis' and si_recurring = 0 and si_endtime >= '2014.05.06.12.00.00' and si_endtime < '2014.05.07.12.00.00'";


				//*****************PRODUCTION CODE - 5/15/2014:  UNCOMMENT AFTER PRIOR LOADS****************

				//strInfoSQL = "SELECT count(SI_ID) from CI_INFOOBJECTS WHERE SI_INSTANCE = 1 and SI_KIND != 'MDAnalysis' and si_recurring = 0 and si_endtime >= '" + Helper.formatDateBO(maxEndDate) + "'" ;

				//*****************PRODUCTION CODE - 5/15/2014:  UNCOMMENT AFTER PRIOR LOADS****************
				strInfoSQL = "SELECT count(SI_ID) from CI_INFOOBJECTS WHERE SI_INSTANCE = 1 and SI_KIND not in ('MDAnalysis','XL.XcelsiusEnterprise') and si_recurring = 0 and si_endtime >= '" + Helper.formatDateBO(maxEndDate) + "'" ;
			//strInfoSQL = "SELECT count(SI_ID) from CI_INFOOBJECTS WHERE SI_INSTANCE = 1 and si_recurring = 0 and si_creation_time >= '" + Helper.formatDateBO(maxCreationDate) + "' and si_schedule_status=3" ;

			System.out.println(strInfoSQL);

			colInfoObjects = (IInfoObjects)objInfoStore.query(strInfoSQL);
			//objInfoObject = (IInfoObject)colInfoObjects.get(i); 

			objInfoObject = (IInfoObject)colInfoObjects.get(0);

			//The VB code used the collection here, but in Java, properties() isn't an available function, so I am trying to get this via InfoObject

			//System.out.println(objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").properties().getProperty("SI_ID"));
			System.out.println(objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").getID());//shows the ID, not the count!
			//count = objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").properties().getProperty("SI_ID");
			//count = objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").getID();
			aggCount = objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT");

			//count = objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").getID();

			strCount = ((IProperties)objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").getValue()).getProperty("SI_ID").getValue().toString();

			System.out.println("Record count is " + strCount);

			if (Integer.valueOf(strCount) > 5000){
				strCount = "5000";
			}
			if (maxID == "0" || maxID == null) {
				//*****************PRODUCTION CODE - 5/15/2014:  UNCOMMENT AFTER PRIOR LOADS****************
				strInfoSQL = "SELECT TOP " + strCount + " * FROM CI_INFOOBJECTS WHERE  SI_INSTANCE = 1 and SI_KIND not in ('MDAnalysis','XL.XcelsiusEnterprise') and si_recurring=0";
				//*****************PRODUCTION CODE - 5/15/2014:  UNCOMMENT AFTER PRIOR LOADS****************


				///**********JUST TEMPORARY FOR TESTING*************
				//strInfoSQL = "SELECT TOP 100 * FROM CI_INFOOBJECTS WHERE  SI_INSTANCE = 1 and si_recurring=0";
				//strInfoSQL = "SELECT TOP 100 si_id, CUID, si_name, si_submitter, si_starttime, si_endtime, si_creation_time, si_schedule_status, si_kind, si_parent_cuid FROM CI_INFOOBJECTS WHERE  SI_INSTANCE = 1 and si_recurring=0";
				//strInfoSQL = "SELECT TOP 100 * FROM CI_INFOOBJECTS WHERE  SI_INSTANCE = 1 and si_recurring=0";
				//strInfoSQL = "SELECT TOP 100 * FROM CI_INFOOBJECTS WHERE  SI_ID = 1751209";
			}
			else
				//strInfoSQL = "SELECT TOP " + strCount + " SI_ID FROM CI_INFOOBJECTS WHERE SI_KIND in ('CrystalReport') and SI_INSTANCE = 0 and SI_CREATION_TIME >= '" + Helper.formatDateBO(maxCreationDate) + "'";

				//strInfoSQL = "SELECT  TOP " + strCount + " *  from CI_INFOOBJECTS WHERE SI_INSTANCE = 1 and SI_KIND != 'MDAnalysis' and si_recurring = 0 and si_endtime >= '2014.05.05.18.56.23' and si_endtime < '2014.05.06.00.00.00'";
				//strInfoSQL = "SELECT TOP " + strCount + " * FROM CI_INFOOBJECTS WHERE  SI_INSTANCE = 1 and SI_KIND = 'Webi' and si_recurring=0 and SI_ENDTIME >= '" + Helper.formatDateBO(maxEndDate) + "'";

				//*****************PRODUCTION CODE - 5/15/2014:  UNCOMMENT AFTER PRIOR LOADS****************
				////strInfoSQL = "SELECT TOP " + strCount + " * FROM CI_INFOOBJECTS WHERE  SI_INSTANCE = 1 and si_recurring=0 and SI_CREATION_TIME >= '" + Helper.formatDateBO(maxCreationDate) + "'";
				strInfoSQL = "SELECT TOP " + strCount + " * FROM CI_INFOOBJECTS WHERE  SI_INSTANCE = 1 and SI_KIND not in ('MDAnalysis','XL.XcelsiusEnterprise') and si_recurring=0 and SI_ENDTIME >= '" + Helper.formatDateBO(maxEndDate) + "'";
			//*****************PRODUCTION CODE - 5/15/2014:  UNCOMMENT AFTER PRIOR LOADS****************




			///**********JUST TEMPORARY FOR TESTING*************
			//strInfoSQL = "SELECT  * FROM CI_INFOOBJECTS WHERE SI_ID=942134";
			//strInfoSQL = "SELECT TOP 100 si_id, CUID, si_name, si_submitter, si_starttime, si_endtime, si_creation_time, si_schedule_status, si_kind, si_parent_cuid FROM CI_INFOOBJECTS WHERE  SI_INSTANCE = 1 and si_recurring=0 and SI_ENDTIME >= '" + Helper.formatDateBO(maxEndDate) + "'";
			//strInfoSQL = "SELECT TOP 100 * FROM CI_INFOOBJECTS WHERE  SI_INSTANCE = 1 and si_recurring=0 and SI_ENDTIME >= '" + Helper.formatDateBO(maxEndDate) + "'";
			//strInfoSQL = "SELECT TOP " + strCount + " * FROM CI_INFOOBJECTS WHERE  SI_ID = 1751209";

			//JUST A TEST TO SEE HOW FAILED INSTANCES WORK IN THE CODE
			//strInfoSQL = "SELECT TOP " + strCount + " SI_ID FROM CI_INFOOBJECTS WHERE  SI_INSTANCE = 1 and si_recurring=0 and si_schedule_status=1 and SI_CREATION_TIME >= '" + Helper.formatDateBO(maxCreationDate) + "'";

			System.out.println(strInfoSQL);
			colInfoObjects = (IInfoObjects)objInfoStore.query(strInfoSQL);
			System.out.println("Line 3448 Count of objects in collection: " + colInfoObjects.size());

			if (colInfoObjects.size() > 0){
				/*Connection con=null;
				Class.forName("oracle.jdbc.driver.OracleDriver");
				con=DriverManager.getConnection("jdbc:oracle:thin:@dlxkrbodb1.lex.lexmark.com:1750:edr1den1","botrack","********");
				Statement s=con.createStatement();*/
				//'This records all the instances found on the BOXI system
				colCount = colInfoObjects.size();
				for (int x=0; x <= colCount; x++){
					//msSQLInsertString = "insert into temp_p_allinstances(report_id, instance_id) values (" + objInfoObject.getParentID() + "," + objInfoObject.getID() + ")";
					//System.out.println(msSQLInsertString);
					//s.execute(msSQLInsertString);
					errorMsg = "";  //resets the error message since it won't get overwritten each time.
					paramValues = ""; //resets the paramValues

					objInfoObject = (IInfoObject)colInfoObjects.get(x);
					//oReport = (IReport)colInfoObjects.get(x);  //4/27/2012:  causes error against 3.1 Prod: $Proxy0 cannot be cast to com.crystaldecisions.sdk.plugin.desktop.report.IReport [Ljava.lang.StackTraceElement;@aa559d
					//That's because this is an instance and it will throw this error if the object is not .rpt, which would usually be
					//the case.
					System.out.println("next report, si_id is " + objInfoObject.getID());
					//System.out.println ("si_id is " + objInfoObject.getID());

					try {
						scheduleStatus = objInfoObject.properties().getProperty("SI_SCHEDULE_STATUS").getValue().toString();
						schedStatus = objInfoObject.getSchedulingInfo().getStatus();
						System.out.println("scheduleStatus is " + scheduleStatus);
						System.out.println("schedStatus is " + schedStatus);
					} 
					catch (Exception ex) {
						System.out.println(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getStackTrace());
					}

					//Only do the rest if the schedule status is 1 or 3
					if (scheduleStatus.equals("1") || scheduleStatus.equals("3"))  {
						try {
							IProperties properties = objInfoObject.properties();
							IProperty startTimeProperty = properties.getProperty(CePropertyID.SI_STARTTIME);
							//String value = (String) creationTimeProperty.getValue();
							startTime.setTime ((Date) startTimeProperty.getValue());  //This works
							System.out.println("start time is " + Helper.msSQLDateTime(startTime));
						}
						catch (Exception ex) {
							System.out.println(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getStackTrace());
						}


						try {
							IProperties properties = objInfoObject.properties();
							IProperty endTimeProperty = properties.getProperty(CePropertyID.SI_ENDTIME);
							//String value = (String) creationTimeProperty.getValue();
							endTime.setTime ((Date) endTimeProperty.getValue());  //This works
							System.out.println("end time is " + Helper.msSQLDateTime(endTime));
						}
						catch (Exception ex) {
							System.out.println(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getStackTrace());
						}

						try {
							//if ((scheduleStatus.equals("1")) || (scheduleStatus.equals("3"))) {
							//runDate = (Calendar)objInfoObject.properties().getProperty("SI_STARTTIME").getValue();
							//startTime = runDate;
							//endTime = (Calendar)objInfoObject.properties().getProperty("SI_ENDTIME").getValue();
							//theInterval = 60/(endTime.getTimeInMillis() - startTime.getTimeInMillis())*1000;
							//}
							theInterval = (endTime.getTimeInMillis() - startTime.getTimeInMillis());
							System.out.println("the duration in milliseconds was " + theInterval);
							theInterval = theInterval/1000;
							//theInterval = 60/(endTime.getTimeInMillis() - startTime.getTimeInMillis())*1000;
							System.out.println("the duration in seconds was " + theInterval);
							theInterval = theInterval/60;
							System.out.println("the duration in minutes was " + theInterval);
						}
						catch (Exception ex) {
							System.out.println(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getStackTrace());
						}
						System.out.println("the duration was " + theInterval);

						try {
							//reportCuid.equals(objInfoObject.getParentCUID().toString());
							reportCuid = objInfoObject.getParentCUID().toString();
							System.out.println("Report cuid is " + reportCuid);
						}
						catch (Exception ex) {
							System.out.println(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getStackTrace());
						}


						try {
							//cuid.equals(objInfoObject.getCUID().toString());
							cuid = objInfoObject.getCUID().toString();
							System.out.println("Instance cuid is " + cuid); //watch out for AeVnF4VTRkdFsYYNxvwfFlI, an Analysis object
						}catch (Exception ex) {
							System.out.println(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getStackTrace());
						}


						try {
							instanceID = objInfoObject.getID();
							System.out.println("report instance id is " + instanceID);
						}
						catch (Exception ex) {
							System.out.println(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getStackTrace());
						}



						try {
							//IProperties properties = objInfoObject.properties();
							//IProperty submitterProperty = properties.getProperty(CePropertyID.SI_SUBMITTER);

							//submitter.equals(submitterProperty.toString());  //doesn't work
							//submitter = submitterProperty.toString();


							submitter =objInfoObject.getSchedulingInfo().properties().getProperty("SI_SUBMITTER").getValue().toString();
							System.out.println("submitter " + submitter);
						}
						catch (Exception ex) {
							System.out.println(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getStackTrace());
						}

						try {
							//Got this from D:\Documents and Settings\bishopp\My Documents\BOXI\Code Examples\xi4_boejava_dg_en\html\topic15.html
							IProperties properties = objInfoObject.properties();
							IProperty creationTimeProperty = properties.getProperty(CePropertyID.SI_CREATION_TIME);
							//String value = (String) creationTimeProperty.getValue();
							creationTime.setTime ((Date) creationTimeProperty.getValue());  //This works
							//System.out.println("creation time is " + Helper.msSQLDateTime(creationTime));
							System.out.println("creation time is " + creationTime.getTime());
						}
						catch (Exception ex)
						{
							ex.printStackTrace();
							System.out.println(" for report id: " +objInfoObject.getID() + " " + ex.getMessage() + " " + ex.getStackTrace());
						}

						try {
							//Got this from D:\Documents and Settings\bishopp\My Documents\BOXI\Code Examples\xi4_boejava_dg_en\html\topic15.html
							//IProperties properties = objInfoObject.properties();
							//IProperty expiryTimeProperty = properties.getProperty(CePropertyId.);
							//creationTime.setTime ((Date) creationTimeProperty.getValue());  //This works

							expiryTime.setTime((Date) objInfoObject.getSchedulingInfo().properties().getProperty("SI_ENDTIME").getValue());
							System.out.println("expiration time is " + expiryTime.getTime());
						}
						catch (Exception ex)
						{
							ex.printStackTrace();
							System.out.println(" for report id: " +objInfoObject.getID() + " " + ex.getMessage() + " " + ex.getStackTrace());
						}


						try {
							siKind.equals(objInfoObject.getKind());
							System.out.println("SI_KIND is " + objInfoObject.getKind().toString());
						} 
						catch (Exception ex) {
							System.out.println(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getStackTrace());
						}


						try {
							//destination = objInfoObject.getSchedulingInfo().getDestination().getName();  //--> destination was null, but it can't be!
							//destination = (String)objInfoObject.getSchedulingInfo().properties().getProperty("SI_DESTINATION").getValue();
							//4/26/2013:  In Enterprise, Analysis reports, SI_KIND='MDAnalysis', has objects where SI_INSTANCE=1.  These error out.

							//VB:  destination = objInfoObject.SendToDestination.Name and this works
							ISendable obj = (ISendable)objInfoObject;						
							IDestinationPlugin destinationPlugin = Helper.getDestinationPlugin(objInfoStore, objInfoObject.getKind());
							IDestination odestination = obj.getSendToDestination();	
							//odestination.getName();
							//odestination.setFromPlugin(destinationPlugin);//this was setting the destination to smtp, not getting what it is
							System.out.println("Line 1547: destination is " + odestination.getName());
							destination = odestination.getName();
							System.out.println("destination is " + destination + " for si_id " + objInfoObject.getID());
							//distributionList = Helper.getDistributionList("uslexbcs02", objInfoObject.getID());
							if (destination != null) {
								if (destination.equals("CrystalEnterprise.Smtp")) {
									System.out.println("Line 1653");
									System.out.println("getting email distribution list");
									//distributionList = Helper.getDistributionList("uslexbcs02", objInfoObject.getID());	
									//distributionList = Helper.getDistributionList(EnterpriseEnv, objInfoObject.getID()); //gives an error
									distributionList = Helper.getDistributionList1(objEnterpriseSession,EnterpriseEnv, objInfoObject.getID()); //gives an error
									System.out.println("Line 1657" + distributionList);
								}
								else
									System.out.println("Destination actually is:  " + destination);
							}

						}
						catch(SDKException sdkEx)
						{
							System.out.println("Line 1607 " + sdkEx.getDetail() + " " + sdkEx.getStackTrace());
							Helper.runMSSQLInsertQuery("insert into STG1_BO4_TRACKER_ERRORS (ERROR_MESSAGE, LOAD_DT) VALUES ('" + sdkEx.getMessage() + " " + sdkEx.getStackTrace() + " " + EnterpriseEnv + "', GETDATE())");
						}


						try {
							//get SI_MACHINE_USED, the job server the job ran on
							IProperties properties = objInfoObject.getSchedulingInfo().properties();
							IProperty machineUsedProperty = properties.getProperty(CePropertyID.SI_MACHINE_USED);
							//String value = (String) creationTimeProperty.getValue();
							machineUsed =  machineUsedProperty.toString();
							System.out.println("job server " + machineUsed);
						}
						catch(Exception Ex)
						{
							System.out.println("Line 1607 " + Ex.getMessage() + " " + Ex.getStackTrace());
							Helper.runMSSQLInsertQuery("insert into STG1_BO4_TRACKER_ERRORS (ERROR_MESSAGE, LOAD_DT) VALUES ('" + Ex.getMessage() + " " + Ex.getStackTrace() + " " + EnterpriseEnv + "', GETDATE())");
						}

						try {
							if (objInfoObject.getKind() == "Webi") {
								webiPrompts = (IPrompts)objInfoObject.properties().getProperty("SI_WEBI_PROMPTS").getValue();
								numPrompts = webiPrompts.size();
								logon1 = "n/a";
								logon2 = "n/a";
								server1 = "n/a";
								server2 = "n/a";
								if (numPrompts > 0 ) {
									//Getting the param list might not be possible
									//List paramList = webiPrompts.getReportParameters();
									Object paramList =  webiPrompts.toArray();
								}

							} else {
								//see if this will work for all file types, .rpt, .xls and .pdf

								// First get the Processing Info properties for the InfoObject.
								IProperties boProcessingInfo = (IProperties) objInfoObject.getProcessingInfo().properties();

								// Make sure that there is processing info set for the InfoObject.
								if (boProcessingInfo != null)
								{
									// Get the SI_PROMPTS property bag from the InfoObject's Processing Info
									IProperties siPrompts = (IProperties) boProcessingInfo.getProperty("SI_PROMPTS").getValue();

									// Make sure that there are parameters.
									if (siPrompts != null)
									{
										// Get the number of prompts (the number of parameters)
										numPrompts = ((Integer)siPrompts.getProperty("SI_NUM_PROMPTS").getValue()).intValue();
									}

									// Get the SI_LOGON_INFO property bag
									IProperties logonProperties = (IProperties) boProcessingInfo.getProperty("SI_LOGON_INFO").getValue();
									if (logonProperties != null) {
										IProperties silogonProperties = (IProperties) logonProperties.getProperty("SI_LOGON1").getValue();


										System.out.println("SI_LOGON1 size is " + silogonProperties.size());
										//16, it's true
										numLogins = logonProperties.getProperty("SI_NUM_LOGONS").toString();
										System.out.println("Num logons is " + numLogins);
										//System.out.println("logon1 is " + logon1);
										//Get the db logon credentials 
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



								//////////////ONLY WORKS FOR CRYSTAL REPORTS FORMAT
								/*iReport = (IReport) objInfoObject;  //can't do this if the report is not in CR format

								//Get the username 
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

								List paramList = iReport.getReportParameters(); //getting an error here
								//Line 1607 The operation is not supported [Ljava.lang.StackTraceElement;@12b9f14
								//ORA-00923: FROM keyword not found where expected
								// [Ljava.lang.StackTraceElement;@1c67b54 runMSSQLInsertQuery line 632
								numPrompts = paramList.size();*/
								/////////////////////////////////////////////

							}


							//The number of prompts is stored differently between Crystal Reports and Webi docs
							//Crystal: SI_PROMPTS
							//Webi:  SI_WEBI_PROMPTS
							//Xcelsius:  


						} catch (Exception ex) {
							System.out.println("Logons " + ex.getMessage() + " " + ex.getStackTrace());
							//IF IS EXCEL OUTPUT
							try {
								IExcel eReport = (IExcel)objInfoObject;
								//Get the username 
								//									ISDKList dbLogons = eReport.getReportLogons();
								IProperties allProperty = (IProperties)eReport.properties();
								//IProperties pathProperty = allProperty.getProperties("SI_FILES");
								//IProperty logonProperty = eReport.getProcessingInfo().properties().getProperty("SI_LOGONS")
								IProperties logonProperties = allProperty.getProperties("SI_LOGON_INFO");
								IProperties logon1Properties = logonProperties.getProperties("SI_LOGON1");
								//IProperty userProperty = logon1Properties.getProperty("SI_USER");


								//Get the db logon credentials 
								for (int h = 0; h < logon1Properties.size(); h++) {	
									IReportLogon dbLogon = (IReportLogon)logon1Properties.get(h);
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
								//List paramList = eReport.getReportParameters(); //getting an error here
								//Line 1607 The operation is not supported [Ljava.lang.StackTraceElement;@12b9f14
								//ORA-00923: FROM keyword not found where expected
								// [Ljava.lang.StackTraceElement;@1c67b54 runMSSQLInsertQuery line 632

								IProperties paramProperties = allProperty.getProperties("SI_PROMPTS");
								numPrompts = ((Integer)paramProperties.getProperty("SI_NUM_PROMPTS").getValue()).intValue();
								System.out.println("nbr prompts is" + numPrompts);
								//numPrompts = paramList.size();
							}
							catch (Exception ex1){
								System.out.println("excel prompts " + ex.getMessage() + " " + ex.getStackTrace());
							}

							//IF IS PDF OUTPUT
							try {
								IPDF pReport = (IPDF)objInfoObject;
								//Get the username 
								//ISDKList dbLogons = eReport.getReportLogons();
								IProperties allProperty = (IProperties)pReport.properties();
								//IProperties pathProperty = allProperty.getProperties("SI_FILES");
								//IProperty logonProperty = eReport.getProcessingInfo().properties().getProperty("SI_LOGONS")
								IProperties logonProperties = allProperty.getProperties("SI_LOGON_INFO");
								IProperties logon1Properties = logonProperties.getProperties("SI_LOGON1"); //get error here for a PDF file
								//$Proxy0 cannot be cast to com.crystaldecisions.sdk.plugin.desktop.report.IReport [Ljava.lang.StackTraceElement;@92af24
								IProperty userProperty = logon1Properties.getProperty("SI_USER");


								//Get the db logon credentials 
								for (int h = 0; h < logon1Properties.size(); h++) {	
									IReportLogon dbLogon = (IReportLogon)logon1Properties.get(h);
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
								//List paramList = eReport.getReportParameters(); //getting an error here
								//Line 1607 The operation is not supported [Ljava.lang.StackTraceElement;@12b9f14
								//ORA-00923: FROM keyword not found where expected
								// [Ljava.lang.StackTraceElement;@1c67b54 runMSSQLInsertQuery line 632

								IProperties paramProperties = allProperty.getProperties("SI_PROMPTS");
								numPrompts = ((Integer)paramProperties.getProperty("SI_NUM_PROMPTS").getValue()).intValue();
								System.out.println("nbr prompts is" + numPrompts);
								//numPrompts = paramList.size();
							}
							catch (Exception ex1){
								System.out.println("PDF prompts " + ex.getMessage() + " " + ex.getStackTrace());
							}

							//IF IS TEXT OUTPUT
							try {
								ITxt tReport = (ITxt)objInfoObject;
								//Get the username 
								//ISDKList dbLogons = eReport.getReportLogons();
								IProperties allProperty = (IProperties)tReport.properties();
								//IProperties pathProperty = allProperty.getProperties("SI_FILES");
								//IProperty logonProperty = eReport.getProcessingInfo().properties().getProperty("SI_LOGONS")
								IProperties logonProperties = allProperty.getProperties("SI_LOGON_INFO");
								IProperties logon1Properties = logonProperties.getProperties("SI_LOGON1");
								IProperty userProperty = logon1Properties.getProperty("SI_USER");


								//Get the db logon credentials 
								for (int h = 0; h < logon1Properties.size(); h++) {	
									IReportLogon dbLogon = (IReportLogon)logon1Properties.get(h);
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
								//List paramList = eReport.getReportParameters(); //getting an error here
								//Line 1607 The operation is not supported [Ljava.lang.StackTraceElement;@12b9f14
								//ORA-00923: FROM keyword not found where expected
								// [Ljava.lang.StackTraceElement;@1c67b54 runMSSQLInsertQuery line 632

								IProperties paramProperties = allProperty.getProperties("SI_PROMPTS");
								numPrompts = ((Integer)paramProperties.getProperty("SI_NUM_PROMPTS").getValue()).intValue();
								System.out.println("nbr prompts is" + numPrompts);
								//numPrompts = paramList.size();
							}
							catch (Exception ex1){
								System.out.println("txt prompts " + ex.getMessage() + " " + ex.getStackTrace());
							}


							System.out.println("prompts " + ex.getMessage() + " " + ex.getStackTrace());
						}


						try {
							paramValues = Helper.getWebiParamValueString(objInfoObject);
							if (paramValues.length() == 0 ){
								paramValues = Helper.getParamValueString(objInfoObject);
							}	

							System.out.println("param values is " + paramValues);
						}
						catch (Exception ex) {

						}


						try {
							if (scheduleStatus.equals("3")) {
								errorMsg = objInfoObject.getSchedulingInfo().getErrorMessage().toString();
								errorMsg = errorMsg.replaceAll("[\\r\\n]", " - ");
								System.out.println(errorMsg);	
							} else
								errorMsg = "no error";
						}
						catch (Exception ex) {

						}

						try
						{
							switch (objInfoObject.getSchedulingInfo().getStatus()) {
							case ISchedulingInfo.ScheduleStatus.COMPLETE:
								System.out.println("schedule status is  " + scheduleStatus);
								//success = true;
								break;
							case ISchedulingInfo.ScheduleStatus.FAILURE:
								//log("Error", objInfoObject.getSchedulingInfo().getErrorMessage());
								//return false;
								errorMsg = Helper.safeSQL(objInfoObject.getSchedulingInfo().getErrorMessage());
								//errorMsg.replaceAll("\r", " - ");
								errorMsg = errorMsg.replaceAll("[\\r\\n]", " - ");  //replaces the carriage return-line feed characters
								//System.out.println(errorMsg);
							}
							/*if (scheduleStatus == "3") {
									//errorMsg = Helper.safeSQL((String)objInfoObject.properties().getProperty("SI_ERROR_MESSAGE").getValue());
									errorMsg = Helper.safeSQL(objInfoObject.getSchedulingInfo().getErrorMessage());
									//errorMsg.replaceAll("\r", " - ");
									errorMsg.replaceAll("[\\r\\n]", " - ");  //replaces the carriage return-line feed characters

								}
								else
									errorMsg = "";*/
						}

						catch (Exception ex)
						{
							//This is old VB.Net COM code to get the error message if it isn't in the SI_ERROR_MESSAGE property bag
							//I will use a catch-all for now

							//Cast the IInfo Object to a IReport object to use the methods of a report
							Helper.runMSSQLInsertQuery("insert into STG1_BO4_TRACKER_ERRORS  '" + ex.getMessage() + " " + ex.getStackTrace() + " " + EnterpriseEnv + "', GETDATE())");
						}


						try {
							IProperties properties = objInfoObject.getProcessingInfo().properties();
							IProperty siFilesProperty = properties.getProperty(CePropertyID.SI_FILES);
							//IProperty outputFileProperty = properties.getProperty(CePropertyID.SI_FILE1);  //not a valid property
							//ifiles = (IFiles)objInfoObject.getProcessingInfo().properties().getProperty("SI_FILES").getValue(); 
							//ifiles = (IFiles) objInfoObject.getProcessingInfo().properties().getProperty(CePropertyID.SI_FILES);

							//IProperties properties = objInfoObject.getSchedulingInfo().properties();
							//IProperty machineUsedProperty = properties.getProperty(CePropertyID.SI_MACHINE_USED);
							//machineUsed =  machineUsedProperty.toString();


							//com.crystaldecisions.sdk.properties.internal.SDKPropertyBag cannot be cast to com.crystaldecisions.sdk.occa.infostore.IFiles
							//outputFile = (String)ifiles.properties().getProperty("SI_PATH").getValue();


							outputFile = siFilesProperty.toString();
							System.out.println("output file is " + outputFile);
							//Some examples:
							//output file is 3&n=1,03&021=8706eb801668d1a34.rpt,0P&0?1=23552,03&S=frs://Input/a_072/129/000/33096/,0P
							//output file is 3&S=frs://Input/a_139/106/013/879243/,0P&n=1,03&021=~ce3338642d37850e14.rpt,0P&0?1=36352,03
							//output file is 3&n=1,03&021=~ce1c305fc86a64730.rpt,0P&0?1=15872,03&S=frs://Input/a_028/150/000/38428/,0P
							//output file is 3&S=frs://Input/a_035/168/000/43043/,0P&n=2,03&021=~ce22f85e742bcf600.rpt,0P&022=~ce22f85e742bd27d1.jpeg,0P&0?1=57856,03&0?2=41296,03
							//output file is 3&S=frs://Output/a_211/176/013/897235/,0P&n=1,03&021=~ce1b005e880782f10.wid,0P&0?1=34802,03
							//Why are some of these in the Input FRS?


							//outputFile = siFilesProperty.getValue(2);  //doesn't work

							//filepath = siFilesProperty.toString(); //use this till you can get the correct code
							//doesn't have all the options to get to the right property
							//si_path
							//si_file1
							//si_num_files
							//si_value1

							IProperties allProperty = (IProperties)objInfoObject.properties();
							IProperties pathProperty = allProperty.getProperties("SI_FILES");
							IProperty path = (IProperty)pathProperty.getProperty("SI_PATH");
							Object getPath = path.getValue();
							filepath = getPath.toString();
							System.out.println("file path is " + filepath);

							IProperty outputFileProperty  = (IProperty)pathProperty.getProperty("SI_FILE1");
							outputFile = outputFileProperty.getValue().toString();
							System.out.println("output file is " + outputFile);

						} 
						catch (Exception Ex) {
							System.out.println("si_file1 and output file " + Ex.getMessage() + " " + Ex.getStackTrace());
						}


						///*****************Production Insert STring*************
						//msSQLInsertString = "insert into " + destTblNm + " (REPORT_ID, INSTANCE_ID, SI_STARTTIME, SI_ENDTIME, DURATION, SI_SUBMITTER, SI_SERVER, SI_OWNER, SI_SCHEDULE_STATUS,REPORT_PATH, SI_NAME, SI_CREATION_TIME, SI_NUM_PROMPTS, DATASRC_LOGIN_NM, SI_PASSWORD, DATASRC_LOGIN_NM2, SI_PASSWORD2, SI_ERROR_MESSAGE, SI_MACHINE_USED, SI_UPDATE_TS, DESTINATION, OUTPUT_FILE, REPORT_CUID, SI_PARENT_FOLDER_CUID, DISTRIBUTION_LIST, si_file1, ENVIRONMENT, SI_KIND, SI_FORMAT_EXPORT_ALLPAGES, CUID, PARAM_VALUES) values (" + objInfoObject.getParentID() + ", " + objInfoObject.getID() + ", to_date('" + Helper.oracleDate(startTime) + "','MM/DD/YYYY HH12:MI:SS AM')" + ", to_date('" + Helper.oracleDate(endTime) + "','MM/DD/YYYY HH12:MI:SS AM')" + ", " + theInterval + ", '" + Helper.safeSQL(submitter) + "', '" + Helper.safeSQL(server) + "', '" + Helper.safeSQL(submitter) + "', " + scheduleStatus + ", '" + Helper.safeSQL(cookieCrumbs) + "', '" + Helper.safeSQL(objInfoObject.getTitle()) + "', to_date('" + Helper.msSQLDateTime(creationTime) + "','MM/DD/YYYY HH12:MI:SS AM'), " + numPrompts + ",'" + logon1 + "','" + pswd1 + "', '" + logon2 + "', '" + pswd2 + "','" + errorMsg + "','" + machineUsed + "', to_date('" + Helper.msSQLDateTime(updateTS) + "','MM/DD/YYYY HH12:MI:SS AM'), '" + destination + "','" + outputFile + "', '" + objInfoObject.getParentCUID() + "', '" + parentFolderCuid + "','" + distributionList + "', '" + si_file1 + "','" + EnterpriseEnv + "','" + objInfoObject.getKind() + "'," + siFormatExportAllpages + "','" + objInfoObject.getCUID() + "','" + Helper.safeSQL(paramValues) + "')";
						//Helper.runMSSQLInsertQuery(msSQLInsertString);

						errorCategory = Helper.getErrorCategory(errorMsg);
						errorCategory = Helper.safeSQL(errorCategory);
						System.out.println("Error Category is " + errorCategory);

						if (paramValues.length() > 3000) {
							System.out.println("The param value string for " + objInfoObject.getID() + " exceeds 3000 char");
							paramValues = paramValues.substring(0, 2995);
						}
						try {
							msSQLInsertString = "insert into " + destTblNm + " (REPORT_ID, INSTANCE_ID, SI_STARTTIME, SI_ENDTIME,  SI_SUBMITTER, SI_SCHEDULE_STATUS, SI_KIND,  SI_NAME, SI_CREATION_TIME, CUID, REPORT_CUID, DURATION, DESTINATION, DISTRIBUTION_LIST, SI_MACHINE_USED, SI_NUM_PROMPTS, PARAM_VALUES, SI_ERROR_MESSAGE, DATASRC_LOGIN_NM, DATASRC_LOGIN_NM2, OUTPUT_FILE, SI_FILE1, ENVIRONMENT, SI_SERVER, ERROR_CATEGORY, EXPIRATION_TIME) values (" + objInfoObject.getParentID() + ", " + instanceID + ", ('" + Helper.msSQLDateTime(startTime) + "')" + ", ('" + Helper.msSQLDateTime(endTime) + "')" + ", '" + Helper.safeSQL(submitter) + "', " + scheduleStatus   + ", '" + objInfoObject.getKind() + "','" + Helper.safeSQL(objInfoObject.getTitle()) + "', ('" + Helper.msSQLDateTime(creationTime) + "') "   +  ",'"  +  cuid + "','" + reportCuid +  "'," + theInterval +", '" + destination + "','" + distributionList + "','" + machineUsed + "', " + numPrompts + ",'" + Helper.safeSQL(paramValues) +"', '" + errorMsg + "', '" + logon1 + "','" + logon2 +"','" + outputFile + "','" + filepath + "','" + env + "','" + server +"', '" + errorCategory + "', ('" + Helper.msSQLDateTime(expiryTime) + "'))";
							System.out.println(msSQLInsertString);
							Helper.runMSSQLInsertQuery(msSQLInsertString);	
						}
						catch (Exception ex){
							//There could be a constraint violation because now INSTANCE_ID must be unique
							System.out.println( ex.getMessage() + " " + ex.getStackTrace());
						}

					} //end if for schedule status

				}
				// Clean up the Enterprise Session.
				if(objEnterpriseSession != null) {
					try {
						objEnterpriseSession.logoff();

					} catch(Exception e_ignore_in_cleanup) {}
				}
			}

			else {
				System.out.println ("No report history found.");
			}
			// Clean up the Enterprise Session.
			if(objEnterpriseSession != null) {
				try {
					objEnterpriseSession.logoff();

				} catch(Exception e_ignore_in_cleanup) {}
			}
		}

		catch (Exception ex)
		{
			System.out.println(ex.getMessage() + " " + ex.getStackTrace());
			//Helper.runMSSQLInsertQuery("insert into STG1_BO4_TRACKER_ERRORS (ERROR_MESSAGE, LOAD_DT) VALUES ('" + ex.getMessage() + " " + ex.getStackTrace() + " " + EnterpriseEnv + "', GETDATE())");
			Helper.runMSSQLInsertQuery("insert into STG1_BO4_TRACKER_ERRORS (ERROR_MESSAGE, LOAD_DT) VALUES ( '" + ex.getMessage() + " " + ex.getStackTrace() + " " + EnterpriseEnv + "', GETDATE())");
			//'sendmail("Logon error", ex.getMessage() + " " + ex.getStackTrace)
			//Helper.sendmail("bishopp@lexmark.com", "bishopp@lexmark.com, amarabes@lexmark.com,ajpatel@lexmark.com,msankar@lexmark.com,ajain@lexmark.com", "Logon error - load_stg1_migr_XI_reports(String, String, String, String, String) - " + EnterpriseEnv, ex.getMessage() + " " + ex.getStackTrace());
			//Helper.sendmail("bishopp@lexmark.com", "bishopp@lexmark.com", "Logon error - load_stg1_boxi_runhist(String, String, String, String, String) - " + EnterpriseEnv, ex.getMessage() + " " + ex.getStackTrace());

		}
	}

	public static void load_stg1_BO_missing_history (String EnterpriseEnv, String login, String pswd,String auth, String destTblNm, String env, String startDate, String endDate) {
		//Pre-condition:  some run history is missing in stage 1
		//Post-condition: the missing run history has been loaded into stage 1

		IEnterpriseSession objEnterpriseSession;
		//IInfoStore objInfoStore;
		IInfoObjects colInfoObjects;
		IInfoObject objInfoObject;

		String strInfoSQL;
		String msSQLInsertString;
		String msSQLSelectString;

		//targetDate.add(Calendar.DATE, -5);
		//Load the bare minimum of runhist from BO Enterprise by using TEMP_OBJECTS table.
		Helper.runMSSQLInsertQuery("truncate table temp_objects"); 

		ISessionMgr objSessionMgr;
		try {
			objSessionMgr = CrystalEnterprise.getSessionMgr();

			objEnterpriseSession = objSessionMgr.logon(login, pswd, EnterpriseEnv, auth); 
			IInfoStore objInfoStore = (IInfoStore) objEnterpriseSession.getService("InfoStore");

			//I hard-coded 5000 because more than that will use too much memory and if I run this daily, there shouldn't be more than 5000 missing records
			strInfoSQL = "SELECT TOP 7000 SI_ID, CUID, SI_KIND FROM CI_INFOOBJECTS WHERE  SI_INSTANCE = 1 and SI_KIND not in ('MDAnalysis','XL.XcelsiusEnterprise') and si_recurring=0 and si_endtime > '" + startDate + "' and si_endtime < '" + endDate + "'";
			System.out.println(strInfoSQL);
			colInfoObjects = (IInfoObjects)objInfoStore.query(strInfoSQL);

			if (colInfoObjects.size() > 0)  {
				int rowcount = colInfoObjects.size();

				System.out.println("We do have objects in collection");
				for (int k =0; k < rowcount; k++)
				{
					objInfoObject = (IInfoObject) colInfoObjects.get(k);

					try {
						msSQLInsertString = "INSERT INTO TEMP_OBJECTS (SI_ID,  CUID, ENVIRONMENT, SI_KIND) VALUES (" + objInfoObject.getID() + ", '"  + objInfoObject.getCUID() + "','" + EnterpriseEnv +  "', '" + objInfoObject.getKind() + "')";
						Helper.runMSSQLInsertQuery(msSQLInsertString);	
					}
					catch (Exception ex){
						System.out.println(ex.getMessage() + " " + ex.getStackTrace());
					}

				}
			}

			// Clean up the Enterprise Session.
			if(objEnterpriseSession != null) {
				try {
					objEnterpriseSession.logoff();

				} catch(Exception e_ignore_in_cleanup) {}
			}

		} catch (SDKException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//Get the recordset of what instance_id's are missing to get that history from BO
		msSQLSelectString = " select a.SI_ID from TEMP_OBJECTS a left outer join " + destTblNm + " b on a.SI_ID = b.instance_id where b.instance_id is null";


		//Connect to MS SQL Server:
		String driver = "mssql.jdbc.driver.SQLDriver";
		String url = "jdbc:sqlserver://DASHWBODB012;databaseName=EDR1DEN1;integratedSecurity=true;";
		String username = "botrack";
		String password = "********";

		int theID = 0;
		try
		{
			System.out.println(msSQLSelectString);
			//Class.forName(driver);		//loads the driver
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			//Connection conn = DriverManager.getConnection(url,username,password);
			Connection conn = DriverManager.getConnection("jdbc:sqlserver://DASHWBODB012", "BOTRACK", "********");
			System.out.println("connected");

			Statement stmt = conn.createStatement() ; 
			ResultSet rs = stmt.executeQuery(msSQLSelectString);
			//Loop thru the recordset id by id to load the history using load_stg1_BO_runhist_By_ID()

			while (rs.next()) 
			{
				theID = rs.getInt("SI_ID"); 
				//Load the missing record into the Tracker database:
				load_stg1_BO_runhist_By_ID (EnterpriseEnv, "tracker", "libambini*8","secLDAP", destTblNm, env, theID);

			}	
			conn = null;
			conn.close();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

	}

	public static void load_stg1_BO_runhist_By_ID (String EnterpriseEnv, String login, String pswd,String auth, String destTblNm, String env, int ID){
		IEnterpriseSession objEnterpriseSession;
		//IInfoStore objInfoStore;
		IInfoObjects colInfoObjects;
		IInfoObject objInfoObject;
		IInfoObject objInfoObject2;
		String errorMsg = "";
		String cookieCrumbs;
		int count;
		String si_owner = "";
		Calendar runDate;
		Calendar startTime= Calendar.getInstance();
		Calendar endTime= Calendar.getInstance();
		Calendar creationTime = Calendar.getInstance();
		Calendar updateTS = Calendar.getInstance();
		Calendar expiryTime = Calendar.getInstance();
		double theInterval = 0;
		Calendar startDate;
		Calendar endDate;
		String dtToday;
		String msSQLInsertString;
		String maxID = "0";
		String logon1 = "unknown";
		String logon2 = "unknown";
		String pswd1 = "unknown";
		String pswd2 = "unknown";
		String datasrc1 = "not found in CMS";
		String datasrc2 = "not found in CMS";
		int numPrompts = 0;
		Calendar maxEndDate;
		String destination = "";
		String outputFile = "";
		String distributionList = "";
		String filepath = "not set";
		String strInfoSQL;
		Calendar maxCreationDate;
		//java.sql.Date maxCreationDate;
		int i = 0;
		IFiles ifiles;
		String scheduleStatus = "999"; //means is not yet known.  I made this up because this is not a valid value.  Tells me it's not getting set from the recordset.
		IProcessingInfo processingInfo;
		IReport iReport;
		String server ="";
		String server1 = "";
		String server2 = "";
		IProperty aggCount;
		String strCount="0";
		int colCount = 0;
		Boolean si_format_export_allpages;
		String siFormatExportAllpages;
		String paramValues = "";
		//		IWebiPrompts iWebiPrompts;
		IPrompts webiPrompts;
		IReport oReport;
		String submitter = "";
		String machineUsed = "";
		String parentFolderCuid = "";
		String siOwner = "";
		String testString = "";
		int schedStatus;
		String siKind = "";
		String cuid = "";
		String reportCuid = "";
		int instanceID = 0;
		String errorMessage = "no error";
		//int numLogins = 0;
		String numLogins = "0";
		String errorCategory;

		try 
		{
			ISessionMgr  objSessionMgr = CrystalEnterprise.getSessionMgr();
			objEnterpriseSession = objSessionMgr.logon(login, pswd, EnterpriseEnv, auth);    
			IInfoStore objInfoStore = (IInfoStore) objEnterpriseSession.getService("InfoStore");

			strInfoSQL = "SELECT * FROM CI_INFOOBJECTS WHERE  SI_INSTANCE = 1 and SI_KIND not in ('MDAnalysis','XL.XcelsiusEnterprise') and si_recurring=0 and si_id = " + ID;


			///**********JUST TEMPORARY FOR TESTING*************
			//strInfoSQL = "SELECT  * FROM CI_INFOOBJECTS WHERE SI_ID=942134";
			//strInfoSQL = "SELECT TOP 100 si_id, CUID, si_name, si_submitter, si_starttime, si_endtime, si_creation_time, si_schedule_status, si_kind, si_parent_cuid FROM CI_INFOOBJECTS WHERE  SI_INSTANCE = 1 and si_recurring=0 and SI_ENDTIME >= '" + Helper.formatDateBO(maxEndDate) + "'";
			//strInfoSQL = "SELECT TOP 100 * FROM CI_INFOOBJECTS WHERE  SI_INSTANCE = 1 and si_recurring=0 and SI_ENDTIME >= '" + Helper.formatDateBO(maxEndDate) + "'";
			//strInfoSQL = "SELECT TOP " + strCount + " * FROM CI_INFOOBJECTS WHERE  SI_ID = 1751209";

			//JUST A TEST TO SEE HOW FAILED INSTANCES WORK IN THE CODE
			//strInfoSQL = "SELECT TOP " + strCount + " SI_ID FROM CI_INFOOBJECTS WHERE  SI_INSTANCE = 1 and si_recurring=0 and si_schedule_status=1 and SI_CREATION_TIME >= '" + Helper.formatDateBO(maxCreationDate) + "'";

			System.out.println(strInfoSQL);
			colInfoObjects = (IInfoObjects)objInfoStore.query(strInfoSQL);
			System.out.println("Line 3448 Count of objects in collection: " + colInfoObjects.size());

			if (colInfoObjects.size() > 0){
				/*Connection con=null;
				Class.forName("oracle.jdbc.driver.OracleDriver");
				con=DriverManager.getConnection("jdbc:oracle:thin:@dlxkrbodb1.lex.lexmark.com:1750:edr1den1","botrack","********");
				Statement s=con.createStatement();*/
				//'This records all the instances found on the BOXI system
				colCount = colInfoObjects.size();
				for (int x=0; x <= colCount; x++){
					//msSQLInsertString = "insert into temp_p_allinstances(report_id, instance_id) values (" + objInfoObject.getParentID() + "," + objInfoObject.getID() + ")";
					//System.out.println(msSQLInsertString);
					//s.execute(msSQLInsertString);
					errorMsg = "";  //resets the error message since it won't get overwritten each time.
					paramValues = ""; //resets the paramValues

					objInfoObject = (IInfoObject)colInfoObjects.get(x);
					//oReport = (IReport)colInfoObjects.get(x);  //4/27/2012:  causes error against 3.1 Prod: $Proxy0 cannot be cast to com.crystaldecisions.sdk.plugin.desktop.report.IReport [Ljava.lang.StackTraceElement;@aa559d
					//That's because this is an instance and it will throw this error if the object is not .rpt, which would usually be
					//the case.
					System.out.println("next report, si_id is " + objInfoObject.getID());
					//System.out.println ("si_id is " + objInfoObject.getID());

					try {
						scheduleStatus = objInfoObject.properties().getProperty("SI_SCHEDULE_STATUS").getValue().toString();
						schedStatus = objInfoObject.getSchedulingInfo().getStatus();
						System.out.println("scheduleStatus is " + scheduleStatus);
						System.out.println("schedStatus is " + schedStatus);
					} 
					catch (Exception ex) {
						System.out.println(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getStackTrace());
					}

					//Only do the rest if the schedule status is 1 or 3
					if (scheduleStatus.equals("1") || scheduleStatus.equals("3"))  {
						try {
							IProperties properties = objInfoObject.properties();
							IProperty startTimeProperty = properties.getProperty(CePropertyID.SI_STARTTIME);
							//String value = (String) creationTimeProperty.getValue();
							startTime.setTime ((Date) startTimeProperty.getValue());  //This works
							System.out.println("start time is " + Helper.msSQLDateTime(startTime));
						}
						catch (Exception ex) {
							System.out.println(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getStackTrace());
						}


						try {
							IProperties properties = objInfoObject.properties();
							IProperty endTimeProperty = properties.getProperty(CePropertyID.SI_ENDTIME);
							//String value = (String) creationTimeProperty.getValue();
							endTime.setTime ((Date) endTimeProperty.getValue());  //This works
							System.out.println("end time is " + Helper.msSQLDateTime(endTime));
						}
						catch (Exception ex) {
							System.out.println(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getStackTrace());
						}

						try {
							//if ((scheduleStatus.equals("1")) || (scheduleStatus.equals("3"))) {
							//runDate = (Calendar)objInfoObject.properties().getProperty("SI_STARTTIME").getValue();
							//startTime = runDate;
							//endTime = (Calendar)objInfoObject.properties().getProperty("SI_ENDTIME").getValue();
							//theInterval = 60/(endTime.getTimeInMillis() - startTime.getTimeInMillis())*1000;
							//}
							theInterval = (endTime.getTimeInMillis() - startTime.getTimeInMillis());
							System.out.println("the duration in milliseconds was " + theInterval);
							theInterval = theInterval/1000;
							//theInterval = 60/(endTime.getTimeInMillis() - startTime.getTimeInMillis())*1000;
							System.out.println("the duration in seconds was " + theInterval);
							theInterval = theInterval/60;
							System.out.println("the duration in minutes was " + theInterval);
						}
						catch (Exception ex) {
							System.out.println(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getStackTrace());
						}
						System.out.println("the duration was " + theInterval);

						try {
							//reportCuid.equals(objInfoObject.getParentCUID().toString());
							reportCuid = objInfoObject.getParentCUID().toString();
							System.out.println("Report cuid is " + reportCuid);
						}
						catch (Exception ex) {
							System.out.println(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getStackTrace());
						}


						try {
							//cuid.equals(objInfoObject.getCUID().toString());
							cuid = objInfoObject.getCUID().toString();
							System.out.println("Instance cuid is " + cuid); //watch out for AeVnF4VTRkdFsYYNxvwfFlI, an Analysis object
						}catch (Exception ex) {
							System.out.println(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getStackTrace());
						}


						try {
							instanceID = objInfoObject.getID();
							System.out.println("report instance id is " + instanceID);
						}
						catch (Exception ex) {
							System.out.println(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getStackTrace());
						}



						try {
							//IProperties properties = objInfoObject.properties();
							//IProperty submitterProperty = properties.getProperty(CePropertyID.SI_SUBMITTER);

							//submitter.equals(submitterProperty.toString());  //doesn't work
							//submitter = submitterProperty.toString();


							submitter =objInfoObject.getSchedulingInfo().properties().getProperty("SI_SUBMITTER").getValue().toString();
							System.out.println("submitter " + submitter);
						}
						catch (Exception ex) {
							System.out.println(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getStackTrace());
						}

						try {
							//Got this from D:\Documents and Settings\bishopp\My Documents\BOXI\Code Examples\xi4_boejava_dg_en\html\topic15.html
							IProperties properties = objInfoObject.properties();
							IProperty creationTimeProperty = properties.getProperty(CePropertyID.SI_CREATION_TIME);
							//String value = (String) creationTimeProperty.getValue();
							creationTime.setTime ((Date) creationTimeProperty.getValue());  //This works
							//System.out.println("creation time is " + Helper.msSQLDateTime(creationTime));
							System.out.println("creation time is " + creationTime.getTime());
						}
						catch (Exception ex)
						{
							ex.printStackTrace();
							System.out.println(" for report id: " +objInfoObject.getID() + " " + ex.getMessage() + " " + ex.getStackTrace());
						}

						try {
							//Got this from D:\Documents and Settings\bishopp\My Documents\BOXI\Code Examples\xi4_boejava_dg_en\html\topic15.html
							//IProperties properties = objInfoObject.properties();
							//IProperty expiryTimeProperty = properties.getProperty(CePropertyId.);
							//creationTime.setTime ((Date) creationTimeProperty.getValue());  //This works

							expiryTime.setTime((Date) objInfoObject.getSchedulingInfo().properties().getProperty("SI_ENDTIME").getValue());
							System.out.println("expiration time is " + expiryTime.getTime());
						}
						catch (Exception ex)
						{
							ex.printStackTrace();
							System.out.println(" for report id: " +objInfoObject.getID() + " " + ex.getMessage() + " " + ex.getStackTrace());
						}


						try {
							siKind.equals(objInfoObject.getKind());
							System.out.println("SI_KIND is " + objInfoObject.getKind().toString());
						} 
						catch (Exception ex) {
							System.out.println(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getStackTrace());
						}


						try {
							//destination = objInfoObject.getSchedulingInfo().getDestination().getName();  //--> destination was null, but it can't be!
							//destination = (String)objInfoObject.getSchedulingInfo().properties().getProperty("SI_DESTINATION").getValue();
							//4/26/2013:  In Enterprise, Analysis reports, SI_KIND='MDAnalysis', has objects where SI_INSTANCE=1.  These error out.

							//VB:  destination = objInfoObject.SendToDestination.Name and this works
							ISendable obj = (ISendable)objInfoObject;						
							IDestinationPlugin destinationPlugin = Helper.getDestinationPlugin(objInfoStore, objInfoObject.getKind());
							IDestination odestination = obj.getSendToDestination();	
							//odestination.getName();
							//odestination.setFromPlugin(destinationPlugin);//this was setting the destination to smtp, not getting what it is
							System.out.println("Line 1547: destination is " + odestination.getName());
							destination = odestination.getName();
							System.out.println("destination is " + destination + " for si_id " + objInfoObject.getID());
							//distributionList = Helper.getDistributionList("uslexbcs02", objInfoObject.getID());
							if (destination != null) {
								if (destination.equals("CrystalEnterprise.Smtp")) {
									System.out.println("Line 1653");
									System.out.println("getting email distribution list");
									//distributionList = Helper.getDistributionList("uslexbcs02", objInfoObject.getID());	
									//distributionList = Helper.getDistributionList(EnterpriseEnv, objInfoObject.getID()); //gives an error
									distributionList = Helper.getDistributionList1(objEnterpriseSession,EnterpriseEnv, objInfoObject.getID()); //gives an error
									System.out.println("Line 1657" + distributionList);
								}
								else
									System.out.println("Destination actually is:  " + destination);
							}

						}
						catch(SDKException sdkEx)
						{
							System.out.println("Line 1607 " + sdkEx.getDetail() + " " + sdkEx.getStackTrace());
							Helper.runMSSQLInsertQuery("insert into STG1_BO4_TRACKER_ERRORS (ERROR_MESSAGE, LOAD_DT) VALUES ('" + sdkEx.getMessage() + " " + sdkEx.getStackTrace() + " " + EnterpriseEnv + "', GETDATE())");
						}


						try {
							//get SI_MACHINE_USED, the job server the job ran on
							IProperties properties = objInfoObject.getSchedulingInfo().properties();
							IProperty machineUsedProperty = properties.getProperty(CePropertyID.SI_MACHINE_USED);
							//String value = (String) creationTimeProperty.getValue();
							machineUsed =  machineUsedProperty.toString();
							System.out.println("job server " + machineUsed);
						}
						catch(Exception Ex)
						{
							System.out.println("Line 1607 " + Ex.getMessage() + " " + Ex.getStackTrace());
							Helper.runMSSQLInsertQuery("insert into STG1_BO4_TRACKER_ERRORS (ERROR_MESSAGE, LOAD_DT) VALUES ('" + Ex.getMessage() + " " + Ex.getStackTrace() + " " + EnterpriseEnv + "', GETDATE())");
						}

						try {
							if (objInfoObject.getKind() == "Webi") {
								webiPrompts = (IPrompts)objInfoObject.properties().getProperty("SI_WEBI_PROMPTS").getValue();
								numPrompts = webiPrompts.size();
								logon1 = "n/a";
								logon2 = "n/a";
								server1 = "n/a";
								server2 = "n/a";
								if (numPrompts > 0 ) {
									//Getting the param list might not be possible
									//List paramList = webiPrompts.getReportParameters();
									Object paramList =  webiPrompts.toArray();
								}

							} else {
								//see if this will work for all file types, .rpt, .xls and .pdf

								// First get the Processing Info properties for the InfoObject.
								IProperties boProcessingInfo = (IProperties) objInfoObject.getProcessingInfo().properties();

								// Make sure that there is processing info set for the InfoObject.
								if (boProcessingInfo != null)
								{
									// Get the SI_PROMPTS property bag from the InfoObject's Processing Info
									IProperties siPrompts = (IProperties) boProcessingInfo.getProperty("SI_PROMPTS").getValue();

									// Make sure that there are parameters.
									if (siPrompts != null)
									{
										// Get the number of prompts (the number of parameters)
										numPrompts = ((Integer)siPrompts.getProperty("SI_NUM_PROMPTS").getValue()).intValue();
									}

									// Get the SI_LOGON_INFO property bag
									IProperties logonProperties = (IProperties) boProcessingInfo.getProperty("SI_LOGON_INFO").getValue();
									if (logonProperties != null) {
										IProperties silogonProperties = (IProperties) logonProperties.getProperty("SI_LOGON1").getValue();


										System.out.println("SI_LOGON1 size is " + silogonProperties.size());
										//16, it's true
										numLogins = logonProperties.getProperty("SI_NUM_LOGONS").toString();
										System.out.println("Num logons is " + numLogins);
										//System.out.println("logon1 is " + logon1);
										//Get the db logon credentials 
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



								//////////////ONLY WORKS FOR CRYSTAL REPORTS FORMAT
								/*iReport = (IReport) objInfoObject;  //can't do this if the report is not in CR format

								//Get the username 
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

								List paramList = iReport.getReportParameters(); //getting an error here
								//Line 1607 The operation is not supported [Ljava.lang.StackTraceElement;@12b9f14
								//ORA-00923: FROM keyword not found where expected
								// [Ljava.lang.StackTraceElement;@1c67b54 runMSSQLInsertQuery line 632
								numPrompts = paramList.size();*/
								/////////////////////////////////////////////

							}


							//The number of prompts is stored differently between Crystal Reports and Webi docs
							//Crystal: SI_PROMPTS
							//Webi:  SI_WEBI_PROMPTS
							//Xcelsius:  


						} catch (Exception ex) {
							System.out.println("Logons " + ex.getMessage() + " " + ex.getStackTrace());
							//IF IS EXCEL OUTPUT
							try {
								IExcel eReport = (IExcel)objInfoObject;
								//Get the username 
								//									ISDKList dbLogons = eReport.getReportLogons();
								IProperties allProperty = (IProperties)eReport.properties();
								//IProperties pathProperty = allProperty.getProperties("SI_FILES");
								//IProperty logonProperty = eReport.getProcessingInfo().properties().getProperty("SI_LOGONS")
								IProperties logonProperties = allProperty.getProperties("SI_LOGON_INFO");
								IProperties logon1Properties = logonProperties.getProperties("SI_LOGON1");
								//IProperty userProperty = logon1Properties.getProperty("SI_USER");


								//Get the db logon credentials 
								for (int h = 0; h < logon1Properties.size(); h++) {	
									IReportLogon dbLogon = (IReportLogon)logon1Properties.get(h);
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
								//List paramList = eReport.getReportParameters(); //getting an error here
								//Line 1607 The operation is not supported [Ljava.lang.StackTraceElement;@12b9f14
								//ORA-00923: FROM keyword not found where expected
								// [Ljava.lang.StackTraceElement;@1c67b54 runMSSQLInsertQuery line 632

								IProperties paramProperties = allProperty.getProperties("SI_PROMPTS");
								numPrompts = ((Integer)paramProperties.getProperty("SI_NUM_PROMPTS").getValue()).intValue();
								System.out.println("nbr prompts is" + numPrompts);
								//numPrompts = paramList.size();
							}
							catch (Exception ex1){
								System.out.println("excel prompts " + ex.getMessage() + " " + ex.getStackTrace());
							}

							//IF IS PDF OUTPUT
							try {
								IPDF pReport = (IPDF)objInfoObject;
								//Get the username 
								//ISDKList dbLogons = eReport.getReportLogons();
								IProperties allProperty = (IProperties)pReport.properties();
								//IProperties pathProperty = allProperty.getProperties("SI_FILES");
								//IProperty logonProperty = eReport.getProcessingInfo().properties().getProperty("SI_LOGONS")
								IProperties logonProperties = allProperty.getProperties("SI_LOGON_INFO");
								IProperties logon1Properties = logonProperties.getProperties("SI_LOGON1"); //get error here for a PDF file
								//$Proxy0 cannot be cast to com.crystaldecisions.sdk.plugin.desktop.report.IReport [Ljava.lang.StackTraceElement;@92af24
								IProperty userProperty = logon1Properties.getProperty("SI_USER");


								//Get the db logon credentials 
								for (int h = 0; h < logon1Properties.size(); h++) {	
									IReportLogon dbLogon = (IReportLogon)logon1Properties.get(h);
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
								//List paramList = eReport.getReportParameters(); //getting an error here
								//Line 1607 The operation is not supported [Ljava.lang.StackTraceElement;@12b9f14
								//ORA-00923: FROM keyword not found where expected
								// [Ljava.lang.StackTraceElement;@1c67b54 runMSSQLInsertQuery line 632

								IProperties paramProperties = allProperty.getProperties("SI_PROMPTS");
								numPrompts = ((Integer)paramProperties.getProperty("SI_NUM_PROMPTS").getValue()).intValue();
								System.out.println("nbr prompts is" + numPrompts);
								//numPrompts = paramList.size();
							}
							catch (Exception ex1){
								System.out.println("PDF prompts " + ex.getMessage() + " " + ex.getStackTrace());
							}

							//IF IS TEXT OUTPUT
							try {
								ITxt tReport = (ITxt)objInfoObject;
								//Get the username 
								//ISDKList dbLogons = eReport.getReportLogons();
								IProperties allProperty = (IProperties)tReport.properties();
								//IProperties pathProperty = allProperty.getProperties("SI_FILES");
								//IProperty logonProperty = eReport.getProcessingInfo().properties().getProperty("SI_LOGONS")
								IProperties logonProperties = allProperty.getProperties("SI_LOGON_INFO");
								IProperties logon1Properties = logonProperties.getProperties("SI_LOGON1");
								IProperty userProperty = logon1Properties.getProperty("SI_USER");


								//Get the db logon credentials 
								for (int h = 0; h < logon1Properties.size(); h++) {	
									IReportLogon dbLogon = (IReportLogon)logon1Properties.get(h);
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
								//List paramList = eReport.getReportParameters(); //getting an error here
								//Line 1607 The operation is not supported [Ljava.lang.StackTraceElement;@12b9f14
								//ORA-00923: FROM keyword not found where expected
								// [Ljava.lang.StackTraceElement;@1c67b54 runMSSQLInsertQuery line 632

								IProperties paramProperties = allProperty.getProperties("SI_PROMPTS");
								numPrompts = ((Integer)paramProperties.getProperty("SI_NUM_PROMPTS").getValue()).intValue();
								System.out.println("nbr prompts is" + numPrompts);
								//numPrompts = paramList.size();
							}
							catch (Exception ex1){
								System.out.println("txt prompts " + ex.getMessage() + " " + ex.getStackTrace());
							}


							System.out.println("prompts " + ex.getMessage() + " " + ex.getStackTrace());
						}


						try {
							paramValues = Helper.getWebiParamValueString(objInfoObject);
							if (paramValues.length() == 0 ){
								paramValues = Helper.getParamValueString(objInfoObject);
							}	

							System.out.println("param values is " + paramValues);
						}
						catch (Exception ex) {

						}


						try {
							if (scheduleStatus.equals("3")) {
								errorMsg = objInfoObject.getSchedulingInfo().getErrorMessage().toString();
								errorMsg = errorMsg.replaceAll("[\\r\\n]", " - ");
								System.out.println(errorMsg);	
							} else
								errorMsg = "no error";
						}
						catch (Exception ex) {

						}

						try
						{
							switch (objInfoObject.getSchedulingInfo().getStatus()) {
							case ISchedulingInfo.ScheduleStatus.COMPLETE:
								System.out.println("schedule status is  " + scheduleStatus);
								//success = true;
								break;
							case ISchedulingInfo.ScheduleStatus.FAILURE:
								//log("Error", objInfoObject.getSchedulingInfo().getErrorMessage());
								//return false;
								errorMsg = Helper.safeSQL(objInfoObject.getSchedulingInfo().getErrorMessage());
								//errorMsg.replaceAll("\r", " - ");
								errorMsg = errorMsg.replaceAll("[\\r\\n]", " - ");  //replaces the carriage return-line feed characters
								//System.out.println(errorMsg);
							}
							/*if (scheduleStatus == "3") {
									//errorMsg = Helper.safeSQL((String)objInfoObject.properties().getProperty("SI_ERROR_MESSAGE").getValue());
									errorMsg = Helper.safeSQL(objInfoObject.getSchedulingInfo().getErrorMessage());
									//errorMsg.replaceAll("\r", " - ");
									errorMsg.replaceAll("[\\r\\n]", " - ");  //replaces the carriage return-line feed characters

								}
								else
									errorMsg = "";*/
						}

						catch (Exception ex)
						{
							//This is old VB.Net COM code to get the error message if it isn't in the SI_ERROR_MESSAGE property bag
							//I will use a catch-all for now

							//Cast the IInfo Object to a IReport object to use the methods of a report
							Helper.runMSSQLInsertQuery("insert into STG1_BO4_TRACKER_ERRORS  '" + ex.getMessage() + " " + ex.getStackTrace() + " " + EnterpriseEnv + "', GETDATE())");
						}


						try {
							IProperties properties = objInfoObject.getProcessingInfo().properties();
							IProperty siFilesProperty = properties.getProperty(CePropertyID.SI_FILES);
							//IProperty outputFileProperty = properties.getProperty(CePropertyID.SI_FILE1);  //not a valid property
							//ifiles = (IFiles)objInfoObject.getProcessingInfo().properties().getProperty("SI_FILES").getValue(); 
							//ifiles = (IFiles) objInfoObject.getProcessingInfo().properties().getProperty(CePropertyID.SI_FILES);

							//IProperties properties = objInfoObject.getSchedulingInfo().properties();
							//IProperty machineUsedProperty = properties.getProperty(CePropertyID.SI_MACHINE_USED);
							//machineUsed =  machineUsedProperty.toString();


							//com.crystaldecisions.sdk.properties.internal.SDKPropertyBag cannot be cast to com.crystaldecisions.sdk.occa.infostore.IFiles
							//outputFile = (String)ifiles.properties().getProperty("SI_PATH").getValue();


							outputFile = siFilesProperty.toString();
							System.out.println("output file is " + outputFile);
							//Some examples:
							//output file is 3&n=1,03&021=8706eb801668d1a34.rpt,0P&0?1=23552,03&S=frs://Input/a_072/129/000/33096/,0P
							//output file is 3&S=frs://Input/a_139/106/013/879243/,0P&n=1,03&021=~ce3338642d37850e14.rpt,0P&0?1=36352,03
							//output file is 3&n=1,03&021=~ce1c305fc86a64730.rpt,0P&0?1=15872,03&S=frs://Input/a_028/150/000/38428/,0P
							//output file is 3&S=frs://Input/a_035/168/000/43043/,0P&n=2,03&021=~ce22f85e742bcf600.rpt,0P&022=~ce22f85e742bd27d1.jpeg,0P&0?1=57856,03&0?2=41296,03
							//output file is 3&S=frs://Output/a_211/176/013/897235/,0P&n=1,03&021=~ce1b005e880782f10.wid,0P&0?1=34802,03
							//Why are some of these in the Input FRS?


							//outputFile = siFilesProperty.getValue(2);  //doesn't work

							//filepath = siFilesProperty.toString(); //use this till you can get the correct code
							//doesn't have all the options to get to the right property
							//si_path
							//si_file1
							//si_num_files
							//si_value1

							IProperties allProperty = (IProperties)objInfoObject.properties();
							IProperties pathProperty = allProperty.getProperties("SI_FILES");
							IProperty path = (IProperty)pathProperty.getProperty("SI_PATH");
							Object getPath = path.getValue();
							filepath = getPath.toString();
							System.out.println("file path is " + filepath);

							IProperty outputFileProperty  = (IProperty)pathProperty.getProperty("SI_FILE1");
							outputFile = outputFileProperty.getValue().toString();
							System.out.println("output file is " + outputFile);

						} 
						catch (Exception Ex) {
							System.out.println("si_file1 and output file " + Ex.getMessage() + " " + Ex.getStackTrace());
						}


						///*****************Production Insert STring*************
						//msSQLInsertString = "insert into " + destTblNm + " (REPORT_ID, INSTANCE_ID, SI_STARTTIME, SI_ENDTIME, DURATION, SI_SUBMITTER, SI_SERVER, SI_OWNER, SI_SCHEDULE_STATUS,REPORT_PATH, SI_NAME, SI_CREATION_TIME, SI_NUM_PROMPTS, DATASRC_LOGIN_NM, SI_PASSWORD, DATASRC_LOGIN_NM2, SI_PASSWORD2, SI_ERROR_MESSAGE, SI_MACHINE_USED, SI_UPDATE_TS, DESTINATION, OUTPUT_FILE, REPORT_CUID, SI_PARENT_FOLDER_CUID, DISTRIBUTION_LIST, si_file1, ENVIRONMENT, SI_KIND, SI_FORMAT_EXPORT_ALLPAGES, CUID, PARAM_VALUES) values (" + objInfoObject.getParentID() + ", " + objInfoObject.getID() + ", to_date('" + Helper.oracleDate(startTime) + "','MM/DD/YYYY HH12:MI:SS AM')" + ", to_date('" + Helper.oracleDate(endTime) + "','MM/DD/YYYY HH12:MI:SS AM')" + ", " + theInterval + ", '" + Helper.safeSQL(submitter) + "', '" + Helper.safeSQL(server) + "', '" + Helper.safeSQL(submitter) + "', " + scheduleStatus + ", '" + Helper.safeSQL(cookieCrumbs) + "', '" + Helper.safeSQL(objInfoObject.getTitle()) + "', to_date('" + Helper.msSQLDateTime(creationTime) + "','MM/DD/YYYY HH12:MI:SS AM'), " + numPrompts + ",'" + logon1 + "','" + pswd1 + "', '" + logon2 + "', '" + pswd2 + "','" + errorMsg + "','" + machineUsed + "', to_date('" + Helper.msSQLDateTime(updateTS) + "','MM/DD/YYYY HH12:MI:SS AM'), '" + destination + "','" + outputFile + "', '" + objInfoObject.getParentCUID() + "', '" + parentFolderCuid + "','" + distributionList + "', '" + si_file1 + "','" + EnterpriseEnv + "','" + objInfoObject.getKind() + "'," + siFormatExportAllpages + "','" + objInfoObject.getCUID() + "','" + Helper.safeSQL(paramValues) + "')";
						//Helper.runMSSQLInsertQuery(msSQLInsertString);

						errorCategory = Helper.getErrorCategory(errorMsg);
						errorCategory = Helper.safeSQL(errorCategory);
						System.out.println("Error Category is " + errorCategory);

						if (paramValues.length() > 3000) {
							System.out.println("The param value string for " + objInfoObject.getID() + " exceeds 3000 char");
							paramValues = paramValues.substring(0, 2995);
						}
						try {
							msSQLInsertString = "insert into " + destTblNm + " (REPORT_ID, INSTANCE_ID, SI_STARTTIME, SI_ENDTIME,  SI_SUBMITTER, SI_SCHEDULE_STATUS, SI_KIND,  SI_NAME, SI_CREATION_TIME, CUID, REPORT_CUID, DURATION, DESTINATION, DISTRIBUTION_LIST, SI_MACHINE_USED, SI_NUM_PROMPTS, PARAM_VALUES, SI_ERROR_MESSAGE, DATASRC_LOGIN_NM, DATASRC_LOGIN_NM2, OUTPUT_FILE, SI_FILE1, ENVIRONMENT, SI_SERVER, ERROR_CATEGORY, EXPIRATION_TIME) values (" + objInfoObject.getParentID() + ", " + instanceID + ", ('" + Helper.msSQLDateTime(startTime) + "')" + ", ('" + Helper.msSQLDateTime(endTime) + "')" + ", '" + Helper.safeSQL(submitter) + "', " + scheduleStatus   + ", '" + objInfoObject.getKind() + "','" + Helper.safeSQL(objInfoObject.getTitle()) + "', ('" + Helper.msSQLDateTime(creationTime) + "') "   +  ",'"  +  cuid + "','" + reportCuid +  "'," + theInterval +", '" + destination + "','" + distributionList + "','" + machineUsed + "', " + numPrompts + ",'" + Helper.safeSQL(paramValues) +"', '" + errorMsg + "', '" + logon1 + "','" + logon2 +"','" + outputFile + "','" + filepath + "','" + env + "','" + server +"', '" + errorCategory + "', ('" + Helper.msSQLDateTime(expiryTime) + "'))";
							System.out.println(msSQLInsertString);
							Helper.runMSSQLInsertQuery(msSQLInsertString);	
						}
						catch (Exception ex){
							//There could be a constraint violation because now INSTANCE_ID must be unique
							System.out.println( ex.getMessage() + " " + ex.getStackTrace());
						}

					} //end if for schedule status

				}
				// Clean up the Enterprise Session.
				if(objEnterpriseSession != null) {
					try {
						objEnterpriseSession.logoff();

					} catch(Exception e_ignore_in_cleanup) {}
				}
			}

			else {
				System.out.println ("No report history found.");
			}
			// Clean up the Enterprise Session.
			if(objEnterpriseSession != null) {
				try {
					objEnterpriseSession.logoff();

				} catch(Exception e_ignore_in_cleanup) {}
			}
		}

		catch (Exception ex)
		{
			System.out.println(ex.getMessage() + " " + ex.getStackTrace());
			//Helper.runMSSQLInsertQuery("insert into STG1_BO4_TRACKER_ERRORS (ERROR_MESSAGE, LOAD_DT) VALUES ('" + ex.getMessage() + " " + ex.getStackTrace() + " " + EnterpriseEnv + "', GETDATE())");
			Helper.runMSSQLInsertQuery("insert into STG1_BO4_TRACKER_ERRORS (ERROR_MESSAGE, LOAD_DT) VALUES ( '" + ex.getMessage() + " " + ex.getStackTrace() + " " + EnterpriseEnv + "', GETDATE())");
			//'sendmail("Logon error", ex.getMessage() + " " + ex.getStackTrace)
			//Helper.sendmail("bishopp@lexmark.com", "bishopp@lexmark.com, amarabes@lexmark.com,ajpatel@lexmark.com,msankar@lexmark.com,ajain@lexmark.com", "Logon error - load_stg1_migr_XI_reports(String, String, String, String, String) - " + EnterpriseEnv, ex.getMessage() + " " + ex.getStackTrace());
			//Helper.sendmail("bishopp@lexmark.com", "bishopp@lexmark.com", "Logon error - load_stg1_boxi_runhist(String, String, String, String, String) - " + EnterpriseEnv, ex.getMessage() + " " + ex.getStackTrace());

		}
	}
	public static void load_stg1_BO_runhist_Date_Range (String EnterpriseEnv, String login, String pswd,String auth, String destTblNm, String env, String stDate, String endDt){
		//This is to run the loads by date range when the missed loads encompasses so many records that it throws a Java heap error
		IEnterpriseSession objEnterpriseSession;
		//IInfoStore objInfoStore;
		IInfoObjects colInfoObjects;
		IInfoObject objInfoObject;
		IInfoObject objInfoObject2;
		String errorMsg = "";
		String cookieCrumbs;
		int count;
		String si_owner = "";
		Calendar runDate;
		Calendar startTime= Calendar.getInstance();
		Calendar endTime= Calendar.getInstance();
		Calendar creationTime = Calendar.getInstance();
		Calendar updateTS = Calendar.getInstance();
		Calendar expiryTime = Calendar.getInstance();
		double theInterval = 0;
		Calendar startDate;
		Calendar endDate;
		String dtToday;
		String msSQLInsertString;
		String maxID = "0";
		String logon1 = "unknown";
		String logon2 = "unknown";
		String pswd1 = "unknown";
		String pswd2 = "unknown";
		String datasrc1 = "not found in CMS";
		String datasrc2 = "not found in CMS";
		int numPrompts = 0;
		Calendar maxEndDate;
		String destination = "";
		String outputFile = "";
		String distributionList = "";
		String filepath = "not set";
		String strInfoSQL;
		Calendar maxCreationDate;
		//java.sql.Date maxCreationDate;
		int i = 0;
		IFiles ifiles;
		String scheduleStatus = "999"; //means is not yet known.  I made this up because this is not a valid value.  Tells me it's not getting set from the recordset.
		IProcessingInfo processingInfo;
		IReport iReport;
		String server ="";
		String server1 = "";
		String server2 = "";
		IProperty aggCount;
		String strCount="0";
		int colCount = 0;
		Boolean si_format_export_allpages;
		String siFormatExportAllpages;
		String paramValues = "";
		//		IWebiPrompts iWebiPrompts;
		IPrompts webiPrompts;
		IReport oReport;
		String submitter = "";
		String machineUsed = "";
		String parentFolderCuid = "";
		String siOwner = "";
		String testString = "";
		int schedStatus;
		String siKind = "";
		String cuid = "";
		String reportCuid = "";
		int instanceID = 0;
		String errorMessage = "no error";
		//int numLogins = 0;
		String numLogins = "0";
		String errorCategory;

		try 
		{
			ISessionMgr  objSessionMgr = CrystalEnterprise.getSessionMgr();
			objEnterpriseSession = objSessionMgr.logon(login, pswd, EnterpriseEnv, auth);    
			IInfoStore objInfoStore = (IInfoStore) objEnterpriseSession.getService("InfoStore");

			maxID = Helper.getMaxID("select max(instance_id) maxID from " + destTblNm);
			System.out.println("maxID is " + maxID);
			//maxCreationDate = Helper.getMaxLoadDt("select max(si_creation_time) maxLoadDate from " + destTblNm);
			//System.out.println("Max creation date is " + maxCreationDate);
			maxEndDate = Helper.getMaxLoadDt("select max(si_endtime) maxLoadDate from " + destTblNm);
			System.out.println("Max end date is " + Helper.msSQLDateTime(maxEndDate));

			//4/26/2013:  In Enterprise, Analysis reports, SI_KIND='MDAnalysis', has objects where SI_INSTANCE=1.  These error out.
			//Added a filter to prevent them from showing up in the recordset.
			if (maxID == "0" || maxID == null) {
				//Is a first-time load
				strInfoSQL = "SELECT count(SI_ID) from CI_INFOOBJECTS WHERE SI_INSTANCE = 1 and si_recurring = 0 and SI_KIND != 'MDAnalysis'";
			}
			else
				//is an incremental load


				//*****************PRODUCTION CODE - 5/15/2014:  UNCOMMENT AFTER PRIOR LOADS****************

				strInfoSQL = "SELECT count(SI_ID) from CI_INFOOBJECTS WHERE SI_INSTANCE = 1 and SI_KIND != 'MDAnalysis' and si_recurring = 0 and si_endtime >= '" + stDate + "' and si_endtime <'" + endDt + "'";

			//*****************PRODUCTION CODE - 5/15/2014:  UNCOMMENT AFTER PRIOR LOADS****************
			//strInfoSQL = "SELECT count(SI_ID) from CI_INFOOBJECTS WHERE SI_INSTANCE = 1 and SI_KIND = 'Webi' and si_recurring = 0 and si_endtime >= '" + Helper.formatDateBO(maxEndDate) + "'" ;
			//strInfoSQL = "SELECT count(SI_ID) from CI_INFOOBJECTS WHERE SI_INSTANCE = 1 and si_recurring = 0 and si_creation_time >= '" + Helper.formatDateBO(maxCreationDate) + "' and si_schedule_status=3" ;

			System.out.println(strInfoSQL);

			colInfoObjects = (IInfoObjects)objInfoStore.query(strInfoSQL);
			//objInfoObject = (IInfoObject)colInfoObjects.get(i); 

			objInfoObject = (IInfoObject)colInfoObjects.get(0);

			//The VB code used the collection here, but in Java, properties() isn't an available function, so I am trying to get this via InfoObject

			//System.out.println(objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").properties().getProperty("SI_ID"));
			System.out.println(objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").getID());//shows the ID, not the count!
			//count = objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").properties().getProperty("SI_ID");
			//count = objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").getID();
			aggCount = objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT");

			count = objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").getID();
			strCount = ((IProperties)objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").getValue()).getProperty("SI_ID").getValue().toString();

			System.out.println("Record count is " + strCount);


			if (maxID == "0" || maxID == null) {
				//*****************PRODUCTION CODE - 5/15/2014:  UNCOMMENT AFTER PRIOR LOADS****************
				strInfoSQL = "SELECT TOP " + strCount + " * FROM CI_INFOOBJECTS WHERE  SI_INSTANCE = 1 and SI_KIND != 'MDAnalysis' and si_recurring=0";
				//*****************PRODUCTION CODE - 5/15/2014:  UNCOMMENT AFTER PRIOR LOADS****************


				///**********JUST TEMPORARY FOR TESTING*************
				//strInfoSQL = "SELECT TOP 100 * FROM CI_INFOOBJECTS WHERE  SI_INSTANCE = 1 and si_recurring=0";
				//strInfoSQL = "SELECT TOP 100 si_id, CUID, si_name, si_submitter, si_starttime, si_endtime, si_creation_time, si_schedule_status, si_kind, si_parent_cuid FROM CI_INFOOBJECTS WHERE  SI_INSTANCE = 1 and si_recurring=0";
				//strInfoSQL = "SELECT TOP 100 * FROM CI_INFOOBJECTS WHERE  SI_INSTANCE = 1 and si_recurring=0";
				//strInfoSQL = "SELECT TOP 100 * FROM CI_INFOOBJECTS WHERE  SI_ID = 1751209";
			}
			else
				//strInfoSQL = "SELECT TOP " + strCount + " SI_ID FROM CI_INFOOBJECTS WHERE SI_KIND in ('CrystalReport') and SI_INSTANCE = 0 and SI_CREATION_TIME >= '" + Helper.formatDateBO(maxCreationDate) + "'";

				//strInfoSQL = "SELECT  TOP " + strCount + " *  from CI_INFOOBJECTS WHERE SI_INSTANCE = 1 and SI_KIND != 'MDAnalysis' and si_recurring = 0 and si_endtime >= '2014.05.05.18.56.23' and si_endtime < '2014.05.06.00.00.00'";

				//*****************PRODUCTION CODE - 5/15/2014:  UNCOMMENT AFTER PRIOR LOADS****************
				////strInfoSQL = "SELECT TOP " + strCount + " * FROM CI_INFOOBJECTS WHERE  SI_INSTANCE = 1 and si_recurring=0 and SI_CREATION_TIME >= '" + Helper.formatDateBO(maxCreationDate) + "'";
				strInfoSQL = "SELECT  TOP " + strCount + " *  from CI_INFOOBJECTS WHERE SI_INSTANCE = 1 and SI_KIND != 'MDAnalysis' and si_recurring = 0 and si_endtime >= '" + stDate + "' and si_endtime <'" + endDt + "'";
			//*****************PRODUCTION CODE - 5/15/2014:  UNCOMMENT AFTER PRIOR LOADS****************

			//strInfoSQL = "SELECT TOP " + strCount + " * FROM CI_INFOOBJECTS WHERE  SI_INSTANCE = 1 and SI_KIND = 'Webi' and si_recurring=0 and SI_ENDTIME >= '" + Helper.formatDateBO(maxEndDate) + "'";



			///**********JUST TEMPORARY FOR TESTING*************
			//strInfoSQL = "SELECT  * FROM CI_INFOOBJECTS WHERE SI_ID=942134";
			//strInfoSQL = "SELECT TOP 100 si_id, CUID, si_name, si_submitter, si_starttime, si_endtime, si_creation_time, si_schedule_status, si_kind, si_parent_cuid FROM CI_INFOOBJECTS WHERE  SI_INSTANCE = 1 and si_recurring=0 and SI_ENDTIME >= '" + Helper.formatDateBO(maxEndDate) + "'";
			//strInfoSQL = "SELECT TOP 100 * FROM CI_INFOOBJECTS WHERE  SI_INSTANCE = 1 and si_recurring=0 and SI_ENDTIME >= '" + Helper.formatDateBO(maxEndDate) + "'";
			//strInfoSQL = "SELECT TOP " + strCount + " * FROM CI_INFOOBJECTS WHERE  SI_ID = 1751209";

			//JUST A TEST TO SEE HOW FAILED INSTANCES WORK IN THE CODE
			//strInfoSQL = "SELECT TOP " + strCount + " SI_ID FROM CI_INFOOBJECTS WHERE  SI_INSTANCE = 1 and si_recurring=0 and si_schedule_status=1 and SI_CREATION_TIME >= '" + Helper.formatDateBO(maxCreationDate) + "'";

			System.out.println(strInfoSQL);
			colInfoObjects = (IInfoObjects)objInfoStore.query(strInfoSQL);
			System.out.println("Line 1390 Count of objects in collection: " + colInfoObjects.size());

			if (colInfoObjects.size() > 0){
				/*Connection con=null;
				Class.forName("oracle.jdbc.driver.OracleDriver");
				con=DriverManager.getConnection("jdbc:oracle:thin:@dlxkrbodb1.lex.lexmark.com:1750:edr1den1","botrack","********");
				Statement s=con.createStatement();*/
				//'This records all the instances found on the BOXI system
				colCount = colInfoObjects.size();
				for (int x=0; x <= colCount; x++){
					//msSQLInsertString = "insert into temp_p_allinstances(report_id, instance_id) values (" + objInfoObject.getParentID() + "," + objInfoObject.getID() + ")";
					//System.out.println(msSQLInsertString);
					//s.execute(msSQLInsertString);
					errorMsg = "";  //resets the error message since it won't get overwritten each time.
					paramValues = ""; //resets the paramValues

					objInfoObject = (IInfoObject)colInfoObjects.get(x);
					//oReport = (IReport)colInfoObjects.get(x);  //4/27/2012:  causes error against 3.1 Prod: $Proxy0 cannot be cast to com.crystaldecisions.sdk.plugin.desktop.report.IReport [Ljava.lang.StackTraceElement;@aa559d
					//That's because this is an instance and it will throw this error if the object is not .rpt, which would usually be
					//the case.
					System.out.println("next report, si_id is " + objInfoObject.getID());
					//System.out.println ("si_id is " + objInfoObject.getID());

					try {
						scheduleStatus = objInfoObject.properties().getProperty("SI_SCHEDULE_STATUS").getValue().toString();
						schedStatus = objInfoObject.getSchedulingInfo().getStatus();
						System.out.println("scheduleStatus is " + scheduleStatus);
						System.out.println("schedStatus is " + schedStatus);
					} 
					catch (Exception ex) {
						System.out.println(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getStackTrace());
					}

					//Only do the rest if the schedule status is 1 or 3
					if (scheduleStatus.equals("1") || scheduleStatus.equals("3"))  {
						try {
							IProperties properties = objInfoObject.properties();
							IProperty startTimeProperty = properties.getProperty(CePropertyID.SI_STARTTIME);
							//String value = (String) creationTimeProperty.getValue();
							startTime.setTime ((Date) startTimeProperty.getValue());  //This works
							System.out.println("start time is " + Helper.msSQLDateTime(startTime));
						}
						catch (Exception ex) {
							System.out.println(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getStackTrace());
						}


						try {
							IProperties properties = objInfoObject.properties();
							IProperty endTimeProperty = properties.getProperty(CePropertyID.SI_ENDTIME);
							//String value = (String) creationTimeProperty.getValue();
							endTime.setTime ((Date) endTimeProperty.getValue());  //This works
							System.out.println("end time is " + Helper.msSQLDateTime(endTime));
						}
						catch (Exception ex) {
							System.out.println(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getStackTrace());
						}

						try {
							//if ((scheduleStatus.equals("1")) || (scheduleStatus.equals("3"))) {
							//runDate = (Calendar)objInfoObject.properties().getProperty("SI_STARTTIME").getValue();
							//startTime = runDate;
							//endTime = (Calendar)objInfoObject.properties().getProperty("SI_ENDTIME").getValue();
							//theInterval = 60/(endTime.getTimeInMillis() - startTime.getTimeInMillis())*1000;
							//}
							theInterval = (endTime.getTimeInMillis() - startTime.getTimeInMillis());
							System.out.println("the duration in milliseconds was " + theInterval);
							theInterval = theInterval/1000;
							//theInterval = 60/(endTime.getTimeInMillis() - startTime.getTimeInMillis())*1000;
							System.out.println("the duration in seconds was " + theInterval);
							theInterval = theInterval/60;
							System.out.println("the duration in minutes was " + theInterval);
						}
						catch (Exception ex) {
							System.out.println(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getStackTrace());
						}
						System.out.println("the duration was " + theInterval);

						try {
							//reportCuid.equals(objInfoObject.getParentCUID().toString());
							reportCuid = objInfoObject.getParentCUID().toString();
							System.out.println("Report cuid is " + reportCuid);
						}
						catch (Exception ex) {
							System.out.println(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getStackTrace());
						}


						try {
							//cuid.equals(objInfoObject.getCUID().toString());
							cuid = objInfoObject.getCUID().toString();
							System.out.println("Instance cuid is " + cuid); //watch out for AeVnF4VTRkdFsYYNxvwfFlI, an Analysis object
						}catch (Exception ex) {
							System.out.println(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getStackTrace());
						}


						try {
							instanceID = objInfoObject.getID();
							System.out.println("report instance id is " + instanceID);
						}
						catch (Exception ex) {
							System.out.println(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getStackTrace());
						}



						try {
							//IProperties properties = objInfoObject.properties();
							//IProperty submitterProperty = properties.getProperty(CePropertyID.SI_SUBMITTER);

							//submitter.equals(submitterProperty.toString());  //doesn't work
							//submitter = submitterProperty.toString();


							submitter =objInfoObject.getSchedulingInfo().properties().getProperty("SI_SUBMITTER").getValue().toString();
							System.out.println("submitter " + submitter);
						}
						catch (Exception ex) {
							System.out.println(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getStackTrace());
						}

						try {
							//Got this from D:\Documents and Settings\bishopp\My Documents\BOXI\Code Examples\xi4_boejava_dg_en\html\topic15.html
							IProperties properties = objInfoObject.properties();
							IProperty creationTimeProperty = properties.getProperty(CePropertyID.SI_CREATION_TIME);
							//String value = (String) creationTimeProperty.getValue();
							creationTime.setTime ((Date) creationTimeProperty.getValue());  //This works
							//System.out.println("creation time is " + Helper.msSQLDateTime(creationTime));
							System.out.println("creation time is " + creationTime.getTime());
						}
						catch (Exception ex)
						{
							ex.printStackTrace();
							System.out.println(" for report id: " +objInfoObject.getID() + " " + ex.getMessage() + " " + ex.getStackTrace());
						}

						try {
							//Got this from D:\Documents and Settings\bishopp\My Documents\BOXI\Code Examples\xi4_boejava_dg_en\html\topic15.html
							//IProperties properties = objInfoObject.properties();
							//IProperty expiryTimeProperty = properties.getProperty(CePropertyId.);
							//creationTime.setTime ((Date) creationTimeProperty.getValue());  //This works

							expiryTime.setTime((Date) objInfoObject.getSchedulingInfo().properties().getProperty("SI_ENDTIME").getValue());
							System.out.println("expiration time is " + expiryTime.getTime());
						}
						catch (Exception ex)
						{
							ex.printStackTrace();
							System.out.println(" for report id: " +objInfoObject.getID() + " " + ex.getMessage() + " " + ex.getStackTrace());
						}


						try {
							siKind.equals(objInfoObject.getKind());
							System.out.println("SI_KIND is " + objInfoObject.getKind().toString());
						} 
						catch (Exception ex) {
							System.out.println(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getStackTrace());
						}


						try {
							//destination = objInfoObject.getSchedulingInfo().getDestination().getName();  //--> destination was null, but it can't be!
							//destination = (String)objInfoObject.getSchedulingInfo().properties().getProperty("SI_DESTINATION").getValue();
							//4/26/2013:  In Enterprise, Analysis reports, SI_KIND='MDAnalysis', has objects where SI_INSTANCE=1.  These error out.

							//VB:  destination = objInfoObject.SendToDestination.Name and this works
							ISendable obj = (ISendable)objInfoObject;						
							IDestinationPlugin destinationPlugin = Helper.getDestinationPlugin(objInfoStore, objInfoObject.getKind());
							IDestination odestination = obj.getSendToDestination();	
							//odestination.getName();
							//odestination.setFromPlugin(destinationPlugin);//this was setting the destination to smtp, not getting what it is
							System.out.println("Line 1547: destination is " + odestination.getName());
							destination = odestination.getName();
							System.out.println("destination is " + destination + " for si_id " + objInfoObject.getID());
							//distributionList = Helper.getDistributionList("uslexbcs02", objInfoObject.getID());
							if (destination != null) {
								if (destination.equals("CrystalEnterprise.Smtp")) {
									System.out.println("Line 1653");
									System.out.println("getting email distribution list");
									//distributionList = Helper.getDistributionList("uslexbcs02", objInfoObject.getID());	
									//distributionList = Helper.getDistributionList(EnterpriseEnv, objInfoObject.getID()); //gives an error
									distributionList = Helper.getDistributionList1(objEnterpriseSession,EnterpriseEnv, objInfoObject.getID()); //gives an error
									System.out.println("Line 1657" + distributionList);
								}
								else
									System.out.println("Destination actually is:  " + destination);
							}

						}
						catch(SDKException sdkEx)
						{
							System.out.println("Line 1607 " + sdkEx.getDetail() + " " + sdkEx.getStackTrace());
							Helper.runMSSQLInsertQuery("insert into STG1_BO4_TRACKER_ERRORS (ERROR_MESSAGE, LOAD_DT) VALUES ('" + sdkEx.getMessage() + " " + sdkEx.getStackTrace() + " " + EnterpriseEnv + "', GETDATE())");
						}


						try {
							//get SI_MACHINE_USED, the job server the job ran on
							IProperties properties = objInfoObject.getSchedulingInfo().properties();
							IProperty machineUsedProperty = properties.getProperty(CePropertyID.SI_MACHINE_USED);
							//String value = (String) creationTimeProperty.getValue();
							machineUsed =  machineUsedProperty.toString();
							System.out.println("job server " + machineUsed);
						}
						catch(Exception Ex)
						{
							System.out.println("Line 1607 " + Ex.getMessage() + " " + Ex.getStackTrace());
							Helper.runMSSQLInsertQuery("insert into STG1_BO4_TRACKER_ERRORS (ERROR_MESSAGE, LOAD_DT) VALUES ('" + Ex.getMessage() + " " + Ex.getStackTrace() + " " + EnterpriseEnv + "', GETDATE())");
						}

						try {
							if (objInfoObject.getKind() == "Webi") {
								webiPrompts = (IPrompts)objInfoObject.properties().getProperty("SI_WEBI_PROMPTS").getValue();
								numPrompts = webiPrompts.size();
								logon1 = "n/a";
								logon2 = "n/a";
								server1 = "n/a";
								server2 = "n/a";
								if (numPrompts > 0 ) {
									//Getting the param list might not be possible
									//List paramList = webiPrompts.getReportParameters();
									Object paramList =  webiPrompts.toArray();
								}

							} else {
								//see if this will work for all file types, .rpt, .xls and .pdf

								// First get the Processing Info properties for the InfoObject.
								IProperties boProcessingInfo = (IProperties) objInfoObject.getProcessingInfo().properties();

								// Make sure that there is processing info set for the InfoObject.
								if (boProcessingInfo != null)
								{
									// Get the SI_PROMPTS property bag from the InfoObject's Processing Info
									IProperties siPrompts = (IProperties) boProcessingInfo.getProperty("SI_PROMPTS").getValue();

									// Make sure that there are parameters.
									if (siPrompts != null)
									{
										// Get the number of prompts (the number of parameters)
										numPrompts = ((Integer)siPrompts.getProperty("SI_NUM_PROMPTS").getValue()).intValue();
									}

									// Get the SI_LOGON_INFO property bag
									IProperties logonProperties = (IProperties) boProcessingInfo.getProperty("SI_LOGON_INFO").getValue();
									if (logonProperties != null) {
										IProperties silogonProperties = (IProperties) logonProperties.getProperty("SI_LOGON1").getValue();


										System.out.println("SI_LOGON1 size is " + silogonProperties.size());
										//16, it's true
										numLogins = logonProperties.getProperty("SI_NUM_LOGONS").toString();
										System.out.println("Num logons is " + numLogins);
										//System.out.println("logon1 is " + logon1);
										//Get the db logon credentials 
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



								//////////////ONLY WORKS FOR CRYSTAL REPORTS FORMAT
								/*iReport = (IReport) objInfoObject;  //can't do this if the report is not in CR format

								//Get the username 
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

								List paramList = iReport.getReportParameters(); //getting an error here
								//Line 1607 The operation is not supported [Ljava.lang.StackTraceElement;@12b9f14
								//ORA-00923: FROM keyword not found where expected
								// [Ljava.lang.StackTraceElement;@1c67b54 runMSSQLInsertQuery line 632
								numPrompts = paramList.size();*/
								/////////////////////////////////////////////

							}


							//The number of prompts is stored differently between Crystal Reports and Webi docs
							//Crystal: SI_PROMPTS
							//Webi:  SI_WEBI_PROMPTS
							//Xcelsius:  


						} catch (Exception ex) {
							System.out.println("Logons " + ex.getMessage() + " " + ex.getStackTrace());
							//IF IS EXCEL OUTPUT
							try {
								IExcel eReport = (IExcel)objInfoObject;
								//Get the username 
								//									ISDKList dbLogons = eReport.getReportLogons();
								IProperties allProperty = (IProperties)eReport.properties();
								//IProperties pathProperty = allProperty.getProperties("SI_FILES");
								//IProperty logonProperty = eReport.getProcessingInfo().properties().getProperty("SI_LOGONS")
								IProperties logonProperties = allProperty.getProperties("SI_LOGON_INFO");
								IProperties logon1Properties = logonProperties.getProperties("SI_LOGON1");
								//IProperty userProperty = logon1Properties.getProperty("SI_USER");


								//Get the db logon credentials 
								for (int h = 0; h < logon1Properties.size(); h++) {	
									IReportLogon dbLogon = (IReportLogon)logon1Properties.get(h);
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
								//List paramList = eReport.getReportParameters(); //getting an error here
								//Line 1607 The operation is not supported [Ljava.lang.StackTraceElement;@12b9f14
								//ORA-00923: FROM keyword not found where expected
								// [Ljava.lang.StackTraceElement;@1c67b54 runMSSQLInsertQuery line 632

								IProperties paramProperties = allProperty.getProperties("SI_PROMPTS");
								numPrompts = ((Integer)paramProperties.getProperty("SI_NUM_PROMPTS").getValue()).intValue();
								System.out.println("nbr prompts is" + numPrompts);
								//numPrompts = paramList.size();
							}
							catch (Exception ex1){
								System.out.println("excel prompts " + ex.getMessage() + " " + ex.getStackTrace());
							}

							//IF IS PDF OUTPUT
							try {
								IPDF pReport = (IPDF)objInfoObject;
								//Get the username 
								//ISDKList dbLogons = eReport.getReportLogons();
								IProperties allProperty = (IProperties)pReport.properties();
								//IProperties pathProperty = allProperty.getProperties("SI_FILES");
								//IProperty logonProperty = eReport.getProcessingInfo().properties().getProperty("SI_LOGONS")
								IProperties logonProperties = allProperty.getProperties("SI_LOGON_INFO");
								IProperties logon1Properties = logonProperties.getProperties("SI_LOGON1"); //get error here for a PDF file
								//$Proxy0 cannot be cast to com.crystaldecisions.sdk.plugin.desktop.report.IReport [Ljava.lang.StackTraceElement;@92af24
								IProperty userProperty = logon1Properties.getProperty("SI_USER");


								//Get the db logon credentials 
								for (int h = 0; h < logon1Properties.size(); h++) {	
									IReportLogon dbLogon = (IReportLogon)logon1Properties.get(h);
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
								//List paramList = eReport.getReportParameters(); //getting an error here
								//Line 1607 The operation is not supported [Ljava.lang.StackTraceElement;@12b9f14
								//ORA-00923: FROM keyword not found where expected
								// [Ljava.lang.StackTraceElement;@1c67b54 runMSSQLInsertQuery line 632

								IProperties paramProperties = allProperty.getProperties("SI_PROMPTS");
								numPrompts = ((Integer)paramProperties.getProperty("SI_NUM_PROMPTS").getValue()).intValue();
								System.out.println("nbr prompts is" + numPrompts);
								//numPrompts = paramList.size();
							}
							catch (Exception ex1){
								System.out.println("PDF prompts " + ex.getMessage() + " " + ex.getStackTrace());
							}

							//IF IS TEXT OUTPUT
							try {
								ITxt tReport = (ITxt)objInfoObject;
								//Get the username 
								//ISDKList dbLogons = eReport.getReportLogons();
								IProperties allProperty = (IProperties)tReport.properties();
								//IProperties pathProperty = allProperty.getProperties("SI_FILES");
								//IProperty logonProperty = eReport.getProcessingInfo().properties().getProperty("SI_LOGONS")
								IProperties logonProperties = allProperty.getProperties("SI_LOGON_INFO");
								IProperties logon1Properties = logonProperties.getProperties("SI_LOGON1");
								IProperty userProperty = logon1Properties.getProperty("SI_USER");


								//Get the db logon credentials 
								for (int h = 0; h < logon1Properties.size(); h++) {	
									IReportLogon dbLogon = (IReportLogon)logon1Properties.get(h);
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
								//List paramList = eReport.getReportParameters(); //getting an error here
								//Line 1607 The operation is not supported [Ljava.lang.StackTraceElement;@12b9f14
								//ORA-00923: FROM keyword not found where expected
								// [Ljava.lang.StackTraceElement;@1c67b54 runMSSQLInsertQuery line 632

								IProperties paramProperties = allProperty.getProperties("SI_PROMPTS");
								numPrompts = ((Integer)paramProperties.getProperty("SI_NUM_PROMPTS").getValue()).intValue();
								System.out.println("nbr prompts is" + numPrompts);
								//numPrompts = paramList.size();
							}
							catch (Exception ex1){
								System.out.println("txt prompts " + ex.getMessage() + " " + ex.getStackTrace());
							}


							System.out.println("prompts " + ex.getMessage() + " " + ex.getStackTrace());
						}


						try {
							paramValues = Helper.getWebiParamValueString(objInfoObject);
							if (paramValues.length() == 0 ){
								paramValues = Helper.getParamValueString(objInfoObject);
							}	

							System.out.println("param values is " + paramValues);
						}
						catch (Exception ex) {

						}


						try {
							if (scheduleStatus.equals("3")) {
								errorMsg = objInfoObject.getSchedulingInfo().getErrorMessage().toString();
								errorMsg = errorMsg.replaceAll("[\\r\\n]", " - ");
								System.out.println(errorMsg);	
							} else
								errorMsg = "no error";
						}
						catch (Exception ex) {

						}

						try
						{
							switch (objInfoObject.getSchedulingInfo().getStatus()) {
							case ISchedulingInfo.ScheduleStatus.COMPLETE:
								System.out.println("schedule status is  " + scheduleStatus);
								//success = true;
								break;
							case ISchedulingInfo.ScheduleStatus.FAILURE:
								//log("Error", objInfoObject.getSchedulingInfo().getErrorMessage());
								//return false;
								errorMsg = Helper.safeSQL(objInfoObject.getSchedulingInfo().getErrorMessage());
								//errorMsg.replaceAll("\r", " - ");
								errorMsg = errorMsg.replaceAll("[\\r\\n]", " - ");  //replaces the carriage return-line feed characters
								//System.out.println(errorMsg);
							}
							/*if (scheduleStatus == "3") {
									//errorMsg = Helper.safeSQL((String)objInfoObject.properties().getProperty("SI_ERROR_MESSAGE").getValue());
									errorMsg = Helper.safeSQL(objInfoObject.getSchedulingInfo().getErrorMessage());
									//errorMsg.replaceAll("\r", " - ");
									errorMsg.replaceAll("[\\r\\n]", " - ");  //replaces the carriage return-line feed characters

								}
								else
									errorMsg = "";*/
						}

						catch (Exception ex)
						{
							//This is old VB.Net COM code to get the error message if it isn't in the SI_ERROR_MESSAGE property bag
							//I will use a catch-all for now

							//Cast the IInfo Object to a IReport object to use the methods of a report
							Helper.runMSSQLInsertQuery("insert into STG1_BO4_TRACKER_ERRORS (ERROR_MESSAGE, LOAD_DT) VALUES ('" + ex.getMessage() + " " + ex.getStackTrace() + " " + EnterpriseEnv + "', GETDATE())");
						}


						try {
							IProperties properties = objInfoObject.getProcessingInfo().properties();
							IProperty siFilesProperty = properties.getProperty(CePropertyID.SI_FILES);
							//IProperty outputFileProperty = properties.getProperty(CePropertyID.SI_FILE1);  //not a valid property
							//ifiles = (IFiles)objInfoObject.getProcessingInfo().properties().getProperty("SI_FILES").getValue(); 
							//ifiles = (IFiles) objInfoObject.getProcessingInfo().properties().getProperty(CePropertyID.SI_FILES);

							//IProperties properties = objInfoObject.getSchedulingInfo().properties();
							//IProperty machineUsedProperty = properties.getProperty(CePropertyID.SI_MACHINE_USED);
							//machineUsed =  machineUsedProperty.toString();


							//com.crystaldecisions.sdk.properties.internal.SDKPropertyBag cannot be cast to com.crystaldecisions.sdk.occa.infostore.IFiles
							//outputFile = (String)ifiles.properties().getProperty("SI_PATH").getValue();


							outputFile = siFilesProperty.toString();
							System.out.println("output file is " + outputFile);
							//Some examples:
							//output file is 3&n=1,03&021=8706eb801668d1a34.rpt,0P&0?1=23552,03&S=frs://Input/a_072/129/000/33096/,0P
							//output file is 3&S=frs://Input/a_139/106/013/879243/,0P&n=1,03&021=~ce3338642d37850e14.rpt,0P&0?1=36352,03
							//output file is 3&n=1,03&021=~ce1c305fc86a64730.rpt,0P&0?1=15872,03&S=frs://Input/a_028/150/000/38428/,0P
							//output file is 3&S=frs://Input/a_035/168/000/43043/,0P&n=2,03&021=~ce22f85e742bcf600.rpt,0P&022=~ce22f85e742bd27d1.jpeg,0P&0?1=57856,03&0?2=41296,03
							//output file is 3&S=frs://Output/a_211/176/013/897235/,0P&n=1,03&021=~ce1b005e880782f10.wid,0P&0?1=34802,03
							//Why are some of these in the Input FRS?


							//outputFile = siFilesProperty.getValue(2);  //doesn't work

							//filepath = siFilesProperty.toString(); //use this till you can get the correct code
							//doesn't have all the options to get to the right property
							//si_path
							//si_file1
							//si_num_files
							//si_value1

							IProperties allProperty = (IProperties)objInfoObject.properties();
							IProperties pathProperty = allProperty.getProperties("SI_FILES");
							IProperty path = (IProperty)pathProperty.getProperty("SI_PATH");
							Object getPath = path.getValue();
							filepath = getPath.toString();
							System.out.println("file path is " + filepath);

							IProperty outputFileProperty  = (IProperty)pathProperty.getProperty("SI_FILE1");
							outputFile = outputFileProperty.getValue().toString();
							System.out.println("output file is " + outputFile);

						} 
						catch (Exception Ex) {
							System.out.println("si_file1 and output file " + Ex.getMessage() + " " + Ex.getStackTrace());
						}


						///*****************Production Insert STring*************
						//msSQLInsertString = "insert into " + destTblNm + " (REPORT_ID, INSTANCE_ID, SI_STARTTIME, SI_ENDTIME, DURATION, SI_SUBMITTER, SI_SERVER, SI_OWNER, SI_SCHEDULE_STATUS,REPORT_PATH, SI_NAME, SI_CREATION_TIME, SI_NUM_PROMPTS, DATASRC_LOGIN_NM, SI_PASSWORD, DATASRC_LOGIN_NM2, SI_PASSWORD2, SI_ERROR_MESSAGE, SI_MACHINE_USED, SI_UPDATE_TS, DESTINATION, OUTPUT_FILE, REPORT_CUID, SI_PARENT_FOLDER_CUID, DISTRIBUTION_LIST, si_file1, ENVIRONMENT, SI_KIND, SI_FORMAT_EXPORT_ALLPAGES, CUID, PARAM_VALUES) values (" + objInfoObject.getParentID() + ", " + objInfoObject.getID() + ", to_date('" + Helper.oracleDate(startTime) + "','MM/DD/YYYY HH12:MI:SS AM')" + ", to_date('" + Helper.oracleDate(endTime) + "','MM/DD/YYYY HH12:MI:SS AM')" + ", " + theInterval + ", '" + Helper.safeSQL(submitter) + "', '" + Helper.safeSQL(server) + "', '" + Helper.safeSQL(submitter) + "', " + scheduleStatus + ", '" + Helper.safeSQL(cookieCrumbs) + "', '" + Helper.safeSQL(objInfoObject.getTitle()) + "', to_date('" + Helper.msSQLDateTime(creationTime) + "','MM/DD/YYYY HH12:MI:SS AM'), " + numPrompts + ",'" + logon1 + "','" + pswd1 + "', '" + logon2 + "', '" + pswd2 + "','" + errorMsg + "','" + machineUsed + "', to_date('" + Helper.msSQLDateTime(updateTS) + "','MM/DD/YYYY HH12:MI:SS AM'), '" + destination + "','" + outputFile + "', '" + objInfoObject.getParentCUID() + "', '" + parentFolderCuid + "','" + distributionList + "', '" + si_file1 + "','" + EnterpriseEnv + "','" + objInfoObject.getKind() + "'," + siFormatExportAllpages + "','" + objInfoObject.getCUID() + "','" + Helper.safeSQL(paramValues) + "')";
						//Helper.runMSSQLInsertQuery(msSQLInsertString);

						errorCategory = Helper.getErrorCategory(errorMsg);
						errorCategory = Helper.safeSQL(errorCategory);
						System.out.println("Error Category is " + errorCategory);

						if (paramValues.length() > 3000) {
							System.out.println("The param value string for " + objInfoObject.getID() + " exceeds 3000 char");
							paramValues = paramValues.substring(0, 2995);
						}

						msSQLInsertString = "insert into " + destTblNm + " (REPORT_ID, INSTANCE_ID, SI_STARTTIME, SI_ENDTIME,  SI_SUBMITTER, SI_SCHEDULE_STATUS, SI_KIND,  SI_NAME, SI_CREATION_TIME, CUID, REPORT_CUID, DURATION, DESTINATION, DISTRIBUTION_LIST, SI_MACHINE_USED, SI_NUM_PROMPTS, PARAM_VALUES, SI_ERROR_MESSAGE, DATASRC_LOGIN_NM, DATASRC_LOGIN_NM2, OUTPUT_FILE, SI_FILE1, ENVIRONMENT, SI_SERVER, ERROR_CATEGORY, EXPIRATION_TIME) values (" + objInfoObject.getParentID() + ", " + instanceID + ", ('" + Helper.msSQLDateTime(startTime) + "')" + ", ('" + Helper.msSQLDateTime(endTime) + "')" + ", '" + Helper.safeSQL(submitter) + "', " + scheduleStatus   + ", '" + objInfoObject.getKind() + "','" + Helper.safeSQL(objInfoObject.getTitle()) + "', ('" + Helper.msSQLDateTime(creationTime) + "') "   +  ",'"  +  cuid + "','" + reportCuid +  "'," + theInterval +", '" + destination + "','" + distributionList + "','" + machineUsed + "', " + numPrompts + ",'" + Helper.safeSQL(paramValues) +"', '" + errorMsg + "', '" + logon1 + "','" + logon2 +"','" + outputFile + "','" + filepath + "','" + env + "','" + server +"', '" + errorCategory + "', ('" + Helper.msSQLDateTime(expiryTime) + "'))";
						System.out.println(msSQLInsertString);
						Helper.runMSSQLInsertQuery(msSQLInsertString);

					} //end if for schedule status

				}
				// Clean up the Enterprise Session.
				if(objEnterpriseSession != null) {
					try {
						objEnterpriseSession.logoff();

					} catch(Exception e_ignore_in_cleanup) {}
				}
			}

			else {
				System.out.println ("No report history found.");
			}
			// Clean up the Enterprise Session.
			if(objEnterpriseSession != null) {
				try {
					objEnterpriseSession.logoff();

				} catch(Exception e_ignore_in_cleanup) {}
			}
		}

		catch (Exception ex)
		{
			System.out.println(ex.getMessage() + " " + ex.getStackTrace());
			Helper.runMSSQLInsertQuery("insert into STG1_BO4_TRACKER_ERRORS (ERROR_MESSAGE, LOAD_DT) VALUES ('" + ex.getMessage() + " " + ex.getStackTrace() + " " + EnterpriseEnv + "', GETDATE())");
			//'sendmail("Logon error", ex.getMessage() + " " + ex.getStackTrace)
			//Helper.sendmail("bishopp@lexmark.com", "bishopp@lexmark.com, amarabes@lexmark.com,ajpatel@lexmark.com,msankar@lexmark.com,ajain@lexmark.com", "Logon error - load_stg1_migr_XI_reports(String, String, String, String, String) - " + EnterpriseEnv, ex.getMessage() + " " + ex.getStackTrace());
			//Helper.sendmail("bishopp@lexmark.com", "bishopp@lexmark.com", "Logon error - load_stg1_boxi_runhist(String, String, String, String, String) - " + EnterpriseEnv, ex.getMessage() + " " + ex.getStackTrace());

		}
	}

	public static void load_stg1_universes(String EnterpriseEnv , String login, String pswd, String auth, String destTblNm ){
		String strInfoSQL ;
		//Logon;
		//ISessionMgr objSessionMgr = new CrystalEnterprise.getSessionMgr(); //should be New()


		IEnterpriseSession objEnterpriseSession;
		//IInfoStore objInfoStore;
		IInfoObjects colInfoObjects;
		IInfoObject objInfoObject;
		IInfoObjects colInfoObjects2;   
		IInfoObject objInfoObject2;
		String strCount = "0";	
		String cookiecrumbs;
		String maxid;
		String msSQLInsertString;	
		Calendar maxCreationDate;		
		IEnterpriseSession eSession;
		IProperty aggCount;
		int colCount = 0;
		int dataConnection =0;
		IProperties dataConnectionProps;
		Calendar creationTime = Calendar.getInstance();
		Calendar updateTime = Calendar.getInstance();
		Object getFile = null;

		try
		{
			ISessionMgr  objSessionMgr = CrystalEnterprise.getSessionMgr();
			objEnterpriseSession = objSessionMgr.logon(login, pswd, EnterpriseEnv, auth);    
			IInfoStore objInfoStore = (IInfoStore) objEnterpriseSession.getService("InfoStore");

			//Since I learned that si_id can be reused by CMS,I started using dates to do incrementals (03/30/2007)

			System.out.println("select max(si_creation_time) from " + destTblNm);
			maxCreationDate = Helper.getMaxLoadDt("select max(si_creation_time) maxLoadDate from " + destTblNm);
			System.out.println("Max creation date is " + maxCreationDate);
			maxid = Helper.getMaxID("select max(si_id) maxID from " + destTblNm);

			//maxid = "0";  I don't know why I hard-coded maxid to be zero.  This means a full load of universe meta data every hour, which is not good.

			if (maxid == "0")  {
				//'strInfoSQL = "SELECT count(SI_ID) from CI_INFOOBJECTS WHERE SI_PROGID= 'CrystalEnterprise.Report' and SI_INSTANCE = 0"
				strInfoSQL = "SELECT count(SI_ID) from CI_APPOBJECTS WHERE SI_KIND in ('Universe')";
			}
			else
				//'strInfoSQL = "SELECT count(SI_ID) from CI_INFOOBJECTS WHERE SI_PROGID= 'CrystalEnterprise.Report' and SI_INSTANCE = 0 and si_creation_time > '" + formatDateBO(maxCreationDate) + "'"
				//'strInfoSQL = "SELECT count(SI_ID) from CI_INFOOBJECTS WHERE SI_PROGID= 'CrystalEnterprise.Report' and SI_INSTANCE = 0 and si_creation_time >= '" + formatDateBO(maxCreationDate) + "'"  '4/18/2007
				strInfoSQL = "SELECT count(SI_ID) from CI_APPOBJECTS WHERE SI_KIND in ('Universe') and si_creation_time > '" + Helper.formatDateBO(maxCreationDate) + "'" ;

			//''***************PRODUCTION CODE***********UNCOMMENT AFTER TEST

			System.out.println(strInfoSQL);

			//EXAMPLE OF RETRIEVING PROPERTY BAG VALUES
			//objInfoObject.properties().getProperty("SI_UPDATE_TS");

			colInfoObjects = (IInfoObjects)objInfoStore.query(strInfoSQL);
			//objInfoObject = (IInfoObject)colInfoObjects.get(i);    
			objInfoObject = (IInfoObject)colInfoObjects.get(0);

			//The VB code used the collection here, but in Java, properties() isn't an available function, so I am trying to get this via InfoObject

			//System.out.println(objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").properties().getProperty("SI_ID"));
			//System.out.println(objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").getID());//shows the ID, not the count!

			aggCount = objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT");

			//count = aggCount.getID().byteValue(); //that's not it
			strCount = ((IProperties)objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").getValue()).getProperty("SI_ID").getValue().toString();

			System.out.println("Record count is " + strCount);
			//count = (int)strCount;

			//System.out.println("Record count is " + aggCount.getValue().toString());

			//''***************PRODUCTION CODE***********UNCOMMENT AFTER TEST
			if (maxid == "0") {
				strInfoSQL = "SELECT TOP " + strCount + " SI_ID FROM CI_APPOBJECTS WHERE SI_KIND in ('Universe')";
			}
			else
				//'strInfoSQL = "SELECT TOP " + count + " SI_ID FROM CI_INFOOBJECTS WHERE SI_PROGID= 'CrystalEnterprise.Report' and SI_INSTANCE = 0 and SI_ID>" + CInt(maxid)
				//'strInfoSQL = "SELECT TOP " + count + " SI_ID FROM CI_INFOOBJECTS WHERE SI_PROGID= 'CrystalEnterprise.Report' and SI_INSTANCE = 0 and SI_CREATION_TIME > '" + formatDateBO(maxCreationDate) + "'"
				strInfoSQL = "SELECT TOP " + strCount + " SI_ID FROM CI_APPOBJECTS WHERE SI_KIND in ('Universe')  and SI_CREATION_TIME > '" + Helper.formatDateBO(maxCreationDate) + "'";

			//''***************PRODUCTION CODE***********UNCOMMENT AFTER TEST
			System.out.println("line 1698 " + strInfoSQL);

			colInfoObjects = (IInfoObjects)objInfoStore.query(strInfoSQL);

			if (colInfoObjects.size() > 0)  {
				System.out.println("We do have objects in collection");		

				//'System.out.println("creation time  " + objInfoObject2.Properties.Item("SI_CREATION_TIME").getValue)

				//oReportAppFactory = (IReportAppFactory)objEnterpriseSession.getService("", "RASReportService");
				System.out.println("collection size is " + colInfoObjects.size());
				colCount = colInfoObjects.size();
				for (int k =0; k < colCount; k++)
				{
					System.out.println("iterator, k, is now " + k);
					objInfoObject = (IInfoObject) colInfoObjects.get(k);

					//'throws an error when I let it just rip through the query result.
					strInfoSQL = "Select * From CI_APPOBJECTS Where SI_INSTANCE=0 and SI_ID =" + objInfoObject.getID();
					System.out.println(strInfoSQL);
					colInfoObjects2 = (IInfoObjects)objInfoStore.query(strInfoSQL);
					int colCount2 = colInfoObjects2.size();

					for (int j =0; j < colCount2; j++)
					{

						objInfoObject2 = (IInfoObject)colInfoObjects2.get(j);	

						cookiecrumbs = Helper.BuildUniversePath(objEnterpriseSession, objInfoObject2.getParentID(), "");

						//cookiecrumbs = Helper.getMyString("12345678");

						System.out.println("Cookiecrumb trail is " + cookiecrumbs);
						System.out.println("Cookiecrumb length is " + cookiecrumbs.length());

						if (cookiecrumbs.length() > 0) {
							//'If the cookiecrumbs ends up null, then it was an Inbox document,
							//'which I don't want.
							System.out.println("cookiecrumbs length > 0");


							System.out.println("objInfoObject2 id:  " + objInfoObject2.getID());
							/*System.out.println("server1 length is " + server1.length());
							if (server1.length() > 0) {
							}*/
							try
							{

								IProperties properties = objInfoObject2.properties();
								IProperty creationTimeProperty = properties.getProperty(CePropertyID.SI_CREATION_TIME);
								//String value = (String) creationTimeProperty.getValue();
								creationTime.setTime ((Date) creationTimeProperty.getValue());  //This works
								System.out.println("creation time is " + creationTime.getTime());
							}
							catch (Exception ex)
							{
								ex.printStackTrace();
								System.out.println(" for report id: " +objInfoObject2.getID() + " " + ex.getMessage() + " " + ex.getStackTrace());
							}


							try
							{
								updateTime.setTime((Date) objInfoObject2.properties().getProperty("SI_UPDATE_TS").getValue());
								System.out.println(updateTime);

							}

							catch (Exception ex)
							{
								ex.printStackTrace();
								System.out.println(" for report id: " +objInfoObject2.getID() + " " + ex.getMessage() + " " + ex.getStackTrace());
							}


							try
							{
								dataConnectionProps = (IProperties)objInfoObject2.properties().getProperty("SI_DATACONNECTION").getValue();
								dataConnection = (Integer)dataConnectionProps.getProperty("1").getValue();

								System.out.println("dataConnection is " + dataConnection);

								//objInfoObject2.properties().getProperty("SI_DATACONNECTION").Properties.Item("1").Value
							}
							catch (Exception ex)
							{
								//throw new RuntimeException(ex);
							}

							try
							{
								//get SI_FILES.SI_FILE1
								//IFiles theFiles = (IFiles)objInfoObject2.getFiles();
								//getFile = theFiles.get(1).toString();
								//System.out.println("The file name is " + getFile);
								//not the answer I expected and can't be right




								IProperties allProperty = (IProperties)objInfoObject2.properties();
								IProperties SI_FILESProperty = allProperty.getProperties("SI_FILES");						

								//IProperty file = (IProperty)pathProperty.getProperty("SI_FILE1");
								//IProperty file = (IProperty)SI_FILESProperty.getProperty("SI_FILE1").getValue();
								IProperty file = (IProperty)SI_FILESProperty.getProperty("SI_FILE1"); //always says is null but in Query Builder it is not null!!
								getFile = file.getValue().toString();
								//String filename = getFile.toString;

								System.out.println("File name is " + getFile);


							}
							catch (Exception ex)
							{
								//throw new RuntimeException(ex);
								System.out.println(" for object: " +objInfoObject2.getTitle() + " " + ex.getMessage() + " " + ex.getStackTrace());
							}


							//msSQLInsertString = "INSERT INTO " + destTblNm + " (SI_ID, SI_PATH, SI_FILE1, REPORT_NM, REPORT_FILE_PATH, DATA_SOURCE_NM, DATA_SOURCE2_NM, DYNAMIC_PICK_LIST_NM, LOAD_DT, SI_CREATION_TIME, SI_UPDATE_TS, SOURCE_SYSTEM, CUID, SI_BUSINESS_VIEW_INFO, SI_LOCAL_FILEPATH, ORIGINAL_FILE_NM, SI_PARENT_FOLDER_CUID, SI_OWNER) VALUES (" + objInfoObject2.getID() + ", '" + Helper.safeSQL(si_path) + "', '" + Helper.safeSQL(si_file1) + "', '" + Helper.safeSQL(objInfoObject2.getTitle()) + "', '" + Helper.safeSQL(cookiecrumbs) + "','" + server1 + "', '" + server2 + "','See STG1_MIGR_RPT_PICK_LIST_REL',sysdate, to_date('" + objInfoObject2.properties().getProperty("SI_CREATION_TIME").getValue() + "','MM/DD/YYYY HH12:MI:SS AM'), to_date('" + objInfoObject2.properties().getProperty("SI_UPDATE_TS").getValue() + "','MM/DD/YYYY HH12:MI:SS AM'), '" + sourceSystem + "','" + objInfoObject2.getCUID() + "','" + Helper.safeSQL(businessViewNm) + "',' " + Helper.safeSQL(localFilePath) + "','" + originalFileNm + "', '" + objInfoObject2.properties().getProperty("SI_PARENT_FOLDER_CUID").getValue() + "','" + si_owner + "')";
							msSQLInsertString = "INSERT INTO " + destTblNm + " (CUID, SI_KIND, SI_UPDATE_TS, SI_NAME, SI_ID, SI_PARENT_FOLDER_CUID, SI_DESCRIPTION, SI_DATACONNECTION, ENVIRONMENT, PATH, SI_CREATION_TIME, FILE_NM, DATASOURCE_TYPE) VALUES ('" + objInfoObject2.getCUID() + "','" + objInfoObject2.getKind() + "',  ('" + Helper.msSQLDateTime(updateTime) + "')" + ", '" + Helper.safeSQL(objInfoObject2.getTitle()) + "', " + objInfoObject2.getID() + ", '" + objInfoObject2.properties().getProperty("SI_PARENT_FOLDER_CUID").getValue() + "','" + Helper.safeSQL(objInfoObject2.properties().getProperty("SI_DESCRIPTION").getValue().toString()) + "'," + dataConnection + ",'" + EnterpriseEnv + "','" + Helper.safeSQL(cookiecrumbs) + "',  ('" + Helper.msSQLDateTime(creationTime) + "')" + ", ' " + getFile + "','unv')";
							//oraInserter.CommandText = msSQLInsertString;
							System.out.println(msSQLInsertString);
							//'Comment out just to see if the getCEReportParams method is working
							try
							{
								Helper.runMSSQLInsertQuery(msSQLInsertString);
							}
							catch (Exception ex)
							{
								ex.printStackTrace();
								System.out.println(" for report id: " + objInfoObject2.getID() + " " + ex.getMessage() + " " + ex.getStackTrace());
								//'Records the skipped si_id
								msSQLInsertString = "insert into temp_p_skippedrptids (report_id) values (" + objInfoObject2.getID() + ")";
								Helper.runMSSQLInsertQuery(msSQLInsertString);
							}






						}

					}

				}

				objSessionMgr = null;
				//objEnterpriseSession = Nothing;
				objInfoStore = null;
				colInfoObjects = null;
				objInfoObject = null;
				colInfoObjects2 = null;
				objInfoObject2 = null;

			}
			// Clean up the Enterprise Session.
			if(objEnterpriseSession != null) {
				try {
					objEnterpriseSession.logoff();

				} catch(Exception e_ignore_in_cleanup) {}
			}
		}
		catch (Exception ex)
		{
			System.out.println(ex.getMessage());
			ex.printStackTrace(System.out);
			Helper.runMSSQLInsertQuery("insert into STG1_BO4_TRACKER_ERRORS (ERROR_MESSAGE, LOAD_DT) VALUES ('" + ex.getMessage() + " " + ex.getStackTrace() + " " + EnterpriseEnv + "', GETDATE())");
			//'sendmail("Logon error", ex.getMessage() + " " + ex.getStackTrace)
			//Helper.sendmail("bishopp@lexmark.com", "bishopp@lexmark.com, amarabes@lexmark.com,ajpatel@lexmark.com,msankar@lexmark.com,ajain@lexmark.com", "Logon error - load_stg1_migr_XI_reports(String, String, String, String, String) - " + EnterpriseEnv, ex.getMessage() + " " + ex.getStackTrace());
			Emailer.sendEmail("bishopp@lexmark.com", "bishopp@lexmark.com", "Logon error - load_stg1_boxi_universes(String, String, String, String, String) - " + EnterpriseEnv, ex.getMessage() + " " + ex.getStackTrace(), null);

		}
	}

	public static void load_stg1_unx_universes(String EnterpriseEnv , String login, String pswd, String auth, String destTblNm ){
		String strInfoSQL ;
		//Logon;
		//ISessionMgr objSessionMgr = new CrystalEnterprise.getSessionMgr(); //should be New()


		IEnterpriseSession objEnterpriseSession;
		//IInfoStore objInfoStore;
		IInfoObjects colInfoObjects;
		IInfoObject objInfoObject;
		IInfoObjects colInfoObjects2;   
		IInfoObject objInfoObject2;
		String strCount = "0";	
		String cookiecrumbs;
		String maxid;
		String msSQLInsertString;	
		Calendar maxCreationDate;		
		IEnterpriseSession eSession;
		IProperty aggCount;
		int colCount = 0;
		int dataConnection =0;
		IProperties dataConnectionProps;
		Calendar creationTime = Calendar.getInstance();
		Calendar updateTime = Calendar.getInstance();
		Object getFile = null;

		try
		{
			ISessionMgr  objSessionMgr = CrystalEnterprise.getSessionMgr();
			objEnterpriseSession = objSessionMgr.logon(login, pswd, EnterpriseEnv, auth);    
			IInfoStore objInfoStore = (IInfoStore) objEnterpriseSession.getService("InfoStore");

			//Since I learned that si_id can be reused by CMS,I started using dates to do incrementals (03/30/2007)

			System.out.println("select max(si_creation_time) from " + destTblNm);
			maxCreationDate = Helper.getMaxLoadDt("select max(si_creation_time) maxLoadDate from " + destTblNm + " where si_kind = 'DSL.MetaDataFile'");
			System.out.println("Max creation date is " + maxCreationDate.getTime());
			maxid = Helper.getMaxID("select max(si_id) maxID from " + destTblNm);

			//Test:
			//maxid = "0";

			if (maxid == "0")  {
				//'strInfoSQL = "SELECT count(SI_ID) from CI_INFOOBJECTS WHERE SI_PROGID= 'CrystalEnterprise.Report' and SI_INSTANCE = 0"
				strInfoSQL = "SELECT count(SI_ID) from CI_APPOBJECTS WHERE SI_SPECIFIC_KIND in ('DSL.Universe')";

			}
			else
				//'strInfoSQL = "SELECT count(SI_ID) from CI_INFOOBJECTS WHERE SI_PROGID= 'CrystalEnterprise.Report' and SI_INSTANCE = 0 and si_creation_time > '" + formatDateBO(maxCreationDate) + "'"
				//'strInfoSQL = "SELECT count(SI_ID) from CI_INFOOBJECTS WHERE SI_PROGID= 'CrystalEnterprise.Report' and SI_INSTANCE = 0 and si_creation_time >= '" + formatDateBO(maxCreationDate) + "'"  '4/18/2007
				strInfoSQL = "SELECT count(SI_ID) from CI_APPOBJECTS WHERE SI_SPECIFIC_KIND in ('DSL.Universe') and si_creation_time > '" + Helper.formatDateBO(maxCreationDate) + "'" ;

			//''***************PRODUCTION CODE***********UNCOMMENT AFTER TEST

			System.out.println(strInfoSQL);

			//EXAMPLE OF RETRIEVING PROPERTY BAG VALUES
			//objInfoObject.properties().getProperty("SI_UPDATE_TS");

			colInfoObjects = (IInfoObjects)objInfoStore.query(strInfoSQL);
			//objInfoObject = (IInfoObject)colInfoObjects.get(i);    
			objInfoObject = (IInfoObject)colInfoObjects.get(0);

			//The VB code used the collection here, but in Java, properties() isn't an available function, so I am trying to get this via InfoObject

			//System.out.println(objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").properties().getProperty("SI_ID"));
			//System.out.println(objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").getID());//shows the ID, not the count!

			aggCount = objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT");

			//count = aggCount.getID().byteValue(); //that's not it
			strCount = ((IProperties)objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").getValue()).getProperty("SI_ID").getValue().toString();

			System.out.println("Record count is " + strCount);
			//count = (int)strCount;

			//System.out.println("Record count is " + aggCount.getValue().toString());

			//''***************PRODUCTION CODE***********UNCOMMENT AFTER TEST
			if (maxid == "0") {
				strInfoSQL = "SELECT TOP " + strCount + " SI_ID FROM CI_APPOBJECTS WHERE SI_SPECIFIC_KIND in ('DSL.Universe')";
				//Test:
				//strInfoSQL = "SELECT TOP " + strCount + " SI_ID FROM CI_APPOBJECTS WHERE SI_SPECIFIC_KIND in ('DSL.Universe') and CUID = 'ATG9mvB1h9xOsVwbb7OoJJQ'";
			}
			else
				//'strInfoSQL = "SELECT TOP " + count + " SI_ID FROM CI_INFOOBJECTS WHERE SI_PROGID= 'CrystalEnterprise.Report' and SI_INSTANCE = 0 and SI_ID>" + CInt(maxid)
				//'strInfoSQL = "SELECT TOP " + count + " SI_ID FROM CI_INFOOBJECTS WHERE SI_PROGID= 'CrystalEnterprise.Report' and SI_INSTANCE = 0 and SI_CREATION_TIME > '" + formatDateBO(maxCreationDate) + "'"
				strInfoSQL = "SELECT TOP " + strCount + " SI_ID FROM CI_APPOBJECTS WHERE SI_SPECIFIC_KIND in ('DSL.Universe')  and SI_CREATION_TIME > '" + Helper.formatDateBO(maxCreationDate) + "'";

			//''***************PRODUCTION CODE***********UNCOMMENT AFTER TEST
			System.out.println("line 1698 " + strInfoSQL);

			colInfoObjects = (IInfoObjects)objInfoStore.query(strInfoSQL);

			if (colInfoObjects.size() > 0)  {
				System.out.println("We do have objects in collection");		




				//'System.out.println("creation time  " + objInfoObject2.Properties.Item("SI_CREATION_TIME").getValue)

				//oReportAppFactory = (IReportAppFactory)objEnterpriseSession.getService("", "RASReportService");
				System.out.println("collection size is " + colInfoObjects.size());
				colCount = colInfoObjects.size();
				for (int k =0; k < colCount; k++)
				{
					System.out.println("iterator, k, is now " + k);
					objInfoObject = (IInfoObject) colInfoObjects.get(k);

					//'throws an error when I let it just rip through the query result.
					strInfoSQL = "Select * From CI_APPOBJECTS Where SI_INSTANCE=0 and SI_ID =" + objInfoObject.getID();
					System.out.println(strInfoSQL);
					colInfoObjects2 = (IInfoObjects)objInfoStore.query(strInfoSQL);
					int colCount2 = colInfoObjects2.size();

					for (int j =0; j < colCount2; j++)
					{

						objInfoObject2 = (IInfoObject)colInfoObjects2.get(j);	

						cookiecrumbs = Helper.BuildUniversePath(objEnterpriseSession, objInfoObject2.getParentID(), "");

						//cookiecrumbs = Helper.getMyString("12345678");

						System.out.println("Cookiecrumb trail is " + cookiecrumbs);
						System.out.println("Cookiecrumb length is " + cookiecrumbs.length());

						if (cookiecrumbs.length() > 0) {
							//'If the cookiecrumbs ends up null, then it was an Inbox document,
							//'which I don't want.
							System.out.println("cookiecrumbs length > 0");


							System.out.println("objInfoObject2 id:  " + objInfoObject2.getID());
							/*System.out.println("server1 length is " + server1.length());
							if (server1.length() > 0) {
							}*/
							try
							{

								IProperties properties = objInfoObject2.properties();
								IProperty creationTimeProperty = properties.getProperty(CePropertyID.SI_CREATION_TIME);
								//String value = (String) creationTimeProperty.getValue();
								creationTime.setTime ((Date) creationTimeProperty.getValue());  //This works
								System.out.println("creation time is " + creationTime.getTime());
							}
							catch (Exception ex)
							{
								ex.printStackTrace();
								System.out.println(" for report id: " +objInfoObject2.getID() + " " + ex.getMessage() + " " + ex.getStackTrace());
							}


							try
							{
								updateTime.setTime((Date) objInfoObject2.properties().getProperty("SI_UPDATE_TS").getValue());
								System.out.println(updateTime);

							}

							catch (Exception ex)
							{
								ex.printStackTrace();
								System.out.println(" for report id: " +objInfoObject2.getID() + " " + ex.getMessage() + " " + ex.getStackTrace());
							}


							try
							{
								//Jan 2, 2014:  this property is only for unv universes
								//dataConnectionProps = (IProperties)objInfoObject2.properties().getProperty("SI_DATACONNECTION").getValue();
								//dataConnection = (Integer)dataConnectionProps.getProperty("1").getValue();
								//Since this property doesn't exist for unx universes, it will throw an error.

								//This gets the correct data connection property if the first attempt above throws an error.
								dataConnectionProps = (IProperties)objInfoObject2.properties().getProperty("SI_SL_UNIVERSE_TO_CONNECTIONS").getValue();
								dataConnection = (Integer)dataConnectionProps.getProperty("1").getValue();
								System.out.println("dataConnection " + dataConnection);
							}
							catch (Exception ex)
							{
								//throw new RuntimeException(ex);

							}

							try
							{
								//get SI_FILES.SI_FILE1
								//IFiles theFiles = (IFiles)objInfoObject2.getFiles();
								//getFile = theFiles.get(1).toString();
								//System.out.println("The file name is " + getFile);
								//not the answer I expected and can't be right




								IProperties allProperty = (IProperties)objInfoObject2.properties();
								IProperties SI_FILESProperty = allProperty.getProperties("SI_FILES");						

								//IProperty file = (IProperty)pathProperty.getProperty("SI_FILE1");
								//IProperty file = (IProperty)SI_FILESProperty.getProperty("SI_FILE1").getValue();
								IProperty file = (IProperty)SI_FILESProperty.getProperty("SI_FILE1"); //always says is null but in Query Builder it is not null!!
								getFile = file.getValue().toString();
								//String filename = getFile.toString;

								System.out.println("File name is " + getFile);


							}
							catch (Exception ex)
							{
								//throw new RuntimeException(ex);
								System.out.println(" for object: " +objInfoObject2.getTitle() + " " + ex.getMessage() + " " + ex.getStackTrace());
							}


							//msSQLInsertString = "INSERT INTO " + destTblNm + " (SI_ID, SI_PATH, SI_FILE1, REPORT_NM, REPORT_FILE_PATH, DATA_SOURCE_NM, DATA_SOURCE2_NM, DYNAMIC_PICK_LIST_NM, LOAD_DT, SI_CREATION_TIME, SI_UPDATE_TS, SOURCE_SYSTEM, CUID, SI_BUSINESS_VIEW_INFO, SI_LOCAL_FILEPATH, ORIGINAL_FILE_NM, SI_PARENT_FOLDER_CUID, SI_OWNER) VALUES (" + objInfoObject2.getID() + ", '" + Helper.safeSQL(si_path) + "', '" + Helper.safeSQL(si_file1) + "', '" + Helper.safeSQL(objInfoObject2.getTitle()) + "', '" + Helper.safeSQL(cookiecrumbs) + "','" + server1 + "', '" + server2 + "','See STG1_MIGR_RPT_PICK_LIST_REL',sysdate, to_date('" + objInfoObject2.properties().getProperty("SI_CREATION_TIME").getValue() + "','MM/DD/YYYY HH12:MI:SS AM'), to_date('" + objInfoObject2.properties().getProperty("SI_UPDATE_TS").getValue() + "','MM/DD/YYYY HH12:MI:SS AM'), '" + sourceSystem + "','" + objInfoObject2.getCUID() + "','" + Helper.safeSQL(businessViewNm) + "',' " + Helper.safeSQL(localFilePath) + "','" + originalFileNm + "', '" + objInfoObject2.properties().getProperty("SI_PARENT_FOLDER_CUID").getValue() + "','" + si_owner + "')";
							msSQLInsertString = "INSERT INTO " + destTblNm + " (CUID, SI_KIND, SI_UPDATE_TS, SI_NAME, SI_ID, SI_PARENT_FOLDER_CUID, SI_DESCRIPTION, SI_DATACONNECTION, ENVIRONMENT, PATH, SI_CREATION_TIME, FILE_NM, DATASOURCE_TYPE) VALUES ('" + objInfoObject2.getCUID() + "','" + objInfoObject2.getKind() + "',  ('" + Helper.msSQLDateTime(updateTime) + "')" + ", '" + Helper.safeSQL(objInfoObject2.getTitle()) + "', " + objInfoObject2.getID() + ", '" + objInfoObject2.properties().getProperty("SI_PARENT_FOLDER_CUID").getValue() + "','" + Helper.safeSQL(objInfoObject2.properties().getProperty("SI_DESCRIPTION").getValue().toString()) + "'," + dataConnection + ",'" + EnterpriseEnv + "','" + Helper.safeSQL(cookiecrumbs) + "',  ('" + Helper.msSQLDateTime(creationTime) + "')" + ", ' " + getFile + "','unx')";
							//oraInserter.CommandText = msSQLInsertString;
							System.out.println(msSQLInsertString);
							//'Comment out just to see if the getCEReportParams method is working
							try
							{
								Helper.runMSSQLInsertQuery(msSQLInsertString);
							}
							catch (Exception ex)
							{
								ex.printStackTrace();
								System.out.println(" for report id: " + objInfoObject2.getID() + " " + ex.getMessage() + " " + ex.getStackTrace());
								//'Records the skipped si_id
								msSQLInsertString = "insert into temp_p_skippedrptids (report_id) values (" + objInfoObject2.getID() + ")";
								Helper.runMSSQLInsertQuery(msSQLInsertString);
							}






						}

					}

				}

				objSessionMgr = null;
				//objEnterpriseSession = Nothing;
				objInfoStore = null;
				colInfoObjects = null;
				objInfoObject = null;
				colInfoObjects2 = null;
				objInfoObject2 = null;

			}
			// Clean up the Enterprise Session.
			if(objEnterpriseSession != null) {
				try {
					objEnterpriseSession.logoff();

				} catch(Exception e_ignore_in_cleanup) {}
			}
		}
		catch (Exception ex)
		{
			System.out.println(ex.getMessage());
			ex.printStackTrace(System.out);
			Helper.runMSSQLInsertQuery("insert into STG1_BO4_TRACKER_ERRORS (ERROR_MESSAGE, LOAD_DT) VALUES ('" + ex.getMessage() + " " + ex.getStackTrace() + " " + EnterpriseEnv + "', GETDATE())");
			//'sendmail("Logon error", ex.getMessage() + " " + ex.getStackTrace)
			//Helper.sendmail("bishopp@lexmark.com", "bishopp@lexmark.com, amarabes@lexmark.com,ajpatel@lexmark.com,msankar@lexmark.com,ajain@lexmark.com", "Logon error - load_stg1_migr_XI_reports(String, String, String, String, String) - " + EnterpriseEnv, ex.getMessage() + " " + ex.getStackTrace());
			Emailer.sendEmail("bishopp@lexmark.com", "bishopp@lexmark.com", "Logon error - load_stg1_boxi_universes(String, String, String, String, String) - " + EnterpriseEnv, ex.getMessage() + " " + ex.getStackTrace(), null);

		}
	}

	public static void load_stg1_Business_Views(String EnterpriseEnv , String login, String pswd, String auth, String destTblNm ){
		String strInfoSQL = "";
		IEnterpriseSession objEnterpriseSession;
		IInfoObjects colInfoObjects = null;
		IInfoObject objInfoObject;
		IInfoObjects colInfoObjects2;   
		IInfoObject objInfoObject2 = null;
		int count = 1000000;
		String strCount = "0";
		Date rundate;
		String si_user;
		String cookiecrumbs;
		String maxid;
		String si_owner = "";
		//ReportClientDocument oReportClientDoc;
		//IReportAppFactory oReportAppFactory;
		IReportLogon oReportLogon;

		String server1   = "none needed";
		String server2  = "none needed";
		String msSQLInsertString;

		String sourceSystem  = "TR";
		String businessViewNm = "";
		String originalFileNm = "";
		String localFilePath  = "";
		Calendar maxCreationDate;
		//java.sql.Date maxCreationDate;
		Calendar creationTime = Calendar.getInstance();
		Calendar updateTime = Calendar.getInstance();
		String parentFolderCuid = null;


		//Helper helperFn = new Helper();  //for some reason this isn't needed
		int i = 0;
		//String maxid;

		IProperties si_files;
		String  si_path;
		String si_file1;
		String server[] = new String[ 2 ];
		IEnterpriseSession eSession;
		IProperty aggCount;
		int colCount = 0;

		IProperty BVProperty;
		IProperties BVProperties;
		Integer descendantCount;
		String descendant;
		IInfoObjects colBVDescendants;
		int colCount2 = 0;
		int colCount3 = 0;
		IInfoObject objBVDescendant;
		String siKind;
		String dataFoundationNm = null;
		String dataConnectionNm = null;

		try
		{
			ISessionMgr  objSessionMgr = CrystalEnterprise.getSessionMgr();
			objEnterpriseSession = objSessionMgr.logon(login, pswd, EnterpriseEnv, auth);    
			IInfoStore objInfoStore = (IInfoStore) objEnterpriseSession.getService("InfoStore");

			//Since I learned that si_id can be reused by CMS,I started using dates to do incrementals (03/30/2007)

			///////////////////////////Don't use these till we're in production
			//System.out.println("select max(si_creation_time) from " + destTblNm);
			maxCreationDate = Helper.getMaxLoadDt("select max(si_creation_time) maxLoadDate from " + destTblNm);
			System.out.println("Max creation date is " + maxCreationDate.getTime());
			maxid = Helper.getMaxID("select max(si_id) maxID from " + destTblNm);
			///////////////////////////
			//maxid = "0";
			System.out.println("maxid is " + maxid);



			if (maxid == "0")  {
				strInfoSQL = "SELECT count(SI_ID) from CI_APPOBJECTS WHERE SI_KIND= 'MetaData.BusinessView'";
			}
			else  ////////UNCOMMENT AFTER WE GO LIVE
				//strInfoSQL = "SELECT count(SI_ID) from CI_APPOBJECTS WHERE SI_KIND= 'MetaData.BusinessView' and si_creation_time > '" + Helper.formatDateBO(maxCreationDate) + "'";
				//There seems to be a bug in BI 4 where the greater than sign and dates, means greater than or equal to
				//This is yielding at least one row each time the below query runs, which isn't right and
				//is loading duplicates.
				//So I must use si_id, which is a little scarey since I know is_id's are reused.
				strInfoSQL = "SELECT count(SI_ID) from CI_APPOBJECTS WHERE SI_KIND= 'MetaData.BusinessView' and si_id > " + maxid;

			//''***************PRODUCTION CODE***********UNCOMMENT AFTER TEST

			System.out.println(strInfoSQL);


			colInfoObjects = (IInfoObjects)objInfoStore.query(strInfoSQL);
			System.out.println("Number of rows:  " + colInfoObjects.getResultSize());
			objInfoObject = (IInfoObject)colInfoObjects.get(0);

			//The VB code used the collection here, but in Java, properties() isn't an available function, so I am trying to get this via InfoObject

			//System.out.println(objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").properties().getProperty("SI_ID"));
			//System.out.println(objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").getID());//shows the ID, not the count!

			aggCount = objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT");
			strCount = ((IProperties)objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").getValue()).getProperty("SI_ID").getValue().toString();

			System.out.println("Record count is " + strCount);
			//count = (int)strCount;

			//System.out.println("Record count is " + aggCount.getValue().toString());

			//''***************PRODUCTION CODE***********UNCOMMENT AFTER TEST
			if (maxid == "0") {
				strInfoSQL = "SELECT TOP " + strCount + " SI_ID FROM CI_APPOBJECTS WHERE SI_KIND in ('MetaData.BusinessView')";
			}
			else  //////////UNCOMMENT AFTER GO LIVE
				//strInfoSQL = "SELECT TOP " + strCount + " SI_ID FROM CI_APPOBJECTS WHERE SI_KIND in ('MetaData.BusinessView') and si_creation_time > '" + Helper.formatDateBO(maxCreationDate) + "'";
				//There seems to be a bug in BI 4 where the greater than sign and dates, means greater than or equal to
				//This is yielding at least one row each time the below query runs, which isn't right and
				//is loading duplicates.
				//So I must use si_id, which is a little scarey since I know is_id's are reused.
				strInfoSQL = "SELECT TOP " + strCount + " SI_ID FROM CI_APPOBJECTS WHERE SI_KIND in ('MetaData.BusinessView') and si_id > " + maxid;

			//''***************PRODUCTION CODE***********UNCOMMENT AFTER TEST
			System.out.println("line 3604 " + strInfoSQL);

			colInfoObjects = (IInfoObjects)objInfoStore.query(strInfoSQL);
			System.out.println("number rows is " + colInfoObjects.size());
			if (colInfoObjects.size() > 0)  {
				//System.out.println("We do have objects in collection");		

				//Connection con=null;
				/*Class.forName("oracle.jdbc.driver.OracleDriver");
				//System.out.println("oracle driver loaded in load_stg1_Crystal_Reports()");
				con=DriverManager.getConnection("jdbc:oracle:thin:@dlxkrbodb1.lex.lexmark.com:1750:edr1den1","botrack","********");
				Statement s=con.createStatement();*/

				System.out.println("collection size is " + colInfoObjects.size());
				colCount = colInfoObjects.size();
				for (int k =0; k < colCount; k++)
				{
					//System.out.println("iterator, k, is now " + k);
					objInfoObject = (IInfoObject) colInfoObjects.get(k);


					//'throws an error when I let it just rip through the query result.
					strInfoSQL = "Select * From CI_APPOBJECTS Where  SI_ID =" + objInfoObject.getID();
					System.out.println(strInfoSQL);
					colInfoObjects2 = (IInfoObjects)objInfoStore.query(strInfoSQL);
					colCount2 = colInfoObjects2.size();

					for (int j =0; j < colCount2; j++)
					{
						objInfoObject2 = (IInfoObject)colInfoObjects2.get(j);

						//Get the data foundation and data connection name
						//Must get the total number of descendants

						try {
							BVProperties = (IProperties)objInfoObject2.properties().getProperty("SI_METADATA_ALL_DESCENDANTS").getValue();
							descendantCount = (Integer)BVProperties.getProperty("SI_TOTAL").getValue();
							System.out.println("Descendant count is " + descendantCount);
							for (int a =1; a < descendantCount; a++){
								//descendant = BVProperties.getProperty("SI_METADATA_ALL_DESCENDANTS").getValue().toString();
								descendant = (String)BVProperties.getProperty(a).getValue();

								strInfoSQL = "select * from ci_appobjects where CUID = '" + descendant + "'";
								//System.out.println("Descendant cuid is " + descendant);
								colBVDescendants = (IInfoObjects)objInfoStore.query(strInfoSQL);
								colCount3 = colBVDescendants.getResultSize(); //this is not useful
								//System.out.println("Number of BV descendants is " + colCount3); //colCount3 is always 1
								if (colCount3 > 0) {

									objBVDescendant = (IInfoObject) colBVDescendants.get(0);
									siKind = objBVDescendant.getKind();
									//System.out.println("Descendant kind is " + siKind);

									if (siKind.equals("MetaData.DataFoundation")) {
										dataFoundationNm = objBVDescendant.getTitle();
									} else if (siKind.equals("MetaData.DataConnection")) {
										dataConnectionNm = objBVDescendant.getTitle();
									}

								}

							}
						}
						catch (Exception ex){
							System.out.println(ex.getMessage() + " " + ex.getStackTrace());
						}
						System.out.println("Data foundation is " + dataFoundationNm + " and data connection name is " + dataConnectionNm);
					}

					try {

						IProperties properties = objInfoObject2.properties();
						IProperty creationTimeProperty = properties.getProperty(CePropertyID.SI_CREATION_TIME);
						//String value = (String) creationTimeProperty.getValue();
						creationTime.setTime ((Date) creationTimeProperty.getValue());  //This works
						//System.out.println("creation time is " + creationTime);
					}
					catch (Exception ex){
						System.out.println(ex.getMessage() + " " + ex.getStackTrace());
					}

					try {

						IProperties properties = objInfoObject2.properties();
						IProperty updateTSProperty = properties.getProperty(CePropertyID.SI_UPDATE_TS);
						//String value = (String) creationTimeProperty.getValue();
						updateTime.setTime ((Date) updateTSProperty.getValue());  //This works
						//System.out.println("update time is " + updateTime);
					}
					catch (Exception ex){
						System.out.println(ex.getMessage() + " " + ex.getStackTrace());
					}
					try {
						parentFolderCuid = objInfoObject2.properties().getProperty("SI_PARENT_FOLDER_CUID").getValue().toString();	
					}
					catch (Exception ex){
						System.out.println(ex.getMessage() + " " + ex.getStackTrace());
					}

					//msSQLInsertString = msSQLInsertString = "INSERT INTO " + destTblNm + " (SI_CREATION_TIME, SI_KIND, SI_PROGID, SI_NAME,SI_UPDATE_TS, SI_ID, SI_PARENT_CUID, FOLDER, CUID, SI_PARENT_FOLDER_CUID, DATA_FOUNDATION_NM, DATA_CONNECTION_NM ) VALUES ( ('" + Helper.msSQLDateTime(creationTime) + "'), '" + objInfoObject2.getKind() + "', '" + objInfoObject2.getProgID() + "','" + Helper.safeSQL(objInfoObject2.getTitle()) + "'," + "  ('" + Helper.msSQLDateTime(updateTime) + "'), '" + objInfoObject2.getID() + "','" + objInfoObject2.getParentCUID() + "', '" + objInfoObject2.getParent().getTitle() + "','" + objInfoObject2.getCUID() + "','" + parentFolderCuid + "','" + dataFoundationNm + "','" + dataConnectionNm + "')";
					msSQLInsertString = msSQLInsertString = "INSERT INTO " + destTblNm + " (SI_CREATION_TIME, SI_KIND, SI_PROGID, SI_NAME,SI_UPDATE_TS, SI_ID, SI_PARENT_CUID, FOLDER, CUID, SI_PARENT_FOLDER_CUID, DATA_FOUNDATION_NM, DATA_CONNECTION_NM ) VALUES ( ('" + Helper.msSQLDateTime(creationTime) + "'), '" + objInfoObject2.getKind() + "', '" + objInfoObject2.getProgID() + "','" + Helper.safeSQL(objInfoObject2.getTitle()) + "'," + "  ('" + Helper.msSQLDateTime(updateTime) + "'), '" + objInfoObject2.getID() + "','" + objInfoObject2.getParentCUID() + "', '" + objInfoObject2.getParent().getTitle() + "','" + objInfoObject2.getCUID() + "','" + parentFolderCuid + "','" + dataFoundationNm + "','" + dataConnectionNm + "')";
					System.out.println(msSQLInsertString);
					Helper.runMSSQLInsertQuery(msSQLInsertString);

				}

				objSessionMgr = null;
				//objEnterpriseSession = Nothing;
				objInfoStore = null;
				colInfoObjects = null;
				objInfoObject = null;
				colInfoObjects2 = null;
				objInfoObject2 = null;
				//s.close();
				//con.close();

			}
			// Clean up the Enterprise Session.
			if(objEnterpriseSession != null) {
				try {
					objEnterpriseSession.logoff();

				} catch(Exception e_ignore_in_cleanup) {}
			}
		}
		catch (Exception ex)
		{
			System.out.println(ex.getMessage());
			ex.printStackTrace(System.out);
			Helper.runMSSQLInsertQuery("insert into STG1_BO4_TRACKER_ERRORS  '" + ex.getMessage() + " " + ex.getStackTrace() + " " + EnterpriseEnv + "', GETDATE())");
			//'sendmail("Logon error", ex.getMessage() + " " + ex.getStackTrace)
			//Helper.sendmail("bishopp@lexmark.com", "bishopp@lexmark.com, amarabes@lexmark.com,ajpatel@lexmark.com,msankar@lexmark.com,ajain@lexmark.com", "Logon error - load_stg1_migr_XI_reports(String, String, String, String, String) - " + EnterpriseEnv, ex.getMessage() + " " + ex.getStackTrace());
			Emailer.sendEmail("bishopp@lexmark.com", "bishopp@lexmark.com", "Logon error - load_stg1_Business_Views(String, String, String, String, String) - " + EnterpriseEnv, ex.getMessage() + " " + ex.getStackTrace(), null);

		}
	}

	public static void load_stg1_boxi_runtime_param(IInfoObject objInfoObject2, String EnterpriseEnv, Connection con) {
		//This function loads the parameter values into a separate table that is a child table to runhist.
		Helper Helper = new Helper();
		try
		{
			System.out.println("ReportID and Instance ID and schedule_status: " + objInfoObject2.getParentID() + " " + objInfoObject2.getID() + " " + objInfoObject2.properties().getProperty("SI_SCHEDULE_STATUS").getValue());
			IReport oReport = (IReport)objInfoObject2;
			IReportParameter oParam;
			String datatype;
			String cefromValue;
			String cetoValue;
			String promptName;
			String promptInsert;
			String promptValue;			

			if (oReport.getReportParameters().size() >0) {

				//Do these things in the calling function:
				//Connection con=null;
				//Class.forName("oracle.jdbc.driver.OracleDriver");
				//con=DriverManager.getConnection("jdbc:oracle:thin:@dlxkrbodb1.lex.lexmark.com:1750:edr1den1","botrack","********");				

				for (int x = 1; x <= oReport.getReportParameters().size(); x++){
					oParam = (IReportParameter)oReport.getReportParameters().get(x);
					promptName = oParam.getParameterName();
					if (oParam.getCurrentValues().size() > 0) {
						switch (oParam.getValueType()) {

						//case CeReportVariableValueType.ceRVBoolean: datatype = "Boolean";
						//case CeReportVariableValueType.ceRVCurrency: datatype= "Currency";
						//case CeReportVariableValueType.ceRVDate: datatype= "Date";
						//case CeReportVariableValueType.ceRVDateTime: datatype = "DateTime";
						//case CeReportVariableValueType.ceRVNumber: datatype = "Number";
						//case CeReportVariableValueType.ceRVString: datatype = "String";
						//case CeReportVariableValueType.ceRVTime: datatype = "Time";
						case 2: datatype = "Boolean";
						case 1: datatype= "Currency";
						case 3: datatype= "Date";
						case 5: datatype = "DateTime";
						case 0: datatype = "Number";
						case 6: datatype = "String";
						case 4: datatype = "Time";
						default: datatype = "unknown";

						}//end switch
						if (oParam.isRangeValueSupported()) {
							cefromValue = (String)oParam.getCurrentValues().addRangeValue().getFromValue().getValue();
							cetoValue = (String)oParam.getCurrentValues().addRangeValue().getToValue().getValue();
							promptInsert = "insert into STG1_BOXI_RUNTIMEPARAM (REPORT_ID, INSTANCE_ID, PARAMETER_NM, PARAMETER_SI_NM, SI_STARTDATE, PARAMETER_DATATYPE, RANGE_FROM_VAL, RANGE_TO_VAL, ENVIRONMENT) values (" + objInfoObject2.getParentID() + "," + objInfoObject2.getID() + ", '" + promptName + "','SI_PROMPT" + x + "', ('" + Helper.oracleDate((Calendar)objInfoObject2.properties().getProperty("SI_STARTTIME").getValue()) + "'),'" + datatype + "','" + cefromValue + "', '" + cetoValue + "','" + EnterpriseEnv + "')";
							Statement s=con.createStatement();
							s.execute(promptInsert);
						}
						else {
							promptValue = (String)oParam.getCurrentValues().addSingleValue().getValue();
							promptInsert = "insert into STG1_BOXI_RUNTIMEPARAM (REPORT_ID, INSTANCE_ID, PARAMETER_NM, PARAMETER_SI_NM, PARAMETER_VAL, SI_STARTDATE, PARAMETER_DATATYPE, ENVIRONMENT) values (" + objInfoObject2.getParentID() + "," + objInfoObject2.getID() + ", '" + promptName + "','SI_PROMPT" + x + "', '" + promptValue + "', ('" + Helper.oracleDate((Calendar)objInfoObject2.properties().getProperty("SI_STARTTIME").getValue()) + "'),'" + datatype + "','" + EnterpriseEnv + "')";
							Statement s=con.createStatement();
							s.execute(promptInsert);
						}
						System.out.println(promptInsert);

					}//end if

				}//end for loop	


			}//end if

		}
		catch (Exception ex)
		{
			System.out.println(ex.getMessage() + " " + ex.getStackTrace());
			Helper.runMSSQLInsertQuery("insert into STG1_BO4_TRACKER_ERRORS (ERROR_MESSAGE, LOAD_DT) VALUES ('" + ex.getMessage() + " " + ex.getStackTrace() + " " + EnterpriseEnv + "', GETDATE())");
			//'sendmail("Logon error", ex.getMessage() + " " + ex.getStackTrace)
			//Helper.sendmail("bishopp@lexmark.com", "bishopp@lexmark.com, amarabes@lexmark.com,ajpatel@lexmark.com,msankar@lexmark.com,ajain@lexmark.com", "Logon error - load_stg1_migr_XI_reports(String, String, String, String, String) - " + EnterpriseEnv, ex.getMessage() + " " + ex.getStackTrace());
			Emailer.sendEmail("bishopp@lexmark.com", "bishopp@lexmark.com", "Logon error - load_stg1_boxi_runtime_param(String, String, String, String, String) - " + EnterpriseEnv, ex.getMessage() + " " + ex.getStackTrace(), null);


		}
	}
	public static void load_stg1_boxi_runhistOld(String EnterpriseEnv, String login, String pswd,String auth, String destTblNm, String environ){
		//Precondition:  jobs that ran since the last load are not in the database table.
		//Postcondition:  jobs that ran tsince the last load are in the database table.

		IEnterpriseSession objEnterpriseSession;
		//IInfoStore objInfoStore;
		IInfoObjects colInfoObjects;
		IInfoObject objInfoObject;
		IInfoObject objInfoObject2;
		String errorMsg = "";
		String cookieCrumbs;
		int count;
		String si_owner = "";
		Calendar runDate;
		Calendar startTime= Calendar.getInstance();
		Calendar endTime= Calendar.getInstance();
		Calendar creationTime = Calendar.getInstance();
		Calendar updateTS = Calendar.getInstance();
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
		int numPrompts = 0;
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
		String scheduleStatus = "999"; //means is not yet known.  I made this up because this is not a valid value.  Tells me it's not getting set from the recordset.
		IProcessingInfo processingInfo;
		IReport iReport;
		String server ="";
		String server1 = "";
		String server2 = "";
		IProperty aggCount;
		String strCount="0";
		int colCount = 0;
		Boolean si_format_export_allpages;
		String siFormatExportAllpages;
		String paramValues = "";
		//		IWebiPrompts iWebiPrompts;
		IPrompts webiPrompts;
		IReport oReport;
		String submitter = "";
		String machineUsed = "";
		String parentFolderCuid = "";
		String siOwner = "";
		String testString = "";

		try 
		{
			ISessionMgr  objSessionMgr = CrystalEnterprise.getSessionMgr();
			objEnterpriseSession = objSessionMgr.logon(login, pswd, EnterpriseEnv, auth);    
			IInfoStore objInfoStore = (IInfoStore) objEnterpriseSession.getService("InfoStore");

			maxID = Helper.getMaxID("select max(instance_id) maxID from " + destTblNm);
			System.out.println("maxID is " + maxID);
			//maxCreationDate = Helper.getMaxLoadDt("select max(si_creation_time) maxLoadDate from " + destTblNm);
			//System.out.println("Max creation date is " + maxCreationDate);
			maxEndDate = Helper.getMaxLoadDt("select max(si_endtime) maxLoadDate from " + destTblNm);
			System.out.println("Max end date is " + Helper.msSQLDateTime(maxEndDate));

			if (maxID == "0") {
				//Is a first-time load
				strInfoSQL = "SELECT count(SI_ID) from CI_INFOOBJECTS WHERE SI_INSTANCE = 1 and si_recurring = 0";
			}
			else
				//is an incremental load
				strInfoSQL = "SELECT count(SI_ID) from CI_INFOOBJECTS WHERE SI_INSTANCE = 1 and si_recurring = 0 and si_endtime >= '" + Helper.formatDateBO(maxEndDate) + "'" ;
			//strInfoSQL = "SELECT count(SI_ID) from CI_INFOOBJECTS WHERE SI_INSTANCE = 1 and si_recurring = 0 and si_creation_time >= '" + Helper.formatDateBO(maxCreationDate) + "' and si_schedule_status=3" ;

			System.out.println(strInfoSQL);

			colInfoObjects = (IInfoObjects)objInfoStore.query(strInfoSQL);
			//objInfoObject = (IInfoObject)colInfoObjects.get(i); 

			objInfoObject = (IInfoObject)colInfoObjects.get(0);

			//The VB code used the collection here, but in Java, properties() isn't an available function, so I am trying to get this via InfoObject

			//System.out.println(objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").properties().getProperty("SI_ID"));
			System.out.println(objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").getID());//shows the ID, not the count!
			//count = objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").properties().getProperty("SI_ID");
			//count = objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").getID();
			aggCount = objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT");

			count = objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").getID();
			strCount = ((IProperties)objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").getValue()).getProperty("SI_ID").getValue().toString();

			System.out.println("Record count is " + strCount);


			if (maxID == "0") {
				strInfoSQL = "SELECT TOP " + strCount + " * FROM CI_INFOOBJECTS WHERE  SI_INSTANCE = 1 and si_recurring=0";
			}
			else
				//strInfoSQL = "SELECT TOP " + strCount + " SI_ID FROM CI_INFOOBJECTS WHERE SI_KIND in ('CrystalReport') and SI_INSTANCE = 0 and SI_CREATION_TIME >= '" + Helper.formatDateBO(maxCreationDate) + "'";
				//PRODUCTION SQL:
				//strInfoSQL = "SELECT TOP " + strCount + " SI_ID FROM CI_INFOOBJECTS WHERE  SI_INSTANCE = 1 and si_recurring=0 and SI_CREATION_TIME >= '" + Helper.formatDateBO(maxCreationDate) + "'";
				strInfoSQL = "SELECT TOP " + strCount + " * FROM CI_INFOOBJECTS WHERE  SI_INSTANCE = 1 and si_recurring=0 and SI_ENDTIME >= '" + Helper.formatDateBO(maxEndDate) + "'";
			//JUST A TEST TO SEE HOW FAILED INSTANCES WORK IN THE CODE
			//strInfoSQL = "SELECT TOP " + strCount + " SI_ID FROM CI_INFOOBJECTS WHERE  SI_INSTANCE = 1 and si_recurring=0 and si_schedule_status=1 and SI_CREATION_TIME >= '" + Helper.formatDateBO(maxCreationDate) + "'";

			System.out.println(strInfoSQL);
			colInfoObjects = (IInfoObjects)objInfoStore.query(strInfoSQL);
			System.out.println("Line 638 Count of objects in collection: " + colInfoObjects.size());

			if (colInfoObjects.size() > 0){
				Connection con=null;
				Class.forName("oracle.jdbc.driver.OracleDriver");
				con=DriverManager.getConnection("jdbc:oracle:thin:@dlxkrbodb1.lex.lexmark.com:1750:edr1den1","botrack","********");
				Statement s=con.createStatement();
				//'This records all the instances found on the BOXI system
				colCount = colInfoObjects.size();
				for (int x=0; x <= colCount; x++){
					//msSQLInsertString = "insert into temp_p_allinstances(report_id, instance_id) values (" + objInfoObject.getParentID() + "," + objInfoObject.getID() + ")";
					//System.out.println(msSQLInsertString);
					//s.execute(msSQLInsertString);
					objInfoObject = (IInfoObject)colInfoObjects.get(x);
					//oReport = (IReport)colInfoObjects.get(x);  //4/27/2012:  causes error against 3.1 Prod: $Proxy0 cannot be cast to com.crystaldecisions.sdk.plugin.desktop.report.IReport [Ljava.lang.StackTraceElement;@aa559d
					//That's because this is an instance and it will throw this error if the object is not .rpt, which would usually be
					//the case.

					System.out.println ("si_id is " + objInfoObject.getID());


					try {
						//destination = objInfoObject.getSchedulingInfo().getDestination().getName();  //--> destination was null, but it can't be!
						//destination = (String)objInfoObject.getSchedulingInfo().properties().getProperty("SI_DESTINATION").getValue();

						//VB:  destination = objInfoObject.SendToDestination.Name and this works
						ISendable obj = (ISendable)objInfoObject;						
						IDestinationPlugin destinationPlugin = Helper.getDestinationPlugin(objInfoStore, objInfoObject.getKind());
						IDestination odestination = obj.getSendToDestination();	
						odestination.setFromPlugin(destinationPlugin);
						System.out.println("Line 823: odestination is " + odestination.getName());
						destination = odestination.getName();
						System.out.println("destination is " + destination + " for si_id " + objInfoObject.getID());
						//distributionList = Helper.getDistributionList("uslexbcs02", objInfoObject.getID());	
						if (destination.equals("CrystalEnterprise.Smtp")) {
							System.out.println("Line 839");
							System.out.println("getting email distribution list");
							//distributionList = Helper.getDistributionList("uslexbcs02", objInfoObject.getID());	
							distributionList = Helper.getDistributionList(EnterpriseEnv, objInfoObject.getID()); //gives an error
							//System.out.println("Line 832" + distributionList);
						}
						else
							System.out.println("Destination actually is:  " + destination);
					}
					catch(SDKException sdkEx)
					{
						System.out.println("Line 850 " + sdkEx.getDetail() + " " + sdkEx.getStackTrace());
						Helper.runMSSQLInsertQuery("insert into STG1_BO4_TRACKER_ERRORS (ERROR_MESSAGE, LOAD_DT) VALUES ('" + sdkEx.getMessage() + " " + sdkEx.getStackTrace() + " " + EnterpriseEnv + "', GETDATE())");
					}

					try {
						ifiles = (IFiles)objInfoObject.properties().getProperty("SI_FILES").getValue(); //gives error
						outputFile = (String)ifiles.properties().getProperty("SI_PATH").getValue();
						System.out.println("output file is " + outputFile);

						IProperties properties = objInfoObject.properties();
						IProperty ifilesProperty = properties.getProperty(CePropertyID.SI_PATH);
						//String value = (String) creationTimeProperty.getValue();
						//outputFile.equals(ifilesProperty.getValue().toString());  //Always NULL - why??
						outputFile.equals(ifilesProperty.getValue());  
						System.out.println("output file is " + outputFile);

						System.out.println("Line 853");
					} catch (Exception ex){
						outputFile.equals("error - no output file");
						System.out.println(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getStackTrace());
					}

					try {
						IProperties properties = objInfoObject.properties();
						IProperty startTimeProperty = properties.getProperty(CePropertyID.SI_STARTTIME);
						//String value = (String) creationTimeProperty.getValue();
						startTime.setTime ((Date) startTimeProperty.getValue());  //This works
						System.out.println("start time is " + startTime);
					}
					catch (Exception ex) {
						System.out.println(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getStackTrace());
					}

					try {
						IProperties properties = objInfoObject.properties();
						IProperty endTimeProperty = properties.getProperty(CePropertyID.SI_ENDTIME);
						//String value = (String) creationTimeProperty.getValue();
						endTime.setTime ((Date) endTimeProperty.getValue());  //This works
						System.out.println("end time is " + startTime);
					}
					catch (Exception ex) {
						System.out.println(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getStackTrace());
					}

					try {
						IProperties properties = objInfoObject.properties();
						IProperty updateTSProperty = properties.getProperty(CePropertyID.SI_UPDATE_TS);
						//String value = (String) creationTimeProperty.getValue();
						updateTS.setTime ((Date) updateTSProperty.getValue());  //This works
						System.out.println("end time is " + updateTS);
					}
					catch (Exception ex) {
						System.out.println(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getStackTrace());
					}


					try {
						IProperties properties = objInfoObject.properties();
						IProperty submitterProperty = properties.getProperty(CePropertyID.SI_SUBMITTER);
						//String value = (String) creationTimeProperty.getValue();
						submitter.equals(submitterProperty.toString());
						System.out.println("submitter " + submitter);
					}
					catch (Exception ex) {
						System.out.println(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getStackTrace());
					}

					try {
						//Got this from D:\Documents and Settings\bishopp\My Documents\BOXI\Code Examples\xi4_boejava_dg_en\html\topic15.html
						IProperties properties = objInfoObject.properties();
						IProperty creationTimeProperty = properties.getProperty(CePropertyID.SI_CREATION_TIME);
						//String value = (String) creationTimeProperty.getValue();
						creationTime.setTime ((Date) creationTimeProperty.getValue());  //This works
						System.out.println("creation time is " + creationTime.getTime());
					}
					catch (Exception ex)
					{
						ex.printStackTrace();
						System.out.println(" for report id: " +objInfoObject.getID() + " " + ex.getMessage() + " " + ex.getStackTrace());
					}

				}

				try {
					IProperties properties = objInfoObject.properties();
					IProperty scheduleStatusProperty = properties.getProperty(CePropertyID.SI_SCHEDULE_STATUS);
					//String value = (String) creationTimeProperty.getValue();
					scheduleStatus.equals(scheduleStatusProperty.toString());
					System.out.println("scheduleStatus " + submitter);
				}
				catch (Exception ex) {

				}

				try {
					IProperties properties = objInfoObject.properties();
					IProperty machineUsedProperty = properties.getProperty(CePropertyID.SI_MACHINE_USED);
					//String value = (String) creationTimeProperty.getValue();
					machineUsed.equals(machineUsedProperty.toString());
					System.out.println("scheduleStatus " + machineUsed);
				}
				catch (Exception ex) {

				}

				try {
					IProperties properties = objInfoObject.properties();
					IProperty parentFolderCuidProperty = properties.getProperty(CePropertyID.SI_PARENT_CUID);
					//String value = (String) creationTimeProperty.getValue();
					parentFolderCuid.equals(parentFolderCuidProperty.toString());
					System.out.println("scheduleStatus " + parentFolderCuid);
				}
				catch (Exception ex) {

				}


				try {
					scheduleStatus = objInfoObject.properties().getProperty("SI_SCHEDULE_STATUS").getValue().toString();
				} catch (Exception ex) {
					System.out.println(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getStackTrace());
				}

				try {
					runDate = (Calendar)objInfoObject.properties().getProperty("SI_STARTTIME").getValue();
					System.out.println("Rundate is " + runDate);
				} catch (Exception ex) {
					System.out.println(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getStackTrace());
				}



				if ((scheduleStatus == "1") || (scheduleStatus == "3")) {
					runDate = (Calendar)objInfoObject.properties().getProperty("SI_STARTTIME").getValue();
					startTime = runDate;
					endTime = (Calendar)objInfoObject.properties().getProperty("SI_ENDTIME").getValue();
					theInterval = 60/(endTime.getTimeInMillis() - startTime.getTimeInMillis())*1000;

					try
					{
						switch (objInfoObject.getSchedulingInfo().getStatus()) {
						case ISchedulingInfo.ScheduleStatus.COMPLETE:
							//success = true;
							break;
						case ISchedulingInfo.ScheduleStatus.FAILURE:
							//log("Error", objInfoObject.getSchedulingInfo().getErrorMessage());
							//return false;
							errorMsg = Helper.safeSQL(objInfoObject.getSchedulingInfo().getErrorMessage());
							//errorMsg.replaceAll("\r", " - ");
							errorMsg.replaceAll("[\\r\\n]", " - ");  //replaces the carriage return-line feed characters

						}
						/*if (scheduleStatus == "3") {
								//errorMsg = Helper.safeSQL((String)objInfoObject.properties().getProperty("SI_ERROR_MESSAGE").getValue());
								errorMsg = Helper.safeSQL(objInfoObject.getSchedulingInfo().getErrorMessage());
								//errorMsg.replaceAll("\r", " - ");
								errorMsg.replaceAll("[\\r\\n]", " - ");  //replaces the carriage return-line feed characters

							}
							else
								errorMsg = "";*/
					}

					catch (Exception ex)
					{
						//This is old VB.Net COM code to get the error message if it isn't in the SI_ERROR_MESSAGE property bag
						//I will use a catch-all for now

						//Cast the IInfo Object to a IReport object to use the methods of a report
						Helper.runMSSQLInsertQuery("insert into STG1_BO4_TRACKER_ERRORS (ERROR_MESSAGE, LOAD_DT) VALUES ('" + ex.getMessage() + " " + ex.getStackTrace() + " " + EnterpriseEnv + "', GETDATE())");
						try {
							//oReport = (IReport)objInfoObject;


							//Set the thumbnail and Repository settings
							//System.out.println("Error message: " + oReport.getSchedulingInfo().getErrorMessage());
							System.out.println("Error message: " + objInfoObject.getSchedulingInfo().getErrorMessage());
							//errorMsg = "Unknown error";
							errorMsg = objInfoObject.getSchedulingInfo().getErrorMessage();

							try
							{
								IExcel eReport = (IExcel)objInfoObject;
								System.out.println("Error message: " + objInfoObject.getID() + " " + objInfoObject.getTitle() + " - " + objInfoObject.getSchedulingInfo().getErrorMessage());
								errorMsg = eReport.getSchedulingInfo().getErrorMessage();
							}
							catch (Exception ex3){
								System.out.println("Not an Excel output");
							}

							//////NEED ONE FOR TXT, TOO
							try
							{
								ITxt tReport = (ITxt)objInfoObject;
								System.out.println("Error message: " + objInfoObject.getID() + " " + objInfoObject.getTitle() + " - " + objInfoObject.getSchedulingInfo().getErrorMessage());
								errorMsg = objInfoObject.getSchedulingInfo().getErrorMessage();
							}
							catch (Exception ex3){
								System.out.println("Not a Txt output");
							}

							try
							{
								IPDF pReport = (IPDF)objInfoObject;
								System.out.println("Error message: " + objInfoObject.getID() + " " + objInfoObject.getTitle() + " - " + objInfoObject.getSchedulingInfo().getErrorMessage());
								errorMsg = pReport.getSchedulingInfo().getErrorMessage();
							}
							catch (Exception ex3){
								System.out.println("Not a Pdf output");
							}

						}
						catch (Exception ex4){
							System.out.println("Not a Crystal Report output");
						}






						System.out.println("Error message is " + errorMsg);

						//try
						//{
						//if (objInfoObject.properties().getProperty("SI_STATUSINFO").Properties.Item("SI_SUBST_STRINGS").Properties.Item("SI_TOTAL").Value = 1) {
						//    errorMsg = Helper.safeSQL(objInfoObject.properties().getProperty"SI_STATUSINFO").Properties.Item("SI_SUBST_STRINGS").Properties.Item("1").Value)
						//    objInfoObject.getSchedulingInfo().getErrorMessage().
						//    errorMsg.replaceAll("[\\r\\n]", " - ");                                
						//else
						//    errorMsg = Helper.safeSQL(objInfoObject2.Properties.Item("SI_STATUSINFO").Properties.Item("SI_SUBST_STRINGS").Properties.Item("1").Value) + " " + safeSQL(objInfoObject2.Properties.Item("SI_STATUSINFO").Properties.Item("SI_SUBST_STRINGS").Properties.Item("2").Value)
						//    errorMsg.replaceAll("[\\r\\n]", " - ");                                
						//}
						//catch (Exception ex1)
						// {}
					}

				}

				try {
					si_owner = (String)objInfoObject.properties().getProperty("SI_OWNER").getValue();
					IProperties properties = objInfoObject.properties();
					IProperty siOwnerProperty = properties.getProperty(CePropertyID.SI_OWNER);
					//String value = (String) creationTimeProperty.getValue();
					siOwner.equals(siOwnerProperty.toString());
					System.out.println("scheduleStatus " + siOwner);

				} catch(Exception ex) {

				}


				//					I used to set cookiecrumbs, but it is so time-consuming
				cookieCrumbs = "";
				System.out.println(objInfoObject.getTitle());

				//Get the logon info
				//processingInfo = objInfoObject.getProcessingInfo();
				//gives error: com.businessobjects.sdk.plugin.desktop.webi.internal.d cannot be cast to com.crystaldecisions.sdk.plugin.desktop.report.IReport [Ljava.lang.StackTraceElement;@188a387
				try {
					if (objInfoObject.getKind() == "Webi") {
						webiPrompts = (IPrompts)objInfoObject.properties().getProperty("SI_WEBI_PROMPTS").getValue();
						numPrompts = webiPrompts.size();
						logon1 = "n/a";
						logon2 = "n/a";
						server1 = "n/a";
						server2 = "n/a";
						if (numPrompts > 0 ) {
							//Getting the param list might not be possible
							//List paramList = webiPrompts.getReportParameters();
							Object paramList =  webiPrompts.toArray();
						}

					} else {

						iReport = (IReport) objInfoObject;

						//Get the username 
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
						List paramList = iReport.getReportParameters();
						numPrompts = paramList.size();
					}


					//The number of prompts is stored differently between Crystal Reports and Webi docs
					//Crystal: SI_PROMPTS
					//Webi:  SI_WEBI_PROMPTS
					//Xcelsius:  


				} catch (Exception ex) {

				}

				//Get the number of prompts


				try {
					si_format_export_allpages = getFormatAllPagesSetting(objInfoObject);
					if (si_format_export_allpages) {
						siFormatExportAllpages = "true";}
					else
						siFormatExportAllpages = "false";
				}
				catch (Exception ex) {
					System.out.println(ex.getMessage() + " " + ex.getStackTrace());
					siFormatExportAllpages = "non-existent";
				}


				//msSQLInsertString = "insert into " + destTblNm + " (REPORT_ID, INSTANCE_ID, SI_STARTTIME, SI_ENDTIME, DURATION, SI_SUBMITTER, SI_SERVER, SI_OWNER, SI_SCHEDULE_STATUS,REPORT_PATH, SI_NAME, SI_CREATION_TIME, SI_NUM_PROMPTS, DATASRC_LOGIN_NM, SI_PASSWORD, DATASRC_LOGIN_NM2, SI_PASSWORD2, SI_ERROR_MESSAGE, SI_MACHINE_USED, SI_UPDATE_TS, DESTINATION, OUTPUT_FILE, REPORT_CUID, SI_PARENT_FOLDER_CUID, DISTRIBUTION_LIST, si_file1) values (" + objInfoObject.getParentID() + ", " + objInfoObject.getID() + ", to_date('" + objInfoObject.properties().getProperty("SI_STARTTIME").getValue() + "','MM/DD/YYYY HH12:MI:SS AM')" + ", to_date('" + objInfoObject.properties().getProperty("SI_ENDTIME").getValue() + "','MM/DD/YYYY HH12:MI:SS AM')" + ", " + theInterval + ", '" + Helper.safeSQL((String)objInfoObject.getSchedulingInfo().properties().getProperty("SI_SUBMITTER").getValue()) + "', '" + Helper.safeSQL(server) + "', '" + Helper.safeSQL((String)objInfoObject.getSchedulingInfo().properties().getProperty("SI_SUBMITTER").getValue()) + "', " + objInfoObject.properties().getProperty("SI_SCHEDULE_STATUS").getValue() + ", '" + Helper.safeSQL(cookieCrumbs) + "', '" + Helper.safeSQL(objInfoObject.getTitle()) + "', to_date('" + objInfoObject.properties().getProperty("SI_CREATION_TIME").getValue() + "','MM/DD/YYYY HH12:MI:SS AM'), " + numPrompts + ",'" + logon1 + "','" + pswd1 + "', '" + logon2 + "', '" + pswd2 + "','" + errorMsg + "','" + objInfoObject.getSchedulingInfo().properties().getProperty("SI_MACHINE_USED").getValue() + "', to_date('" + objInfoObject.properties().getProperty("SI_UPDATE_TS").getValue() + "','MM/DD/YYYY HH12:MI:SS AM'), '" + destination + "','" + outputFile + "', '" + objInfoObject.getParentCUID() + "', '" + objInfoObject.properties().getProperty("SI_PARENT_FOLDER_CUID").getValue() + "','" + Helper.getDistributionList(EnterpriseEnv, objInfoObject.getID()) + "', '" + si_file1 + "')";

				//THIS IS THE NEW QUERY. Convert to java syntax.
				//msSQLInsertString = "insert into " + destTblNm + " (REPORT_ID, INSTANCE_ID, SI_STARTTIME, SI_ENDTIME, DURATION, SI_SUBMITTER, SI_SERVER, SI_OWNER, SI_SCHEDULE_STATUS,REPORT_PATH, SI_NAME, SI_CREATION_TIME, SI_NUM_PROMPTS, DATASRC_LOGIN_NM, SI_PASSWORD, DATASRC_LOGIN_NM2, SI_PASSWORD2, SI_ERROR_MESSAGE, SI_MACHINE_USED, SI_UPDATE_TS, DESTINATION, OUTPUT_FILE, REPORT_CUID, SI_PARENT_FOLDER_CUID, DISTRIBUTION_LIST, si_file1, ENVIRONMENT, SI_KIND, SI_FORMAT_EXPORT_ALLPAGES, CUID, PARAM_VALUES) values (" + objInfoObject2.ParentID + ", " + objInfoObject2.ID + ", to_date('" + startTime + "','MM/DD/YYYY HH12:MI:SS AM')" + ", to_date('" + endTime + "','MM/DD/YYYY HH12:MI:SS AM')" + ", " + theInterval + ", '" + safeSQL(objInfoObject2.SchedulingInfo.Properties.Item("SI_SUBMITTER").Value) + "', '" + safeSQL(server) + "', '" + safeSQL(objInfoObject2.SchedulingInfo.Properties.Item("SI_SUBMITTER").Value) + "', " + objInfoObject2.Properties.Item("SI_SCHEDULE_STATUS").Value + ", '" + safeSQL(cookieCrumbs) + "', '" + safeSQL(objInfoObject2.Title) + "', to_date('" + objInfoObject2.Properties.Item("SI_CREATION_TIME").Value + "','MM/DD/YYYY HH12:MI:SS AM'), " + numPrompts + ",'" + logon1 + "','" + pswd1 + "', '" + logon2 + "', '" + pswd2 + "','" + errorMsg + "','" + objInfoObject2.SchedulingInfo.Properties.Item("SI_MACHINE_USED").Value + "', to_date('" + objInfoObject2.Properties.Item("SI_UPDATE_TS").Value + "','MM/DD/YYYY HH12:MI:SS AM'), '" + destination + "','" + outputFile + "', '" + objInfoObject2.ParentCUID + "', '" + objInfoObject2.Properties.Item("SI_PARENT_FOLDER_CUID").Value + "','" + distributionList + "', '" + si_file1 + "','" + environ + "','" + objInfoObject2.Kind + "','" + siFormatExportAllpages + "','" + objInfoObject2.CUID + "','" + safeSQL(paramValues) + "')"

				//paramValues = Helper.getParamValueString(oReport);

				///*****************Production Insert STring*************
				//msSQLInsertString = "insert into " + destTblNm + " (REPORT_ID, INSTANCE_ID, SI_STARTTIME, SI_ENDTIME, DURATION, SI_SUBMITTER, SI_SERVER, SI_OWNER, SI_SCHEDULE_STATUS,REPORT_PATH, SI_NAME, SI_CREATION_TIME, SI_NUM_PROMPTS, DATASRC_LOGIN_NM, SI_PASSWORD, DATASRC_LOGIN_NM2, SI_PASSWORD2, SI_ERROR_MESSAGE, SI_MACHINE_USED, SI_UPDATE_TS, DESTINATION, OUTPUT_FILE, REPORT_CUID, SI_PARENT_FOLDER_CUID, DISTRIBUTION_LIST, si_file1, ENVIRONMENT, SI_KIND, SI_FORMAT_EXPORT_ALLPAGES, CUID, PARAM_VALUES) values (" + objInfoObject.getParentID() + ", " + objInfoObject.getID() + ", to_date('" + Helper.oracleDate(startTime) + "','MM/DD/YYYY HH12:MI:SS AM')" + ", to_date('" + Helper.oracleDate(endTime) + "','MM/DD/YYYY HH12:MI:SS AM')" + ", " + theInterval + ", '" + Helper.safeSQL(submitter) + "', '" + Helper.safeSQL(server) + "', '" + Helper.safeSQL(submitter) + "', " + scheduleStatus + ", '" + Helper.safeSQL(cookieCrumbs) + "', '" + Helper.safeSQL(objInfoObject.getTitle()) + "', to_date('" + Helper.msSQLDateTime(creationTime) + "','MM/DD/YYYY HH12:MI:SS AM'), " + numPrompts + ",'" + logon1 + "','" + pswd1 + "', '" + logon2 + "', '" + pswd2 + "','" + errorMsg + "','" + machineUsed + "', to_date('" + Helper.msSQLDateTime(updateTS) + "','MM/DD/YYYY HH12:MI:SS AM'), '" + destination + "','" + outputFile + "', '" + objInfoObject.getParentCUID() + "', '" + parentFolderCuid + "','" + distributionList + "', '" + si_file1 + "','" + EnterpriseEnv + "','" + objInfoObject.getKind() + "'," + siFormatExportAllpages + "','" + objInfoObject.getCUID() + "','" + Helper.safeSQL(paramValues) + "')";
				//Helper.runMSSQLInsertQuery(msSQLInsertString);

				//Just setting this string is throwing an error.
				//System.out.println(msSQLInsertString);
				System.out.println(objInfoObject.getParentID()) ;
				System.out.println(objInfoObject.getID());
				//System.out.println(  objInfoObject.properties().getProperty("SI_STARTTIME").getValue());
				//System.out.println( objInfoObject.properties().getProperty("SI_ENDTIME").getValue());
				System.out.println(startTime);
				System.out.println(endTime);
				System.out.println( theInterval);
				//System.out.println( Helper.safeSQL((String)objInfoObject.getSchedulingInfo().properties().getProperty("SI_SUBMITTER").getValue()));
				System.out.println(submitter);
				System.out.println( Helper.safeSQL(server));
				//System.out.println( objInfoObject.properties().getProperty("SI_SCHEDULE_STATUS").getValue());
				System.out.println(scheduleStatus);
				System.out.println( Helper.safeSQL(cookieCrumbs));
				System.out.println( Helper.safeSQL(objInfoObject.getTitle()));
				//System.out.println( objInfoObject.properties().getProperty("SI_CREATION_TIME").getValue());
				System.out.println( numPrompts);
				System.out.println( logon1 );
				System.out.println( pswd1 );
				System.out.println( logon2);
				System.out.println( pswd2);
				System.out.println( errorMsg );
				//System.out.println( objInfoObject.getSchedulingInfo().properties().getProperty("SI_MACHINE_USED").getValue());
				//System.out.println( objInfoObject.properties().getProperty("SI_UPDATE_TS").getValue());
				System.out.println( destination);
				System.out.println( outputFile);
				System.out.println( objInfoObject.getParentCUID());
				//System.out.println( objInfoObject.properties().getProperty("SI_PARENT_FOLDER_CUID").getValue());
				System.out.println( distributionList);
				System.out.println( si_file1);
				System.out.println( EnterpriseEnv);
				System.out.println( objInfoObject.getKind());
				System.out.println(  siFormatExportAllpages);
				System.out.println( objInfoObject.getCUID());
				System.out.println( Helper.safeSQL(paramValues));

				//testString = objInfoObject.getParentID() + ", " + objInfoObject.getID() + ", to_date('" + objInfoObject.properties().getProperty("SI_STARTTIME").getValue() + "','MM/DD/YYYY HH12:MI:SS AM')" + ", to_date('" + objInfoObject.properties().getProperty("SI_ENDTIME").getValue() + "','MM/DD/YYYY HH12:MI:SS AM')" + ", " + theInterval + ", '" + Helper.safeSQL((String)objInfoObject.getSchedulingInfo().properties().getProperty("SI_SUBMITTER").getValue()) + "', '" + Helper.safeSQL(server) + "', '" + Helper.safeSQL((String)objInfoObject.getSchedulingInfo().properties().getProperty("SI_SUBMITTER").getValue()) + "', " + objInfoObject.properties().getProperty("SI_SCHEDULE_STATUS").getValue() + ", '" + Helper.safeSQL(cookieCrumbs) + "', '" + Helper.safeSQL(objInfoObject.getTitle()) + "', to_date('" + objInfoObject.properties().getProperty("SI_CREATION_TIME").getValue() + "','MM/DD/YYYY HH12:MI:SS AM'), " + numPrompts + ",'" + logon1 + "','" + pswd1 + "', '" + logon2 + "', '" + pswd2 + "','" + errorMsg + "','" + objInfoObject.getSchedulingInfo().properties().getProperty("SI_MACHINE_USED").getValue() + "', to_date('" + objInfoObject.properties().getProperty("SI_UPDATE_TS").getValue() + "','MM/DD/YYYY HH12:MI:SS AM'), '" + destination + "','" + outputFile + "', '" + objInfoObject.getParentCUID() + "', '" + objInfoObject.properties().getProperty("SI_PARENT_FOLDER_CUID").getValue() + "','" + distributionList + "', '" + si_file1 + "','" + EnterpriseEnv + "','" + objInfoObject.getKind() + "'," + siFormatExportAllpages + "','" + objInfoObject.getCUID() + "','" + Helper.safeSQL(paramValues) + "')";
				//testString = objInfoObject.getParentID() + ", " + objInfoObject.getID() + ", to_date('" + objInfoObject.properties().getProperty("SI_STARTTIME").getValue() + "','MM/DD/YYYY HH12:MI:SS AM')" + ", to_date('" + objInfoObject.properties().getProperty("SI_ENDTIME").getValue() + "','MM/DD/YYYY HH12:MI:SS AM')" + ", " + theInterval + ", '" + Helper.safeSQL((String)objInfoObject.getSchedulingInfo().properties().getProperty("SI_SUBMITTER").getValue()) + "', '" + Helper.safeSQL(server) + "', '" + Helper.safeSQL((String)objInfoObject.getSchedulingInfo().properties().getProperty("SI_SUBMITTER").getValue()) + "', " + objInfoObject.properties().getProperty("SI_SCHEDULE_STATUS").getValue() + ", '" + Helper.safeSQL(cookieCrumbs) + "', '" + Helper.safeSQL(objInfoObject.getTitle()) + "', to_date('" + objInfoObject.properties().getProperty("SI_CREATION_TIME").getValue() + "','MM/DD/YYYY HH12:MI:SS AM'), " + numPrompts + ",'" + logon1 + "','" + pswd1 + "', '" + logon2 + "', '" + pswd2 + "','" + errorMsg + "','";// + objInfoObject.getSchedulingInfo().properties().getProperty("SI_MACHINE_USED").getValue() + "', to_date('" + objInfoObject.properties().getProperty("SI_UPDATE_TS").getValue() + "','MM/DD/YYYY HH12:MI:SS AM'), '" + destination + "','" + outputFile + "', '" + objInfoObject.getParentCUID() + "', '" + objInfoObject.properties().getProperty("SI_PARENT_FOLDER_CUID").getValue() + "','" + distributionList + "', '" + si_file1 + "','" + EnterpriseEnv + "','" + objInfoObject.getKind() + "'," + siFormatExportAllpages + "','" + objInfoObject.getCUID() + "','" + Helper.safeSQL(paramValues) + "')";
				//testString = objInfoObject.getParentID() + ", " + objInfoObject.getID() + ", to_date('" + objInfoObject.properties().getProperty("SI_STARTTIME").getValue() + "','MM/DD/YYYY HH12:MI:SS AM')" + ", to_date('" + objInfoObject.properties().getProperty("SI_ENDTIME").getValue() + "','MM/DD/YYYY HH12:MI:SS AM')" + ", " + theInterval + ", '" + Helper.safeSQL((String)objInfoObject.getSchedulingInfo().properties().getProperty("SI_SUBMITTER").getValue()) + "', '" + Helper.safeSQL(server) + "', '" + Helper.safeSQL((String)objInfoObject.getSchedulingInfo().properties().getProperty("SI_SUBMITTER").getValue()) + "', " + objInfoObject.properties().getProperty("SI_SCHEDULE_STATUS").getValue() + ", '" + Helper.safeSQL(cookieCrumbs) + "', '" + Helper.safeSQL(objInfoObject.getTitle()) + "', to_date('" + objInfoObject.properties().getProperty("SI_CREATION_TIME").getValue() + "','MM/DD/YYYY HH12:MI:SS AM'), " + numPrompts + ",'" + logon1 + "','" + pswd1 + "', '" + logon2 + "', '" + pswd2 + "','" + errorMsg + "','";
				//testString = objInfoObject.getSchedulingInfo().properties().getProperty("SI_MACHINE_USED").getValue() + "', to_date('" + objInfoObject.properties().getProperty("SI_UPDATE_TS").getValue() + "','MM/DD/YYYY HH12:MI:SS AM'), '" + destination + "','" + outputFile + "', '" + objInfoObject.getParentCUID() + "', '" + objInfoObject.properties().getProperty("SI_PARENT_FOLDER_CUID").getValue() + "','" + distributionList + "', '" + si_file1 + "','" + EnterpriseEnv + "','" + objInfoObject.getKind() + "'," + siFormatExportAllpages + "','" + objInfoObject.getCUID() + "','" + Helper.safeSQL(paramValues) + "')";
				//System.out.println(testString);


				try
				{

					//Helper.runMSSQLInsertQuery(msSQLInsertString);
					destination = "";
				}
				catch (Exception ex)
				{
					System.out.println("Problem with insert query");
					Helper.runMSSQLInsertQuery("insert into STG1_BO4_TRACKER_ERRORS (ERROR_MESSAGE, LOAD_DT) VALUES ('" + ex.getMessage() + " " + ex.getStackTrace() + " " + EnterpriseEnv + "', GETDATE())");
					System.out.println(ex.getMessage() + " " + ex.getStackTrace());
				}
			}

			// Clean up the Enterprise Session.
			if(objEnterpriseSession != null) {
				try {
					objEnterpriseSession.logoff();

				} catch(Exception e_ignore_in_cleanup) {}
			}

		}
		catch (Exception ex)
		{
			System.out.println(ex.getMessage() + " " + ex.getStackTrace());
			Helper.runMSSQLInsertQuery("insert into STG1_BO4_TRACKER_ERRORS (ERROR_MESSAGE, LOAD_DT) VALUES ('" + ex.getMessage() + " " + ex.getStackTrace() + " " + EnterpriseEnv + "', GETDATE())");
			//'sendmail("Logon error", ex.getMessage() + " " + ex.getStackTrace)
			//Helper.sendmail("bishopp@lexmark.com", "bishopp@lexmark.com, amarabes@lexmark.com,ajpatel@lexmark.com,msankar@lexmark.com,ajain@lexmark.com", "Logon error - load_stg1_migr_XI_reports(String, String, String, String, String) - " + EnterpriseEnv, ex.getMessage() + " " + ex.getStackTrace());
			//Helper.sendmail("bishopp@lexmark.com", "bishopp@lexmark.com", "Logon error - load_stg1_boxi_runhist(String, String, String, String, String) - " + EnterpriseEnv, ex.getMessage() + " " + ex.getStackTrace());

		}


	}

	public static void load_stg1_short_runhist(String EnterpriseEnv, String login, String pswd,String auth, String destTblNm, String environ){
		//Precondition:  jobs that ran since the last load are not in the database table.
		//Postcondition:  jobs that ran tsince the last load are in the database table.
		//Differs from load_stg1_boxi_runhist() in that it uses very few fields

		IEnterpriseSession objEnterpriseSession;
		//IInfoStore objInfoStore;
		IInfoObjects colInfoObjects;
		IInfoObject objInfoObject;
		IInfoObject objInfoObject2;
		String errorMsg = "";
		String cookieCrumbs;
		int count;
		String si_owner = "";
		Calendar runDate;
		Calendar startTime= Calendar.getInstance();
		Calendar endTime= Calendar.getInstance();
		Calendar creationTime = Calendar.getInstance();
		Calendar updateTS = Calendar.getInstance();
		long theInterval = 0;
		Calendar startDate;
		Calendar endDate;
		String dtToday;
		String msSQLInsertString;
		String maxID = "0";
		String logon1 = "unknown";
		String logon2 = "unknown";
		String pswd1 = "unknown";
		String pswd2 = "unknown";
		String datasrc1 = "not found in CMS";
		String datasrc2 = "not found in CMS";
		int numPrompts = 0;
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
		String scheduleStatus = "999"; //means is not yet known.  I made this up because this is not a valid value.  Tells me it's not getting set from the recordset.
		IProcessingInfo processingInfo;
		IReport iReport;
		String server ="";
		String server1 = "";
		String server2 = "";
		IProperty aggCount;
		String strCount="0";
		int colCount = 0;
		Boolean si_format_export_allpages;
		String siFormatExportAllpages;
		String paramValues = "";
		//		IWebiPrompts iWebiPrompts;
		IPrompts webiPrompts;
		IReport oReport;
		String submitter = "";
		String machineUsed = "";
		String parentFolderCuid = "";
		String siOwner = "";
		String testString = "";
		int schedStatus;
		String siKind = "";
		String cuid = "";
		String reportCuid = "";
		int instanceID = 0;

		try 
		{
			ISessionMgr  objSessionMgr = CrystalEnterprise.getSessionMgr();
			objEnterpriseSession = objSessionMgr.logon(login, pswd, EnterpriseEnv, auth);    
			IInfoStore objInfoStore = (IInfoStore) objEnterpriseSession.getService("InfoStore");

			maxID = Helper.getMaxID("select max(instance_id) maxID from " + destTblNm);
			System.out.println("maxID is " + maxID);
			//maxCreationDate = Helper.getMaxLoadDt("select max(si_creation_time) maxLoadDate from " + destTblNm);
			//System.out.println("Max creation date is " + maxCreationDate);
			maxEndDate = Helper.getMaxLoadDt("select max(si_endtime) maxLoadDate from " + destTblNm);
			System.out.println("Max end date is " + Helper.msSQLDateTime(maxEndDate));

			if (maxID == "0" || maxID == null) {
				//Is a first-time load
				strInfoSQL = "SELECT count(SI_ID) from CI_INFOOBJECTS WHERE SI_INSTANCE = 1 and si_recurring = 0";
			}
			else
				//is an incremental load
				strInfoSQL = "SELECT count(SI_ID) from CI_INFOOBJECTS WHERE SI_INSTANCE = 1 and si_recurring = 0 and si_endtime >= '" + Helper.formatDateBO(maxEndDate) + "'" ;
			//strInfoSQL = "SELECT count(SI_ID) from CI_INFOOBJECTS WHERE SI_INSTANCE = 1 and si_recurring = 0 and si_creation_time >= '" + Helper.formatDateBO(maxCreationDate) + "' and si_schedule_status=3" ;

			System.out.println(strInfoSQL);

			colInfoObjects = (IInfoObjects)objInfoStore.query(strInfoSQL);
			//objInfoObject = (IInfoObject)colInfoObjects.get(i); 

			objInfoObject = (IInfoObject)colInfoObjects.get(0);

			//The VB code used the collection here, but in Java, properties() isn't an available function, so I am trying to get this via InfoObject

			//System.out.println(objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").properties().getProperty("SI_ID"));
			System.out.println(objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").getID());//shows the ID, not the count!
			//count = objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").properties().getProperty("SI_ID");
			//count = objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").getID();
			aggCount = objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT");

			count = objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").getID();
			strCount = ((IProperties)objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").getValue()).getProperty("SI_ID").getValue().toString();

			System.out.println("Record count is " + strCount);


			if (maxID == "0" || maxID == null) {
				strInfoSQL = "SELECT TOP " + strCount + " * FROM CI_INFOOBJECTS WHERE  SI_INSTANCE = 1 and si_recurring=0";

				///**********JUST TEMPORARY FOR TESTING*************
				//strInfoSQL = "SELECT TOP 100 * FROM CI_INFOOBJECTS WHERE  SI_INSTANCE = 1 and si_recurring=0";
				//strInfoSQL = "SELECT TOP 100 si_id, CUID, si_name, si_submitter, si_starttime, si_endtime, si_creation_time, si_schedule_status, si_kind, si_parent_cuid FROM CI_INFOOBJECTS WHERE  SI_INSTANCE = 1 and si_recurring=0";
				//strInfoSQL = "SELECT TOP 100 * FROM CI_INFOOBJECTS WHERE  SI_INSTANCE = 1 and si_recurring=0";
			}
			else
				//strInfoSQL = "SELECT TOP " + strCount + " SI_ID FROM CI_INFOOBJECTS WHERE SI_KIND in ('CrystalReport') and SI_INSTANCE = 0 and SI_CREATION_TIME >= '" + Helper.formatDateBO(maxCreationDate) + "'";
				//PRODUCTION SQL:
				//strInfoSQL = "SELECT TOP " + strCount + " * FROM CI_INFOOBJECTS WHERE  SI_INSTANCE = 1 and si_recurring=0 and SI_CREATION_TIME >= '" + Helper.formatDateBO(maxCreationDate) + "'";
				strInfoSQL = "SELECT TOP " + strCount + " * FROM CI_INFOOBJECTS WHERE  SI_INSTANCE = 1 and si_recurring=0 and SI_ENDTIME >= '" + Helper.formatDateBO(maxEndDate) + "'";

			///**********JUST TEMPORARY FOR TESTING*************
			//strInfoSQL = "SELECT TOP 100 si_id, CUID, si_name, si_submitter, si_starttime, si_endtime, si_creation_time, si_schedule_status, si_kind, si_parent_cuid FROM CI_INFOOBJECTS WHERE  SI_INSTANCE = 1 and si_recurring=0 and SI_ENDTIME >= '" + Helper.formatDateBO(maxEndDate) + "'";
			//strInfoSQL = "SELECT TOP 100 * FROM CI_INFOOBJECTS WHERE  SI_INSTANCE = 1 and si_recurring=0 and SI_ENDTIME >= '" + Helper.formatDateBO(maxEndDate) + "'";


			//JUST A TEST TO SEE HOW FAILED INSTANCES WORK IN THE CODE
			//strInfoSQL = "SELECT TOP " + strCount + " SI_ID FROM CI_INFOOBJECTS WHERE  SI_INSTANCE = 1 and si_recurring=0 and si_schedule_status=1 and SI_CREATION_TIME >= '" + Helper.formatDateBO(maxCreationDate) + "'";

			System.out.println(strInfoSQL);
			colInfoObjects = (IInfoObjects)objInfoStore.query(strInfoSQL);
			System.out.println("Line 1889 Count of objects in collection: " + colInfoObjects.size());

			if (colInfoObjects.size() > 0){
				Connection con=null;
				Class.forName("oracle.jdbc.driver.OracleDriver");
				con=DriverManager.getConnection("jdbc:oracle:thin:@dlxkrbodb1.lex.lexmark.com:1750:edr1den1","botrack","********");
				Statement s=con.createStatement();
				//'This records all the instances found on the BOXI system
				colCount = colInfoObjects.size();
				for (int x=0; x <= colCount; x++){
					//msSQLInsertString = "insert into temp_p_allinstances(report_id, instance_id) values (" + objInfoObject.getParentID() + "," + objInfoObject.getID() + ")";
					//System.out.println(msSQLInsertString);
					//s.execute(msSQLInsertString);
					objInfoObject = (IInfoObject)colInfoObjects.get(x);
					//oReport = (IReport)colInfoObjects.get(x);  //4/27/2012:  causes error against 3.1 Prod: $Proxy0 cannot be cast to com.crystaldecisions.sdk.plugin.desktop.report.IReport [Ljava.lang.StackTraceElement;@aa559d
					//That's because this is an instance and it will throw this error if the object is not .rpt, which would usually be
					//the case.

					System.out.println ("si_id is " + objInfoObject.getID());

					try {
						//reportCuid.equals(objInfoObject.getParentCUID().toString());
						reportCuid = objInfoObject.getParentCUID().toString();
						System.out.println("Report cuid is " + reportCuid);
					}
					catch (Exception ex) {
						System.out.println(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getStackTrace());
					}


					try {
						//cuid.equals(objInfoObject.getCUID().toString());
						cuid = objInfoObject.getCUID().toString();
						System.out.println("Instance cuid is " + cuid);
					}catch (Exception ex) {
						System.out.println(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getStackTrace());
					}


					try {
						instanceID = objInfoObject.getID();
						System.out.println("report instance id is " + instanceID);
					}
					catch (Exception ex) {
						System.out.println(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getStackTrace());
					}



					try {
						IProperties properties = objInfoObject.properties();
						IProperty startTimeProperty = properties.getProperty(CePropertyID.SI_STARTTIME);
						//String value = (String) creationTimeProperty.getValue();
						startTime.setTime ((Date) startTimeProperty.getValue());  //This works
						System.out.println("start time is " + Helper.msSQLDateTime(startTime));
					}
					catch (Exception ex) {
						System.out.println(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getStackTrace());
					}

					/*try {
						runDate = (Calendar)objInfoObject.properties().getProperty("SI_STARTTIME").getValue();
						System.out.println("Rundate is " + runDate);
					} catch (Exception ex) {
						System.out.println(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getStackTrace());
					}*/

					try {
						IProperties properties = objInfoObject.properties();
						IProperty endTimeProperty = properties.getProperty(CePropertyID.SI_ENDTIME);
						//String value = (String) creationTimeProperty.getValue();
						endTime.setTime ((Date) endTimeProperty.getValue());  //This works
						System.out.println("end time is " + Helper.msSQLDateTime(endTime));
					}
					catch (Exception ex) {
						System.out.println(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getStackTrace());
					}				


					try {
						//IProperties properties = objInfoObject.properties();
						//IProperty submitterProperty = properties.getProperty(CePropertyID.SI_SUBMITTER);

						//submitter.equals(submitterProperty.toString());  //doesn't work
						//submitter = submitterProperty.toString();


						submitter =objInfoObject.getSchedulingInfo().properties().getProperty("SI_SUBMITTER").getValue().toString();
						System.out.println("submitter " + submitter);
					}
					catch (Exception ex) {
						System.out.println(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getStackTrace());
					}

					try {
						//Got this from D:\Documents and Settings\bishopp\My Documents\BOXI\Code Examples\xi4_boejava_dg_en\html\topic15.html
						IProperties properties = objInfoObject.properties();
						IProperty creationTimeProperty = properties.getProperty(CePropertyID.SI_CREATION_TIME);
						//String value = (String) creationTimeProperty.getValue();
						creationTime.setTime ((Date) creationTimeProperty.getValue());  //This works
						//System.out.println("creation time is " + Helper.msSQLDateTime(creationTime));
						System.out.println("creation time is " + creationTime.getTime());
					}
					catch (Exception ex)
					{
						ex.printStackTrace();
						System.out.println(" for report id: " +objInfoObject.getID() + " " + ex.getMessage() + " " + ex.getStackTrace());
					}



					/*try {
						IProperties properties = objInfoObject.properties();
						IProperty scheduleStatusProperty = properties.getProperty(CePropertyID.SI_SCHEDULE_STATUS);
						//String value = (String) creationTimeProperty.getValue();
						scheduleStatus.equals(scheduleStatusProperty.toString());
						System.out.println("scheduleStatus " + scheduleStatus);
					}
					catch (Exception ex) {

					}*/


					try {
						scheduleStatus = objInfoObject.properties().getProperty("SI_SCHEDULE_STATUS").getValue().toString();
						schedStatus = objInfoObject.getSchedulingInfo().getStatus();
					} 
					catch (Exception ex) {
						System.out.println(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getStackTrace());
					}

					try {
						siKind.equals(objInfoObject.getKind());
					} 
					catch (Exception ex) {
						System.out.println(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getStackTrace());
					}



					try
					{
						//Helper.runMSSQLInsertQuery(msSQLInsertString);
						destination = "";
						destination.equals(objInfoObject.getSchedulingInfo().getDestination().toString());
					}
					catch (Exception ex)
					{
						System.out.println("Problem with insert query");
						//Helper.runMSSQLInsertQuery("insert into STG1_BO4_TRACKER_ERRORS (ERROR_MESSAGE, LOAD_DT) VALUES (" + ex.getMessage() + " " + ex.getStackTrace() + " " + EnterpriseEnv + "', GETDATE())");
						System.out.println(ex.getMessage() + " " + ex.getStackTrace());
					}


					//paramValues = Helper.getParamValueString(oReport);

					///*****************Production Insert STring*************
					msSQLInsertString = "insert into " + destTblNm + " (REPORT_ID, INSTANCE_ID, SI_STARTTIME, SI_ENDTIME,  SI_SUBMITTER, SI_SCHEDULE_STATUS, SI_KIND,  SI_NAME, SI_CREATION_TIME, CUID, REPORT_CUID) values (" + objInfoObject.getParentID() + ", " + instanceID + ", ('" + Helper.msSQLDateTime(startTime) + "')" + ", ('" + Helper.msSQLDateTime(endTime) + "')" + ", '" + Helper.safeSQL(submitter) + "', " + scheduleStatus   + ", '" + objInfoObject.getKind() + "','" + Helper.safeSQL(objInfoObject.getTitle()) + "', ('" + Helper.msSQLDateTime(creationTime) + "') "   +  ",'"  +  cuid + "','" + reportCuid +  "')";
					System.out.println(msSQLInsertString);
					Helper.runMSSQLInsertQuery(msSQLInsertString);

				}

			}

			else {
				System.out.println ("No report history found.");
			}

			// Clean up the Enterprise Session.
			if(objEnterpriseSession != null) {
				try {
					objEnterpriseSession.logoff();

				} catch(Exception e_ignore_in_cleanup) {}
			}
		}

		catch (Exception ex)
		{
			System.out.println(ex.getMessage() + " " + ex.getStackTrace());
			Helper.runMSSQLInsertQuery("insert into STG1_BO4_TRACKER_ERRORS (ERROR_MESSAGE, LOAD_DT) VALUES ('" + ex.getMessage() + " " + ex.getStackTrace() + " " + EnterpriseEnv + "', GETDATE())");
			//'sendmail("Logon error", ex.getMessage() + " " + ex.getStackTrace)
			//Helper.sendmail("bishopp@lexmark.com", "bishopp@lexmark.com, amarabes@lexmark.com,ajpatel@lexmark.com,msankar@lexmark.com,ajain@lexmark.com", "Logon error - load_stg1_migr_XI_reports(String, String, String, String, String) - " + EnterpriseEnv, ex.getMessage() + " " + ex.getStackTrace());
			//Helper.sendmail("bishopp@lexmark.com", "bishopp@lexmark.com", "Logon error - load_stg1_boxi_runhist(String, String, String, String, String) - " + EnterpriseEnv, ex.getMessage() + " " + ex.getStackTrace());

		}


	}

	public static void deleteDups(String targetTable, String columnNames){
		//Since as of 3/31/2007, I am now loading only by si_creation_date, the tables get the same record more than once.
		//Must delete these dups as they go in the or reporting will be inflated.
		//Precondition:  there might be duplicate rows in the table
		//Postcondition:  there are no duplicate rows in the table
		try
		{
			String driver = "mssql.jdbc.driver.SQLDriver";
			String url = "jdbc:sqlserver://DASHWBODB012;databaseName=EDR1DEN1;integratedSecurity=true;";

			ConnectMSSQLServer connServer = new ConnectMSSQLServer();
			connServer.dbConnect("jdbc:sqlserver://DASHWBODB012", "BOTRACK", "********");


			//Class.forName(driver);		//loads the driver
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			Connection conn = DriverManager.getConnection("jdbc:sqlserver://DASHWBODB012", "BOTRACK", "********");
			System.out.println("connected");


			Statement stmt = conn.createStatement() ; 

		}

		catch (Exception ex)
		{
			ex.printStackTrace();
			System.out.println(ex.getMessage() + " " + ex.getStackTrace());
		}		
	}
	public static void getReportErrorMessage()
	{
		IInfoStore iStore=null;
		IInfoObjects oInfoObjects=null;
		IInfoObject oInfoObject=null;
		SDKException failure = null;
		IEnterpriseSession es = null;


		String user = "tracker";
		String password = "libambini*8";
		String cmsName = "uslexbcs01";
		String cmsAuthType = "secEnterprise";
		String kind = "";

		try
		{
			es = CrystalEnterprise.getSessionMgr().logon(user, password, cmsName, cmsAuthType);
		}
		catch(SDKException e)
		{
			System.out.println("Logon failed");
		}


		try {

			//Query for the report whose properties will be changed
			iStore = (IInfoStore) es.getService("", "InfoStore");
			//oInfoObjects = iStore.query("SELECT * FROM CI_INFOOBJECTS WHERE SI_ID = 17696 and si_instance = 1");
			//oInfoObjects = iStore.query("SELECT * FROM CI_INFOOBJECTS WHERE SI_ID in (34214572, 31034180) and si_instance = 1");
			oInfoObjects = iStore.query("SELECT TOP 3200 * FROM CI_INFOOBJECTS WHERE SI_SCHEDULE_STATUS=3 and si_instance = 1 and si_kind in('Excel', 'Pdf') and si_endtime >='2009.05.03'");
			System.out.println(oInfoObjects.size());
			int theCount = oInfoObjects.size();
			//oInfoObject = (IInfoObject)oInfoObjects.get(0);
			String errorMessage = "";

			for (int j=0; j<theCount; j++){
				oInfoObject = (IInfoObject)oInfoObjects.get(j);
				//System.out.println(oInfoObject.getTitle());
				//System.out.println(oInfoObject.getKind());

				//Cast the IInfo Object to a IReport object to use the methods of a report
				//IReport oReport = (IReport)oInfoObject;
				kind=oInfoObject.getKind();
				try
				{
					errorMessage = (String)oInfoObject.properties().getProperty("SI_ERROR_MESSAGE").getValue();
				}
				catch (Exception ex){
					//System.out.println(oInfoObject.getKind() + " SI_ERROR_MESSAGE  was not found in the collection " + oInfoObject.getID() + " " + oInfoObject.getTitle());
					try
					{
						IExcel oReport = (IExcel)oInfoObject;
						System.out.println("Error message: " + oInfoObject.getID() + " " + oInfoObject.getTitle() + " - " + oReport.getSchedulingInfo().getErrorMessage());
					}
					catch (Exception ex3){
						//System.out.println("Not an Excel output");
					}

					try
					{
						IPDF oReport = (IPDF)oInfoObject;
						System.out.println("Error message: " + oInfoObject.getID() + " " + oInfoObject.getTitle() + " - " + oReport.getSchedulingInfo().getErrorMessage());
					}
					catch (Exception ex3){
						//System.out.println("Not a Pdf output");
					}

					/*	try {
						System.out.println("Looking for the error message...");
						if (kind == "Excel"){
							System.out.println("Excel report failed.");
							//Cast the IInfo Object to a IExcel object to use the methods of an excel object
							IExcel oReport = (IExcel)oInfoObject;
							//Set the thumbnail and Repository settings
							System.out.println("Error message: " + oReport.getSchedulingInfo().getErrorMessage());
						}
						else if (kind == "Pdf"){
							System.out.println("Pdf report failed.");
							//Cast the IInfo Object to a IPDF object to use the methods of a pdf
							IPDF oReport = (IPDF)oInfoObject;
							//Set the thumbnail and Repository settings
							System.out.println("Error message: " + oReport.getSchedulingInfo().getErrorMessage());
						}
						else
							System.out.println("msg:  something is wrong... '" + oInfoObject.getKind() + "'");
					}
					catch (Exception ex1)
					{
						System.out.println(ex1.getMessage() + " " + ex1.getStackTrace());
					}*/

				}

			}
			// Clean up the Enterprise Session.
			if(es != null) {
				try {
					es.logoff();

				} catch(Exception e_ignore_in_cleanup) {}
			}
		}


		catch (SDKException e) {
			System.out.println(e.getMessage() + " " + e.getStackTrace());
			//throw e;			
		}
	}





	public static void load_stg1_Webi_Reports (String EnterpriseEnv , String login, String pswd, String auth, String destTblNm ){
		//Pre-condition:  new Webi, Xcelsius, Explorer and Flash reports are not in the Tracker database
		//Post-condition: new Webi, Xcelsius, Explorer and Flash reports are in the Tracker database
		String strInfoSQL ;
		//Logon;
		//ISessionMgr objSessionMgr = new CrystalEnterprise.getSessionMgr(); //should be New()


		IEnterpriseSession objEnterpriseSession;
		//IInfoStore objInfoStore;
		IInfoObjects colInfoObjects;
		IInfoObject objInfoObject;
		IInfoObjects colInfoObjects2;   
		IInfoObject objInfoObject2;
		int count = 1000000;
		String strCount = "0";
		Date rundate;
		String si_user;
		String cookiecrumbs;
		String maxid;
		String si_owner = "";
		//CrystalReportPluginLib.TablePrefix tablePrefix;
		//ReportClientDocument oReportClientDoc;
		//IReportAppFactory oReportAppFactory;
		IReportLogon oReportLogon;

		String server1   = "none needed";
		String server2  = "none needed";
		String msSQLInsertString;

		String sourceSystem  = "TR";
		String businessViewNm = "";
		String originalFileNm = "";
		String localFilePath  = "";
		Calendar maxCreationDate;
		//java.sql.Date maxCreationDate;
		Calendar creationTime = Calendar.getInstance();

		//Helper helperFn = new Helper();  //for some reason this isn't needed
		int i = 0;
		//String maxid;

		IProperties si_files;
		String  si_path = "";
		String si_file1 = "";
		String server[] = new String[ 2 ];
		IEnterpriseSession eSession;
		IProperty aggCount;
		int colCount = 0;
		IProperties siUniverseProps;
		IProperties siDSLUniverseProps;
		int siUniverse = 0;
		int siDSLUniverse = 0;
		String webiDocProperties = "";
		Calendar si_timestamp;
		int si_doc_common_connection = 0; //this is used when a Webi report uses BICS connection
		//I'll also use it for SI_COM_CONNECTION  for Analysis reports
		String datasourceType = "";
		String si_datasourceid ="";
		String si_datasourceproviderid = "";
		String datasourceNm = "";
		String reportNm = "";
		String firstSix = "";
		String reportNameTestResult;

		try 
		{                            	
			//si_timestamp = "#1/1/1980#";
			String str_date = "1980-01-01";
			DateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
			Date date = (Date) formatter.parse(str_date);
			si_timestamp = Calendar.getInstance();
			si_timestamp.setTime(date);
		}
		catch (Exception e) {
			System.out.println("Exception :" + e);
		}

		try
		{
			ISessionMgr  objSessionMgr = CrystalEnterprise.getSessionMgr();
			objEnterpriseSession = objSessionMgr.logon(login, pswd, EnterpriseEnv, auth);    
			IInfoStore objInfoStore = (IInfoStore) objEnterpriseSession.getService("InfoStore");

			//Since I learned that si_id can be reused by CMS,I started using dates to do incrementals (03/30/2007)

			System.out.println("select max(si_creation_time) from " + destTblNm);
			maxCreationDate = Helper.getMaxLoadDt("select max(si_creation_time) maxLoadDate from " + destTblNm);
			System.out.println("Max creation date is " + maxCreationDate);
			System.out.println("Max creation date is " + Helper.monthDayYear(maxCreationDate));
			maxid = Helper.getMaxID("select max(si_id) maxID from " + destTblNm);

			//////////just for testing////////
			//maxid = "0";
			//////////////////////////////////

			if (maxid == "0")  {
				//'strInfoSQL = "SELECT count(SI_ID) from CI_INFOOBJECTS WHERE SI_PROGID= 'CrystalEnterprise.Report' and SI_INSTANCE = 0"
				strInfoSQL = "SELECT count(SI_ID) from CI_INFOOBJECTS WHERE SI_KIND in ('Webi','Xcelsius','Flash','XL.XcelsiusEnterprise','DataDiscovery','MDAnalysis') and SI_INSTANCE = 0";
				//strInfoSQL = "SELECT count(SI_ID) from CI_INFOOBJECTS WHERE SI_KIND in ('Webi','Xcelsius','Flash','XL.XcelsiusEnterprise','DataDiscovery') and SI_INSTANCE = 0 and CUID in ('AUSqSsaKXZtCrt7v4wWrKB4','AZpEqXME70hMlyn9ZRU0bwA','ARsqwXIoLf5KgA.bZWAwKb0','AWWRiiyFfyNMoa4iBXmZq0M','AXkJ1ozyyhFCvQ_KP4dV4w8','ARIZDfSz8i1IsjEk2oyLeHY')";
				//strInfoSQL = "SELECT count(SI_ID) from CI_INFOOBJECTS WHERE SI_KIND in ('MDAnalysis') and SI_INSTANCE = 0";
			}
			else
				//'strInfoSQL = "SELECT count(SI_ID) from CI_INFOOBJECTS WHERE SI_PROGID= 'CrystalEnterprise.Report' and SI_INSTANCE = 0 and si_creation_time > '" + formatDateBO(maxCreationDate) + "'"
				//'strInfoSQL = "SELECT count(SI_ID) from CI_INFOOBJECTS WHERE SI_PROGID= 'CrystalEnterprise.Report' and SI_INSTANCE = 0 and si_creation_time >= '" + formatDateBO(maxCreationDate) + "'"  '4/18/2007
				strInfoSQL = "SELECT count(SI_ID) from CI_INFOOBJECTS WHERE SI_KIND in ('Webi','Xcelsius','Flash','XL.XcelsiusEnterprise','DataDiscovery','MDAnalysis') and SI_INSTANCE = 0 and si_creation_time >= '" + Helper.formatDateBO(maxCreationDate) + "'" ;
			//strInfoSQL = "SELECT count(SI_ID) from CI_INFOOBJECTS WHERE SI_KIND in ('Webi','Xcelsius','Flash','XL.XcelsiusEnterprise','DataDiscovery') and SI_INSTANCE = 0  and CUID in ('AUSqSsaKXZtCrt7v4wWrKB4','AZpEqXME70hMlyn9ZRU0bwA','ARsqwXIoLf5KgA.bZWAwKb0','AWWRiiyFfyNMoa4iBXmZq0M','AXkJ1ozyyhFCvQ_KP4dV4w8','ARIZDfSz8i1IsjEk2oyLeHY')" ;
			//strInfoSQL = "SELECT count(SI_ID) from CI_INFOOBJECTS WHERE SI_KIND in ('MDAnalysis') and SI_INSTANCE = 0";

			//''***************PRODUCTION CODE***********UNCOMMENT AFTER TEST

			System.out.println(strInfoSQL);

			//EXAMPLE OF RETRIEVING PROPERTY BAG VALUES
			//objInfoObject.properties().getProperty("SI_UPDATE_TS");

			colInfoObjects = (IInfoObjects)objInfoStore.query(strInfoSQL);
			//objInfoObject = (IInfoObject)colInfoObjects.get(i);    
			objInfoObject = (IInfoObject)colInfoObjects.get(0);

			//The VB code used the collection here, but in Java, properties() isn't an available function, so I am trying to get this via InfoObject

			//System.out.println(objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").properties().getProperty("SI_ID"));
			System.out.println(objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").getID());//shows the ID, not the count!
			//count = objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").properties().getProperty("SI_ID");
			//count = objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").getID();
			aggCount = objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT");
			//count = aggCount.getValue().toString();
			//count = aggCount.getID().SIZE; //32
			//count = aggCount.getID().intValue(); //same as si_id value
			//count = aggCount.BAG;
			//count = aggCount.ALL; 65536, wrong 
			//count = aggCount.PROP_ID_LIST; //too big
			//count = aggCount.getID().hashCode(); //same as si_id
			//count = aggCount.getFlags(); //that's not it
			//count = aggCount.BINARY; //that's not it
			//count = aggCount.getID().byteValue(); //that's not it
			strCount = ((IProperties)objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").getValue()).getProperty("SI_ID").getValue().toString();

			System.out.println("Record count is " + strCount);
			//count = (int)strCount;

			//System.out.println("Record count is " + aggCount.getValue().toString());

			//''***************PRODUCTION CODE***********UNCOMMENT AFTER TEST
			if (maxid == "0") {
				strInfoSQL = "SELECT TOP " + strCount + " SI_ID FROM CI_INFOOBJECTS WHERE SI_KIND in ('Webi','Xcelsius','Flash','XL.XcelsiusEnterprise','DataDiscovery','MDAnalysis') and SI_INSTANCE = 0";
				//strInfoSQL = "SELECT TOP " + strCount + " SI_ID FROM CI_INFOOBJECTS WHERE SI_KIND in ('Webi','Xcelsius','Flash','XL.XcelsiusEnterprise','DataDiscovery') and SI_INSTANCE = 0 and CUID in ('AUSqSsaKXZtCrt7v4wWrKB4','AZpEqXME70hMlyn9ZRU0bwA','ARsqwXIoLf5KgA.bZWAwKb0','AWWRiiyFfyNMoa4iBXmZq0M','AXkJ1ozyyhFCvQ_KP4dV4w8','ARIZDfSz8i1IsjEk2oyLeHY')";
				//strInfoSQL = "SELECT TOP " + strCount + " SI_ID FROM CI_INFOOBJECTS WHERE SI_KIND in ('MDAnalysis') and SI_INSTANCE = 0";
			}
			else
				//'strInfoSQL = "SELECT TOP " + count + " SI_ID FROM CI_INFOOBJECTS WHERE SI_PROGID= 'CrystalEnterprise.Report' and SI_INSTANCE = 0 and SI_ID>" + CInt(maxid)
				//'strInfoSQL = "SELECT TOP " + count + " SI_ID FROM CI_INFOOBJECTS WHERE SI_PROGID= 'CrystalEnterprise.Report' and SI_INSTANCE = 0 and SI_CREATION_TIME > '" + formatDateBO(maxCreationDate) + "'"
				strInfoSQL = "SELECT TOP " + strCount + " SI_ID FROM CI_INFOOBJECTS WHERE SI_KIND in ('Webi','Xcelsius','Flash','XL.XcelsiusEnterprise','DataDiscovery','MDAnalysis') and SI_INSTANCE = 0 and SI_CREATION_TIME >= '" + Helper.formatDateBO(maxCreationDate) + "'";
			//strInfoSQL = "SELECT TOP " + strCount + " SI_ID FROM CI_INFOOBJECTS WHERE CUID in ('AUSqSsaKXZtCrt7v4wWrKB4','AZpEqXME70hMlyn9ZRU0bwA','ARsqwXIoLf5KgA.bZWAwKb0','AWWRiiyFfyNMoa4iBXmZq0M','AXkJ1ozyyhFCvQ_KP4dV4w8','ARIZDfSz8i1IsjEk2oyLeHY')";
			//strInfoSQL = "SELECT TOP " + strCount + " SI_ID FROM CI_INFOOBJECTS WHERE SI_KIND in ('MDAnalysis') and SI_INSTANCE = 0";

			//////////////////////////////////////////
			/////////////////////////////////////////
			////JUST TEMPORARY TO FIGURE OUT WHY THESE WON'T LOAD''''////////////////
			//strInfoSQL = "SELECT TOP 50 SI_ID FROM CI_INFOOBJECTS WHERE CUID in ('AS6pDsMLpUFGtOOw82Mi_x8','AZYhVai5THRFnUflSl5IJB8','AUfwZGEN_Q5MvEATQEFoCEg','AVvDqdqgW6BNkc8pWqvnDyw','Af1S_5pydA5AnJgJQqE7iPM','AaR2gcKo3GRKn_wLWAiuYbc','AYLSiYBuBQpPjOTE5Zy6bIY','AeAVs5dFnf1DrgKDygaqJ14','AYHoQErgrBlOhMrCwrpuEzM','AVEyKTklgdBJtPDqtnXNFPs','AW7AKurb.O1DvjHH29lFKUQ','Ac4V_n4Xm_9Nvfxy0FjwjMg','AfDUCMPmmhFNr8_3Zm9u7nQ','Af0sJK5psUBAoP4sYxtfQPo','AUc1fT1Q1r1DpySnhYCPq7g','AfNEKVmYV1dHh.7zajqbsdI','AW8sJb8ithJKl_a1K8lHUd0','AQUKobsOKzRBh8fdkhVQ5B4','AY5jB8mm_PVFuUCddeZmVy0','Abe82YwehkdGr_NOHqACl7Y')";		
			////////////////////////////////////////
			////////////////////////////////////////

			//''***************PRODUCTION CODE***********UNCOMMENT AFTER TEST
			System.out.println("line 241 " + strInfoSQL);

			colInfoObjects = (IInfoObjects)objInfoStore.query(strInfoSQL);
			System.out.println("number of Webi reports:  " + colInfoObjects.size());
			if (colInfoObjects.size() > 0)  {
				System.out.println("We do have objects in collection");		

				//Connection con=null;
				//Class.forName("oracle.jdbc.driver.OracleDriver");
				//System.out.println("oracle driver loaded in load_stg1_Webi_Reports()");
				//con=DriverManager.getConnection("jdbc:oracle:thin:@dlxkrbodb1.lex.lexmark.com:1750:edr1den1","botrack","********");
				//Statement s=con.createStatement();


				//'System.out.println("creation time  " + objInfoObject2.Properties.Item("SI_CREATION_TIME").getValue)

				//oReportAppFactory = (IReportAppFactory)objEnterpriseSession.getService("", "RASReportService");
				System.out.println("collection size is " + colInfoObjects.size());
				colCount = colInfoObjects.size();
				for (int k =0; k < colCount; k++)
				{
					System.out.println("iterator, k, is now " + k);
					objInfoObject = (IInfoObject) colInfoObjects.get(k);
					//'Record all the si_ids discovered by the query to BOXI
					msSQLInsertString = "insert into TEMP40_P_ALLRPTIDS (report_id) values (" + objInfoObject.getID() + ")";
					System.out.println(msSQLInsertString);
					Helper.runMSSQLInsertQuery(msSQLInsertString);
					//s.execute(msSQLInsertString);
					System.out.println("Insert to TEMP40_P_ALLRPTIDS done");

					//'throws an error when I let it just rip through the query result.
					strInfoSQL = "Select * From CI_INFOOBJECTS Where SI_INSTANCE=0 and SI_ID =" + objInfoObject.getID();
					System.out.println(strInfoSQL);
					colInfoObjects2 = (IInfoObjects)objInfoStore.query(strInfoSQL);
					int colCount2 = colInfoObjects2.size();

					for (int j =0; j < colCount2; j++)
					{
						objInfoObject2 = (IInfoObject)colInfoObjects2.get(j);

						//Only do the rest of this if the report isn't an ad-hoc Webi or Analysis autosave
						reportNm = objInfoObject2.getTitle();
						if (!reportNm.equals("Analysis autosave")) {

							//The minimum number of digits that we see in a Webi ad-hoc instance is 6
							//Any report with a name with less than 4 digits will throw a "index out of range error".
							//Test for report length.  Most likly the report is called "Test"!
							if (reportNm.length() > 5){
								firstSix = reportNm.substring(0, 5);
								reportNameTestResult = Helper.report_Name_Validator(firstSix);
								System.out.println("Report name test result is " + reportNameTestResult);

							} else {
								reportNameTestResult = "false";
							}

							System.out.println("Report name test result is " + reportNameTestResult);
							if (reportNameTestResult.equals("false")) {

								//cookiecrumbs = Helper.BuildCookieTrail(objEnterpriseSession, objInfoObject2.getParentID(), "");
								Helper crumbs = new Helper();
								cookiecrumbs = crumbs.getFolderPath1(objEnterpriseSession, objInfoObject2.getParentID(), "");

								//cookiecrumbs = Helper.getMyString("12345678");

								System.out.println("Cookiecrumb trail is " + cookiecrumbs);
								System.out.println("Cookiecrumb length is " + cookiecrumbs.length());

								if (cookiecrumbs.length() > 0) {
									//'If the cookiecrumbs ends up null, then it was an Inbox document,
									//'which I don't want.
									System.out.println("cookiecrumbs length > 0");

									System.out.println("objInfoObject2 id:  " + objInfoObject2.getID());
									/*System.out.println("server1 length is " + server1.length());
							if (server1.length() > 0) {
							}*/


									try
									{
										//'Get the local file path if it exists
										localFilePath = (String)objInfoObject2.properties().getProperty("SI_LOCAL_FILEPATH").getValue();
										//'Get the original file name
										localFilePath = (String)objInfoObject2.properties().getProperty("SI_LOCAL_FILEPATH").getValue();
										localFilePath.indexOf("~");

										originalFileNm = (localFilePath.substring(localFilePath.indexOf("~")));
										System.out.println("original file name is " + originalFileNm);
									}
									catch (Exception ex)
									{
										//I don't care if I get this value or not.
										//throw new RuntimeException(ex);
									}


									try
									{
										si_owner = objInfoObject2.properties().getProperty("SI_OWNER").getValue().toString();
									}
									catch (Exception ex)
									{
										//throw new RuntimeException(ex);
									}

									try 
									{
										//siUniverse = objInfoObject2.Properties.Item("SI_UNIVERSE").Properties.Item("1").Value;
										siUniverseProps = (IProperties) objInfoObject2.properties().getProperty("SI_UNIVERSE").getValue();
										siUniverse = (Integer) siUniverseProps.getProperty("1").getValue();
										datasourceType = "unv";
										datasourceNm = "see SI_UNIVERSE";
									}
									catch (Exception ex) {

									}

									try 
									{
										//I'll store the DSL universe id (.unx) in the same field as the .unv to make joins easier
										siDSLUniverseProps = (IProperties) objInfoObject2.properties().getProperty("SI_DSL_UNIVERSE").getValue();
										siUniverse = (Integer) siDSLUniverseProps.getProperty("1").getValue();
										datasourceType="unx";
										datasourceNm = "see SI_UNIVERSE";
									}
									catch (Exception ex) {

									}

									try
									{
										//Got this from D:\Documents and Settings\bishopp\My Documents\BOXI\Code Examples\xi4_boejava_dg_en\html\topic15.html
										IProperties properties = objInfoObject2.properties();
										IProperty creationTimeProperty = properties.getProperty(CePropertyID.SI_CREATION_TIME);
										//String value = (String) creationTimeProperty.getValue();
										creationTime.setTime ((Date) creationTimeProperty.getValue());  //This works
										//System.out.println("creation time is " + creationTime);
										System.out.println("creation time is " + creationTime.getTime());
									}
									catch (Exception ex){
										System.out.println("exception detected in getting creation time" + ex.getMessage() + " " + ex.getStackTrace());
									}

									try
									{
										webiDocProperties = (String)objInfoObject2.properties().getProperty("SI_WEBI_DOC_PROPERTIES").getValue().toString();
										//Just save the first 2000 characters to get the required info.  The column accepts only up to 2000 characters and I see no reason to go beyond that.
										webiDocProperties =webiDocProperties.substring(0, 1999);
										System.out.println("Length of webiDocProperties " + webiDocProperties.length());
									}
									catch (Exception ex) {
									}

									try
									{
										si_datasourceid = (String)objInfoObject2.properties().getProperty("SI_DATASOURCEID").getValue().toString();
										//Must parse this XML to find "name="LexmarkHANAProd" or "name="LexmarkNonProd".  You can also get the cube name from it, too, which is strange if it's against Hana Enterprise.
										System.out.println("si_datasourceid is " + si_datasourceid);
										if (si_datasourceid.contains("LexmarkHANAProd")) {
											datasourceNm = "LexmarkHANAProd";
											datasourceType = "HANA";
										}

										if (si_datasourceid.contains("LexmarkNonProd")) {
											datasourceNm = "LexmarkNonProd";
											datasourceType = "HANA";
										}

										if (si_datasourceid.contains("SAP_Connection_to_HANA")) {
											datasourceNm = "SAP_Connection_to_HANA";
											datasourceType = "HANA";
										}

										if (si_datasourceid.length() < 35) {
											//This is a cuid of a universe
											datasourceNm = "";
											datasourceType = "HANA";
											String CUID = si_datasourceid.substring(7, 29);
											//Need to get the si_id of the universe so we can populate SI_UNIVERSE
											siUniverse = Helper.getUniverseID(objEnterpriseSession, CUID);
										}

									}
									catch (Exception ex) {

									}

									try
									{
										si_datasourceproviderid = (String)objInfoObject2.properties().getProperty("SI_DATASOURCEPROVIDERID").getValue().toString();
									}
									catch (Exception ex) {

									}

									try
									{
										si_timestamp = (Calendar)objInfoObject2.properties().getProperty("SI_TIMESTAMP").getValue();
									}
									catch (Exception ex) {

									}

									try 
									{
										si_files = (IProperties) objInfoObject2.properties().getProperty("SI_FILES").getValue();
										si_path = (String) si_files.getProperty("SI_PATH").getValue();
										si_file1 = (String) si_files.getProperty("SI_FILE1").getValue();
									}
									catch (Exception ex) {

									}

									try
									{
										//si_doc_common_connection = (int)objInfoObject2.properties().getProperty("SI_DOC_COMMON_CONNECTION").getID();

										IProperties cxnProperties = (IProperties)objInfoObject2.properties().getProperty("SI_DOC_COMMON_CONNECTION").getValue();
										//IProperty creationTimeProperty = properties.getProperty(CePropertyID.SI_CREATION_TIME);

										IProperty  docCommonConnProp = (IProperty)cxnProperties.get("1");
										si_doc_common_connection = (Integer) docCommonConnProp.getValue();
										datasourceType = "Bex"; 

										System.out.println("si_doc_common_connection is " + si_doc_common_connection);
									}
									catch (Exception ex) {

									}

									try
									{
										//si_doc_common_connection = (int)objInfoObject2.properties().getProperty("SI_DOC_COMMON_CONNECTION").getID();
										//This is the connection property on an Analysis report
										IProperties cxnProperties = (IProperties)objInfoObject2.properties().getProperty("SI_COM_CONNECTION").getValue();
										//IProperty creationTimeProperty = properties.getProperty(CePropertyID.SI_CREATION_TIME);

										IProperty  comConnProp = (IProperty)cxnProperties.get("1");
										si_doc_common_connection = (Integer) comConnProp.getValue();
										datasourceType = "Bex"; 

										System.out.println("si_doc_common_connection is " + si_doc_common_connection);
									}
									catch (Exception ex) {
										System.out.println(ex.getMessage());
									}

									//msSQLInsertString = "INSERT INTO " + destTblNm + " (SI_ID, SI_PATH, SI_FILE1, REPORT_NM, REPORT_FILE_PATH, DATA_SOURCE_NM, DATA_SOURCE2_NM, DYNAMIC_PICK_LIST_NM, LOAD_DT, SI_CREATION_TIME, SI_UPDATE_TS, SOURCE_SYSTEM, CUID, SI_BUSINESS_VIEW_INFO, SI_LOCAL_FILEPATH, ORIGINAL_FILE_NM, SI_PARENT_FOLDER_CUID, SI_OWNER) VALUES (" + objInfoObject2.getID() + ", '" + Helper.safeSQL(si_path) + "', '" + Helper.safeSQL(si_file1) + "', '" + Helper.safeSQL(objInfoObject2.getTitle()) + "', '" + Helper.safeSQL(cookiecrumbs) + "','" + server1 + "', '" + server2 + "','See STG1_MIGR_RPT_PICK_LIST_REL',sysdate, to_date('" + objInfoObject2.properties().getProperty("SI_CREATION_TIME").getValue() + "','MM/DD/YYYY HH12:MI:SS AM'), to_date('" + objInfoObject2.properties().getProperty("SI_UPDATE_TS").getValue() + "','MM/DD/YYYY HH12:MI:SS AM'), '" + sourceSystem + "','" + objInfoObject2.getCUID() + "','" + Helper.safeSQL(businessViewNm) + "',' " + Helper.safeSQL(localFilePath) + "','" + originalFileNm + "', '" + objInfoObject2.properties().getProperty("SI_PARENT_FOLDER_CUID").getValue() + "','" + si_owner + "')";
									//msSQLInsertString = "INSERT INTO " + destTblNm + " (SI_ID, SI_PATH, SI_FILE1, REPORT_NM, REPORT_FILE_PATH,  LOAD_DT, SI_CREATION_TIME,  CUID, SI_PARENT_CUID, SI_OWNER, ENVIRONMENT, SI_KIND, SI_PARENTID, SI_UNIVERSE, SI_WEBI_DOC_PROPERTIES, SI_DOC_COMMON_CONNECTION, DATASOURCE_TYPE, DATA_SOURCE_NM) VALUES (" + objInfoObject2.getID() + ", '" + Helper.safeSQL(si_path) + "', '" + Helper.safeSQL(si_file1) + "', '" + Helper.safeSQL(objInfoObject2.getTitle()) + "', '" + Helper.safeSQL(cookiecrumbs) + "', sysdate, to_date('" + Helper.msSQLDateTime(creationTime) + "','DD-MM-YYYY HH24:MI:SS'), '" + objInfoObject2.getCUID() + "','" + objInfoObject2.getParentCUID() + "','" + si_owner + "','" + EnterpriseEnv + "','" + objInfoObject2.getKind()+ "'," + objInfoObject2.getParentID() + "," + siUniverse + " ,'" + Helper.safeSQL(webiDocProperties) + "', " + si_doc_common_connection + ", '" + datasourceType +"', '" + datasourceNm +"' )";
									msSQLInsertString = "INSERT INTO " + destTblNm + " (SI_ID, SI_PATH, SI_FILE1, REPORT_NM, REPORT_FILE_PATH,  LOAD_DT, SI_CREATION_TIME,  CUID, SI_PARENT_CUID, SI_OWNER, ENVIRONMENT, SI_KIND, SI_PARENTID, SI_UNIVERSE, SI_WEBI_DOC_PROPERTIES, SI_DOC_COMMON_CONNECTION, DATASOURCE_TYPE, DATA_SOURCE_NM) VALUES (" + objInfoObject2.getID() + ", '" + Helper.safeSQL(si_path) + "', '" + Helper.safeSQL(si_file1) + "', '" + Helper.safeSQL(objInfoObject2.getTitle()) + "', '" + Helper.safeSQL(cookiecrumbs) + "', GETDATE(), ('" + Helper.msSQLDateTime(creationTime) + "'), '" + objInfoObject2.getCUID() + "','" + objInfoObject2.getParentCUID() + "','" + si_owner + "','" + EnterpriseEnv + "','" + objInfoObject2.getKind()+ "'," + objInfoObject2.getParentID() + "," + siUniverse + " ,'" + Helper.safeSQL(webiDocProperties) + "', " + si_doc_common_connection + ", '" + datasourceType +"', '" + datasourceNm +"' )";


									//oraInserter.CommandText = msSQLInsertString;
									System.out.println(msSQLInsertString);
									//'Comment out just to see if the getCEReportParams method is working
									try
									{
										Helper.runMSSQLInsertQuery(msSQLInsertString);
									}
									catch (Exception ex)
									{
										ex.printStackTrace();
										System.out.println(" for report id: " + objInfoObject2.getID() + " " + ex.getMessage() + " " + ex.getStackTrace());
										//'Records the skipped si_id
										msSQLInsertString = "insert into temp_p_skippedrptids (report_id) values (" + objInfoObject2.getID() + ")";
										//Helper.runMSSQLInsertQuery(msSQLInsertString);
									}
								}
							}
							originalFileNm = "";
							localFilePath = "";
							businessViewNm = "";
							si_doc_common_connection = 0;
							datasourceType = "";
							datasourceNm = "";
							siUniverse = 0;
							siDSLUniverse = 0;

							//''Then find all the dynamic pick list prompts and store in the Rel table
							//'9/13/2006:  Not sure this is necessary because we already know which ones have dynamic pick list from CE
							//'oraInsertString2 = getCEReportParams(objEnterpriseSession, objCERpt, objInfoObject2.getID())
							//'If oraInsertString2 <> Nothing Then
							//'    System.out.println(oraInsertString2)
							//'    oraInserter.CommandText = oraInsertString2
							//'    oraInserter.ExecuteNonQuery()
							//'End If
							//server1 = "not found in CMS";
							//server2 = "not found in CMS";
							server1 = "";
							server2 = "";



						}
					}
				}
				//s.close();
				//con.close();
				objSessionMgr = null;
				//objEnterpriseSession = Nothing;
				objInfoStore = null;
				colInfoObjects = null;
				objInfoObject = null;
				colInfoObjects2 = null;
				objInfoObject2 = null;

			}
			// Clean up the Enterprise Session.
			if(objEnterpriseSession != null) {
				try {
					objEnterpriseSession.logoff();

				} catch(Exception e_ignore_in_cleanup) {}
			}
		}
		catch (Exception ex)
		{
			System.out.println(ex.getMessage());
			ex.printStackTrace(System.out);
			Helper.runMSSQLInsertQuery("insert into STG1_BO4_TRACKER_ERRORS (ERROR_MESSAGE, LOAD_DT) VALUES ('" + ex.getMessage() + " " + ex.getStackTrace() + " " + EnterpriseEnv + "', GETDATE())");
			//'sendmail("Logon error", ex.getMessage() + " " + ex.getStackTrace)
			//Helper.sendmail("bishopp@lexmark.com", "bishopp@lexmark.com, amarabes@lexmark.com,ajpatel@lexmark.com,msankar@lexmark.com,ajain@lexmark.com", "Logon error - load_stg1_migr_XI_reports(String, String, String, String, String) - " + EnterpriseEnv, ex.getMessage() + " " + ex.getStackTrace());
			Emailer.sendEmail("bishopp@lexmark.com", "bishopp@lexmark.com", "Logon error - load_stg1_boxi_web(String, String, String, String, String) - " + EnterpriseEnv, ex.getMessage() + " " + ex.getStackTrace(),null);

		}

	}

	public static void load_stg1_boxi_recurringIds(String EnterpriseEnv, String login, String pswd,String auth, String destTblNm, String environ){
		//Precondition:  today's recurring job id's since the last load are not in the database table.
		//Postcondition:  today's recurring jobs id's since the last load are in the database table.

		IEnterpriseSession objEnterpriseSession;
		//IInfoStore objInfoStore;
		IInfoObjects colInfoObjects;
		IInfoObjects colRecurObjects;
		IInfoObject objInfoObject;
		IInfoObject objInstObject;
		IInfoObject objRecurringObject;
		String strInfoSQL;
		IProperty aggCount;
		int count;
		String strCount="0";

		try {
			ISessionMgr  objSessionMgr = CrystalEnterprise.getSessionMgr();
			objEnterpriseSession = objSessionMgr.logon(login, pswd, EnterpriseEnv, auth);    
			IInfoStore objInfoStore = (IInfoStore) objEnterpriseSession.getService("InfoStore");

			strInfoSQL = "SELECT count(SI_ID) from CI_INFOOBJECTS WHERE SI_INSTANCE = 1 and si_recurring = 1";
			System.out.println(strInfoSQL);

			colInfoObjects = (IInfoObjects)objInfoStore.query(strInfoSQL);
			//objInfoObject = (IInfoObject)colInfoObjects.get(i); 

			objInfoObject = (IInfoObject)colInfoObjects.get(0);

			//The VB code used the collection here, but in Java, properties() isn't an available function, so I am trying to get this via InfoObject

			//System.out.println(objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").properties().getProperty("SI_ID"));
			System.out.println(objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").getID());//shows the ID, not the count!
			//count = objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").properties().getProperty("SI_ID");
			//count = objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").getID();
			aggCount = objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT");

			count = objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").getID();
			strCount = ((IProperties)objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").getValue()).getProperty("SI_ID").getValue().toString();

			System.out.println("Record count is " + strCount);

			strInfoSQL = "SELECT TOP " + strCount + " SI_ID FROM CI_INFOOBJECTS WHERE  SI_INSTANCE = 1 and si_recurring=1  order by SI_ID";

			System.out.println(strInfoSQL);
			colRecurObjects = (IInfoObjects)objInfoStore.query(strInfoSQL);
			System.out.println("Line 5515 Count of objects in collection: " + colRecurObjects.size());

			//'*************INSERT THE SI_ID'S INTO A TEMP TABLE********
			//Helper.runMSSQLInsertQuery("truncate table temp_recurring_ids"); //Since I run this app as BOTRACK, he can't truncate
			Helper.runMSSQLInsertQuery("delete from temp_recurring_ids");

			int numRows = colRecurObjects.size();
			for (int y = 0; y < colRecurObjects.size(); y++){
				objRecurringObject = (IInfoObject)colRecurObjects.get(y);
				Helper.runMSSQLInsertQuery("insert into temp_recurring_ids (recurring_id) values (" + objRecurringObject.getID() + ")");

			}
			//Log off BOBJ Enterprise;
			objEnterpriseSession.logoff();
			objSessionMgr = null;
			objInfoStore = null;

			//Reading from the table, load one-by-one to the staging table
			//1. Connect to Oracle
			//2. Run the SELECT statement
			//3. Loop through the recordset and call the load function for each row.
			//4. Disconnect form Oracle

			String driver = "oracle.jdbc.driver.OracleDriver";
			String url = "jdbc:oracle:thin:@dlxkrbodb1.lex.lexmark.com:1750:edr1den1";
			String username = "botrack";
			String password = "********";
			String oraSelectString = "Select RECURRING_ID from temp_recurring_ids";
			int recurringID = 0;
			try
			{
				System.out.println(oraSelectString);
				Class.forName(driver);		//loads the driver
				Connection conn = DriverManager.getConnection(url,username,password);	

				Statement stmt = conn.createStatement() ; 
				ResultSet rs = stmt.executeQuery(oraSelectString);
				while (rs.next()) 
				{
					recurringID = rs.getInt("RECURRING_ID"); 
					LoadFunctions.load_stg1_boxi_recurringjobsByID(EnterpriseEnv, login, pswd, auth,destTblNm, environ, recurringID);
				}
				conn = null;
				conn.close();
			}	
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
			// Clean up the Enterprise Session.
			if(objEnterpriseSession != null) {
				try {
					objEnterpriseSession.logoff();

				} catch(Exception e_ignore_in_cleanup) {}
			}

		}
		catch (Exception ex){
			System.out.println(ex.getMessage() + " " + ex.getStackTrace());
		}
	}

	public static void load_temp_recurring_jobs(String EnterpriseEnv, String login, String pswd,String auth, String destTblNm, String environ){
		IEnterpriseSession objEnterpriseSession;
		//IInfoStore objInfoStore;
		IInfoObjects colInfoObjects;
		IInfoObjects colRecurObjects;
		IInfoObject objInfoObject;
		IInfoObject objInstObject;
		IInfoObject objRecurringObject;
		String strInfoSQL;
		IProperty aggCount;
		int count;
		String strCount="0";
		String msSQLInsertString;
		String mssqlSelectString;
		int theID;

		try {
			ISessionMgr  objSessionMgr = CrystalEnterprise.getSessionMgr();
			objEnterpriseSession = objSessionMgr.logon(login, pswd, EnterpriseEnv, auth);    
			IInfoStore objInfoStore = (IInfoStore) objEnterpriseSession.getService("InfoStore");


			strInfoSQL = "SELECT count(SI_ID) from CI_INFOOBJECTS WHERE SI_INSTANCE = 1 and si_recurring = 1";


			System.out.println(strInfoSQL);

			colInfoObjects = (IInfoObjects)objInfoStore.query(strInfoSQL);
			//objInfoObject = (IInfoObject)colInfoObjects.get(i); 

			objInfoObject = (IInfoObject)colInfoObjects.get(0);

			//The VB code used the collection here, but in Java, properties() isn't an available function, so I am trying to get this via InfoObject

			//System.out.println(objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").properties().getProperty("SI_ID"));
			System.out.println(objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").getID());//shows the ID, not the count!
			//count = objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").properties().getProperty("SI_ID");
			//count = objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").getID();
			aggCount = objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT");

			count = objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").getID();
			strCount = ((IProperties)objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").getValue()).getProperty("SI_ID").getValue().toString();

			System.out.println("Record count is " + strCount);

			strInfoSQL = "SELECT TOP " + strCount + " SI_ID FROM CI_INFOOBJECTS WHERE  SI_INSTANCE = 1 and si_recurring=1  order by SI_ID";

			System.out.println(strInfoSQL);
			colRecurObjects = (IInfoObjects)objInfoStore.query(strInfoSQL);
			System.out.println("Line 4910 Count of objects in collection: " + colRecurObjects.size());

			//'*************INSERT THE SI_ID'S INTO A TEMP TABLE TO COMPARE WITH WHAT YOU GOT LOADED IN THE RECURRINGJOBS TABLE********
			Helper.runMSSQLInsertQuery("truncate table temp_recurring_ids");

			//DON'T DO THIS ANYMORE/////////////  Why not??????
			for (int y = 0; y < colRecurObjects.size(); y++){
				objRecurringObject = (IInfoObject)colRecurObjects.get(y);
				Helper.runMSSQLInsertQuery("insert into temp_recurring_ids (recurring_id) values (" + objRecurringObject.getID() + ")");
			}
			// Clean up the Enterprise Session.
			if(objEnterpriseSession != null) {
				try {
					objEnterpriseSession.logoff();

				} catch(Exception e_ignore_in_cleanup) {}
			}
		}
		catch (Exception ex)
		{
			System.out.println("Error getting missing recurring id's " + ex.getStackTrace() + " " + ex.getMessage());
			ex.printStackTrace();
		}

	}
	public static void load_stg1_boxi_recurringMakeup(String EnterpriseEnv, String login, String pswd,String auth, String destTblNm, String environ){
		//Precondition:  today's recurring jobs snapshot since the last load are not in the database table.
		//Postcondition:  today's recurring jobs snapshot since the last load are in the database table.

		IEnterpriseSession objEnterpriseSession;
		//IInfoStore objInfoStore;
		IInfoObjects colInfoObjects;
		IInfoObjects colRecurObjects;
		IInfoObject objInfoObject;
		IInfoObject objInstObject;
		IInfoObject objRecurringObject;
		String strInfoSQL;
		IProperty aggCount;
		int count;
		String strCount="0";
		String msSQLInsertString;
		String mssqlSelectString;
		int theID;

		try {
			ISessionMgr  objSessionMgr = CrystalEnterprise.getSessionMgr();
			objEnterpriseSession = objSessionMgr.logon(login, pswd, EnterpriseEnv, auth);    
			IInfoStore objInfoStore = (IInfoStore) objEnterpriseSession.getService("InfoStore");


			strInfoSQL = "SELECT count(SI_ID) from CI_INFOOBJECTS WHERE SI_INSTANCE = 1 and si_recurring = 1";


			System.out.println(strInfoSQL);

			colInfoObjects = (IInfoObjects)objInfoStore.query(strInfoSQL);
			//objInfoObject = (IInfoObject)colInfoObjects.get(i); 

			objInfoObject = (IInfoObject)colInfoObjects.get(0);

			//The VB code used the collection here, but in Java, properties() isn't an available function, so I am trying to get this via InfoObject

			//System.out.println(objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").properties().getProperty("SI_ID"));
			System.out.println(objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").getID());//shows the ID, not the count!
			//count = objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").properties().getProperty("SI_ID");
			//count = objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").getID();
			aggCount = objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT");

			count = objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").getID();
			strCount = ((IProperties)objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").getValue()).getProperty("SI_ID").getValue().toString();

			System.out.println("Record count is " + strCount);

			strInfoSQL = "SELECT TOP " + strCount + " SI_ID FROM CI_INFOOBJECTS WHERE  SI_INSTANCE = 1 and si_recurring=1  order by SI_ID";

			System.out.println(strInfoSQL);
			colRecurObjects = (IInfoObjects)objInfoStore.query(strInfoSQL);
			System.out.println("Line 4910 Count of objects in collection: " + colRecurObjects.size());

			//'*************INSERT THE SI_ID'S INTO A TEMP TABLE TO COMPARE WITH WHAT YOU GOT LOADED IN THE RECURRINGJOBS TABLE********
			//Helper.runMSSQLInsertQuery("truncate table temp_recurring_ids");

			//I loaded this on the server so comment for now
			/*	for (int y = 0; y < colRecurObjects.size(); y++){
				objRecurringObject = (IInfoObject)colRecurObjects.get(y);
				Helper.runMSSQLInsertQuery("insert into temp_recurring_ids (recurring_id) values (" + objRecurringObject.getID() + ")");
			}*/

			//Now put in some code to load the needed records to the staging table based on what's missing.
			//1. Get the set of si_id's that don't exist in the stage1 recurringjobs table.
			//2.  Iterate through this recordset by calling load_stg1_boxi_recurringjobsByID()

			//Get the missing id's:
			String driver = "mssql.jdbc.driver.SQLDriver";
			String url = "jdbc:sqlserver://DASHWBODB012;databaseName=EDR1DEN1;integratedSecurity=true;";
			String username = "botrack";
			String password = "********";
			int theCount = 0;
			try
			{
				mssqlSelectString = "select RECURRING_ID from temp_recurring_ids a left outer join " + destTblNm + " b on a.recurring_id = b.instance_id where b.instance_id is null"; 
				System.out.println(mssqlSelectString);
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				Connection conn = DriverManager.getConnection("jdbc:sqlserver://DASHWBODB012", "BOTRACK", "********");
				System.out.println("connected");

				Statement stmt = conn.createStatement() ; 
				ResultSet rs = stmt.executeQuery(mssqlSelectString);
				while (rs.next()) 
				{
					theID = rs.getInt("RECURRING_ID"); 
					load_stg1_boxi_recurringjobsByID(EnterpriseEnv,login, pswd,auth, destTblNm, environ, theID);
				}
				conn = null;
				conn.close();
			}	
			catch (Exception ex)
			{
				System.out.println("Error getting missing recurring id's " + ex.getStackTrace() + " " + ex.getMessage());
				ex.printStackTrace();
			}

			// Clean up the Enterprise Session.
			if(objEnterpriseSession != null) {
				try {
					objEnterpriseSession.logoff();

				} catch(Exception e_ignore_in_cleanup) {}
			}
		}
		catch (Exception ex){
			System.out.println(ex.getMessage() + " " + ex.getStackTrace());
		}
	}

	public static void load_stg1_boxi_recurringjobIds(String EnterpriseEnv, String login, String pswd,String auth, String destTblNm, String environ){
		//Precondition:  today's recurring jobs snapshot since the last load are not in the database table.
		//Postcondition:  today's recurring jobs snapshot since the last load are in the database table.

		IEnterpriseSession objEnterpriseSession;
		//IInfoStore objInfoStore;
		IInfoObjects colInfoObjects;
		IInfoObjects colRecurObjects;
		IInfoObject objInfoObject;
		IInfoObject objInstObject;
		IInfoObject objRecurringObject;
		String errorMsg = "";
		String cookieCrumbs;
		int count;
		String si_owner;
		Calendar runDate = Calendar.getInstance();
		Calendar startTime = Calendar.getInstance();
		startTime.set(1980,1,1,0,0,0);
		Calendar endTime = Calendar.getInstance();
		endTime.set(1980,1,1,0,0,0);
		long theInterval = 0;
		Calendar startDate = Calendar.getInstance();;
		startDate.set(1980,1,1,0,0,0);
		Calendar endDate = Calendar.getInstance();;
		endDate.set(1980,1,1,0,0,0);
		String dtToday;
		String msSQLInsertString;
		String maxID ="0";
		String logon1 = "unknown";
		String logon2 = "unknown";
		String pswd1 = "unknown";
		String pswd2 = "unknown";
		String datasrc1 = "not found in CMS";
		String datasrc2 = "not found in CMS";
		int numPrompts = 0;
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
		String scheduleStatus = "9";
		IProcessingInfo processingInfo;
		IReport iReport;
		String server;
		String server1 = "";
		String server2 = "";
		IProperty aggCount;
		String strCount="0";
		int colCount = 0;
		Boolean si_format_export_allpages;
		String siFormatExportAllpages  = "non-existent";
		String paramValues;
		String recurringIDList ="0";
		String machine_used;
		//IPrompts siPrompts;
		int nbrPrompts =0;
		String promptName;
		String paramValueString = "";

		Calendar nextRunTime = Calendar.getInstance();
		nextRunTime.set(1980,1,1,0,0,0);
		String machineUsed = "";
		String submitter = "";
		String owner = "";
		Calendar creationTime = Calendar.getInstance();
		creationTime.set(1980,1,1,0,0,0);

		Calendar updateTS = Calendar.getInstance();
		updateTS.set(1980,1,1,0,0,0);

		String parentCuid = "unknown";
		String parentFolderCuid = "";
		String cuid = "";
		int scheduleType = 0;
		int scheduleIntervalMinutes = 0;
		int scheduleIntervalDays = 0;
		int scheduleIntervalHours = 0;
		int scheduleIntervalMonths = 0;



		try {
			ISessionMgr  objSessionMgr = CrystalEnterprise.getSessionMgr();
			objEnterpriseSession = objSessionMgr.logon(login, pswd, EnterpriseEnv, auth);    
			IInfoStore objInfoStore = (IInfoStore) objEnterpriseSession.getService("InfoStore");


			String driver = "oracle.jdbc.driver.OracleDriver";
			String url = "jdbc:oracle:thin:@dlxkrbodb1.lex.lexmark.com:1750:edr1den1";
			String username = "botrack";
			String password = "********";

			//Get the recurring_id's that are in the temp_recurring_ids but not in stg1_bo4p_recurringjobs
			String oraSelectString ="select distinct i.RECURRING_ID from temp_recurring_ids i, (select instance_id from stg1_bo4p_recurringjobs where load_dt >= GETDATE()) rc where i.RECURRING_ID =  rc.INSTANCE_ID (+) and rc.instance_id is null";
			try
			{
				System.out.println(oraSelectString);
				Class.forName(driver);		//loads the driver
				Connection conn = DriverManager.getConnection(url,username,password);	

				Statement stmt = conn.createStatement() ; 
				ResultSet rs = stmt.executeQuery(oraSelectString);
				while (rs.next()) 
				{
					recurringIDList += ", " + rs.getString("RECURRING_ID"); 

				}
				recurringIDList = recurringIDList.substring(2, recurringIDList.length());
				System.out.println(recurringIDList);  //make sure the string looks right
				//conn = null;
				conn.close();
			}	
			catch (Exception ex)
			{
				ex.printStackTrace();
				System.out.println(ex.getMessage() + " " + ex.getStackTrace() + " line 4959");
			}
			strInfoSQL = "SELECT count(SI_ID) from CI_INFOOBJECTS WHERE SI_INSTANCE = 1 and SI_RECURRING=1 and si_id in (" + recurringIDList + ")";

			System.out.println(strInfoSQL);

			colInfoObjects = (IInfoObjects)objInfoStore.query(strInfoSQL);
			//objInfoObject = (IInfoObject)colInfoObjects.get(i); 

			objInfoObject = (IInfoObject)colInfoObjects.get(0);

			//The VB code used the collection here, but in Java, properties() isn't an available function, so I am trying to get this via InfoObject

			//System.out.println(objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").properties().getProperty("SI_ID"));
			System.out.println(objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").getID());//shows the ID, not the count!
			//count = objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").properties().getProperty("SI_ID");
			//count = objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").getID();
			aggCount = objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT");

			count = objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").getID();
			strCount = ((IProperties)objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").getValue()).getProperty("SI_ID").getValue().toString();

			System.out.println("Record count is " + strCount);

			//strInfoSQL = "SELECT TOP " + strCount + " SI_ID FROM CI_INFOOBJECTS WHERE SI_KIND in ('CrystalReport') and SI_INSTANCE = 0 and SI_CREATION_TIME >= '" + Helper.formatDateBO(maxCreationDate) + "'";
			//PRODUCTION SQL:
			strInfoSQL = "SELECT TOP " + strCount + " SI_ID FROM CI_INFOOBJECTS WHERE  SI_INSTANCE = 1 and si_recurring=1 and si_id in (" + recurringIDList + ") order by SI_ID";
			//JUST A TEST TO SEE HOW FAILED INSTANCES WORK IN THE CODE
			//strInfoSQL = "SELECT TOP " + strCount + " SI_ID FROM CI_INFOOBJECTS WHERE  SI_INSTANCE = 1 and si_recurring=0 and si_schedule_status=1 and SI_CREATION_TIME >= '" + Helper.formatDateBO(maxCreationDate) + "'";

			System.out.println(strInfoSQL);
			colRecurObjects = (IInfoObjects)objInfoStore.query(strInfoSQL);
			System.out.println("Line 210 Count of objects in collection: " + colRecurObjects.size());


			//'*************INSERT THE SI_ID'S INTO A TEMP TABLE TO COMPARE WITH WHAT YOU GOT LOADED IN THE RECURRINGJOBS TABLE********
			//Helper.runMSSQLInsertQuery("truncate table temp_recurring_ids");


			for (int y = 0; y < colRecurObjects.size(); y++){
				objRecurringObject = (IInfoObject)colRecurObjects.get(y);
				Helper.runMSSQLInsertQuery("insert into temp_recurring_ids (recurring_id) values (" + objRecurringObject.getID() + ")");
			}
			// Clean up the Enterprise Session.
			if(objEnterpriseSession != null) {
				try {
					objEnterpriseSession.logoff();

				} catch(Exception e_ignore_in_cleanup) {}
			}

		}
		catch (Exception ex) {
			System.out.println(ex.getMessage() + " " + ex.getStackTrace() + " line 5008");
		}

	}



	public static void deleteDups(String targetTable, String columnNames, String Env){
		//'Since as of 3/31/2007, I am now loading only by si_creation_date, the tables get the same record more than once.
		//'Must delete these dups as they go in the or reporting will be inflated.
		//OracleCommand oradeleter;
		//Dim devConn As New OracleConnection
		// Dim oraDeleteString As String
		//devConn.ConnectionString = oraConnStr
		//oradeleter = New OracleCommand
		//oradeleter.CommandType = Data.CommandType.Text
		//oradeleter.Connection = devConn
		//oradeleter.Connection.Open()
		String oraDeleteString = "";
		System.out.println(targetTable.indexOf("STG1"));
		System.out.println(targetTable.startsWith("STG1"));
		//Aug 18, 2014:  need to limit this to the last few days. As is, it's running too slowly
		//Changed it to look at the last five load dates only
		if (targetTable.startsWith("STG1")) {
			//oraDeleteString = "delete from " + targetTable + " where rowid in (select max(rowid) from  " + targetTable + " group by " + columnNames + " having count(*) > 1)";
			//oraDeleteString = "delete from " + targetTable + " where rowid in (select max(rowid) from  " + targetTable + " where trunc(load_dt) >= trunc(sysdate-5) group by " + columnNames + " having count(*) > 1)";
			oraDeleteString = "delete from " + targetTable + " where rowid in (select max(rowid) from  " + targetTable + " where trunc(load_dt) >= trunc(sysdate-5) group by " + columnNames + " having count(*) > 1)";
		}
		else {
			//oraDeleteString = "delete from " + targetTable + " where environment = '" + Env + "' and rowid in (select max(rowid) from  " + targetTable + " where environment = '" + Env + "' group by " + columnNames + " having count(*) > 1)";
			oraDeleteString = "delete from " + targetTable + " where environment = '" + Env + "' and rowid in (select max(rowid) from  " + targetTable + " where environment = '" + Env + "'  and trunc(load_dt) >= trunc(sysdate-5) group by " + columnNames + " having count(*) > 1)";

		}

		System.out.println(oraDeleteString);
		try {
			Helper.runMSSQLInsertQuery(oraDeleteString);  //I'm hoping this will work for a delete query
		}
		catch (Exception ex){
			System.out.println("Problem with delete query " + ex.getMessage() + " " + ex.getStackTrace());
		}

	}

	public static void load_report_path_snapshot(String EnterpriseEnv, String login, String pswd,String auth, String destTblNm) {
		//Pre-condition:  a current snapshot of report cuid's and their paths is not available
		//Post-condition:  a current snapshot of report cuid's and their paths is available

		IEnterpriseSession objEnterpriseSession;
		//IInfoStore objInfoStore;
		IInfoObjects colInfoObjects;
		IInfoObjects colInfoObjects2;
		IInfoObject objInfoObject;
		IInfoObject objInfoObject2;
		String errorMsg = "";
		String cookieCrumbs;
		String strInfoSQL ="";
		String strCount = "";
		String msSQLInsertString;
		String cookiecrumbs = "";
		int parentID = 0;
		String reportKind = "";
		Calendar creationTime = Calendar.getInstance();
		try {	
			//truncate the table so that it has the latest records
			Helper.runMSSQLInsertQuery("truncate table temp_reports"); 

			ISessionMgr  objSessionMgr = CrystalEnterprise.getSessionMgr();
			objEnterpriseSession = objSessionMgr.logon(login, pswd, EnterpriseEnv, auth); 

			//A way to tell if the function even started in case no error is caught.
			//Helper.runMSSQLInsertQuery("insert into STG1_BO4_TRACKER_ERRORS (ERROR_MESSAGE, LOAD_DT) VALUES ('load_report_path_snapshot logged onto  "  + EnterpriseEnv + "', GETDATE())");
			Helper.runMSSQLInsertQuery("insert into STG1_BO4_TRACKER_ERRORS (ERROR_MESSAGE) VALUES ('load_report_path_snapshot logged onto  "  + EnterpriseEnv + "')");
			//Emailer.sendEmail("bishopp@lexmark.com", "bishopp@lexmark.com", "load_report_path_snapshot - " + EnterpriseEnv," logon successful " , null);

			IInfoStore objInfoStore = (IInfoStore) objEnterpriseSession.getService("InfoStore");
			strInfoSQL = "SELECT count(SI_ID) from CI_INFOOBJECTS WHERE SI_KIND in ('CrystalReport', 'Webi', 'DataDiscovery','XL.XcelsiusEnterprise','MDAnalysis') and SI_INSTANCE = 0";

			System.out.println(strInfoSQL);
			colInfoObjects = (IInfoObjects)objInfoStore.query(strInfoSQL);
			System.out.println("Number of rows:  " + colInfoObjects.getResultSize());
			objInfoObject = (IInfoObject)colInfoObjects.get(0);
			strCount = ((IProperties)objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").getValue()).getProperty("SI_ID").getValue().toString();

			System.out.println("Record count is " + strCount);
			//strInfoSQL = "SELECT TOP " + strCount + " * FROM CI_INFOOBJECTS WHERE SI_KIND in ('CrystalReport', 'Webi') and SI_INSTANCE = 0";
			strInfoSQL = "SELECT TOP " + strCount + " SI_ID, CUID, SI_PARENTID, SI_NAME, SI_KIND, SI_CREATION_TIME FROM CI_INFOOBJECTS WHERE SI_KIND in ('CrystalReport', 'Webi', 'DataDiscovery','XL.XcelsiusEnterprise','MDAnalysis') and SI_INSTANCE = 0";


			colInfoObjects2 = (IInfoObjects)objInfoStore.query(strInfoSQL);

			if (colInfoObjects2.size() > 0)  {
				int rowcount = colInfoObjects2.size();

				System.out.println("We do have objects in collection");
				for (int k =0; k < rowcount; k++)
				{
					objInfoObject2 = (IInfoObject) colInfoObjects2.get(k);
					parentID = objInfoObject2.getParentID();
					//cookiecrumbs = Helper.BuildCookieTrail(objEnterpriseSession, parentID, ""); //not sure this will work
					Helper crumbs = new Helper();
					cookiecrumbs = crumbs.getFolderPath1(objEnterpriseSession, parentID, "");

					System.out.println("Cookiecrumb trail is " + cookiecrumbs);
					System.out.println("Cookiecrumb length is " + cookiecrumbs.length());

					if (cookiecrumbs.length() > 0) {
						//'If the cookiecrumbs ends up null, then it was an Inbox document,
						//'which I don't want.
						System.out.println("cookiecrumbs length > 0");

						reportKind = objInfoObject2.getKind();

						/*  if (reportKind.equals("CrystalReport")) {
                      //msSQLInsertString = "INSERT INTO " + destTblNm + " (SI_ID, SI_PATH, SI_FILE1,  REPORT_NM, REPORT_FILE_PATH, CUID, DATA_SOURCE_NM, DATA_SOURCE2_NM) VALUES (" + objInfoObject2.getID() + ", '" + safeSQL(objInfoObject2.Properties.Item("SI_FILES").Properties.Item("SI_PATH").Value) + "', '" + safeSQL(objInfoObject2.Properties.Item("SI_FILES").Properties.Item("SI_FILE1").Value) + "', '" + safeSQL(objInfoObject2.getTitle()) + "', '" + safeSQL(cookiecrumbs) + "','" + objInfoObject2.CUID + "', '" + server1 + "','" + server2 + "')";
				    	  msSQLInsertString = "INSERT INTO " + destTblNm + " (SI_ID,  REPORT_NM, REPORT_FILE_PATH, CUID) VALUES (" + objInfoObject2.getID() + ", '" + Helper.safeSQL(objInfoObject2.getTitle()) + "', '" + Helper.safeSQL(cookiecrumbs) + "','" + objInfoObject2.getCUID() + "')";
				      } else {
				    	  msSQLInsertString = "INSERT INTO " + destTblNm + " (SI_ID,  REPORT_NM, REPORT_FILE_PATH, CUID) VALUES (" + objInfoObject2.getID() + ", '" + Helper.safeSQL(objInfoObject2.getTitle()) + "', '" + Helper.safeSQL(cookiecrumbs) + "','" + objInfoObject2.getCUID() + "')";

				      }*/

						try {
							IProperties properties = objInfoObject2.properties();
							IProperty creationTimeProperty = properties.getProperty(CePropertyID.SI_CREATION_TIME);
							//String value = (String) creationTimeProperty.getValue();
							creationTime.setTime ((Date) creationTimeProperty.getValue());  //This works
							System.out.println("creation time is " + creationTime.getTime());	
						}
						catch (Exception ex){
							System.out.println(ex.getMessage() + " " + ex.getStackTrace());
						}

						try {
							msSQLInsertString = "INSERT INTO " + destTblNm + " (SI_ID,  REPORT_NM, REPORT_FILE_PATH, CUID, SI_KIND, SI_CREATION_TIME) VALUES (" + objInfoObject2.getID() + ", '" + Helper.safeSQL(objInfoObject2.getTitle()) + "', '" + Helper.safeSQL(cookiecrumbs) + "','" + objInfoObject2.getCUID() + "','" + objInfoObject2.getKind() + "','" + Helper.msSQLDateTime(creationTime) +"')";
							Helper.runMSSQLInsertQuery(msSQLInsertString);	
						}
						catch (Exception ex){
							System.out.println(ex.getMessage() + " " + ex.getStackTrace());
						}




					}
				}
				objSessionMgr = null;
				//objEnterpriseSession = Nothing;
				objInfoStore = null;
				colInfoObjects = null;
				objInfoObject = null;
				colInfoObjects2 = null;
				objInfoObject2 = null;

			}
			// Clean up the Enterprise Session.
			if(objEnterpriseSession != null) {
				try {
					objEnterpriseSession.logoff();

				} catch(Exception e_ignore_in_cleanup) {}
			}
		}
		catch (Exception ex){

		}
	}

	public static void load_stg1_bo_groups (String EnterpriseEnv,String login, String pswd, 
			String auth, String destTblNm){

		/*	//A way to tell if the function even started in case no error is caught.
		Helper.runMSSQLInsertQuery("insert into STG1_BO4_TRACKER_ERRORS (ERROR_MESSAGE, LOAD_DT) VALUES ('load_stg1_bo_groups started  "  + EnterpriseEnv + "', GETDATE())");
		try {
			System.out.println("load_stg1_bo_groups: attempting to send email... ");
			Emailer.sendEmail("bishopp@lexmark.com", "bishopp@lexmark.com", "load_stg1_bo_groups - " + EnterpriseEnv," started " , null);
		}
		catch (Exception ex){
			System.out.println("Problem sending email line 8110 " + ex.getMessage() + " " + ex.getStackTrace());
			Helper.runMSSQLInsertQuery("insert into STG1_BO4_TRACKER_ERRORS (ERROR_MESSAGE, LOAD_DT) VALUES ('Problem sending email line 8110 " + ex.getMessage() + " " + ex.getStackTrace() + ", "  + EnterpriseEnv + "', GETDATE())");
		}*/


		String strInfoSQL ;
		//Logon;
		//ISessionMgr objSessionMgr = new CrystalEnterprise.getSessionMgr(); //should be New()


		IEnterpriseSession objEnterpriseSession;
		//IInfoStore objInfoStore;
		IInfoObjects colInfoObjects;
		IInfoObject objInfoObject;
		IInfoObjects colInfoObjects2;   
		IInfoObject objInfoObject2;
		//int count = 1000000;
		String strCount = "0";
		Integer maxID;
		Integer count;
		String msSQLInsertString;        
		Calendar defaultDate = Calendar.getInstance();
		defaultDate.set(1980,1,1,0,0,0);
		String lastlogontime  = Helper.oracleDate(defaultDate);
		Calendar calLastLogonTime;
		Date dtLastLogonTime;
		Calendar maxCreationDate= Calendar.getInstance();
		String maxid;
		String groupname = "";
		IProperty aggCount;
		int colCount;
		Calendar creationTime = Calendar.getInstance();
		Calendar updateTime = Calendar.getInstance();
		String SI_OWNER = "";
		String SI_NAME = "";
		String updateTS = "";
		//String createDate = "";
		Calendar createDate = Calendar.getInstance();
		String parentCuid = "";


		try
		{
			ISessionMgr  objSessionMgr = CrystalEnterprise.getSessionMgr();
			objEnterpriseSession = objSessionMgr.logon(login, pswd, EnterpriseEnv, auth); 

			//A way to tell if the function even started in case no error is caught.
			Helper.runMSSQLInsertQuery("insert into STG1_BO4_TRACKER_ERRORS (ERROR_MESSAGE, LOAD_DT) VALUES ('load_stg1_bo_groups logged onto  "  + EnterpriseEnv + "', GETDATE())");
			//Emailer.sendEmail("bishopp@lexmark.com", "bishopp@lexmark.com", "load_stg1_bo_groups - " + EnterpriseEnv," logon successful " , null);


			IInfoStore objInfoStore = (IInfoStore) objEnterpriseSession.getService("InfoStore");

			//Since I learned that si_id can be reused by CMS,I started using dates to do incrementals (03/30/2007)

			//System.out.println("select max(si_creation_time) from " + destTblNm);
			//maxCreationDate = Helper.getMaxLoadDt("select max(si_creation_time) maxLoadDate from " + destTblNm);
			//System.out.println("Max creation date is " + maxCreationDate);
			//maxid = Helper.getMaxID("select max(si_id) maxID from " + destTblNm);
			//Hard-code this so you get a full load everyday
			maxid = "0";
			if (maxid == "0")  {
				//'strInfoSQL = "SELECT count(SI_ID) from CI_INFOOBJECTS WHERE SI_PROGID= 'CrystalEnterprise.Report' and SI_INSTANCE = 0"
				strInfoSQL = "SELECT count(SI_ID) from CI_SYSTEMOBJECTS WHERE SI_KIND= 'UserGroup'";
			}
			else
				//'strInfoSQL = "SELECT count(SI_ID) from CI_INFOOBJECTS WHERE SI_PROGID= 'CrystalEnterprise.Report' and SI_INSTANCE = 0 and si_creation_time > '" + formatDateBO(maxCreationDate) + "'"
				//'strInfoSQL = "SELECT count(SI_ID) from CI_INFOOBJECTS WHERE SI_PROGID= 'CrystalEnterprise.Report' and SI_INSTANCE = 0 and si_creation_time >= '" + formatDateBO(maxCreationDate) + "'"  '4/18/2007
				strInfoSQL = "SELECT count(SI_ID) from CI_SYSTEMOBJECTS WHERE SI_KIND= 'UserGroup' and si_creation_time > '" + Helper.formatDateBO(maxCreationDate) + "'" ;

			//''***************PRODUCTION CODE***********UNCOMMENT AFTER TEST

			System.out.println(strInfoSQL);

			//EXAMPLE OF RETRIEVING PROPERTY BAG VALUES
			//objInfoObject.properties().getProperty("SI_UPDATE_TS");

			colInfoObjects = (IInfoObjects)objInfoStore.query(strInfoSQL);
			//objInfoObject = (IInfoObject)colInfoObjects.get(i);    
			objInfoObject = (IInfoObject)colInfoObjects.get(0);

			//The VB code used the collection here, but in Java, properties() isn't an available function, so I am trying to get this via InfoObject

			//System.out.println(objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").properties().getProperty("SI_ID"));
			System.out.println(objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").getID());//shows the ID, not the count!

			aggCount = objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT");

			//count = aggCount.getID().byteValue(); //that's not it
			strCount = ((IProperties)objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").getValue()).getProperty("SI_ID").getValue().toString();

			System.out.println("Record count is " + strCount);
			//count = (int)strCount;

			//System.out.println("Record count is " + aggCount.getValue().toString());

			//''***************PRODUCTION CODE***********UNCOMMENT AFTER TEST
			if (maxid == "0") {
				strInfoSQL = "SELECT TOP " + strCount + " SI_ID FROM CI_SYSTEMOBJECTS WHERE SI_KIND= 'UserGroup'";
			}
			else
				//'strInfoSQL = "SELECT TOP " + count + " SI_ID FROM CI_INFOOBJECTS WHERE SI_PROGID= 'CrystalEnterprise.Report' and SI_INSTANCE = 0 and SI_ID>" + CInt(maxid)
				//'strInfoSQL = "SELECT TOP " + count + " SI_ID FROM CI_INFOOBJECTS WHERE SI_PROGID= 'CrystalEnterprise.Report' and SI_INSTANCE = 0 and SI_CREATION_TIME > '" + formatDateBO(maxCreationDate) + "'"
				strInfoSQL = "SELECT TOP " + strCount + " SI_ID FROM CI_SYSTEMOBJECTS WHERE  SI_KIND= 'UserGroup' and SI_CREATION_TIME > '" + Helper.formatDateBO(maxCreationDate) + "'";

			//''***************PRODUCTION CODE***********UNCOMMENT AFTER TEST
			System.out.println("line 1595 " + strInfoSQL);

			colInfoObjects = (IInfoObjects)objInfoStore.query(strInfoSQL);
			Helper.runMSSQLInsertQuery("insert into STG1_BO4_TRACKER_ERRORS (ERROR_MESSAGE, LOAD_DT) VALUES ('load_stg1_bo_groups line 8222  "  + EnterpriseEnv + "', GETDATE())");
			if (colInfoObjects.size() > 0)  {
				System.out.println("We do have objects in collection");		

				/*Connection con=null;
				Class.forName("oracle.jdbc.driver.OracleDriver");
				System.out.println("oracle driver loaded in this function");
				con=DriverManager.getConnection("jdbc:oracle:thin:@dlxkrbodb1.lex.lexmark.com:1750:edr1den1","botrack","********");
				Statement s=con.createStatement();*/


				//'System.out.println("creation time  " + objInfoObject2.Properties.Item("SI_CREATION_TIME").getValue)

				//oReportAppFactory = (IReportAppFactory)objEnterpriseSession.getService("", "RASReportService");
				System.out.println("collection size is " + colInfoObjects.size());
				colCount = colInfoObjects.size();
				for (int k =0; k < colCount; k++)
				{
					groupname = "";
					//lastlogontime.set(1980,1,1,0,0,0);
					defaultDate.set(1980,1,1,0,0,0);
					lastlogontime  = Helper.oracleDate(defaultDate);

					System.out.println("iterator, k, is now " + k);
					objInfoObject = (IInfoObject) colInfoObjects.get(k);

					//'throws an error when I let it just rip through the query result.
					//strInfoSQL = "Select * From CI_APPOBJECTS Where SI_INSTANCE=0 and SI_ID =" + objInfoObject.getID();
					try {

						//strInfoSQL = "Select SI_ID, SI_OWNER, SI_UPDATE_TS, SI_CREATION_TIME,  SI_LASTLOGONTIME, SI_USERFULLNAME, SI_ALIASES, CUID From CI_SYSTEMOBJECTS Where SI_ID =" + objInfoObject.properties().getProperty(1).getValue();
						strInfoSQL = "Select SI_ID, SI_OWNER, SI_NAME, SI_UPDATE_TS, SI_CREATION_TIME,  SI_PROGID, SI_KIND, CUID, SI_PARENT_CUID, SI_LASTLOGONTIME, SI_USERFULLNAME, SI_ALIASES From CI_SYSTEMOBJECTS Where SI_ID =" + objInfoObject.getID();
						System.out.println(strInfoSQL);
						colInfoObjects2 = (IInfoObjects)objInfoStore.query(strInfoSQL);
						int colCount2 = colInfoObjects2.size();
						System.out.println("collection size is " + colCount2);
						//for (int j =0; j < colCount2; j++)
						//{

						objInfoObject2 = (IInfoObject)colInfoObjects2.get(0);	
						try 
						{
							//Got this from D:\Documents and Settings\bishopp\My Documents\BOXI\Code Examples\xi4_boejava_dg_en\html\topic15.html
							IProperties properties = objInfoObject2.properties();
							IProperty creationTimeProperty = properties.getProperty(CePropertyID.SI_CREATION_TIME);
							//String value = (String) creationTimeProperty.getValue();
							creationTime.setTime ((Date) creationTimeProperty.getValue());  //This works
							System.out.println("creation time is " + creationTime.getTime());
						}
						catch (Exception ex){
							System.out.println(ex.getMessage() + " " + ex.getStackTrace());
						}

						try
						{
							groupname = (String)objInfoObject2.properties().getProperty("SI_NAME").getValue();

						}
						catch (Exception ex){
							System.out.println(ex.getMessage() + " " + ex.getStackTrace());
						}

						try {
							SI_OWNER = Helper.safeSQL(objInfoObject2.properties().getProperty("SI_OWNER").getValue().toString());
						}
						catch (Exception ex){
							System.out.println(ex.getMessage() + " " + ex.getStackTrace());
						}

						try {
							SI_NAME = Helper.safeSQL(objInfoObject2.getTitle());
						}
						catch (Exception ex){
							System.out.println(ex.getMessage() + " " + ex.getStackTrace());
						} 

						try {
							//updateTS = Helper.oracleDate((Calendar)objInfoObject2.properties().getProperty("SI_UPDATE_TS").getValue().toString());
							//updateTS = Helper.oracleDate((Calendar)objInfoObject2.properties().getProperty("SI_UPDATE_TS").getValue());
							updateTime.setTime((Date) objInfoObject2.properties().getProperty("SI_UPDATE_TS").getValue());
						}
						catch (Exception ex){
							System.out.println(ex.getMessage() + " " + ex.getStackTrace());
						} 

						try {
							//createDate = Helper.oracleDate((Calendar)objInfoObject2.properties().getProperty("SI_CREATION_TIME").getValue());
							creationTime.setTime((Date) objInfoObject2.properties().getProperty("SI_CREATION_TIME").getValue());
						}
						catch (Exception ex){
							System.out.println(ex.getMessage() + " " + ex.getStackTrace());
						} 

						try {
							parentCuid = objInfoObject2.properties().getProperty("SI_PARENT_CUID").getValue().toString();
						}
						catch (Exception ex){
							System.out.println(ex.getMessage() + " " + ex.getStackTrace());
						} 

						try
						{
							//Helper.runMSSQLInsertQuery("insert into STG1_BO4_TRACKER_ERRORS (ERROR_MESSAGE, LOAD_DT) VALUES ('load_stg1_bo_groups line 8324  "  + EnterpriseEnv + "', GETDATE())");
							//msSQLInsertString = "INSERT INTO " + destTblNm + " (SI_ID, SI_OWNER, SI_NAME, SI_UPDATE_TS, SI_CREATION_TIME,  SI_KIND, CUID, SI_PARENT_CUID) VALUES (" + objInfoObject2.getID() + ", '" + SI_OWNER + "','" + SI_NAME + "', ('" + Helper.msSQLDateTime(updateTime) + "'), ('" + Helper.msSQLDateTime(creationTime) + "'), '" + objInfoObject2.getKind() + "', '" + objInfoObject2.getCUID() + "','" + parentCuid + "')";
							msSQLInsertString = "INSERT INTO " + destTblNm + " (SI_ID, SI_OWNER, SI_NAME, SI_UPDATE_TS, SI_CREATION_TIME,  SI_KIND, CUID, SI_PARENT_CUID) VALUES (" + objInfoObject2.getID() + ", '" + SI_OWNER + "','" + SI_NAME + "', ('" + Helper.msSQLDateTime(updateTime) + "'), ('" + Helper.msSQLDateTime(creationTime) + "'), '" + objInfoObject2.getKind() + "', '" + objInfoObject2.getCUID() + "','" + parentCuid + "')";
							System.out.println(msSQLInsertString);
							Helper.runMSSQLInsertQuery(msSQLInsertString);

							System.out.println(destTblNm);
							//For each group, load the user names into the stage1 user_group_rel table
							if (destTblNm.equals("STG1_BO4P_GROUPS")){
								load_stg1_bo_user_group_rel(objEnterpriseSession,objInfoObject2.getID(),"stg1_bo4p_user_group_rel");
							}
							else if (destTblNm.equals("STG1_BO4Q_GROUPS")){
								load_stg1_bo_user_group_rel(objEnterpriseSession,objInfoObject2.getID(),"stg1_bo4q_user_group_rel");
							}
							else if (destTblNm.equals("STG1_BO4D_GROUPS")){
								load_stg1_bo_user_group_rel(objEnterpriseSession,objInfoObject2.getID(),"stg1_bo4d_user_group_rel");
							}
							else if (destTblNm.equals("STG1_BO4V_GROUPS")){
								load_stg1_bo_user_group_rel(objEnterpriseSession,objInfoObject2.getID(),"stg1_bo4v_user_group_rel");
							}
						}
						catch (Exception ex){
							System.out.println(ex.getMessage() + " " + ex.getStackTrace());
						}

						//}

					}
					catch (Exception ex){
						System.out.println(ex.getMessage() + " " + ex.getStackTrace());
						ex.printStackTrace(System.out);
						Helper.runMSSQLInsertQuery("insert into STG1_BO4_TRACKER_ERRORS (ERROR_MESSAGE, LOAD_DT) VALUES ('" + ex.getMessage() + " " + ex.getStackTrace() + " " + EnterpriseEnv + "', GETDATE())");
						//'sendmail("Logon error", ex.getMessage() + " " + ex.getStackTrace)
						//Helper.sendmail("bishopp@lexmark.com", "bishopp@lexmark.com, amarabes@lexmark.com,ajpatel@lexmark.com,msankar@lexmark.com,ajain@lexmark.com", "Logon error - load_stg1_migr_XI_reports(String, String, String, String, String) - " + EnterpriseEnv, ex.getMessage() + " " + ex.getStackTrace());
						Emailer.sendEmail("bishopp@lexmark.com", "bishopp@lexmark.com", "Logon error - load_stg1_boxi_users(String, String, String, String, String) - " + EnterpriseEnv, ex.getMessage() + " " + ex.getStackTrace(), null);

					}


				}
				objSessionMgr = null;
				//objEnterpriseSession = Nothing;
				objInfoStore = null;
				colInfoObjects = null;
				objInfoObject = null;
				colInfoObjects2 = null;
				objInfoObject2 = null;

			}
			// Clean up the Enterprise Session.
			if(objEnterpriseSession != null) {
				try {
					objEnterpriseSession.logoff();

				} catch(Exception e_ignore_in_cleanup) {}
			}
		}
		catch (Exception ex)
		{
			System.out.println(ex.getMessage() + " " + ex.getStackTrace());
			ex.printStackTrace(System.out);
			Helper.runMSSQLInsertQuery("insert into STG1_BO4_TRACKER_ERRORS (ERROR_MESSAGE, LOAD_DT) VALUES ('" + ex.getMessage() + " " + ex.getStackTrace() + " " + EnterpriseEnv + "', GETDATE())");
			//'sendmail("Logon error", ex.getMessage() + " " + ex.getStackTrace)
			//Helper.sendmail("bishopp@lexmark.com", "bishopp@lexmark.com, amarabes@lexmark.com,ajpatel@lexmark.com,msankar@lexmark.com,ajain@lexmark.com", "Logon error - load_stg1_migr_XI_reports(String, String, String, String, String) - " + EnterpriseEnv, ex.getMessage() + " " + ex.getStackTrace());
			Emailer.sendEmail("bishopp@lexmark.com", "bishopp@lexmark.com", "Logon error - load_stg1_boxi_users(String, String, String, String, String) - " + EnterpriseEnv, ex.getMessage() + " " + ex.getStackTrace(), null);
		}			
	}

	public static void load_stg1_bo_user_group_rel(IEnterpriseSession objEnterpriseSession , int groupID, String destTblNm) {
		//Pre-condition:  the table STG1_BO4x_user_group_rel lacks the user_group_rel records for a new group (all users per group)
		//Post-condition: the table STG1_BO4x_user_group_rel has the user_group_rel records for a new group (all users per group)

		IInfoStore objInfoStore;
		IInfoObjects colInfoObjects;
		IInfoObjects rsGroups;
		IInfoObjects colUsers;
		//IInfoObject Group;
		IInfoObject objInfoObject;
		IInfoObject objUserObject;
		IInfoObjects result;
		Integer numUsers[];
		String msSQLInsertString ="";
		String strSQL;
		IProperties GroupProps;
		IProperty numUsersProp;
		IUserGroup Group;

		//			' Get the information for the group membership with the ID provided.

		try {

			//'objEnterpriseService = objEnterpriseSession.GetService("InfoStore")
			//objInfoStore = objEnterpriseSession.Service("", "InfoStore")
			objInfoStore = (IInfoStore) objEnterpriseSession.getService("InfoStore");
			strSQL = "SELECT top 1 * FROM CI_SYSTEMOBJECTS WHERE SI_KIND='UserGroup' and SI_ID ='" + groupID + "'";
			System.out.println(strSQL);
			rsGroups = objInfoStore.query("SELECT top 1 * FROM CI_SYSTEMOBJECTS WHERE SI_KIND='UserGroup' and SI_ID ='" + groupID + "'");

			if (rsGroups.size() == 0) {
				System.out.println("No members for group " + groupID);
			}
			else
				Group = (IUserGroup)rsGroups.get(0);

			try {
				Group = (IUserGroup) rsGroups.get(0);

				numUsers = (Integer[])Group.getUsers().toArray(new Integer[0]);
				GroupProps = Group.properties();

				Integer users[] = (Integer[])Group.getUsers().toArray(new Integer[0]); 
				IUser user; 
				for (int j=0; j<users.length; j++) { 
					result = objInfoStore.query("Select TOP 1 SI_NAME From CI_SYSTEMOBJECTS Where SI_PROGID='CrystalEnterprise.User' AND SI_ID="+users[j]); 
					user = (IUser) result.get(0); 
					System.out.println("user is " + user.getTitle());
					System.out.println("user id is " + user.getID()); //This is the user si_id, which is what the table expects
					//msSQLInsertString = "insert into " + destTblNm + " " + Group.getID() + ",'" + user.getTitle() + "', GETDATE()";
					msSQLInsertString = "insert into " + destTblNm + " values(" + Group.getID() + ",'" + user.getID() + "', GETDATE())"; 

					System.out.println(msSQLInsertString);
					Helper.runMSSQLInsertQuery(msSQLInsertString);
				} 


				/*for (int i = 0; i< result.size(); i++) {
					//msSQLInsertString = "insert into " + destTblNm + " select USER_GROUP_REL_SEQ.NEXTVAL, " + Group.getID() + ",'" + Group.properties().getProperty("SI_GROUP_MEMBERS").Properties.Item(i + 1).getValue + "', GETDATE()";
					msSQLInsertString = "insert into " + destTblNm + " select USER_GROUP_REL_SEQ.NEXTVAL, " + Group.getID() + ",'" + user + "', GETDATE()";
//					'The reason you have to add a 1 to the iterator is because the first record being returned was the value of SI_TOTAL, which is wrong
//					'Then the next value was the first user si_id
//					'And then of course the last user si_id was skipped.
//					'so i+1 causes the code to start at the first user si_id and finish all of them.  See notes form 6/4/2008.
					System.out.println(msSQLInsertString);
					//s.execute(msSQLInsertString);
					Helper.runMSSQLInsertQuery(msSQLInsertString);
				}*/


				objInfoStore = null;
				colInfoObjects = null;
				objInfoObject = null;
				rsGroups = null;
				Group = null;
			}
			catch (Exception ex) {
				System.out.println(ex.getMessage() + " " + ex.getStackTrace());
				Helper.runMSSQLInsertQuery("insert into STG1_BO4_TRACKER_ERRORS (ERROR_MESSAGE, LOAD_DT) VALUES ('" + ex.getMessage() + " " + ex.getStackTrace() + " " + msSQLInsertString + "', GETDATE())");
			}
			//Oct 29, 2013:  Do NOT do this since it logs off the calling function.
			//// Clean up the Enterprise Session.
			//if(objEnterpriseSession != null) {
			//	try {
			//		objEnterpriseSession.logoff();

			//	} catch(Exception e_ignore_in_cleanup) {}
			//}
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage() + " " + ex.getStackTrace());
			Helper.runMSSQLInsertQuery("insert into STG1_BO4_TRACKER_ERRORS (ERROR_MESSAGE, LOAD_DT) VALUES ('" + ex.getMessage() + " " + ex.getStackTrace() + " " + msSQLInsertString + "', GETDATE())");
			//				sendmail("Error", ex.Message + " " + ex.StackTrace)
			//sendmail("reporting@lexmark.com", "bishopp@lexmark.com", "Error - load_stg1_migr_XI_user_group_rel - ", ex.getMessage() + " " + ex.getStackTrace() + " " + strSQL + " " + destTblNm)

		}

	}

	public static void load_stg1_foldersec_rel(String EnterpriseEnv, String login, String pswd,String auth, String destTblNm) {
		//Pre-condition:  a current snapshot of report cuid's and their paths is not available
		//Post-condition:  a current snapshot of report cuid's and their paths is available

		IEnterpriseSession objEnterpriseSession;
		//IInfoStore objInfoStore;
		IInfoObjects colInfoObjects;
		IInfoObjects colInfoObjects2;
		IInfoObject objInfoObject;
		IInfoObject objInfoObject2;
		IInfoObject folder;
		String errorMsg = "";
		String cookieCrumbs;
		String strInfoSQL ="";
		String strCount = "";
		String msSQLInsertString;
		String cookiecrumbs = "";
		int parentID = 0;
		String userType;
		ISecurityInfo2 securityInfo;
		IEffectiveRights knownRights;	  
		//IObjectPrincipal UserName; deprecated
		//IObjectPrincipals FolderUsers; deprecated

		IExplicitPrincipal UserName;
		IExplicitPrincipal FolderUser;
		IExplicitPrincipals FolderUsers;


		String rights = "unknown";
		String inheritedRights = "unknown";
		String AccessRight = "Has access";
		int FolderUsersCount;

		try {	

			ISessionMgr  objSessionMgr = CrystalEnterprise.getSessionMgr();
			objEnterpriseSession = objSessionMgr.logon(login, pswd, EnterpriseEnv, auth); 

			//A way to tell if the function even started in case no error is caught.
			//Helper.runMSSQLInsertQuery("insert into STG1_BO4_TRACKER_ERRORS (ERROR_MESSAGE, LOAD_DT) VALUES ('load_report_path_snapshot logged onto  "  + EnterpriseEnv + "', GETDATE())");
			//Emailer.sendEmail("bishopp@lexmark.com", "bishopp@lexmark.com", "load_report_path_snapshot - " + EnterpriseEnv," logon successful " , null);

			IInfoStore objInfoStore = (IInfoStore) objEnterpriseSession.getService("InfoStore");
			strInfoSQL = "SELECT count(SI_ID) from CI_INFOOBJECTS WHERE SI_KIND = 'Folder'";

			System.out.println(strInfoSQL);
			colInfoObjects = (IInfoObjects)objInfoStore.query(strInfoSQL);
			System.out.println("Number of rows:  " + colInfoObjects.getResultSize());
			objInfoObject = (IInfoObject)colInfoObjects.get(0);
			strCount = ((IProperties)objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").getValue()).getProperty("SI_ID").getValue().toString();

			System.out.println("Record count is " + strCount);
			//strInfoSQL = "SELECT TOP " + strCount + " * FROM CI_INFOOBJECTS WHERE SI_KIND in ('CrystalReport', 'Webi') and SI_INSTANCE = 0";
			strInfoSQL = "SELECT TOP " + strCount + " * FROM CI_INFOOBJECTS WHERE SI_KIND in ('Folder')";


			colInfoObjects2 = (IInfoObjects)objInfoStore.query(strInfoSQL);

			if (colInfoObjects2.size() > 0)  {
				int rowcount = colInfoObjects2.size();

				System.out.println("We do have objects in collection");
				for (int k =0; k < rowcount; k++)
				{
					try {
						folder = (IInfoObject) colInfoObjects2.get(k);
						System.out.println(folder.getID());
						//System.out.println(folder.getSecurityInfo2().getEffectivePrincipals().get(0).getName()); causes error (It's null)
						//System.out.println(folder.getSecurityInfo2().getEffectivePrincipals().get(0).getID()); causes error (It's null)
						Helper crumbs = new Helper();
						cookiecrumbs = crumbs.getFolderPath1(objEnterpriseSession, folder.getID(), "");


						System.out.println("Cookiecrumb trail is " + cookiecrumbs);
						System.out.println("Cookiecrumb length is " + cookiecrumbs.length());

						try {
							//Iterate through the object principles, each group that been added to this folder
							//with some sort of rights set, should appear here.
							//Iterator i = folder.getSecurityInfo().getObjectPrincipals().iterator(); //deprecated code
							Iterator i = folder.getSecurityInfo2().getEffectivePrincipals().iterator();
							while(i.hasNext()) 
							{

								//IObjectPrincipal objPri = (IObjectPrincipal)i.next();
								IEffectivePrincipal objPri = (IEffectivePrincipal)i.next();
								//System.out.println("   Name: " + objPri.getName() + ", Role: " +  objPri.getRole() + "<br>");
								//System.out.println("   Name: " + objPri.getName() + ", Role: " +  objPri.getRoles().get(0) +  ", Rights " + objPri.getRights().toString() + " inherit folders " + objPri.isInheritFolders() + " inherit groups " + objPri.isInheritGroups()); //objPri.getName() lists the group name
								//objPri.getRoles() is usually blank and if it isn't it's something like: com.crystaldecisions.sdk.occa.infostore.internal.EffectiveRole@bd5df
								IExplicitPrincipals explicitPrincipals = folder.getSecurityInfo2().newExplicitPrincipals();
								//IExplicitPrincipal explicitPrincipal = explicitPrincipals.add(user.getID());
								IExplicitPrincipal explicitPrincipal = explicitPrincipals.get(0);
								//String rightName = explicitPrincipal.getName();
								//System.out.println("Right name " + rightName);

								ISecurityInfo2 boSecurityInfo2 = folder.getSecurityInfo2();
								IExplicitPrincipals  boExplicitPrincipals = boSecurityInfo2.getExplicitPrincipals();
								IExplicitPrincipal    boExplicitPrincipal = boExplicitPrincipals.get(folder.getID());
								//The following are always null and throw exceptions
								//System.out.println( "Inherit from parent folder? " + boExplicitPrincipal.isInheritFolders());
								//System.out.println( "Inherit from parent group? " + boExplicitPrincipal.isInheritGroups());

								/*//If the object principle is the group we are looking for, change the role.
								if (objPri.getName().equals("Test Group")){ 
									IRole newRole = IRole.VIEW_ON_DEMAND;
									System.out.println("<BR>" + "   * Changing the role for " + objPri.getName() + " to " + newRole + "<br>");
									//objPri.setRole(newRole);

								}*/

								if (objPri.isInheritGroups()) {
									AccessRight = "Has Access";
								}
								else
									AccessRight = "No Access";
								System.out.println("iterator k is " + k);
								//The objPri.getRights() gets all the very granular rights.  I just need to know if the group or user has access or not.
								msSQLInsertString = "INSERT INTO " + destTblNm + " ( SI_FOLDER_ID, SI_USER_ID, ACCESS_RIGHT, FOLDER_NM, FOLDER_PATH, SI_NAME, PARENT_FOLDER_ID, USER_TYPE, ENVIRONMENT) values (" + folder.getID() + " , " + objPri.getID() + ", '" +  AccessRight + "', '" + Helper.safeSQL(folder.getTitle()) + "','" + Helper.safeSQL(cookiecrumbs) + "','" + objPri.getName() + "'," + folder.getParentID() + ",'" + "Group" + "','" + EnterpriseEnv + "')";
								System.out.println(msSQLInsertString);
								Helper.runMSSQLInsertQuery(msSQLInsertString);

							}

						}
						catch (Exception Ex){
							System.out.println(Ex.getMessage() + " " + Ex.getStackTrace());

						}


						//Might not need this part=============:
						try {
							//FolderUsers =folder.SecurityInfo.ObjectPrincipals (VB version)

							FolderUsers = folder.getSecurityInfo2().getExplicitPrincipals();

							FolderUsersCount = FolderUsers.size();
							for (int j =0; j < FolderUsersCount; j++){
								//UserName = FolderUsers.get(j);
								FolderUser = FolderUsers.get(j);  //the folder user is either a group or a user.  Most likely a group.
								//System.out.println("Folder user name is " + FolderUser.getName());
								userType = getUserType(objEnterpriseSession, FolderUser.getID());


								//msSQLInsertString = "INSERT INTO " + destTblNm + " ( SI_FOLDER_ID, SI_USER_ID, ACCESS_RIGHT, FOLDER_NM, FOLDER_PATH, SI_NAME, PARENT_FOLDER_ID, USER_TYPE, ENVIRONMENT) values (" + folder.getID() + " , " + UserName.getID() + ", '" + getRoleName(UserName.getRoles().get(0), AccessRight) + "', '" + Helper.safeSQL(folder.getTitle()) + "','" + Helper.safeSQL(cookiecrumbs) + "','" + UserName.getName() + "'," + folder.getParentID() + ",'" + userType + "','" + EnterpriseEnv + "')";
								//	msSQLInsertString = "INSERT INTO " + destTblNm + " ( SI_FOLDER_ID, SI_USER_ID, ACCESS_RIGHT, FOLDER_NM, FOLDER_PATH, SI_NAME, PARENT_FOLDER_ID, USER_TYPE, ENVIRONMENT) values (" + folder.getID() + " , " + UserName.getID() + ", '" +  UserName.getRights() + "', '" + Helper.safeSQL(UserName.getName()) + "','" + Helper.safeSQL(cookiecrumbs) + "','" + UserName.getName() + "'," + folder.getParentID() + ",'" + userType + "','" + EnterpriseEnv + "')";
								msSQLInsertString = "INSERT INTO " + destTblNm + " ( SI_FOLDER_ID, SI_USER_ID, ACCESS_RIGHT, FOLDER_NM, FOLDER_PATH, SI_NAME, PARENT_FOLDER_ID, USER_TYPE, ENVIRONMENT) values (" + folder.getID() + " , " + FolderUser.getID() + ", '" +  FolderUser.getRights() + "', '" + Helper.safeSQL(FolderUser.getName()) + "','" + Helper.safeSQL(cookiecrumbs) + "','" + FolderUser.getName() + "'," + folder.getParentID() + ",'" + userType + "','" + EnterpriseEnv + "')";
								System.out.println(msSQLInsertString);
								Helper.runMSSQLInsertQuery(msSQLInsertString);
							}

						}
						catch (Exception Ex){
							System.out.println(Ex.getMessage() + " " + Ex.getStackTrace());

						}
					}
					catch (Exception Ex){
						System.out.println(Ex.getMessage() + " " + Ex.getStackTrace());

					}

				}
				objSessionMgr = null;
				//objEnterpriseSession = Nothing;
				objInfoStore = null;
				colInfoObjects = null;
				objInfoObject = null;
				colInfoObjects2 = null;
				objInfoObject2 = null;
			}
			// Clean up the Enterprise Session.
			if(objEnterpriseSession != null) {
				try {
					objEnterpriseSession.logoff();

				} catch(Exception e_ignore_in_cleanup) {}
			}
		}
		catch (Exception ex){

		}
	}

	public static String getUserType(IEnterpriseSession objEnterpriseSession, int userid) {

		String strInfoSQL;
		IInfoStore objInfoStore;
		IInfoObjects colInfoObjects;
		IInfoObject user;
		String userKind = "";

		try {

			strInfoSQL = "select * from ci_systemobjects where si_id = " + userid;

			objInfoStore = (IInfoStore) objEnterpriseSession.getService("InfoStore");
			colInfoObjects = objInfoStore.query(strInfoSQL);
			user = (IInfoObject)colInfoObjects.get(0);

			userKind = user.getKind();

		}
		catch (Exception ex){
			System.out.println(ex.getMessage() + " " + ex.getStackTrace());
		}
		return userKind;

	}

	public static void load_stg1_data_conn(String EnterpriseEnv , String login, String pswd, String auth, String destTblNm ) {
		//Pre-condition:  new data connections are not in the table
		//Post-condition:  new data connections are in the table

		IEnterpriseSession objEnterpriseSession;
		//IInfoStore objInfoStore;
		IInfoObjects colInfoObjects;
		IInfoObject objInfoObject;
		IInfoObjects colInfoObjects2;   
		IInfoObject objInfoObject2;
		String strInfoSQL; 
		Calendar maxCreationDate = Calendar.getInstance();
		Calendar creationTime = Calendar.getInstance();
		Calendar updateTime = Calendar.getInstance();
		String maxid;
		int aggCount;
		String strCount;
		int colCount;
		String msSQLInsertString;
		String provider = "";  //only populates for SI_KIND= CommonConnection
		String server = "";    //only populates for SI_KIND= CommonConnection

		try
		{

			//Since I learned that si_id can be reused by CMS,I started using dates to do incrementals (03/30/2007)

			System.out.println("select max(si_creation_time) from " + destTblNm);
			maxCreationDate = Helper.getMaxLoadDt("select max(si_creation_time) maxLoadDate from " + destTblNm);
			System.out.println("Max creation date is " + maxCreationDate);
			maxid = Helper.getMaxID("select max(si_id) maxID from " + destTblNm);

			//maxid = "0";  //Set to zero for testing or an occasional full load

			if (maxid == "0")  {
				strInfoSQL = "SELECT count(SI_ID) from CI_APPOBJECTS WHERE SI_KIND in ('CommonConnection','MetaData.DataConnection','CCIS.DataConnection')";
			}
			else
				strInfoSQL = "SELECT count(SI_ID) from CI_APPOBJECTS WHERE SI_KIND in ('CommonConnection','MetaData.DataConnection','CCIS.DataConnection') and si_creation_time > '" + Helper.formatDateBO(maxCreationDate) + "'" ;



			System.out.println(strInfoSQL);

			//EXAMPLE OF RETRIEVING PROPERTY BAG VALUES
			//objInfoObject.properties().getProperty("SI_UPDATE_TS");

			ISessionMgr  objSessionMgr = CrystalEnterprise.getSessionMgr();
			objEnterpriseSession = objSessionMgr.logon(login, pswd, EnterpriseEnv, auth);    
			IInfoStore objInfoStore = (IInfoStore) objEnterpriseSession.getService("InfoStore");

			colInfoObjects = (IInfoObjects)objInfoStore.query(strInfoSQL);
			//objInfoObject = (IInfoObject)colInfoObjects.get(i);    
			objInfoObject = (IInfoObject)colInfoObjects.get(0);

			//The VB code used the collection here, but in Java, properties() isn't an available function, so I am trying to get this via InfoObject

			//System.out.println(objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").properties().getProperty("SI_ID"));
			//System.out.println(objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").getID());//shows the ID, not the count!

			//aggCount = objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT");

			//count = aggCount.getID().byteValue(); //that's not it
			strCount = ((IProperties)objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").getValue()).getProperty("SI_ID").getValue().toString();

			System.out.println("Record count is " + strCount);
			//count = (int)strCount;

			//System.out.println("Record count is " + aggCount.getValue().toString());

			//''***************PRODUCTION CODE***********UNCOMMENT AFTER TEST
			if (maxid == "0") {
				strInfoSQL = "SELECT TOP " + strCount + " SI_ID FROM CI_APPOBJECTS WHERE SI_KIND in ('CommonConnection','MetaData.DataConnection','CCIS.DataConnection')";
			}
			else
				//'strInfoSQL = "SELECT TOP " + count + " SI_ID FROM CI_INFOOBJECTS WHERE SI_PROGID= 'CrystalEnterprise.Report' and SI_INSTANCE = 0 and SI_ID>" + CInt(maxid)
				//'strInfoSQL = "SELECT TOP " + count + " SI_ID FROM CI_INFOOBJECTS WHERE SI_PROGID= 'CrystalEnterprise.Report' and SI_INSTANCE = 0 and SI_CREATION_TIME > '" + formatDateBO(maxCreationDate) + "'"
				strInfoSQL = "SELECT TOP " + strCount + " SI_ID FROM CI_APPOBJECTS WHERE SI_KIND in ('CommonConnection','MetaData.DataConnection','CCIS.DataConnection')  and SI_CREATION_TIME > '" + Helper.formatDateBO(maxCreationDate) + "'";

			//''***************PRODUCTION CODE***********UNCOMMENT AFTER TEST
			System.out.println("line 1698 " + strInfoSQL);

			colInfoObjects = (IInfoObjects)objInfoStore.query(strInfoSQL);

			if (colInfoObjects.size() > 0)  {
				System.out.println("We do have objects in collection");		


				//'System.out.println("creation time  " + objInfoObject2.Properties.Item("SI_CREATION_TIME").getValue)

				//oReportAppFactory = (IReportAppFactory)objEnterpriseSession.getService("", "RASReportService");
				System.out.println("collection size is " + colInfoObjects.size());
				colCount = colInfoObjects.size();
				for (int k =0; k < colCount; k++)
				{
					System.out.println("iterator, k, is now " + k);
					objInfoObject = (IInfoObject) colInfoObjects.get(k);

					//'throws an error when I let it just rip through the query result.
					strInfoSQL = "Select * From CI_APPOBJECTS Where SI_INSTANCE=0 and SI_ID =" + objInfoObject.getID();
					System.out.println(strInfoSQL);
					colInfoObjects2 = (IInfoObjects)objInfoStore.query(strInfoSQL);
					int colCount2 = colInfoObjects2.size();

					for (int j =0; j < colCount2; j++)
					{

						objInfoObject2 = (IInfoObject)colInfoObjects2.get(j);	

						System.out.println("objInfoObject2 id:  " + objInfoObject2.getID());
						/*System.out.println("server1 length is " + server1.length());
							if (server1.length() > 0) {
							}*/
						try
						{

							IProperties properties = objInfoObject2.properties();
							IProperty creationTimeProperty = properties.getProperty(CePropertyID.SI_CREATION_TIME);
							//String value = (String) creationTimeProperty.getValue();

							creationTime.setTime ((Date) creationTimeProperty.getValue());  //This works
							System.out.println("creation time is " + creationTime.getTime());
						}
						catch (Exception ex)
						{
							ex.printStackTrace();
							System.out.println(" for report id: " +objInfoObject2.getID() + " " + ex.getMessage() + " " + ex.getStackTrace());
						}


						try
						{
							updateTime.setTime((Date) objInfoObject2.properties().getProperty("SI_UPDATE_TS").getValue());
							System.out.println(updateTime);

						}

						catch (Exception ex)
						{
							ex.printStackTrace();
							System.out.println(" for report id: " +objInfoObject2.getID() + " " + ex.getMessage() + " " + ex.getStackTrace());
						}

						try
						{
							provider = objInfoObject2.properties().getProperty("SI_PROVIDER_CAPTION").getValue().toString();
						}
						catch (Exception ex)
						{
							ex.printStackTrace();
							System.out.println(" for report id: " +objInfoObject2.getID() + " " + ex.getMessage() + " " + ex.getStackTrace());
						}

						try
						{
							server = objInfoObject2.properties().getProperty("SI_SERVER").getValue().toString();
						}
						catch (Exception ex)
						{
							ex.printStackTrace();
							System.out.println(" for report id: " +objInfoObject2.getID() + " " + ex.getMessage() + " " + ex.getStackTrace());
						}

						msSQLInsertString = "INSERT INTO " + destTblNm + " (CUID, SI_KIND, SI_UPDATE_TS, SI_NAME, SI_ID, SI_PARENT_FOLDER_CUID,  SI_CREATION_TIME, SI_OWNER, SI_PARENTID,  SI_PARENT_CUID, SI_PROVIDER_CAPTION, SI_SERVER) VALUES ('" + objInfoObject2.getCUID() + "','" + objInfoObject2.getKind() + "',  ('" + Helper.msSQLDateTime(updateTime) + "')" + ", '" + Helper.safeSQL(objInfoObject2.getTitle()) + "', " + objInfoObject2.getID() + ", '" + objInfoObject2.properties().getProperty("SI_PARENT_FOLDER_CUID").getValue() +   "',  ('" + Helper.msSQLDateTime(creationTime) + "'), '" + objInfoObject2.getOwner() +"'," + objInfoObject2.getParentID() + ", '" + objInfoObject2.getParentCUID() + "','" + provider + "','" + server + "' )";
						System.out.println(msSQLInsertString);

						try
						{
							Helper.runMSSQLInsertQuery(msSQLInsertString);

						}
						catch (Exception ex)
						{
							ex.printStackTrace();
							System.out.println(" for data connection id: " + objInfoObject2.getID() + " " + ex.getMessage() + " " + ex.getStackTrace());
							//'Records the skipped si_id

							Helper.runMSSQLInsertQuery(msSQLInsertString);
						}

					}
				}
			}

			objSessionMgr = null;
			//objEnterpriseSession = Nothing;
			objInfoStore = null;
			colInfoObjects = null;
			objInfoObject = null;
			colInfoObjects2 = null;
			objInfoObject2 = null;
			// Clean up the Enterprise Session.
			if(objEnterpriseSession != null) {
				try {
					objEnterpriseSession.logoff();

				} catch(Exception e_ignore_in_cleanup) {}
			}
		}
		catch (Exception ex)
		{
			System.out.println(ex.getMessage() + " " + ex.getStackTrace() + " line 6505");
			Helper.runMSSQLInsertQuery("insert into STG1_BO4_TRACKER_ERRORS (ERROR_MESSAGE, LOAD_DT), '" + ex.getMessage() + " " + ex.getStackTrace() + " " + EnterpriseEnv + "', GETDATE())");
			//'sendmail("Logon error", ex.getMessage() + " " + ex.getStackTrace)
			//Helper.sendmail("bishopp@lexmark.com", "bishopp@lexmark.com, amarabes@lexmark.com,ajpatel@lexmark.com,msankar@lexmark.com,ajain@lexmark.com", "Logon error - load_stg1_migr_XI_reports(String, String, String, String, String) - " + EnterpriseEnv, ex.getMessage() + " " + ex.getStackTrace());
			//Helper.sendmail("bishopp@lexmark.com", "bishopp@lexmark.com", "Logon error - load_stg1_boxi_runhist(String, String, String, String, String) - " + EnterpriseEnv, ex.getMessage() + " " + ex.getStackTrace());

		}
	}


	public static void update_deleted_reports(String destTblNm) {
		//Pre-condition:  deleted records are not flagged in staging tables 
		//Must run load_temp_XI_reports_faster() first
		//Post-condition: deleted reports are flagged in staging tables 
		String IDCol = "SI_ID";


		try {
			String oraUpdateString;
			String msSQLUpdateString;
			/*String driver = "oracle.jdbc.driver.OracleDriver";
			String url = "jdbc:oracle:thin:@dlxkrbodb1.lex.lexmark.com:1750:edr1den1";
			String username = "botrack";
			String password = "********";


			Connection con=null;
			Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println("oracle driver loaded in load_stg1_Webi_Reports()");
			con=DriverManager.getConnection("jdbc:oracle:thin:@dlxkrbodb1.lex.lexmark.com:1750:edr1den1","botrack","********");
			Statement s=con.createStatement();*/

			destTblNm = destTblNm.toUpperCase();
			if (destTblNm.contains("RECURRINGJOBS") | destTblNm.contains("RUNHIST")) {
				IDCol = "INSTANCE_ID";
			}

			//oraUpdateString = "UPDATE " + destTblNm + " t1 ";
			//oraUpdateString += " set update_dt = sysdate, delete_flg ='Y' where t1.si_id in (select t1.si_id from " + destTblNm + " t1, botrack.temp_reports t2 where t1.si_id = t2.si_id (+) and t2.si_id is null)";

			msSQLUpdateString = "UPDATE " + destTblNm;
			//msSQLUpdateString += " set update_dt=GETDATE(), delete_flg='Y' where si_id in (select a.si_id from " + destTblNm + " a left outer join TEMP_REPORTS b on a.cuid=b.cuid where b.cuid is null)";
			msSQLUpdateString += " set delete_flg_dt=GETDATE(), delete_flg='Y' where " + IDCol + " in (select a." + IDCol + " from " + destTblNm + " a left outer join TEMP_OBJECTS b on a.cuid=b.cuid where b.cuid is null and a.delete_flg_dt is null)";

			System.out.println(msSQLUpdateString);

			try {
				//oraUpdater.ExecuteNonQuery()
				Helper.runMSSQLInsertQuery(msSQLUpdateString);
			}
			catch (Exception ex) {
				System.out.println(ex.getMessage() + " " + ex.getStackTrace());
			}

			//oraUpdater.Dispose()
			//devConn.Dispose()
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage() + " " + ex.getStackTrace());
		}

	}//End Sub

	public static void update_report_table( String destTblNm, String EnterpriseEnv ) {
		//'Precondition:  the report paths are out-dated
		//'Postcondition:  the report paths  are up-to-date; also updates the stage 3 tables.

		//'Dim strSQL As String
		String oraUpdateString;
		String msSQLUpdateString;
		int maxID;
		String colNm1;
		String colNm2;
		String colNm3;
		String colNm4;
		String colNm5;
		String colNm6;
		String colNm7;
		String colNm8;
		String colNm9;
		String colNm10;
	

		///////////////////////// TEST ///////////////////////////
		//Prod - Business Views
		/*msSQLUpdateString = "UPDATE RPT_BO4_BUS_VIEWS ";
		msSQLUpdateString += " set delete_flg_dt=GETDATE(), delete_flg='Y' where si_id in (select a.si_id from RPT_BO4_BUS_VIEWS a left outer join STG1_BO4P_BUS_VIEWS b on a.cuid=b.cuid where b.cuid is null and a.DELETE_FLG_DT is null)";
		try {
			Helper.runMSSQLInsertQuery(msSQLUpdateString);
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage() + " " + ex.getStackTrace());
		}

		//QA - Business Views
		msSQLUpdateString = "UPDATE RPT_BO4_BUS_VIEWS ";
		msSQLUpdateString += " set delete_flg_dt=GETDATE(), delete_flg='Y' where si_id in (select a.si_id from RPT_BO4_BUS_VIEWS a left outer join STG1_BO4Q_BUS_VIEWS b on a.cuid=b.cuid where b.cuid is null and a.DELETE_FLG_DT is null)";
		try {
			Helper.runMSSQLInsertQuery(msSQLUpdateString);
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage() + " " + ex.getStackTrace());
		}


		//Dev - Business Views
		msSQLUpdateString = "UPDATE RPT_BO4_BUS_VIEWS ";
		msSQLUpdateString += " set delete_flg_dt=GETDATE(), delete_flg='Y' where si_id in (select a.si_id from RPT_BO4_BUS_VIEWS a left outer join STG1_BO4D_BUS_VIEWS b on a.cuid=b.cuid where b.cuid is null and a.DELETE_FLG_DT is null)";
		try {
			Helper.runMSSQLInsertQuery(msSQLUpdateString);
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage() + " " + ex.getStackTrace());
		}


		//Val - Business Views
		msSQLUpdateString = "UPDATE RPT_BO4_BUS_VIEWS ";
		msSQLUpdateString += " set delete_flg_dt=GETDATE(), delete_flg='Y' where si_id in (select a.si_id from RPT_BO4_BUS_VIEWS a left outer join STG1_BO4V_BUS_VIEWS b on a.cuid=b.cuid where b.cuid is null and a.DELETE_FLG_DT is null)";
		try {
			Helper.runMSSQLInsertQuery(msSQLUpdateString);
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage() + " " + ex.getStackTrace());
		}

		//Prod - Data_Conn
		msSQLUpdateString = "UPDATE RPT_BO4_DATA_CONN ";
		msSQLUpdateString += " set delete_flg_dt=GETDATE(), delete_flg='Y' where si_id in (select a.si_id from RPT_BO4_DATA_CONN a left outer join STG1_BO4P_DATA_CONN b on a.cuid=b.cuid where b.cuid is null and a.DELETE_FLG_DT is null)";
		try {
			Helper.runMSSQLInsertQuery(msSQLUpdateString);
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage() + " " + ex.getStackTrace());
		}

		//QA - Data_Conn
		msSQLUpdateString = "UPDATE RPT_BO4_DATA_CONN ";
		msSQLUpdateString += " set delete_flg_dt=GETDATE(), delete_flg='Y' where si_id in (select a.si_id from RPT_BO4_DATA_CONN a left outer join STG1_BO4Q_DATA_CONN b on a.cuid=b.cuid where b.cuid is null and a.DELETE_FLG_DT is null)";
		try {
			Helper.runMSSQLInsertQuery(msSQLUpdateString);
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage() + " " + ex.getStackTrace());
		}

		//Dev - Data_Conn
		msSQLUpdateString = "UPDATE RPT_BO4_DATA_CONN ";
		msSQLUpdateString += " set delete_flg_dt=GETDATE(), delete_flg='Y' where si_id in (select a.si_id from RPT_BO4_DATA_CONN a left outer join STG1_BO4D_DATA_CONN b on a.cuid=b.cuid where b.cuid is null and a.DELETE_FLG_DT is null)";
		try {
			Helper.runMSSQLInsertQuery(msSQLUpdateString);
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage() + " " + ex.getStackTrace());
		}

		//Val - Data_Conn
		msSQLUpdateString = "UPDATE RPT_BO4_DATA_CONN ";
		msSQLUpdateString += " set delete_flg_dt=GETDATE(), delete_flg='Y' where si_id in (select a.si_id from RPT_BO4_DATA_CONN a left outer join STG1_BO4V_DATA_CONN b on a.cuid=b.cuid where b.cuid is null and a.DELETE_FLG_DT is null)";
		try {
			Helper.runMSSQLInsertQuery(msSQLUpdateString);
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage() + " " + ex.getStackTrace());
		}

		//Prod - Recurring
		msSQLUpdateString = "UPDATE RPT_BO4_RECURRSNAPSHOT ";
		msSQLUpdateString += " set delete_flg_dt=GETDATE(), delete_flg='Y' where instance_id in (select a.instance_id from RPT_BO4_RECURRSNAPSHOT a left outer join STG1_BO4P_RECURRINGJOBS b on a.cuid=b.cuid where b.cuid is null and a.DELETE_FLG_DT is null)";
		try {
			Helper.runMSSQLInsertQuery(msSQLUpdateString);
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage() + " " + ex.getStackTrace());
		}

		//QA - Recurring
		msSQLUpdateString = "UPDATE RPT_BO4_RECURRSNAPSHOT ";
		msSQLUpdateString += " set delete_flg_dt=GETDATE(), delete_flg='Y' where instance_id in (select a.instance_id from RPT_BO4_RECURRSNAPSHOT a left outer join STG1_BO4Q_RECURRINGJOBS b on a.cuid=b.cuid where b.cuid is null and a.DELETE_FLG_DT is null)";
		try {
			Helper.runMSSQLInsertQuery(msSQLUpdateString);
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage() + " " + ex.getStackTrace());
		}

		//Dev - Recurring
		msSQLUpdateString = "UPDATE RPT_BO4_RECURRSNAPSHOT ";
		msSQLUpdateString += " set delete_flg_dt=GETDATE(), delete_flg='Y' where instance_id in (select a.instance_id from RPT_BO4_RECURRSNAPSHOT a left outer join STG1_BO4D_RECURRINGJOBS b on a.cuid=b.cuid where b.cuid is null and a.DELETE_FLG_DT is null)";
		try {
			Helper.runMSSQLInsertQuery(msSQLUpdateString);
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage() + " " + ex.getStackTrace());
		}

		//Val - Recurring
		msSQLUpdateString = "UPDATE RPT_BO4_RECURRSNAPSHOT ";
		msSQLUpdateString += " set delete_flg_dt=GETDATE(), delete_flg='Y' where instance_id in (select a.instance_id from RPT_BO4_RECURRSNAPSHOT a left outer join STG1_BO4V_RECURRINGJOBS b on a.cuid=b.cuid where b.cuid is null and a.DELETE_FLG_DT is null)";
		try {
			Helper.runMSSQLInsertQuery(msSQLUpdateString);
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage() + " " + ex.getStackTrace());
		}

		//Prod - Universes
		msSQLUpdateString = "UPDATE RPT_BO4_UNIVERSES ";
		msSQLUpdateString += " set delete_flg_dt=GETDATE(), delete_flg='Y' where si_id in (select a.si_id from RPT_BO4_UNIVERSES a left outer join STG1_BO4P_UNIVERSES b on a.cuid=b.cuid where b.cuid is null and a.DELETE_FLG_DT is null)";
		try {
			Helper.runMSSQLInsertQuery(msSQLUpdateString);
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage() + " " + ex.getStackTrace());
		}

		//QA - Universes
		msSQLUpdateString = "UPDATE RPT_BO4_UNIVERSES ";
		msSQLUpdateString += " set delete_flg_dt=GETDATE(), delete_flg='Y' where si_id in (select a.si_id from RPT_BO4_UNIVERSES a left outer join STG1_BO4Q_UNIVERSES b on a.cuid=b.cuid where b.cuid is null and a.DELETE_FLG_DT is null)";
		try {
			Helper.runMSSQLInsertQuery(msSQLUpdateString);
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage() + " " + ex.getStackTrace());
		}

		//Dev - Universes
		msSQLUpdateString = "UPDATE RPT_BO4_UNIVERSES ";
		msSQLUpdateString += " set delete_flg_dt=GETDATE(), delete_flg='Y' where si_id in (select a.si_id from RPT_BO4_UNIVERSES a left outer join STG1_BO4D_UNIVERSES b on a.cuid=b.cuid where b.cuid is null and a.DELETE_FLG_DT is null)";
		try {
			Helper.runMSSQLInsertQuery(msSQLUpdateString);
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage() + " " + ex.getStackTrace());
		}

		//Val - Universes
		msSQLUpdateString = "UPDATE RPT_BO4_UNIVERSES ";
		msSQLUpdateString += " set delete_flg_dt=GETDATE(), delete_flg='Y' where si_id in (select a.si_id from RPT_BO4_UNIVERSES a left outer join STG1_BO4V_UNIVERSES b on a.cuid=b.cuid where b.cuid is null and a.DELETE_FLG_DT is null)";
		try {
			Helper.runMSSQLInsertQuery(msSQLUpdateString);
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage() + " " + ex.getStackTrace());
		}
		 */
		///////////////////////// END TEST ///////////////////////

		try{


			//'Update the list of reports in RPT_BOXI3_REPORTS to track the migrated
			// 'SI_IDs incrementally every day

			//'Get the latest SI_ID from RPT_BOXI3_REPORTS

			colNm1 = "report_id";
			colNm2 = "report_file_path";
			colNm3 = "si_file1";
			colNm4 = "si_path";
			colNm5 = "cuid";
			colNm6 = "report_nm";
			colNm7 = "SI_UNIVERSE";
			colNm8 = "SI_DSL_UNIVERSE";
			colNm9 = "DELETE_FLG";
			colNm10 = "DELETE_FLG_DT";


			//'maxID = getMaxID("select max(" & colNm1 & ") from RPT_BOXI3_REPORTS")



			//''6/2/2008:  get rid of the maxID condition and add in colNm6:            

			//'Query for when report path changed but cuid didn't
			oraUpdateString = "UPDATE " + destTblNm + " t1 ";
			oraUpdateString += " set " + colNm2 + " =(select report_file_path from  botrack.temp_xi_reports t2";
			oraUpdateString += " where t1." + colNm5 + " = t2.cuid), ";
			oraUpdateString += " update_dt = (select sysdate from botrack.temp_xi_reports t2";
			oraUpdateString += " where t1." + colNm5 + " = t2.cuid)";
			oraUpdateString += " where exists (select null from botrack.temp_xi_reports t3 where t1.cuid = t3.cuid and t1.report_file_path <> t3.report_file_path)";
			oraUpdateString += " and t1." + colNm2 + "  <> (select report_file_path from  botrack.temp_xi_reports t2 where t1.cuid = t2.cuid and t2.report_file_path is not null)";
			if (destTblNm.indexOf("stg1", 0) == 0) {
				oraUpdateString += "and t1.environment = ' " + EnterpriseEnv + "'";
			}

			//'Query for when report path changed but cuid didn't
			////////////////////The conversion of this is not finished
			msSQLUpdateString = "UPDATE " + destTblNm;
			msSQLUpdateString += " set " + colNm2 + " = t2.report_file_path," ;
			msSQLUpdateString += " update_dt = GETDATE()";
			msSQLUpdateString += " from " + destTblNm + " t1 ";
			msSQLUpdateString += " inner join temp_reports t2 ";
			msSQLUpdateString += " on t1.cuid = t2.cuid "; 
			if (destTblNm.indexOf("stg1", 0) == 0) {
				msSQLUpdateString += "and t1.environment = ' " + EnterpriseEnv + "'";
			}


			//oraUpdater.CommandText = oraUpdateString
			System.out.println(msSQLUpdateString);

			try {
				//oraUpdater.ExecuteNonQuery()
				Helper.runMSSQLInsertQuery(msSQLUpdateString);
			}
			catch (Exception ex) {
				System.out.println(ex.getMessage() + " " + ex.getStackTrace());
			}

			//'Query for when report name changed but cuid didn't
			oraUpdateString = "UPDATE " + destTblNm + " t1 ";
			oraUpdateString += " set " + colNm6 + " =(select report_nm from  botrack.temp_xi_reports t2";
			oraUpdateString += " where t1." + colNm5 + " = t2.cuid), ";
			oraUpdateString += colNm2 + " = (select REPORT_FILE_PATH from  botrack.temp_xi_reports  t2";
			oraUpdateString += " where t1." + colNm5 + " = t2.cuid), ";
			oraUpdateString += " update_dt = (select sysdate from botrack.temp_xi_reports t2";
			oraUpdateString += " where t1." + colNm5 + " = t2.cuid) ";
			oraUpdateString += " where exists (select null from botrack.temp_xi_reports t3 where t1.cuid = t3.cuid and t1.report_nm <> t3.report_nm)";
			oraUpdateString += " and t1." + colNm6 + "  <> (select t2.report_nm from  botrack.temp_xi_reports t2 where t1.cuid = t2.cuid and t2.report_nm is not null)";

			if (destTblNm.indexOf("stg1", 0) == 0) {
				oraUpdateString += "and t1.environment = ' " + EnterpriseEnv + "'";
			}

			System.out.println(oraUpdateString);

			//'Query for when report name changed but cuid didn't
			msSQLUpdateString = "UPDATE " + destTblNm;
			msSQLUpdateString += " set " + colNm6 + " = t2.report_nm," ;
			msSQLUpdateString += " update_dt = GETDATE()";
			msSQLUpdateString += " from " + destTblNm + " t1 ";
			msSQLUpdateString += " inner join temp_reports t2 ";
			msSQLUpdateString += " on t1.cuid = t2.cuid "; 
			if (destTblNm.indexOf("stg1", 0) ==0) {
				msSQLUpdateString += "and t1.environment = ' " + EnterpriseEnv + "'";
			}
			System.out.println(msSQLUpdateString);

			
			try {
				Helper.runMSSQLInsertQuery(msSQLUpdateString);
			}
			catch (Exception ex)
			{
				System.out.println(ex.getMessage() + " " + ex.getStackTrace());
			}


			//'Query for when cuid changed but report name didn't, set by report name and path
			oraUpdateString = "UPDATE " + destTblNm + " t1 ";
			oraUpdateString += " set " + colNm5 + " =(select cuid from  botrack.temp_xi_reports t2";
			oraUpdateString += " where t1." + colNm6 + " = t2.report_nm and t1." + colNm2 + " =t2.report_file_path), ";
		oraUpdateString += " update_dt = (select sysdate from botrack.temp_xi_reports t2";
			oraUpdateString += " where t1." + colNm6 + " = t2.report_nm and t1." + colNm2 + " =t2.report_file_path) ";
			oraUpdateString += " where exists (select null from botrack.temp_xi_reports t3 where t1.cuid <> t3.cuid and t1.report_nm = t3.report_nm and t1.report_file_path =t3.report_file_path)";
			oraUpdateString += " and t1." + colNm5 + "  <> (select t2.cuid from  botrack.temp_xi_reports t2 where t1.report_nm = t2.report_nm and t1.report_file_path =t2.report_file_path and t2.cuid is not null)";
			if (destTblNm.indexOf("stg1", 0) ==0) {
				msSQLUpdateString += "and t1.environment = ' " + EnterpriseEnv + "'";
			}

			//'Query for when cuid changed but report name didn't, set by report name and path
			msSQLUpdateString = "UPDATE " + destTblNm;
			msSQLUpdateString += " set " + colNm5 + " = t2.cuid," ;
			msSQLUpdateString += " update_dt = GETDATE()";
			msSQLUpdateString += " from " + destTblNm + " t1 ";
			msSQLUpdateString += " inner join temp_reports t2 ";
			msSQLUpdateString += " on t1.report_nm = t2.report_nm ";
			msSQLUpdateString += " and t1.report_file_path = t2.report_file_path ";
			if (destTblNm.indexOf("stg1", 0) ==0) {
				msSQLUpdateString += "and t1.environment = ' " + EnterpriseEnv + "'";
			}
			System.out.println(msSQLUpdateString);
			try {
				Helper.runMSSQLInsertQuery(msSQLUpdateString);
			}
			catch (Exception ex) {
				System.out.println(ex.getMessage() + " " + ex.getStackTrace());
			}

			

			// 'Query for when si_universe changed but cuid didn't

			oraUpdateString = "UPDATE RPT_BO4_REPORTS t1 ";
			oraUpdateString += " set " + colNm7 + " =(select SI_UNIVERSE from  botrack.temp_xi_reports t2";
			oraUpdateString += " where t1." + colNm5 + " = t2.cuid) ";
			oraUpdateString += " where exists (select null from botrack.temp_xi_reports t3 where t1.cuid = t3.cuid and t3.report_file_path is not null)";
			oraUpdateString += " and t1." + colNm7 + "  <> (select SI_UNIVERSE from  botrack.temp_xi_reports t2 where t1.cuid = t2.cuid and t2.report_file_path is not null)";
			oraUpdateString += " and t1.ENVIRONMENT = '" + EnterpriseEnv + "'";

			// 'Query for when si_universe changed but cuid didn't
			msSQLUpdateString = "UPDATE RPT_BO4_REPORTS ";
			msSQLUpdateString += " set " + colNm7 + " = t2.SI_UNIVERSE," ;
			msSQLUpdateString += " update_dt = GETDATE()";
			msSQLUpdateString += " from RPT_BO4_REPORTS t1" ;
			msSQLUpdateString += " inner join temp_reports t2 ";
			msSQLUpdateString += " on t1.CUID = t2.CUID ";

			System.out.println(msSQLUpdateString);

			try {
				Helper.runMSSQLInsertQuery(msSQLUpdateString);
			}
			catch (Exception ex)
			{
				System.out.println(ex.getMessage() + " " + ex.getStackTrace());
			}

			// 'Query for when si_dsl_universe changed but cuid didn't
			/*-------------------si_dsl_universe doesn't exist in RPT_BO4_REPORTS!!!!
				oraUpdateString = "UPDATE RPT_BO4_REPORTS t1 ";
				oraUpdateString += " set " + colNm8 + " =(select SI_DSL_UNIVERSE from  botrack.temp_xi_reports t2";
				oraUpdateString += " where t1." + colNm5 + " = t2.cuid) ";
				oraUpdateString += " where exists (select null from botrack.temp_xi_reports t3 where t1.cuid = t3.cuid and t3.report_file_path is not null)";
				oraUpdateString += " and t1." + colNm8 + "  <> (select SI_DSL_UNIVERSE from  botrack.temp_xi_reports t2 where t1.cuid = t2.cuid and t2.report_file_path is not null)";
				oraUpdateString += " and t1.ENVIRONMENT = '" + EnterpriseEnv + "'";


				try {
					Helper.runMSSQLInsertQuery(oraUpdateString);
				}
				catch (Exception ex) {
					System.out.println(ex.getMessage() + " " + ex.getStackTrace());
				}
			 */

						//'Query for when report path changed, cuid changed but report name didn't
			// 'Might not be possible since many reports are duplicated across folders

		}

		catch (Exception ex)
		{
			System.out.println(ex.getMessage() + " " + ex.getStackTrace());
		}


	} //End Sub

	public static void update_stage3_delete_flag() {
		//Pre-condition:  the delete flag is out of date
		//Post-condition:  the delete flag is up to date.
		
		String msSQLUpdateString;
		String msSQLInsertString =	"insert into STG1_BO4_TRACKER_ERRORS (ERROR_MESSAGE, LOAD_DT) VALUES ('update_stage3_delete_flag() has started', GETDATE())";
		try {
			Helper.runMSSQLInsertQuery(msSQLInsertString);
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage() + " " + ex.getStackTrace());
		}
		
		//////////////////////Set the delete_flg in Stage 3 tables/////////////
		//Prod - Webi
	
		//msSQLUpdateString = "UPDATE RPT_BO4_REPORTS ";
		//msSQLUpdateString += " set delete_flg='Y', delete_flg_dt = GETDATE() where si_id in (select a.si_id from RPT_BO4_REPORTS a left outer join STG1_BO4P_WX_REPORTS b on a.cuid=b.cuid where b.cuid is null and a.DELETE_FLG_DT is null)";
		//I think this is setting the delete flag all the time.

		msSQLUpdateString = "UPDATE RPT_BO4_REPORTS SET RPT_BO4_REPORTS.delete_flg = STG1_BO4P_WX_REPORTS.delete_flg, RPT_BO4_REPORTS.delete_flg_dt = STG1_BO4P_WX_REPORTS.delete_flg_dt FROM STG1_BO4P_WX_REPORTS, RPT_BO4_REPORTS WHERE STG1_BO4P_WX_REPORTS.si_id = RPT_BO4_REPORTS.si_id";

		System.out.println(msSQLUpdateString);
		try {
			Helper.runMSSQLInsertQuery(msSQLUpdateString);
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage() + " " + ex.getStackTrace());
		}


		//'QA - Webi
	
		//msSQLUpdateString = "UPDATE RPT_BO4_REPORTS ";
		//msSQLUpdateString += " set delete_flg_dt=GETDATE(), delete_flg='Y' where si_id in (select a.si_id from RPT_BO4_REPORTS a left outer join STG1_BO4Q_WX_REPORTS b on a.cuid=b.cuid where b.cuid is null and a.DELETE_FLG_DT is null)";

		msSQLUpdateString = "UPDATE RPT_BO4_REPORTS SET RPT_BO4_REPORTS.delete_flg = STG1_BO4Q_WX_REPORTS.delete_flg, RPT_BO4_REPORTS.delete_flg_dt = STG1_BO4Q_WX_REPORTS.delete_flg_dt FROM STG1_BO4Q_WX_REPORTS, RPT_BO4_REPORTS WHERE STG1_BO4Q_WX_REPORTS.si_id = RPT_BO4_REPORTS.si_id";

		System.out.println(msSQLUpdateString);

		try {
			Helper.runMSSQLInsertQuery(msSQLUpdateString);
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage() + " " + ex.getStackTrace());
		}


		//'Dev - Webi
		
		//msSQLUpdateString = "UPDATE RPT_BO4_REPORTS ";
		//msSQLUpdateString += " set delete_flg_dt=GETDATE(), delete_flg='Y' where si_id in (select a.si_id from RPT_BO4_REPORTS a left outer join STG1_BO4D_WX_REPORTS b on a.cuid=b.cuid where b.cuid is null and a.DELETE_FLG_DT is null)";

		msSQLUpdateString = "UPDATE RPT_BO4_REPORTS SET RPT_BO4_REPORTS.delete_flg = STG1_BO4D_WX_REPORTS.delete_flg, RPT_BO4_REPORTS.delete_flg_dt = STG1_BO4D_WX_REPORTS.delete_flg_dt FROM STG1_BO4D_WX_REPORTS, RPT_BO4_REPORTS WHERE STG1_BO4D_WX_REPORTS.si_id = RPT_BO4_REPORTS.si_id";

		System.out.println(msSQLUpdateString);

		try {
			Helper.runMSSQLInsertQuery(msSQLUpdateString);
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage() + " " + ex.getStackTrace());
		}

		//Val - Webi
		//msSQLUpdateString = "UPDATE RPT_BO4_REPORTS ";
		//msSQLUpdateString += " set delete_flg_dt=GETDATE(), delete_flg='Y' where si_id in (select a.si_id from RPT_BO4_REPORTS a left outer join STG1_BO4V_WX_REPORTS b on a.cuid=b.cuid where b.cuid is null and a.DELETE_FLG_DT is null)";

		msSQLUpdateString = "UPDATE RPT_BO4_REPORTS SET RPT_BO4_REPORTS.delete_flg = STG1_BO4V_WX_REPORTS.delete_flg, RPT_BO4_REPORTS.delete_flg_dt = STG1_BO4V_WX_REPORTS.delete_flg_dt FROM STG1_BO4V_WX_REPORTS, RPT_BO4_REPORTS WHERE STG1_BO4V_WX_REPORTS.si_id = RPT_BO4_REPORTS.si_id";

		System.out.println(msSQLUpdateString);

		try {
			Helper.runMSSQLInsertQuery(msSQLUpdateString);
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage() + " " + ex.getStackTrace());
		}




		//'Prod - Crystal
	

		//msSQLUpdateString = "UPDATE RPT_BO4_REPORTS ";
		//msSQLUpdateString += " set delete_flg_dt=GETDATE(), delete_flg='Y' where si_id in (select a.si_id from RPT_BO4_REPORTS a left outer join STG1_BO4P_CREPORTS b on a.cuid=b.cuid where b.cuid is null and a.DELETE_FLG_DT is null)";
		msSQLUpdateString = "UPDATE RPT_BO4_REPORTS SET RPT_BO4_REPORTS.delete_flg = STG1_BO4P_CREPORTS.delete_flg, RPT_BO4_REPORTS.delete_flg_dt = STG1_BO4P_CREPORTS.delete_flg_dt FROM STG1_BO4P_CREPORTS, RPT_BO4_REPORTS WHERE STG1_BO4P_CREPORTS.si_id = RPT_BO4_REPORTS.si_id";
		//oraUpdater.CommandText = oraUpdateString
		//System.out.println(oraUpdater.CommandText)

		System.out.println(msSQLUpdateString);

		try {
			Helper.runMSSQLInsertQuery(msSQLUpdateString);
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage() + " " + ex.getStackTrace());
		}


		//'QA - Crystal
		
		//msSQLUpdateString = "UPDATE RPT_BO4_REPORTS ";
		//msSQLUpdateString += " set delete_flg_dt=GETDATE(), delete_flg='Y' where si_id in (select a.si_id from RPT_BO4_REPORTS a left outer join STG1_BO4Q_CREPORTS b on a.cuid=b.cuid where b.cuid is null and a.DELETE_FLG_DT is null)";

		msSQLUpdateString = "UPDATE RPT_BO4_REPORTS SET RPT_BO4_REPORTS.delete_flg = STG1_BO4Q_CREPORTS.delete_flg, RPT_BO4_REPORTS.delete_flg_dt = STG1_BO4Q_CREPORTS.delete_flg_dt FROM STG1_BO4Q_CREPORTS, RPT_BO4_REPORTS WHERE STG1_BO4Q_CREPORTS.si_id = RPT_BO4_REPORTS.si_id";
		System.out.println(msSQLUpdateString);

		try {
			Helper.runMSSQLInsertQuery(msSQLUpdateString);
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage() + " " + ex.getStackTrace());
		}


		//'Dev - Crystal
	
		//msSQLUpdateString = "UPDATE RPT_BO4_REPORTS ";
		//msSQLUpdateString += " set delete_flg_dt=GETDATE(), delete_flg='Y' where si_id in (select a.si_id from RPT_BO4_REPORTS a left outer join STG1_BO4D_CREPORTS b on a.cuid=b.cuid where b.cuid is null and a.DELETE_FLG_DT is null)";

		msSQLUpdateString = "UPDATE RPT_BO4_REPORTS SET RPT_BO4_REPORTS.delete_flg = STG1_BO4D_CREPORTS.delete_flg, RPT_BO4_REPORTS.delete_flg_dt = STG1_BO4D_CREPORTS.delete_flg_dt FROM STG1_BO4D_CREPORTS, RPT_BO4_REPORTS WHERE STG1_BO4D_CREPORTS.si_id = RPT_BO4_REPORTS.si_id";
		System.out.println(msSQLUpdateString);

		try {
			Helper.runMSSQLInsertQuery(msSQLUpdateString);
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage() + " " + ex.getStackTrace());
		}

		//Val - Crystal
		//msSQLUpdateString = "UPDATE RPT_BO4_REPORTS ";
		//msSQLUpdateString += " set delete_flg_dt=GETDATE(), delete_flg='Y' where si_id in (select a.si_id from RPT_BO4_REPORTS a left outer join STG1_BO4V_CREPORTS b on a.cuid=b.cuid where b.cuid is null and a.DELETE_FLG_DT is null)";

		msSQLUpdateString = "UPDATE RPT_BO4_REPORTS SET RPT_BO4_REPORTS.delete_flg = STG1_BO4V_CREPORTS.delete_flg, RPT_BO4_REPORTS.delete_flg_dt = STG1_BO4V_CREPORTS.delete_flg_dt FROM STG1_BO4V_CREPORTS, RPT_BO4_REPORTS WHERE STG1_BO4V_CREPORTS.si_id = RPT_BO4_REPORTS.si_id";
		System.out.println(msSQLUpdateString);

		try {
			Helper.runMSSQLInsertQuery(msSQLUpdateString);
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage() + " " + ex.getStackTrace());
		}

		//Prod - Business Views
		msSQLUpdateString = "UPDATE RPT_BO4_BUS_VIEWS SET RPT_BO4_BUS_VIEWS.delete_flg = STG1_BO4P_BUS_VIEWS.delete_flg, RPT_BO4_BUS_VIEWS.delete_flg_dt = STG1_BO4P_BUS_VIEWS.delete_flg_dt FROM STG1_BO4P_BUS_VIEWS, RPT_BO4_BUS_VIEWS WHERE STG1_BO4P_BUS_VIEWS.si_id = RPT_BO4_BUS_VIEWS.si_id";

		try {
			Helper.runMSSQLInsertQuery(msSQLUpdateString);
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage() + " " + ex.getStackTrace());
		}

		//QA - Business Views
		msSQLUpdateString = "UPDATE RPT_BO4_BUS_VIEWS SET RPT_BO4_BUS_VIEWS.delete_flg = STG1_BO4Q_BUS_VIEWS.delete_flg, RPT_BO4_BUS_VIEWS.delete_flg_dt = STG1_BO4Q_BUS_VIEWS.delete_flg_dt FROM STG1_BO4Q_BUS_VIEWS, RPT_BO4_BUS_VIEWS WHERE STG1_BO4Q_BUS_VIEWS.si_id = RPT_BO4_BUS_VIEWS.si_id";

		try {
			Helper.runMSSQLInsertQuery(msSQLUpdateString);
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage() + " " + ex.getStackTrace());
		}


		//Dev - Business Views
		msSQLUpdateString = "UPDATE RPT_BO4_BUS_VIEWS SET RPT_BO4_BUS_VIEWS.delete_flg = STG1_BO4D_BUS_VIEWS.delete_flg, RPT_BO4_BUS_VIEWS.delete_flg_dt = STG1_BO4D_BUS_VIEWS.delete_flg_dt FROM STG1_BO4D_BUS_VIEWS, RPT_BO4_BUS_VIEWS WHERE STG1_BO4D_BUS_VIEWS.si_id = RPT_BO4_BUS_VIEWS.si_id";

		try {
			Helper.runMSSQLInsertQuery(msSQLUpdateString);
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage() + " " + ex.getStackTrace());
		}


		//Val - Business Views
		msSQLUpdateString = "UPDATE RPT_BO4_BUS_VIEWS SET RPT_BO4_BUS_VIEWS.delete_flg = STG1_BO4V_BUS_VIEWS.delete_flg, RPT_BO4_BUS_VIEWS.delete_flg_dt = STG1_BO4V_BUS_VIEWS.delete_flg_dt FROM STG1_BO4V_BUS_VIEWS, RPT_BO4_BUS_VIEWS WHERE STG1_BO4V_BUS_VIEWS.si_id = RPT_BO4_BUS_VIEWS.si_id";

		try {
			Helper.runMSSQLInsertQuery(msSQLUpdateString);
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage() + " " + ex.getStackTrace());
		}

		//Prod - Data_Conn
		msSQLUpdateString = "UPDATE RPT_BO4_DATA_CONN SET RPT_BO4_DATA_CONN.delete_flg = STG1_BO4P_DATA_CONN.delete_flg, RPT_BO4_DATA_CONN.delete_flg_dt = STG1_BO4P_DATA_CONN.delete_flg_dt FROM STG1_BO4P_DATA_CONN, RPT_BO4_DATA_CONN WHERE STG1_BO4P_DATA_CONN.si_id = RPT_BO4_DATA_CONN.si_id";

		try {
			Helper.runMSSQLInsertQuery(msSQLUpdateString);
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage() + " " + ex.getStackTrace());
		}

		//QA - Data_Conn
		msSQLUpdateString = "UPDATE RPT_BO4_DATA_CONN SET RPT_BO4_DATA_CONN.delete_flg = STG1_BO4Q_DATA_CONN.delete_flg, RPT_BO4_DATA_CONN.delete_flg_dt = STG1_BO4Q_DATA_CONN.delete_flg_dt FROM STG1_BO4Q_DATA_CONN, RPT_BO4_DATA_CONN WHERE STG1_BO4Q_DATA_CONN.si_id = RPT_BO4_DATA_CONN.si_id";

		try {
			Helper.runMSSQLInsertQuery(msSQLUpdateString);
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage() + " " + ex.getStackTrace());
		}

		//Dev - Data_Conn
		msSQLUpdateString = "UPDATE RPT_BO4_DATA_CONN SET RPT_BO4_DATA_CONN.delete_flg = STG1_BO4D_DATA_CONN.delete_flg, RPT_BO4_DATA_CONN.delete_flg_dt = STG1_BO4D_DATA_CONN.delete_flg_dt FROM STG1_BO4D_DATA_CONN, RPT_BO4_DATA_CONN WHERE STG1_BO4D_DATA_CONN.si_id = RPT_BO4_DATA_CONN.si_id";

		try {
			Helper.runMSSQLInsertQuery(msSQLUpdateString);
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage() + " " + ex.getStackTrace());
		}

		//Val - Data_Conn
		msSQLUpdateString = "UPDATE RPT_BO4_DATA_CONN SET RPT_BO4_DATA_CONN.delete_flg = STG1_BO4V_DATA_CONN.delete_flg, RPT_BO4_DATA_CONN.delete_flg_dt = STG1_BO4V_DATA_CONN.delete_flg_dt FROM STG1_BO4V_DATA_CONN, RPT_BO4_DATA_CONN WHERE STG1_BO4V_DATA_CONN.si_id = RPT_BO4_DATA_CONN.si_id";

		try {
			Helper.runMSSQLInsertQuery(msSQLUpdateString);
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage() + " " + ex.getStackTrace());
		}

		//Prod - Recurring
		msSQLUpdateString = "UPDATE RPT_BO4_RECURRSNAPSHOT SET RPT_BO4_RECURRSNAPSHOT.delete_flg = STG1_BO4P_RECURRINGJOBS.delete_flg, RPT_BO4_RECURRSNAPSHOT.delete_flg_dt = STG1_BO4P_RECURRINGJOBS.delete_flg_dt FROM STG1_BO4P_RECURRINGJOBS, RPT_BO4_RECURRSNAPSHOT WHERE STG1_BO4P_RECURRINGJOBS.instance_id = RPT_BO4_RECURRSNAPSHOT.instance_id ";

		try {
			Helper.runMSSQLInsertQuery(msSQLUpdateString);
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage() + " " + ex.getStackTrace());
		}

		//QA - Recurring
		msSQLUpdateString = "UPDATE RPT_BO4_RECURRSNAPSHOT SET RPT_BO4_RECURRSNAPSHOT.delete_flg = STG1_BO4Q_RECURRINGJOBS.delete_flg, RPT_BO4_RECURRSNAPSHOT.delete_flg_dt = STG1_BO4Q_RECURRINGJOBS.delete_flg_dt FROM STG1_BO4Q_RECURRINGJOBS, RPT_BO4_RECURRSNAPSHOT WHERE STG1_BO4Q_RECURRINGJOBS.instance_id = RPT_BO4_RECURRSNAPSHOT.instance_id";

		try {
			Helper.runMSSQLInsertQuery(msSQLUpdateString);
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage() + " " + ex.getStackTrace());
		}

		//Dev - Recurring
		msSQLUpdateString = "UPDATE RPT_BO4_RECURRSNAPSHOT SET RPT_BO4_RECURRSNAPSHOT.delete_flg = STG1_BO4D_RECURRINGJOBS.delete_flg, RPT_BO4_RECURRSNAPSHOT.delete_flg_dt = STG1_BO4D_RECURRINGJOBS.delete_flg_dt FROM STG1_BO4D_RECURRINGJOBS, RPT_BO4_RECURRSNAPSHOT WHERE STG1_BO4D_RECURRINGJOBS.instance_id = RPT_BO4_RECURRSNAPSHOT.instance_id ";

		try {
			Helper.runMSSQLInsertQuery(msSQLUpdateString);
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage() + " " + ex.getStackTrace());
		}

		//Val - Recurring
		msSQLUpdateString = "UPDATE RPT_BO4_RECURRSNAPSHOT SET RPT_BO4_RECURRSNAPSHOT.delete_flg = STG1_BO4V_RECURRINGJOBS.delete_flg, RPT_BO4_RECURRSNAPSHOT.delete_flg_dt = STG1_BO4V_RECURRINGJOBS.delete_flg_dt FROM STG1_BO4V_RECURRINGJOBS, RPT_BO4_RECURRSNAPSHOT WHERE STG1_BO4V_RECURRINGJOBS.instance_id = RPT_BO4_RECURRSNAPSHOT.instance_id";

		try {
			Helper.runMSSQLInsertQuery(msSQLUpdateString);
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage() + " " + ex.getStackTrace());
		}

		//Prod - Universes
		msSQLUpdateString = "UPDATE RPT_BO4_UNIVERSES SET RPT_BO4_UNIVERSES.delete_flg = STG1_BO4P_UNIVERSES.delete_flg, RPT_BO4_UNIVERSES.delete_flg_dt = STG1_BO4P_UNIVERSES.delete_flg_dt FROM STG1_BO4P_UNIVERSES, RPT_BO4_UNIVERSES WHERE STG1_BO4P_UNIVERSES.si_id = RPT_BO4_UNIVERSES.si_id";

		try {
			Helper.runMSSQLInsertQuery(msSQLUpdateString);
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage() + " " + ex.getStackTrace());
		}

		//QA - Universes
		msSQLUpdateString = "UPDATE RPT_BO4_UNIVERSES SET RPT_BO4_UNIVERSES.delete_flg = STG1_BO4Q_UNIVERSES.delete_flg, RPT_BO4_UNIVERSES.delete_flg_dt = STG1_BO4Q_UNIVERSES.delete_flg_dt FROM STG1_BO4Q_UNIVERSES, RPT_BO4_UNIVERSES WHERE STG1_BO4Q_UNIVERSES.si_id = RPT_BO4_UNIVERSES.si_id";

		try {
			Helper.runMSSQLInsertQuery(msSQLUpdateString);
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage() + " " + ex.getStackTrace());
		}

		//Dev - Universes
		msSQLUpdateString = "UPDATE RPT_BO4_UNIVERSES SET RPT_BO4_UNIVERSES.delete_flg = STG1_BO4D_UNIVERSES.delete_flg, RPT_BO4_UNIVERSES.delete_flg_dt = STG1_BO4D_UNIVERSES.delete_flg_dt FROM STG1_BO4D_UNIVERSES, RPT_BO4_UNIVERSES WHERE STG1_BO4D_UNIVERSES.si_id = RPT_BO4_UNIVERSES.si_id";

		try {
			Helper.runMSSQLInsertQuery(msSQLUpdateString);
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage() + " " + ex.getStackTrace());
		}

		//Val - Universes
		msSQLUpdateString = "UPDATE RPT_BO4_UNIVERSES SET RPT_BO4_UNIVERSES.delete_flg = STG1_BO4V_UNIVERSES.delete_flg, RPT_BO4_UNIVERSES.delete_flg_dt = STG1_BO4V_UNIVERSES.delete_flg_dt FROM STG1_BO4V_UNIVERSES, RPT_BO4_UNIVERSES WHERE STG1_BO4V_UNIVERSES.si_id = RPT_BO4_UNIVERSES.si_id";

		try {
			Helper.runMSSQLInsertQuery(msSQLUpdateString);
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage() + " " + ex.getStackTrace());
		}
		
		msSQLInsertString =	"insert into STG1_BO4_TRACKER_ERRORS (ERROR_MESSAGE, LOAD_DT) VALUES ('update_stage3_delete_flag() has ended', GETDATE())";
		try {
			Helper.runMSSQLInsertQuery(msSQLInsertString);
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage() + " " + ex.getStackTrace());
		}
	}

	public static void load_stage3_tables() {
		//String strSQL;
		String msSQLInsertString;
		Calendar now=Calendar.getInstance();

		try {


			//*******************************TEST********************************
			/*	// ''Prod recurring jobs
						//msSQLInsertString = "insert into RPT_BOXI_RECURRSNAPSHOT SELECT REPORT_ID, INSTANCE_ID, SI_STARTTIME,    SI_ENDTIME, SI_SUBMITTER, SI_SERVER,    SI_OWNER, SI_SCHEDULE_STATUS, SI_SCHEDULE_TYPE,    SI_SCHEDULE_INTERVAL_MINUTES, SI_SCHEDULE_INTERVAL_HOURS, SI_SCHEDULE_INTERVAL_MONTHS,    SI_NAME, SI_CREATION_TIME, SI_DESTINATION,    SI_PASSWORD, DATASRC_LOGIN_NM, DATASRC_LOGIN_NM2,    SI_PASSWORD2, SI_NUM_PROMPTS, REPORT_PATH,    LOAD_DT, SNAPSHOT_DT, SI_NEXTRUNTIME,    REPORT_CUID, SI_MACHINE_USED, DISTRIBUTION_LIST 'Prod' FROM STG1_MIGR_XIR2P_RECURRINGJOBS where load_dt >= to_date('" + oracleOnTheHour(Now()) + "','DD-MM-YYYY HH24:MI:SS')";
						msSQLInsertString = "insert into RPT_BO4_RECURRSNAPSHOT SELECT a.REPORT_ID, a.INSTANCE_ID, a.SI_STARTTIME,    a.SI_ENDTIME, a.SI_SUBMITTER, a.SI_SERVER,    a.SI_OWNER, a.SI_SCHEDULE_STATUS, a.SI_SCHEDULE_TYPE,    a.SI_SCHEDULE_INTERVAL_MINUTES, a.SI_SCHEDULE_INTERVAL_HOURS, a.SI_SCHEDULE_INTERVAL_MONTHS,    a.SI_NAME, a.SI_CREATION_TIME, a.SI_DESTINATION,    a.SI_PASSWORD, a.DATASRC_LOGIN_NM, a.DATASRC_LOGIN_NM2,    a.SI_PASSWORD2, a.SI_NUM_PROMPTS, a.REPORT_PATH,    a.LOAD_DT, a.SNAPSHOT_DT, a.SI_NEXTRUNTIME,    a.REPORT_CUID, a.SI_MACHINE_USED, 'Prod' ENVIRONMENT, a.DISTRIBUTION_LIST ,  a.PARAM_VALUES, a.CUID, a.SI_FORMAT_EXPORT_ALLPAGES, a.SI_KIND, a.SI_SCHEDULE_INTERVAL_DAYS, a.CALENDAR_ID, a.INTERVAL_NTH_DAY FROM STG1_BO4P_RECURRINGJOBS a left outer join RPT_BO4_RECURRSNAPSHOT b on a.instance_id = b.instance_id where b.instance_id is null";
						System.out.println(msSQLInsertString);;
						try {
							//runMSSQLInsertQuery(strSQL)
							Helper.runMSSQLInsertQuery(msSQLInsertString);
						}
						catch (Exception ex){
							System.out.println(ex.getMessage() + " " + ex.getStackTrace());
						}*/

			/*//Prod
			//Webi
			msSQLInsertString = "insert into RPT_BO4_REPORTS (SI_ID, SI_PATH, SI_FILE1, REPORT_NM, REPORT_FILE_PATH, LOAD_DT, SI_CREATION_TIME, CUID, SI_OWNER, SI_PARENTID, environment, source_system, data_source_nm, SI_KIND, SI_UNIVERSE, CREATE_DATE, LOAD_DATE, SI_DOC_COMMON_CONNECTION, DATASOURCE_TYPE) SELECT a.SI_ID, a.SI_PATH, a.SI_FILE1, a.REPORT_NM, a.REPORT_FILE_PATH, a.LOAD_DT, a.SI_CREATION_TIME, a.CUID, a.SI_OWNER, a.SI_PARENTID, 'Prod' ENVIRONMENT , 'na/' SOURCE_SYSTEM , a.DATA_SOURCE_NM, a.SI_KIND, a.SI_UNIVERSE, CONVERT(DATETIME, CONVERT(DATE, a.SI_CREATION_TIME)), CONVERT(DATETIME, CONVERT(DATE, a.LOAD_DT)), a.SI_DOC_COMMON_CONNECTION, a.DATASOURCE_TYPE FROM STG1_BO4P_wx_reports a left outer join RPT_BO4_REPORTS b on a.si_id = b.si_id where b.si_id is null";

			System.out.println(msSQLInsertString);;
			try {
				//runMSSQLInsertQuery(strSQL)
				Helper.runMSSQLInsertQuery(msSQLInsertString);
			}
			catch (Exception ex){
				System.out.println(ex.getMessage() + " " + ex.getStackTrace());
			}

			//Crystal
			msSQLInsertString = "insert into RPT_BO4_REPORTS (SI_ID, SI_PATH, SI_FILE1, REPORT_NM, REPORT_FILE_PATH, SOURCE_SYSTEM,    DATA_SOURCE_NM, DATA_SOURCE2_NM, DYNAMIC_PICK_LIST_NM,    LOAD_DT, SI_CREATION_TIME, SI_UPDATE_TS,    CUID, SI_OWNER, SI_BUSINESS_VIEW_INFO,    SI_PARENTID, SI_LOCAL_FILEPATH, ORIGINAL_FILE_NM,    SI_PARENT_FOLDER_CUID, UPDATE_DT, ENVIRONMENT,   SI_KIND,  CREATE_DATE,   UPDATE_DATE, LOAD_DATE, DATASOURCE_TYPE) SELECT cr.SI_ID,  cr.SI_PATH,  cr.SI_FILE1,  cr.REPORT_NM,  cr.REPORT_FILE_PATH,  cr.SOURCE_SYSTEM,  cr.DATA_SOURCE_NM,  cr.DATA_SOURCE2_NM,  cr.DYNAMIC_PICK_LIST_NM,  cr.LOAD_DT,  cr.SI_CREATION_TIME,  cr.SI_UPDATE_TS,  cr.CUID,  cr.SI_OWNER,  cr.SI_BUSINESS_VIEW_INFO,  cr.SI_PARENTID,  cr.SI_LOCAL_FILEPATH,  cr.ORIGINAL_FILE_NM,  cr.SI_PARENT_FOLDER_CUID,  cr.UPDATE_DT,  'Prod' ENVIRONMENT,  cr.SI_KIND, CONVERT(DATETIME, CONVERT(DATE, cr.si_creation_time)) creation_Date, CONVERT(DATETIME, CONVERT(DATE, cr.SI_UPDATE_TS)) UPDATE_DATE,  CONVERT(DATETIME, CONVERT(DATE, LOAD_DT)) LOAD_DATE, cr.DATASOURCE_TYPE FROM   STG1_BO4p_creports cr left outer join (select cuid from RPT_BO4_REPORTS where environment='Prod' and si_kind='CrystalReport') b on cr.CUID = b.CUID where b.CUID   IS NULL";

			System.out.println(msSQLInsertString);;
			try {
				//runMSSQLInsertQuery(strSQL)
				Helper.runMSSQLInsertQuery(msSQLInsertString);
			}
			catch (Exception ex){
				System.out.println(ex.getMessage() + " " + ex.getStackTrace());
			}*/


			/*	//Val
			msSQLInsertString = "INSERT INTO RPT_BO4_DATA_CONN (SI_METADATA_BVCONN_PASSWORD, SI_METADATA_BVCONN_USERNAME, SI_CREATION_TIME, SI_KIND, SI_UPDATE_TS, SI_NAME, SI_ID, SI_PARENT_FOLDER_CUID, SI_PARENTID, SI_PARENT_FOLDER, SI_PARENT_CUID, CUID, SI_OWNER, LOAD_DT, SI_PROVIDER_CAPTION, SI_SERVER,ENVIRONMENT) select a.SI_METADATA_BVCONN_PASSWORD, a.SI_METADATA_BVCONN_USERNAME, a.SI_CREATION_TIME, a.SI_KIND, a.SI_UPDATE_TS, a.SI_NAME, a.SI_ID, a.SI_PARENT_FOLDER_CUID, a.SI_PARENTID, a.SI_PARENT_FOLDER, a.SI_PARENT_CUID, a.CUID, a.SI_OWNER, a.LOAD_DT, a.SI_PROVIDER_CAPTION, a.SI_SERVER,'Val' environment from STG1_BO4V_DATA_CONN a left outer join (select * from RPT_BO4_DATA_CONN b where environment='Val') b on a.si_id = b.si_id where b.si_id is null";
			System.out.println(msSQLInsertString);
			try {
				//runMSSQLInsertQuery(strSQL)
				Helper.runMSSQLInsertQuery(msSQLInsertString);
			}
			catch (Exception ex){
				System.out.println(ex.getMessage() + " " + ex.getStackTrace());
			}*/
			/*// '''''''''''''''''''''''UNIVERSES'''''''''''''''''''''''''''''''
				//'Prod Universes
				//Determine the last time records were inserted
				String maxid = "0";

				maxid = Helper.getMaxID("select max(si_id) maxID from STG1_BO4p_universes");

				//May 8, 2014:  discovered that no records were getting inserted from stage1 to stage3 because it's asking for 
				//any record newer than now.  I have no idea why I would have done this.
				//msSQLInsertString = "INSERT INTO RPT_BO4_UNIVERSES (   CUID, SI_KIND, SI_UPDATE_TS,    SI_NAME, SI_ID, SI_PARENT_FOLDER_CUID,    SI_DESCRIPTION, SI_DATACONNECTION, ENVIRONMENT,    MACHINE_NM, LOAD_DT, PATH,    SI_CREATION_TIME, DATASOURCE_TYPE)  select CUID, SI_KIND, SI_UPDATE_TS,    SI_NAME, SI_ID, SI_PARENT_FOLDER_CUID,    SI_DESCRIPTION, SI_DATACONNECTION, 'Prod',    environment, LOAD_DT, PATH,    SI_CREATION_TIME, DATASOURCE_TYPE from STG1_BO4p_universes where load_dt >= to_date('" + Helper.oracleOnTheHour(now) + "','DD-MM-YYYY HH24:MI:SS')";
				//Now that universes are loaded once a day, I'm not sure that the function OracleOnTheHour() is really need.  I just need to make sure I load only new universes.
				msSQLInsertString = "INSERT INTO RPT_BO4_UNIVERSES (   CUID, SI_KIND, SI_UPDATE_TS,    SI_NAME, SI_ID, SI_PARENT_FOLDER_CUID,    SI_DESCRIPTION, SI_DATACONNECTION, ENVIRONMENT,    MACHINE_NM, LOAD_DT, PATH,    SI_CREATION_TIME, DATASOURCE_TYPE)  select CUID, SI_KIND, SI_UPDATE_TS,    SI_NAME, SI_ID, SI_PARENT_FOLDER_CUID,    SI_DESCRIPTION, SI_DATACONNECTION, 'Prod',    environment, LOAD_DT, PATH,    SI_CREATION_TIME, DATASOURCE_TYPE from STG1_BO4p_universes where si_id > " + maxid;
				System.out.println(msSQLInsertString);
				try {
					//runMSSQLInsertQuery(strSQL)
					Helper.runMSSQLInsertQuery(msSQLInsertString);
				}
				catch (Exception ex){
					System.out.println(ex.getMessage() + " " + ex.getStackTrace());
				}

				//'QA Universes
				maxid = Helper.getMaxID("select max(si_id) maxID from STG1_BO4q_universes");
				//msSQLInsertString = "INSERT INTO RPT_BO4_UNIVERSES (   CUID, SI_KIND, SI_UPDATE_TS,    SI_NAME, SI_ID, SI_PARENT_FOLDER_CUID,    SI_DESCRIPTION, SI_DATACONNECTION, ENVIRONMENT,    MACHINE_NM, LOAD_DT, PATH,    SI_CREATION_TIME, DATASOURCE_TYPE)  select CUID, SI_KIND, SI_UPDATE_TS,    SI_NAME, SI_ID, SI_PARENT_FOLDER_CUID,    SI_DESCRIPTION, SI_DATACONNECTION, 'QA',    environment, LOAD_DT, PATH,    SI_CREATION_TIME, DATASOURCE_TYPE from STG1_BO4q_universes where load_dt >= to_date('" + Helper.oracleOnTheHour(now) + "','DD-MM-YYYY HH24:MI:SS')";
				msSQLInsertString = "INSERT INTO RPT_BO4_UNIVERSES (   CUID, SI_KIND, SI_UPDATE_TS,    SI_NAME, SI_ID, SI_PARENT_FOLDER_CUID,    SI_DESCRIPTION, SI_DATACONNECTION, ENVIRONMENT,    MACHINE_NM, LOAD_DT, PATH,    SI_CREATION_TIME, DATASOURCE_TYPE)  select CUID, SI_KIND, SI_UPDATE_TS,    SI_NAME, SI_ID, SI_PARENT_FOLDER_CUID,    SI_DESCRIPTION, SI_DATACONNECTION, 'Prod',    environment, LOAD_DT, PATH,    SI_CREATION_TIME, DATASOURCE_TYPE from STG1_BO4q_universes where si_id > " + maxid;
				System.out.println(msSQLInsertString);
				try {
					//runMSSQLInsertQuery(strSQL)
					Helper.runMSSQLInsertQuery(msSQLInsertString);
				}
				catch (Exception ex){
					System.out.println(ex.getMessage() + " " + ex.getStackTrace());
				}

				//'ITG Universes
				//msSQLInsertString = "INSERT INTO RPT_BO4_UNIVERSES (   CUID, SI_KIND, SI_UPDATE_TS,    SI_NAME, SI_ID, SI_PARENT_FOLDER_CUID,    SI_DESCRIPTION, SI_DATACONNECTION, ENVIRONMENT,    MACHINE_NM, LOAD_DT, PATH,    SI_CREATION_TIME)  select CUID, SI_KIND, SI_UPDATE_TS,    SI_NAME, SI_ID, SI_PARENT_FOLDER_CUID,    SI_DESCRIPTION, SI_DATACONNECTION, 'ITG',    environment, LOAD_DT, PATH,    SI_CREATION_TIME from STG1_BO4t_universes where load_dt >= to_date('" + Helper.oracleOnTheHour(now) + "','DD-MM-YYYY HH24:MI:SS')";
				//System.out.println(msSQLInsertString);
				//Helper.runMSSQLInsertQuery(msSQLInsertString);

				//'Dev Universes
				maxid = Helper.getMaxID("select max(si_id) maxID from STG1_BO4d_universes");
				//msSQLInsertString = "INSERT INTO RPT_BO4_UNIVERSES (   CUID, SI_KIND, SI_UPDATE_TS,    SI_NAME, SI_ID, SI_PARENT_FOLDER_CUID,    SI_DESCRIPTION, SI_DATACONNECTION, ENVIRONMENT,    MACHINE_NM, LOAD_DT, PATH,    SI_CREATION_TIME, DATASOURCE_TYPE)  select CUID, SI_KIND, SI_UPDATE_TS,    SI_NAME, SI_ID, SI_PARENT_FOLDER_CUID,    SI_DESCRIPTION, SI_DATACONNECTION, 'Dev',    environment, LOAD_DT, PATH,    SI_CREATION_TIME, DATASOURCE_TYPE from STG1_BO4D_universes where load_dt >= to_date('" + Helper.oracleOnTheHour(now) + "','DD-MM-YYYY HH24:MI:SS')";
				msSQLInsertString = "INSERT INTO RPT_BO4_UNIVERSES (   CUID, SI_KIND, SI_UPDATE_TS,    SI_NAME, SI_ID, SI_PARENT_FOLDER_CUID,    SI_DESCRIPTION, SI_DATACONNECTION, ENVIRONMENT,    MACHINE_NM, LOAD_DT, PATH,    SI_CREATION_TIME, DATASOURCE_TYPE)  select CUID, SI_KIND, SI_UPDATE_TS,    SI_NAME, SI_ID, SI_PARENT_FOLDER_CUID,    SI_DESCRIPTION, SI_DATACONNECTION, 'Prod',    environment, LOAD_DT, PATH,    SI_CREATION_TIME, DATASOURCE_TYPE from STG1_BO4d_universes where si_id > " + maxid;
				System.out.println(msSQLInsertString);
				try {
					//runMSSQLInsertQuery(strSQL)
					Helper.runMSSQLInsertQuery(msSQLInsertString);
				}
				catch (Exception ex){
					System.out.println(ex.getMessage() + " " + ex.getStackTrace());
				}
			 */
			//*********************************END TEST************************************

			// '''''''''''''''RECURRING JOBS ''''''''''''''' 9/14/2009 - I'm not sure this is really helpful since I get snapshots of recurring jobs each day in the staging tables.  So I'm commenting out for now.
			//Uncommented because apparently I was doing these snapshots in VB through Jul 3, 2012.

			//Need to ensure these run only once per day

			// ''Val recurring jobs
			//msSQLInsertString = "insert into RPT_BOXI_RECURRSNAPSHOT SELECT REPORT_ID, INSTANCE_ID, SI_STARTTIME,    SI_ENDTIME, SI_SUBMITTER, SI_SERVER,    SI_OWNER, SI_SCHEDULE_STATUS, SI_SCHEDULE_TYPE,    SI_SCHEDULE_INTERVAL_MINUTES, SI_SCHEDULE_INTERVAL_HOURS, SI_SCHEDULE_INTERVAL_MONTHS,    SI_NAME, SI_CREATION_TIME, SI_DESTINATION,    SI_PASSWORD, DATASRC_LOGIN_NM, DATASRC_LOGIN_NM2,    SI_PASSWORD2, SI_NUM_PROMPTS, REPORT_PATH,    LOAD_DT, SNAPSHOT_DT, SI_NEXTRUNTIME,    REPORT_CUID, SI_MACHINE_USED, DISTRIBUTION_LIST 'Prod' FROM STG1_MIGR_XIR2P_RECURRINGJOBS where load_dt >= to_date('" + oracleOnTheHour(Now()) + "','DD-MM-YYYY HH24:MI:SS')";
			msSQLInsertString = "insert into RPT_BO4_RECURRSNAPSHOT (REPORT_ID, INSTANCE_ID, SI_STARTTIME, SI_ENDTIME,SI_SUBMITTER, SI_SERVER, SI_OWNER, SI_SCHEDULE_STATUS, SI_SCHEDULE_TYPE, SI_SCHEDULE_INTERVAL_MINUTES, SI_SCHEDULE_INTERVAL_HOURS, SI_SCHEDULE_INTERVAL_MONTHS, SI_NAME,SI_CREATION_TIME, SI_DESTINATION, SI_PASSWORD, DATASRC_LOGIN_NM, DATASRC_LOGIN_NM2, SI_PASSWORD2, SI_NUM_PROMPTS, REPORT_PATH, LOAD_DT, SNAPSHOT_DT, SI_NEXTRUNTIME, REPORT_CUID, SI_MACHINE_USED, ENVIRONMENT, DISTRIBUTION_LIST , PARAM_VALUES,CUID, SI_FORMAT_EXPORT_ALLPAGES, SI_KIND, SI_SCHEDULE_INTERVAL_DAYS, CALENDAR_ID, INTERVAL_NTH_DAY) SELECT a.REPORT_ID, a.INSTANCE_ID, a.SI_STARTTIME,    a.SI_ENDTIME, a.SI_SUBMITTER, a.SI_SERVER,    a.SI_OWNER, a.SI_SCHEDULE_STATUS, a.SI_SCHEDULE_TYPE,    a.SI_SCHEDULE_INTERVAL_MINUTES, a.SI_SCHEDULE_INTERVAL_HOURS, a.SI_SCHEDULE_INTERVAL_MONTHS,    a.SI_NAME, a.SI_CREATION_TIME, a.SI_DESTINATION,    a.SI_PASSWORD, a.DATASRC_LOGIN_NM, a.DATASRC_LOGIN_NM2,    a.SI_PASSWORD2, a.SI_NUM_PROMPTS, a.REPORT_PATH,    a.LOAD_DT, a.SNAPSHOT_DT, a.SI_NEXTRUNTIME,    a.REPORT_CUID, a.SI_MACHINE_USED, 'Val', a.DISTRIBUTION_LIST ,  a.PARAM_VALUES, a.CUID, a.SI_FORMAT_EXPORT_ALLPAGES, a.SI_KIND, a.SI_SCHEDULE_INTERVAL_DAYS, a.CALENDAR_ID, a.INTERVAL_NTH_DAY FROM STG1_BO4V_RECURRINGJOBS a left outer join RPT_BO4_RECURRSNAPSHOT b on a.instance_id = b.instance_id where b.instance_id is null";
			System.out.println(msSQLInsertString);;
			try {
				//runMSSQLInsertQuery(strSQL)
				Helper.runMSSQLInsertQuery(msSQLInsertString);
			}
			catch (Exception ex){
				System.out.println(ex.getMessage() + " " + ex.getStackTrace());
			}



			// ''Prod recurring jobs
			//msSQLInsertString = "insert into RPT_BOXI_RECURRSNAPSHOT SELECT REPORT_ID, INSTANCE_ID, SI_STARTTIME,    SI_ENDTIME, SI_SUBMITTER, SI_SERVER,    SI_OWNER, SI_SCHEDULE_STATUS, SI_SCHEDULE_TYPE,    SI_SCHEDULE_INTERVAL_MINUTES, SI_SCHEDULE_INTERVAL_HOURS, SI_SCHEDULE_INTERVAL_MONTHS,    SI_NAME, SI_CREATION_TIME, SI_DESTINATION,    SI_PASSWORD, DATASRC_LOGIN_NM, DATASRC_LOGIN_NM2,    SI_PASSWORD2, SI_NUM_PROMPTS, REPORT_PATH,    LOAD_DT, SNAPSHOT_DT, SI_NEXTRUNTIME,    REPORT_CUID, SI_MACHINE_USED, DISTRIBUTION_LIST 'Prod' FROM STG1_MIGR_XIR2P_RECURRINGJOBS where load_dt >= to_date('" + oracleOnTheHour(Now()) + "','DD-MM-YYYY HH24:MI:SS')";
			//msSQLInsertString = "insert into RPT_BO4_RECURRSNAPSHOT SELECT a.REPORT_ID, a.INSTANCE_ID, a.SI_STARTTIME,    a.SI_ENDTIME, a.SI_SUBMITTER, a.SI_SERVER,    a.SI_OWNER, a.SI_SCHEDULE_STATUS, a.SI_SCHEDULE_TYPE,    a.SI_SCHEDULE_INTERVAL_MINUTES, a.SI_SCHEDULE_INTERVAL_HOURS, a.SI_SCHEDULE_INTERVAL_MONTHS,    a.SI_NAME, a.SI_CREATION_TIME, a.SI_DESTINATION,    a.SI_PASSWORD, a.DATASRC_LOGIN_NM, a.DATASRC_LOGIN_NM2,    a.SI_PASSWORD2, a.SI_NUM_PROMPTS, a.REPORT_PATH,    a.LOAD_DT, a.SNAPSHOT_DT, a.SI_NEXTRUNTIME,    a.REPORT_CUID, a.SI_MACHINE_USED, 'Prod' ENVIRONMENT, a.DISTRIBUTION_LIST ,  a.PARAM_VALUES, a.CUID, a.SI_FORMAT_EXPORT_ALLPAGES, a.SI_KIND, a.SI_SCHEDULE_INTERVAL_DAYS, a.CALENDAR_ID, a.INTERVAL_NTH_DAY FROM STG1_BO4P_RECURRINGJOBS a left outer join RPT_BO4_RECURRSNAPSHOT b on a.instance_id = b.instance_id where b.instance_id is null";
			msSQLInsertString = "insert into RPT_BO4_RECURRSNAPSHOT (REPORT_ID, INSTANCE_ID, SI_STARTTIME, SI_ENDTIME,SI_SUBMITTER, SI_SERVER, SI_OWNER, SI_SCHEDULE_STATUS, SI_SCHEDULE_TYPE, SI_SCHEDULE_INTERVAL_MINUTES, SI_SCHEDULE_INTERVAL_HOURS, SI_SCHEDULE_INTERVAL_MONTHS, SI_NAME,SI_CREATION_TIME, SI_DESTINATION, SI_PASSWORD, DATASRC_LOGIN_NM, DATASRC_LOGIN_NM2, SI_PASSWORD2, SI_NUM_PROMPTS, REPORT_PATH, LOAD_DT, SNAPSHOT_DT, SI_NEXTRUNTIME, REPORT_CUID, SI_MACHINE_USED, ENVIRONMENT, DISTRIBUTION_LIST , PARAM_VALUES,CUID, SI_FORMAT_EXPORT_ALLPAGES, SI_KIND, SI_SCHEDULE_INTERVAL_DAYS, CALENDAR_ID, INTERVAL_NTH_DAY) SELECT a.REPORT_ID, a.INSTANCE_ID, a.SI_STARTTIME,    a.SI_ENDTIME, a.SI_SUBMITTER, a.SI_SERVER,    a.SI_OWNER, a.SI_SCHEDULE_STATUS, a.SI_SCHEDULE_TYPE,    a.SI_SCHEDULE_INTERVAL_MINUTES, a.SI_SCHEDULE_INTERVAL_HOURS, a.SI_SCHEDULE_INTERVAL_MONTHS,    a.SI_NAME, a.SI_CREATION_TIME, a.SI_DESTINATION,    a.SI_PASSWORD, a.DATASRC_LOGIN_NM, a.DATASRC_LOGIN_NM2,    a.SI_PASSWORD2, a.SI_NUM_PROMPTS, a.REPORT_PATH,    a.LOAD_DT, a.SNAPSHOT_DT, a.SI_NEXTRUNTIME,    a.REPORT_CUID, a.SI_MACHINE_USED, 'Prod', a.DISTRIBUTION_LIST ,  a.PARAM_VALUES, a.CUID, a.SI_FORMAT_EXPORT_ALLPAGES, a.SI_KIND, a.SI_SCHEDULE_INTERVAL_DAYS, a.CALENDAR_ID, a.INTERVAL_NTH_DAY FROM STG1_BO4P_RECURRINGJOBS a left outer join RPT_BO4_RECURRSNAPSHOT b on a.instance_id = b.instance_id where b.instance_id is null";
			System.out.println(msSQLInsertString);;
			try {
				//runMSSQLInsertQuery(strSQL)
				Helper.runMSSQLInsertQuery(msSQLInsertString);
			}
			catch (Exception ex){
				System.out.println(ex.getMessage() + " " + ex.getStackTrace());
			}

			//''QA recurring jobs
			//msSQLInsertString = "insert into RPT_BOXI_RECURRSNAPSHOT SELECT REPORT_ID, INSTANCE_ID, SI_STARTTIME,    SI_ENDTIME, SI_SUBMITTER, SI_SERVER,    SI_OWNER, SI_SCHEDULE_STATUS, SI_SCHEDULE_TYPE,    SI_SCHEDULE_INTERVAL_MINUTES, SI_SCHEDULE_INTERVAL_HOURS, SI_SCHEDULE_INTERVAL_MONTHS,    SI_NAME, SI_CREATION_TIME, SI_DESTINATION,    SI_PASSWORD, DATASRC_LOGIN_NM, DATASRC_LOGIN_NM2,    SI_PASSWORD2, SI_NUM_PROMPTS, REPORT_PATH,    LOAD_DT, SNAPSHOT_DT, SI_NEXTRUNTIME,    REPORT_CUID, SI_MACHINE_USED, DISTRIBUTION_LIST 'QA' FROM STG1_MIGR_XIR2Q_RECURRINGJOBS where load_dt >= to_date('" + oracleOnTheHour(Now()) + "','DD-MM-YYYY HH24:MI:SS')";
			//msSQLInsertString = "insert into RPT_BO4_RECURRSNAPSHOT SELECT REPORT_ID, INSTANCE_ID, SI_STARTTIME,    SI_ENDTIME, SI_SUBMITTER, SI_SERVER,    SI_OWNER, SI_SCHEDULE_STATUS, SI_SCHEDULE_TYPE,    SI_SCHEDULE_INTERVAL_MINUTES, SI_SCHEDULE_INTERVAL_HOURS, SI_SCHEDULE_INTERVAL_MONTHS,    SI_NAME, SI_CREATION_TIME, SI_DESTINATION,    SI_PASSWORD, DATASRC_LOGIN_NM, DATASRC_LOGIN_NM2,    SI_PASSWORD2, SI_NUM_PROMPTS, REPORT_PATH,    LOAD_DT, SNAPSHOT_DT, SI_NEXTRUNTIME,    REPORT_CUID, SI_MACHINE_USED, 'QA', DISTRIBUTION_LIST , PARAM_VALUES, CUID, SI_FORMAT_EXPORT_ALLPAGES, SI_KIND, SI_SCHEDULE_INTERVAL_DAYS, CALENDAR_ID, INTERVAL_NTH_DAY FROM STG1_BO4Q_RECURRINGJOBS where load_dt >= GETDATE()";
			//msSQLInsertString = "insert into RPT_BO4_RECURRSNAPSHOT SELECT a.REPORT_ID, a.INSTANCE_ID, a.SI_STARTTIME,    a.SI_ENDTIME, a.SI_SUBMITTER, a.SI_SERVER,    a.SI_OWNER, a.SI_SCHEDULE_STATUS, a.SI_SCHEDULE_TYPE,    a.SI_SCHEDULE_INTERVAL_MINUTES, a.SI_SCHEDULE_INTERVAL_HOURS, a.SI_SCHEDULE_INTERVAL_MONTHS,    a.SI_NAME, a.SI_CREATION_TIME, a.SI_DESTINATION,    a.SI_PASSWORD, a.DATASRC_LOGIN_NM, a.DATASRC_LOGIN_NM2,    a.SI_PASSWORD2, a.SI_NUM_PROMPTS, a.REPORT_PATH,    a.LOAD_DT, a.SNAPSHOT_DT, a.SI_NEXTRUNTIME,    a.REPORT_CUID, a.SI_MACHINE_USED, 'QA', a.DISTRIBUTION_LIST ,  a.PARAM_VALUES, a.CUID, a.SI_FORMAT_EXPORT_ALLPAGES, a.SI_KIND, a.SI_SCHEDULE_INTERVAL_DAYS, a.CALENDAR_ID, a.INTERVAL_NTH_DAY FROM STG1_BO4Q_RECURRINGJOBS a left outer join RPT_BO4_RECURRSNAPSHOT b on a.instance_id = b.instance_id where b.instance_id is null";
			msSQLInsertString = "insert into RPT_BO4_RECURRSNAPSHOT (REPORT_ID, INSTANCE_ID, SI_STARTTIME, SI_ENDTIME,SI_SUBMITTER, SI_SERVER, SI_OWNER, SI_SCHEDULE_STATUS, SI_SCHEDULE_TYPE, SI_SCHEDULE_INTERVAL_MINUTES, SI_SCHEDULE_INTERVAL_HOURS, SI_SCHEDULE_INTERVAL_MONTHS, SI_NAME,SI_CREATION_TIME, SI_DESTINATION, SI_PASSWORD, DATASRC_LOGIN_NM, DATASRC_LOGIN_NM2, SI_PASSWORD2, SI_NUM_PROMPTS, REPORT_PATH, LOAD_DT, SNAPSHOT_DT, SI_NEXTRUNTIME, REPORT_CUID, SI_MACHINE_USED, ENVIRONMENT, DISTRIBUTION_LIST , PARAM_VALUES,CUID, SI_FORMAT_EXPORT_ALLPAGES, SI_KIND, SI_SCHEDULE_INTERVAL_DAYS, CALENDAR_ID, INTERVAL_NTH_DAY) SELECT a.REPORT_ID, a.INSTANCE_ID, a.SI_STARTTIME,    a.SI_ENDTIME, a.SI_SUBMITTER, a.SI_SERVER,    a.SI_OWNER, a.SI_SCHEDULE_STATUS, a.SI_SCHEDULE_TYPE,    a.SI_SCHEDULE_INTERVAL_MINUTES, a.SI_SCHEDULE_INTERVAL_HOURS, a.SI_SCHEDULE_INTERVAL_MONTHS,    a.SI_NAME, a.SI_CREATION_TIME, a.SI_DESTINATION,    a.SI_PASSWORD, a.DATASRC_LOGIN_NM, a.DATASRC_LOGIN_NM2,    a.SI_PASSWORD2, a.SI_NUM_PROMPTS, a.REPORT_PATH,    a.LOAD_DT, a.SNAPSHOT_DT, a.SI_NEXTRUNTIME,    a.REPORT_CUID, a.SI_MACHINE_USED, 'QA', a.DISTRIBUTION_LIST ,  a.PARAM_VALUES, a.CUID, a.SI_FORMAT_EXPORT_ALLPAGES, a.SI_KIND, a.SI_SCHEDULE_INTERVAL_DAYS, a.CALENDAR_ID, a.INTERVAL_NTH_DAY FROM STG1_BO4Q_RECURRINGJOBS a left outer join RPT_BO4_RECURRSNAPSHOT b on a.instance_id = b.instance_id where b.instance_id is null";
			System.out.println(msSQLInsertString);;
			try {
				//runMSSQLInsertQuery(strSQL)
				Helper.runMSSQLInsertQuery(msSQLInsertString);
			}
			catch (Exception ex){
				System.out.println(ex.getMessage() + " " + ex.getStackTrace());
			}


			//''Dev recurring jobs
			//'strSQL = "insert into RPT_BOXI_RECURRSNAPSHOT SELECT REPORT_ID, INSTANCE_ID, SI_STARTTIME,    SI_ENDTIME, SI_SUBMITTER, SI_SERVER,    SI_OWNER, SI_SCHEDULE_STATUS, SI_SCHEDULE_TYPE,    SI_SCHEDULE_INTERVAL_MINUTES, SI_SCHEDULE_INTERVAL_HOURS, SI_SCHEDULE_INTERVAL_MONTHS,    SI_NAME, SI_CREATION_TIME, SI_DESTINATION,    SI_PASSWORD, DATASRC_LOGIN_NM, DATASRC_LOGIN_NM2,    SI_PASSWORD2, SI_NUM_PROMPTS, REPORT_PATH,    LOAD_DT, SNAPSHOT_DT, SI_NEXTRUNTIME,    REPORT_CUID, SI_MACHINE_USED, DISTRIBUTION_LIST 'Dev' FROM STG1_MIGR_XIR2D_RECURRINGJOBS where load_dt >= to_date('" & oracleOnTheHour(Now()) & "','DD-MM-YYYY HH24:MI:SS')"
			//msSQLInsertString = "insert into RPT_BO4_RECURRSNAPSHOT SELECT REPORT_ID, INSTANCE_ID, SI_STARTTIME,    SI_ENDTIME, SI_SUBMITTER, SI_SERVER,    SI_OWNER, SI_SCHEDULE_STATUS, SI_SCHEDULE_TYPE,    SI_SCHEDULE_INTERVAL_MINUTES, SI_SCHEDULE_INTERVAL_HOURS, SI_SCHEDULE_INTERVAL_MONTHS,    SI_NAME, SI_CREATION_TIME, SI_DESTINATION,    SI_PASSWORD, DATASRC_LOGIN_NM, DATASRC_LOGIN_NM2,    SI_PASSWORD2, SI_NUM_PROMPTS, REPORT_PATH,    LOAD_DT, SNAPSHOT_DT, SI_NEXTRUNTIME,    REPORT_CUID, SI_MACHINE_USED, 'Dev', DISTRIBUTION_LIST , PARAM_VALUES, CUID, SI_FORMAT_EXPORT_ALLPAGES, SI_KIND, SI_SCHEDULE_INTERVAL_DAYS, CALENDAR_ID, INTERVAL_NTH_DAY FROM STG1_BO4D_RECURRINGJOBS where load_dt >= GETDATE()";
			//msSQLInsertString = "insert into RPT_BO4_RECURRSNAPSHOT SELECT a.REPORT_ID, a.INSTANCE_ID, a.SI_STARTTIME,    a.SI_ENDTIME, a.SI_SUBMITTER, a.SI_SERVER,    a.SI_OWNER, a.SI_SCHEDULE_STATUS, a.SI_SCHEDULE_TYPE,    a.SI_SCHEDULE_INTERVAL_MINUTES, a.SI_SCHEDULE_INTERVAL_HOURS, a.SI_SCHEDULE_INTERVAL_MONTHS,    a.SI_NAME, a.SI_CREATION_TIME, a.SI_DESTINATION,    a.SI_PASSWORD, a.DATASRC_LOGIN_NM, a.DATASRC_LOGIN_NM2,    a.SI_PASSWORD2, a.SI_NUM_PROMPTS, a.REPORT_PATH,    a.LOAD_DT, a.SNAPSHOT_DT, a.SI_NEXTRUNTIME,    a.REPORT_CUID, a.SI_MACHINE_USED, 'Dev', a.DISTRIBUTION_LIST ,  a.PARAM_VALUES, a.CUID, a.SI_FORMAT_EXPORT_ALLPAGES, a.SI_KIND, a.SI_SCHEDULE_INTERVAL_DAYS, a.CALENDAR_ID, a.INTERVAL_NTH_DAY FROM STG1_BO4D_RECURRINGJOBS a left outer join RPT_BO4_RECURRSNAPSHOT b on a.instance_id = b.instance_id where b.instance_id is null";
			msSQLInsertString = "insert into RPT_BO4_RECURRSNAPSHOT (REPORT_ID, INSTANCE_ID, SI_STARTTIME, SI_ENDTIME,SI_SUBMITTER, SI_SERVER, SI_OWNER, SI_SCHEDULE_STATUS, SI_SCHEDULE_TYPE, SI_SCHEDULE_INTERVAL_MINUTES, SI_SCHEDULE_INTERVAL_HOURS, SI_SCHEDULE_INTERVAL_MONTHS, SI_NAME,SI_CREATION_TIME, SI_DESTINATION, SI_PASSWORD, DATASRC_LOGIN_NM, DATASRC_LOGIN_NM2, SI_PASSWORD2, SI_NUM_PROMPTS, REPORT_PATH, LOAD_DT, SNAPSHOT_DT, SI_NEXTRUNTIME, REPORT_CUID, SI_MACHINE_USED, ENVIRONMENT, DISTRIBUTION_LIST , PARAM_VALUES,CUID, SI_FORMAT_EXPORT_ALLPAGES, SI_KIND, SI_SCHEDULE_INTERVAL_DAYS, CALENDAR_ID, INTERVAL_NTH_DAY) SELECT a.REPORT_ID, a.INSTANCE_ID, a.SI_STARTTIME,    a.SI_ENDTIME, a.SI_SUBMITTER, a.SI_SERVER,    a.SI_OWNER, a.SI_SCHEDULE_STATUS, a.SI_SCHEDULE_TYPE,    a.SI_SCHEDULE_INTERVAL_MINUTES, a.SI_SCHEDULE_INTERVAL_HOURS, a.SI_SCHEDULE_INTERVAL_MONTHS,    a.SI_NAME, a.SI_CREATION_TIME, a.SI_DESTINATION,    a.SI_PASSWORD, a.DATASRC_LOGIN_NM, a.DATASRC_LOGIN_NM2,    a.SI_PASSWORD2, a.SI_NUM_PROMPTS, a.REPORT_PATH,    a.LOAD_DT, a.SNAPSHOT_DT, a.SI_NEXTRUNTIME,    a.REPORT_CUID, a.SI_MACHINE_USED, 'Dev', a.DISTRIBUTION_LIST ,  a.PARAM_VALUES, a.CUID, a.SI_FORMAT_EXPORT_ALLPAGES, a.SI_KIND, a.SI_SCHEDULE_INTERVAL_DAYS, a.CALENDAR_ID, a.INTERVAL_NTH_DAY FROM STG1_BO4D_RECURRINGJOBS a left outer join RPT_BO4_RECURRSNAPSHOT b on a.instance_id = b.instance_id where b.instance_id is null";
			System.out.println(msSQLInsertString);;
			try {
				//runMSSQLInsertQuery(strSQL)
				Helper.runMSSQLInsertQuery(msSQLInsertString);
			}
			catch (Exception ex){
				System.out.println(ex.getMessage() + " " + ex.getStackTrace());
			}



			//'//'''''''''''''''RUNTIME HISTORY'''''''''''''''''
			//VAL runtime history
			//strSQL = "insert into RPT_BOXI_RUNHIST SELECT REPORT_ID, INSTANCE_ID, SI_STARTTIME,    SI_ENDTIME, DURATION, SI_SUBMITTER,    SI_SERVER, SI_OWNER, SI_SCHEDULE_STATUS,    SI_ERROR_MESSAGE, LOAD_DT, REPORT_PATH,    SI_NAME, SI_CREATION_TIME, SI_PASSWORD,    DATASRC_LOGIN_NM, DATASRC_LOGIN_NM2, SI_PASSWORD2,    SI_NUM_PROMPTS, SI_MACHINE_USED, SI_UPDATE_TS,    DESTINATION, OUTPUT_FILE, REPORT_CUID,    SI_PARENT_FOLDER_CUID, DISTRIBUTION_LIST, SI_FILE1, 'QA' FROM STG1_MIGR_Q_RUNTIMEIMAGE where load_dt >= to_date('" & oracleOnTheHour(Now()) & "','DD-MM-YYYY HH24:MI:SS')"
			//'strSQL = "insert into RPT_BO4_RUNHIST (REPORT_ID, INSTANCE_ID, SI_STARTTIME,    SI_ENDTIME, DURATION, SI_SUBMITTER,    SI_SERVER, SI_OWNER, SI_SCHEDULE_STATUS,    SI_ERROR_MESSAGE, LOAD_DT, REPORT_PATH,    SI_NAME, SI_CREATION_TIME, SI_PASSWORD,    DATASRC_LOGIN_NM, DATASRC_LOGIN_NM2, SI_PASSWORD2,    SI_NUM_PROMPTS, SI_MACHINE_USED, SI_UPDATE_TS,    DESTINATION, OUTPUT_FILE, REPORT_CUID,    SI_PARENT_FOLDER_CUID, DISTRIBUTION_LIST, SI_FILE1, ENVIRONMENT,  SI_KIND,  PARAM_VALUES, ERROR_CATEGORY, INSTANCE_CREATE_DATE, START_DATE, END_DATE, UPDATE_DATE, LOAD_DATE) SELECT REPORT_ID, INSTANCE_ID, SI_STARTTIME,    SI_ENDTIME, DURATION, SI_SUBMITTER,    SI_SERVER, SI_OWNER, SI_SCHEDULE_STATUS,    SI_ERROR_MESSAGE, LOAD_DT, REPORT_PATH,    SI_NAME, SI_CREATION_TIME, SI_PASSWORD,    DATASRC_LOGIN_NM, DATASRC_LOGIN_NM2, SI_PASSWORD2,    SI_NUM_PROMPTS, SI_MACHINE_USED, SI_UPDATE_TS,    DESTINATION, OUTPUT_FILE, REPORT_CUID,    SI_PARENT_FOLDER_CUID, DISTRIBUTION_LIST, SI_FILE1, 'QA', SI_KIND,  PARAM_VALUES, ERROR_CATEGORY, trunc(SI_CREATION_TIME), trunc(SI_STARTTIME), trunc(SI_ENDTIME), trunc(SI_UPDATE_TS), trunc(LOAD_DT)  FROM STG1_BO4Q_RUNHIST where load_dt >= to_date('" & oracleOnTheHour(Now()) & "','DD-MM-YYYY HH24:MI:SS')"
			//msSQLInsertString = "insert into RPT_BO4_RUNHIST (REPORT_ID, INSTANCE_ID, SI_STARTTIME,    SI_ENDTIME, DURATION, SI_SUBMITTER,    SI_SERVER, SI_OWNER, SI_SCHEDULE_STATUS,    SI_ERROR_MESSAGE, LOAD_DT, REPORT_PATH,    SI_NAME, SI_CREATION_TIME, SI_PASSWORD,    DATASRC_LOGIN_NM, DATASRC_LOGIN_NM2, SI_PASSWORD2,    SI_NUM_PROMPTS, SI_MACHINE_USED, SI_UPDATE_TS,    DESTINATION, OUTPUT_FILE, REPORT_CUID,    SI_PARENT_FOLDER_CUID, DISTRIBUTION_LIST, SI_FILE1, ENVIRONMENT,  SI_KIND,  PARAM_VALUES, ERROR_CATEGORY, INSTANCE_CREATE_DATE, START_DATE, END_DATE, UPDATE_DATE, LOAD_DATE, EXPIRATION_TIME) SELECT prh.REPORT_ID, prh.INSTANCE_ID, prh.SI_STARTTIME,    prh.SI_ENDTIME, prh.DURATION, prh.SI_SUBMITTER,    prh.SI_SERVER, prh.SI_OWNER, prh.SI_SCHEDULE_STATUS,    prh.SI_ERROR_MESSAGE, prh.LOAD_DT, prh.REPORT_PATH,    prh.SI_NAME, prh.SI_CREATION_TIME, prh.SI_PASSWORD,    prh.DATASRC_LOGIN_NM, prh.DATASRC_LOGIN_NM2, prh.SI_PASSWORD2,    prh.SI_NUM_PROMPTS, prh.SI_MACHINE_USED, prh.SI_UPDATE_TS,    prh.DESTINATION, prh.OUTPUT_FILE, prh.REPORT_CUID,    prh.SI_PARENT_FOLDER_CUID, prh.DISTRIBUTION_LIST, prh.SI_FILE1, 'Val', prh.SI_KIND,  substring(prh.PARAM_VALUES, 1, 2970), prh.ERROR_CATEGORY, trunc(prh.SI_CREATION_TIME), trunc(prh.SI_STARTTIME), trunc(prh.SI_ENDTIME), trunc(prh.SI_UPDATE_TS), trunc(prh.LOAD_DT), prh.EXPIRATION_TIME  FROM STG1_BO4V_RUNHIST prh left outer join RPT_BO4_RUNHIST RH on prh.instance_id = rh.instance_id where rh.instance_id is null";
			msSQLInsertString = "insert into RPT_BO4_RUNHIST (REPORT_ID, INSTANCE_ID, SI_STARTTIME,    SI_ENDTIME, DURATION, SI_SUBMITTER,    SI_SERVER, SI_OWNER, SI_SCHEDULE_STATUS,    SI_ERROR_MESSAGE, LOAD_DT, REPORT_PATH,    SI_NAME, SI_CREATION_TIME, SI_PASSWORD,    DATASRC_LOGIN_NM, DATASRC_LOGIN_NM2, SI_PASSWORD2,    SI_NUM_PROMPTS, SI_MACHINE_USED, SI_UPDATE_TS,    DESTINATION, OUTPUT_FILE, REPORT_CUID,    SI_PARENT_FOLDER_CUID, DISTRIBUTION_LIST, SI_FILE1, ENVIRONMENT,  SI_KIND,  PARAM_VALUES, ERROR_CATEGORY, INSTANCE_CREATE_DATE, START_DATE, END_DATE, UPDATE_DATE, LOAD_DATE, EXPIRATION_TIME) SELECT prh.REPORT_ID, prh.INSTANCE_ID, prh.SI_STARTTIME,    prh.SI_ENDTIME, prh.DURATION, prh.SI_SUBMITTER,    prh.SI_SERVER, prh.SI_OWNER, prh.SI_SCHEDULE_STATUS,    prh.SI_ERROR_MESSAGE, prh.LOAD_DT, prh.REPORT_PATH,    prh.SI_NAME, prh.SI_CREATION_TIME, prh.SI_PASSWORD,    prh.DATASRC_LOGIN_NM, prh.DATASRC_LOGIN_NM2, prh.SI_PASSWORD2,    prh.SI_NUM_PROMPTS, prh.SI_MACHINE_USED, prh.SI_UPDATE_TS,    prh.DESTINATION, prh.OUTPUT_FILE, prh.REPORT_CUID,    prh.SI_PARENT_FOLDER_CUID, prh.DISTRIBUTION_LIST, prh.SI_FILE1, 'Val', prh.SI_KIND,  substring(prh.PARAM_VALUES, 1, 2970), prh.ERROR_CATEGORY, CONVERT(DATETIME, CONVERT(DATE, prh.SI_CREATION_TIME)), CONVERT(DATETIME, CONVERT(DATE, prh.SI_STARTTIME)), CONVERT(DATETIME, CONVERT(DATE, prh.SI_ENDTIME)), CONVERT(DATETIME, CONVERT(DATE, prh.SI_UPDATE_TS)), CONVERT(DATETIME, CONVERT(DATE, prh.LOAD_DT)), prh.EXPIRATION_TIME  FROM STG1_BO4V_RUNHIST prh left outer join RPT_BO4_RUNHIST RH on prh.instance_id = rh.instance_id where rh.instance_id is null";

			System.out.println(msSQLInsertString);;
			try {
				//runMSSQLInsertQuery(strSQL)
				Helper.runMSSQLInsertQuery(msSQLInsertString);
			}
			catch (Exception ex){
				System.out.println(ex.getMessage() + " " + ex.getStackTrace());
			}

			//'Prod runtime history
			//strSQL = "insert into RPT_BOXI_RUNHIST SELECT REPORT_ID, INSTANCE_ID, SI_STARTTIME,    SI_ENDTIME, DURATION, SI_SUBMITTER,    SI_SERVER, SI_OWNER, SI_SCHEDULE_STATUS,    SI_ERROR_MESSAGE, LOAD_DT, REPORT_PATH,    SI_NAME, SI_CREATION_TIME, SI_PASSWORD,    DATASRC_LOGIN_NM, DATASRC_LOGIN_NM2, SI_PASSWORD2,    SI_NUM_PROMPTS, SI_MACHINE_USED, SI_UPDATE_TS,    DESTINATION, OUTPUT_FILE, REPORT_CUID,    SI_PARENT_FOLDER_CUID, DISTRIBUTION_LIST, SI_FILE1, 'Prod' FROM STG1_MIGR_P_RUNTIMEIMAGE where load_dt >= to_date('" & oracleOnTheHour(Now()) & "','DD-MM-YYYY HH24:MI:SS')"
			//strSQL = "insert into RPT_BOXI3_RUNHIST (REPORT_ID, INSTANCE_ID, SI_STARTTIME,    SI_ENDTIME, DURATION, SI_SUBMITTER,    SI_SERVER, SI_OWNER, SI_SCHEDULE_STATUS,    SI_ERROR_MESSAGE, LOAD_DT, REPORT_PATH,    SI_NAME, SI_CREATION_TIME, SI_PASSWORD,    DATASRC_LOGIN_NM, DATASRC_LOGIN_NM2, SI_PASSWORD2,    SI_NUM_PROMPTS, SI_MACHINE_USED, SI_UPDATE_TS,    DESTINATION, OUTPUT_FILE, REPORT_CUID,    SI_PARENT_FOLDER_CUID, DISTRIBUTION_LIST, SI_FILE1, ENVIRONMENT, SI_KIND,  PARAM_VALUES, ERROR_CATEGORY, CREATE_DATE, START_DATE, END_DATE, UPDATE_DATE, LOAD_DATE) SELECT REPORT_ID, INSTANCE_ID, SI_STARTTIME,    SI_ENDTIME, DURATION, SI_SUBMITTER,    SI_SERVER, SI_OWNER, SI_SCHEDULE_STATUS,    SI_ERROR_MESSAGE, LOAD_DT, REPORT_PATH,    SI_NAME, SI_CREATION_TIME, SI_PASSWORD,    DATASRC_LOGIN_NM, DATASRC_LOGIN_NM2, SI_PASSWORD2,    SI_NUM_PROMPTS, SI_MACHINE_USED, SI_UPDATE_TS,    DESTINATION, OUTPUT_FILE, REPORT_CUID,    SI_PARENT_FOLDER_CUID, DISTRIBUTION_LIST, SI_FILE1, 'Prod',  SI_KIND, ENVIRONMENT, PARAM_VALUES, ERROR_CATEGORY, trunc(SI_CREATION_TIME), trunc(SI_STARTTIME), trunc(SI_ENDTIME), trunc(SI_UPDATE_TS), trunc(LOAD_DT)  FROM STG1_BOXI3P_RUNHIST where load_dt >= to_date('" & oracleOnTheHour(Now()) & "','DD-MM-YYYY HH24:MI:SS')"

			//msSQLInsertString = "insert into RPT_BO4_RUNHIST (REPORT_ID, INSTANCE_ID, SI_STARTTIME,    SI_ENDTIME, DURATION, SI_SUBMITTER,    SI_SERVER, SI_OWNER, SI_SCHEDULE_STATUS,    SI_ERROR_MESSAGE, LOAD_DT, REPORT_PATH,    SI_NAME, SI_CREATION_TIME, SI_PASSWORD,    DATASRC_LOGIN_NM, DATASRC_LOGIN_NM2, SI_PASSWORD2,    SI_NUM_PROMPTS, SI_MACHINE_USED, SI_UPDATE_TS,    DESTINATION, OUTPUT_FILE, REPORT_CUID,    SI_PARENT_FOLDER_CUID, DISTRIBUTION_LIST, SI_FILE1, ENVIRONMENT,  SI_KIND,  PARAM_VALUES, ERROR_CATEGORY, INSTANCE_CREATE_DATE, START_DATE, END_DATE, UPDATE_DATE, LOAD_DATE, EXPIRATION_TIME) SELECT prh.REPORT_ID, prh.INSTANCE_ID, prh.SI_STARTTIME,    prh.SI_ENDTIME, prh.DURATION, prh.SI_SUBMITTER,    prh.SI_SERVER, prh.SI_OWNER, prh.SI_SCHEDULE_STATUS,    prh.SI_ERROR_MESSAGE, prh.LOAD_DT, prh.REPORT_PATH,    prh.SI_NAME, prh.SI_CREATION_TIME, prh.SI_PASSWORD,    prh.DATASRC_LOGIN_NM, prh.DATASRC_LOGIN_NM2, prh.SI_PASSWORD2,    prh.SI_NUM_PROMPTS, prh.SI_MACHINE_USED, prh.SI_UPDATE_TS,    prh.DESTINATION, prh.OUTPUT_FILE, prh.REPORT_CUID,    prh.SI_PARENT_FOLDER_CUID, prh.DISTRIBUTION_LIST, prh.SI_FILE1, 'Prod', prh.SI_KIND,  substring(prh.PARAM_VALUES, 1, 2970), prh.ERROR_CATEGORY, trunc(prh.SI_CREATION_TIME), trunc(prh.SI_STARTTIME), trunc(prh.SI_ENDTIME), trunc(prh.SI_UPDATE_TS), trunc(prh.LOAD_DT), prh.EXPIRATION_TIME  FROM STG1_BO4P_RUNHIST prh left outer join RPT_BO4_RUNHIST RH on prh.instance_id = rh.instance_id where rh.instance_id is null";
			msSQLInsertString = "insert into RPT_BO4_RUNHIST (REPORT_ID, INSTANCE_ID, SI_STARTTIME,    SI_ENDTIME, DURATION, SI_SUBMITTER,    SI_SERVER, SI_OWNER, SI_SCHEDULE_STATUS,    SI_ERROR_MESSAGE, LOAD_DT, REPORT_PATH,    SI_NAME, SI_CREATION_TIME, SI_PASSWORD,    DATASRC_LOGIN_NM, DATASRC_LOGIN_NM2, SI_PASSWORD2,    SI_NUM_PROMPTS, SI_MACHINE_USED, SI_UPDATE_TS,    DESTINATION, OUTPUT_FILE, REPORT_CUID,    SI_PARENT_FOLDER_CUID, DISTRIBUTION_LIST, SI_FILE1, ENVIRONMENT,  SI_KIND,  PARAM_VALUES, ERROR_CATEGORY, INSTANCE_CREATE_DATE, START_DATE, END_DATE, UPDATE_DATE, LOAD_DATE, EXPIRATION_TIME) SELECT prh.REPORT_ID, prh.INSTANCE_ID, prh.SI_STARTTIME,    prh.SI_ENDTIME, prh.DURATION, prh.SI_SUBMITTER,    prh.SI_SERVER, prh.SI_OWNER, prh.SI_SCHEDULE_STATUS,    prh.SI_ERROR_MESSAGE, prh.LOAD_DT, prh.REPORT_PATH,    prh.SI_NAME, prh.SI_CREATION_TIME, prh.SI_PASSWORD,    prh.DATASRC_LOGIN_NM, prh.DATASRC_LOGIN_NM2, prh.SI_PASSWORD2,    prh.SI_NUM_PROMPTS, prh.SI_MACHINE_USED, prh.SI_UPDATE_TS,    prh.DESTINATION, prh.OUTPUT_FILE, prh.REPORT_CUID,    prh.SI_PARENT_FOLDER_CUID, prh.DISTRIBUTION_LIST, prh.SI_FILE1, 'Prod' ENVIRONMENT, prh.SI_KIND,  substring(prh.PARAM_VALUES, 1, 2970), prh.ERROR_CATEGORY, CONVERT(DATETIME, CONVERT(DATE, prh.SI_CREATION_TIME)),CONVERT(DATETIME, CONVERT(DATE, prh.SI_STARTTIME)), CONVERT(DATETIME, CONVERT(DATE, prh.SI_ENDTIME)), CONVERT(DATETIME, CONVERT(DATE, prh.SI_UPDATE_TS)), CONVERT(DATETIME, CONVERT(DATE, prh.LOAD_DT)), prh.EXPIRATION_TIME  FROM STG1_BO4P_RUNHIST prh left outer join RPT_BO4_RUNHIST RH on prh.instance_id = rh.instance_id where rh.instance_id is null";

			try {
				//runMSSQLInsertQuery(strSQL)
				Helper.runMSSQLInsertQuery(msSQLInsertString);
			}
			catch (Exception ex){
				System.out.println(ex.getMessage() + " " + ex.getStackTrace());
			}


			//QA runtime history
			//strSQL = "insert into RPT_BOXI_RUNHIST SELECT REPORT_ID, INSTANCE_ID, SI_STARTTIME,    SI_ENDTIME, DURATION, SI_SUBMITTER,    SI_SERVER, SI_OWNER, SI_SCHEDULE_STATUS,    SI_ERROR_MESSAGE, LOAD_DT, REPORT_PATH,    SI_NAME, SI_CREATION_TIME, SI_PASSWORD,    DATASRC_LOGIN_NM, DATASRC_LOGIN_NM2, SI_PASSWORD2,    SI_NUM_PROMPTS, SI_MACHINE_USED, SI_UPDATE_TS,    DESTINATION, OUTPUT_FILE, REPORT_CUID,    SI_PARENT_FOLDER_CUID, DISTRIBUTION_LIST, SI_FILE1, 'QA' FROM STG1_MIGR_Q_RUNTIMEIMAGE where load_dt >= to_date('" & oracleOnTheHour(Now()) & "','DD-MM-YYYY HH24:MI:SS')"
			//'strSQL = "insert into RPT_BO4_RUNHIST (REPORT_ID, INSTANCE_ID, SI_STARTTIME,    SI_ENDTIME, DURATION, SI_SUBMITTER,    SI_SERVER, SI_OWNER, SI_SCHEDULE_STATUS,    SI_ERROR_MESSAGE, LOAD_DT, REPORT_PATH,    SI_NAME, SI_CREATION_TIME, SI_PASSWORD,    DATASRC_LOGIN_NM, DATASRC_LOGIN_NM2, SI_PASSWORD2,    SI_NUM_PROMPTS, SI_MACHINE_USED, SI_UPDATE_TS,    DESTINATION, OUTPUT_FILE, REPORT_CUID,    SI_PARENT_FOLDER_CUID, DISTRIBUTION_LIST, SI_FILE1, ENVIRONMENT,  SI_KIND,  PARAM_VALUES, ERROR_CATEGORY, INSTANCE_CREATE_DATE, START_DATE, END_DATE, UPDATE_DATE, LOAD_DATE) SELECT REPORT_ID, INSTANCE_ID, SI_STARTTIME,    SI_ENDTIME, DURATION, SI_SUBMITTER,    SI_SERVER, SI_OWNER, SI_SCHEDULE_STATUS,    SI_ERROR_MESSAGE, LOAD_DT, REPORT_PATH,    SI_NAME, SI_CREATION_TIME, SI_PASSWORD,    DATASRC_LOGIN_NM, DATASRC_LOGIN_NM2, SI_PASSWORD2,    SI_NUM_PROMPTS, SI_MACHINE_USED, SI_UPDATE_TS,    DESTINATION, OUTPUT_FILE, REPORT_CUID,    SI_PARENT_FOLDER_CUID, DISTRIBUTION_LIST, SI_FILE1, 'QA', SI_KIND,  PARAM_VALUES, ERROR_CATEGORY, trunc(SI_CREATION_TIME), trunc(SI_STARTTIME), trunc(SI_ENDTIME), trunc(SI_UPDATE_TS), trunc(LOAD_DT)  FROM STG1_BO4Q_RUNHIST where load_dt >= to_date('" & oracleOnTheHour(Now()) & "','DD-MM-YYYY HH24:MI:SS')"
			//msSQLInsertString = "insert into RPT_BO4_RUNHIST (REPORT_ID, INSTANCE_ID, SI_STARTTIME,    SI_ENDTIME, DURATION, SI_SUBMITTER,    SI_SERVER, SI_OWNER, SI_SCHEDULE_STATUS,    SI_ERROR_MESSAGE, LOAD_DT, REPORT_PATH,    SI_NAME, SI_CREATION_TIME, SI_PASSWORD,    DATASRC_LOGIN_NM, DATASRC_LOGIN_NM2, SI_PASSWORD2,    SI_NUM_PROMPTS, SI_MACHINE_USED, SI_UPDATE_TS,    DESTINATION, OUTPUT_FILE, REPORT_CUID,    SI_PARENT_FOLDER_CUID, DISTRIBUTION_LIST, SI_FILE1, ENVIRONMENT,  SI_KIND,  PARAM_VALUES, ERROR_CATEGORY, INSTANCE_CREATE_DATE, START_DATE, END_DATE, UPDATE_DATE, LOAD_DATE, EXPIRATION_TIME) SELECT prh.REPORT_ID, prh.INSTANCE_ID, prh.SI_STARTTIME,    prh.SI_ENDTIME, prh.DURATION, prh.SI_SUBMITTER,    prh.SI_SERVER, prh.SI_OWNER, prh.SI_SCHEDULE_STATUS,    prh.SI_ERROR_MESSAGE, prh.LOAD_DT, prh.REPORT_PATH,    prh.SI_NAME, prh.SI_CREATION_TIME, prh.SI_PASSWORD,    prh.DATASRC_LOGIN_NM, prh.DATASRC_LOGIN_NM2, prh.SI_PASSWORD2,    prh.SI_NUM_PROMPTS, prh.SI_MACHINE_USED, prh.SI_UPDATE_TS,    prh.DESTINATION, prh.OUTPUT_FILE, prh.REPORT_CUID,    prh.SI_PARENT_FOLDER_CUID, prh.DISTRIBUTION_LIST, prh.SI_FILE1, 'QA', prh.SI_KIND,  substring(prh.PARAM_VALUES, 1, 2970), prh.ERROR_CATEGORY, trunc(prh.SI_CREATION_TIME), trunc(prh.SI_STARTTIME), trunc(prh.SI_ENDTIME), trunc(prh.SI_UPDATE_TS), trunc(prh.LOAD_DT), prh.EXPIRATION_TIME  FROM STG1_BO4Q_RUNHIST prh left outer join RPT_BO4_RUNHIST RH on prh.instance_id = rh.instance_id where rh.instance_id is null";
			msSQLInsertString = "insert into RPT_BO4_RUNHIST (REPORT_ID, INSTANCE_ID, SI_STARTTIME,    SI_ENDTIME, DURATION, SI_SUBMITTER,    SI_SERVER, SI_OWNER, SI_SCHEDULE_STATUS,    SI_ERROR_MESSAGE, LOAD_DT, REPORT_PATH,    SI_NAME, SI_CREATION_TIME, SI_PASSWORD,    DATASRC_LOGIN_NM, DATASRC_LOGIN_NM2, SI_PASSWORD2,    SI_NUM_PROMPTS, SI_MACHINE_USED, SI_UPDATE_TS,    DESTINATION, OUTPUT_FILE, REPORT_CUID,    SI_PARENT_FOLDER_CUID, DISTRIBUTION_LIST, SI_FILE1, ENVIRONMENT,  SI_KIND,  PARAM_VALUES, ERROR_CATEGORY, INSTANCE_CREATE_DATE, START_DATE, END_DATE, UPDATE_DATE, LOAD_DATE, EXPIRATION_TIME) SELECT prh.REPORT_ID, prh.INSTANCE_ID, prh.SI_STARTTIME,    prh.SI_ENDTIME, prh.DURATION, prh.SI_SUBMITTER,    prh.SI_SERVER, prh.SI_OWNER, prh.SI_SCHEDULE_STATUS,    prh.SI_ERROR_MESSAGE, prh.LOAD_DT, prh.REPORT_PATH,    prh.SI_NAME, prh.SI_CREATION_TIME, prh.SI_PASSWORD,    prh.DATASRC_LOGIN_NM, prh.DATASRC_LOGIN_NM2, prh.SI_PASSWORD2,    prh.SI_NUM_PROMPTS, prh.SI_MACHINE_USED, prh.SI_UPDATE_TS,    prh.DESTINATION, prh.OUTPUT_FILE, prh.REPORT_CUID,    prh.SI_PARENT_FOLDER_CUID, prh.DISTRIBUTION_LIST, prh.SI_FILE1, 'QA', prh.SI_KIND,  substring(prh.PARAM_VALUES, 1, 2970), prh.ERROR_CATEGORY, CONVERT(DATETIME, CONVERT(DATE, prh.SI_CREATION_TIME)), CONVERT(DATETIME, CONVERT(DATE, prh.SI_STARTTIME)), CONVERT(DATETIME, CONVERT(DATE, prh.SI_ENDTIME)), CONVERT(DATETIME, CONVERT(DATE, prh.SI_UPDATE_TS)), CONVERT(DATETIME, CONVERT(DATE, prh.LOAD_DT)), prh.EXPIRATION_TIME  FROM STG1_BO4Q_RUNHIST prh left outer join RPT_BO4_RUNHIST RH  on prh.instance_id = rh.instance_id where rh.instance_id is null";
			System.out.println(msSQLInsertString);
			try {
				//runMSSQLInsertQuery(strSQL)
				Helper.runMSSQLInsertQuery(msSQLInsertString);
			}
			catch (Exception ex){
				System.out.println(ex.getMessage() + " " + ex.getStackTrace());
			}


			// 'Dev runtime history
			// 'strSQL = "insert into RPT_BOXI_RUNHIST SELECT REPORT_ID, INSTANCE_ID, SI_STARTTIME,    SI_ENDTIME, DURATION, SI_SUBMITTER,    SI_SERVER, SI_OWNER, SI_SCHEDULE_STATUS,    SI_ERROR_MESSAGE, LOAD_DT, REPORT_PATH,    SI_NAME, SI_CREATION_TIME, SI_PASSWORD,    DATASRC_LOGIN_NM, DATASRC_LOGIN_NM2, SI_PASSWORD2,    SI_NUM_PROMPTS, SI_MACHINE_USED, SI_UPDATE_TS,    DESTINATION, OUTPUT_FILE, REPORT_CUID,    SI_PARENT_FOLDER_CUID, DISTRIBUTION_LIST, SI_FILE1, 'Dev' FROM STG1_MIGR_D_RUNTIMEIMAGE where load_dt >= to_date('" & oracleOnTheHour(Now()) & "','DD-MM-YYYY HH24:MI:SS')"
			// 'strSQL = "insert into RPT_BO4_RUNHIST (REPORT_ID, INSTANCE_ID, SI_STARTTIME,    SI_ENDTIME, DURATION, SI_SUBMITTER,    SI_SERVER, SI_OWNER, SI_SCHEDULE_STATUS,    SI_ERROR_MESSAGE, LOAD_DT, REPORT_PATH,    SI_NAME, SI_CREATION_TIME, SI_PASSWORD,    DATASRC_LOGIN_NM, DATASRC_LOGIN_NM2, SI_PASSWORD2,    SI_NUM_PROMPTS, SI_MACHINE_USED, SI_UPDATE_TS,    DESTINATION, OUTPUT_FILE, REPORT_CUID,    SI_PARENT_FOLDER_CUID, DISTRIBUTION_LIST, SI_FILE1,ENVIRONMENT, SI_KIND,  PARAM_VALUES, ERROR_CATEGORY, INSTANCE_CREATE_DATE, START_DATE, END_DATE, UPDATE_DATE, LOAD_DATE) SELECT REPORT_ID, INSTANCE_ID, SI_STARTTIME,    SI_ENDTIME, DURATION, SI_SUBMITTER,    SI_SERVER, SI_OWNER, SI_SCHEDULE_STATUS,    SI_ERROR_MESSAGE, LOAD_DT, REPORT_PATH,    SI_NAME, SI_CREATION_TIME, SI_PASSWORD,    DATASRC_LOGIN_NM, DATASRC_LOGIN_NM2, SI_PASSWORD2,    SI_NUM_PROMPTS, SI_MACHINE_USED, SI_UPDATE_TS,    DESTINATION, OUTPUT_FILE, REPORT_CUID,    SI_PARENT_FOLDER_CUID, DISTRIBUTION_LIST, SI_FILE1, 'Dev', SI_KIND,  PARAM_VALUES, ERROR_CATEGORY, trunc(SI_CREATION_TIME), trunc(SI_STARTTIME), trunc(SI_ENDTIME), trunc(SI_UPDATE_TS), trunc(LOAD_DT)  FROM STG1_BO4T_RUNHIST where load_dt >= to_date('" & oracleOnTheHour(Now()) & "','DD-MM-YYYY HH24:MI:SS')"
			//msSQLInsertString = "insert into RPT_BO4_RUNHIST (REPORT_ID, INSTANCE_ID, SI_STARTTIME,    SI_ENDTIME, DURATION, SI_SUBMITTER,    SI_SERVER, SI_OWNER, SI_SCHEDULE_STATUS,    SI_ERROR_MESSAGE, LOAD_DT, REPORT_PATH,    SI_NAME, SI_CREATION_TIME, SI_PASSWORD,    DATASRC_LOGIN_NM, DATASRC_LOGIN_NM2, SI_PASSWORD2,    SI_NUM_PROMPTS, SI_MACHINE_USED, SI_UPDATE_TS,    DESTINATION, OUTPUT_FILE, REPORT_CUID,    SI_PARENT_FOLDER_CUID, DISTRIBUTION_LIST, SI_FILE1, ENVIRONMENT,  SI_KIND,  PARAM_VALUES, ERROR_CATEGORY, INSTANCE_CREATE_DATE, START_DATE, END_DATE, UPDATE_DATE, LOAD_DATE, EXPIRATION_TIME) SELECT prh.REPORT_ID, prh.INSTANCE_ID, prh.SI_STARTTIME,    prh.SI_ENDTIME, prh.DURATION, prh.SI_SUBMITTER,    prh.SI_SERVER, prh.SI_OWNER, prh.SI_SCHEDULE_STATUS,    prh.SI_ERROR_MESSAGE, prh.LOAD_DT, prh.REPORT_PATH,    prh.SI_NAME, prh.SI_CREATION_TIME, prh.SI_PASSWORD,    prh.DATASRC_LOGIN_NM, prh.DATASRC_LOGIN_NM2, prh.SI_PASSWORD2,    prh.SI_NUM_PROMPTS, prh.SI_MACHINE_USED, prh.SI_UPDATE_TS,    prh.DESTINATION, prh.OUTPUT_FILE, prh.REPORT_CUID,    prh.SI_PARENT_FOLDER_CUID, prh.DISTRIBUTION_LIST, prh.SI_FILE1, 'Dev', prh.SI_KIND,  prh.PARAM_VALUES, prh.ERROR_CATEGORY, trunc(prh.SI_CREATION_TIME), trunc(prh.SI_STARTTIME), trunc(prh.SI_ENDTIME), trunc(prh.SI_UPDATE_TS), trunc(prh.LOAD_DT), prh.EXPIRATION_TIME  FROM STG1_BO4D_RUNHIST prh left outer join RPT_BO4_RUNHIST RH on prh.instance_id = rh.instance_id where rh.instance_id is null";
			msSQLInsertString = "insert into RPT_BO4_RUNHIST (REPORT_ID, INSTANCE_ID, SI_STARTTIME,    SI_ENDTIME, DURATION, SI_SUBMITTER,    SI_SERVER, SI_OWNER, SI_SCHEDULE_STATUS,    SI_ERROR_MESSAGE, LOAD_DT, REPORT_PATH,    SI_NAME, SI_CREATION_TIME, SI_PASSWORD,    DATASRC_LOGIN_NM, DATASRC_LOGIN_NM2, SI_PASSWORD2,    SI_NUM_PROMPTS, SI_MACHINE_USED, SI_UPDATE_TS,    DESTINATION, OUTPUT_FILE, REPORT_CUID,    SI_PARENT_FOLDER_CUID, DISTRIBUTION_LIST, SI_FILE1, ENVIRONMENT,  SI_KIND,  PARAM_VALUES, ERROR_CATEGORY, INSTANCE_CREATE_DATE, START_DATE, END_DATE, UPDATE_DATE, LOAD_DATE, EXPIRATION_TIME) SELECT prh.REPORT_ID, prh.INSTANCE_ID, prh.SI_STARTTIME,    prh.SI_ENDTIME, prh.DURATION, prh.SI_SUBMITTER,    prh.SI_SERVER, prh.SI_OWNER, prh.SI_SCHEDULE_STATUS,    prh.SI_ERROR_MESSAGE, prh.LOAD_DT, prh.REPORT_PATH,    prh.SI_NAME, prh.SI_CREATION_TIME, prh.SI_PASSWORD,    prh.DATASRC_LOGIN_NM, prh.DATASRC_LOGIN_NM2, prh.SI_PASSWORD2,    prh.SI_NUM_PROMPTS, prh.SI_MACHINE_USED, prh.SI_UPDATE_TS,    prh.DESTINATION, prh.OUTPUT_FILE, prh.REPORT_CUID,    prh.SI_PARENT_FOLDER_CUID, prh.DISTRIBUTION_LIST, prh.SI_FILE1, 'Dev', prh.SI_KIND,  substring(prh.PARAM_VALUES, 1, 2970), prh.ERROR_CATEGORY, CONVERT(DATETIME, CONVERT(DATE, prh.SI_CREATION_TIME)), CONVERT(DATETIME, CONVERT(DATE, prh.SI_STARTTIME)), CONVERT(DATETIME, CONVERT(DATE, prh.SI_ENDTIME)), CONVERT(DATETIME, CONVERT(DATE, prh.SI_UPDATE_TS)), CONVERT(DATETIME, CONVERT(DATE, prh.LOAD_DT)), prh.EXPIRATION_TIME  FROM STG1_BO4D_RUNHIST prh left outer join RPT_BO4_RUNHIST RH on prh.instance_id = rh.instance_id where rh.instance_id is null";


			try {
				//runMSSQLInsertQuery(strSQL)
				Helper.runMSSQLInsertQuery(msSQLInsertString);
			}
			catch (Exception ex){
				System.out.println(ex.getMessage() + " " + ex.getStackTrace());
			}


			// ''*******NOTE******** Now the load date in stage 3 tables is the load date for the stage 3 table.  Formerly I used the load date from stage 1, but I really need to know when stage 3 was loaded

			// '''''''''''''''''''''''''''''WEBI REPORTS TO STAGE 3 TABLE

			// 'Val reports
			//msSQLInsertString = "insert into RPT_BO4_REPORTS (SI_ID, SI_PATH,     SI_FILE1,     REPORT_NM,   REPORT_FILE_PATH, LOAD_DT,   SI_CREATION_TIME,      CUID,    SI_OWNER,    SI_PARENTID, environment, source_system, data_source_nm,        SI_KIND, SI_UNIVERSE,    CREATE_DATE, LOAD_DATE,    SI_DOC_COMMON_CONNECTION, DATASOURCE_TYPE)                            SELECT a.SI_ID,  a.SI_PATH,    a.SI_FILE1,  a.REPORT_NM,    a.REPORT_FILE_PATH,  a.LOAD_DT ,           a.SI_CREATION_TIME,   a.CUID,   a.SI_OWNER,    a.SI_PARENTID,  'Val' ,   'na/' ,   a.DATA_SOURCE_NM,  a.SI_KIND,   a.SI_UNIVERSE,  a.creationTime,                           a.theDate,            a.SI_DOC_COMMON_CONNECTION, a.DATASOURCE_TYPE FROM                           (SELECT cr.SI_ID,  cr.SI_PATH,  cr.SI_FILE1,  cr.REPORT_NM,  cr.REPORT_FILE_PATH,  sysdate theDate,  cr.SI_CREATION_TIME,  cr.CUID,  cr.SI_OWNER,  cr.SI_PARENTID,  'Val' ,  'na/' ,   cr.DATA_SOURCE_NM,  cr.SI_KIND,  cr.SI_UNIVERSE,  TRUNC(cr.SI_CREATION_TIME) creationTime, TRUNC(sysdate) aDate, cr.SI_DOC_COMMON_CONNECTION, cr.DATASOURCE_TYPE  FROM  STG1_BO4v_wx_reports cr) a,(select cuid from rpt_bo4_reports where environment='Val') b where a.cuid = b.cuid(+) and b.cuid is null";
			//msSQLInsertString = "insert into RPT_BO4_REPORTS (SI_ID, SI_PATH,     SI_FILE1,     REPORT_NM,   REPORT_FILE_PATH, LOAD_DT,   SI_CREATION_TIME,      CUID,    SI_OWNER,    SI_PARENTID, environment, source_system, data_source_nm,        SI_KIND, SI_UNIVERSE,    CREATE_DATE, LOAD_DATE,    SI_DOC_COMMON_CONNECTION, DATASOURCE_TYPE)                            SELECT a.SI_ID,  a.SI_PATH,    a.SI_FILE1,  a.REPORT_NM,    a.REPORT_FILE_PATH,  a.LOAD_DT ,           a.SI_CREATION_TIME,   a.CUID,   a.SI_OWNER,    a.SI_PARENTID,  'Val' ENVIRONMENT,   'na/' SOURCE_SYSTEM ,   a.DATA_SOURCE_NM,  a.SI_KIND,   a.SI_UNIVERSE,  a.creationTime,                           a.theDate,            a.SI_DOC_COMMON_CONNECTION, a.DATASOURCE_TYPE FROM                           (SELECT cr.SI_ID,  cr.SI_PATH,  cr.SI_FILE1,  cr.REPORT_NM,  cr.REPORT_FILE_PATH,  GETDATE() theDate,  cr.SI_CREATION_TIME,  cr.CUID,  cr.SI_OWNER,  cr.SI_PARENTID,  'Val'  ENVIRONMENT,  'na/' SOURCE_SYSTEM ,   cr.DATA_SOURCE_NM,  cr.SI_KIND,  cr.SI_UNIVERSE,  CONVERT(DATETIME, CONVERT(DATE, cr.SI_CREATION_TIME)) creationTime, CONVERT(DATETIME, CONVERT(DATE, GETDATE())) aDate, cr.SI_DOC_COMMON_CONNECTION, cr.DATASOURCE_TYPE  FROM  STG1_BO4v_wx_reports cr) a left outer join (select cuid from rpt_bo4_reports where environment='Val') b on a.cuid = b.cuid where b.cuid is null";
			msSQLInsertString = "insert into RPT_BO4_REPORTS (SI_ID, SI_PATH, SI_FILE1, REPORT_NM, REPORT_FILE_PATH, LOAD_DT, SI_CREATION_TIME, CUID, SI_OWNER, SI_PARENTID, environment, source_system, data_source_nm, SI_KIND, SI_UNIVERSE, CREATE_DATE, LOAD_DATE, SI_DOC_COMMON_CONNECTION, DATASOURCE_TYPE) SELECT a.SI_ID, a.SI_PATH, a.SI_FILE1, a.REPORT_NM, a.REPORT_FILE_PATH, a.LOAD_DT, a.SI_CREATION_TIME, a.CUID, a.SI_OWNER, a.SI_PARENTID, 'Val' ENVIRONMENT , 'na/' SOURCE_SYSTEM , a.DATA_SOURCE_NM, a.SI_KIND, a.SI_UNIVERSE, CONVERT(DATETIME, CONVERT(DATE, a.SI_CREATION_TIME)), CONVERT(DATETIME, CONVERT(DATE, a.LOAD_DT)), a.SI_DOC_COMMON_CONNECTION, a.DATASOURCE_TYPE FROM STG1_BO4V_wx_reports a left outer join RPT_BO4_REPORTS b on a.si_id = b.si_id where b.si_id is null";

			System.out.println(msSQLInsertString);
			try {
				//runMSSQLInsertQuery(strSQL)
				Helper.runMSSQLInsertQuery(msSQLInsertString);
			}
			catch (Exception ex){
				System.out.println(ex.getMessage() + " " + ex.getStackTrace());
			}


			//'Prod reports
			//msSQLInsertString = "insert into RPT_BO4_REPORTS (SI_ID, SI_PATH, SI_FILE1, REPORT_NM, REPORT_FILE_PATH, LOAD_DT, SI_CREATION_TIME, CUID, SI_OWNER,     SI_PARENTID, environment, source_system, data_source_nm, SI_KIND, SI_UNIVERSE, CREATE_DATE, LOAD_DATE, SI_DOC_COMMON_CONNECTION, DATASOURCE_TYPE, DATASOURCE_NM) SELECT a.SI_ID,  a.SI_PATH,  a.SI_FILE1,  a.REPORT_NM,  a.REPORT_FILE_PATH,  a.LOAD_DT ,  a.SI_CREATION_TIME,  a.CUID,  a.SI_OWNER,  a.SI_PARENTID,  'Prod' ,  'na/' ,  'see SI_UNIVERSE',  a.SI_KIND,  a.SI_UNIVERSE,  a.creationTime,  a.theDate, a.SI_DOC_COMMON_CONNECTION FROM (SELECT cr.SI_ID,  cr.SI_PATH,  cr.SI_FILE1,  cr.REPORT_NM,  cr.REPORT_FILE_PATH,  sysdate theDate,  cr.SI_CREATION_TIME,  cr.CUID,  cr.SI_OWNER,  cr.SI_PARENTID,  'Prod' ,  'na/' ,  'see SI_UNIVERSE',  cr.SI_KIND,  cr.SI_UNIVERSE,  TRUNC(cr.SI_CREATION_TIME) creationTime,  TRUNC(sysdate) aDate, cr.SI_DOC_COMMON_CONNECTION, cr.DATASOURCE_TYPE, cr.DATASOURCE_NM FROM  STG1_BO4p_wx_reports cr) a,(select cuid from rpt_bo4_reports where environment='Prod') b where a.cuid = b.cuid(+) and b.cuid is null";
			//msSQLInsertString = "insert into RPT_BO4_REPORTS (SI_ID, SI_PATH, SI_FILE1, REPORT_NM, REPORT_FILE_PATH, LOAD_DT, SI_CREATION_TIME, CUID, SI_OWNER,     SI_PARENTID, environment, source_system, data_source_nm, SI_KIND, SI_UNIVERSE, CREATE_DATE, LOAD_DATE, SI_DOC_COMMON_CONNECTION, DATASOURCE_TYPE, DATASOURCE_NM) SELECT a.SI_ID,  a.SI_PATH,  a.SI_FILE1,  a.REPORT_NM,  a.REPORT_FILE_PATH,  a.LOAD_DT ,  a.SI_CREATION_TIME,  a.CUID,  a.SI_OWNER,  a.SI_PARENTID,  'Prod' ,  'na/' ,  'see SI_UNIVERSE',  a.SI_KIND,  a.SI_UNIVERSE,  a.creationTime,  a.theDate, a.SI_DOC_COMMON_CONNECTION,  a.DATASOURCE_TYPE, a.DATASOURCE_NM  FROM (SELECT cr.SI_ID,  cr.SI_PATH,  cr.SI_FILE1,  cr.REPORT_NM,  cr.REPORT_FILE_PATH,  sysdate theDate,  cr.SI_CREATION_TIME,  cr.CUID,  cr.SI_OWNER,  cr.SI_PARENTID,  'Prod' ,  'na/' ,  'see SI_UNIVERSE',  cr.SI_KIND,  cr.SI_UNIVERSE,  TRUNC(cr.SI_CREATION_TIME) creationTime,  TRUNC(sysdate) aDate, cr.SI_DOC_COMMON_CONNECTION, cr.DATASOURCE_TYPE, cr.DATASOURCE_NM FROM  STG1_BO4p_wx_reports cr) a,(select cuid from rpt_bo4_reports where environment='Prod') b where a.cuid = b.cuid(+) and b.cuid is null";
			//Jan 8, 2014:  I changed saving the data source name to use the old field DATA_SOURCE_NM instead of the new and unnecessary field, DATASOURCE_NM
			//msSQLInsertString = "insert into RPT_BO4_REPORTS (SI_ID, SI_PATH,     SI_FILE1,     REPORT_NM,   REPORT_FILE_PATH, LOAD_DT,   SI_CREATION_TIME,      CUID,    SI_OWNER,    SI_PARENTID, environment, source_system, data_source_nm,        SI_KIND, SI_UNIVERSE,    CREATE_DATE, LOAD_DATE,    SI_DOC_COMMON_CONNECTION, DATASOURCE_TYPE)                            SELECT a.SI_ID,  a.SI_PATH,    a.SI_FILE1,  a.REPORT_NM,    a.REPORT_FILE_PATH,  a.LOAD_DT ,           a.SI_CREATION_TIME,   a.CUID,   a.SI_OWNER,    a.SI_PARENTID,  'Prod' ,   'na/' ,   a.DATA_SOURCE_NM,  a.SI_KIND,   a.SI_UNIVERSE,  a.creationTime,                           a.theDate,            a.SI_DOC_COMMON_CONNECTION, a.DATASOURCE_TYPE FROM                           (SELECT cr.SI_ID,  cr.SI_PATH,  cr.SI_FILE1,  cr.REPORT_NM,  cr.REPORT_FILE_PATH,  sysdate theDate,  cr.SI_CREATION_TIME,  cr.CUID,  cr.SI_OWNER,  cr.SI_PARENTID,  'Prod' ,  'na/' ,   cr.DATA_SOURCE_NM,  cr.SI_KIND,  cr.SI_UNIVERSE,  TRUNC(cr.SI_CREATION_TIME) creationTime, TRUNC(sysdate) aDate, cr.SI_DOC_COMMON_CONNECTION, cr.DATASOURCE_TYPE  FROM  STG1_BO4p_wx_reports cr) a,(select cuid from rpt_bo4_reports where environment='Prod') b where a.cuid = b.cuid(+) and b.cuid is null";
			//msSQLInsertString = "insert into RPT_BO4_REPORTS (SI_ID, SI_PATH,     SI_FILE1,     REPORT_NM,   REPORT_FILE_PATH, LOAD_DT,   SI_CREATION_TIME,      CUID,    SI_OWNER,    SI_PARENTID, environment, source_system, data_source_nm,        SI_KIND, SI_UNIVERSE,    CREATE_DATE, LOAD_DATE,    SI_DOC_COMMON_CONNECTION, DATASOURCE_TYPE)                            SELECT a.SI_ID,  a.SI_PATH,    a.SI_FILE1,  a.REPORT_NM,    a.REPORT_FILE_PATH,  a.LOAD_DT ,           a.SI_CREATION_TIME,   a.CUID,   a.SI_OWNER,    a.SI_PARENTID,  'Prod' ENVIRONMENT ,   'na/' SOURCE_SYSTEM ,   a.DATA_SOURCE_NM,  a.SI_KIND,   a.SI_UNIVERSE,  a.creationTime,                           a.theDate,            a.SI_DOC_COMMON_CONNECTION, a.DATASOURCE_TYPE FROM                           (SELECT cr.SI_ID,  cr.SI_PATH,  cr.SI_FILE1,  cr.REPORT_NM,  cr.REPORT_FILE_PATH,  GETDATE() theDate,  cr.SI_CREATION_TIME,  cr.CUID,  cr.SI_OWNER,  cr.SI_PARENTID,  'Prod' ENVIRONMENT ,  'na/' SOURCE_SYSTEM ,   cr.DATA_SOURCE_NM,  cr.SI_KIND,  cr.SI_UNIVERSE, CONVERT(DATETIME, CONVERT(DATE, cr.SI_CREATION_TIME))  creationTime, CONVERT(DATETIME, CONVERT(DATE, GETDATE())) aDate, cr.SI_DOC_COMMON_CONNECTION, cr.DATASOURCE_TYPE  FROM  STG1_BO4p_wx_reports cr) a left outer join (select cuid from rpt_bo4_reports where environment='Prod') b on a.cuid = b.cuid where b.cuid is null";
			msSQLInsertString = "insert into RPT_BO4_REPORTS (SI_ID, SI_PATH, SI_FILE1, REPORT_NM, REPORT_FILE_PATH, LOAD_DT, SI_CREATION_TIME, CUID, SI_OWNER, SI_PARENTID, environment, source_system, data_source_nm, SI_KIND, SI_UNIVERSE, CREATE_DATE, LOAD_DATE, SI_DOC_COMMON_CONNECTION, DATASOURCE_TYPE) SELECT a.SI_ID, a.SI_PATH, a.SI_FILE1, a.REPORT_NM, a.REPORT_FILE_PATH, a.LOAD_DT, a.SI_CREATION_TIME, a.CUID, a.SI_OWNER, a.SI_PARENTID, 'Prod' ENVIRONMENT , 'na/' SOURCE_SYSTEM , a.DATA_SOURCE_NM, a.SI_KIND, a.SI_UNIVERSE, CONVERT(DATETIME, CONVERT(DATE, a.SI_CREATION_TIME)), CONVERT(DATETIME, CONVERT(DATE, a.LOAD_DT)), a.SI_DOC_COMMON_CONNECTION, a.DATASOURCE_TYPE FROM STG1_BO4P_wx_reports a left outer join RPT_BO4_REPORTS b on a.si_id = b.si_id where b.si_id is null";

			System.out.println(msSQLInsertString);;
			try {
				//runMSSQLInsertQuery(strSQL)
				Helper.runMSSQLInsertQuery(msSQLInsertString);
			}
			catch (Exception ex){
				System.out.println(ex.getMessage() + " " + ex.getStackTrace());
			}


			//'QA reports
			//This query just isn't working correctly.  I am going to write it using inline queries, which I hate to do, but have no other choice.
			//msSQLInsertString = "insert into RPT_BO4_REPORTS (SI_ID, SI_PATH, SI_FILE1, REPORT_NM, REPORT_FILE_PATH, LOAD_DT, SI_CREATION_TIME, CUID, SI_OWNER,     SI_PARENTID, environment, source_system, data_source_nm, SI_KIND, SI_UNIVERSE, CREATE_DATE, LOAD_DATE, SI_DOC_COMMON_CONNECTION, DATASOURCE_TYPE, DATASOURCE_NM) SELECT a.SI_ID,  a.SI_PATH,  a.SI_FILE1,  a.REPORT_NM,  a.REPORT_FILE_PATH,  a.LOAD_DT ,  a.SI_CREATION_TIME,  a.CUID,  a.SI_OWNER,  a.SI_PARENTID,  'QA' ,  'na/' ,  'see SI_UNIVERSE',  a.SI_KIND,  a.SI_UNIVERSE,  a.creationTime,  a.theDate, a.SI_DOC_COMMON_CONNECTION,  a.DATASOURCE_TYPE, a.DATASOURCE_NM  FROM (SELECT cr.SI_ID,  cr.SI_PATH,  cr.SI_FILE1,  cr.REPORT_NM,  cr.REPORT_FILE_PATH,  sysdate theDate,  cr.SI_CREATION_TIME,  cr.CUID,  cr.SI_OWNER,  cr.SI_PARENTID,  'QA' ,  'na/' ,  'see SI_UNIVERSE',  cr.SI_KIND,  cr.SI_UNIVERSE,  TRUNC(cr.SI_CREATION_TIME) creationTime,  TRUNC(sysdate) aDate, cr.SI_DOC_COMMON_CONNECTION, cr.DATASOURCE_TYPE, cr.DATASOURCE_NM FROM  STG1_BO4q_wx_reports cr) a,(select cuid from rpt_bo4_reports where environment='QA') b where a.cuid = b.cuid(+) and b.cuid is null";
			//broken:  msSQLInsertString = "insert into RPT_BO4_REPORTS (SI_ID, SI_PATH, SI_FILE1, REPORT_NM, REPORT_FILE_PATH, LOAD_DT, SI_CREATION_TIME, CUID, SI_OWNER,     SI_PARENTID, environment, source_system, data_source_nm, SI_KIND, SI_UNIVERSE, CREATE_DATE, LOAD_DATE, SI_DOC_COMMON_CONNECTION, DATASOURCE_TYPE, DATA_SOURCE_NM) SELECT a.SI_ID,  a.SI_PATH,  a.SI_FILE1,  a.REPORT_NM,  a.REPORT_FILE_PATH,  a.LOAD_DT ,  a.SI_CREATION_TIME,  a.CUID,  a.SI_OWNER,  a.SI_PARENTID,  'QA' ,  'na/' ,  a.DATASOURCE_TYPE,  a.SI_KIND,  a.SI_UNIVERSE,  a.creationTime,  a.theDate, a.SI_DOC_COMMON_CONNECTION,  a.DATASOURCE_TYPE, a.DATASOURCE_NM  FROM (SELECT cr.SI_ID,  cr.SI_PATH,  cr.SI_FILE1,  cr.REPORT_NM,  cr.REPORT_FILE_PATH,  sysdate theDate,  cr.SI_CREATION_TIME,  cr.CUID,  cr.SI_OWNER,  cr.SI_PARENTID,  'QA' ENVIRONMENT,  'na/' ,  a.DATASOURCE_TYPE,  cr.SI_KIND,  cr.SI_UNIVERSE,  TRUNC(cr.SI_CREATION_TIME) creationTime,  TRUNC(sysdate) aDate, cr.SI_DOC_COMMON_CONNECTION, cr.DATASOURCE_TYPE, cr.DATASOURCE_NM FROM  STG1_BO4q_wx_reports cr) a,(select cuid from rpt_bo4_reports where environment='QA') b where a.cuid = b.cuid(+) and b.cuid is null";
			//msSQLInsertString = "insert into RPT_BO4_REPORTS (SI_ID, SI_PATH,     SI_FILE1,     REPORT_NM,   REPORT_FILE_PATH, LOAD_DT,   SI_CREATION_TIME,      CUID,    SI_OWNER,    SI_PARENTID, environment, source_system, data_source_nm,        SI_KIND, SI_UNIVERSE,    CREATE_DATE, LOAD_DATE,    SI_DOC_COMMON_CONNECTION, DATASOURCE_TYPE)                            SELECT a.SI_ID,  a.SI_PATH,    a.SI_FILE1,  a.REPORT_NM,    a.REPORT_FILE_PATH,  a.LOAD_DT ,           a.SI_CREATION_TIME,   a.CUID,   a.SI_OWNER,    a.SI_PARENTID,  'QA' ENVIRONMENT, 'na/' ,   a.DATA_SOURCE_NM,  a.SI_KIND,   a.SI_UNIVERSE,  a.creationTime,                           a.theDate,            a.SI_DOC_COMMON_CONNECTION, a.DATASOURCE_TYPE FROM                           (SELECT cr.SI_ID,  cr.SI_PATH,  cr.SI_FILE1,  cr.REPORT_NM,  cr.REPORT_FILE_PATH,  sysdate theDate,  cr.SI_CREATION_TIME,  cr.CUID,  cr.SI_OWNER,  cr.SI_PARENTID,  'QA' ENVIRONMENT,  'na/' ,   cr.DATA_SOURCE_NM,  cr.SI_KIND,  cr.SI_UNIVERSE,  TRUNC(cr.SI_CREATION_TIME) creationTime, TRUNC(sysdate) aDate, cr.SI_DOC_COMMON_CONNECTION, cr.DATASOURCE_TYPE  FROM  STG1_BO4q_wx_reports cr) a,(select cuid from rpt_bo4_reports where environment='QA') b where a.cuid = b.cuid(+) and b.cuid is null";
			//msSQLInsertString = "insert into RPT_BO4_REPORTS (SI_ID, SI_PATH,     SI_FILE1,     REPORT_NM,   REPORT_FILE_PATH, LOAD_DT,   SI_CREATION_TIME,      CUID,    SI_OWNER,    SI_PARENTID, environment, source_system, data_source_nm,        SI_KIND, SI_UNIVERSE,    CREATE_DATE, LOAD_DATE,    SI_DOC_COMMON_CONNECTION, DATASOURCE_TYPE)                            SELECT a.SI_ID,  a.SI_PATH,    a.SI_FILE1,  a.REPORT_NM,    a.REPORT_FILE_PATH,  a.LOAD_DT ,           a.SI_CREATION_TIME,   a.CUID,   a.SI_OWNER,    a.SI_PARENTID,  'QA' ENVIRONMENT, 'na/' SOURCE_SYSTEM ,   a.DATA_SOURCE_NM,  a.SI_KIND,   a.SI_UNIVERSE,  a.creationTime,                           a.theDate,            a.SI_DOC_COMMON_CONNECTION, a.DATASOURCE_TYPE FROM                           (SELECT cr.SI_ID,  cr.SI_PATH,  cr.SI_FILE1,  cr.REPORT_NM,  cr.REPORT_FILE_PATH,  GETDATE() theDate,  cr.SI_CREATION_TIME,  cr.CUID,  cr.SI_OWNER,  cr.SI_PARENTID,  'QA' ENVIRONMENT,  'na/' SOURCE_SYSTEM ,   cr.DATA_SOURCE_NM,  cr.SI_KIND,  cr.SI_UNIVERSE,  CONVERT(DATETIME, CONVERT(DATE, cr.SI_CREATION_TIME)) creationTime, CONVERT(DATETIME, CONVERT(DATE, GETDATE())) aDate, cr.SI_DOC_COMMON_CONNECTION, cr.DATASOURCE_TYPE  FROM  STG1_BO4q_wx_reports cr) a left outer join (select cuid from rpt_bo4_reports where environment='QA') b on a.cuid = b.cuid where b.cuid is null";
			msSQLInsertString = "insert into RPT_BO4_REPORTS (SI_ID, SI_PATH, SI_FILE1, REPORT_NM, REPORT_FILE_PATH, LOAD_DT, SI_CREATION_TIME, CUID, SI_OWNER, SI_PARENTID, environment, source_system, data_source_nm, SI_KIND, SI_UNIVERSE, CREATE_DATE, LOAD_DATE, SI_DOC_COMMON_CONNECTION, DATASOURCE_TYPE) SELECT a.SI_ID, a.SI_PATH, a.SI_FILE1, a.REPORT_NM, a.REPORT_FILE_PATH, a.LOAD_DT, a.SI_CREATION_TIME, a.CUID, a.SI_OWNER, a.SI_PARENTID, 'QA' ENVIRONMENT , 'na/' SOURCE_SYSTEM , a.DATA_SOURCE_NM, a.SI_KIND, a.SI_UNIVERSE, CONVERT(DATETIME, CONVERT(DATE, a.SI_CREATION_TIME)), CONVERT(DATETIME, CONVERT(DATE, a.LOAD_DT)), a.SI_DOC_COMMON_CONNECTION, a.DATASOURCE_TYPE FROM STG1_BO4Q_wx_reports a left outer join RPT_BO4_REPORTS b on a.si_id = b.si_id where b.si_id is null";

			System.out.println(msSQLInsertString);;
			try {
				//runMSSQLInsertQuery(strSQL)
				Helper.runMSSQLInsertQuery(msSQLInsertString);
			}
			catch (Exception ex){
				System.out.println(ex.getMessage() + " " + ex.getStackTrace());
			}


			// 'Dev reports
			//msSQLInsertString = "insert into RPT_BO4_REPORTS (SI_ID, SI_PATH, SI_FILE1, REPORT_NM, REPORT_FILE_PATH, LOAD_DT, SI_CREATION_TIME, CUID, SI_OWNER,     SI_PARENTID, environment, source_system, data_source_nm, SI_KIND, SI_UNIVERSE, CREATE_DATE, LOAD_DATE, SI_DOC_COMMON_CONNECTION, DATASOURCE_TYPE, DATASOURCE_NM) SELECT a.SI_ID,  a.SI_PATH,  a.SI_FILE1,  a.REPORT_NM,  a.REPORT_FILE_PATH,  a.LOAD_DT ,  a.SI_CREATION_TIME,  a.CUID,  a.SI_OWNER,  a.SI_PARENTID,  'Dev' ,  'na/' ,  'see SI_UNIVERSE',  a.SI_KIND,  a.SI_UNIVERSE,  a.creationTime,  a.theDate, a.SI_DOC_COMMON_CONNECTION FROM (SELECT cr.SI_ID,  cr.SI_PATH,  cr.SI_FILE1,  cr.REPORT_NM,  cr.REPORT_FILE_PATH,  sysdate theDate,  cr.SI_CREATION_TIME,  cr.CUID,  cr.SI_OWNER,  cr.SI_PARENTID,  'Dev' ,  'na/' ,  'see SI_UNIVERSE',  cr.SI_KIND,  cr.SI_UNIVERSE,  TRUNC(cr.SI_CREATION_TIME) creationTime,  TRUNC(sysdate) aDate, cr.SI_DOC_COMMON_CONNECTION, cr.DATASOURCE_TYPE, cr.DATASOURCE_NM FROM  STG1_BO4d_wx_reports cr) a,(select cuid from rpt_bo4_reports where environment='Dev') b where a.cuid = b.cuid(+) and b.cuid is null";
			//msSQLInsertString = "insert into RPT_BO4_REPORTS (SI_ID, SI_PATH, SI_FILE1, REPORT_NM, REPORT_FILE_PATH, LOAD_DT, SI_CREATION_TIME, CUID, SI_OWNER,     SI_PARENTID, environment, source_system, data_source_nm, SI_KIND, SI_UNIVERSE, CREATE_DATE, LOAD_DATE, SI_DOC_COMMON_CONNECTION, DATASOURCE_TYPE, DATASOURCE_NM) SELECT a.SI_ID,  a.SI_PATH,  a.SI_FILE1,  a.REPORT_NM,  a.REPORT_FILE_PATH,  a.LOAD_DT ,  a.SI_CREATION_TIME,  a.CUID,  a.SI_OWNER,  a.SI_PARENTID,  'Dev' ,  'na/' ,  'see SI_UNIVERSE',  a.SI_KIND,  a.SI_UNIVERSE,  a.creationTime,  a.theDate, a.SI_DOC_COMMON_CONNECTION,  a.DATASOURCE_TYPE, a.DATASOURCE_NM  FROM (SELECT cr.SI_ID,  cr.SI_PATH,  cr.SI_FILE1,  cr.REPORT_NM,  cr.REPORT_FILE_PATH,  sysdate theDate,  cr.SI_CREATION_TIME,  cr.CUID,  cr.SI_OWNER,  cr.SI_PARENTID,  'Dev' ,  'na/' ,  'see SI_UNIVERSE',  cr.SI_KIND,  cr.SI_UNIVERSE,  TRUNC(cr.SI_CREATION_TIME) creationTime,  TRUNC(sysdate) aDate, cr.SI_DOC_COMMON_CONNECTION, cr.DATASOURCE_TYPE, cr.DATASOURCE_NM FROM  STG1_BO4d_wx_reports cr) a,(select cuid from rpt_bo4_reports where environment='Dev') b where a.cuid = b.cuid(+) and b.cuid is null";
			//broken: msSQLInsertString = "insert into RPT_BO4_REPORTS (SI_ID, SI_PATH, SI_FILE1, REPORT_NM, REPORT_FILE_PATH, LOAD_DT, SI_CREATION_TIME, CUID, SI_OWNER,     SI_PARENTID, environment, source_system, data_source_nm, SI_KIND, SI_UNIVERSE, CREATE_DATE, LOAD_DATE, SI_DOC_COMMON_CONNECTION, DATASOURCE_TYPE, DATA_SOURCE_NM) SELECT a.SI_ID,  a.SI_PATH,  a.SI_FILE1,  a.REPORT_NM,  a.REPORT_FILE_PATH,  a.LOAD_DT ,  a.SI_CREATION_TIME,  a.CUID,  a.SI_OWNER,  a.SI_PARENTID,  'Dev' ,  'na/' ,  a.DATASOURCE_TYPE,  a.SI_KIND,  a.SI_UNIVERSE,  a.creationTime,  a.theDate, a.SI_DOC_COMMON_CONNECTION,  a.DATASOURCE_TYPE, a.DATASOURCE_NM  FROM (SELECT cr.SI_ID,  cr.SI_PATH,  cr.SI_FILE1,  cr.REPORT_NM,  cr.REPORT_FILE_PATH,  sysdate theDate,  cr.SI_CREATION_TIME,  cr.CUID,  cr.SI_OWNER,  cr.SI_PARENTID,  'Dev' ,  'na/' ,  a.DATASOURCE_TYPE,  cr.SI_KIND,  cr.SI_UNIVERSE,  TRUNC(cr.SI_CREATION_TIME) creationTime,  TRUNC(sysdate) aDate, cr.SI_DOC_COMMON_CONNECTION, cr.DATASOURCE_TYPE, cr.DATASOURCE_NM FROM  STG1_BO4d_wx_reports cr) a,(select cuid from rpt_bo4_reports where environment='Dev') b where a.cuid = b.cuid(+) and b.cuid is null";
			//msSQLInsertString = "insert into RPT_BO4_REPORTS (SI_ID, SI_PATH,     SI_FILE1,     REPORT_NM,   REPORT_FILE_PATH, LOAD_DT,   SI_CREATION_TIME,      CUID,    SI_OWNER,    SI_PARENTID, environment, source_system, data_source_nm,        SI_KIND, SI_UNIVERSE,    CREATE_DATE, LOAD_DATE,    SI_DOC_COMMON_CONNECTION, DATASOURCE_TYPE)                            SELECT a.SI_ID,  a.SI_PATH,    a.SI_FILE1,  a.REPORT_NM,    a.REPORT_FILE_PATH,  a.LOAD_DT ,           a.SI_CREATION_TIME,   a.CUID,   a.SI_OWNER,    a.SI_PARENTID,  'Dev' ,   'na/' ,   a.DATA_SOURCE_NM,  a.SI_KIND,   a.SI_UNIVERSE,  a.creationTime,                           a.theDate,            a.SI_DOC_COMMON_CONNECTION, a.DATASOURCE_TYPE FROM                           (SELECT cr.SI_ID,  cr.SI_PATH,  cr.SI_FILE1,  cr.REPORT_NM,  cr.REPORT_FILE_PATH,  sysdate theDate,  cr.SI_CREATION_TIME,  cr.CUID,  cr.SI_OWNER,  cr.SI_PARENTID,  'Dev' ,  'na/' ,   cr.DATA_SOURCE_NM,  cr.SI_KIND,  cr.SI_UNIVERSE,  TRUNC(cr.SI_CREATION_TIME) creationTime, TRUNC(sysdate) aDate, cr.SI_DOC_COMMON_CONNECTION, cr.DATASOURCE_TYPE  FROM  STG1_BO4d_wx_reports cr) a,(select cuid from rpt_bo4_reports where environment='Dev') b where a.cuid = b.cuid(+) and b.cuid is null";
			//msSQLInsertString = "insert into RPT_BO4_REPORTS (SI_ID, SI_PATH,     SI_FILE1,     REPORT_NM,   REPORT_FILE_PATH, LOAD_DT,   SI_CREATION_TIME,      CUID,    SI_OWNER,    SI_PARENTID, environment, source_system, data_source_nm,        SI_KIND, SI_UNIVERSE,    CREATE_DATE, LOAD_DATE,    SI_DOC_COMMON_CONNECTION, DATASOURCE_TYPE)                            SELECT a.SI_ID,  a.SI_PATH,    a.SI_FILE1,  a.REPORT_NM,    a.REPORT_FILE_PATH,  a.LOAD_DT ,           a.SI_CREATION_TIME,   a.CUID,   a.SI_OWNER,    a.SI_PARENTID,  'Dev' ENVIRONMENT,   'na/' SOURCE_SYSTEM ,   a.DATA_SOURCE_NM,  a.SI_KIND,   a.SI_UNIVERSE,  a.creationTime,                           a.theDate,            a.SI_DOC_COMMON_CONNECTION, a.DATASOURCE_TYPE FROM                           (SELECT cr.SI_ID,  cr.SI_PATH,  cr.SI_FILE1,  cr.REPORT_NM,  cr.REPORT_FILE_PATH,  GETDATE() theDate,  cr.SI_CREATION_TIME,  cr.CUID,  cr.SI_OWNER,  cr.SI_PARENTID,  'Dev' ENVIRONMENT,  'na/' SOURCE_SYSTEM ,   cr.DATA_SOURCE_NM,  cr.SI_KIND,  cr.SI_UNIVERSE,  CONVERT(DATETIME, CONVERT(DATE, cr.SI_CREATION_TIME)) creationTime, CONVERT(DATETIME, CONVERT(DATE, GETDATE())) aDate, cr.SI_DOC_COMMON_CONNECTION, cr.DATASOURCE_TYPE  FROM  STG1_BO4d_wx_reports cr) a left outer join (select cuid from rpt_bo4_reports where environment='Dev') b on a.cuid = b.cuid where b.cuid is null";
			msSQLInsertString = "insert into RPT_BO4_REPORTS (SI_ID, SI_PATH, SI_FILE1, REPORT_NM, REPORT_FILE_PATH, LOAD_DT, SI_CREATION_TIME, CUID, SI_OWNER, SI_PARENTID, environment, source_system, data_source_nm, SI_KIND, SI_UNIVERSE, CREATE_DATE, LOAD_DATE, SI_DOC_COMMON_CONNECTION, DATASOURCE_TYPE) SELECT a.SI_ID, a.SI_PATH, a.SI_FILE1, a.REPORT_NM, a.REPORT_FILE_PATH, a.LOAD_DT, a.SI_CREATION_TIME, a.CUID, a.SI_OWNER, a.SI_PARENTID, 'Dev' ENVIRONMENT , 'na/' SOURCE_SYSTEM , a.DATA_SOURCE_NM, a.SI_KIND, a.SI_UNIVERSE, CONVERT(DATETIME, CONVERT(DATE, a.SI_CREATION_TIME)), CONVERT(DATETIME, CONVERT(DATE, a.LOAD_DT)), a.SI_DOC_COMMON_CONNECTION, a.DATASOURCE_TYPE FROM STG1_BO4D_wx_reports a left outer join RPT_BO4_REPORTS b on a.si_id = b.si_id where b.si_id is null";

			System.out.println(msSQLInsertString);;
			try {
				//runMSSQLInsertQuery(strSQL)
				Helper.runMSSQLInsertQuery(msSQLInsertString);
			}
			catch (Exception ex){
				System.out.println(ex.getMessage() + " " + ex.getStackTrace());
			}


			//Sandbox
			//msSQLInsertString = "insert into RPT_BO4_REPORTS (SI_ID, SI_PATH,     SI_FILE1,     REPORT_NM,   REPORT_FILE_PATH, LOAD_DT,   SI_CREATION_TIME,      CUID,    SI_OWNER,    SI_PARENTID, environment, source_system, data_source_nm,        SI_KIND, SI_UNIVERSE,    CREATE_DATE, LOAD_DATE,    SI_DOC_COMMON_CONNECTION, DATASOURCE_TYPE)                            SELECT a.SI_ID,  a.SI_PATH,    a.SI_FILE1,  a.REPORT_NM,    a.REPORT_FILE_PATH,  a.LOAD_DT ,           a.SI_CREATION_TIME,   a.CUID,   a.SI_OWNER,    a.SI_PARENTID,  'Sbx' ENVIRONMENT,   'na/' SOURCE_SYSTEM ,   a.DATA_SOURCE_NM,  a.SI_KIND,   a.SI_UNIVERSE,  a.creationTime,                           a.theDate,            a.SI_DOC_COMMON_CONNECTION, a.DATASOURCE_TYPE FROM                           (SELECT cr.SI_ID,  cr.SI_PATH,  cr.SI_FILE1,  cr.REPORT_NM,  cr.REPORT_FILE_PATH,  GETDATE() theDate,  cr.SI_CREATION_TIME,  cr.CUID,  cr.SI_OWNER,  cr.SI_PARENTID,  'Sbx ENVIRONMENT' ,  'na/' SOURCE_SYSTEM ,   cr.DATA_SOURCE_NM,  cr.SI_KIND,  cr.SI_UNIVERSE,  CONVERT(DATETIME, CONVERT(DATE, cr.SI_CREATION_TIME)) creationTime, CONVERT(DATETIME, CONVERT(DATE, GETDATE())) aDate, cr.SI_DOC_COMMON_CONNECTION, cr.DATASOURCE_TYPE  FROM  STG1_BO4s_wx_reports cr) a left outer join (select cuid from rpt_bo4_reports where environment='Sbx') b on a.cuid = b.cuid where b.cuid is null";
			msSQLInsertString = "insert into RPT_BO4_REPORTS (SI_ID, SI_PATH, SI_FILE1, REPORT_NM, REPORT_FILE_PATH, LOAD_DT, SI_CREATION_TIME, CUID, SI_OWNER, SI_PARENTID, environment, source_system, data_source_nm, SI_KIND, SI_UNIVERSE, CREATE_DATE, LOAD_DATE, SI_DOC_COMMON_CONNECTION, DATASOURCE_TYPE) SELECT a.SI_ID, a.SI_PATH, a.SI_FILE1, a.REPORT_NM, a.REPORT_FILE_PATH, a.LOAD_DT, a.SI_CREATION_TIME, a.CUID, a.SI_OWNER, a.SI_PARENTID, 'Sbx' ENVIRONMENT , 'na/' SOURCE_SYSTEM , a.DATA_SOURCE_NM, a.SI_KIND, a.SI_UNIVERSE, CONVERT(DATETIME, CONVERT(DATE, a.SI_CREATION_TIME)), CONVERT(DATETIME, CONVERT(DATE, a.LOAD_DT)), a.SI_DOC_COMMON_CONNECTION, a.DATASOURCE_TYPE FROM STG1_BO4S_wx_reports a left outer join RPT_BO4_REPORTS b on a.si_id = b.si_id where b.si_id is null";


			// 'CRYSTAL REPORTS
			//'Val reports
			//'strSQL = "insert into RPT_BOXI_REPORTS SELECT SI_ID, SI_PATH, SI_FILE1, REPORT_NM, REPORT_FILE_PATH, SOURCE_SYSTEM,    DATA_SOURCE_NM, DATA_SOURCE2_NM, DYNAMIC_PICK_LIST_NM,    LOAD_DT, SI_CREATION_TIME, SI_UPDATE_TS,    CUID, SI_OWNER, SI_BUSINESS_VIEW_INFO,    SI_PARENTID, SI_LOCAL_FILEPATH, ORIGINAL_FILE_NM,    SI_PARENT_FOLDER_CUID, UPDATE_DT, 'Dev'   FROM STG1_MIGR_XIR2D_REPORTS where load_dt >= to_date('" & oracleOnTheHour(Now()) & "','DD-MM-YYYY HH24:MI:SS')"
			// 'strSQL = "insert into RPT_BO4_REPORTS (SI_ID, SI_PATH, SI_FILE1, REPORT_NM, REPORT_FILE_PATH, SOURCE_SYSTEM,    DATA_SOURCE_NM, DATA_SOURCE2_NM, DYNAMIC_PICK_LIST_NM,    LOAD_DT, SI_CREATION_TIME, SI_UPDATE_TS,    CUID, SI_OWNER, SI_BUSINESS_VIEW_INFO,    SI_PARENTID, SI_LOCAL_FILEPATH, ORIGINAL_FILE_NM,    SI_PARENT_FOLDER_CUID, UPDATE_DT, ENVIRONMENT,   SI_KIND,  CREATE_DATE,   UPDATE_DATE, LOAD_DATE) SELECT SI_ID, SI_PATH, SI_FILE1, REPORT_NM, REPORT_FILE_PATH, SOURCE_SYSTEM,    DATA_SOURCE_NM, DATA_SOURCE2_NM, DYNAMIC_PICK_LIST_NM,    sysdate, SI_CREATION_TIME, SI_UPDATE_TS,    CUID, SI_OWNER, SI_BUSINESS_VIEW_INFO,    SI_PARENTID, SI_LOCAL_FILEPATH, ORIGINAL_FILE_NM,    SI_PARENT_FOLDER_CUID, UPDATE_DT, 'Dev', SI_KIND,   trunc(si_creation_time), trunc(SI_UPDATE_TS), trunc(sysdate)   FROM STG1_BO4T_CREPORTS where load_dt >= to_date('" & oracleOnTheHour(Now()) & "','DD-MM-YYYY HH24:MI:SS')"
			//11/8/2012:  For some reason this query doesn't seem to work all the time.
			//msSQLInsertString = "insert into RPT_BO4_REPORTS (SI_ID, SI_PATH, SI_FILE1, REPORT_NM, REPORT_FILE_PATH, SOURCE_SYSTEM,    DATA_SOURCE_NM, DATA_SOURCE2_NM, DYNAMIC_PICK_LIST_NM,    LOAD_DT, SI_CREATION_TIME, SI_UPDATE_TS,    CUID, SI_OWNER, SI_BUSINESS_VIEW_INFO,    SI_PARENTID, SI_LOCAL_FILEPATH, ORIGINAL_FILE_NM,    SI_PARENT_FOLDER_CUID, UPDATE_DT, ENVIRONMENT,   SI_KIND,  CREATE_DATE,   UPDATE_DATE, LOAD_DATE) SELECT cr.SI_ID, cr.SI_PATH, cr.SI_FILE1, cr.REPORT_NM, cr.REPORT_FILE_PATH, cr.SOURCE_SYSTEM,    cr.DATA_SOURCE_NM, cr.DATA_SOURCE2_NM, cr.DYNAMIC_PICK_LIST_NM,    sysdate, cr.SI_CREATION_TIME, cr.SI_UPDATE_TS,    cr.CUID, cr.SI_OWNER, cr.SI_BUSINESS_VIEW_INFO,    cr.SI_PARENTID, cr.SI_LOCAL_FILEPATH, cr.ORIGINAL_FILE_NM,    cr.SI_PARENT_FOLDER_CUID, cr.UPDATE_DT, 'Dev', cr.SI_KIND,  trunc(cr.si_creation_time), trunc(cr.SI_UPDATE_TS), trunc(sysdate)   from RPT_BO4_reports v, STG1_BO4V_creports cr where cr.CUID = v.CUID (+) and v.CUID is null";
			//msSQLInsertString = "insert into RPT_BO4_REPORTS (SI_ID, SI_PATH, SI_FILE1, REPORT_NM, REPORT_FILE_PATH, SOURCE_SYSTEM,    DATA_SOURCE_NM, DATA_SOURCE2_NM, DYNAMIC_PICK_LIST_NM,    LOAD_DT, SI_CREATION_TIME, SI_UPDATE_TS,    CUID, SI_OWNER, SI_BUSINESS_VIEW_INFO,    SI_PARENTID, SI_LOCAL_FILEPATH, ORIGINAL_FILE_NM,    SI_PARENT_FOLDER_CUID, UPDATE_DT, ENVIRONMENT,   SI_KIND,  CREATE_DATE,   UPDATE_DATE, LOAD_DATE) SELECT a.SI_ID,  a.SI_PATH,  a.SI_FILE1,  a.REPORT_NM,  a.REPORT_FILE_PATH,  a.SOURCE_SYSTEM,  a.DATA_SOURCE_NM,  a.DATA_SOURCE2_NM,  a.DYNAMIC_PICK_LIST_NM,  a.theDate,  a.SI_CREATION_TIME,  a.SI_UPDATE_TS,  a.CUID,  a.SI_OWNER,  a.SI_BUSINESS_VIEW_INFO,  a.SI_PARENTID,  a.SI_LOCAL_FILEPATH,  a.ORIGINAL_FILE_NM,  a.SI_PARENT_FOLDER_CUID,  a.UPDATE_DT,  'Val',  a.SI_KIND,  a.creationDate,  a.updateTS,  a.LOAD_DT   from (SELECT cr.SI_ID,  cr.SI_PATH,  cr.SI_FILE1,  cr.REPORT_NM,  cr.REPORT_FILE_PATH,  cr.SOURCE_SYSTEM,  cr.DATA_SOURCE_NM,  cr.DATA_SOURCE2_NM,  cr.DYNAMIC_PICK_LIST_NM,  sysdate theDate,  cr.SI_CREATION_TIME,  cr.SI_UPDATE_TS,  cr.CUID,  cr.SI_OWNER,  cr.SI_BUSINESS_VIEW_INFO,  cr.SI_PARENTID,  cr.SI_LOCAL_FILEPATH,  cr.ORIGINAL_FILE_NM,  cr.SI_PARENT_FOLDER_CUID,  cr.UPDATE_DT,  'Val',  cr.SI_KIND,  TRUNC(cr.si_creation_time) creationDate,  TRUNC(cr.SI_UPDATE_TS) updateTS,  TRUNC(sysdate) aDate FROM   STG1_BO4v_creports cr) a,(select cuid from RPT_BO4_REPORTS where environment='Val' and si_kind='CrystalReport')b WHERE a.CUID = b.CUID (+) AND b.CUID   IS NULL";
			//msSQLInsertString = "insert into RPT_BO4_REPORTS (SI_ID, SI_PATH, SI_FILE1, REPORT_NM, REPORT_FILE_PATH, SOURCE_SYSTEM,    DATA_SOURCE_NM, DATA_SOURCE2_NM, DYNAMIC_PICK_LIST_NM,    LOAD_DT, SI_CREATION_TIME, SI_UPDATE_TS,    CUID, SI_OWNER, SI_BUSINESS_VIEW_INFO,    SI_PARENTID, SI_LOCAL_FILEPATH, ORIGINAL_FILE_NM,    SI_PARENT_FOLDER_CUID, UPDATE_DT, ENVIRONMENT,   SI_KIND,  CREATE_DATE,   UPDATE_DATE, LOAD_DATE, DATASOURCE_TYPE) SELECT a.SI_ID,  a.SI_PATH,  a.SI_FILE1,  a.REPORT_NM,  a.REPORT_FILE_PATH,  a.SOURCE_SYSTEM,  a.DATA_SOURCE_NM,  a.DATA_SOURCE2_NM,  a.DYNAMIC_PICK_LIST_NM,  a.theDate,  a.SI_CREATION_TIME,  a.SI_UPDATE_TS,  a.CUID,  a.SI_OWNER,  a.SI_BUSINESS_VIEW_INFO,  a.SI_PARENTID,  a.SI_LOCAL_FILEPATH,  a.ORIGINAL_FILE_NM,  a.SI_PARENT_FOLDER_CUID,  a.UPDATE_DT,  'Val',  a.SI_KIND,  a.creationDate,  a.updateTS,  a.LOAD_DT , a.datasource   from (SELECT cr.SI_ID,  cr.SI_PATH,  cr.SI_FILE1,  cr.REPORT_NM,  cr.REPORT_FILE_PATH,  cr.SOURCE_SYSTEM,  cr.DATA_SOURCE_NM,  cr.DATA_SOURCE2_NM,  cr.DYNAMIC_PICK_LIST_NM,  sysdate theDate,  cr.SI_CREATION_TIME,  cr.SI_UPDATE_TS,  cr.CUID,  cr.SI_OWNER,  cr.SI_BUSINESS_VIEW_INFO,  cr.SI_PARENTID,  cr.SI_LOCAL_FILEPATH,  cr.ORIGINAL_FILE_NM,  cr.SI_PARENT_FOLDER_CUID,  cr.UPDATE_DT,  'Val',  cr.SI_KIND,  TRUNC(cr.si_creation_time) creationDate,  TRUNC(cr.SI_UPDATE_TS) updateTS,  TRUNC(sysdate) aDate, cr.DATASOURCE_TYPE datasource FROM   STG1_BO4d_creports cr) a,(select cuid from RPT_BO4_REPORTS where environment='Val' and si_kind='CrystalReport')b WHERE a.CUID = b.CUID (+) AND b.CUID   IS NULL";
			//msSQLInsertString = "insert into RPT_BO4_REPORTS (SI_ID, SI_PATH, SI_FILE1, REPORT_NM, REPORT_FILE_PATH, SOURCE_SYSTEM,    DATA_SOURCE_NM, DATA_SOURCE2_NM, DYNAMIC_PICK_LIST_NM,    LOAD_DT, SI_CREATION_TIME, SI_UPDATE_TS,    CUID, SI_OWNER, SI_BUSINESS_VIEW_INFO,    SI_PARENTID, SI_LOCAL_FILEPATH, ORIGINAL_FILE_NM,    SI_PARENT_FOLDER_CUID, UPDATE_DT, ENVIRONMENT,   SI_KIND,  CREATE_DATE,   UPDATE_DATE, LOAD_DATE, DATASOURCE_TYPE) SELECT a.SI_ID,  a.SI_PATH,  a.SI_FILE1,  a.REPORT_NM,  a.REPORT_FILE_PATH,  a.SOURCE_SYSTEM,  a.DATA_SOURCE_NM,  a.DATA_SOURCE2_NM,  a.DYNAMIC_PICK_LIST_NM,  a.theDate,  a.SI_CREATION_TIME,  a.SI_UPDATE_TS,  a.CUID,  a.SI_OWNER,  a.SI_BUSINESS_VIEW_INFO,  a.SI_PARENTID,  a.SI_LOCAL_FILEPATH,  a.ORIGINAL_FILE_NM,  a.SI_PARENT_FOLDER_CUID,  a.UPDATE_DT,  'Val',  a.SI_KIND,  a.creationDate,  a.updateTS,  a.LOAD_DT , a.datasource   from (SELECT cr.SI_ID,  cr.SI_PATH,  cr.SI_FILE1,  cr.REPORT_NM,  cr.REPORT_FILE_PATH,  cr.SOURCE_SYSTEM,  cr.DATA_SOURCE_NM,  cr.DATA_SOURCE2_NM,  cr.DYNAMIC_PICK_LIST_NM,  GETDATE() theDate,  cr.SI_CREATION_TIME,  cr.SI_UPDATE_TS,  cr.CUID,  cr.SI_OWNER,  cr.SI_BUSINESS_VIEW_INFO,  cr.SI_PARENTID,  cr.SI_LOCAL_FILEPATH,  cr.ORIGINAL_FILE_NM,  cr.SI_PARENT_FOLDER_CUID,  cr.UPDATE_DT,  'Val',  cr.SI_KIND,  CONVERT(DATETIME, CONVERT(DATE, cr.si_creation_time)) creationDate, CONVERT(DATETIME, CONVERT(DATE, cr.SI_UPDATE_TS)) updateTS,  CONVERT(DATETIME, CONVERT(DATE, GETDATE())) aDate, cr.DATASOURCE_TYPE datasource FROM   STG1_BO4d_creports cr) a left outer join (select cuid from RPT_BO4_REPORTS where environment='Val' and si_kind='CrystalReport')b on a.CUID = b.CUID where b.CUID   IS NULL";
			//msSQLInsertString = "insert into RPT_BO4_REPORTS (SI_ID, SI_PATH, SI_FILE1, REPORT_NM, REPORT_FILE_PATH, SOURCE_SYSTEM,    DATA_SOURCE_NM, DATA_SOURCE2_NM, DYNAMIC_PICK_LIST_NM,    LOAD_DT, SI_CREATION_TIME, SI_UPDATE_TS,    CUID, SI_OWNER, SI_BUSINESS_VIEW_INFO,    SI_PARENTID, SI_LOCAL_FILEPATH, ORIGINAL_FILE_NM,    SI_PARENT_FOLDER_CUID, UPDATE_DT, ENVIRONMENT,   SI_KIND,  CREATE_DATE,   UPDATE_DATE, LOAD_DATE, DATASOURCE_TYPE) SELECT cr.SI_ID,  cr.SI_PATH,  cr.SI_FILE1,  cr.REPORT_NM,  cr.REPORT_FILE_PATH,  cr.SOURCE_SYSTEM,  cr.DATA_SOURCE_NM,  cr.DATA_SOURCE2_NM,  cr.DYNAMIC_PICK_LIST_NM,  cr.LOAD_DT,  cr.SI_CREATION_TIME,  cr.SI_UPDATE_TS,  cr.CUID,  cr.SI_OWNER,  cr.SI_BUSINESS_VIEW_INFO,  cr.SI_PARENTID,  cr.SI_LOCAL_FILEPATH,  cr.ORIGINAL_FILE_NM,  cr.SI_PARENT_FOLDER_CUID,  cr.UPDATE_DT,  'Val' ENVIRONMENT,  cr.SI_KIND, CONVERT(DATETIME, CONVERT(DATE, cr.si_creation_time)) creation_Date, CONVERT(DATETIME, CONVERT(DATE, cr.SI_UPDATE_TS)) UPDATE_DATE,  CONVERT(DATETIME, CONVERT(DATE, LOAD_DT)) LOAD_DATE, cr.DATASOURCE_TYPE FROM   STG1_BO4v_creports cr left outer join (select cuid from RPT_BO4_REPORTS where environment='Val' and si_kind='CrystalReport') b on cr.CUID = b.CUID where b.CUID   IS NULL";
			msSQLInsertString = "insert into RPT_BO4_REPORTS (SI_ID, SI_PATH, SI_FILE1, REPORT_NM, REPORT_FILE_PATH, SOURCE_SYSTEM,    DATA_SOURCE_NM, DATA_SOURCE2_NM, DYNAMIC_PICK_LIST_NM,    LOAD_DT, SI_CREATION_TIME, SI_UPDATE_TS,    CUID, SI_OWNER, SI_BUSINESS_VIEW_INFO,    SI_PARENTID, SI_LOCAL_FILEPATH, ORIGINAL_FILE_NM,    SI_PARENT_FOLDER_CUID, UPDATE_DT, ENVIRONMENT,   SI_KIND,  CREATE_DATE,   UPDATE_DATE, LOAD_DATE, DATASOURCE_TYPE) SELECT cr.SI_ID,  cr.SI_PATH,  cr.SI_FILE1,  cr.REPORT_NM,  cr.REPORT_FILE_PATH,  cr.SOURCE_SYSTEM,  cr.DATA_SOURCE_NM,  cr.DATA_SOURCE2_NM,  cr.DYNAMIC_PICK_LIST_NM,  cr.LOAD_DT,  cr.SI_CREATION_TIME,  cr.SI_UPDATE_TS,  cr.CUID,  cr.SI_OWNER,  cr.SI_BUSINESS_VIEW_INFO,  cr.SI_PARENTID,  cr.SI_LOCAL_FILEPATH,  cr.ORIGINAL_FILE_NM,  cr.SI_PARENT_FOLDER_CUID,  cr.UPDATE_DT,  'Val' ENVIRONMENT,  cr.SI_KIND, CONVERT(DATETIME, CONVERT(DATE, cr.si_creation_time)) creation_Date, CONVERT(DATETIME, CONVERT(DATE, cr.SI_UPDATE_TS)) UPDATE_DATE,  CONVERT(DATETIME, CONVERT(DATE, LOAD_DT)) LOAD_DATE, cr.DATASOURCE_TYPE FROM   STG1_BO4v_creports cr left outer join (select SI_ID from RPT_BO4_REPORTS where environment='Val' and si_kind='CrystalReport') b on cr.SI_ID = b.SI_ID where b.SI_ID   IS NULL";
			System.out.println(msSQLInsertString);;
			try {
				//runMSSQLInsertQuery(strSQL)
				Helper.runMSSQLInsertQuery(msSQLInsertString);
			}
			catch (Exception ex){
				System.out.println(ex.getMessage() + " " + ex.getStackTrace());
			}

			// 'Prod reports
			// 'strSQL = "insert into RPT_BOXI_REPORTS SELECT SI_ID, SI_PATH, SI_FILE1, REPORT_NM, REPORT_FILE_PATH, SOURCE_SYSTEM,    DATA_SOURCE_NM, DATA_SOURCE2_NM, DYNAMIC_PICK_LIST_NM,    LOAD_DT, SI_CREATION_TIME, SI_UPDATE_TS,    CUID, SI_OWNER, SI_BUSINESS_VIEW_INFO,    SI_PARENTID, SI_LOCAL_FILEPATH, ORIGINAL_FILE_NM,    SI_PARENT_FOLDER_CUID, UPDATE_DT, 'Prod'   FROM STG1_MIGR_XIR2P_REPORTS where load_dt >= to_date('" & oracleOnTheHour(Now()) & "','DD-MM-YYYY HH24:MI:SS')"
			// 'strSQL = "insert into RPT_BO4_REPORTS (SI_ID, SI_PATH, SI_FILE1, REPORT_NM, REPORT_FILE_PATH, SOURCE_SYSTEM,    DATA_SOURCE_NM, DATA_SOURCE2_NM, DYNAMIC_PICK_LIST_NM,    LOAD_DT, SI_CREATION_TIME, SI_UPDATE_TS,    CUID, SI_OWNER, SI_BUSINESS_VIEW_INFO,    SI_PARENTID, SI_LOCAL_FILEPATH, ORIGINAL_FILE_NM,    SI_PARENT_FOLDER_CUID, UPDATE_DT, ENVIRONMENT,   SI_KIND,  CREATE_DATE,   UPDATE_DATE, LOAD_DATE) SELECT SI_ID, SI_PATH, SI_FILE1, REPORT_NM, REPORT_FILE_PATH, SOURCE_SYSTEM,    DATA_SOURCE_NM, DATA_SOURCE2_NM, DYNAMIC_PICK_LIST_NM,    sysdate, SI_CREATION_TIME, SI_UPDATE_TS,    CUID, SI_OWNER, SI_BUSINESS_VIEW_INFO,    SI_PARENTID, SI_LOCAL_FILEPATH, ORIGINAL_FILE_NM,    SI_PARENT_FOLDER_CUID, UPDATE_DT, 'Prod', SI_KIND,  trunc(si_creation_time), trunc(SI_UPDATE_TS), trunc(sysdate)   FROM STG1_BO4P_CREPORTS where load_dt >= to_date('" & oracleOnTheHour(Now()) & "','DD-MM-YYYY HH24:MI:SS')"
			//11/8/2012:  For some reason this query doesn't seem to work all the time.
			//msSQLInsertString = "insert into RPT_BO4_REPORTS (SI_ID, SI_PATH, SI_FILE1, REPORT_NM, REPORT_FILE_PATH, SOURCE_SYSTEM,    DATA_SOURCE_NM, DATA_SOURCE2_NM, DYNAMIC_PICK_LIST_NM,    LOAD_DT, SI_CREATION_TIME, SI_UPDATE_TS,    CUID, SI_OWNER, SI_BUSINESS_VIEW_INFO,    SI_PARENTID, SI_LOCAL_FILEPATH, ORIGINAL_FILE_NM,    SI_PARENT_FOLDER_CUID, UPDATE_DT, ENVIRONMENT,   SI_KIND,  CREATE_DATE,   UPDATE_DATE, LOAD_DATE) SELECT cr.SI_ID, cr.SI_PATH, cr.SI_FILE1, cr.REPORT_NM, cr.REPORT_FILE_PATH, cr.SOURCE_SYSTEM,    cr.DATA_SOURCE_NM, cr.DATA_SOURCE2_NM, cr.DYNAMIC_PICK_LIST_NM,    sysdate, cr.SI_CREATION_TIME, cr.SI_UPDATE_TS,    cr.CUID, cr.SI_OWNER, cr.SI_BUSINESS_VIEW_INFO,    cr.SI_PARENTID, cr.SI_LOCAL_FILEPATH, cr.ORIGINAL_FILE_NM,    cr.SI_PARENT_FOLDER_CUID, cr.UPDATE_DT, 'Prod', cr.SI_KIND,  trunc(cr.si_creation_time), trunc(cr.SI_UPDATE_TS), trunc(sysdate)   from RPT_BO4_reports v, STG1_BO4p_creports cr where cr.CUID = v.CUID (+) and v.CUID is null";
			//msSQLInsertString = "insert into RPT_BO4_REPORTS (SI_ID, SI_PATH, SI_FILE1, REPORT_NM, REPORT_FILE_PATH, SOURCE_SYSTEM,    DATA_SOURCE_NM, DATA_SOURCE2_NM, DYNAMIC_PICK_LIST_NM,    LOAD_DT, SI_CREATION_TIME, SI_UPDATE_TS,    CUID, SI_OWNER, SI_BUSINESS_VIEW_INFO,    SI_PARENTID, SI_LOCAL_FILEPATH, ORIGINAL_FILE_NM,    SI_PARENT_FOLDER_CUID, UPDATE_DT, ENVIRONMENT,   SI_KIND,  CREATE_DATE,   UPDATE_DATE, LOAD_DATE, DATASOURCE_TYPE) SELECT a.SI_ID,  a.SI_PATH,  a.SI_FILE1,  a.REPORT_NM,  a.REPORT_FILE_PATH,  a.SOURCE_SYSTEM,  a.DATA_SOURCE_NM,  a.DATA_SOURCE2_NM,  a.DYNAMIC_PICK_LIST_NM,  a.theDate,  a.SI_CREATION_TIME,  a.SI_UPDATE_TS,  a.CUID,  a.SI_OWNER,  a.SI_BUSINESS_VIEW_INFO,  a.SI_PARENTID,  a.SI_LOCAL_FILEPATH,  a.ORIGINAL_FILE_NM,  a.SI_PARENT_FOLDER_CUID,  a.UPDATE_DT,  'Prod',  a.SI_KIND,  a.creationDate,  a.updateTS,  a.LOAD_DT , a.datasource  from (SELECT cr.SI_ID,  cr.SI_PATH,  cr.SI_FILE1,  cr.REPORT_NM,  cr.REPORT_FILE_PATH,  cr.SOURCE_SYSTEM,  cr.DATA_SOURCE_NM,  cr.DATA_SOURCE2_NM,  cr.DYNAMIC_PICK_LIST_NM,  sysdate theDate,  cr.SI_CREATION_TIME,  cr.SI_UPDATE_TS,  cr.CUID,  cr.SI_OWNER,  cr.SI_BUSINESS_VIEW_INFO,  cr.SI_PARENTID,  cr.SI_LOCAL_FILEPATH,  cr.ORIGINAL_FILE_NM,  cr.SI_PARENT_FOLDER_CUID,  cr.UPDATE_DT,  'Prod',  cr.SI_KIND,  TRUNC(cr.si_creation_time) creationDate,  TRUNC(cr.SI_UPDATE_TS) updateTS,  TRUNC(sysdate) aDate, cr.DATASOURCE_TYPE datasource FROM   STG1_BO4p_creports cr) a,(select cuid from RPT_BO4_REPORTS where environment='Prod' and si_kind='CrystalReport')b WHERE a.CUID = b.CUID (+) AND b.CUID   IS NULL";
			//msSQLInsertString = "insert into RPT_BO4_REPORTS (SI_ID, SI_PATH, SI_FILE1, REPORT_NM, REPORT_FILE_PATH, SOURCE_SYSTEM,    DATA_SOURCE_NM, DATA_SOURCE2_NM, DYNAMIC_PICK_LIST_NM,    LOAD_DT, SI_CREATION_TIME, SI_UPDATE_TS,    CUID, SI_OWNER, SI_BUSINESS_VIEW_INFO,    SI_PARENTID, SI_LOCAL_FILEPATH, ORIGINAL_FILE_NM,    SI_PARENT_FOLDER_CUID, UPDATE_DT, ENVIRONMENT,   SI_KIND,  CREATE_DATE,   UPDATE_DATE, LOAD_DATE, DATASOURCE_TYPE) SELECT a.SI_ID,  a.SI_PATH,  a.SI_FILE1,  a.REPORT_NM,  a.REPORT_FILE_PATH,  a.SOURCE_SYSTEM,  a.DATA_SOURCE_NM,  a.DATA_SOURCE2_NM,  a.DYNAMIC_PICK_LIST_NM,  a.theDate,  a.SI_CREATION_TIME,  a.SI_UPDATE_TS,  a.CUID,  a.SI_OWNER,  a.SI_BUSINESS_VIEW_INFO,  a.SI_PARENTID,  a.SI_LOCAL_FILEPATH,  a.ORIGINAL_FILE_NM,  a.SI_PARENT_FOLDER_CUID,  a.UPDATE_DT,  'Prod' ENVIRONMENT,  a.SI_KIND,  a.creationDate,  a.updateTS,  a.LOAD_DT , a.datasource  from (SELECT cr.SI_ID,  cr.SI_PATH,  cr.SI_FILE1,  cr.REPORT_NM,  cr.REPORT_FILE_PATH,  cr.SOURCE_SYSTEM,  cr.DATA_SOURCE_NM,  cr.DATA_SOURCE2_NM,  cr.DYNAMIC_PICK_LIST_NM,  GETDATE() theDate,  cr.SI_CREATION_TIME,  cr.SI_UPDATE_TS,  cr.CUID,  cr.SI_OWNER,  cr.SI_BUSINESS_VIEW_INFO,  cr.SI_PARENTID,  cr.SI_LOCAL_FILEPATH,  cr.ORIGINAL_FILE_NM,  cr.SI_PARENT_FOLDER_CUID,  cr.UPDATE_DT,  'Prod' ENVIRONMENT,  cr.SI_KIND, CONVERT(DATETIME, CONVERT(DATE, cr.si_creation_time)) creationDate, CONVERT(DATETIME, CONVERT(DATE, cr.SI_UPDATE_TS)) updateTS,  CONVERT(DATETIME, CONVERT(DATE, GETDATE())) aDate, cr.DATASOURCE_TYPE datasource FROM   STG1_BO4p_creports cr) a left outer join (select cuid from RPT_BO4_REPORTS where environment='Prod' and si_kind='CrystalReport')b on a.CUID = b.CUID where b.CUID   IS NULL";
			//msSQLInsertString = "insert into RPT_BO4_REPORTS (SI_ID, SI_PATH, SI_FILE1, REPORT_NM, REPORT_FILE_PATH, SOURCE_SYSTEM,    DATA_SOURCE_NM, DATA_SOURCE2_NM, DYNAMIC_PICK_LIST_NM,    LOAD_DT, SI_CREATION_TIME, SI_UPDATE_TS,    CUID, SI_OWNER, SI_BUSINESS_VIEW_INFO,    SI_PARENTID, SI_LOCAL_FILEPATH, ORIGINAL_FILE_NM,    SI_PARENT_FOLDER_CUID, UPDATE_DT, ENVIRONMENT,   SI_KIND,  CREATE_DATE,   UPDATE_DATE, LOAD_DATE, DATASOURCE_TYPE) SELECT cr.SI_ID,  cr.SI_PATH,  cr.SI_FILE1,  cr.REPORT_NM,  cr.REPORT_FILE_PATH,  cr.SOURCE_SYSTEM,  cr.DATA_SOURCE_NM,  cr.DATA_SOURCE2_NM,  cr.DYNAMIC_PICK_LIST_NM,  cr.LOAD_DT,  cr.SI_CREATION_TIME,  cr.SI_UPDATE_TS,  cr.CUID,  cr.SI_OWNER,  cr.SI_BUSINESS_VIEW_INFO,  cr.SI_PARENTID,  cr.SI_LOCAL_FILEPATH,  cr.ORIGINAL_FILE_NM,  cr.SI_PARENT_FOLDER_CUID,  cr.UPDATE_DT,  'Prod' ENVIRONMENT,  cr.SI_KIND, CONVERT(DATETIME, CONVERT(DATE, cr.si_creation_time)) creation_Date, CONVERT(DATETIME, CONVERT(DATE, cr.SI_UPDATE_TS)) UPDATE_DATE,  CONVERT(DATETIME, CONVERT(DATE, LOAD_DT)) LOAD_DATE, cr.DATASOURCE_TYPE FROM   STG1_BO4p_creports cr left outer join (select cuid from RPT_BO4_REPORTS where environment='Prod' and si_kind='CrystalReport') b on cr.CUID = b.CUID where b.CUID   IS NULL";
			msSQLInsertString = "insert into RPT_BO4_REPORTS (SI_ID, SI_PATH, SI_FILE1, REPORT_NM, REPORT_FILE_PATH, SOURCE_SYSTEM,    DATA_SOURCE_NM, DATA_SOURCE2_NM, DYNAMIC_PICK_LIST_NM,    LOAD_DT, SI_CREATION_TIME, SI_UPDATE_TS,    CUID, SI_OWNER, SI_BUSINESS_VIEW_INFO,    SI_PARENTID, SI_LOCAL_FILEPATH, ORIGINAL_FILE_NM,    SI_PARENT_FOLDER_CUID, UPDATE_DT, ENVIRONMENT,   SI_KIND,  CREATE_DATE,   UPDATE_DATE, LOAD_DATE, DATASOURCE_TYPE) SELECT cr.SI_ID,  cr.SI_PATH,  cr.SI_FILE1,  cr.REPORT_NM,  cr.REPORT_FILE_PATH,  cr.SOURCE_SYSTEM,  cr.DATA_SOURCE_NM,  cr.DATA_SOURCE2_NM,  cr.DYNAMIC_PICK_LIST_NM,  cr.LOAD_DT,  cr.SI_CREATION_TIME,  cr.SI_UPDATE_TS,  cr.CUID,  cr.SI_OWNER,  cr.SI_BUSINESS_VIEW_INFO,  cr.SI_PARENTID,  cr.SI_LOCAL_FILEPATH,  cr.ORIGINAL_FILE_NM,  cr.SI_PARENT_FOLDER_CUID,  cr.UPDATE_DT,  'Prod' ENVIRONMENT,  cr.SI_KIND, CONVERT(DATETIME, CONVERT(DATE, cr.si_creation_time)) creation_Date, CONVERT(DATETIME, CONVERT(DATE, cr.SI_UPDATE_TS)) UPDATE_DATE,  CONVERT(DATETIME, CONVERT(DATE, LOAD_DT)) LOAD_DATE, cr.DATASOURCE_TYPE FROM   STG1_BO4p_creports cr left outer join (select SI_ID from RPT_BO4_REPORTS where environment='Prod' and si_kind='CrystalReport') b on cr.SI_ID = b.SI_ID where b.SI_ID   IS NULL";

			System.out.println(msSQLInsertString);;
			try {
				//runMSSQLInsertQuery(strSQL)
				Helper.runMSSQLInsertQuery(msSQLInsertString);
			}
			catch (Exception ex){
				System.out.println(ex.getMessage() + " " + ex.getStackTrace());
			}

			// 'QA reports
			// 'strSQL = "insert into RPT_BOXI_REPORTS SELECT SI_ID, SI_PATH, SI_FILE1, REPORT_NM, REPORT_FILE_PATH, SOURCE_SYSTEM,    DATA_SOURCE_NM, DATA_SOURCE2_NM, DYNAMIC_PICK_LIST_NM,    LOAD_DT, SI_CREATION_TIME, SI_UPDATE_TS,    CUID, SI_OWNER, SI_BUSINESS_VIEW_INFO,    SI_PARENTID, SI_LOCAL_FILEPATH, ORIGINAL_FILE_NM,    SI_PARENT_FOLDER_CUID, UPDATE_DT, 'QA'   FROM STG1_MIGR_XIR2Q_REPORTS where load_dt >= to_date('" & oracleOnTheHour(Now()) & "','DD-MM-YYYY HH24:MI:SS')"
			// 'strSQL = "insert into RPT_BO4_REPORTS (SI_ID, SI_PATH, SI_FILE1, REPORT_NM, REPORT_FILE_PATH, SOURCE_SYSTEM,    DATA_SOURCE_NM, DATA_SOURCE2_NM, DYNAMIC_PICK_LIST_NM,    LOAD_DT, SI_CREATION_TIME, SI_UPDATE_TS,    CUID, SI_OWNER, SI_BUSINESS_VIEW_INFO,    SI_PARENTID, SI_LOCAL_FILEPATH, ORIGINAL_FILE_NM,    SI_PARENT_FOLDER_CUID, UPDATE_DT, ENVIRONMENT,   SI_KIND,  CREATE_DATE,   UPDATE_DATE, LOAD_DATE) SELECT SI_ID, SI_PATH, SI_FILE1, REPORT_NM, REPORT_FILE_PATH, SOURCE_SYSTEM,    DATA_SOURCE_NM, DATA_SOURCE2_NM, DYNAMIC_PICK_LIST_NM,    sysdate, SI_CREATION_TIME, SI_UPDATE_TS,    CUID, SI_OWNER, SI_BUSINESS_VIEW_INFO,    SI_PARENTID, SI_LOCAL_FILEPATH, ORIGINAL_FILE_NM,    SI_PARENT_FOLDER_CUID, UPDATE_DT, 'QA', SI_KIND,   trunc(si_creation_time), trunc(SI_UPDATE_TS), trunc(sysdate)   FROM STG1_BO4Q_CREPORTS where load_dt >= to_date('" & oracleOnTheHour(Now()) & "','DD-MM-YYYY HH24:MI:SS')"
			//11/8/2012:  For some reason this query doesn't seem to work all the time.
			//msSQLInsertString = "insert into RPT_BO4_REPORTS (SI_ID, SI_PATH, SI_FILE1, REPORT_NM, REPORT_FILE_PATH, SOURCE_SYSTEM,    DATA_SOURCE_NM, DATA_SOURCE2_NM, DYNAMIC_PICK_LIST_NM,    LOAD_DT, SI_CREATION_TIME, SI_UPDATE_TS,    CUID, SI_OWNER, SI_BUSINESS_VIEW_INFO,    SI_PARENTID, SI_LOCAL_FILEPATH, ORIGINAL_FILE_NM,    SI_PARENT_FOLDER_CUID, UPDATE_DT, ENVIRONMENT,   SI_KIND,  CREATE_DATE,   UPDATE_DATE, LOAD_DATE) SELECT cr.SI_ID, cr.SI_PATH, cr.SI_FILE1, cr.REPORT_NM, cr.REPORT_FILE_PATH, cr.SOURCE_SYSTEM,    cr.DATA_SOURCE_NM, cr.DATA_SOURCE2_NM, cr.DYNAMIC_PICK_LIST_NM,    sysdate, cr.SI_CREATION_TIME, cr.SI_UPDATE_TS,    cr.CUID, cr.SI_OWNER, cr.SI_BUSINESS_VIEW_INFO,    cr.SI_PARENTID, cr.SI_LOCAL_FILEPATH, cr.ORIGINAL_FILE_NM,    cr.SI_PARENT_FOLDER_CUID, cr.UPDATE_DT, 'QA', cr.SI_KIND,  trunc(cr.si_creation_time), trunc(cr.SI_UPDATE_TS), trunc(sysdate)   from RPT_BO4_reports v, STG1_BO4q_creports cr where cr.CUID = v.CUID (+) and v.CUID is null";
			//msSQLInsertString = "insert into RPT_BO4_REPORTS (SI_ID, SI_PATH, SI_FILE1, REPORT_NM, REPORT_FILE_PATH, SOURCE_SYSTEM,    DATA_SOURCE_NM, DATA_SOURCE2_NM, DYNAMIC_PICK_LIST_NM,    LOAD_DT, SI_CREATION_TIME, SI_UPDATE_TS,    CUID, SI_OWNER, SI_BUSINESS_VIEW_INFO,    SI_PARENTID, SI_LOCAL_FILEPATH, ORIGINAL_FILE_NM,    SI_PARENT_FOLDER_CUID, UPDATE_DT, ENVIRONMENT,   SI_KIND,  CREATE_DATE,   UPDATE_DATE, LOAD_DATE, DATASOURCE_TYPE) SELECT a.SI_ID,  a.SI_PATH,  a.SI_FILE1,  a.REPORT_NM,  a.REPORT_FILE_PATH,  a.SOURCE_SYSTEM,  a.DATA_SOURCE_NM,  a.DATA_SOURCE2_NM,  a.DYNAMIC_PICK_LIST_NM,  a.theDate,  a.SI_CREATION_TIME,  a.SI_UPDATE_TS,  a.CUID,  a.SI_OWNER,  a.SI_BUSINESS_VIEW_INFO,  a.SI_PARENTID,  a.SI_LOCAL_FILEPATH,  a.ORIGINAL_FILE_NM,  a.SI_PARENT_FOLDER_CUID,  a.UPDATE_DT,  'QA',   a.SI_KIND,  a.creationDate,  a.updateTS,  a.LOAD_DT , a.datasource   from (SELECT cr.SI_ID,  cr.SI_PATH,  cr.SI_FILE1,  cr.REPORT_NM,  cr.REPORT_FILE_PATH,  cr.SOURCE_SYSTEM,  cr.DATA_SOURCE_NM,  cr.DATA_SOURCE2_NM,  cr.DYNAMIC_PICK_LIST_NM,  sysdate theDate,  cr.SI_CREATION_TIME,  cr.SI_UPDATE_TS,  cr.CUID,  cr.SI_OWNER,  cr.SI_BUSINESS_VIEW_INFO,  cr.SI_PARENTID,  cr.SI_LOCAL_FILEPATH,  cr.ORIGINAL_FILE_NM,  cr.SI_PARENT_FOLDER_CUID,  cr.UPDATE_DT,  'QA',  cr.SI_KIND,  TRUNC(cr.si_creation_time) creationDate,  TRUNC(cr.SI_UPDATE_TS) updateTS,  TRUNC(sysdate) aDate, cr.DATASOURCE_TYPE datasource FROM   STG1_BO4q_creports cr) a,(select cuid from RPT_BO4_REPORTS where environment='Prod' and si_kind='CrystalReport')b WHERE a.CUID = b.CUID (+) AND b.CUID   IS NULL";
			//msSQLInsertString = "insert into RPT_BO4_REPORTS (SI_ID, SI_PATH, SI_FILE1, REPORT_NM, REPORT_FILE_PATH, SOURCE_SYSTEM,    DATA_SOURCE_NM, DATA_SOURCE2_NM, DYNAMIC_PICK_LIST_NM,    LOAD_DT, SI_CREATION_TIME, SI_UPDATE_TS,    CUID, SI_OWNER, SI_BUSINESS_VIEW_INFO,    SI_PARENTID, SI_LOCAL_FILEPATH, ORIGINAL_FILE_NM,    SI_PARENT_FOLDER_CUID, UPDATE_DT, ENVIRONMENT,   SI_KIND,  CREATE_DATE,   UPDATE_DATE, LOAD_DATE, DATASOURCE_TYPE) SELECT a.SI_ID,  a.SI_PATH,  a.SI_FILE1,  a.REPORT_NM,  a.REPORT_FILE_PATH,  a.SOURCE_SYSTEM,  a.DATA_SOURCE_NM,  a.DATA_SOURCE2_NM,  a.DYNAMIC_PICK_LIST_NM,  a.theDate,  a.SI_CREATION_TIME,  a.SI_UPDATE_TS,  a.CUID,  a.SI_OWNER,  a.SI_BUSINESS_VIEW_INFO,  a.SI_PARENTID,  a.SI_LOCAL_FILEPATH,  a.ORIGINAL_FILE_NM,  a.SI_PARENT_FOLDER_CUID,  a.UPDATE_DT,  'QA',   a.SI_KIND,  a.creationDate,  a.updateTS,  a.LOAD_DT , a.datasource   from (SELECT cr.SI_ID,  cr.SI_PATH,  cr.SI_FILE1,  cr.REPORT_NM,  cr.REPORT_FILE_PATH,  cr.SOURCE_SYSTEM,  cr.DATA_SOURCE_NM,  cr.DATA_SOURCE2_NM,  cr.DYNAMIC_PICK_LIST_NM,  GETDATE() theDate,  cr.SI_CREATION_TIME,  cr.SI_UPDATE_TS,  cr.CUID,  cr.SI_OWNER,  cr.SI_BUSINESS_VIEW_INFO,  cr.SI_PARENTID,  cr.SI_LOCAL_FILEPATH,  cr.ORIGINAL_FILE_NM,  cr.SI_PARENT_FOLDER_CUID,  cr.UPDATE_DT,  'QA',  cr.SI_KIND,  CONVERT(DATETIME, CONVERT(DATE, cr.si_creation_time)) creationDate, CONVERT(DATETIME, CONVERT(DATE, cr.SI_UPDATE_TS)) updateTS,  CONVERT(DATETIME, CONVERT(DATE, GETDATE())) aDate, cr.DATASOURCE_TYPE datasource FROM   STG1_BO4q_creports cr) a left outer join (select cuid from RPT_BO4_REPORTS where environment='Prod' and si_kind='CrystalReport')b on a.CUID = b.CUID  where b.CUID   IS NULL";
			//msSQLInsertString = "insert into RPT_BO4_REPORTS (SI_ID, SI_PATH, SI_FILE1, REPORT_NM, REPORT_FILE_PATH, SOURCE_SYSTEM,    DATA_SOURCE_NM, DATA_SOURCE2_NM, DYNAMIC_PICK_LIST_NM,    LOAD_DT, SI_CREATION_TIME, SI_UPDATE_TS,    CUID, SI_OWNER, SI_BUSINESS_VIEW_INFO,    SI_PARENTID, SI_LOCAL_FILEPATH, ORIGINAL_FILE_NM,    SI_PARENT_FOLDER_CUID, UPDATE_DT, ENVIRONMENT,   SI_KIND,  CREATE_DATE,   UPDATE_DATE, LOAD_DATE, DATASOURCE_TYPE) SELECT cr.SI_ID,  cr.SI_PATH,  cr.SI_FILE1,  cr.REPORT_NM,  cr.REPORT_FILE_PATH,  cr.SOURCE_SYSTEM,  cr.DATA_SOURCE_NM,  cr.DATA_SOURCE2_NM,  cr.DYNAMIC_PICK_LIST_NM,  cr.LOAD_DT,  cr.SI_CREATION_TIME,  cr.SI_UPDATE_TS,  cr.CUID,  cr.SI_OWNER,  cr.SI_BUSINESS_VIEW_INFO,  cr.SI_PARENTID,  cr.SI_LOCAL_FILEPATH,  cr.ORIGINAL_FILE_NM,  cr.SI_PARENT_FOLDER_CUID,  cr.UPDATE_DT,  'QA' ENVIRONMENT,  cr.SI_KIND, CONVERT(DATETIME, CONVERT(DATE, cr.si_creation_time)) creation_Date, CONVERT(DATETIME, CONVERT(DATE, cr.SI_UPDATE_TS)) UPDATE_DATE,  CONVERT(DATETIME, CONVERT(DATE, LOAD_DT)) LOAD_DATE, cr.DATASOURCE_TYPE FROM   STG1_BO4q_creports cr left outer join (select cuid from RPT_BO4_REPORTS where environment='QA' and si_kind='CrystalReport') b on cr.CUID = b.CUID where b.CUID   IS NULL";
			msSQLInsertString = "insert into RPT_BO4_REPORTS (SI_ID, SI_PATH, SI_FILE1, REPORT_NM, REPORT_FILE_PATH, SOURCE_SYSTEM,    DATA_SOURCE_NM, DATA_SOURCE2_NM, DYNAMIC_PICK_LIST_NM,    LOAD_DT, SI_CREATION_TIME, SI_UPDATE_TS,    CUID, SI_OWNER, SI_BUSINESS_VIEW_INFO,    SI_PARENTID, SI_LOCAL_FILEPATH, ORIGINAL_FILE_NM,    SI_PARENT_FOLDER_CUID, UPDATE_DT, ENVIRONMENT,   SI_KIND,  CREATE_DATE,   UPDATE_DATE, LOAD_DATE, DATASOURCE_TYPE) SELECT cr.SI_ID,  cr.SI_PATH,  cr.SI_FILE1,  cr.REPORT_NM,  cr.REPORT_FILE_PATH,  cr.SOURCE_SYSTEM,  cr.DATA_SOURCE_NM,  cr.DATA_SOURCE2_NM,  cr.DYNAMIC_PICK_LIST_NM,  cr.LOAD_DT,  cr.SI_CREATION_TIME,  cr.SI_UPDATE_TS,  cr.CUID,  cr.SI_OWNER,  cr.SI_BUSINESS_VIEW_INFO,  cr.SI_PARENTID,  cr.SI_LOCAL_FILEPATH,  cr.ORIGINAL_FILE_NM,  cr.SI_PARENT_FOLDER_CUID,  cr.UPDATE_DT,  'QA' ENVIRONMENT,  cr.SI_KIND, CONVERT(DATETIME, CONVERT(DATE, cr.si_creation_time)) creation_Date, CONVERT(DATETIME, CONVERT(DATE, cr.SI_UPDATE_TS)) UPDATE_DATE,  CONVERT(DATETIME, CONVERT(DATE, LOAD_DT)) LOAD_DATE, cr.DATASOURCE_TYPE FROM   STG1_BO4q_creports cr left outer join (select SI_ID from RPT_BO4_REPORTS where environment='QA' and si_kind='CrystalReport') b on cr.SI_ID = b.SI_ID where b.SI_ID   IS NULL";
			System.out.println(msSQLInsertString);;
			try {
				//runMSSQLInsertQuery(strSQL)
				Helper.runMSSQLInsertQuery(msSQLInsertString);
			}
			catch (Exception ex){
				System.out.println(ex.getMessage() + " " + ex.getStackTrace());
			}


			//'Dev reports
			//'strSQL = "insert into RPT_BOXI_REPORTS SELECT SI_ID, SI_PATH, SI_FILE1, REPORT_NM, REPORT_FILE_PATH, SOURCE_SYSTEM,    DATA_SOURCE_NM, DATA_SOURCE2_NM, DYNAMIC_PICK_LIST_NM,    LOAD_DT, SI_CREATION_TIME, SI_UPDATE_TS,    CUID, SI_OWNER, SI_BUSINESS_VIEW_INFO,    SI_PARENTID, SI_LOCAL_FILEPATH, ORIGINAL_FILE_NM,    SI_PARENT_FOLDER_CUID, UPDATE_DT, 'Dev'   FROM STG1_MIGR_XIR2D_REPORTS where load_dt >= to_date('" & oracleOnTheHour(Now()) & "','DD-MM-YYYY HH24:MI:SS')"
			// 'strSQL = "insert into RPT_BO4_REPORTS (SI_ID, SI_PATH, SI_FILE1, REPORT_NM, REPORT_FILE_PATH, SOURCE_SYSTEM,    DATA_SOURCE_NM, DATA_SOURCE2_NM, DYNAMIC_PICK_LIST_NM,    LOAD_DT, SI_CREATION_TIME, SI_UPDATE_TS,    CUID, SI_OWNER, SI_BUSINESS_VIEW_INFO,    SI_PARENTID, SI_LOCAL_FILEPATH, ORIGINAL_FILE_NM,    SI_PARENT_FOLDER_CUID, UPDATE_DT, ENVIRONMENT,   SI_KIND,  CREATE_DATE,   UPDATE_DATE, LOAD_DATE) SELECT SI_ID, SI_PATH, SI_FILE1, REPORT_NM, REPORT_FILE_PATH, SOURCE_SYSTEM,    DATA_SOURCE_NM, DATA_SOURCE2_NM, DYNAMIC_PICK_LIST_NM,    sysdate, SI_CREATION_TIME, SI_UPDATE_TS,    CUID, SI_OWNER, SI_BUSINESS_VIEW_INFO,    SI_PARENTID, SI_LOCAL_FILEPATH, ORIGINAL_FILE_NM,    SI_PARENT_FOLDER_CUID, UPDATE_DT, 'Dev', SI_KIND,   trunc(si_creation_time), trunc(SI_UPDATE_TS), trunc(sysdate)   FROM STG1_BO4T_CREPORTS where load_dt >= to_date('" & oracleOnTheHour(Now()) & "','DD-MM-YYYY HH24:MI:SS')"
			//11/8/2012:  For some reason this query doesn't seem to work all the time.
			//msSQLInsertString = "insert into RPT_BO4_REPORTS (SI_ID, SI_PATH, SI_FILE1, REPORT_NM, REPORT_FILE_PATH, SOURCE_SYSTEM,    DATA_SOURCE_NM, DATA_SOURCE2_NM, DYNAMIC_PICK_LIST_NM,    LOAD_DT, SI_CREATION_TIME, SI_UPDATE_TS,    CUID, SI_OWNER, SI_BUSINESS_VIEW_INFO,    SI_PARENTID, SI_LOCAL_FILEPATH, ORIGINAL_FILE_NM,    SI_PARENT_FOLDER_CUID, UPDATE_DT, ENVIRONMENT,   SI_KIND,  CREATE_DATE,   UPDATE_DATE, LOAD_DATE) SELECT cr.SI_ID, cr.SI_PATH, cr.SI_FILE1, cr.REPORT_NM, cr.REPORT_FILE_PATH, cr.SOURCE_SYSTEM,    cr.DATA_SOURCE_NM, cr.DATA_SOURCE2_NM, cr.DYNAMIC_PICK_LIST_NM,    sysdate, cr.SI_CREATION_TIME, cr.SI_UPDATE_TS,    cr.CUID, cr.SI_OWNER, cr.SI_BUSINESS_VIEW_INFO,    cr.SI_PARENTID, cr.SI_LOCAL_FILEPATH, cr.ORIGINAL_FILE_NM,    cr.SI_PARENT_FOLDER_CUID, cr.UPDATE_DT, 'Dev', cr.SI_KIND,  trunc(cr.si_creation_time), trunc(cr.SI_UPDATE_TS), trunc(sysdate)   from RPT_BO4_reports v, STG1_BO4D_creports cr where cr.CUID = v.CUID (+) and v.CUID is null";
			//msSQLInsertString = "insert into RPT_BO4_REPORTS (SI_ID, SI_PATH, SI_FILE1, REPORT_NM, REPORT_FILE_PATH, SOURCE_SYSTEM,    DATA_SOURCE_NM, DATA_SOURCE2_NM, DYNAMIC_PICK_LIST_NM,    LOAD_DT, SI_CREATION_TIME, SI_UPDATE_TS,    CUID, SI_OWNER, SI_BUSINESS_VIEW_INFO,    SI_PARENTID, SI_LOCAL_FILEPATH, ORIGINAL_FILE_NM,    SI_PARENT_FOLDER_CUID, UPDATE_DT, ENVIRONMENT,   SI_KIND,  CREATE_DATE,   UPDATE_DATE, LOAD_DATE, DATASOURCE_TYPE) SELECT a.SI_ID,  a.SI_PATH,  a.SI_FILE1,  a.REPORT_NM,  a.REPORT_FILE_PATH,  a.SOURCE_SYSTEM,  a.DATA_SOURCE_NM,  a.DATA_SOURCE2_NM,  a.DYNAMIC_PICK_LIST_NM,  a.theDate,  a.SI_CREATION_TIME,  a.SI_UPDATE_TS,  a.CUID,  a.SI_OWNER,  a.SI_BUSINESS_VIEW_INFO,  a.SI_PARENTID,  a.SI_LOCAL_FILEPATH,  a.ORIGINAL_FILE_NM,  a.SI_PARENT_FOLDER_CUID,  a.UPDATE_DT,  'Dev',  a.SI_KIND,  a.creationDate,  a.updateTS,  a.LOAD_DT , a.datasource   from (SELECT cr.SI_ID,  cr.SI_PATH,  cr.SI_FILE1,  cr.REPORT_NM,  cr.REPORT_FILE_PATH,  cr.SOURCE_SYSTEM,  cr.DATA_SOURCE_NM,  cr.DATA_SOURCE2_NM,  cr.DYNAMIC_PICK_LIST_NM,  sysdate theDate,  cr.SI_CREATION_TIME,  cr.SI_UPDATE_TS,  cr.CUID,  cr.SI_OWNER,  cr.SI_BUSINESS_VIEW_INFO,  cr.SI_PARENTID,  cr.SI_LOCAL_FILEPATH,  cr.ORIGINAL_FILE_NM,  cr.SI_PARENT_FOLDER_CUID,  cr.UPDATE_DT,  'Dev',  cr.SI_KIND,  TRUNC(cr.si_creation_time) creationDate,  TRUNC(cr.SI_UPDATE_TS) updateTS,  TRUNC(sysdate) aDate, cr.DATASOURCE_TYPE datasource FROM   STG1_BO4d_creports cr) a,(select cuid from RPT_BO4_REPORTS where environment='Dev' and si_kind='CrystalReport')b WHERE a.CUID = b.CUID (+) AND b.CUID   IS NULL";
			//msSQLInsertString = "insert into RPT_BO4_REPORTS (SI_ID, SI_PATH, SI_FILE1, REPORT_NM, REPORT_FILE_PATH, SOURCE_SYSTEM,    DATA_SOURCE_NM, DATA_SOURCE2_NM, DYNAMIC_PICK_LIST_NM,    LOAD_DT, SI_CREATION_TIME, SI_UPDATE_TS,    CUID, SI_OWNER, SI_BUSINESS_VIEW_INFO,    SI_PARENTID, SI_LOCAL_FILEPATH, ORIGINAL_FILE_NM,    SI_PARENT_FOLDER_CUID, UPDATE_DT, ENVIRONMENT,   SI_KIND,  CREATE_DATE,   UPDATE_DATE, LOAD_DATE, DATASOURCE_TYPE) SELECT a.SI_ID,  a.SI_PATH,  a.SI_FILE1,  a.REPORT_NM,  a.REPORT_FILE_PATH,  a.SOURCE_SYSTEM,  a.DATA_SOURCE_NM,  a.DATA_SOURCE2_NM,  a.DYNAMIC_PICK_LIST_NM,  a.theDate,  a.SI_CREATION_TIME,  a.SI_UPDATE_TS,  a.CUID,  a.SI_OWNER,  a.SI_BUSINESS_VIEW_INFO,  a.SI_PARENTID,  a.SI_LOCAL_FILEPATH,  a.ORIGINAL_FILE_NM,  a.SI_PARENT_FOLDER_CUID,  a.UPDATE_DT,  'Dev',  a.SI_KIND,  a.creationDate,  a.updateTS,  a.LOAD_DT , a.datasource   from (SELECT cr.SI_ID,  cr.SI_PATH,  cr.SI_FILE1,  cr.REPORT_NM,  cr.REPORT_FILE_PATH,  cr.SOURCE_SYSTEM,  cr.DATA_SOURCE_NM,  cr.DATA_SOURCE2_NM,  cr.DYNAMIC_PICK_LIST_NM,  GETDATE() theDate,  cr.SI_CREATION_TIME,  cr.SI_UPDATE_TS,  cr.CUID,  cr.SI_OWNER,  cr.SI_BUSINESS_VIEW_INFO,  cr.SI_PARENTID,  cr.SI_LOCAL_FILEPATH,  cr.ORIGINAL_FILE_NM,  cr.SI_PARENT_FOLDER_CUID,  cr.UPDATE_DT,  'Dev',  cr.SI_KIND,  CONVERT(DATETIME, CONVERT(DATE, cr.si_creation_time)) creationDate, CONVERT(DATETIME, CONVERT(DATE, cr.SI_UPDATE_TS)) updateTS,  CONVERT(DATETIME, CONVERT(DATE, GETDATE())) aDate, cr.DATASOURCE_TYPE datasource FROM   STG1_BO4d_creports cr) a left outer join (select cuid from RPT_BO4_REPORTS where environment='Dev' and si_kind='CrystalReport')b on a.CUID = b.CUID where b.CUID   IS NULL";
			//msSQLInsertString = "insert into RPT_BO4_REPORTS (SI_ID, SI_PATH, SI_FILE1, REPORT_NM, REPORT_FILE_PATH, SOURCE_SYSTEM,    DATA_SOURCE_NM, DATA_SOURCE2_NM, DYNAMIC_PICK_LIST_NM,    LOAD_DT, SI_CREATION_TIME, SI_UPDATE_TS,    CUID, SI_OWNER, SI_BUSINESS_VIEW_INFO,    SI_PARENTID, SI_LOCAL_FILEPATH, ORIGINAL_FILE_NM,    SI_PARENT_FOLDER_CUID, UPDATE_DT, ENVIRONMENT,   SI_KIND,  CREATE_DATE,   UPDATE_DATE, LOAD_DATE, DATASOURCE_TYPE) SELECT cr.SI_ID,  cr.SI_PATH,  cr.SI_FILE1,  cr.REPORT_NM,  cr.REPORT_FILE_PATH,  cr.SOURCE_SYSTEM,  cr.DATA_SOURCE_NM,  cr.DATA_SOURCE2_NM,  cr.DYNAMIC_PICK_LIST_NM,  cr.LOAD_DT,  cr.SI_CREATION_TIME,  cr.SI_UPDATE_TS,  cr.CUID,  cr.SI_OWNER,  cr.SI_BUSINESS_VIEW_INFO,  cr.SI_PARENTID,  cr.SI_LOCAL_FILEPATH,  cr.ORIGINAL_FILE_NM,  cr.SI_PARENT_FOLDER_CUID,  cr.UPDATE_DT,  'Dev' ENVIRONMENT,  cr.SI_KIND, CONVERT(DATETIME, CONVERT(DATE, cr.si_creation_time)) creation_Date, CONVERT(DATETIME, CONVERT(DATE, cr.SI_UPDATE_TS)) UPDATE_DATE,  CONVERT(DATETIME, CONVERT(DATE, LOAD_DT)) LOAD_DATE, cr.DATASOURCE_TYPE FROM   STG1_BO4d_creports cr left outer join (select cuid from RPT_BO4_REPORTS where environment='Dev' and si_kind='CrystalReport') b on cr.CUID = b.CUID where b.CUID   IS NULL";
			msSQLInsertString = "insert into RPT_BO4_REPORTS (SI_ID, SI_PATH, SI_FILE1, REPORT_NM, REPORT_FILE_PATH, SOURCE_SYSTEM,    DATA_SOURCE_NM, DATA_SOURCE2_NM, DYNAMIC_PICK_LIST_NM,    LOAD_DT, SI_CREATION_TIME, SI_UPDATE_TS,    CUID, SI_OWNER, SI_BUSINESS_VIEW_INFO,    SI_PARENTID, SI_LOCAL_FILEPATH, ORIGINAL_FILE_NM,    SI_PARENT_FOLDER_CUID, UPDATE_DT, ENVIRONMENT,   SI_KIND,  CREATE_DATE,   UPDATE_DATE, LOAD_DATE, DATASOURCE_TYPE) SELECT cr.SI_ID,  cr.SI_PATH,  cr.SI_FILE1,  cr.REPORT_NM,  cr.REPORT_FILE_PATH,  cr.SOURCE_SYSTEM,  cr.DATA_SOURCE_NM,  cr.DATA_SOURCE2_NM,  cr.DYNAMIC_PICK_LIST_NM,  cr.LOAD_DT,  cr.SI_CREATION_TIME,  cr.SI_UPDATE_TS,  cr.CUID,  cr.SI_OWNER,  cr.SI_BUSINESS_VIEW_INFO,  cr.SI_PARENTID,  cr.SI_LOCAL_FILEPATH,  cr.ORIGINAL_FILE_NM,  cr.SI_PARENT_FOLDER_CUID,  cr.UPDATE_DT,  'Dev' ENVIRONMENT,  cr.SI_KIND, CONVERT(DATETIME, CONVERT(DATE, cr.si_creation_time)) creation_Date, CONVERT(DATETIME, CONVERT(DATE, cr.SI_UPDATE_TS)) UPDATE_DATE,  CONVERT(DATETIME, CONVERT(DATE, LOAD_DT)) LOAD_DATE, cr.DATASOURCE_TYPE FROM   STG1_BO4d_creports cr left outer join (select SI_ID from RPT_BO4_REPORTS where environment='Dev' and si_kind='CrystalReport') b on cr.SI_ID = b.SI_ID where b.SI_ID   IS NULL";
			System.out.println(msSQLInsertString);;
			try {
				//runMSSQLInsertQuery(strSQL)
				Helper.runMSSQLInsertQuery(msSQLInsertString);
			}
			catch (Exception ex){
				System.out.println(ex.getMessage() + " " + ex.getStackTrace());
			}


			//''''''''''''''''''''''''''Business Views''''''''''''''''''''''''''''''''
			//'Now stage three business view table


			//'oraUpdateString = "UPDATE RPT_BO4_BUS_VIEWS t1 set data_foundation_nm = (select t2.si_name from STG1_BO4T_ t2 where t1.environment = t2.environment and t1.CUID = "

			//'Val Business Views
			msSQLInsertString = "INSERT INTO RPT_BO4_BUS_VIEWS (   SI_CREATION_TIME, SI_KIND, SI_PROGID,    SI_NAME, SI_ID, SI_PARENT_CUID,    LOAD_DT, FOLDER, CUID,    SI_PARENT_FOLDER_CUID, DATA_CONNECTION_NM, DATA_FOUNDATION_NM, ENVIRONMENT) select bvs.SI_CREATION_TIME, bvs.SI_KIND, bvs.SI_PROGID,    bvs.SI_NAME, bvs.SI_ID, bvs.SI_PARENT_CUID,    bvs.LOAD_DT, bvs.FOLDER, bvs.CUID,    bvs.SI_PARENT_FOLDER_CUID, bvs.DATA_CONNECTION_NM, bvs.DATA_FOUNDATION_NM, 'Val' ENVIRONMENT from STG1_BO4v_bus_views bvs left outer join (select CUID from RPT_BO4_BUS_VIEWS where environment='Val') bv on bvs.CUID = bv.CUID where bv.CUID is null";
			System.out.println(msSQLInsertString);
			try {
				//runMSSQLInsertQuery(strSQL)
				Helper.runMSSQLInsertQuery(msSQLInsertString);
			}
			catch (Exception ex){
				System.out.println(ex.getMessage() + " " + ex.getStackTrace());
			}


			//'Prod Business Views
			//'strSQL = "INSERT INTO RPT_BO4_BUS_VIEWS (   SI_CREATION_TIME, SI_KIND, SI_PROGID,    SI_NAME, SI_ID, SI_PARENT_CUID,    LOAD_DT, FOLDER, CUID,    SI_PARENT_FOLDER_CUID, DATA_CONNECTION_NM, DATA_FOUNDATION_NM, ENVIRONMENT) select SI_CREATION_TIME, SI_KIND, SI_PROGID,    SI_NAME, SI_ID, SI_PARENT_CUID,    LOAD_DT, FOLDER, CUID,    SI_PARENT_FOLDER_CUID, DATA_CONNECTION_NM, DATA_FOUNDATION_NM, 'Prod' from STG1_BO4p_bus_views where load_dt >= to_date('" & oracleOnTheHour(Now()) & "','DD-MM-YYYY HH24:MI:SS')"
			//msSQLInsertString = "INSERT INTO RPT_BO4_BUS_VIEWS (   SI_CREATION_TIME, SI_KIND, SI_PROGID,    SI_NAME, SI_ID, SI_PARENT_CUID,    LOAD_DT, FOLDER, CUID,    SI_PARENT_FOLDER_CUID, DATA_CONNECTION_NM, DATA_FOUNDATION_NM, ENVIRONMENT) select bvs.SI_CREATION_TIME, bvs.SI_KIND, bvs.SI_PROGID,    bvs.SI_NAME, bvs.SI_ID, bvs.SI_PARENT_CUID,    bvs.LOAD_DT, bvs.FOLDER, bvs.CUID,    bvs.SI_PARENT_FOLDER_CUID, bvs.DATA_CONNECTION_NM, bvs.DATA_FOUNDATION_NM, 'Prod' ENVIRONMENT from STG1_BO4p_bus_views bvs  left outer join RPT_BO4_BUS_VIEWS bv on bvs.CUID = bv.CUID where bv.CUID is null";
			//msSQLInsertString = "INSERT INTO RPT_BO4_BUS_VIEWS (   SI_CREATION_TIME, SI_KIND, SI_PROGID,    SI_NAME, SI_ID, SI_PARENT_CUID,    LOAD_DT, FOLDER, CUID,    SI_PARENT_FOLDER_CUID, DATA_CONNECTION_NM, DATA_FOUNDATION_NM, ENVIRONMENT) select bvs.SI_CREATION_TIME, bvs.SI_KIND, bvs.SI_PROGID,    bvs.SI_NAME, bvs.SI_ID, bvs.SI_PARENT_CUID,    bvs.LOAD_DT, bvs.FOLDER, bvs.CUID,    bvs.SI_PARENT_FOLDER_CUID, bvs.DATA_CONNECTION_NM, bvs.DATA_FOUNDATION_NM, 'Prod' ENVIRONMENT from STG1_BO4p_bus_views bvs left outer join (select CUID from RPT_BO4_BUS_VIEWS where environment='Prod') bv on bvs.CUID = bv.CUID where bv.CUID is null";
			//msSQLInsertString = "INSERT INTO RPT_BO4_BUS_VIEWS (   SI_CREATION_TIME, SI_KIND, SI_PROGID,    SI_NAME, SI_ID, SI_PARENT_CUID,    LOAD_DT, FOLDER, CUID,    SI_PARENT_FOLDER_CUID, DATA_CONNECTION_NM, DATA_FOUNDATION_NM, ENVIRONMENT) select bvs.SI_CREATION_TIME, bvs.SI_KIND, bvs.SI_PROGID,    bvs.SI_NAME, bvs.SI_ID, bvs.SI_PARENT_CUID,    bvs.LOAD_DT, bvs.FOLDER, bvs.CUID,    bvs.SI_PARENT_FOLDER_CUID, bvs.DATA_CONNECTION_NM, bvs.DATA_FOUNDATION_NM, 'Prod' ENVIRONMENT from STG1_BO4p_bus_views bvs left outer join (select si_id from RPT_BO4_BUS_VIEWS where environment='Prod') bv on bvs.si_id = bv.CUID where bv.si_id is null";
			msSQLInsertString = "INSERT INTO RPT_BO4_BUS_VIEWS (   SI_CREATION_TIME, SI_KIND, SI_PROGID,    SI_NAME, SI_ID, SI_PARENT_CUID,    LOAD_DT, FOLDER, CUID,    SI_PARENT_FOLDER_CUID, DATA_CONNECTION_NM, DATA_FOUNDATION_NM, ENVIRONMENT) select bvs.SI_CREATION_TIME, bvs.SI_KIND, bvs.SI_PROGID,    bvs.SI_NAME, bvs.SI_ID, bvs.SI_PARENT_CUID,    bvs.LOAD_DT, bvs.FOLDER, bvs.CUID,    bvs.SI_PARENT_FOLDER_CUID, bvs.DATA_CONNECTION_NM, bvs.DATA_FOUNDATION_NM, 'Prod' ENVIRONMENT from STG1_BO4p_bus_views bvs left outer join (select CUID from RPT_BO4_BUS_VIEWS where environment='Prod') bv on bvs.CUID = bv.CUID where bv.CUID is null";
			System.out.println(msSQLInsertString);
			try {
				//runMSSQLInsertQuery(strSQL)
				Helper.runMSSQLInsertQuery(msSQLInsertString);
			}
			catch (Exception ex){
				System.out.println(ex.getMessage() + " " + ex.getStackTrace());
			}

			//'QA Business Views
			//'strSQL = "INSERT INTO RPT_BO4_BUS_VIEWS (   SI_CREATION_TIME, SI_KIND, SI_PROGID,    SI_NAME, SI_ID, SI_PARENT_CUID,    LOAD_DT, FOLDER, CUID,    SI_PARENT_FOLDER_CUID, DATA_CONNECTION_NM, DATA_FOUNDATION_NM, ENVIRONMENT) select SI_CREATION_TIME, SI_KIND, SI_PROGID,    SI_NAME, SI_ID, SI_PARENT_CUID,    LOAD_DT, FOLDER, CUID,    SI_PARENT_FOLDER_CUID, DATA_CONNECTION_NM, DATA_FOUNDATION_NM, 'QA' from STG1_BO4q_bus_views where load_dt >= to_date('" & oracleOnTheHour(Now()) & "','DD-MM-YYYY HH24:MI:SS')"
			//msSQLInsertString = "INSERT INTO RPT_BO4_BUS_VIEWS (   SI_CREATION_TIME, SI_KIND, SI_PROGID,    SI_NAME, SI_ID, SI_PARENT_CUID,    LOAD_DT, FOLDER, CUID,    SI_PARENT_FOLDER_CUID, DATA_CONNECTION_NM, DATA_FOUNDATION_NM, ENVIRONMENT) select bvs.SI_CREATION_TIME, bvs.SI_KIND, bvs.SI_PROGID,    bvs.SI_NAME, bvs.SI_ID, bvs.SI_PARENT_CUID,    bvs.LOAD_DT, bvs.FOLDER, bvs.CUID,    bvs.SI_PARENT_FOLDER_CUID, bvs.DATA_CONNECTION_NM, bvs.DATA_FOUNDATION_NM, 'QA' ENVIRONMENT from STG1_BO4q_bus_views bvs left outer join (select si_id from RPT_BO4_BUS_VIEWS where environment='QA') bv on bvs.si_id = bv.CUID where bv.si_id is null";
			msSQLInsertString = "INSERT INTO RPT_BO4_BUS_VIEWS (   SI_CREATION_TIME, SI_KIND, SI_PROGID,    SI_NAME, SI_ID, SI_PARENT_CUID,    LOAD_DT, FOLDER, CUID,    SI_PARENT_FOLDER_CUID, DATA_CONNECTION_NM, DATA_FOUNDATION_NM, ENVIRONMENT) select bvs.SI_CREATION_TIME, bvs.SI_KIND, bvs.SI_PROGID,    bvs.SI_NAME, bvs.SI_ID, bvs.SI_PARENT_CUID,    bvs.LOAD_DT, bvs.FOLDER, bvs.CUID,    bvs.SI_PARENT_FOLDER_CUID, bvs.DATA_CONNECTION_NM, bvs.DATA_FOUNDATION_NM, 'QA' ENVIRONMENT from STG1_BO4q_bus_views bvs left outer join (select CUID from RPT_BO4_BUS_VIEWS where environment='QA') bv on bvs.CUID = bv.CUID where bv.CUID is null";
			System.out.println(msSQLInsertString);
			try {
				//runMSSQLInsertQuery(strSQL)
				Helper.runMSSQLInsertQuery(msSQLInsertString);
			}
			catch (Exception ex){
				System.out.println(ex.getMessage() + " " + ex.getStackTrace());
			}


			//'Dev Business Views
			//'strSQL = "INSERT INTO RPT_BO4_BUS_VIEWS (   SI_CREATION_TIME, SI_KIND, SI_PROGID,    SI_NAME, SI_ID, SI_PARENT_CUID,    LOAD_DT, FOLDER, CUID,    SI_PARENT_FOLDER_CUID, DATA_CONNECTION_NM, DATA_FOUNDATION_NM, ENVIRONMENT) select SI_CREATION_TIME, SI_KIND, SI_PROGID,    SI_NAME, SI_ID, SI_PARENT_CUID,    LOAD_DT, FOLDER, CUID,    SI_PARENT_FOLDER_CUID, DATA_CONNECTION_NM, DATA_FOUNDATION_NM, 'Dev' from STG1_BO4T_bus_views where load_dt >= to_date('" & oracleOnTheHour(Now()) & "','DD-MM-YYYY HH24:MI:SS')"
			//msSQLInsertString = "INSERT INTO RPT_BO4_BUS_VIEWS (   SI_CREATION_TIME, SI_KIND, SI_PROGID,    SI_NAME, SI_ID, SI_PARENT_CUID,    LOAD_DT, FOLDER, CUID,    SI_PARENT_FOLDER_CUID, DATA_CONNECTION_NM, DATA_FOUNDATION_NM, ENVIRONMENT) select bvs.SI_CREATION_TIME, bvs.SI_KIND, bvs.SI_PROGID,    bvs.SI_NAME, bvs.SI_ID, bvs.SI_PARENT_CUID,    bvs.LOAD_DT, bvs.FOLDER, bvs.CUID,    bvs.SI_PARENT_FOLDER_CUID, bvs.DATA_CONNECTION_NM, bvs.DATA_FOUNDATION_NM, 'Dev' from STG1_BO4D_bus_views bvs, RPT_BO4_BUS_VIEWS bv where bvs.CUID = bv.CUID (+) and bv.CUID is null";
			//msSQLInsertString = "INSERT INTO RPT_BO4_BUS_VIEWS (   SI_CREATION_TIME, SI_KIND, SI_PROGID,    SI_NAME, SI_ID, SI_PARENT_CUID,    LOAD_DT, FOLDER, CUID,    SI_PARENT_FOLDER_CUID, DATA_CONNECTION_NM, DATA_FOUNDATION_NM, ENVIRONMENT) select bvs.SI_CREATION_TIME, bvs.SI_KIND, bvs.SI_PROGID,    bvs.SI_NAME, bvs.SI_ID, bvs.SI_PARENT_CUID,    bvs.LOAD_DT, bvs.FOLDER, bvs.CUID,    bvs.SI_PARENT_FOLDER_CUID, bvs.DATA_CONNECTION_NM, bvs.DATA_FOUNDATION_NM, 'Dev' from STG1_BO4D_bus_views bvs left outer join RPT_BO4_BUS_VIEWS bv on bvs.CUID = bv.CUID where bv.CUID is null";
			//msSQLInsertString = "INSERT INTO RPT_BO4_BUS_VIEWS (   SI_CREATION_TIME, SI_KIND, SI_PROGID,    SI_NAME, SI_ID, SI_PARENT_CUID,    LOAD_DT, FOLDER, CUID,    SI_PARENT_FOLDER_CUID, DATA_CONNECTION_NM, DATA_FOUNDATION_NM, ENVIRONMENT) select bvs.SI_CREATION_TIME, bvs.SI_KIND, bvs.SI_PROGID,    bvs.SI_NAME, bvs.SI_ID, bvs.SI_PARENT_CUID,    bvs.LOAD_DT, bvs.FOLDER, bvs.CUID,    bvs.SI_PARENT_FOLDER_CUID, bvs.DATA_CONNECTION_NM, bvs.DATA_FOUNDATION_NM, 'Dev' ENVIRONMENT from STG1_BO4d_bus_views bvs left outer join (select si_id from RPT_BO4_BUS_VIEWS where environment='Dev') bv on bvs.si_id = bv.CUID where bv.si_id is null";
			msSQLInsertString = "INSERT INTO RPT_BO4_BUS_VIEWS (   SI_CREATION_TIME, SI_KIND, SI_PROGID,    SI_NAME, SI_ID, SI_PARENT_CUID,    LOAD_DT, FOLDER, CUID,    SI_PARENT_FOLDER_CUID, DATA_CONNECTION_NM, DATA_FOUNDATION_NM, ENVIRONMENT) select bvs.SI_CREATION_TIME, bvs.SI_KIND, bvs.SI_PROGID,    bvs.SI_NAME, bvs.SI_ID, bvs.SI_PARENT_CUID,    bvs.LOAD_DT, bvs.FOLDER, bvs.CUID,    bvs.SI_PARENT_FOLDER_CUID, bvs.DATA_CONNECTION_NM, bvs.DATA_FOUNDATION_NM, 'Dev' ENVIRONMENT from STG1_BO4d_bus_views bvs left outer join (select CUID from RPT_BO4_BUS_VIEWS where environment='Dev') bv on bvs.CUID = bv.CUID where bv.CUID is null";
			System.out.println(msSQLInsertString);
			try {
				//runMSSQLInsertQuery(strSQL)
				Helper.runMSSQLInsertQuery(msSQLInsertString);
			}
			catch (Exception ex){
				System.out.println(ex.getMessage() + " " + ex.getStackTrace());
			}


			// '''''''''''''''''''''''UNIVERSES'''''''''''''''''''''''''''''''

			//Determine the last time records were inserted

			//Oct 22, 2014:  I rewrote to look for missing records in stage 3 rather than going by si_id
			//May 8, 2014:  discovered that no records were getting inserted from stage1 to stage3 because it's asking for 
			//any record newer than now.  I have no idea why I would have done this.
			//I'm going to compare by si_id because it's more complicated to do it by last load date and risk dups.
			//String maxid = "0";

			//'Val Universes
			//msSQLInsertString = "INSERT INTO RPT_BO4_UNIVERSES (   CUID, SI_KIND, SI_UPDATE_TS,    SI_NAME, SI_ID, SI_PARENT_FOLDER_CUID,    SI_DESCRIPTION, SI_DATACONNECTION, ENVIRONMENT,     LOAD_DT, PATH,    SI_CREATION_TIME, DATASOURCE_TYPE) select u.CUID, u.SI_KIND, u.SI_UPDATE_TS,    u.SI_NAME, u.SI_ID, u.SI_PARENT_FOLDER_CUID,    u.SI_DESCRIPTION, u.SI_DATACONNECTION, 'Val'  environment, u.LOAD_DT, u.PATH,    u.SI_CREATION_TIME, u.DATASOURCE_TYPE from STG1_BO4v_universes u left outer join (select CUID from RPT_BO4_UNIVERSES where environment='Val') ru on u.CUID = ru.CUID where ru.CUID is null"; 
			msSQLInsertString = "INSERT INTO RPT_BO4_UNIVERSES (   CUID, SI_KIND, SI_UPDATE_TS,    SI_NAME, SI_ID, SI_PARENT_FOLDER_CUID,    SI_DESCRIPTION, SI_DATACONNECTION, ENVIRONMENT,     LOAD_DT, PATH,    SI_CREATION_TIME, DATASOURCE_TYPE) select u.CUID, u.SI_KIND, u.SI_UPDATE_TS,    u.SI_NAME, u.SI_ID, u.SI_PARENT_FOLDER_CUID,    u.SI_DESCRIPTION, u.SI_DATACONNECTION, 'Val'  environment, u.LOAD_DT, u.PATH,    u.SI_CREATION_TIME, u.DATASOURCE_TYPE from STG1_BO4v_universes u left outer join (select SI_ID from RPT_BO4_UNIVERSES where environment='Val') ru on u.SI_ID = ru.SI_ID where ru.SI_ID is null";
			System.out.println(msSQLInsertString);
			try {
				//runMSSQLInsertQuery(strSQL)
				Helper.runMSSQLInsertQuery(msSQLInsertString);
			}
			catch (Exception ex){
				System.out.println(ex.getMessage() + " " + ex.getStackTrace());
			}

			//'Prod Universes
			//maxid = Helper.getMaxID("select max(si_id) maxID from STG1_BO4p_universes");

			//msSQLInsertString = "INSERT INTO RPT_BO4_UNIVERSES (   CUID, SI_KIND, SI_UPDATE_TS,    SI_NAME, SI_ID, SI_PARENT_FOLDER_CUID,    SI_DESCRIPTION, SI_DATACONNECTION, ENVIRONMENT,    MACHINE_NM, LOAD_DT, PATH,    SI_CREATION_TIME, DATASOURCE_TYPE)  select CUID, SI_KIND, SI_UPDATE_TS,    SI_NAME, SI_ID, SI_PARENT_FOLDER_CUID,    SI_DESCRIPTION, SI_DATACONNECTION, 'Prod',    environment, LOAD_DT, PATH,    SI_CREATION_TIME, DATASOURCE_TYPE from STG1_BO4p_universes where load_dt >= to_date('" + Helper.oracleOnTheHour(now) + "','DD-MM-YYYY HH24:MI:SS')";
			//Now that universes are loaded once a day, I'm not sure that the function OracleOnTheHour() is really need.  I just need to make sure I load only new universes.
			//msSQLInsertString = "INSERT INTO RPT_BO4_UNIVERSES (   CUID, SI_KIND, SI_UPDATE_TS,    SI_NAME, SI_ID, SI_PARENT_FOLDER_CUID,    SI_DESCRIPTION, SI_DATACONNECTION, ENVIRONMENT,    MACHINE_NM, LOAD_DT, PATH,    SI_CREATION_TIME, DATASOURCE_TYPE)  select CUID, SI_KIND, SI_UPDATE_TS,    SI_NAME, SI_ID, SI_PARENT_FOLDER_CUID,    SI_DESCRIPTION, SI_DATACONNECTION, 'Prod',    environment, LOAD_DT, PATH,    SI_CREATION_TIME, DATASOURCE_TYPE from STG1_BO4p_universes where si_id > maxid";
			msSQLInsertString = "INSERT INTO RPT_BO4_UNIVERSES (   CUID, SI_KIND, SI_UPDATE_TS,    SI_NAME, SI_ID, SI_PARENT_FOLDER_CUID,    SI_DESCRIPTION, SI_DATACONNECTION, ENVIRONMENT,   LOAD_DT, PATH,    SI_CREATION_TIME, DATASOURCE_TYPE) select u.CUID, u.SI_KIND, u.SI_UPDATE_TS,    u.SI_NAME, u.SI_ID, u.SI_PARENT_FOLDER_CUID,    u.SI_DESCRIPTION, u.SI_DATACONNECTION, 'Prod'  environment, u.LOAD_DT, u.PATH,    u.SI_CREATION_TIME, u.DATASOURCE_TYPE from STG1_BO4p_universes u left outer join (select SI_ID from RPT_BO4_UNIVERSES where environment='Prod') ru on u.SI_ID = ru.SI_ID where ru.SI_ID is null"; 
			System.out.println(msSQLInsertString);
			try {
				//runMSSQLInsertQuery(strSQL)
				Helper.runMSSQLInsertQuery(msSQLInsertString);
			}
			catch (Exception ex){
				System.out.println(ex.getMessage() + " " + ex.getStackTrace());
			}

			//'QA Universes
			//maxid = Helper.getMaxID("select max(si_id) maxID from STG1_BO4q_universes");
			//msSQLInsertString = "INSERT INTO RPT_BO4_UNIVERSES (   CUID, SI_KIND, SI_UPDATE_TS,    SI_NAME, SI_ID, SI_PARENT_FOLDER_CUID,    SI_DESCRIPTION, SI_DATACONNECTION, ENVIRONMENT,    MACHINE_NM, LOAD_DT, PATH,    SI_CREATION_TIME, DATASOURCE_TYPE)  select CUID, SI_KIND, SI_UPDATE_TS,    SI_NAME, SI_ID, SI_PARENT_FOLDER_CUID,    SI_DESCRIPTION, SI_DATACONNECTION, 'QA',    environment, LOAD_DT, PATH,    SI_CREATION_TIME, DATASOURCE_TYPE from STG1_BO4q_universes where load_dt >= to_date('" + Helper.oracleOnTheHour(now) + "','DD-MM-YYYY HH24:MI:SS')";
			//msSQLInsertString = "INSERT INTO RPT_BO4_UNIVERSES (   CUID, SI_KIND, SI_UPDATE_TS,    SI_NAME, SI_ID, SI_PARENT_FOLDER_CUID,    SI_DESCRIPTION, SI_DATACONNECTION, ENVIRONMENT,    MACHINE_NM, LOAD_DT, PATH,    SI_CREATION_TIME, DATASOURCE_TYPE)  select CUID, SI_KIND, SI_UPDATE_TS,    SI_NAME, SI_ID, SI_PARENT_FOLDER_CUID,    SI_DESCRIPTION, SI_DATACONNECTION, 'Prod',    environment, LOAD_DT, PATH,    SI_CREATION_TIME, DATASOURCE_TYPE from STG1_BO4q_universes where si_id > maxid";
			msSQLInsertString = "INSERT INTO RPT_BO4_UNIVERSES (   CUID, SI_KIND, SI_UPDATE_TS,    SI_NAME, SI_ID, SI_PARENT_FOLDER_CUID,    SI_DESCRIPTION, SI_DATACONNECTION, ENVIRONMENT,   LOAD_DT, PATH,    SI_CREATION_TIME, DATASOURCE_TYPE) select u.CUID, u.SI_KIND, u.SI_UPDATE_TS,    u.SI_NAME, u.SI_ID, u.SI_PARENT_FOLDER_CUID,    u.SI_DESCRIPTION, u.SI_DATACONNECTION, 'QA'  environment, u.LOAD_DT, u.PATH,    u.SI_CREATION_TIME, u.DATASOURCE_TYPE from STG1_BO4q_universes u left outer join (select SI_ID from RPT_BO4_UNIVERSES where environment='QA') ru on u.SI_ID = ru.SI_ID where ru.SI_ID is null";

			System.out.println(msSQLInsertString);
			try {
				//runMSSQLInsertQuery(strSQL)
				Helper.runMSSQLInsertQuery(msSQLInsertString);
			}
			catch (Exception ex){
				System.out.println(ex.getMessage() + " " + ex.getStackTrace());
			}

			//'ITG Universes
			//msSQLInsertString = "INSERT INTO RPT_BO4_UNIVERSES (   CUID, SI_KIND, SI_UPDATE_TS,    SI_NAME, SI_ID, SI_PARENT_FOLDER_CUID,    SI_DESCRIPTION, SI_DATACONNECTION, ENVIRONMENT,    MACHINE_NM, LOAD_DT, PATH,    SI_CREATION_TIME)  select CUID, SI_KIND, SI_UPDATE_TS,    SI_NAME, SI_ID, SI_PARENT_FOLDER_CUID,    SI_DESCRIPTION, SI_DATACONNECTION, 'ITG',    environment, LOAD_DT, PATH,    SI_CREATION_TIME from STG1_BO4t_universes where load_dt >= to_date('" + Helper.oracleOnTheHour(now) + "','DD-MM-YYYY HH24:MI:SS')";
			//System.out.println(msSQLInsertString);
			//Helper.runMSSQLInsertQuery(msSQLInsertString);

			//'Dev Universes
			//maxid = Helper.getMaxID("select max(si_id) maxID from STG1_BO4d_universes");
			//msSQLInsertString = "INSERT INTO RPT_BO4_UNIVERSES (   CUID, SI_KIND, SI_UPDATE_TS,    SI_NAME, SI_ID, SI_PARENT_FOLDER_CUID,    SI_DESCRIPTION, SI_DATACONNECTION, ENVIRONMENT,    MACHINE_NM, LOAD_DT, PATH,    SI_CREATION_TIME, DATASOURCE_TYPE)  select CUID, SI_KIND, SI_UPDATE_TS,    SI_NAME, SI_ID, SI_PARENT_FOLDER_CUID,    SI_DESCRIPTION, SI_DATACONNECTION, 'Dev',    environment, LOAD_DT, PATH,    SI_CREATION_TIME, DATASOURCE_TYPE from STG1_BO4D_universes where load_dt >= to_date('" + Helper.oracleOnTheHour(now) + "','DD-MM-YYYY HH24:MI:SS')";
			//msSQLInsertString = "INSERT INTO RPT_BO4_UNIVERSES (   CUID, SI_KIND, SI_UPDATE_TS,    SI_NAME, SI_ID, SI_PARENT_FOLDER_CUID,    SI_DESCRIPTION, SI_DATACONNECTION, ENVIRONMENT,    MACHINE_NM, LOAD_DT, PATH,    SI_CREATION_TIME, DATASOURCE_TYPE)  select CUID, SI_KIND, SI_UPDATE_TS,    SI_NAME, SI_ID, SI_PARENT_FOLDER_CUID,    SI_DESCRIPTION, SI_DATACONNECTION, 'Prod',    environment, LOAD_DT, PATH,    SI_CREATION_TIME, DATASOURCE_TYPE from STG1_BO4d_universes where si_id > maxid";
			msSQLInsertString = "INSERT INTO RPT_BO4_UNIVERSES (   CUID, SI_KIND, SI_UPDATE_TS,    SI_NAME, SI_ID, SI_PARENT_FOLDER_CUID,    SI_DESCRIPTION, SI_DATACONNECTION, ENVIRONMENT,   LOAD_DT, PATH,    SI_CREATION_TIME, DATASOURCE_TYPE) select u.CUID, u.SI_KIND, u.SI_UPDATE_TS,    u.SI_NAME, u.SI_ID, u.SI_PARENT_FOLDER_CUID,    u.SI_DESCRIPTION, u.SI_DATACONNECTION, 'Dev'  environment, u.LOAD_DT, u.PATH,    u.SI_CREATION_TIME, u.DATASOURCE_TYPE from STG1_BO4d_universes u left outer join (select SI_ID from RPT_BO4_UNIVERSES where environment='Dev') ru on u.SI_ID = ru.SI_ID where ru.SI_ID is null";
			System.out.println(msSQLInsertString);
			try {
				//runMSSQLInsertQuery(strSQL)
				Helper.runMSSQLInsertQuery(msSQLInsertString);
			}
			catch (Exception ex){
				System.out.println(ex.getMessage() + " " + ex.getStackTrace());
			}


			//*************************** DATA CONNECTIONS ************************//New as of Jan 6, 2014
			//Val
			msSQLInsertString = "INSERT INTO RPT_BO4_DATA_CONN (SI_METADATA_BVCONN_PASSWORD, SI_METADATA_BVCONN_USERNAME, SI_CREATION_TIME, SI_KIND, SI_UPDATE_TS, SI_NAME, SI_ID, SI_PARENT_FOLDER_CUID, SI_PARENTID, SI_PARENT_FOLDER, SI_PARENT_CUID, CUID, SI_OWNER, LOAD_DT, SI_PROVIDER_CAPTION, SI_SERVER,ENVIRONMENT) select a.SI_METADATA_BVCONN_PASSWORD, a.SI_METADATA_BVCONN_USERNAME, a.SI_CREATION_TIME, a.SI_KIND, a.SI_UPDATE_TS, a.SI_NAME, a.SI_ID, a.SI_PARENT_FOLDER_CUID, a.SI_PARENTID, a.SI_PARENT_FOLDER, a.SI_PARENT_CUID, a.CUID, a.SI_OWNER, a.LOAD_DT, a.SI_PROVIDER_CAPTION, a.SI_SERVER,'Val' environment from STG1_BO4V_DATA_CONN a left outer join (select * from RPT_BO4_DATA_CONN b where environment='Val') b on a.si_id = b.si_id where b.si_id is null";
			System.out.println(msSQLInsertString);
			try {
				//runMSSQLInsertQuery(strSQL)
				Helper.runMSSQLInsertQuery(msSQLInsertString);
			}
			catch (Exception ex){
				System.out.println(ex.getMessage() + " " + ex.getStackTrace());
			}


			//Prod
			msSQLInsertString = "INSERT INTO RPT_BO4_DATA_CONN (SI_METADATA_BVCONN_PASSWORD, SI_METADATA_BVCONN_USERNAME, SI_CREATION_TIME, SI_KIND, SI_UPDATE_TS, SI_NAME, SI_ID, SI_PARENT_FOLDER_CUID, SI_PARENTID, SI_PARENT_FOLDER, SI_PARENT_CUID, CUID, SI_OWNER, LOAD_DT, SI_PROVIDER_CAPTION, SI_SERVER,ENVIRONMENT) select a.SI_METADATA_BVCONN_PASSWORD, a.SI_METADATA_BVCONN_USERNAME, a.SI_CREATION_TIME, a.SI_KIND, a.SI_UPDATE_TS, a.SI_NAME, a.SI_ID, a.SI_PARENT_FOLDER_CUID, a.SI_PARENTID, a.SI_PARENT_FOLDER, a.SI_PARENT_CUID, a.CUID, a.SI_OWNER, a.LOAD_DT, a.SI_PROVIDER_CAPTION, a.SI_SERVER,'Prod' environment from STG1_BO4p_DATA_CONN a left outer join (select * from RPT_BO4_DATA_CONN b where environment='Prod') b on a.si_id = b.si_id where b.si_id is null";
			System.out.println(msSQLInsertString);
			try {
				//runMSSQLInsertQuery(strSQL)
				Helper.runMSSQLInsertQuery(msSQLInsertString);
			}
			catch (Exception ex){
				System.out.println(ex.getMessage() + " " + ex.getStackTrace());
			}

			//QA
			msSQLInsertString = "INSERT INTO RPT_BO4_DATA_CONN (SI_METADATA_BVCONN_PASSWORD, SI_METADATA_BVCONN_USERNAME, SI_CREATION_TIME, SI_KIND, SI_UPDATE_TS, SI_NAME, SI_ID, SI_PARENT_FOLDER_CUID, SI_PARENTID, SI_PARENT_FOLDER, SI_PARENT_CUID, CUID, SI_OWNER, LOAD_DT, SI_PROVIDER_CAPTION, SI_SERVER,ENVIRONMENT) select a.SI_METADATA_BVCONN_PASSWORD, a.SI_METADATA_BVCONN_USERNAME, a.SI_CREATION_TIME, a.SI_KIND, a.SI_UPDATE_TS, a.SI_NAME, a.SI_ID, a.SI_PARENT_FOLDER_CUID, a.SI_PARENTID, a.SI_PARENT_FOLDER, a.SI_PARENT_CUID, a.CUID, a.SI_OWNER, a.LOAD_DT, a.SI_PROVIDER_CAPTION, a.SI_SERVER,'QA' environment from STG1_BO4q_DATA_CONN a left outer join (select * from RPT_BO4_DATA_CONN b where environment='QA') b on a.si_id = b.si_id where b.si_id is null";
			System.out.println(msSQLInsertString);
			try {
				//runMSSQLInsertQuery(strSQL)
				Helper.runMSSQLInsertQuery(msSQLInsertString);
			}
			catch (Exception ex){
				System.out.println(ex.getMessage() + " " + ex.getStackTrace());
			}

			//Dev
			msSQLInsertString = "INSERT INTO RPT_BO4_DATA_CONN (SI_METADATA_BVCONN_PASSWORD, SI_METADATA_BVCONN_USERNAME, SI_CREATION_TIME, SI_KIND, SI_UPDATE_TS, SI_NAME, SI_ID, SI_PARENT_FOLDER_CUID, SI_PARENTID, SI_PARENT_FOLDER, SI_PARENT_CUID, CUID, SI_OWNER, LOAD_DT, SI_PROVIDER_CAPTION, SI_SERVER,ENVIRONMENT) select a.SI_METADATA_BVCONN_PASSWORD, a.SI_METADATA_BVCONN_USERNAME, a.SI_CREATION_TIME, a.SI_KIND, a.SI_UPDATE_TS, a.SI_NAME, a.SI_ID, a.SI_PARENT_FOLDER_CUID, a.SI_PARENTID, a.SI_PARENT_FOLDER, a.SI_PARENT_CUID, a.CUID, a.SI_OWNER, a.LOAD_DT, a.SI_PROVIDER_CAPTION, a.SI_SERVER,'Dev' environment from STG1_BO4d_DATA_CONN a left outer join (select * from RPT_BO4_DATA_CONN b where environment='Dev') b on a.si_id = b.si_id where b.si_id is null";
			System.out.println(msSQLInsertString);
			try {
				//runMSSQLInsertQuery(strSQL)
				Helper.runMSSQLInsertQuery(msSQLInsertString);
			}
			catch (Exception ex){
				System.out.println(ex.getMessage() + " " + ex.getStackTrace());
			}



			/*	//Sbx
			msSQLInsertString = "INSERT INTO RPT_BO4_DATA_CONN (SI_METADATA_BVCONN_PASSWORD, SI_METADATA_BVCONN_USERNAME, SI_CREATION_TIME, SI_KIND, SI_UPDATE_TS, SI_NAME, SI_ID, SI_PARENT_FOLDER_CUID, SI_PARENTID, SI_PARENT_FOLDER, SI_PARENT_CUID, CUID, SI_OWNER, LOAD_DT, SI_PROVIDER_CAPTION, SI_SERVER,ENVIRONMENT) select a.SI_METADATA_BVCONN_PASSWORD, a.SI_METADATA_BVCONN_USERNAME, a.SI_CREATION_TIME, a.SI_KIND, a.SI_UPDATE_TS, a.SI_NAME, a.SI_ID, a.SI_PARENT_FOLDER_CUID, a.SI_PARENTID, a.SI_PARENT_FOLDER, a.SI_PARENT_CUID, a.CUID, a.SI_OWNER, a.LOAD_DT, a.SI_PROVIDER_CAPTION, a.SI_SERVER,'Sbx' environment from STG1_BO4s_DATA_CONN a left outer join (select * from RPT_BO4_DATA_CONN b where environment='Sbx') b on a.si_id = b.si_id where b.si_id is null";
			System.out.println(msSQLInsertString);
			try {
				//runMSSQLInsertQuery(strSQL)
				Helper.runMSSQLInsertQuery(msSQLInsertString);
			}
			catch (Exception ex){
				System.out.println(ex.getMessage() + " " + ex.getStackTrace());
			}*/
			//Oct 22, 2014:  I don't need to delete dups anymore because I created constraints to prevent them.

			//11/8/2012:  I added these dedup functions because with the new way to add rows to stage 3, I get dups.
			//If the old way had worked, I expect I wouldn't be in this position.
			/*LoadFunctions.deleteDups("STG1_bo4p_creports", "report_file_path, report_nm", "");
			LoadFunctions.deleteDups("STG1_BO4D_creports", "report_file_path, report_nm", "");
			LoadFunctions.deleteDups("STG1_bo4q_creports", "report_file_path, report_nm", "");
			LoadFunctions.deleteDups("STG1_bo4v_creports", "report_file_path, report_nm", "");

			LoadFunctions.deleteDups("STG1_bo4p_wx_reports", "cuid", "");
			LoadFunctions.deleteDups("STG1_bo4q_wx_reports", "cuid", "");
			LoadFunctions.deleteDups("STG1_BO4D_wx_reports", "cuid", "");
			LoadFunctions.deleteDups("STG1_BO4v_wx_reports", "cuid", "");

			LoadFunctions.deleteDups("STG1_bo4p_runhist", "instance_id, report_id", "");
			LoadFunctions.deleteDups("STG1_BO4D_runhist", "instance_id, report_id", "");
			LoadFunctions.deleteDups("STG1_bo4q_runhist", "instance_id, report_id", "");
			LoadFunctions.deleteDups("STG1_bo4v_runhist", "instance_id, report_id", "");

			LoadFunctions.deleteDups("STG1_BO4D_data_found", "CUID", "");
			LoadFunctions.deleteDups("STG1_bo4p_data_found", "CUID", "");
			LoadFunctions.deleteDups("STG1_bo4v_data_found", "CUID", "");
			LoadFunctions.deleteDups("STG1_bo4q_data_found", "CUID", "");


			LoadFunctions.deleteDups("STG1_bo4p_universes", "si_id", "");
			LoadFunctions.deleteDups("STG1_bo4q_universes", "si_id", "");
			LoadFunctions.deleteDups("STG1_BO4D_universes", "si_id", "");
			LoadFunctions.deleteDups("STG1_bo4v_universes", "si_id", "");

			LoadFunctions.deleteDups("RPT_bo4_universes", "CUID, machine_nm, environment", "Prod");
			LoadFunctions.deleteDups("RPT_bo4_universes", "CUID, machine_nm, environment", "QA");
			LoadFunctions.deleteDups("RPT_bo4_universes", "CUID, machine_nm, environment", "Dev");
			LoadFunctions.deleteDups("RPT_bo4_universes", "CUID, machine_nm, environment", "Val");


			LoadFunctions.deleteDups("STG1_bo4p_bus_views", "CUID", "");
			LoadFunctions.deleteDups("STG1_bo4q_bus_views", "CUID", "");
			LoadFunctions.deleteDups("STG1_BO4D_bus_views", "CUID", "");
			LoadFunctions.deleteDups("STG1_bo4v_bus_views", "CUID", "");

			LoadFunctions.deleteDups("RPT_bo4_bus_views", "CUID, environment", "Prod");
			LoadFunctions.deleteDups("RPT_bo4_bus_views", "CUID, environment", "QA");
			LoadFunctions.deleteDups("RPT_bo4_bus_views", "CUID, environment", "Val");
			LoadFunctions.deleteDups("RPT_bo4_bus_views", "CUID, environment", "Dev");

			LoadFunctions.deleteDups("STG1_bo4p_data_conn", "CUID", "");
			LoadFunctions.deleteDups("STG1_bo4q_data_conn", "CUID", "");
			LoadFunctions.deleteDups("STG1_BO4D_data_conn", "CUID", "");
			LoadFunctions.deleteDups("STG1_bo4v_data_conn", "CUID", "");

			//New as of Jan 6, 2014
			LoadFunctions.deleteDups("RPT_bo4_data_conn", "CUID, environment", "Prod");
			LoadFunctions.deleteDups("RPT_bo4_data_conn", "CUID, environment", "QA");
			LoadFunctions.deleteDups("RPT_bo4_data_conn", "CUID, environment", "Dev");
			LoadFunctions.deleteDups("RPT_bo4_data_conn", "CUID, environment", "Val");
			LoadFunctions.deleteDups("RPT_bo4_data_conn", "CUID, environment", "Sbx");

			LoadFunctions.deleteDups("RPT_bo4_reports", "report_file_path, report_nm", "Prod");
			LoadFunctions.deleteDups("RPT_bo4_reports", "report_file_path, report_nm", "QA");
			LoadFunctions.deleteDups("RPT_bo4_reports", "report_file_path, report_nm", "Dev");
			LoadFunctions.deleteDups("RPT_bo4_reports", "report_file_path, report_nm", "Val");*/
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage() + " " + ex.getStackTrace());
		}


	} //End Sub

	public static void setDeletedRecurring (String EnterpriseEnv,String login, String pswd, 
			String auth, String destTblNm){
		//Pre-condition:  recurring jobs that no longer exist in BO are seen as active in Tracker
		//Post-condition: recurring jobs that no longer exist in BO are marked as deleted.
		IEnterpriseSession objEnterpriseSession;
		//IInfoStore objInfoStore;
		IInfoObjects colInfoObjects;
		IInfoObjects colRecurObjects;
		IInfoObject objInfoObject;
		IInfoObject objInstObject;
		String msSQLSelectString = "select instance_id from " + destTblNm + " where (delete_flg is null or delete_flg <> 'Y') order by instance_id";
		String msSQLUpdateString;
		String strInfoSQL;

		try {

			//Log onto BO Enterprise
			ISessionMgr  objSessionMgr = CrystalEnterprise.getSessionMgr();
			objEnterpriseSession = objSessionMgr.logon(login, pswd, EnterpriseEnv, auth);    
			IInfoStore objInfoStore = (IInfoStore) objEnterpriseSession.getService("InfoStore");

			//Get a recordset of recurring jobs from the Tracker database
			/*String driver = "oracle.jdbc.driver.OracleDriver";
			String url = "jdbc:oracle:thin:@dlxkrbodb1.lex.lexmark.com:1750:edr1den1";
			String username = "botrack";
			String password = "********";*/
			String recurringID ="0";
			int theCount = 0;
			try
			{

				System.out.println(msSQLSelectString);
				//Class.forName(driver);		//loads the driver
				//Connection conn = DriverManager.getConnection(url,username,password);	

				//Statement stmt = conn.createStatement(); 
				//ResultSet rs = stmt.executeQuery(oraSelectString);
				//System.out.println("The number of records found: " + rs.getFetchSize());
				// Load the SQLServerDriver class, build the connection string, and get a connection 
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver"); 
				String connectionUrl = "jdbc:sqlserver://DASHWBODB012;" + 
						"database=EDR1DEN1;" + 
						"user=BOTRACK;" + 
						"password=********"; 
				Connection conn = DriverManager.getConnection(connectionUrl); 
				Statement stmt = conn.createStatement(); 
				msSQLUpdateString = "update " + destTblNm + " set delete_flg_dt = GETDATE(), delete_flg = 'Y' where instance_id in (0";				
				ResultSet rs = stmt.executeQuery(msSQLSelectString);
				while (rs.next()) 
				{
					theCount = theCount+1;
					System.out.println("The count of recurring jobs in Tracker looked at so far: " + theCount);
					recurringID = rs.getString("INSTANCE_ID"); 
					strInfoSQL = "select si_id from ci_infoobjects where si_id=" + recurringID;
					colInfoObjects = (IInfoObjects)objInfoStore.query(strInfoSQL);
					if (colInfoObjects.getResultSize() < 1) {
						System.out.println("This recurring id was not found on BO: " + recurringID);
						////set Delete_flg to "Y"
						msSQLUpdateString = msSQLUpdateString + ", " + recurringID;

					}
				}
				System.out.println("The count of recurring jobs in Tracker found: " + theCount);
				//Clean up the update statement
				msSQLUpdateString = msSQLUpdateString + ")";

				stmt.executeUpdate(msSQLUpdateString);

				// Clean up the Enterprise Session.
				if(objEnterpriseSession != null) {
					try {
						objEnterpriseSession.logoff();

					} catch(Exception e_ignore_in_cleanup) {}
				}
				conn = null;
				//conn.close();
			}	
			catch (Exception ex)
			{
				ex.printStackTrace();
			}


		}
		catch (Exception ex){

		}

	}
	public static void update_Universe_Conn_nm (String enterpriseEnv, String destTblNm, String dataConnTbl){
		//When the stage 1 universe tables are loaded, they have the id of the data connection,
		//but not the name.
		//Pre-condition:  data-connection_nm is null
		//Post-condition:  data-connection_nm is not null
		String msSQLUpdateString;
		//SQL to update the table
		msSQLUpdateString = "UPDATE " + destTblNm;
		msSQLUpdateString += " set data_connection_nm = t2.si_name" ;
		msSQLUpdateString += " from " + destTblNm + " t1 ";
		msSQLUpdateString += " inner join " + dataConnTbl + " t2 ";
		msSQLUpdateString += " on t1.si_dataconnection = t2.si_id "; 

		System.out.println(msSQLUpdateString);

		//Run the SQL
		try {
			Helper.runMSSQLInsertQuery(msSQLUpdateString);
		}
		catch (Exception Ex){
			System.out.println(Ex.getMessage() + " " + Ex.getStackTrace());
		}
	}

	public static void load_temp_objects(String EnterpriseEnv, String login, String pswd,String auth, String destTblNm, String kind) {
		//Pre-condition:  a current snapshot of an objects basic columns is not available
		//Post-condition:  a current snapshot of an objects basic columns is available

		//Valid kind: "CREPORTS", "WX_REPORTS", "BUS_VIEWS", "DATA_CONN", "RECURRINGJOBS", "UNIVERSES","USERS","GROUPS"

		IEnterpriseSession objEnterpriseSession;
		//IInfoStore objInfoStore;
		IInfoObjects colInfoObjects;
		IInfoObjects colInfoObjects2;
		IInfoObject objInfoObject;
		IInfoObject objInfoObject2;
		String errorMsg = "";
		String strInfoSQL ="";
		String strCount = "";
		String msSQLInsertString;
		int parentID = 0;
		String objectKind = "";

		try {	
			//truncate the table so that it has the latest records
			Helper.runMSSQLInsertQuery("truncate table temp_objects"); 

			ISessionMgr  objSessionMgr = CrystalEnterprise.getSessionMgr();
			objEnterpriseSession = objSessionMgr.logon(login, pswd, EnterpriseEnv, auth); 

			//A way to tell if the function even started in case no error is caught.
			//Helper.runMSSQLInsertQuery("insert into STG1_BO4_TRACKER_ERRORS (ERROR_MESSAGE, LOAD_DT) VALUES ('load_report_path_snapshot logged onto  "  + EnterpriseEnv + "', GETDATE())");
			Helper.runMSSQLInsertQuery("insert into STG1_BO4_TRACKER_ERRORS (ERROR_MESSAGE) VALUES ('load_temp_objects logged onto  "  + EnterpriseEnv + "')");
			//Emailer.sendEmail("bishopp@lexmark.com", "bishopp@lexmark.com", "load_report_path_snapshot - " + EnterpriseEnv," logon successful " , null);

			//Set the BOBJ CMS query based on the object type
			if (kind.equals("BUS_VIEWS")) {
				strInfoSQL = "SELECT count(SI_ID) from CI_APPOBJECTS WHERE SI_KIND = 'MetaData.BusinessView'";
			}
			else if (kind.equals("UNIVERSES")) {
				strInfoSQL = "SELECT count(SI_ID) from CI_APPOBJECTS WHERE SI_KIND in ('Universe') or SI_SPECIFIC_KIND in ('DSL.Universe')";
			}
			else if (kind.equals("DATA_CONN")) {
				strInfoSQL = "SELECT count(SI_ID) from CI_APPOBJECTS WHERE SI_KIND in ('CommonConnection','MetaData.DataConnection','CCIS.DataConnection')";
			}
			else if (kind.equals("RECURRINGJOBS")){
				strInfoSQL = "SELECT count(SI_ID) from CI_INFOOBJECTS WHERE SI_INSTANCE=1 and SI_SCHEDULE_STATUS = 9";
			}
			else if (kind.equals("CREPORTS")) {
				strInfoSQL = "SELECT count(SI_ID) from CI_INFOOBJECTS WHERE SI_KIND='CrystalReport' and SI_INSTANCE=0";
			}
			else if (kind.equals("WX_REPORTS")) {
				strInfoSQL = "SELECT count(SI_ID) from CI_INFOOBJECTS WHERE SI_KIND in ('Webi','Xcelsius','Flash','XL.XcelsiusEnterprise','DataDiscovery','MDAnalysis') and SI_INSTANCE=0";
			}
			else if (kind.equals("USERS")){
				strInfoSQL = "SELECT count(SI_ID) from SYSTEMOBJECTS WHERE si_kind in ('User')";
			}
			else if (kind.equals("GROUPS")){
				strInfoSQL = "SELECT count(SI_ID) from SYSTEMOBJECTS WHERE si_kind in ('UserGroup')";
			}



			IInfoStore objInfoStore = (IInfoStore) objEnterpriseSession.getService("InfoStore");
			//strInfoSQL = "SELECT count(SI_ID) from CI_INFOOBJECTS WHERE SI_KIND in ('CrystalReport', 'Webi', 'DataDiscovery','XL.XcelsiusEnterprise','MDAnalysis') and SI_INSTANCE = 0";

			System.out.println(strInfoSQL);
			colInfoObjects = (IInfoObjects)objInfoStore.query(strInfoSQL);
			System.out.println("Number of rows:  " + colInfoObjects.getResultSize());
			objInfoObject = (IInfoObject)colInfoObjects.get(0);
			strCount = ((IProperties)objInfoObject.properties().getProperty("SI_AGGREGATE_COUNT").getValue()).getProperty("SI_ID").getValue().toString();

			System.out.println("Record count is " + strCount);
			//strInfoSQL = "SELECT TOP " + strCount + " * FROM CI_INFOOBJECTS WHERE SI_KIND in ('CrystalReport', 'Webi') and SI_INSTANCE = 0";
			//strInfoSQL = "SELECT TOP " + strCount + " SI_ID, CUID, SI_PARENTID, SI_NAME, SI_KIND, SI_CREATION_TIME FROM CI_INFOOBJECTS WHERE SI_KIND in ('CrystalReport', 'Webi', 'DataDiscovery','XL.XcelsiusEnterprise','MDAnalysis') and SI_INSTANCE = 0";
			if (kind.equals("BUS_VIEWS")) {
				strInfoSQL = "SELECT  TOP " + strCount + " SI_ID, SI_CUID, SI_KIND from CI_APPOBJECTS WHERE SI_KIND = 'MetaData.BusinessView'";
			}
			else if (kind.equals("UNIVERSES")) {
				strInfoSQL = "SELECT  TOP " + strCount + " SI_ID, SI_CUID, SI_KIND from CI_APPOBJECTS WHERE SI_KIND in( 'Universe') or SI_SPECIFIC_KIND in ('DSL.Universe')";
			}
			else if (kind.equals("DATA_CONN")) {
				strInfoSQL = "SELECT  TOP " + strCount + " SI_ID, SI_CUID, SI_KIND from CI_APPOBJECTS WHERE SI_KIND in ('CommonConnection','MetaData.DataConnection','CCIS.DataConnection')";
			}
			else if (kind.equals("RECURRINGJOBS")){
				strInfoSQL = "SELECT  TOP " + strCount + " SI_ID, SI_CUID, SI_KIND from CI_INFOOBJECTS WHERE SI_INSTANCE=1 and SI_SCHEDULE_STATUS = 9";
			}
			else if (kind.equals("CREPORTS")) {
				strInfoSQL = "SELECT  TOP " + strCount + " SI_ID, SI_CUID, SI_KIND from CI_INFOOBJECTS WHERE SI_KIND='CrystalReport' and SI_INSTANCE=0";
			}
			else if (kind.equals("WX_REPORTS")) {
				strInfoSQL = "SELECT  TOP " + strCount + " SI_ID, SI_CUID, SI_KIND from CI_INFOOBJECTS WHERE SI_KIND in ('Webi','Xcelsius','Flash','XL.XcelsiusEnterprise','DataDiscovery','MDAnalysis') and SI_INSTANCE=0";
			}
			else if (kind.equals("USERS")){
				strInfoSQL = "SELECT  TOP " + strCount + " SI_ID, SI_CUID, SI_KIND from SYSTEMOBJECTS WHERE si_kind in ('User')";
			}
			else if (kind.equals("GROUPS")){
				strInfoSQL = "SELECT  TOP " + strCount + " SI_ID, SI_CUID, SI_KIND from SYSTEMOBJECTS WHERE si_kind in ('UserGroup')";
			}



			colInfoObjects2 = (IInfoObjects)objInfoStore.query(strInfoSQL);

			if (colInfoObjects2.size() > 0)  {
				int rowcount = colInfoObjects2.size();

				System.out.println("We do have objects in collection");
				for (int k =0; k < rowcount; k++)
				{
					objInfoObject2 = (IInfoObject) colInfoObjects2.get(k);
					parentID = objInfoObject2.getParentID();


					objectKind = objInfoObject2.getKind();

					/*  if (objectKind.equals("CrystalReport")) {
                      //msSQLInsertString = "INSERT INTO " + destTblNm + " (SI_ID, SI_PATH, SI_FILE1,  REPORT_NM, REPORT_FILE_PATH, CUID, DATA_SOURCE_NM, DATA_SOURCE2_NM) VALUES (" + objInfoObject2.getID() + ", '" + safeSQL(objInfoObject2.Properties.Item("SI_FILES").Properties.Item("SI_PATH").Value) + "', '" + safeSQL(objInfoObject2.Properties.Item("SI_FILES").Properties.Item("SI_FILE1").Value) + "', '" + safeSQL(objInfoObject2.getTitle()) + "', '" + safeSQL(cookiecrumbs) + "','" + objInfoObject2.CUID + "', '" + server1 + "','" + server2 + "')";
				    	  msSQLInsertString = "INSERT INTO " + destTblNm + " (SI_ID,  REPORT_NM, REPORT_FILE_PATH, CUID) VALUES (" + objInfoObject2.getID() + ", '" + Helper.safeSQL(objInfoObject2.getTitle()) + "', '" + Helper.safeSQL(cookiecrumbs) + "','" + objInfoObject2.getCUID() + "')";
				      } else {
				    	  msSQLInsertString = "INSERT INTO " + destTblNm + " (SI_ID,  REPORT_NM, REPORT_FILE_PATH, CUID) VALUES (" + objInfoObject2.getID() + ", '" + Helper.safeSQL(objInfoObject2.getTitle()) + "', '" + Helper.safeSQL(cookiecrumbs) + "','" + objInfoObject2.getCUID() + "')";

				      }*/

					try {
						msSQLInsertString = "INSERT INTO TEMP_OBJECTS (SI_ID,  CUID, ENVIRONMENT, SI_KIND) VALUES (" + objInfoObject2.getID() + ", '"  + objInfoObject2.getCUID() + "','" + EnterpriseEnv +  "', '" + objInfoObject2.getKind() + "')";
						Helper.runMSSQLInsertQuery(msSQLInsertString);	
					}
					catch (Exception ex){
						System.out.println(ex.getMessage() + " " + ex.getStackTrace());
					}

				}
				objSessionMgr = null;
				//objEnterpriseSession = Nothing;
				objInfoStore = null;
				colInfoObjects = null;
				objInfoObject = null;
				colInfoObjects2 = null;
				objInfoObject2 = null;

			}
			// Clean up the Enterprise Session.
			if(objEnterpriseSession != null) {
				try {
					objEnterpriseSession.logoff();

				} catch(Exception e_ignore_in_cleanup) {}
			}
		}
		catch (Exception ex){

		}
	}
}
