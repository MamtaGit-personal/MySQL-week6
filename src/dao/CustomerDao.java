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
			customer.add(new Customer(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
		}
		return customer;
	}
}
