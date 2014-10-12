package edu.gatech.math2605.project3;

/**
 * The Class Entry.
 * 
 * Represents an entry in an mxn matrix.
 */
public class Entry {
	
	private int m;
	private int n;
	private double value;
	
	/**
	 * Instantiates a new entry.
	 *
	 * @param m the m
	 * @param n the n
	 * @param value the value
	 */
	public Entry(int m, int n, double value){
		this.m = m;
		this.n = n;
		this.value = value;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return (value + " " + m + "x" + n);
	}

	/**
	 * Gets the m.
	 *
	 * @return the m
	 */
	public int getM() {
		return m;
	}

	/**
	 * Sets the m.
	 *
	 * @param m the new m
	 */
	public void setM(int m) {
		this.m = m;
	}

	/**
	 * Gets the n.
	 *
	 * @return the n
	 */
	public int getN() {
		return n;
	}

	/**
	 * Sets the n.
	 *
	 * @param n the new n
	 */
	public void setN(int n) {
		this.n = n;
	}

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public double getValue() {
		return value;
	}

	/**
	 * Sets the value.
	 *
	 * @param value the new value
	 */
	public void setValue(double value) {
		this.value = value;
	}

}
