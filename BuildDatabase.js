// Q separate data and collections
// Q make empty collections?

// Admin
// Warning: Running This Script Will Change Root Password!
// Warning: Running This Script Will Reset Database.MediaCollection!
// Warning: All Users Has Execute Rights In This Schema!



// select db
use mediacollection
// Reset the schema.
// clear it
db.dropDatabase()

// needed to make same again??
use mediacollection

// TODO INSERT Admin into Acount!

// make collection
//db.Account
db.createCollection("Account")
// These test accounts are used in the creation of new Media/Reviews/Ratings
db.Account.insert({Name : 'MrFoo', User : 'Foo', Pass : 'Foo' })
db.Account.insert({Name : 'MrsBar', User : 'Bar', Pass : 'Bar' })
db.Account.insert({Name : 'Alice', User : 'AUser', Pass : 'APass' })
db.Account.insert({Name : 'Bob', User : 'BUser', Pass : 'BPass' })
// Use this account to log in during development, it has no preset reviews/ratings.
db.Account.insert({Name : 'User42', User : 'User', Pass : 'Password' })


db.createCollection("Creator")
// add index
db.createCollection("Genre")
db.createCollection("Mediatype")
// add base types!!! is this it?
db.Mediatype.insert({Name : 'Album'})
db.Mediatype.insert({Name : 'Movie'})
db.Mediatype.insert({Name : 'E-Book'})
db.Mediatype.insert({Name : 'Undefined'})
// Genre defaults to undefined.

db.createCollection("Media")
db.Media.insert								// USES SP !

// add index
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
db.Media.ensureIndex( { userid: 1 } )
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!


db.createCollection("Contributor")
db.createCollection("Rating")
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

//////////////////////////////////////////////////////////////////////////
// Clear Stored procedures
// REALLY NEEDED IF DB IS DROPPED ERY TIME?
db.system.js.remove({})
//db.system.js.remove({_id : "Rate" })

// Make stored procedures equivalent
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



// list contents of db
//db.fruit.find().pretty()

// show what has been made
//show collections
//show users
//db.getUsers()


//show roles
//show profile
//show dbs


