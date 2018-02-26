package attempt2;

public class StartClient 
{
	public static void main(String[] args)
	{
		Client client = new Client("localhost",4444,"Cristi");
		client.runClient();
		
		
	}
}
