CREATE DEFINER=`root`@`localhost` PROCEDURE `Rate`(mediaId integer, user varchar(32), pass varchar(32), rating integer)
    MODIFIES SQL DATA
BEGIN
	declare YES integer;

SELECT 
    Id
INTO YES FROM
    Account
WHERE
    user = Account.user
        AND pass = Account.pass;
		
            -- username & pwd == entry in Account
    if (YES is not null) then 
		insert into Rating (Media_Id, Account_Id, Rating) VALUES (MediaId, YES, rating);
	end if;

END