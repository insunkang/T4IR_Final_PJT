package carServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class CarControlServer {

	private ServerSocket andserver;
	private ServerSocket carserver;
	private ServerSocket tabserver;
	static Socket tab;
	static Socket car;
	static Socket phone;
	P2SReciverThread rt1 = null;
	T2SReciverThread rt2 = null;
	C2SReciverThread rt3 = null;

	public void connect() {
		try {
			andserver = new ServerSocket(33334);
			carserver = new ServerSocket(33335);
			tabserver = new ServerSocket(33336);
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
						rt1 = new P2SReciverThread(phone);
						rt1.start();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		th1.start();

		Thread th2 = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						System.out.println("태블릿접속대기");
						tab = tabserver.accept();
						String ip = tab.getInetAddress().getHostAddress();
						System.out.println(ip + "태블릿접속!!");
						if (rt2 != null) {
							rt2.stop();
							if (rt2 != null) {
								rt2.ioWork();
							}
							if (rt3 != null) {
								rt3.ioWork();
							}
							rt2 = null;
							System.out.println("rt2중지");
						}
						rt2 = new T2SReciverThread(tab);
						rt2.start();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		th2.start();
		Thread th3 = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						System.out.println("차량접속대기");
						car = carserver.accept();
						String ip = car.getInetAddress().getHostAddress();
						System.out.println(ip + "차량접속!!");
						if (rt3 != null) {
							rt3.stop();
							rt3.ioWork();
							rt3 = null;
							System.out.println("rt3중지");
						}
						rt3 = new C2SReciverThread(car);
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
