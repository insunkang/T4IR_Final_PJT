package backup;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Vector;


public class HomeServer {
	private ServerSocket server;
	HashMap<String,User> userlist = new HashMap<String,User>();	 
	HashMap<String,User> homelist = new HashMap<String,User>();	 
	public void connect() {
		try {
			server = new ServerSocket(23335);
			
			
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
						System.out.println(ip+"접속!!\\n");
						User user = new User(client, userlist,homelist);
					
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
		new HomeServer().connect();
	}
}