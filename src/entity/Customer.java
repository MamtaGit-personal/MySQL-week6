package entity;

public class Customer {
	private int customerId;
	private String firstName;
	private String lastName;
	private String phone;
	private String email; 
	private boolean isActive;
	
	public Customer(int id, String first_name, String last_name, String phone, String email, boolean isActive) {
		this.customerId = id;
		this.firstName = first_name;
		this.lastName = last_name;
		this.phone = phone;
		this.email = email;
		this.setActive(isActive);
	}

	public int getCustomerId() {
		return customerId;
	}
	
	// We CANNOT set a customerId and hence no setter for customerId.
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String first_name) {
		this.firstName = first_name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String last_name) {
		this.lastName = last_name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

}
