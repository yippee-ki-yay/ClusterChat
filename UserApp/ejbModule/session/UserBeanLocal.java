package session;

import java.util.ArrayList;

import javax.ejb.Remote;

import model.User;

@Remote
public interface UserBeanLocal {

	public String login(User u);
	
	public String register(User u);
	
	public String getMsg(String m);
	
	public ArrayList<User> listUsers();
	
	public String logout(User u);
}
