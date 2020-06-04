package can;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import driving2.Client;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

//CAN�ϰ� ����� �� �ִ� Listener
//�ø��� ����� ���ؼ� �����Ͱ� ���۵Ǿ��� �� ����Ǵ� Ŭ����
public class SerialListener implements SerialPortEventListener{
	BufferedInputStream bis; // ĵ������� input�Ǵ� �����͸� �б� ���� ���µ� ��Ʈ��
	PrintWriter pw;
	public SerialListener(BufferedInputStream bis, PrintWriter pw) {
		this.bis = bis;
		this.pw = pw;
	}
	
	//�����Ͱ� ���۵ɶ����� ȣ��Ǵ� �޼ҵ�
	@Override
	public void serialEvent(SerialPortEvent event) {
		ArrayList<String> list = new ArrayList<>();
		
		switch(event.getEventType()) {
		//�����Ͱ� �����ϸ�
		case SerialPortEvent.DATA_AVAILABLE:
			byte[] readBuffer = new byte[128];
			try {
				while(bis.available()>0) {
					int numbyte = bis.read(readBuffer);
				}
				String data = new String(readBuffer);
				data = data.trim();
				System.out.println("CAN�ø��� ��Ʈ�� ���۵� ������ ===> "+data);
				//���۵Ǵ� �޽����� �˻��ؼ� �����ϰ� �ٸ� ��ġ�� �����ϰų�
				//CarŬ���̾�Ʈ ��ü�� �����ؼ� ������ ���۵ǵ��� ó��
				String mData[] = data.split(":");
				for(int i=1; i<mData.length; i++) {
					//System.out.println(i+"��° data => "+mData[i]);
					String checkId = mData[i].substring(9,11);
					String checkValue = mData[i].substring(25, 27);
					//System.out.println(i+"��° **** ID **** "+checkId);
					//System.out.println(i+"��° **** VALUE **** "+checkValue);
					if(checkId.equals("01")) {
						String id = "�µ�";
						pw.println(Client.nickname+"/temperature/"+checkValue);
						System.out.print(id+":"+checkValue+"  ");
					}else if(checkId.equals("02")) {
						String id = "�ٵ�";
						System.out.print(id+":"+checkValue);
						pw.println(Client.nickname+"/humidity/"+checkValue);
						
					}
				}
				System.out.println();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
