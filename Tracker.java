
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.crystaldecisions.sdk.exception.SDKException;
import com.crystaldecisions.sdk.framework.CrystalEnterprise;
import com.crystaldecisions.sdk.framework.IEnterpriseSession;
import com.crystaldecisions.sdk.framework.ISessionMgr;
import com.crystaldecisions.sdk.occa.infostore.CePropertyID;
import com.crystaldecisions.sdk.occa.infostore.IDestination;
import com.crystaldecisions.sdk.occa.infostore.IDestinationPlugin;
import com.crystaldecisions.sdk.occa.infostore.IFiles;
import com.crystaldecisions.sdk.occa.infostore.IInfoObject;
import com.crystaldecisions.sdk.occa.infostore.IInfoObjects;
import com.crystaldecisions.sdk.occa.infostore.IInfoStore;
import com.crystaldecisions.sdk.occa.infostore.IProcessingInfo;
import com.crystaldecisions.sdk.occa.infostore.ISchedulingInfo;
import com.crystaldecisions.sdk.occa.infostore.ISendable;
import com.crystaldecisions.sdk.occa.managedreports.IReportAppFactory;
import com.crystaldecisions.sdk.occa.report.application.ReportClientDocument;
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
import com.crystaldecisions.sdk.prompting.IPrompts;
import com.crystaldecisions.sdk.properties.IProperties;
import com.crystaldecisions.sdk.properties.IProperty;
import com.crystaldecisions.sdk.properties.ISDKList;

public class Tracker {
	Connection conn;


	public static void main(String[] args) {

		/////////////////////// TEST AREA //////////////////////////
		//LoadFunctions.load_stg1_Webi_Reports("PASHWBOBJ013","tracker","********","secLDAP","STG1_BO4P_WX_REPORTS");

		/*String reportNm = "FN0013HA - GL Details (T-code equivalent: FAGLL03) ";
		String firstSix = reportNm.substring(0, 5);
		String flag = "";
		flag = Helper.report_Name_Validator(firstSix);
		System.out.println("The report name starts with a number, true or false? " + flag);*/
				
		//LoadFunctions.load_stg1_Crystal_Reports("PASHWBOBJ013","tracker","********","secLDAP","STG1_BO4P_CREPORTS");
		//LoadFunctions.load_stg1_BO_runhist("PASHWBOBJ013","tracker","********","secLDAP","STG1_BO4P_RUNHIST", "Prod");
		
	
		//Dev
				//LoadFunctions.load_stg1_BO_runhist("DASHWBOBJ011","tracker","********","secLDAP","STG1_BO4D_RUNHIST", "Dev");


		//Val
		//LoadFunctions.load_stg1_Webi_Reports("dashwbobj012","tracker","********","secLDAP","STG1_BO4V_WX_REPORTS");

		/*
		LoadFunctions.load_stg1_Crystal_Reports("dashwbobj012","tracker","********","secLDAP","STG1_BO4V_CREPORTS");

		
		//The delete dups fn should now be obsolete because I put constraints on the primary key for each table.
		//As of Oct 20, 2014, there are about 65,000 instances on Val.  I need all of them
		//so I can simulate periodic loads.
		for (int a = 0; a <= 1; a++){
			LoadFunctions.load_stg1_BO_runhist("dashwbobj012","tracker","********","secLDAP","STG1_BO4V_RUNHIST", "Val");

		}


		LoadFunctions.load_stg1_universes("dashwbobj012","tracker","********","secLDAP","STG1_BO4V_UNIVERSES");
		LoadFunctions.load_stg1_unx_universes("dashwbobj012","tracker","********","secLDAP","STG1_BO4V_UNIVERSES");
		LoadFunctions.load_stg1_Business_Views("dashwbobj012","tracker","********","secLDAP","STG1_BO4V_BUS_VIEWS");

		LoadFunctions.load_stg1_data_conn("dashwbobj012","tracker","********","secLDAP","STG1_BO4V_DATA_CONN");

		LoadFunctions.load_stg1_Webi_Reports("DASHWBOBJ011","tracker","********","secLDAP","STG1_BO4D_WX_REPORTS");
		 */
		/////////////////////////////////////////////////////////////

		///////////////////////////////////// PRODUCTION AREA //////////////////////////////

		/////////////******************* PRIOR TO RELEASE FOR BO 4.1, CHANGE THE TABLE NAMES (REMOVE ".") and change to secLDAP

		//		1.  Schedule History
		
		//Prod
		LoadFunctions.load_stg1_BO_runhist("PASHWBOBJ013","tracker","********","secLDAP","STG1_BO4P_RUNHIST", "Prod");

		//Val
		LoadFunctions.load_stg1_BO_runhist("DASHWBOBJ012","tracker","********","secLDAP","STG1_BO4V_RUNHIST", "Val");


		//QA
		LoadFunctions.load_stg1_BO_runhist("QASHWBOBJ013","tracker","********","secLDAP","STG1_BO4Q_RUNHIST", "QA");

		//Dev
		LoadFunctions.load_stg1_BO_runhist("DASHWBOBJ011","tracker","********","secLDAP","STG1_BO4D_RUNHIST", "Dev");

		

		//Sandbox
		//LoadFunctions.load_stg1_BO_runhist("DASHWBOBJ013","tracker","********","secLDAP","STG1_BO4S_RUNHIST", "Sbx");


		//2.  New Reports
		//Val
		LoadFunctions.load_stg1_Crystal_Reports("DASHWBOBJ012","tracker","********","secLDAP","STG1_BO4V_CREPORTS");

		LoadFunctions.load_stg1_Webi_Reports("DASHWBOBJ012","tracker","********","secLDAP","STG1_BO4V_WX_REPORTS");


		//QA
		LoadFunctions.load_stg1_Crystal_Reports("QASHWBOBJ013","tracker","********","secLDAP","STG1_BO4Q_CREPORTS");

		LoadFunctions.load_stg1_Webi_Reports("QASHWBOBJ013","tracker","********","secLDAP","STG1_BO4Q_WX_REPORTS");

		//Dev
		LoadFunctions.load_stg1_Crystal_Reports("DASHWBOBJ011","tracker","********","secLDAP","STG1_BO4D_CREPORTS");

		LoadFunctions.load_stg1_Webi_Reports("DASHWBOBJ011","tracker","********","secLDAP","STG1_BO4D_WX_REPORTS");

		//Prod
		LoadFunctions.load_stg1_Crystal_Reports("PASHWBOBJ013","tracker","********","secLDAP","STG1_BO4P_CREPORTS");

		LoadFunctions.load_stg1_Webi_Reports("PASHWBOBJ013","tracker","********","secLDAP","STG1_BO4P_WX_REPORTS");

		//Sandbox
		//LoadFunctions.load_stg1_Crystal_Reports("DASHWBOBJ013","tracker","********","secLDAP","STG1_BO4S_CREPORTS");

		//LoadFunctions.load_stg1_Webi_Reports("DASHWBOBJ013","tracker","********","secLDAP","STG1_BO4S_WX_REPORTS");




		//3.  New Universes
		//Val
		LoadFunctions.load_stg1_universes("DASHWBOBJ012","tracker","********","secLDAP","STG1_BO4V_UNIVERSES");
		LoadFunctions.load_stg1_unx_universes("DASHWBOBJ012","tracker","********","secLDAP","STG1_BO4V_UNIVERSES");

		//QA

		LoadFunctions.load_stg1_universes("QASHWBOBJ013","tracker","********","secLDAP","STG1_BO4Q_UNIVERSES");
		LoadFunctions.load_stg1_unx_universes("QASHWBOBJ013","tracker","********","secLDAP","STG1_BO4Q_UNIVERSES");

		//Dev
		LoadFunctions.load_stg1_universes("DASHWBOBJ011","tracker","********","secLDAP","STG1_BO4D_UNIVERSES");
		LoadFunctions.load_stg1_unx_universes("DASHWBOBJ011","tracker","********","secLDAP","STG1_BO4D_UNIVERSES");


		//Prod
		LoadFunctions.load_stg1_universes("PASHWBOBJ013","tracker","********","secLDAP","STG1_BO4P_UNIVERSES");
		LoadFunctions.load_stg1_unx_universes("PASHWBOBJ013","tracker","********","secLDAP","STG1_BO4p_UNIVERSES");


		//Sandbox
		//LoadFunctions.load_stg1_universes("DASHWBOBJ013","tracker","********","secLDAP","STG1_BO4S_UNIVERSES");
		//LoadFunctions.load_stg1_unx_universes("DASHWBOBJ013","tracker","********","secLDAP","STG1_BO4S_UNIVERSES");




		//4.  New Business Views (add scheduled LOV's later)
		//Val		
		LoadFunctions.load_stg1_Business_Views("DASHWBOBJ012","tracker","********","secLDAP","STG1_BO4V_BUS_VIEWS");

		//Dev
		LoadFunctions.load_stg1_Business_Views("DASHWBOBJ011","tracker","********","secLDAP","STG1_BO4D_BUS_VIEWS");

		//QA
		LoadFunctions.load_stg1_Business_Views("QASHWBOBJ013","tracker","********","secLDAP","STG1_BO4Q_BUS_VIEWS");

		//Prod
		LoadFunctions.load_stg1_Business_Views("PASHWBOBJ013","tracker","********","secLDAP","STG1_BO4P_BUS_VIEWS");


		//Sandbox
		//LoadFunctions.load_stg1_Business_Views("DASHWBOBJ013","tracker","********","secLDAP","STG1_BO4S_BUS_VIEWS");



		//5.  Delete Dups
		//I redesigned the tables in MS SQL Server so that they won't accept duplicates.  
		//The queries against BOBJ could include duplicates because I need to be sure
		//there are no gaps, but the constraint on the primary keys will prevent duplicates.
		//It's actually a better design.


		//6.  Load stage 3 tables
		//load_stage3_tables(); Do this in Snapshots instead
		//ITG

		//QA

		//Prod

		//7.  Load Data Connections
		//Val
		LoadFunctions.load_stg1_data_conn("DASHWBOBJ012","tracker","********","secLDAP","STG1_BO4V_DATA_CONN");

		//Dev
		LoadFunctions.load_stg1_data_conn("DASHWBOBJ011","tracker","********","secLDAP","STG1_BO4D_DATA_CONN");

		//QA
		LoadFunctions.load_stg1_data_conn("QASHWBOBJ013","tracker","********","secLDAP","STG1_BO4Q_DATA_CONN");

		//Prod
		LoadFunctions.load_stg1_data_conn("PASHWBOBJ013","tracker","********","secLDAP","STG1_BO4P_DATA_CONN");

		//Sandbox
		//LoadFunctions.load_stg1_data_conn("DASHWBOBJ013","tracker","********","secLDAP","STG1_BO4S_DATA_CONN");


		///////////////////////////////////// END PRODUCTION AREA //////////////////////////////

	}



}
