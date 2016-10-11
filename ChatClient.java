import java.net.*;
import java.util.LinkedList;
import java.util.Scanner;
import java.io.*;

public class ChatClient {

	private static LinkedList<String> messageQueue = new LinkedList<String>();
	private static boolean listening = true;
	
	public static void main(String[] args) throws IOException {
		
		Scanner userInput = new Scanner(System.in);
		System.out.println("Enter host IP.");
		String ipAddr = userInput.nextLine();
		
		System.out.println("Host IP is: " + ipAddr);
		
		System.out.println("Enter host port.");
		int port = userInput.nextInt();
		
		System.out.println("Host port is " + port);
		System.out.println("Connecting . . . ");
		
		try (
			Socket socket = new Socket(InetAddress.getByName(ipAddr), port);
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		) {
			BufferedReader chatInput = new BufferedReader(new InputStreamReader(System.in));
			
			String fromServer;
            String fromUser;

            System.out.println("Connected.");
            
            new ClientThread(socket, messageQueue).start();
            
            while (listening) {
                
                fromUser = chatInput.readLine();
                if (fromUser != null) {
    
            		System.out.println("Client: " + fromUser);
            		out.println(fromUser);
            	
                }
            }
		}
		catch(UnknownHostException e) {
			System.err.println("Unknown host: " + ipAddr);
			System.exit(1);
		}
		catch(IOException e) {
			System.err.println("Couldn't get I/O for connection to host.");
			System.exit(1);
		}
	}
	
	public static void endChat() {
		System.exit(0);
	}

}
