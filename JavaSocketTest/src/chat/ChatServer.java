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

//ʵ�ֳ��������ҹ��ܣ�������ͻ��ˣ�һ���ͻ���д�룬�����ͻ��˿�ͬʱ����

public class ChatServer {

	private int port = 10099;
	private ServerSocket serverSocket;
	public static List<Socket> socketList = new ArrayList<Socket>();
	
	public ChatServer() throws IOException{
		serverSocket = new ServerSocket(port);
		System.out.println("����������");
	}
	
	public void service(){
		Socket socket = null;
		while(true){
			try {
				socket = serverSocket.accept();
				socketList.add(socket);
				//ÿ�����µĿͻ������ӣ������߳�Ϊ�ÿͻ��˷���
				Thread thread = new Thread(new HandlerThread(socket));
				System.out.println("�̣߳�"+thread.getName()+"����");
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
			//ѭ����socket�ж�ȡ�ͻ��˷��͹���������
			while((msg = readFromClient())!=null){
				//����socketList�е�ÿһ��Socket,��������������ÿһ��Socket����һ�Ρ�
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
