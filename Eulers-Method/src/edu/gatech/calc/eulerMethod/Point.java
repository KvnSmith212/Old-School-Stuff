package edu.gatech.calc.eulerMethod;

/**
 * The Class Point.
 * 
 * Represents some value pair (x,y). Supports addition of two points and
 * mulitplication of a point and a double.
 */
public class Point {

	private double x;
	private double y;

	/**
	 * Instantiates a new point.
	 * 
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 */
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Adds the point to a point passed in.
	 * 
	 * @param p
	 *            the p
	 * @return the point
	 */
	public Point add(Point p) {
		Point temp = new Point(0, 0);
		temp.x = p.x + this.x;
		temp.y = p.y + this.y;

		return temp;
	}

	/**
	 * Multiply the point by some double d.
	 * 
	 * @param d
	 *            the d
	 * @return the point
	 */
	public Point mult(Double d) {
		Point temp = new Point(0, 0);
		temp.x = d * this.x;
		temp.y = d * this.y;

		return temp;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}
}
