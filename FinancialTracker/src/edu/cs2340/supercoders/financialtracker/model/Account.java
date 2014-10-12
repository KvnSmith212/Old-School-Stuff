package edu.cs2340.supercoders.financialtracker.model;

import java.util.LinkedList;
import java.util.List;

/**
 * The Class Account.
 * 
 * This is relatively bare at the moment. Simply holds and account type
 * (Checkings/Savings) and the balance in the account.
 */
public class Account {
	
	private String name;

	/** The account type. */
	private String accountType;

	/** The balance. */
	private double balance;
	
	private List<Transaction> transactionHistory = new LinkedList<Transaction>();
	
	private int transCount = 0;

	/**
	 * Instantiates a new account.
	 * 
	 * @param type
	 *            the type
	 * @param balance
	 *            the balance
	 */
	public Account(String name, String type, double balance) {
		this.name = name;
		accountType = type;
		this.balance = balance;
	}
	
	/**
	 * Either adds to the balance or subtracts from the balance depending on what type of transaction it is. Also adds the transaction to the transaction history.
	 * 
	 * @param trans
	 */
	public void addTrans(Transaction trans){
		transactionHistory.add(trans);
		transCount++;
		double amount = trans.getAmount();
		String kind = trans.getType();
		if (kind.equals("deposit")){
			balance = balance + amount;
		} else {
			balance = balance - amount;
		}
	}
	
	public List<Transaction> getTransHistory() {
		return transactionHistory;
	}
	
	public String getName() {
		return name;
	}

	/**
	 * Gets the account type.
	 * 
	 * @return the account type
	 */
	public String getAccountType() {
		return accountType;
	}

	/**
	 * Gets the balance.
	 * 
	 * @return the balance
	 */
	public double getBalance() {
		return balance;
	}
	
	public int getTransCount(){
		return transCount;
	}

}
