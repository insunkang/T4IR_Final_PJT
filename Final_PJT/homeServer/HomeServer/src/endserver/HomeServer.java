package endserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Vector;


public class HomeServer {
	private ServerSocket server;
	HashMap<String, HashMap<String, User>> userlist = new HashMap<>();
	HashMap<String, HashMap<String, User>> homelist = new HashMap<>();
	//HashMap<String,User> userlist = new HashMap<String,User>();	 
	//HashMap<String,User> homelist = new HashMap<String,User>();
	//HashMap<String, User> checkhid;
	//HashMap<String, User> checkpid;
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
						User user = new User(client, userlist, homelist);
						user.getFamily();
						if(user.getcategory().equals("phone")) {
							if(userlist.get(user.getFamily()) == null) {
								userlist.put(user.getFamily(), new HashMap<String, User>());
								userlist.get(user.getFamily()).put(user.getfamid(), user);
							}else {
								userlist.get(user.getFamily()).put(user.getfamid(), user);
							}
						}else {
							if(homelist.get(user.getFamily()) == null) {
								homelist.put(user.getFamily(), new HashMap<String, User>());
								homelist.get(user.getFamily()).put(user.getfamid(), user);
							}else {
								homelist.get(user.getFamily()).put(user.getfamid(), user);
							}
						}
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
