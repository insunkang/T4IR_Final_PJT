package trash;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class P2SReciverThread extends Thread {
	BufferedReader in;
	String resMsg;
	Socket client;
	static List<PrintWriter> pl = new ArrayList<>();
	public P2SReciverThread(Socket client) {
		this.client = client;
		try {
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			PrintWriter pw = new PrintWriter(HomeControlServer.phoneList.get(pl.size()).getOutputStream(), true);
			pl.add(pw);
			System.out.println(pl);
		} catch (IOException e) {
		}
	}
	public void ioWork() {
		pl=null;
	}
	@Override
	public void run() {
		while (true) {
			try {
				resMsg = in.readLine();				
				if (resMsg == null || resMsg.equals("")) {
					continue;
				}
				System.out.println("클라이언트에서 보내온 메시지>>" + resMsg);
				for(int i=0; i<pl.size();i++) {
					pl.get(i).println(resMsg);
				}
			} catch (Exception e) {

			}
		}
	}
}
