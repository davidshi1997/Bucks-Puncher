
import java.util.*;

public class Store {
	Double minPrice;
	Double maxPrice;
	Double currentPrice;
	String name;
	
	public Store( String s , double min , double max , double current )
	{
		name = s;
		minPrice = min;
		maxPrice =  max;
		currentPrice = current;
	}
	
	public Double getMinPrice()
	{
		return minPrice;
	}
	
	public Double getMaxPrice()
	{
		return maxPrice;
	}
	
	public Double getCurrentPrice()
	{
		return currentPrice;
	}
	
	public String getName()
	{
		return name;
	}
	
}
