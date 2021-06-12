-- 1. Show all the transactionIDs for a given Customer. A customer may have checkedout > 1 book 
DROP PROCEDURE IF EXISTS FindTransactionIdsForAGivenCustomerId;

DELIMITER //

CREATE PROCEDURE FindTransactionIdsForAGivenCustomerId(IN customerID INT)
BEGIN
	SELECT t.id as transctionID, b.id as bookID, b.title, t.due_date, t.action 
    FROM transactions t
    join books b 
    on t.book_id = b.id
    where t.customer_id = customerID;
END //
DELIMITER ;

-- To call the stored procedure – 
call FindTransactionIdsForAGivenCustomerId(1);

-- 2. When a customer renews a book, the transactionDate is today's date and due_date is transactionDate + 21 and action = renew.
DROP PROCEDURE IF EXISTS RenewTransactionByTransactionId;

DELIMITER //

CREATE PROCEDURE RenewTransactionByTransactionId(IN transactionID INT)
BEGIN
	Declare transactionDate Date;
	-- transactionDate is always today's date
	-- Due_date is always 3 weeks from the transactionDate
	Set transactionDate = (Select Date(now()));
    
    Update transactions
    Set action = 'renew',
    transaction_date = transactionDate,
    due_date = (Select date_add(transactionDate, Interval +21 day))
    where id = transactionID;
END //
DELIMITER ;

-- To call the stored procedure – 
call RenewTransactionByTransactionId(1);

-- 3. When a customer returns a book, action = return, dueDate = null and transactionDate = today's date
DROP PROCEDURE IF EXISTS ReturnTransactionByTransactionId;

DELIMITER //
CREATE PROCEDURE ReturnTransactionByTransactionId(IN transactionID INT)
BEGIN
	
    Update transactions
    Set action = 'return',
    transaction_date = (SELECT CURDATE()),
    due_date = null
    where id = transactionID;
END //
DELIMITER ;

-- To call the stored procedure – 
call ReturnTransactionByTransactionId (1);

-- 4. Update the books table. Set the status = 'unavailable' for renew/checkout and status = 'available' for return for the given bookID
DROP PROCEDURE IF EXISTS UpdateBookStatusForAGivenBookId;

DELIMITER //

CREATE PROCEDURE UpdateBookStatusForAGivenBookId (IN bookID INT,
IN bookStatus varchar(25))
BEGIN
	
	Update books
	Set status = bookStatus
	where id = bookID;
END //
DELIMITER ;

-- To call the stored procedure – 
call UpdateBookStatusForAGivenBookId (1);


-- 5. We want to show all the books that a customer has, when the customer wants to renew or return a book, so that right transactionID is selected for renew/return
DROP PROCEDURE IF EXISTS FindAllTransactionIdForACustomer;

DELIMITER //

CREATE PROCEDURE FindAllTransactionIdForACustomer(IN customerID INT)
BEGIN
	DROP TABLE IF EXISTS transactionIdTable;
    CREATE TEMPORARY TABLE transactionIdTable
	AS (
		Select id as transactionID, book_id, customer_id, action from transactions 
		where customer_id = customerID and action in ('checkout', 'renew')
	);
    
    SELECT * FROM transactionIdTable;
END //

DELIMITER ;

-- To call the stored procedure – 
call FindAllTransactionIdForACustomer(1);

-- 6. Show all the book_ids for the specified author. An author may have multiple books of the same title or multiple titles
DROP PROCEDURE IF EXISTS FindBookIdForAGivenAuthor;

DELIMITER //

CREATE PROCEDURE FindBookIdForAGivenAuthor(IN bookAuthor varchar(50)
)
BEGIN
	DROP TABLE IF EXISTS bookIdTable;
    CREATE TEMPORARY TABLE bookIdTable
	AS (
		Select id, title, status from books 
		where author = bookAuthor and status = 'available' order by id desc 
	);
    
    SET @count = (SELECT  count(*) from bookIdTable );
    IF @count = 0 THEN 
		SELECT -1 as bookCount, -1 as bookID, 'unavailable' as status, bookTitle as title;
     ELSE SELECT @count as bookCount, id as bookID, status, title from bookIdTable;
     End IF;
END //
DELIMITER ;

-- To call the stored procedure – 
call FindBookIdForAGivenAuthor('Jay Schetty');


-- 7. Show all the book_ids for the specified title. An author may have multiple books of the same title
DROP PROCEDURE IF EXISTS FindBookIdForAGivenTitle;

DELIMITER //

CREATE PROCEDURE FindBookIdForAGivenTitle(IN bookTitle varchar(50)
)
BEGIN
	DROP TABLE IF EXISTS bookIdTable;
    CREATE TEMPORARY TABLE bookIdTable
	AS (
		Select id, title, status from books 
		where title = bookTitle and status = 'available' order by id desc limit 1
	);
    
    SET @count = (SELECT  count(*) from bookIdTable );
    IF @count = 0 THEN 
		SELECT -1 as bookCount, -1 as bookID, 'unavailable' as status, bookTitle as title;
     ELSE SELECT 1 as bookCount, id as bookID, status, title from bookIdTable;
     End IF;
END //
DELIMITER ;

-- To call the stored procedure – 
call FindBookIdForAGivenTitle('Think like a Monk');


-- 8. Show the transaction_id for a given book_id
DROP PROCEDURE IF EXISTS FindTransactionIdForAGivenBookId;

DELIMITER //

CREATE PROCEDURE FindTransactionIdForAGivenBookId(IN bookID INT)
BEGIN
	DROP TABLE IF EXISTS transactionIdTable;
    CREATE TEMPORARY TABLE transactionIdTable
	AS (
		Select id as transactionID, book_id, action from transactions 
		where book_id = bookID and action not in ('checkout', 'renew')
	);
    
    SET @count = (SELECT  count(*) from transactionIdTable );
    IF @count = 0 THEN 
		SELECT -1 as transactionID, bookID, 'Transaction Not Present or book is checkedout' as Action;
     ELSE SELECT * from transactionIdTable;
     End IF;
END //
DELIMITER ;

-- To call the stored procedure – 
call FindTransactionIdForAGivenBookId(1);

