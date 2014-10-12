package edu.gatech.calc.eulerMethod;

import java.util.HashMap;
import java.util.Map;

import org.mvel2.MVEL;

/**
 * The Class Function.
 * 
 * This class represents a vector field F = (f(x,y), g(x,y)). It contains
 * appropriate getters and setters and an evaluate method to return F(x,y)
 */
public class Function {

	private String f;
	private String g;

	/**
	 * Instantiates a new function.
	 * 
	 * @param f
	 * @param g
	 */
	public Function(String f, String g) {
		this.f = f;
		this.g = g;
	}

	/**
	 * Evaluate the vector field F for a give (x,y).
	 * 
	 * @param x
	 *            the value for x
	 * @param y
	 *            the value for y
	 * @return the result of F(x,y)
	 */
	public Point evalF(double x, double y) {
		Map<String, Double> vars = new HashMap<String, Double>();
		vars.put("x", x);
		vars.put("y", y);
		double resultx = (Double) MVEL.eval(f, vars);
		double resulty = (Double) MVEL.eval(g, vars);

		return new Point(resultx, resulty);
	}

	public String getF() {
		return f;
	}

	public void setF(String f) {
		this.f = f;
	}

	public String getG() {
		return g;
	}

	public void setG(String g) {
		this.g = g;
	}
}
