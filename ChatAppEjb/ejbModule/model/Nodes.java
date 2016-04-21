package model;

import java.util.ArrayList;

public class Nodes 
{
	private Nodes() {}
	
	private static Nodes node = new Nodes();
	
	public ArrayList<Host> nodes = new ArrayList<Host>();
	
	public static Nodes getInstance()
	{
		return node;
	}
}
