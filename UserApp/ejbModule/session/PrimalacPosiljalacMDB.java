package session;


import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;

import model.User;
import model.UserData;

/*
 * Prvo probati sa queue/mojQueue2. Posto u tom slucaju ovaj MDB 
 * ne "hvata" poruke iz JMSQueue aplikacije, queue se prazni preko 
 * nje. Ako i ovaj bean prima iz istog reda (mojQueue), onda
 * sama aplikacija nece ni stici da dobije poruku (MDB) ce je prvi
 * "pojesti"
 * 
 * Osim toga, probati i da JMSQueue ostane upaljena, a da se 
 * startuje jos jedna instanca iste aplikacije (dok MDB ne "hvata"
 * poruke). Videcemo da queue polako raste (sa svakim startovanjem
 * aplikacije).
 * 
 */


@MessageDriven(activationConfig =
{
  @ActivationConfigProperty(propertyName="destinationType",
    propertyValue="javax.jms.Queue"),
  @ActivationConfigProperty(propertyName="destination",
    propertyValue="queue/mojQueue")
})

public class PrimalacPosiljalacMDB implements MessageListener {

    @Resource(mappedName = "java:/ConnectionFactory")
	private ConnectionFactory connectionFactory;
	
  public void onMessage (Message msg) {
    try {
      TextMessage tmsg = (TextMessage) msg;
      try {
          String text = tmsg.getText();
          
          System.out.println(text);
          
          if(text.startsWith("login"))
          {
        	  String[] parts = text.split("/");
        	  boolean status = login(new User(parts[1], parts[2]));
        	  
        	  if(status)
        	  {
        		  sendResponse(msg, "true");
        	  }
        	  else
        	  {
        		  sendResponse(msg, "false");
        	  }
          }
          else if(text.equals("list"))
          {
        	 sendResponse(msg, "lista");
          }
          
      } catch (JMSException e) {
          e.printStackTrace();
      }
    } catch (Exception e) {
      e.printStackTrace ();
    }
  }
  
  private void sendResponse(Message msg, String sentMsg)
  {
	  Destination odKoga;
	try {
		odKoga = msg.getJMSReplyTo();
		 Topic topic = (Topic) odKoga;  
			
	      TopicConnection connection = (TopicConnection)connectionFactory.createConnection("guest", "guestguest");
	      TopicSession session = connection.createTopicSession(false,
	              Session.AUTO_ACKNOWLEDGE);
	      
	      TopicPublisher publisher = session.createPublisher(null);
	      // create and publish a message
	      TextMessage message = session.createTextMessage();
	      message.setText(sentMsg);
	      publisher.publish(message);
	     
	      System.out.println(">>>RequestResponse MDB: Reply sent.");
	      publisher.close();
	      connection.close();
	} catch (JMSException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }
  
  private boolean login(User u)
  {
	  for(User currUser : UserData.getInstance().userList)
		{
			if(currUser.getUsername().equals(u.getUsername()) && 
				currUser.getPassword().equals(u.getPassword()))
			{
				UserData.getInstance().currentUsers.add(u);
				return true;
			}
		}
		
		return false;
  }
  
  private boolean register(User u)
  {
	  return true;
  }
  
  private boolean logout(String username)
  {
	  return true;
  }
  
  private String list()
  {
	  return "";
  }

}