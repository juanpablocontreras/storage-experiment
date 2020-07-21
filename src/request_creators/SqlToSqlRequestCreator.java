package request_creators;

import request_types.SqlRequest;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.sql.*;

public class SqlToSqlRequestCreator extends RequestCreator{

	public SqlToSqlRequestCreator(ConcurrentLinkedQueue<SqlRequest> requestQeue, int queueCapacity) throws Exception {
		super(requestQeue, queueCapacity);
	}
	
	
	@Override
	public void run() {
		
		//Access database and send every row of a table to the queue
		System.out.println("Request Creator thread started");
		
		try {
			
			//db driver registration
			Class.forName("com.mysql.jdbc.Driver"); 
			
			//connection
			Connection sqlcon = DriverManager.getConnection(
			"jdbc:mysql://localhost:3306/" + 
			"STRG_EXP_ORIG?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",
			"juan",
			"Matusalen13"); 
			
			//get every row of table Tweets
			Statement stmt=sqlcon.createStatement(); 
			ResultSet rs = stmt.executeQuery("SELECT * FROM Tweets");
			
			//for every Tweet: wrap it into an IO request and send it to the IO queue
			String str_i = "INSERT INTO Tweets VALUES (";
			String str_e = ")";
			
			while(rs.next()) {
				String query = str_i;
				query += rs.getInt(1);
				query += ",";
				query += "\" " + rs.getString(2) + "\"";
				query += ",";
				query += "\" " + rs.getString(3) + "\"";
				query += str_e;
						
				SqlRequest request = new SqlRequest(query);
				this.ioRequestQueue.add(request);
				System.out.println("Request Creator thread created: " + query );
			}
			
			sqlcon.close();
			
		}catch(Exception e) {
			System.out.println(e);
		}
		
		

	}

}
