package session;

import java.io.File;
import java.io.IOException;

import javax.ejb.LocalBean;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

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
		
		return ret;
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
