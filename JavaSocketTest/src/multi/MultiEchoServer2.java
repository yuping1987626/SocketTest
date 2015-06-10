package multi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.server.SocketSecurityException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//客户端同时处理多客户端(方法二)：创建一个线程池，由其中的工作线程来为客户服务。
//线程池为线程生命周期开销问题和系统资源不足问题提供了解决方案:
//(1)减少了创建和销毁线程的次数，每个工作线程都可以一直被重用，能执行多个任务。
//(2)可以根据系统的承载能力，方便地调整线程池中线程的数目，防止因为消耗过量系统资源而导致系统崩溃。

public class MultiEchoServer2 {

	private int port = 9999;
	private ServerSocket serverSocket;
	private ExecutorService executorService; //线程池
	private final int POOL_SIZE = 4; //单个CPU时线程池中工作线
	
	public MultiEchoServer2() throws IOException{
		serverSocket = new ServerSocket(port);
		//Runtime.getRuntime().availableProcessors()获取系统运行时cpu个数
		executorService = Executors.newFixedThreadPool(POOL_SIZE * Runtime.getRuntime().availableProcessors());
		System.out.println("服务器启动");
	}
	
	public void service(){
		Socket socket = null;
		while(true){
			try {
				socket = serverSocket.accept();
				executorService.execute(new Thread(new OneHandler(socket)));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
	}
	
	public static void main(String[] args) throws IOException {
		new MultiEchoServer2().service();
	}
}

class OneHandler implements Runnable{
	
	private Socket socket ;
	
	public OneHandler(Socket socket){
		this.socket = socket;
	}
	
	private PrintWriter getWriter(Socket socket) throws IOException{
		
		OutputStream outStream = socket.getOutputStream();
		return new PrintWriter(outStream);
	}
	
	private BufferedReader getReader(Socket socket) throws IOException{
		InputStream inStream = socket.getInputStream();
		return new BufferedReader(new InputStreamReader(inStream));
	}
	
	public String echo(String msg){
		return "echo"+msg;
	}

	@Override
	public void run() {
		
		try {
			System.out.println("New connection accepted "+socket.getInetAddress()+":"+socket.getPort());
			PrintWriter out = getWriter(socket);
			BufferedReader in = getReader(socket);
			String msg = null;
			while((msg = in.readLine())!=null){
				System.out.println(msg);
				System.out.println("线程："+Thread.currentThread().getName()+":"+msg);
				out.println(echo(msg));
				out.flush();
				if(msg.equals("bye")){
					break;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				if(socket!=null)
					socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
