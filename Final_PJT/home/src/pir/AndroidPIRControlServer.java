package pir;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
//안드로이드(클라이언트)의 요청을 받으면 차 안에서 장치와 통신하는
//시리얼통신객체 쪽으로 요청을 전달하는 서버
public class AndroidPIRControlServer {
	private ServerSocket server;	
	public void connect() {
		try {
			server = new ServerSocket(12345);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Thread th = new Thread(new Runnable(){
			@Override
			public void run() {
				while(true){
					try {
						Socket client = server.accept();
						String ip = client.getInetAddress().getHostAddress();
						System.out.println(ip+"사용자접속!!\n");
						//new ArduinoSerialReadUsingEvent(client).main();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		th.start();
	}
	public static void main(String[] args) {
		new AndroidPIRControlServer().connect();
	}

}
