
// Admin
// Warning: Running This Script Will Change Root Password!
// Warning: Running This Script Will Reset Database.MediaCollection!
// Warning: All Users Has Execute Rights In This Schema!


// select db
use mediacollection
// clear it
db.dropDatabase()
// needed to do again after drop?
use mediacollection
// TODO: INSERT Admin into Acount!


// make collections

//Create User, Contains password and username for use in the post-review/post-rating procedures.
db.createCollection("Account")
// These test accounts are used in the creation of new Media/Reviews/Ratings
db.Account.insert({Name : 'MrFoo', User : 'Foo', Pass : 'Foo' })
db.Account.insert({Name : 'MrsBar', User : 'Bar', Pass : 'Bar' })
db.Account.insert({Name : 'Alice', User : 'AUser', Pass : 'APass' })
db.Account.insert({Name : 'Bob', User : 'BUser', Pass : 'BPass' })
// Use this account to log in during development, it has no preset reviews/ratings.
db.Account.insert({Name : 'User42', User : 'User', Pass : 'Password' })

// Create Director/Artist
db.createCollection("Creator")
db.Creator.insert({Name : "Rammstein"})

// Adding index
db.Creator.ensureIndex( { Name: 1 } )

// Create Genre
db.createCollection("Genre")
db.Genre.insert({Name : "Rammstyle"})
// TODO connect dem in good fashion

// Create Media Types
db.createCollection("Mediatype")
// TODO: add base types!!! is this it?
db.Mediatype.insert({Name : 'Album'})
db.Mediatype.insert({Name : 'Movie'})
db.Mediatype.insert({Name : 'E-Book'})
db.Mediatype.insert({Name : 'Undefined'})
// Genre defaults to undefined.

// Create Movie/Album
db.createCollection("Media")

//  needs more research
db.Media.insert({ 
	Title : 'Rosenrot', 
	Creator : "Rammstein", // FK
	Genre : "Rammstyle", 	// FK
	Year : "2009",
	Duration : "120",
	Mediatype : "1",	// FK
	AddedBy : ""
})
// Ref 2 who added it!

db.Media.ensureIndex( { Title: 1 } )
db.Media.ensureIndex( { Mediatype_Id: 1 } )


// researching how N:M tables are used in noSQL DB's
db.createCollection("Contributor")

// Create Rating, Username is unique so that every user may only create one rating.  
db.createCollection("Rating")
db.Rating.insert({User : "BUser", Rating : "2"})
db.Rating.insert({User : "AUser", Rating : "5"})

// Create Review, Username is unique so that every user may only create one review.
db.createCollection("Review")
db.Review.insert({
	User : "Bar", 
	Title : "Excellent!", 
	Review : "With deep flavor and class..."
})
db.Review.insert({
	User : "Foo", 
	Title : "Supberb.", 
	Review : "Very Good. Such Wack."
})
db.Review.insert({
	User : "AUser", 
	Title : "Naaa?", 
	Review : "Sorry, not feeling it.."
})
db.Review.insert({
	User : "BUser", 
	Title : "Alright", 
	Review : "Could have used more SQL.."
})


// show what has been made
//show collections


// rm users
db.dropAllUsers()

// Create a database user with read access for every normal user, the user does not need to supply
// Login credentials when reading from the database. Login information will be sent when required by the 
// Database, when creating a review or rating. 

// make user for clientapp
// TODO sync privlieges
db.createUser(
    {
      user: "clientapp",
      pwd: "qwerty",
      roles: [
         { role: "read", db: "mediacollection" },
         { role: "readWrite", db: "mediacollection" }
      ]
    }
)


// list users
db.getUsers()


// show what has been mde
//show collections
//show users
//db.getUsers()
//show roles
//show profile
//show dbs


