package backup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.Vector;

public class User extends Thread {
	// ChatServerView에서 넘겨받을 데이터
	Socket client;
	InputStream is;
	InputStreamReader ir;
	BufferedReader br;

	OutputStream os;
	PrintWriter pw;

	String nickname;
	StringTokenizer st;
	HashMap<String, User> userlist;
	HashMap<String, User> homelist;
	// 임의로 프로그램 실행할때 사용하기 위해서 선언한 객체
	HashMap<String, User> checklist;
	String category;

	public User() {

	}

	public User(Socket client, HashMap<String, User> userlist, HashMap<String, User> homelist) {
		super();
		this.client = client;
		this.userlist = userlist;
		this.homelist = homelist;
		ioWork();
	}

	public void ioWork() { // 처음 접속했을 때 한 번 실행되는 메소드

		try {

			is = client.getInputStream();
			ir = new InputStreamReader(is);
			br = new BufferedReader(ir);

			os = client.getOutputStream();
			pw = new PrintWriter(os, true);

			nickname = br.readLine();
			System.out.println("id:" + nickname);
			String[] data = nickname.split("/");
			category = data[0];
			if (category.equals("home")) {
				checklist = homelist;
			} else {
				checklist = userlist;
			}
			if (data.length > 1) {
				if (!check(data[1], checklist)) {
					checklist.put(data[1], this);
				}
			}
			System.out.println("폰!!!!!!!!!!!!!!!!!!!!!!!!!:" + userlist.size());
			System.out.println("홈**************************************:" + homelist.size());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 중복확인 - 기존에 저장된 아이디가 중복저장되지 않도록 하기 위해서 체크
	public boolean check(String id, HashMap<String, User> list) {
		boolean result = false;
		if (list.get(id) != null) {
			result = true;
		}
		System.out.println(result);
		return result;
	}

	private void filteringMsg(String msg) {
		
			System.out.println("서버가 받은 클라이언트의 메시지:" + msg);
			st = new StringTokenizer(msg, "/");
			String protocol = st.nextToken();
			
			//조명 제어(and > 서버)
			if (protocol.equals("led")) {
				// 여기에서 클라이언트에게 메시지를 전달합니다.
				String message = st.nextToken();
				String category = st.nextToken();
				String id = st.nextToken();
				System.out.println(message + ":" + category + ":" + id);
				// 서버에서 클라이언트의 메시지를 분석해서 메시지를 전달할 클라이언트를 정의
				User userclient = null;
				if (category.equals("phone")) {
					userclient = homelist.get(id);
				}
				if (category.equals("home")) {
					userclient = userlist.get(id);
				}
				if (userclient != null) {
					userclient.sendMsg(message);
				}
			}
			
			//에어컨 제어(and > 서버)
			if (protocol.equals("air")) {
				// 여기에서 클라이언트에게 메시지를 전달합니다.
				String message = st.nextToken();
				String category = st.nextToken();
				String id = st.nextToken();
				System.out.println(message + ":" + category + ":" + id);
				// 서버에서 클라이언트의 메시지를 분석해서 메시지를 전달할 클라이언트를 정의
				User userclient = null;
				if (category.equals("phone")) {
					userclient = homelist.get(id);
				}
				if (category.equals("home")) {
					userclient = userlist.get(id);
				}
				if (userclient != null) {
					userclient.sendMsg(message);
				}
			}
			
			//온습도 제어(라떼 > 서버)
			if (protocol.equals("pan")) {
				// 여기에서 클라이언트에게 메시지를 전달합니다.
				String message = st.nextToken();
				try {
					String category = st.nextToken();
					String id = st.nextToken();
					System.out.println(message + ":" + category + ":" + id);
					// 서버에서 클라이언트의 메시지를 분석해서 메시지를 전달할 클라이언트를 정의
					User userclient = null;
					if (category.equals("phone")) {
						userclient = homelist.get(id);
					}
					if (category.equals("home")) {
						userclient = userlist.get(id);
					}
					if (userclient != null) {
						userclient.sendMsg(message);
					}
				} catch(NoSuchElementException e) {
					System.out.println("에러~~~~~~~~~~~~~");
				}
			}
			
			//경보 제어(라떼 > 서버)
			if (protocol.equals("pirLed")) {
				// 여기에서 클라이언트에게 메시지를 전달합니다.
				String message = st.nextToken();
				String category = st.nextToken();
				String id = st.nextToken();
				System.out.println(message + ":" + category + ":" + id);
				// 서버에서 클라이언트의 메시지를 분석해서 메시지를 전달할 클라이언트를 정의
				User userclient = null;
				if (category.equals("phone")) {
					userclient = homelist.get(id);
				}
				if (category.equals("home")) {
					userclient = userlist.get(id);
				}
				if (userclient != null) {
					userclient.sendMsg(message);
				}
			}
		
	}

	public void sendMsg(String message) {
		pw.println(message);
		pw.flush();
	}

	public void run() {
		while (true) {
			try {
				String msg = br.readLine();
				if(msg != null) {
					System.out.println("nickname:"+nickname+"------------------------------------->"+msg);
					//if (msg != null) {
						filteringMsg(msg);
					//}
				}
			} catch (IOException e) {
				System.out.println("클라이언트가 접속을 끊음");
				try {
					// 자원반납
					is.close();
					ir.close();
					br.close();
					os.close();
					pw.close();
					client.close();
					//
				} catch (IOException e1) {
				}
				break;
			}
		}
	}

}
