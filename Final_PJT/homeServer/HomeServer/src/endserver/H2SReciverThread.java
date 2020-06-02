package endserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

public class H2SReciverThread extends Thread {
	BufferedReader in;
	Socket home;
	List<PrintWriter> ppwList;
	String authId;

	public String getAuthId() {
		return authId;
	}
	
	public H2SReciverThread(Socket home) {
		this.home = home;
		try {
			in = new BufferedReader(new InputStreamReader(home.getInputStream()));
			authId = in.readLine();
		} catch (IOException e) {
			System.out.println("오류H0");
		}
		ppwList = new ArrayList<PrintWriter>();
	}

	public void ioWork() {
		ppwList.clear();
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
				System.out.println("집에서 보내온 메시지>>" + resMsg);
				pwSet();
				filteringMsg(resMsg);
				ioWork();
			} catch (IOException e) {
				e.printStackTrace();
				try {
					//CarControlServer.carList.get(authId.split("/")[0]).remove(authId.split("/")[1]);
					in.close();
					home.close();
					System.out.println("집 접속종료");
					this.stop();
				} catch (IOException e1) {
				}
			}
		}
	}

	public void pwSet() {
		if (HomeControlServer.phoneList.get(authId.split("/")[0]) != null) {
			for (String member : HomeControlServer.phoneList.get(authId.split("/")[0]).keySet()) {
				try {
					if (!HomeControlServer.phoneList.get(authId.split("/")[0]).get(member).isClosed()) {
						ppwList.add(new PrintWriter(
								HomeControlServer.phoneList.get(authId.split("/")[0]).get(member).getOutputStream(),
								true));
					}
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("오류H4");
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

	private void filteringMsg(String msg) {
		try {
			System.out.println(ppwList);
			StringTokenizer token = new StringTokenizer(msg, "/");
			String protocol = token.nextToken();
			String message = token.nextToken();
			String category = token.nextToken();
			String family = token.nextToken();
			String id = token.nextToken();
			if(protocol.equals("gas")) {
				 phoneBroadCast(message);
			} else if ( protocol.equals("flame")) {
				phoneBroadCast(message);
			} else if (protocol.equals("fan")) {
				phoneBroadCast(message);
			}
		} catch (NoSuchElementException e) {
			System.out.println("오류H6");
		}
	}

}
