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

//�ͻ���ͬʱ�����ͻ���(������)������һ���̳߳أ������еĹ����߳���Ϊ�ͻ�����
//�̳߳�Ϊ�߳��������ڿ��������ϵͳ��Դ���������ṩ�˽������:
//(1)�����˴����������̵߳Ĵ�����ÿ�������̶߳�����һֱ�����ã���ִ�ж������
//(2)���Ը���ϵͳ�ĳ�������������ص����̳߳����̵߳���Ŀ����ֹ��Ϊ���Ĺ���ϵͳ��Դ������ϵͳ������

public class MultiEchoServer2 {

	private int port = 9999;
	private ServerSocket serverSocket;
	private ExecutorService executorService; //�̳߳�
	private final int POOL_SIZE = 4; //����CPUʱ�̳߳��й�����
	
	public MultiEchoServer2() throws IOException{
		serverSocket = new ServerSocket(port);
		//Runtime.getRuntime().availableProcessors()��ȡϵͳ����ʱcpu����
		executorService = Executors.newFixedThreadPool(POOL_SIZE * Runtime.getRuntime().availableProcessors());
		System.out.println("����������");
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
				System.out.println("�̣߳�"+Thread.currentThread().getName()+":"+msg);
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
