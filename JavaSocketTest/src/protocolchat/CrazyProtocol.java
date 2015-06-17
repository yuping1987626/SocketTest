package protocolchat;

public interface CrazyProtocol {

	int PROTOCOL_LEN = 2;
	//用户名标志
	String USER_ROUND = "&@";
	//私聊标志
	String PRIVATE_ROUND = "*《";
	//用户名重复标志
	String NAME_REP = "%?";
	//登陆成功标志
	String LOGIN_SUCCESS = "#!";
	//用户和消息分隔符
	String SPILT_SIGN = "~~";
	//公共消息
	String MSG_ROUND = "^^";
	
}
