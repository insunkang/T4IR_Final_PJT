package ksh_final_serial;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;

import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import ksh_final_can.CANReadWriteTest;

public class SerialListener implements SerialPortEventListener {

	private InputStream in;
	CANReadWriteTest crw;
	public SerialListener(InputStream in, CANReadWriteTest crw) {
		super();
		this.in=in;
		this.crw = crw;
	}
	
	//이벤트가 발생할때마다 호출되는 메소드
	//발생한 이벤트의 정보를 담고있는 객체 - SerialPortEvent
	@Override
	public void serialEvent(SerialPortEvent event) {
		if(event.getEventType()==SerialPortEvent.DATA_AVAILABLE) {
			try {
				//전송되는 데이터의 크기를 추출
				int check_size=in.available();
				byte[] data=new byte[check_size];
				in.read(data,0,check_size);
				System.out.println("받은 데이터:"+new String(data));
				String msg = new String(data);
				if(msg!=""&msg!="\n") {
					StringTokenizer st = new StringTokenizer(msg,"/");
					String T = st.nextToken();
					String Tvalue = st.nextToken();
					String H = st.nextToken();
					String Hvalue = st.nextToken();
					String send = "";
					
					String id = "00000001"; // 송신할 메시지의 구분id
					String Tdata = "00000000000000"+Tvalue; // 송신할 데이터
					send = id+Tdata;
					crw.send(send);
					
					id = "00000002"; // 송신할 메시지의 구분id
					String Hdata = "00000000000000"+Hvalue; // 송신할 데이터
					send = id+Hdata;
					crw.send(send);
				}
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
