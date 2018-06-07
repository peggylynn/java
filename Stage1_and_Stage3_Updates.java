import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Calendar;

public class Stage1_and_Stage3_Updates {
	public static void main(String[] args) {
		long difference;
		int days;
		int yesterdayDate; 
		Calendar currentDt = Calendar.getInstance();
		//System.out.println("Current date or time is " + currentDt);
		//System.out.println("current time is " + currentDt.toString());
		System.out.println("Current date is " + Helper.oracleDateTime(currentDt));
		Calendar lastLoadDt= Calendar.getInstance();
		currentDt = Calendar.getInstance();

		int todayDate = currentDt.get(Calendar.DATE);

		/////////////////// TEST ////////////////////////////
		//LoadFunctions.update_stage3_delete_flag();
		//LoadFunctions.update_report_table("STG1_BO4P_CREPORTS", "Prod");
		//LoadFunctions.load_stage3_tables();
		/*LoadFunctions.load_stg1_BO_missing_history("PASHWBOBJ013", "tracker", "libambini*8", "secLDAP", "stg1_bo4p_runhist", "Prod", "2014.12.18","2014.12.19");
		LoadFunctions.load_stg1_BO_missing_history("QASHWBOBJ013", "tracker", "libambini*8", "secLDAP", "stg1_bo4Q_runhist", "QA", "2014.12.18","2014.12.19");
		LoadFunctions.load_stg1_BO_missing_history("DASHWBOBJ011", "tracker", "libambini*8", "secLDAP", "stg1_bo4D_runhist", "Dev", "2014.12.18","2014.12.19");
		LoadFunctions.load_stg1_BO_missing_history("DASHWBOBJ012", "tracker", "libambini*8", "secLDAP", "stg1_bo4V_runhist", "Val", "2014.12.18","2014.12.19");
	*/	
		//LoadFunctions.update_report_table("STG1_BO4V_CREPORTS", "Val");
		//Need to see why this isn't loading for Users and Groups
		//LoadFunctions.load_temp_objects("DASHWBOBJ012", "tracker", "libambini*8", "secLDAP", "stg1_bo4v_users", "USERS");
		//LoadFunctions.load_temp_objects("DASHWBOBJ012", "tracker", "libambini*8", "secLDAP", "stg1_bo4v_groups", "GROUPS");
		
		
		//This function doesn't work
		//LoadFunctions.setDeletedRecurring("QASHWBOBJ013","tracker","libambini*8","secLDAP","STG1_BO4Q_RECURRINGJOBS");

		//LoadFunctions.load_temp_objects("DASHWBOBJ012", "tracker", "libambini*8", "secLDAP", "STG1_BO4V_CREPORTS","CrystalReport");
		/*LoadFunctions.load_temp_objects("QASHWBOBJ013", "tracker", "libambini*8", "secLDAP", "STG1_BO4Q_RECURRINGJOBS","recurringjob");
		//Valid kind:  unv, bv, data_conn, recurringjob, CrystalReport, Webi, and runhist
		LoadFunctions.update_deleted_reports("STG1_BO4Q_RECURRINGJOBS");*/


		//LoadFunctions.load_stage3_tables();
		//LoadFunctions.load_report_path_snapshot("DASHWBOBJ012","tracker","libambini*8","secLDAP","TEMP_REPORTS");
		//run on QA and see how long it takes to load
		//folder adjusting function here
		//LoadFunctions.update_deleted_reports("STG1_BO4V_CREPORTS");
		//LoadFunctions.update_deleted_reports("STG1_BO4V_WX_REPORTS");
		//LoadFunctions.update_report_table("STG1_BO4V_CREPORTS", "Val");	
		//LoadFunctions.update_report_table("STG1_BO4V_WX_REPORTS", "Val");
		//LoadFunctions.load_stage3_tables();
		//LoadFunctions.update_Universe_Conn_nm("Val","STG1_BO4V_UNIVERSES", "STG1_BO4V_DATA_CONN");
		//LoadFunctions.update_Universe_Conn_nm("Dev","STG1_BO4D_UNIVERSES", "STG1_BO4D_DATA_CONN");
		//LoadFunctions.update_Universe_Conn_nm("QA","STG1_BO4Q_UNIVERSES", "STG1_BO4Q_DATA_CONN");
		//6.  Load stage 3 tables
		//		LoadFunctions.load_stage3_tables();
		/////////////////// END TEST ////////////////////////


		//1. UPDATE REPORT PATH AND DELETE FLAG
		
		//////////////////Need to loop through all the environments and object types to set the delete flag
		String[] envArray = {"V", "D","Q", "P"};
		String[] envNmArray = {"DASHWBOBJ012", "DASHWBOBJ011","QASHWBOBJ013", "PASHWBOBJ013"};
		String[] objArray = {"CREPORTS", "WX_REPORTS", "BUS_VIEWS", "DATA_CONN", "RECURRINGJOBS", "UNIVERSES","USERS","GROUPS"};
		String EnterpriseEnv;

		String base = "STG1_BO4";
		String env;
		String tbl;
		String tblNm;
		for (int i=0; i < envArray.length; i++){
			env = envArray[i] + "_";
			EnterpriseEnv = envNmArray[i];
			for (int j = 0; j < objArray.length; j++){
				//System.out.println("What I appended: " + objArray[j]);
				tbl = objArray[j];
				//System.out.println("line 45 " + tbl);
				// Call the function with the variables
				tblNm = base + env + tbl;
				System.out.println("line 68 " + tblNm);
				LoadFunctions.load_temp_objects(EnterpriseEnv, "tracker", "libambini*8", "secLDAP", tblNm, objArray[j]);
				LoadFunctions.update_deleted_reports(tblNm);
				//System.out.println("line 49 the index " + objArray[j]);

			}
		}
		tblNm ="";
		System.out.println("line 76 " + tblNm);

		//Prod
		//Uncomment after the folder adjusting function works.
		//LoadFunctions.load_report_path_snapshot("plexwbobj003","tracker","libambini*8","secLDAP","temp_XI_Reports");
		//I had to change where the table resides because despite granting BOTRACK all priviledges, the database wouldn't
		//let BOTRACK truncate the table.
		//LoadFunctions.load_report_path_snapshot("plexwbobj003","tracker","libambini*8","secLDAP","TEMP_REPORTS");
		LoadFunctions.load_report_path_snapshot("PASHWBOBJ013","tracker","libambini*8","secLDAP","TEMP_REPORTS");
		//run on QA and see how long it takes to load
		//folder adjusting function here
		//LoadFunctions.update_deleted_reports("STG1_BO4P_CREPORTS");
		LoadFunctions.update_report_table("STG1_BO4P_CREPORTS", "Prod");	
		LoadFunctions.update_report_table("STG1_BO4P_WX_REPORTS", "Prod");


		//QA
		//LoadFunctions.load_report_path_snapshot("qlexwbobj003","tracker","libambini*8","secLDAP","temp_XI_Reports");
		LoadFunctions.load_report_path_snapshot("QASHWBOBJ013","tracker","libambini*8","secLDAP","TEMP_REPORTS");
		//folder adjusting function here
		//LoadFunctions.update_deleted_reports("STG1_BO4Q_CREPORTS");
		LoadFunctions.update_report_table("STG1_BO4Q_CREPORTS", "QA");
		LoadFunctions.update_report_table("STG1_BO4Q_WX_REPORTS", "QA");

		//Dev
		//LoadFunctions.load_report_path_snapshot("DASHWBOBJ011","tracker","libambini*8","secLDAP","temp_XI_Reports");
		LoadFunctions.load_report_path_snapshot("DASHWBOBJ011","tracker","libambini*8","secLDAP","TEMP_REPORTS");
		//folder adjusting function here
		//LoadFunctions.update_deleted_reports("STG1_BO4D_CREPORTS");
		LoadFunctions.update_report_table("STG1_BO4D_CREPORTS", "Dev");
		LoadFunctions.update_report_table("STG1_BO4D_WX_REPORTS", "Dev");

		//Val
		//LoadFunctions.load_report_path_snapshot("DASHWBOBJ012","tracker","libambini*8","secLDAP","temp_XI_Reports");
		LoadFunctions.load_report_path_snapshot("DASHWBOBJ012","tracker","libambini*8","secLDAP","TEMP_REPORTS");
		//folder adjusting function here
		//LoadFunctions.update_deleted_reports("STG1_BO4V_CREPORTS");
		LoadFunctions.update_report_table("STG1_BO4V_CREPORTS", "Val");
		LoadFunctions.update_report_table("STG1_BO4V_WX_REPORTS", "Val");

	

		/*	//Sandbox
		//LoadFunctions.load_report_path_snapshot("dlexwbobj004","tracker","libambini*8","secLDAP","temp_XI_Reports");
		LoadFunctions.load_report_path_snapshot("dlexwbobj004","tracker","libambini*8","secLDAP","TEMP_REPORTS");
		//folder adjusting function here
		LoadFunctions.update_deleted_reports("STG1_BO4S_CREPORTS");
		LoadFunctions.update_report_table("STG1_BO4S_CREPORTS", "Sbx");
		LoadFunctions.update_report_table("STG1_BO4S_WX_REPORTS", "Sbx");*/

		//2.  Update data connection name
		LoadFunctions.update_Universe_Conn_nm("Val","STG1_BO4V_UNIVERSES", "STG1_BO4V_DATA_CONN");
		LoadFunctions.update_Universe_Conn_nm("Dev","STG1_BO4D_UNIVERSES", "STG1_BO4D_DATA_CONN");
		LoadFunctions.update_Universe_Conn_nm("QA","STG1_BO4Q_UNIVERSES", "STG1_BO4Q_DATA_CONN");
		
		//3.  Load stage 3 tables
		LoadFunctions.load_stage3_tables();
		
		//4.  Update stage 3 tables
		LoadFunctions.update_stage3_delete_flag();
	}
}
