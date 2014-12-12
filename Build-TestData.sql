-- Admin
-- Generate Test Data.
INSERT INTO Account (Name, User, Pass) VALUES ("MrFoo", "Foo", "Foo");
INSERT INTO Account (Name, User, Pass) VALUES ("MrsBar", "Bar", "Bar");
INSERT INTO Account (Name, User, Pass) VALUES ("Alice", "AUser", "APass");
INSERT INTO Account (Name, User, Pass) VALUES ("Bob", "BUser", "BPass"); 

-- Use this account to log in during development, it has no preset reviews/ratings.
INSERT INTO Account (Name, User, Pass) VALUES ("User42", "User", "Pass");

-- Adding some Albums to database.
CALL AddMedia("Foo", "Foo", "Rosenrot", "2009", "Rammstyle", 120, 1, @pkid);
CALL AddCreator("Foo", "Foo", "Rammstein", @pkid);
CALL MakeReview("Bar", "Bar", "Excellent!", "With deep flavor and class...", @pkid);
CALL MakeReview("Foo", "Foo", "Supberb.", "Very Good. Such Wack.", @pkid);
CALL MakeReview("AUser", "APass", "Naaa?", "Sorry, not feeling it..", @pkid);
CALL MakeReview("Buser", "BPass", "Alright", "Could have used more SQL..", @pkid);
CALL Rate(@pkid, "BUser", "BPass", 2);
CALL Rate(@pkid, "AUser", "APass", 5);

CALL AddMedia("Bar", "Bar", "Reise, Reise", "2006", "Rammstyle", 120, 1, @pkid);
CALL AddCreator("Foo", "Foo", "Rammstein", @pkid);
CALL MakeReview("Bar", "Bar", "Extraordinary!", "This album.. !!!", @pkid);
CALL MakeReview("Foo", "Foo", "Awesome.", "Can NOT have enough.", @pkid);
CALL MakeReview("AUser", "APass", "Eeeh", "i dunn get it.", @pkid);
CALL MakeReview("Buser", "BPass", "Okay", "Better than their last..", @pkid);
CALL Rate(@pkid, "BUser", "BPass", 4);
CALL Rate(@pkid, "AUser", "APass", 1);

CALL AddMedia("Bar", "Bar", "Imagine", "2006", "Lennstyle", 120, 1, @pkid);
CALL AddCreator("Foo", "Foo", "John Lennon", @pkid);
CALL MakeReview("Bar", "Bar", "Its good", "I liked this album.", @pkid);
CALL Rate(@pkid, "BUser", "BPass", 2);
CALL Rate(@pkid, "AUser", "APass", 3);


CALL AddMedia("Bar", "Bar", "Taylor Swift^2", "2012", "New Stuff", 120, 1, @pkid);
CALL AddCreator("Foo", "Foo", "Swift", @pkid);
CALL AddCreator("Foo", "Foo", "Go", @pkid);
CALL AddCreator("Foo", "Foo", "Erlang", @pkid);
CALL MakeReview("Bar", "Bar", "Dont Know", "Rammstein def better.", @pkid);
CALL MakeReview("Foo", "Foo", "Mediocre", "Nothing new here.", @pkid);
CALL MakeReview("AUser", "APass", "^2 ?", "What does it even mean??.", @pkid);
CALL Rate(@pkid, "BUser", "BPass", 2);
CALL Rate(@pkid, "AUser", "APass", 1);

CALL AddMedia("AUser", "APass", "Chipmunks", "2014", "Chipcore", 120, 1, @pkid);
CALL AddCreator("Foo", "Foo", "Alvin", @pkid);
CALL AddCreator("Foo", "Foo", "Simon", @pkid);
CALL AddCreator("Foo", "Foo", "Theodore", @pkid);

CALL AddMedia("BUser", "BPass", "Chipmunks: REMIXED", "2014", "Chipcore", 120, 1, @pkid);
CALL AddCreator("Foo", "Foo", "Lysette", @pkid);
CALL AddCreator("Foo", "Foo", "Aniela", @pkid);




