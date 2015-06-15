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

//客户端同时处理多客户端(方法一)：为每个客户分配一个线程
//该方法缺点：（1）服务器创建和销毁工作线程的开销很大 （2）频繁创建和销毁线程，那么将导致频繁地切换线程
public class MultiEchoServer1 {

	private int port = 9999;
	private ServerSocket serverSocket;
	
	public MultiEchoServer1() throws IOException{
		serverSocket = new ServerSocket(port);
		System.out.println("服务器启动");
	}
	
	public void service(){
		
		while(true){
			Socket socket = null;
			try {
				socket = serverSocket.accept();
				Thread workThread = new Thread(new HandlerThread(socket));
				
				System.out.println("线程："+workThread.getName()+"启动");
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
				//获取当前线程名称：Thread.currentThread().getName()
				System.out.println(Thread.currentThread().getName()+"发送信息："+msg);
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


