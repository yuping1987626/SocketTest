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
			//��ȡsocket��Ӧ��������
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			//��ȡsocket��Ӧ�������
			ps = new PrintStream(socket.getOutputStream());
			String line = null;
			while((line = br.readLine()) != null){
				System.out.println("line:"+line);
				if(line.startsWith(CrazyProtocol.USER_ROUND) && line.endsWith(CrazyProtocol.USER_ROUND)){
					//��ȡ�û���
					String userName = getRealMsg(line);
					if(Server.clients.containsKey(userName)){
						System.out.println(userName+"�û����ظ�");
						ps.println(CrazyProtocol.NAME_REP);
						ps.flush();
					}else{
						System.out.println(userName+"��½�ɹ�");
						ps.println(CrazyProtocol.LOGIN_SUCCESS);
						//ps.flush();
						Server.clients.put(userName, ps);
						System.out.println("�¼���һ���û���Map��СĿǰΪ��"+Server.clients.size());
					}
				}else if(line.startsWith(CrazyProtocol.PRIVATE_ROUND) && line.endsWith(CrazyProtocol.PRIVATE_ROUND)){
					String userAndMsg = getRealMsg(line);
					System.out.println(userAndMsg);
					String user = userAndMsg.split(CrazyProtocol.SPILT_SIGN)[0];
					System.out.println("�û�����"+user);
					String msg = userAndMsg.split(CrazyProtocol.SPILT_SIGN)[1];
					System.out.println("��Ϣ�ǣ�"+msg);
					System.out.println(Server.clients.get(user));;
					Server.clients.get(user).println(Server.clients.getKeyByValue(ps)+"���Ķ���˵"+msg);
					//Server.clients.get(user).flush();
				}else{
					//�õ���ʵ����Ϣ
					
					String msg = getRealMsg(line);
					System.out.println("������Ϣ"+msg);
					int i=0;
					System.out.println("Map��С��"+Server.clients.size());
					for(PrintStream clientPS : Server.clients.valueSet()){
						System.out.println(i++);
						System.out.println("�û�����="+Server.clients.getKeyByValue(ps));
						clientPS.println(Server.clients.getKeyByValue(ps)+"�Դ��˵:"+msg);
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
