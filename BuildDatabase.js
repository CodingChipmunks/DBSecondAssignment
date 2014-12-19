
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


db.createCollection("Media")
// Create Movie/Album
db.Media.insert({ 
	Title : "Rosenrot", 
	Creator : [
		{Name: "Rammstein"}
	],	
	Genre : "Rammstyle",
	Year : "2009",
	Review : [
		{
			User : "Bar", 
			Title : "Excellent!", 
			Text : "With deep flavor and class..." ,
			Mediatype : "Album"
		},
		{
			User : "Foo", 
			Title : "Supberb.", 
			Text : "Very Good. Such Wack.",
			Mediatype : "Album"
		}
	],
	Rating : [
		{User: "Foo", Score : "1"}, 
		{User: "Bar", Score : "5"}
	],	// calc. avg. in app, prevent entering wrong values and duplicated users!
	AddedBy : "Foo",
	Mediatype : "Album"
})


db.Media.ensureIndex( { Title: 1 } )
db.Media.ensureIndex( { Mediatype_Id: 1 } )

// Ref 2 who added it!

db.Media.insert({ 
	Title : "Reise, Reise", 
	Creator : [
		{Name: "Rammstein"}
	],
	Genre : "Rammstyle",
	Year : "2006",
	Review : [
		{
			User : "Bar", 
			Title : "Extraordinary!", 
			Text : "This album.. !!!",
			Mediatype : "Album"
		},
		{
			User : "Foo", 
			Title : "Awesome.", 
			Text : "Can NOT have enough.",
			Mediatype : "Album"
		}
	],
	Rating : [
		{User: "Foo", Score : "5"}, 
		{User: "Bar", Score : "5"}
	],
	AddedBy : "Bar",
	Mediatype : "Album"
})

db.Media.insert({ 
	Title : "Imagine", 
	Creator : [
		{Name: "John Lennon"}
	],
	Genre : "Lennstyle",
	Year : "2006",
	Review : [
		{
			User : "Bar", 
			Title : "Its good.", 
			Text : "I liked this album.",
			Mediatype : "Album"
		},
		{
			User : "Foo", 
			Title : "Eeeh.", 
			Text : "I dunn get it.",
			Mediatype : "Album"
		}
	],
	Rating : [
		{User: "Foo", Score : "3"}, 
		{User: "Bar", Score : "4"}
	],
	AddedBy : "Bar",
	Mediatype : "Album"
})

db.Media.insert({ 
	Title : "Chipmunks", 
	Creator : [
		{Name: "Alvin"}
	],
	Genre : "Chipcore",
	Year : "2014",
	Review : [

	],
	//Rating : [

	//],
	AddedBy : "Bar",
	Mediatype : "Album"
})

db.Media.insert({ 
	Title : "Lord ot. Rings", 
	Creator : [
		{Name: "Peter Jackson"}
	],
	Genre : "Balrogs",
	Year : "2006",
	Duration : "228",
	Review : [

	],
	//Rating : [

	//],
	AddedBy : "Bar",
	Mediatype : "Movie"
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


