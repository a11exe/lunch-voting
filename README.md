[![Build Status](https://travis-ci.org/a11exe/lunch-voting.svg?branch=master)](https://travis-ci.org/a11exe/lunch-voting)
[![codecov](https://codecov.io/gh/a11exe/lunch-voting/branch/master/graph/badge.svg)](https://codecov.io/gh/a11exe/lunch-voting)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/58745f4f85674746838a857fdd7e2014)](https://www.codacy.com/manual/a11exe/lunch-voting?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=a11exe/lunch-voting&amp;utm_campaign=Badge_Grade)

Design and implement a REST API using Hibernate/Spring/SpringMVC (or Spring-Boot) **without frontend**.

The task is:

Build a voting system for deciding where to have lunch.

 * 2 types of users: admin and regular users
 * Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
 * Menu changes each day (admins do the updates)
 * Users can vote on which restaurant they want to have lunch at
 * Only one vote counted per user
 * If user votes again the same day:
    - If it is before 11:00 we asume that he changed his mind.
    - If it is after 11:00 then it is too late, vote can't be changed

Each restaurant provides new menu each day.

As a result, provide a link to github repository.

It should contain the code and **README.md with API documentation and curl commands to get data for voting and vote.**

-----------------------------
P.S.: Make sure everything works with latest version that is on github :)

P.P.S.: Asume that your API will be used by a frontend developer to build frontend on top of that.

## Used technologies

+ Java 11
+ Maven
+ Spring Security
+ Spring 5
+ Spring Core (Beans, Context)
+ Spring Data Access (ORM, Spring Data JPA (Hibernate), Transactions)
+ DBs: H2
+ RESTful services
+ Spring Security Test / JUnit 5

## Install

    git clone https://github.com/a11exe/lunch-voting
    
## Run

    mvn clean package -DskipTests=true org.codehaus.cargo:cargo-maven2-plugin:1.7.5:run

## Credentionals
DB connection
+ JDBC URL: jdbc:h2:file:~/voting
+ User: sa, password: zD5z6Wx

REST auth User(role, email, password)
+ User, "user@yandex.ru", "password"
+ Admin, "admin@gmail.com", "admin"

## CURL Commands

### Users
**Access allowed for Admin only**

        curl -X GET 'http://localhost:8080/lunch-voting/rest/users' -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'
        curl -X GET 'http://localhost:8080/lunch-voting/rest/users/100001' -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'
        curl -X GET 'http://localhost:8080/lunch-voting/rest/users/find/by-email?=admin@gmail.com' -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'
        curl -X POST 'http://localhost:8080/lunch-voting/rest/users' -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' -H 'Content-Type: application/json' -d '{"name": "new-user","email": "new-email@new.com","password": "newPassword"}'
        curl -X DELETE 'http://localhost:8080/lunch-voting/rest/users/100001' -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'
        curl -X PUT 'http://localhost:8080/lunch-voting/rest/users/100002' -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' -H 'Content-Type: application/json' -d '{"id": 100002,"name": "userUpdate","email": "user@yandex.ru","password": "password","roles": ["ROLE_USER"]}'

### Restaurant
**Access allowed for Admin and User**
        
        curl -X GET 'http://localhost:8080/lunch-voting/rest/restaurants' -H 'Authorization: Basic dXNlcl9vbmVAeWFuZGV4LnJ1OnBhc3N3b3Jk'
        curl -X GET 'http://localhost:8080/lunch-voting/rest/restaurants/100004' -H 'Authorization: Basic dXNlcl9vbmVAeWFuZGV4LnJ1OnBhc3N3b3Jk'
**Access allowed for Admin only**

        curl -X POST 'http://localhost:8080/lunch-voting/rest/restaurants' -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' -H 'Content-Type: application/json' -d '{"name": "New restaurant","description": "New restaurant desc"}'
        curl -X DELETE 'http://localhost:8080/lunch-voting/rest/restaurants/100004' -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'
        curl -X PUT 'http://localhost:8080/lunch-voting/rest/restaurants/100005' -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' -H 'Content-Type: application/json' -d '{"name": "restaurant update","description": "New restaurant desc"}'
        
### Menu
**Access allowed for Admin and User**
        
        curl -X GET 'http://localhost:8080/lunch-voting/rest/menus' -H 'Authorization: Basic dXNlcl9vbmVAeWFuZGV4LnJ1OnBhc3N3b3Jk'
        curl -X GET 'http://localhost:8080/lunch-voting/rest/menus/100004' -H 'Authorization: Basic dXNlcl9vbmVAeWFuZGV4LnJ1OnBhc3N3b3Jk'
        curl -X GET 'http://localhost:8080/lunch-voting/rest/menus/find/by-date?date=2015-05-30' -H 'Authorization: Basic dXNlcl9vbmVAeWFuZGV4LnJ1OnBhc3N3b3Jk'
        curl -X GET 'http://localhost:8080/lunch-voting/rest/menus/find/by-period?startDate=2015-05-30&endDate=2015-09-30' -H 'Authorization: Basic dXNlcl9vbmVAeWFuZGV4LnJ1OnBhc3N3b3Jk'
        curl -X GET 'http://localhost:8080/lunch-voting/rest/menus/find/by-restaurant-id?id=100005' -H 'Authorization: Basic dXNlcl9vbmVAeWFuZGV4LnJ1OnBhc3N3b3Jk'
**Access allowed for Admin only**

        curl -X POST 'http://localhost:8080/lunch-voting/rest/menus' -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' -H 'Content-Type: application/json' -d '{"date":"2019-09-13","description":"new menu","restaurant":"http://localhost:8080/lunch-voting/rest/restaurants/100006"}'
        curl -X DELETE 'http://localhost:8080/lunch-voting/rest/menus/100004' -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'
        curl -X PUT 'http://localhost:8080/lunch-voting/rest/menus/100005' -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' -H 'Content-Type: application/json' -d '{"date":"2019-09-13","updated description":"updated menu","restaurant":"http://localhost:8080/lunch-voting/rest/restaurants/100006"}'

### Vote

**Access allowed for Admin and User**

id: menu id
        
        curl -X POST 'http://localhost:8080/lunch-voting/rest/votes' -H 'Authorization: Basic dXNlcl9vbmVAeWFuZGV4LnJ1OnBhc3N3b3Jk' -H 'Content-Type: application/json' -d '{"id":"100008"}'
        
voting results by restaurants 
        
        curl -X GET 'http://localhost:8080/lunch-voting/rest/votes/results/by-date?date=2015-05-30' -H 'Authorization: Basic dXNlcl9vbmVAeWFuZGV4LnJ1OnBhc3N3b3Jk'
        
**Access allowed for Admin only**

        curl -X GET 'http://localhost:8080/lunch-voting/rest/votes' -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'
        curl -X GET 'http://localhost:8080/lunch-voting/rest/votes/100012' -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'
        curl -X GET 'http://localhost:8080/lunch-voting/rest/votes/find/by-date?date=2015-05-30' -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'
        curl -X GET 'http://localhost:8080/lunch-voting/rest/votes/find/by-period?startDate=2015-05-30&endDate=2015-09-30' -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'        
