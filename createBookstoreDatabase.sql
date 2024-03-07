DROP DATABASE IF EXISTS bookstore_database;
CREATE DATABASE bookstore_database;
USE bookstore_database;

CREATE TABLE WebElementDataTbl (
    element_name VARCHAR(40) NOT NULL,
    identifier_type VARCHAR(20),
    element_data VARCHAR(120),
    PRIMARY KEY (element_name)
);

CREATE TABLE UserLoginTbl(
	username VARCHAR(40) NOT NULL,
    pswrd VARCHAR(40),
    valid_creds BOOL,
    userid VARCHAR(40)
);

CREATE TABLE BookDataTbl(
	isbn VARCHAR(20) NOT NULL,
    title VARCHAR(100),
    subtitle VARCHAR(100),
    author VARCHAR(100),
    publish_date VARCHAR(50),
    publisher VARCHAR(50),
    pages SMALLINT,
    descript VARCHAR(1000),
    website VARCHAR(150),
    PRIMARY KEY (isbn)
);

INSERT INTO WebElementDataTbl (element_name, identifier_type, element_data)
VALUES("login_username", "id", "userName"),
("login_password", "id", "password"),
("login_loginbtn", "id", "login"),
("profile_bookstorebtn", "id", "gotoStore"),
("bookstore_selecttitle", "id", "see-book-"),
("profile_booktitle", "id", "see-book-"),
("profile_deletebooksmodalbtn", "id", "closeSmallModal-ok"),
("profile_deleteonebookmodalbtn", "id", "closeSmallModal-ok"),
("book_addtocollectionbtn", "xpath", "//button[text()='Add To Your Collection']"),
("book_returntostorebtn", "xpath", "//button[text()='Back To Book Store']"),
("book_gotoprofile", "xpath", "//span[text()='Profile']"),
("bookstore_gotoprofile", "xpath", "//span[text()='Profile']"),
("profile_deleteallbooks", "xpath", "//button[text()='Delete All Books']"),
("profile_logoutbtn", "xpath", "//button[text()='Log out']"),
("profile_deleteonebook", "xpath", "//a[text()='BookTitle']/parent::span/parent::div/parent::div/following-sibling::div[3]/div/span"),
("bookstore_titlecol", "xpath", "//div/span/a[text()='BookTitle']"),
("bookstore_authorcolextension", "xpath", "/ancestor::div[@class='rt-td']/following-sibling::div[1]"),
("bookstore_pubcolextension", "xpath", "/ancestor::div[@class='rt-td']/following-sibling::div[2]"),
("book_isbntext", "xpath", "//div[@id='ISBN-wrapper']/div[2]/label"),
("book_titletext","xpath", "//div[@id='title-wrapper']/div[2]/label"),
("book_subtitletext", "xpath", "//div[@id='subtitle-wrapper']/div[2]/label"),
("book_authortext", "xpath", "//div[@id='author-wrapper']/div[2]/label"),
("book_publishertext", "xpath", "//div[@id='publisher-wrapper']/div[2]/label"),
("book_totalpagestext", "xpath", "//div[@id='pages-wrapper']/div[2]/label"),
("book_descriptiontext", "xpath", "//div[@id='description-wrapper']/div[2]/label"),
("book_websitetext", "xpath", "//div[@id='website-wrapper']/div[2]/label"),
("profile_titlecol", "xpath", "//div/span/a[text()='BookTitle']"),
("profile_authorcolextension", "xpath", "/ancestor::div[@class='rt-td']/following-sibling::div[1]"),
("profile_pubcolextension", "xpath", "/ancestor::div[@class='rt-td']/following-sibling::div[2]"),
("profile_showrowselector", "xpath", "//span[2]/select"),
("profile_selecttitle", "id", "see-book-");


INSERT INTO UserLoginTbl(username, pswrd, valid_creds, userid)
VALUES("TestName1", "TestPass#1", true, "a71754f4-74aa-4ec5-bfb2-721ba867bc11"),
("parallelTestName2", "parallelTestPass#2", true, "cc827423-9d07-4933-9631-659c9858d8d1"),
("TestName1", "TestFail#1", false, "N/A"),
("TestName2", "TestFail#2", false, "N/A");

INSERT INTO BookDataTbl(isbn, title, subtitle, author, 
publish_date, publisher, pages, 
descript, website)
VALUES("9781449325862", "Git Pocket Guide", "A Working Introduction", "Richard E. Silverman",
"2020-06-04T08:48:39.000Z","O'Reilly Media", 234, 
"This pocket guide is the perfect on-the-job companion to Git, the distributed version control system. It provides a compact, readable introduction to Git for new users, as well as a reference to common commands and procedures for those of you with Git exp", "http://chimera.labs.oreilly.com/books/1230000000561/index.html"
),
("9781449331818", "Learning JavaScript Design Patterns", "A JavaScript and jQuery Developer's Guide", "Addy Osmani",
"2020-06-04T09:11:40.000Z", "O'Reilly Media", 254,
"With Learning JavaScript Design Patterns, you'll learn how to write beautiful, structured, and maintainable JavaScript by applying classical and modern design patterns to the language. If you want to keep your code efficient, more manageable, and up-to-da", "http://www.addyosmani.com/resources/essentialjsdesignpatterns/book/"
),
("9781449337711", "Designing Evolvable Web APIs with ASP.NET", "Harnessing the Power of the Web", "Glenn Block et al.",
"2020-06-04T09:12:43.000Z", "O'Reilly Media", 238,
"Design and build Web APIs for a broad range of clients—including browsers and mobile devices—that can adapt to change over time. This practical, hands-on guide takes you through the theory and tools you need to build evolvable HTTP services with Microsoft", "http://chimera.labs.oreilly.com/books/1234000001708/index.html"
),
("9781449365035", "Speaking JavaScript", "An In-Depth Guide for Programmers", "Axel Rauschmayer",
"2014-02-01T00:00:00.000Z", "O'Reilly Media", 460,
"Like it or not, JavaScript is everywhere these days-from browser to server to mobile-and now you, too, need to learn the language or dive deeper than you have. This concise book guides you into and through JavaScript, written by a veteran programmer who o", "http://speakingjs.com/"
),
("9781491904244", "You Don't Know JS", "ES6 & Beyond", "Kyle Simpson",
"2015-12-27T00:00:00.000Z", "O'Reilly Media", 278,
"No matter how much experience you have with JavaScript, odds are you don’t fully understand the language. As part of the \\\"You Don’t Know JS\\\" series, this compact guide focuses on new features available in ECMAScript 6 (ES6), the latest version of the st",  "https://github.com/getify/You-Dont-Know-JS/tree/master/es6%20&%20beyond"
),
("9781491950296", "Programming JavaScript Applications", "Robust Web Architecture with Node, HTML5, and Modern JS Libraries", "Eric Elliott",
"2014-07-01T00:00:00.000Z", "O'Reilly Media", 254,
"Take advantage of JavaScript's power to build robust web-scale or enterprise applications that are easy to extend and maintain. By applying the design patterns outlined in this practical book, experienced JavaScript developers will learn how to write flex", "http://chimera.labs.oreilly.com/books/1234000000262/index.html"
),
("9781593275846","Eloquent JavaScript, Second Edition", "A Modern Introduction to Programming", "Marijn Haverbeke",
"2014-12-14T00:00:00.000Z", "No Starch Press", 472,
"JavaScript lies at the heart of almost every modern web application, from social apps to the newest browser-based games. Though simple for beginners to pick up and play with, JavaScript is a flexible, complex language that you can use to build full-scale ", "http://eloquentjavascript.net/"
),
("9781593277574", "Understanding ECMAScript 6", "The Definitive Guide for JavaScript Developers", "Nicholas C. Zakas",
"2016-09-03T00:00:00.000Z", "No Starch Press", 352,
"ECMAScript 6 represents the biggest update to the core of JavaScript in the history of the language. In Understanding ECMAScript 6, expert developer Nicholas C. Zakas provides a complete guide to the object types, syntax, and other exciting changes that E", "https://leanpub.com/understandinges6/read");