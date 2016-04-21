package session;

import javax.ejb.Local;
import javax.ejb.Remote;

import model.User;

@Remote
public interface UserBeanLocal {

	public String login(User u);
	
	public String register(User u);
	
	public String getMsg(String m);
}
