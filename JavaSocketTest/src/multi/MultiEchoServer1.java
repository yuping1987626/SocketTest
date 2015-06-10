package multi;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

//�ͻ���ͬʱ�����ͻ���(����һ)��Ϊÿ���ͻ�����һ���߳�
//�÷���ȱ�㣺��1�����������������ٹ����̵߳Ŀ����ܴ� ��2��Ƶ�������������̣߳���ô������Ƶ�����л��߳�
public class MultiEchoServer1 {

	private int port = 9999;
	private ServerSocket serverSocket;
	
	public MultiEchoServer1() throws IOException{
		serverSocket = new ServerSocket(port);
		System.out.println("����������");
	}
	
	public void service(){
		
		while(true){
			Socket socket = null;
			try {
				socket = serverSocket.accept();
				Thread workThread = new Thread(new HandlerThread(socket));
				
				System.out.println("�̣߳�"+workThread.getName()+"����");
				workThread.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public static void main(String[] args) throws IOException {
		new MultiEchoServer1().service();
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
			
			System.out.println("New connection accepted "+socket.getInetAddress()+":"+socket.getPort());
			PrintWriter out = getPrinter(socket);
			BufferedReader in = getReader(socket);
			
			String msg = null;
			while((msg = in.readLine()) != null){
				//��ȡ��ǰ�߳����ƣ�Thread.currentThread().getName()
				System.out.println(Thread.currentThread().getName()+"������Ϣ��"+msg);
				out.println(echo(msg));
				out.flush();
				if(msg.equals("bye"))
					break;
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				if(socket != null){
					socket.close();	
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private PrintWriter getPrinter(Socket socket) throws IOException{
		OutputStream out = socket.getOutputStream();
		return new PrintWriter(out);
	}
	
	private BufferedReader getReader(Socket socket) throws IOException{
		InputStream in = socket.getInputStream();
		return new BufferedReader(new InputStreamReader(in));
	}
	
	public String echo(String str){
		return "echo:" + str;
	}
}


