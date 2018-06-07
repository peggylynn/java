import com.crystaldecisions.sdk.framework.CrystalEnterprise;
import com.crystaldecisions.sdk.framework.IEnterpriseSession;
import com.crystaldecisions.sdk.framework.ISessionMgr;
import com.crystaldecisions.sdk.occa.infostore.IInfoObject;
import com.crystaldecisions.sdk.occa.infostore.IInfoObjects;
import com.crystaldecisions.sdk.occa.infostore.IInfoStore;
import com.crystaldecisions.sdk.properties.IProperties;
import com.crystaldecisions.sdk.properties.IProperty;


public class Test_Recurring {
	public static void main(String[] args) {
		load_temp_recurring_jobs("PASHWBOBJ013","tracker","libambini*8","secLDAP","STG1_BO4P_RECURRINGJOBS", "Prod");
	}
	public static void load_temp_recurring_jobs(String EnterpriseEnv, String login, String pswd,String auth, String destTblNm, String environ){
		//All this does is load the temp_recurring_jobs so I can test.  Loading from my laptop is TOO SLOWWWWW.
		
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
}
