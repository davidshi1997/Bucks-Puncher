
import java.util.*;

public class Brand {

	String name;
	ArrayList<URL> urls;
	
	public Brand(String s) {
		name = s;
		urls = new ArrayList<URL>();
	}

	public void addURL( URL url )
	{
		urls.add(url);
	}
	
	public ArrayList<URL> getURLS()
	{
		return urls;
	}
	
	public String toString()
	{
		return name;
	}
	
}
