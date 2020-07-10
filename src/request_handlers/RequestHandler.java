package request_handlers;

import request_types.*;
import request_transmitters.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.Random;

public class RequestHandler extends Thread{
	
	//Fields
	protected ConcurrentLinkedQueue<SqlToSqlRequest> ioRequestQueue; //reference to the IORequestQueue
	protected int queueCapacity;
	protected int numIOrequestsPerDataTransfer = 1;
	
	
	//Constructor(s)
	public RequestHandler(	ConcurrentLinkedQueue<SqlToSqlRequest> requestQeue, 
							int queueCapacity, 
							int numIOrequestsPerDataTransfer) 
							throws Exception
	{
		if(requestQeue == null) {
			throw new Exception("constructor queue not initialized");
		}
		this.ioRequestQueue = requestQeue;
		this.queueCapacity = queueCapacity;
		this.numIOrequestsPerDataTransfer = numIOrequestsPerDataTransfer;
	}
	
	
	
	@Override
	public void run() {
		//Perform Data transfers until application is closed
		
		//list to hold the IO requests for one data transfer
		ConcurrentLinkedQueue<SqlToSqlRequest> IOrequestsToTransfer = new ConcurrentLinkedQueue<SqlToSqlRequest>();
		SqlToSqlRequest tempRequest;
		
		SqlToSqlRequestTransmitter transmitter = new SqlToSqlRequestTransmitter();
		
		long startTime;
		long ioTimes[];
		
		Random rand = new Random();
		
		while(true) {
			//One data transfer
			
			
			/*
			 * GET the IO requests for the data transfer
			 */
			
			
			IOrequestsToTransfer.clear();
			
			//build IOrequestsToTransfer list using the IO requests in the ioRequestQueue
			int dataTransSize = this.numIOrequestsPerDataTransfer;
			while(dataTransSize > 0) {
				tempRequest = this.ioRequestQueue.poll();
				
				if(tempRequest != null) {
					//add request to temporary list
					IOrequestsToTransfer.add(tempRequest);
					dataTransSize--;
				}
				
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
			
			/*
			 * 	Perform the IO requests of the data transfer
			 */
			
			System.out.println("---------START DATA TRANSFER-------");
			
			ioTimes = new long[this.numIOrequestsPerDataTransfer + 1];
			ioTimes[0] = System.currentTimeMillis();
			
			
			for(int i=1; i<= this.numIOrequestsPerDataTransfer; i++) {
				
				System.out.println("Handler Thread is transmitting request " + IOrequestsToTransfer.peek().query);
				transmitter.performIORequest(IOrequestsToTransfer.poll());
				ioTimes[i] = System.currentTimeMillis();
			}
			
			
			/*
			 *  Output results
			 */
			
			for(int i=0; i<= this.numIOrequestsPerDataTransfer; i++) {
				System.out.println(ioTimes[i]);
			}
			
			System.out.println("---------END DATA TRANSFER-------");
		}
		
	}
	
	
	
}
