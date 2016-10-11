import java.net.*;
import java.io.*;

public class ServerThread extends Thread {

	private Socket socket = null;
	
	public ServerThread(Socket socket) {
		super("ServerThread");
		this.socket = socket;
	}
	
	public void run() {
		
		try (
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		) {
			String inputLine;
			
			ChatServer.insertClient(out);
            
            while ((inputLine = in.readLine()) != null) {
            	System.out.println(inputLine);
                ChatServer.insertMessage(inputLine);
                
                if(inputLine.equals("Disconnect")) {
                	break;
                }
            }
            socket.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
}
