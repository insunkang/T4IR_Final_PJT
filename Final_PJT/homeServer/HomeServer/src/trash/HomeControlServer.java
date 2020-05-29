package trash;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class HomeControlServer {

	private ServerSocket andserver;
	static List<Socket> phoneList;
	P2SReciverThread rt;
	public void connect() {
		phoneList = new ArrayList<Socket>();
		try {
			andserver = new ServerSocket(54321);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Thread th = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						System.out.println("안드로이드접속대기");
						Socket phone = andserver.accept();
						String ip = phone.getInetAddress().getHostAddress();
						System.out.println(ip + "사용자접속!!\n");
						phoneList.add(phone);
						if (rt != null) {
							rt.stop();
							rt.ioWork();
							rt = null;
							System.out.println("rt1중지");
						}
						P2SReciverThread rt1 = new P2SReciverThread(phone);
						rt1.start();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		th.start();
	}

	public static void main(String[] args) {
		new HomeControlServer().connect();

	}

}
