package homeappliances;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;

public class ReceiverThread extends Thread {
	BufferedReader br;
	OutputStream os;
	
	public ReceiverThread(OutputStream os, BufferedReader br) {
		this.br = br;
		this.os = os;
	}

	public void run() {
		while (true) {
			try {
				String msg = br.readLine();
				System.out.println("클라이언트가 보낸 메시지:" + msg);
				if (msg.equals("L_auto")) {
					os.write('0');
				} else if (msg.equals("L_manual")) {
					os.write('1');
					char L_temp = br.readLine().charAt(0);
					System.out.println("L_manul값: " + L_temp);
					os.write(L_temp);
				} else if (msg.equals("K_auto")) {
					os.write('2');
				} else if (msg.equals("K_manual")) {
					os.write('3');
					char K_temp = br.readLine().charAt(0);
					System.out.println("K_manul값: " + K_temp);
					os.write(K_temp);

				} else if (msg.equals("FAN_auto")) {
					os.write('4');
					String A_auto = (br.readLine());
					System.out.println("설정 온도 값: " + A_auto);
					os.write(A_auto.getBytes());
				} else if (msg.equals("FAN_on")) {
					os.write('5');
				} else if (msg.equals("FAN_off")) {
					os.write('6');
				}
			} catch (IOException e) {
				System.out.println("서버가 접속을 끊음");
				try {
					br.close();
					os.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
			}

		}
	}
}
