package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient {
	
	
	

	public static void main(String[] args) throws IOException {
		Socket s = new Socket("127.0.0.1", 10099);
		new Thread(new ClientThread(s)).start();
		
		PrintWriter out = new PrintWriter(s.getOutputStream());
		String line = null;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while((line = br.readLine()) != null){
			out.println(line);
			out.flush();
		}
		
	}
	
}

class ClientThread implements Runnable{
	
	private Socket socket;
	private BufferedReader in;
	
	public ClientThread(Socket socket) throws IOException{
		this.socket = socket;
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		String content = null;
		try {
			while((content=in.readLine())!= null){
				System.out.println(content);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
