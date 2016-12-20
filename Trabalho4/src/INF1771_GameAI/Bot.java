package INF1771_GameAI;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import INF1771_GameClient.Dto.*;
import INF1771_GameClient.Socket.*;

public class Bot implements Runnable {

	private String name;
	private String host = "139.82.2.67";

	HandleClient client = new HandleClient();
	Map<Long, PlayerInfo> playerList = new HashMap<Long, PlayerInfo>();
	List<ShotInfo> shotList = new ArrayList<ShotInfo>();
	List<ScoreBoard> scoreList = new ArrayList<ScoreBoard>();

	private GameAI gameAi ;

	long time = 0;

	String gameStatus = "";
	String sscoreList = "";

	List<String> msg = new ArrayList<String>();
	double msgSeconds = 0;
	int timer_interval = 100;

	public Bot(String name,GameAI gameAi) {
		// Set command listener to process commands received from server
		
		 this.name=name;
		 this.gameAi=gameAi;
		
		client.addCommandListener(new CommandListener() {
			

			@Override
			public void receiveCommand(String[] cmd) {
				
				if (cmd != null)
					if (cmd.length > 0)
						try {
							if (cmd[0].equals("o")) {
								if (cmd.length > 1) {
									if (cmd[1].trim().equals(""))
										gameAi.GetObservationsClean();

									else {
										List<String> o = new ArrayList<String>();
											
										if (cmd[1].indexOf(",") > -1) {
											String[] os = cmd[1].split(",");
											for (int i = 0; i < os.length; i++)
												o.add(os[i]);
										} else
											o.add(cmd[1]);

										gameAi.GetObservations(o);
									}
								}
							
								else
									gameAi.GetObservationsClean();

								ready = true;

							} else if (cmd[0].equals("s")) {
								if (cmd.length > 1) {
									gameAi.SetStatus(Integer.parseInt(cmd[1]), Integer.parseInt(cmd[2]), cmd[3], cmd[4],
											Long.parseLong(cmd[5]), Integer.parseInt(cmd[6]));
								}
							} else if (cmd[0].equals("player")) {

								if (cmd.length == 8)
									if (!playerList.containsKey(Long.parseLong(cmd[1])))
										playerList.put(Long.parseLong(cmd[1]),
												new PlayerInfo(Long.parseLong(cmd[1]), cmd[2], Integer.parseInt(cmd[3]),
														Integer.parseInt(cmd[4]),
														PlayerInfo.Direction.values()[Integer.parseInt(cmd[5])],
														PlayerInfo.State.values()[Integer.parseInt(cmd[6])],
														convertFromString(cmd[7])));
									else {
										playerList.put(Long.parseLong(cmd[1]),
												new PlayerInfo(Long.parseLong(cmd[1]), cmd[2], Integer.parseInt(cmd[3]),
														Integer.parseInt(cmd[4]),
														PlayerInfo.Direction.values()[Integer.parseInt(cmd[5])],
														PlayerInfo.State.values()[Integer.parseInt(cmd[6])],
														convertFromString(cmd[7])));

									}

							} else if (cmd[0].equals("g")) {

								if (cmd.length == 3) {
									if (!gameStatus.equals(cmd[1]))
										playerList.clear();

									if (!gameStatus.equals(cmd[1]))
										System.out.println("New Game Status: " + cmd[1]);

									gameStatus = cmd[1];
									time = Long.parseLong(cmd[2]);
								}
							} else if (cmd[0].equals("u")) {
								if (cmd.length > 1) {
									for (int i = 1; i < cmd.length; i++) {
										String[] a = cmd[i].split("#");

										if (a.length == 4)
											scoreList.add(new ScoreBoard(a[0], (a[1].equals("connected")),
													Integer.parseInt(a[2]), Integer.parseInt(a[3]),
													Color.black));
										else if (a.length == 5)
											scoreList.add(new ScoreBoard(a[0], (a[1].equals("connected")),
													Integer.parseInt(a[2]), Integer.parseInt(a[3]),
													convertFromString(a[4])
													));
									}
									sscoreList = "";
									for (ScoreBoard sb : scoreList) {
										sscoreList += sb.name + "\n";
										sscoreList += (sb.connected ? "connected" : "offline") + "\n";
										sscoreList += sb.energy + "\n";
										sscoreList += sb.score + "\n";
										sscoreList += "---\n";
									}
									scoreList.clear();
								}
							} else if (cmd[0].equals("notification")) {
								if (cmd.length > 1) {
									if (msg.size() == 0)
										msgSeconds = 0;
									msg.add(cmd[1]);
								}

							} else if (cmd[0].equals("hello")) {
								if (cmd.length > 1) {
									if (msg.size() == 0)
										msgSeconds = 0;

									msg.add(cmd[1] + " has entered the game!");
								}

							} else if (cmd[0].equals("goodbye")) {

								if (cmd.length > 1) {
									if (msg.size() == 0)
										msgSeconds = 0;

									msg.add(cmd[1] + " has left the game!");
								}

							} else if (cmd[0].equals("changename")) {
								if (cmd.length > 1) {
									if (msg.size() == 0)
										msgSeconds = 0;

									msg.add(cmd[1] + " is now known as " + cmd[2] + ".");
								}
							} else if (cmd[0].equals("h")) {
								if (cmd.length > 1) {
									List<String> o = new ArrayList<String>();
									o.add("hit");
									gameAi.GetObservations(o);
									msg.add("you hit " + cmd[1]);
								}
							} else if (cmd[0].equals("d")) {
								if (cmd.length > 1) {
									List<String> o = new ArrayList<String>();
									o.add("damage");
									gameAi.GetObservations(o);
									msg.add(cmd[1] + " hit you");
								}
							}
						

						} catch (Exception ex) {
							ex.printStackTrace();
						}

			}

		});

		// Set change status listener
		client.addChangeStatusListener(new CommandListener() {

			@Override
			public void receiveCommand(String[] cmd) {

				if (client.connected) {
					System.out.println("Connected");
					client.sendName(name);
					client.sendRequestGameStatus();
					client.sendRequestUserStatus();
					client.sendRequestObservation();

				} else
					System.out.println("Disconnected");
			}
		});

		client.connect(host);
		Thread ctThread = new Thread(this);
		ctThread.start();

	}

	/**
	 * Convert a string color received from server to color
	 * 
	 * @param c
	 *            string color
	 * @return color object
	 */
	private Color convertFromString(String c) {
		String[] p = c.split("(,)|(])");

		int A = Integer.parseInt(p[0].substring(p[0].indexOf('=') + 1));
		int R = Integer.parseInt(p[1].substring(p[1].indexOf('=') + 1));
		int G = Integer.parseInt(p[2].substring(p[2].indexOf('=') + 1));
		int B = Integer.parseInt(p[3].substring(p[3].indexOf('=') + 1));

		return new Color(R, G, B, A);
	}

	/**
	 * send a message to other users
	 * 
	 * @param msg
	 *            message string
	 */
	private void sendMsg(String msg) {
		if (msg.trim().length() > 0)
			client.sendSay(msg);
	}

	/**
	 * Get current game time as string
	 * 
	 * @return current time as string
	 */
	private String GetTime() {
		int hours = (int) time / 3600;
		int minutes = ((int) time % 3600) / 60;
		int seconds = (int) time % 60;

		return String.format("%02d:%02d:%02d", hours, minutes, seconds);
	}

	/**
	 * Execute some decision
	 */
	boolean ready =true;
	private void DoDecision() {
		
		if(ready){
			ready = false;
		String decision = gameAi.GetDecision();

		if (decision.equals("virar_direita"))
			client.sendTurnRight();
		else if (decision.equals("virar_esquerda"))
			client.sendTurnLeft();
		else if (decision.equals("andar"))
			client.sendForward();
		else if (decision.equals("atacar"))
			client.sendShoot();
		else if (decision.equals("pegar_ouro"))
			client.sendGetItem();
		else if (decision.equals("pegar_anel"))
			client.sendGetItem();
		else if (decision.equals("pegar_powerup"))
			client.sendGetItem();
		else if (decision.equals("andar_re"))
			client.sendBackward();
		else if (decision.equals("bad")){
			client.sendTurnRight();
			client.sendForward();
		}

		java.util.Random rand = new java.util.Random();
		int n=rand.nextInt(2);
		if(n==0)
			client.sendColor(new Color(255 ,215 ,0));
		else
			client.sendColor(new Color(255, 255, 255));
		
				
		//client.sendColor(Color.YELLOW);
		client.sendRequestUserStatus();
		client.sendRequestObservation();
		}

	}

	/**
	 * Client thread (runnable)
	 */
	@Override
	public void run() {

		while (true) {
			try {
				Thread.sleep(timer_interval);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			msgSeconds += timer_interval;

			client.sendRequestGameStatus();
			if (gameStatus.equals("Game"))
				DoDecision();
			else if (msgSeconds >= 5000) {

				System.out.println(gameStatus);
				System.out.println(GetTime());
				System.out.println("-----------------");
				System.out.println(sscoreList);

				client.sendRequestScoreboard();
			}

			if (msgSeconds >= 5000) {
				if (msg.size() > 0) {
					for (String s : msg)
						System.out.println(s);
					msg.clear();
				}
				msgSeconds = 0;
			}
		}
	}
}
