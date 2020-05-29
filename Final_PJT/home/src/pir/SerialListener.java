package pir;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

//�̺�Ʈ�� ���ؼ� ó���ϴ� ������Ŭ����
//�ø�����Ʈ�� �����Ͱ� ���۵ɶ����� �̺�Ʈ�� �߻��ϰ� �̺�Ʈ�� �߻��ɶ� �����͸� �б�
public class SerialListener  implements SerialPortEventListener{
	private InputStream in;
	private OutputStream out;
	private PrintWriter pw;
	String homeId;
	public SerialListener(InputStream in, OutputStream out, String homeId) {
		super();
		this.in = in;
		this.out = out;
		this.homeId = homeId;
	}
	//�̺�Ʈ�� �߻��Ҷ����� ȣ��Ǵ� �޼ҵ�
	//�߻��� �̺�Ʈ�� ������ ��� �ִ� ��ü - SerialPortEvent
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
				System.out.println("���� ������:"+msg);
				pw = new PrintWriter(out);
				if(msg.trim().equals("pirLed/on")) {
					pw.println(msg.trim()+","+"on/"+"home/"+homeId);
				}else {
					pw.println(msg.trim()+","+"off/"+"home/"+homeId);
				}
				/*if(msg!=""&msg!="\n") {
					pw.println("job/"+msg+"/"+"home/"+homeId);
				}*/
			} catch (IOException e) {
				e.printStackTrace();
			}//
		}
	}
}






