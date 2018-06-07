
import java.util.Calendar;


public class RecurringJobs {
	//I created this class to debug recurring jobs so I don't have to wait for other functions to run
	public static void main(String[] args) {

		//LoadFunctions.load_stage3_tables();

		//LoadFunctions.load_stg1_boxi_recurringIds("plexwbobj003","tracker","libambini*8","secEnterprise","STG1_BOXI3P_RECURRINGJOBS", "Prod");

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

		//**********************TEST***************
		//Val
		//LoadFunctions.load_stg1_boxi_recurringjobsIncremental("dashwbobj012","tracker","libambini*8","secLDAP","STG1_BO4V_RECURRINGJOBS", "Val");
		//LoadFunctions.setDeletedRecurring ("DASHWBOBJ012","tracker","libambini*8","secLDAP","STG1_BO4V_RECURRINGJOBS");
		
		//Dev

		//LoadFunctions.load_stg1_bo_users("dlexwbobj002","tracker","libambini*8","secEnterprise","BISHOPP.STG1_BO4D_USERS");
		//LoadFunctions.load_stg1_boxi_recurringjobs("dlexwbobj002","tracker","libambini*8","secEnterprise","BISHOPP.STG1_BO4D_RECURRINGJOBS", "Dev", "Full");

		
		//PROD
		LoadFunctions.load_stg1_boxi_recurringMakeup("PASHWBOBJ013","tracker","libambini*8","secLDAP","STG1_BO4P_RECURRINGJOBS", "Prod");
		//LoadFunctions.load_stg1_boxi_recurringjobsIncremental("PASHWBOBJ013","tracker","libambini*8","secLDAP","STG1_BO4P_RECURRINGJOBS", "Prod");
		//LoadFunctions.load_stg1_boxi_recurringjobs("plexwbobj003","tracker","libambini*8","secEnterprise","BISHOPP.STG1_BO4P_RECURRINGJOBS", "Prod", "Full");
		//This is a new function to load only the ID's into an Oracle table, then read from the table the recurring id to get the recurring record one-by-one
		//LoadFunctions.load_stg1_boxi_recurringIds("plexwbobj003","tracker","libambini*8","secEnterprise","BISHOPP.STG1_BO4P_RECURRINGJOBS", "Prod");
		//LoadFunctions.setDeletedRecurring ("plexwbobj003","tracker","libambini*8","secEnterprise","BISHOPP.STG1_BO4P_RECURRINGJOBS");
		
		//*******************END TEST**********************
		
		//1. Recurring Jobs - incremental
		LoadFunctions.load_stg1_boxi_recurringjobsIncremental("DASHWBOBJ012","tracker","libambini*8","secLDAP","STG1_BO4V_RECURRINGJOBS", "Val");
		LoadFunctions.load_stg1_boxi_recurringjobsIncremental("PASHWBOBJ013","tracker","libambini*8","secLDAP","STG1_BO4P_RECURRINGJOBS", "Prod");
		LoadFunctions.load_stg1_boxi_recurringjobsIncremental("QASHWBOBJ013","tracker","libambini*8","secLDAP","STG1_BO4Q_RECURRINGJOBS", "QA");
		LoadFunctions.load_stg1_boxi_recurringjobsIncremental("DASHWBOBJ011","tracker","libambini*8","secLDAP","STG1_BO4D_RECURRINGJOBS", "Dev");
		
		
		//Mark deleted recurring jobs
		LoadFunctions.setDeletedRecurring ("DASHWBOBJ012","tracker","libambini*8","secLDAP","STG1_BO4V_RECURRINGJOBS");
		LoadFunctions.setDeletedRecurring ("PASHWBOBJ013","tracker","libambini*8","secLDAP","STG1_BO4P_RECURRINGJOBS");
		LoadFunctions.setDeletedRecurring ("QASHWBOBJ013","tracker","libambini*8","secLDAP","STG1_BO4Q_RECURRINGJOBS");
		LoadFunctions.setDeletedRecurring ("DASHWBOBJ011","tracker","libambini*8","secLDAP","STG1_BO4D_RECURRINGJOBS");
		

	}
}
