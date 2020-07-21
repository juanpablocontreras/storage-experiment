package request_handlers;

import request_types.*;
import request_transmitters.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class RequestHandler extends Thread{
	
	//Fields
	protected ConcurrentLinkedQueue<SqlRequest> ioRequestQueue; //reference to the IORequestQueue
	protected int queueCapacity;
	protected int numIOrequestsPerDataTransfer = 1;
	protected int interIOprocessTime = 100;
	
	
	//Constructor(s)
	public RequestHandler(	ConcurrentLinkedQueue<SqlRequest> requestQeue, 
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
		
		try {
			//list to hold the IO requests for one data transfer
			ConcurrentLinkedQueue<SqlRequest> IOrequestsToTransfer = new ConcurrentLinkedQueue<SqlRequest>();
			SqlRequest tempRequest;
			
			
			
			
			
			while(true) {
				//One data transfer for each loop in
				
				
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
						Thread.sleep(interIOprocessTime);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				
				/*
				 * 	Perform the IO requests of the data transfer
				 */
				
				System.out.println("---------START DATA TRANSFER-------");
				
				//Clear cache IO queue and start time measurements
				ArrayList<Long> transmitterTimes = new ArrayList<Long>();
				transmitterTimes.add(System.currentTimeMillis());
				
		
				//Transmit the data
				SqlRequestTransmitter transmitter = new SqlRequestTransmitter(); //open connection
				
				while(!IOrequestsToTransfer.isEmpty()) {
					System.out.println("Handler Thread is transmitting request " + IOrequestsToTransfer.peek().query);
					transmitter.performIORequest(IOrequestsToTransfer.poll());
					transmitterTimes.add(System.currentTimeMillis()); //measure time
				}
				transmitter.closeConnection(); //close connection
				
				//Output Time results
				Iterator<Long> timeIter = transmitterTimes.iterator();
				while(timeIter.hasNext()) {
					System.out.println(timeIter.next());
				}
				
				System.out.println("---------END DATA TRANSFER-------");
			}
		}catch(Exception e){
			System.out.println(e);
		}
		
		
	}
	
	
	
}
