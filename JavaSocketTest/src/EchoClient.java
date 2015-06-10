import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class EchoClient {

	private int port = 8000;
	private String host = "localhost";
	private Socket socket;
	
	public EchoClient() throws IOException{
		socket = new Socket(host, port);
	}
	
	private PrintWriter getWriter(Socket socket) throws IOException{
		OutputStream out = socket.getOutputStream();
		return new PrintWriter(out);
	}
	
	private BufferedReader getReader(Socket socket) throws IOException{
		InputStream in = socket.getInputStream();
		return new BufferedReader(new InputStreamReader(in));
	}
	
	public void talk(){
		try {
			PrintWriter writer = getWriter(socket);
			BufferedReader reader = getReader(socket);
			
			BufferedReader localReader=new BufferedReader(new InputStreamReader(System.in));
			//Scanner input = new Scanner(System.in);
			
			while(true){
				
				String msg = localReader.readLine(); 
				writer.println(msg);
				writer.flush();
				
				if(msg.equals("bye")){
					break;
				}
				
				System.out.println(reader.readLine());
					
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	public static void main(String[] args) throws IOException {
		new EchoClient().talk();
		
	}
}
