package edu.gatech.calc.eulerMethod;

import java.util.HashMap;
import java.util.Map;

import org.mvel2.MVEL;

public class MvelTesting {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Map<String, Double> vars = new HashMap<String, Double>();
		vars.put("x", 4.0);
		vars.put("y", 10.0);
		double result = (Double) MVEL.eval("x + y", vars);
		
		System.out.println(result);
		System.out.println((Double) MVEL.eval("-x", vars));
		System.out.println((Double) MVEL.eval("(x+y)*y", vars));
		System.out.println((Double) MVEL.eval("Math.pow(x, 2.0)", vars));

	}

}
