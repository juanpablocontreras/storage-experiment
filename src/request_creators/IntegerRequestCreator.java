package request_creators;

import java.util.concurrent.ConcurrentLinkedQueue;

public class IntegerRequestCreator extends RequestCreator{

	public IntegerRequestCreator(ConcurrentLinkedQueue<Integer> requestQeue, int queueCapacity) throws Exception {
		super(requestQeue,queueCapacity);
	}
	
	@Override
	public void run() {
		int value = 0;
		while(true) {
			
			if(this.ioRequestQueue.size() < this.queueCapacity) {
				this.ioRequestQueue.add(value);
				System.out.println("created request: " + value++);
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
