package session;

import java.util.ArrayList;

import javax.ejb.Remote;

import model.Host;
import model.User;

@Remote
public interface ChatRemote 
{
	public ArrayList<Host> registerHost(Host h);
	
	public String login(User u);
	
	public String logout(String name);
	
	public String register(User u);
	
	public String himaster();
}