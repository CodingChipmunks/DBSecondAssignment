-- Admin
-- Warning: Running This Script Will Reset Your Database.

-- Reset the schema.
DROP SCHEMA IF EXISTS MediaCollection;

-- Create DB
CREATE SCHEMA IF NOT EXISTS MediaCollection;
USE MediaCollection;

-- Generate tables

-- Create User, Contains password and username for use in the post-review/post-rating procedures.
CREATE TABLE IF NOT EXISTS Account(
		Name VARCHAR(24) NOT NULL, 
        Pass VARCHAR(24), 
	PRIMARY KEY (Name)
);

CREATE TABLE IF NOT EXISTS User( 
		Id INTEGER AUTO_INCREMENT PRIMARY KEY,
		Account_Name VARCHAR(24),
	FOREIGN KEY(Account_Name) REFERENCES Account(Name) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Account used when values are added to the database from "admin" (in Build-TestData)
INSERT INTO Account(Name, Pass) VALUES ('Admin', 'zuAZpOpvPasB4JD7ka7GW8DCXGiAN4');
INSERT INTO User(Account_Name) VALUES ('Admin');

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
        Year INTEGER, 
        Duration INTEGER, 
        User_Id INTEGER DEFAULT 1,
	PRIMARY KEY (Id), 
	FOREIGN KEY (Mediatype_Id) REFERENCES Mediatype(Id),
    FOREIGN KEY (User_Id) REFERENCES User(Id) ON DELETE SET NULL ON UPDATE CASCADE,
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
		User_Id INTEGER UNIQUE, 
		Rating INTEGER, 
	FOREIGN KEY (User_Id) REFERENCES User(Id) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (Media_Id) REFERENCES Media(Id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Create Review, Username is unique so that every user may only create one review.
CREATE TABLE IF NOT EXISTS Review(
		Media_Id INTEGER, 
        User_Id INTEGER UNIQUE, 
        Title VARCHAR(24) NOT NULL, 
		Text VARCHAR(500) NOT NULL, 
	FOREIGN KEY (User_Id) REFERENCES User(Id) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (Media_Id) REFERENCES Media(Id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Create a database user with read access for every normal user, the user does not need to supply
-- Login credentials when reading from the database. Login information will be sent when required by the 
-- Database, when creating a review or rating. 
CREATE USER 'clientapp'@'localhost' IDENTIFIED BY 'qwerty';
REVOKE ALL ON *.* FROM 'clientapp'@'%';					  -- Remove any default.
GRANT SELECT ON MediaCollection.* TO 'clientapp'@'%';	  -- Grant read on all tables.
REVOKE ALL ON MediaCollection.User FROM 'clientapp'@'%';  -- Removes select from User table, where passwords are stored.
-- The admin logs on with write access, enabling the creation of accounts in User table, or to add
-- Media.