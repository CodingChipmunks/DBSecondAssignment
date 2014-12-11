SELECT Media.*, avg(Rating) FROM Media, Rating 
	WHERE Media.MediaType_Id = 1 
    AND Media.Id = Rating.Media_Id
	AND  (SELECT avg(Rating) FROM Rating WHERE Media.Id = Rating.Media_Id) > 0
    GROUP BY Media.Id;