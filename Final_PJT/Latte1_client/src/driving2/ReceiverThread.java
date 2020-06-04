package driving2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ReceiverThread extends Thread {
	BufferedReader br;// 클라이언트의 메시지를 읽는 스트림
	OutputStream os;

	public ReceiverThread(OutputStream os, BufferedReader br) {
		this.br = br;
		this.os = os;
	}

	@Override
	public void run() {
		// 클라이언트의 메시지르 ㄹ받아서 아두이노로 데이터를 전송
		while (true) {
			String msg;
			String id = "00000000"; // 송신할 메시지의 구분id
			String data;
			String canmsg;
			try {
				msg = br.readLine();
				System.out.println(msg);
				if (msg != null) {
					System.out.println(msg);
					if (msg.equals("forward")) {
						os.write('f');
					} else if (msg.equals("left")) {
						os.write('x');
					} else if (msg.equals("right")) {
						os.write('y');
					} else if (msg.equals("back")) {
						os.write('b');
					} else if (msg.equals("up")) {
						os.write('u');
					} else if (msg.equals("down")) {
						os.write('d');
					} else if (msg.equals("leftspin")) {
						os.write('l');
					} else if (msg.equals("rightspin")) {
						os.write('r');
					} else if (msg.equals("stop")) {
						os.write('s');
					} else if (msg.equals("emergency")) {
						data = "00000000000000"+"11"; // 송신할 데이터
						canmsg = id + data;
						Client.canObj.send(canmsg);
					} else if (msg.equals("leftLight")) {
						data = "00000000000000"+"12"; // 송신할 데이터
						canmsg = id + data;
						Client.canObj.send(canmsg);
					} else if (msg.equals("rightLight")) {
						data = "00000000000000"+"13"; // 송신할 데이터
						canmsg = id + data;
						Client.canObj.send(canmsg);
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

}
