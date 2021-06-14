package dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import entity.Book;
import entity.Customer;

public class BookDao {
	
	private TransactionDao transactionDao = new TransactionDao();
	private CustomerDao customerDao = new CustomerDao();

	private Scanner scanner = new Scanner(System.in);
	private Connection connection;
	
	// used in getBooks() method
	private final String SHOW_ALL_BOOKS_QUERY = "SELECT * FROM books";
	
	// used in deleteBookFromTheCatalogue()
	private final String DELETE_BOOK_BY_ID_QUERY = "DELETE FROM books WHERE id = ?";
	
	// used in addNewBook() method
	private final String INSERT_NEW_BOOK_QUERY = "INSERT INTO books(title, author, genre, status) VALUES(?,?,?,?)";
	
	// used in deleteBookFromTheCatalogue() and checkoutBookByTitle() methods
	private final String CALL_SP_TO_UPDATE_BOOK_STATUS = "{CALL UpdateBookStatusForAGivenBookId(?,?)}";
	
	// used in renewBookUsingCustomerPhone() and checkoutBookByTitle() methods
	private final String CALL_SP_TO_FIND_BOOKID_FOR_GIVEN_TITLE = "{CALL FindBookIdForAGivenTitle(?)}";
	
	// used in checkoutBookByAuthor() methods. There can > 1 book_id because an author can write > 1 book
	private final String CALL_SP_TO_FIND_BOOK_IDS_FOR_GIVEN_AUTHOR = "{CALL FindBookIdForAGivenAuthor(?)}";
		
	//used in renewBookUsingCustomerPhone()
	private final String SHOW_BOOKID_FOR_GIVEN_TRANSACTION_ID_QUERY = "SELECT book_id from transactions where id = ?";
	
	public BookDao() {
		connection = DBConnection.getConnection();
	}
	
	public List<Book> getBooks() throws SQLException {
		System.out.println("Book DAO -> getBooks():\n--------------");
		ResultSet rs = connection.prepareStatement(SHOW_ALL_BOOKS_QUERY).executeQuery();
		List<Book> books = new ArrayList<Book>();
		
		while(rs.next()) {
			books.add(new Book(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
		}
		return books;
	}
	
	public void addNewBook(String title, String authorName, String genre, String status) throws SQLException {
		System.out.println("Book DAO -> addNewBook():\n--------------");
		PreparedStatement ps = connection.prepareStatement(INSERT_NEW_BOOK_QUERY);
		ps.setString(1, title);
		ps.setString(2, authorName);
		ps.setString(3, genre);  
		ps.setString(4, status);
		ps.executeUpdate();
	}
	
	public void deleteBookFromTheCatalogue(String title) throws SQLException {
		System.out.println("Book DAO -> Delete a Book():\n--------------");
		
		//Find the book id from the books table for a given title where status = 'available'
		CallableStatement stmt = connection.prepareCall(CALL_SP_TO_FIND_BOOKID_FOR_GIVEN_TITLE);
		stmt.setString(1, title);
		ResultSet rs = stmt.executeQuery();
		rs.next();
		int bookId = rs.getInt(2); 
		
		if(bookId == -1) {
			System.out.println("CAN NOT delete the book for Book Id is: " + bookId);
			System.out.println("Choose another book because the book is: " + rs.getString(3));
		}
		else {
			//First delete the row in the transactions table where book_id = bookID
			System.out.println("Transactions Table -> Delete a row for a given bookId: " + bookId);
			transactionDao.deleteRowFromTransactionByBookId(bookId);
		
			//Then delete the row in the books table where id = bookID
			System.out.println("Books Table -> Delete a Book for a given bookId " + bookId);
			PreparedStatement ps = connection.prepareStatement(DELETE_BOOK_BY_ID_QUERY);
			ps.setInt(1, bookId);
			ps.executeUpdate();
			System.out.println("Books Table -> The book has been deleted successfully for a given bookId " + bookId + "!");
		}
	}
	
	public void checkoutBookByTitle(String title) throws SQLException {
		System.out.println("Book DAO -> checkoutBookByTitle():\n--------------");
		CallableStatement stmt = connection.prepareCall(CALL_SP_TO_FIND_BOOKID_FOR_GIVEN_TITLE);
		stmt.setString(1, title);
		ResultSet rs = stmt.executeQuery(); 
		rs.next();		
		if(rs.getInt(1) == -1) {  // need to verify if I should check for -1
			System.out.println("The book is UNAVAILABLE. Please select ANOTHER Book to checkout...");
		} 
		else {
			System.out.print("Enter your phone as XXX-XXX-XXXX: ");
			String phone = scanner.nextLine();
			
			// When we checkout a book, first we set the status in the books table = 'unavailable' for a given book_id
			CallableStatement stmt2 = connection.prepareCall(CALL_SP_TO_UPDATE_BOOK_STATUS);
			stmt2.setInt(1, rs.getInt(2));
			stmt2.setString(2, "unavailable");
			stmt2.executeUpdate();
			
			// Then we checkout the book using the transactions table. We set the due_date, transaction_date and action etc. columns
			transactionDao.checkoutTransactionByBookId(rs.getInt(2), phone);
		}
		
	}
	
	public void checkoutBookByAutor(String author) throws SQLException {
		System.out.println("Book DAO -> checkoutBookByAuthor():\n--------------");
		CallableStatement stmt = connection.prepareCall(CALL_SP_TO_FIND_BOOK_IDS_FOR_GIVEN_AUTHOR);
		stmt.setString(1, author);
		ResultSet rs = stmt.executeQuery(); 
		rs.next();
		if(rs.getInt(1) == -1) {  
			System.out.println("The book is UNAVAILABLE. Please select ANOTHER Book to checkout...");
		} else {
			do{
				// Show all the books for a given author
				System.out.println("BookID: " + rs.getInt(2) + ", status: " + rs.getString(3)
				+ ", Title: " + rs.getString(4));
			}while(rs.next());
			
			System.out.print("Select the right Title: ");
			String title = scanner.nextLine();
			checkoutBookByTitle(title);
		}//else
	
	}
	
	public void renewBookUsingCustomerPhone(String phone) throws SQLException {
		System.out.println("Book DAO -> renew a Book--------------");
		
		// Find the customer id for a given phone first
		List<Customer> customer = customerDao.getCustomerInfoUsingPhone(phone);
		System.out.println("CustomerID: " + customer.get(0).getCustomerId() + ", FirstName: " + customer.get(0).getFirstName()
		+ ", Phone: " + customer.get(0).getPhone()) ;
				
		// Find all the books in the transactions table using CustomerId. A customer may have checked out more than ONE book. 
		transactionDao.FindTransactionByCustomerID(customer.get(0).getCustomerId());
						
		System.out.print("Enter the right Transaction ID to renew that book: ");
		int transactionID = Integer.parseInt(scanner.nextLine());
		transactionDao.renewTransactionByTransactionID(transactionID);
			
		// Find the book_id from the transactions table for the given transactionID
		PreparedStatement ps = connection.prepareStatement(SHOW_BOOKID_FOR_GIVEN_TRANSACTION_ID_QUERY);
		ps.setInt(1, transactionID);
		ResultSet rs = ps.executeQuery(); 
		rs.next();		
		
		// Update the books table. Set the status = 'unavailable' for the given book_id
		CallableStatement stmt = connection.prepareCall(CALL_SP_TO_UPDATE_BOOK_STATUS);
		stmt.setInt(1, rs.getInt(1));
		stmt.setString(2, "unavailable");
		stmt.executeUpdate();
					
	}
	
	public void returnBookUsingCustomerPhone(String phone) throws SQLException {
		System.out.println("Book DAO -> return a Book--------------");
		
		// Find the customer id for a given phone first
		List<Customer> customer = customerDao.getCustomerInfoUsingPhone(phone);
		System.out.println("CustomerID: " + customer.get(0).getCustomerId() + ", FirstName: " + customer.get(0).getFirstName()
		+ ", Phone: " + customer.get(0).getPhone()) ;
		
		// Find all the books in the transactions table using CustomerId. A customer may have checked out more than ONE book. 
		transactionDao.FindTransactionByCustomerID(customer.get(0).getCustomerId());
		System.out.print("Enter the right Transaction ID to return that book: ");
		int transactionID = Integer.parseInt(scanner.nextLine());
		transactionDao.returnTransactionByTransactionID(transactionID);
		
		// Find the book_id from the transactions table for the given transactionID
		PreparedStatement ps = connection.prepareStatement(SHOW_BOOKID_FOR_GIVEN_TRANSACTION_ID_QUERY);
		ps.setInt(1, transactionID);
		ResultSet rs = ps.executeQuery(); 
		rs.next();		
				
		// Update the books table. Set the status = 'unavailable' for the given book_id
		CallableStatement stmt = connection.prepareCall(CALL_SP_TO_UPDATE_BOOK_STATUS);
		stmt.setInt(1, rs.getInt(1));
		stmt.setString(2, "available");
		stmt.executeUpdate();
		
	}
		
}
