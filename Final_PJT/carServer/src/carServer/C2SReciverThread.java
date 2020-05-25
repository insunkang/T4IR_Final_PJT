package carServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

public class C2SReciverThread extends Thread {
	BufferedReader in;
	String resMsg;
	Socket car;
	PrintWriter pw1; // 폰
	PrintWriter pw2; // 탭

	public C2SReciverThread(Socket car) {
		this.car = car;
		try {
			in = new BufferedReader(new InputStreamReader(car.getInputStream()));
		} catch (IOException e) {
		}
	}
	public void ioWork() {
		pw2=null;
	}
	@Override
	public void run() {
		while (true) {
			try {
				/*
				if(CarControlServer.phone!=null&&pw1==null) {
					pw1 = new PrintWriter(CarControlServer.phone.getOutputStream(),true);
				}
				*/
				if(CarControlServer.tab!=null&&pw2==null) {
					pw2 = new PrintWriter(CarControlServer.tab.getOutputStream(),true);
				}
				resMsg = in.readLine();
				if (resMsg == null || resMsg.equals("") || resMsg.equals("\n")) {
					continue;
				}
				System.out.println("클라이언트에서 보내온 메시지>>" + resMsg);
				filteringMsg(resMsg);

			} catch (Exception e) {

			}
		}
	}

	private void filteringMsg(String msg) {
		try {
			StringTokenizer token = new StringTokenizer(msg, "/");
			String protocol = token.nextToken();
			String message = token.nextToken();
			if (protocol.equals("temporature")) {
				pw2.println(msg);
			} else if (protocol.equals("humidity")) {
				pw2.println(msg);
			}
		} catch (NoSuchElementException e) {

		}
	}
}
