import java.net.*;
import java.util.Scanner;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

public class ChatClient extends JPanel implements ActionListener, WindowListener {

	private static boolean listening = true;
	private static boolean ready = false; //prevent messages from being sent early
	
	private static JTextField inputTextField;
	private static JTextArea viewTextArea;
	
	private static PrintWriter socketOutput;
	private static ClientThread thread;
	private static String name;
	
	public ChatClient() {
		super(new BorderLayout());
		createGUI();
	}
	
	public static void main(String[] args) throws IOException {
		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				showGUI();
			}
		});
		
		Scanner userInput = new Scanner(System.in);
		
		System.out.println("What is your name?");
		name = userInput.nextLine();
		System.out.println("Name is: " + name);
		
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
			socketOutput = out;
			
            ready = true;
            
            System.out.println("Connected.");
            
            thread = new ClientThread(socket);
            thread.start();
            
            socketOutput.println(buildMessage(Message.LOGIN)); 
            
            while (listening) {
                
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
	
	private static void showGUI() {
		JFrame frame = new JFrame("ChatClient");
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setResizable(false);
		
		ChatClient c = new ChatClient();
		frame.setContentPane(c);
		frame.addWindowListener(c);
		
		frame.pack();
		frame.setLocationRelativeTo(null); //center frame
		frame.setVisible(true);
	}
	
	private void createGUI() {
		inputTextField = new JTextField(40);
		inputTextField.addActionListener(this);
		viewTextArea = new JTextArea(20, 40);
		viewTextArea.setEditable(false);
		
		inputTextField.setFont(new Font("Arial", Font.PLAIN, 18));
		viewTextArea.setFont(new Font("Arial", Font.PLAIN, 18));
		
		viewTextArea.setLineWrap(true);
		viewTextArea.setWrapStyleWord(true);

		//Layout
		JPanel inputPane = new JPanel(new BorderLayout());
		JPanel viewPane = new JPanel(new BorderLayout());
		JPanel inputBorder = new JPanel(new BorderLayout());
		JPanel viewBorder = new JPanel(new BorderLayout());
		JPanel inputPadding = new JPanel(new BorderLayout());
		
		viewBorder.add(viewTextArea, BorderLayout.CENTER);
		inputBorder.add(inputPadding, BorderLayout.CENTER);
		inputPadding.add(inputTextField, BorderLayout.CENTER);
		viewPane.add(viewBorder, BorderLayout.CENTER);
		inputPane.add(inputBorder, BorderLayout.CENTER);
		add(inputPane, BorderLayout.SOUTH);
		add(viewPane, BorderLayout.CENTER);
		
		//Borders
		Color borderColor = new Color(230, 230, 230);
		inputPane.setBorder(BorderFactory.createMatteBorder(0, 15, 15, 15, borderColor));
		viewPane.setBorder(BorderFactory.createMatteBorder(15, 15, 15, 15, borderColor));
		inputBorder.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		viewBorder.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		inputPadding.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.WHITE));
		viewTextArea.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.WHITE));
		inputTextField.setBorder(BorderFactory.createEmptyBorder());
		
		//ScrollPane
		JScrollPane viewScrollPane = new JScrollPane(viewTextArea);
		viewBorder.add(viewScrollPane, BorderLayout.EAST);
		
	}

	public static void displayMessage(String msg) {
		viewTextArea.append(msg + "\n");
		viewTextArea.setCaretPosition(viewTextArea.getDocument().getLength());
	}
	
	private static String buildMessage(Message type) {
		if(type.equals(Message.LOGIN)) {
			return (type.getId() + name + " has logged in.");
		}
		else if(type.equals(Message.LOGOFF)) {
			return (type.getId() + name + " has logged off.");
		}
		else {
			return null;
		}
	}
	
	private static String buildMessage(Message type, String msg) {
		if(type.equals(Message.CHAT)) {
			return (type.getId() + name + ": " + msg);
		}
		else {
			return null;
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent evt) {
		if(ready) {
			socketOutput.println(buildMessage(Message.CHAT, inputTextField.getText()));
			inputTextField.setText("");
		}
	}
	
	@Override
	public void windowClosing(WindowEvent arg0) {
		System.out.println("Disconnected.");
		socketOutput.println(buildMessage(Message.LOGOFF));
		thread.setEnd(true);
		System.exit(0);
	}
	
	//----------------Unused WindowListener methods----------
	@Override
	public void windowClosed(WindowEvent evt) {}
	@Override
	public void windowActivated(WindowEvent arg0) {}
	@Override
	public void windowDeactivated(WindowEvent arg0) {}
	@Override
	public void windowDeiconified(WindowEvent arg0) {}
	@Override
	public void windowIconified(WindowEvent arg0) {}
	@Override
	public void windowOpened(WindowEvent arg0) {}
	
}
