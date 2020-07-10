package request_creators;

import request_types.SqlToSqlRequest;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SqlToSqlRequestCreator extends RequestCreator{

	public SqlToSqlRequestCreator(ConcurrentLinkedQueue<SqlToSqlRequest> requestQeue, int queueCapacity) throws Exception {
		super(requestQeue, queueCapacity);
	}
	
	
	@Override
	public void run() {
		
		int value = 0;
		String query = "query: ";
		
		
		while(true) {
			if(this.ioRequestQueue.size() < this.queueCapacity) {
				this.ioRequestQueue.add(new SqlToSqlRequest(query + value));
				System.out.println("Request Creator thread created " + query + value++);
			}
			
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		

	}

}
