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
            	System.err.println(inputLine);
            	
            	//parse message type
            	int msgType = (int) inputLine.charAt(0);
            	
                ChatServer.insertMessage(inputLine.substring(1));
                
                if(msgType == Message.LOGOFF.getId()) {
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
