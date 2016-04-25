package session;

import java.util.ArrayList;

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

	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String login(User u) 
	{
		//prosledimo ovo nasem user appu
		
		ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target("http://" + Nodes.getInstance().masterAddr + ":8080/UserAppClient/rest/users/login/");
        Response response = target.request().post(Entity.entity(new User(u.getUsername(), u.getPassword()), MediaType.APPLICATION_JSON));
        String ret = response.readEntity(String.class);
        System.out.println(ret);
		
        //prodjemo kroz sve nodove i svima posaljemo da imamo novog usera
        for(Host h : Nodes.getInstance().nodes)
        {
        	//add the bitches
        }
        
		return ret;
	}

	@GET
	@Path("/himaster")
	@Produces(MediaType.TEXT_PLAIN)
	@Override
	public String himaster()
	{
		Nodes.getInstance().nodes.add(new Host(Nodes.getInstance().masterAddr, "master")); //dodali mastera
		
		ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target("http://" + Nodes.getInstance().masterAddr + ":8080/ChatAppClient/rest/users/registerHost/");
        Response response = target.request().post(Entity.entity(new Host("nasa addr", "komp2"), MediaType.APPLICATION_JSON));
		
		return "OK";
	}
	
	@POST
	@Path("/register")
	@Produces(MediaType.TEXT_PLAIN)
	@Override
	public String register(User u) 
	{
		ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target("http://" + Nodes.getInstance().masterAddr + ":8080/UserAppClient/rest/users/register/");
        Response response = target.request().post(Entity.entity(new User(u.getUsername(), u.getPassword()), MediaType.APPLICATION_JSON));
        String ret = response.readEntity(String.class);
        System.out.println(ret);
		
		return ret;
	}
	
	//gledamo ovo kao master cvor
		//ovo pozivaju drugi cvorevi mi dodamo kod sebe sta su nam poslali i vratimo listu svih cvoreva
		@POST
		@Path("/registerHost")
		@Consumes(MediaType.APPLICATION_JSON)
		@Produces(MediaType.APPLICATION_JSON)
		public ArrayList<Host> registerHost(Host h)
		{
			Nodes.getInstance().nodes.add(h);
			System.out.println("Pogodio register");
			//pozovemo od drugih register metodu da oni ubace kod sebe novog cvoras (OVO SAMO MASTER)
			/*for(Host n : Nodes.getInstance().nodes)
			{
				ResteasyClient client = new ResteasyClientBuilder().build();
		        ResteasyWebTarget target = client.target( n.getAddress() + "/ChatAppClient/rest/cluster/register/");
		        Response response = target.request().post(Entity.entity(h, MediaType.APPLICATION_JSON));
		        String ret = response.readEntity(String.class);
		        System.out.println(ret);
			}*/
			
			
			
			//isto samo MASTER
			//posaljemo listu cvorova onom ko se sad registrovao
			return Nodes.getInstance().nodes;
		}
		
		@GET
		@Path("/unregisterHost")
		@Consumes(MediaType.APPLICATION_JSON)
		@Produces(MediaType.APPLICATION_JSON)
		void unregisterHost(Host h)
		{
			Nodes.getInstance().nodes.remove(h);
			
			//posaljemo svima da se unregistrovao (OVO SAMO MASTER)
			for(Host currHost : Nodes.getInstance().nodes)
			{
				//opali rest client request
			}
		}

		@GET
		@Path("/logout/{name}")
		@Produces(MediaType.TEXT_PLAIN)
		@Override
		public String logout(@PathParam("name") String name) 
		{
			ResteasyClient client = new ResteasyClientBuilder().build();
	        ResteasyWebTarget target = client.target("http://" + Nodes.getInstance().masterAddr + ":8080/UserAppClient/rest/users/logout/" + name);
	        Response response = target.request().get();
	        String ret = response.readEntity(String.class);
	        System.out.println(ret);
			
			return ret;
		}

}
