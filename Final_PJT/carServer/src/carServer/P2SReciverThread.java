package carServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

public class P2SReciverThread extends Thread {
	BufferedReader in;
	Socket phone;
	List<PrintWriter> cpwList;
	List<PrintWriter> tpwList;
	String authId;

	public String getAuthId() {
		return authId;
	}

	public P2SReciverThread(Socket phone) {
		this.phone = phone;
		try {
			in = new BufferedReader(new InputStreamReader(phone.getInputStream()));
			authId = in.readLine();
		} catch (IOException e) {
			System.out.println("오류P0");
		}
		cpwList = new ArrayList<PrintWriter>();
		tpwList = new ArrayList<PrintWriter>();
	}

	public void ioWork() {
		tpwList.clear();
		cpwList.clear();
	}

	@Override
	public void run() {
		while (true) {
			try {
				
				String resMsg = in.readLine();
				if (resMsg == null) {
					continue;

				} else if (resMsg.equals("") || resMsg.equals("\n")) {
					continue;
				}
				System.out.println("안드로이드에서 보내온 메시지>>" + resMsg);
				pwSet();
				filteringMsg(resMsg);
				ioWork();
			} catch (Exception e) {
				try {
					//CarControlServer.phoneList.get(authId.split("/")[0]).remove(authId.split("/")[1]);
					in.close();
					phone.close();
					System.out.println("안드로이드접속종료");
					this.stop();
				} catch (IOException e1) {
				}
				e.printStackTrace();
			}
		}
	}

	public void pwSet() {
		if (CarControlServer.carList.get(authId.split("/")[0]) != null) {
			for (String member : CarControlServer.carList.get(authId.split("/")[0]).keySet()) {
				try {
					if (!CarControlServer.carList.get(authId.split("/")[0]).get(member).isClosed()) {
						cpwList.add(new PrintWriter(
								CarControlServer.carList.get(authId.split("/")[0]).get(member).getOutputStream(),
								true));
					}
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("오류T4");
				}
			}
		}
		if (CarControlServer.tabList.get(authId.split("/")[0]) != null) {
			for (String member : CarControlServer.tabList.get(authId.split("/")[0]).keySet()) {
				try {
					if (!CarControlServer.tabList.get(authId.split("/")[0]).get(member).isClosed()) {
						tpwList.add(new PrintWriter(
								CarControlServer.tabList.get(authId.split("/")[0]).get(member).getOutputStream(),
								true));
					}
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("오류T4");
				}
			}
		}
	}

	public void tabBroadCast(String resMsg) {
		int size = tpwList.size();
		for (int i = 0; i < size; i++) {
			tpwList.get(i).println(resMsg);
		}
	}

	public void carBroadCast(String resMsg) {
		int size = cpwList.size();
		for (int i = 0; i < size; i++) {
			cpwList.get(i).println(resMsg);
		}
	}

	private void filteringMsg(String msg) {
		try {
			StringTokenizer token = new StringTokenizer(msg, "/");
			String family = token.nextToken();
			String id = token.nextToken();
			String protocol = token.nextToken();
			String message = token.nextToken();
			if (protocol.equals("LED")) {
				carBroadCast(message);
			} else if(protocol.equals("move")) {
				carBroadCast(message);
			} else if(protocol.equals("control")) {
				if(token.hasMoreTokens()) {
					message = message + "/"+token.nextToken();
				}
				tabBroadCast(message);
			} else if(protocol.equals("start")) {
				tabBroadCast(protocol +"/" +message);
			}
		} catch (NoSuchElementException e) {
			System.out.println("오류C6");
		}
	}
}
