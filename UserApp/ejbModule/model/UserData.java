package model;

import java.util.ArrayList;

public class UserData 
{
	private UserData(){}
	
	private static  UserData userData = new UserData();
	
	public ArrayList<User> userList = new ArrayList<User>();
	
	public ArrayList<User> currentUsers = new ArrayList<User>();
	
	public static UserData getInstance()
	{
		return userData;
	}
	
	
}
