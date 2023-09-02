package javachat.server;
import java.util.*;
import java.io.*;
import java.net.*;
public class ChatClient {
  private String hostname;
  private int port;
  private String username;
	public ChatClient(String hostname, int port) {
		this.hostname=hostname;
		this.port=port;
	}
	public static void main(String[] args) {
		if(args.length<2)
			return;
		String hostname=args[0];
		int port=Integer.parseInt(args[1]);
		ChatClient client= new ChatClient(hostname,port);
		client.execute();
	}

	public void execute() {
        try {
            Socket socket = new Socket(hostname, port);
 
            System.out.println("Connected to the chat server");
 
            new ReadThread(socket, this).start();
            new WriteThread(socket, this).start();
 
        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O Error: " + ex.getMessage());
        }
 
    }

void setUserName(String userName) {
    this.username = userName;
}

String getUserName() {
    return this.username;
}
 class ReadThread extends Thread{
	 private Socket socket;
	 private ChatClient client;
	 private BufferedReader br;
	  public ReadThread(Socket socket, ChatClient client) {
		super();
		this.socket = socket;
		this.client = client;
	try {
		InputStream input= socket.getInputStream();
		 br= new BufferedReader(new InputStreamReader(input));
		
	}catch(IOException e1){
		System.out.println("I/O Error: " + e1.getMessage());
	}
	  }
	public void run() {
		  while(true) {
			  try {
				  String response=br.readLine();
				  System.out.println("\n"+response);
				  if(client.getUserName()!=null) {
					  System.out.println("["+ client.getUserName()+"]");
				  }
				  
			  }
			  catch(IOException e1){
					System.out.println("I/O Error: " + e1.getMessage());
					  e1.printStackTrace();
		                break;
				}
		  }
		 
	  }
  }
 class WriteThread extends Thread{              
	 private Socket socket;
	 private ChatClient client;
	 private PrintWriter pw;
	 

  public WriteThread(Socket socket, ChatClient client) {
		super();
		this.socket = socket;
		this.client = client;
		
		try {
			OutputStream output= socket.getOutputStream();
			 pw= new PrintWriter(output,true);
			
		}catch(IOException e1){
			System.out.println("I/O Error: " + e1.getMessage());
		}
  }



public void run() {

      Console console = System.console();
	  String userName=console.readLine("Enter Your name");
	  client.setUserName(userName);
	  String text;
	  do {
		  text=console.readLine();
		  pw.println(text);
	  }while(!text.equals("bye"));
	  try {
		  socket.close();
	  }
	  catch(IOException e1){
			System.out.println("I/O Error: " + e1.getMessage());
		
	  
	  }
 }
 }
}
