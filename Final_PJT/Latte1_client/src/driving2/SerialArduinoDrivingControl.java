package driving2;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.TooManyListenersException;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

public class SerialArduinoDrivingControl {
	OutputStream os;
	InputStream is;
	PrintWriter pw;

	public SerialArduinoDrivingControl(PrintWriter pw) {
		this.pw = pw;
	}

	public void connect(String portName) {
		try {
			CommPortIdentifier commPortIdentifier = CommPortIdentifier.getPortIdentifier(portName);
			if (commPortIdentifier.isCurrentlyOwned()) {
				//CommPort commPort = commPortIdentifier.
				System.out.println("포트사용불가능$$$$$$");
			} else {
				System.out.println("포트사용가능!!!!!!");
				try {
					CommPort commPort = commPortIdentifier.open("basic_serial", 5000);
					if (commPort instanceof SerialPort) {
						SerialPort serialPort = (SerialPort) commPort;
						try {
							serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
									SerialPort.PARITY_NONE);
							is = serialPort.getInputStream();
							os = serialPort.getOutputStream();
						
							try {
								serialPort.addEventListener(new SerialListener(is, pw));
								serialPort.notifyOnDataAvailable(true);
							} catch(TooManyListenersException e) {
								
							}
							
						} catch (UnsupportedCommOperationException e) {
							e.printStackTrace();
						} catch (IOException e) {

						}
					} else {
						System.out.println("not Serial");
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
