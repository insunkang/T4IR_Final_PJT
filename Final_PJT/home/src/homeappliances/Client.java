package homeappliances;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends Thread{
	String nickname;
	Socket socket;
	
	BufferedReader br;
	PrintWriter pw;
	SerialArduinoHomeControl serialObj;
	OutputStream os;
	
	public void connectServer() {
		try {
			socket = new Socket(Variable.ip, Variable.port);
			if (socket != null) {
				ioWork();
				new ReceiverThread(os,br).start();
			}
		} catch (UnknownHostException e) {
		} catch (IOException e) {
		}
		
	}
	public void ioWork() {
		try {
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			pw = new PrintWriter(socket.getOutputStream(), true);
			serialObj = new SerialArduinoHomeControl(pw);
			serialObj.connect(Variable.Serialport);
			os = serialObj.getOutput();
		} catch (IOException e) {
		}
	}

	public static void main(String[] args) {
		Client client = new Client();
		client.connectServer();
		
	}
}
