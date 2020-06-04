package can;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.TooManyListenersException;

import gnu.io.SerialPort;

public class CANReadWriteTest {
	SerialConnect serialConnect; // CAN시리얼 포트 연결
	OutputStream out; // CAN과 output통신할 스트림
	PrintWriter pw;
	public CANReadWriteTest(String portname,PrintWriter pw) {
		this.pw = pw;
		// 시리얼 통신을 위한 연결작업
		serialConnect = new SerialConnect();
		serialConnect.connect(portname, this.getClass().getName());

		// input, output작업을 하기 위해 리스너를 port에 연결
		SerialPort commport = (SerialPort) serialConnect.getCommPort();
		SerialListener listener = new SerialListener(serialConnect.getBis(), pw);
		try {
			commport.addEventListener(listener);
			commport.notifyOnDataAvailable(true);
		} catch (TooManyListenersException e) {
			e.printStackTrace();
		}
		// output스트림 받기
		out = serialConnect.getOut();
		
		//처음 CAN통신을 위한 준비 작업을 할 때는 수신가능한 메시지를 보낼 수 있도록
		new Thread(new CANWriteThread()).start();
		
	}//end 생성자
	
	public void send(String msg) {
		new Thread(new CANWriteThread(msg)).start();
	}
	
	class CANWriteThread implements Runnable{
		//송신할 데이터를 저장할 변수
		String data;
		//************처음에 수신가능 설정*****************
		public CANWriteThread(){
			this.data = ":G11A9\r";
		}
		//***********메시지 보낼때마다 실행*****************
		public CANWriteThread(String msg){
			this.data = convert_data(msg);
		}
		public String convert_data(String msg) {
			msg = msg.toUpperCase();// 메시지를 대문자로 변환
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
		String id = "11111111"; // 송신할 메시지의 구분id
		String data = "1111111111111111"; // 송신할 데이터
		String msg = id + data;
		CANReadWriteTest canObj = new CANReadWriteTest("COM10");
		canObj.send(msg);
	}
*/
}
