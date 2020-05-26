package home;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class HomeServerView {
	ServerSocket server;
	Socket socket;
	Vector<User> userlist = new Vector<User>();
	
	public static void main(String[] args) {
		
	}
	
	public void serverStart(int port) {
		
	}
	
	public void connection() {
		try {
			server = new ServerSocket(12345);
			if(server != null) {
				connection();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					try {
						System.out.println("立加 措扁");
						socket = server.accept();
						String ip = socket.getInetAddress().getHostAddress();
						System.out.println(ip+"立加\n");
						User user = new User(socket, userlist);
						user.start();
						
					} catch (IOException e) {
						e.printStackTrace();
					}
				}//end while
			}
		});
		thread.start();
	}

}
