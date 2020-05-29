package homeappliances;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.TooManyListenersException;

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
				System.out.println("포트 사용할 수 없습니다.");
			} else {
				System.out.println("포트 사용가능.");
				try {
					CommPort commPort = commPortIdentifier.open("basic_serial", 5000);
					if (commPort instanceof SerialPort) {
						System.out.println("SerialPort");
						SerialPort serialPort = (SerialPort) commPort;
						try {
							serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
									SerialPort.PARITY_NONE);
							RfidControl(serialPort);
							InputStream in = serialPort.getInputStream();
							is = serialPort.getInputStream();
							os = serialPort.getOutputStream();
							try {
								serialPort.addEventListener(new HomeSerialListener(is, pw, "1111"));
								serialPort.notifyOnDataAvailable(true);
							} catch (TooManyListenersException e) {
							}
						} catch (UnsupportedCommOperationException e) {
							e.printStackTrace();
						} catch (IOException e) {
						}
					} else {
						System.out.println("SerialPort만 작업할 수 있습니다.");
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

	public void RfidControl(SerialPort serialPort) {
		PrintWriter pr;
		Scanner key = new Scanner(System.in);
		

		Thread t1 = new Thread(new Runnable() {

			@Override
			public void run() {
				String toggle = "on";
				while (true) {
					try {
						String msg = key.next();
						if (msg.equals("0005276273")) {
							if (toggle.equals("on")) {
								toggle = "off";
							} else if (toggle.equals("off")) {
								toggle = "on";
							}
						}
						System.out.println("클라이언트에게 받은 메시지:" + msg);
						if (toggle.equals("off")) {
							os.write('9');
						} else if (toggle.equals("on")) {
							os.write('8');
						}
					} catch (IOException e) {
						System.out.println(e);
					}
				}
			}
		});
		t1.start();
	}

	public OutputStream getOutput() {
		return os;
	}

}
