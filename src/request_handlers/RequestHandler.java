package request_handlers;

import request_types.*;
import request_transmitters.*;
import ioQueues.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class RequestHandler extends Thread{
	
	//Fields
	protected SyncListIOQueue ioRequestQueue; //reference to the IORequestQueue
	protected int numIOrequestsPerDataTransfer = 1;
	protected int interIOprocessTime = 100;
	protected int pollingTime = 100; //amount of time between unsuccessful polls
	protected int numAttempts = 10; //number of attempts to get item from queue before proceeding to the data transfer anyway
	protected Transmitter transmitter;
	
	public RequestHandler(	
			SyncListIOQueue requestQeue,
			int numIOrequestsPerDataTransfer, 
			Transmitter transmitter) throws Exception
	{
		if(requestQeue == null) {
			throw new Exception("constructor queue not initialized");
		}
		this.ioRequestQueue = requestQeue;
		this.numIOrequestsPerDataTransfer = numIOrequestsPerDataTransfer;
		this.transmitter = transmitter;
	}
	
	
	
	@Override
	public void run() {
		//Perform Data transfers until application is closed
		
		try {
			//list to hold the IO requests for one data transfer
			SyncListIOQueue dataTransferIORequests;
			
			
			while(true) {
				//Perform 1 data transfer
				
				
				//GET all IO requests for the data transfer into the cached list
				dataTransferIORequests = getRequestsForTransfer();
				
				//Perform the IO requests of the data transfer
				if(dataTransferIORequests != null && !dataTransferIORequests.isEmpty()) {
					performDataTransferIORequests(dataTransferIORequests);
				}else {
					System.out.println("No Requests...");
					Thread.sleep(pollingTime);
				}
				
				
				/*
				
				
				//Clear cache IO queue and start time measurements
				ArrayList<Long> transmitterTimes = new ArrayList<Long>();
				transmitterTimes.add(System.currentTimeMillis());
				
				//Transmit the data
				SqlRequestTransmitter transmitter = new SqlRequestTransmitter(); //open connection
				
				while(!IOrequestsToTransfer.isEmpty()) {
					System.out.println("Handler Thread is transmitting request " + IOrequestsToTransfer.peek().operation);
					transmitter.performIORequest(IOrequestsToTransfer.poll());
					transmitterTimes.add(System.currentTimeMillis()); //measure time
				}
				transmitter.closeConnection(); //close connection
				
				//Output Time results
				Iterator<Long> timeIter = transmitterTimes.iterator();
				while(timeIter.hasNext()) {
					System.out.println(timeIter.next());
				}
				*/
				
			}
		}catch(Exception e){
			System.out.println(e);
		}
		
		
	}
	
	private SyncListIOQueue getRequestsForTransfer() throws InterruptedException {
		
		SyncListIOQueue dataTransferIORequests = new SyncListIOQueue(this.numIOrequestsPerDataTransfer);
		IORequest tempRequest;
		
		//GET all IO requests for the data transfer into the cached list
		int dataTransSize = this.numIOrequestsPerDataTransfer;
		while(dataTransSize > 0) {
			
			//try getting an item from the IO queue
			tempRequest = this.ioRequestQueue.poll();
			if(tempRequest == null) {
				int attempt = numAttempts;
				
				while(tempRequest == null) {
					Thread.sleep(pollingTime);
					
					if(attempt-- <= 0) {
						return dataTransferIORequests; //all attempts were made to get the item from queue
					}
				}
			}
			
			dataTransferIORequests.add(tempRequest); //add request to data transfer cached list
			dataTransSize--;
			
			Thread.sleep(interIOprocessTime);
		}
		
		return dataTransferIORequests;
	}
	
	private void performDataTransferIORequests(SyncListIOQueue dataTransferIORequests) throws Exception 
	{
		synchronized(dataTransferIORequests) {
			
			if(dataTransferIORequests != null && !dataTransferIORequests.isEmpty()) {
				
				System.out.println("---------START DATA TRANSFER-------");
				
				//set up connection
				IORequest temReq = dataTransferIORequests.peek();
				String[] params = temReq.targetConnectionParams;
				this.transmitter.setUpConnection(params);
				
				//transmit the data
				while(!dataTransferIORequests.isEmpty()) {
					transmitter.performIORequest(dataTransferIORequests.poll());
				}
				
				//close connection
				transmitter.closeConnection();
				
				System.out.println("---------END DATA TRANSFER-------");
				
			}
		}
	}
	
}
