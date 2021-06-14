package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import entity.Book;
import entity.Customer;

public class CustomerDao {
	private Connection connection;
	private final String FIND_CUSTOMERID_BY_PHONE_QUERY = "SELECT * FROM customers where phone = ?";
	private final String CREATE_NEW_CUSTOMER_QUERY = "INSERT INTO customers (first_name, last_name, phone, email, isactive) VALUES(?, ?, ?, ?, true)";
	private final String DELETE_CUSTOMER_BY_PHONE = "UPDATE customers SET isactive = false WHERE id = ?";
	//private final String GET_CUSTID_FOR_GIVEN_PHONE = "SELECT id FROM customers WHERE phone = ?";
	
	public CustomerDao() {
		connection = DBConnection.getConnection();
	}
	
	public List<Customer> getCustomerInfoUsingPhone(String phone) throws SQLException {
		System.out.println("Customer DAO -> getCustomers by Phone number:\n--------------");
		PreparedStatement ps = connection.prepareStatement(FIND_CUSTOMERID_BY_PHONE_QUERY);
		ps.setString(1, phone);
		ResultSet rs = ps.executeQuery();
		 
		List<Customer> customer = new ArrayList<Customer>();
		while(rs.next()) {
			customer.add(new Customer(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getBoolean(6)));
		}
		return customer;
	}

	
	public void addCustomer(String firstName, String lastName, String phone, String email) throws SQLException {
		System.out.println("Customer DAO -> Add a Customer():\n--------------");

		PreparedStatement ps = connection.prepareStatement(CREATE_NEW_CUSTOMER_QUERY);
		ps.setString(1, firstName);
		ps.setString(2, lastName);
		ps.setString(3, phone);
		ps.setString(4, email);
		ps.executeUpdate();
	}
	
	public void deleteCustomer(String phone) throws SQLException {
		System.out.println("Customer DAO -> Delete a Customer():\n--------------");
		
		//Find customer id by given phone
		List<Customer> customer = getCustomerInfoUsingPhone(phone);
		System.out.println("Customer ID:  " + customer.get(0).getCustomerId() + " / First Name:  " + 
				customer.get(0).getFirstName() + " / Phone:  " + customer.get(0).getPhone());
						
		//Change the active status of the customer to inactive, rather than delete
		PreparedStatement ps = connection.prepareStatement(DELETE_CUSTOMER_BY_PHONE);
		ps.setInt(1, customer.get(0).getCustomerId());
		ps.executeUpdate();
	}
	

}
