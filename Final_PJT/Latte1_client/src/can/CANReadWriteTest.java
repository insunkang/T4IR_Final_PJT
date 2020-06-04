package can;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.TooManyListenersException;

import gnu.io.SerialPort;

public class CANReadWriteTest {
	SerialConnect serialConnect; // CAN�ø��� ��Ʈ ����
	OutputStream out; // CAN�� output����� ��Ʈ��
	PrintWriter pw;
	public CANReadWriteTest(String portname,PrintWriter pw) {
		this.pw = pw;
		// �ø��� ����� ���� �����۾�
		serialConnect = new SerialConnect();
		serialConnect.connect(portname, this.getClass().getName());

		// input, output�۾��� �ϱ� ���� �����ʸ� port�� ����
		SerialPort commport = (SerialPort) serialConnect.getCommPort();
		SerialListener listener = new SerialListener(serialConnect.getBis(), pw);
		try {
			commport.addEventListener(listener);
			commport.notifyOnDataAvailable(true);
		} catch (TooManyListenersException e) {
			e.printStackTrace();
		}
		// output��Ʈ�� �ޱ�
		out = serialConnect.getOut();
		
		//ó�� CAN����� ���� �غ� �۾��� �� ���� ���Ű����� �޽����� ���� �� �ֵ���
		new Thread(new CANWriteThread()).start();
		
	}//end ������
	
	public void send(String msg) {
		new Thread(new CANWriteThread(msg)).start();
	}
	
	class CANWriteThread implements Runnable{
		//�۽��� �����͸� ������ ����
		String data;
		//************ó���� ���Ű��� ����*****************
		public CANWriteThread(){
			this.data = ":G11A9\r";
		}
		//***********�޽��� ���������� ����*****************
		public CANWriteThread(String msg){
			this.data = convert_data(msg);
		}
		public String convert_data(String msg) {
			msg = msg.toUpperCase();// �޽����� �빮�ڷ� ��ȯ
			msg = "W28" + msg;
			msg = msg + makeCheckSum(msg);

			return ":" + msg + "\r";
		}

		public String makeCheckSum(String msg) {
			char[] data_arr = msg.toCharArray();
			int sum = 0;
			for (int i = 0; i < data_arr.length; i++) {
				sum += data_arr[i];
			}
			sum = (sum & 0xff);
			return Integer.toHexString(sum).toUpperCase();
		}
		@Override
		public void run() {
			byte[] outputdata = data.getBytes();
			try {
				out.write(outputdata);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	/*
	public static void main(String[] args) {
		String id = "11111111"; // �۽��� �޽����� ����id
		String data = "1111111111111111"; // �۽��� ������
		String msg = id + data;
		CANReadWriteTest canObj = new CANReadWriteTest("COM10");
		canObj.send(msg);
	}
*/
}
