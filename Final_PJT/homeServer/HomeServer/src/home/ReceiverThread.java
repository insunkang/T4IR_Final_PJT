package home;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
public class ReceiverThread extends Thread {
	Socket client;
	BufferedReader br;					
	PrintWriter pw;						
	
	public ReceiverThread() {
		// TODO Auto-generated constructor stub
	}
	public ReceiverThread(Socket client) {
		this.client = client;
		try {
			br = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
			pw = new PrintWriter(this.client.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				String msg = br.readLine();
				System.out.println("클라이언트가 전송한 메시지: " + msg);
				//클라이언트가 전송하는 데이터를 읽어서 시리얼통신할 수 있는 객체에 전달
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
