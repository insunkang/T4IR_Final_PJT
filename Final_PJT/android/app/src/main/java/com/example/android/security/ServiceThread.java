package com.example.android.security;

import android.os.Handler;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ServiceThread extends Thread{
    Handler handler;
    boolean isRun = true;
    InputStream is;
    InputStreamReader isr;
    BufferedReader br;
    Socket socket;
    OutputStream os;
    PrintWriter pw;

    public ServiceThread(Handler handler){
        this.handler = handler;
    }

    public void stopForever(){
        synchronized (this) {
            this.isRun = false;
        }
    }

    public void run(){
        try {
            socket = new Socket("70.12.228.82", 12345);
            if (socket != null) {
                ioWork();
            }
            //서버에서 전달되는 메시지를 읽는 쓰레드
            Thread t1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        String msg;
                        try {
                            msg = br.readLine();
                            Log.d("chat", "서버로 부터 수신된 메시지>>"
                                    + msg);
                            if (msg.equals("on")){
                                handler.sendEmptyMessage(0);//쓰레드에 있는 핸들러에게 메세지를 보냄
                            }
                        } catch (IOException e) {

                        }
                    }
                }
            });
            t1.start();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    void ioWork(){
        try {
            is = socket.getInputStream();
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);

            os = socket.getOutputStream();
            pw = new PrintWriter(os,true);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
