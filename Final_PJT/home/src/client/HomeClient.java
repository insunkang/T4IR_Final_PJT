package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import homeappliances.ReceiverThread;
import homeappliances.SerialArduinoHomeControl;
import homeappliances.Variable;

public class HomeClient {
    InputStream is;
    InputStreamReader isr;
    BufferedReader br;
    Socket socket;
    OutputStream os;
    PrintWriter pw;
    String homeId;
    SerialArduinoHomeControl serialObj;
    
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
	            
	            //new ArduinoSerialReadUsingEvent(socket, homeId, hpw);
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
			serialObj = new SerialArduinoHomeControl(pw);
	        serialObj.connect(Variable.Serialport);
	        new ReceiverThread(serialObj.getOutput(), br).start();
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
