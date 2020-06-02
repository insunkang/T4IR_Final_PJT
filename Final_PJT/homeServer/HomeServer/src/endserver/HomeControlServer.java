package endserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import javax.net.SocketFactory;

public class HomeControlServer {

	private ServerSocket andserver;
	private ServerSocket homeserver;
	Socket home;
	Socket phone;
	static HashMap<String,HashMap<String,Socket>> phoneList = new HashMap<>();
	static HashMap<String,HashMap<String,Socket>> homeList = new HashMap<>();
	
	P2SReciverThread rt1 = null;
	H2SReciverThread rt3 = null;

	public void connect() {
		try {
			andserver = new ServerSocket(23335);
			homeserver = new ServerSocket(23336);
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

		
		Thread th3 = new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("집접속대기");
				while (true) {
					try {
						home = homeserver.accept();
						String ip = home.getInetAddress().getHostAddress();
						System.out.println(ip + "집접속!!");
						rt3 = new H2SReciverThread(home);
						String[] homeId = rt3.getAuthId().split("/");
						System.out.println(rt3.getAuthId());
						if(homeList.get(homeId[0])==null) {
							homeList.put(homeId[0],new HashMap<String,Socket>());
							homeList.get(homeId[0]).put(homeId[1],home);
							System.out.println(homeList);
							System.out.println(homeList.get(homeId[0]));
							System.out.println("객체생성");
						} else {
							homeList.get(homeId[0]).put(homeId[1], home);
							System.out.println(homeList);
							System.out.println(homeList.get(homeId[0]));
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
		new HomeControlServer().connect();

	}

}
