package newhome;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class CarControlServer {

	private ServerSocket andserver;
	private ServerSocket homeserver;
	static Socket home;
	static Socket phone;
	P2SReciverThread rt1 = null;
	H2SReciverThread rt3 = null;

	public void connect() {
		try {
			andserver = new ServerSocket(23334);
			homeserver = new ServerSocket(23335);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Thread th1 = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						System.out.println("안드로이드접속대기");
						phone = andserver.accept();
						String ip = phone.getInetAddress().getHostAddress();
						System.out.println(ip + "안드로이드접속!!");
						if (rt1 != null) {
							rt1.stop();
							rt1.ioWork();
							rt1 = null;
							System.out.println("rt1중지");
						}
						if(rt3!=null) {
							rt3.ioWork();
						}
						rt1 = new P2SReciverThread(phone);
						rt1.start();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		th1.start();

		
		Thread th3 = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						System.out.println("홈접속대기");
						home = homeserver.accept();
						String ip = home.getInetAddress().getHostAddress();
						System.out.println(ip + "홈접속!!");
						if (rt3 != null) {
							rt3.stop();
							rt3.ioWork();
							rt3 = null;
							System.out.println("rt3중지");
						}
						if(rt1!=null) {
							rt1.ioWork();
						}
						rt3 = new H2SReciverThread(home);
						rt3.start();

					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		th3.start();

	}

	public static void main(String[] args) {
		new CarControlServer().connect();

	}

}
