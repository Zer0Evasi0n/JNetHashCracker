package synthetics;
import javax.swing.*;

import synthetics.net.Client;

import java.awt.GridBagLayout;
import java.awt.event.*;

@SuppressWarnings("serial")
public class GUI extends JFrame {
	
	//Other constructors
	public static JobManagement jb;
	
	//Panels
	JPanel ratePanel = new JPanel();
		JButton rateButton = new JButton("Rate");
	JPanel listPanel = new JPanel();
		JButton listButton = new JButton("List");
	JPanel listenerPanel = new JPanel();
		JButton listenerButton = new JButton("Listener");
	JPanel consoleInputPanel = new JPanel();
		static JTextField consoleInput = new JTextField(60);
	JPanel consoleTextPanel = new JPanel();
		static JTextArea consoleText = new JTextArea();
		static Console console = new Console(consoleText);
	JPanel statusPanel = new JPanel();
		static StatusBar hashStatus = new StatusBar();

	public GUI() {
		
		//Setting panels location and size
		consoleTextPanel.setLayout(new GridBagLayout());
			//consoleTextPanel.setLocation(10,10);
			//consoleTextPanel.setSize(330,20);
		consoleInputPanel.setLayout(null);
			consoleInputPanel.setLocation(10,140);
			consoleInputPanel.setSize(460,20);
		ratePanel.setLayout(null);
			ratePanel.setLocation(370,10);
			ratePanel.setSize(250,30);
		listPanel.setLayout(null);
			listPanel.setLocation(370,50);
			listPanel.setSize(250,30);
		listenerPanel.setLayout(null);
			listenerPanel.setLocation(370,90);
			listenerPanel.setSize(250,30);
		statusPanel.setLayout(null);
			statusPanel.setLocation(10,160);
			statusPanel.setSize(400,30);
		
		//Setting button location and size
		consoleText.setLocation(10,10);
			consoleText.setSize(350,120);
		consoleInput.setLocation(0,0);
			consoleInput.setSize(460,20);
		rateButton.setLocation(0,0);
			rateButton.setSize(100,30);
		listButton.setLocation(0,0);
			listButton.setSize(100,30);
		listenerButton.setLocation(0,0);
			listenerButton.setSize(100,30);
		hashStatus.setLocation(0,0);
			hashStatus.setSize(400,30);
		
		//Add buttons to panels
		consoleTextPanel.add(consoleText);
		consoleInputPanel.add(consoleInput);
		ratePanel.add(rateButton);
		listPanel.add(listButton);
		listenerPanel.add(listenerButton);
		statusPanel.add(hashStatus);
		
		//Add panels to Frame
		add(consoleText);
		add(ratePanel);
		add(listPanel);
		add(listenerPanel);
		add(consoleInputPanel);
		add(statusPanel);
		
		//Listeners
		consoleInput.addKeyListener(new KeyAdapter() 
		{
			@SuppressWarnings("unchecked")
			public void keyPressed(KeyEvent e) 
			{
					int key = e.getKeyCode();
					if(key == KeyEvent.VK_ENTER)
					{
						jb = new JobManagement();
						String inputCommand = consoleInput.getText();
						console.swrite("> " + inputCommand);
						String command = inputCommand;
						int number;
						String[] split = inputCommand.split(" ");
						if (split[0].equals("list")) {
							if (Startup.getConnectedNumbers() == 0) {
								out("[JN]: There are no connected clients");
							} else {
								for (int i = 0; i < Startup.clients.size(); i ++) {
									Startup.c = (Client)Startup.clients.get(i);
									if (Startup.c.isConnected()) {
										out("     [" + Startup.c.clientID + "] Client " + Startup.c.cs.getInetAddress().getHostAddress() + " " + Startup.c.clientHashRate + "kH/s" + " || Hashing: " + Startup.c.isCracking);
									}
								}
							}
						} else if (split[0].equals("listener")) {
							split = command.split(" ");
							try {
								if (split[1].equals("add")) {
									try {
										number = Integer.parseInt(split[2]);
										if (!Startup.listeners.contains(number)) {
											Startup.listeners.add(number);
											out("[JN]: Added listener " + number);
											Startup.addListener(number);
										} else {
											out("[JN]: Listener " + number + " already existed");
										}
									} catch (Exception e2) { out("Please use a number as an argument"); }
								} else if (split[1].equals("del")) {
									try {
										number = Integer.parseInt(split[2]);
										if (!Startup.listeners.contains(number)) {
											out("[JN]: Listener " + number + " already existed");
										} else {
											Startup.listeners.remove((Object)number);
											out("[JN]: Deleted listener " + number);
											Startup.delListener(number);
										}
									} catch (Exception e2) { e2.printStackTrace(); out("[JN]: Please use a number as an argument"); }
								} else if (split[1].equals("list")) {
									if (Startup.listeners.size() == 0) {
										System.out.println("There are no listeners");
									} else {
										for (int i = 0; i < Startup.listeners.size(); i ++) {
											out(Startup.listeners.get(i) + "");
										}
									}
								} else {
									out("[JN]: Accepted arguments for listener (add|del|list) number");
								}
							} catch (Exception e2) { out("[JN]: Accepted arguments for listener (add|del|list) number"); }
						} else if(split[0].equals("killall")) {
							while(Startup.clients.size() > 0) {
								Startup.killAll = true;
							}
							Startup.killAll = false;
						} else if(split[0].equals("kill")) {
							split = command.split(" ");
							try {
								Startup.killID = split[1];
								while(Startup.killTarget == null) {
									Startup.killSwitch = true;
								}
								Startup.killSwitch = false;
							} catch (Exception noID) { out("[JN]: The correct format for the kill command is kill (ID)");}
						} else if(split[0].equals("devmode")) {
							try{
								Startup.devMode = true;
							} catch (Exception dMode) { out("[JN]: Devmode has no arguments"); }
						} else if (split[0].equals("rate")) {
							out(Startup.getHashRate() + "kH/s");
						} else if (split[0].equals("stop")) {
							Startup.stopCracking();
						} else if (split[0].equals("crack")) {
							try {
								if (!Startup.cracking) {
									//String arguments = command.replaceAll(split[1], "");
									Startup.currentHashString = split[1];
									Startup.array = Startup.comblist;
								}
								jb.resetStart();
								Startup.startCracking();
							} catch (Exception e2) {
								out("[JN]: crack (hash)");
							}
						}
						else if (split[0].equals("status")) {
							if (Startup.cracking) {
								out("[JN]: Solving " + " @" + Startup.getHashRate() + "kH/s for " + (Float.valueOf(System.currentTimeMillis() - Startup.initiateTime)/1000F) + "sec");
							} else {
								out("[JN]: Not solving a hash");
							}
						}
						else if (split[0].equals("help")) {
							out("[JN]:  ==============[J-Net Commands]===============");
							out("> listener (add|del|list) number - adds a port to listen");
							out("> rate - retrieves hash rate (kH/s)");
							out("> crack (hash) (start length)");
							out("> list - lists connected clients");
							out("> stop - stops cracking");
							out("> status - gives a status on the current crack");
						} else {
							out("[JN]: Unknown command. Try help");
						}
						consoleInput.setText("");
					}
				}
			}
		);
	}
	public static void out(String output) {
		GUI.console.swrite(output);
	}
	public static void init() {
		GUI gui = new GUI();
		gui.setLayout(null);
		gui.setTitle("J-Net Distributed MD5 Hash Cracker");
		gui.setSize(500,230);
		gui.setResizable(false);
		
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gui.setVisible(true);
	}
	
	public static void update() {
		int connected = 0;
		for (int i = 0; i < Startup.clients.size(); i ++) {
			Startup.c = (Client)Startup.clients.get(i);
			if (Startup.c.isConnected()) {
				connected += 1;
			}
		}
		hashStatus.setMessage(Startup.getStatus(), connected);
	}
}