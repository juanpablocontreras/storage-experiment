package request_transmitters;

import java.sql.*;

import request_types.SqlRequest;

public class SqlRequestTransmitter {
	
	public Connection sqlcon = null;
	
	
	public SqlRequestTransmitter() throws Exception {
		
	
		//db driver registration
		Class.forName("com.mysql.jdbc.Driver"); 

		//connection
		Connection sqlcon = DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/" + 
				"STRG_EXP_TARGET?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",
				"juan",
				"Matusalen13"); 
		this.sqlcon = sqlcon;
	}
	
	public void performIORequest(SqlRequest request) throws Exception {
		
		Statement stmt = this.sqlcon.createStatement(); 
		
		stmt.executeUpdate(request.query);
		
	}
	
	public void closeConnection() throws SQLException {
		this.sqlcon.close();
	}
	
}
