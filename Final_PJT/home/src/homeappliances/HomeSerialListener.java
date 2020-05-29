package homeappliances;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;

import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

//�̺�Ʈ�� ���� ó���ϴ� ������ Ŭ����
//�ø�����Ʈ�� �����Ͱ� ���۵ɶ����� �̺�Ʈ�� �߻��ϰ� �̺�Ʈ�� �߻��ɶ� �����͸� �б�
public class HomeSerialListener implements SerialPortEventListener{
	private InputStream in;
	private PrintWriter pw;
	String homeId;
	StringTokenizer st;
	public HomeSerialListener(InputStream in, PrintWriter pw, String homeId) {
		super();
		this.in = in;
		this.pw = pw;
		this.homeId = homeId;
	}
	//�̺�Ʈ�� �߻��ɶ����� ȣ��Ǵ� �޼ҵ�
	//�߻��� �̺�Ʈ�̤� ������ ��� �ִ� ��ü - SerialPortEvent
	@Override
	public void serialEvent(SerialPortEvent event) {
		//���۵� �����Ͱ� �ִ� ��� �����͸� �о �ֿܼ� ���
		if(event.getEventType()==SerialPortEvent.DATA_AVAILABLE) {
			try {
				//���۵Ǵ� �������� ũ�⸦ ����
				int check_size = in.available();
				byte[] data = new byte[check_size];
				in.read(data,0,check_size);
				String msg = new String(data);
				msg=msg.trim();
				System.out.println("���� ������:"+msg);
				if(msg!=""&msg!="\n") {
					st = new StringTokenizer(msg, "/");
					String classify = st.nextToken();
					if(classify.equals("pan")) {
						pw.println(msg+"/home/"+homeId);
						pw.flush();
					}else if(classify.equals("pirLed")) {
						pw.println(msg+"/home/"+homeId);
						pw.flush();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}//
		}
	}
	
}
