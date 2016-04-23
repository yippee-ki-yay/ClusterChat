package websockets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Singleton;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.*;

import com.sun.org.apache.xerces.internal.util.URI;

import model.Host;
import model.Nodes;

@ServerEndpoint("/websocket")
@Singleton
public class ChatBackend 
{
	Logger log = Logger.getLogger("Websockets endpoint");
	List<Session> sessions = new ArrayList<Session>();
	Timer t = new Timer();
	HashMap<String, Session> sessionsMap = new HashMap<String, Session>();

	public ChatBackend() 
	{
	}
		
	@OnOpen
	public void onOpen(Session session) {
		if (!sessions.contains(session)) {
			sessions.add(session);
			
			sessionsMap.put("", session);
			log.info("Dodao sesiju: " + session.getId() + " u endpoint-u: " + this.hashCode() + ", ukupno sesija: " + sessions.size());
		}
	}
	
	@OnMessage
	public void echoTextMessage(Session session, String msg, boolean last) {
		try {
			if (session.isOpen()) {
				
				System.out.println(Nodes.getInstance().nodes.size());
				String[] parts = msg.split(":");
				if(parts.length > 1)
				{
					//check to see if the msg if private
					if(parts[0].equals("{\"to\""))
					{
						//get the name and remove " from json
						String user = parts[1].split(",")[0].replaceAll("\"", "");
						
						System.out.println(session.getId());
						
						Session userTo = sessionsMap.get(user);
						userTo.getBasicRemote().sendText(msg, last);
						session.getBasicRemote().sendText(msg, last);
						System.out.println("saljemo samo privatnu");
						return;
					}
					else if(parts[0].equals("{\"name\"")) //register user session <--> username
					{
						String user = parts[1].split(",")[0].replaceAll("\"", "");
						System.out.println("Registrrr: " + user);
						sessionsMap.put(user, session);
						
						/*ResteasyClient client = new ResteasyClientBuilder().build();
				        ResteasyWebTarget target = client.target("http://192.168.1.100:8080/UserAppClient/rest/users/list/");
				        Response response = target.request().get();
				        String ret = response.readEntity(String.class);
				        System.out.println(ret);*/
					}
						
				}
				
				for(Host h : Nodes.getInstance().nodes)
				{
					
					sendToNodes("ws://10.42.0.88:8080/ChatAppClient/websocket", msg);
				}
				
				//posaljemo svim aktivnim sesijama na cvoru
				for (Session s : sessions) {
						s.getBasicRemote().sendText(msg, last);
						
				}
			}
		} catch (IOException e) {
			try {
				session.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public void sendToNodes(String addr, String message)
	{
		WebSocketClient c = new WebSocketClient(addr);
		c.sendMessage(message);
	}

	@OnClose
	public void close(Session session) {
		sessions.remove(session);
		log.info("Zatvorio: " + session.getId() + " u endpoint-u: " + this.hashCode());
	}
	
	@OnError
	public void error(Session session, Throwable t) {
		sessions.remove(session);
		log.log(Level.SEVERE, "Gre�ka u sessiji: " + session.getId() + " u endpoint-u: " + this.hashCode() + ", tekst: " + t.getMessage());
		t.printStackTrace();
	}
}
