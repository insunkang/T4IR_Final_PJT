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

public class T2SReciverThread extends Thread {
	BufferedReader in;
	Socket tablet;
	List<PrintWriter> ppwList;
	List<PrintWriter> cpwList;
	String authId;

	public String getAuthId() {
		return authId;
	}
	
	public T2SReciverThread(Socket tablet) {
		this.tablet = tablet;
		try {
			in = new BufferedReader(new InputStreamReader(tablet.getInputStream()));
			authId = in.readLine();
		} catch (IOException e) {
			System.out.println("오류T0");
		}
		ppwList = new ArrayList<PrintWriter>();
		cpwList = new ArrayList<PrintWriter>();
	}

	public void ioWork() {
		ppwList.clear();
		cpwList.clear();
	}

	@Override
	public void run() {
		while (true) {
			try {
				if(tablet.isClosed()) {
					System.out.println("태블릿 소켓종료");
					break;
				}
				String resMsg = "";
				resMsg = in.readLine();
				if (resMsg == null) {
					continue;

				} else if (resMsg.equals("") || resMsg.equals("\n")) {
					continue;
				}
				System.out.println("태블릿에서 보내온 메시지>>" + resMsg);
				pwSet();
				filteringMsg(resMsg);
				ioWork();
			} catch (IOException e) {
				e.printStackTrace();
				try {
					//CarControlServer.tabList.get(authId.split("/")[0]).remove(authId.split("/")[1]);
					in.close();
					tablet.close();
					System.out.println("태블릿접속종료");
					this.stop();
				} catch (IOException e1) {
				}
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
	}

	public void phoneBroadCast(String resMsg) {
		int size = ppwList.size();
		for (int i = 0; i < size; i++) {
			ppwList.get(i).println(resMsg);
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
			} else if (protocol.equals("Oil")) {
				phoneBroadCast(protocol+"/"+message);
			}
		} catch (NoSuchElementException e) {
			System.out.println("오류C6");
		}
	}
}
