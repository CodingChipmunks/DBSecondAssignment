// Q separate data and collections
// Q make empty collections?

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
// TODO: add index

// Create Genre
db.createCollection("Genre")

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
//db.Media.insert								// USES SP !
// TODO: add index

// a way of adding index, needs more research
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
db.Media.ensureIndex( { userid: 1 } )
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

// researching how N:M tables are used in noSQL DB's
db.createCollection("Contributor")

// Create Rating, Username is unique so that every user may only create one rating.  
db.createCollection("Rating")

// Create Review, Username is unique so that every user may only create one review.
db.createCollection("Review")

// show what has been made
show collections


// rm users
db.dropAllUsers()

// Create a database user with read access for every normal user, the user does not need to supply
// Login credentials when reading from the database. Login information will be sent when required by the 
// Database, when creating a review or rating. 

// make user for clientapp
db.createUser(
    {
      user: "clientapp",
      pwd: "qwerty",
      roles: [
         { role: "read", db: "first" },
         { role: "readWrite", db: "healthy" }
      ]
    }
)


// list users
db.getUsers()



// TODO EQV. 2 stored procedures

// Clear Stored procedures
db.system.js.remove({})

// Make equivalent of stored procedures 
db.system.js.save(
   {
     _id : "Rate" ,
     value : function (x, y){ return x + y; }
   }
);

// call scripts
db.eval( "myAddFunction( 1, 2)" )

// vs.
db.loadServerScripts();
myAddFunction(3, 5);


// show what has been mde
//show collections
//show users
//db.getUsers()
//show roles
//show profile
//show dbs


