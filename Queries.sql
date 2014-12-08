-- Server Side

-- Create use 
CREATE USER 'clientapp'@'%'
IDENTIFIED BY 'qwerty';
GRANT SELECT ON MediaCollection.* TO 'clientapp'@'%';

-- Generate tables

-- Generate test data



-- Purge tables



-- Client side

-- Get all artists
SELECT * FROM Artist;

-- Get all albums
SELECT * FROM Album;



