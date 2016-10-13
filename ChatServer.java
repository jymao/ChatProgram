import java.net.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
import java.io.*;

public class ChatServer {

	//meant as a way for new logins to see the last few messages in the chat
	private static LinkedList<String> messageQueue = new LinkedList<String>();
	private static ArrayList<PrintWriter> clientOutputs = new ArrayList<PrintWriter>();
	
	private static final int QUEUE_SIZE = 10;
	
	public static void main(String[] args) {
		
		Scanner input = new Scanner(System.in);
		System.out.println("Enter port number.");
		int port = input.nextInt();
		
		System.out.println("Port is " + port);
		System.out.println("Starting server . . . ");
		
		boolean listening = true;
        
        try (ServerSocket serverSocket = new ServerSocket(port)) { 
        	
        	System.out.println("Ready.");
        	
            while (listening) {
	            new ServerThread(serverSocket.accept()).start();
	        }
	    } catch (IOException e) {
            System.err.println("Could not listen on port " + port);
            System.exit(-1);
        }
	}
	
	public static synchronized void insertClient(PrintWriter client) {
		clientOutputs.add(client);
	}
	
	public static synchronized void insertMessage(String msg) {
		if(messageQueue.size() >= QUEUE_SIZE) {
			messageQueue.remove();
		}
		messageQueue.add(msg);
		
		//inform clients of new message
		for(PrintWriter out: clientOutputs) {
			out.println(msg);
		}
	}

}
