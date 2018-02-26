package attempt2;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server 
{
	private ServerSocket serverSock;
	private ExecutorService ex;
	private boolean shouldRun;
	private List<User> userList;
	
	public Server()
	{
		try 
		{
			serverSock = new ServerSocket(4444);
			ex = Executors.newCachedThreadPool();
			shouldRun = true;
			userList = new ArrayList<User>();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void runServer()
	{
		try
		{
			while(shouldRun)
			{
				User u = new User(serverSock.accept(),userList);
				System.out.println("New User Connected successfuly!");
				userList.add(u);
				ex.execute(u);
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void close() throws IOException
	{
		shouldRun = false;
		ex.shutdown();
		serverSock.close();
	}
	
	
	
}


class User implements Runnable
{

	private Socket userSock;
	private BufferedReader userIn;
	private DataOutputStream userOut;
	private List<User> list;
	
	public void setList(List<User> l)
	{
		list = l;
	}
	
	public User(Socket sock,List<User> l) throws IOException
	{
		userSock = sock;
		userIn = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		userOut = new DataOutputStream(sock.getOutputStream());
		list = l;
	}
	
	public Socket getUserSock() {
		return userSock;
	}

	public BufferedReader getUserIn() {
		return userIn;
	}

	public DataOutputStream getUserOut() {
		return userOut;
	}

	@Override
	public void run() 
	{
		try
		{
			String message = "";
			do
			{
				message = userIn.readLine();
				System.out.println("Port: "+userSock.getPort()+"  :  "+message);
				for(User u : list)
					u.getUserOut().writeBytes(message+"\n");
				
			}
			while(message!="exit\n");
			//close();
		}
		catch(SocketException se) {System.out.println("Error!");}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
	}
	
	public void close() throws IOException
	{
		userIn.close();
		userOut.close();
		userSock.close();
	}
	
}
