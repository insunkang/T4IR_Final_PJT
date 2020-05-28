package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

import homeappliances.ReceiverThread;
import homeappliances.SerialArduinoHomeControl;
import homeappliances.Variable;
import pir.ArduinoSerialReadUsingEvent;

public class HomeClient {
    InputStream is;
    InputStreamReader isr;
    BufferedReader br;
    Socket socket;
    OutputStream os;
    PrintWriter pw;
    String homeId;
    
    ArduinoSerialReadUsingEvent serialObj;
    BufferedReader hbr;
	PrintWriter hpw;
	OutputStream hos;
    
	public HomeClient() {
		connect();
		
	}
	public void connect() {
		try {
			socket = new Socket(Variable.ip, Variable.port);
			System.out.println("���Ӽ���...");
	        if (socket != null) {
	        	System.out.println("���̾ƴϴ�.");
	        	//������ �Ŀ� Ŭ���̾�Ʈ�� ���̵� �����մϴ�.
	        	homeId = "1111";
	            ioWork();
	            new ReceiverThread(hos,hbr).start();
	            new ArduinoSerialReadUsingEvent(socket, homeId, hpw);
	        }
		} catch (IOException e2) {
			e2.printStackTrace();
		}
   	}
	void ioWork(){
	    try {
	        is = socket.getInputStream();
	        isr = new InputStreamReader(is);
	        br = new BufferedReader(isr);

	        os = socket.getOutputStream();
	        pw = new PrintWriter(os,true);
	        //
	        hbr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			hpw = new PrintWriter(socket.getOutputStream(), true);
			serialObj = new ArduinoSerialReadUsingEvent(socket, homeId, hpw);
			hos = serialObj.getOutput();
	        //
	        //���⼭ Ŭ���̾�Ʈ�� ���̵� �������� �����մϴ�.
	        pw.println("home/"+homeId);
            pw.flush();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	}
	public static void main(String[] args) {
		new HomeClient();
	}
}
