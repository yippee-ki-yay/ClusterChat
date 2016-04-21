package session;

import java.util.ArrayList;
import java.io.File;
import java.io.IOException;

import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Response;

import model.User;
import model.UserData;

/**
 * Session Bean implementation class UserBean
 */
@Stateless
@LocalBean
@Path("/users")
public class UserBean implements UserBeanLocal {
	
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
	@Produces(MediaType.TEXT_PLAIN)
	@Override
	public String login(User u) 
	{
		
		System.out.println("Ima: " + UserData.getInstance().userList.size());
		
		for(User currUser : UserData.getInstance().userList)
		{
			if(currUser.getUsername().equals(u.getUsername()) && 
				currUser.getPassword().equals(u.getPassword()))
			{
				return "true";
			}
		}
		
		return "false";
	}

	@POST
	@Path("/register")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	@Override
	public String register(User u) 
	{
		//if the user is already in the list we can't register
		for(User curr : UserData.getInstance().userList)
		{
			if(curr.getUsername().equals(u.getUsername()))
			{
				return "false";
			}
		}
		
		System.out.println("U registri: " + u.getUsername());
		
		UserData.getInstance().userList.add(u);
		
		System.out.println("Ima: " + UserData.getInstance().userList.size());
		
		return "true";
	}

   
}
