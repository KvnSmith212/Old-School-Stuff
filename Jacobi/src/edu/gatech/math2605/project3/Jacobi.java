package edu.gatech.math2605.project3;

import java.util.LinkedList;
import java.util.List;

import Jama.Matrix;

/**
 * The Class Jacobi.
 * 
 * This class will generate a random mxn matrix and run the Jacobi Algorithm.
 * The Algorithm can be run by either choosing the largest off diagonal element
 * for Bij or systematically going through the upper right triangle.
 */
public class Jacobi {

	// A is the original Matrix, while B will represent the inprogress or
	// diagonlized matrix.
	private Matrix A;
	private Matrix B;

	/**
	 * Instantiates the class and creates the random, symmetric, matrix.
	 * 
	 * @param m
	 *            the m
	 * @param n
	 *            the n
	 */
	public Jacobi(int m, int n) {
		A = Matrix.random(m, n);
		makeSymmetric(A);
	}

	/**
	 * Runs the jacobi algorithm by choosing the largest off diagonal element.
	 * 
	 * @return the list
	 */
	public List<PartialPoint> runAlg() {
		B = A.copy();
		Matrix bSmall;
		Matrix U;
		Matrix G;

		List<PartialPoint> points = new LinkedList<PartialPoint>();
		int k = 0;
		double sum = 1;

		System.out.println("**********NEW MATRIX***********");
		B.print(4, 4);

		while (sum > Math.pow(10, -9)) {
			/*
			 * Step 1: Find the largest off diagonal element of A
			 */
			Entry largestOff = largestOffElement(B);

			/*
			 * Step 2: Calculate the Givens matrix by doing diagonalization to
			 * the 2x2 matrix created using the largest off diagonal element
			 */
			bSmall = findSmall(B, largestOff);
			U = bSmall.eig().getV();
			G = findG(U, largestOff, B.getRowDimension(),
					B.getColumnDimension());

			/*
			 * Step 3: B = G^T B G
			 */
			B = G.transpose().times(B).times(G);

			/*
			 * Stop when the summation of every (off diagonal entry squared) is
			 * <= 10^-9
			 */
			sum = checkSum(B);
			System.out.println("Off(B) for " + k + "th iteration:  " + sum);

			k++;
			points.add(new PartialPoint(k, Math.log(sum)));

		}
		
		B.print(4, 4);

		return points;
	}

	/**
	 * Run the algorithm by systematically choosing points in the upper right
	 * triangle
	 * 
	 * @return the list
	 */
	public List<PartialPoint> runAlgSweep() {
		B = A.copy();
		Matrix bSmall;
		Matrix U;
		Matrix G;

		List<PartialPoint> points = new LinkedList<PartialPoint>();
		int k = 0;
		double sum = 1;

		Entry next = new Entry(1, 2, A.get(0, 1));
		int i = 1;
		int j = 2;

		System.out.println("**********NEW MATRIX***********");
		B.print(4, 4);

		while (sum > Math.pow(10, -9)) {
			/*
			 * Step 1: Sweep through the off elements systematically.
			 */
			if (j < 5)
				j++;
			else if (j == 5 && i < 4) {
				i++;
				j = i + 1;
			} else {
				i = 1;
				j = 2;
			}
			next.setValue(A.get(i - 1, j - 1));
			next.setM(i);
			next.setN(j);

			/*
			 * Step 2: Calculate the Givens matrix by doing diagonalization to
			 * the 2x2 matrix created using the largest off diagonal element
			 */
			bSmall = findSmall(B, next);
			U = bSmall.eig().getV();
			G = findG(U, next, B.getRowDimension(), B.getColumnDimension());

			/*
			 * Step 3: B = G^T B G
			 */
			B = G.transpose().times(B).times(G);

			/*
			 * Stop when the summation of every (off diagonal entry squared) is
			 * <= 10^-9
			 */
			sum = checkSum(B);
			System.out.println("Off(B) for " + k + "th iteration:  " + sum);

			k++;
			points.add(new PartialPoint(k, Math.log(sum)));
		}
		
		B.print(4, 4);

		return points;
	}

	/**
	 * Because the random matrix isn't symmetric by default, I have to go
	 * through it and fix things up to make it symmetric.
	 * 
	 * @param A
	 *            the symmetric matrix
	 */
	private void makeSymmetric(Matrix A) {
		for (int i = 0; i < A.getRowDimension(); i++) {
			for (int j = 0; j < A.getColumnDimension(); j++) {
				if (i != j && A.get(i, j) != A.get(j, i)) {
					A.set(i, j, A.get(j, i));
				}
			}
		}
	}

	/**
	 * Largest off element.
	 * 
	 * @param A
	 *            the a
	 * @return the largest off diagonal entry
	 */
	private Entry largestOffElement(Matrix A) {
		Entry offElement = new Entry(1, 2, A.get(0, 1));

		for (int i = 0; i < A.getRowDimension(); i++) {
			for (int j = 0; j < A.getColumnDimension(); j++) {
				if (i != j
						&& Math.abs(A.get(i, j)) > Math.abs(offElement
								.getValue())) {
					offElement.setValue(A.get(i, j));
					offElement.setM(i + 1);
					offElement.setN(j + 1);
				}
			}
		}

		return offElement;
	}

	/**
	 * Find small matrix used in the creation of the givens matrix
	 * 
	 * @param A
	 *            the a
	 * @param off
	 *            the off
	 * @return the matrix
	 */
	private Matrix findSmall(Matrix A, Entry off) {
		Matrix aSmall = new Matrix(2, 2);
		double a11 = A.get(off.getM() - 1, off.getM() - 1);
		double a12 = A.get(off.getM() - 1, off.getN() - 1);
		double a21 = A.get(off.getN() - 1, off.getM() - 1);
		double a22 = A.get(off.getN() - 1, off.getN() - 1);
		aSmall.set(0, 0, a11);
		aSmall.set(0, 1, a12);
		aSmall.set(1, 0, a21);
		aSmall.set(1, 1, a22);

		return aSmall;
	}

	/**
	 * Find the givens matrix.
	 * 
	 * @param U
	 *            the u
	 * @param off
	 *            the off
	 * @param GM
	 *            the gm
	 * @param GN
	 *            the gn
	 * @return the matrix
	 */
	private Matrix findG(Matrix U, Entry off, int GM, int GN) {
		Matrix G = Matrix.identity(GM, GN);
		int m = off.getM();
		int n = off.getN();
		G.set(m - 1, m - 1, U.get(0, 0));
		G.set(m - 1, n - 1, U.get(0, 1));
		G.set(n - 1, m - 1, U.get(1, 0));
		G.set(n - 1, n - 1, U.get(1, 1));

		return G;
	}

	/**
	 * Returns the magnitude of the Matrix using only the off diagonal elements
	 * so we can check to see if it's below what we want it to be below.
	 * 
	 * @param A
	 *            the a
	 * @return the double
	 */
	private Double checkSum(Matrix A) {
		double sum = 0;

		for (int i = 0; i < A.getRowDimension(); i++) {
			for (int j = 0; j < A.getColumnDimension(); j++) {
				if (i != j) {
					sum += Math.pow(A.get(i, j), 2);
				}
			}
		}

		return sum;
	}

	/**
	 * Off a.
	 * 
	 * @return the double
	 */
	public Double offA() {
		return checkSum(A);
	}

	/**
	 * Gets the a.
	 * 
	 * @return the a
	 */
	public Matrix getA() {
		return A;
	}

	/**
	 * Gets the b.
	 * 
	 * @return the b
	 */
	public Matrix getB() {
		return B;
	}
}
