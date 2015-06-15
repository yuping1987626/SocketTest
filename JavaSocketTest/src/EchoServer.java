import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

//一对一，单线程
public class EchoServer {

	private int port = 9999;
	private ServerSocket serverSocket;
	
	public EchoServer() throws IOException{
		serverSocket = new ServerSocket(port);
		System.out.println("服务器启动");
	}
	
	private PrintWriter getWriter(Socket socket) throws IOException{
		OutputStream out = socket.getOutputStream();
		return new PrintWriter(out, true);
	}
	
	private BufferedReader getReader(Socket socket) throws IOException {
		InputStream in = socket.getInputStream();
		return new BufferedReader(new InputStreamReader(in));
	}
	
	public String echo(String str){
		return "echo:" + str;
	}
	
	public void service(){
		while(true){
			Socket socket = null;
			try {
				socket = serverSocket.accept();
				System.out.println("New connection accepted " +socket.getInetAddress() + ":" +socket.getPort());
				BufferedReader reader = getReader(socket);
				PrintWriter writer = getWriter(socket);
				
				String msg = null;
				while((msg = reader.readLine())!=null){
					System.out.println(msg);
					writer.println(echo(msg));
					writer.flush();
					if(msg.equals("bye"))
						break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				try {
					if(socket!=null){
						socket.close();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void main(String[] args) throws IOException {
		new EchoServer().service();
	}
}
