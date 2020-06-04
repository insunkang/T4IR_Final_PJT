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

//�ø�������� ����ϴ� ��ü
public class SerialConnect {
	InputStream in;
	BufferedInputStream bis;
	OutputStream out;
	CommPort commPort;
	public void connect(String portName, String appName) {
		try {
			// COM��Ʈ�� ���� �����ϰ� ��밡���� �������� Ȯ��
			CommPortIdentifier commPortIdentifier = CommPortIdentifier.getPortIdentifier(portName);
			if (commPortIdentifier.isCurrentlyOwned()) {
				System.out.println("��Ʈ ����� �� �����ϴ�.");
			} else {
				System.out.println("��Ʈ�� ��� ����.");
				// port�� ��� �����ϸ� ��Ʈ�� ���� ��Ʈ��ü�� ������
				// �Ű�����1 : ��Ʈ�� ���� ����ϴ� ���α׷��� �̸�
				// �Ű�����2 : ��Ʈ�� ���� ����ϱ� ���ؼ� ��ٸ��� �ð�
				commPort = commPortIdentifier.open(appName, 5000);
				System.out.println(commPort);

				if (commPort instanceof SerialPort) {
					System.out.println("SerialPort");
					SerialPort serialPort = (SerialPort) commPort;
					// SerialPort�� ���� ����
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
					System.out.println("SerialPort�� �۾��� �� �ֽ��ϴ�.");
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
