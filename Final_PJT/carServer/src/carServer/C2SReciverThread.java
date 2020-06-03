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

public class C2SReciverThread extends Thread {
	BufferedReader in;
	Socket car;
	List<PrintWriter> ppwList;
	List<PrintWriter> tpwList;
	String authId;

	public String getAuthId() {
		return authId;
	}
	
	public C2SReciverThread(Socket car) {
		this.car = car;
		try {
			in = new BufferedReader(new InputStreamReader(car.getInputStream()));
			authId = in.readLine();
		} catch (IOException e) {
			System.out.println("오류C0");
		}
		ppwList = new ArrayList<PrintWriter>();
		tpwList = new ArrayList<PrintWriter>();
	}

	public void ioWork() {
		ppwList.clear();
		tpwList.clear();
	}

	@Override
	public void run() {
		while (true) {
			try {
				String resMsg = "";
				resMsg = in.readLine();
				if (resMsg == null) {
					continue;

				} else if (resMsg.equals("") || resMsg.equals("\n")) {
					continue;
				}
				System.out.println("차에서 보내온 메시지>>" + resMsg);
				pwSet();
				filteringMsg(resMsg);
				ioWork();
			} catch (IOException e) {
				e.printStackTrace();
				try {
					//CarControlServer.carList.get(authId.split("/")[0]).remove(authId.split("/")[1]);
					in.close();
					car.close();
					System.out.println("차량 접속종료");
					this.stop();
				} catch (IOException e1) {
				}
			}
		}
	}

	public void pwSet() {
		if (CarControlServer.phoneList.get(authId.split("/")[0]) != null) {
			for (String member : CarControlServer.phoneList.get(authId.split("/")[0]).keySet()) {
				try {
					if (!CarControlServer.phoneList.get(authId.split("/")[0]).get(member).isClosed()) {
						ppwList.add(new PrintWriter(
								CarControlServer.phoneList.get(authId.split("/")[0]).get(member).getOutputStream(),
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

	public void phoneBroadCast(String resMsg) {
		int size = ppwList.size();
		for (int i = 0; i < size; i++) {
			ppwList.get(i).println(resMsg);
		}
	}

	public void tabBroadCast(String resMsg) {
		int size = tpwList.size();
		for (int i = 0; i < size; i++) {
			tpwList.get(i).println(resMsg);
		}
	}

	private void filteringMsg(String msg) {
		try {
			StringTokenizer token = new StringTokenizer(msg, "/");
			String family = token.nextToken();
			String id = token.nextToken();
			String protocol = token.nextToken();
			String message = token.nextToken();
			if (protocol.equals("temperature")) {
				tabBroadCast(protocol + "/" + message);
			} else if (protocol.equals("humidity")) {
				tabBroadCast(protocol + "/" + message);
			}
		} catch (NoSuchElementException e) {
			System.out.println("오류C6");
		}
	}

}
