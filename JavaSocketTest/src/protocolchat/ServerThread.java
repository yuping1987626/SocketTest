package protocolchat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class ServerThread implements Runnable{
	
	private Socket socket ;
	private BufferedReader br = null;
	private PrintStream ps = null;
	
	
	public ServerThread(Socket socket){
		this.socket = socket;
	}
	

	@Override
	public void run() {
		
		try {
			//获取socket对应的输入流
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			//获取socket对应的输出流
			ps = new PrintStream(socket.getOutputStream());
			String line = null;
			while((line = br.readLine()) != null){
				System.out.println("line:"+line);
				if(line.startsWith(CrazyProtocol.USER_ROUND) && line.endsWith(CrazyProtocol.USER_ROUND)){
					//获取用户名
					String userName = getRealMsg(line);
					if(Server.clients.containsKey(userName)){
						System.out.println(userName+"用户名重复");
						ps.println(CrazyProtocol.NAME_REP);
						ps.flush();
					}else{
						System.out.println(userName+"登陆成功");
						ps.println(CrazyProtocol.LOGIN_SUCCESS);
						//ps.flush();
						Server.clients.put(userName, ps);
						System.out.println("新加入一个用户，Map大小目前为："+Server.clients.size());
					}
				}else if(line.startsWith(CrazyProtocol.PRIVATE_ROUND) && line.endsWith(CrazyProtocol.PRIVATE_ROUND)){
					String userAndMsg = getRealMsg(line);
					System.out.println(userAndMsg);
					String user = userAndMsg.split(CrazyProtocol.SPILT_SIGN)[0];
					System.out.println("用户名："+user);
					String msg = userAndMsg.split(CrazyProtocol.SPILT_SIGN)[1];
					System.out.println("消息是："+msg);
					System.out.println(Server.clients.get(user));;
					Server.clients.get(user).println(Server.clients.getKeyByValue(ps)+"悄悄对你说"+msg);
					//Server.clients.get(user).flush();
				}else{
					//得到真实的消息
					
					String msg = getRealMsg(line);
					System.out.println("公共信息"+msg);
					int i=0;
					System.out.println("Map大小："+Server.clients.size());
					for(PrintStream clientPS : Server.clients.valueSet()){
						System.out.println(i++);
						System.out.println("用户名是="+Server.clients.getKeyByValue(ps));
						clientPS.println(Server.clients.getKeyByValue(ps)+"对大家说:"+msg);
						clientPS.flush();
					}
				}
			}
		} catch (IOException e) {
			Server.clients.removeByValue(ps);
			System.out.println(Server.clients.size());
			try {
				if(br != null){
					br.close();
				}
				if(ps != null){
					ps.close();
				}
				if(socket != null){
					socket.close();
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}
	
	public String getRealMsg(String line){
		return line.substring(CrazyProtocol.PROTOCOL_LEN, line.length()-CrazyProtocol.PROTOCOL_LEN);
	}

}
