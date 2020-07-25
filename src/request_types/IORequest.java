package request_types;

public class IORequest {
	
	public long size;			//size of the data in the io request
	public long id;				//id of the IO request
	public long queueTimeArrival; //System time when arrived in Queue
	public long queueTimePolled; //System time when polled from queue
	
	public String operation; //Operation or Query that will be executed by Transmitter (choose appropriate transmitter)
	public OperationType optype;
}
