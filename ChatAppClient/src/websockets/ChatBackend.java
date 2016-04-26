package websockets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

	HashMap<String, Session> sessionsMap = new HashMap<String, Session>();

	public ChatBackend() 
	{
	}
		
	@OnOpen
	public void onOpen(Session session) {
			
			log.info("Dodao sesiju: " + session.getId() + " u endpoint-u: " + this.hashCode() + ", ukupno sesija: " + sessionsMap.size());
	}
	
	@OnMessage
	public void echoTextMessage(Session session, String msg, boolean last) {
		try {
			if (session.isOpen()) {
				
				System.out.println("Msg got:  " + msg);
				
				boolean broadcasted = false;
				
				//if this is a broadcasted msg don't send to others
				if(msg.substring(msg.length()-1).equals("b"))
				{
					msg = msg.substring(0,msg.length()-1);
					broadcasted = true;
				}
				
				
				
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
						
						if(userTo == null)
						{
							System.out.println("Nemam ovog usera na naski server");
							
							if(broadcasted == false)
							{
								for(Host h : Nodes.getInstance().nodes)
								{
									session.getBasicRemote().sendText(msg, last);
									System.out.println("Sent request to another node");
									sendToNodes("ws://" + h.getAddress() + ":8080/ChatAppClient/websocket", msg, session);
								}
							}
							
						}
						else
						{
							userTo.getBasicRemote().sendText(msg, last);
							session.getBasicRemote().sendText(msg, last);
							System.out.println("saljemo samo privatnu");
						}
						
						return;
					}
					else if(parts[0].equals("{\"name\"")) //register user session <--> username
					{
						String user = parts[1].split(",")[0].replaceAll("\"", "");
						System.out.println("Registrrr: " + user);
						
						sessionsMap.put(user, session);
						
					}
					else if(parts[0].equals("{\"remove\""))
					{
						String user = parts[1].split(",")[0].replaceAll("\"", "");
						System.out.println("Removee: " + user);
						sessionsMap.remove(user);
					}
						
				}
				
				
				if(broadcasted == false)
				{
					for(Host h : Nodes.getInstance().nodes)
					{
						System.out.println("Sent request to another node");
						sendToNodes("ws://" + h.getAddress() + ":8080/ChatAppClient/websocket", msg, session);
					}
				}
			
				for (Map.Entry<String, Session> entry : sessionsMap.entrySet())
				{
					System.out.println(entry.getKey());
					entry.getValue().getBasicRemote().sendText(msg, last);
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
	
	public void sendToNodes(String addr, String message, Session currSession)
	{
		WebSocketClient c = new WebSocketClient(addr, currSession);
		c.sendMessage(message + "b");
	}

	@OnClose
	public void close(Session session) {
		for (Map.Entry<String, Session> entry : sessionsMap.entrySet())
		{
			if(entry.getValue().getId().equals(session.getId()))
			{
				sessionsMap.remove(entry);
				 return;
			}
		   
		}
		log.info("Zatvorio: " + session.getId() + " u endpoint-u: " + this.hashCode());
	}
	
	@OnError
	public void error(Session session, Throwable t) {
		for (Map.Entry<String, Session> entry : sessionsMap.entrySet())
		{
			if(entry.getValue().getId().equals(session.getId()))
			{
				sessionsMap.remove(entry);
				 return;
			}
		   
		}
		log.log(Level.SEVERE, "Gre�ka u sessiji: " + session.getId() + " u endpoint-u: " + this.hashCode() + ", tekst: " + t.getMessage());
		t.printStackTrace();
	}
}