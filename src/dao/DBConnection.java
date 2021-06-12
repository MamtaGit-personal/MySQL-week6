package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class DBConnection {
	/* URL=jdbc:mysql://hostname:port/databasename. */
	private final static String URL = "jdbc:mysql://localhost:3306/library";
	private final static String USERNAME = "root";
	private static String PASSWORD;	
	
	private static Connection connection;
	private static DBConnection instance;
		
	private static Scanner scanner = new Scanner(System.in);
		
	private DBConnection(Connection connection) {
		DBConnection.connection = connection;
	}	
	
	public static Connection getConnection() {
		if(instance == null) {
			try {
				System.out.print("Enter your password: ");
				PASSWORD = scanner.nextLine();
				System.out.println("Beginning of a connection");
				connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
				instance = new DBConnection(connection);
				System.out.println("Connected Successfully");
			}
			catch(SQLException e) {
				System.out.println("Error connecting to the database");
				e.printStackTrace();
			}
		}// if
		return DBConnection.connection;
	}
}
