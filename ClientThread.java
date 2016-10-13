import java.net.*;
import java.io.*;

public class ClientThread extends Thread{
	
	private Socket socket = null;
	private boolean end = false;
	
	public ClientThread(Socket socket) {
		super("ClientThread");
		this.socket = socket;
	}
	
	public void run() {
		
		try (
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		) {
			String inputLine;
            
            while ((inputLine = in.readLine()) != null) {
            	ChatClient.displayMessage(inputLine);

            	if(end) {
            		break;
            	}
            }
            socket.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setEnd(boolean end) { this.end = end; }
}
