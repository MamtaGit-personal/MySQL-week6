package entity;

public class Book {
	private int bookId;
	private String title;
	private String author;
	private String genre;
	private String status; 
	
	public Book(int id, String title, String author, String genre, String status) {
		this.bookId = id;
		this.title = title;
		this.author = author;
		this.genre = genre;
		this.status = status;
	}

	public int getBookId() {
		return bookId;
	}

	// We CANNOT change bookId, title or author for a book and hence no respective setters.
	
	public String getTitle() {
		return title;
	}

	public String getAuthor() {
		return author;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	

}
