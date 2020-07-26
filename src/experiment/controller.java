package experiment;

import java.util.concurrent.ConcurrentLinkedQueue;
import request_creators.*;
import request_transmitters.*;
import request_types.*;
import request_handlers.*;
import ioQueues.*;


public class controller {

	
	
	public static void main(String[] args) {
		// initiates the request creator, IO queue, request handler, and request transmitter
		int ioQueueCapacity = 20;
		int numIORequestsPerDataTransfer = 5;
		SyncListIOQueue ioqueue = new SyncListIOQueue(ioQueueCapacity);
		
		System.out.println("controller started...");
		
		try {
			
			//Instantiate SQL request creator
			SqlRCreator creator = new SqlRCreator(
					ioqueue, 
					"EXP_ORIG",
					"EXP_TARGET",
					"juan",
					"LapinCoquin13",
					"Small100_ORIG");
			
			Transmitter sqlTransmitter = new SqlRequestTransmitter();
			RequestHandler handler = new RequestHandler(
					ioqueue, 
					numIORequestsPerDataTransfer,
					sqlTransmitter);
			
			creator.start();
			handler.start();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}




