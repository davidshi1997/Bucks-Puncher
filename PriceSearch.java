
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import org.jgraph.*;
import org.jgrapht.*;

public class PriceSearch extends JFrame {

	// usable panel
	JButton addCategoryJButton;
	JButton addItemJButton;
	JButton addBrandJButton;

	JPanel userJPanel;

	JLabel categoryJLabel;
	JComboBox<Category> categoryJComboBox;

	JLabel itemJLabel;
	JComboBox<Item> itemJComboBox;

	JLabel brandJLabel;
	JComboBox<Brand> brandJComboBox;

	// textfields with graph 
	JTextField currentItemTypeJTextField;
	JTextField currentItemJTextField;
	JTextField currentBrandJTextField;

	// initialize variables
	ArrayList<Category> categories;

	// load and save JButtons
	JButton loadJButton;
	JButton saveJButton;
	
	public PriceSearch()
	{
		Container contentPane = getContentPane();
		contentPane.setLayout(null);

		// setup predetermined sets
		setVars();

		// initialize user panel and components
		userJPanel = new JPanel();
		userJPanel.setBounds(20, 20, 540, 200);
		userJPanel.setBorder( new TitledBorder ( "User Control" ) );
		userJPanel.setLayout(null);
		contentPane.add( userJPanel );

		categoryJLabel = new JLabel();
		categoryJLabel.setBounds( 25 , 30 , 90 , 30 );
		categoryJLabel.setText("Item Category:");
		categoryJLabel.setVisible(true);
		userJPanel.add( categoryJLabel );

		categoryJComboBox = new JComboBox( categories.toArray() );
		categoryJComboBox.setBounds( 115 , 37 , 200 , 17 );
		categoryJComboBox.setVisible(true);
		userJPanel.add( categoryJComboBox );
		categoryJComboBox.addActionListener(
			new ActionListener()
			{
				public void actionPerformed( ActionEvent e )
				{
					if( categoryJComboBox.getSelectedItem() != null )
					{
						categoryJComboBoxActionPerformed( e );
					}
				}
			}
		);

		addCategoryJButton = new JButton();
		addCategoryJButton.setBounds( 340 , 37 , 150 , 17 );
		addCategoryJButton.setText( "Add Item Category" );
		addCategoryJButton.setVisible(true);
		userJPanel.add(addCategoryJButton);
		addCategoryJButton.addActionListener(
			new ActionListener()
			{
				public void actionPerformed( ActionEvent e )
				{
					addCategoryJButtonActionPerformed( e );
				}
			}
		);

		itemJLabel = new JLabel();
		itemJLabel.setBounds( 80 , 90 , 30 , 30 );
		itemJLabel.setText("Item:");
		itemJLabel.setVisible(true);
		userJPanel.add( itemJLabel );

		itemJComboBox = new JComboBox( ((Category)
				categoryJComboBox.getSelectedItem()).getItems().toArray() );
		itemJComboBox.setBounds( 115 , 97 , 200 , 17 );
		itemJComboBox.setVisible(true);
		userJPanel.add( itemJComboBox );
		itemJComboBox.addActionListener( 
			new ActionListener()
			{
				public void actionPerformed( ActionEvent e )
				{
					if( itemJComboBox.getSelectedItem() != null )
					{
						itemJComboBoxActionListener( e );
					}
				}
			}
		);

		addItemJButton = new JButton();
		addItemJButton.setBounds( 340 , 97 , 150 , 17 );
		addItemJButton.setText( "Add Item" );
		addItemJButton.setVisible(true);
		userJPanel.add( addItemJButton );
		addItemJButton.addActionListener(
			new ActionListener()
			{
				public void actionPerformed( ActionEvent e )
				{
					addItemJButtonActionListener( e );
				}
			}
		);

		brandJLabel = new JLabel();
		brandJLabel.setBounds( 70 , 145 , 40 , 30 );
		brandJLabel.setText("Brand:");
		brandJLabel.setVisible(true);
		userJPanel.add( brandJLabel );

		brandJComboBox = new JComboBox( ((Item)
				itemJComboBox.getSelectedItem()).getBrands().toArray() );
		brandJComboBox.setBounds( 115 , 152 , 200 , 17 );
		brandJComboBox.setVisible(true);
		userJPanel.add( brandJComboBox );
		brandJComboBox.addActionListener(
			new ActionListener()
				{
				public void actionPerformed( ActionEvent e )
				{
					if( brandJComboBox.getSelectedItem() != null )
					{
						brandJComboBoxActionPerformed( e );
					}

				}
			}
		);

		addBrandJButton = new JButton();
		addBrandJButton.setBounds( 340 , 152 , 150 , 17 );
		addBrandJButton.setText( "Add Brand" );
		addBrandJButton.setVisible(true);
		userJPanel.add( addBrandJButton );
		addBrandJButton.addActionListener(
			new ActionListener()
			{
				public void actionPerformed( ActionEvent e )
				{
					addBrandJButtonActionPerformed( e );
				}
			}
		);

		setTitle( "Price Search" ); // set title bar string
		setSize( 600 , 300 );        // set window size
		setVisible( true );         // display window
	}

	private void categoryJComboBoxActionPerformed(ActionEvent e) {
		Category cat = (Category) categoryJComboBox.getSelectedItem();
		updateItemJComboBox( cat );
	}

	private void addCategoryJButtonActionPerformed(ActionEvent e) {
		String s = JOptionPane.showInputDialog("Input Item Category");
		if( s != null )
		{
			Category cat = new Category( s );
			categories.add( cat );
			categoryJComboBox.addItem( cat );
			updateItemJComboBox( cat );
		}	
	}

	private void itemJComboBoxActionListener(ActionEvent e) {
		Item item = (Item) itemJComboBox.getSelectedItem();
		updateBrandJComboBox( item );
	}

	private void addItemJButtonActionListener(ActionEvent e) {
		if( categoryJComboBox.getSelectedItem() == null )
		{
			JOptionPane.showMessageDialog(null, 
					"No Category Selected", "Error", JOptionPane.ERROR_MESSAGE); 
		}
		else
		{
			String s = JOptionPane.showInputDialog("Input Item");
			if( s!= null )
			{
				Category cat = (Category) categoryJComboBox.getSelectedItem();
				Item item = new Item( s );
				cat.addItem( item );
				updateItemJComboBox( cat );
				updateBrandJComboBox( item );	
			}
		}
	}

	// Plotting Class
	private void brandJComboBoxActionPerformed(ActionEvent e) {
		DrawGraph d = new DrawGraph();
		d.createAndShowGui( 
				((Item) itemJComboBox.getSelectedItem()).
				toString().replaceAll("\\s","") , 
				((Brand) brandJComboBox.getSelectedItem()).
				toString().replaceAll("\\s","") );
				
	}

	// add a Brand
	private void addBrandJButtonActionPerformed(ActionEvent e) {
		// reject bogus input
		if( categoryJComboBox.getSelectedItem() == null )
		{
			JOptionPane.showMessageDialog(null, 
					"No Category Selected", "Error", JOptionPane.ERROR_MESSAGE); 
		}
		else if( itemJComboBox.getSelectedItem() == null )
		{
			JOptionPane.showMessageDialog(null, 
					"No Item Selected", "Error", JOptionPane.ERROR_MESSAGE); 
		}
		else // passed the test
		{
			String s = JOptionPane.showInputDialog("Input Brand");
			if( s != null )
			{
				Item item = (Item) itemJComboBox.getSelectedItem();
				Brand brand = new Brand( s );
				item.addBrand( brand );
				updateBrandJComboBox( item );
			}
		}
	}

	// update itemJComboBox
	private void updateItemJComboBox( Category category )
	{
		// prevent ActionListener
		itemJComboBox.setEnabled(false);
		brandJComboBox.setEnabled(false);
		// clear JComboBoxes
		itemJComboBox.removeAllItems();
		brandJComboBox.removeAllItems();
		// update ItemJComboBox
		Iterator<Item> items = category.getItems().iterator();
		while( items.hasNext() )
		{
			itemJComboBox.addItem(items.next());
		}

		// turn on ActionListener
		brandJComboBox.setEnabled(true);
		itemJComboBox.setEnabled(true);

	}

	// update brandJComboBox
	private void updateBrandJComboBox( Item item )
	{
		// prevent ActionListener
		brandJComboBox.setEnabled(false);
		brandJComboBox.removeAllItems();
		// update ItemJComboBox
		Iterator<Brand> brands = item.getBrands().iterator();
		while( brands.hasNext() )
		{
			brandJComboBox.addItem(brands.next());
		}

		// turn on ActionListener
		brandJComboBox.setEnabled(true);
	}

	// I Don't want to talk about this
	private void setVars()
	{
		categories = new ArrayList<Category>();
		categories.add( new Category( "Technology" ) );
		categories.add( new Category( "Food" ) );
		categories.add( new Category( "Printed Material" ) );
		for( int i = 1; i <= 33; i++ )
		{
			categories.get(0).addItem( new Item("Product" + i));
		}
		for( int i = 33; i <= 67; i++ )
		{
			categories.get(1).addItem( new Item("Product" + i));
		}
		for( int i = 68; i <= 100; i++ )
		{
			categories.get(2).addItem( new Item("Product" + i));
		}
		for( int j = 0; j < categories.size(); j++ )
		{
			for( int k = 0; k < categories.get(j).getItems().size(); k++ )
			{
				categories.get(j).getItems().get(k).addBrand( 
						new Brand("Coca-Cola") );
				categories.get(j).getItems().get(k).addBrand( new Brand("Kraft") );
				categories.get(j).getItems().get(k).addBrand( new Brand("Nestle") );
				categories.get(j).getItems().get(k).addBrand( new Brand("P&G") );
				categories.get(j).getItems().get(k).addBrand( 
						new Brand("Johnson and Johnson") );
				categories.get(j).getItems().get(k).addBrand( 
						new Brand("Unilever") );
				categories.get(j).getItems().get(k).addBrand( new Brand("MARS") );
				categories.get(j).getItems().get(k).addBrand(new Brand("Kellogg's"));
				categories.get(j).getItems().get(k).addBrand(
						new Brand("General Mills"));
				categories.get(j).getItems().get(k).addBrand(
						new Brand("Pepsico"));
			}
			for( int k = 0; k < categories.get(j).getItems().size(); k++ )
			{
				for( int i = 0; i < 
						categories.get(j).getItems().get(k).getBrands().size(); i++ )
				{
					categories.get(j).getItems().get(k).getBrands().get(i).addURL(
							new URL("Walmart"));
					categories.get(j).getItems().get(k).getBrands().get(i).addURL(
							new URL("Costco"));
					categories.get(j).getItems().get(k).getBrands().get(i).addURL(
							new URL("Amazon"));	
				}
			}
		}
	}

	public static void main(String[] args) {
		PriceSearch gui = new PriceSearch();
		gui.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

	}

}
