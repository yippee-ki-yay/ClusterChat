package session;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import model.Host;
import model.Nodes;
import model.User;

@LocalBean
@Path("/users")
@Stateless
public class ChatBean implements ChatRemote
{
	@GET
	@Path("/test")
	@Produces(MediaType.TEXT_PLAIN)
	public String test() {
		
		
		return "OK";
	}
	
	@GET
	@Path("/getmsg/{msg}")
	@Produces(MediaType.TEXT_PLAIN)
	@Override
	public String getMsg(@PathParam("msg") String m) 
	{
		return "Hello " + m;
	}

	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String login(User u) 
	{
		//prosledimo ovo nasem user appu
		
		ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target("http://localhost:8080/UserAppClient/rest/users/login/");
        Response response = target.request().post(Entity.entity(new User(u.getUsername(), u.getPassword()), MediaType.APPLICATION_JSON));
        String ret = response.readEntity(String.class);
        System.out.println(ret);
		
        //prodjemo kroz sve nodove i svima posaljemo da imamo novog usera
        for(Host h : Nodes.getInstance().nodes)
        {
        	//add the bitches
        }
        
		return "sone";
	}

	@POST
	@Path("/register")
	@Produces(MediaType.TEXT_PLAIN)
	@Override
	public String register(User u) 
	{
		ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target("http://localhost:8080/UserAppClient/rest/users/register/");
        Response response = target.request().post(Entity.entity(new User(u.getUsername(), u.getPassword()), MediaType.APPLICATION_JSON));
        String ret = response.readEntity(String.class);
        System.out.println(ret);
		
		return ret;
	}

}
