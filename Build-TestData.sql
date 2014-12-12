-- Admin
-- Generate Test Data.

-- These test accounts are used in the creation of new Media/Reviews/Ratings
INSERT INTO Account (Name, User, Pass) VALUES ("MrFoo", "Foo", "Foo");
INSERT INTO Account (Name, User, Pass) VALUES ("MrsBar", "Bar", "Bar");
INSERT INTO Account (Name, User, Pass) VALUES ("Alice", "AUser", "APass");
INSERT INTO Account (Name, User, Pass) VALUES ("Bob", "BUser", "BPass"); 

-- Use this account to log in during development, it has no preset reviews/ratings.
INSERT INTO Account (Name, User, Pass) VALUES ("User42", "User", "Password");

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


-- Adding some movies
CALL AddMedia("Bar", "Bar", "The Fellowship", "2004", "Fantasy", 120, 2, @pkid);
CALL AddCreator("Foo", "Foo", "Peter Jackson", @pkid);
CALL MakeReview("Bar", "Bar", "One.. ", ".. To rule them all!", @pkid);
CALL MakeReview("Foo", "Foo", "Yes", "Will watch again.", @pkid);
CALL MakeReview("AUser", "APass", "Unsure", "Too many orcs..", @pkid);
CALL MakeReview("Buser", "BPass", "Alright", "Not enough elves..", @pkid);
CALL Rate(@pkid, "BUser", "BPass", 5);
CALL Rate(@pkid, "AUser", "APass", 5);

CALL AddMedia("AUser", "APass", "Two Towers", "2006", "Fantasy", 120, 2, @pkid);
CALL AddCreator("Foo", "Foo", "Peter Jackson", @pkid);
CALL MakeReview("Bar", "Bar", "No! Gollum!", "Go away gollum!!", @pkid);
CALL MakeReview("Foo", "Foo", "Doom.", "Frodo is doomed.", @pkid);
CALL MakeReview("AUser", "APass", "Ents?", "Moving trees? Silly.", @pkid);
CALL Rate(@pkid, "BUser", "BPass", 4);
CALL Rate(@pkid, "AUser", "APass", 5);

CALL AddMedia("Bar", "Bar", "Return of the King", "2008", "Fantasy", 120, 2, @pkid);
CALL AddCreator("Foo", "Foo", "Peter Jackson", @pkid);
CALL MakeReview("Bar", "Bar", "By Galadriel!", "This is the End!", @pkid);
CALL MakeReview("Foo", "Foo", "Yes", "Will watch again.", @pkid);
CALL MakeReview("AUser", "APass", "Unsure", "Too many orcs..", @pkid);
CALL MakeReview("Buser", "BPass", "Alright", "Not enough elves..", @pkid);
CALL Rate(@pkid, "BUser", "BPass", 5);
CALL Rate(@pkid, "AUser", "APass", 5);

CALL AddMedia("Foo", "Foo", "Hobbit", "2013", "Fantasy", 120, 2, @pkid);
CALL AddCreator("Foo", "Foo", "Peter Jackson", @pkid);
CALL MakeReview("Bar", "Bar", "Hobbits!", "Hobbits Everywhere!", @pkid);
CALL MakeReview("Foo", "Foo", "Elves!", "Elves Everywhere!", @pkid);
CALL Rate(@pkid, "BUser", "BPass", 3);
CALL Rate(@pkid, "AUser", "APass", 5);

CALL AddMedia("AUser", "APass", "Hobbit: The Desolation of Smaug", "2014", "Fantasy", 120, 2, @pkid);
CALL AddCreator("Foo", "Foo", "Peter Jackson", @pkid);
CALL MakeReview("Buser", "BPass", "Smaug", "He the dragon yo.", @pkid);
CALL Rate(@pkid, "BUser", "BPass", 3);
CALL Rate(@pkid, "AUser", "APass", 5);

CALL AddMedia("AUser", "APass", "Hobbit: Battle of the Five Armies", "2015", "Fantasy", 120, 2, @pkid);
CALL AddCreator("Foo", "Foo", "Peter Jackson", @pkid);
CALL MakeReview("Buser", "BPass", "Go Hobbit", "Yay Hobbits. Yay.", @pkid);
CALL Rate(@pkid, "BUser", "BPass", 4);
CALL Rate(@pkid, "AUser", "APass", 5);

-- Adding some books
CALL AddMedia("AUser", "APass", "Dragon Reborn", "1993", "Fantasy", 120, 3, @pkid);
CALL AddCreator("Foo", "Foo", "Robert Jordan", @pkid);
CALL MakeReview("Bar", "Bar", "Amazing!", "Featuring a compelling story..", @pkid);
CALL MakeReview("Foo", "Foo", "Really!?", "The Dragon Is Reborn??", @pkid);
CALL MakeReview("AUser", "APass", "Perfection", "Its in the pages..", @pkid);
CALL MakeReview("Buser", "BPass", "Need.", "Lost my copy. :(", @pkid);
CALL Rate(@pkid, "BUser", "BPass", 5);
CALL Rate(@pkid, "AUser", "APass", 5);

CALL AddMedia("AUser", "APass", "The Path of Light", "1997", "Fantasy", 120, 3, @pkid);
CALL AddCreator("Foo", "Foo", "Robert Jordan", @pkid);
CALL AddCreator("Foo", "Foo", "Brandon Sanderson", @pkid);
CALL MakeReview("Bar", "Bar", "What Now?", "Got stuck in the nether..", @pkid);
CALL MakeReview("Foo", "Foo", "The Light.. !?", ".. It burnsss!", @pkid);
CALL Rate(@pkid, "BUser", "BPass", 5);
CALL Rate(@pkid, "AUser", "APass", 5);

CALL AddMedia("BUser", "BPass", "Thief Althalus", "2000", "Fantasy", 120, 3, @pkid);
CALL AddCreator("Foo", "Foo", "David Eddings", @pkid);
CALL AddCreator("Foo", "Foo", "Leigh Eddings", @pkid);
CALL MakeReview("Foo", "Foo", "Superior.", "The best, very good.", @pkid);
CALL MakeReview("AUser", "APass", "TL;DR", "Sorry, too many pages in book!", @pkid);
CALL MakeReview("Buser", "BPass", "A Good Read", "Can Recommend.", @pkid);
CALL Rate(@pkid, "BUser", "BPass", 4);
CALL Rate(@pkid, "AUser", "APass", 5);




