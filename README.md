# java
Java source code for BOBJ sdk and others

Hello Esteemed Visitor!
Here a couple examples of java code I've written.  These are against Business Objects SDK.  I called it the Tracker application and it was essentially a home-grown ETL tool without a user interface.

Most of the files here were to help us understand the meta data on BOBJ reports.  BOBJ doesn't come with very good monitoring and the query tool yields a hierarchical output that makes trend analysis difficult.

The vendor frowned upon querying the system tables directly and not wanting to risk voiding any support agreements, I created a separate database to house the history of the meta data from Business Objects.

My database was more normalized than the vendor's.  Their design was to group all reports, recurring instances, successful and failed instances into the same table in the hierarchical manner I mentioned.  This was going to make reporting more difficult if I merely created a non-hierarchical table with the same data.  I therefore split up the object types into different tables:  Reports, Recurring Instances, and a third one for successes and failures.

Furthermore, the property bags BOBJ stores on Crystal Reports versus Web Intelligence reports were different enough that I need to stage them separately.  Ultimately I designed a reporting table that housed all report types.

In a similar manner I stored data on all the users and groups along with folder security to be able to better monitor the security set up.

With these tables, I created a number of admin reports to enhance the monitoring of our BOBJ system.  This made it much easier to spot problems before end-users did.  

I designed this before Business Objects created their Audit Universe.  However, the Audit Universe is cryptic and difficult to use so we continued to use the Tracker reports for many years.
