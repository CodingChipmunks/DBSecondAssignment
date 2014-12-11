-- Admin
-- Warning: Running This Script Will Change Root Password!
-- Warning: Running This Script Will Reset Database.MediaCollection!
-- Warning: All Users Has Execute Rights In This Schema!

-- Reset the schema.
DROP SCHEMA IF EXISTS mediacollection;

-- Create DB
CREATE SCHEMA IF NOT EXISTS mediacollection;
USE mediacollection;

-- Generate tables

-- Create User, Contains password and username for use in the post-review/post-rating procedures.
CREATE TABLE IF NOT EXISTS Account(
		Id INTEGER AUTO_INCREMENT PRIMARY KEY,
		Name VARCHAR(24) NOT NULL, 			-- Allowed read, not required in logins, displayed in reviews etc.
        User VARCHAR(32) NOT NULL UNIQUE,	-- Disallow read of this column.
        Pass VARCHAR(32) NOT NULL 			-- Disallow read of this column. Password stored in cleartext. :'(
);

-- Account used when values are added to the database from "admin" (in Build-TestData)
INSERT INTO Account(Name, User, Pass) VALUES ('Admin', 'Admin-MediaCollection', 'zuAZpOpvPasB4JD7ka7GW8DCXGiAN4');

-- Create Director/Artist
CREATE TABLE IF NOT EXISTS Creator(
		Id INTEGER AUTO_INCREMENT, 
		Name VARCHAR(24) NOT NULL, 
    PRIMARY KEY (Id)
);
    
CREATE INDEX NameIndex ON Creator (Name);

-- Create Genre
CREATE TABLE IF NOT EXISTS Genre(
		Id INTEGER AUTO_INCREMENT, 
        Name VARCHAR(24) NOT NULL, 
	PRIMARY KEY (Id)
);

-- Create Media Types
CREATE TABLE IF NOT EXISTS Mediatype(
		Id INTEGER AUTO_INCREMENT,
        Name VARCHAR(16) NOT NULL,
	PRIMARY KEY (Id)
);

-- Base types.
INSERT INTO Mediatype (Name) VALUES ("Album");
INSERT INTO Mediatype (Name) VALUES ("Movie");
INSERT INTO Mediatype (Name) VALUES ("E-Book");
INSERT INTO Genre (Name) VALUES ("Undefined"); -- Genre defaults to undefined.

-- Create Movie/Album
CREATE TABLE IF NOT EXISTS Media(
		Id INTEGER AUTO_INCREMENT, 
        Mediatype_Id INTEGER, 
        Genre_Id INTEGER DEFAULT 1, 
        Title VARCHAR(24) NOT NULL, 
        Year VARCHAR(16), 
        Duration INTEGER, 
        Account_Id INTEGER DEFAULT 1,
	PRIMARY KEY (Id), 
	FOREIGN KEY (Mediatype_Id) REFERENCES Mediatype(Id),
    FOREIGN KEY (Account_Id) REFERENCES Account(Id) ON DELETE SET NULL ON UPDATE CASCADE,
    FOREIGN KEY (Genre_Id) REFERENCES Genre(Id) ON DELETE CASCADE ON UPDATE CASCADE
);
    
-- Add Indexing Media on Media Type and Title
CREATE INDEX MediaIndex ON Media (Mediatype_Id);
CREATE INDEX TitleIndex ON Media (Title);

-- N:M Creator <-> Media
CREATE TABLE IF NOT EXISTS Contributor(
		Creator_Id INTEGER,
        Media_Id INTEGER, 
	FOREIGN KEY (Creator_Id) REFERENCES Creator(Id) ON DELETE CASCADE ON UPDATE CASCADE, 
	FOREIGN KEY (Media_Id) REFERENCES Media(Id) ON DELETE CASCADE ON UPDATE CASCADE 
);

-- Create Rating, Username is unique so that every user may only create one rating.  
CREATE TABLE IF NOT EXISTS Rating(
		Media_Id INTEGER, 
		Account_Id INTEGER, 
		Rating INTEGER, 
	FOREIGN KEY (Account_Id) REFERENCES Account(Id) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (Media_Id) REFERENCES Media(Id) ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY (Account_Id, Media_Id)
);

-- Create Review, Username is unique so that every user may only create one review.
CREATE TABLE IF NOT EXISTS Review(
		Media_Id INTEGER, 
        Account_Id INTEGER, 
        Title VARCHAR(24) NOT NULL, 
		Text VARCHAR(500) NOT NULL,
	 FOREIGN KEY (Account_Id)  REFERENCES Account(Id) ON DELETE CASCADE ON UPDATE CASCADE,
	 FOREIGN KEY (Media_Id)    REFERENCES Media(Id) ON DELETE CASCADE ON UPDATE CASCADE,
     PRIMARY KEY (Account_Id, Media_Id)
);



-- Create a database user with read access for every normal user, the user does not need to supply
-- Login credentials when reading from the database. Login information will be sent when required by the 
-- Database, when creating a review or rating. 
DROP USER 'clientapp'@'localhost';
CREATE USER 'clientapp'@'localhost' IDENTIFIED BY 'qwerty'; -- the password is on github and in the application.

-- Grant select on all columns in the following tables 
GRANT EXECUTE ON mediacollection.* TO 'clientapp'@'localhost'; 
GRANT SELECT ON mediacollection.Mediatype TO 'clientapp'@'localhost';	
GRANT SELECT ON mediacollection.Media TO 'clientapp'@'localhost';	
GRANT SELECT ON mediacollection.Contributor TO 'clientapp'@'localhost';	
GRANT SELECT ON mediacollection.Creator TO 'clientapp'@'localhost';	
GRANT SELECT ON mediacollection.Genre TO 'clientapp'@'localhost';	
GRANT SELECT ON mediacollection.Rating TO 'clientapp'@'localhost';	
GRANT SELECT ON mediacollection.Review TO 'clientapp'@'localhost';	

-- Do not allow access to Account passwords. Accountname is exposed, add nick-name?
GRANT SELECT (Name, Id) ON mediacollection.Account TO 'clientapp'@'localhost'; 
-- The admin logs on with write access, enabling the creation of accounts in User table.
-- Media.

-- ----------------------- STORED PROCEDURES ----------------------------------
DROP PROCEDURE IF EXISTS AddCreator;

DELIMITER $$
CREATE PROCEDURE VerifyAccount(
in p_user varchar(32),
in p_pass varchar(32),
out s_accountId int)
BEGIN
	SET s_accountId := -1;
    SELECT Account.Id INTO s_accountId FROM Account WHERE (Account.User = p_user AND Account.pass = p_pass);
END$$

DELIMITER $$
CREATE PROCEDURE AddCreator(
in p_user varchar(32),
in p_pass varchar(32),
in p_creator varchar(32),
in p_mediaId int)
BEGIN
	DECLARE MediaId int; 
    DECLARE AccountId int;
    DECLARE CreatorId int;
    
    SELECT Account.Id INTO AccountId FROM Account WHERE (Account.User = p_user AND Account.pass = p_pass);
    
    IF AccountId IS NOT NULL THEN
		-- if not exist genre then create
        IF (NOT EXISTS (SELECT * FROM Creator WHERE Creator.Name = p_creator)) THEN 
			INSERT INTO Creator(Name) VALUES (p_creator); 
        END IF; 
        
		SELECT Id INTO CreatorId FROM Creator WHERE Name = p_creator;
		INSERT INTO Contributor(Media_Id, Creator_Id) VALUES (p_mediaId, CreatorId);
	END IF;
END$$


DROP PROCEDURE IF EXISTS AddMedia;

DELIMITER $$
CREATE PROCEDURE AddMedia(
in p_user varchar(32),
in p_pass varchar(32),
in p_title varchar(32),
in p_year varchar(16),
in p_genre varchar(16),
in p_duration int,
in p_mediatype int,
out r_mediaId int) 
BEGIN
	DECLARE MediaId int;
    DECLARE AccountId int;
    DECLARE GenreId int;
    
    SELECT Account.Id INTO AccountId FROM Account WHERE (Account.User = p_user AND Account.pass = p_pass);
    
    IF AccountId IS NOT NULL THEN
		-- if not exist genre then create
        IF (NOT EXISTS (SELECT * FROM Genre WHERE Genre.Name = p_genre)) THEN 
			INSERT INTO GENRE(Name) VALUES (p_genre); 
        END IF; 
        
		SELECT Id INTO GenreId FROM Genre WHERE Name = p_genre;
		INSERT INTO Media(Title, Year, Duration, MediaType_Id, Genre_Id, Account_Id) VALUES (p_title, p_year, p_duration, p_mediatype, GenreId, AccountId);
        SELECT LAST_INSERT_ID() INTO r_mediaId;
	END IF;
END$$

DROP PROCEDURE IF EXISTS MakeReview;

DELIMITER $$
CREATE PROCEDURE MakeReview(
    in  p_user varchar(32), 
    in p_pass  varchar(32),
    in p_title varchar(32),  
    in p_text varchar(500),
    in p_media int)
BEGIN
    DECLARE AccountId int;
 
    SELECT Account.Id INTO AccountId FROM Account WHERE (Account.User = p_user AND Account.pass = p_pass);
 
    IF AccountId > 0 THEN
		INSERT INTO Review (Media_Id, Account_Id, Text, Title) VALUES (p_media, AccountId, p_title, p_text);
    END IF;
 
END$$

DROP PROCEDURE IF EXISTS Rate;

DELIMITER $$
-- CREATE DEFINER=`root`@`localhost` PROCEDURE `Rate`(mediaId integer, user varchar(32), pass varchar(32), rating integer)
 --    MODIFIES SQL DATA
 CREATE PROCEDURE Rate(mediaId integer, user varchar(32), pass varchar(32), rating integer)
BEGIN
	declare YES integer;

SELECT 
    Id
INTO YES FROM
    Account
WHERE
    user = Account.user
        AND pass = Account.pass;
		
            -- username & pwd == entry in Account
    if (YES is not null) then 
		insert into Rating (Media_Id, Account_Id, Rating) VALUES (MediaId, YES, rating);
	end if;

END$$