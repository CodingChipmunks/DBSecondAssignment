-- Admin
-- Generate Test Data.

INSERT INTO Creator (Name) VALUES ("John Lennon");
INSERT INTO Creator (Name) VALUES ("Rammstein");
INSERT INTO Creator (Name) VALUES ("Stanley Kubrick");
INSERT INTO Creator (Name) VALUES ("Cristopher Nolan");

INSERT INTO Genre (Name) VALUES ("Action");
INSERT INTO Genre (Name) VALUES ("Adventure");
INSERT INTO Genre (Name) VALUES ("Singer/Songwriter");
INSERT INTO Genre (Name) VALUES ("Education"); 

-- Authentication for uploading new content and submitting reviews, this is only visible to the
-- server or the admin creating a new account.
INSERT INTO Account (Name, Pass) VALUES ("Foo", "Foo");
INSERT INTO Account (Name, Pass) VALUES ("Bar", "Bar");
INSERT INTO Account (Name, Pass) VALUES ("FooBar", "FooBar");

-- These should be created when the account is created, accessible read from all.
INSERT INTO User (Account_Name) VALUES ("Foo");
INSERT INTO User (Account_Name) VALUES ("Bar");
INSERT INTO User (Account_Name) VALUES ("FooBar");

-- Album = 1, Video = 2, E-Book = 3, Adding media.
INSERT INTO Media (Mediatype_Id, Genre_Id, Title, Year, Duration) VALUES (1, 3, "Imagine", 1971, 43);
INSERT INTO Media (Mediatype_Id, Genre_Id, Title, Year, Duration) VALUES (1, 3, "Rosenrot", 2005, 58);
INSERT INTO Media (Mediatype_Id, Genre_Id, Title, Year, Duration) VALUES (1, 3, "Reise, Reise", 2004, 34);

INSERT INTO Media (Mediatype_Id, Genre_Id, Title, Year, Duration) VALUES (2, 3, "The Shining", 1996, 180);
INSERT INTO Media (Mediatype_Id, Genre_Id, Title, Year, Duration) VALUES (2, 3, "Full Metal Jacket", 1998, 120);
INSERT INTO Media (Mediatype_Id, Genre_Id, Title, Year, Duration) VALUES (2, 3, "Interstellar", 1992, 90);

INSERT INTO Media (Mediatype_Id, Genre_Id, Title, Year, Duration) VALUES (3, 4, "Databases", 2009, 681);
INSERT INTO Media (Mediatype_Id, Genre_Id, Title, Year, Duration) VALUES (3, 4, "Databases^2", 2011, 2975);

-- Adding media authors, Recommended to do this at least once for every media on insertion:
INSERT INTO Contributor(Creator_Id, Media_Id) VALUES (2,2); -- Rammstein = Rosenrot
INSERT INTO Contributor(Creator_Id, Media_Id) VALUES (2,3); -- Rammstein = Reise, Reise

-- Review added by [1] = Foo:Foo, Second parameter.
INSERT INTO Review (Media_Id, User_Id, Title, Text) VALUES (1, 1, "Rammstein??", "Ist Sehr Gut. Keine Fragen."); 

