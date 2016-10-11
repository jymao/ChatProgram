import java.net.*;
import java.util.LinkedList;
import java.io.*;

public class ClientThread extends Thread{
	
	private Socket socket = null;
	private LinkedList<String> messageQueue = null;
	
	public ClientThread(Socket socket, LinkedList<String> messageQueue) {
		super("ClientThread");
		this.socket = socket;
		this.messageQueue = messageQueue;
	}
	
	public void run() {
		
		try (
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		) {
			String inputLine;
            
            while ((inputLine = in.readLine()) != null) {
            	insertMessage(inputLine);
            	System.out.println("Server: " + inputLine); 
            	
            	if(inputLine.equals("Disconnect")) {
            		ChatClient.endChat();
            		break;
            	}
            }
            socket.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private void insertMessage(String msg) {
		if(messageQueue.size() >= ChatServer.QUEUE_SIZE) {
			messageQueue.remove();
		}
		messageQueue.add(msg);
	}
}
