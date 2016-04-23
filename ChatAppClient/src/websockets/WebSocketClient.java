package websockets;

import java.io.IOException;
import java.net.URI;
import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

@ClientEndpoint
public class WebSocketClient {
   private Session session;

   public WebSocketClient(String uri){
	   System.out.println(uri);
      try{
         WebSocketContainer container=ContainerProvider.
            getWebSocketContainer();
         container.connectToServer(this, new URI(uri));

      }catch(Exception ex){

      }
   }

   @OnOpen
   public void onOpen(Session session){
      this.session=session;
   }

   @OnMessage
   public void onMessage(String message, Session session){
     // clientWindow.writeServerMessage(message);
	   System.out.println(message);
   }

   public void sendMessage(String message){
      try {
         session.getBasicRemote().sendText(message);
      } catch (IOException ex) {

      }
   }
}