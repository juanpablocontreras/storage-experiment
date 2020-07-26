package request_types;

public class SqlRequest extends IORequest{
	
	public String target_db_name;
	public String target_db_table;

	public SqlRequest( 
			long size, 
			long id,
			String[] targetConnectionParams,
			String target_db_name,
			String target_db_table)
	{
		super(size, id, targetConnectionParams);
		this.target_db_name = target_db_name;
		this.target_db_table = target_db_table;
		
	}
}
