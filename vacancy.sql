CREATE USER gorbunov@localhost identified BY '123456';
GRANT usage ON *.* TO gorbunov@localhost identified BY '123456';
CREATE DATABASE IF NOT EXISTS VacancyDB;
GRANT ALL privileges ON VacancyDB.* TO gorbunov@localhost;
use VacancyDB;
CREATE TABLE `Vacancy` (`id` int(11) unsigned NOT NULL AUTO_INCREMENT,`title` varchar(255) NOT NULL DEFAULT '',`salary` varchar(255) DEFAULT NULL,`city` varchar(255) NOT NULL DEFAULT '',`companyName` varchar(255) NOT NULL DEFAULT '',`siteName` varchar(255) NOT NULL DEFAULT '',`url` varchar(255) NOT NULL DEFAULT '',`urlLocalSiteCompany` varchar(255) NOT NULL DEFAULT '',PRIMARY KEY (`id`)) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
FLUSH PRIVILEGES;
