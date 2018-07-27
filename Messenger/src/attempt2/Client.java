package attempt2;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client 
{
	private Socket sock;
	private BufferedReader input;
	private DataOutputStream output;
	private String ip;
	private int portNum;
	private ExecutorService ex;
	private String name;
	
	
	public Client(String ip, int port,String nam)
	{
		try
		{
			name = nam;
			this.ip = ip;
			portNum = port;
			sock = new Socket(ip,portNum);
			input = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			output = new DataOutputStream(sock.getOutputStream());
			ex = Executors.newFixedThreadPool(2);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void runClient()
	{
		ex.execute(new Runnable() 
		{

			@Override
			public void run() 
			{
				try(Scanner sc = new Scanner(System.in))
				{
					String message = "";
					do
					{
						//System.out.print("You: ");
						message = sc.nextLine();
						output.writeBytes(name+": "+message+"\n");
					}
					while(!message.equals("exit"));//!!!!problem
					close();
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
				
			}
			
		});
		
		ex.execute(new Runnable()
		{
			
			@Override
			public void run() 
			{
				try
				{
					String chat = "";
					do
					{
						chat = input.readLine();
						System.out.println(chat);
					}
					while(!chat.equals("exit"));
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
				
			}
			
		});
		
		
	}
	
	public void close() throws IOException
	{
		input.close();
		output.close();
		sock.close();
		ex.shutdown();
	}
	
	
}
