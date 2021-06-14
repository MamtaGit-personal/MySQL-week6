-- drop database if exists library;

create database if not exists library;
use library;


drop table if exists transactions;
drop table if exists books;
drop table if exists customers;


CREATE TABLE customers(
	id int(11) not null auto_increment,
  	first_name varchar(25) not null,
	last_name varchar(25),
  	phone varchar(16) not null unique,
	email varchar(384) null,
   	isactive boolean not null default True,  -- when a customer is removed the flag is set to false otherwise true
	primary key(id)
);

CREATE TABLE books(
	id int(11) not null auto_increment,
    title varchar(40) not null,
	author varchar(25) not null,
    genre varchar(25),
    status varchar(20) not null default 'available',  -- available or unavailable
    primary key(id)
);

CREATE TABLE transactions(
    id int(11) not null auto_increment,
    book_id int(11) not null,
    customer_id int(11) not null,
    action varchar(25),                -- checkout/renew/return
    transaction_date Date not null,
    due_date Date,
    primary key(id),
    foreign key (customer_id) references customers(id),
    foreign key(book_id) references books(id)
);

Insert into customers(email, phone, first_name, last_name)
Values("sam@gmail.com", "111-111-1111", "Sam", "Rosen");
 
Insert into customers(email, phone, first_name, last_name)
Values("pam@gmail.com", "111-111-2222", "Pam", "Rosen");
 
Insert into customers(email, phone, first_name, last_name)
Values("ham@gmail.com", "111-111-3333", "Ham", "Rosen");
 
Insert into customers(email, phone, first_name, last_name)
Values("tim@gmail.com", "111-111-4444", "Tim", "Gibbon");
 


Insert into books(title, author, genre, status)
Values("The Vanishing Half", "Brit Bennet", "Mystery", "available");
 
Insert into books(title, author, genre, status)
Values("Next Year in Havana", "Chanel Cleeton", "Fiction", "available");
 
Insert into books(title, author, genre, status)
Values("Think like a Monk", "Jay Shetty", "Self-help", "available");
 
 
Insert into books(title, author, genre, status)
Values("The Vanishing Half", "Brit Bennet", "Mystery", "available");
