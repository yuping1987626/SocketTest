package protocolchat;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	private static final int SERVER_PORT = 9999;
	
	public static CrazyMap<String, PrintStream> clients = new CrazyMap<>();
	
	public void init(){
		try(ServerSocket ss = new ServerSocket(SERVER_PORT)) {
			
			System.out.println("Server Start Success£¡£¡£¡");
			while(true){
				
				Socket socket = ss.accept();
				
				new Thread(new ServerThread(socket)).start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Server Start Failure");
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		Server server = new Server();
		server.init();
	}
}
