package request_transmitters;

import request_types.SqlToSqlRequest;

public class SqlToSqlRequestTransmitter {
	
	
	public void performIORequest(SqlToSqlRequest request) {
		
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
