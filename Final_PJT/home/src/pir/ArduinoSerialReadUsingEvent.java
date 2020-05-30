package pir;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.TooManyListenersException;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import homeappliances.HomeSerialListener;
import homeappliances.Variable;

public class ArduinoSerialReadUsingEvent {
	Socket socket;
	OutputStream out;
	OutputStream outardu;
	String homeId;
	OutputStream os;
	InputStream is;
	PrintWriter pw;
	public ArduinoSerialReadUsingEvent(Socket socket, String homeId, PrintWriter pw) {
		super();
		this.socket = socket;
		this.homeId = homeId;
		this.pw = pw;
		try {
			CommPortIdentifier commPortIdentifier = 
					CommPortIdentifier.getPortIdentifier(Variable.Serialport);
			if(commPortIdentifier.isCurrentlyOwned()) {
				System.out.println("��Ʈ ����� �� �����ϴ�.");
			}else {
				System.out.println("��Ʈ ��밡��.");
				CommPort commPort =
						commPortIdentifier.open("basic_serial",
								5000);
				if(commPort instanceof SerialPort) {
					System.out.println("SerialPort");
					SerialPort serialPort = (SerialPort)commPort;
					//SerialPort�� ���� ����
					serialPort.setSerialPortParams(9600,
							SerialPort.DATABITS_8,
							SerialPort.STOPBITS_1,
							SerialPort.PARITY_NONE);
					RfidControl(serialPort);
					InputStream in = serialPort.getInputStream();
					out = socket.getOutputStream();
					os = serialPort.getOutputStream();
					
					//Arduino�� ���ؼ� �ݺ��ؼ� ������ �����͸� ���� �� �ֵ��� 
					//�̺�Ʈ�� �����ϵ��� ����
					SerialListener listener = new SerialListener(in, out, homeId);
					serialPort.addEventListener(listener);
					//�ø�����Ʈ�� ���۵Ǿ� ������ �����Ͱ� �ִٸ� noti�ϸ� �̺�Ʈ
					//�����ʰ� ����ǵ��� ó��
					serialPort.notifyOnDataAvailable(true);
				}else {
					System.out.println("SerialPort�� �۾��� �� �ֽ��ϴ�.");
				}
			}
		} catch (NoSuchPortException e) {
			e.printStackTrace();
		} catch (PortInUseException e) {
			e.printStackTrace();
		} catch (UnsupportedCommOperationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TooManyListenersException e) {
			e.printStackTrace();
		}
	}
	public void RfidControl(SerialPort serialPort) {
		PrintWriter pr;
		Scanner key = new Scanner(System.in);
		try {
			outardu = serialPort.getOutputStream();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
				
		Thread t1 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				String toggle = "on";
				while(true){
					try {
						String msg = key.next();
						if(msg.equals("0005276273")) {
							if(toggle.equals("on")) {
								toggle = "off";
							}else if(toggle.equals("off")) {
								toggle = "on";
							}
						}
						System.out.println("Ŭ���̾�Ʈ���� ���� �޽���:"+msg);
						if(toggle.equals("off")) {
							outardu.write('9');
						}else if(toggle.equals("on")){
							outardu.write('8');
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
