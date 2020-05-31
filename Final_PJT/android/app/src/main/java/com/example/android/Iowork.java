package com.example.android;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

public class Iowork {

    InputStream is;
    InputStreamReader isr;
    BufferedReader br;
    OutputStream os;
    PrintWriter pw;

    public Iowork(InputStream is, InputStreamReader isr, BufferedReader br, OutputStream os, PrintWriter pw) {
        this.is = is;
        this.isr = isr;
        this.br = br;
        this.os = os;
        this.pw = pw;
    }

    public InputStream getIs() {
        return is;
    }

    public void setIs(InputStream is) {
        this.is = is;
    }

    public InputStreamReader getIsr() {
        return isr;
    }

    public void setIsr(InputStreamReader isr) {
        this.isr = isr;
    }

    public BufferedReader getBr() {
        return br;
    }

    public void setBr(BufferedReader br) {
        this.br = br;
    }

    public OutputStream getOs() {
        return os;
    }

    public void setOs(OutputStream os) {
        this.os = os;
    }

    public PrintWriter getPw() {
        return pw;
    }

    public void setPw(PrintWriter pw) {
        this.pw = pw;
    }
}
