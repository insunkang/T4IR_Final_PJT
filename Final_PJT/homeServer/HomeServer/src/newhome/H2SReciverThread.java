package newhome;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

public class H2SReciverThread extends Thread {
	BufferedReader in;
	String resMsg;
	Socket car;
	static PrintWriter pw; // 폰

	public H2SReciverThread(Socket car) {
		this.car = car;
		try {
			in = new BufferedReader(new InputStreamReader(car.getInputStream()));
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
				if(CarControlServer.phone!=null&&pw==null) {
					pw = new PrintWriter(CarControlServer.phone.getOutputStream(),true);
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
			
			if (protocol.equals("pan")) {
				System.out.println(pw);
				String temperature = token.nextToken() +"/" +token.nextToken();
				String humidity = token.nextToken() +"/" +token.nextToken();
				String home = token.nextToken();
				String homeid = token.nextToken();
				pw.println(temperature);
				pw.println(humidity);
			}
		} catch (NoSuchElementException e) {

		}
	}
}
