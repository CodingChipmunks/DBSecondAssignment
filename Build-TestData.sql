-- Admin
-- Generate Test Data.

INSERT INTO Creator (Name) VALUES ("John Lennon");
INSERT INTO Creator (Name) VALUES ("Rammstein");
INSERT INTO Creator (Name) VALUES ("Stanley Kubrick");
INSERT INTO Creator (Name) VALUES ("Cristopher Nolan");

INSERT INTO Genre (Name) VALUES ("Action");
INSERT INTO Genre (Name) VALUES ("Adventure");
INSERT INTO Genre (Name) VALUES ("Good Music");
INSERT INTO Genre (Name) VALUES ("Education"); 

-- Authentication for uploading new content and submitting reviews, this is only visible to the
-- server or the admin creating a new account.
INSERT INTO Account (Name, User, Pass) VALUES ("Foo", "Foo", "Foo");
INSERT INTO Account (Name, User, Pass) VALUES ("Bar", "Bar", "Bar");
INSERT INTO Account (Name, User, Pass) VALUES ("FooBar", "FooBar", "FooBar");

-- Album = 1, Video = 2, E-Book = 3, Adding media.
INSERT INTO Media (Mediatype_Id, Genre_Id, Title, Year, Duration) VALUES (1, 4, "Imagine", 1971, 43);
INSERT INTO Media (Mediatype_Id, Genre_Id, Title, Year, Duration) VALUES (1, 4, "Rosenrot", 2005, 58);
INSERT INTO Media (Mediatype_Id, Genre_Id, Title, Year, Duration) VALUES (1, 4, "Reise, Reise", 2004, 34);

INSERT INTO Media (Mediatype_Id, Genre_Id, Title, Year, Duration) VALUES (2, 2, "The Shining", 1996, 180);
INSERT INTO Media (Mediatype_Id, Genre_Id, Title, Year, Duration) VALUES (2, 2, "Full Metal Jacket", 1998, 120);
INSERT INTO Media (Mediatype_Id, Genre_Id, Title, Year, Duration) VALUES (2, 2, "Interstellar", 1992, 90);

INSERT INTO Media (Mediatype_Id, Genre_Id, Title, Year, Duration) VALUES (3, 5, "Databases", 2009, 681);
INSERT INTO Media (Mediatype_Id, Genre_Id, Title, Year, Duration) VALUES (3, 5, "Databases^2", 2011, 2975);

-- Adding media authors, Recommended to do this at least once for every media on insertion:
INSERT INTO Contributor(Creator_Id, Media_Id) VALUES (2,2); -- Rammstein = Rosenrot
INSERT INTO Contributor(Creator_Id, Media_Id) VALUES (2,3); -- Rammstein = Reise, Reise
INSERT INTO Contributor(Creator_Id, Media_Id) VALUES (2,3); -- Rammstein = Reise, Reise

INSERT INTO Contributor(Creator_Id, Media_Id) VALUES (1,1); -- 

INSERT INTO Contributor(Creator_Id, Media_Id) VALUES (2,3); -- Rammstein = Reise, Reise
INSERT INTO Contributor(Creator_Id, Media_Id) VALUES (2,3); -- Rammstein = Reise, Reise
INSERT INTO Contributor(Creator_Id, Media_Id) VALUES (2,3); -- Rammstein = Reise, Reise

INSERT INTO Contributor(Creator_Id, Media_Id) VALUES (2,3); -- Rammstein = Reise, Reise
INSERT INTO Contributor(Creator_Id, Media_Id) VALUES (2,3); -- Rammstein = Reise, Reise
INSERT INTO Contributor(Creator_Id, Media_Id) VALUES (2,3); -- Rammstein = Reise, Reise

-- Review added by [1] = Foo:Foo, Second parameter.
INSERT INTO Review (Media_Id, Account_Id, Title, Text) VALUES (1, 1, "Rammstein??", "Ist Sehr Gut. Keine Fragen."); 

