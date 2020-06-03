package carServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import javax.net.SocketFactory;

public class CarControlServer {

	private ServerSocket andserver;
	private ServerSocket carserver;
	private ServerSocket tabserver;
	Socket tab;
	Socket car;
	Socket phone;
	static HashMap<String,HashMap<String,Socket>> phoneList = new HashMap<>();
	static HashMap<String,HashMap<String,Socket>> tabList = new HashMap<>();
	static HashMap<String,HashMap<String,Socket>> carList = new HashMap<>();
	
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
				System.out.println("안드로이드접속대기");
				while (true) {
					try {
						phone = andserver.accept();
						String ip = phone.getInetAddress().getHostAddress();
						System.out.println(ip + "안드로이드접속!!");
						rt1 = new P2SReciverThread(phone);
						String[] homeId = rt1.getAuthId().split("/"); // 가족id와 개인id를 배열로 받게함
						System.out.println(rt1.getAuthId());
						if(phoneList.get(homeId[0])==null) {
							phoneList.put(homeId[0],new HashMap<String,Socket>());
							phoneList.get(homeId[0]).put(homeId[1],phone);
			 	 	 		System.out.println(phoneList);
			 	 	 		System.out.println(phoneList.get(homeId[0]));
			 	 	 		System.out.println("객체생성");
						} else {
							if(phoneList.get(homeId[0]).get(homeId[1])!=null) {
								phoneList.get(homeId[0]).get(homeId[1]).close();
							}
							phoneList.get(homeId[0]).put(homeId[1], phone);
							System.out.println(phoneList);
			 	 	 		System.out.println(phoneList.get(homeId[0]));
			 	 	 		System.out.println("객체생성하지않고 갱신");
						}
						rt1.start();
						System.out.println(Thread.activeCount());
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
				System.out.println("태블릿접속대기");
				while (true) {
					try {
						tab = tabserver.accept();
						String ip = tab.getInetAddress().getHostAddress();
						System.out.println(ip + "태블릿접속!!");
						rt2 = new T2SReciverThread(tab);
						String[] homeId = rt2.getAuthId().split("/");
						System.out.println(rt2.getAuthId());
						if(tabList.get(homeId[0])==null) {
							tabList.put(homeId[0],new HashMap<String,Socket>());
							tabList.get(homeId[0]).put(homeId[1],tab);
			 	 	 		System.out.println(tabList);
			 	 	 		System.out.println(tabList.get(homeId[0]));
			 	 	 		System.out.println("객체생성");
						} else {
							if(tabList.get(homeId[0]).get(homeId[1])!=null) {
								tabList.get(homeId[0]).get(homeId[1]).close();
							}
							tabList.get(homeId[0]).put(homeId[1], tab);
							System.out.println(tabList);
			 	 	 		System.out.println(tabList.get(homeId[0]));
			 	 	 		System.out.println("객체생성하지않고 갱신");
						}
						rt2.start();
						System.out.println(Thread.activeCount());
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
				System.out.println("차량접속대기");
				while (true) {
					try {
						car = carserver.accept();
						String ip = car.getInetAddress().getHostAddress();
						System.out.println(ip + "차량접속!!");
						rt3 = new C2SReciverThread(car);
						String[] homeId = rt3.getAuthId().split("/");
						System.out.println(rt3.getAuthId());
						if(carList.get(homeId[0])==null) {
							carList.put(homeId[0],new HashMap<String,Socket>());
							carList.get(homeId[0]).put(homeId[1],car);
							System.out.println(carList);
							System.out.println(carList.get(homeId[0]));
							System.out.println("객체생성");
						} else {
							carList.get(homeId[0]).put(homeId[1], car);
							System.out.println(carList);
							System.out.println(carList.get(homeId[0]));
							System.out.println("객체생성하지않고 갱신");
						}
						rt3.start();
						System.out.println(Thread.activeCount());
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
