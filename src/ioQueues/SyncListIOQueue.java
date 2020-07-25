package ioQueues;

import java.util.ArrayList;
import java.util.List;
import request_types.IORequest;

public class SyncListIOQueue {
	
	private ArrayList<IORequest> queue; //Queue that holds the IO requests
	private int maxSize; //maximum size the Queue can grow to (maximum number of elements
	
	public void SyncListIOQueue(int maxSize) {
		this.queue = new ArrayList<IORequest>();
		this.maxSize = maxSize;
	}
	
	
	public synchronized boolean add(IORequest request) {
		if(this.queue.size() < this.maxSize) {
			this.queue.add(request);
			return true;
		}else {
			return false;
		}
	}
	
	public synchronized IORequest poll() {
		if(!this.queue.isEmpty()) {
			return this.queue.remove(0);
		}else {
			return null;
		}
	}

}
