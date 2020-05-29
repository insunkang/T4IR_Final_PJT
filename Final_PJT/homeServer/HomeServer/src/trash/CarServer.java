package trash;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Vector;

import home.User;


public class CarServer {
	private ServerSocket server;
	HashMap<String,User> userlist = new HashMap<String,User>();	 
	HashMap<String,User> carlist = new HashMap<String,User>();	 
	public void connect() {
		try {
			server = new ServerSocket(12345);
			
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Thread th = new Thread(new Runnable(){
			@Override
			public void run() {
				while(true){
					try {
						Socket client = server.accept();
						String ip = client.getInetAddress().getHostAddress();
						System.out.println(ip+"�젒�냽!!\n");
						User user = new User(client, userlist,carlist);
						user.start();
						
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		th.start();
	}
	public static void main(String[] args) {
		new CarServer().connect();
	}
}
