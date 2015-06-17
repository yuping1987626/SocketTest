package protocolchat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

public class Client {
	
	private static final int SERVER_PORT = 9999;
	private Socket socket;
	private PrintStream ps;
	private BufferedReader brServer;
	private BufferedReader keyIn;
	
	public static void main(String[] args) {
		Client client = new Client();
		client.init();
		
		client.readAndSend();
	}
	
	public void init(){
		
		try {
			//初始化键盘输入流
			keyIn = new BufferedReader(new InputStreamReader(System.in));
			socket = new Socket("127.0.0.1",SERVER_PORT);
			ps = new PrintStream(socket.getOutputStream());
			brServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			        
			String tip = "";
			while(true){
				String userName = JOptionPane.showInputDialog(tip+"输入用户名：");
				//在用户名前后增加协议字符串后发送
				ps.println(CrazyProtocol.USER_ROUND + userName +CrazyProtocol.USER_ROUND);
				ps.flush();
				String result = brServer.readLine();
				//如果用户名重复，则开始下次循环
				if(result.equals(CrazyProtocol.NAME_REP)){
					tip = "用户名重复，请重新输入";
					continue;
				}
				//如果服务器返回成功，则结束循环
				if(result.equals(CrazyProtocol.LOGIN_SUCCESS)){
					break;
				}
			}
			new ClientThread(brServer).start();
			
		} catch (UnknownHostException e) {
			System.out.println("找不到远程服务器，请确定服务器是否启动！！！");
			closeRs();
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("网络异常，请重新登陆！！！");
			closeRs();
			e.printStackTrace();
		}
		
	}
	
	//读取键盘输出
	private void readAndSend(){
		
		try {
			String line = null;
			while((line = keyIn.readLine()) != null){
				//如果发送的信息中有冒号且以"/"开头
				if(line.indexOf(":")>0 && line.startsWith("/")){
					String lines = line.substring(1);
					System.out.println(line);
					System.out.println(lines);
					ps.println(CrazyProtocol.PRIVATE_ROUND + lines.split(":")[0]+
							CrazyProtocol.SPILT_SIGN + lines.split(":")[1] + CrazyProtocol.PRIVATE_ROUND);
					ps.flush();
				}else{
					ps.println(CrazyProtocol.MSG_ROUND + line + CrazyProtocol.MSG_ROUND);
					ps.flush();
				}
			}
		} catch (IOException e) {
			System.out.println("网络异常，请重新登陆！！！");
			closeRs();
			e.printStackTrace();
		}
	}
	
	private void closeRs(){
		try {
			if(keyIn != null){
				keyIn.close();
			}
			if(brServer != null){
				brServer.close();
			}
			if(ps != null){
				ps.close();
			}
			if(socket != null){
				socket.close();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
}
