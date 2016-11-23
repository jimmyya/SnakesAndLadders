package com.qg.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Int;
import static sun.audio.AudioPlayer.player;

/**
 * 服务端
 * @author CHEN
 *
 */
public class Server {
		
	private final static int PORT=3000;

	/**
	 * 收集客户端
	 * 并启动对客户端的监视线程
	 */
    public void server() {
		try {
			Map<Integer,Socket> sockets=new HashMap<>();//客户端集合
			ServerSocket serverSocket=new ServerSocket(PORT);
			while(true) {
				Socket socket=serverSocket.accept();
				Thread newSocket=new ClientThread(socket,sockets);
				newSocket.start();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new Server().server();
	}

}

/**
 * 客户端线程类
 */
class ClientThread extends Thread {
	private final static int PLAYNUM=5;
	Socket s;
	Map<Integer,Socket> sockets;
	public ClientThread (Socket s,Map sockets) {
		this.s=s;
		this.sockets=sockets;
	}

	@Override
	public void run() {
		try {
			//寻找num
			int i;
			for(i=0;i<sockets.size();i++) {
				if(sockets.containsKey(i)) {continue;}
				else break;
			}
			int num=i;
			BufferedReader in=new BufferedReader(new InputStreamReader(s.getInputStream()));
			//发送编号
			PrintWriter outNum=new PrintWriter(s.getOutputStream());
			outNum.println(num);
			outNum.flush();
			if(num<PLAYNUM) sockets.put(num,s);
			while(true) {
				String str="";
				try {
					str = in.readLine();
				} catch (Exception e) {
					System.out.println("玩家"+num+"退出");
					sockets.remove(num);
					break;
				}
				System.out.println(str);
				for(i=0;i<sockets.size();i++) {
					Socket temp=sockets.get(i);
					PrintWriter out=new PrintWriter(temp.getOutputStream());
					out.println(str+":"+sockets.size());
					out.flush();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
