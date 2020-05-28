package rfid;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class RfidControl {
	public static void main(String[] args) {
		PrintWriter pr;
		SerialArduinoRFIDTest serialObj;
		OutputStream out;
		Scanner key = new Scanner(System.in);
		serialObj = new SerialArduinoRFIDTest();
		serialObj.connect("COM7");
		out = serialObj.getOutput();

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
						System.out.println("클라이언트에게 받은 메시지:"+msg);
						if(toggle.equals("off")) {
							out.write('9');
						}else if(toggle.equals("on")){
							out.write('5');
						}
					} catch (IOException e) {
						System.out.println(e);
					}
				}
			}
		});
		t1.start();
	}

}
