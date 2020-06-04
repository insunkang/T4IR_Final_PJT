package driving2;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

//�̺�Ʈ�� ���� ó���ϴ� ������ Ŭ����
//�ø�����Ʈ�� �����Ͱ� ���۵ɶ����� �̺�Ʈ�� �߻��ϰ� �̺�Ʈ�� �߻��ɶ� �����͸� �б�
public class SerialListener implements SerialPortEventListener{
	private InputStream in;
	private PrintWriter pw;
	public SerialListener(InputStream in, PrintWriter pw) {
		super();
		this.in = in;
		this.pw = pw;
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
				System.out.println("���� ������:"+new String(data));
				String msg = new String(data);
				if(msg!=""&msg!="\n") {
					pw.println(msg);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}//
		}
	}
	
}
