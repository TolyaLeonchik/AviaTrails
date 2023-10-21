# AviaTrails

The service provides an opportunity to book and pay for air tickets, search flights by parameters and detailed information about flights.

## Database

The avia database in memory is connected to the project.  It contains 7 tables.
UserInfo - stores information about users. At the time of creation, it has 3 users.
Airports - contains information about airports. At the time of creation, it has 6 airports.
Airlines - contains information about airlines, including their identifiers, names,  cities and countries.
Flight - contains flight information including date and time of departure, place of departure, destination, flight duration and the airline operating the flight.
Payment history - contains information about the history of payments related to flight ticket bookings. It stores data on the payment amount, bank card number and the associated booking.
Ticket - contains information about tickets booked by users. It stores data about users, the number of tickets booked, their price and related flight information.
Markdown is a lightweight markup language based on the formatting conventions
that people naturally use in email.

Technologies and tools used to work with the database:
- DBMS: PostgreSQL
- ORM: Hibernate
- Migration management: Flyway

Database Structure:
[ProjectStructureDB.md](ProjectStructureDB.md)

Instructions for installing and configuring the database for the project:
1. Install and configure PostgreSQL
2. Create a new database named "avia"
3. Run the migrations using Flyway utility
4. import the initial data from the "V1_1_0__my_first_migration.sql" file

## Registration
To register, you must go to "http://localhost:8080/registration".
We must pass json format
 ```sh
 ( POST method: {"login": "exampleLogin", "password": "examplePassword"} )
 ```
to "http://localhost:8080/registration" after which the user will be created and placed in the database.

## Authentication
Get access to endpoints we can only pass authentication.
After passing the authentication, the user has the role USER, MODERATOR or ADMIN.
The database contains 3 users with different roles:
- ADMIN: (Login: admin, Password: password),
- MODERATOR: (Login: moderator, Password: password),
- USER:(Login: user, Password: password);
 ```sh
EXCEPTIONS(no authentication required):
"http://localhost:8080/registration",
"http://localhost:8080/authentication",
"http://localhost:8080/flights/{flightId}",
"http://localhost:8080/flights/search"
"http://localhost:8080/flights/allPage"
 ```
# Instruction for passing validation when creating/updating records in the database
The endpoints described below will provide examples where you need to enter JSON format data.
Please follow the rules to successfully create/update entries:
```sh
"firstName", "lastName", "airline", "portCityFrom", "portCityTo", "portNameFrom", "portNameTo", 
"returnAirline", "returnPortCityFrom", "returnPortCityTo", "returnPortNameFrom", "returnPortNameTo",
"portName", "portCity", "portCountry", "airlineName", "airlineCountry"- must have a capitalized name.

"countOfTickets", "returnCountOfTickets", "balance", "flightPrice", "numberOfFreeSeats", "airportId" - notNull

"departureTime" notEquals "arrivalTime" and "departureTime" must be before "arrivalTime"
"airportCode" - must be a string of 3 capital Latin letters.
"numberCard" - must have a string lenght of 12
"email" - should be email format: "test@test.com"
"phoneNumber" - must have a string length of 12-15 and consist of digits. Can start with '+'. 
```

# User capabilities
 ___
- no authentication required.

| Available endpoints                                                                                      | Description                                                   |
|----------------------------------------------------------------------------------------------------------|---------------------------------------------------------------|
| http://localhost:8080/flights/info/{flightId}                                                            | GET method: Get data about flight with a specific id.         |
| http://localhost:8080/flights/search?cityOfDeparture={cityFrom}&cityOfArrival={cityTo}&date={yyyy-MM-dd} | GET method: Get data about flight with a specific parameters. |
| http://localhost:8080/flights/allPage                                                                    | GET method: Get data about all flights.                       |
`Examples: `
- For GET method: "http://localhost:8080/flights/{flightId}":
 ```sh
"http://localhost:8080/flights/4"
 ```
- For GET method: "http://localhost:8080/flights/search?cityOfDeparture={cityFrom}&cityOfArrival={cityTo}&date={yyyy-MM-dd}":
 ```sh
"http://localhost:8080/flights/search?cityOfDeparture=Minsk&cityOfArrival=Amsterdam&date=2023-09-07"
 ```
___

- ROLE_USER - a user who's logged in.

| Available endpoints                                                                                      | Description                                                                |
|----------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------|
| http://localhost:8080/user                                                                               | GET method: Get data about user who sent the request.                      |
| http://localhost:8080/user                                                                               | PUT method: Update user.                                                   |
| http://localhost:8080/booking                                                                            | POST method: Create ticket.                                                |
| http://localhost:8080/booking/user                                                                       | GET method: Get data about all tickets of a user who sent the request.     |
| http://localhost:8080/booking/pay                                                                        | POST method: Payment for tickets booked by the user who sent the request.. |
| http://localhost:8080/booking/refund/{ticketId}                                                          | DELETE method: Refund of a ticket with a specific ID.                      |
| http://localhost:8080/flights/info/{flightId}                                                            | GET method: Get data about flight with a specific id.                      |
| http://localhost:8080/flights/search?cityOfDeparture={cityFrom}&cityOfArrival={cityTo}&date={yyyy-MM-dd} | GET method: Get data about flight with a specific parameters.              |
`Examples: `
- For POST method: "http://localhost:8080/booking", you need to pass json format:
 ```sh
{
        "firstName": "TestName",
        "lastName": "TestLastNamea",
        "airline": "NameAirline",
        "portCityFrom": "NamePortCityDeparture",
        "portCityTo": "NamePortCityArrival",
        "portNameFrom": "NamePortDeparture",
        "portNameTo": "NamePortArrival",
        "departureTime": "yyyy-MM-dd'T'HH:mm:ss",
        "countOfTickets": 2
        "returnTicket": true,
        "returnAirline": "NameAirline",
        "returnPortCityFrom": "NamePortCityDeparture",
        "returnPortCityTo": "NamePortCityArrival",
        "returnPortNameFrom": "NamePortDeparture",
        "returnPortNameTo": "NamePortArrival",
        "returnDepartureTime": "yyyy-MM-dd'T'HH:mm:ss",
        "returnCountOfTickets": 2
    }
 ```
- For POST method: "http://localhost:8080/booking/pay", you need to pass json format:
 ```sh
 "http://localhost:8080/booking/pay
{
        "firstName": "TestName",
        "lastName": "TestLastName",
        "numberCard": "0000000000000000",
        "balance": 2000
    }
 ```
- For DELETE method: "http://localhost:8080/booking/refund/{ticketId}":
 ```sh
"http://localhost:8080/booking/refund/14"
 ```
 ___

- ROLE_MODERATOR - grants extended rights over the basic ROLE_USER role.

| Available endpoints                                                                                      | Description                                                            |
|----------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------|
| http://localhost:8080/user                                                                               | GET method: Get data about user who sent the request.                  |
| http://localhost:8080/user/{userId}                                                                      | GET method: Get data about user with a specific id.                    |
| http://localhost:8080/user                                                                               | POST method: Сreate user.                                              |
| http://localhost:8080/user                                                                               | PUT method: Update user.                                               |
| http://localhost:8080/booking                                                                            | POST method: Сreate ticket.                                            |
| http://localhost:8080/booking/user                                                                       | GET method: Get data about all tickets of a user who sent the request. |
| http://localhost:8080/booking/user/{userId}                                                              | GET method: Get data about all tickets of a user with a specific id.   |
| http://localhost:8080/booking/pay                                                                        | POST method: Payment for tickets booked by the user.                   |
| http://localhost:8080/booking/refund/{ticketId}                                                          | DELETE method: Refund of a ticket with a specific ID.                  |
| http://localhost:8080/flights/info/{flightId}                                                            | GET method: Get data about flight with a specific id.                  |
| http://localhost:8080/flights/search?cityOfDeparture={cityFrom}&cityOfArrival={cityTo}&date={yyyy-MM-dd} | GET method: Get data about flight with a specific parameters.          |
`Examples: `
- For GET method: "http://localhost:8080/user/{userId}":
 ```sh
"http://localhost:8080/user/4"
 ```
- For GET method: "http://localhost:8080/booking/user/{userId}":
 ```sh
"http://localhost:8080/booking/user/4"
 ```

`IMPORTANT!!! `
  ```sh
Unlike ROLE_USER, ROLE_MODERATOR has access to all users, not just its own account.
 ```

 ___

-  ROLE_ADMIN - has the highest privilege level in the system.

ROLE_ADMIN has unlimited rights: add, edit, delete users, customize their roles and access differentiation and access to all information.

| Available endpoints                                                                                      | Description                                                                |
|----------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------|
| http://localhost:8080/user/all                                                                           | GET method: Get data about ALL users.                                      |
| http://localhost:8080/user                                                                               | GET method: Get data about user who sent the request.                      |
| http://localhost:8080/user/{userId}                                                                      | GET method: Get data about user with a specific id.                        |
| http://localhost:8080/user                                                                               | POST method: Create user.                                                  |
| http://localhost:8080/user                                                                               | PUT method: Update user.                                                   |
| http://localhost:8080/user/{userId}                                                                      | DELETE method: Deleting a user with a specific ID.                         |
| http://localhost:8080/booking/allTickets                                                                 | GET method: Get data about all users tickets.                              |
| http://localhost:8080/booking                                                                            | POST method: Create ticket.                                                |
| http://localhost:8080/booking/user                                                                       | GET method: Get data about all tickets of a user who sent the request.     |
| http://localhost:8080/booking/user/{userId}                                                              | GET method: Get data about all tickets of a user with a specific id.       |
| http://localhost:8080/booking/pay                                                                        | POST method: Payment for tickets booked by the user who sent the request.. |
| http://localhost:8080/booking/refund/{ticketId}                                                          | DELETE method: Refund of a ticket with a specific ID.                      |
| http://localhost:8080/flights                                                                            | GET method: Get data about all flights.                                    |
| http://localhost:8080/flights/{flightId}                                                                 | GET method: Get data about flightDTO with a specific id.                   |
| http://localhost:8080/flights/info/{flightId}                                                            | GET method: Get ALL data about flight with a specific id.                  |
| http://localhost:8080/flights/search?cityOfDeparture={cityFrom}&cityOfArrival={cityTo}&date={yyyy-MM-dd} | GET method: Get data about flight with a specific parameters.              |
| http://localhost:8080/flights                                                                            | POST method: Create flight.                                                |
| http://localhost:8080/flights/{flight}                                                                   | DELETE method: Deleting a flight with a specific ID.                       |
| http://localhost:8080/airport                                                                            | GET method: Get data about all airports.                                   |
| http://localhost:8080/airport/{airportId}                                                                | GET method: Get data about airport with a specific id.                     |
| http://localhost:8080/airport                                                                            | POST method: Create airport.                                               |
| http://localhost:8080/airport                                                                            | PUT method: Update airport.                                                |
| http://localhost:8080/airport/{airportId}                                                                | DELETE method: Deleting a airport with a specific ID.                      |
| http://localhost:8080/airline                                                                            | GET method: Get data about all airlines.                                   |
| http://localhost:8080/airline/{airlineId}                                                                | GET method: Get data about airline with a specific id.                     |
| http://localhost:8080/airline                                                                            | POST method: Create airline.                                               |
| http://localhost:8080/airline                                                                            | PUT method: Update airline.                                                |
| http://localhost:8080/airline/{airlineId}                                                                | DELETE method: Deleting a airline with a specific ID.                      |
`Examples:`
- For POST method: "http://localhost:8080/user", you need to pass json format:
 ```sh
{
        "firstName": "TestName",
        "lastName": "TestLastName",
        "email": "test@gmail.com",
        "phoneNumber": "+375297777777"
}
 ```
- For PUT method: "http://localhost:8080/user", you need to pass json format:
 ```sh
{
        "id": 11,
        "firstName": "Testing",
        "lastName": "Test",
        "email": "test@gmail.com",
        "phoneNumber": "+375297777777"
    }
 ```
- For DELETE method: "http://localhost:8080/user/{userId}":
 ```sh
"http://localhost:8080/user/14"
 ```
- For GET method: "http://localhost:8080/flights/{flightId}":
 ```sh
"http://localhost:8080/flights/14"
 ```
- For POST method: "http://localhost:8080/flights", you need to pass json format:
 ```sh
{
        "id": 11,
        "airlineId": 5,
        "fromAirportId": 16,
        "toAirportId": 18,
        "departureTime": "2023-10-21T13:30:00",
        "arrivalTime": "2023-10-21T15:50:00",
        "flightPrice": 400,
        "numberOfFreeSeats": 100
}
 ```
- For PUT method: "http://localhost:8080/flights", you need to pass json format:
 ```sh
{
        "airlineId": 6,
        "fromAirportId": 3,
        "toAirportId": 9,
        "departureTime": "2023-10-21T13:17:00",
        "arrivalTime": "2023-10-22T17:17:00",
        "flightPrice": 100,
        "numberOfFreeSeats": 23
    }
 ```
- For DELETE method: "http://localhost:8080/flights/{flightId}":
 ```sh
"http://localhost:8080/flights/14"
 ```
- For GET method: "http://localhost:8080/airport/{airportId}":
 ```sh
"http://localhost:8080/airport/14"
```
- For POST method: "http://localhost:8080/airport", you need to pass json format:
 ```sh
{
        "id": 4,
        "portName": "TestName",
        "portCity": "TestCity",
        "portCountry": "Country",
        "airportCode": "TTT"
}
 ```
- For PUT method: "http://localhost:8080/airport", you need to pass json format:
 ```sh
{
        "id": 4,
        "portName": "TestName2",
        "portCity": "TestCity2",
        "portCountry": "Country2",
        "airportCode": "AAA"
    }
 ```
- For DELETE method: "http://localhost:8080/airport/{airportId}":
 ```sh
"http://localhost:8080/airport/14"
 ```
- For GET method: "http://localhost:8080/airline/{airlineId}":
 ```sh
"http://localhost:8080/airline/14"
```
- For POST method: "http://localhost:8080/airline", you need to pass json format:
 ```sh
{
        "id": 6,
        "airlineName": "TestName",
        "airlineCountry": "TestCountry",
        "airportId": 4
} 
}
 ```
- For PUT method: "http://localhost:8080/airline", you need to pass json format:
 ```sh
{
        "id": 6,
        "airlineName": "TestName2",
        "airlineCountry": "TestCountry2",
        "airportId": 5
    }
 ```
- For DELETE method: "http://localhost:8080/airline/{airlineId}":
 ```sh
"http://localhost:8080/airline/14"
 ```