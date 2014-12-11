DROP PROCEDURE IF EXISTS MakeReview;

DELIMITER $$
CREATE PROCEDURE MakeReview(
    in  p_user varchar(32), 
    in p_pass  varchar(32),
    in p_title varchar(32),
    in p_text varchar(500),
    in p_media int)
BEGIN
    DECLARE AccountId int;
 
    SELECT Account.Id INTO AccountId FROM Account WHERE (Account.User = p_user AND Account.pass = p_pass);
 
    IF AccountId > 0 THEN
		INSERT INTO Review (Media_Id, Account_Id, Text, Title) VALUES (p_media, AccountId, p_title, p_text);
    END IF;
 
END$$

-- CALL MakeReview("Bar", "Bar", "Title", "ReviiiewssYYys", -50);