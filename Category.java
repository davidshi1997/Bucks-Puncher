
import java.util.*;

public class Category {

	String name;
	ArrayList<Item> items;
	
	public Category( String s )
	{
		name = s;
		items = new ArrayList<Item>();
	}
	
	public void addItem( Item item )
	{
		items.add( item );
	}
	
	public ArrayList<Item> getItems()
	{
		return items;
	}
	
	public String toString()
	{
		return name;
	}
	
}
