import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Calendar;

public class Users_and_Groups {
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
		
		////////////////////// TEST ////////////////////////
		//LoadFunctions.load_stg1_bo_users("PASHWBOBJ013","tracker","********","secLDAP","STG1_BO4P_USERS");
		/*lastLoadDt = Helper.getMaxLoadDt("select max(LOAD_DT) maxLoadDate from STG1_BO4V_USERS");
		yesterdayDate  = lastLoadDt.get(Calendar.DATE);
		System.out.println("today's date is " + todayDate + " and yesterday's date is " + yesterdayDate);
		difference = todayDate - yesterdayDate;
		System.out.println("the date difference is  "+ difference);
		System.out.println("The calendar day is " + currentDt.get(Calendar.DATE));
		//if (difference !=0 || currentDt.get(Calendar.DATE)==1) {
		if (difference !=0 ) {
			LoadFunctions.load_stg1_bo_users("DASHWBOBJ012","tracker","********","secLDAP","STG1_BO4V_USERS");
		}
		
		lastLoadDt = Helper.getMaxLoadDt("select max(LOAD_DT) maxLoadDate from STG1_BO4V_GROUPS");
		yesterdayDate  = lastLoadDt.get(Calendar.DATE);
		System.out.println("today's date is " + todayDate + " and yesterday's date is " + yesterdayDate);
		difference = todayDate - yesterdayDate;
		System.out.println("the date difference is  "+ difference);
		System.out.println("The calendar day is " + currentDt.get(Calendar.DATE));
		//if (difference !=0 || currentDt.get(Calendar.DATE)==1) {
		if (difference !=0 ) {
			LoadFunctions.load_stg1_bo_groups("DASHWBOBJ012","tracker","********","secLDAP","STG1_BO4V_GROUPS");
		}
		
		LoadFunctions.load_stg1_foldersec_rel("DASHWBOBJ012","tracker","********","secLDAP","STG1_BO4_FOLDER_SEC_REL");
		*/
		/////////////////////////// END TEST ////////////////////////
		
		
		//2.  Users and Groups (snapshots)
		
		//Val
		lastLoadDt = Helper.getMaxLoadDt("select max(LOAD_DT) maxLoadDate from STG1_BO4V_USERS");
		yesterdayDate  = lastLoadDt.get(Calendar.DATE);
		System.out.println("today's date is " + todayDate + " and yesterday's date is " + yesterdayDate);
		difference = todayDate - yesterdayDate;
		System.out.println("the date difference is  "+ difference);
		System.out.println("The calendar day is " + currentDt.get(Calendar.DATE));
		//if (difference !=0 || currentDt.get(Calendar.DATE)==1) {
		if (difference !=0 ) {
			LoadFunctions.load_stg1_bo_users("DASHWBOBJ012","tracker","********","secLDAP","STG1_BO4V_USERS");
		}
		
		lastLoadDt = Helper.getMaxLoadDt("select max(LOAD_DT) maxLoadDate from STG1_BO4V_GROUPS");
		yesterdayDate  = lastLoadDt.get(Calendar.DATE);
		System.out.println("today's date is " + todayDate + " and yesterday's date is " + yesterdayDate);
		difference = todayDate - yesterdayDate;
		System.out.println("the date difference is  "+ difference);
		System.out.println("The calendar day is " + currentDt.get(Calendar.DATE));
		//if (difference !=0 || currentDt.get(Calendar.DATE)==1) {
		if (difference !=0 ) {
			LoadFunctions.load_stg1_bo_groups("DASHWBOBJ012","tracker","********","secLDAP","STG1_BO4V_GROUPS");
		}

		//Dev
		lastLoadDt = Helper.getMaxLoadDt("select max(LOAD_DT) maxLoadDate from STG1_BO4D_USERS");
		yesterdayDate  = lastLoadDt.get(Calendar.DATE);
		System.out.println("today's date is " + todayDate + " and yesterday's date is " + yesterdayDate);
		difference = todayDate - yesterdayDate;
		System.out.println("the date difference is  "+ difference);
		System.out.println("The calendar day is " + currentDt.get(Calendar.DATE));
		//if (difference !=0 || currentDt.get(Calendar.DATE)==1) {
		if (difference !=0 ) {
			LoadFunctions.load_stg1_bo_users("DASHWBOBJ011","tracker","********","secLDAP","STG1_BO4D_USERS");

		}
		
		lastLoadDt = Helper.getMaxLoadDt("select max(LOAD_DT) maxLoadDate from STG1_BO4D_GROUPS");
		yesterdayDate  = lastLoadDt.get(Calendar.DATE);
		System.out.println("today's date is " + todayDate + " and yesterday's date is " + yesterdayDate);
		difference = todayDate - yesterdayDate;
		System.out.println("the date difference is  "+ difference);
		System.out.println("The calendar day is " + currentDt.get(Calendar.DATE));
		//if (difference !=0 || currentDt.get(Calendar.DATE)==1) {
		if (difference !=0 ) {
			LoadFunctions.load_stg1_bo_groups("DASHWBOBJ011","tracker","********","secLDAP","STG1_BO4D_GROUPS");

		}
		
		//QA
		lastLoadDt = Helper.getMaxLoadDt("select max(LOAD_DT) maxLoadDate from STG1_BO4Q_USERS");
		yesterdayDate  = lastLoadDt.get(Calendar.DATE);
		System.out.println("today's date is " + todayDate + " and yesterday's date is " + yesterdayDate);
		difference = todayDate - yesterdayDate;
		System.out.println("the date difference is  "+ difference);
		System.out.println("The calendar day is " + currentDt.get(Calendar.DATE));
		//if (difference !=0 || currentDt.get(Calendar.DATE)==1) {
		if (difference !=0 ) {
			LoadFunctions.load_stg1_bo_users("QASHWBOBJ013","tracker","********","secLDAP","STG1_BO4Q_USERS");
		}
		
		lastLoadDt = Helper.getMaxLoadDt("select max(LOAD_DT) maxLoadDate from STG1_BO4Q_GROUPS");
		yesterdayDate  = lastLoadDt.get(Calendar.DATE);
		System.out.println("today's date is " + todayDate + " and yesterday's date is " + yesterdayDate);
		difference = todayDate - yesterdayDate;
		System.out.println("the date difference is  "+ difference);
		System.out.println("The calendar day is " + currentDt.get(Calendar.DATE));
		//if (difference !=0 || currentDt.get(Calendar.DATE)==1) {
		if (difference !=0 ) {
			LoadFunctions.load_stg1_bo_groups("QASHWBOBJ013","tracker","********","secLDAP","STG1_BO4Q_GROUPS");
		}
		
		//Prod
		lastLoadDt = Helper.getMaxLoadDt("select max(LOAD_DT) maxLoadDate from STG1_BO4P_USERS");
		yesterdayDate  = lastLoadDt.get(Calendar.DATE);
		System.out.println("today's date is " + todayDate + " and yesterday's date is " + yesterdayDate);
		difference = todayDate - yesterdayDate;
		System.out.println("the date difference is  "+ difference);
		System.out.println("The calendar day is " + currentDt.get(Calendar.DATE));
		//if (difference !=0 || currentDt.get(Calendar.DATE)==1) {
		if (difference !=0 ) {
			LoadFunctions.load_stg1_bo_users("PASHWBOBJ013","tracker","********","secLDAP","STG1_BO4P_USERS");
		}
		
		lastLoadDt = Helper.getMaxLoadDt("select max(LOAD_DT) maxLoadDate from STG1_BO4P_GROUPS");
		yesterdayDate  = lastLoadDt.get(Calendar.DATE);
		System.out.println("today's date is " + todayDate + " and yesterday's date is " + yesterdayDate);
		difference = todayDate - yesterdayDate;
		System.out.println("the date difference is  "+ difference);
		System.out.println("The calendar day is " + currentDt.get(Calendar.DATE));
		//if (difference !=0 || currentDt.get(Calendar.DATE)==1) {
		if (difference !=0 ) {
			LoadFunctions.load_stg1_bo_groups("PASHWBOBJ013","tracker","********","secLDAP","STG1_BO4P_GROUPS");
		}
		
		/*
		//Sandbox
		lastLoadDt = Helper.getMaxLoadDt("select max(LOAD_DT) maxLoadDate from STG1_BO4S_USERS");
		yesterdayDate  = lastLoadDt.get(Calendar.DATE);
		System.out.println("today's date is " + todayDate + " and yesterday's date is " + yesterdayDate);
		difference = todayDate - yesterdayDate;
		System.out.println("the date difference is  "+ difference);
		System.out.println("The calendar day is " + currentDt.get(Calendar.DATE));
		//if (difference !=0 || currentDt.get(Calendar.DATE)==1) {
		if (difference !=0 ) {
			LoadFunctions.load_stg1_bo_users("dlexwbobj004","tracker","********","secEnterprise","STG1_BO4S_USERS");
		}
		
		lastLoadDt = Helper.getMaxLoadDt("select max(LOAD_DT) maxLoadDate from STG1_BO4S_GROUPS");
		yesterdayDate  = lastLoadDt.get(Calendar.DATE);
		System.out.println("today's date is " + todayDate + " and yesterday's date is " + yesterdayDate);
		difference = todayDate - yesterdayDate;
		System.out.println("the date difference is  "+ difference);
		System.out.println("The calendar day is " + currentDt.get(Calendar.DATE));
		//if (difference !=0 || currentDt.get(Calendar.DATE)==1) {
		if (difference !=0 ) {
			LoadFunctions.load_stg1_bo_groups("dlexwbobj004","tracker","********","secEnterprise","STG1_BO4S_GROUPS");
		}
		*/
		LoadFunctions.load_stg1_foldersec_rel("DASHWBOBJ012","tracker","********","secLDAP","STG1_BO4_FOLDER_SEC_REL");
		LoadFunctions.load_stg1_foldersec_rel("QASHWBOBJ013","tracker","********","secLDAP","STG1_BO4_FOLDER_SEC_REL");
		LoadFunctions.load_stg1_foldersec_rel("DASHWBOBJ011","tracker","********","secLDAP","STG1_BO4_FOLDER_SEC_REL");
		LoadFunctions.load_stg1_foldersec_rel("PASHWBOBJ013","tracker","********","secLDAP","STG1_BO4_FOLDER_SEC_REL");
	}
}
