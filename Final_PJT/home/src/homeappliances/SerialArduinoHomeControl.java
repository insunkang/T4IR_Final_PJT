package homeappliances;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.TooManyListenersException;

import homeappliances.HomeSerialListener;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

public class SerialArduinoHomeControl {
	OutputStream os;
	InputStream is;
	PrintWriter pw;

	public SerialArduinoHomeControl() {
	}

	public SerialArduinoHomeControl(PrintWriter pw) {
		this.pw = pw;
	}

	public void connect(String portName) {
		try {
			CommPortIdentifier commPortIdentifier = CommPortIdentifier.getPortIdentifier(portName);
			if (commPortIdentifier.isCurrentlyOwned()) {
				System.out.println("��Ʈ ����� �� �����ϴ�.");
			} else {
				System.out.println("��Ʈ ��밡��.");
				try {
					CommPort commPort = commPortIdentifier.open("basic_serial", 5000);
					if (commPort instanceof SerialPort) {
						System.out.println("SerialPort");
						SerialPort serialPort = (SerialPort) commPort;
						try {
							serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
									SerialPort.PARITY_NONE);
							is = serialPort.getInputStream();
							os = serialPort.getOutputStream();
							/*try {
								serialPort.addEventListener(new HomeSerialListener(is, pw));
								serialPort.notifyOnDataAvailable(true);
							} catch (TooManyListenersException e) {
							}*/
						} catch (UnsupportedCommOperationException e) {
							e.printStackTrace();
						} catch (IOException e) {
						}
					} else {
						System.out.println("SerialPort�� �۾��� �� �ֽ��ϴ�.");
					}
				} catch (PortInUseException e) {
					e.printStackTrace();
				}
			}
		} catch (NoSuchPortException e) {
			e.printStackTrace();
		} finally {

		}
	}

	public OutputStream getOutput() {
		return os;
	}

}
