package request_transmitters;

import java.sql.*;

import request_types.*;

public class SqlRequestTransmitter extends Transmitter{
	
	public Connection sqlcon = null;
	
	/*
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
	
	*/

	@Override
	public void setUpConnection(String params[]) {
		//params:
		//[0] Database name
		//[1] Database user
		//[2] Database user password
		//[3] Table name
		
		
		System.out.println("setting up transmitter connection");
		System.out.println(params[0]);
		System.out.println(params[1]);
		System.out.println(params[2]);
		System.out.println(params[3]);
		
	}


	@Override
	public void performIORequest(IORequest request) {
		
		System.out.println("performing the IO request: " + request.id);
	}


	@Override
	public void closeConnection() {
		
		System.out.println("closing transmitter connection");
	}
	
	/*
	public void performIORequest(IORequest request) {
		
		//Statement stmt = this.sqlcon.createStatement(); 
		
		//stmt.executeUpdate(request.query);
		
	}
	
	public void closeConnection() throws SQLException {
		this.sqlcon.close();
	}
	*/
	
	
	
}
