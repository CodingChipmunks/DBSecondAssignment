
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
// TODO: Make ref. to account ? if login is supposed to be available
//Create User, Contains password and username for use in the post-review/post-rating procedures.
db.createCollection("Account")
// These test accounts are used in the creation of new Media/Reviews/Ratings
db.Account.insert({Name : 'MrFoo', User : 'Foo'})
db.Account.insert({Name : 'MrsBar', User : 'Bar')

// Use this account to log in during development, it has no preset reviews/ratings.
db.Account.insert({Name : 'User42', User : 'User'})

// Create Movie/Album
db.Media.insert({ 
	Title : "Rosenrot", 
	Creator : "Rammstein",	// will become list []
	Genre : "Rammstyle",
	Year : "2009",
	Duration : "120",
	Review : [
		{
			User : "Bar", 
			Title : "Excellent!", 
			Review : "With deep flavor and class..." 
		},
		{
			User : "Foo", 
			Title : "Supberb.", 
			Review : "Very Good. Such Wack."
		}
	],
	Rating : [
		{User: "Foo", Rating : "1"}, 
		{User: "Bar", Rating : "5"}
	],	// calc. avg. in app, prevent wrong values!
	AddedBy : "Foo",
	Mediatype : "Album"
})
// Ref 2 who added it!

db.Media.ensureIndex( { Title: 1 } )
db.Media.ensureIndex( { Mediatype_Id: 1 } )


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


