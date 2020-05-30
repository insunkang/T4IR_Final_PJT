package pir;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
//�ȵ���̵�(Ŭ���̾�Ʈ)�� ��û�� ������ �� �ȿ��� ��ġ�� ����ϴ�
//�ø�����Ű�ü ������ ��û�� �����ϴ� ����
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
						System.out.println(ip+"���������!!\n");
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
