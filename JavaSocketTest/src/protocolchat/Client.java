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
			//��ʼ������������
			keyIn = new BufferedReader(new InputStreamReader(System.in));
			socket = new Socket("127.0.0.1",SERVER_PORT);
			ps = new PrintStream(socket.getOutputStream());
			brServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			        
			String tip = "";
			while(true){
				String userName = JOptionPane.showInputDialog(tip+"�����û�����");
				//���û���ǰ������Э���ַ�������
				ps.println(CrazyProtocol.USER_ROUND + userName +CrazyProtocol.USER_ROUND);
				ps.flush();
				String result = brServer.readLine();
				//����û����ظ�����ʼ�´�ѭ��
				if(result.equals(CrazyProtocol.NAME_REP)){
					tip = "�û����ظ�������������";
					continue;
				}
				//������������سɹ��������ѭ��
				if(result.equals(CrazyProtocol.LOGIN_SUCCESS)){
					break;
				}
			}
			new ClientThread(brServer).start();
			
		} catch (UnknownHostException e) {
			System.out.println("�Ҳ���Զ�̷���������ȷ���������Ƿ�����������");
			closeRs();
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("�����쳣�������µ�½������");
			closeRs();
			e.printStackTrace();
		}
		
	}
	
	//��ȡ�������
	private void readAndSend(){
		
		try {
			String line = null;
			while((line = keyIn.readLine()) != null){
				//������͵���Ϣ����ð������"/"��ͷ
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
			System.out.println("�����쳣�������µ�½������");
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
