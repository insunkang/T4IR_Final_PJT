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
			System.out.println("접속성공...");
	        if (socket != null) {
	        	System.out.println("널이아니다.");
	        	//접속한 후에 클라이언트의 아이디를 생성합니다.
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
	        //여기서 클라이언트의 아이디를 서버에게 전송합니다.
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
