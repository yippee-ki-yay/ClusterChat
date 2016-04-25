package session;

import javax.ejb.Remote;
import javax.ws.rs.core.Response;

import model.User;

@Remote
public interface UserBeanLocal {

	public String login(User u);
	
	public String register(User u);
	
	public String getMsg(String m);
	
	public Response listUsers();
	
	public String logout(String name);
}
