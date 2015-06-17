package protocolchat;

import java.io.BufferedReader;
import java.io.IOException;

public class ClientThread extends Thread{
	
	//该客户端处理线程负责处理的输入流
	BufferedReader br = null;
	public ClientThread(BufferedReader br){
		this.br = br;
	}

	public void run(){
		
		try {
			String line = null;
			System.out.println(br.readLine());
			while((line=br.readLine())!= null){
				System.out.println(line);}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				if(br != null){
					br.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
