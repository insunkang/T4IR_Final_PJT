package ksh_final_can;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;

import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import ksh_final_serial.SerialArduinoControl;

//시리얼 통신을 통해서 데이터가 전송되었을 때
public class SerialListener implements SerialPortEventListener{
	BufferedInputStream bis;//캔통신으로 input되는 데이터를 읽기 위해 오픈된 스트림
	OutputStream os;
	public SerialListener(BufferedInputStream bis, OutputStream os) {
		this.bis=bis;
		this.os=os;
		// TODO Auto-generated constructor stub
	}

	//데이터가 전송될때마다 호출되는 메소드
	@Override
	public void serialEvent(SerialPortEvent event) {
		
		switch(event.getEventType()) {
		case SerialPortEvent.DATA_AVAILABLE :
			byte[] readBuffer=new byte[128];
			try {
				while(bis.available()>0) {
					int numbyte=bis.read(readBuffer);
				}
				String data=new String(readBuffer);
				data = data.trim();
				System.out.println("Can시리얼포트로 전송할 데이터:"+data);
				//전송되는 메시지를 검사해서 적절하게 다른 장치를 제어하거나
				//Car클라이언트 객체로 전달해서 서버로 전송되도록 처리
				String check = data.substring(1, 2);
				System.out.println(check);
				if(check.equals("U")) {
					String checkValue = data.substring(26, 28);
					System.out.println("check::::"+checkValue);
					if(checkValue.equals("11")) {
						System.out.println("emergency");
						os.write('e');
					}else if(checkValue.equals("12")) {
						System.out.println("leftLight");
						os.write('L');
					}else if(checkValue.equals("13")) {
						System.out.println("rightLight");
						os.write('R');
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
}
