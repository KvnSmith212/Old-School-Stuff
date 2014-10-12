package edu.gatech.math2605.project3;

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
import javax.swing.JPanel;

import Jama.Matrix;

/**
 * The Class JacobiApp.
 * 
 * This class will present a graphical representation of the Jacobi Algorithm.
 * 10 Randomly generated Matrices will be created and diagonlized using the
 * Jacobi algorithm, and then graphed on a plane such that the x axis represents
 * the kth iteration of the algorith and the y axis represents bk, or the
 * magnitude of the matrix at that iteration.
 * 
 * In addition, the original matrix as well as the diagonalized matrix are
 * displayed and labeled.
 */
@SuppressWarnings("serial")
public class JacobiApp extends JFrame implements ActionListener {

	private int width;
	private int height;

	// x and y for the various points
	private int x;
	private int y;

	// x and y for the line y = xln(9/10) + ln(offA) where offA is the magnitude
	// of the original matrix
	private int lx;
	private int ly;

	// The origin point
	private int x0;
	private int y0;

	// JFrame Components.
	private JPanel mainPanel;
	private JPanel paintPanel;
	private JPanel buttonPanel;
	private JButton sorted;
	private JButton unSorted;

	// The magnitude of A
	private double offA;

	// A is the original matrix for the last run of the Jacobi Alg, and B will
	// be the diagonal matrix of the last run.
	private Matrix A;
	private Matrix B;

	// This is the list of points to be graphed.
	private static List<PartialPoint> points;

	/**
	 * Instantiates the Application
	 */
	public JacobiApp() {
		// JFrame setups
		width = 800;
		height = 800;
		setSize(width, height);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		setup();

		repaint();

		setVisible(true);

		// Defining where I want the origin of the graph to be.
		x0 = 50;
		y0 = paintPanel.getHeight() - 400;
	}

	/**
	 * Setup of the JPanels, Buttons, etc using a GridBagLayout
	 */
	public void setup() {
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		paintPanel = new JPanel();
		paintPanel.setSize(width, height - 50);
		buttonPanel = new JPanel();

		sorted = new JButton("Sorted Off Value");
		sorted.addActionListener(this);
		buttonPanel.add(sorted);

		unSorted = new JButton("Unsorted Off Value");
		unSorted.addActionListener(this);
		buttonPanel.add(unSorted);

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

		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, paintPanel.getWidth(), paintPanel.getHeight());
		g.setColor(Color.BLACK);
		g.drawLine(x0, 0, x0, paintPanel.getHeight());
		g.drawLine(x0, y0, paintPanel.getWidth(), y0);

		// Drawing the points on the graph
		g.setColor(Color.BLACK);
		for (int i = 0; i < points.size(); i++) {
			x = (int) (points.get(i).getK() * 10 + x0);
			y = (int) ((-1) * points.get(i).getBk() * 7.5 + y0);
			g.fillOval(x - 2, y - 2, 4, 4);
		}

		// Drawing the Matrices on the Graph
		if (A != null) {
			g.drawString("Original Matrix", 100, 75);
			paintMatrix(100, 100, A, g);
		}
		if (B != null) {
			g.drawString("Diagonlized Matrix", 300, 575);
			paintMatrix(300, 600, B, g);
		}

		// Drawing the line given above.
		g.setColor(Color.GREEN);
		for (int i = 0; i < 600; i++) {
			lx = 10 * i + x0;
			ly = (int) ((-1) * (i * Math.log(9.0 / 10.0) + Math.log(offA))
					* 7.5 + y0);
			g.fillOval(lx - 1, ly - 2, 4, 4);
		}
	}

	/**
	 * Paint matrix.
	 * 
	 * @param x
	 *            the x location of the top left of the matrix
	 * @param y
	 *            the y location of the top left of the matrix
	 * @param A
	 *            the matrix to be painted
	 * @param g
	 *            the Graphics object
	 */
	public void paintMatrix(int x, int y, Matrix A, Graphics g) {
		for (int i = 0; i < A.getRowDimension(); i++) {
			for (int j = 0; j < A.getColumnDimension(); j++) {
				String temp;
				if (Math.abs(A.get(i, j)) < .0001)
					temp = 0.0 + "";
				else
					temp = Math.floor(A.get(i, j) * 100000) / 100000.0 + "";
				g.drawString(temp, j * 100 + x, i * 20 + y);
			}
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

		// Run the algorithm ten times with the largest off diagonal values
		if (e.getSource() == sorted) {

			points.clear();
			offA = 0;

			for (int i = 0; i < 10; i++) {
				Jacobi run = new Jacobi(5, 5);
				points.addAll(run.runAlg());
				A = run.getA();
				B = run.getB();
				if (offA == 0)
					offA = run.offA();
				else if (run.offA() > offA)
					offA = run.offA();
			}
		}

		// Run the algorithm ten times systematically choosing off diagonal
		// values
		if (e.getSource() == unSorted) {
			points.clear();
			offA = 0;

			for (int i = 0; i < 10; i++) {
				Jacobi run = new Jacobi(5, 5);
				points.addAll(run.runAlgSweep());
				A = run.getA();
				B = run.getB();
				if (offA == 0)
					offA = run.offA();
				else if (run.offA() > offA)
					offA = run.offA();
			}
		}

		repaint();

	}

	/**
	 * Create the Application
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {

		points = new LinkedList<PartialPoint>();

		new JacobiApp();
	}

}
