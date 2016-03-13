# Openbox SF-5 settings editor, Spring + Backbone.js version #

Idea of this pet project derives from my old 1C:Enterprise 8.2 (<http://1c-dn.com/>) tool, released in 2010, for satellite television gadget, Openbox SF-5.
- <http://openbox.ua/instruments/sf5/>   - official page of the gadget;
- <http://infostart.ru/public/76804/>	 - page of the initial 1C:Enterprise 8.2 project.

Backend implementation has been taken from <https://github.com/detec/SF5Spring> with enhancements and RESTful service modified to comply with common Javascript frameworks practices.

## Features ##

Leaving behind satellite television details, Openbox SF-5 settings editor is a representation of a typical full-cycle CRUD application. It is able to:

- import catalogue data from structured text files (refreshed transponder data from resources like <http://ru.kingofsat.net>) into relational database;
- create and edit own entities (gadget settings) using catalogue data, store them in database and reuse when needed.

## User authentication ##

This implementation of Openbox SF-5 settings editor provides SQL-based user authentication (Spring Security 4 used) and registration, so that it can be run in a cloud. Each user can access only his/her own SF-5 settings. At the same time, a user has the right to update common catalogue with transponder data, without the need for the administrator to do this routine job. User administrator with credentials admin/1 is checked and, if necessary, created at every application startup. Form login authentication is used for accessing web pages and REST service.

## REST service ##

This Openbox SF-5 settings editor implementation provides RESTful API for getting entities from database with the help of Spring MVC 4. For most endpoints Jackson 2 is used, however, for SF-5 XML format, where exact XML structure is required, Spring's Jaxb2Marshaller is used. Here is the list of supported endpoints, relative to application context path:

- Satellites
	- jaxrs/satellites/ GET								- get all satellites;
	- jaxrs/satellites/filter/id/{satelliteId} GET 		- get satellite by its ID;
	- jaxrs/satellites/filter/{type}/{typeValue} GET 	- get satellites, filtered by arbitrary field name and field value.
	
- Transponders
	- jaxrs/transponders/filter/{type}/{typeValue} GET 	- get transponders, filtered by arbitrary field name and field value;
	- jaxrs/transponders/{transponderId} GET 			- get transponder by its ID;
	- jaxrs/transponders/filter;satId={satId} GET 		- get all transponders from specified satellite;
	- jaxrs/transponders/ GET 							- get all transponders;
	- jaxrs/transponders/ POST							- upload .ini file with transponders for further import. Content-type should be multipart/form-data.
	
- Users (most endpoints require form login authentication)
	- jaxrs/users/filter/username/{login} GET 			- get user by its login, for ADMIN role or user authenticated;
	- jaxrs/users/create POST 							- create new user, only for ADMIN role, new user ID is returned in "UserId" HTTP header; 
	- jaxrs/users/currentuser GET 						- get currently authenticated user;
	- jaxrs/users/exists/username/{login} GET 			- check if such username exists, boolean value returned.
	
- OpenBox SF-5 settings (require form login authentication)
	- jaxrs/usersettings/ POST								- post user setting to this endpoint to create a new setting, user authenticated and the one in setting should coincide;
	- jaxrs/usersettings/{settingId} PUT 					- update user setting, user authenticated and the one in setting should coincide;
	- jaxrs/usersettings/{settingId} DELETE 				- delete setting that belongs to authenticated user;
	- jaxrs/usersettings/ GET								- get all user's settings, based on credentials provided;
	- jaxrs/usersettings/filter/{type}/{typeValue} GET 		- get user's settings, filtered by arbitrary field name and field value, based on credentials provided;
	- jaxrs/usersettings/filter/id/{settingId} GET 			- get setting by its ID, based on credentials provided;
	- jaxrs/usersettings/filter/id/{settingId}/sf5 GET		- get setting by its ID, based on credentials provided, in Openbox SF-5 XML format; only "text/plain" "Accept" HTTP header is supported.

## System requirements ##

- configured non-XA datasource with JNDI name "java:jboss/datasources/MySQLDS"; MySQL and H2 supported;
- WildFly 10 application server;
- MySQL 5+ database server, running at the same host with WildFly (for profiles dev, prod and openshift);
- Java 8.

## Technologies ##

- Backbone.js;
- Spring 4 (Spring Core, Security, MVC, XML-based and Java configuration combined);
- Hibernate ORM 5.0.7;
- Hibernate Validator 5.2;
- Jackson 2.5;
- JDBC (for Spring Security only);
- Maven 3.3 with plugins compiler, surefire, resources, war, tomcat7, cargo, jaxws;
- WildFly 10;
- Java 8.

The project can be built either with Maven (3.3 or higher) or Eclipse (4.5 or higher).