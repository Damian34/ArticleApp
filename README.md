Article REST Api
====================

Apliction is using framework:
Javalin to run rest api
postgresql DB, here is some data:

data are to connect to the PostgreSQL database and you can look in to them with pgAdmin 
(db is 20mb and is stand on https://api.elephantsql.com)
host name: dumbo.db.elephantsql.com
password:  PHm0jpvwUdwMtElhSwmHCA85Ucl9G6l8
username:  xvwxelvu

DROP table article;

CREATE TABLE article(
    id SERIAL PRIMARY KEY,
    contents varchar,
    publication_date date,
    name varchar(255),
    author varchar(255),
    recording_date timestamp default CURRENT_TIMESTAMP
); 

-- i know that here are not articles but books but i didnt have idea what to put^^

INSERT INTO article(contents,publication_date,name,author)VALUES('The Hobbit or There and Back Again','1937-09-21','Hobbit','J.R.R. Tolkiena');
INSERT INTO article(contents,publication_date,name,author)VALUES('Lord of the Rings. The Fellowship of the Ring - How It All Began?','1954-07-29','Lord of the Rings','J.R.R. Tolkiena');
INSERT INTO article(contents,publication_date,name,author)VALUES('Shrek! is a humorous fantasy picture book published in 1990 by American book writer and cartoonist William Steig, about ...','1990-10-17','Shrek','William Steig');
INSERT INTO article(contents,publication_date,name,author)VALUES('Pan Tadeusz, czyli ostatni zajazd na Litwie','1834-06-08','Pan Tadeusz','Adam Mickiewicz');
INSERT INTO article(contents,publication_date,name,author)VALUES('Lalka – powieść społeczno-obyczajowa Bolesława Prusa publikowana w odcinkach w latach 1887–1889 w dzienniku „Kurier Codzienny”','1883-09-29','Lalka','Bolesław Prus');

To run most endpoints

https://www.postman.com/

Endpoints:

http://localhost:7070/article

it is GET method and can be runned by browser
return json with all press articles sorted by publication date descending

http://localhost:7070/article/2

it is GET method and can be runned by browser
return json by id with single article, when id is out of range there will be shown message "Not Found!"

http://localhost:7070/article/keyWord

it is GETmethod and can be runned by browser
return json by keyWord with articles in which contents exist the keyWord

http://localhost:7070/add

it is PUT method and can't be runned by browser, it worth to use e.g Postman application to to use it
purpose of the method is to add new articles to Database, in order to do it in postman in body tab place e.g json like below
(using this method it is possible to add the same article multiple times cuz they will be saved under different ids
and if the Json format is incorrect or aren't filed with every fields then they wont be added to Json and will be shown message "Incorrect Format!")

{
"contents" : "Harry Potter is a series of seven fantasy novels written by British author J. K. Rowling. The novels chronicle the lives of a young wizard ...",
"publicationDate" : "1997-06-26",
"name" : "Harry Potter",
"author" : "J.K. Rowling"
}

http://localhost:7070/modify

it is POST method and can't be runned by browser(or can be but by JS), it worth to use e.g Postman application to to use it
purpose of the method is to modify existing articles in Database, in order to do it in postman in body tab place e.g json like below
(using this method it is possible to modify multiple fields in single article(the number of fields is arbitrary) ,but field "id" is necessary
and if the Json format is incorrect or if id doesn't exist then they won't be modificated and will be shown message "Incorrect Format!")

{
"id" : 8,
"contents" : "Harry Potter is a series of seven fantasy novels written by British author J. K. Rowling. The novels chronicle the lives of a young wizard ...",
"publicationDate" : "1997-06-26",
"name" : "Harry Potter",
"author" : "J.K. Rowling"
}

or

{
"id" : 8,
"name" : "Harry Potter2!",
"author" : "Rowling"
}

etc..

http://localhost:7070/delete/10

it is DELETE method and can't be runned by browser, it worth to use e.g Postman application to to use it
purpose of the method is to delete articles in Database, in order to do it use postman, there is not needed Json in request body
(if the given id does not exist then will be shown message "Not Found!" otherwise "Deleted!")


 