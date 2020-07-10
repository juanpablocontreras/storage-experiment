package experiment;

import java.util.concurrent.ConcurrentLinkedQueue;
import request_creators.*;
import request_transmitters.*;
import request_types.*;
import request_handlers.*;


public class controller {

	
	
	public static void main(String[] args) {
		// initiates the request creator, IO queue, request handler, and request transmitter
		
		System.out.println("controller started...");
		
		ConcurrentLinkedQueue<SqlToSqlRequest> ioQueue = new ConcurrentLinkedQueue<SqlToSqlRequest>();
		int ioQueueCapacity = 20;
		
		int numIORequestsPerDataTransfer = 5;
		
		try {
			SqlToSqlRequestCreator creator = new SqlToSqlRequestCreator(ioQueue, ioQueueCapacity);
			RequestHandler handler = new RequestHandler(ioQueue, ioQueueCapacity, numIORequestsPerDataTransfer);
			
			creator.start();
			handler.start();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}




