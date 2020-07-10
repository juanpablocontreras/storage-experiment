package request_creators;

import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class RequestCreator<RequestType> extends Thread {
	
	protected ConcurrentLinkedQueue<RequestType> ioRequestQueue; //reference to the IORequestQueue
	protected int queueCapacity;
	
	public RequestCreator(ConcurrentLinkedQueue<RequestType> requestQeue, int queueCapacity) throws Exception {
		if(requestQeue == null) {
			throw new Exception("constructor queue not initialized");
		}
		this.ioRequestQueue = requestQeue;
		this.queueCapacity = queueCapacity;
	}
	
	
	
	
}
