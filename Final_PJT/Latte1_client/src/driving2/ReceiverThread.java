package driving2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ReceiverThread extends Thread {
	BufferedReader br;// Ŭ���̾�Ʈ�� �޽����� �д� ��Ʈ��
	OutputStream os;

	public ReceiverThread(OutputStream os, BufferedReader br) {
		this.br = br;
		this.os = os;
	}

	@Override
	public void run() {
		// Ŭ���̾�Ʈ�� �޽����� ���޾Ƽ� �Ƶ��̳�� �����͸� ����
		while (true) {
			String msg;
			String id = "00000000"; // �۽��� �޽����� ����id
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
						data = "00000000000000"+"11"; // �۽��� ������
						canmsg = id + data;
						Client.canObj.send(canmsg);
					} else if (msg.equals("leftLight")) {
						data = "00000000000000"+"12"; // �۽��� ������
						canmsg = id + data;
						Client.canObj.send(canmsg);
					} else if (msg.equals("rightLight")) {
						data = "00000000000000"+"13"; // �۽��� ������
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
