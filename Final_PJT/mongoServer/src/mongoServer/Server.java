package mongoServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	
	ServerSocket server;
	
	InputStream is;
	InputStreamReader ir;
	BufferedReader br;
	
	OutputStream os;
	PrintWriter pw;
	
	String position;
	
	public void connect() {
		try {
			server=new ServerSocket(12345);
			
			System.out.println("사용자 접속 대기중");
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		Thread th=new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true) {
				try {
					Socket client=server.accept();
					String ip=client.getInetAddress().getHostAddress();
					System.out.println(ip+"접속!\n");
					if(client!=null) {
						speeWork(client);
					}
				} catch (IOException e) {
					
					e.printStackTrace();
				}
				}
			}
		});
		th.start();
	}

	public void speeWork(Socket client) {
		try {
			is=client.getInputStream();
			ir=new InputStreamReader(is);
			br=new BufferedReader(ir);
			
			os=client.getOutputStream();
			pw=new PrintWriter(os, true);
			
			position=br.readLine();
			System.out.println("받은 데이터:::::"+position);
			String[] data=position.split("/");
			if(data[0]!=null) {
				MongoTest mongoTest=new MongoTest(data[3], data[5]);
				WordCloud wordcloud=new WordCloud();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public static void main(String[] args) {
		new Server().connect();
	}

}
