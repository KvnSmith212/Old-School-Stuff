package edu.cs2340.supercoders.financialtracker.model;

import java.sql.Timestamp;
import java.util.Date;

public class Transaction {

	private String name;

	private double amount;

	private String type;
	private String source;
	
	private String userEnteredTime;
	
	private Timestamp createdTime;

	public Transaction(String name, String source, double amount, String type, String ts) {
		this.name = name;
		this.amount = amount;
		this.type = type;
		this.userEnteredTime = ts;
		Date date = new Date();
		this.createdTime = new Timestamp(date.getTime());
	}

	public String getName() {
		return name;
	}
	public String getSource() {
		return source;
	}

	public String getType() {
		return type;
	}

	public double getAmount() {
		return amount;
	}
	
	public String getTime() {
		return userEnteredTime;
	}
	public String getCreatedTime() {
		return createdTime.toString();
	}

	public String toString() {
		String s = "[" + type + "]" + name + ": " + amount + "- ";
		return s;
	}

}
