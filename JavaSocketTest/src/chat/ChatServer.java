package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

//实现初级聊天室功能：启多个客户端，一个客户端写入，其他客户端可同时读出

public class ChatServer {

	private int port = 10099;
	private ServerSocket serverSocket;
	public static List<Socket> socketList = new ArrayList<Socket>();
	
	public ChatServer() throws IOException{
		serverSocket = new ServerSocket(port);
		System.out.println("服务器启动");
	}
	
	public void service(){
		Socket socket = null;
		while(true){
			try {
				socket = serverSocket.accept();
				socketList.add(socket);
				//每当有新的客户端连接，启动线程为该客户端服务
				Thread thread = new Thread(new HandlerThread(socket));
				System.out.println("线程："+thread.getName()+"启动");
				thread.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) throws IOException {
		new ChatServer().service();
	}
	
}

class HandlerThread implements Runnable{

	private Socket socket;
	public HandlerThread(Socket socket){
		this.socket = socket;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			
			String msg = null;
			//循环从socket中读取客户端发送过来的数据
			while((msg = readFromClient())!=null){
				//遍历socketList中的每一个Socket,将读到的内容向每一个Socket发送一次。
				for(Socket s : ChatServer.socketList){
					PrintWriter outStream = getWriter(s);
					outStream.println(Thread.currentThread().getName()+":"+msg);
					outStream.flush();

				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private BufferedReader getReader(Socket socket) throws IOException{
		InputStream in = socket.getInputStream();
		return new BufferedReader(new InputStreamReader(in));
	}
	
	private PrintWriter getWriter(Socket socket) throws IOException{
		OutputStream out = socket.getOutputStream();
		return new PrintWriter(out);
	}
	
	private String readFromClient(){
		try {
			BufferedReader in = getReader(socket);
			return in.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			ChatServer.socketList.remove(socket);
			e.printStackTrace();
		}
		return null;
	}
	
	
}
