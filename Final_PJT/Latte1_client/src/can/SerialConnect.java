package can;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

//시리얼통신을 담당하는 객체
public class SerialConnect {
	InputStream in;
	BufferedInputStream bis;
	OutputStream out;
	CommPort commPort;
	public void connect(String portName, String appName) {
		try {
			// COM포트가 실제 존재하고 사용가능한 상태인지 확인
			CommPortIdentifier commPortIdentifier = CommPortIdentifier.getPortIdentifier(portName);
			if (commPortIdentifier.isCurrentlyOwned()) {
				System.out.println("포트 사용할 수 없습니다.");
			} else {
				System.out.println("포트를 사용 가능.");
				// port가 사용 가능하면 포트를 열고 포트객체를 얻어오기
				// 매개변수1 : 포트를 열고 사용하는 프로그램의 이름
				// 매개변수2 : 포트를 열고 통신하기 위해서 기다리는 시간
				commPort = commPortIdentifier.open(appName, 5000);
				System.out.println(commPort);

				if (commPort instanceof SerialPort) {
					System.out.println("SerialPort");
					SerialPort serialPort = (SerialPort) commPort;
					// SerialPort에 대한 설정
					try {
						serialPort.setSerialPortParams(38400, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
								SerialPort.PARITY_NONE);
						in = serialPort.getInputStream();
						bis = new BufferedInputStream(in);
						out = serialPort.getOutputStream();
						
						
					} catch (UnsupportedCommOperationException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					System.out.println("SerialPort만 작업할 수 있습니다.");
				}
			}
		} catch (NoSuchPortException | PortInUseException e) {
			e.printStackTrace();
		}
	}
	public BufferedInputStream getBis() {
		return bis;
	}
	public OutputStream getOut() {
		return out;
	}
	public CommPort getCommPort() {
		return commPort;
	}
	
}//end class
