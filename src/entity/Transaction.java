package entity;

import java.sql.Date;

public class Transaction {
	private int transactionId;
	private int book_id;
	private int customer_id;
	private String action;
	private Date transaction_date; 
	private Date due_date;
	
	public Transaction(int id, int book_id, int customer_id, String action, Date transaction_date, Date due_date) {
		this.transactionId =id;
		this.book_id = book_id;
		this.customer_id = customer_id;
		this.action = action;
		this.transaction_date = transaction_date;
		this.due_date = due_date;
	}

	public int getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}

	public int getBook_id() {
		return book_id;
	}

	public void setBook_id(int book_id) {
		this.book_id = book_id;
	}

	public int getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(int customer_id) {
		this.customer_id = customer_id;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Date getTransaction_date() {
		return transaction_date;
	}

	public void setTransaction_date(Date transaction_date) {
		this.transaction_date = transaction_date;
	}

	public Date getDue_date() {
		return due_date;
	}

	public void setDue_date(Date due_date) {
		this.due_date = due_date;
	}

}
