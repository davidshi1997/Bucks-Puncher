
import java.util.*;

public class Item {

	String name;
	ArrayList<Brand> brands;
	
	public Item( String s )
	{
		name = s;
		brands = new ArrayList<Brand>();
	}
	
	public void addBrand( Brand brand )
	{
		brands.add( brand );
	}
	
	public ArrayList<Brand> getBrands()
	{
		return brands;
	}
	
	public String toString()
	{
		return name;
	}
	
}
