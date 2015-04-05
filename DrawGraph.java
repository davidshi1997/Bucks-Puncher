import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.*;

import javax.swing.*;

import java.io.*;

@SuppressWarnings("serial")
public class DrawGraph extends JPanel {
	Random rand = new Random();
	private int PREF_W = 1200;
	private int PREF_H = 600;
	private int BORDER_GAP = 25;
	private int BORDER = 0;
	private int LEFT_SHIFT = 20;
	private int DOWN_SHIFT = 50;
	private Color GRID_COLOR = new Color(200, 200, 200, 200);
	private Color GRAPH_COLOR1 = Color.green;
	private Color GRAPH_COLOR2 = Color.red;
	private Color GRAPH_COLOR3 = Color.blue;
	private Color GRAPH_POINT_COLOR1 = new Color(150, 50, 50, 180);
	private static final Stroke GRAPH_STROKE = new BasicStroke(2f);
	private int GRAPH_POINT_WIDTH = 4;
	private int Y_HATCH_CNT = 10;
	private List<Double> prices;

	JLabel titleJLabel;

	JPanel userJPanel;

	JLabel lowestPriceJLabel;
	JLabel currentPriceJLabel;

	JButton switchJButton;
	ArrayList<Store> stores;
	int index = 0;
	
	// specify display format
	DecimalFormat dollars = new DecimalFormat( "$0.00" );

	public DrawGraph()
	{

	}

	public DrawGraph(List<Double> prices) {
		this.prices = prices;
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(PREF_W, PREF_H);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		double xScale = ((double) getWidth() - 2 * BORDER_GAP - BORDER - LEFT_SHIFT) / (365 - 1);
		double yScale = ((double) getHeight() - 2 * BORDER_GAP - BORDER - DOWN_SHIFT) / (getMaxPrice() - getMinPrice());

		List<Point> graphPoints = new ArrayList<Point>();
		int j = 0;
		for (int i = 0; i < prices.size(); i++) {
			int x1 = (int) (j * xScale + BORDER + BORDER_GAP);
			j++;
			if (j == 365) {
				j = 0;
			}
			int y1 = (int) ((getMaxPrice() - prices.get(i)) * yScale + BORDER_GAP + DOWN_SHIFT);
			graphPoints.add(new Point(x1, y1));
		}

		// create x and y axes 
		g2.drawLine(BORDER_GAP, getHeight() - BORDER_GAP, BORDER_GAP, BORDER_GAP + DOWN_SHIFT);
		g2.drawLine(BORDER_GAP, getHeight() - BORDER_GAP, getWidth() - BORDER_GAP - LEFT_SHIFT, getHeight() - BORDER_GAP);

		// create hatch marks for y axis. 
		for (int i = 0; i < Y_HATCH_CNT; i++) {
			int x0 = BORDER_GAP;
			int x1 = getWidth() - BORDER_GAP - LEFT_SHIFT;
			int y0 = getHeight() - (((i + 1) * (getHeight() - BORDER_GAP * 2 - DOWN_SHIFT)) / Y_HATCH_CNT + BORDER_GAP);
			int y1 = y0;
			if (prices.size() > 0) {
				g2.setColor(GRID_COLOR);
				g2.drawLine(x0, y0, x1, y1);
				g2.setColor(Color.BLACK);
				String yLabel = ((int) ((getMinPrice() + (getMaxPrice() - getMinPrice()) * ((i * 1.0) / Y_HATCH_CNT)) * 100)) / 100.0 + "";
				FontMetrics metrics = g2.getFontMetrics();
				int labelWidth = metrics.stringWidth(yLabel);
				g2.drawString(yLabel, x0 - labelWidth - 5, y0 + (metrics.getHeight() / 2) - 3);
			}
		}

		// and for x axis
		for (int i = 0; i < 365; i++) {
			int x0 = (i + 1) * (getWidth() - BORDER_GAP * 2 - LEFT_SHIFT) / (365 - 1) + BORDER_GAP;
			int x1 = x0;
			int y0 = getHeight() - BORDER_GAP;
			int y1 = BORDER_GAP + DOWN_SHIFT;

			if ((i % ((int) ((365 / 20)) + 1)) == 0) {
				g2.setColor(GRID_COLOR);
				g2.drawLine(x0, y0, x1, y1);
				g2.setColor(Color.BLACK);
				String xLabel = i + "";
				FontMetrics metrics = g2.getFontMetrics();
				int labelWidth = metrics.stringWidth(xLabel);
				g2.drawString(xLabel, x0 - labelWidth / 2, y0 + metrics.getHeight() + 3);
			}
		}

		
		//Making strokes for line 1
		Stroke oldStroke = g2.getStroke();
		g2.setColor(GRAPH_COLOR1);
		g2.setStroke(GRAPH_STROKE);
		for (int i = 0; i < 364; i++) {
			int x1 = graphPoints.get(i).x;
			int y1 = graphPoints.get(i).y;
			int x2 = graphPoints.get(i + 1).x;
			int y2 = graphPoints.get(i + 1).y;
			g2.drawLine(x1, y1, x2, y2);         
		}

		//Making Points
		g2.setStroke(oldStroke);      
		g2.setColor(GRAPH_COLOR1);

		for (int i = 0; i < 365; i++) {
			int x = graphPoints.get(i).x - GRAPH_POINT_WIDTH / 2;
			int y = graphPoints.get(i).y - GRAPH_POINT_WIDTH / 2;
			int ovalW = GRAPH_POINT_WIDTH;
			int ovalH = GRAPH_POINT_WIDTH;
			g2.fillOval(x, y, ovalW, ovalH);
		}

		for( int i = 0; i < 365; i++ )
		{
			graphPoints.remove(0);
		}

		
		// draw 2
		oldStroke = g2.getStroke();
		g2.setColor(GRAPH_COLOR2);
		g2.setStroke(GRAPH_STROKE);
		for (int i = 0; i < 364; i++) {
			int x1 = graphPoints.get(i).x;
			int y1 = graphPoints.get(i).y;
			int x2 = graphPoints.get(i + 1).x;
			int y2 = graphPoints.get(i + 1).y;
			g2.drawLine(x1, y1, x2, y2);         
		}

		g2.setStroke(oldStroke);      
		g2.setColor(GRAPH_COLOR2);

		for (int i = 0; i < 365; i++) {
			int x = graphPoints.get(0).x - GRAPH_POINT_WIDTH / 2;
			int y = graphPoints.get(0).y - GRAPH_POINT_WIDTH / 2;
			int ovalW = GRAPH_POINT_WIDTH;
			int ovalH = GRAPH_POINT_WIDTH;
			g2.fillOval(x, y, ovalW, ovalH);
			graphPoints.remove(0);
		}

		// draw 3
		oldStroke = g2.getStroke();
		g2.setColor(GRAPH_COLOR3);
		g2.setStroke(GRAPH_STROKE);
		for (int i = 0; i < 364; i++) {
			int x1 = graphPoints.get(i).x;
			int y1 = graphPoints.get(i).y;
			int x2 = graphPoints.get(i + 1).x;
			int y2 = graphPoints.get(i + 1).y;
			g2.drawLine(x1, y1, x2, y2);         
		}

		g2.setStroke(oldStroke);      
		g2.setColor(GRAPH_COLOR3);
		for (int i = 0; i < 365; i++) {
			int x = graphPoints.get(0).x - GRAPH_POINT_WIDTH / 2;
			int y = graphPoints.get(0).y - GRAPH_POINT_WIDTH / 2;
			int ovalW = GRAPH_POINT_WIDTH;
			int ovalH = GRAPH_POINT_WIDTH;
			g2.fillOval(x, y, ovalW, ovalH);
			graphPoints.remove(0);
		}
	}


	private double getMinPrice() {
		double minPrice = Double.MAX_VALUE;
		for ( int i = 0; i < prices.size(); i++ ) {
			minPrice = Math.min(minPrice, prices.get(i) );
		}
		return minPrice;
	}

	private double getMinPrice( ArrayList<Double> joe ) {
		double minPrice = Double.MAX_VALUE;
		for ( int i = 0; i < joe.size(); i++ ) {
			minPrice = Math.min(minPrice, joe.get(i) );
		}
		return minPrice;
	}

	private double getMaxPrice( ArrayList<Double> joe ) {
		double maxPrice = Double.MIN_VALUE;
		for ( int i = 0; i < joe.size(); i++ ) {
			maxPrice = Math.max(maxPrice, joe.get(i) );
		}
		return maxPrice;
	}

	private double getMaxPrice() {
		double maxPrice = Double.MIN_VALUE;
		for ( int i = 0; i < prices.size(); i++ ) {
			maxPrice = Math.max(maxPrice, prices.get(i) );
		}
		return maxPrice;
	}

	public void setPrices(ArrayList<Double> prices) {
		this.prices = prices;
		invalidate();
		this.repaint();
	}

	public List<Double> getPrices() {
		return prices;
	}
	
	public void createAndShowGui(String item, String brand) {
		List<Double> prices = new ArrayList<Double>();
		stores = new ArrayList<Store>();
		DrawGraph mainPanel = null;
		Scanner input = null;
		try {
			input = new Scanner(new File("productlistyear.txt"));
		}
		catch (FileNotFoundException e) {
			System.out.println("File not found.");
		}

		while (input.hasNextLine()) {
			String nextToken = input.next();
			String nextToken2 = input.next();
			if (!nextToken.equalsIgnoreCase(item) || !nextToken2.equalsIgnoreCase(brand)) {
				input.nextLine();
			}
			else{
				// temp store
				ArrayList<Double> temp = new ArrayList<Double>();

				String name = input.next();
				while(input.hasNextDouble()) {
					double p = input.nextDouble();
					temp.add(p);
					prices.add(p);
				}
				stores.add( 
						new Store( name , getMinPrice(temp) , 
								getMaxPrice(temp) , temp.get(temp.size()-1) ) );
				mainPanel = new DrawGraph(prices);
				//dSystem.out.println("Drawn!");
			}
		}

		//mainPanel.setPreferredSize(new Dimension(PREF_W, PREF_H));
		mainPanel.setBounds(0, 0, PREF_W - 200, PREF_H - 100);


		//Container contentPane = getRootPane();
		JFrame frame = new JFrame("DrawGraph");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().add(mainPanel);
		frame.setBounds(0, 0, PREF_W, PREF_H);
		frame.setLayout(null);
		//frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true); 

		titleJLabel = new JLabel( brand );
		titleJLabel.setBounds(PREF_W-200, 0, 200, PREF_H);
		titleJLabel.setVisible(true);
		mainPanel.add(titleJLabel,BorderLayout.NORTH);

		/*
		userJPanel = new JPanel( new BorderLayout() );
		userJPanel.setBounds( PREF_W , 0 , 200 , PREF_H );
		userJPanel.setVisible(true);
		frame.add(userJPanel);
		 */

		lowestPriceJLabel = new JLabel();
		lowestPriceJLabel.setText( "Lowest Price: " + 
				dollars.format( mainPanel.getMinPrice() ) );
		lowestPriceJLabel.setVisible(true);
		lowestPriceJLabel.setBounds(PREF_W-200 , 100 , 150 , 50);
		frame.add(lowestPriceJLabel);

		currentPriceJLabel = new JLabel();
		currentPriceJLabel.setText( "" );
		currentPriceJLabel.setVisible(true);

		/*
		switchJButton = new JButton( "Switch Store" );
		switchJButton.setBounds(PREF_W-200, 50, 150, 30);
		switchJButton.setVisible(true);
		frame.add( switchJButton );
		switchJButton.addActionListener(
				new ActionListener()
				{
					public void actionPerformed( ActionEvent e )
					{
						switchJButtonActionPerformed( e );
					}
				}
				); */
	}

	public void switchJButtonActionPerformed( ActionEvent e )
	{
		index++;
		
		if( index == stores.size() )
		{
			index = -1;
			currentPriceJLabel.setText( "" );
		}
		else
		{
			currentPriceJLabel.setText( "Lowest Price: " + 
					dollars.format( stores.get(index).getMinPrice() ) );
		}

	}

	/*
	public static void main(String[] args) {
		String item = "Product5";
		String brand = "Coca-Cola";
		DrawGraph d = new DrawGraph();
		d.createAndShowGui(item, brand);

	} 
	*/
}
