package dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionDao {
	
	private Connection connection;
	
	// used in checkoutTransactionByBookId()
	private final String CALL_SP_TO_CHECKOUT_BOOK = "{CALL CheckoutTransactionByBookId(?,?)}";
	
	// used in deleteRowFromTransactionByBookId()
	private final String CALL_SP_TO_FIND_TRANSACTION_ID_FOR_GIVEN_BOOKID = "{CALL FindTransactionIdForAGivenBookId(?)}";
	
	//used in FindTransactionByCustomerID()
	private final String CALL_SP_TO_FIND_TRANSACTION_IDS_FOR_GIVEN_CUSTOMERID = "{CALL FindTransactionIdsForAGivenCustomerId(?)}";
	
	// used in deleteRowFromTransactionByBookId()
	private final String DELETE_TRANSACTION_FOR_A_GIVEN_ID_QUERY = "DELETE FROM transactions where id = ?";
	
	// used in renewBookByTransactionID()
	private final String CALL_SP_TO_RENEW_TRANSACTION_FOR_A_GIVEN_ID_QUERY = "{CALL RenewTransactionByTransactionId(?)}";
	
	// used in returnBookByTransactionID()
	private final String CALL_SP_FOR_RETURN_TRANSACTION_FOR_A_GIVEN_ID_QUERY = "{CALL ReturnTransactionByTransactionId(?)}";
	
	public TransactionDao() {
		connection = DBConnection.getConnection();
	}
	
	public void checkoutTransactionByBookId(int bookId, String phone) throws SQLException {  
		System.out.println("Book DAO -> actionCheckoutBookByBookdId():\n--------------");
		CallableStatement stmt = connection.prepareCall(CALL_SP_TO_CHECKOUT_BOOK);
		stmt.setInt(1, bookId);
		stmt.setString(2, phone);
		//stmt.setInt(2, custID);
		stmt.executeUpdate();
	}
	
	public void renewTransactionByTransactionID(int transactionID) throws SQLException{
		CallableStatement stmt = connection.prepareCall(CALL_SP_TO_RENEW_TRANSACTION_FOR_A_GIVEN_ID_QUERY);
		stmt.setInt(1, transactionID);
		stmt.executeUpdate();
		System.out.println("Renew Transaction was Successful!");
	}
	
	public void returnTransactionByTransactionID(int transactionID) throws SQLException{
		CallableStatement stmt = connection.prepareCall(CALL_SP_FOR_RETURN_TRANSACTION_FOR_A_GIVEN_ID_QUERY);
		stmt.setInt(1, transactionID);
		stmt.executeUpdate();
		System.out.println("Return Transaction was Successful!");
	}
	
	public void deleteRowFromTransactionByBookId(int bookId) throws SQLException {
		//Find in the transactions table if a given bookId exists.
		CallableStatement stmt = connection.prepareCall(CALL_SP_TO_FIND_TRANSACTION_ID_FOR_GIVEN_BOOKID);
		stmt.setInt(1, bookId);
		ResultSet rs = stmt.executeQuery();
		rs.next();
		int transactionId = rs.getInt(1); 
		
		if(transactionId == -1) {
			System.out.println("Transaction Id is negative: " + transactionId);
			System.out.println("Cannot delete any row from the Transactions table for given bookID: " + bookId);
		}
		else {
			//Once you know that the transaction exists for the given bookId, delete the transaction.
			PreparedStatement ps = connection.prepareStatement(DELETE_TRANSACTION_FOR_A_GIVEN_ID_QUERY);
			ps.setInt(1, rs.getInt(1));
			ps.executeUpdate();
			System.out.println("Delete Transaction was Successful!");
		}
	}

	public void FindTransactionByCustomerID(int customerId) throws SQLException {
		CallableStatement stmt = connection.prepareCall(CALL_SP_TO_FIND_TRANSACTION_IDS_FOR_GIVEN_CUSTOMERID);
		stmt.setInt(1, customerId);
		ResultSet rs = stmt.executeQuery();
		
		// show all the Transaction IDs to select the RIGHT transactionID for renewal
		while(rs.next()){
			System.out.println("TransactionID: " + rs.getInt(1) + ", BookId: " + rs.getInt(2)
			+ ", Title: " + rs.getString(3)) ;
		}
	}
		
}
