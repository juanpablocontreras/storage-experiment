package request_transmitters;

import request_types.IORequest;

public abstract class Transmitter {
	
	public abstract void setUpConnection(String[] params);
	
	public abstract void performIORequest(IORequest request);
	
	public abstract void closeConnection();
	
}
