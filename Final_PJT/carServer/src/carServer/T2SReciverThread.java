package carServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class T2SReciverThread extends Thread{
	BufferedReader in;
	String resMsg;
	Socket client;
	PrintWriter pw;
	public T2SReciverThread(Socket client) {
		this.client = client;
		try {
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		} catch (IOException e) {
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			try {
				if(CarControlServer.tab!=null&&pw==null) {
					pw = new PrintWriter(CarControlServer.car.getOutputStream(),true);
				}
				resMsg = in.readLine();
				if(resMsg==null ||resMsg.equals("") || resMsg.equals("\n")) {
					continue;
				}
				System.out.println("클라이언트에서 보내온 메시지>>" + resMsg);
				pw.println(resMsg);
			} catch (Exception e) {

			}
		}
	}
}
