package newhome;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class P2SReciverThread extends Thread {
	BufferedReader in;
	String resMsg;
	Socket client;
	static PrintWriter pw;

	public P2SReciverThread(Socket client) {
		this.client = client;
		try {
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		} catch (IOException e) {
		}
		pw=null;
	}

	public void ioWork() {
		pw=null;
	}

	@Override
	public void run() {
		while (true) {
			try {
				if (CarControlServer.home != null && pw == null) {
					pw = new PrintWriter(CarControlServer.home.getOutputStream(), true);
				}
				resMsg = in.readLine();

				if (resMsg == null || resMsg.equals("") || resMsg.equals("\n")) {
					continue;
				}
				System.out.println("안드로이드에서 보내온 메시지>>" + resMsg);
				pw.println(resMsg);
			} catch (Exception e) {

			}
		}
	}
}
