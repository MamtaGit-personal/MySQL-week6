package application;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import dao.BookDao;
import entity.Book;

public class Menu {
	
	private BookDao bookDao = new BookDao();
	private CustomerDao customerDao = new CustomerDao();
	//private TransactionDao transactionDao = new TransactionDao();
	private Scanner scanner = new Scanner(System.in);
	
	private List<String> options = Arrays.asList(
			"1 -Display books",
			"2 -Add a book",
			"3 -Delete a book",
			"4 -Add a customer",
			"5 -Delete a customer",
			"6 -Checkout a book",
			"7 -Renew a book",
			"8 -Return a book"
			);
	
	public void start() {
		String selection = "";
				
		do {
			printMenu();
			selection = scanner.nextLine();
			System.out.println("The Selection is: " + selection);
			try {
				if(selection.equals("1")) {
					displayBooks();
				}
				else if(selection.equals("2")) {
					addBook();
				}
				else if(selection.equals("3")) {
					deleteBook();
				}
				else if(selection.equals("4")) {
					addCustomer();
				}
				else if(selection.equals("5")) {
					deleteCustomer();
				}
				else if(selection.equals("6")) {
					checkoutBook();
				}
				else if(selection.equals("7")) {
					renewBook();
				}
				else if(selection.equals("8")) {
					returnBook();
				}
				else break;
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
			
			System.out.println();
			System.out.println("PRESS ENTER TO DISPLAY MENU OR -1 TO STOP.");
			scanner.nextLine();
			
		}while(!selection.equals("-1"));
	}
	
	private void displayBooks() throws SQLException {
		List<Book> books = bookDao.getBooks();
		for(Book book : books) {
			System.out.println("BookID: " + book.getBookId() + ", Title: " + book.getTitle() + ", Author: " 
					+ book.getAuthor() + ", Genre: " + book.getGenre() + ",  Status: " + book.getStatus());
		}
	}
	
	private void addBook() throws SQLException {
		System.out.print("Book Title: ");
		String title = scanner.nextLine();
		System.out.print("Author's First Name: ");
		String fName = scanner.nextLine();
		System.out.print("Author's Last Name: ");
		String lName = scanner.nextLine();
		System.out.print("Genre: ");
		String genre = scanner.nextLine();
		String fullName = fName.concat(" ").concat(lName);
		bookDao.addNewBook(title, fullName, genre, "available");
	}
	
	private void deleteBook() throws SQLException {
		System.out.print("Book Title: ");
		String title = scanner.nextLine();
		bookDao.deleteBookFromTheCatalogue(title);
	}
	
	private void checkoutBook() throws SQLException {
		System.out.println("You can search by Title or Author....");
		System.out.print("Enter 1 (search by Title): \nEnter 2 (search by Author):\n ");
		int searchOption = Integer.parseInt(scanner.nextLine());
		
		if(searchOption == 1) {
			System.out.print("Enter Book Title: ");
			String title = scanner.nextLine();
			bookDao.checkoutBookByTitle(title);
		}
		else if(searchOption == 2){
			System.out.print("Enter Author's First Name: ");
			String fName = scanner.nextLine();
			System.out.print("Enter Author's Last Name: ");
			String lName = scanner.nextLine();
			String authorName = fName.concat(" ").concat(lName);
			bookDao.checkoutBookByAuthor(authorName);
		}
		else {
			System.out.print("Enter the right search option...");
			
		}
	}
	
	private void renewBook() throws SQLException {
		System.out.print("Enter your phone as XXX-XXX-XXXX: ");
		String phone = scanner.nextLine();
		
		bookDao.renewBookUsingCustomerPhone(phone);
	}
	
	private void returnBook() throws SQLException {
		System.out.print("Enter your phone as XXX-XXX-XXXX: ");
		String phone = scanner.nextLine();
		
		bookDao.returnBookUsingCustomerPhone(phone);
	}
	
	private void addCustomer() throws SQLException {
		System.out.print("Enter first name:  ");
		String firstName = scanner.nextLine();
		System.out.print("Enter last name:  ");
		String lastName = scanner.nextLine();
		System.out.print("Enter phone number as XXX-XXX-XXXX:  ");
		String phone = scanner.nextLine();
		System.out.print("Enter email address:  ");
		String email = scanner.nextLine();
		
		customerDao.addCustomer(firstName, lastName, phone, email);
	}
	
	private void deleteCustomer() throws SQLException {
		System.out.print("Enter phone number as XXX-XXX-XXXX");
		String phone = scanner.nextLine();
		
		customerDao.deleteCustomer(phone);
	}
	
	
	private void printMenu() {
		System.out.println("Select an Option:\n--------------");
		for(int i = 0; i<options.size(); i++) {
			System.out.println(i+1+ ") " + options.get(i));
			
		}
	}
	
}
