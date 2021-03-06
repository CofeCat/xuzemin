package com.jdrd.bodyControl.main;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class BodyControl {
	private static String IP_ADDR = "192.168.88.106";
	private static int PORT = 12345;
	private static Process ARproc = null;
	private static Process ADproc = null;
	public static void main(String[] args) {
		while(true){
			Socket socket = null;  
			InputStream inputStream = null;
			PrintWriter out = null;
			try {
				socket = new Socket(IP_ADDR,PORT);
				out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "GBK")), true);
				inputStream = socket.getInputStream();
				byte buffer[] = new byte[1024 * 4];
				int temp = 0;
				out.println("connect");
				while ((temp = inputStream.read(buffer)) != -1) {
					String msg = new String(buffer, 0, temp, "GBK");
					System.out.println(msg);
					out.println("success");
					if(msg.equals("openAR")){
						openAR();
					}else if(msg.equals("openAD")){
						openAD();
					}else if(msg.equals("close")){
						close();
					}
				}
				ADproc.destroy();
				ARproc.destroy();
				out.close(); 
				inputStream.close();
				socket.close();
			}catch(IOException e) { 
				e.printStackTrace();
			}
		
		}
	}
	private static void openAD(){
		Runtime rt = Runtime.getRuntime();
		String fileLac = "";
		try {
			if(ARproc !=null){
				ARproc.destroy();
			}
			if(!ADproc.isAlive()){
				fileLac = "C:\\Users\\Administrator\\Desktop\\FaceMask.exe";
				ADproc = rt.exec(fileLac);
			}
		} catch (Exception e) {
			System.out.println("open failure");
		}
	}
	private static void openAR(){
		Runtime rt = Runtime.getRuntime();
		String fileLac = "";
		try {
			if(ADproc != null){
				ADproc.destroy();
			}
			if(!ARproc.isAlive()){
				fileLac = "C:\\Users\\Administrator\\Desktop\\1234\\ZhongTai.exe";
				ARproc = rt.exec(fileLac);
			}
		} catch (Exception e) {
			System.out.println("open failure");
		}
	}
	private static void close(){
		try {
			Runtime.getRuntime().exec("shutdown -s -t 1"); 
		} catch (Exception e) {
			System.out.println("open failure");
		}
	}
}
