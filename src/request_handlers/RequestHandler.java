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
	protected int interIOprocessTime = 1000;
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
			ArrayList<IORequest> dataTransferIORequests;
			
			
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
			}
		}catch(Exception e){
			System.out.println(e);
		}
		
		
	}
	
	private ArrayList<IORequest> getRequestsForTransfer() throws InterruptedException {
		
		//SyncListIOQueue dataTransferIORequests = new SyncListIOQueue(this.numIOrequestsPerDataTransfer);
		ArrayList<IORequest> dataTransferIORequests = new ArrayList<IORequest>();
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
	
	private void performDataTransferIORequests(ArrayList<IORequest> dataTransferIORequests) throws Exception 
	{
		synchronized(dataTransferIORequests) {
			
			if(dataTransferIORequests != null && !dataTransferIORequests.isEmpty()) {
				
				//output IOqueue wait times
				
				Iterator<IORequest> IOiter = dataTransferIORequests.iterator();
				while(IOiter.hasNext()) {
					IORequest tempReq = IOiter.next();
					long queueTime = tempReq.queueTimePolled - tempReq.queueTimeArrival;
					//System.out.println("Request ID " + tempReq.id + " Queue Arrival time: " + tempReq.queueTimeArrival);
					//System.out.println("Request ID " + tempReq.id + " Queue Retirval time: " + tempReq.queueTimePolled);
					System.out.println("Handler detected request ID " + tempReq.id + " spent " + queueTime + " in IO queue");
				}
				
				
				//Set up transmitter time measurements
				ArrayList<Long> transmitterTimes = new ArrayList<Long>();
				
				System.out.println("---------START DATA TRANSFER-------");
				
				//set up connection
				IORequest temReq = dataTransferIORequests.get(0);
				String[] params = temReq.targetConnectionParams;
				this.transmitter.setUpConnection(params);
				
				//transmit the data
				transmitterTimes.add(System.currentTimeMillis());
				
				for(IORequest request:dataTransferIORequests) {
					transmitter.performIORequest(request);
					transmitterTimes.add(System.currentTimeMillis());
				}
				
				//close connection
				transmitter.closeConnection();
				
				//output transmitter times
				System.out.println("Transmitter times");
				for(int i=1; i<transmitterTimes.size();i++) {
					System.out.println(transmitterTimes.get(i) - transmitterTimes.get(i-1));
				}
				
				System.out.println("---------END DATA TRANSFER-------");
				
			}
		}
	}
	
}
