package edu.gatech.calc.eulerMethod;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * The Class Main.
 * 
 * Takes in some vector field F, a time step h, a total time t, and a start
 * point x(0). Then generates graph up until n*h >= t, multiplies h by 1/10, and
 * does that 2 more times.
 */
@SuppressWarnings("serial")
public class Main extends JFrame implements ActionListener {

	/** The width of the application. */
	private int width;
	/** The height of the application. */
	private int height;

	/** The origin x location of the graph. */
	private int x0;
	/** The origin y location of the graph. */
	private int y0;

	// JFrame Components.
	/** The main panel to hold smaller panels. */
	private JPanel mainPanel;
	/** The panel for the graph to be drawn on */
	private JPanel paintPanel;
	/** The panel to hold a start button under the graph */
	private JPanel buttonPanel;
	/** Opens up JOptionPanes to set parameters for drawing the graph */
	private JButton start;

	// Parameters for drawing the graph
	/** Multiplier for the graph so we can see better */
	private final int GRAPH_SIZE = 90;
	/** The vector field F to be drawn */
	Function F;
	/** The start point for drawing. */
	Point startP;
	/** The time step. Smaller h means more accurate graph. */
	double h;
	/** The final time to be drawn. n*h should be smaller then this */
	double t;

	// Points lists
	/** The graph with highest time step */
	List<Point> graph1 = new LinkedList<Point>();
	/** The graph with middle time step */
	List<Point> graph2 = new LinkedList<Point>();
	/** The graph with smallest time step. */
	List<Point> graph3 = new LinkedList<Point>();

	/**
	 * Instantiates a new main.
	 */
	public Main() {
		// JFrame setups
		width = 800;
		height = 800;
		setSize(width, height);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// More setup
		F = new Function(null, null);
		startP = new Point(0, 0);

		setup();

		repaint();

		setVisible(true);

		// Defining where I want the origin of the graph to be.
		x0 = 400;
		y0 = 350;
	}

	/**
	 * Setup of the JPanels, Buttons, etc using a GridBagLayout.
	 */
	public void setup() {
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		paintPanel = new JPanel();
		paintPanel.setSize(width, height - 50);
		buttonPanel = new JPanel();

		start = new JButton("Start");
		start.addActionListener(this);
		buttonPanel.add(start);

		c.gridx = 1;
		c.gridy = 1;
		c.ipadx = width;
		c.ipady = height - 50;
		mainPanel.add(paintPanel, c);
		c.gridy = 2;
		c.ipady = 50;
		mainPanel.add(buttonPanel, c);

		add(mainPanel);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Container#paint(java.awt.Graphics)
	 */
	public void paint(Graphics g) {
		super.paint(g);

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, paintPanel.getWidth(), paintPanel.getHeight());
		g.setColor(Color.LIGHT_GRAY);
		g.drawLine(x0, 0, x0, paintPanel.getHeight());
		g.drawLine(0, y0, paintPanel.getWidth(), y0);

		paintGraph(g, graph1, Color.GREEN);
		paintGraph(g, graph2, Color.CYAN);
		paintGraph(g, graph3, Color.ORANGE);
	}

	/**
	 * Paints a line from a list of points given.
	 * 
	 * @param g
	 *            the Graphics object
	 * @param list
	 *            the list of points to draw
	 * @param c
	 *            the color
	 */
	public void paintGraph(Graphics g, List<Point> list, Color c) {
		Point temp;
		Point temp2;
		g.setColor(c);

		for (int i = 0; i < list.size() - 1; i++) {
			temp = list.get(i);
			temp2 = list.get(i + 1);
			g.drawLine((int) (x0 + temp.getX() * GRAPH_SIZE),
					(int) (y0 - temp.getY() * GRAPH_SIZE),
					(int) (x0 + temp2.getX() * GRAPH_SIZE),
					(int) (y0 - temp2.getY() * GRAPH_SIZE));
		}
	}

	/**
	 * Euler Method. Used to approximate the line given by some vector filed F.
	 * x(n*h) = x((n-1)h) + hF(x((n-1)h)) Start with x(0) and keep going until
	 * satisfied.
	 * 
	 * @param list
	 *            the list
	 */
	private void euler(List<Point> list) {
		double runningT = h;
		list.add(startP);
		Point temp = startP;

		while (runningT < t) {
			temp = temp.add(F.evalF(temp.getX(), temp.getY()).mult(h));
			System.out.println("Point at h = " + runningT + " is (" + temp.getX() + "," + temp.getY() + ")");
			list.add(temp);
			runningT += h;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		//reset the graphs
		graph1.clear();
		graph2.clear();
		graph2.clear();
		
		//Get necessary info to draw the graphs
		F.setF(JOptionPane
				.showInputDialog("Please enter f(x, y) here. Input as you would when coding with Java. For Example: 5(x^2) should be 5*(Math.pow(x, 2))"));
		F.setG(JOptionPane
				.showInputDialog("Please enter g(x, y) here. Input as you would when coding with Java. For Example: 5(x^2) should be 5*(Math.pow(x, 2))"));
		h = Double.parseDouble(JOptionPane
				.showInputDialog("Please enter h here."));
		t = Double.parseDouble(JOptionPane
				.showInputDialog("Please enter t here."));
		startP.setX(Double.parseDouble(JOptionPane
				.showInputDialog("Please enter starting x location")));
		startP.setY(Double.parseDouble(JOptionPane
				.showInputDialog("Please enter starting y location")));
		
		//Run euler's and generate points to be drawn.
		euler(graph1);
		h = h * .1;
		euler(graph2);
		h = h * .1;
		euler(graph3);
		
		//Draw the graph
		repaint();

	}

	/**
	 * Create the Application.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		new Main();
	}
}
