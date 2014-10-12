package edu.gatech.math2605.project3;

/**
 * The Class PartialPoint.
 * 
 * This class provides a point to be graphed where k is the kth iteration of the
 * Jacobi algorithm and bk is the natural logarithm of the magnitude of a
 * matrix. (In this case 5x5)
 */
public class PartialPoint {

	private int k;
	private double bk;

	/**
	 * Instantiates a new partial point.
	 * 
	 * @param k
	 *            the k
	 * @param bk
	 *            the bk
	 */
	public PartialPoint(int k, double bk) {
		this.k = k;
		this.bk = bk;
	}

	/**
	 * Gets the k.
	 * 
	 * @return the k
	 */
	public int getK() {
		return k;
	}

	/**
	 * Sets the k.
	 * 
	 * @param k
	 *            the new k
	 */
	public void setK(int k) {
		this.k = k;
	}

	/**
	 * Gets the bk.
	 * 
	 * @return the bk
	 */
	public double getBk() {
		return bk;
	}

	/**
	 * Sets the bk.
	 * 
	 * @param bk
	 *            the new bk
	 */
	public void setBk(double bk) {
		this.bk = bk;
	}
}
